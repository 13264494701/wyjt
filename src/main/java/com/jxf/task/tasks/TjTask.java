package com.jxf.task.tasks;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
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

import com.jxf.rc.entity.RcTianji;
import com.jxf.rc.service.RcCaDataServiceV2;
import com.jxf.rc.service.RcTianjiService;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.JSONUtil;
import com.jxf.svc.utils.StringUtils;


@DisallowConcurrentExecution
public class TjTask implements Job {

	private static Logger logger = LoggerFactory.getLogger(TjTask.class);
	
	@Autowired
	private RcCaDataServiceV2 rcCaDataService;
	@Autowired
	private RcTianjiService rcTjService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberExtendService memberExtendService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		data_proc(); 
	}
	
	private void data_proc() {
		RcTianji rcTianji = new RcTianji();
		rcTianji.setProdType(RcTianji.ProdType.app);
    	rcTianji.setDataStatus(RcTianji.DataStatus.data_created);
    	List<RcTianji> rcTianjiList = rcTjService.findList(rcTianji);
    	
       for(RcTianji tianji:rcTianjiList) {
			try {
	
				String taskData = FileUtils.readFileToString(new File(Global.getConfig("uploadPath")+tianji.getDataPath()),"utf-8");
				tianji.setTaskData(taskData);
				switch(tianji.getChannelType()){
			    case shebao :
			    	shebao(tianji);
			       break; 
			    case gongjijin :
			    	gongjijin(tianji);
			       break; 
			    case taobao :
			    	taobao(tianji);
			       break; 
			    case yunyingshang :
			    	yunyingshang(tianji);
			       break;
			    case xuexinwang :
			    	xuexinwang(tianji);
			       break;
			    case wangyin :
			    	wangyin(tianji);
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
	
	private void shebao(RcTianji tianji) {
		try {
			//社保
			Map<String,Object> map = new HashMap<String,Object>();
			Map<String,Object> flowMap = new HashMap<String,Object>();
			JSONObject taskData = JSONUtil.getJSONFromString(tianji.getTaskData());
			JSONArray dataList = taskData.getJSONObject("data").getJSONArray("data_list");
			JSONObject userData = dataList.getJSONObject(0);
			JSONArray flowData =  dataList.getJSONArray(2);
			String insureDay = (String) userData.get("start_insure_day");//起缴日期
			String workDay = (String) userData.get("work_start_day");//参加工作日期
			String comName = (String) userData.get("com_name");//单位名称
			String liveAddr = (String) userData.get("live_addr");//家庭住址
			map.put("insureDay", insureDay);
			map.put("workDay", workDay);
			map.put("comName", comName);
			map.put("liveAddr", liveAddr);
			//详情
			for (int i = 0; i < flowData.size(); i++) {
				Map<String,Object> detailMap = new HashMap<String,Object>();
				JSONObject flowDetail = flowData.getJSONObject(i);
				String payDate = (String) flowDetail.get("pay_date");//缴纳时间
				String baseRmb = (String) flowDetail.get("base_rmb");//缴费基数
				String comRmb = (String) flowDetail.get("com_rmb");//单位缴费
				String perRmb = (String) flowDetail.get("per_rmb");//个人缴费
				String payCond = (String) flowDetail.get("pay_cond");//缴费状态
				String flowType = (String) flowDetail.get("flow_type");//险种
				detailMap.put("payDate", payDate);
				detailMap.put("baseRmb", baseRmb);
				detailMap.put("comRmb", comRmb);
				detailMap.put("perRmb", perRmb);
				detailMap.put("payCond", payCond);
				detailMap.put("flowType", flowType);
				flowMap.putAll(detailMap);
			}
			map.put("flowMap", flowMap);
			String json = JSONUtil.toJson(map);
			logger.debug("====json===="+json);
			Member	member = memberService.get(tianji.getOrgId());
			
			RcCaDataV2 caDataV2 = new RcCaDataV2();
			caDataV2.setPhoneNo(tianji.getPhoneNo());
			caDataV2.setType(RcCaDataV2.Type.shebao);
			caDataV2.setStatus(RcCaDataV2.Status.processing);
			List<RcCaDataV2> caDataList =  rcCaDataService.findList(caDataV2);
			RcCaDataV2 caData = null;
			if(!Collections3.isEmpty(caDataList)) {
				caData = caDataList.get(0);
			}else {
				caData = new RcCaDataV2();
			}
			caData.setStatus(RcCaDataV2.Status.success);
			
			caData.setPhoneNo(tianji.getPhoneNo());
			caData.setType(RcCaDataV2.Type.shebao);
			caData.setStatus(RcCaDataV2.Status.processing);
			caData.setContent(json);
			caData.setPhoneNo(member.getUsername());
			caData.setIdNo(member.getIdNo());
			caData.setName(member.getName());
			caData.setProvider(RcCaDataV2.Provider.tj);
			rcCaDataService.save(caData);
			
			tianji.setDataStatus(RcTianji.DataStatus.data_arranged);
			rcTjService.save(tianji);
			
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
	
	
	private void gongjijin(RcTianji tianji) {
		try {
			//公积金
			Map<String,Object> map = new HashMap<String,Object>();
			Map<String,Object> flowMap = new HashMap<String,Object>();
			JSONObject taskData = JSONUtil.getJSONFromString(tianji.getTaskData());
			JSONArray dataList = taskData.getJSONObject("data").getJSONArray("data_list");
			JSONObject userData = dataList.getJSONObject(0);
			JSONArray flowData =  dataList.getJSONArray(1);
			String fundStatus = (String) userData.get("fund_status");//缴费状态
			String startDate = (String) userData.get("acc_start_day");//开户日期
			String lastDate = (String) userData.get("last_op_date");//最后缴费日期
			String comName = (String) userData.get("com_name");//缴纳单位名称
			String liveAddr = (String) userData.get("live_addr");//家庭住址
			map.put("fundStatus", fundStatus);
			map.put("startDate", startDate);
			map.put("lastDate", lastDate);
			map.put("comName", comName);
			map.put("liveAddr", liveAddr);
			//详情
			for (int i = 0; i < flowData.size(); i++) {
				Map<String,Object> detailMap = new HashMap<String,Object>();
				JSONObject flowDetail = flowData.getJSONObject(i);
				String endDate = (String) flowDetail.get("end_date");//到账日期
				String payType = (String) flowDetail.get("pay_type");//业务类型
				String inRmb = (String) flowDetail.get("in_month_rmb");//缴存额(元)
				String outRmb = (String) flowDetail.get("out_month_rmb");//取出额(元)
				String balRmb = (String) flowDetail.get("balance_rmb");//余额(元)
				detailMap.put("endDate", endDate);
				detailMap.put("payType", payType);
				detailMap.put("inRmb", inRmb);
				detailMap.put("outRmb", outRmb);
				detailMap.put("balRmb", balRmb);
				flowMap.putAll(detailMap);
			}
			map.put("flowMap", flowMap);
			String json = JSONUtil.toJson(map);
			logger.debug("====json===="+json);
			
			Member	member = memberService.get(tianji.getOrgId());
			RcCaDataV2 caDataV2 = new RcCaDataV2();
			caDataV2.setPhoneNo(tianji.getPhoneNo());
			caDataV2.setType(RcCaDataV2.Type.gjj);
			caDataV2.setStatus(RcCaDataV2.Status.processing);
			List<RcCaDataV2> caDataList =  rcCaDataService.findList(caDataV2);
			RcCaDataV2 caData = null;
			if(!Collections3.isEmpty(caDataList)) {
				caData = caDataList.get(0);
			}else {
				caData = new RcCaDataV2();
			}
			caData.setStatus(RcCaDataV2.Status.success);
			caData.setContent(json);
			caData.setIdNo(member.getIdNo());
			caData.setName(member.getName());
			caData.setProvider(RcCaDataV2.Provider.tj);
			rcCaDataService.save(caData);
			
			tianji.setDataStatus(RcTianji.DataStatus.data_arranged);
			rcTjService.save(tianji);
			
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
	
	private void taobao(RcTianji tianji) {
		try {
			JSONObject task_data_obj = JSONObject.parseObject(tianji.getTaskData());
			JSONObject taobaoData = new JSONObject();
			JSONObject data = task_data_obj.getJSONObject("data");
			JSONArray dataList = data.getJSONArray("data_list");
			JSONObject userData = (JSONObject) dataList.get(0);
			//淘宝账号信息
			JSONObject taobao_user = userData.getJSONObject("taobao_user");
			//淘宝账号信息
			JSONObject jiebei = userData.getJSONObject("jiebei");
			//真实姓名
			String realName = taobao_user.getString("name");
			//绑定手机号
			String binding_phone = taobao_user.getString("binding_phone");
			//淘宝订单信息
			JSONArray taobao_order = userData.getJSONArray("taobao_order");
			
			int count3M = 0; // 三个月内订单数量
			double sumAmt3M = 0; // 三个月内订单金额

			int count6M = 0; // 六个月内订单数量
			double sumAmt6M = 0; // 六个月内订单金额

						
			double maxPrice3M = 0; // 三个月最贵
			String maxPriceProductName3M = "";
			double maxPrice6M = 0; // 六个月最贵
			String maxPriceProductName6M = "";
			for (Object object3 : taobao_order) {
				JSONObject order = (JSONObject) object3;
				String order_time = "";
				String payment_time = order.getString("payment_time");
				String confirmation_time = order.getString("confirmation_time");
				String transaction_time = order.getString("transaction_time");
				String order_amount = order.getString("actual_fee");
				JSONArray products = order.getJSONArray("products");
				if(StringUtils.isNotBlank(payment_time)) {
					order_time = payment_time;
				}else if(StringUtils.isNotBlank(confirmation_time)){
					order_time = confirmation_time;
				}else if(StringUtils.isNotBlank(transaction_time)) {
					order_time = transaction_time;
				}
				if(StringUtils.isNotBlank(order_time)) {
					long pastDays = DateUtils.pastDays(DateUtils.parse(order_time));
					if(pastDays<=90) {
						sumAmt3M = sumAmt3M + Double.valueOf(order_amount);
						count3M++;	
						for(int j = 0;j<products.size();j++){  
							JSONObject product_obj = products.getJSONObject(j);
							double product_price = Double.valueOf(product_obj.getString("product_price"));
							String product_name = product_obj.getString("product_name");
							if (product_price > maxPrice3M) {
								maxPrice3M= product_price;
								maxPriceProductName3M = product_name;
							}	
						}

					}
					if(pastDays<=180) {
						sumAmt6M = sumAmt6M + Double.valueOf(order_amount);
						count6M++;
						for(int j = 0;j<products.size();j++){  
							JSONObject product_obj = products.getJSONObject(j);
							double product_price = Double.valueOf(product_obj.getString("product_price"));
							String product_name = product_obj.getString("product_name");
							if (product_price > maxPrice6M) {
								maxPrice6M= product_price;
								maxPriceProductName6M = product_name;
							}	
						}
					}	
				}
			}
			//淘宝收货地址
			JSONArray taobao_deliver_addr = userData.getJSONArray("taobao_deliver_addr");
			String taoadree = "";
			for (Object object2 : taobao_deliver_addr) {
				JSONObject deliver_addr = (JSONObject) object2;
				String is_default_address = deliver_addr.getString("is_default_address");
				//默认地址
				String isDefaultAddr = "1";
				if(StringUtils.equals(is_default_address, isDefaultAddr)) {
					//地区
					String area = deliver_addr.getString("area");
					//详细地址
					String address = deliver_addr.getString("address");
					taoadree = area + address;
				}
			}
			if(StringUtils.isBlank(taoadree)) {
				JSONObject deliver_addr = (JSONObject) taobao_deliver_addr.get(0);
				taoadree = deliver_addr.getString("area") + deliver_addr.getString("address");
			}
			//支付宝账号信息
			JSONObject alipay_user = userData.getJSONObject("alipay_user");
			//余额宝余额
			String yuebaoAmount = alipay_user.getString("amount");
			String tbYEbao = "";
			if(StringUtils.isNotBlank(yuebaoAmount)) {
				tbYEbao = StringUtils.decimalToStr(new BigDecimal(yuebaoAmount), 2);
			}
			//花呗
			JSONObject huabei = userData.getJSONObject("huabei");
			//花呗总额度
			String total_amount = huabei.getString("total_amount");
			String tbHuabei = "";
			if(StringUtils.isNotBlank(total_amount)) {
				tbHuabei = StringUtils.decimalToStr(new BigDecimal(total_amount), 2);
			}
			
			
			taobaoData.put("real_name", realName);
			taobaoData.put("phoneNo", binding_phone);
			taobaoData.put("user_level", taobao_user.getString("vip_level"));
			taobaoData.put("vip_count", taobao_user.getString("score"));
			taobaoData.put("zmf", "无数据");
			taobaoData.put("jiebei_quota",  jiebei.getString("credit_amount"));
			taobaoData.put("credit_quota", tbHuabei);
			taobaoData.put("fin_act_bal", tbYEbao);
			
			taobaoData.put("count3M", count3M);
			taobaoData.put("sumAmt3M", sumAmt3M);
			taobaoData.put("avgAmt3M", sumAmt3M/3);
			
			taobaoData.put("count6M", count6M);
			taobaoData.put("sumAmt6M", sumAmt6M);
			taobaoData.put("avgAmt6M", sumAmt6M/6);
			
			
			taobaoData.put("maxPrice3M", maxPrice3M);
			taobaoData.put("maxPriceProductName3M", maxPriceProductName3M);
			taobaoData.put("maxPrice6M", maxPrice6M);
			taobaoData.put("maxPriceProductName6M", maxPriceProductName6M);
			taobaoData.put("default_receive_addr", taoadree);
			
			Member	member = memberService.get(tianji.getOrgId());
			//身份证与无忧借条是否一致
			String id_no_match = "0";
			if(StringUtils.equals(alipay_user.getString("real_name"), member.getName()) 
					|| StringUtils.equals(taobao_user.getString("name"), member.getName())) {
				id_no_match = "1";
			}
			taobaoData.put("id_no_match", id_no_match);
			
			
			RcCaDataV2 caDataV2 = new RcCaDataV2();
			caDataV2.setPhoneNo(tianji.getPhoneNo());
			caDataV2.setType(RcCaDataV2.Type.taobao);
			caDataV2.setStatus(RcCaDataV2.Status.processing);
			List<RcCaDataV2> caDataList =  rcCaDataService.findList(caDataV2);
			RcCaDataV2 caData = null;
			if(!Collections3.isEmpty(caDataList)) {
				caData = caDataList.get(0);
			}else {
				caData = new RcCaDataV2();
			}
			caData.setStatus(RcCaDataV2.Status.success);
			
			caData.setContent(taobaoData.toString());
			caData.setPhoneNo(member.getUsername());
			caData.setIdNo(member.getIdNo());
			caData.setName(member.getName());
			caData.setType(RcCaDataV2.Type.taobao);
			caData.setProvider(RcCaDataV2.Provider.tj);
			rcCaDataService.save(caData);
			
			tianji.setDataStatus(RcTianji.DataStatus.data_arranged);
			rcTjService.save(tianji);
			// 认证成功后添加淘宝认证项	
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

	private void yunyingshang(RcTianji tianji) {
		try {
			JSONObject operatorInfo = new JSONObject();
			String tjReportData = FileUtils.readFileToString(new File(Global.getConfig("uploadPath")+tianji.getReportPath()),"utf-8");
			JSONObject report_data_obj = JSONObject.parseObject(tjReportData);
			JSONObject reportJson = report_data_obj.getJSONObject("json");
			//基本信息
			JSONObject basic_info = reportJson.getJSONObject("basic_info");
			//用户信息检测
			JSONObject user_info_check = reportJson.getJSONObject("user_info_check");
			//用户画像
			JSONObject user_portrait = reportJson.getJSONObject("user_portrait");
			//通讯记录
			JSONArray call_log = reportJson.getJSONArray("call_log");
			//运营商月消费
			JSONArray monthly_consumption = reportJson.getJSONArray("monthly_consumption");
			//特殊号码类别
			JSONArray special_cate = reportJson.getJSONArray("special_cate");
			//风险检测
			JSONObject risk_analysis = reportJson.getJSONObject("risk_analysis");
			//联系人地区分析
			JSONArray contact_area_analysis = reportJson.getJSONArray("contact_area_analysis");
			
			JSONObject mobileInfo = new JSONObject();
			//账户状态 欠费，停机，正常 等 ，天机无
//			mobileInfo.put("account_status", );"2016-01-01"
			//入网时长
			String regTime = basic_info.getString("reg_time");
			Date regDate = null;
			regDate = DateUtils.parseDate(regTime);
			int monthNum = DateUtils.getDifferenceOfTwoDate(regDate, new Date())/30;
			mobileInfo.put("mobile_net_age", monthNum);
			//号码归属地
			mobileInfo.put("mobile_area", basic_info.getString("phone_location"));
			mobileInfo.put("id_no_match", basic_info.getString("id_card_check"));
			String phone = basic_info.getString("phone");
			mobileInfo.put("authPhone", phone);
			//是否实名 1：实名，2：未实名，3：无法判断
			mobileInfo.put("reliability", basic_info.getIntValue("reliability"));
			mobileInfo.put("phone_no_match", StringUtils.equals(tianji.getPhoneNo(), phone) ? "匹配":"不匹配");
			operatorInfo.put("mobile_info", mobileInfo);
			
			//前10手机联系人风险分析
			JSONObject top10_contact_risk_analysis = new JSONObject();
			//前10联系人黑名单人数占比
			top10_contact_risk_analysis.put("black_top10_contact_total_count_ratio", user_info_check.getJSONObject("check_black_info").getInteger("contacts_class1_blacklist_cnt")+"");
			//天机无此字段
//			top10_contact_risk_analysis.put("manyheads_top10_contact_recent6month_have_partnercode_count", );
//			top10_contact_risk_analysis.put("manyheads_top10_contact_recent6month_partnercode_count_avg", );
			operatorInfo.put("top10_contact_risk_analysis", top10_contact_risk_analysis);
//			
			
			//联系人命中逾期情况
			JSONObject contact_overdue_hit_analysis = new JSONObject();
			//主叫联系人命中逾期数量
			int call_overdue_hit_count = 0;
			//主叫联系人命中逾期总通话时间
			double call_overdue_hit_time = 0;
			//被叫联系人命中逾期数量
			int called_overdue_hit_count = 0;
			//被叫联系人命中逾期通话总时间
			double called_overdue_hit_time = 0;
			//互通联系人命中逾期数量
			int communicate_overdue_hit_count = 0;
			//互通联系人命中逾期通话总时长
			double communicate_overdue_hit_time = 0;
			JSONArray contacts_overdue = risk_analysis.getJSONArray("contacts_overdue");
			//联系人类型：主叫联系人
			int callType = 1;
			//联系人类型：被叫联系人
			int calledType = 2;
			//联系人类型：互通联系人
			int communicateType = 3;			
			for (Object object : contacts_overdue) {
				JSONObject contactOverdue = (JSONObject) object;
				int type = contactOverdue.getIntValue("type");
				int hitCnt = contactOverdue.getIntValue("hit_cnt");
				int seconds = contactOverdue.getIntValue("seconds");
				if(type == callType) {
					call_overdue_hit_count = call_overdue_hit_count + hitCnt;
					call_overdue_hit_time = call_overdue_hit_time + Double.valueOf(seconds)/60;
				}else if (type == calledType){
					called_overdue_hit_count = called_overdue_hit_count + hitCnt;
					called_overdue_hit_time = called_overdue_hit_time + Double.valueOf(seconds)/60;
				}else if(type == communicateType){
					communicate_overdue_hit_count = communicate_overdue_hit_count + hitCnt;
					communicate_overdue_hit_time = communicate_overdue_hit_time + Double.valueOf(seconds)/60;
				}
			}
			contact_overdue_hit_analysis.put("call_overdue_hit_count", call_overdue_hit_count);
			contact_overdue_hit_analysis.put("call_overdue_hit_time", call_overdue_hit_time);
			contact_overdue_hit_analysis.put("called_overdue_hit_count", called_overdue_hit_count);
			contact_overdue_hit_analysis.put("called_overdue_hit_time", called_overdue_hit_time);
			contact_overdue_hit_analysis.put("communicate_overdue_hit_count", communicate_overdue_hit_count);
			contact_overdue_hit_analysis.put("communicate_overdue_hit_time", communicate_overdue_hit_time);
			operatorInfo.put("contact_overdue_analysis", contact_overdue_hit_analysis);
			
			//联系人催收风险分析
			JSONObject contact_suspect_collection_analysis = new JSONObject();
			int suspect_collection_contact_count_passive_6month = 0;
			int suspect_collection_call_count_passive_6month = 0;
			
			//紧急联系人统计
			Member member = memberService.findByUsername(tianji.getPhoneNo());
			MemberExtend memberExtend = memberExtendService.getByMember(member);	
			if(memberExtend!=null) {
				JSONArray emergency_contact_stats_list = new JSONArray();
				for (Object object : call_log) {
					JSONObject callRecord = (JSONObject) object;
					String contact_number = callRecord.getString("phone");
					JSONObject emergency_contact_stats = new JSONObject();
					if(StringUtils.equals(contact_number, memberExtend.getEcpPhoneNo1())) {
						emergency_contact_stats.put("ecp", memberExtend.getEcp1());
						emergency_contact_stats.put("contact_number", callRecord.getInteger("talk_cnt").toString());
						JSONArray  monthDetails = callRecord.getJSONArray("month_detail");
						int oneMonthContactSeconds = 0;
						int threeMonthContactSeconds = 0;
						int sixMonthContactSeconds = 0;
						if(!Collections3.isEmpty(monthDetails)) {
							for(int i = 0; i < monthDetails.size(); i++) {
								JSONObject detail = (JSONObject) monthDetails.get(i);
								int talkSeconds = detail.getInteger("talk_seconds");
								if(i == 0) {
									oneMonthContactSeconds = talkSeconds;
									threeMonthContactSeconds = talkSeconds;
									sixMonthContactSeconds = talkSeconds;
								}else if(i > 0 && i < 3) {
									threeMonthContactSeconds = threeMonthContactSeconds + talkSeconds;
									sixMonthContactSeconds = sixMonthContactSeconds + talkSeconds;
								}else if(i >=3 && i < 6){
									sixMonthContactSeconds = sixMonthContactSeconds + talkSeconds;
								}else {
									continue;
								}
							}
						}
						emergency_contact_stats.put("call_time_1month", oneMonthContactSeconds/60);
						emergency_contact_stats.put("call_time_3month", threeMonthContactSeconds/60);
						emergency_contact_stats.put("call_time_6month", sixMonthContactSeconds/60);
						emergency_contact_stats.put("call_count_1month", callRecord.getString("contact_1m"));
						emergency_contact_stats.put("call_count_3month", callRecord.getString("contact_3m"));
						emergency_contact_stats.put("call_count_6month", callRecord.getString("contact_3m_plus"));	
						emergency_contact_stats_list.add(emergency_contact_stats);	
					}
					if(StringUtils.equals(contact_number, memberExtend.getEcpPhoneNo2())) {
						emergency_contact_stats.put("ecp", memberExtend.getEcp2());
						emergency_contact_stats.put("contact_number", callRecord.getInteger("talk_cnt").toString());
						JSONArray  monthDetails = callRecord.getJSONArray("month_detail");
						int oneMonthContactSeconds = 0;
						int threeMonthContactSeconds = 0;
						int sixMonthContactSeconds = 0;
						if(!Collections3.isEmpty(monthDetails)) {
							for(int i = 0; i < monthDetails.size(); i++) {
								JSONObject detail = (JSONObject) monthDetails.get(i);
								int talkSeconds = detail.getInteger("talk_seconds");
								if(i == 0) {
									oneMonthContactSeconds = talkSeconds;
									threeMonthContactSeconds = talkSeconds;
									sixMonthContactSeconds = talkSeconds;
								}else if(i > 0 && i < 3) {
									threeMonthContactSeconds = threeMonthContactSeconds + talkSeconds;
									sixMonthContactSeconds = sixMonthContactSeconds + talkSeconds;
								}else if(i >=3 && i < 6){
									sixMonthContactSeconds = sixMonthContactSeconds + talkSeconds;
								}else {
									continue;
								}
							}
						}
						emergency_contact_stats.put("call_time_1month", oneMonthContactSeconds/60);
						emergency_contact_stats.put("call_time_3month", threeMonthContactSeconds/60);
						emergency_contact_stats.put("call_time_6month", sixMonthContactSeconds/60);
						emergency_contact_stats.put("call_count_1month", callRecord.getString("contact_1m"));
						emergency_contact_stats.put("call_count_3month", callRecord.getString("contact_3m"));
						emergency_contact_stats.put("call_count_6month", callRecord.getString("contact_3m_plus"));	
						emergency_contact_stats_list.add(emergency_contact_stats);	
					
					}
					if(StringUtils.equals(contact_number, memberExtend.getEcpPhoneNo3())) {
						emergency_contact_stats.put("ecp", memberExtend.getEcp3());
						emergency_contact_stats.put("contact_number", callRecord.getInteger("talk_cnt").toString());
						JSONArray  monthDetails = callRecord.getJSONArray("month_detail");
						int oneMonthContactSeconds = 0;
						int threeMonthContactSeconds = 0;
						int sixMonthContactSeconds = 0;
						if(!Collections3.isEmpty(monthDetails)) {
							for(int i = 0; i < monthDetails.size(); i++) {
								JSONObject detail = (JSONObject) monthDetails.get(i);
								int talkSeconds = detail.getInteger("talk_seconds");
								if(i == 0) {
									oneMonthContactSeconds = talkSeconds;
									threeMonthContactSeconds = talkSeconds;
									sixMonthContactSeconds = talkSeconds;
								}else if(i > 0 && i < 3) {
									threeMonthContactSeconds = threeMonthContactSeconds + talkSeconds;
									sixMonthContactSeconds = sixMonthContactSeconds + talkSeconds;
								}else if(i >=3 && i < 6){
									sixMonthContactSeconds = sixMonthContactSeconds + talkSeconds;
								}else {
									continue;
								}
							}
						}
						emergency_contact_stats.put("call_time_1month", oneMonthContactSeconds/60);
						emergency_contact_stats.put("call_time_3month", threeMonthContactSeconds/60);
						emergency_contact_stats.put("call_time_6month", sixMonthContactSeconds/60);
						emergency_contact_stats.put("call_count_1month", callRecord.getString("contact_1m"));
						emergency_contact_stats.put("call_count_3month", callRecord.getString("contact_3m"));
						emergency_contact_stats.put("call_count_6month", callRecord.getString("contact_3m_plus"));	
						emergency_contact_stats_list.add(emergency_contact_stats);	
					
					}
				}
				operatorInfo.put("emergency_contact_stats", emergency_contact_stats_list);
			}
			
			//风险联系人统计
			JSONArray risk_contact_stats = new JSONArray();
			for (Object object : special_cate) {
				JSONObject specialPhone = (JSONObject) object;
				JSONObject risk_contact_stats_map = new JSONObject();
				risk_contact_stats_map.put("risk_type", specialPhone.getString("cate"));
				JSONArray monthDetails = specialPhone.getJSONArray("month_detail");
				int call_count_3month = 0;
				int call_time_3month = 0;
				int call_count_6month = 0;
				int call_time_6month = 0;
				if(!Collections3.isEmpty(monthDetails)) {
					for(int i = 0; i < monthDetails.size(); i++) {
						JSONObject detail = (JSONObject) monthDetails.get(i);
						
						int talkCount = detail.getIntValue("talk_cnt");
						int talkSeconds = detail.getIntValue("talk_seconds");
						if(i < 3) {
							call_count_3month = call_count_3month + talkCount;
							call_count_6month = call_count_6month + talkCount;
							call_time_3month = call_time_3month + talkSeconds;
							call_time_6month = call_time_6month + talkSeconds;
						}else if(i >= 3 && i < 6){
							call_count_6month = call_count_6month + talkCount;
							call_time_6month = call_time_6month + talkSeconds;
						}else {
							continue;
						}
					}
				}
				risk_contact_stats_map.put("call_count_3month", call_count_3month);
				risk_contact_stats_map.put("call_time_3month", call_time_3month/60);
				risk_contact_stats_map.put("call_count_6month", call_count_6month);
				risk_contact_stats_map.put("call_time_6month", call_time_6month/60);
				risk_contact_stats.add(risk_contact_stats_map);
			}
			operatorInfo.put("risk_contact_stats", risk_contact_stats);
			
			//全部联系统计
			//三月内通话次数
			int call_count_3month = 0;
			//三月内主动通话次数
			int call_count_active_3month = 0;
			//三月内被动通话次数
			int call_count_passive_3month = 0;
			//三月内通话号码数量
			int contact_count_3month = 0;
			//三月内主动通话号码数量
			int contact_count_active_3month =0;
			//三月内被动通话号码数量
			int contact_count_passive_3month = 0;
			//三月内通话时长
			int call_time_3month = 0;
			
			int call_count_6month = 0;
			int call_count_active_6month = 0;
			int call_count_passive_6month = 0;
			int contact_count_6month = 0;
			int contact_count_active_6month = 0;
			int contact_count_passive_6month = 0;
			int call_time_6month = 0;
			if(!Collections3.isEmpty(monthly_consumption)) {
				for(int i = 0; i < monthly_consumption.size(); i++) {
					JSONObject detail = (JSONObject) monthly_consumption.get(i);
					int talkCount = detail.getIntValue("talk_cnt");
					int talkSeconds = detail.getIntValue("talk_seconds");
					int callCount = detail.getIntValue("call_cnt");
					int calledCount = detail.getIntValue("called_cnt");
					if(i < 3) {
						call_count_3month = call_count_3month + talkCount;
						call_count_active_3month = call_count_active_3month + callCount;
						call_count_passive_3month = call_count_passive_3month + calledCount;
						call_time_3month = call_time_3month + talkSeconds;
						
						call_count_6month = call_count_6month + talkCount;
						call_count_active_6month = call_count_active_3month + callCount;
						call_count_passive_6month = call_count_passive_6month + calledCount;
						call_time_6month = call_time_6month + talkSeconds;
					}
				}
			}
			for (Object object : call_log) {
				JSONObject callRecord = (JSONObject) object;
				String phoneInfo = callRecord.getString("phone_info");
				String collectionCall = "催收类";
				if(StringUtils.equals(phoneInfo, collectionCall)) {
					suspect_collection_contact_count_passive_6month++;
					suspect_collection_call_count_passive_6month++;
				}
				int callCount = callRecord.getIntValue("call_cnt");
				int calledCount = callRecord.getIntValue("called_cnt");
				boolean hasCallContactInThreeMonth = false;
				boolean hasActiveCallContactInThreeMonth = false;
				boolean hasPassiveCallContactInThreeMonth = false;
				boolean hasCallContactInSixMonth = false;
				boolean hasActiveCallContactInSixMonth = false;
				boolean hasPassiveCallContactInSixMonth = false;
				if(!(callCount == 0 && calledCount == 0)) {
					JSONArray monthDetails = callRecord.getJSONArray("month_detail");
					for (Object object2 : monthDetails) {
						//遍历每个人的月通话记录
						JSONObject detail = (JSONObject) object2;
						int call_cnt = detail.getIntValue("call_cnt");
						int called_cnt = detail.getIntValue("called_cnt");
						String callMonth = detail.getString("month");
						String nowMonth = DateUtils.getDate("yyyy-MM");
						String threeMonthBefore = CalendarUtil.addMonth(nowMonth, -3);
						String sixMonthBefore = CalendarUtil.addMonth(nowMonth, -6);
						if(callMonth.compareTo(threeMonthBefore) >= 0) {
							//三月内
							hasCallContactInThreeMonth = true;
							hasCallContactInSixMonth = true;
							if(call_cnt != 0) {
								hasActiveCallContactInThreeMonth = true;
								hasActiveCallContactInSixMonth = true;
							}else if(called_cnt != 0){
								hasPassiveCallContactInThreeMonth = true;
								hasPassiveCallContactInSixMonth = true;
							}
						}else if(callMonth.compareTo(threeMonthBefore) < 0 && callMonth.compareTo(sixMonthBefore) >=0) {
							//三月-六月内
							hasCallContactInSixMonth = true;
							if(call_cnt != 0) {
								hasActiveCallContactInSixMonth = true;
							}else if(called_cnt != 0){
								hasPassiveCallContactInSixMonth = true;
							}
						}
					}
				}
				if(hasCallContactInThreeMonth) {
					contact_count_3month++;
				}
				if(hasActiveCallContactInThreeMonth) {
					contact_count_active_3month++;
				}
				if(hasPassiveCallContactInThreeMonth) {
					contact_count_passive_3month++;
				}
				if(hasCallContactInSixMonth) {
					contact_count_6month++;
				}
				if(hasActiveCallContactInSixMonth) {
					contact_count_active_6month++;
				}
				if(hasPassiveCallContactInSixMonth) {
					contact_count_passive_6month++;
				}
			}
			JSONObject all_contact_stats = new JSONObject();
			all_contact_stats.put("call_count_3month", call_count_3month);
			all_contact_stats.put("call_count_active_3month", call_count_active_3month);
			all_contact_stats.put("call_count_passive_3month", call_count_passive_3month);
			all_contact_stats.put("contact_count_3month", contact_count_3month);
			all_contact_stats.put("contact_count_active_3month", contact_count_active_3month);
			all_contact_stats.put("contact_count_passive_3month", contact_count_passive_3month);	
			all_contact_stats.put("call_time_3month", call_time_3month);
				
			all_contact_stats.put("call_count_6month", call_count_6month);
			all_contact_stats.put("call_count_active_6month", call_count_active_6month);
			all_contact_stats.put("call_count_passive_6month", call_count_passive_6month);
			all_contact_stats.put("contact_count_6month", contact_count_6month);
			all_contact_stats.put("contact_count_active_6month", contact_count_active_6month);
			all_contact_stats.put("contact_count_passive_6month", contact_count_passive_6month);	
			all_contact_stats.put("call_time_6month", call_time_6month);
	
			operatorInfo.put("all_contact_stats",all_contact_stats);
			
			contact_suspect_collection_analysis.put("suspect_collection_contact_count_passive_6month", suspect_collection_contact_count_passive_6month);
			contact_suspect_collection_analysis.put("suspect_collection_call_count_passive_6month", suspect_collection_call_count_passive_6month);
			operatorInfo.put("contact_suspect_collection_analysis", contact_suspect_collection_analysis);
			
			//消费统计
			BigDecimal consumAmountThreeMonth = BigDecimal.ZERO;
			BigDecimal consumAmountSixMonth = BigDecimal.ZERO;
			JSONObject consumption_stats = new JSONObject();
			for(int i = 0; i < monthly_consumption.size();i++) {
				JSONObject monthConsum = (JSONObject)monthly_consumption.get(i);
				String amount = monthConsum.getString("call_consumption");
				if(i < 3) {
					if(StringUtils.isNotBlank(amount)) {
						consumAmountThreeMonth = consumAmountThreeMonth.add(new BigDecimal(amount));
						consumAmountSixMonth = consumAmountSixMonth.add(new BigDecimal(amount));
					}
				}if(i < 6) {
					if(StringUtils.isNotBlank(amount)) {
						consumAmountSixMonth = consumAmountSixMonth.add(new BigDecimal(amount));
					}
				}
				
			}
			consumption_stats.put("consume_amount_3month", StringUtils.decimalToStr(consumAmountThreeMonth, 2));
			consumption_stats.put("consume_amount_6month", StringUtils.decimalToStr(consumAmountSixMonth, 2));
			
			JSONObject task_data_obj = JSONObject.parseObject(tianji.getTaskData());
			JSONObject data = task_data_obj.getJSONObject("data");
			JSONArray dataList = data.getJSONArray("data_list");
			for (Object object : dataList) {
				JSONObject dataJson = (JSONObject) object;
				JSONArray billdata = dataJson.getJSONArray("billdata");
				JSONObject bill = (JSONObject) billdata.get(0);
				String package_fee = bill.getString("package_fee");
				consumption_stats.put("package_fee", package_fee);
			}
			operatorInfo.put("consumption_stats", consumption_stats);
			
			//静默活跃统计
			int silence_day_0call_3month = 0; 
			int silence_day_0call_6month = 0;
			int continueSilentMoreThan3Days = 1;
			int continueSilentOver3DaysCountInThreeMonth = 0;
			int continueSilentOver3DaysCountInSixMonth = 0;
			double silence_day_0call_ratio_3month = 0;
			int longest_silence_day_0call_3month = 0;
			int longest_silence_day_0call_6month = 0;
			int monthly_silent_days_3month = 0;
			int monthly_silent_days_6month = 0;
			JSONObject silent_days = user_portrait.getJSONObject("silent_days");
			JSONArray silent_details = silent_days.getJSONArray("silent_detail");
			for (int i = 0;i < silent_details.size(); i++) {
				String silentDate = (String) silent_details.get(i);
				String silentMonth = silentDate.substring(0,7);
				String nowMonth = DateUtils.getDate("yyyy-MM");
				String threeMonthBefore = CalendarUtil.addMonth(nowMonth, -3);
				String sixMonthBefore = CalendarUtil.addMonth(nowMonth, -6);
				if(i != silent_details.size()-1 && silent_details.size() >= 3) {
					String nextSilentDate = (String) silent_details.get(i+1);
					String expectNextDate = CalendarUtil.addDay(silentDate, 1);
					if(StringUtils.equals(nextSilentDate, expectNextDate)) {
						continueSilentMoreThan3Days++;
					}else {
						if(continueSilentMoreThan3Days >= 3) {
							if(silentMonth.compareTo(threeMonthBefore) >= 0) {
							continueSilentOver3DaysCountInThreeMonth++;
							continueSilentOver3DaysCountInSixMonth++;
							}else if(silentMonth.compareTo(threeMonthBefore) < 0 && silentMonth.compareTo(sixMonthBefore) >=0){
								continueSilentOver3DaysCountInSixMonth++;
							}
						}
						continueSilentMoreThan3Days = 1;
					}
				}
				if(silentMonth.compareTo(threeMonthBefore) >= 0) {
					silence_day_0call_3month++;
					silence_day_0call_6month++;
				}else if(silentMonth.compareTo(threeMonthBefore) < 0 && silentMonth.compareTo(sixMonthBefore) >=0){
					silence_day_0call_3month++;
				}
				int maxSilentDays = silent_days.getIntValue("max_interval");
				int monthlyAvgSilentDays = silent_days.getIntValue("monthly_avg_days");
				monthly_silent_days_3month = monthlyAvgSilentDays;
				monthly_silent_days_6month = monthlyAvgSilentDays;
				longest_silence_day_0call_3month = maxSilentDays;
				longest_silence_day_0call_6month = maxSilentDays;
				JSONArray maxSilentDateList = silent_days.getJSONArray("max_detail");
				for (Object object : maxSilentDateList) {
					String maxSilentDate = (String) object;
					String continueSilentEndMonth = maxSilentDate.substring(11).substring(0,7);
					if(continueSilentEndMonth.compareTo(threeMonthBefore) >= 0) {
						silence_day_0call_ratio_3month = Double.valueOf(maxSilentDays) / 90;
					}
				}
			}
			JSONObject active_silence_stats = new JSONObject();
			//近3月通话活跃天数
			active_silence_stats.put("longest_silence_day_0call_3month", longest_silence_day_0call_3month);
			active_silence_stats.put("longest_silence_day_0call_6month", longest_silence_day_0call_6month);
			active_silence_stats.put("monthly_silent_days_3month", monthly_silent_days_3month);
			active_silence_stats.put("monthly_silent_days_6month", monthly_silent_days_6month);
//			active_silence_stats.put("active_day_1call_3month", "");
			//近3月最大连续通话活跃天数
//			active_silence_stats.put("max_continue_active_day_1call_3month", );
			//近3月无通话静默天数
			active_silence_stats.put("silence_day_0call_3month", silence_day_0call_3month);
			//近3月连续无通话和发送短信静默>=3天的次数
			active_silence_stats.put("continue_silence_day_over3_0call_0msg_send_3month", continueSilentOver3DaysCountInThreeMonth);
			//近3月最大连续无通话静默天数比例
			active_silence_stats.put("silence_day_0call_ratio_3month", new BigDecimal(silence_day_0call_ratio_3month).setScale(2).doubleValue());
			//近3月最大连续无主叫通话静默天数比例
//			active_silence_stats.put("silence_day_0call_active_ratio_3month",);
			//近6月通话活跃天数
//			active_silence_stats.put("active_day_1call_6month", );
//			//近6月最大连续通话活跃天数
//			active_silence_stats.put("max_continue_active_day_1call_6month", );
			//近6月无通话静默天数
			active_silence_stats.put("silence_day_0call_6month", silence_day_0call_6month);
			//近6月连续无通话和发送短信静默>=3天的次数
			active_silence_stats.put("continue_silence_day_over3_0call_0msg_send_6month", continueSilentOver3DaysCountInSixMonth);
			//近6月最大连续无主叫通话静默天数比例
//			active_silence_stats.put("silence_day_0call_active_ratio_6month",);
			operatorInfo.put("active_silence_stats", active_silence_stats);
			
			//时间段通话统计
			JSONObject call_duration_stats_2hour_obj = new JSONObject();
			operatorInfo.put("call_duration_stats_2hour_obj", call_duration_stats_2hour_obj);
			
			//夜间通话统计
			
			JSONObject night_activities = user_portrait.getJSONObject("night_activities");
			//月均分钟数
			double monthly_avg_seconds  = Double.valueOf(night_activities.getFloat("monthly_avg_seconds"));
			//月均占比
			double monthly_avg_seconds_ratio = Double.valueOf(night_activities.getFloat("monthly_avg_seconds_ratio"));
			//夜间通话占比
			double night_talk_seconds_ratio = Double.valueOf(night_activities.getFloat("night_talk_seconds_ratio"));
			//夜间短信占比
			double night_msg_ratio = Double.valueOf(night_activities.getFloat("night_msg_ratio"));
			
			night_activities.put("monthly_avg_seconds", monthly_avg_seconds);
			night_activities.put("monthly_avg_seconds_ratio", monthly_avg_seconds_ratio);
			night_activities.put("night_talk_seconds_ratio", night_talk_seconds_ratio);
			night_activities.put("night_msg_ratio", night_msg_ratio);
			operatorInfo.put("night_activities", night_activities);
			
			//联系人所在地TOP5
			JSONArray contact_area_top5 = new JSONArray();
			int contactsCount = call_log.size();
			TreeMap<String, String> sortMap = new TreeMap<String,String>();
			for (Object object : contact_area_analysis) {
				JSONObject contactArea = (JSONObject) object;
				int contactPhoneCnt = contactArea.getIntValue("contact_phone_cnt");
				String area = contactArea.getString("area");
				sortMap.put(contactPhoneCnt+"_"+area, area);
			}
			NavigableMap<String, String>  descSortMap = sortMap.descendingMap();
			int count = 0;
			for (Map.Entry<String, String> entry : descSortMap.entrySet()) {
				if(count > 5) {
					break;
				}
				JSONObject contact_area = new JSONObject();
				String countAndArea = entry.getKey();
				String[] countAndAreaArray = countAndArea.split("_");
				contact_area.put("contact_area", countAndAreaArray[1]);
				contact_area.put("contact_area_count", countAndAreaArray[0]);
				contact_area.put("contact_area_ratio",StringUtils.decimalToStr(new BigDecimal(Double.valueOf(countAndAreaArray[0])/contactsCount), 2)+"%");
				contact_area_top5.add(contact_area);
				count++;
			}
			operatorInfo.put("contact_area_top5", contact_area_top5);
			
			//通话记录详情
			List<Map<String, String>> callRecordDetails = new ArrayList<Map<String,String>>();
			for (Object object : dataList) {
				JSONObject dataJson = (JSONObject) object;
				JSONArray telData = dataJson.getJSONArray("teldata");
				for (Object object2 : telData) {
					JSONObject monthTelRecord = (JSONObject) object2;
					JSONArray monthTelDetails = monthTelRecord.getJSONArray("items");
					for (Object object3 : monthTelDetails) {
						JSONObject telDetail = (JSONObject) object3;
						Map<String, String> callRecordDetail = new HashMap<String,String>();
						//呼叫类型 1：主叫2：被叫3：未识别状态4：转呼
						String call_type = telDetail.getString("call_type");
						//对方号码
						String receive_phone = telDetail.getString("receive_phone");
						//发起通话时间
						String call_time = telDetail.getString("call_time");
						//通话时长
						String trade_time = telDetail.getString("trade_time");
						callRecordDetail.put("call_type", call_type);
						callRecordDetail.put("receive_phone", receive_phone);
						callRecordDetail.put("call_time", call_time);
						callRecordDetail.put("trade_time", trade_time);
						callRecordDetails.add(callRecordDetail);
					}
				}
			}
			Collections.sort(callRecordDetails , new Comparator<Map<String, String>>() {
		        @Override
		        public int compare(Map<String, String> o1, Map<String, String> o2) {
		            Integer o1Value = Integer.valueOf(o1.get("call_time").toString());
		            Integer o2Value = Integer.valueOf(o2.get("call_time").toString());
		            return o2Value.compareTo(o1Value);
		        }
		    });
			operatorInfo.put("callRecordDetails", JSONArray.parseArray(JSONObject.toJSONString(callRecordDetails)));
			
			RcCaDataV2 caDataV2 = new RcCaDataV2();
			caDataV2.setPhoneNo(tianji.getPhoneNo());
			caDataV2.setType(RcCaDataV2.Type.yys);
			caDataV2.setStatus(RcCaDataV2.Status.processing);
			List<RcCaDataV2> caDataList =  rcCaDataService.findList(caDataV2);
			RcCaDataV2 caData = null;
			if(!Collections3.isEmpty(caDataList)) {
				caData = caDataList.get(0);
			}else {
				caData = new RcCaDataV2();
			}
			caData.setStatus(RcCaDataV2.Status.success);
			
			caData.setContent(JSONObject.toJSONString(operatorInfo));
			caData.setPhoneNo(member.getUsername());
			caData.setIdNo(member.getIdNo());
			caData.setName(member.getName());
			caData.setType(RcCaDataV2.Type.yys);
			caData.setProvider(RcCaDataV2.Provider.tj);
			rcCaDataService.save(caData);
			
			tianji.setDataStatus(RcTianji.DataStatus.data_arranged);
			rcTjService.save(tianji);
			
			// 认证成功后添加运营商认证项			
			if(StringUtils.equals(member.getUsername(), phone)) {
				int verifiedList = member.getVerifiedList();
				verifiedList = VerifiedUtils.addVerified(verifiedList, 6);
				member.setVerifiedList(verifiedList);
				RedisUtils.put("memberInfo" + member.getId(), "operatorStatus", "1");
			};
			memberService.save(member);
			
		} catch (NullPointerException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}

	}
	
	
	
	private void xuexinwang(RcTianji rcTianji) {
		//学信网
		try {
			JSONObject task_data_obj = JSONObject.parseObject(rcTianji.getTaskData());
			Member member = memberService.get(rcTianji.getOrgId());
			JSONObject data_obj = task_data_obj.getJSONObject("data");
			JSONArray dataList = data_obj.getJSONArray("data_list");
			JSONObject dataJson = dataList.getJSONObject(0);
			JSONArray edu_infoList = dataJson.getJSONArray("edu_info");
			JSONObject user_info = dataJson.getJSONObject("user_info");
			JSONObject edu_info = null;
			if(edu_infoList.size() == 1) {
				edu_info = edu_infoList.getJSONObject(0);
			}else {
				String degree = "";
				Map<String, String> degreeMap = new TreeMap<String,String>();
				degreeMap.put("0其他", "0");
				degreeMap.put("1专科", "0");
				degreeMap.put("2成高", "0");
				degreeMap.put("3本科", "0");
				degreeMap.put("4硕士", "0");
				degreeMap.put("5博士", "0");
				for (Object object : edu_infoList) {
					edu_info.getString("education_level");
					edu_info = (JSONObject) object;
					degree = edu_info.getString("format_education_level");
					//比较degree拿到最高学历
					switch (degree) {
					case "专科":
						degreeMap.put("1专科", edu_info.toString());
						break;
					case "博士研究生":
						degreeMap.put("5博士", edu_info.toString());
						break;
					case "成人高考":
						degreeMap.put("2成高", edu_info.toString());
						break;
					case "本科":
						degreeMap.put("3本科", edu_info.toString());
						break;
					case "硕士研究生":
						degreeMap.put("4硕士", edu_info.toString());
						break;
					case "第二学士学位":
						degreeMap.put("3本科", edu_info.toString());
						break;
					case "其他":
						degreeMap.put("0其他", edu_info.toString());
						break;
					default:
						break;
					}
				}
				for(Map.Entry<String, String> entry :degreeMap.entrySet()) {
					if(!StringUtils.equals(entry.getValue(), "0")) {
						edu_info = JSONObject.parseObject(entry.getValue());
					}
					continue;
				}
			}
			//毕业院校
			String school = edu_info.getString("graduated_school");
			//专业名称
			String major = edu_info.getString("major");
			//学历层次
			String edu_level = edu_info.getString("education_level");
			//学习形式
			String edu_form = edu_info.getString("learning_form");
			//入学时间
			String entrance_date = edu_info.getString("admission_date");
			//毕业时间
			String graduate_date = edu_info.getString("graduation_date");
			JSONObject contentJson = new JSONObject();
			contentJson.put("realname", user_info.getString("name"));
			contentJson.put("card_id", user_info.getString("card_no"));
			contentJson.put("department", major);
			contentJson.put("school", school);
			contentJson.put("edu_level", edu_level);
			contentJson.put("edu_form", edu_form);
			contentJson.put("entrance_date", entrance_date);
			contentJson.put("graduate_date", graduate_date);
			
			RcCaDataV2 caDataV2 = new RcCaDataV2();
			caDataV2.setPhoneNo(rcTianji.getPhoneNo());
			caDataV2.setType(RcCaDataV2.Type.xxw);
			caDataV2.setStatus(RcCaDataV2.Status.processing);
			List<RcCaDataV2> caDataList =  rcCaDataService.findList(caDataV2);
			RcCaDataV2 caData = null;
			if(!Collections3.isEmpty(caDataList)) {
				caData = caDataList.get(0);
			}else {
				caData = new RcCaDataV2();
			}
			caData.setStatus(RcCaDataV2.Status.success);
			caData.setContent(contentJson.toString());
			caData.setPhoneNo(member.getUsername());
			caData.setIdNo(member.getIdNo());
			caData.setName(member.getName());
			caData.setType(RcCaDataV2.Type.xxw);
			caData.setProvider(RcCaDataV2.Provider.tj);
			rcCaDataService.save(caData);
			
			rcTianji.setDataStatus(RcTianji.DataStatus.data_arranged);
			rcTjService.save(rcTianji);
			
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
	
	
	
	private void wangyin(RcTianji rcTianji) {
		try {
			JSONArray listJie = new JSONArray();
			JSONArray listXin = new JSONArray();
			JSONObject task_data_obj = JSONObject.parseObject(rcTianji.getTaskData());
			JSONArray infoLists = task_data_obj.getJSONArray("card_lists");
			for (Object object : infoLists) {
				JSONObject cardInfo = (JSONObject) object;
				JSONArray bill_flowList = cardInfo.getJSONArray("bill_flow");
				JSONArray cards = cardInfo.getJSONArray("card");
				for (Object object2 : cards) {
					JSONObject card = (JSONObject) object2;
					//借记卡总余额 信用卡总额度
					BigDecimal balance = BigDecimal.ZERO;
					//借记卡总收入
					BigDecimal allInc = BigDecimal.ZERO;
					//借记卡总支出
					BigDecimal allOuc = BigDecimal.ZERO;
					//月平均收入
					BigDecimal avaInc = BigDecimal.ZERO;
					//月平均支出
					BigDecimal avaOut = BigDecimal.ZERO;
					String debitCardType = "debit";
					String creditCardType = "credit";
					String cardType = card.getString("type");
					int billFlowListSize = bill_flowList.size();
					if (StringUtils.equals(cardType, debitCardType)) {
						//一般情况下当前月份天数不满，从上一个月开始统计
						for (int i = 1; i < billFlowListSize; i++) {
							//遍历账单月列表
							JSONObject bill_flow = (JSONObject) bill_flowList.get(i);
							JSONArray flowList = bill_flow.getJSONArray("flow");
							for (Object object3 : flowList) {
								//遍历每月账单列表
								JSONObject flow = (JSONObject) object3;
								//存入,单位：元
								String deposit = flow.getString("deposit");
								//支出,单位：元
								String payment = flow.getString("payment");
								if (StringUtils.isNotBlank(deposit)) {
									allInc = allInc.add(new BigDecimal(deposit));
								}
								if (StringUtils.isNotBlank(payment)) {
									allOuc = allOuc.add(new BigDecimal(payment));
								}
							}
						}
						BigDecimal monthNum = new BigDecimal(billFlowListSize - 1);
						if(monthNum.compareTo(BigDecimal.ZERO) > 0) {
							avaInc = allInc.divide(monthNum,RoundingMode.HALF_UP);
							avaOut = allOuc.divide(monthNum,RoundingMode.HALF_UP);
						}else {
							avaInc = allInc;
							avaOut = allOuc;
						}
						//当前余额
						JSONObject today_bill_flow = (JSONObject) bill_flowList.get(0);
						JSONArray todayFlowList = today_bill_flow.getJSONArray("flow");
						JSONObject currentFlow = (JSONObject) todayFlowList.get(0);
						String balanceStr = StringUtils.decimalToStr(new BigDecimal(currentFlow.getString("balance")), 2);
						
						JSONObject debitCardInfo = new JSONObject();
						debitCardInfo.put("name", card.getString("name"));
						debitCardInfo.put("deposit_bank", card.getString("bank_name"));
						debitCardInfo.put("status", card.getString("card_status"));
						debitCardInfo.put("account", card.getString("card_no"));
						debitCardInfo.put("balance", balanceStr);
						debitCardInfo.put("avabalance", balanceStr);
						debitCardInfo.put("inc", StringUtils.decimalToStr(avaInc, 2));
						debitCardInfo.put("out", StringUtils.decimalToStr(avaOut, 2));
						//最低偿还能力
						debitCardInfo.put("xinMin", StringUtils.decimalToStr(avaInc.multiply(new BigDecimal(0.3)),2));
						//最高偿还能力
						debitCardInfo.put("xinMax", StringUtils.decimalToStr(avaInc.multiply(new BigDecimal(0.5)),2));
						listJie.add(debitCardInfo);
					} else if (StringUtils.equals(cardType, creditCardType)) {
						JSONObject creditCardInfo = new JSONObject();
						creditCardInfo.put("name", card.getString("name"));
						creditCardInfo.put("deposit_bank", card.getString("bank_name"));
						creditCardInfo.put("status", "");
						creditCardInfo.put("account", card.getString("card_no"));
						//信用卡额度 单位：分
						BigDecimal creditLimit =  new BigDecimal(Double.valueOf(card.getString("credit_limit"))/100);
						balance = balance.compareTo(creditLimit) > 0 ? balance:creditLimit;
						String balanceStr = StringUtils.decimalToStr(balance, 2);
						creditCardInfo.put("balance", balanceStr);
						creditCardInfo.put("avabalance", balanceStr);
						//消费总金额
						BigDecimal shoppingAmount = BigDecimal.ZERO;
						//还款总金额
						BigDecimal repayAmount = BigDecimal.ZERO;
						//三月内逾期次数
						int threrover = 0;
						//六月内逾期次数
						int sixover = 0;
						for (int i = 0; i < billFlowListSize; i++) {
							JSONObject bill_flow = (JSONObject) bill_flowList.get(i);
							JSONArray flowList = bill_flow.getJSONArray("flow");
							for (Object object3 : flowList) {
								JSONObject flow = (JSONObject) object3;
								//账单交易金额，消费为正， 还款为负 单位：分
								String rmb_amount = flow.getString("rmb_amount");
								//账单类型
								String category = flow.getString("category");
								//还款
								String repayType = "PAYMENTS";
								//消费
								String shoppingType = "SHOPPING";
								//逾期
								String overdueType = "OVERDUEPAYMENT";
								if (StringUtils.isNotBlank(rmb_amount) && StringUtils.equals(category, shoppingType)) {
									BigDecimal rmbAmount = new BigDecimal(Double.valueOf(rmb_amount)/100);
									shoppingAmount = shoppingAmount.add(rmbAmount);
								}else if(StringUtils.isNotBlank(rmb_amount) && StringUtils.equals(category, repayType)) {
									BigDecimal rmbAmount = new BigDecimal(Double.valueOf(rmb_amount)/100);
									repayAmount = repayAmount.add(rmbAmount.negate());
								}else {
									continue;
								}
								if(StringUtils.equals(category, overdueType)) {
									if (i < 4) {
										threrover++;
										sixover++;
									} else if (i < 7) {
										sixover++;
									}
								}
							}
						}
						BigDecimal monthNum = new BigDecimal(billFlowListSize - 1);
						//月均消费金额
						String inc = "";
						//月均还款金额
						String out = "";
						if(monthNum.compareTo(BigDecimal.ZERO) > 0) {
							inc = StringUtils.decimalToStr(shoppingAmount.divide(monthNum,RoundingMode.HALF_UP), 2);
							out = StringUtils.decimalToStr(repayAmount.divide(monthNum,RoundingMode.HALF_UP), 2);
						}else {
							inc = StringUtils.decimalToStr(shoppingAmount, 2);
							out = StringUtils.decimalToStr(repayAmount, 2);
						}
						creditCardInfo.put("inc", inc);
						creditCardInfo.put("out", out);
						creditCardInfo.put("threrover", threrover + "");
						creditCardInfo.put("sixover", sixover + "");
						listXin.add(creditCardInfo);
					} else {
						continue;
					}
				}
			}
			JSONObject contentJson = new JSONObject();
			contentJson.put("listjie", listJie);
			contentJson.put("listxin", listXin);
			
			Member member = memberService.get(rcTianji.getOrgId());
			RcCaDataV2 caDataV2 = new RcCaDataV2();
			caDataV2.setPhoneNo(rcTianji.getPhoneNo());
			caDataV2.setType(RcCaDataV2.Type.wangyin);
			caDataV2.setStatus(RcCaDataV2.Status.processing);
			List<RcCaDataV2> caDataList =  rcCaDataService.findList(caDataV2);
			RcCaDataV2 caData = null;
			if(!Collections3.isEmpty(caDataList)) {
				caData = caDataList.get(0);
			}else {
				caData = new RcCaDataV2();
			}
			caData.setStatus(RcCaDataV2.Status.success);
			caData.setContent(JSONObject.toJSONString(contentJson));
			caData.setPhoneNo(member.getUsername());
			caData.setIdNo(member.getIdNo());
			caData.setName(member.getName());
			caData.setType(RcCaDataV2.Type.wangyin);
			caData.setProvider(RcCaDataV2.Provider.tj);
			rcCaDataService.save(caData);

			rcTianji.setDataStatus(RcTianji.DataStatus.data_arranged);
			rcTjService.save(rcTianji);

			// 认证成功后添加银行账单认证项
			int verifiedList = member.getVerifiedList();
			verifiedList = VerifiedUtils.addVerified(verifiedList, 7);
			member.setVerifiedList(verifiedList);
			RedisUtils.put("memberInfo" + member.getId(), "bankTrxStatus", "1");
			memberService.save(member);
		} catch (NullPointerException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
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
