package com.jxf.web.callback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.rc.entity.RcTianji;
import com.jxf.rc.entity.RcTianji.ChannelType;
import com.jxf.rc.entity.RcTianji.DataStatus;
import com.jxf.rc.entity.TianjiConstant;
import com.jxf.rc.service.RcTianjiService;
import com.jxf.rc.utils.TianJiHttpUtils;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.StringUtils;
import com.jxf.ufang.entity.UfangLoanMarketApplyer;
import com.jxf.ufang.entity.UfangLoanMarketApplyer.OperatorStatus;
import com.jxf.ufang.service.UfangLoanMarketApplyerService;
import com.jxf.ufang.util.UfangUserUtils;
import com.jxf.web.model.ResponseData;


/**
 * @author Administrator
 */
@RestController
@RequestMapping(value = "/callback/tianji")
public class TianjiCallbackController {
	
	private static Logger log = LoggerFactory.getLogger(TianjiCallbackController.class);

	@Autowired
	private RcTianjiService rcTianjiService;
	@Autowired
	private UfangLoanMarketApplyerService ufangLoanMarketApplyerService;
	@Autowired
	private MemberService memberService;

	 @RequestMapping(value = "/callbackForH5", consumes = "multipart/form-data", method = RequestMethod.POST)
	public ResponseData callbackForH5(HttpServletRequest request) {
		 log.info("收到天机H5回调");
		String userId = request.getParameter("userId");//用户的ID 会员：memberId,优放贷申请人：applyerId,优放：
		String outUniqueId = request.getParameter("outUniqueId");//调用方生成的会话唯一标识id rctianji表的id
		String state = request.getParameter("state");//当前状态
		String account = request.getParameter("account");//查询的账户名
		String search_id = request.getParameter("search_id");//查询ID
		String accountType = request.getParameter("accountType");//抓取类型
		if (log.isDebugEnabled()){
			log.debug("天机回调成功");
			log.debug("userId={}",userId);
			log.debug("state={}",state);
			log.debug("search_id={}",search_id);
			log.debug("accountType={}",accountType);
		}
		if(StringUtils.isNotBlank(state) && StringUtils.equals(state, "report")) {
			//抓取成功
			long ex = 10 * 1000L;
	        String value = String.valueOf(System.currentTimeMillis() + ex);
	        boolean lock = RedisUtils.lock(outUniqueId, value);
	        //获取锁
	        while(!lock) {
	        	try {
					Thread.sleep(300);
					value = String.valueOf(System.currentTimeMillis() + ex);
					lock = RedisUtils.lock(outUniqueId, value);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
	        }   
        	distributeTaskData(userId, outUniqueId, search_id, accountType);
	        //释放锁
	        RedisUtils.unlock(outUniqueId, value);
			return ResponseData.success("任务成功");
		}
		if(StringUtils.isNotBlank(state) && StringUtils.equals(state, "crawl_fail")) {
			//抓取失败
			String errorReasonDetail = request.getParameter("errorReasonDetail");
			log.error("天机回调：userId:{}返回state:{},accountType:{}数据抓取失败：{}",userId,state,accountType,errorReasonDetail);
			return ResponseData.error("认证失败");
		}
		if(StringUtils.equals(state, "report") && StringUtils.endsWith(accountType, "mobile")) {
			//生成报告成功
			 long ex = 10 * 1000L;
		        String value = String.valueOf(System.currentTimeMillis() + ex);
		        boolean lock = RedisUtils.lock(outUniqueId, value);
		        //获取锁
		        while(!lock) {
		        	try {
						Thread.sleep(300);
						value = String.valueOf(System.currentTimeMillis() + ex);
						lock = RedisUtils.lock(outUniqueId, value);
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
		        } 
		        distributeReportData(userId, outUniqueId, account, search_id, accountType);
			    //释放锁
			    RedisUtils.unlock(outUniqueId, value);
		 
				return ResponseData.success("报告生成成功");
		}
		if(StringUtils.isNotBlank(state) && StringUtils.equals(state, "report_fail")) {
			//生成报告失败
			log.error("天机回调：userId:{}返回state:{},accountType:{}认证失败",userId,state,accountType);
			return ResponseData.error("生成报告失败");
		}
		return ResponseData.success("操作成功");
	}
	
	 @RequestMapping(value = "/callbackForSdk", consumes = "multipart/form-data", method = RequestMethod.POST)
		public ResponseData callbackForSdk(HttpServletRequest request) {
		 	log.info("收到天机SDK认证回调");
		 	//session和userId相当于taskId notice_attached_param是前端认证时传的参数
			String userId = request.getParameter("user_id");//当module为微粒贷wld，芝麻分zmf，美团商户mt_busi时，user_id为此次请求的唯一标示
			String session = request.getParameter("session");//当module为运营商mobile，淘宝taobao，支付宝alipay，邮箱ec时，session为此次请求的唯一标示
			String status = request.getParameter("status");//抓取状态：1=抓取中，2=抓取完成，3=抓取失败，4=抓取部分成功
			String module = request.getParameter("module");//抓取模块：运营商mobile，淘宝taobao，支付宝alipay，邮箱ec，微粒贷wld，芝麻分zmf，美团商户mt_busi
			String notice_attached_param = request.getParameter("notice_attached_param");//
			if (log.isDebugEnabled()){
				log.debug("天机sdk回调成功");
				log.debug("session={}",session);
				log.debug("userId={}",userId);
				log.debug("status={}",status);
				log.debug("module={}",module);
				log.debug("notice_attached_param={}",notice_attached_param);
			}
			String taskId = "";
			String search_id = "";
			if(module.equals("wld")||module.equals("zmf")||module.equals("mt_busi")) {
				taskId = userId;
				search_id = userId;
			}else {
				taskId = session;
				search_id = session;
			}
			if(StringUtils.isNotBlank(status) && StringUtils.equals(status, "2")) {
				//抓取成功
				long ex = 10 * 1000L;
		        String value = String.valueOf(System.currentTimeMillis() + ex);
		        boolean lock = RedisUtils.lock(taskId, value);
		        //获取锁
		        while(!lock) {
		        	try {
						Thread.sleep(300);
						value = String.valueOf(System.currentTimeMillis() + ex);
						lock = RedisUtils.lock(taskId, value);
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
		        }
		        //sdk回调的userId不是会员memberId和applyerId，这里需要取透传参数里的
		        String memberId = "";
		        JSONObject phoneJsoonObject = (JSONObject) JSONObject.parse(notice_attached_param);
		        String phoneNo = phoneJsoonObject.getString("userPhone");//尝试取ios系统的
		        if(StringUtils.isBlank(phoneNo)) {
		        	//是安卓用户
		        	phoneNo = phoneJsoonObject.getString("cellPhone");
		        }
		        ChannelType channelType = ChannelType.yunyingshang;
		        switch (module) {
				case "taobao":
					channelType = ChannelType.taobao;
					break;
				case "alipay":
					channelType = ChannelType.alipay;
					break;
				case "wld":
					channelType = ChannelType.weilidai;
					break;
				case "zmf":
					channelType = ChannelType.zmf;
					break;
				default:
					break;
				}
		        RcTianji rcTianji = new RcTianji();
		        rcTianji.setPhoneNo(phoneNo);
		        rcTianji.setDataStatus(DataStatus.task_created);
		        rcTianji.setChannelType(channelType);
		        List<RcTianji> tianjiList = rcTianjiService.findList(rcTianji);
		        if(Collections3.isEmpty(tianjiList)) {
		        	return ResponseData.error("天机Sdk回调查询认证记录错误");
		        }
		        rcTianji = tianjiList.get(0);
		        String outUniqueId = rcTianji.getId().toString();
	        	distributeTaskData(memberId, outUniqueId, search_id, module);
		        //释放锁
		        RedisUtils.unlock(taskId, value);
		        
		        //运营商，淘宝，支付宝会发起获取报告的任务
		        String method = "";
		        switch (module) {
				case "mobile":
					method = TianjiConstant.tj_method_getData_yys;
					break;
				case "taobao":
					//淘宝支付宝暂时不获取报告数据
					method = TianjiConstant.tj_method_getData_taobao;
					break;
				case "alipay":
					method = TianjiConstant.tj_method_getData_alipay;
					break;
				case "wld":case "zmf":case "ec":case "mt_busi":
					return ResponseData.success("认证任务成功");
				default:
					return ResponseData.success("认证任务成功");
				}
		        String version = "";
		        if(module.equals("alipay")) {
		        	version = "2.0";
		        }else if(module.equals("mobile")) {
		        	version = "3.0";
		        }else {
		        	version = "1.0";
		        }
		        Map<String, String> params = new HashMap<String, String>();
		        params.put("type", module);
		        params.put("platform", "api");
		        params.put("name",rcTianji.getRealName());
		        params.put("phone", rcTianji.getPhoneNo());
		        params.put("idNumber", rcTianji.getIdNo());
		        params.put("userId", session);
		        params.put("outUniqueId", rcTianji.getId().toString());
		        params.put("version", version);
		        params.put("notifyUrl", Global.getConfig("domain")+"/callback/tainji/callbackForH5");
		        JSONObject reportRsponse = sendReportTask(params, method);
		        if(!StringUtils.equals(reportRsponse.getString("error"), "200")) {
		        	log.error("天机认证：{}发起获取报告的任务请求失败！返回error:{},msg:{}",reportRsponse.getJSONObject("tianji_api_tianjireport_collectuser_response").getString("outUniqueId"),reportRsponse.getString("error"),reportRsponse.getString("msg"));
		        	return ResponseData.error("数据抓取任务成功，发送报告请求失败！");
		        }
		        return ResponseData.success("报告任务请求成功");
			}
			if(StringUtils.isNotBlank(status) && StringUtils.equals(status, "3")) {
				//抓取失败
				String errorReasonDetail = request.getParameter("msg");
				log.error("天机回调：userId:{},accountType:{}数据抓取失败：{}",userId,module,errorReasonDetail);
				return ResponseData.error("认证失败");
			}
			return ResponseData.success("操作成功");
		}
	 
	 
	 
	private void distributeTaskData(String userId,String outUniqueId,String search_id, String accountType) {
		switch(accountType){
		    case "taobao_crawl" :
				saveTaskData(RcTianji.ChannelType.taobao,search_id,outUniqueId,TianjiConstant.tj_method_getData_taobao,accountType);
		       break;
		    case "jd_crawl" :
				saveTaskData(RcTianji.ChannelType.jingdong,search_id,outUniqueId,TianjiConstant.tj_method_getData_jingdong,accountType);
		       break; 
		    case "mobile" :
		    	saveTaskData(RcTianji.ChannelType.yunyingshang,search_id,outUniqueId,TianjiConstant.tj_method_getData_yys,accountType);
		       break;
		    case "insure" :
		    	saveTaskData(RcTianji.ChannelType.shebao,search_id,outUniqueId,TianjiConstant.tj_method_getData_shebao,accountType);
		       break; 
		    case "fund" :
		    	saveTaskData(RcTianji.ChannelType.gongjijin,search_id,outUniqueId,TianjiConstant.tj_method_getData_gjj,accountType);
		       break; 
		    case "chsi" :
		    	saveTaskData(RcTianji.ChannelType.xuexinwang,search_id,outUniqueId,TianjiConstant.tj_method_getData_xuexin,accountType);	
		       break;
		    case "ibank_crawl" :
		    	saveTaskData(RcTianji.ChannelType.wangyin,search_id,outUniqueId,TianjiConstant.tj_method_getData_wangyin,accountType);
		       break;
		    case "alipay_crawl" :
		    	saveTaskData(RcTianji.ChannelType.alipay,search_id,outUniqueId,TianjiConstant.tj_method_getData_alipay,accountType);
		       break;
		    default : 
		       log.error("渠道类型不支持[{}]",accountType);
		       break;
	    }
	}
	
	private void distributeReportData(String userId,String outUniqueId,String account,String search_id, String accountType) {
		switch(accountType){
		    case "jd" :
		    	saveReportData(RcTianji.ChannelType.jingdong, userId, outUniqueId, account, search_id, accountType);
		       break; 
		    case "mobile" :
		    	saveReportData(RcTianji.ChannelType.yunyingshang, userId, outUniqueId, account, search_id, accountType);
		       break;
		    case "alipay" :
		    	saveReportData(ChannelType.alipay, userId, outUniqueId, account, search_id, accountType);
		       break;
		    case "credit" :
		    	saveReportData(ChannelType.wangyin, userId, outUniqueId, account, search_id, accountType);
		    	break;
		    case "credit_email" :
		    	break;
		    case "taobao" :
		    	saveReportData(ChannelType.taobao, userId, outUniqueId, account, search_id, accountType);
		    	break;
		    case "insure_rp" :
		    	saveReportData(ChannelType.shebao, userId, outUniqueId, account, search_id, accountType);
		    	break;
		    case "fund_rp" :
		    	saveReportData(ChannelType.gongjijin, userId, outUniqueId, account, search_id, accountType);
		    	break;
		    default : 
		       log.error("渠道类型不支持[{}]",accountType);
		       break;
	    }
	}
	
	private void saveTaskData(RcTianji.ChannelType channelType,String searchId,String  outUniqueId,String method,String accountType) {
		try {
			log.info("天机抓取{}数据任务结束。",channelType);
			JSONObject responseData = rcTianjiService.getCrawlData(searchId, accountType, method,outUniqueId);
			if (responseData != null && StringUtils.equals(responseData.getString("error"), "200")) {
				String responseMethodName = method.replace(".", "_") + "_response";
				JSONObject respData = responseData.getJSONObject(responseMethodName);
				//这里有问题
				JSONObject userdata = null;
				if(channelType.equals(ChannelType.yunyingshang)) {
					JSONObject data_obj = respData.getJSONObject("data");
					JSONArray dataList = data_obj.getJSONArray("data_list");
					JSONObject basicData = (JSONObject) dataList.get(0);
					userdata = basicData.getJSONObject("userdata");
				}
					if (outUniqueId.contains("ufang_")) {	
						RcTianji rcTianji = rcTianjiService.get(Long.parseLong(outUniqueId.replace("ufang_", "")));
						rcTianji.setChannelType(channelType);
						rcTianji.setTaskId(searchId);
						if(channelType.equals(ChannelType.yunyingshang)) {
							rcTianji.setChannelSrc(userdata.getString("user_source"));	
						}
						rcTianji.setTaskData(respData.toString());
						rcTianji.setDataStatus(RcTianji.DataStatus.data_created);
						rcTianjiService.saveTaskData(rcTianji);
					}else if(StringUtils.contains(outUniqueId, "ufdebt_")){
						//TODO 这里去applyerId有问题
						Long applyerId = Long.valueOf(StringUtils.substring(outUniqueId, 7));//outUniqueId是RcTianji的id
						RcTianji rcTianji = rcTianjiService.findByTaskId(searchId);
						if(rcTianji==null) {
							rcTianji = new RcTianji();
							rcTianji.setUser(UfangUserUtils.getUser());
							rcTianji.setProdType(RcTianji.ProdType.ufdebt);
						    rcTianji.setOrgId(applyerId);
						    rcTianji.setChannelType(channelType);
						    rcTianji.setTaskId(searchId);   
						    rcTianji.setReportStatus(RcTianji.ReportStatus.task_created);
						    
							UfangLoanMarketApplyer applyer = ufangLoanMarketApplyerService.get(applyerId);
							applyer.setReportTaskId(outUniqueId);
							applyer.setOperatorStatus(OperatorStatus.authed);
							ufangLoanMarketApplyerService.save(applyer);
							
							rcTianji.setPhoneNo(applyer.getPhoneNo());
						    rcTianji.setRealName(applyer.getName());
						    rcTianji.setIdNo(applyer.getIdNo());
						    rcTianji.setUserName(applyer.getPhoneNo());
						}
					    if(channelType.equals(ChannelType.yunyingshang)) {
					    	rcTianji.setChannelSrc(userdata.getString("user_source"));	
					    }
						rcTianji.setTaskData(respData.toString());
						rcTianji.setDataStatus(RcTianji.DataStatus.data_created);
						rcTianjiService.saveTaskData(rcTianji);	
										
					}else{
						RcTianji rcTianji = rcTianjiService.findByTaskId(searchId);
						if(rcTianji==null) {
							//取会员信息
							Member member = memberService.get(Long.valueOf(outUniqueId));
							rcTianji = new RcTianji();
					    	rcTianji.setProdType(RcTianji.ProdType.app);
							rcTianji.setOrgId(Long.parseLong(outUniqueId));			
							rcTianji.setChannelType(channelType);
							rcTianji.setTaskId(searchId);
							rcTianji.setPhoneNo(member.getUsername());
							rcTianji.setRealName(member.getName());
						    rcTianji.setIdNo(member.getIdNo());
							rcTianji.setUserName(member.getUsername());
							rcTianji.setUser(UfangUserUtils.getUser());
							rcTianji.setReportStatus(RcTianji.ReportStatus.task_created);
						}
						if(channelType.equals(ChannelType.yunyingshang)) {
							rcTianji.setChannelSrc(userdata.getString("user_source"));	
						}
						rcTianji.setTaskData(respData.toString());
						rcTianji.setDataStatus(RcTianji.DataStatus.data_created);						
						rcTianjiService.saveTaskData(rcTianji);
					}									        
				} 
		} catch (Exception e) {    
			log.error(Exceptions.getStackTraceAsString(e));
		}

	}
	
	private void saveReportData(RcTianji.ChannelType channelType,String userId,String outUniqueId,String account,String search_id, String accountType) {
		try {
			JSONObject detailResult = rcTianjiService.getReportDetail(userId, accountType, outUniqueId);
			if (detailResult != null) {
				if (StringUtils.equals(detailResult.getString("error"), "200")) {
					JSONObject detail = detailResult.getJSONObject("tianji_api_tianjireport_detail_response");
					if (outUniqueId.contains("ufang_")) {	
						//TODO
					}else if(StringUtils.contains(outUniqueId, "ufdebt_")){
						// TODO 
					}else {
					    RcTianji rcTianji = rcTianjiService.findByTaskId(search_id);
					    if(rcTianji==null) {
					    	rcTianji = new RcTianji();
					    	rcTianji.setProdType(RcTianji.ProdType.app);
							rcTianji.setOrgId(Long.parseLong(outUniqueId));			
							rcTianji.setChannelType(channelType);
							rcTianji.setTaskId(search_id);
							rcTianji.setDataStatus(RcTianji.DataStatus.task_created);
							rcTianji.setUser(UfangUserUtils.getUser());
					    }
					    String gunzip = StringUtils.gunzip(detail.toString());
					    rcTianji.setHtmlStr(detail.getString("html"));
						rcTianji.setReportData(gunzip);
						rcTianji.setReportStatus(RcTianji.ReportStatus.report_created);
						rcTianjiService.saveTaskReport(rcTianji);	
					}									        
				} 
			}
		} catch (Exception e) {    
			log.error(Exceptions.getStackTraceAsString(e));
		}
	}
	
	private JSONObject sendReportTask(Map<String, String> params,String method) {
		//组装参数
		TianJiHttpUtils tianJiHttpUtils = new TianJiHttpUtils();
		tianJiHttpUtils.setBizData(JSON.toJSONString(params));
		tianJiHttpUtils.setMethod(method);
		tianJiHttpUtils.setTimestamp(String.valueOf(System.currentTimeMillis()));
		tianJiHttpUtils.setSign();
		JSONObject response = tianJiHttpUtils.post();
		if(response != null) {
			log.info("天机发起报告返回结果码：{}",response.getString("error"));
		}
		return response;
	}
	
	
	/**
	 * 一元支付样例
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "xinyanBgTemplate")
	public ModelAndView xinyanBgTemplate() {
		ModelAndView mv2 = new ModelAndView("app/creditArchives/xinyongbgExamples");
		return mv2;
	}
	
}