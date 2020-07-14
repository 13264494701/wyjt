package com.jxf.web.ufang;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberFriendRelation;
import com.jxf.mem.entity.MemberLoanReport;
import com.jxf.mem.service.MemberFriendRelationService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.H5Utils;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.mms.msg.utils.VerifyCodeUtils;
import com.jxf.rc.entity.RcCaData;
import com.jxf.rc.entity.RcCaYysDetails;
import com.jxf.rc.service.RcCaDataService;
import com.jxf.rc.service.RcCaYysDetailsService;
import com.jxf.svc.annotation.AccessLimit;
import com.jxf.svc.cache.CacheConstant;
import com.jxf.svc.cache.CacheUtils;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.EncryptUtils;
import com.jxf.svc.utils.JSONUtil;
import com.jxf.svc.utils.SnowFlake;
import com.jxf.ufang.entity.UfangAnalysisBean;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.util.LoanAnalysisUtils;
import com.jxf.ufang.util.UfangUserUtils;

/**
 * 好友管理表Controller
 * @author wo
 * @version 2018-10-11
 */
@Controller("ufangFriendRelationController")
@RequestMapping(value = "${ufangPath}/friend")
public class UfangFriendRelationController extends UfangBaseController {

	@Autowired
	private MemberFriendRelationService friendRelationService;
	@Autowired
	private SendSmsMsgService sendSmsMsgService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private RcCaDataService rcCaDataService;
	@Autowired
	private RcCaYysDetailsService rcCaYysDetailsService;
	
	@ModelAttribute
	public MemberFriendRelation get(@RequestParam(required=false) Long id) {
		MemberFriendRelation entity = null;
		if (id!=null){
			entity = friendRelationService.get(id);
		}
		if (entity == null){
			entity = new MemberFriendRelation();
		}
		return entity;
	}

	@RequiresPermissions("ufang:friend:view")
	@RequestMapping(value = {"list", ""})
	public String list(MemberFriendRelation friendRelation, HttpServletRequest request, HttpServletResponse response, Model model) {
		UfangUser user = UfangUserUtils.getUser();
		if (UfangUser.BindStatus.binded.equals(user.getBindStatus())) {
			friendRelation.setMember(user.getMember());
			Page<MemberFriendRelation> page = friendRelationService.findPage(new Page<MemberFriendRelation>(request, response), friendRelation);
			model.addAttribute("page", page);
		}
		return "ufang/friend/friendRelationList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("ufang:friend:view")
	@RequestMapping(value = "add")
	public String add(MemberFriendRelation memberFriendRelation, Model model) {
		UfangUser ufangUser = UfangUserUtils.getUser();
		if (UfangUser.BindStatus.unbind.equals(ufangUser.getBindStatus())) {
			model.addAttribute("bindStatus","0");
		}
		model.addAttribute("memberFriendRelation", memberFriendRelation);
		return "ufang/friend/memberFriendRelationAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("ufang:friend:view")
	@RequestMapping(value = "query")
	public String query(MemberFriendRelation memberFriendRelation, Model model) {
		model.addAttribute("memberFriendRelation", memberFriendRelation);
		return "ufang/friend/memberFriendRelationQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("ufang:friend:view")
	@RequestMapping(value = "update")
	public String update(MemberFriendRelation memberFriendRelation, Model model) {
		model.addAttribute("memberFriendRelation", memberFriendRelation);
		return "ufang/friend/memberFriendRelationUpdate";
	}

	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("ufang:friend:edit")
	@RequestMapping(value = "save")
	public String save(MemberFriendRelation memberFriendRelation, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, memberFriendRelation)){
			return add(memberFriendRelation, model);
		}
		friendRelationService.save(memberFriendRelation);
		addMessage(redirectAttributes, "保存好友关系成功");
		return "redirect:"+ufangPath+"/friend/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("ufang:friend:edit")
	@RequestMapping(value = "delete")
	public String delete(MemberFriendRelation memberFriendRelation, RedirectAttributes redirectAttributes) {
		friendRelationService.delete(memberFriendRelation);
		addMessage(redirectAttributes, "删除好友关系成功");
		return "redirect:"+ufangPath+"/friend/?repage";
	}
	
	@AccessLimit(maxCount = 1, seconds = 2)
	@RequestMapping(value = "/sendSmsVerifyCode")
	@ResponseBody
	public String sendSmsVerifyCode(String phoneNo) {
		Member friend = memberService.findByUsername(phoneNo);
		//查询用户是否存在
		if (friend == null) {		
			return "该手机号未注册无忧借条账号";
		}
		Member member = UfangUserUtils.getUser().getMember();
		MemberFriendRelation friendRelation = friendRelationService.findByMemberIdAndFriendId(member.getId(), friend.getId());
		if (friendRelation != null) {
			return "对方已经是你的好友了";
		}
		//生成短信验证码
		String smsCode = VerifyCodeUtils.genSmsValidCode();
		//将短信验证码存入缓存中
		CacheUtils.put(CacheConstant.TEMP_CACHE, CacheConstant.TEMP_CACHE + phoneNo, smsCode);
		sendSmsMsgService.sendSms("ufangAddFriend", phoneNo, smsCode);
		
		return "success";
	}


	@RequestMapping(value = "/addFriend")
	public String register(String phoneNo, String smsCode, RedirectAttributes redirectAttributes) {

		String serverSmsCode = (String) CacheUtils.get(CacheConstant.TEMP_CACHE, CacheConstant.TEMP_CACHE + phoneNo);
		if (StringUtils.isNotBlank(serverSmsCode) && StringUtils.equals(serverSmsCode, smsCode)) {
			redirectAttributes.addFlashAttribute("msg", "添加好友成功");
			//建立好友关系

			Member friend = memberService.findByUsername(phoneNo);
			Member member = UfangUserUtils.getUser().getMember();


			MemberFriendRelation friendRelation = new MemberFriendRelation();
			friendRelation.setMember(member);
			friendRelation.setFriend(friend);
			friendRelation.setUnread(0);
			friendRelation.setFreeCaAuthStatus(MemberFriendRelation.FreeCaAuthStatus.unauthorized);
			friendRelation.setFreeCaAuthTime(null);
			friendRelation.setChargeCaStatus(MemberFriendRelation.ChargeCaStatus.unpay);
			friendRelation.setChargeCaTime(null);
			friendRelationService.save(friendRelation);

			friendRelation.setIsNewRecord(true);
			friendRelation.setMember(friend);
			friendRelation.setFriend(member);
			friendRelation.setUnread(0);
			friendRelation.setFreeCaAuthStatus(MemberFriendRelation.FreeCaAuthStatus.unauthorized);
			friendRelation.setFreeCaAuthTime(null);
			friendRelation.setChargeCaStatus(MemberFriendRelation.ChargeCaStatus.unpay);
			friendRelation.setChargeCaTime(null);
			friendRelationService.save(friendRelation);


		} else {
			redirectAttributes.addFlashAttribute("msg", "验证码错误，添加好友失败");
		}
		return "redirect:" + ufangPath + "/friend/add";
	}

	/**
	 * 	好友借贷分析
	 * @throws Exception 
	 */
	@RequiresPermissions("ufang:friend:view")
	@RequestMapping(value = "loanAnalysis")
	public String loanAnalysis(MemberFriendRelation memberFriendRelation, RedirectAttributes redirectAttributes,Model model) throws Exception {
		UfangAnalysisBean analysisBean = new UfangAnalysisBean();
		Member member = UfangUserUtils.getUser().getMember();
		Member friend = memberService.get(memberFriendRelation.getFriend());
		
		Boolean flag = friendRelationService.checkFriendRelation(member.getId(), friend.getId());
		if(flag == null||flag == false) {
			addMessage(redirectAttributes, "该用户不是你的好友,不能查看");
			return "redirect:" + ufangPath + "/friend/?repage";
		}
		Integer verifiedList = friend.getVerifiedList();
		if(VerifiedUtils.isVerified(verifiedList,1)&&VerifiedUtils.isVerified(verifiedList,2)) {
			String idNo = friend.getIdNo();
			LoanAnalysisUtils analysis = new LoanAnalysisUtils();
			JSONObject result = analysis.getResult(idNo);
			String header = result.getString("header");
			Map<String, Object> map = JSONUtil.toMap(header);
			String ret_code = (String) map.get("ret_code");
			if(StringUtils.equals("000000", ret_code)) {
				String body = result.getString("body");
				Object content = JSON.parse(body);
				analysisBean.setId(SnowFlake.getId());
				analysisBean.setContent(content);
				analysisBean.setCreateTime(new Date());
				analysisBean.setUserId(UfangUserUtils.getUser().getId());
				analysisBean.setMemberId(friend.getId());
				mongoTemplate.save(analysisBean);
				model.addAttribute("content",content);
				model.addAttribute("memberFriendRelation",memberFriendRelation);
			}else {
				addMessage(redirectAttributes, (String)map.get("ret_msg"));
				return "redirect:" + ufangPath + "/friend/?repage";
			}
			logger.debug("===========借贷分析返回RESULT："+result);
		}else {
			addMessage(redirectAttributes, "好友未进行实名认证,不能查看");
			return "redirect:" + ufangPath + "/friend/?repage";
		}
		return "ufang/friend/friendloanAnalysis";		
	}
	/**
	 * 好友信用档案
	 */
	@RequiresPermissions("ufang:friend:view")
	@RequestMapping(value = "loanReport")
	public String loanReport(MemberLoanReport memberLoanReport, Model model) {
		
		friendRelationService.getFriendCreditRecord(memberLoanReport);
		
		model.addAttribute("memberLoanReport", memberLoanReport);
		return "ufang/friend/friendLoanReport";
	}
	
	/**
	 * 公积金详情
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "accumulationDetails")
	public ModelAndView accumulationDetails(HttpServletRequest request, Model model) {

		String param = request.getParameter("param");
		Member member = memberService.get(Long.parseLong(param));
		ModelAndView mv2 = new ModelAndView("ufang/friend/accumulationFundDetails");
		mv2 = H5Utils.addPlatform(member, mv2);
		List<Map> listMapFromJson2 = new ArrayList<Map>();
		List<Map> listMapFromJson = new ArrayList<Map>();
		RcCaData caData = new RcCaData();
		caData.setPhoneNo(member.getUsername());
		caData.setType(RcCaData.Type.gongjijin);
		List<RcCaData> findList = rcCaDataService.findList(caData);
		if(findList.size()>0) {
			RcCaData rcCaData = findList.get(0);
			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap = JSONUtil.toMap(rcCaData.getContent());
			listMapFromJson2 = JSON.parseArray(userMap.get("bill_record").toString(), Map.class);
			for (Map<String, Object> obj : listMapFromJson2) {
				Map<String, Object> map = new HashMap<String, Object>();
				String time = "未知";
				if (StringUtils.isNotEmpty(obj.get("deal_time") + "") && obj.get("deal_time") != null) {
					String text = obj.get("deal_time").toString();
					String[] split = text.split("-");
					time = split[0] + split[1];
					// 类型
				}
				String desc = "未知";
				if (StringUtils.isNotEmpty(obj.get("desc") + "") && !obj.get("desc").equals("null")) {
					if (obj.get("desc").toString().contains("2")) {
						String[] split2 = obj.get("desc").toString().split("2", 6);
						desc = split2[0];
					} else {
						desc = obj.get("desc").toString();
					}
				}
				
				int outcom = 0;
				int yue = 0;
				int incom = 0;
				if (StringUtils.isNotEmpty(obj.get("outcome") + "") && !obj.get("outcome").equals("null")) {
					outcom = (int) (Integer.parseInt(obj.get("outcome").toString()) / 100);
				}
				if (StringUtils.isNotEmpty(obj.get("balance") + "") && !obj.get("balance").equals("null")) {
					yue = (int) (Integer.parseInt(obj.get("balance").toString()) / 100);
				}
				if (StringUtils.isNotEmpty(obj.get("income") + "") && !obj.get("income").equals("null")) {
					incom = (int) (Integer.parseInt(obj.get("income").toString()) / 100);
				}
				if (desc.length() > 4) {
					desc = desc.substring(0, 4) + "..";
					
				}
				map.put("outcom", outcom);
				map.put("incom", incom);
				map.put("yue", yue);
				map.put("desc", desc);
				map.put("time", time);
				listMapFromJson.add(map);
			}
		}
		model.addAttribute("listMapFromJson2", listMapFromJson);
		String memberToken = request.getHeader("x-memberToken");
		model.addAttribute("memberToken", memberToken);
		return mv2;
	}
	
	/**
	 * 社保详情
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "socialSecurityDetails")
	public ModelAndView socialSecurityDetails(HttpServletRequest request, Model model) {

		String param = request.getParameter("param");
		Member member = memberService.get(Long.parseLong(param));
		ModelAndView mv2 = new ModelAndView("ufang/friend/socialSecurityDetails");
		mv2 = H5Utils.addPlatform(member, mv2);
		List<Map> listMapFromJsonYaL = new ArrayList<Map>();
		List<Map> listMapFromJsonYiL = new ArrayList<Map>();
		List<Map> listMapFromJsonSiY = new ArrayList<Map>();
		List<Map> listMapFromJsonGoS = new ArrayList<Map>();
		List<Map> listMapFromJsonSeY = new ArrayList<Map>();
		List<Map> listMapFromJsonYaL1 = new ArrayList<Map>();
		List<Map> listMapFromJsonYiL1 = new ArrayList<Map>();
		List<Map> listMapFromJsonSiY1 = new ArrayList<Map>();
		List<Map> listMapFromJsonGoS1 = new ArrayList<Map>();
		List<Map> listMapFromJsonSeY1 = new ArrayList<Map>();
		RcCaData caData = new RcCaData();
		caData.setPhoneNo(member.getUsername());
		caData.setType(RcCaData.Type.shebao);
		List<RcCaData> findList = rcCaDataService.findList(caData);
		if(findList.size()>0) {
			RcCaData rcCaData = findList.get(0);
			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap = JSONUtil.toMap(rcCaData.getContent());
			String string3 = userMap.get("endowment_insurance").toString();
			listMapFromJsonYaL = JSON.parseArray(string3, Map.class);
			String string4 = userMap.get("medical_insurance").toString();
			listMapFromJsonYiL = JSON.parseArray(string4, Map.class);
			if (userMap.get("unemployment_insurance") != null) {
				String string5 = userMap.get("unemployment_insurance").toString();
				listMapFromJsonSiY = JSON.parseArray(string5, Map.class);
			}
			
			String string6 = userMap.get("accident_insurance").toString();
			listMapFromJsonGoS = JSON.parseArray(string6, Map.class);
			
			String string7 = userMap.get("maternity_insurance").toString();
			listMapFromJsonSeY = JSON.parseArray(string7, Map.class);
			for (Map<String, Object> obj : listMapFromJsonYaL) {
				Map<String, Object> map = new HashMap<String, Object>();
				int jishu = 0;
				int com = 0;
				int ge = 0;
				if (obj.get("base_number") != null) {
					jishu = (int) (Integer.parseInt(obj.get("base_number").toString()) / 100);
				}
				if (obj.get("monthly_company_income") != null) {
					com = (int) (Integer.parseInt(obj.get("monthly_company_income").toString()) / 100);
				}
				if (obj.get("monthly_personal_income") != null) {
					ge = (int) (Integer.parseInt(obj.get("monthly_personal_income").toString()) / 100);
				}
				map.put("jishu", jishu);
				map.put("ge1", ge);
				map.put("com", com);
				map.put("month", obj.get("month"));
				map.put("type", obj.get("type"));
				listMapFromJsonYaL1.add(map);
			}
			for (Map<String, Object> obj : listMapFromJsonYaL) {
				Map<String, Object> map = new HashMap<String, Object>();
				int jishu = 0;
				int com = 0;
				int ge = 0;
				if (obj.get("base_number") != null) {
					jishu = (int) (Integer.parseInt(obj.get("base_number").toString()) / 100);
				}
				if (obj.get("monthly_company_income") != null) {
					com = (int) (Integer.parseInt(obj.get("monthly_company_income").toString()) / 100);
				}
				if (obj.get("monthly_personal_income") != null) {
					ge = (int) (Integer.parseInt(obj.get("monthly_personal_income").toString()) / 100);
				}
				map.put("jishu", jishu);
				map.put("ge1", ge);
				map.put("com", com);
				map.put("month", obj.get("month"));
				map.put("type", obj.get("type"));
				listMapFromJsonYaL1.add(map);
			}
			for (Map<String, Object> obj : listMapFromJsonSiY) {
				Map<String, Object> map = new HashMap<String, Object>();
				int jishu = 0;
				int com = 0;
				int ge = 0;
				if (obj.get("base_number") != null) {
					jishu = (int) (Integer.parseInt(obj.get("base_number").toString()) / 100);
				}
				if (obj.get("monthly_company_income") != null) {
					com = (int) (Integer.parseInt(obj.get("monthly_company_income").toString()) / 100);
				}
				if (obj.get("monthly_personal_income") != null) {
					ge = (int) (Integer.parseInt(obj.get("monthly_personal_income").toString()) / 100);
				}
				map.put("jishu", jishu);
				map.put("ge1", ge);
				map.put("com", com);
				map.put("month", obj.get("month"));
				map.put("type", obj.get("type"));
				listMapFromJsonSiY1.add(map);
			}
			for (Map<String, Object> obj : listMapFromJsonGoS) {
				Map<String, Object> map = new HashMap<String, Object>();
				int jishu = 0;
				int com = 0;
				int ge = 0;
				if (obj.get("base_number") != null) {
					jishu = (int) (Integer.parseInt(obj.get("base_number").toString()) / 100);
				}
				if (obj.get("monthly_company_income") != null) {
					com = (int) (Integer.parseInt(obj.get("monthly_company_income").toString()) / 100);
				}
				if (obj.get("monthly_personal_income") != null) {
					ge = (int) (Integer.parseInt(obj.get("monthly_personal_income").toString()) / 100);
				}
				map.put("jishu", jishu);
				map.put("ge1", ge);
				map.put("com", com);
				map.put("month", obj.get("month"));
				map.put("type", obj.get("type"));
				listMapFromJsonGoS1.add(map);
			}
			for (Map<String, Object> obj : listMapFromJsonSeY) {
				Map<String, Object> map = new HashMap<String, Object>();
				int jishu = 0;
				int com = 0;
				int ge = 0;
				if (obj.get("base_number") != null) {
					jishu = (int) (Integer.parseInt(obj.get("base_number").toString()) / 100);
				}
				if (obj.get("monthly_company_income") != null) {
					com = (int) (Integer.parseInt(obj.get("monthly_company_income").toString()) / 100);
				}
				if (obj.get("monthly_personal_income") != null) {
					ge = (int) (Integer.parseInt(obj.get("monthly_personal_income").toString()) / 100);
				}
				map.put("jishu", jishu);
				map.put("ge1", ge);
				map.put("com", com);
				map.put("month", obj.get("month"));
				map.put("type", obj.get("type"));
				listMapFromJsonSeY1.add(map);
			}
		}
		model.addAttribute("listMapFromJsonYaL", listMapFromJsonYaL1);
		model.addAttribute("listMapFromJsonYiL", listMapFromJsonYiL1);
		model.addAttribute("listMapFromJsonSiY", listMapFromJsonSiY1);
		model.addAttribute("listMapFromJsonGoS", listMapFromJsonGoS1);
		model.addAttribute("listMapFromJsonSeY", listMapFromJsonSeY1);
		String memberToken = request.getHeader("x-memberToken");
		model.addAttribute("memberToken", memberToken);
		return mv2;
	}

	/**
	 * 话费记录详情
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "telephoneChargesDetails")
	public ModelAndView telephoneChargesDetails(HttpServletRequest request, Model model) {

		//Member member = memberService.get(247332211534204928L);
		String param = request.getParameter("param");
		Member member = memberService.get(Long.parseLong(param));
		ModelAndView mv = new ModelAndView(new MappingJackson2JsonView());
		ModelAndView mv2 = new ModelAndView("ufang/friend/telephoneChargesDetails");
		mv2 = H5Utils.addPlatform(member, mv2);
		List<Map> listMapFromJson2 = new ArrayList<Map>();
		List<Map> listMapFromJson = new ArrayList<Map>();
		RcCaYysDetails entity = new RcCaYysDetails();
		entity.setMemberId(member.getId());
		List<RcCaYysDetails> findList = rcCaYysDetailsService.findList(entity);
		if(findList.size()>0) {
			RcCaYysDetails rcCaData = findList.get(0);
			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap = JSONUtil.toMap(rcCaData.getContent());
			String string51 = userMap.get("bill_info").toString();
			listMapFromJson2 = JSON.parseArray(string51, Map.class);
			for (Map<String, Object> obj : listMapFromJson2) {
				Map<String, Object> map = new HashMap<String, Object>();
				String stri = obj.get("bill_cycle").toString();
				String money = obj.get("bill_fee").toString();
				int moneyNew = (int) (Integer.parseInt(money) / 100);
				map.put("stri", stri);
				map.put("moneyNew", moneyNew);
				listMapFromJson.add(map);
			}
		}
		model.addAttribute("listMapFromJson2", listMapFromJson);
		String memberToken = request.getHeader("x-memberToken");
		model.addAttribute("memberToken", memberToken);
		return mv2;
	}
	/**
	 * 通话记录详情
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "callRecordDetails")
	public ModelAndView callRecordDetails(HttpServletRequest request, Model model) {

		String memberId = request.getParameter("param");
		Member member = memberService.get(Long.parseLong(memberId));
		ModelAndView mv2 = new ModelAndView("ufang/friend/callRecordDetails");
		
        List<Map<String, String>> callList = new ArrayList<Map<String, String>>();
		RcCaYysDetails yysDetails = new RcCaYysDetails();
		yysDetails.setMemberId(member.getId());
		List<RcCaYysDetails> yysDetailsList = rcCaYysDetailsService.findList(yysDetails);
		if(yysDetailsList==null||yysDetailsList.size()==0) {
			return mv2;
		}
		RcCaYysDetails rcCaData = yysDetailsList.get(0);	
		JSONObject rcCaDataObj = JSON.parseObject(rcCaData.getContent());
		if(rcCaDataObj.containsKey("call_info")) {
			JSONArray callInfoList = rcCaDataObj.getJSONArray("call_info");  
			if(callInfoList!=null&&callInfoList.size()>0){
				 for(int i = 0 ;i<callInfoList.size();i++){  
					 JSONObject callInfo = callInfoList.getJSONObject(i);
					 if(callInfo.containsKey("call_record")){
						 JSONArray callRecordList = callInfo.getJSONArray("call_record"); 
						 if(callRecordList!=null&&callRecordList.size()>0) {
							 for(int j = 0 ;j<callRecordList.size();j++){  
								    JSONObject callRecord = callRecordList.getJSONObject(j);
									Map<String, String> callMap = new HashMap<String, String>();
									double count1 =(double)(Integer.parseInt(callRecord.get("call_time").toString()))/60; 
									int count=(int)Math.ceil(count1);
									String moblie=EncryptUtils.encryptString(callRecord.get("call_other_number").toString(),EncryptUtils.Type.PHONE);
									callMap.put("moblie", moblie);
									callMap.put("time", callRecord.getString("call_start_time"));
									callMap.put("count", String.valueOf(count));
									callMap.put("type", callRecord.getString("call_type_name"));
									callList.add(callMap);
								 
							 }
						 }
					 } 
				 }		
			}	
		}
		model.addAttribute("list", callList);
		return mv2;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}