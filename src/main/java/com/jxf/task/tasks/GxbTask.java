package com.jxf.task.tasks;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.jxf.rc.entity.RcCaData;
import com.jxf.rc.entity.RcCaDataV2;
import com.jxf.rc.entity.RcGxb;
import com.jxf.rc.entity.RcSjmh;
import com.jxf.rc.service.RcCaDataServiceV2;
import com.jxf.rc.service.RcGxbService;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.JSONUtil;
import com.jxf.svc.utils.ObjectUtils;
import com.jxf.svc.utils.StringUtils;

@DisallowConcurrentExecution
public class GxbTask implements Job {

	private static Logger log = LoggerFactory.getLogger(GxbTask.class);
	@Autowired
	private RcCaDataServiceV2 rcCaDataService;
	@Autowired
	private RcGxbService gxbService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberExtendService memberExtendService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		data_proc(); 
		report_proc();
	}
	
	private void data_proc() {
		RcGxb gxb = new RcGxb();
		gxb.setProdType(RcGxb.ProdType.app);
		gxb.setDataStatus(RcGxb.DataStatus.data_created);
    	List<RcGxb> rcGxbList = gxbService.findListWithoutEmpNo(gxb);
    	
       for(RcGxb rcGxb:rcGxbList) {
			try {
	
				String taskData = FileUtils.readFileToString(new File(Global.getConfig("uploadPath")+rcGxb.getDataPath()),"utf-8");
				rcGxb.setTaskData(taskData);
				switch(rcGxb.getAuthType()){
			    case shebao :
			    	shebao(rcGxb);
			       break; 
			    case gongjijin :
			    	gongjijin(rcGxb);
			       break; 
			    case taobao :
			    	taobao(rcGxb);
			       break; 
			    case yunyingshang :
			    	yunyingshang(rcGxb);
			       break;
			    case xuexinwang :
			    	xuexinwang(rcGxb);
			       break;
			    case wangyin :
			    	wangyin(rcGxb);
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
		
		RcGxb gxb = new RcGxb();
		gxb.setProdType(RcGxb.ProdType.app);
		gxb.setReportStatus(RcGxb.ReportStatus.report_created);
    	List<RcGxb> rcGxbList = gxbService.findList(gxb);
    	
        for(RcGxb rcGxb:rcGxbList) {
			try {
	
				String reportData = FileUtils.readFileToString(new File(Global.getConfig("uploadPath")+rcGxb.getReportPath()),"utf-8");
				rcGxb.setReportData(reportData);
				switch(rcGxb.getAuthType()){
			    case yunyingshang :
//			    	yunyingshang_report(rcGxb);
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
	
	private void shebao(RcGxb rcGxb) {
		try {
			//社保
			Map<String,Object> map = new HashMap<String,Object>();
			Map<String,Object> flowMap = new HashMap<String,Object>();
			JSONObject taskData = JSONUtil.getJSONFromString(rcGxb.getTaskData());
			JSONObject baseInfo = taskData.getJSONObject("baseInfo");
			JSONArray billRecord = taskData.getJSONArray("billRecordDTOS");
			String insureDay = (String) baseInfo.get("workDate");//起缴日期
			String workDay = (String) baseInfo.get("workDate");//参加工作日期
			String comName = (String) baseInfo.get("company");//单位名称
			String liveAddr = (String) baseInfo.get("address");//家庭住址
			map.put("insureDay", insureDay);
			map.put("workDay", workDay);
			map.put("comName", comName);
			map.put("liveAddr", liveAddr);
			//详情
			for (int i = 0; i < billRecord.size(); i++) {
				Map<String,Object> detailMap = new HashMap<String,Object>();
				JSONObject flowDetail = billRecord.getJSONObject(i);
				String payDate = (String) flowDetail.get("payDate");//缴纳时间
				String baseRmb = (String) flowDetail.get("baseAmount");//缴费基数
				String comRmb = (String) flowDetail.get("companyPay");//单位缴费
				String perRmb = (String) flowDetail.get("personalPay");//个人缴费
				String payCond = (String) flowDetail.get("payStatus");//缴费状态
				String flowType = (String) flowDetail.get("insuranceType");//险种
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
			log.debug("====json===="+json);
			
			Member	member = memberService.get(rcGxb.getOrgId());
			RcCaDataV2 caData = new RcCaDataV2();
			caData.setContent(json);
			caData.setPhoneNo(member.getUsername());
			caData.setIdNo(member.getIdNo());
			caData.setName(member.getName());
			caData.setType(RcCaDataV2.Type.shebao);
			caData.setProvider(RcCaDataV2.Provider.gxb);
			rcCaDataService.save(caData);
			
			rcGxb.setDataStatus(RcGxb.DataStatus.data_arranged);
			gxbService.save(rcGxb);
			
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
	
	
	private void gongjijin(RcGxb rcGxb) {
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			Map<String,Object> flowMap = new HashMap<String,Object>();
			JSONObject taskData = JSONUtil.getJSONFromString(rcGxb.getTaskData());
			JSONObject baseInfo = taskData.getJSONObject("houseFundBaseInfoDTO ");
			JSONArray billRecord = taskData.getJSONArray("houseFundBillDTOList");
			String fundStatus = (String) baseInfo.get("depositStatus");//缴费状态
			String startDate = (String) baseInfo.get("beginDate");//开户日期
			String lastDate = (String) baseInfo.get("lastBillDate");//最后缴费日期
			String comName = (String) baseInfo.get("corporationName");//缴纳单位名称
			String liveAddr = (String) baseInfo.get("homeAddress");//家庭住址
			map.put("fundStatus", fundStatus);
			map.put("startDate", startDate);
			map.put("lastDate", lastDate);
			map.put("comName", comName);
			map.put("liveAddr", liveAddr);
			//详情
			for (int i = 0; i < billRecord.size(); i++) {
				Map<String,Object> detailMap = new HashMap<String,Object>();
				JSONObject flowDetail = billRecord.getJSONObject(i);
				String endDate = (String) flowDetail.get("billDate");//到账日期
				String payType = (String) flowDetail.get("description");//业务类型
				String inRmb = (String) flowDetail.get("income");//缴存额(元)
				String outRmb = (String) flowDetail.get("outcome");//取出额(元)
				String balRmb = (String) flowDetail.get("balance");//余额(元)
				detailMap.put("endDate", endDate);
				detailMap.put("payType", payType);
				detailMap.put("inRmb", inRmb);
				detailMap.put("outRmb", outRmb);
				detailMap.put("balRmb", balRmb);
				flowMap.putAll(detailMap);
			}
			map.put("flowMap", flowMap);
			String json = JSONUtil.toJson(map);
			log.debug("====json===="+json);
			
			//公积金
			Member	member = memberService.get(rcGxb.getOrgId());
			RcCaDataV2 caData = new RcCaDataV2();
			caData.setContent(json);
			caData.setPhoneNo(member.getUsername());
			caData.setIdNo(member.getIdNo());
			caData.setName(member.getName());
			caData.setType(RcCaDataV2.Type.gjj);
			caData.setProvider(RcCaDataV2.Provider.gxb);
			rcCaDataService.save(caData);
			
			rcGxb.setDataStatus(RcGxb.DataStatus.data_arranged);
			gxbService.save(rcGxb);
			
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
	
	private void taobao(RcGxb rcGxb) {
		// 淘宝
		try {
			JSONObject task_data_obj = JSONObject.parseObject(rcGxb.getTaskData());
			JSONObject baseInfo = task_data_obj.getJSONObject("baseInfo");
			//用户基本信息字段
			JSONObject ecommerceBaseInfo = baseInfo.getJSONObject("ecommerceBaseInfo");
			//收货地址
			JSONArray ecommerceConsigneeAddresses = baseInfo.getJSONArray("ecommerceConsigneeAddresses");
			StringBuilder taoadree = new StringBuilder();
			for (Object object : ecommerceConsigneeAddresses) {
				JSONObject addressInfo = (JSONObject) object;
				//默认收货地址
				if(addressInfo.getIntValue("defaultStatus") == 1) {
					//区域
					String region = addressInfo.getString("region");
					//具体地址
					String address = addressInfo.getString("address");
					taoadree = taoadree.append(region).append(address);
				}
			}
			//淘宝交易记录
			JSONArray taobaoOrders = baseInfo.getJSONArray("taobaoOrders");
			
			Member member = memberService.getByIdNo(rcGxb.getIdNo());
			//淘宝信息
			JSONObject taobaoData = new JSONObject();
			taobaoData.put("real_name", ecommerceBaseInfo.getString("name"));
			taobaoData.put("phoneNo", ecommerceBaseInfo.getString("mobile"));
			taobaoData.put("user_level", ecommerceBaseInfo.getString("taobaoVipLevel"));
			taobaoData.put("vip_count", ecommerceBaseInfo.getInteger("taoScore"));
			taobaoData.put("zmf", "无数据");
			taobaoData.put("jiebei_quota",  "无数据");
			taobaoData.put("credit_quota", ecommerceBaseInfo.getDouble("huabeiAmount"));
			taobaoData.put("fin_act_bal", ecommerceBaseInfo.getDouble("yuebaoBalance"));
			
			int count3M = 0; // 三个月内订单数量
			double sumAmt3M = 0; // 三个月内订单金额

			int count6M = 0; // 六个月内订单数量
			double sumAmt6M = 0; // 六个月内订单金额

						
			double maxPrice3M = 0; // 三个月最贵
			String maxPriceProductName3M = "";
			double maxPrice6M = 0; // 六个月最贵
			String maxPriceProductName6M = "";
			for (Object object : taobaoOrders ) {
				JSONObject order = (JSONObject) object;
				String order_time = "";
				String payTime = order.getString("payTime");
				String createTime = order.getString("createTime");
				double actualFee = order.getDoubleValue("actualFee"); 
				JSONArray subOrders = order.getJSONArray("subOrders");
				if(StringUtils.isNotBlank(payTime)) {
					order_time = payTime;
				}else {
					order_time = createTime;
				}
				if(StringUtils.isNotBlank(order_time)) {
					long pastDays = DateUtils.pastDays(DateUtils.parse(order_time));
					if(pastDays<=90) {
						sumAmt3M = sumAmt3M + actualFee;
						count3M++;	
						for(int j = 0;j<subOrders.size();j++){  
							JSONObject product_obj = subOrders.getJSONObject(j);
							double product_price = product_obj.getDoubleValue("original");
							String product_name = product_obj.getString("itemTitle");
							if (product_price > maxPrice3M) {
								maxPrice3M= product_price;
								maxPriceProductName3M = product_name;
							}	
						}

					}
					if(pastDays<=180) {
						sumAmt6M = sumAmt6M + Double.valueOf(actualFee);
						count6M++;
						for(int j = 0;j<subOrders.size();j++){  
							JSONObject product_obj = subOrders.getJSONObject(j);
							double product_price =product_obj.getDoubleValue("original");
							String product_name = product_obj.getString("itemTitle");
							if (product_price > maxPrice6M) {
								maxPrice6M= product_price;
								maxPriceProductName6M = product_name;
							}	
						}
					}	
				}
			}
			
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
			
			RcCaDataV2 caDataV2 = new RcCaDataV2();
			caDataV2.setPhoneNo(rcGxb.getPhoneNo());
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
			caData.setContent(taobaoData.toString());
			caData.setPhoneNo(member.getUsername());
			caData.setIdNo(member.getIdNo());
			caData.setName(member.getName());
			caData.setType(RcCaDataV2.Type.taobao);	
			caData.setProvider(RcCaDataV2.Provider.gxb);
			rcCaDataService.save(caData);
			
			rcGxb.setDataStatus(RcGxb.DataStatus.data_arranged);
			gxbService.save(rcGxb);
			
									
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
//			rcCaDataService.save(caData);
			
			sjmh.setReportStatus(RcSjmh.ReportStatus.report_arranged);
//			gxbService.save(sjmh);
	
		} catch (NullPointerException e) {
			log.error(Exceptions.getStackTraceAsString(e));
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
	}
	
	private void yunyingshang(RcGxb rcGxb) {
		// 运营商
		try {

			String user_mobile = rcGxb.getPhoneNo();

			JSONObject task_data_obj = JSONObject.parseObject(rcGxb.getTaskData());
			String authPhoneNo = task_data_obj.getString("mobile");
			int reliability = task_data_obj.getIntValue("reliability");
			String state= task_data_obj.getString("state");
			String open_time = task_data_obj.getString("open_time");
			int netAge = CalendarUtil.getIntervalDays(open_time, DateUtils.getDate())/30;
			String province = task_data_obj.getString("province");
			String city = task_data_obj.getString("city");
			
			//账单信息
			JSONArray bills   = task_data_obj.getJSONArray("bills");
			//通话记录信息 
			JSONArray calls   = task_data_obj.getJSONArray("calls");
			//通话费用
			Double callFee = 0D;
			for (Object object : calls) {
				JSONObject monthRecords = (JSONObject) object;
				JSONArray items = monthRecords.getJSONArray("items");
				for (Object object2 : items) {
					JSONObject callRecord = (JSONObject) object2;
					int fee = callRecord.getIntValue("fee");
					callFee = callFee + Double.valueOf(fee)/100;
				}
			}
			//手机号分析
			JSONObject mobileInfo = new JSONObject();
			mobileInfo.put("authPhoneNo", authPhoneNo);
			mobileInfo.put("phone_no_match", StringUtils.equals(authPhoneNo, rcGxb.getPhoneNo())?"是":"否");
			if(reliability == 1) {
				mobileInfo.put("reliability", "是");
			}else if(reliability == 0) {
				mobileInfo.put("reliability", "否");
			}else {
				mobileInfo.put("reliability", "未知");
			}
			mobileInfo.put("state", state);
			mobileInfo.put("netAge", netAge);
			mobileInfo.put("mobile_area", province+city);
			
			//用户信息检测
			String tjReportData = FileUtils.readFileToString(new File(Global.getConfig("uploadPath")+rcGxb.getReportPath()),"utf-8");
			JSONObject report_data_obj = JSONObject.parseObject(tjReportData);
			JSONArray user_info_check = report_data_obj.getJSONArray("user_info_check");
			JSONObject userInofCheck = new JSONObject();
			//查询过该用户的相关企业数
			long searched_org_cnt = 0;
			//直接联系人中黑名单人数
			long contacts_class1_blacklist_cnt = 0;
			//直接联系人人数
			long contacts_class1_cnt = 0;
			
			for (Object object : user_info_check) {
				JSONObject checkInfo = (JSONObject) object;
				JSONObject check_search_info = checkInfo.getJSONObject("check_search_info");
				searched_org_cnt = check_search_info.getLongValue("searched_org_cnt");
				JSONObject check_black_info = checkInfo.getJSONObject("check_black_info");
				contacts_class1_blacklist_cnt = check_black_info.getLong("contacts_class1_blacklist_cnt");
				contacts_class1_cnt = check_black_info.getLongValue("contacts_class1_cnt");
			}
			userInofCheck.put("searched_org_cnt", searched_org_cnt);
			userInofCheck.put("contacts_class1_blacklist_cnt",contacts_class1_blacklist_cnt);
			userInofCheck.put("contacts_class1_cnt", contacts_class1_cnt);
			//用户姓名+身份证是否出现在法院黑名单
			StringBuilder is_name_and_idcard_in_court_black = new StringBuilder();
			JSONArray basic_check_items = report_data_obj.getJSONArray("basic_check_items");
			for (Object object : basic_check_items) {
				JSONObject item = (JSONObject) object;
				if(StringUtils.equals(item.getString("check_item"), "is_name_and_idcard_in_court_black")) {
					is_name_and_idcard_in_court_black = is_name_and_idcard_in_court_black.append(item.getString("result"));
				}
			}
			
			//紧急联系人
			JSONArray emergency_contact_stats = new JSONArray();
			Member member = memberService.findByUsername(rcGxb.getPhoneNo());
			MemberExtend memberExtend = memberExtendService.getByMember(member);
			if(memberExtend!=null && !Collections3.isEmpty(calls)) {
				JSONObject ecp1Info = new JSONObject();
				ecp1Info.put("ecp",  memberExtend.getEcp1());
				JSONObject ecp2Info = new JSONObject();
				ecp2Info.put("ecp", memberExtend.getEcp2());
				JSONObject ecp3Info = new JSONObject();
				ecp3Info.put("ecp", memberExtend.getEcp3());
				for(int i=0;i<calls.size();i++) {
					JSONObject callMonth = (JSONObject) calls.get(i);
					JSONArray items = callMonth.getJSONArray("items");
					int ecp1OneMonthContactSeconds = 0;
					int ecp1ThreeMonthContactSeconds = 0;
					int ecp1SixMonthContactSeconds = 0;
					int ecp2OneMonthContactSeconds = 0;
					int ecp2ThreeMonthContactSeconds = 0;
					int ecp2SixMonthContactSeconds = 0;
					int ecp3OneMonthContactSeconds = 0;
					int ecp3ThreeMonthContactSeconds = 0;
					int ecp3SixMonthContactSeconds = 0;
					for (Object object : items) {
						JSONObject item = (JSONObject) object;
						String phoneNo = item.getString("peer_number");
						int duration = item.getIntValue("duration");
						if(StringUtils.equals(phoneNo, memberExtend.getEcpPhoneNo1())) {
							if(i == 0) {
								ecp1OneMonthContactSeconds = ecp1OneMonthContactSeconds + duration;
								ecp1ThreeMonthContactSeconds = ecp1ThreeMonthContactSeconds + duration;
								ecp1SixMonthContactSeconds = ecp1SixMonthContactSeconds + duration;
							}else if( i < 3) {
								ecp1ThreeMonthContactSeconds = ecp1ThreeMonthContactSeconds + duration;
								ecp1SixMonthContactSeconds = ecp1SixMonthContactSeconds + duration;
							}else if (i < 6){
								ecp1SixMonthContactSeconds =ecp1SixMonthContactSeconds + duration;
							}
						}else if(StringUtils.equals(phoneNo, memberExtend.getEcpPhoneNo1())) {
							if(i == 0) {
								ecp2OneMonthContactSeconds = ecp2OneMonthContactSeconds + duration;
								ecp2ThreeMonthContactSeconds = ecp2ThreeMonthContactSeconds + duration;
								ecp2SixMonthContactSeconds = ecp2SixMonthContactSeconds + duration;
							}else if( i < 3) {
								ecp2ThreeMonthContactSeconds = ecp2ThreeMonthContactSeconds + duration;
								ecp2SixMonthContactSeconds = ecp2SixMonthContactSeconds + duration;
							}else if (i < 6){
								ecp2SixMonthContactSeconds =ecp2SixMonthContactSeconds + duration;
							}
						}else if(StringUtils.equals(phoneNo, memberExtend.getEcpPhoneNo1())) {
							if(i == 0) {
								ecp3OneMonthContactSeconds = ecp3OneMonthContactSeconds + duration;
								ecp3ThreeMonthContactSeconds = ecp3ThreeMonthContactSeconds + duration;
								ecp3SixMonthContactSeconds = ecp3SixMonthContactSeconds + duration;
							}else if( i < 3) {
								ecp3ThreeMonthContactSeconds = ecp1ThreeMonthContactSeconds + duration;
								ecp1SixMonthContactSeconds = ecp1SixMonthContactSeconds + duration;
							}else if (i < 6){
								ecp1SixMonthContactSeconds =ecp1SixMonthContactSeconds + duration;
							}
						}
					}
					ecp1Info.put("call_time_1month", ecp1OneMonthContactSeconds/60);
					ecp1Info.put("call_time_3month", ecp1ThreeMonthContactSeconds/60);
					ecp1Info.put("call_time_6month", ecp1SixMonthContactSeconds/60);
					ecp2Info.put("call_time_1month", ecp2OneMonthContactSeconds/60);
					ecp2Info.put("call_time_3month", ecp2ThreeMonthContactSeconds/60);
					ecp2Info.put("call_time_6month", ecp2SixMonthContactSeconds/60);
					ecp3Info.put("call_time_1month", ecp3OneMonthContactSeconds/60);
					ecp3Info.put("call_time_3month", ecp3ThreeMonthContactSeconds/60);
					ecp3Info.put("call_time_6month", ecp3SixMonthContactSeconds/60);
				}
				emergency_contact_stats.add(ecp1Info);
				emergency_contact_stats.add(ecp2Info);
				emergency_contact_stats.add(ecp3Info);
			}
			//风险联系人
			JSONArray call_risk_analysis = report_data_obj.getJSONArray("call_risk_analysis");
			JSONArray risk_contact_stats = new JSONArray();
			for (Object object : call_risk_analysis) {
				JSONObject item = (JSONObject) object;
				String desc = item.getString("analysis_desc");
				if(StringUtils.equals(desc, "中介") ||StringUtils.equals(desc, "骚扰") ||StringUtils.equals(desc, "贷款")
						||StringUtils.equals(desc, "信用卡")||StringUtils.equals(desc, "催收")) {
					JSONObject riskAnalysis = new JSONObject();
					riskAnalysis.put("analysis_desc", desc);
					riskAnalysis.put("call_cnt_3m", item.getLongValue("call_cnt_3m"));
					riskAnalysis.put("call_cnt_6m", item.getLongValue("call_cnt_6m"));
					riskAnalysis.put("call_time_3m", item.getLongValue("call_time_3m")/60);
					riskAnalysis.put("call_time_6m", item.getLongValue("call_time_6m")/60);
					risk_contact_stats.add(riskAnalysis);
				}
			}
			//社交分析
			JSONObject contact_area_top5 = new JSONObject();
			JSONObject friend_circle = report_data_obj.getJSONObject("friend_circle");
			JSONArray summary = friend_circle.getJSONArray("summary");
			int firendCount3Month = 0;
			int firendCount6Month = 0;
			for (Object object : summary) {
				JSONObject item = (JSONObject) object;
				String key = item.getString("key");
				if(StringUtils.equals(key, "friend_num_3m")) {
					firendCount3Month = Integer.valueOf(item.getString("value"));
				}else if(StringUtils.equals(key, "friend_num_6m")) {
					firendCount6Month = Integer.valueOf(item.getString("value"));
				}
			}
			JSONArray location_top_list = friend_circle.getJSONArray("location_top_list");
			for (Object object : location_top_list) {
				JSONObject monthList = (JSONObject) object;
				String key = monthList.getString("key");
				if(StringUtils.equals(key, "location_top10_3m")) {
					JSONArray location_top10_3m = new JSONArray();
					JSONArray items = monthList.getJSONArray("top_item");
					if(!Collections3.isEmpty(items)) {
						for(int i = 0 ; i < items.size(); i++) {
							if(i > 4) {
								break;
							}
							JSONObject item = (JSONObject) items.get(i);
							JSONObject location = new JSONObject();
							location.put("contact_area", item.getString("location"));
							location.put("contact_area_count", item.getLongValue("peer_number_cnt"));
							location.put("contact_area_ratio", item.getLongValue("peer_number_cnt")/firendCount3Month);
							location_top10_3m.add(location);
						}
					}
					contact_area_top5.put("location_top10_3m", location_top10_3m);
				}else if (StringUtils.equals(key, "location_top10_6m")) {
					JSONArray location_top10_6m = new JSONArray();
					JSONArray items = monthList.getJSONArray("top_item");
					if(!Collections3.isEmpty(items)) {
						for(int i = 0 ; i < items.size(); i++) {
							if(i > 4) {
								break;
							}
							JSONObject item = (JSONObject) items.get(i);
							JSONObject location = new JSONObject();
							location.put("contact_area", item.getString("location"));
							location.put("contact_area_count", item.getLongValue("peer_number_cnt"));
							location.put("contact_area_ratio", item.getLongValue("peer_number_cnt")/firendCount6Month);
							location_top10_6m.add(location);
						}
					}
					contact_area_top5.put("location_top10_6m", location_top10_6m);
				}
			}
			//活跃度分析
			JSONObject active_silence_stats = new JSONObject();
			JSONArray active_degree = report_data_obj.getJSONArray("active_degree");
			for (Object object : active_degree) {
				JSONObject activePoint = (JSONObject) object;
				String app_point = activePoint.getString("app_point");
				if(StringUtils.equals(app_point, "call_day") || StringUtils.equals(app_point, "continue_power_off_cnt") 
						|| StringUtils.equals(app_point, "no_call_day_pct") || StringUtils.equals(app_point, "power_off_day_pct")) {
					JSONObject item = activePoint.getJSONObject("item");
					active_silence_stats.put(app_point+"_3m", item.getString("item_3m")); 
					active_silence_stats.put(app_point+"_6m", item.getString("item_6m")); 
				}
			}
			
			//通话统计
			JSONArray call_record_stats = new JSONArray();
			JSONObject call_record_3m = new JSONObject();
			JSONObject call_record_6m = new JSONObject();
			//通话次数
			long call_count_3m = 0;
			//主叫次数
			long dial_count_3m = 0;
			//被叫次数
			long dialed_count_3m = 0;
			//通话总分钟
			long call_time_count_3m = 0;
			
			Map<String, Integer> call_phone_map_3m = new HashMap<String,Integer>();
			Map<String, Integer> dialMap_3m = new HashMap<String,Integer>();
			Map<String, Integer> dialedMap_3m = new HashMap<String,Integer>();
			//通话号码归属地
			Map<String, Integer> callAreaMpa_3m = new HashMap<String,Integer>();
			
			long call_count_6m = 0;
			long dial_count_6m = 0;
			long dialed_count_6m = 0;
			long call_time_count_6m = 0;
			Map<String, Integer> call_phone_map_6m = new HashMap<String,Integer>();
			Map<String, Integer> dialMap_6m = new HashMap<String,Integer>();
			Map<String, Integer> dialedMap_6m = new HashMap<String,Integer>();
			Map<String, Integer> callAreaMpa_6m = new HashMap<String,Integer>();
			
			//通话时间段
			JSONArray call_record_duration = new JSONArray();
			JSONObject call_record_duration_3m = new JSONObject();
			JSONObject call_record_duration_6m = new JSONObject();
			long call_cnt_morning_3m = 0;
			long call_cnt_noon_3m = 0;
			long call_cnt_afternoon_3m = 0;
			long call_cnt_evening_3m = 0;
			long call_cnt_night_3m = 0;

			long call_cnt_morning_6m = 0;
			long call_cnt_noon_6m = 0;
			long call_cnt_afternoon_6m = 0;
			long call_cnt_evening_6m = 0;
			long call_cnt_night_6m = 0;
			
			JSONArray call_contact_detail = report_data_obj.getJSONArray("call_contact_detail");
			for (Object object : call_contact_detail) {
				JSONObject detail = (JSONObject) object;
				String peer_num = detail.getString("peer_num");
				String peer_city = detail.getString("city");
				
				call_count_3m = call_count_3m + detail.getLongValue("call_cnt_3m");
				dial_count_3m = dial_count_3m + detail.getLongValue("dial_cnt_3m");
				dialed_count_3m = dialed_count_3m + detail.getLongValue("dialed_cnt_3m");
				call_time_count_3m = call_time_count_3m + detail.getLongValue("call_time_3m");
				if(detail.getLongValue("call_cnt_3m") != 0) {
					call_phone_map_3m.put(peer_num, 1);
					callAreaMpa_3m.put(peer_city, 1);
				}
				if(detail.getLongValue("dial_cnt_3m") != 0) {
					dialMap_3m.put(peer_num, 1);
				}
				if(detail.getLongValue("dialed_cnt_3m") != 0) {
					dialedMap_3m.put(peer_num, 1);
				}
				
				call_count_6m = call_count_6m + detail.getLongValue("call_cnt_6m");
				dial_count_6m = dial_count_6m + detail.getLongValue("dial_cnt_6m");
				dialed_count_6m = dialed_count_6m + detail.getLongValue("dialed_cnt_6m");
				call_time_count_6m = call_time_count_6m + detail.getLongValue("call_time_6m");
				if(detail.getLongValue("call_cnt_6m") != 0) {
					call_phone_map_6m.put(peer_num, 1);
					callAreaMpa_6m.put(peer_city, 1);
				}
				if(detail.getLongValue("dial_cnt_6m") != 0) {
					dialMap_6m.put(peer_num, 1);
				}
				if(detail.getLongValue("dialed_cnt_6m") != 0) {
					dialedMap_6m.put(peer_num, 1);
				}
				call_cnt_morning_3m = call_cnt_morning_3m + detail.getLongValue("call_cnt_morning_3m");
				call_cnt_noon_3m = call_cnt_noon_3m + detail.getLongValue("call_cnt_noon_3m");
				call_cnt_afternoon_3m = call_cnt_afternoon_3m + detail.getLongValue("call_cnt_afternoon_3m");
				call_cnt_evening_3m = call_cnt_evening_3m + detail.getLongValue("call_cnt_evening_3m");
				call_cnt_night_3m = call_cnt_night_3m + detail.getLongValue("call_cnt_night_3m");
				
				call_cnt_morning_6m = call_cnt_morning_6m + detail.getLongValue("call_cnt_morning_6m");
				call_cnt_noon_6m = call_cnt_noon_6m + detail.getLongValue("call_cnt_noon_6m");
				call_cnt_afternoon_6m = call_cnt_afternoon_6m + detail.getLongValue("call_cnt_afternoon_6m");
				call_cnt_evening_6m = call_cnt_evening_6m + detail.getLongValue("call_cnt_evening_6m");
				call_cnt_night_6m = call_cnt_night_6m + detail.getLongValue("call_cnt_night_6m");
				
			}
			call_record_3m.put("call_count_3m", call_count_3m);
			call_record_3m.put("dial_count_3m", dial_count_3m);
			call_record_3m.put("dialed_count_3m", dialed_count_3m);
			call_record_3m.put("call_phone_count_3m", call_phone_map_3m.size());
			call_record_3m.put("dial_phone_count_3m", dialMap_3m.size());
			call_record_3m.put("dialed_phone_count_3m", dialedMap_3m.size());
			call_record_3m.put("call_time_count_3m", call_time_count_3m/60);
			call_record_3m.put("call_area_count_3m", callAreaMpa_3m.size());
			
			call_record_6m.put("call_count_6m", call_count_6m);
			call_record_6m.put("dial_count_6m", dial_count_6m);
			call_record_6m.put("dialed_count_3m", dialed_count_6m);
			call_record_6m.put("call_phone_count_3m", call_phone_map_6m.size());
			call_record_6m.put("dial_phone_count_3m", dialMap_6m.size());
			call_record_6m.put("dialed_phone_count_3m", dialedMap_6m.size());
			call_record_6m.put("call_time_count_3m", call_time_count_6m/60);
			call_record_6m.put("call_area_count_3m", callAreaMpa_6m.size());
			
			call_record_stats.add(call_record_3m);
			call_record_stats.add(call_record_6m);
			
			call_record_duration_3m.put("5:30-11:30", call_cnt_morning_3m);
			call_record_duration_3m.put("11:30-13:30", call_cnt_noon_3m);
			call_record_duration_3m.put("13:30-17:30", call_cnt_afternoon_3m);
			call_record_duration_3m.put("17:30-23:30", call_cnt_evening_3m);
			call_record_duration_3m.put("23:30-1:30", call_cnt_night_3m);
			
			call_record_duration_6m.put("5:30-11:30", call_cnt_morning_6m);
			call_record_duration_6m.put("11:30-13:30", call_cnt_noon_6m);
			call_record_duration_6m.put("13:30-17:30", call_cnt_afternoon_6m);
			call_record_duration_6m.put("17:30-23:30", call_cnt_evening_6m);
			call_record_duration_6m.put("23:30-1:30", call_cnt_night_6m);
			
			call_record_duration.add(call_record_duration_3m);
			call_record_duration.add(call_record_duration_6m);
			
			//消费账单
			JSONArray fee_bills = new JSONArray();
			JSONObject fee_bills_3m = new JSONObject();
			JSONObject fee_bills_6m = new JSONObject();
			JSONArray consumption_details = report_data_obj.getJSONArray("consumption_detail");
			for (Object object : consumption_details) {
				JSONObject consumption_detail = (JSONObject) object; 
				String app_point = consumption_detail.getString("app_point");
				if(StringUtils.equals(app_point, "total_fee")||StringUtils.equals(app_point, "recharge_amount")
						||StringUtils.equals(app_point, "voice_fee")) {
					JSONObject item = consumption_detail.getJSONObject("item");
					fee_bills_3m.put(app_point + "_3m", item.getString("item_3m"));
					fee_bills_6m.put(app_point + "_6m", item.getString("item_6m"));
				}
			}
			int base_fee = 0;
			base_fee = ((JSONObject)bills.get(0)).getIntValue("base_fee");
			fee_bills_3m.put("base_fee", base_fee);
			fee_bills_6m.put("base_fee", base_fee);
			
			//通话明细
			JSONArray call_details = new JSONArray();
			for (Object object : calls) {
				JSONObject monthCall = (JSONObject) object;
				JSONArray items = monthCall.getJSONArray("items");
				for (Object object2 : items) {
					JSONObject detail = (JSONObject) object2;
					String dial_type = detail.getString("dial_type");
					switch (dial_type) {
					case "DIAL":
						dial_type = "主叫";
						break;
					case "DIALED":
						dial_type = "被叫";
						break;
					case "TRANSFER":
						dial_type = "呼叫转移";
						break;
					default:
						break;
					}
					JSONObject call_detail = new JSONObject(); 
					call_detail.put("call_type", dial_type);
					call_detail.put("receive_phone", detail.getString("peer_number"));
					call_detail.put("call_time", detail.getString("time"));
					call_detail.put("trade_time", detail.getIntValue("duration"));
					call_details.add(call_detail);
				}
			}
			
			JSONObject opreatorInfo = new JSONObject();
			opreatorInfo.put("mobileInfo", mobileInfo);
			opreatorInfo.put("userInofCheck", userInofCheck);
			opreatorInfo.put("emergency_contact_stats", emergency_contact_stats);
			opreatorInfo.put("risk_contact_stats", risk_contact_stats);
			opreatorInfo.put("contact_area_top5", contact_area_top5);
			opreatorInfo.put("active_silence_stats", active_silence_stats);
			opreatorInfo.put("call_record_stats", call_record_stats);
			opreatorInfo.put("call_record_duration", call_record_duration);
			opreatorInfo.put("fee_bills", fee_bills);
			opreatorInfo.put("call_details", call_details);
			String task_json = JSONUtil.toJson(opreatorInfo);
			// 整理好的数据
			RcCaDataV2 caDataV2 = new RcCaDataV2();
			caDataV2.setPhoneNo(rcGxb.getPhoneNo());
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
			caData.setContent(task_json);
			caData.setPhoneNo(member.getUsername());
			caData.setIdNo(member.getIdNo());
			caData.setName(member.getName());
			caData.setType(RcCaDataV2.Type.yys);
			caData.setProvider(RcCaDataV2.Provider.gxb);
			rcCaDataService.save(caData);

			rcGxb.setDataStatus(RcGxb.DataStatus.data_arranged);
			gxbService.save(rcGxb);

//			// 详情数据
//			RcCaYysDetails rcCaYysDetails = new RcCaYysDetails();
//			rcCaYysDetails.setContent(rcGxb.getTaskData());
//			rcCaYysDetails.setMemberId(rcGxb.getOrgId());
//			rcCaYysDetailsService.save(rcCaYysDetails);
			
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
	
	private void xuexinwang(RcGxb rcGxb) {
		try {
			JSONObject task_data_obj = JSONObject.parseObject(rcGxb.getTaskData());
			Member member = memberService.get(rcGxb.getOrgId());
			JSONArray educationInfoList = task_data_obj.getJSONArray("educationInfoList");
			JSONObject chsiBaseInfo = task_data_obj.getJSONObject("chsiBaseInfo");
			JSONObject educationInfo = null;
			String highestEduFlag = "1";
			for (Object object : educationInfoList) {
				educationInfo = (JSONObject) object;
				if (educationInfo.getString("highestEduFlag").equals(highestEduFlag)) {
					break;
				}
			}
			RcCaDataV2 caData = new RcCaDataV2();
			caData.setPhoneNo(member.getUsername());
			caData.setIdNo(member.getIdNo());
			caData.setName(member.getName());
			caData.setType(RcCaDataV2.Type.xxw);
			caData.setProvider(RcCaDataV2.Provider.gxb);

			// 毕业院校
			String school = educationInfo.getString("university");
			// 专业名称
			String major = educationInfo.getString("major");
			// 学历层次
			String edu_level = educationInfo.getString("degree");
			// 学习形式
			String edu_form = educationInfo.getString("educationStyle");
			// 入学时间
			String entrance_date = educationInfo.getString("enrolDate");
			// 毕业时间
			String graduate_date = educationInfo.getString("graduateDate");
			JSONObject contentJson = new JSONObject();
			contentJson.put("realname", chsiBaseInfo.getString("name"));
			contentJson.put("card_id", chsiBaseInfo.getString("credentialsNum"));
			contentJson.put("department", major);
			contentJson.put("school", school);
			contentJson.put("edu_level", edu_level);
			contentJson.put("edu_form", edu_form);
			contentJson.put("entrance_date", entrance_date);
			contentJson.put("graduate_date", graduate_date);

			caData.setContent(contentJson.toString());
			rcCaDataService.save(caData);

			rcGxb.setDataStatus(RcGxb.DataStatus.data_arranged);
			gxbService.save(rcGxb);

			// 认证成功后添加学信网认证项
			int verifiedList = member.getVerifiedList();
			verifiedList = VerifiedUtils.addVerified(verifiedList, 8);
			member.setVerifiedList(verifiedList);
			RedisUtils.put("memberInfo" + member.getId(), "xuexingwangStatus", "1");
			memberService.save(member);

		} catch (NullPointerException e) {
			log.error(Exceptions.getStackTraceAsString(e));
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
	}
	
	private void wangyin(RcGxb rcGxb) {
		try {
			JSONArray listJie = new JSONArray();
			JSONArray listXin = new JSONArray();
			JSONObject task_data_obj = JSONObject.parseObject(rcGxb.getTaskData());
			JSONObject baseInfo = task_data_obj.getJSONObject("baseInfoDto");
			String name = baseInfo.getString("name");
			JSONArray billLines = task_data_obj.getJSONArray("billLines");
			for (Object object : billLines) {
				JSONObject billLine = (JSONObject) object;
				JSONArray bills = billLine.getJSONArray("bills");
				JSONArray cards = billLine.getJSONArray("cards");
				//借记卡账户总余额 信用卡总额度
				BigDecimal balance = BigDecimal.ZERO;
				//借记卡账户总可用余额 信用卡总可用额度
				BigDecimal avabalance = BigDecimal.ZERO;
				for (Object object2 : cards) {
					JSONObject card = (JSONObject) object2;
					//卡类型：0其他 1储蓄 2信用
					String cardType = card.getString("cardType");
					String debitCard = "1";
					String creditCard = "2";
					if (StringUtils.equals(cardType, debitCard)) {
						JSONObject debitCardInfo = new JSONObject();
						debitCardInfo.put("name", name);
						debitCardInfo.put("deposit_bank", card.getString("openBank"));
						debitCardInfo.put("status", card.getString("status"));
						debitCardInfo.put("account", card.getString("cardNo"));
						balance = balance.add(new BigDecimal(card.getString("balance")));
						debitCardInfo.put("balance", StringUtils.decimalToStr(balance, 2));
						debitCardInfo.put("avabalance", StringUtils.decimalToStr(balance, 2));
						//借记卡总收入
						BigDecimal allInc = BigDecimal.ZERO;
						//借记卡总支出
						BigDecimal allOut = BigDecimal.ZERO;
						//月平均收入
						BigDecimal avaInc = BigDecimal.ZERO;
						//月平均支出
						BigDecimal avaOut = BigDecimal.ZERO;
						//一般情况下当前月份天数不满，从上一个月开始统计
						for (int i = 1; i < bills.size(); i++) {
							JSONObject bill = (JSONObject) bills.get(i);
							String income = bill.getString("income");
							String outcome = bill.getString("outcome");
							if (StringUtils.isNotBlank(income)) {
								BigDecimal inc = new BigDecimal(income);
								allInc = allInc.add(inc);
							}
							if (StringUtils.isNotBlank(outcome)) {
								BigDecimal out = new BigDecimal(outcome);
								allOut = allOut.add(out);
							}
						}
						BigDecimal monthNum = new BigDecimal(bills.size() - 1);
						if(monthNum.compareTo(BigDecimal.ZERO) > 0) {
							avaInc = allInc.divide(monthNum,RoundingMode.HALF_UP);
							//支出都是负值
							avaOut = allOut.negate().divide(monthNum,RoundingMode.HALF_UP);
						}else {
							avaInc = allInc;
							avaOut = allOut;
						}
						debitCardInfo.put("inc", StringUtils.decimalToStr(avaInc, 2));
						debitCardInfo.put("out", StringUtils.decimalToStr(avaOut, 2));
						// 最低偿还能力
						debitCardInfo.put("xinMin", StringUtils.decimalToStr(avaInc.multiply(new BigDecimal(0.3)), 2));
						// 最高偿还能力
						debitCardInfo.put("xinMax", StringUtils.decimalToStr(avaInc.multiply(new BigDecimal(0.5)), 2));
						listJie.add(debitCardInfo);
					} else if (StringUtils.equals(cardType, creditCard)) {
						JSONObject creditCardInfo = new JSONObject();
						creditCardInfo.put("name", name);
						creditCardInfo.put("deposit_bank", card.getString("openBank"));
						creditCardInfo.put("status", card.getString("status"));
						creditCardInfo.put("account", card.getString("cardNo"));
						BigDecimal creditLimit = new BigDecimal(card.getString("creditLimit"));
						BigDecimal creditUsageLimit = new BigDecimal(card.getString("creditUsageLimit"));
						balance = balance.compareTo(creditLimit) > 0 ? balance:creditLimit;
						avabalance = avabalance.compareTo(creditUsageLimit) > 0 ? avabalance:creditUsageLimit;
						creditCardInfo.put("balance", StringUtils.decimalToStr(balance, 2));
						creditCardInfo.put("avabalance", StringUtils.decimalToStr(avabalance, 2));
						//消费总金额
						BigDecimal shoppingAmount = BigDecimal.ZERO;
						//还款总金额
						BigDecimal repayAmount = BigDecimal.ZERO;
						//三月内逾期次数
						int threrover = 0;
						//六月内逾期次数
						int sixover = 0;
						int billsSize = bills.size();
						// 一般情况下当前月份天数不满，从上一个月开始取统计
						for (int i = 1; i < billsSize; i++) {
							JSONObject bill = (JSONObject) bills.get(i);
							JSONArray billDetails = bill.getJSONArray("billDetails"); 
							if(Collections3.isEmpty(billDetails)) {
								continue;
							}
							for (Object object3 : billDetails) {
								JSONObject billDetail = (JSONObject) object3;
								String cardNo = billDetail.getString("cardNo");
								String lastFourCardNo = StringUtils.substring(cardNo, cardNo.length() - 4);
								if (StringUtils.equals(lastFourCardNo, card.getString("lastFourNo"))) {
									BigDecimal amount = new BigDecimal(billDetail.getString("amount"));
									if(amount.compareTo(BigDecimal.ZERO) >= 0) {
										shoppingAmount = shoppingAmount.add(amount);
									}else {
										amount = amount.negate();
										repayAmount = repayAmount.add(amount);
									}
								}
								String endDate = bill.getString("endDate");
								String paymentDueDate = bill.getString("paymentDueDate");
								if (endDate != null && paymentDueDate != null && endDate.compareTo(paymentDueDate) > 0) {
									if (i < 4) {
										threrover++;
										sixover++;
									} else if (i < 7) {
										sixover++;
									}
								}
							}
						}
						//月均消费金额
						String inc = "";
						//月均还款金额
						String out = "";
						BigDecimal monthNum = new BigDecimal(billsSize - 1);
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
			
			Member member = memberService.get(rcGxb.getOrgId());
			RcCaDataV2 caData = new RcCaDataV2();
			caData.setContent(JSONObject.toJSONString(contentJson));
			caData.setPhoneNo(member.getUsername());
			caData.setIdNo(member.getIdNo());
			caData.setName(member.getName());
			caData.setType(RcCaDataV2.Type.wangyin);
			caData.setProvider(RcCaDataV2.Provider.gxb);
			rcCaDataService.save(caData);

			rcGxb.setDataStatus(RcGxb.DataStatus.data_arranged);
			gxbService.save(rcGxb);

			// 认证成功后添加银行账单认证项
			int verifiedList = member.getVerifiedList();
			verifiedList = VerifiedUtils.addVerified(verifiedList, 7);
			member.setVerifiedList(verifiedList);
			RedisUtils.put("memberInfo" + member.getId(), "bankTrxStatus", "1");
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
