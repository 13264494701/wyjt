package com.jxf.task.tasks;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.rc.entity.RcCaData;

import com.jxf.rc.entity.RcCaYysDetails;
import com.jxf.rc.entity.RcSjmh;
import com.jxf.rc.service.RcCaDataService;

import com.jxf.rc.service.RcCaYysDetailsService;
import com.jxf.rc.service.RcSjmhService;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.JSONUtil;
import com.jxf.svc.utils.ObjectUtils;

@DisallowConcurrentExecution
public class SjmhTask implements Job {

	private static Log log = LogFactory.getLog(SjmhTask.class);
	@Autowired
	private RcCaDataService rcCaDataService;
	@Autowired
	private RcSjmhService rcSjmhService;
	@Autowired
	private RcCaYysDetailsService rcCaYysDetailsService;
	
	@Autowired
	private MemberService memberService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		data_proc(); 
		report_proc();
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
				log.error(Exceptions.getStackTraceAsString(e));
				continue;
			}	

		}
	}
	
	private void report_proc() {
		
	 	RcSjmh rcSjmh = new RcSjmh();
    	rcSjmh.setProdType(RcSjmh.ProdType.app);
    	rcSjmh.setChannelType(RcSjmh.ChannelType.yunyingshang);
    	rcSjmh.setReportStatus(RcSjmh.ReportStatus.report_created);
    	List<RcSjmh> rcSjmhList = rcSjmhService.findList(rcSjmh);
    	
        for(RcSjmh sjmh:rcSjmhList) {
			try {
	
				String reportData = FileUtils.readFileToString(new File(Global.getConfig("uploadPath")+sjmh.getReportPath()),"utf-8");
				sjmh.setReportData(reportData);
				switch(sjmh.getChannelType()){
			    case yunyingshang :
			    	yunyingshang_report(sjmh);
			       break;
			    default : 
			       break;
		   }

			} catch (IOException e) {
				log.error(Exceptions.getStackTraceAsString(e));
				continue;
			}	

		}
	}
	
	private void shebao(RcSjmh sjmh) {
		try {
			//社保
			Member	member = memberService.get(sjmh.getOrgId());
			RcCaData caData = new RcCaData();
			caData.setContent(sjmh.getTaskData());
			caData.setPhoneNo(member.getUsername());
			caData.setIdNo(member.getIdNo());
			caData.setName(member.getName());
			caData.setType(RcCaData.Type.shebao);
			caData.setProvider(RcCaData.Provider.sjmh);
			rcCaDataService.save(caData);
			
			sjmh.setDataStatus(RcSjmh.DataStatus.data_arranged);
			rcSjmhService.save(sjmh);
			
			// 认证成功后添加社保认证项
	
			int verifiedList=member.getVerifiedList();
			verifiedList = VerifiedUtils.addVerified(verifiedList,9);
			member.setVerifiedList(verifiedList);	
			RedisUtils.put("memberInfo"+member.getId(), "shebaoStatus", "1");
			memberService.save(member);
		
		} catch (NullPointerException e) {
			log.error(Exceptions.getStackTraceAsString(e));
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
	}
	
	
	private void gongjijin(RcSjmh sjmh) {
		try {
			//公积金
			Member	member = memberService.get(sjmh.getOrgId());
			RcCaData caData = new RcCaData();
			caData.setContent(sjmh.getTaskData());
			caData.setPhoneNo(member.getUsername());
			caData.setIdNo(member.getIdNo());
			caData.setName(member.getName());
			caData.setType(RcCaData.Type.gongjijin);
			caData.setProvider(RcCaData.Provider.sjmh);
			rcCaDataService.save(caData);
			
			sjmh.setDataStatus(RcSjmh.DataStatus.data_arranged);
			rcSjmhService.save(sjmh);
			
			// 认证成功后添加公积金认证项
			
			int verifiedList=member.getVerifiedList();
			verifiedList = VerifiedUtils.addVerified(verifiedList,10);
			member.setVerifiedList(verifiedList);	
			RedisUtils.put("memberInfo"+member.getId(), "gongjijingStatus", "1");
			memberService.save(member);
		
		} catch (NullPointerException e) {
			log.error(Exceptions.getStackTraceAsString(e));
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
	}
	
	private void taobao(RcSjmh sjmh) {
		// 淘宝
		try {

			JSONObject task_data_obj = JSONObject.parseObject(sjmh.getTaskData());		
			JSONObject base_info_obj = task_data_obj.getJSONObject("base_info");
			JSONObject account_info_obj = task_data_obj.getJSONObject("account_info");
			JSONArray receiver_list_arr = task_data_obj.getJSONArray("receiver_list");
			JSONArray order_list_arr = task_data_obj.getJSONArray("order_list");
			
			String username = base_info_obj.getString("mobile");
	
			int tbYue1 = 0;
			int tbYEbao1 = 0;
			int tbHuabei1 = 0;
	

			Integer tbYue = account_info_obj.getInteger("account_balance");				
			Integer tbYEbao = account_info_obj.getInteger("financial_account_balance");
			Integer tbHuabei = account_info_obj.getInteger("credit_quota");
			tbYue1 = tbYue==null?0: tbYue/ 100;
			tbYEbao1 = tbYEbao==null?0: tbYEbao/ 100;				
			tbHuabei1 = tbHuabei==null?0: tbHuabei/ 100;
		
			String taoadree = ""; // 淘宝默认地址

			for(int i = 0 ;i<receiver_list_arr.size();i++){  
				JSONObject receiver_obj = receiver_list_arr.getJSONObject(i);
				if(StringUtils.equals(receiver_obj.getString("default"), "1")) {
					taoadree = receiver_obj.getString("area");
				}
			}
			if (receiver_list_arr.size()>0&&StringUtils.isBlank(taoadree)) {
				taoadree = receiver_list_arr.getJSONObject(0).getString("area");
			}

			int sumAmount = 0; // 半年内订单金额
			int minAmount = 0; // 半年最便宜
			int maxAmount = 0; // 半年内最贵
			int count = 0; // 月购买件数
			
			for(int i = 0 ;i<order_list_arr.size();i++){  
				JSONObject obj_obj = order_list_arr.getJSONObject(i);
				Integer order_amount = obj_obj.getInteger("order_amount");
				sumAmount = sumAmount + order_amount;
				count++;
				if (order_amount > maxAmount) {
					maxAmount= order_amount;
				}
				if (order_amount < minAmount) {
					minAmount = order_amount;
				}
			}
				
			Map<String, Object> newTB = new HashMap<String, Object>();
			newTB.put("tbYue", tbYue1);
			newTB.put("tbYEbao", tbYEbao1);
			newTB.put("tbHuabei", tbHuabei1);
			newTB.put("taoadree", taoadree);
			newTB.put("money", sumAmount / 100);
			newTB.put("minmoney", minAmount / 100);
			newTB.put("maxmoney", maxAmount / 100);
			newTB.put("yuemoney", sumAmount / 6 / 100);
			newTB.put("yueNum", count / 6);
			newTB.put("username", username);
			String content = JSONUtil.toJson(newTB);
			Member	member = memberService.get(sjmh.getOrgId());
			RcCaData caData = new RcCaData();
			caData.setContent(content);
			caData.setPhoneNo(member.getUsername());
			caData.setIdNo(member.getIdNo());
			caData.setName(member.getName());
			caData.setType(RcCaData.Type.taobao);	
			caData.setProvider(RcCaData.Provider.sjmh);
			rcCaDataService.save(caData);
			
			sjmh.setDataStatus(RcSjmh.DataStatus.data_arranged);
			rcSjmhService.save(sjmh);
			
									
			// 认证成功后添加淘宝认证项
			
			int verifiedList=member.getVerifiedList();
			verifiedList = VerifiedUtils.addVerified(verifiedList,5);
			member.setVerifiedList(verifiedList);
			RedisUtils.put("memberInfo"+member.getId(), "taobaoStatus", "1");
			memberService.save(member);
			

		} catch (NullPointerException e) {
			log.error(Exceptions.getStackTraceAsString(e));
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
	}

	private void yunyingshang_report(RcSjmh sjmh) {
		// 运营商报告
		try {
			String behavior_score = "0";
			String black_top10_contact_total_count_ratio = "0";
			String manyheads_top10_contact_recent6month_have_partnercode_count = "0"; // 平台人数
			String manyheads_top10_contact_recent6month_partnercode_count_avg = "0"; // 平台
	
			Map<String, Object> ca_data_map = new HashMap<String, Object>();
			JSONObject report_data_obj = JSONObject.parseObject(sjmh.getReportData());
			
			// 综合分数
			if (ObjectUtils.isNotBlank(report_data_obj.get("behavior_score"))) {
				JSONObject behavior_score_obj = report_data_obj.getJSONObject("behavior_score");
				behavior_score = behavior_score_obj.getString("total_score");
			}
			ca_data_map.put("behavior_score", behavior_score);

			if (ObjectUtils.isNotBlank(report_data_obj.get("contact_blacklist_analysis"))) {
				JSONObject contact_blacklist_analysis_obj = report_data_obj.getJSONObject("contact_blacklist_analysis");
				black_top10_contact_total_count_ratio = ObjectUtils.isNotBlank(contact_blacklist_analysis_obj.getString("black_top10_contact_total_count_ratio"))?contact_blacklist_analysis_obj.getString("black_top10_contact_total_count_ratio"):"0";
			
			}
			// 平台数
			ca_data_map.put("black_top10_contact_total_count_ratio", black_top10_contact_total_count_ratio);
			if (ObjectUtils.isNotBlank(report_data_obj.get("contact_manyheads_analysis"))) {
				JSONObject contact_manyheads_analysis_obj = report_data_obj.getJSONObject("contact_manyheads_analysis");
				manyheads_top10_contact_recent6month_partnercode_count_avg = ObjectUtils.isNotBlank(contact_manyheads_analysis_obj.getString("manyheads_top10_contact_recent6month_partnercode_count_avg"))?contact_manyheads_analysis_obj.getString("manyheads_top10_contact_recent6month_partnercode_count_avg"):"0";
				manyheads_top10_contact_recent6month_have_partnercode_count = ObjectUtils.isNotBlank(contact_manyheads_analysis_obj.getString("manyheads_top10_contact_recent6month_have_partnercode_count"))?contact_manyheads_analysis_obj.getString("manyheads_top10_contact_recent6month_have_partnercode_count"):"0";
			}
			ca_data_map.put("manyheads_top10_contact_recent6month_partnercode_count_avg",manyheads_top10_contact_recent6month_partnercode_count_avg);
			ca_data_map.put("manyheads_top10_contact_recent6month_have_partnercode_count",manyheads_top10_contact_recent6month_have_partnercode_count);
			// 催收
			if (ObjectUtils.isNotBlank(report_data_obj.get("contact_suspect_collection_analysis"))) {
				ca_data_map.put("contact_suspect_collection_analysis",report_data_obj.getString("contact_suspect_collection_analysis"));
			} else {
				ca_data_map.put("contact_suspect_collection_analysis", null);
			}
			// risk_contact_stats 风险
			if (ObjectUtils.isNotBlank(report_data_obj.get("risk_contact_stats") )) {
				ca_data_map.put("risk_contact_stats", report_data_obj.get("risk_contact_stats").toString());
			} else {
				ca_data_map.put("risk_contact_stats", null);
			}
			// 金融 finance_contact_stats
			if (ObjectUtils.isNotBlank(report_data_obj.get("finance_contact_stats"))) {
				ca_data_map.put("finance_contact_stats", report_data_obj.get("finance_contact_stats").toString());
			} else {
				ca_data_map.put("finance_contact_stats", null);
			}
			if (ObjectUtils.isNotBlank(report_data_obj.get("carrier_consumption_stats"))) {
				ca_data_map.put("carrier_consumption_stats", report_data_obj.get("carrier_consumption_stats").toString());
			} else {
				ca_data_map.put("carrier_consumption_stats", null);
			}
			// 静默 active_silence_stats
			if (ObjectUtils.isNotBlank(report_data_obj.get("active_silence_stats"))) {
				ca_data_map.put("active_silence_stats", report_data_obj.get("active_silence_stats").toString());
			} else {
				ca_data_map.put("active_silence_stats", null);
			}
			// 全部联系人
			if (ObjectUtils.isNotBlank(report_data_obj.get("all_contact_stats"))) {
				ca_data_map.put("all_contact_stats", report_data_obj.get("all_contact_stats").toString());
			} else {
				ca_data_map.put("all_contact_stats", null);
			}
			if (ObjectUtils.isNotBlank(report_data_obj.get("call_duration_stats_2hour"))) {
				ca_data_map.put("call_duration_stats_2hour", report_data_obj.getString("call_duration_stats_2hour"));
			} else {
				ca_data_map.put("call_duration_stats_2hour", null);
			}
			// 整理好的数据
			String content = JSONUtil.toJson(ca_data_map);
			Member	member = memberService.get(sjmh.getOrgId());
			RcCaData caData = new RcCaData();
			caData.setContent(content);
			caData.setPhoneNo(member.getUsername());
			caData.setIdNo(member.getIdNo());
			caData.setName(member.getName());
			caData.setType(RcCaData.Type.yunyingshang_report);	
			caData.setProvider(RcCaData.Provider.sjmh);
			rcCaDataService.save(caData);
			
			sjmh.setReportStatus(RcSjmh.ReportStatus.report_arranged);
			rcSjmhService.save(sjmh);
	
		} catch (NullPointerException e) {
			log.error(Exceptions.getStackTraceAsString(e));
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
	}
	
	private void yunyingshang(RcSjmh sjmh) {
		// 运营商
		try {

			String channel_src = sjmh.getChannelSrc();
			String channel_attr = sjmh.getChannelAttr();
			String created_time = DateUtils.formatDateTime(sjmh.getCreateTime());
			String user_mobile = sjmh.getPhoneNo();


			JSONObject task_data_obj = JSONObject.parseObject(sjmh.getTaskData());
			JSONObject base_info_obj = task_data_obj.getJSONObject("base_info");
			JSONObject account_info_obj = task_data_obj.getJSONObject("account_info");
			JSONArray call_info_arr = task_data_obj.getJSONArray("call_info");
			JSONArray bill_info_arr = task_data_obj.getJSONArray("bill_info");
			String net_age ="6";
			String identity_code = "123";
			if(ObjectUtils.isNotBlank(base_info_obj)) {
				identity_code = ObjectUtils.isNotBlank(base_info_obj.getString("cert_num"))?base_info_obj.getString("cert_num"):"--";
			}
			if(ObjectUtils.isNotBlank(account_info_obj)) {
				net_age = account_info_obj.getString("net_age");		
			}

			int tongNum = 0; // 半年通话时长
			int tongCishu = 0;// 半年通话次数

			Integer total_call_count = 0;
			Integer total_call_time = 0;

			for (int i = 0; i < call_info_arr.size(); i++) {
				JSONObject call_info_obj = call_info_arr.getJSONObject(i);
				
				Integer call_count = call_info_obj.getInteger("total_call_count");
				Integer call_time = call_info_obj.getInteger("total_call_time");
				
				total_call_count = total_call_count + (call_count==null?0:call_count);
				total_call_time = total_call_time + (call_time==null?0:call_time);
			}

			tongNum = (int) tongNum / 60;// 算成分钟
			int dayTongNum = (int) tongNum / 6 / 30; // 每天时间
			// 循环计算
			int total_Hutong = 0; // 夜间通话
			int total_jngMoNum = 0; // 静默通话

			for (int i = 0; i < call_info_arr.size(); i++) {
				JSONObject call_info_obj = call_info_arr.getJSONObject(i);
				String shuJuMofangData = dealCallInfo(call_info_obj);
				String[] split = shuJuMofangData.split(",");
				String string2 = split[0];
				String string3 = split[1];
				total_jngMoNum = total_jngMoNum + Integer.parseInt(string2);
				total_Hutong = total_Hutong + Integer.parseInt(string3);
			}

			// 计算总费用
			int frrMoney = 0;

			for (int i = 0; i < bill_info_arr.size(); i++) {
				JSONObject bill_info_obj = bill_info_arr.getJSONObject(i);
				Integer bill_fee = bill_info_obj.getInteger("bill_fee");
				frrMoney = frrMoney + (bill_fee==null?0:bill_fee);
			}

			int taocanmoney = 0;// 基本套餐金额
			int diejiamoney = 0;// 叠加套餐金额
			int zengzhimoney = 0;// 增值服务消费金额（联通秘书）
			int othermoney = 0; // 其他费用
			int money = 0;
			int taocanmoney1 = 0;// 基本套餐金额
			int diejiamoney1 = 0;// 叠加套餐金额
			int zengzhimoney1 = 0;// 增值服务消费金额（联通秘书）
			int othermoney1 = 0; // 其他费用
			int money1 = 0;
			int taocanmoney3 = 0;// 基本套餐金额
			int diejiamoney3 = 0;// 叠加套餐金额
			int zengzhimoney3 = 0;// 增值服务消费金额（联通秘书）
			int othermoney3 = 0; // 其他费用
			int money3 = 0;
			// 计算消费能力

			for (int i = 0; i < bill_info_arr.size(); i++) {
				JSONObject bill_info_obj = bill_info_arr.getJSONObject(i);
				String billInfoStr = dealBillInfo(bill_info_obj);
				String[] split = billInfoStr.split(",");
				String string2 = split[0];
				String string3 = split[1];
				String string4 = split[2];
				String string5 = split[3];
				String string7 = split[4];
				money = money + Integer.parseInt(string2);
				taocanmoney = taocanmoney + Integer.parseInt(string3);
				diejiamoney = diejiamoney + Integer.parseInt(string4);
				zengzhimoney = zengzhimoney + Integer.parseInt(string5);
				othermoney = othermoney + Integer.parseInt(string7);
				if (i == 0) {
					money1 = money1 + Integer.parseInt(string2);
					taocanmoney1 = taocanmoney1 + Integer.parseInt(string3);
					diejiamoney1 = diejiamoney1 + Integer.parseInt(string4);
					zengzhimoney1 = zengzhimoney1 + Integer.parseInt(string5);
					othermoney1 = othermoney1 + Integer.parseInt(string7);
				} else if (i < 4) {
					money3 = money3 + Integer.parseInt(string2);
					taocanmoney3 = taocanmoney3 + Integer.parseInt(string3);
					diejiamoney3 = diejiamoney3 + Integer.parseInt(string4);
					zengzhimoney3 = zengzhimoney3 + Integer.parseInt(string5);
					othermoney3 = othermoney3 + Integer.parseInt(string7);

				}

			}

			frrMoney = frrMoney / 6 / 100; // 通话费用
			Map<String, Object> task_map = new HashMap<String, Object>();
			task_map.put("channel_src", channel_src);
			task_map.put("channel_attr", channel_attr);
			task_map.put("created_time", created_time);
			task_map.put("user_mobile", user_mobile);
			task_map.put("identity_code", identity_code);
			task_map.put("time", net_age);
			task_map.put("frrMoney", frrMoney);
			task_map.put("Hutong", total_Hutong);
			task_map.put("dayTongNum", dayTongNum);
			task_map.put("tongNum", tongNum);
			task_map.put("tongCishu", tongCishu);
			task_map.put("jngMoNum", total_jngMoNum);

			task_map.put("money", money);
			task_map.put("taocanmoney", taocanmoney);
			task_map.put("diejiamoney", diejiamoney);
			task_map.put("zengzhimoney", zengzhimoney);
			task_map.put("othermoney", othermoney);

			task_map.put("money1", money1);
			task_map.put("taocanmoney1", taocanmoney1);
			task_map.put("diejiamoney1", diejiamoney1);
			task_map.put("zengzhimoney1", zengzhimoney1);
			task_map.put("othermoney1", othermoney1);

			task_map.put("money3", money3);
			task_map.put("taocanmoney3", taocanmoney3);
			task_map.put("diejiamoney3", diejiamoney3);
			task_map.put("zengzhimoney3", zengzhimoney3);
			task_map.put("othermoney3", othermoney3);

			String task_json = JSONUtil.toJson(task_map);
			// 整理好的数据
			Member member = memberService.get(sjmh.getOrgId());
			RcCaData caData = new RcCaData();
			caData.setContent(task_json);
			caData.setPhoneNo(member.getUsername());
			caData.setIdNo(member.getIdNo());
			caData.setName(member.getName());
			caData.setType(RcCaData.Type.yunyingshang);
			caData.setProvider(RcCaData.Provider.sjmh);
			rcCaDataService.save(caData);

			sjmh.setDataStatus(RcSjmh.DataStatus.data_arranged);
			rcSjmhService.save(sjmh);

			// 详情数据
			RcCaYysDetails rcCaYysDetails = new RcCaYysDetails();
			rcCaYysDetails.setContent(sjmh.getTaskData());
			rcCaYysDetails.setMemberId(sjmh.getOrgId());
			rcCaYysDetailsService.save(rcCaYysDetails);
			
			// 认证成功后添加运营商认证项
			
			if(StringUtils.equals(member.getUsername(), user_mobile)) {
				int verifiedList = member.getVerifiedList();
				verifiedList = VerifiedUtils.addVerified(verifiedList, 6);
				member.setVerifiedList(verifiedList);
				RedisUtils.put("memberInfo" + member.getId(), "operatorStatus", "1");
			};
			memberService.save(member);

		} catch (NullPointerException e) {
			log.error(Exceptions.getStackTraceAsString(e));
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
	}
	
	private void xuexinwang(RcSjmh sjmh) {
		// 学信网
		try {
			JSONObject task_data_obj = JSONObject.parseObject(sjmh.getTaskData());
			Member member = memberService.get(sjmh.getOrgId());
			RcCaData caData = new RcCaData();
			caData.setContent(task_data_obj.getString("school_info"));
			caData.setPhoneNo(member.getUsername());
			caData.setIdNo(member.getIdNo());
			caData.setName(member.getName());
			caData.setType(RcCaData.Type.xuexinwang);
			caData.setProvider(RcCaData.Provider.sjmh);
			rcCaDataService.save(caData);
			
			sjmh.setDataStatus(RcSjmh.DataStatus.data_arranged);
			rcSjmhService.save(sjmh);
			
			// 认证成功后添加学信网认证项
			
			int verifiedList=member.getVerifiedList();
			verifiedList = VerifiedUtils.addVerified(verifiedList,8);
			member.setVerifiedList(verifiedList);	
			RedisUtils.put("memberInfo"+member.getId(), "xuexingwangStatus", "1");
			memberService.save(member);
	
		} catch (NullPointerException e) {
			log.error(Exceptions.getStackTraceAsString(e));
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
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
				debit_card_map.put("account", debit_card_accounts_obj.getString("account"));// 卡状态

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
				credit_card_map.put("account", obj_obj.getString("account"));
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
			RcCaData caData = new RcCaData();
			caData.setContent(content);
			caData.setPhoneNo(member.getUsername());
			caData.setIdNo(member.getIdNo());
			caData.setName(member.getName());
			caData.setType(RcCaData.Type.wangyin);
			caData.setProvider(RcCaData.Provider.sjmh);
			rcCaDataService.save(caData);

			sjmh.setDataStatus(RcSjmh.DataStatus.data_arranged);
			rcSjmhService.save(sjmh);
			
			// 认证成功后添加银行账单认证项
			int verifiedList=member.getVerifiedList();
			verifiedList = VerifiedUtils.addVerified(verifiedList,7);
			member.setVerifiedList(verifiedList);	
			RedisUtils.put("memberInfo"+member.getId(), "bankTrxStatus", "1");
			memberService.save(member);

		} catch (NullPointerException e) {
			log.error(Exceptions.getStackTraceAsString(e));
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
	}
	
	public static String dealCallInfo(JSONObject call_info_obj) {

		int Hutong = 0;// 夜间
		JSONArray call_record_arr = call_info_obj.getJSONArray("call_record");
		for (int i = 0; i < call_record_arr.size(); i++) {
			JSONObject call_record_obj = call_record_arr.getJSONObject(i);
				String TongCishu = call_record_obj.getString("call_start_time");// 通话时间
				String[] split = TongCishu.split(" ");
				// 数据带回来如果不包含 时分秒自定义一个时间
				String time = "12:00:00";
				if (split.length >= 2) {
					time = split[1];
				}
				String startTime = "05:30:00";
				String endTime = "23:30:00";
				SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
				Date date = null;
				Date startdate = null;
				Date enddate = null;
				try {
					date = formatter.parse(time);
					startdate = formatter.parse(startTime);
					enddate = formatter.parse(endTime);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (date.getTime() < startdate.getTime() && date.getTime() > enddate.getTime()) {
					Hutong = Hutong + 1;
				}
			}
	
		int jngMoNum = 0; // 静默
		List<String> iList = new ArrayList<String>();
		for (int i = 1; i < 32; i++) {
			if (i < 10) {
				iList.add("0" + i);
			} else {
				iList.add("" + i);
			}
		}

		for (int i = 0; i < call_record_arr.size(); i++) {
			JSONObject call_record_obj = call_record_arr.getJSONObject(i);
				String TongCishu = call_record_obj.getString("call_start_time");// 通话时间
				if (TongCishu != null && !TongCishu.equals("null")) {
					String[] split = TongCishu.split("-");
					if (split.length > 2) {
						String time = split[2];
						String[] split2 = time.split(" ");
						String day = "31";
						if (split2.length > 0) {
							day = split2[0];
						}
						for (int j = 0; j < iList.size(); j++) {
							if (day.equals(iList.get(j))) {
								iList.remove(j); // 移出有过通话的日期
							}
						}
						jngMoNum = iList.size();

					}
				}
			}

		String zong = jngMoNum + "," + Hutong;
		return zong;

	}

	// 计算套餐
	public String dealBillInfo(JSONObject bill_info_obj) {

		JSONArray bill_record_arr = bill_info_obj.getJSONArray("bill_record");

		// 花费金额
		int taocanmoney = 0;// 基本套餐金额
		int diejiamoney = 0;// 叠加套餐金额
		int zengzhimoney = 0;// 增值服务消费金额（联通秘书）
		int othermoney = 0; // 其他费用
		Integer bill_fee = bill_info_obj.getInteger("bill_fee")==null?0:bill_info_obj.getInteger("bill_fee");
		
		for (int i = 0; i < bill_record_arr.size(); i++) {
			JSONObject bill_record_obj = bill_record_arr.getJSONObject(i);
			taocanmoney = taocanmoney + (bill_record_obj.getInteger("fee_amount")==null?0:bill_record_obj.getInteger("fee_amount"));
			
//			if (bill_record_obj.get("fee_amount") != null && !bill_record_obj.get("fee_amount").equals("null")) {
//				othermoney = Integer.parseInt(bill_record_obj.get("fee_amount").toString()) + othermoney;
//			} else if (bill_record_obj.get("fee_name").toString().equals("联通秘书")) {
//				zengzhimoney = Integer.parseInt(bill_record_obj.get("fee_amount").toString()) + zengzhimoney;
//			} else if (bill_record_obj.get("fee_name").toString().equals("叠加套餐包")) {
//				diejiamoney = Integer.parseInt(bill_record_obj.get("fee_amount").toString()) + diejiamoney;
//
//			} else if (bill_record_obj.get("fee_name").toString().equals("基本套餐费")) {
//				taocanmoney = Integer.parseInt(bill_record_obj.get("fee_amount").toString()) + taocanmoney;
//
//			} else {
//				othermoney = Integer.parseInt(bill_record_obj.get("fee_amount").toString()) + othermoney;
//			}

		}
		taocanmoney = taocanmoney / 100;
		diejiamoney = diejiamoney / 100;
		zengzhimoney = zengzhimoney / 100;
		othermoney = othermoney / 100;
		String zong = bill_fee/ 100 + "," + taocanmoney + "," + diejiamoney + "," + zengzhimoney + "," + othermoney;
		return zong;
	}

}
