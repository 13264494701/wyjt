package com.jxf.rc.service.impl;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.protocol.Protocol;
import org.ebaoquan.rop.thirdparty.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.dao.NfsLoanOperatingRecordDao;
import com.jxf.loan.dao.NfsLoanRecordDao;
import com.jxf.loan.entity.NfsLoanOperatingRecord;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.MySSLProtocolSocketFactory;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.rc.dao.RcCaDataDao;
import com.jxf.rc.entity.RcCaData;
import com.jxf.rc.service.RcCaDataService;
import com.jxf.rc.utils.ThirdPartyUtils;
import com.jxf.rc.utils.TianjiSample;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.security.MD5Utils;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Encodes;
import com.jxf.svc.utils.EncryptUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.HttpUtils;
import com.jxf.svc.utils.JSONUtil;
import com.jxf.svc.utils.RSAUtils;
import com.jxf.web.model.wyjt.app.member.CaAuthResponseResult;
import com.jxf.web.model.wyjt.app.member.CaAuthResponseResult.UserBriefLegalize;
import com.jxf.web.model.wyjt.app.member.CreditReportResponseResult;
import com.jxf.web.model.wyjt.app.member.CreditReportResponseResult.CreditTable;
import com.jxf.web.model.wyjt.app.member.CreditReportResponseResult.CreditTableBasicItem;
import com.jxf.web.model.wyjt.app.member.CreditReportResponseResult.CreditTableSpecialHandle;

/**
 * 信用报告数据表ServiceImpl
 * 
 * @author lmy
 * @version 2018-12-17
 */
@Service("rcCaDataService")
@Transactional(readOnly = false)
public class RcCaDataServiceImpl extends CrudServiceImpl<RcCaDataDao, RcCaData> implements RcCaDataService {

	@Autowired
	private RcCaDataDao caDataDao;
	@Autowired
	private MemberService memberService;

	@Autowired
	private NfsLoanRecordService loanRecordService;

	@Autowired
	private NfsLoanRecordDao fsLoanRecordDao;

	@Autowired
	private NfsLoanOperatingRecordDao loanOperatingRecordDao;
	
	/**
	 * 日志对象
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public RcCaData get(Long id) {
//		Criteria criteria = Criteria.where("_id").is(id);
//		Query query = BasicQuery.query(criteria);  
//		RcCaData caData =  mongoTemplate.findOne(query , RcCaData.class);
//		return caData;
		return super.get(id);
	}
	
	@Override
	public List<RcCaData> findList(RcCaData rcCaData) {
//		Sort sort = new Sort(Sort.Direction.DESC, "createTime");
//		Criteria criteria = Criteria.where("memberId").is(rcCaData.getMemberId());
//		if(rcCaData.getType() != null){
//			criteria.andOperator(Criteria.where("type").is(rcCaData.getType()));
//		}
//		Query query = new Query().addCriteria(criteria);
//		List<RcCaData> findList = mongoTemplate.find(query.with(sort), RcCaData.class);
//		return findList;
		return super.findList(rcCaData);
	}
	
	@Override
	public Page<RcCaData> findPage(Page<RcCaData> page, RcCaData rcCaData) {
//		Criteria criteria = Criteria.where("memberId").is(rcCaData.getMemberId());
//		if(rcCaData.getType() != null){
//			criteria.andOperator(Criteria.where("type").is(rcCaData.getType()));
//		}
//		Query query = new Query().addCriteria(criteria);
//		Sort sort = new Sort(Sort.Direction.DESC, "createTime");//多条件DEVID、time
//    	
//    	long total = mongoTemplate.count(query.with(sort), RcCaData.class);
//    	Page<RcCaData> page = rcCaData.getPage();
//		
//		int skip = (page.getPageNo() - 1) * 30;
//    	query.skip(skip);// skip相当于从那条记录开始
//    	query.limit(30);// 从skip开始,取多少条记录
//    	List<RcCaData> rcCaDataList = mongoTemplate.find(query, RcCaData.class);
//    	page = new Page<RcCaData>(page.getPageNo(), 30 ,total, rcCaDataList);
//		return page;
		
		return super.findPage(page, rcCaData);
	}

	@Override
	@Transactional(readOnly = false)
	public void save(RcCaData rcCaData) {
//		if(rcCaData.getId() == null){
//			rcCaData.setId(SnowFlake.getId());
//		}
//		mongoTemplate.save(rcCaData);
		
		super.save(rcCaData);
	}

	@Transactional(readOnly = false)
	public void delete(RcCaData rcCaData) {
		super.delete(rcCaData);
	}
	
	@Override
	public RcCaData getByPhoneNoAndType(String phoneNo,RcCaData.Type type) {

		RcCaData caData = new RcCaData();
		caData.setPhoneNo(phoneNo);
		caData.setType(type);
		return caDataDao.getByPhoneNoAndType(caData);
	}

	@Override
	public int getRenZhengNum(Member merber) {
		// 统计认证次数
		int count = 1;
		Integer verifiedList = merber.getVerifiedList();
		if(VerifiedUtils.isVerified(verifiedList, 5)){//淘宝
			count++;
		}
		if(VerifiedUtils.isVerified(verifiedList, 6)){//运营商
			count++;
		}
		if(VerifiedUtils.isVerified(verifiedList, 4)){//芝麻分
			count++;
		}
		if(VerifiedUtils.isVerified(verifiedList, 8)){//学信网
			count++;
		}
		if(VerifiedUtils.isVerified(verifiedList, 9)){//社保
			count++;
		}
		if(VerifiedUtils.isVerified(verifiedList, 10)){//公积金
			count++;
		}
		if(VerifiedUtils.isVerified(verifiedList, 7)){//银行卡账单
			count++;
		}
		return count;
	}



	@Override
	public UserBriefLegalize getRenZhengData(RcCaData caData, int type) {
		UserBriefLegalize result = new CaAuthResponseResult().new UserBriefLegalize();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);

		// 获取用户的信息
		Member member = memberService.findByUsername(caData.getPhoneNo());
		RcCaData rcCaData = caDataDao.getByPhoneNoAndType(caData);
		// 淘宝
		if (caData.getType().ordinal() == 1) {
			result.setName("淘宝认证");
			if (rcCaData !=null) {
				if (cal.getTimeInMillis() <= rcCaData.getCreateTime().getTime()) {
					result.setStatus(1);
					result.setDate(DateUtils.formatDate(rcCaData.getCreateTime()));
					result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.yy_" + result.getStatus()));
				} else {
					result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.yy_" + result.getStatus()));
					result.setDate("已过期");
					result.setStatus(1);
					this.removeVerified(member,5);
				}
			} else {
				result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.yy_" + result.getStatus()));
			}
			Map<String, String> jsonMap = new HashMap<String, String>();
			jsonMap.put("br.partnerCode", ThirdPartyUtils.partner_code);
			jsonMap.put("br.partnerKey", ThirdPartyUtils.partner_key);
			jsonMap.put("br.channel_code", ThirdPartyUtils.TBchannel_code);
			jsonMap.put("br.passback_params", member.getId() + "");
			String jsonStringFromMap = JSONUtil.toJson(jsonMap);
			result.setUrl(jsonStringFromMap);
			result.setJumpStatus("taobao");
			result.setAuthenType(type);
			result.setPageSatus(2);
		} else if (caData.getType().ordinal() == 9) {
			result.setName("运营商认证");
			if (rcCaData !=null) {
				if (cal.getTimeInMillis() <= rcCaData.getCreateTime().getTime()) {
					result.setStatus(1);
					result.setDate(DateUtils.formatDate(rcCaData.getCreateTime()));
					result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.yy_" + result.getStatus()));
				} else {
					result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.yy_" + result.getStatus()));
					result.setStatus(1);
					result.setDate("已过期");
					this.removeVerified(member,6);
				}
			} else {
				result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.yy_" + result.getStatus()));
			}
			
			result.setUrl(
					"https://amazing.shujumohe.com/box/yys?box_token=" + Global.getConfig("sjmhtoken") + "&arr_pass_hide=real_name,identity_code,passback_params&real_name="
							+ member.getName() + "&identity_code=" + member.getIdNo() + "&passback_params="
							+ member.getId());
			result.setJumpStatus("yunying");
			result.setAuthenType(type);
			result.setPageSatus(3);

		} else if (caData.getType().ordinal() == 3) {
			result.setName("芝麻分");
			// 查询是否认证过
			if (rcCaData !=null) {
				if (cal.getTimeInMillis() <= rcCaData.getCreateTime().getTime()) {
					result.setStatus(1);
					result.setDate(DateUtils.formatDate(rcCaData.getCreateTime()));
					result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.zm_" + result.getStatus()));
				} else {
					result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.zm_" + result.getStatus()));
					result.setDate("已过期");
					result.setStatus(1);
					this.removeVerified(member,4);
				}
			} else {
				result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.zm_" + result.getStatus()));
			}
			String zmToken = getZmToken(member.getName(), member.getUsername(), member.getIdNo(),member.getId()+"");

			result.setUrl(Global.getConfig("gxb.sesameAppUrl") + zmToken);
			result.setJumpStatus("zhima");
			result.setAuthenType(type);
			result.setPageSatus(4);

		} else if (caData.getType().ordinal() == 4) {
			result.setName("学信网");
			if (rcCaData !=null) {
				if (cal.getTimeInMillis() <= rcCaData.getCreateTime().getTime()) {
					result.setStatus(1);
					result.setDate(DateUtils.formatDate(rcCaData.getCreateTime()));
					result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.xx_" + result.getStatus()));
				} else {
					result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.xx_" + result.getStatus()));
					result.setDate("已过期");
					result.setStatus(1);
					this.removeVerified(member,8);
				}
			} else {
				result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.xx_" + result.getStatus()));
			}
			result.setUrl(
					"https://open.shujumohe.com/box/chsi?box_token=" + Global.getConfig("sjmhtoken") + "&arr_pass_hide=passback_params&passback_params="
							+ member.getId());
			result.setJumpStatus("xuexin");
			result.setAuthenType(type);
			result.setPageSatus(5);

		} else if (caData.getType().ordinal() == 5) {
			result.setName("社保");
			if (rcCaData !=null) {
				if (cal.getTimeInMillis() <= rcCaData.getCreateTime().getTime()) {
					result.setStatus(1);
					result.setDate(DateUtils.formatDate(rcCaData.getCreateTime()));
					result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.sb_" + result.getStatus()));
				} else {
					result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.sb_" + result.getStatus()));
					result.setDate("已过期");
					result.setStatus(1);
					this.removeVerified(member,9);
				}
			} else {
				result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.sb_" + result.getStatus()));
			}
			result.setUrl(
					"https://open.shujumohe.com/box/she_bao?box_token=" + Global.getConfig("sjmhtoken") + "&arr_pass_hide=passback_params&passback_params="
							+ member.getId());
			result.setJumpStatus("shebao");
			result.setAuthenType(type);
			result.setPageSatus(6);
		} else if (caData.getType().ordinal() == 6) {
			result.setName("公积金");
	
			// 查询是否认证过
			if (rcCaData !=null) {
				if (cal.getTimeInMillis() <= rcCaData.getCreateTime().getTime()) {
					result.setStatus(1);
					result.setDate(DateUtils.formatDate(rcCaData.getCreateTime()));
					result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.gj_" + result.getStatus()));
				} else {
					result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.gj_" + result.getStatus()));
					result.setDate("已过期");
					result.setStatus(1);
					this.removeVerified(member,10);
				}
			} else {
				result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.gj_" + result.getStatus()));
			}
			result.setUrl(
					"https://open.shujumohe.com/box/gjj?box_token=" + Global.getConfig("sjmhtoken") + "&arr_pass_hide=passback_params&passback_params="
							+ member.getId());
			result.setJumpStatus("gongji");
			result.setAuthenType(type);
			result.setPageSatus(7);
		} else if (caData.getType().ordinal() == 8) {
			result.setName("银行卡账单");
			// 查询是否认证过
			if (rcCaData !=null) {
				if (cal.getTimeInMillis() <= rcCaData.getCreateTime().getTime()) {
					result.setStatus(1);
					result.setDate(DateUtils.formatDate(rcCaData.getCreateTime()));
					result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.wy_" + result.getStatus()));
				} else {
					result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.wy_" + result.getStatus()));
					result.setDate("已过期");
					result.setStatus(1);
					this.removeVerified(member,7);
				}
			} else {
				result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.wy_" + result.getStatus()));
			}
			result.setUrl(
					"https://open.shujumohe.com/box/wy?box_token=" + Global.getConfig("sjmhtoken") + "&arr_pass_hide=passback_params&passback_params="
							+ member.getId());
			result.setJumpStatus("wangyin");
			result.setAuthenType(type);
			result.setNewStatus(1);
			result.setPageSatus(8);
			// 判断是否支付过一元报告
		}
		return result;
	}
	
	
	// 获取公信宝token
	public  String getZmToken(String name, String phoneNo, String idNo, String sequenceNo) {
		StringBuffer ss = new StringBuffer();
		ss.append("{");
		ss.append("\"name\":");
		ss.append("\"" + name + "\",");

		ss.append("\"phone\":");
		ss.append("\"" + phoneNo + "\",");

		ss.append("\"authItem\":");
		ss.append("\"" + ThirdPartyUtils.authItem + "\",");

		ss.append("\"appId\":");
		ss.append("\"" + Global.getConfig("gxb.appIdForApp") + "\",");

		ss.append("\"idcard\":");
		ss.append("\"" + idNo + "\",");

		ss.append("\"sequenceNo\":");
		ss.append("\"" + sequenceNo + "\",");

		Date date = new Date();
		ss.append("\"timestamp\":");
		ss.append("\"" + date.getTime() + "\",");

		String sign = MD5Utils.EncoderByMd5( Global.getConfig("gxb.appIdForApp") + Global.getConfig("gxb.appSecretForApp") + ThirdPartyUtils.authItem
				+ date.getTime() + sequenceNo);
		ss.append("\"sign\":");
		ss.append("\"" + sign + "\"");
		ss.append("}");
		String ess = ss.toString();
		HttpClient httpClient = new HttpClient();
		// 设置连接超时时间(单位毫秒)
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(60 * 1000);
		// 设置读取超时时间(单位毫秒)
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(60 * 1000);
		Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);
		PostMethod method = new PostMethod(ThirdPartyUtils.url);
		String token = "0126200676000WKN35TiMa7vFCb4Q3Ds";
		try {
			RequestEntity entity = new StringRequestEntity(ess, "application/json", "UTF-8");
			method.setRequestEntity(entity);
			httpClient.executeMethod(method);
			int code = method.getStatusCode();
			// 判断请求是否成功
			if (code == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String str = "";
				while ((str = reader.readLine()) != null) {
					stringBuffer.append(str);
				}
				// 获取第三方的结果
				String rsp = stringBuffer.toString();
				if (StringUtils.isBlank(rsp)) {
					// 如果没有拿到token，给个默认假的
					return token;
				} else {
					Map<String, Object> mapFromJson = JSONUtil.toMap(rsp);
					if (mapFromJson.get("retCode").toString().equals("1")) {
						Map<String, Object> mapFromJson1 = JSONUtil.toMap(mapFromJson.get("data").toString());
						token = mapFromJson1.get("token").toString();
					} else {
						return token;
					}
				}
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}
		return token;
	}



	@Override
	public CreditTable getCreditarchivesShuJuYuan(Map<String, Object> mapYY) {
		CreditTable creditTable = new CreditReportResponseResult().new CreditTable();
		creditTable.setTitle("数据源分析");
		creditTable.setSubtitle("");
		creditTable.setTableType(101);
		// 是否具有筛选的表格内数据 3
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem.setTableFiltrateTitle("");
		creditTableBasicItem.setTableHeadData("数据源^使用时长(月)^权限搜集时间");
		// 表格内容数组
		ArrayList<String> tableBodyData = new ArrayList<String>();
		// 1紧急联系人 2 淘宝 3运营商 4芝麻认证 5学信网 6 社保 7公积金
		if (mapYY.size() > 0) {
			String string = mapYY.get("created_time").toString();
			String[] split = string.split(" ");
			String yyString = (mapYY.get("channel_src").toString().equals("null") ? ""
					: mapYY.get("channel_src").toString()) + "^"
					+ (mapYY.get("time") == null ? "无数据" : mapYY.get("time")) + "^" + split[0];
			tableBodyData.add(yyString);
		} else {
			tableBodyData.add("");
		}

		creditTableBasicItem.setTableBodyData(tableBodyData);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem.setSpecialHandle(specialHandle);
		creditTableBasicItemsList.add(creditTableBasicItem);
		creditTable.setTableItem(creditTableBasicItemsList);
		return creditTable;
	}

	@Override
	public CreditTable getCreditarchivesFengXian(Map<String, Object> userMap, Map<String, Object> mapYY, String mapXX,
			Member Buser) {
		CreditTable creditTable1 = new CreditReportResponseResult().new CreditTable();
		creditTable1.setTitle("风险排查");
		creditTable1.setSubtitle("数据源于交叉验证");
		creditTable1.setTableType(102);
		// 是否具有筛选的表格内数据 3
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList1 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem1 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem1.setTableFiltrateTitle("");
		creditTableBasicItem1.setTableHeadData("");
		// 表格内容数组
		ArrayList<String> tableBodyData1 = new ArrayList<String>();
		String fengtb = "无数据";
		String fengxs = "无数据";
		String fengxm = "无数据";
		String fengyx = "无数据";
		String fengym = "无数据";
		String fengf = "手机号是否命中风险名单^否";
		Member member = memberService.get(Buser.getId());

		// 淘宝数据
		if (mapYY.size() > 0 && mapYY != null) {
			if (!mapYY.get("username").toString().contains("*")) {
				if (mapYY.get("username").toString().equals(Buser.getUsername())) {
					fengtb = "注册手机号与淘宝注册手机号是否一致^是";
				} else {
					fengtb = "注册手机号与淘宝注册手机号是否一致^否";
				}

			} else {
				String username = Buser.getUsername();
				int count = 0;
				char[] charArray = username.toCharArray();
				char[] charArray2 = mapYY.get("username").toString().toCharArray();
				for (int j = 0; j < charArray2.length; j++) {
					if (charArray[j] == charArray2[j]) {
						count = count + 1;
					}
				}
				if (count >= 7) {
					fengtb = "注册手机号与淘宝注册手机号是否一致^是";
				} else {
					fengtb = "注册手机号与淘宝注册手机号是否一致^否";
				}
			}
		} else {
			fengtb = "注册手机号与淘宝注册手机号是否一致^无数据";
		}
		tableBodyData1.add(fengtb);
		// 运营商
		if (userMap != null && userMap.size() > 0) {
			String identity_code = userMap.get("identity_code").toString();
			List<String> list = new ArrayList<String>();
			if (identity_code.contains("*")) {
				String[] split = identity_code.split("\\*");
				for (int i = 0; i < split.length; i++) {
					if (!split[i].equals("")) {
						list.add(split[i]);
					}
				}
				boolean tur = false;
				for (int i = 0; i < list.size(); i++) {
					tur = false;
					if (member.getIdNo().contains(list.get(i))) {
						tur = true;
					}
				}
				if (tur == true) {
					fengym = "姓名与运营商是否匹配^是";
					fengyx = "身份证与运营商是否匹配^是";
					// 设置特殊字段判断未带回来数据
				} else {
					fengym = "姓名与运营商是否匹配^否";
					fengyx = "身份证与运营商是否匹配^否";
				}

			} else {
				if (identity_code.equals(member.getIdNo())) {
					fengym = "姓名与运营商是否匹配^是";
					fengyx = "身份证与运营商是否匹配^是";
					// 设置特殊字段判断未带回来数据
				} else if (identity_code.equals("未知")) {
					fengym = "姓名与运营商是否匹配^无数据";
					fengyx = "身份证与运营商是否匹配^无数据";
				} else {
					fengym = "姓名与运营商是否匹配^否";
					fengyx = "身份证与运营商是否匹配^否";
				}
			}
		} else {
			fengym = "姓名与运营商是否匹配^无数据";
			fengyx = "身份证与运营商是否匹配^无数据";
		}
		tableBodyData1.add(fengyx);
		tableBodyData1.add(fengym);
		// 学信网
		if (StringUtils.isNotBlank(mapXX)) {
			List<Map> listMapFromJson = JSON.parseArray(mapXX, Map.class);
			if (listMapFromJson.size() > 0) {
				Map<String, Object> mapYY1 = listMapFromJson.get(0);
				if (mapYY1.get("card_id").toString().equals(member.getIdNo())) {
					fengxm = "姓名与学信网是否匹配^是";
					fengxs = "身份证与学信网是否匹配^是";
				} else {
					fengxm = "姓名与学信网是否匹配^否";
					fengxs = "身份证与学信网是否匹配^否";
				}
			} else {
				fengxm = "姓名与学信网是否匹配^无数据";
				fengxs = "身份证与学信网是否匹配^无数据";
			}
		} else {
			fengxm = "姓名与学信网是否匹配^无数据";
			fengxs = "身份证与学信网是否匹配^无数据";
		}
		tableBodyData1.add(fengxm);
		tableBodyData1.add(fengxs);
		try {
			String information = TianjiSample.getInformation(Buser.getUsername(), member.getIdNo(), member.getName());
			if (StringUtils.isBlank(information)) {
				fengf = "手机号是否命中风险名单^否";
			} else {
				fengf = "手机号是否命中风险名单^是";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		tableBodyData1.add(fengf);
		// tableBodyData1.add(fengd);
		creditTableBasicItem1.setTableBodyData(tableBodyData1);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle1 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem1.setSpecialHandle(specialHandle1);
		creditTableBasicItemsList1.add(creditTableBasicItem1);
		creditTable1.setTableItem(creditTableBasicItemsList1);
		return creditTable1;
	}

	@Override
	public CreditTable getCreditarchivesJieDai(Member Buser) {
		CreditTable creditTable2 = new CreditReportResponseResult().new CreditTable();
		creditTable2.setTitle("借贷数据分析");
		creditTable2.setSubtitle("数据源于无忧借条，不包含补偿金");
		creditTable2.setTableType(103);
		// 是否具有筛选的表格内数据 3
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList2 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem2_1 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem2_1.setTableFiltrateTitle("最近一周");
		creditTableBasicItem2_1.setTableHeadData("类型^总金额（元）^次数（次）");
		// 表格内容数组
		// 0:全部；1：一周；2：一个月；3:6个月；4:6个月前；
		ArrayList<String> crelistweek = getloaninforCreditRecord(Buser, 1);
		creditTableBasicItem2_1.setTableBodyData(crelistweek);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle2_1 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem2_1.setSpecialHandle(specialHandle2_1);
		creditTableBasicItemsList2.add(creditTableBasicItem2_1);
		// 最近一月
		CreditTableBasicItem creditTableBasicItem2_2 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem2_2.setTableFiltrateTitle("最近一个月");
		creditTableBasicItem2_2.setTableHeadData("类型^总金额（元）^次数（次）");
		// 表格内容数组
		ArrayList<String> crelistOneMoth = getloaninforCreditRecord(Buser, 2);
		creditTableBasicItem2_2.setTableBodyData(crelistOneMoth);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle2_2 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem2_2.setSpecialHandle(specialHandle2_2);
		creditTableBasicItemsList2.add(creditTableBasicItem2_2);
		// 最近六个月
		CreditTableBasicItem creditTableBasicItem2_3 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem2_3.setTableFiltrateTitle("最近六个月");
		creditTableBasicItem2_3.setTableHeadData("类型^总金额（元）^次数（次）");
		// 表格内容数组
		ArrayList<String> crelistsixMoth = getloaninforCreditRecord(Buser, 3);
		creditTableBasicItem2_3.setTableBodyData(crelistsixMoth);

		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle2_3 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem2_3.setSpecialHandle(specialHandle2_3);
		creditTableBasicItemsList2.add(creditTableBasicItem2_3);
		// 六个月前
		CreditTableBasicItem creditTableBasicItem2_4 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem2_4.setTableFiltrateTitle("六个月前");
		creditTableBasicItem2_4.setTableHeadData("类型^总金额（元）^次数（次）");
		// 表格内容数组
		ArrayList<String> crelistsixMothbo = getloaninforCreditRecord(Buser, 4);
		creditTableBasicItem2_4.setTableBodyData(crelistsixMothbo);

		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle2_4 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem2_4.setSpecialHandle(specialHandle2_4);
		creditTableBasicItemsList2.add(creditTableBasicItem2_4);
		// 总计
		CreditTableBasicItem creditTableBasicItem2_5 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem2_5.setTableFiltrateTitle("总计");
		creditTableBasicItem2_5.setTableHeadData("类型^总金额（元）^次数（次）");
		// 表格内容数组
		ArrayList<String> crelisttotal = getloaninforCreditRecord(Buser, 0);
		creditTableBasicItem2_5.setTableBodyData(crelisttotal);

		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle2_5 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem2_5.setSpecialHandle(specialHandle2_5);
		creditTableBasicItemsList2.add(creditTableBasicItem2_5);
		creditTable2.setTableItem(creditTableBasicItemsList2);
		return creditTable2;
	}

	@Override
	public CreditTable getCreditarchivesYuQi(Member Buser) {
		CreditTable creditTable3 = new CreditReportResponseResult().new CreditTable();
		creditTable3.setTitle("逾期记录");
		creditTable3.setSubtitle("数据源于无忧借条");
		creditTable3.setTableType(103);
		// 是否具有筛选的表格内数据 3
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList3 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem3_1 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem3_1.setTableFiltrateTitle("最近一周");
		creditTableBasicItem3_1.setTableHeadData("类型^总金额（%）^次数（%）");
		// 表格内容数组
		// 0:全部；1：一周；2：一个月；3:6个月；4:6个月前；
		ArrayList<String> creoverlistweek = getoverloaninforCreditRecord(Buser, 1);
		creditTableBasicItem3_1.setTableBodyData(creoverlistweek);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle3_1 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem3_1.setSpecialHandle(specialHandle3_1);
		creditTableBasicItemsList3.add(creditTableBasicItem3_1);
		// 最近一月
		CreditTableBasicItem creditTableBasicItem3_2 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem3_2.setTableFiltrateTitle("最近一个月");
		creditTableBasicItem3_2.setTableHeadData("类型^总金额（%）^次数（%）");
		// 表格内容数组
		ArrayList<String> creoverlistone = getoverloaninforCreditRecord(Buser, 2);
		creditTableBasicItem3_2.setTableBodyData(creoverlistone);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle3_2 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem3_2.setSpecialHandle(specialHandle3_2);
		creditTableBasicItemsList3.add(creditTableBasicItem3_2);
		// 最近六个月
		CreditTableBasicItem creditTableBasicItem3_3 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem3_3.setTableFiltrateTitle("最近六个月");
		creditTableBasicItem3_3.setTableHeadData("类型^总金额（%）^次数（%）");
		// 表格内容数组
		ArrayList<String> creoverlistsix = getoverloaninforCreditRecord(Buser, 3);
		creditTableBasicItem3_3.setTableBodyData(creoverlistsix);

		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle3_3 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem3_3.setSpecialHandle(specialHandle3_3);
		creditTableBasicItemsList3.add(creditTableBasicItem3_3);

		// 六個月前
		CreditTableBasicItem creditTableBasicItem3_4 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem3_4.setTableFiltrateTitle("六个月前");
		creditTableBasicItem3_4.setTableHeadData("类型^总金额（%）^次数（%）");
		// 表格内容数组
		ArrayList<String> creoverlistsixbe = getoverloaninforCreditRecord(Buser, 4);
		creditTableBasicItem3_4.setTableBodyData(creoverlistsixbe);

		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle3_4 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem3_4.setSpecialHandle(specialHandle3_4);
		creditTableBasicItemsList3.add(creditTableBasicItem3_4);

		// 六個月前
		CreditTableBasicItem creditTableBasicItem3_5 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem3_5.setTableFiltrateTitle("总计");
		creditTableBasicItem3_5.setTableHeadData("类型^总金额（%）^次数（%）");
		// 表格内容数组
		ArrayList<String> creoverlisttotal = getoverloaninforCreditRecord(Buser, 0);
		creditTableBasicItem3_5.setTableBodyData(creoverlisttotal);

		// 是否需要特苏处理5
		ArrayList<CreditTableSpecialHandle> specialHandle3_5 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem3_5.setSpecialHandle(specialHandle3_5);
		creditTableBasicItemsList3.add(creditTableBasicItem3_5);

		creditTable3.setTableItem(creditTableBasicItemsList3);
		return creditTable3;
	}

	@Override
	public CreditTable getCreditarchivesZhiMa(Map<String, Object> userMap) {
		CreditTable creditTable6 = new CreditReportResponseResult().new CreditTable();
		creditTable6.setTitle("芝麻分认证");
		creditTable6.setSubtitle("");
		creditTable6.setTableType(102);
		// 是否具有筛选的表格内数据 3
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList6 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem6 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem6.setTableFiltrateTitle("");
		creditTableBasicItem6.setTableHeadData("");
		// 表格内容数组 1紧急联系人 2 淘宝 3运营商 4芝麻认证 5学信网 6 社保 7公积金
		ArrayList<String> tableBodyData6 = new ArrayList<String>();
		// 1紧急联系人 2 淘宝 3运营商 4芝麻认证 5学信网 6 社保 7公积金
		// 判断是否是本人
		if (userMap != null) {
			String body1 = "身份证是否与芝麻分一致^无数据";
			if (userMap.get("status") != null && !userMap.get("status").toString().equals("null")) {
				String staus = userMap.get("status").toString();
				if (staus.equals("1")) {
					body1 = "身份证是否与芝麻分一致^是";
				} else if (staus.equals("0")) {
					body1 = "身份证是否与芝麻分一致^否";
				}
			}

			String body = "芝麻分^" + userMap.get("zmFen").toString();
			tableBodyData6.add(body);
			tableBodyData6.add(body1);
		} else {
			tableBodyData6.add("");
		}
		creditTableBasicItem6.setTableBodyData(tableBodyData6);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle6 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem6.setSpecialHandle(specialHandle6);
		creditTableBasicItemsList6.add(creditTableBasicItem6);
		creditTable6.setTableItem(creditTableBasicItemsList6);
		return creditTable6;
	}

	@Override
	public CreditTable getCreditarchivesMobileNum(Map<String, Object> userMap, Member Buser) {
		CreditTable creditTable8 = new CreditReportResponseResult().new CreditTable();
		creditTable8.setTitle("手机号分析");
		creditTable8.setSubtitle("数据源于运营商");
		creditTable8.setTableType(102);
		// 是否具有筛选的表格内数据 3
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList8 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem8 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem8.setTableFiltrateTitle("");
		creditTableBasicItem8.setTableHeadData("");
		// 表格内容数组
		ArrayList<String> tableBodyData8 = new ArrayList<String>();
		String phone1 = "注册手机号^" + EncryptUtils.encryptString(Buser.getUsername(), EncryptUtils.Type.PHONE);
		// 手机号
		String phone2 = "运营商手机号^"
				+ EncryptUtils.encryptString(userMap.get("user_mobile").toString(), EncryptUtils.Type.PHONE);
		tableBodyData8.add(phone1);
		tableBodyData8.add(phone2);
		creditTableBasicItem8.setTableBodyData(tableBodyData8);

		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle8 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem8.setSpecialHandle(specialHandle8);
		creditTableBasicItemsList8.add(creditTableBasicItem8);
		creditTable8.setTableItem(creditTableBasicItemsList8);
		return creditTable8;
	}

	@Override
	public CreditTable getCreditarchivesYYover(Map<String, Object> userMap) {
		CreditTable creditTable10 = new CreditReportResponseResult().new CreditTable();
		creditTable10.setTitle("消费能力");
		creditTable10.setSubtitle("数据源于运营商");
		creditTable10.setTableType(102);
		// 是否具有筛选的表格内数据 3
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList10 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem10_1 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem10_1.setTableFiltrateTitle("最近一个月");
		creditTableBasicItem10_1.setTableHeadData("");
		// 表格内容数组
		ArrayList<String> tableBodyData10_1 = new ArrayList<String>();
		Map<String, Object> mapYY = userMap;
		String mLend = "消费总额（元）^" + mapYY.get("money1");
		String mLend1 = "基础套餐金额（元）^" + mapYY.get("taocanmoney1");
		String mLend2 = "叠加套餐金额（元）^" + mapYY.get("diejiamoney1");
		String mLend3 = "增值业务（元）^" + mapYY.get("zengzhimoney1");
		String mLend4 = "其他消费金额（元）^" + mapYY.get("othermoney1");
		tableBodyData10_1.add(mLend);
		tableBodyData10_1.add(mLend1);
		tableBodyData10_1.add(mLend2);
		tableBodyData10_1.add(mLend3);
		tableBodyData10_1.add(mLend4);

		creditTableBasicItem10_1.setTableBodyData(tableBodyData10_1);
		// 是否需要特苏处理10
		ArrayList<CreditTableSpecialHandle> specialHandle10_1 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem10_1.setSpecialHandle(specialHandle10_1);
		creditTableBasicItemsList10.add(creditTableBasicItem10_1);
		// 最近一月
		CreditTableBasicItem creditTableBasicItem10_2 = new CreditReportResponseResult().new CreditTableBasicItem();
		;
		creditTableBasicItem10_2.setTableFiltrateTitle("最近三个月");
		creditTableBasicItem10_2.setTableHeadData("");
		// 表格内容数组
		ArrayList<String> tableBodyData10_2 = new ArrayList<String>();
		String mLend12 = "消费总额（元）^" + mapYY.get("money3");
		String mLend11 = "基础套餐金额（元）^" + mapYY.get("taocanmoney3");
		String mLend21 = "叠加套餐金额（元）^" + mapYY.get("diejiamoney3");
		String mLend31 = "增值业务（元）^" + mapYY.get("zengzhimoney3");
		String mLend41 = "其他消费金额（元）^" + mapYY.get("othermoney3");
		tableBodyData10_2.add(mLend12);
		tableBodyData10_2.add(mLend11);
		tableBodyData10_2.add(mLend21);
		tableBodyData10_2.add(mLend31);
		tableBodyData10_2.add(mLend41);

		creditTableBasicItem10_2.setTableBodyData(tableBodyData10_2);
		// 是否需要特苏处理10
		ArrayList<CreditTableSpecialHandle> specialHandle10_2 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem10_2.setSpecialHandle(specialHandle10_2);
		creditTableBasicItemsList10.add(creditTableBasicItem10_2);
		// 最近六个月
		CreditTableBasicItem creditTableBasicItem10_10 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem10_10.setTableFiltrateTitle("最近六个月");
		creditTableBasicItem10_10.setTableHeadData("");
		// 表格内容数组
		ArrayList<String> tableBodyData10_10 = new ArrayList<String>();
		String mLend121 = "消费总额（元）^" + mapYY.get("money");
		String mLend111 = "基础套餐金额（元）^" + mapYY.get("taocanmoney");
		String mLend211 = "叠加套餐金额（元）^" + mapYY.get("diejiamoney");
		String mLend311 = "增值业务（元）^" + mapYY.get("zengzhimoney");
		String mLend411 = "其他消费金额（元）^" + mapYY.get("othermoney");
		tableBodyData10_10.add(mLend121);
		tableBodyData10_10.add(mLend111);
		tableBodyData10_10.add(mLend211);
		tableBodyData10_10.add(mLend311);
		tableBodyData10_10.add(mLend411);
		creditTableBasicItem10_10.setTableBodyData(tableBodyData10_10);
		// 是否需要特苏处理10
		ArrayList<CreditTableSpecialHandle> specialHandle10_10 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem10_10.setSpecialHandle(specialHandle10_10);
		creditTableBasicItemsList10.add(creditTableBasicItem10_10);
		creditTable10.setTableItem(creditTableBasicItemsList10);
		return creditTable10;
	}

	@Override
	public CreditTable getCreditarchivesTBover(Map<String, Object> userMap) {
		CreditTable creditTable11 = new CreditReportResponseResult().new CreditTable();
		creditTable11.setTitle("淘宝消费情况");
		creditTable11.setSubtitle("数据源于淘宝");
		creditTable11.setTableType(102);
		// 是否具有筛选的表格内数据 3
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList11 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem11 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem11.setTableFiltrateTitle("");
		creditTableBasicItem11.setTableHeadData("");
		// 表格内容数组

		ArrayList<String> tableBodyData11 = new ArrayList<String>();
		Map<String, Object> mapTB1 = userMap;
		String phone11 = "半年淘宝订单金额^" + mapTB1.get("money") + "（元）";
		String phone21 = "半年内价格最贵的商品^" + mapTB1.get("maxmoney") + "（元）";
		String phone211 = "半年内价格最低的商品^" + mapTB1.get("minmoney") + "（元）";
		String phone212 = "半年内淘宝月均消费^" + mapTB1.get("yuemoney") + "（元）";
		String phone213 = "半年内淘宝消费商品数量^" + mapTB1.get("yueNum") + "（件）";
		tableBodyData11.add(phone11);
		tableBodyData11.add(phone21);
		tableBodyData11.add(phone211);
		tableBodyData11.add(phone212);
		tableBodyData11.add(phone213);
		creditTableBasicItem11.setTableBodyData(tableBodyData11);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle11 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem11.setSpecialHandle(specialHandle11);
		creditTableBasicItemsList11.add(creditTableBasicItem11);
		creditTable11.setTableItem(creditTableBasicItemsList11);
		return creditTable11;
	}

	@Override
	public CreditTable getCreditarchivesTBPayover(Map<String, Object> userMap) {
		CreditTable creditTable12 = new CreditReportResponseResult().new CreditTable();
		creditTable12.setTitle("淘宝绑定支付宝信息");
		creditTable12.setSubtitle("数据源于淘宝");
		creditTable12.setTableType(102);
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList12 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem12 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem12.setTableFiltrateTitle("");
		creditTableBasicItem12.setTableHeadData("");
		// 表格内容数组
		ArrayList<String> tableBodyData12 = new ArrayList<String>();

		Map<String, Object> mapTB = userMap;
		// String tbYue = "支付宝总额（元）^" +
		// (mapTB.get("tbYue").toString().equals("null") ? 0 :
		// mapTB.get("tbYue").toString());
		String tbYEbao = "余额宝余额（元）^"
				+ (mapTB.get("tbYEbao").toString().equals("null") ? 0 : mapTB.get("tbYEbao").toString());
		String tbHuabei = "花呗信用额度（元）^"
				+ (mapTB.get("tbHuabei").toString().equals("null") ? 0 : mapTB.get("tbHuabei").toString());
		// tableBodyData12.add(tbYue);
		tableBodyData12.add(tbYEbao);
		tableBodyData12.add(tbHuabei);

		creditTableBasicItem12.setTableBodyData(tableBodyData12);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle12 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem12.setSpecialHandle(specialHandle12);
		creditTableBasicItemsList12.add(creditTableBasicItem12);
		creditTable12.setTableItem(creditTableBasicItemsList12);
		return creditTable12;
	}

	@Override
	public CreditTable getCreditarchivesaddress(Map<String, Object> userMap, Member Buser) {
		CreditTable creditTable13 = new CreditReportResponseResult().new CreditTable();
		creditTable13.setTitle("地址与收货地址");
		creditTable13.setSubtitle("数据源于交叉认证");
		creditTable13.setTableType(104);
		// 是否具有筛选的表格内数据 3
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList13 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem13 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem13.setTableFiltrateTitle("");
		creditTableBasicItem13.setTableHeadData("");
		// 表格内容数组
		// newTB.put("taoadree", taoadree);
		ArrayList<String> tableBodyData13 = new ArrayList<String>();
		Map<String, Object> map3 = userMap;
		if (StringUtils.isEmpty(Buser.getAddr())) {
			Buser.setAddr("");
		}
		String phone13 = "现居住地址^" + (StringUtils.isBlank(Buser.getAddr()) ? "" : Buser.getAddr());
		String phone32 = "淘宝收货地址^"
				+ (StringUtils.isBlank(map3.get("taoadree").toString()) ? "" : map3.get("taoadree").toString());
		tableBodyData13.add(phone13);
		tableBodyData13.add(phone32);
		creditTableBasicItem13.setTableBodyData(tableBodyData13);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle13 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem13.setSpecialHandle(specialHandle13);
		creditTableBasicItemsList13.add(creditTableBasicItem13);
		creditTable13.setTableItem(creditTableBasicItemsList13);
		return creditTable13;
	}

	@Override
	public CreditTable getCreditarchivesSheBao(Map<String, Object> userMap, Member owner, int type) {
		CreditTable creditTable14 = new CreditReportResponseResult().new CreditTable();
		creditTable14.setTitle("社保分析");
		creditTable14.setSubtitle("数据源于社保");
		creditTable14.setTableType(104);
		// 是否具有筛选的表格内数据 3
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList14 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem14 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem14.setTableFiltrateTitle("");
		creditTableBasicItem14.setTableHeadData("");
		// 表格内容数组
		ArrayList<String> tableBodyData14 = new ArrayList<String>();
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle14 = new ArrayList<CreditTableSpecialHandle>();
		Map<String, Object> map3 = userMap;
		map3 = JSONUtil.toMap(map3.get("user_info").toString());
		String shebao1 = "起缴日^-";
		if (map3.get("begin_date") != null) {
			shebao1 = "起缴日^"+ (map3.get("begin_date").toString().equals("null") ? "" : map3.get("begin_date").toString());
		}
		String shebao4 = "参加工作日期^-";
		if (map3.get("time_to_work") != null) {
			shebao4 = "参加工作日期^" + (map3.get("time_to_work").toString().toString().equals("null") ? ""
					: map3.get("time_to_work").toString());
		}
		String shebao5 = "单位名称^" + (map3.get("company_name").toString().toString().equals("null") ? ""
				: map3.get("company_name").toString().toString());
		tableBodyData14.add(shebao1);
		tableBodyData14.add(shebao4);
		tableBodyData14.add(shebao5);
		if (type == 0) {
			String shebao2 = "家庭住址^" + (StringUtils.isBlank(map3.get("home_address").toString()) ? ""
					: map3.get("home_address").toString());
			String shebao3 = "缴费详情^点击查看";
			tableBodyData14.add(shebao2);
			tableBodyData14.add(shebao3);
			CreditTableSpecialHandle creditTableSpecialHandle14_2 = new CreditReportResponseResult().new CreditTableSpecialHandle();
			creditTableSpecialHandle14_2.setHandleType(200);
			creditTableSpecialHandle14_2.setColumnNum(2);
			creditTableSpecialHandle14_2.setRowNum(5);
			creditTableSpecialHandle14_2.setHandleParameter("#436EEE");

			CreditTableSpecialHandle creditTableSpecialHandle14_3 = new CreditReportResponseResult().new CreditTableSpecialHandle();
			creditTableSpecialHandle14_3.setHandleType(201);
			creditTableSpecialHandle14_3.setColumnNum(2);
			creditTableSpecialHandle14_3.setRowNum(5);
			creditTableSpecialHandle14_3.setHandleParameter(Global.getConfig("domain")
					+ "/app/wyjt/api?actionName=member&methodName=socialSecurityDetails&param=" + owner.getId());
			specialHandle14.add(creditTableSpecialHandle14_2);
			specialHandle14.add(creditTableSpecialHandle14_3);
		}
		creditTableBasicItem14.setTableBodyData(tableBodyData14);
		creditTableBasicItem14.setSpecialHandle(specialHandle14);
		creditTableBasicItemsList14.add(creditTableBasicItem14);
		creditTable14.setTableItem(creditTableBasicItemsList14);
		return creditTable14;
	}

	@Override
	public CreditTable getCreditarchivesGongJing(Map<String, Object> userMap, Member Buser, int type) {
		CreditTable creditTable15 = new CreditReportResponseResult().new CreditTable();
		creditTable15.setTitle("公积金分析");
		creditTable15.setSubtitle("数据源于公积金");
		creditTable15.setTableType(104);
		// 是否具有筛选的表格内数据 3
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList15 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem15 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem15.setTableFiltrateTitle("");
		creditTableBasicItem15.setTableHeadData("");
		// 表格内容数组
		ArrayList<String> tableBodyData15 = new ArrayList<String>();

		Map<String, Object> map3 = userMap;
		map3 = JSONUtil.toMap(map3.get("base_info").toString());
		String gongjijin1 = "开户日期^-";
		if (map3.get("begin_date") != null) {
			gongjijin1 = "开户日期^"
					+ (map3.get("begin_date").toString().equals("null") ? "" : map3.get("begin_date").toString());
		}
		String gongjijin4 = "最后缴费日期^-";
		if (map3.get("last_pay_date") != null) {
			gongjijin4 = "最后缴费日期^"
					+ (map3.get("last_pay_date").toString().equals("null") ? "" : map3.get("last_pay_date").toString());
		}
		String gongjijin6 = "缴费状态^"
				+ (map3.get("pay_status_desc").toString().equals("null") ? "" : map3.get("pay_status_desc").toString());
		String gongjijin5 = "单位名称^-";
		if (map3.get("corp_name") != null) {
			gongjijin5 = "单位名称^"
					+ (map3.get("corp_name").toString().equals("null") ? "" : map3.get("corp_name").toString());
		}
		tableBodyData15.add(gongjijin1);
		tableBodyData15.add(gongjijin4);
		tableBodyData15.add(gongjijin6);
		tableBodyData15.add(gongjijin5);
		ArrayList<CreditTableSpecialHandle> specialHandle15 = new ArrayList<CreditTableSpecialHandle>();
		if (type == 0) {
			String gongjijin2 = "家庭住址^-";
			if (map3.get("home_address") != null) {
				gongjijin2 = "家庭住址^" + (map3.get("home_address").toString().equals("null") ? ""
						: map3.get("home_address").toString());
			}
			String gongjijin3 = "缴费详情^点击查看";
			tableBodyData15.add(gongjijin2);
			tableBodyData15.add(gongjijin3);
			CreditTableSpecialHandle creditTableSpecialHandle15_2 = new CreditReportResponseResult().new CreditTableSpecialHandle();
			creditTableSpecialHandle15_2.setHandleType(200);
			creditTableSpecialHandle15_2.setColumnNum(2);
			creditTableSpecialHandle15_2.setRowNum(6);
			creditTableSpecialHandle15_2.setHandleParameter("#436EEE");

			CreditTableSpecialHandle creditTableSpecialHandle15_3 = new CreditReportResponseResult().new CreditTableSpecialHandle();
			creditTableSpecialHandle15_3.setHandleType(201);
			creditTableSpecialHandle15_3.setColumnNum(2);
			creditTableSpecialHandle15_3.setRowNum(6);
			creditTableSpecialHandle15_3.setHandleParameter(Global.getConfig("domain")
					+ "/app/wyjt/api?actionName=member&methodName=accumulationDetails&param=" + Buser.getId());
			specialHandle15.add(creditTableSpecialHandle15_2);
			specialHandle15.add(creditTableSpecialHandle15_3);
		}

		creditTableBasicItem15.setTableBodyData(tableBodyData15);
		creditTableBasicItem15.setSpecialHandle(specialHandle15);
		creditTableBasicItemsList15.add(creditTableBasicItem15);
		creditTable15.setTableItem(creditTableBasicItemsList15);
		return creditTable15;
	}

	@Override
	public CreditTable getCreditarchivesXX(String userMap) {
		CreditTable creditTable16 = new CreditReportResponseResult().new CreditTable();
		creditTable16.setTitle("学信网分析");
		creditTable16.setSubtitle("数据源于学信网");
		creditTable16.setTableType(104);
		// 是否具有筛选的表格内数据 3
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList16 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem16 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem16.setTableFiltrateTitle("");
		creditTableBasicItem16.setTableHeadData("");
		// 表格内容数组
		ArrayList<String> tableBodyData16 = new ArrayList<String>();

		List<Map> listMapFromJson = JSON.parseArray(userMap, Map.class);
		String xuexin1 = "入学时间^--";
		String xuexin4 = "毕业时间^--";
		String xuexin6 = "院校^--";
		String xuexin5 = "专业^--";
		String xuexin2 = "学历^--";
		if (listMapFromJson.size() > 0) {
			Map<String, Object> map3 = listMapFromJson.get(0);
			xuexin1 = "入学时间^"
					+ (map3.get("entrance_date").toString().equals("null") ? "" : map3.get("entrance_date").toString());
			if (map3.get("graduate_date") != null && !map3.get("graduate_date").equals("")) {
				xuexin4 = "毕业时间^" + (map3.get("graduate_date").toString().equals("null") ? ""
						: map3.get("graduate_date").toString());
			}
			xuexin6 = "院校^" + (map3.get("school").toString().equals("null") ? "" : map3.get("school").toString());
			xuexin5 = "专业^" + (map3.get("major").toString().equals("null") ? "" : map3.get("major").toString());
			xuexin2 = "学历^" + (map3.get("edu_level").toString().equals("null") ? "" : map3.get("edu_level").toString());
		}
		tableBodyData16.add(xuexin1);
		tableBodyData16.add(xuexin4);
		tableBodyData16.add(xuexin6);
		tableBodyData16.add(xuexin5);
		tableBodyData16.add(xuexin2);
		creditTableBasicItem16.setTableBodyData(tableBodyData16);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle16 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem16.setSpecialHandle(specialHandle16);
		creditTableBasicItemsList16.add(creditTableBasicItem16);
		creditTable16.setTableItem(creditTableBasicItemsList16);
		return creditTable16;

	}


	// 表情转换空
	private static String filterEmoji(String source) {
		int len = source.length();
		StringBuilder buf = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if ((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
					|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
					|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
					|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF))) {
				buf.append(codePoint);
			}
		}
		return buf.toString();
	}

	@Override
	public CreditTable getCreditarchivesWYJieJi(Map<String, Object> userMap, Member loadById) {
		CreditTable creditTable9 = new CreditReportResponseResult().new CreditTable();
		creditTable9.setTitle("借记卡");
		creditTable9.setSubtitle("尾号(" + userMap.get("account").toString().substring(
				userMap.get("account").toString().length() - 4, userMap.get("account").toString().length()) + ")");
		creditTable9.setTableType(104);
		// 是否具有筛选的表格内数据 3
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList9 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem9 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem9.setTableFiltrateTitle("");
		creditTableBasicItem9.setTableHeadData("");

		// 表格内容数组

		Map<String, Object> mapYY = userMap;
		String phone1 = "本人银行卡^否";
		ArrayList<String> tableBodyData8 = new ArrayList<String>();
		if (loadById.getName().equals(mapYY.get("name").toString())) {
			phone1 = "本人银行卡^是";
		}
		String phone3 = "开户行^" + mapYY.get("deposit_bank").toString();
		String phone4 = "卡状态^" + mapYY.get("status").toString();
		String phone5 = "月均收入^" + mapYY.get("inc").toString();
		String phone9 = "月均支出^" + mapYY.get("out").toString();
		String phone7 = "账户余额^" + mapYY.get("balance").toString();
		String phone8 = "可用余额^" + mapYY.get("avabalance").toString();
		String phone10 = "偿还能力^" + mapYY.get("xinMin").toString() + "-" + mapYY.get("xinMax").toString();
		tableBodyData8.add(phone1);
		tableBodyData8.add(phone3);
		tableBodyData8.add(phone4);
		tableBodyData8.add(phone5);
		tableBodyData8.add(phone9);
		tableBodyData8.add(phone7);
		tableBodyData8.add(phone8);
		tableBodyData8.add(phone10);
		creditTableBasicItem9.setTableBodyData(tableBodyData8);

		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle16 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem9.setSpecialHandle(specialHandle16);
		creditTableBasicItemsList9.add(creditTableBasicItem9);
		creditTable9.setTableItem(creditTableBasicItemsList9);
		return creditTable9;
	}

	@Override
	public CreditTable getCreditarchivesWYXinYong(Map<String, Object> userMap, Member loadById) {
		CreditTable creditTable9 = new CreditReportResponseResult().new CreditTable();
		creditTable9.setTitle("信用卡");
		creditTable9.setSubtitle("尾号(" + userMap.get("account").toString().substring(
				userMap.get("account").toString().length() - 4, userMap.get("account").toString().length()) + ")");
		creditTable9.setTableType(104);
		// 是否具有筛选的表格内数据 3
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList9 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem9 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem9.setTableFiltrateTitle("");
		creditTableBasicItem9.setTableHeadData("");

		// 表格内容数组
		Map<String, Object> mapYY = userMap;
		String phone1 = "本人银行卡^否";
		ArrayList<String> tableBodyData8 = new ArrayList<String>();
		if (loadById.getName().equals(mapYY.get("name").toString())) {
			phone1 = "本人银行卡^是";
		}
		String phone3 = "开户行^" + mapYY.get("deposit_bank").toString();
		String phone5 = "月均账单额^" + mapYY.get("inc").toString();
		String phone9 = "月均还款额^" + mapYY.get("out").toString();
		String phone7 = "信用卡额度^" + mapYY.get("balance").toString();
		String phone8 = "信用卡可用额度^" + mapYY.get("avabalance").toString();
		String phone10 = "最近3个月逾期账单数^" + mapYY.get("threrover").toString();
		String phone6 = "最近6个月逾期账单数^" + mapYY.get("sixover").toString();
		tableBodyData8.add(phone1);
		tableBodyData8.add(phone3);
		tableBodyData8.add(phone5);
		tableBodyData8.add(phone9);
		tableBodyData8.add(phone7);
		tableBodyData8.add(phone8);
		tableBodyData8.add(phone10);
		tableBodyData8.add(phone6);
		creditTableBasicItem9.setTableBodyData(tableBodyData8);

		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle16 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem9.setSpecialHandle(specialHandle16);
		creditTableBasicItemsList9.add(creditTableBasicItem9);
		creditTable9.setTableItem(creditTableBasicItemsList9);
		return creditTable9;
	}

	@Override
	public CreditTable getCreditarchivesJinJiV2(Map<String, Object> map3, Map<String, Object> map4, Member member,
			Member friend) {
		CreditTable creditTable7 = new CreditReportResponseResult().new CreditTable();
		creditTable7.setTitle("紧急联系人分析");
		creditTable7.setSubtitle("数据源于交叉验证");
		creditTable7.setTableType(106);
		// 是否具有筛选的表格内数据 3
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList7 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem7 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem7.setTableFiltrateTitle("");
		creditTableBasicItem7.setTableHeadData("");
		// 表格内容数组
		ArrayList<String> tableBodyData7 = new ArrayList<String>();
		// 1紧急联系人 2 淘宝 3运营商 4芝麻认证 5学信网 6 社保 7公积金
		// 判断是否有逾期的借条
		NfsLoanRecord nfsLoanRecord = new NfsLoanRecord();
		nfsLoanRecord.setLoaner(member);
		nfsLoanRecord.setLoanee(friend);
		nfsLoanRecord.setStatus(NfsLoanRecord.Status.overdue);
		List<NfsLoanRecord> find = loanRecordService.findList(nfsLoanRecord);
		String body1 = "姓名^" + map4.get("nameF").toString() + "^" + map4.get("nameM").toString() + "^"+ map4.get("nameL").toString();//
		String body3 = "";
		if (!Collections3.isEmpty(find)) {
			body3 = "联系方式^" + map4.get("moblieF").toString() + "^" + map4.get("moblieM").toString() + "^"+ map4.get("moblieL").toString();//
		} else {
			body3 = "联系方式^" + EncryptUtils.encryptString(map4.get("moblieF").toString(), EncryptUtils.Type.PHONE) + "^"
					+ EncryptUtils.encryptString(map4.get("moblieM").toString(), EncryptUtils.Type.PHONE) + "^"
					+ EncryptUtils.encryptString(map4.get("moblieL").toString(), EncryptUtils.Type.PHONE); // +
			// "^"
			// +
			// map4.get("moblieL").toString()
		}
		int contF = 0;
		int contM = 0;
		int contL = 0;
		int tongHuaF = 0;
		int tongHuaM = 0;
		int tongHuaL = 0;
		// //取得半年
		int countF = 0;
		int countM = 0;
		int countL = 0;
		if (map3.get("call_info") != null) {
			String string5 = map3.get("call_info").toString();
			List<Map> listMapFromJson = JSON.parseArray(string5, Map.class);
			// //取得半年
			List<List<Map>> listNew = new ArrayList<List<Map>>();
			for (int i = 0; i < listMapFromJson.size(); i++) {
				Map<String, Object> map = listMapFromJson.get(i);
				if (map != null) {
					List<Map> list2 = new ArrayList<>();
					String jsonStringFromMap = JSONUtil.toJson(map);
					Map mapFromJsonString = JSONUtil.toMap(jsonStringFromMap);
					list2 = JSON.parseArray(mapFromJsonString.get("call_record").toString(), Map.class);
					listNew.add(list2);
				}
			}
			if (listNew.size() > 0) {
				for (List<Map> obj : listNew) {
					for (Map<String, Object> list : obj) {
						String strin = list.get("call_other_number").toString();
						if (strin.equals(map4.get("moblieF").toString())) {
							contF = contF + 1;
							String strin1 = list.get("call_time").toString();
							tongHuaF = tongHuaF + Integer.parseInt(strin1);
						}
						if (strin.equals(map4.get("moblieM").toString())) {
							contM = contM + 1;
							String strin1 = list.get("call_time").toString();
							tongHuaM = tongHuaM + Integer.parseInt(strin1);
						}
						if (strin.equals(map4.get("moblieL").toString())) {
							contL = contL + 1;
							String strin1 = list.get("call_time").toString();
							tongHuaL = tongHuaL + Integer.parseInt(strin1);
						}
					}
				}
			}
			String string = map3.get("sms_info").toString();
			List<Map> listMapFromJson1 = JSON.parseArray(string, Map.class);
			List<List<Map>> listNewsm = new ArrayList<List<Map>>();
			for (int i = 0; i < listMapFromJson1.size(); i++) {
				Map<String, Object> map = listMapFromJson1.get(i);
				if (map != null) {
					List<Map> list2 = new ArrayList<Map>();
					String jsonStringFromMap = JSONUtil.toJson(map);
					Map mapFromJsonString = JSONUtil.toMap(jsonStringFromMap);
					list2 = JSON.parseArray(mapFromJsonString.get("sms_record").toString(), Map.class);
					listNewsm.add(list2);
				}
			}
			if (listNewsm.size() > 0) {
				for (List<Map> obj : listNewsm) {
					if (obj.size() > 0) {
						for (Map<String, Object> list : obj) {
							String strin = list.get("msg_other_num").toString();
							if (strin.equals(map4.get("moblieF").toString())) {
								countF = countF + 1;
							}
							if (strin.equals(map4.get("moblieM").toString())) {
								countM = countM + 1;
							}
							if (strin.equals(map4.get("moblieL").toString())) {
								countL = countL + 1;
							}
						}
					}
				}
			}
		}

		tongHuaF = tongHuaF / 60;
		tongHuaM = tongHuaM / 60;
		tongHuaL = tongHuaL / 60;
		String body2 = "半年內通话次数^" + contF + "^" + contM + "^" + contL;
		String body4 = "通话时长^" + tongHuaF + "^" + tongHuaM + "^" + tongHuaL;
		String body5 = "短信次数^" + countF + "^" + countM + "^" + countL;
		tableBodyData7.add(body1);
		tableBodyData7.add(body3);
		tableBodyData7.add(body2);
		tableBodyData7.add(body4);
		tableBodyData7.add(body5);
		creditTableBasicItem7.setTableBodyData(tableBodyData7);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle7 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem7.setSpecialHandle(specialHandle7);
		creditTableBasicItemsList7.add(creditTableBasicItem7);
		creditTable7.setTableItem(creditTableBasicItemsList7);
		return creditTable7;
	}

	@Override
	public CreditTable getCreditarchivesYYBgZHF(Map<String, Object> userMap) {
		CreditTable creditTable9 = new CreditReportResponseResult().new CreditTable();
		creditTable9.setTitle("用户行为综合评分");
		creditTable9.setSubtitle("数据来源于运营商");
		creditTable9.setTableType(102);
		creditTable9.setExplainType(1);
		creditTable9.setExplainNote("用户行为的综合评分由五个方面组成：基本信息、历史借贷行为、通话行为、联系人信息、消费行为。");
		// 是否具有筛选的表格内数据 3
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList9 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem9 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem9.setTableFiltrateTitle("");
		creditTableBasicItem9.setTableHeadData("");

		// 表格内容数组

		ArrayList<String> tableBodyData8 = new ArrayList<String>();
		String phone6 = "用户综合评分^" + userMap.get("behavior_score").toString();

		tableBodyData8.add(phone6);
		creditTableBasicItem9.setTableBodyData(tableBodyData8);

		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle16 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem9.setSpecialHandle(specialHandle16);
		creditTableBasicItemsList9.add(creditTableBasicItem9);
		creditTable9.setTableItem(creditTableBasicItemsList9);
		return creditTable9;
	}

	@Override
	public CreditTable getCreditarchivesYYBgLxFx(Map<String, Object> userMap) {
		CreditTable creditTable9 = new CreditReportResponseResult().new CreditTable();
		creditTable9.setTitle("前十联系人风险分析");
		creditTable9.setSubtitle("数据来源于运营商");
		creditTable9.setTableType(102);
		// 是否具有筛选的表格内数据 3
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList9 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem9 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem9.setTableFiltrateTitle("");
		creditTableBasicItem9.setTableHeadData("");

		// 表格内容数组

		ArrayList<String> tableBodyData8 = new ArrayList<String>();
		String phone6 = "命中黑名单人数占比^" + userMap.get("black_top10_contact_total_count_ratio").toString();
		String phone1 = "申请过贷款平台的人数^"
				+ userMap.get("manyheads_top10_contact_recent6month_have_partnercode_count").toString();
		String phone2 = "人均申请贷款平台数^"
				+ userMap.get("manyheads_top10_contact_recent6month_partnercode_count_avg").toString();

		tableBodyData8.add(phone6);
		tableBodyData8.add(phone1);
		tableBodyData8.add(phone2);
		creditTableBasicItem9.setTableBodyData(tableBodyData8);

		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle16 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem9.setSpecialHandle(specialHandle16);
		creditTableBasicItemsList9.add(creditTableBasicItem9);
		creditTable9.setTableItem(creditTableBasicItemsList9);
		return creditTable9;
	}

	@Override
	public CreditTable getCreditarchivesYYBgCsFx(Map<String, Object> userMap) {
		CreditTable creditTable2 = new CreditReportResponseResult().new CreditTable();
		creditTable2.setTitle("联系人催收风险分析");
		creditTable2.setSubtitle("数据来源于运营商");
		creditTable2.setTableType(103);
		creditTable2.setExplainType(1);
		creditTable2.setExplainNote("疑似催收号码由通话行为模型推断得到，供参考。");
		ArrayList<String> crelistweek = new ArrayList<String>();
		ArrayList<String> crelistOneMoth = new ArrayList<String>();
		ArrayList<String> crelistsixMoth = new ArrayList<String>();
		String w1 = "0";
		String w2 = "0";
		String w3 = "0";
		String w4 = "0";
		String w5 = "0";
		String w6 = "0";
		String w7 = "0";
		String w11 = "0";
		String w21 = "0";
		String w31 = "0";
		String w41 = "0";
		String w51 = "0";
		String w61 = "0";
		String w71 = "0";

		// 最近三个月
		String w12 = "0";
		String w22 = "0";
		String w32 = "0";
		String w42 = "0";
		String w52 = "0";
		String w62 = "0";
		String w72 = "0";

		String wb1 = "0";
		String wb2 = "0";
		String wb3 = "0";
		String wb4 = "0";
		String wb5 = "0";
		String wb6 = "0";
		String wb7 = "0";
		String wb11 = "0";
		String wb21 = "0";
		String wb31 = "0";
		String wb41 = "0";
		String wb51 = "0";
		String wb61 = "0";
		String wb71 = "0";

		// 最近三个月
		String wb12 = "0";
		String wb22 = "0";
		String wb32 = "0";
		String wb42 = "0";
		String wb52 = "0";
		String wb62 = "0";
		String wb72 = "0";

		if (userMap.get("contact_suspect_collection_analysis") != null
				&& !userMap.get("contact_suspect_collection_analysis").equals("null")) {
			List<Map> listMapFromJson = JSON.parseArray(userMap.get("contact_suspect_collection_analysis").toString(),
					Map.class);
			if (listMapFromJson.size() > 0) {
				for (Map<String, Object> list : listMapFromJson) {
					if (list.get("risk_type").toString().equals("催收")) {

						w1 = list.get("contact_count_1month").toString();
						w2 = list.get("contact_count_active_1month").toString();
						w3 = list.get("contact_count_passive_1month").toString();
						w4 = list.get("contact_count_3month").toString();
						w5 = list.get("contact_count_active_3month").toString();
						w6 = list.get("contact_count_passive_3month").toString();
						w7 = list.get("contact_count_6month").toString();
						w11 = list.get("contact_count_active_6month").toString();
						w21 = list.get("contact_count_passive_6month").toString();
						w31 = list.get("call_count_1month").toString();
						w41 = list.get("call_count_active_1month").toString();
						w51 = list.get("call_count_passive_1month").toString();
						w61 = list.get("call_count_3month").toString();
						w71 = list.get("call_count_active_3month").toString();
						w12 = list.get("call_count_passive_3month").toString();
						w22 = list.get("call_count_6month").toString();
						w32 = list.get("call_count_active_6month").toString();
						w42 = list.get("call_count_passive_6month").toString();
						w52 = list.get("msg_count_1month").toString();
						w62 = list.get("msg_count_3month").toString();
						w72 = list.get("msg_count_6month").toString();
					} else if (list.get("risk_type").toString().equals("疑似催收")) {

						wb1 = list.get("contact_count_1month").toString();
						wb2 = list.get("contact_count_active_1month").toString();
						wb3 = list.get("contact_count_passive_1month").toString();
						wb4 = list.get("contact_count_3month").toString();
						wb5 = list.get("contact_count_active_3month").toString();
						wb6 = list.get("contact_count_passive_3month").toString();
						wb7 = list.get("contact_count_6month").toString();
						wb11 = list.get("contact_count_active_6month").toString();
						wb21 = list.get("contact_count_passive_6month").toString();
						wb31 = list.get("call_count_1month").toString();
						wb41 = list.get("call_count_active_1month").toString();
						wb51 = list.get("call_count_passive_1month").toString();
						wb61 = list.get("call_count_3month").toString();
						wb71 = list.get("call_count_active_3month").toString();
						wb12 = list.get("call_count_passive_3month").toString();
						wb22 = list.get("call_count_6month").toString();
						wb32 = list.get("call_count_active_6month").toString();
						wb42 = list.get("call_count_passive_6month").toString();
						wb52 = list.get("msg_count_1month").toString();
						wb62 = list.get("msg_count_3month").toString();
						wb72 = list.get("msg_count_6month").toString();

					}
				}

			}
		}
		String phoneth1 = "通话号码数量^" + w1 + "^" + wb1;
		String phoneth3 = "通话号码数量^" + w4 + "^" + wb4;
		String phoneth6 = "通话号码数量^" + w7 + "^" + wb7;
		String phonezj1 = "主叫号码数量^" + w2 + "^" + wb2;
		String phonezj3 = "主叫号码数量^" + w5 + "^" + wb5;
		String phonezj6 = "主叫号码数量^" + w11 + "^" + wb11;
		String phonebj1 = "被叫号码数量^" + w3 + "^" + wb3;
		String phonebj3 = "被叫号码数量^" + w6 + "^" + wb6;
		String phonebj6 = "被叫号码数量^" + w21 + "^" + wb21;
		String phonetc1 = "通话次数^" + w31 + "^" + wb31;
		String phonetc3 = "通话次数^" + w61 + "^" + wb61;
		String phonetc6 = "通话次数^" + w22 + "^" + wb22;
		String phonezc1 = "主叫通话次数^" + w41 + "^" + wb41;
		String phonezc3 = "主叫通话次数^" + w71 + "^" + wb71;
		String phonezc6 = "主叫通话次数^" + w32 + "^" + wb32;
		String phonebc1 = "被叫通话次数^" + w51 + "^" + wb51;
		String phonebc3 = "被叫通话次数^" + w12 + "^" + wb12;
		String phonebc6 = "被叫通话次数^" + w42 + "^" + wb42;
		String phonedx1 = "短信次数^" + w52 + "^" + wb52;
		String phonedx3 = "短信次数^" + w62 + "^" + wb62;
		String phonedx6 = "短信次数^" + w72 + "^" + wb72;
		crelistweek.add(phoneth1);
		crelistweek.add(phonezj1);
		crelistweek.add(phonebj1);
		crelistweek.add(phonetc1);
		crelistweek.add(phonezc1);
		crelistweek.add(phonebc1);
		crelistweek.add(phonedx1);

		// 最近三个月
		crelistOneMoth.add(phoneth3);
		crelistOneMoth.add(phonezj3);
		crelistOneMoth.add(phonebj3);
		crelistOneMoth.add(phonetc3);
		crelistOneMoth.add(phonezc3);
		crelistOneMoth.add(phonebc3);
		crelistOneMoth.add(phonedx3);

		// 最近六个月
		crelistsixMoth.add(phoneth6);
		crelistsixMoth.add(phonezj6);
		crelistsixMoth.add(phonebj6);
		crelistsixMoth.add(phonetc6);
		crelistsixMoth.add(phonezc6);
		crelistsixMoth.add(phonebc6);
		crelistsixMoth.add(phonedx6);

		// 是否具有筛选的表格内数据 3
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList2 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem2_1 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem2_1.setTableFiltrateTitle("最近一个月");
		creditTableBasicItem2_1.setTableHeadData("类型^催收号码^疑似催收号码");
		// 表格内容数组
		creditTableBasicItem2_1.setTableBodyData(crelistweek);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle2_1 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem2_1.setSpecialHandle(specialHandle2_1);
		creditTableBasicItemsList2.add(creditTableBasicItem2_1);
		// 最近一月
		CreditTableBasicItem creditTableBasicItem2_2 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem2_2.setTableFiltrateTitle("最近三个月");
		creditTableBasicItem2_2.setTableHeadData("类型^催收号码^疑似催收号码");
		// 表格内容数组

		creditTableBasicItem2_2.setTableBodyData(crelistOneMoth);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle2_2 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem2_2.setSpecialHandle(specialHandle2_2);
		creditTableBasicItemsList2.add(creditTableBasicItem2_2);
		// 最近六个月
		CreditTableBasicItem creditTableBasicItem2_3 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem2_3.setTableFiltrateTitle("最近六个月");
		creditTableBasicItem2_3.setTableHeadData("类型^催收号码^疑似催收号码");
		// 表格内容数组
		creditTableBasicItem2_3.setTableBodyData(crelistsixMoth);

		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle2_3 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem2_3.setSpecialHandle(specialHandle2_3);
		creditTableBasicItemsList2.add(creditTableBasicItem2_3);
		creditTable2.setTableItem(creditTableBasicItemsList2);
		return creditTable2;
	}

	@Override
	public CreditTable getCreditarchivesYYBgFXlr(Map<String, Object> userMap) {
		CreditTable creditTable2 = new CreditReportResponseResult().new CreditTable();
		creditTable2.setTitle("风险联系人");
		creditTable2.setSubtitle("数据来源于运营商");
		creditTable2.setTableType(101);
		ArrayList<String> crelistweek = new ArrayList<String>();
		ArrayList<String> crelistOneMoth = new ArrayList<String>();
		String w1 = "0";
		String w2 = "0";
		String w3 = "0";
		String w4 = "0";
		// 澳门
		String wl1 = "0";
		String wl2 = "0";
		String wl3 = "0";
		String wl4 = "0";
		// 律师
		String ws1 = "0";
		String ws2 = "0";
		String ws3 = "0";
		String ws4 = "0";
		// 110
		String wj1 = "0";
		String wj2 = "0";
		String wj3 = "0";
		String wj4 = "0";
		// 120

		String wy1 = "0";
		String wy2 = "0";
		String wy3 = "0";
		String wy4 = "0";
		// 法院
		String wf1 = "0";
		String wf2 = "0";
		String wf3 = "0";
		String wf4 = "0";

		if (userMap.get("risk_contact_stats") != null && !userMap.get("risk_contact_stats").equals("null")) {
			List<Map> listMapFromJson = JSON.parseArray(userMap.get("risk_contact_stats").toString(), Map.class);
			if (listMapFromJson.size() > 0) {
				for (Map<String, Object> list : listMapFromJson) {
					if (list.get("risk_type").toString().equals("小贷")) {
						w1 = list.get("call_count_active_6month").toString();
						w2 = list.get("call_count_passive_6month").toString();
						w3 = list.get("call_time_active_6month").toString();
						w4 = list.get("call_time_passive_6month").toString();

					} else if (list.get("risk_type").toString().equals("澳门电话")) {
						wl1 = list.get("call_count_active_6month").toString();
						wl2 = list.get("call_count_passive_6month").toString();
						wl3 = list.get("call_time_active_6month").toString();
						wl4 = list.get("call_time_passive_6month").toString();

					} else if (list.get("risk_type").toString().equals("律师号码")) {
						ws1 = list.get("call_count_active_6month").toString();
						ws2 = list.get("call_count_passive_6month").toString();
						ws3 = list.get("call_time_active_6month").toString();
						ws4 = list.get("call_time_passive_6month").toString();

					} else if (list.get("risk_type").toString().equals("110")) {
						wj1 = list.get("call_count_active_6month").toString();
						wj2 = list.get("call_count_passive_6month").toString();
						wj3 = list.get("call_time_active_6month").toString();
						wj4 = list.get("call_time_passive_6month").toString();

					} else if (list.get("risk_type").toString().equals("120")) {
						wy1 = list.get("call_count_active_6month").toString();
						wy1 = list.get("call_count_passive_6month").toString();
						wy1 = list.get("call_time_active_6month").toString();
						wy1 = list.get("call_time_passive_6month").toString();

					} else if (list.get("risk_type").toString().equals("法院号码")) {
						wf1 = list.get("call_count_active_6month").toString();
						wf2 = list.get("call_count_passive_6month").toString();
						wf3 = list.get("call_time_active_6month").toString();
						wf4 = list.get("call_time_passive_6month").toString();

					}
				}

			}
		}
		String phoneth1 = "小贷公司^" + w1 + "^" + w3;
		String phoneth3 = "小贷公司^" + w2 + "^" + w4;
		String phoneth6 = "澳门电话^" + wl1 + "^" + wl3;
		String phonezj1 = "澳门电话^" + wl2 + "^" + wl4;
		String phonezj3 = "律师号码^" + ws1 + "^" + ws3;
		String phonezj6 = "律师号码^" + ws2 + "^" + ws4;
		String phonebj1 = "110^" + wj1 + "^" + wj3;
		String phonebj3 = "110^" + wj2 + "^" + wj4;
		String phonebj6 = "120^" + wy1 + "^" + wy3;
		String phonetc1 = "120^" + wy2 + "^" + wy4;
		String phonetc6 = "法院号码^" + wf1 + "^" + wf3;
		String phonebc3 = "法院号码^" + wf2 + "^" + wf4;
		crelistweek.add(phoneth1);
		crelistweek.add(phoneth6);
		crelistweek.add(phonezj3);
		crelistweek.add(phonebj1);
		crelistweek.add(phonebj6);
		crelistweek.add(phonetc6);

		// 最近三个月
		crelistOneMoth.add(phoneth3);
		crelistOneMoth.add(phonezj1);
		crelistOneMoth.add(phonezj6);
		crelistOneMoth.add(phonebj3);
		crelistOneMoth.add(phonetc1);
		crelistOneMoth.add(phonebc3);

		// 是否具有筛选的表格内数据 3
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList2 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem2_1 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem2_1.setTableFiltrateTitle("主叫");
		creditTableBasicItem2_1.setTableHeadData("类型^近6月通话次数^近6月通话时长");
		// 表格内容数组
		creditTableBasicItem2_1.setTableBodyData(crelistweek);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle2_1 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem2_1.setSpecialHandle(specialHandle2_1);
		creditTableBasicItemsList2.add(creditTableBasicItem2_1);
		// 最近一月
		CreditTableBasicItem creditTableBasicItem2_2 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem2_2.setTableFiltrateTitle("被叫");
		creditTableBasicItem2_2.setTableHeadData("类型^近6月通话次数^近6月通话时长");
		// 表格内容数组

		creditTableBasicItem2_2.setTableBodyData(crelistOneMoth);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle2_2 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem2_2.setSpecialHandle(specialHandle2_2);
		creditTableBasicItemsList2.add(creditTableBasicItem2_2);

		creditTable2.setTableItem(creditTableBasicItemsList2);
		return creditTable2;
	}

	@Override
	public CreditTable getCreditarchivesYYBgJrLr(Map<String, Object> userMap) {
		CreditTable creditTable9 = new CreditReportResponseResult().new CreditTable();
		creditTable9.setTitle("金融机构联系人");
		creditTable9.setSubtitle("数据来源于运营商");
		creditTable9.setTableType(101);
		// 是否具有筛选的表格内数据 3
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList9 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem9 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem9.setTableFiltrateTitle("");
		creditTableBasicItem9.setTableHeadData("类型^近6月通话号码数^近6月通话次数");

		String w1 = "0";
		String w2 = "0";
		String w3 = "0";
		String w4 = "0";
		// 澳门
		String wl1 = "0";
		String wl2 = "0";
		String wl3 = "0";
		String wl4 = "0";
		// 律师
		String ws1 = "0";
		String ws2 = "0";
		String ws3 = "0";
		String ws4 = "0";
		ArrayList<String> tableBodyData8 = new ArrayList<String>();
		if (userMap.get("finance_contact_stats") != null && !userMap.get("finance_contact_stats").equals("null")) {
			List<Map> listMapFromJson = JSON.parseArray(userMap.get("finance_contact_stats").toString(), Map.class);
			if (listMapFromJson.size() > 0) {
				for (Map<String, Object> list : listMapFromJson) {
					if (list.get("contact_type").toString().equals("银行")) {
						w1 = list.get("contact_count_6month").toString();
						w3 = list.get("call_count_6month").toString();

					} else if (list.get("contact_type").toString().equals("投资理财")) {
						w2 = list.get("contact_count_6month").toString();
						w4 = list.get("call_count_6month").toString();

					} else if (list.get("contact_type").toString().equals("其他金融机构")) {
						wl1 = list.get("contact_count_6month").toString();
						wl3 = list.get("call_count_6month").toString();

					} else if (list.get("contact_type").toString().equals("证券")) {
						wl2 = list.get("contact_count_6month").toString();
						wl4 = list.get("call_count_6month").toString();

					} else if (list.get("contact_type").toString().equals("基金")) {
						ws1 = list.get("contact_count_6month").toString();
						ws3 = list.get("call_count_6month").toString();

					} else if (list.get("contact_type").toString().equals("期货")) {
						ws2 = list.get("contact_count_6month").toString();
						ws4 = list.get("call_count_6month").toString();
					}
				}

			}
		}
		String phone1 = "银行^" + w1 + "^" + w3;
		String phone2 = "投资理财^" + w2 + "^" + w4;
		String phone3 = "其他金融机构^" + wl1 + "^" + wl3;
		String phone4 = "证券^" + wl2 + "^" + wl4;
		String phone5 = "基金^" + ws1 + "^" + ws3;
		String phone6 = "期货^" + ws2 + "^" + ws4;
		tableBodyData8.add(phone1);
		tableBodyData8.add(phone2);
		tableBodyData8.add(phone3);
		tableBodyData8.add(phone4);
		tableBodyData8.add(phone5);
		tableBodyData8.add(phone6);
		creditTableBasicItem9.setTableBodyData(tableBodyData8);

		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle16 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem9.setSpecialHandle(specialHandle16);
		creditTableBasicItemsList9.add(creditTableBasicItem9);
		creditTable9.setTableItem(creditTableBasicItemsList9);
		return creditTable9;
	}

	@Override
	public CreditTable getCreditarchivesYYBgThTj(Map<String, Object> userMap) {
		CreditTable creditTable2 = new CreditReportResponseResult().new CreditTable();
		creditTable2.setTitle("全部通话统计");
		creditTable2.setSubtitle("数据来源于运营商");
		creditTable2.setTableType(102);
		ArrayList<String> crelistweek = new ArrayList<String>();
		ArrayList<String> crelistOneMoth = new ArrayList<String>();
		ArrayList<String> crelistOne = new ArrayList<String>();

		String wo1 = "-";
		String wo2 = "-";
		String wo3 = "-";
		String wo4 = "-";
		String wo5 = "-";
		String wo6 = "-";
		String wo7 = "-";
		String wo11 = "-";
		String wo21 = "-";
		String wo31 = "-";
		String wo41 = "-";
		String wo51 = "-";
		String wo61 = "-";
		String wo71 = "-";

		String w1 = "0";
		String w2 = "0";
		String w3 = "0";
		String w4 = "0";
		String w5 = "0";
		String w6 = "0";
		String w7 = "0";
		String w8 = "0";
		String w11 = "0";
		String w21 = "0";
		String w31 = "0";
		String w41 = "0";
		String w51 = "0";
		String w61 = "0";
		String w71 = "0";

		String wb1 = "0";
		String wb2 = "0";
		String wb3 = "0";
		String wb4 = "0";
		String wb5 = "0";
		String wb6 = "0";
		String wb7 = "0";
		String wb11 = "0";
		String wb21 = "0";
		String wb31 = "0";
		String wb41 = "0";
		String wb51 = "0";
		String wb61 = "0";
		String wb71 = "0";

		if (userMap.get("all_contact_stats") != null && !userMap.get("all_contact_stats").equals("null")) {
			Map<String, Object> list = JSONUtil.toMap(userMap.get("all_contact_stats").toString());
			if (list != null) {

				w1 = list.get("contact_count_3month").toString();
				w2 = list.get("contact_count_mutual_3month").toString();
				w3 = list.get("call_count_3month").toString();
				w4 = list.get("call_count_active_3month").toString();
				w5 = list.get("call_count_passive_3month").toString();
				w6 = list.get("call_count_late_night_3month").toString();
				w7 = list.get("call_count_work_time_3month").toString();
				w11 = list.get("call_count_offwork_time_3month").toString();
				if (list.get("contact_count_mobile_3month") != null) {
					w21 = list.get("contact_count_mobile_3month").toString();
				}
				w31 = list.get("call_time_3month").toString();
				w41 = list.get("call_time_active_3month").toString();
				w51 = list.get("call_time_passive_3month").toString();
				if (list.get("contact_count_telephone_3month") != null) {
					w61 = list.get("contact_count_telephone_3month").toString();
				}
				if (list.get("contact_count_not_mobile_telephone_3month") != null) {
					w71 = list.get("contact_count_not_mobile_telephone_3month").toString();
				}

				// 66月的
				wb1 = list.get("contact_count_6month").toString();
				wb2 = list.get("contact_count_mutual_6month").toString();
				wb3 = list.get("call_count_6month").toString();
				wb4 = list.get("call_count_active_6month").toString();
				wb5 = list.get("call_count_passive_6month").toString();
				wb6 = list.get("call_count_late_night_6month").toString();
				wb7 = list.get("call_count_work_time_6month").toString();
				wb11 = list.get("call_count_offwork_time_6month").toString();
				wb21 = list.get("contact_count_mobile_6month").toString();
				wb31 = list.get("call_time_6month").toString();
				wb41 = list.get("call_time_active_6month").toString();
				wb51 = list.get("call_time_passive_6month").toString();
				wb61 = list.get("contact_count_telephone_6month").toString();
				wb71 = list.get("contact_count_not_mobile_telephone_6month").toString();

				// 一月份 -代表无数据
				wo1 = list.get("contact_count_1month").toString();
				if (list.get("contact_count_mutual_1month") != null) {
					wo2 = list.get("contact_count_mutual_1month").toString();
				}
				if (list.get("call_count_1month") != null) {
					wo3 = list.get("call_count_1month").toString();
				}
				if (list.get("call_count_active_1month") != null) {
					wo4 = list.get("call_count_active_1month").toString();
				}
				if (list.get("call_count_passive_1month") != null) {
					wo5 = list.get("call_count_passive_1month").toString();
				}
				if (list.get("call_count_late_night_1month") != null) {
					wo6 = list.get("call_count_late_night_1month").toString();
				}
				if (list.get("call_count_offwork_time_1month") != null) {
					wo7 = list.get("call_count_offwork_time_1month").toString();
				}
				if (list.get("call_count_offwork_time_1month") != null) {
					wo11 = list.get("call_count_offwork_time_1month").toString();
				}
				if (list.get("contact_count_mooile_1month") != null) {
					wo21 = list.get("contact_count_mooile_1month").toString();
				}
				wo31 = list.get("call_time_1month").toString();
				if (list.get("call_time_active_1month") != null) {
					wo41 = list.get("call_time_active_1month").toString();
				}
				if (list.get("call_time_passive_1month") != null) {
					wo51 = list.get("call_time_passive_1month").toString();
				}
				if (list.get("contact_count_telephone_1month") != null) {
					wo61 = list.get("contact_count_telephone_1month").toString();
				}
				if (list.get("contact_count_not_mooile_telephone_1month") != null) {
					wo71 = list.get("contact_count_not_mooile_telephone_1month").toString();
				}

			}

		}
		String phone1 = "通话号码数量（个）^" + w1;
		String phone2 = "互通号码数量（个）^" + w2;
		String phone3 = "通话次数（次）^" + w3;
		String phone4 = "主叫通话次数（次）^" + w4;
		String phone5 = "被叫通话次数（次）^" + w5;
		String phone6 = "通话时长（秒）^" + w31;
		String phone7 = "主叫通话时长（秒）^" + w41;
		String phone8 = "被叫通话时长（秒）^" + w51;
		String phone9 = "深夜时间通话（次）^" + w6;
		String phone10 = "工作时间通话（次）^" + w7;
		String phone11 = "非工作时间通话（次）^" + w11;
		String phone12 = "手机通话号码数量（个）^" + w21;
		String phone13 = "固话通话号码数量（个）^" + w61;
		String phone14 = "其他通话号码数量（个）^" + w71;

		String phoneb1 = "通话号码数量（个）^" + wb1;
		String phoneb2 = "互通号码数量（个）^" + wb2;
		String phoneb3 = "通话次数（次）^" + wb3;
		String phoneb4 = "主叫通话次数（次）^" + wb4;
		String phoneb5 = "被叫通话次数（次）^" + wb5;
		String phoneb6 = "通话时长（秒）^" + wb31;
		String phoneb7 = "主叫通话时长（秒）^" + wb41;
		String phoneb8 = "被叫通话时长（秒）^" + wb51;
		String phoneb9 = "深夜时间通话（次）^" + wb6;
		String phoneb10 = "工作时间通话（次）^" + wb7;
		String phoneb11 = "非工作时间通话（次）^" + wb11;
		String phoneb12 = "手机通话号码数量（个）^" + wb21;
		String phoneb13 = "固话通话号码数量（个）^" + wb61;
		String phoneb14 = "其他通话号码数量（个）^" + wb71;

		String phoneo1 = "通话号码数量（个）^" + wo1;
		String phoneo2 = "互通号码数量（个）^" + wo2;
		String phoneo3 = "通话次数（次）^" + wo3;
		String phoneo4 = "主叫通话次数（次）^" + wo4;
		String phoneo5 = "被叫通话次数（次）^" + wo5;
		String phoneo6 = "通话时长（秒）^" + wo31;
		String phoneo7 = "主叫通话时长（秒）^" + wo41;
		String phoneo8 = "被叫通话时长（秒）^" + wo51;
		String phoneo9 = "深夜时间通话（次）^" + wo6;
		String phoneo10 = "工作时间通话（次）^" + wo7;
		String phoneo11 = "非工作时间通话（次^" + wo11;
		String phoneo12 = "手机通话号码数量（个）^" + wo21;
		String phoneo13 = "固话通话号码数量（个）^" + wo61;
		String phoneo14 = "其他通话号码数量（个）^" + wo71;

		// 三个月
		crelistweek.add(phone1);
		crelistweek.add(phone2);
		crelistweek.add(phone3);
		crelistweek.add(phone4);
		crelistweek.add(phone5);
		crelistweek.add(phone7);
		crelistweek.add(phone8);
		crelistweek.add(phone9);
		crelistweek.add(phone10);
		crelistweek.add(phone11);
		crelistweek.add(phone12);
		crelistweek.add(phone13);
		crelistweek.add(phone14);

		// 最近6个月
		crelistOneMoth.add(phoneb1);
		crelistOneMoth.add(phoneb2);
		crelistOneMoth.add(phoneb3);
		crelistOneMoth.add(phoneb4);
		crelistOneMoth.add(phoneb5);
		crelistOneMoth.add(phoneb7);
		crelistOneMoth.add(phoneb8);
		crelistOneMoth.add(phoneb9);
		crelistOneMoth.add(phoneb10);
		crelistOneMoth.add(phoneb11);
		crelistOneMoth.add(phoneb12);
		crelistOneMoth.add(phoneb13);
		crelistOneMoth.add(phoneb14);

		// 最近一个月
		crelistOne.add(phoneo1);
		crelistOne.add(phoneo2);
		crelistOne.add(phoneo3);
		crelistOne.add(phoneo4);
		crelistOne.add(phoneo5);
		crelistOne.add(phoneo7);
		crelistOne.add(phoneo8);
		crelistOne.add(phoneo9);
		crelistOne.add(phoneo10);
		crelistOne.add(phoneo11);
		crelistOne.add(phoneo12);
		crelistOne.add(phoneo13);
		crelistOne.add(phoneo14);

		// 是否具有筛选的表格内数据 3 三月
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList2 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem2_1 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem2_1.setTableFiltrateTitle("最近三个月");
		creditTableBasicItem2_1.setTableHeadData("");
		// 表格内容数组
		creditTableBasicItem2_1.setTableBodyData(crelistweek);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle2_1 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem2_1.setSpecialHandle(specialHandle2_1);
		// 最近六月
		CreditTableBasicItem creditTableBasicItem2_2 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem2_2.setTableFiltrateTitle("最近六个月");
		creditTableBasicItem2_2.setTableHeadData("");
		// 表格内容数组

		creditTableBasicItem2_2.setTableBodyData(crelistOneMoth);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle2_2 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem2_2.setSpecialHandle(specialHandle2_2);

		// 最近一月
		CreditTableBasicItem creditTableBasicItem2_3 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem2_3.setTableFiltrateTitle("最近一个月");
		creditTableBasicItem2_3.setTableHeadData("");
		// 表格内容数组

		creditTableBasicItem2_3.setTableBodyData(crelistOne);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle2_3 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem2_3.setSpecialHandle(specialHandle2_3);
		creditTableBasicItemsList2.add(creditTableBasicItem2_2);
		creditTableBasicItemsList2.add(creditTableBasicItem2_1);
		creditTableBasicItemsList2.add(creditTableBasicItem2_3);

		creditTable2.setTableItem(creditTableBasicItemsList2);
		return creditTable2;
	}

	@Override
	public CreditTable getCreditarchivesYYBgHFXF(Map<String, Object> userMap, Map<String, Object> userMapold) {
		CreditTable creditTable2 = new CreditReportResponseResult().new CreditTable();
		creditTable2.setTitle("运营商消费统计");
		creditTable2.setSubtitle("数据来源于运营商");
		creditTable2.setTableType(102);
		ArrayList<String> crelistweek = new ArrayList<String>();
		ArrayList<String> crelistOneMoth = new ArrayList<String>();
		ArrayList<String> crelistsixMoth = new ArrayList<String>();
		String w1 = "0";
		String w2 = "0";
		String w3 = "0";
		String w4 = "0";
		String w5 = "0";
		String w6 = "0";
		String w7 = "0";
		String w8 = "0";
		String w9 = "0";

		if (userMap.get("carrier_consumption_stats") != null
				&& !userMap.get("carrier_consumption_stats").equals("null")) {
			Map<String, Object> list = JSONUtil.toMap(userMap.get("carrier_consumption_stats").toString());
			if (list != null) {

				w1 = Integer.parseInt(list.get("consume_amount_1month").toString()) / 100 + "";
				w2 = Integer.parseInt(list.get("consume_amount_3month").toString()) / 100 + "";
				w3 = Integer.parseInt(list.get("consume_amount_6month").toString()) / 100 + "";
				w4 = list.get("recharge_count_1month").toString();
				w5 = list.get("recharge_count_3month").toString();
				w6 = list.get("recharge_count_6month").toString();
				w7 = Integer.parseInt(list.get("recharge_amount_1month").toString()) / 100 + "";
				w8 = Integer.parseInt(list.get("recharge_amount_3month").toString()) / 100 + "";
				w9 = Integer.parseInt(list.get("recharge_amount_6month").toString()) / 100 + "";
			}
		}
		Map<String, Object> mapYY = null;
		if (userMapold.get("3") != null) {
			mapYY = JSONUtil.toMap(userMapold.get("3").toString());
		}
		String mLend1 = "基础套餐金额（元）^" + 0;
		String mLend2 = "叠加套餐金额（元）^" + 0;
		String mLend3 = "增值业务（元）^" + 0;
		String mLend4 = "其他消费金额（元）^" + 0;

		String mLend11 = "基础套餐金额（元）^" + 0;
		String mLend21 = "叠加套餐金额（元）^" + 0;
		String mLend31 = "增值业务（元）^" + 0;
		String mLend41 = "其他消费金额（元）^" + 0;

		String mLend111 = "基础套餐金额（元）^" + 0;
		String mLend211 = "叠加套餐金额（元）^" + 0;
		String mLend311 = "增值业务（元）^" + 0;
		String mLend411 = "其他消费金额（元）^" + 0;
		if (mapYY != null) {
			mLend1 = "基础套餐金额（元）^" + mapYY.get("taocanmoney1");
			mLend2 = "叠加套餐金额（元）^" + mapYY.get("diejiamoney1");
			mLend3 = "增值业务（元）^" + mapYY.get("zengzhimoney1");
			mLend4 = "其他消费金额（元）^" + mapYY.get("othermoney1");

			mLend11 = "基础套餐金额（元）^" + mapYY.get("taocanmoney3");
			mLend21 = "叠加套餐金额（元）^" + mapYY.get("diejiamoney3");
			mLend31 = "增值业务（元）^" + mapYY.get("zengzhimoney3");
			mLend41 = "其他消费金额（元）^" + mapYY.get("othermoney3");

			mLend111 = "基础套餐金额（元）^" + mapYY.get("taocanmoney");
			mLend211 = "叠加套餐金额（元）^" + mapYY.get("diejiamoney");
			mLend311 = "增值业务（元）^" + mapYY.get("zengzhimoney");
			mLend411 = "其他消费金额（元）^" + mapYY.get("othermoney");

		}

		String phoneth1 = "消费金额（元）^" + w1;
		String phoneth3 = "消费金额（元）^" + w2;
		String phoneth6 = "消费金额（元）^" + w3;
		String phonezj1 = "充值次数（次）^" + w4;
		String phonezj3 = "充值次数（次）^" + w5;
		String phonezj6 = "充值次数（次）^" + w6;
		String phone1 = "充值金额（元）^" + w7;
		String phone3 = "充值金额（元）^" + w8;
		String phone6 = "充值金额（元）^" + w9;

		crelistweek.add(phoneth1);
		crelistweek.add(phonezj1);
		crelistweek.add(phone1);
		crelistweek.add(mLend1);
		crelistweek.add(mLend2);
		crelistweek.add(mLend3);
		crelistweek.add(mLend4);

		// 最近三个月
		crelistOneMoth.add(phoneth3);
		crelistOneMoth.add(phonezj3);
		crelistOneMoth.add(phone3);
		crelistOneMoth.add(mLend11);
		crelistOneMoth.add(mLend21);
		crelistOneMoth.add(mLend31);
		crelistOneMoth.add(mLend41);

		// 最近六个月
		crelistsixMoth.add(phoneth6);
		crelistsixMoth.add(phonezj6);
		crelistsixMoth.add(phone6);
		crelistsixMoth.add(mLend111);
		crelistsixMoth.add(mLend211);
		crelistsixMoth.add(mLend311);
		crelistsixMoth.add(mLend411);

		// 是否具有筛选的表格内数据 3
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList2 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem2_1 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem2_1.setTableFiltrateTitle("最近一个月");
		creditTableBasicItem2_1.setTableHeadData("");
		// 表格内容数组
		creditTableBasicItem2_1.setTableBodyData(crelistweek);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle2_1 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem2_1.setSpecialHandle(specialHandle2_1);
		creditTableBasicItemsList2.add(creditTableBasicItem2_1);
		// 最近一月
		CreditTableBasicItem creditTableBasicItem2_2 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem2_2.setTableFiltrateTitle("最近三个月");
		creditTableBasicItem2_2.setTableHeadData("");
		// 表格内容数组

		creditTableBasicItem2_2.setTableBodyData(crelistOneMoth);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle2_2 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem2_2.setSpecialHandle(specialHandle2_2);
		creditTableBasicItemsList2.add(creditTableBasicItem2_2);
		// 最近六个月
		CreditTableBasicItem creditTableBasicItem2_3 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem2_3.setTableFiltrateTitle("最近六个月");
		creditTableBasicItem2_3.setTableHeadData("");
		// 表格内容数组
		creditTableBasicItem2_3.setTableBodyData(crelistsixMoth);

		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle2_3 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem2_3.setSpecialHandle(specialHandle2_3);
		creditTableBasicItemsList2.add(creditTableBasicItem2_3);
		creditTable2.setTableItem(creditTableBasicItemsList2);
		return creditTable2;
	}

	@Override
	public CreditTable getCreditarchivesYYBgJMTJ(Map<String, Object> userMap) {
		CreditTable creditTable2 = new CreditReportResponseResult().new CreditTable();
		creditTable2.setTitle("静默活跃统计");
		creditTable2.setSubtitle("数据来源于运营商");
		creditTable2.setTableType(102);
		ArrayList<String> crelistweek = new ArrayList<String>();
		ArrayList<String> crelistOneMoth = new ArrayList<String>();
		String w1 = "0";
		String w2 = "0";
		String w3 = "0";
		String w4 = "0";
		String w5 = "0";
		String w6 = "0";
		String w7 = "0";
		String w8 = "0";
		String w9 = "0";
		String w10 = "0";
		String w11 = "0";
		String w12 = "0";

		if (userMap.get("active_silence_stats") != null && !userMap.get("active_silence_stats").equals("null")) {
			Map<String, Object> list = JSONUtil.toMap(userMap.get("active_silence_stats").toString());
			if (list != null) {

				w1 = list.get("active_day_1call_3month").toString();
				w2 = list.get("active_day_1call_6month").toString();
				w3 = list.get("max_continue_active_day_1call_3month").toString();
				w4 = list.get("max_continue_active_day_1call_6month").toString();
				w5 = list.get("silence_day_0call_3month").toString();
				w6 = list.get("silence_day_0call_6month").toString();
				w7 = list.get("continue_silence_day_over3_0call_3month").toString();
				w8 = list.get("continue_silence_day_over3_0call_6month").toString();
				w9 = list.get("continue_silence_day_over15_0call_3month").toString();
				w10 = list.get("continue_silence_day_over15_0call_6month").toString();
				w11 = list.get("max_continue_silence_day_0call_3month").toString();
				w12 = list.get("max_continue_silence_day_0call_6month").toString();
			}
		}
		String phone1 = "通话活跃天数（天）^" + w1;
		String phone2 = "最大连续通话活跃天数（天）^" + w3;
		String phone3 = "无通话静默天数（天）^" + w5;
		String phone4 = "连续无通话静默大于3天（次）^" + w7;
		String phone5 = "连续无通话静默大于15天（次）^" + w9;
		String phone6 = "最大连续无通话静默天数（天）^" + w11;

		String phone31 = "通话活跃天数（天）^" + w2;
		String phone32 = "最大连续通话活跃天数（天）^" + w4;
		String phone33 = "无通话静默天数（天）^" + w6;
		String phone34 = "连续无通话静默大于3天（次）^" + w8;
		String phone35 = "连续无通话静默大于15天（次）^" + w10;
		String phone36 = "最大连续无通话静默天数（天）^" + w12;

		crelistweek.add(phone1);
		crelistweek.add(phone2);
		crelistweek.add(phone3);
		crelistweek.add(phone4);
		crelistweek.add(phone5);
		crelistweek.add(phone6);

		// 最近三个月
		crelistOneMoth.add(phone31);
		crelistOneMoth.add(phone32);
		crelistOneMoth.add(phone33);
		crelistOneMoth.add(phone34);
		crelistOneMoth.add(phone35);
		crelistOneMoth.add(phone36);

		// 是否具有筛选的表格内数据 3
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList2 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem2_1 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem2_1.setTableFiltrateTitle("最近三个月");
		creditTableBasicItem2_1.setTableHeadData("");
		// 表格内容数组
		creditTableBasicItem2_1.setTableBodyData(crelistweek);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle2_1 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem2_1.setSpecialHandle(specialHandle2_1);
		creditTableBasicItemsList2.add(creditTableBasicItem2_1);
		// 最近一月
		CreditTableBasicItem creditTableBasicItem2_2 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem2_2.setTableFiltrateTitle("最近六个月");
		creditTableBasicItem2_2.setTableHeadData("");
		// 表格内容数组

		creditTableBasicItem2_2.setTableBodyData(crelistOneMoth);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle2_2 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem2_2.setSpecialHandle(specialHandle2_2);
		creditTableBasicItemsList2.add(creditTableBasicItem2_2);

		creditTable2.setTableItem(creditTableBasicItemsList2);
		return creditTable2;
	}

	@Override
	public CreditTable getCreditarchivesYYBgSJTJ(Map<String, Object> userMap) {
		CreditTable creditTable2 = new CreditReportResponseResult().new CreditTable();
		creditTable2.setTitle("近半年时间段通话统计");
		creditTable2.setSubtitle("来源于运营商");
		creditTable2.setTableType(101);
		ArrayList<String> crelistweek = new ArrayList<String>();

		String w1 = "0";
		String w2 = "0";
		String w3 = "0";
		String w4 = "0";
		String w5 = "0";
		String w6 = "0";
		String w7 = "0";
		String w8 = "0";
		String w9 = "0";
		String w10 = "0";
		String w11 = "0";
		String w12 = "0";

		// 节假日
		String wb1 = "0";
		String wb2 = "0";
		String wb3 = "0";
		String wb4 = "0";
		String wb5 = "0";
		String wb6 = "0";
		String wb7 = "0";
		String wb8 = "0";
		String wb9 = "0";
		String wb10 = "0";
		String wb11 = "0";
		String wb12 = "0";

		if (userMap.get("call_duration_stats_2hour") != null
				&& !userMap.get("call_duration_stats_2hour").equals("null")) {
			Map<String, Object> list = JSONUtil.toMap(userMap.get("call_duration_stats_2hour").toString());
			if (list != null) {

				if (list.get("call_duration_workday_6month") != null
						&& !list.get("call_duration_workday_6month").equals("null")) {
					Map<String, Object> list1 = JSONUtil.toMap(list.get("call_duration_workday_6month").toString());
					w1 = list1.get("t_0").toString();
					w2 = list1.get("t_1").toString();
					w3 = list1.get("t_2").toString();
					w4 = list1.get("t_3").toString();
					w5 = list1.get("t_4").toString();
					w6 = list1.get("t_5").toString();
					w7 = list1.get("t_6").toString();
					w8 = list1.get("t_7").toString();
					w9 = list1.get("t_8").toString();
					w10 = list1.get("t_9").toString();
					w11 = list1.get("t_10").toString();
					w12 = list1.get("t_11").toString();

				}

				if (list.get("call_duration_holiday_6month") != null
						&& !list.get("call_duration_workday_6month").equals("null")) {
					Map<String, Object> list1 = JSONUtil.toMap(list.get("call_duration_holiday_6month").toString());
					wb1 = list1.get("t_0").toString();
					wb2 = list1.get("t_1").toString();
					wb3 = list1.get("t_2").toString();
					wb4 = list1.get("t_3").toString();
					wb5 = list1.get("t_4").toString();
					wb6 = list1.get("t_5").toString();
					wb7 = list1.get("t_6").toString();
					wb8 = list1.get("t_7").toString();
					wb9 = list1.get("t_8").toString();
					wb10 = list1.get("t_9").toString();
					wb11 = list1.get("t_10").toString();
					wb12 = list1.get("t_11").toString();

				}

			}
		}
		String phone1 = "00:00-02:00^" + w1 + "^" + wb1;
		String phone2 = "02:00-04:00^" + w2 + "^" + wb2;
		String phone12 = "04:00-06:00^" + w3 + "^" + wb3;
		String phone3 = "06:00-08:00^" + w4 + "^" + wb4;
		String phone4 = "08:00-10:00^" + w5 + "^" + wb5;
		String phone5 = "10:00-12:00^" + w6 + "^" + wb6;
		String phone6 = "12:00-14:00^" + w7 + "^" + wb7;
		String phone7 = "14:00-16:00^" + w8 + "^" + wb8;
		String phone8 = "16:00-18:00^" + w9 + "^" + wb9;
		String phone9 = "18:00-20:00^" + w10 + "^" + wb10;
		String phone10 = "20:00-22:00^" + w11 + "^" + wb11;
		String phone11 = "22:00-24:00^" + w12 + "^" + wb12;
		crelistweek.add(phone1);
		crelistweek.add(phone2);
		crelistweek.add(phone12);
		crelistweek.add(phone3);
		crelistweek.add(phone4);
		crelistweek.add(phone5);
		crelistweek.add(phone6);
		crelistweek.add(phone7);
		crelistweek.add(phone8);
		crelistweek.add(phone9);
		crelistweek.add(phone10);
		crelistweek.add(phone11);

		// 是否具有筛选的表格内数据 3
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList2 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem2_1 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem2_1.setTableFiltrateTitle("");
		creditTableBasicItem2_1.setTableHeadData("时间段^工作日时长（秒）^节假日时长（秒）");
		// 表格内容数组
		creditTableBasicItem2_1.setTableBodyData(crelistweek);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle2_1 = new ArrayList<CreditTableSpecialHandle>();
		creditTableBasicItem2_1.setSpecialHandle(specialHandle2_1);
		creditTableBasicItemsList2.add(creditTableBasicItem2_1);
		creditTable2.setTableItem(creditTableBasicItemsList2);
		return creditTable2;
	}

	@Override
	public CreditTable getCreditarchivesYYBgTHXQ(Map<String, Object> userMap, Member Buser) {
		CreditTable creditTable9 = new CreditReportResponseResult().new CreditTable();
		creditTable9.setTitle("通话明细");
		creditTable9.setSubtitle("数据源于运营商");
		creditTable9.setTableType(102);
		// 是否具有筛选的表格内数据 3
		ArrayList<CreditTableBasicItem> creditTableBasicItemsList9 = new ArrayList<CreditTableBasicItem>();
		CreditTableBasicItem creditTableBasicItem9 = new CreditReportResponseResult().new CreditTableBasicItem();
		creditTableBasicItem9.setTableFiltrateTitle("");
		creditTableBasicItem9.setTableHeadData("");
		// 表格内容数组
		ArrayList<String> tableBodyData8 = new ArrayList<String>();
		String phone7 = "通话记录详情^点击查看";
		String phone8 = "话费详情^点击查看";
		tableBodyData8.add(phone7);
		tableBodyData8.add(phone8);
		creditTableBasicItem9.setTableBodyData(tableBodyData8);
		// 是否需要特苏处理4
		ArrayList<CreditTableSpecialHandle> specialHandle9 = new ArrayList<CreditTableSpecialHandle>();
		CreditTableSpecialHandle creditTableSpecialHandle9_1 = new CreditReportResponseResult().new CreditTableSpecialHandle();
		creditTableSpecialHandle9_1.setHandleType(200);
		creditTableSpecialHandle9_1.setColumnNum(2);
		creditTableSpecialHandle9_1.setRowNum(1);
		creditTableSpecialHandle9_1.setHandleParameter("#436EEE");
		CreditTableSpecialHandle creditTableSpecialHandle9_3 = new CreditReportResponseResult().new CreditTableSpecialHandle();
		creditTableSpecialHandle9_3.setHandleType(201);
		creditTableSpecialHandle9_3.setColumnNum(2);
		creditTableSpecialHandle9_3.setRowNum(1);
		creditTableSpecialHandle9_3.setHandleParameter(Global.getConfig("domain")
				+ "/app/wyjt/api?actionName=member&methodName=callRecordDetails&param=" + Buser.getId());
		CreditTableSpecialHandle creditTableSpecialHandle9_2 = new CreditReportResponseResult().new CreditTableSpecialHandle();
		creditTableSpecialHandle9_2.setHandleType(200);
		creditTableSpecialHandle9_2.setColumnNum(2);
		creditTableSpecialHandle9_2.setRowNum(2);
		creditTableSpecialHandle9_2.setHandleParameter("#436EEE");

		CreditTableSpecialHandle creditTableSpecialHandle9_4 = new CreditReportResponseResult().new CreditTableSpecialHandle();
		creditTableSpecialHandle9_4.setHandleType(201);
		creditTableSpecialHandle9_4.setColumnNum(2);
		creditTableSpecialHandle9_4.setRowNum(2);
		creditTableSpecialHandle9_4.setHandleParameter(Global.getConfig("domain")
				+ "/app/wyjt/api?actionName=member&methodName=telephoneChargesDetails&param=" + Buser.getId());
		specialHandle9.add(creditTableSpecialHandle9_1);
		specialHandle9.add(creditTableSpecialHandle9_3);
		specialHandle9.add(creditTableSpecialHandle9_2);
		specialHandle9.add(creditTableSpecialHandle9_4);
		creditTableBasicItem9.setSpecialHandle(specialHandle9);
		creditTableBasicItemsList9.add(creditTableBasicItem9);
		creditTable9.setTableItem(creditTableBasicItemsList9);
		return creditTable9;
	}

	@Override
	public ArrayList<String> getloaninforCreditRecord(Member user, int type) {
		ArrayList<String> tList = new ArrayList<String>();
		BigDecimal remainRepayMoney = new BigDecimal("0"); // 待还金额
		String remainRepayNum = "0"; // 待还次数
		BigDecimal remainCollectMoney = new BigDecimal("0"); // 待收金额
		String remainCollectNum = "0"; // 待收次数
		BigDecimal overRepayMoney = new BigDecimal("0"); // 逾期已还金额
		String overRepayNum = "0"; // 逾期已还次数
		BigDecimal overMoney = new BigDecimal("0"); // 逾期未还金额
		String overNum = "0"; // 逾期未还金额
		BigDecimal lendMoney = new BigDecimal("0"); // 借出金额
		String lendNum = "0"; // 借出次数
		BigDecimal borrowMoney = new BigDecimal("0"); // 借入金额
		String borrowNum = "0"; // 借入次数
		String onTimeNum = "0"; // 按时还款
		BigDecimal onTimeMoney = new BigDecimal("0"); // 按时还款
		String beforeTimeNum = "0"; // 延期还款次数
		BigDecimal beforeTimeMoney = new BigDecimal("0"); // 延期还款
		Date date = new Date();
		Date nowdate = new Date();
		// 时间的判断（六月前的是0，其余为1）
		int timeType = 1;
		if (type == 0) {
			nowdate = null;
		} else if (type == 1) {
			nowdate = DateUtils.addCalendarByDate(date, -7);
		} else if (type == 2) {
			nowdate = DateUtils.addCalendarByMonth(date, -1);
		} else if (type == 3) {
			nowdate = DateUtils.addCalendarByMonth(date, -6);
		} else if (type == 4) {
			nowdate = DateUtils.addCalendarByMonth(date, -6);
			timeType = 0;
		}
		NfsLoanRecord nfsLoanRecord1 = new NfsLoanRecord();
		nfsLoanRecord1.setLoanee(user);
		NfsLoanRecord nfsLoanRecord2 = new NfsLoanRecord();
		nfsLoanRecord2.setLoaner(user);
		// 借入
		List<NfsLoanRecord> findborrowandLendList3 = fsLoanRecordDao.findLoanList(nfsLoanRecord1, nowdate, timeType);
		if (findborrowandLendList3.size() > 0) {
			BigDecimal count = new BigDecimal(0);
			for (NfsLoanRecord re : findborrowandLendList3) {
				count = re.getAmount().add(re.getInterest()).add(count);
			}
			borrowNum = findborrowandLendList3.size() + "";
			borrowMoney = count;
		}
		// 待还
		List<NfsLoanRecord> findborrowandLendList = fsLoanRecordDao.findBorrowandLendList(nfsLoanRecord1, nowdate,
				timeType);
		if (findborrowandLendList.size() > 0) {
			BigDecimal count = new BigDecimal(0);
			for (NfsLoanRecord re : findborrowandLendList) {
				count = re.getDueRepayAmount().add(count);
			}
			remainRepayNum = findborrowandLendList.size() + "";
			remainRepayMoney = count;
		}
		// 统计逾期已还金额
		List<NfsLoanRecord> findborrowandLendList1 = fsLoanRecordDao.findBorrowandLendOverList(nfsLoanRecord1, nowdate,
				timeType);
		if (findborrowandLendList1.size() > 0) {
			BigDecimal count = new BigDecimal(0);
			for (NfsLoanRecord re : findborrowandLendList1) {
				count = re.getAmount().add(re.getInterest()).add(count);
			}
			overRepayNum = findborrowandLendList1.size() + "";
			overRepayMoney = count;
		}
		// 逾期未还
		List<NfsLoanRecord> findborrowandLendList2 = fsLoanRecordDao.findBorrowOverNotReturnList(nfsLoanRecord1,
				nowdate, timeType);
		if (findborrowandLendList2.size() > 0) {
			BigDecimal count = new BigDecimal(0);
			for (NfsLoanRecord re : findborrowandLendList2) {
				count = re.getAmount().add(re.getInterest()).add(count);
			}
			overMoney = count;
			overNum = findborrowandLendList2.size() + "";
		}
		// 总借出
		List<NfsLoanRecord> findborrowandLendList5 = fsLoanRecordDao.findLoanList(nfsLoanRecord2, nowdate, timeType);
		if (findborrowandLendList5.size() > 0) {
			BigDecimal count = new BigDecimal(0);
			for (NfsLoanRecord re : findborrowandLendList5) {
				count = re.getAmount().add(re.getInterest()).add(count);
			}
			lendNum = findborrowandLendList5.size() + "";
			lendMoney = count;
		}
		// 待收
		List<NfsLoanRecord> findborrowandLendList6 = fsLoanRecordDao.findBorrowandLendList(nfsLoanRecord2, nowdate,
				timeType);
		if (findborrowandLendList6.size() > 0) {
			BigDecimal count = new BigDecimal(0);
			for (NfsLoanRecord re : findborrowandLendList6) {
				count = re.getDueRepayAmount().add(count);
			}
			remainCollectNum = findborrowandLendList6.size() + "";
			remainCollectMoney = count;
		}
		// 统计按时还款

		List<NfsLoanRecord> findborrowandLendList7 = fsLoanRecordDao.findBorrowOnTimeReturnList(nfsLoanRecord1, nowdate,
				timeType);
		if (findborrowandLendList7.size() > 0) {
			BigDecimal count = new BigDecimal(0);
			for (NfsLoanRecord re : findborrowandLendList7) {
				count = re.getAmount().add(re.getInterest()).add(count);
			}
			onTimeMoney = count;
			onTimeNum = findborrowandLendList7.size() + "";
		}
		NfsLoanOperatingRecord nfsLoanOperatingRecord = new NfsLoanOperatingRecord();
		nfsLoanOperatingRecord.setOldRecord(nfsLoanRecord1);
		// 延期 findBorrowDelyList
		List<NfsLoanOperatingRecord> findborrowandLendList8 = loanOperatingRecordDao.findBorrowDelyList(nfsLoanOperatingRecord, nowdate, timeType);
		if (findborrowandLendList8.size() > 0) {
			BigDecimal count = new BigDecimal(0);
			for (NfsLoanOperatingRecord re : findborrowandLendList8) {
				count = re.getNowRecord().getAmount().add(re.getNowRecord().getInterest()).add(count);
			}
			beforeTimeNum = findborrowandLendList8.size() + "";
			beforeTimeMoney = count;
		}
		remainRepayMoney = remainRepayMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
		remainCollectMoney = remainCollectMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
		lendMoney = lendMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
		overRepayMoney = overRepayMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
		overMoney = overMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
		onTimeMoney = onTimeMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
		beforeTimeMoney = beforeTimeMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
		borrowMoney = borrowMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
		tList.add("待还^" + remainRepayMoney + "^" + remainRepayNum);
		tList.add("待收^" + remainCollectMoney + "^" + remainCollectNum);
		tList.add("借入^" + borrowMoney + "^" + borrowNum);
		tList.add("借出^" + lendMoney + "^" + lendNum);
		tList.add("逾期已还^" + overRepayMoney + "^" + overRepayNum);
		tList.add("逾期未还^" + overMoney + "^" + overNum);
		tList.add("按时还款^" + onTimeMoney + "^" + onTimeNum);
		tList.add("申请延期^" + beforeTimeMoney + "^" + beforeTimeNum);
		return tList;
	}

	@Override
	public ArrayList<String> getoverloaninforCreditRecord(Member user, int type) {
		ArrayList<String> tList = new ArrayList<String>();
		BigDecimal remainRepayMoney = new BigDecimal("0"); // 总金额
		String remainRepayNum = "0"; // 总次数
		BigDecimal overRepayMoney = new BigDecimal("0"); // 逾期已还金额
		String overRepayNum = "0"; // 逾期已还次数
		BigDecimal overMoney = new BigDecimal("0"); // 逾期未还金额
		String overNum = "0"; // 逾期未还金额
		Date date = new Date();
		Date nowdate = new Date();
		int timeType = 1;
		if (type == 0) {
			nowdate = null;
		} else if (type == 1) {
			nowdate = DateUtils.addCalendarByDate(date, -7);
		} else if (type == 2) {
			nowdate = DateUtils.addCalendarByMonth(date, -1);
		} else if (type == 3) {
			nowdate = DateUtils.addCalendarByMonth(date, -6);
		} else if (type == 4) {
			nowdate = DateUtils.addCalendarByMonth(date, -6);
			timeType = 0;
		}
		// 借入
		NfsLoanRecord nfsLoanRecord1 = new NfsLoanRecord();
		nfsLoanRecord1.setLoanee(user);
		// 借入
		List<NfsLoanRecord> findborrowandLendList3 = fsLoanRecordDao.findLoanList(nfsLoanRecord1, nowdate, timeType);
		if (findborrowandLendList3.size() > 0) {
			BigDecimal count = new BigDecimal(0);
			for (NfsLoanRecord re : findborrowandLendList3) {
				count = re.getAmount().add(re.getInterest()).add(count);
			}
			remainRepayNum = findborrowandLendList3.size() + "";
			remainRepayMoney = count;
		}

		// 统计逾期已还金额
		List<NfsLoanRecord> findborrowandLendList1 = fsLoanRecordDao.findBorrowandLendOverList(nfsLoanRecord1, nowdate,
				timeType);
		if (findborrowandLendList1.size() > 0) {
			BigDecimal count = new BigDecimal(0);
			for (NfsLoanRecord re : findborrowandLendList1) {
				count = re.getAmount().add(re.getInterest()).add(count);
			}
			overRepayNum = findborrowandLendList1.size() + "";
			overRepayMoney = count;
		}
		// 逾期未还
		List<NfsLoanRecord> findborrowandLendList2 = fsLoanRecordDao.findBorrowOverNotReturnList(nfsLoanRecord1,
				nowdate, timeType);
		if (findborrowandLendList2.size() > 0) {
			BigDecimal count = new BigDecimal(0);
			for (NfsLoanRecord re : findborrowandLendList2) {
				count = re.getAmount().add(re.getInterest()).add(count);
			}
			overMoney = count;
			overNum = findborrowandLendList2.size() + "";
		}

		// 金额百分比
		BigDecimal overMony = overMoney.add(overRepayMoney);
		BigDecimal baifenMoney = new BigDecimal("0");
		if (remainRepayMoney.intValue() != 0) {
			baifenMoney = overMony.divide(remainRepayMoney, 4, RoundingMode.HALF_UP);
		}

		// 次数百分比
		int totalNum = Integer.parseInt(remainRepayNum);
		int overnum = Integer.parseInt(overNum) + Integer.parseInt(overRepayNum);
		double baifenNum = 0;
		if (totalNum != 0) {
			baifenNum = (double) (overnum) / totalNum;
		}
		double yihuanNum = 0;
		double weihuanNum = 0;
		if (totalNum != 0) {
			yihuanNum = (double) (Integer.parseInt(overRepayNum)) / totalNum;
			weihuanNum = (double) (Integer.parseInt(overNum)) / totalNum;
		}
		BigDecimal yihuanMoney = new BigDecimal("0");
		BigDecimal weihuanMoney = new BigDecimal("0");
		if (remainRepayMoney.intValue() != 0) {
			yihuanMoney = overRepayMoney.divide(remainRepayMoney, 4, RoundingMode.HALF_UP);
			weihuanMoney = overMoney.divide(remainRepayMoney, 4, RoundingMode.HALF_UP);
		}
		// 保一位小数
		BigDecimal baifenMoney1 = baifenMoney.multiply(new BigDecimal("1000")).divide(new BigDecimal(10)).setScale(1,
				BigDecimal.ROUND_HALF_UP);
		BigDecimal baifenNum1 = new BigDecimal(baifenNum * 1000).divide(new BigDecimal(10)).setScale(1,
				BigDecimal.ROUND_HALF_UP);
		BigDecimal yihuanMoney1 = yihuanMoney.multiply(new BigDecimal("1000")).divide(new BigDecimal(10)).setScale(1,
				BigDecimal.ROUND_HALF_UP);
		BigDecimal yihuanNum1 = new BigDecimal(yihuanNum * 1000).divide(new BigDecimal(10)).setScale(1,
				BigDecimal.ROUND_HALF_UP);
		BigDecimal weihuanMoney1 = weihuanMoney.multiply(new BigDecimal("1000")).divide(new BigDecimal(10)).setScale(1,
				BigDecimal.ROUND_HALF_UP);
		BigDecimal weihuanNum1 = new BigDecimal(weihuanNum * 1000).divide(new BigDecimal(10)).setScale(1,
				BigDecimal.ROUND_HALF_UP);

		tList.add("逾期占比^" + baifenMoney1 + "^" + baifenNum1);
		// tList.add("当前逾期^" + overRepayMoney + "^" + overnum);
		tList.add("逾期已还占比^" + yihuanMoney1 + "^" + yihuanNum1);
		tList.add("逾期未还占比^" + weihuanMoney1 + "^" + weihuanNum1);
		return tList;
	}


	@Override
	public void createEmergencyContact(Member member, String nameF, String moblieF, String nameM, String moblieM,
			String nameL, String moblieL) {
		RcCaData caData = new RcCaData();
		Map<String, Object> map = new HashMap<String, Object>();
		nameF = filterEmoji(nameF);
		nameM = filterEmoji(nameM);
		nameL = filterEmoji(nameL);
		if (StringUtils.isBlank(nameF)) {
			nameF = "-";
		}
		if (StringUtils.isBlank(nameM)) {
			nameM = "-";
		}
		if (StringUtils.isBlank(nameL)) {
			nameL = "-";
		}
		map.put("nameF", nameF);
		map.put("moblieF", moblieF);
		map.put("nameM", nameM);
		map.put("moblieM", moblieM);
		map.put("nameL", nameL);
		map.put("moblieL", moblieL);
		String note = JSONUtil.toJson(map);
		caData.setPhoneNo(member.getUsername());
		caData.setIdNo(member.getIdNo());
		caData.setName(member.getName());
		caData.setContent(note);
		caData.setType(RcCaData.Type.emergency_people);
		caData.setProvider(RcCaData.Provider.sjmh);
		save(caData);
	}
	
	/**
	 * 如果之前是已认证状态过期了的话 变更成未认证
	 * @param member
	 * @param index
	 */
	private void removeVerified(Member member, int index) {
		Integer verifiedList = member.getVerifiedList();
		Boolean verified = VerifiedUtils.isVerified(verifiedList, index);
		if(verified){
			Integer removeVerified = VerifiedUtils.removeVerified(verifiedList, index);
			member.setVerifiedList(removeVerified);
			memberService.save(member);
		}
	}
	/**
	 * 获取天机H5地址
	 */
	@Override
	public String getTJAddr(Member member, int type) {
		String url = "https://openapi.rong360.com/gateway?logid=" + UUID.randomUUID().toString().replaceAll("-", "");
		Map<String,Object> params = new HashMap<String,Object>();//系统参数
		Map<String,Object> bizData = new HashMap<String,Object>();//业务参数
		String appId = "2010475";
		String version = "";
		String methodType = "";
		String returnUrl = Global.getConfig("domain")+"/app/wyjt/member/returnUrl?phoneNo=" + member.getUsername() + "&type=";
		if(type == 2) {//淘宝认证
			methodType = "taobao_crawl";
			returnUrl = returnUrl + 2;
		}else if(type == 3) {//运营商认证
			methodType = "mobile";
			version = "3";
			returnUrl = returnUrl + 1;
		}else if(type == 4) {//支付宝认证
			methodType = "alipay_crawl";
			returnUrl = returnUrl + 0;
		}else if(type == 5) {//学信网认证
			methodType = "chsi";
			returnUrl = returnUrl + 3;
		}else if(type == 6) {//社保认证
			methodType = "insure";
			returnUrl = returnUrl + 4;
		}else if(type == 7) {//公积金认证
			methodType = "fund";
			returnUrl = returnUrl + 5;
		}else if(type == 8) {//网银借记卡认证
			methodType = "ibank_crawl";
			returnUrl = returnUrl + 6;
		}
		
		bizData.put("type", methodType);
		bizData.put("platform", "web");
		bizData.put("name", member.getName());
		bizData.put("phone", member.getUsername());
		bizData.put("idNumber", member.getIdNo());
		bizData.put("userId", member.getId());
		bizData.put("outUniqueId", member.getId());
		bizData.put("version", version);
		bizData.put("notifyUrl", Global.getConfig("domain")+"/callback/tianji/callbackForH5");
		bizData.put("returnUrl", returnUrl);
		
		params.put("method", "tianji.api.tianjireport.collectuser");
		params.put("app_id", appId);
		params.put("format", "json");
		params.put("timestamp", String.valueOf(new Date().getTime()));
		params.put("biz_data", JSONUtil.toJson(bizData));
		params.put("version", "1.0.0");
		params.put("sign_type", "RSA");
		String paramsStr = getSortParams(params);
		String privateKey = getPrivateKey();
		byte[] bytes = null;
		try {
			bytes = RSAUtils.generateSHA1withRSASigature(paramsStr, privateKey, "utf-8");
		} catch (Exception e1) {
			Exceptions.getStackTraceAsString(e1);
			logger.debug("=========异常信息========"+e1);
		}
		params.put("sign", Encodes.encodeBase64(bytes));
		String json = JSONUtil.toJson(params);
		String response = "";
		try {
			response = HttpUtils.doPost(url, json);
		} catch (Exception e) {
			Exceptions.getStackTraceAsString(e);
			logger.debug("=========异常信息========"+e);
		}
		return response;
	}
	/**
     * 按key进行正序排列,之间以&相连
     * @param params 参数键值对
     * @return String 返回拼接好的字符串
     */
    public String getSortParams(Map<String, Object> params) {
        SortedMap<String, Object> map = new TreeMap<String, Object>();
        for (String key: params.keySet()) {
            map.put(key, params.get(key));
        }
        
        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        String str = "";
        while (iter.hasNext()) {
            String key = iter.next();
            String value = (String) map.get(key);
            str += key + "=" + value + "&";
        }
        if(str.length()>0){
            str = str.substring(0, str.length()-1);
        }
        return str;
    }
	/**
     * 从文件中获取私钥
     * @return String 返回文件中的私钥字符串
     */
    public String getPrivateKey() {
		try {
			String path = RcCaDataServiceImpl.class.getClassLoader().getResource("private_key.pem").getPath();
			FileReader fileReader = new FileReader(path);
			BufferedReader br = new BufferedReader(fileReader);
			String tempStr = "";
			String privateKey = "";
			int i=0;
			while((tempStr = br.readLine()) != null) {
				if(i==0 || i==15) {
					if(tempStr.contains("BEGIN PRIVATE KEY") || tempStr.contains("END PRIVATE KEY")) {
						i++;
						continue;
					}else {
						privateKey = privateKey + tempStr + "\n";
					}
				}else{
					privateKey = privateKey + tempStr + "\n";
					i++;
				}
			}
			br.close();
			fileReader.close();
			return privateKey;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getGxbToken(Member member, int type) {
		String response = "";
		String token = "";
		String authItem = getAuthItem(type);//获取认证项
		long timestamp = new Date().getTime();
		String sequenceNo = String.valueOf(member.getId());
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sequenceNo", sequenceNo);
		params.put("phone", member.getUsername());
		params.put("authItem", authItem);
		params.put("name", member.getName());
		params.put("idcard", member.getIdNo());
		params.put("appId", Global.getConfig("gxb.appIdForApp"));
		params.put("timestamp", timestamp);
		
		String sign = MD5Utils.EncoderByMd5(Global.getConfig("gxb.appIdForApp") + Global.getConfig("gxb.appSecretForApp") + authItem + timestamp + sequenceNo);
		params.put("sign", sign);
		
		String json = JSONUtil.toJson(params);
		String url = ThirdPartyUtils.url;
		try {
			response = HttpUtils.doPost(url, json);
		} catch (Exception e) {
			Exceptions.getStackTraceAsString(e);
			logger.debug("=========异常信息========"+e);
		}
		Map<String, Object> map = JSONUtil.toMap(response);
		int code = (int) map.get("retCode");
		if(code == 1) {
			JSONObject jsonObject = (JSONObject) map.get("data");
			token = (String) jsonObject.get("token");
		}else {
			logger.error("======获取公信宝token失败======原因:"+(String) map.get("retMsg"));
		}
		
		return token;
	}

	private String getAuthItem(int type) {//2 淘宝 3运营商 4芝麻分 5学信网 6 社保 7公积金  8网银
		String authItem = "";
		switch(type) {
			case 2:
				authItem = "ecommerce";
				break;
			case 3:
				authItem = "operator_plus";
				break;
			case 4:
				authItem = "sesame_multiple";
				break;
			case 5:
				authItem = "chsi";
				break;
			case 6:
				authItem = "shebao";
				break;
			case 7:
				authItem = "housefund";
				break;
			case 8:
				authItem = "ebank";
				break;
		}
		return authItem;
	}

}