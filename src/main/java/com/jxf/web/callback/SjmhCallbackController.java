package com.jxf.web.callback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.rc.entity.RcSjmh;
import com.jxf.rc.service.RcSjmhService;
import com.jxf.rc.utils.ThirdPartyUtils;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.utils.Exceptions;

import com.jxf.svc.utils.ObjectUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.ufang.entity.UfangLoanMarketApplyer;
import com.jxf.ufang.entity.UfangLoanMarketApplyer.OperatorStatus;
import com.jxf.ufang.service.UfangLoanMarketApplyerService;
import com.jxf.ufang.util.UfangUserUtils;
import com.jxf.web.model.ResponseData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


import java.util.Map;


/**
 * @author Administrator
 */
@RestController
@RequestMapping(value = "/callback/sjmh")
public class SjmhCallbackController {
	
	private static Logger log = LoggerFactory.getLogger(SjmhCallbackController.class);

	@Autowired
	private MemberService memberService;
	@Autowired
	private RcSjmhService rcSjmhService;
	@Autowired
	private UfangLoanMarketApplyerService ufangLoanMarketApplyerService;

	@PostMapping(value = "")
	public ResponseData callback(String notify_type,String notify_event,String notify_data, String  passback_params) {
		if (log.isDebugEnabled()){
			log.debug("数据魔盒回调成功");
			log.debug("notify_type={}",notify_type);
			log.debug("notify_event={}",notify_event);
			log.debug("notify_data={}",notify_data);
			log.debug("passback_params={}",passback_params);
		}
		Map<String, String> notify_data_map = JSON.parseObject(notify_data, new TypeReference<Map<String, String>>() {});
		String code = notify_data_map.get("code");
		if(!StringUtils.equals(code, "0")) {
			log.error("数据魔盒回调：passback_params:{}返回code:{},认证失败",passback_params,code);
			return ResponseData.error("认证失败");
		}
		String task_id = notify_data_map.get("task_id");
		String message = notify_data_map.get("message");
		String data = notify_data_map.get("data");
		
		//任务成功
		if(StringUtils.equals(notify_type, "ACQUIRE")||StringUtils.equals(notify_type, "ACQUIRE_SDK")) {
			if(StringUtils.equals(notify_event, "SUCCESS")) {
		        long ex = 10 * 1000L;
		        String value = String.valueOf(System.currentTimeMillis() + ex);
		        boolean lock = RedisUtils.lock(task_id, value);
		        //获取锁
		        while(!lock) {
		        	try {
						Thread.sleep(300);
						value = String.valueOf(System.currentTimeMillis() + ex);
						lock = RedisUtils.lock(task_id, value);
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
		        }   
	        	distributeTaskData(task_id,data,passback_params,message);
		        //释放锁
		        RedisUtils.unlock(task_id, value);
				return ResponseData.success("任务成功");
			}else {
				return ResponseData.success("任务失败");	
			}	
		}
		
		//报告成功
		if(StringUtils.equals(notify_type, "REPORT")) {
			if(StringUtils.equals(notify_event, "SUCCESS")) {
		        long ex = 10 * 1000L;
		        String value = String.valueOf(System.currentTimeMillis() + ex);
		        boolean lock = RedisUtils.lock(task_id, value);
		        //获取锁
		        while(!lock) {
		        	try {
						Thread.sleep(300);
						value = String.valueOf(System.currentTimeMillis() + ex);
						lock = RedisUtils.lock(task_id, value);
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
		        } 
		        distributeReportData(task_id,data,passback_params,message); 
			    //释放锁
			    RedisUtils.unlock(task_id, value);
		 
				return ResponseData.success("报告生成成功");
			}else {
				return ResponseData.success("报告生成失败");	
			}	
		}
				
		return ResponseData.success("操作成功");
	}
	
	
	private void distributeTaskData(String task_id,String data,String  passback_params,String message) {
		Map<String, String> data_map = JSON.parseObject(data, new TypeReference<Map<String, String>>() {});
		String channel_type = data_map.get("channel_type");
		String channel_code = data_map.get("channel_code");

		switch(channel_type){
		    case "DS" :
				// 京东
				if (channel_code.equals("000001")) {
					saveTaskData(RcSjmh.ChannelType.jingdong,task_id,ThirdPartyUtils.jingdongQueryUrl,passback_params,message);
				}
				// 淘宝
				if (channel_code.equals("000003")) {
					saveTaskData(RcSjmh.ChannelType.taobao,task_id,ThirdPartyUtils.taobaoQueryUrl,passback_params,message);
				}
		       break; 
		    case "YYS" :
		    	saveTaskData(RcSjmh.ChannelType.yunyingshang,task_id,ThirdPartyUtils.yunyingshangQueryUrl,passback_params,message);
		       break;
		    case "SHE_BAO" :
		    	saveTaskData(RcSjmh.ChannelType.shebao,task_id,ThirdPartyUtils.shebaoQueryUrl,passback_params,message);
		       break; 
		    case "GJJ" :
		    	saveTaskData(RcSjmh.ChannelType.gongjijin,task_id,ThirdPartyUtils.shebaoQueryUrl,passback_params,message);
		       break; 
		    case "CHSI" :
		    	saveTaskData(RcSjmh.ChannelType.xuexinwang,task_id,ThirdPartyUtils.xuexinwangQueryUrl,passback_params,message);	
		       break;
		    case "WY" :
		    	saveTaskData(RcSjmh.ChannelType.wangyin,task_id,ThirdPartyUtils.wangyinQueryUrl,passback_params,message);
		       break;
		    default : 
		       log.error("渠道类型不支持[{}]",channel_type);
		       break;
	    }
	}
	
	private void distributeReportData(String task_id,String data,String  passback_params,String message) {
		Map<String, String> data_map = JSON.parseObject(data, new TypeReference<Map<String, String>>() {});
		String channel_type = data_map.get("channel_type");
		String channel_code = data_map.get("channel_code");

		switch(channel_type){
		    case "DS" :
				// 京东
				if (channel_code.equals("000001")) {
					saveReportData(RcSjmh.ChannelType.jingdong,task_id,ThirdPartyUtils.jingdongQueryUrl,passback_params,message);
				}
				// 淘宝
				if (channel_code.equals("000003")) {
					saveReportData(RcSjmh.ChannelType.taobao,task_id,ThirdPartyUtils.taobaoQueryUrl,passback_params,message);
				}
		       break; 
		    case "YYS" :
		    	saveReportData(RcSjmh.ChannelType.yunyingshang,task_id,ThirdPartyUtils.yunyingshangReportQueryUrl,passback_params,message);
		       break;
		    case "SHE_BAO" :
		       break; 
		    case "GJJ" :
		       break; 
		    case "CHSI" :
		       break;
		    case "WY" :
		       break;
		    default : 
		       log.error("渠道类型不支持[{}]",channel_type);
		       break;
	    }
	}
	
	private void saveTaskData(RcSjmh.ChannelType channelType,String task_id,String queryUrl,String  passback_params,String message) {
		
		try {
			String task_result = rcSjmhService.getTaskReuslt(task_id,queryUrl);
			if (StringUtils.isNotBlank(task_result)) {
				JSONObject task_result_obj = JSONObject.parseObject(task_result);
				JSONObject data_obj = task_result_obj.getJSONObject("data");
				if (StringUtils.equals(task_result_obj.getString("code"), "0")&&ObjectUtils.isNotBlank(data_obj)) {													
					if (passback_params.contains("ufang_")) {	
						RcSjmh rcSjmh = rcSjmhService.get(Long.parseLong(passback_params.replace("ufang_", "")));
						rcSjmh.setChannelType(channelType);
						rcSjmh.setTaskId(task_id);
						rcSjmh.setChannelCode(data_obj.getString("channel_code"));
						rcSjmh.setChannelSrc(data_obj.getString("channel_src"));	
						rcSjmh.setChannelAttr(data_obj.getString("channel_attr"));
						rcSjmh.setTaskData(data_obj.getString("task_data"));
						rcSjmh.setRmk(message);
						rcSjmh.setDataStatus(RcSjmh.DataStatus.data_created);
						rcSjmhService.saveTaskData(rcSjmh);
					}else if(StringUtils.contains(passback_params, "ufdebt_")){
						Long applyerId = Long.valueOf(StringUtils.substring(passback_params, 7));
						RcSjmh rcSjmh = rcSjmhService.findByTaskId(task_id);
						if(rcSjmh==null) {
							rcSjmh = new RcSjmh();
							rcSjmh.setUser(UfangUserUtils.getUser());
							rcSjmh.setProdType(RcSjmh.ProdType.ufdebt);
						    rcSjmh.setOrgId(applyerId);
						    rcSjmh.setChannelType(channelType);
						    rcSjmh.setTaskId(task_id);   
						    rcSjmh.setReportStatus(RcSjmh.ReportStatus.task_created);
						    
							UfangLoanMarketApplyer applyer = ufangLoanMarketApplyerService.get(applyerId);
							applyer.setReportTaskId(task_id);
							applyer.setOperatorStatus(OperatorStatus.authed);
							ufangLoanMarketApplyerService.save(applyer);
						}
					    rcSjmh.setPhoneNo(data_obj.getString("user_mobile"));
					    rcSjmh.setRealName(data_obj.getString("real_name"));
					    rcSjmh.setIdNo(data_obj.getString("identity_code"));
					    rcSjmh.setUserName(data_obj.getString("user_name"));
						rcSjmh.setChannelCode(data_obj.getString("channel_code"));
						rcSjmh.setChannelSrc(data_obj.getString("channel_src"));	
						rcSjmh.setChannelAttr(data_obj.getString("channel_attr"));
						rcSjmh.setTaskData(data_obj.getString("task_data"));
						rcSjmh.setRmk(message);						
						rcSjmh.setDataStatus(RcSjmh.DataStatus.data_created);
						rcSjmhService.saveTaskData(rcSjmh);	
										
					}else{
						RcSjmh rcSjmh = rcSjmhService.findByTaskId(task_id);
						Member member = memberService.get(Long.parseLong(passback_params));
						if(rcSjmh==null) {
							rcSjmh = new RcSjmh();
					    	rcSjmh.setProdType(RcSjmh.ProdType.app);
							rcSjmh.setOrgId(member.getId());			
							rcSjmh.setChannelType(channelType);
							rcSjmh.setTaskId(task_id);
							rcSjmh.setPhoneNo(member.getUsername());
							rcSjmh.setIdNo(member.getIdNo());
							rcSjmh.setRealName(member.getName());
							rcSjmh.setUserName(data_obj.getString("user_name"));
							rcSjmh.setUser(UfangUserUtils.getUser());
							rcSjmh.setReportStatus(RcSjmh.ReportStatus.task_created);
						}
						rcSjmh.setChannelCode(data_obj.getString("channel_code"));
						rcSjmh.setChannelSrc(data_obj.getString("channel_src"));
						rcSjmh.setChannelAttr(data_obj.getString("channel_attr"));
						rcSjmh.setTaskData(data_obj.getString("task_data"));
						rcSjmh.setRmk(message);
						rcSjmh.setDataStatus(RcSjmh.DataStatus.data_created);						
						rcSjmhService.saveTaskData(rcSjmh);
					}									        
				} 
			}
		} catch (Exception e) {    
			log.error(Exceptions.getStackTraceAsString(e));
		}

	}
	
	private void saveReportData(RcSjmh.ChannelType channelType,String task_id,String queryUrl,String  passback_params,String message) {
		
		try {
			String task_result = rcSjmhService.getTaskReuslt(task_id,queryUrl);
			if (StringUtils.isNotBlank(task_result)) {
				JSONObject task_result_obj = JSONObject.parseObject(task_result);
				if (StringUtils.equals(task_result_obj.getString("code"), "0")&&StringUtils.isNotBlank(task_result_obj.getString("data"))) {													
					if (passback_params.contains("ufang_")) {	

					}else if(StringUtils.contains(passback_params, "ufdebt_")){
						
					}else {
					    RcSjmh rcSjmh = rcSjmhService.findByTaskId(task_id);
					    if(rcSjmh==null) {
					    	rcSjmh = new RcSjmh();
					    	rcSjmh.setProdType(RcSjmh.ProdType.app);
							rcSjmh.setOrgId(Long.parseLong(passback_params));			
							rcSjmh.setChannelType(channelType);
							rcSjmh.setTaskId(task_id);
							rcSjmh.setRmk(message);
							rcSjmh.setDataStatus(RcSjmh.DataStatus.task_created);
							rcSjmh.setUser(UfangUserUtils.getUser());
					    }
					    String gunzip = StringUtils.gunzip(task_result_obj.getString("data"));
						rcSjmh.setReportData(gunzip);
						rcSjmh.setReportStatus(RcSjmh.ReportStatus.report_created);
						rcSjmhService.saveTaskReport(rcSjmh);	
					}									        
				} 
			}
		} catch (Exception e) {    
			log.error(Exceptions.getStackTraceAsString(e));
		}

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