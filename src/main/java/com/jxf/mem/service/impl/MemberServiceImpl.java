package com.jxf.mem.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import com.jxf.mem.dao.MemberActDao;
import com.jxf.mem.dao.MemberDao;
import com.jxf.mem.dao.MemberPointDao;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberAct;
import com.jxf.mem.entity.MemberCard;
import com.jxf.mem.service.MemberActService;
import com.jxf.mem.service.MemberCardService;
import com.jxf.mem.service.MemberDistributionService;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberPointService;
import com.jxf.mem.service.MemberRankService;
import com.jxf.mem.service.MemberService;


import com.jxf.mem.utils.MemUtils;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.nfs.constant.ActSubConstant;
import com.jxf.rc.entity.RcConstant;
import com.jxf.rc.entity.RcQuota;
import com.jxf.rc.service.RcQuotaService;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.model.HandleRsp;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.security.PasswordUtils;
import com.jxf.svc.security.Principal;
import com.jxf.svc.security.jwt.JwtUtil;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.IdGen;
import com.jxf.svc.utils.ObjectUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.gxt.MemberInfoGxtResponseResult;
import com.jxf.web.model.wyjt.app.WechatLoginRequestParam;
import com.jxf.web.model.wyjt.app.member.MemberInfoResponseResult;




/**
 * 会员管理ServiceImpl
 * @author HUO
 * @version 2016-04-25
 */
@Service("memberService")
@Transactional(readOnly = true)
public class MemberServiceImpl extends CrudServiceImpl<MemberDao, Member> implements MemberService{

	@Autowired
	private MemberDao memberDao;
	@Autowired
	private MemberActDao memberActDao;
	@Autowired
	private MemberPointDao memberPointDao;
	@Autowired
	private MemberRankService memberRankService;
	@Autowired
	private MemberActService memberActService;
	@Autowired
	private MemberDistributionService memberDistributionService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberPointService memberPointService;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private MemberCardService memberCardService;
	@Autowired
	private RcQuotaService rcQuotaService;


	
	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	
	
	public Member get(Long id) {
		return super.get(id);
	}
	
	public List<Member> findList(Member member) {
		return super.findList(member);
	}
	
	public Page<Member> findPage(Page<Member> page, Member member) {
		return super.findPage(page, member);
	}
	
	@Transactional(readOnly = false)
	public void save(Member member) {
		if(member.getIsNewRecord()){
			member.preInsert();
			member.setMemberRank(memberRankService.findDefault());
			member.setIsEnabled(true);
			member.setIsLocked(false);
			member.setLoginFailureCount(0);
			member.setLockedDate(null);
			member.setLoginDate(new Date());
			member.setLockKey(null);
			member.setSafeKeyValue(IdGen.uuid());
			memberDao.insert(member);
		}else{
			member.preUpdate();
			memberDao.update(member);
//			int numAttempts = 1;
//			int updateLines = 0;
//			do {
//				updateLines = memberDao.update(member);// 乐观锁重试
//				if (updateLines > 0 || numAttempts > Constant.MAX_ATTEMPTS) {
//					break;
//				} else {
//					logger.warn("会员[{}]更新失败，发起第{}次重试",member.getId(), numAttempts);
//					member = memberDao.get(member);
//					numAttempts++;
//				}
//			} while (updateLines == 0);
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public int insertHis(Member member) {

		return memberDao.insertHis(member);
	}
	
	@Transactional(readOnly = false)
	public void delete(Member member) {
		super.delete(member);
	}
	
	@Transactional(readOnly = false)
	public void initMember(Member member,Long referrerId) {
		
		save(member);
		addMemberInitTask(member,referrerId);
	}
	
	private void addMemberInitTask(final Member member,final Long referrerId) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				memberActService.initMemAct(member); //账户表
				memberPointService.initMemPoint(member);//积分表
				memberDistributionService.initMemDist(member);
			}
		});
	}

	public boolean isAuthenticated() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		return requestAttributes != null && requestAttributes.getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, RequestAttributes.SCOPE_SESSION) != null;
	}
	

	public Member getCurrent() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		Principal principal = requestAttributes != null ? (Principal) requestAttributes.getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, RequestAttributes.SCOPE_SESSION) : null;
		Long id = principal != null ? principal.getId() : null;

		return id!=null?memberDao.get(id):null;

	}
	
	public Member getCurrent2() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		Principal principal = requestAttributes != null ? (Principal) requestAttributes.getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, RequestAttributes.SCOPE_SESSION) : null;
		Long id = principal != null ? principal.getId() : null;

		return new Member(id);

	}

	public List<Member> findListByEmail(String email) {
		return memberDao.findListByEmail(email);
	}
	

	public Member findByUsername(String username) {
		return memberDao.findByUsername(username);
	}

	public boolean usernameExists(String username) {
		return StringUtils.equals(memberDao.usernameExists(username), "1");
	}
	
	public boolean emailExists(String email) {
		return StringUtils.equals(memberDao.emailExists(email), "1");
	}
	public boolean emailUnique(String previousEmail, String currentEmail) {
		if (StringUtils.equalsIgnoreCase(previousEmail, currentEmail)) {
			return true;
		}
		return !StringUtils.equals(memberDao.emailExists(currentEmail), "1");
	}


	@Override
	public String getCurrentUsername() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		Principal principal = requestAttributes != null ? (Principal) requestAttributes.getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, RequestAttributes.SCOPE_SESSION) : null;
		return principal != null ? principal.getLoginName() : null;
	}

	@Override
	public Member getByIdNo(String idNo) {
		return memberDao.getByIdNo(idNo);
	}
	
	@Override
	public Member getBySafeKey(String safeKey) {

		return memberDao.getBySafeKey(safeKey);
	}
	@Override
	@Transactional(readOnly= false)
	public void resetPayPw(Long memberId, String payPw1) {
		memberDao.resetPayPw(memberId,payPw1);
	}
	@Override
	@Transactional(readOnly= false)
	public int updateRankNo(Member member) {
		return memberDao.updateRankNo(member);
	}

	@Override
	public BigDecimal getAvlBal(Member member) {

		return memberActService.getAvlBal(member);
	}

	@Override
	public Long findByOrgUsername(String username) {

		return memberDao.findByOrgUsername(username);
	}
	
	@Override
	public String getSpareMobile(String username) {
		
		return memberDao.getSpareMobile(username);
	}
	
	@Override
	@Transactional(readOnly= false)
	public HandleRsp updateUsername(String oldUsername,String newUsername) {

		Member newMember = findByUsername(newUsername);
		if(newMember!=null) {
			Integer verifiedList = newMember.getVerifiedList();	
			if(VerifiedUtils.isVerified(verifiedList, 1)&&VerifiedUtils.isVerified(verifiedList, 2)) {
				return HandleRsp.fail("新用户已经做过实名认证,不能删除");
			}							

			List<MemberAct> memberActList = memberActService.getMemberActList(newMember);
			for (MemberAct act : memberActList) {
				if(act.getCurBal().compareTo(BigDecimal.ZERO) != 0) {
					return HandleRsp.fail("新用户账户余额不为零,不能删除");
				}
			}
			memberDao.insertHis(newMember);
			memberDao.delete(newMember);
			memberActDao.deleteByMemberId(newMember.getId());
			memberPointDao.deleteByMemberId(newMember.getId());
		}
		
		Member oldMember = findByUsername(oldUsername);
		oldMember.setUsername(newUsername);
		memberDao.updateUsername(oldMember);
		return HandleRsp.success("更新成功");
	}
	
	
	@Override
	public MemberInfoResponseResult getMemberInfo(Member member) {

		MemberInfoResponseResult result = new MemberInfoResponseResult();
		Boolean getFromDB = true;
//		Map<String, Object> memberInfoMap = RedisUtils.getHashEntries("memberInfo"+member.getId());
//		
//		if(memberInfoMap != null && memberInfoMap.size()>0){
//			try {
//				result = (MemberInfoResponseResult) ObjectUtils.mapToObject(memberInfoMap, MemberInfoResponseResult.class);
//				getFromDB = false;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}			
//		}
		if(getFromDB) {		
			BigDecimal curBal =  BigDecimal.ZERO;									
			MemberAct memberAct = new MemberAct();
			memberAct.setMember(member);
			List<MemberAct> memberActList = memberActService.findList(memberAct);
			for (MemberAct act : memberActList) {
                if(StringUtils.equals(act.getSubNo(), ActSubConstant.MEMBER_AVL_BAL)) {
                	BigDecimal avlBal = act.getCurBal();
                	curBal = curBal.add(avlBal);
                	result.setAvlBal(StringUtils.decimalToStr(avlBal, 2));
				}else if(StringUtils.equals(act.getSubNo(), ActSubConstant.MEMBER_LOAN_BAL)) {
					curBal = curBal.add(act.getCurBal());
					result.setLoanBal(StringUtils.decimalToStr(act.getCurBal(), 2));
				}else if(StringUtils.equals(act.getSubNo(), ActSubConstant.MEMBER_PENDING_REPAYMENT)){//总待还
					BigDecimal pendingRepayment = act.getCurBal();
					result.setPendingRepayment(StringUtils.decimalToStr(pendingRepayment, 2));
				}else if(StringUtils.equals(act.getSubNo(), ActSubConstant.MEMBER_PENDING_RECEIVE)){//总待收
					BigDecimal pendingReceive = act.getCurBal();
					result.setPendingReceive(StringUtils.decimalToStr(pendingReceive, 2));
				}
			}
			int newMsgCount = memberMessageService.getCountsUnRead(member);//未读消息数
			result.setNewMsgCount(newMsgCount);
			result.setCurBal(StringUtils.decimalToStr(curBal, 2));
			
			result.setMemberId(String.valueOf(member.getId()));
			result.setName(StringUtils.isNotBlank(member.getName())?StringEscapeUtils.unescapeHtml4(member.getName()): "未实名认证"  );
			result.setNickname(member.getNickname());
			result.setHeadImage(member.getHeadImage());
			result.setPhoneNo(member.getUsername());
			result.setIdNo(member.getIdNo());
			result.setEmail(member.getEmail());
			result.setAddr(member.getAddr());
			result.setRankName(member.getMemberRank() == null ? "" : member.getMemberRank().getRankNo());
			
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			RcQuota memberQuota = rcQuotaService.getByMemberId(member.getId());
			if(StringUtils.equals(request.getHeader("x-osType"), "ios") && StringUtils.equals(request.getHeader("x-appVersion"), "4.10")){
				result.setQuotaAssessment("");
			}else {
				if(memberQuota != null && memberQuota.getQuota() != null) {
					String quotaAssesment = String.format(RcConstant.REMINDER_OF_QUOTA_IN_MEMBERINFO_PAGE, StringUtils.numberToStringWithComma(memberQuota.getQuota()));
					result.setQuotaAssessment(quotaAssesment);
				}else {
					result.setQuotaAssessment(RcConstant.REMINDER_OF_NO_QUOTA_IN_MEMBERINFO_PAGE);
				}
			}
			Integer verifiedList = member.getVerifiedList();
			result.setRealIdentityStatus(VerifiedUtils.isVerified(verifiedList)?1:0);//实名认证	
			result.setBankCardStatus(VerifiedUtils.isVerified(verifiedList, 3)?1:0);//银行卡认证					
			result.setZhimafenStatus(VerifiedUtils.isVerified(verifiedList,4)?1:0);//芝麻信用
			result.setTaobaoStatus(VerifiedUtils.isVerified(verifiedList, 5)?1:0);//淘宝授权
			result.setYunyingshangStatus(VerifiedUtils.isVerified(verifiedList,6)?1:0);//运营商授权
			result.setBankTrxStatus(VerifiedUtils.isVerified(verifiedList,7)?1:0);//银行卡账单
			result.setXuexingwangStatus(VerifiedUtils.isVerified(verifiedList,8)?1:0);//学信网
			result.setShebaoStatus(VerifiedUtils.isVerified(verifiedList,9)?1:0);//社保
			result.setGongjijingStatus(VerifiedUtils.isVerified(verifiedList,10)?1:0);//公积金
			result.setPayPwStatus(VerifiedUtils.isVerified(verifiedList, 22)?1:0);//支付密码已设置
			result.setEmailStatus(VerifiedUtils.isVerified(verifiedList, 23)?1:0);
			result.setEmergencyStatus(VerifiedUtils.isVerified(verifiedList, 24)?1:0);//绑定了紧急联系人
			result.setTooManyFriends(VerifiedUtils.isVerified(verifiedList, 25)?1:0);
			result.setShowCurBal(1);
			result.setRealUsername(member.getUsername());
			result.setIsAuth(member.getIsAuth() == true?"1":"0");
			RedisUtils.putAll("memberInfo"+member.getId(),ObjectUtils.objectToMap(result));
		}
		
		return MemUtils.maskResult(result);
	}
	@Override
	@Transactional(readOnly = false)
	public MemberInfoResponseResult appLogin(Member member,Long wxUserInfoId,HttpServletRequest request,WechatLoginRequestParam wechatLoginRequestParam) {
		String osType = request.getHeader("x-osType");
    	String appVersion = request.getHeader("x-appVersion");
		String loginIp = Global.getRemoteAddr(request);
		member.setLoginIp(loginIp);
		member.setLoginDate(new Date());
		member.setLoginFailureCount(0);
		member.setIsAuth(true);
		memberService.save(member);

		String deviceToken;
//    		String osType;
		String osVersion;
//    		String appVersion;
		String ak;
		String deviceModel;
		String channeId;
		String pushToken;
		String loginTime;

		if (wechatLoginRequestParam != null) {
			deviceToken = wechatLoginRequestParam.getDeviceToken();
			osType = wechatLoginRequestParam.getOsType();
			osVersion = wechatLoginRequestParam.getOsVersion();
			appVersion = wechatLoginRequestParam.getAppVersion();
			ak = wechatLoginRequestParam.getAk();
			deviceModel = wechatLoginRequestParam.getDeviceModel();
			channeId = wechatLoginRequestParam.getChanneId();
			pushToken = request.getHeader("x-pushToken");
			loginTime = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
		} else {
			deviceToken = (String) RedisUtils.getHashKey("loginInfo" + wxUserInfoId, "deviceToken");
			osType = (String) RedisUtils.getHashKey("loginInfo" + wxUserInfoId, "osType");
			osVersion = (String) RedisUtils.getHashKey("loginInfo" + wxUserInfoId, "osVersion");
			appVersion = (String) RedisUtils.getHashKey("loginInfo" + wxUserInfoId, "appVersion");
			ak = (String) RedisUtils.getHashKey("loginInfo" + wxUserInfoId, "ak");
			deviceModel = (String) RedisUtils.getHashKey("loginInfo" + wxUserInfoId, "deviceModel");
			channeId = (String) RedisUtils.getHashKey("loginInfo" + wxUserInfoId, "channeId");
			pushToken = (String) RedisUtils.getHashKey("loginInfo" + wxUserInfoId, "deviceToken");
			loginIp = (String) RedisUtils.getHashKey("loginInfo" + wxUserInfoId, "deviceToken");
			loginTime = (String) RedisUtils.getHashKey("loginInfo" + wxUserInfoId, "deviceToken");
		}

		String cacheDeviceToken = (String) RedisUtils.getHashKey("loginInfo" + member.getId(), "deviceToken");
		MemberInfoResponseResult result = memberService.getMemberInfo(member);
		if (StringUtils.isNotBlank(cacheDeviceToken) && StringUtils.equals(cacheDeviceToken, deviceToken)) {
			result.setResultCode(1);
			result.setResultMessage("该账号已在其它设备登录,如非本人操作，请及时修改密码或与客服联系。");
		}
		RedisUtils.delete("loginInfo" + wxUserInfoId);
		RedisUtils.put("loginInfo" + member.getId(), "osType", osType);
		RedisUtils.put("loginInfo" + member.getId(), "osVersion", osVersion);
		RedisUtils.put("loginInfo" + member.getId(), "appVersion", appVersion);
		RedisUtils.put("loginInfo" + member.getId(), "ak", ak);
		RedisUtils.put("loginInfo" + member.getId(), "deviceModel", deviceModel);
		RedisUtils.put("loginInfo" + member.getId(), "deviceToken", deviceToken);
		RedisUtils.put("loginInfo" + member.getId(), "channeId", channeId);
		RedisUtils.put("loginInfo" + member.getId(), "pushToken", pushToken);
		RedisUtils.put("loginInfo" + member.getId(), "loginIp", loginIp);
		RedisUtils.put("loginInfo" + member.getId(), "loginTime", loginTime);
		
		HashMap<String, Object> payLoad = new HashMap<>();
		payLoad.put("id", member.getId());
		payLoad.put("deviceToken", deviceToken);
		String memberToken = JwtUtil.generToken(payLoad, Global.getTokenTimeout());
		result.setMemberToken(memberToken);
		return result;
    	
		
	}
	@Override
	public ResponseData checkPayPwd(String pwd, Member member) {

		//TODO 优化使用RedisUtils.increment
		if(!VerifiedUtils.isVerified(member.getVerifiedList(), 22)) {
			return ResponseData.error("请先设置支付密码");
		}
		String payPassword = member.getPayPassword();
		Integer wrongTimes = 0;
		String isLocked = (String)RedisUtils.getHashKey("payPassword"+member.getId(), "isLocked");
		if(StringUtils.isBlank(isLocked) || StringUtils.equals(isLocked, "0")) {
			//未冻结
			boolean pass = PasswordUtils.validatePassword(pwd, payPassword);
			if(pass) {
				RedisUtils.put("payPassword"+member.getId(), "wrongTimes", "0");
				return ResponseData.success("密码正确");
			}else {
				String wrongTimesStr = (String)RedisUtils.getHashKey("payPassword"+member.getId(), "wrongTimes");
				wrongTimes = StringUtils.toInteger(wrongTimesStr) + 1;
				if(wrongTimes >= 5) {
					RedisUtils.put("payPassword"+member.getId(), "wrongTimes", String.valueOf(wrongTimes));
					RedisUtils.put("payPassword"+member.getId(), "isLocked", "1");
					RedisUtils.put("payPassword"+member.getId(), "lockedTime", System.currentTimeMillis()+"");
					return ResponseData.error("账户已冻结，请30分钟后再试");
				}else {
					RedisUtils.put("payPassword"+member.getId(), "wrongTimes", String.valueOf(wrongTimes));
					return ResponseData.error("密码输入错误，您还有" + (5-wrongTimes) + "次机会");
				}
			}
		}else {
			Long nowTime = System.currentTimeMillis();
			String lockedTimeStr = (String)RedisUtils.getHashKey("payPassword"+member.getId(), "lockedTime");
			Long lockedTime = Long.valueOf(lockedTimeStr);
			if(nowTime - lockedTime > 1000*60*30) {
				RedisUtils.put("payPassword"+member.getId(), "isLocked", "0");
				boolean pass = PasswordUtils.validatePassword(pwd, payPassword);
				if(pass) {
					RedisUtils.put("payPassword"+member.getId(), "wrongTimes", "0");
					return ResponseData.success("密码正确");
				}else {
					wrongTimes = wrongTimes + 1;
					RedisUtils.put("payPassword"+member.getId(), "wrongTimes", wrongTimes + "");
					return ResponseData.error("密码输入错误，您还有" + (5-wrongTimes) + "次机会");
				}
			}else {
				return ResponseData.error("账户已冻结，请30分钟后再试");
			}
		}
	}


	@Override
	public BigDecimal getCulBal(Member member) {

		BigDecimal curBal =  BigDecimal.ZERO;									
		MemberAct memberAct = new MemberAct();
		memberAct.setMember(member);
		List<MemberAct> memberActList = memberActService.findList(memberAct);
		for (MemberAct act : memberActList) {
            if(StringUtils.equals(act.getSubNo(), ActSubConstant.MEMBER_AVL_BAL)) {
            	curBal = curBal.add(act.getCurBal());
			}else if(StringUtils.equals(act.getSubNo(), ActSubConstant.MEMBER_LOAN_BAL)) {
				curBal = curBal.add(act.getCurBal());
			}
		}
		return curBal;
	}



	@Override
	public Boolean lockExists(Member member) {
		
		if(StringUtils.equals(memberDao.lockExists1(member), "1"))
		{
			return true;
		}
//		if(StringUtils.equals(memberDao.lockExists2(member), "1"))
//		{
//			return true;
//		}
//		if(StringUtils.equals(memberDao.lockExists3(member), "1"))
//		{
//			return true;
//		}
		return false;
	}

	@Override
	@Transactional(readOnly = false)
	public ResponseData dealPhoneNo(Member member, String type, String phoneNo) {
		
		if(StringUtils.equals("0", type)) {
			MemberCard card = memberCardService.getCardByMember(member);
			if(card != null) {
				HandleRsp rsp = memberCardService.checkCard4Factors(card.getCardNo(), member.getName(),  member.getIdNo(), phoneNo);
				if(!rsp.getStatus()) {
					return ResponseData.againChange("预留手机号不匹配");
				}
			}
		}else {
			memberCardService.unBindBankCard(member);
		}
		Member newMember = get(member);
		newMember.setUsername(phoneNo);
		save(newMember);
		return ResponseData.success("手机号修改成功");
	}

	@Override
	public MemberInfoGxtResponseResult getMemberInfoGxt(Member member) {
		
		MemberInfoGxtResponseResult result = new MemberInfoGxtResponseResult();
		BigDecimal curBal =  BigDecimal.ZERO;									
		MemberAct memberAct = new MemberAct();
		memberAct.setMember(member);
		List<MemberAct> memberActList = memberActService.findList(memberAct);
		for (MemberAct act : memberActList) {
            if(StringUtils.equals(act.getSubNo(), ActSubConstant.MEMBER_AVL_BAL)) {
            	BigDecimal avlBal = act.getCurBal();
            	curBal = curBal.add(avlBal);
			}else if(StringUtils.equals(act.getSubNo(), ActSubConstant.MEMBER_LOAN_BAL)) {
				curBal = curBal.add(act.getCurBal());
			}else if(StringUtils.equals(act.getSubNo(), ActSubConstant.MEMBER_PENDING_REPAYMENT)){//总待还
				BigDecimal pendingRepayment = act.getCurBal();
				result.setPendingRepayment(StringUtils.decimalToStr(pendingRepayment, 2));
			}else if(StringUtils.equals(act.getSubNo(), ActSubConstant.MEMBER_PENDING_RECEIVE)){//总待收
				BigDecimal pendingReceive = act.getCurBal();
				result.setPendingReceive(StringUtils.decimalToStr(pendingReceive, 2));
			}
		}
		result.setCurBal(StringUtils.decimalToStr(curBal, 2));
		
		result.setName(StringUtils.isNotBlank(member.getName())?StringEscapeUtils.unescapeHtml4(member.getName()): member.getUsername()  );
		
		result.setHeadImage(member.getHeadImage() == null? Global.getConfig("domain")+Global.getConfig("default.headImage"):member.getHeadImage());
		result.setUsername(member.getUsername());
		
		Integer verifiedList = member.getVerifiedList();
		result.setRealIdentityStatus(VerifiedUtils.isVerified(verifiedList)?1:0);//实名认证	
		result.setBankCardStatus(VerifiedUtils.isVerified(verifiedList, 3)?1:0);//银行卡认证					
		result.setEmailStatus(VerifiedUtils.isVerified(verifiedList, 23)?1:0);
		result.setEmail(member.getEmail());
		result.setPayPwStatus(VerifiedUtils.isVerified(verifiedList, 22)?1:0);
		result.setIdNo(member.getIdNo());
		result.setId(member.getId().toString());
		result.setRealUsername(member.getUsername());
		return MemUtils.maskResult(result);
	}






}