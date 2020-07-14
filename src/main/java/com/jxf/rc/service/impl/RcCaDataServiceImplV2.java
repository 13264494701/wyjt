package com.jxf.rc.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.rc.dao.RcCaDataDaoV2;

import com.jxf.rc.entity.RcCaDataV2;
import com.jxf.rc.entity.RcCaDataV2.Provider;
import com.jxf.rc.entity.RcCaDataV2.Type;
import com.jxf.rc.service.RcCaDataServiceV2;
import com.jxf.rc.utils.ThirdPartyUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.security.MD5Utils;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Encodes;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.HttpUtils;
import com.jxf.svc.utils.JSONUtil;
import com.jxf.web.model.wyjt.app.member.CaAuthResponseResult;
import com.jxf.web.model.wyjt.app.member.CaAuthResponseResult.UserBriefLegalize;


/**
 * 信用报告数据表ServiceImpl
 * 
 * @author wo
 * @version 2019-08-12
 */
@Service("rcCaDataServiceV2")
@Transactional(readOnly = false)
public class RcCaDataServiceImplV2 extends CrudServiceImpl<RcCaDataDaoV2, RcCaDataV2> implements RcCaDataServiceV2 {

	@Autowired
	private RcCaDataDaoV2 caDataDao;
	@Autowired
	private MemberService memberService;


	
	/**
	 * 日志对象
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public RcCaDataV2 get(Long id) {

		return super.get(id);
	}
	
	@Override
	public List<RcCaDataV2> findList(RcCaDataV2 rcCaData) {

		return super.findList(rcCaData);
	}
	
	@Override
	public Page<RcCaDataV2> findPage(Page<RcCaDataV2> page, RcCaDataV2 rcCaData) {		
		return super.findPage(page, rcCaData);
	}

	@Override
	@Transactional(readOnly = false)
	public void save(RcCaDataV2 rcCaData) {
	
		super.save(rcCaData);
	}

	@Transactional(readOnly = false)
	public void delete(RcCaDataV2 rcCaData) {
		super.delete(rcCaData);
	}
	
	@Override
	public RcCaDataV2 getByPhoneNoAndType(String phoneNo,RcCaDataV2.Type type) {

		RcCaDataV2 caData = new RcCaDataV2();
		caData.setPhoneNo(phoneNo);
		caData.setType(type);
		return caDataDao.getByPhoneNoAndType(caData);
	}	
	@Override
	public RcCaDataV2 getSuccessRecord(String phoneNo,RcCaDataV2.Type type) {
		RcCaDataV2 caData = new RcCaDataV2();
		caData.setPhoneNo(phoneNo);
		caData.setType(type);
		caData.setStatus(RcCaDataV2.Status.success);
		List<RcCaDataV2> list = caDataDao.findList(caData);
		if(!Collections3.isEmpty(list)) {
			return list.get(0);
		}else {
			return null;
		}
	}
	
	

	@Override
	public UserBriefLegalize obtainRenZhengData(Member member, Type type) {
		UserBriefLegalize result = new CaAuthResponseResult().new UserBriefLegalize();
		String url = "";
		String urlStr = Global.getConfig("domain")+"/app/wyjt/member/returnUrl?phoneNo="+member.getUsername()+"&type=";
		RcCaDataV2 rcCaData = getByPhoneNoAndType(member.getUsername(),type);
		// 淘宝
		if (type.equals(RcCaDataV2.Type.taobao)) {
			// 查询是否认证过 1信用记录 2 淘宝 3运营商 4芝麻分 5学信网 6 社保 7公积金 8网银
			getRcStatus(rcCaData, result, member, 5);
			int thirdApplyType = Provider.tj.ordinal();
			int authenType = ThirdPartyUtils.AUTHETYPEH5;
			if(thirdApplyType == 0) {
				Map<String, String> jsonMap = new HashMap<String, String>();
				jsonMap.put("br.partnerCode", ThirdPartyUtils.partner_code);
				jsonMap.put("br.partnerKey", ThirdPartyUtils.partner_key);
				jsonMap.put("br.channel_code", ThirdPartyUtils.TBchannel_code);
				jsonMap.put("br.passback_params", member.getId() + "");
				url = JSONUtil.toJson(jsonMap);
			}
			obtainToAuthByStatus(result);
			
			if(result.getStatus() == 1) {
				result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.tb_" + result.getStatus()));
				result.setSingleCaReportUrl(Global.getConfig("domain")+"/gxt/report_taobao?key="+member.getSafeKeyValue()+"&type=taobao");
			}else {
				result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.tb_" + 0));
			}
			result.setName("淘宝认证");
			result.setJumpStatus("taobao");
			result.setUrl(url);
			result.setPageSatus(2);
			result.setAuthenType(authenType);
			result.setThirdApplyType(thirdApplyType);
		} else if (type.equals(RcCaDataV2.Type.yys)) {
			// 查询是否认证过
			getRcStatus(rcCaData, result, member, 6);
			int thirdApplyType = Provider.sjmh.ordinal();
			int authenType = ThirdPartyUtils.AUTHETYPEH5;
			if(thirdApplyType == 0) {
				url = "https://amazing.shujumohe.com/box/yys?box_token=" + Global.getConfig("sjmhtoken") + "&arr_pass_hide=real_name,identity_code,passback_params&real_name="
						+ member.getName() + "&identity_code=" + member.getIdNo() + "&passback_params=" + member.getId()+"&cb="+Encodes.urlEncode(urlStr+1);
			}
			
			obtainToAuthByStatus(result);
			
			if(result.getStatus() == 1) {
				result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.yy_" + result.getStatus()));
				result.setSingleCaReportUrl(Global.getConfig("domain")+"/gxt/report_yys?key="+member.getSafeKeyValue()+"&type=yys");
			}else {
				result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.yy_" + 0));
			}
			result.setName("运营商认证");
			result.setJumpStatus("yys");
			result.setUrl(url);
			result.setPageSatus(3);
			result.setAuthenType(authenType);
			result.setThirdApplyType(thirdApplyType);
		} else if (type.equals(RcCaDataV2.Type.zmf)) {
			// 查询是否认证过
			getRcStatus(rcCaData, result, member, 4); 
			int thirdApplyType = Provider.tj.ordinal();
			int authenType = ThirdPartyUtils.AUTHETYPEH5;
			if(thirdApplyType == 0) {//zmf这里用的是公信宝的h5
				String token = getGxbToken(member, 4);
				String str = "https://prod.gxb.io/v2/auth?returnUrl=";
				String encodeUrl = Encodes.urlEncode(Global.getConfig("domain")+"/app/wyjt/member/returnUrl?phoneNo="+member.getUsername()+"&type=0");
				url = str + encodeUrl + "&token="+token;
			}
			
			obtainToAuthByStatus(result);
			
			if(result.getStatus() == 1) {
				result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.zm_" + result.getStatus()));
				result.setSingleCaReportUrl(Global.getConfig("domain")+"/gxt/report_zmf?key="+member.getSafeKeyValue()+"&type=zmf");
			}else {
				result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.zm_" + 0));
			}
			result.setName("芝麻分");
			result.setJumpStatus("zmf");
			result.setUrl(url);
			result.setPageSatus(4);
			result.setAuthenType(authenType);
			result.setThirdApplyType(thirdApplyType);
		} else if (type.equals(RcCaDataV2.Type.xxw)) {
			// 查询是否认证过
			getRcStatus(rcCaData, result, member, 8); 
			int thirdApplyType = Provider.tj.ordinal();
			int authenType = ThirdPartyUtils.AUTHETYPEH5;
			if(thirdApplyType == 0) {
				url = "https://open.shujumohe.com/box/chsi?box_token=" + Global.getConfig("sjmhtoken") + 
						"&arr_pass_hide=passback_params&passback_params="+ member.getId()+"&cb="+Encodes.urlEncode(urlStr+3);
			}
			
			obtainToAuthByStatus(result);
			
			if(result.getStatus() == 1) {
				result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.xx_" + result.getStatus()));
				result.setSingleCaReportUrl(Global.getConfig("domain")+"/gxt/report_xxw?key="+member.getSafeKeyValue()+"&type=xxw");
			}else {
				result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.xx_" + 0));
			}
			result.setName("学信网");
			result.setJumpStatus("xxw");
			result.setUrl(url);
			result.setPageSatus(5);
			result.setAuthenType(authenType);
			result.setThirdApplyType(thirdApplyType);
		} else if (type.equals(RcCaDataV2.Type.shebao)) {
			// 查询是否认证过
			getRcStatus(rcCaData, result, member, 9); 
			int thirdApplyType = Provider.tj.ordinal();
			int authenType = ThirdPartyUtils.AUTHETYPEH5;
			if(thirdApplyType == 0) {
				url = "https://open.shujumohe.com/box/she_bao?box_token=" + Global.getConfig("sjmhtoken") 
				+ "&arr_pass_hide=passback_params&passback_params="+ member.getId()+"&cb="+Encodes.urlEncode(urlStr+4);
			}
			
			obtainToAuthByStatus(result);
			
			if(result.getStatus() == 1) {
				result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.sb_" + result.getStatus()));
				result.setSingleCaReportUrl(Global.getConfig("domain")+"/gxt/report_shebao?key="+member.getSafeKeyValue()+"&type=shebao");
			}else {
				result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.sb_" + 0));
			}
			result.setName("社保");
			result.setJumpStatus("shebao");
			result.setUrl(url);
			result.setPageSatus(6);
			result.setAuthenType(authenType);
			result.setThirdApplyType(thirdApplyType);
		} else if (type.equals(RcCaDataV2.Type.gjj)) {
			// 查询是否认证过
			getRcStatus(rcCaData, result, member, 10); 
			int thirdApplyType = Provider.tj.ordinal();
			int authenType = ThirdPartyUtils.AUTHETYPEH5;
			if(thirdApplyType == 0) {
				url = "https://open.shujumohe.com/box/gjj?box_token=" + Global.getConfig("sjmhtoken") 
				+ "&arr_pass_hide=passback_params&passback_params=" + member.getId()+"&cb="+Encodes.urlEncode(urlStr+5);
			}
			
			obtainToAuthByStatus(result);
			
			if(result.getStatus() == 1) {
				result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.gj_" + result.getStatus()));
				result.setSingleCaReportUrl(Global.getConfig("domain")+"/gxt/report_gjj?key="+member.getSafeKeyValue()+"&type=gjj");
			}else {
				result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.gj_" + 0));
			}
			result.setName("公积金");
			result.setJumpStatus("gjj");
			result.setUrl(url);
			result.setPageSatus(7);
			result.setAuthenType(authenType);
			result.setThirdApplyType(thirdApplyType);
		} else if (type.equals(RcCaDataV2.Type.wangyin)) {
			// 查询是否认证过
			getRcStatus(rcCaData, result, member, 7);
			int thirdApplyType = Provider.tj.ordinal();
			int authenType = ThirdPartyUtils.AUTHETYPEH5;
			if(thirdApplyType == 0) {
				url = "https://open.shujumohe.com/box/wy?box_token=" + Global.getConfig("sjmhtoken") 
				+ "&arr_pass_hide=passback_params&passback_params=" + member.getId()+"&cb="+Encodes.urlEncode(urlStr+6);
			}
			
			obtainToAuthByStatus(result);
			
			if(result.getStatus() == 1) {
				result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.wy_" + result.getStatus()));
				result.setSingleCaReportUrl(Global.getConfig("domain")+"/gxt/report_yhk?key="+member.getSafeKeyValue()+"&type=wangyin");
			}else {
				result.setImage(Global.getConfig("domain") + Global.getConfig("caIcon.wy_" + 0));
			}
			result.setName("银行卡账单");
			result.setJumpStatus("wangyin");
			result.setUrl(url);
			result.setPageSatus(8);
			result.setAuthenType(authenType);
			result.setThirdApplyType(thirdApplyType);
		}
		return result;
	}

	private void obtainToAuthByStatus(UserBriefLegalize result) {
		int status = result.getStatus();
		if(status == 3) {
			result.setToAuth(1);
			result.setAuthMessage("正在认证,请稍等");
		}else {
			result.setToAuth(0);
		}
	}
	private void getRcStatus(RcCaDataV2 rcCaData, UserBriefLegalize result, Member member, int i) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		if (rcCaData !=null) {
			if(rcCaData.getStatus().equals(RcCaDataV2.Status.processing)) {
				result.setDate("认证中");
				result.setStatus(3);
			}else if(rcCaData.getStatus().equals(RcCaDataV2.Status.failure)) {
				result.setDate("认证失败");
				result.setStatus(4);
			}else {
				if (cal.getTimeInMillis() <= rcCaData.getCreateTime().getTime()) {
					result.setStatus(1);
					result.setDate(DateUtils.formatDate(rcCaData.getCreateTime()));
				} else {
					result.setDate("已过期");
					result.setStatus(2);
					this.removeVerified(member,i);
				}
			}
		}else {
			result.setDate("未认证");
		}
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
}