package com.jxf.task.tasks;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.rc.entity.RcCaDataV2;
import com.jxf.rc.entity.RcSjmh;
import com.jxf.rc.service.RcCaDataServiceV2;
import com.jxf.rc.service.RcCaYysDetailsService;
import com.jxf.rc.service.RcSjmhService;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.JSONUtil;
import com.jxf.svc.utils.ObjectUtils;

@DisallowConcurrentExecution
public class SjmhV2Task implements Job {


	private static Logger logger = LoggerFactory.getLogger(SjmhV2Task.class);

	@Autowired
	private RcSjmhService rcSjmhService;
	@Autowired
	private RcCaYysDetailsService rcCaYysDetailsService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberExtendService memberExtendService;
	@Autowired
	private RcCaDataServiceV2 rcCaDataServiceV2;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		data_proc(); 
	}
	
	private void data_proc() {
    	RcSjmh rcSjmh = new RcSjmh();
    	rcSjmh.setProdType(RcSjmh.ProdType.app);
    	rcSjmh.setDataStatus(RcSjmh.DataStatus.data_created);
    	List<RcSjmh> rcSjmhList = rcSjmhService.findList(rcSjmh);
    	
       for(RcSjmh sjmh:rcSjmhList) {
			try {
	
				String taskData = FileUtils.readFileToString(new File(Global.getConfig("uploadPath")+sjmh.getDataPath()),"utf-8");
				sjmh.setTaskData(taskData);
				switch(sjmh.getChannelType()){
			    case shebao :
			    	shebao(sjmh);
			       break; 
			    case gongjijin :
			    	gongjijin(sjmh);
			       break; 
			    case taobao :
			    	taobao(sjmh);
			       break; 
			    case yunyingshang :
			    	yunyingshang(sjmh);
			       break;
			    case xuexinwang :
			    	xuexinwang(sjmh);
			       break;
			    case wangyin :
			    	wangyin(sjmh);
			       break;
			    default : 
			       break;
		        }

			} catch (IOException e) {
				logger.error(Exceptions.getStackTraceAsString(e));
				continue;
			}	

		}
	}

	
	private void shebao(RcSjmh sjmh) {
		try {
			//社保
			Member	member = memberService.get(sjmh.getOrgId());
			RcCaDataV2 caDataV2 = new RcCaDataV2();
			caDataV2.setPhoneNo(member.getUsername());
			caDataV2.setType(RcCaDataV2.Type.shebao);
			caDataV2.setStatus(RcCaDataV2.Status.processing);
			List<RcCaDataV2> caDataList =  rcCaDataServiceV2.findList(caDataV2);
			RcCaDataV2 caData = null;
			if(!Collections3.isEmpty(caDataList)) {
				caData = caDataList.get(0);
			}else {
				caData = new RcCaDataV2();
			}
			caData.setContent(sjmh.getTaskData());
			caData.setPhoneNo(member.getUsername());
			caData.setIdNo(member.getIdNo());
			caData.setName(member.getName());
			caData.setType(RcCaDataV2.Type.shebao);
			caData.setProvider(RcCaDataV2.Provider.sjmh);
			caData.setStatus(RcCaDataV2.Status.success);
			rcCaDataServiceV2.save(caData);
			
			sjmh.setDataStatus(RcSjmh.DataStatus.data_arranged);
			rcSjmhService.save(sjmh);
			
			// 认证成功后添加社保认证项
	
			int verifiedList=member.getVerifiedList();
			verifiedList = VerifiedUtils.addVerified(verifiedList,9);
			member.setVerifiedList(verifiedList);	
			RedisUtils.put("memberInfo"+member.getId(), "shebaoStatus", "1");
			memberService.save(member);
		
		} catch (NullPointerException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}
	}
	
	
	private void gongjijin(RcSjmh sjmh) {
		try {
			//公积金
			Member	member = memberService.get(sjmh.getOrgId());
			RcCaDataV2 caDataV2 = new RcCaDataV2();
			caDataV2.setPhoneNo(member.getUsername());
			caDataV2.setType(RcCaDataV2.Type.gjj);
			caDataV2.setStatus(RcCaDataV2.Status.processing);
			List<RcCaDataV2> caDataList =  rcCaDataServiceV2.findList(caDataV2);
			RcCaDataV2 caData = null;
			if(!Collections3.isEmpty(caDataList)) {
				caData = caDataList.get(0);
			}else {
				caData = new RcCaDataV2();
			}
			caData.setContent(sjmh.getTaskData());
			caData.setPhoneNo(member.getUsername());
			caData.setIdNo(member.getIdNo());
			caData.setName(member.getName());
			caData.setType(RcCaDataV2.Type.gjj);
			caData.setProvider(RcCaDataV2.Provider.sjmh);
			caData.setStatus(RcCaDataV2.Status.success);
			rcCaDataServiceV2.save(caData);
			
			sjmh.setDataStatus(RcSjmh.DataStatus.data_arranged);
			rcSjmhService.save(sjmh);
			
			// 认证成功后添加公积金认证项
			
			int verifiedList=member.getVerifiedList();
			verifiedList = VerifiedUtils.addVerified(verifiedList,10);
			member.setVerifiedList(verifiedList);	
			RedisUtils.put("memberInfo"+member.getId(), "gongjijingStatus", "1");
			memberService.save(member);
		
		} catch (NullPointerException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}
	}
	



	
	private void xuexinwang(RcSjmh sjmh) {
		// 学信网
		try {
			JSONObject task_data_obj = JSONObject.parseObject(sjmh.getTaskData());
			Member member = memberService.get(sjmh.getOrgId());
			RcCaDataV2 caDataV2 = new RcCaDataV2();
			caDataV2.setPhoneNo(member.getUsername());
			caDataV2.setType(RcCaDataV2.Type.xxw);
			caDataV2.setStatus(RcCaDataV2.Status.processing);
			List<RcCaDataV2> caDataList =  rcCaDataServiceV2.findList(caDataV2);
			RcCaDataV2 caData = null;
			if(!Collections3.isEmpty(caDataList)) {
				caData = caDataList.get(0);
			}else {
				caData = new RcCaDataV2();
			}
			caData.setContent(task_data_obj.getString("school_info"));
			caData.setPhoneNo(member.getUsername());
			caData.setIdNo(member.getIdNo());
			caData.setName(member.getName());
			caData.setType(RcCaDataV2.Type.xxw);
			caData.setProvider(RcCaDataV2.Provider.sjmh);
			caData.setStatus(RcCaDataV2.Status.success);
			rcCaDataServiceV2.save(caData);
			
			sjmh.setDataStatus(RcSjmh.DataStatus.data_arranged);
			rcSjmhService.save(sjmh);
			
			// 认证成功后添加学信网认证项
			
			int verifiedList=member.getVerifiedList();
			verifiedList = VerifiedUtils.addVerified(verifiedList,8);
			member.setVerifiedList(verifiedList);	
			RedisUtils.put("memberInfo"+member.getId(), "xuexingwangStatus", "1");
			memberService.save(member);
	
		} catch (NullPointerException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}
	}
	
	private void wangyin(RcSjmh sjmh) {
	try {
		// 银行卡账单
		JSONObject task_data_obj = JSONObject.parseObject(sjmh.getTaskData());		
		JSONArray debit_card_accounts_arr = task_data_obj.getJSONArray("debit_card_accounts");
		JSONArray credit_card_accounts_arr = task_data_obj.getJSONArray("credit_card_accounts");
			
			List<Map<String, Object>> listjie = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> listxin = new ArrayList<Map<String, Object>>();
			// 整理借记卡数据

			for (int i = 0; i < debit_card_accounts_arr.size(); i++) {
				Map<String, Object> debit_card_map = new HashMap<String, Object>();
				JSONObject debit_card_accounts_obj = debit_card_accounts_arr.getJSONObject(i);
				debit_card_map.put("name", debit_card_accounts_obj.getString("name"));// 名字
				debit_card_map.put("deposit_bank", debit_card_accounts_obj.getString("deposit_bank")); // 开户行
				debit_card_map.put("status", debit_card_accounts_obj.getString("status"));// 卡状态
				debit_card_map.put("account", debit_card_accounts_obj.getString("account").substring(debit_card_accounts_obj.getString("account").length()-4));// 卡状态

				int total_inc = 0;
				int total_out = 0;
				int xinMin = 0;
				int xinMax = 0;
				int total_balance = 0;
				int total_avabalance = 0;

				JSONArray sub_accounts_arr = debit_card_accounts_obj.getJSONArray("sub_accounts");

				for (int j = 0; j < sub_accounts_arr.size(); j++) {
					
					JSONObject sub_account_obj = sub_accounts_arr.getJSONObject(j);
					Integer available_balance = ObjectUtils.isNotBlank(sub_account_obj.getInteger("available_balance"))?sub_account_obj.getInteger("available_balance"):0;
					Integer balance =  ObjectUtils.isNotBlank(sub_account_obj.getInteger("balance"))?sub_account_obj.getInteger("balance"):0;
					
					total_avabalance = total_avabalance + available_balance;					
					total_balance = total_balance + balance;
					JSONArray account_detail_arr = sub_account_obj.getJSONArray("account_detail");

					for (int k = 0; k < account_detail_arr.size(); k++) {
						  JSONObject account_detail_obj = account_detail_arr.getJSONObject(k);
						  Integer incom  = account_detail_obj.getInteger("income");
						  Integer oncom =  account_detail_obj.getInteger("outcome");		
						  total_inc = total_inc + (incom==null?0:incom);							
						  total_out = total_out + (oncom==null?0:oncom);
					}				

				}

				total_inc = total_inc / 100 / 6;
				total_out = total_out / 100 / 6;
				total_balance = total_balance / 100;
				total_avabalance = total_avabalance / 100;
				xinMin = (int) (total_inc * 0.3);
				xinMax = (int) (total_inc * 0.5);
				debit_card_map.put("balance", total_balance);
				debit_card_map.put("avabalance", total_avabalance);
				debit_card_map.put("inc", total_inc);
				debit_card_map.put("out", total_out);
				debit_card_map.put("xinMin", xinMin);
				debit_card_map.put("xinMax", xinMax);
				listjie.add(debit_card_map);
			}
	
			// 信用卡
			for (int i = 0; i < credit_card_accounts_arr.size(); i++) {
				Map<String, Object> credit_card_map = new HashMap<String, Object>();
				JSONObject obj_obj = credit_card_accounts_arr.getJSONObject(i);
				credit_card_map.put("name", obj_obj.getString("name"));// 名字
				credit_card_map.put("deposit_bank", obj_obj.getString("deposit_bank")); // 开户行
				credit_card_map.put("account", obj_obj.getString("account").substring(obj_obj.getString("account").length()-4));
				int total_inc = 0; // 账单
				int total_out = 0; // 还款
				int total_balance = 0; // 信用额度
				int total_avabalance = 0; // 可用额度
				int threrover = 0; // 三个月
				int sixover = 0; // 六个月
				JSONArray sub_accounts_arr = obj_obj.getJSONArray("sub_accounts");

				for (int j = 0; j < sub_accounts_arr.size(); j++) {
					JSONObject sub_account_obj = sub_accounts_arr.getJSONObject(j);
					Integer available_quota = sub_account_obj.getInteger("available_quota");
					Integer credit_quota = sub_account_obj.getInteger("credit_quota");
					total_avabalance = total_avabalance + (available_quota==null?0:available_quota);
					total_balance = total_balance + (credit_quota==null?0:credit_quota);

					JSONArray settled_bills_arr = obj_obj.getJSONArray("settled_bills");
                    if(ObjectUtils.isNotBlank(settled_bills_arr)) {
    					for (int k = 0; k < settled_bills_arr.size(); k++) {
    						JSONObject settled_bills_obj = settled_bills_arr.getJSONObject(k);
    						Integer incom = settled_bills_obj.getInteger("income");
    						Integer oncom = settled_bills_obj.getInteger("outcome");
    						total_inc = total_inc + (incom==null?0:incom);			
    						total_out = total_out + (oncom==null?0:oncom);
    						// 6各月逾期数
    						String overdue_status = settled_bills_obj.getString("overdue_status");
    						if (StringUtils.equals(overdue_status, "3")) {
    							sixover = sixover + 1;
    						}
    					}
    					int count = settled_bills_arr.size();
    					if (count > 3) {
    						count = count- 3;
    					}
    					for (int k = 0; k < count; k++) {
    						JSONObject settled_bills_obj = settled_bills_arr.getJSONObject(k);
    						String overdue_status = settled_bills_obj.getString("overdue_status");
    						if (StringUtils.equals(overdue_status, "3")) {
    							threrover = threrover + 1;
    						}

    					}
                    }

				}

				total_inc = total_inc / 100 / 6;
				total_out = total_out / 100 / 6;
				total_balance = total_balance / 100;
				total_avabalance = total_avabalance / 100;
				credit_card_map.put("balance", total_balance);
				credit_card_map.put("avabalance", total_avabalance);
				credit_card_map.put("inc", total_inc);
				credit_card_map.put("out", total_out);
				credit_card_map.put("threrover", threrover);
				credit_card_map.put("sixover", sixover);
				listxin.add(credit_card_map);
			}

			Map<String, Object> stMap = new HashMap<String, Object>();
			stMap.put("listjie", listjie);
			stMap.put("listxin", listxin);
			String content = JSONUtil.toJson(stMap);
			Member	member = memberService.get(sjmh.getOrgId());
			RcCaDataV2 caDataV2 = new RcCaDataV2();
			caDataV2.setPhoneNo(member.getUsername());
			caDataV2.setType(RcCaDataV2.Type.wangyin);
			caDataV2.setStatus(RcCaDataV2.Status.processing);
			List<RcCaDataV2> caDataList =  rcCaDataServiceV2.findList(caDataV2);
			RcCaDataV2 caData = null;
			if(!Collections3.isEmpty(caDataList)) {
				caData = caDataList.get(0);
			}else {
				caData = new RcCaDataV2();
			}
			caData.setContent(content);
			caData.setPhoneNo(member.getUsername());
			caData.setIdNo(member.getIdNo());
			caData.setName(member.getName());
			caData.setType(RcCaDataV2.Type.wangyin);
			caData.setProvider(RcCaDataV2.Provider.sjmh);
			caData.setStatus(RcCaDataV2.Status.success);
			rcCaDataServiceV2.save(caData);

			sjmh.setDataStatus(RcSjmh.DataStatus.data_arranged);
			rcSjmhService.save(sjmh);
			
			// 认证成功后添加银行账单认证项
			int verifiedList=member.getVerifiedList();
			verifiedList = VerifiedUtils.addVerified(verifiedList,7);
			member.setVerifiedList(verifiedList);	
			RedisUtils.put("memberInfo"+member.getId(), "bankTrxStatus", "1");
			memberService.save(member);

		} catch (NullPointerException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}
	}
	
	private void yunyingshang(RcSjmh sjmh) {
		try {
			Map<String, Object> yysMap = new HashMap<String, Object>();
//			String sjmhData = FileUtils.readFileToString(new File(Global.getConfig("uploadPath")+sjmh.getDataPath()),"utf-8");
			String sjmhReportData = FileUtils.readFileToString(new File(Global.getConfig("uploadPath")+sjmh.getReportPath()),"utf-8");
//			JSONObject task_data_obj = JSONObject.parseObject(sjmhData);
			JSONObject report_data_obj = JSONObject.parseObject(sjmhReportData);
//			JSONObject base_info_obj = task_data_obj.getJSONObject("base_info");
//			JSONObject account_info_obj = task_data_obj.getJSONObject("account_info");
//			JSONArray call_info_arr = task_data_obj.getJSONArray("call_info");
			
			DecimalFormat df = new DecimalFormat("#.00");
			
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
						emergency_contact_stats_map.put("ecp1", memberExtend.getEcp1());
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
						emergency_contact_stats_map.put("ecp2", memberExtend.getEcp1());
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
						emergency_contact_stats_map.put("ecp3", memberExtend.getEcp1());
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
				Map<String, Object> tempMap = new HashMap<String, Object>();
				int n = 0;//1 位包含ecp1 2位包含ecp2 3位包含ecp3
				for (int i = 0; i < emergency_contact_stats.size(); i++) {
					Map<String, Object> map = (Map<String, Object>) emergency_contact_stats.get(i);
					if(map.containsKey("ecp1")) {
						n = n|(1<< 1);
					}else if(map.containsKey("ecp2")) {
						n = n|(1<< 2);
					}else if(map.containsKey("ecp3")) {
						n = n|(1<< 3);
					}
				}
				if(!((n&(1<< 1))==(1<< 1))) {//不包含  ecp1
					tempMap.put("ecp1", memberExtend.getEcp1());
					tempMap.put("contact_number", 0);
					tempMap.put("call_time_1month", 0);
					tempMap.put("call_time_3month", 0);
					tempMap.put("call_time_6month", 0);
					
					tempMap.put("call_count_1month", 0);
					tempMap.put("call_count_3month", 0);
					tempMap.put("call_count_6month", 0);	
					emergency_contact_stats.add(tempMap);
				}
				if(!((n&(1<< 2))==(1<< 2))) {//不包含  ecp2
					tempMap.put("ecp2", memberExtend.getEcp2());
					tempMap.put("contact_number", 0);
					tempMap.put("call_time_1month", 0);
					tempMap.put("call_time_3month", 0);
					tempMap.put("call_time_6month", 0);
					
					tempMap.put("call_count_1month", 0);
					tempMap.put("call_count_3month", 0);
					tempMap.put("call_count_6month", 0);	
					emergency_contact_stats.add(tempMap);
				}
				if(!((n&(1<< 3))==(1<< 3))) {//不包含  ecp3
					tempMap.put("ecp3", memberExtend.getEcp3());
					tempMap.put("contact_number", 0);
					tempMap.put("call_time_1month", 0);
					tempMap.put("call_time_3month", 0);
					tempMap.put("call_time_6month", 0);
					
					tempMap.put("call_count_1month", 0);
					tempMap.put("call_count_3month", 0);
					tempMap.put("call_count_6month", 0);	
					emergency_contact_stats.add(tempMap);
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
			
			consumption_stats_map.put("recharge_amount_3month", consumption_stats_obj.getInteger("recharge_amount_3month")/100);
			consumption_stats_map.put("recharge_count_3month", consumption_stats_obj.getInteger("recharge_count_3month"));
			consumption_stats_map.put("consume_amount_3month", consumption_stats_obj.getInteger("consume_amount_3month")/100);
			
			consumption_stats_map.put("recharge_amount_6month", consumption_stats_obj.getInteger("recharge_amount_6month")/100);
			consumption_stats_map.put("recharge_count_6month", consumption_stats_obj.getInteger("recharge_count_6month"));
			consumption_stats_map.put("consume_amount_6month", consumption_stats_obj.getInteger("consume_amount_6month")/100);
			
			yysMap.put("consumption_stats",consumption_stats_map);
			//静默活跃统计
			Map<String, Object> active_silence_stats_map = new HashMap<String, Object>();
			JSONObject active_silence_stats_obj = report_data_obj.getJSONObject("active_silence_stats");
			
			active_silence_stats_map.put("active_day_1call_3month", active_silence_stats_obj.get("active_day_1call_3month"));
			active_silence_stats_map.put("max_continue_active_day_1call_3month", active_silence_stats_obj.get("max_continue_active_day_1call_3month"));
			active_silence_stats_map.put("silence_day_0call_3month", active_silence_stats_obj.get("silence_day_0call_3month"));
			active_silence_stats_map.put("continue_silence_day_over3_0call_0msg_send_3month", active_silence_stats_obj.get("continue_silence_day_over3_0call_0msg_send_3month"));
			
			active_silence_stats_map.put("silence_day_0call_ratio_3month",df.format(active_silence_stats_obj.getDouble("silence_day_0call_3month")*100/90 ));
			active_silence_stats_map.put("silence_day_0call_active_ratio_3month",df.format(active_silence_stats_obj.getDouble("silence_day_0call_active_3month")*100/90 ));
			
			active_silence_stats_map.put("active_day_1call_6month", active_silence_stats_obj.get("active_day_1call_6month"));
			active_silence_stats_map.put("max_continue_active_day_1call_6month", active_silence_stats_obj.get("max_continue_active_day_1call_6month"));
			active_silence_stats_map.put("silence_day_0call_6month", active_silence_stats_obj.get("silence_day_0call_6month"));
			active_silence_stats_map.put("continue_silence_day_over3_0call_0msg_send_6month", active_silence_stats_obj.get("continue_silence_day_over3_0call_0msg_send_6month"));
			
			
			active_silence_stats_map.put("silence_day_0call_ratio_6month",df.format(active_silence_stats_obj.getDouble("silence_day_0call_6month")*100/180 ));
			active_silence_stats_map.put("silence_day_0call_active_ratio_6month",df.format(active_silence_stats_obj.getDouble("silence_day_0call_active_6month")*100/180) );
			
			yysMap.put("active_silence_stats",active_silence_stats_map);
			
			
			//时间段通话统计
			JSONObject call_duration_stats_2hour_obj = report_data_obj.getJSONObject("call_duration_stats_2hour");		
			yysMap.put("call_duration_stats_2hour",call_duration_stats_2hour_obj);				
			String content = JSONUtil.toJson(yysMap);	
			
			Member member2 = memberService.get(sjmh.getOrgId());
			RcCaDataV2 caDataV2 = new RcCaDataV2();
			caDataV2.setPhoneNo(member2.getUsername());
			caDataV2.setType(RcCaDataV2.Type.yys);
			caDataV2.setStatus(RcCaDataV2.Status.processing);
			List<RcCaDataV2> caDataList =  rcCaDataServiceV2.findList(caDataV2);
			RcCaDataV2 caData = null;
			if(!Collections3.isEmpty(caDataList)) {
				caData = caDataList.get(0);
			}else {
				caData = new RcCaDataV2();
			}
			caData.setContent(content);
			caData.setPhoneNo(member2.getUsername());
			caData.setIdNo(member2.getIdNo());
			caData.setName(member2.getName());
			caData.setType(RcCaDataV2.Type.yys);
			caData.setProvider(RcCaDataV2.Provider.sjmh);
			caData.setStatus(RcCaDataV2.Status.success);
			rcCaDataServiceV2.save(caData);
			
			sjmh.setDataStatus(RcSjmh.DataStatus.data_arranged);
			rcSjmhService.save(sjmh);
			
			// 认证成功后添加社保认证项
	
			int verifiedList=member.getVerifiedList();
			verifiedList = VerifiedUtils.addVerified(verifiedList,6);
			member.setVerifiedList(verifiedList);	
			RedisUtils.put("memberInfo"+member.getId(), "yysStatus", "1");
			memberService.save(member);
			
		} catch (NullPointerException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}
	}
	private void taobao(RcSjmh sjmh) {
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
			
			Member member = memberService.get(sjmh.getOrgId());
			RcCaDataV2 caDataV2 = new RcCaDataV2();
			caDataV2.setPhoneNo(member.getUsername());
			caDataV2.setType(RcCaDataV2.Type.taobao);
			caDataV2.setStatus(RcCaDataV2.Status.processing);
			List<RcCaDataV2> caDataList =  rcCaDataServiceV2.findList(caDataV2);
			RcCaDataV2 caData = null;
			if(!Collections3.isEmpty(caDataList)) {
				caData = caDataList.get(0);
			}else {
				caData = new RcCaDataV2();
			}
			caData.setContent(content);
			caData.setPhoneNo(member.getUsername());
			caData.setIdNo(member.getIdNo());
			caData.setName(member.getName());
			caData.setType(RcCaDataV2.Type.taobao);
			caData.setProvider(RcCaDataV2.Provider.sjmh);
			caData.setStatus(RcCaDataV2.Status.success);
			rcCaDataServiceV2.save(caData);
			
			sjmh.setDataStatus(RcSjmh.DataStatus.data_arranged);
			rcSjmhService.save(sjmh);
			
			// 认证成功后添加社保认证项
	
			int verifiedList=member.getVerifiedList();
			verifiedList = VerifiedUtils.addVerified(verifiedList,5);
			member.setVerifiedList(verifiedList);	
			RedisUtils.put("memberInfo"+member.getId(), "taobaoStatus", "1");
			memberService.save(member);

		} catch (NullPointerException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}
	}
}
