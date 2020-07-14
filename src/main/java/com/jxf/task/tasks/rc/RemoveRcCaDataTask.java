package com.jxf.task.tasks.rc;




import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.ebaoquan.rop.thirdparty.org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberExtend;
import com.jxf.mem.service.MemberExtendService;
import com.jxf.mem.service.MemberService;

import com.jxf.rc.dao.RcCaDataDaoV2;
import com.jxf.rc.entity.RcCaData;
import com.jxf.rc.entity.RcCaDataV2;
import com.jxf.rc.entity.RcSjmh;
import com.jxf.rc.service.RcCaDataService;
import com.jxf.rc.service.RcSjmhService;

import com.jxf.svc.config.Global;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.JSONUtil;


/**
 * 迁移RcCaData数据
 *
 * @author xrd
 */
@DisallowConcurrentExecution
public class RemoveRcCaDataTask implements Job {

	private static Logger logger = LoggerFactory.getLogger(RemoveRcCaDataTask.class);

	@Autowired
	private RcCaDataService rcCaDataService;
	@Autowired
	private RcCaDataDaoV2 rcCaDataDaoV2;
	@Autowired
	private RcSjmhService sjmhService;
	
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberExtendService memberExtendService;
	
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

		Date beginDate = CalendarUtil.StringToDate("2019-08-01");
		Date endDate = CalendarUtil.StringToDate("2019-08-09");
		Date date = beginDate;
		while (date.getTime() <= endDate.getTime()) {
			String dateStr = CalendarUtil.DateToString(date, "yyyy-MM-dd HH");
			logger.warn("开始获取{}的数据",dateStr);
			try {
			remove(dateStr);
			} catch (Exception e) {
				logger.error("出错{}",Exceptions.getStackTraceAsString(e));		
			}
			logger.warn("结束获取{}的数据",dateStr);
			date = CalendarUtil.addHour(date, 1);
		}
    }



	/**
	 * @param dateStr
	 * @return
	 */
	private boolean remove(String dateStr)  {
		
		Calendar beginCalendar = DateUtils.toCalendar(DateUtils.parseDate(dateStr));
//		beginCalendar.set(Calendar.HOUR_OF_DAY, beginCalendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		beginCalendar.set(Calendar.MINUTE, beginCalendar.getActualMinimum(Calendar.MINUTE));
		beginCalendar.set(Calendar.SECOND, beginCalendar.getActualMinimum(Calendar.SECOND));
		Date beginTime = beginCalendar.getTime();

		Calendar endCalendar = DateUtils.toCalendar(DateUtils.parseDate(dateStr));
//		endCalendar.set(Calendar.HOUR_OF_DAY, endCalendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		endCalendar.set(Calendar.MINUTE, endCalendar.getActualMaximum(Calendar.MINUTE));
		endCalendar.set(Calendar.SECOND, endCalendar.getActualMaximum(Calendar.SECOND));
		Date endTime = endCalendar.getTime();
//		logger.warn("开始获取{}的数据",beginTime);
//		logger.warn("结束获取{}的数据",endTime);
	

        
		RcCaData rcCaData = new RcCaData();
		rcCaData.setBeginTime(beginTime);
		rcCaData.setEndTime(endTime);
		List<RcCaData> caDataList = rcCaDataService.findList(rcCaData);	
		logger.warn("处理数据{}条",caDataList.size());

		for(RcCaData caData:caDataList) {

			RcCaDataV2 rcCaDataV2 = new RcCaDataV2();
			rcCaDataV2.setId(caData.getId());
			rcCaDataV2.setPhoneNo(caData.getPhoneNo());
			rcCaDataV2.setIdNo(caData.getIdNo());
			rcCaDataV2.setName(caData.getName());
			rcCaDataV2.setProvider(RcCaDataV2.Provider.values()[caData.getProvider().ordinal()]);
			boolean flag = true;
			switch(caData.getType()){
			    case zhimafen :
			    	rcCaDataV2.setType(RcCaDataV2.Type.zmf);
			    	rcCaDataV2.setContent(caData.getContent());
			       break;
			    case yunyingshang :
			       {
			    	rcCaDataV2.setType(RcCaDataV2.Type.yys);
			    	RcSjmh sjmh = sjmhService.findByPhoneNoChannelTypeReportStatus(caData.getPhoneNo(), RcSjmh.ChannelType.yunyingshang, RcSjmh.ReportStatus.report_arranged);			    		
			    	if(sjmh!=null) {
			    		rcCaDataV2.setReportNo(sjmh.getTaskId());
			    		rcCaDataV2.setContent(yunyingshang(sjmh));	
			    	}else {
			    		flag = false;
			    	}
			        break;
			       }
			    case taobao :
			       {
			    	rcCaDataV2.setType(RcCaDataV2.Type.taobao);			    	
			    	RcSjmh sjmh = sjmhService.findByPhoneNoChannelTypeDataStatus(caData.getPhoneNo(), RcSjmh.ChannelType.taobao, RcSjmh.DataStatus.data_arranged);  	
			    	if(sjmh!=null) {
			    	    rcCaDataV2.setContent(taobao(sjmh));	
			    	}else {
			    		flag = false;
			    	}
			        break; 
			      }
			    case xuexinwang :
			    	rcCaDataV2.setType(RcCaDataV2.Type.xxw);
			    	rcCaDataV2.setContent(caData.getContent());
			       break;
			    case shebao :
			    	rcCaDataV2.setType(RcCaDataV2.Type.shebao);
			    	rcCaDataV2.setContent(caData.getContent());
			       break; 
			    case gongjijin :
			    	rcCaDataV2.setType(RcCaDataV2.Type.gjj);
			    	rcCaDataV2.setContent(caData.getContent());
			       break; 
			    case wangyin :
			    	rcCaDataV2.setType(RcCaDataV2.Type.wangyin);
			    	rcCaDataV2.setContent(caData.getContent());
			       break;
			    default :
			       logger.warn("忽略{}数据类型{}",caData.getId(),caData.getType());
			       flag = false;
			       break;
	        }
			if(flag) {
				rcCaDataV2.setStatus(RcCaDataV2.Status.success);
				rcCaDataV2.setCreateBy(caData.getCreateBy());
				rcCaDataV2.setCreateTime(caData.getCreateTime());
				rcCaDataV2.setUpdateBy(caData.getUpdateBy());
				rcCaDataV2.setUpdateTime(caData.getUpdateTime());
				rcCaDataV2.setDelFlag(caData.getDelFlag());
				rcCaDataDaoV2.insert(rcCaDataV2);			
			}	
		}
	
		return true;
	}
 
	private String yunyingshang(RcSjmh sjmh) {
		try {
			Map<String, Object> yysMap = new HashMap<String, Object>();
//			String sjmhData = FileUtils.readFileToString(new File(Global.getConfig("uploadPath")+sjmh.getDataPath()),"utf-8");
			String sjmhReportData = FileUtils.readFileToString(new File(Global.getConfig("uploadPath")+sjmh.getReportPath()),"utf-8");
//			JSONObject task_data_obj = JSONObject.parseObject(sjmhData);
			JSONObject report_data_obj = JSONObject.parseObject(sjmhReportData);
//			JSONObject base_info_obj = task_data_obj.getJSONObject("base_info");
//			JSONObject account_info_obj = task_data_obj.getJSONObject("account_info");
//			JSONArray call_info_arr = task_data_obj.getJSONArray("call_info");
			
			
			
			JSONObject mobile_info_obj = report_data_obj.getJSONObject("mobile_info");
			String user_mobile = mobile_info_obj.getString("user_mobile");
			JSONObject info_match_obj = report_data_obj.getJSONObject("info_match");
			
			Map<String, Object> mobile_info_map = new HashMap<String, Object>();
			mobile_info_map.put("account_status", mobile_info_obj.get("account_status"));
			mobile_info_map.put("mobile_net_age", mobile_info_obj.get("mobile_net_age"));
			mobile_info_map.put("mobile_area",mobile_info_obj.get("mobile_net_addr"));	
			mobile_info_map.put("id_no_match",info_match_obj.get("identity_code_check_yys"));
			mobile_info_map.put("phone_no_match",StringUtils.equals(user_mobile, sjmh.getPhoneNo())?"匹配":"不匹配");
			yysMap.put("mobile_info", mobile_info_map);
			
			//前10手机联系人风险分析
			Map<String, Object> top10_contact_risk_analysis_map = new HashMap<String, Object>();
			JSONObject contact_blacklist_analysis_obj = report_data_obj.getJSONObject("contact_blacklist_analysis");
			JSONObject contact_manyheads_analysis_obj = report_data_obj.getJSONObject("contact_manyheads_analysis");
			top10_contact_risk_analysis_map.put("black_top10_contact_total_count_ratio", contact_blacklist_analysis_obj.getOrDefault("black_top10_contact_total_count_ratio", ""));
			top10_contact_risk_analysis_map.put("manyheads_top10_contact_recent6month_have_partnercode_count", contact_manyheads_analysis_obj.getOrDefault("manyheads_top10_contact_recent6month_have_partnercode_count", ""));
			top10_contact_risk_analysis_map.put("manyheads_top10_contact_recent6month_partnercode_count_avg", contact_manyheads_analysis_obj.getOrDefault("manyheads_top10_contact_recent6month_partnercode_count_avg", ""));
			yysMap.put("top10_contact_risk_analysis", top10_contact_risk_analysis_map);
			
			//联系人催收风险分析
			Map<String, Object> contact_suspect_collection_analysis_map = new HashMap<String, Object>();
			JSONArray contact_suspect_collection_analysis_arr = report_data_obj.getJSONArray("contact_suspect_collection_analysis");
			for (int i = 0; i < contact_suspect_collection_analysis_arr.size(); i++) {
				JSONObject contact_suspect_collection_analysis_obj = contact_suspect_collection_analysis_arr.getJSONObject(i);
				String risk_type= contact_suspect_collection_analysis_obj.getString("risk_type");
				if(StringUtils.equals(risk_type, "催收")) {
					contact_suspect_collection_analysis_map.put("collection_contact_count_passive_6month", contact_suspect_collection_analysis_obj.getOrDefault("contact_count_passive_6month", ""));
					contact_suspect_collection_analysis_map.put("collection_call_count_passive_6month", contact_suspect_collection_analysis_obj.getOrDefault("call_count_passive_6month", ""));
				}
				if(StringUtils.equals(risk_type, "疑似催收")) {
					contact_suspect_collection_analysis_map.put("suspect_collection_contact_count_passive_6month", contact_suspect_collection_analysis_obj.getOrDefault("contact_count_passive_6month", ""));
					contact_suspect_collection_analysis_map.put("suspect_collection_call_count_passive_6month", contact_suspect_collection_analysis_obj.getOrDefault("call_count_passive_6month", ""));
				}
			}
			yysMap.put("contact_suspect_collection_analysis", contact_suspect_collection_analysis_map);
			
			//紧急联系人统计
			Member member = memberService.findByUsername(user_mobile);
			MemberExtend memberExtend = memberExtendService.getByMember(member);	
			if(memberExtend!=null) {
				
				JSONArray all_contact_detail_arr = report_data_obj.getJSONArray("all_contact_detail");
				JSONArray emergency_contact_stats = new JSONArray();
				for (int i = 0; i < all_contact_detail_arr.size(); i++) {
					JSONObject contact_detail_obj = all_contact_detail_arr.getJSONObject(i);
					Map<String, Object> emergency_contact_stats_map = new HashMap<String, Object>();
					String contact_number = contact_detail_obj.getString("contact_number");
					if(StringUtils.equals(contact_number, memberExtend.getEcpPhoneNo1())) {
						emergency_contact_stats_map.put("ecp", memberExtend.getEcp1());
						emergency_contact_stats_map.put("contact_number", contact_number);
						emergency_contact_stats_map.put("call_time_1month", contact_detail_obj.get("call_time_1month"));
						emergency_contact_stats_map.put("call_time_3month", contact_detail_obj.get("call_time_3month"));
						emergency_contact_stats_map.put("call_time_6month", contact_detail_obj.get("call_time_6month"));
						
						emergency_contact_stats_map.put("call_count_1month", contact_detail_obj.get("call_count_1month"));
						emergency_contact_stats_map.put("call_count_3month", contact_detail_obj.get("call_count_3month"));
						emergency_contact_stats_map.put("call_count_6month", contact_detail_obj.get("call_count_6month"));	
						emergency_contact_stats.add(emergency_contact_stats_map);	
					}
					if(StringUtils.equals(contact_number, memberExtend.getEcpPhoneNo2())) {
						emergency_contact_stats_map.put("ecp", memberExtend.getEcp1());
						emergency_contact_stats_map.put("contact_number", contact_number);
						emergency_contact_stats_map.put("call_time_1month", contact_detail_obj.get("call_time_1month"));
						emergency_contact_stats_map.put("call_time_3month", contact_detail_obj.get("call_time_3month"));
						emergency_contact_stats_map.put("call_time_6month", contact_detail_obj.get("call_time_6month"));
						
						emergency_contact_stats_map.put("call_count_1month", contact_detail_obj.get("call_count_1month"));
						emergency_contact_stats_map.put("call_count_3month", contact_detail_obj.get("call_count_3month"));
						emergency_contact_stats_map.put("call_count_6month", contact_detail_obj.get("call_count_6month"));	
						emergency_contact_stats.add(emergency_contact_stats_map);	
					}
					if(StringUtils.equals(contact_number, memberExtend.getEcpPhoneNo3())) {
						emergency_contact_stats_map.put("ecp", memberExtend.getEcp1());
						emergency_contact_stats_map.put("contact_number", contact_number);
						emergency_contact_stats_map.put("call_time_1month", contact_detail_obj.get("call_time_1month"));
						emergency_contact_stats_map.put("call_time_3month", contact_detail_obj.get("call_time_3month"));
						emergency_contact_stats_map.put("call_time_6month", contact_detail_obj.get("call_time_6month"));
						
						emergency_contact_stats_map.put("call_count_1month", contact_detail_obj.get("call_count_1month"));
						emergency_contact_stats_map.put("call_count_3month", contact_detail_obj.get("call_count_3month"));
						emergency_contact_stats_map.put("call_count_6month", contact_detail_obj.get("call_count_6month"));	
						emergency_contact_stats.add(emergency_contact_stats_map);	
					}
					
				}
				yysMap.put("emergency_contact_stats",emergency_contact_stats);
			}
	
			//风险联系人统计
			JSONArray risk_contact_stats_arr = report_data_obj.getJSONArray("risk_contact_stats");
			JSONArray riskContactStats = new JSONArray();
			for (int i = 0; i < risk_contact_stats_arr.size(); i++) {
				JSONObject risk_contact_stats_obj = risk_contact_stats_arr.getJSONObject(i);
				Map<String, Object> risk_contact_stats_map = new HashMap<String, Object>();
				risk_contact_stats_map.put("risk_type", risk_contact_stats_obj.get("risk_type"));
				risk_contact_stats_map.put("call_count_3month", risk_contact_stats_obj.get("call_count_3month"));
				risk_contact_stats_map.put("call_time_3month", risk_contact_stats_obj.get("call_time_3month"));
				risk_contact_stats_map.put("call_count_6month", risk_contact_stats_obj.get("call_count_6month"));
				risk_contact_stats_map.put("call_time_6month", risk_contact_stats_obj.get("call_time_6month"));
				riskContactStats.add(risk_contact_stats_map);		
			}
			yysMap.put("risk_contact_stats",riskContactStats);
			
			//全部联系统计
			JSONObject all_contact_stats_obj = report_data_obj.getJSONObject("all_contact_stats");
			Map<String, Object> all_contact_stats_map = new HashMap<String, Object>();
			all_contact_stats_map.put("call_count_3month", all_contact_stats_obj.get("call_count_3month"));
			all_contact_stats_map.put("call_count_active_3month", all_contact_stats_obj.get("call_count_active_3month"));
			all_contact_stats_map.put("call_count_passive_3month", all_contact_stats_obj.get("call_count_passive_3month"));
			all_contact_stats_map.put("contact_count_3month", all_contact_stats_obj.get("contact_count_3month"));
			all_contact_stats_map.put("contact_count_active_3month", all_contact_stats_obj.get("contact_count_active_3month"));
			all_contact_stats_map.put("contact_count_passive_3month", all_contact_stats_obj.get("contact_count_passive_3month"));	
			all_contact_stats_map.put("call_time_3month", all_contact_stats_obj.get("call_time_3month"));
				
			all_contact_stats_map.put("call_count_6month", all_contact_stats_obj.get("call_count_6month"));
			all_contact_stats_map.put("call_count_active_6month", all_contact_stats_obj.get("call_count_active_6month"));
			all_contact_stats_map.put("call_count_passive_6month", all_contact_stats_obj.get("call_count_passive_6month"));
			all_contact_stats_map.put("contact_count_6month", all_contact_stats_obj.get("contact_count_6month"));
			all_contact_stats_map.put("contact_count_active_6month", all_contact_stats_obj.get("contact_count_active_6month"));
			all_contact_stats_map.put("contact_count_passive_6month", all_contact_stats_obj.get("contact_count_passive_6month"));	
			all_contact_stats_map.put("call_time_6month", all_contact_stats_obj.get("call_time_6month"));
	
			yysMap.put("all_contact_stats",all_contact_stats_map);
			
			//消费统计
			Map<String, Object> consumption_stats_map = new HashMap<String, Object>();
			JSONObject consumption_stats_obj = report_data_obj.getJSONObject("carrier_consumption_stats");
			
			consumption_stats_map.put("recharge_amount_3month", consumption_stats_obj.get("recharge_amount_3month"));
			consumption_stats_map.put("recharge_count_3month", consumption_stats_obj.get("recharge_count_3month"));
			consumption_stats_map.put("consume_amount_3month", consumption_stats_obj.get("consume_amount_3month"));
			
			consumption_stats_map.put("recharge_amount_6month", consumption_stats_obj.get("recharge_amount_6month"));
			consumption_stats_map.put("recharge_count_6month", consumption_stats_obj.get("recharge_count_6month"));
			consumption_stats_map.put("consume_amount_6month", consumption_stats_obj.get("consume_amount_6month"));
			
			yysMap.put("consumption_stats",consumption_stats_map);
			//静默活跃统计
			Map<String, Object> active_silence_stats_map = new HashMap<String, Object>();
			JSONObject active_silence_stats_obj = report_data_obj.getJSONObject("active_silence_stats");
			
			active_silence_stats_map.put("active_day_1call_3month", active_silence_stats_obj.get("active_day_1call_3month"));
			active_silence_stats_map.put("max_continue_active_day_1call_3month", active_silence_stats_obj.get("max_continue_active_day_1call_3month"));
			active_silence_stats_map.put("silence_day_0call_3month", active_silence_stats_obj.get("silence_day_0call_3month"));
			active_silence_stats_map.put("continue_silence_day_over3_0call_0msg_send_3month", active_silence_stats_obj.get("continue_silence_day_over3_0call_0msg_send_3month"));
			active_silence_stats_map.put("silence_day_0call_ratio_3month",active_silence_stats_obj.getInteger("silence_day_0call_3month")/90 );
			active_silence_stats_map.put("silence_day_0call_active_ratio_3month",active_silence_stats_obj.getInteger("silence_day_0call_active_3month")/90 );
			
			active_silence_stats_map.put("active_day_1call_6month", active_silence_stats_obj.get("active_day_1call_6month"));
			active_silence_stats_map.put("max_continue_active_day_1call_6month", active_silence_stats_obj.get("max_continue_active_day_1call_6month"));
			active_silence_stats_map.put("silence_day_0call_6month", active_silence_stats_obj.get("silence_day_0call_6month"));
			active_silence_stats_map.put("continue_silence_day_over3_0call_0msg_send_6month", active_silence_stats_obj.get("continue_silence_day_over3_0call_0msg_send_6month"));
			active_silence_stats_map.put("silence_day_0call_active_ratio_6month",active_silence_stats_obj.getInteger("silence_day_0call_active_6month")/180 );
			
			yysMap.put("active_silence_stats",active_silence_stats_map);
			
			
			//时间段通话统计
			JSONObject call_duration_stats_2hour_obj = report_data_obj.getJSONObject("call_duration_stats_2hour");		
			yysMap.put("call_duration_stats_2hour",call_duration_stats_2hour_obj);				

			
			String content = JSONUtil.toJson(yysMap);			
			return 	content;
			
		} catch (NullPointerException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}
		return "";	
	}
	private String taobao(RcSjmh sjmh) {
		// 淘宝
		try {
			String sjmhData = FileUtils.readFileToString(new File(Global.getConfig("uploadPath")+sjmh.getDataPath()),"utf-8");
			
			JSONObject task_data_obj = JSONObject.parseObject(sjmhData);		
			JSONObject base_info_obj = task_data_obj.getJSONObject("base_info");
			JSONObject account_info_obj = task_data_obj.getJSONObject("account_info");
			JSONArray receiver_list_arr = task_data_obj.getJSONArray("receiver_list");
			JSONArray order_list_arr = task_data_obj.getJSONArray("order_list");
	
			Integer act_bal = account_info_obj.getInteger("account_balance");				
			Integer fin_act_bal = account_info_obj.getInteger("financial_account_balance");
			Integer credit_quota = account_info_obj.getInteger("credit_quota");
			Integer jiebei_quota = account_info_obj.getInteger("jiebei_quota");
			act_bal = act_bal==null?0: act_bal/ 100;
			fin_act_bal = fin_act_bal==null?0: fin_act_bal/ 100;				
			credit_quota = credit_quota==null?0: credit_quota/ 100;
			jiebei_quota = jiebei_quota==null?0: jiebei_quota/ 100;
		
			String default_receive_addr = ""; // 淘宝默认地址
			for(int i = 0 ;i<receiver_list_arr.size();i++){  
				JSONObject receiver_obj = receiver_list_arr.getJSONObject(i);
				if(StringUtils.equals(receiver_obj.getString("default"), "1")) {
					default_receive_addr = receiver_obj.getString("area");
				}
			}
			if (receiver_list_arr.size()>0&&StringUtils.isBlank(default_receive_addr)) {
				default_receive_addr = receiver_list_arr.getJSONObject(0).getString("area");
			}
			int count3M = 0; // 三个月内订单数量
			int sumAmt3M = 0; // 三个月内订单金额

			int count6M = 0; // 六个月内订单数量
			int sumAmt6M = 0; // 六个月内订单金额

						
			int maxPrice3M = 0; // 三个月最贵
			String maxPriceProductName3M = "";
			int maxPrice6M = 0; // 六个月最贵
			String maxPriceProductName6M = "";

			
			for(int i = 0 ;i<order_list_arr.size();i++){  
				JSONObject order_obj = order_list_arr.getJSONObject(i);
				String order_status = order_obj.getString("order_status");
				if(!StringUtils.equals(order_status, "交易成功")) {
					continue;
				}
				Integer order_amount = order_obj.getInteger("order_amount");
				String order_time = order_obj.getString("order_time");
				JSONArray product_list_arr = order_obj.getJSONArray("product_list");

				long pastDays = DateUtils.pastDays(DateUtils.parse(order_time));
				if(pastDays<=90) {
					sumAmt3M = sumAmt3M + order_amount;
					count3M++;	
					for(int j = 0;j<product_list_arr.size();j++){  
						JSONObject product_obj = product_list_arr.getJSONObject(j);
						Integer product_price = product_obj.getInteger("product_price");
						String product_name = order_obj.getString("product_name");
						if (product_price > maxPrice3M) {
							maxPrice3M= product_price;
							maxPriceProductName3M = product_name;
						}	
					}

				}
				if(pastDays<=180) {
					sumAmt6M = sumAmt6M + order_amount;
					count6M++;
					for(int j = 0;j<product_list_arr.size();j++){  
						JSONObject product_obj = product_list_arr.getJSONObject(j);
						Integer product_price = product_obj.getInteger("product_price");
						String product_name = order_obj.getString("product_name");
						if (product_price > maxPrice6M) {
							maxPrice6M= product_price;
							maxPriceProductName6M = product_name;
						}	
					}
				}				
			}
			
			Map<String, Object> taobaoMap = new HashMap<String, Object>();

			taobaoMap.put("real_name", base_info_obj.get("real_name"));
			taobaoMap.put("phoneNo", base_info_obj.getString("mobile"));
			taobaoMap.put("user_level", base_info_obj.get("user_level"));
			taobaoMap.put("vip_count", base_info_obj.get("vip_count"));
			taobaoMap.put("zmf", account_info_obj.get("zhima_point"));
			taobaoMap.put("jiebei_quota", jiebei_quota);
			taobaoMap.put("credit_quota", credit_quota);
			taobaoMap.put("fin_act_bal", fin_act_bal);
			
			taobaoMap.put("count3M", count3M);
			taobaoMap.put("sumAmt3M", sumAmt3M);
			taobaoMap.put("avgAmt3M", sumAmt3M/3);
			
			taobaoMap.put("count6M", count6M);
			taobaoMap.put("sumAmt6M", sumAmt6M);
			taobaoMap.put("avgAmt6M", sumAmt6M/6);
			
			
			taobaoMap.put("maxPrice3M", maxPrice3M);
			taobaoMap.put("maxPriceProductName3M", maxPriceProductName3M);
			taobaoMap.put("maxPrice6M", maxPrice6M);
			taobaoMap.put("maxPriceProductName6M", maxPriceProductName6M);
			
			taobaoMap.put("default_receive_addr", default_receive_addr);
			
			
			String content = JSONUtil.toJson(taobaoMap);			
			return 	content;

		} catch (NullPointerException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}
		return "";
	}

}