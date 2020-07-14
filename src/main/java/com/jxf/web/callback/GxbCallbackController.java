package com.jxf.web.callback;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.rc.entity.RcCaData;
import com.jxf.rc.entity.RcGxb;
import com.jxf.rc.entity.RcGxb.GxbAuthType;
import com.jxf.rc.entity.RcGxb.ProdType;
import com.jxf.rc.entity.RcGxb.ReportStatus;
import com.jxf.rc.service.RcCaDataService;
import com.jxf.rc.service.RcCaDataServiceV2;
import com.jxf.rc.service.RcGxbService;
import com.jxf.rc.utils.ThirdPartyUtils;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.HttpUtils;
import com.jxf.svc.utils.JSONUtil;
import com.jxf.svc.utils.StringUtils;
import com.jxf.ufang.entity.UfangLoanMarketApplyer;
import com.jxf.ufang.entity.UfangLoanMarketApplyer.SesameStatus;
import com.jxf.ufang.service.UfangLoanMarketApplyerService;
import com.jxf.ufang.util.UfangUserUtils;

/**
 * @author Administrator
 */
@RestController
@RequestMapping(value = "/callback/gxb")
public class GxbCallbackController {
	private static final Logger log = LoggerFactory.getLogger(GxbCallbackController.class);

	@Autowired
	private RcCaDataService rcCaDataService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private UfangLoanMarketApplyerService ufangLoanMarketApplyerService;
    @Autowired
    private RcGxbService rcGxbService;
    @Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
    @Autowired
	private RcCaDataServiceV2 rcCaDataServiceV2;
    
    @RequestMapping(value = "/callBackNew")
	public String callBackNew(@RequestBody String request) {
		processTask(request);
		Map<String, String> data = new HashMap<String,String>();
		data.put("retCode", "1");
		data.put("retMsg", "成功");
		return JSON.toJSONString(data);
	}
    
	private void processTask(String request) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				JSONObject response = JSONObject.parseObject(request);
				String sequenceNo = response.getString("sequenceNo");// 外部系统用户授权唯一性标志，由接入方token授权时指定 898954878787878754
				String token = response.getString("token");// 爬虫系统一次授权唯一标识
				String authStatus = response.getString("authStatus");// 授权状态1/0
				String authJson = response.getString("authJson");// 授权结果内容JsonString
				String authItem = response.getString("authItem");// 授权项
				if (StringUtils.isNotBlank(authStatus) && StringUtils.equals(authStatus, "0")) {
					// 授权失败
					log.error("id:{}公信宝认证：{}授权失败！", sequenceNo, authItem);
				}
				// 授权成功的 保存token ,authJson是原始数据
				long ex = 10 * 1000L;
				String value = String.valueOf(System.currentTimeMillis() + ex);
				boolean lock = RedisUtils.lock(token, value);
				// 获取锁
				while (!lock) {
					try {
						Thread.sleep(300);
						value = String.valueOf(System.currentTimeMillis() + ex);
						lock = RedisUtils.lock(token, value);
					} catch (InterruptedException e) {
						log.error(Exceptions.getStackTraceAsString(e));
						break;
					}
				}
				// 分发任务
				distributeTaskData(authItem, token, authJson, sequenceNo);
				// 释放锁
				RedisUtils.unlock(token, value);
				// 如果是运营商就拉取运营商报告
				if (StringUtils.equals(authItem, "operator_plus")) {
					// 发请求获取报告
					String appId = Global.getConfig("gxb.appId");
					String appSecret = Global.getConfig("gxb.appSecret");
					Long timestamp = System.currentTimeMillis();
					String sign = DigestUtils.md5Hex(String.format("%s%s%s", appId, appSecret, timestamp));
					Map<String, String> querys = new HashMap<String, String>();
					querys.put("appId", appId);
					querys.put("sign", sign);
					querys.put("timestamp", String.valueOf(timestamp));
					Map<String, String> headers = new HashMap<String, String>();
					try {
						HttpResponse get = HttpUtils.doGet(ThirdPartyUtils.yunyingshangReportCrawlUrl + "/" + token,
								headers, querys);
						if (get != null) {
							HttpEntity resEntity = get.getEntity();
							if (resEntity != null) {
								String result = EntityUtils.toString(resEntity, "utf-8");
								JSONObject resJson = JSONObject.parseObject(result);
								String retCode = resJson.getString("retCode");
								int successCode = 1;
								if (Integer.valueOf(retCode) == successCode) {
									// 抓取成功
									JSONObject data = resJson.getJSONObject("data");
									JSONObject authResult = data.getJSONObject("authResult");
									RcGxb rcGxb = rcGxbService.findByToken(token);
									rcGxb.setReportData(authResult.toString());
									rcGxb.setReportStatus(ReportStatus.report_created);
									rcGxbService.saveTaskReport(rcGxb);
								} else {
									// 报告抓取失败
									log.error("公信宝运营商数据报告抓取失败！retCode:{},retMsg:{}", retCode,
											resJson.getString("retMsg"));
								}
							}
						}
					} catch (Exception e) {
						log.error("公信宝token:{}拉取运营商报告异常：{}", token, Exceptions.getStackTraceAsString(e));
					}
				}
			}
		});

	}
    
    private void distributeTaskData(String authItem,String token,String authJson,String sequenceNo) {
    	switch (authItem) {
    	case "sesame_multiple":
    		saveTaskData(GxbAuthType.sesame_multiple, token, authJson,authItem,sequenceNo);
    		break;
		case "chsi":
			//学信网
			saveTaskData(GxbAuthType.xuexinwang, token,authJson,authItem,sequenceNo);
			break;
		case "operator_plus":
			saveTaskData(GxbAuthType.yunyingshang, token,authJson,authItem,sequenceNo);
			break;
		case "ecommerce":
			//电商
			saveTaskData(GxbAuthType.taobao, token,authJson,authItem,sequenceNo);
			break;
		case "shebao":
			saveTaskData(GxbAuthType.shebao, token,authJson,authItem,sequenceNo);
			break;
		case "housefund":
			saveTaskData(GxbAuthType.gongjijin, token,authJson,authItem,sequenceNo);
			break;
		case "ebank":
			saveTaskData(GxbAuthType.wangyin, token,authJson,authItem,sequenceNo);
			break;
		default:
			log.error("不支持的公信宝认证类型：{}",authItem);
			break;
		}
    }
    
    private void saveTaskData(GxbAuthType authType,String token,String authJson,String authItem,String sequenceNo) {
    	RcGxb rcGxb = rcGxbService.findByToken(token);
    	if(rcGxb == null) {
    		Member member = memberService.get(Long.valueOf(sequenceNo));
    		rcGxb = new RcGxb();
    		rcGxb.setUserEmpNo(UfangUserUtils.getUser().getEmpNo());
    		rcGxb.setOrgId(member.getId());
    		rcGxb.setProdType(ProdType.app);
    		rcGxb.setAuthType(authType);
    		rcGxb.setName(member.getName());
    		rcGxb.setPhoneNo(member.getUsername());
	    	rcGxb.setOrgId(Long.valueOf(sequenceNo));
	    	rcGxb.setToken(token);
    	}
    	rcGxb.setTaskData(authJson);
    	rcGxb.setDataStatus(RcGxb.DataStatus.data_created);
    	if(authType.equals(GxbAuthType.yunyingshang)) {
    		JSONObject authResult = JSONObject.parseObject(authJson);
    		String channelSrc = authResult.getString("carrier");//CHINA_UNICOM
    		String province = authResult.getString("province");
    		String city = authResult.getString("city");
    		String authPhoneNo = authResult.getString("mobile");
    		String channelAttr = province + "-" + city==null?"":city;
    		rcGxb.setChannelCode(authItem);
    		rcGxb.setChannelSrc(channelSrc);
    		rcGxb.setChannelAttr(channelAttr);
    		rcGxb.setAuthPhoneNo(authPhoneNo);
    	}
    	rcGxbService.saveTaskData(rcGxb);
    }
    
	@PostMapping(value = "")
	public Map<String, Object> callback(@RequestBody String request) {
		try {
			Map<String, String> req = JSON.parseObject(request, new TypeReference<Map<String, String>>() {});
			String authJson = req.get("authJson");
			String token = req.get("token");
			String authItem = req.get("authItem");
			String sequenceNo = req.get("sequenceNo");
			RcGxb rcGxb = new RcGxb();
			if(StringUtils.equals(authItem, "sesame_multiple")){//芝麻分
				rcGxb = rcGxbService.get(Long.parseLong(sequenceNo));
			}else{
				Criteria criteria = Criteria.where("_id").is(Long.parseLong(sequenceNo));
				Query query = BasicQuery.query(criteria);  
				rcGxb =  mongoTemplate.findOne(query , RcGxb.class);
			}
				
			@SuppressWarnings("unchecked")
			Map<String,Object> parse = (Map<String, Object>) JSON.parse(authJson);
			String appType = (String) parse.get("appType");//平台
			if(StringUtils.equals("jdb", appType)){
				rcGxb.setSubItem(RcGxb.SubItem.jdb);
			}else if(StringUtils.equals("hhd", appType)){
				rcGxb.setSubItem(RcGxb.SubItem.hhd);
			}else if(StringUtils.equals("jjd", appType)){
				rcGxb.setSubItem(RcGxb.SubItem.jjd);
			}
			rcGxb.setToken(token);
			rcGxb.setAuthResult(authJson);
			if(StringUtils.equals(authItem, "sesame_multiple")){
				rcGxbService.save(rcGxb);
			}else{
				mongoTemplate.save(rcGxb);
			}
			
			Map<String, Object> success = new HashMap<>(16);
			success.put("retCode", 1);
			success.put("retMsg", "成功");
			return success;
		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>(16);
			error.put("retCode", 2);
			error.put("retMsg", "失败原因:" + e.getMessage());
			log.error(Exceptions.getStackTraceAsString(e));
			return error;
		}
	}

	@PostMapping(value = "/callbackAPP")
	public String callbackAPP(@RequestBody String request) {
		Map<String, String> req = JSON.parseObject(request, new TypeReference<Map<String, String>>() {
		});
		String phone = req.get("phone");
		String authJson = req.get("authJson");
		String sequenceNo = req.get("sequenceNo");
		Map<String, Object> authJsonjosn = JSONUtil.toMap(authJson);
		String zmfen = authJsonjosn.get("sesameScore").toString();
		String status = authJsonjosn.get("status").toString();
		Member member = memberService.findByUsername(phone);
		
		if(StringUtils.equals(status, "1")) {
			int verifiedList = member.getVerifiedList();
			verifiedList = VerifiedUtils.addVerified(verifiedList, 4);
			member.setVerifiedList(verifiedList);	
			memberService.save(member);
			RedisUtils.put("memberInfo"+member.getId(), "zhimafenStatus", "1");		
		}
		
		Map<String, Object> map = new HashMap<>(16);
		if (!sequenceNo.equals(member.getId() + "")) {
			map.put("retCode", 0);
			map.put("retMsg", "失败");
			String josn = JSONUtil.toJson(map);
			return josn;
		}

		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("zmFen", zmfen);
		map1.put("status", status);
		String content = JSONUtil.toJson(map1);
		RcCaData rcCaData = new RcCaData();
		rcCaData.setPhoneNo(member.getUsername());
		rcCaData.setIdNo(member.getIdNo());
		rcCaData.setName(member.getName());
		rcCaData.setContent(content);
		rcCaData.setType(RcCaData.Type.zhimafen);
		rcCaData.setProvider(RcCaData.Provider.gxb);
		rcCaDataService.save(rcCaData);
		map.put("retCode", 1);
		map.put("retMsg", "成功");
		String josn = JSONUtil.toJson(map);
		return josn;
	}

	@RequestMapping(value = "/returnUrl")
	public ModelAndView returnUrl(HttpServletRequest request, Model model) {
//		ModelAndView mv = new ModelAndView(new MappingJackson2JsonView());
		ModelAndView mv2 = new ModelAndView("app/creditArchives/getMofangShuJu");
		model.addAttribute("code",0 );
		model.addAttribute("message","提交成功");
		return mv2;
	}
	
	/**
	 * @description 优放贷芝麻分认证回调
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/callBackForUfangDebtZmfAuth")
	public String callBackForUfangDebtZmfAuth(@RequestBody String request) {
		Map<String, String> req = JSON.parseObject(request, new TypeReference<Map<String, String>>() {});
		String authJson = req.get("authJson");
		String sequenceNo = req.get("sequenceNo");
		log.info("========收到优放贷申请人ID{}芝麻分认证回调=========",sequenceNo);
		Map<String, Object> authJsonjosn = JSONUtil.toMap(authJson);
		String zmfen = authJsonjosn.get("sesameScore").toString();
		String status = authJsonjosn.get("status").toString();
		UfangLoanMarketApplyer applyer = ufangLoanMarketApplyerService.get(Long.valueOf(sequenceNo));
		
		if(StringUtils.equals(status, "0")) {
			//token和用户输入手机号不是同一个人 认证失败
			log.error("贷超申请人{}认证芝麻分输入手机号与本人手机号不一致,芝麻分认证失败!",applyer.getId());
			applyer.setSesameStatus(SesameStatus.unauth);
			applyer.setRmk(applyer.getRmk() == null ?"":applyer.getRmk() + "#用户认证芝麻分输入手机号与本人手机号不一致,芝麻分认证失败!");
		}else {
			applyer.setSesameStatus(SesameStatus.authed);
			applyer.setSesameScore(Integer.valueOf(zmfen));
		}
		ufangLoanMarketApplyerService.save(applyer);
		
		RcCaData rcCaData = new RcCaData();
		rcCaData.setPhoneNo(applyer.getPhoneNo());
		rcCaData.setIdNo(applyer.getIdNo());
		rcCaData.setName(applyer.getName());
		rcCaData.setContent(authJson);
		rcCaData.setType(RcCaData.Type.zhimafen);
		rcCaData.setProvider(RcCaData.Provider.gxb);
		rcCaDataService.save(rcCaData);
		
		Map<String, Object> map = new HashMap<>(16);
		map.put("retCode", 1);
		map.put("retMsg", "成功");
		String respJson = JSONUtil.toJson(map);
		return respJson;
	}
	
	/**
	 * @description 优放贷芝麻分认证结束跳转页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/returnUfangDebtPage")
	public ModelAndView returnUfangDebtPage(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("ufangDebt/perfectInfo");
		String uid = request.getParameter("uid");
		String success = request.getParameter("success");
		UfangLoanMarketApplyer applyer = ufangLoanMarketApplyerService.get(Long.valueOf(uid));
		if(StringUtils.equals(success, "1")) {
			applyer.setSesameStatus(SesameStatus.authed);
		}else {
			applyer.setSesameStatus(SesameStatus.unauth);
		}
		mv.addObject("applyer", applyer);
		return mv;
	}
}