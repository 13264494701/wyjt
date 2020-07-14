package com.jxf.web.app.wyjt;


import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.constant.LoanConstant;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberLoginInfo;
import com.jxf.mem.service.MemberLoginInfoService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.DESForLogin;
import com.jxf.mem.utils.DirectLoginUtils;
import com.jxf.mem.utils.H5Utils;
import com.jxf.svc.annotation.AccessLimit;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.config.Setting;
import com.jxf.svc.security.PasswordUtils;
import com.jxf.svc.security.jwt.JwtUtil;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.StringUtils;
import com.jxf.svc.utils.SystemUtils;
import com.jxf.web.app.BaseController;
import com.jxf.web.model.CommonData;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.DirectLoginRequestParam;
import com.jxf.web.model.wyjt.app.LoginSubmitRequestParam;
import com.jxf.web.model.wyjt.app.SmsCodeSubmitRequestParam;
import com.jxf.web.model.wyjt.app.WechatLoginRequestParam;
import com.jxf.web.model.wyjt.app.WechatLoginResponseResult;
import com.jxf.web.model.wyjt.app.WechatLoginResultResponseResult;
import com.jxf.web.model.wyjt.app.member.MemberInfoResponseResult;
import com.jxf.wx.account.entity.WxAccount;
import com.jxf.wx.account.service.WxAccountService;
import com.jxf.wx.user.entity.WxUserInfo;
import com.jxf.wx.user.service.WxUserInfoService;





/**
 * @类功能说明： 会员登录
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @公司名称：
 * @作者：HUOJIABAO
 * @创建时间：2016年12月21日 下午4:07:01
 * @版本：V1.0
 */
@Controller("wyjtAppLoginController")
@RequestMapping(value = "${wyjtApp}/login")
public class LoginController extends BaseController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private WxUserInfoService wxUserInfoService;
    @Autowired
    private WxAccountService wxAccountService;
    @Autowired
    private MemberLoginInfoService memberLoginInfoService;
 
    /**
     * 密码登陆
     */
    @AccessLimit(maxCount = 10, seconds = 2)
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public @ResponseBody
    ResponseData submit(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String loginIp = Global.getRemoteAddr(request);
    	if(StringUtils.contains(CommonData.IP_BLACKLIST, loginIp)) {
    		int r = (int)(1+Math.random()*(10-1+1));
    		if(r%3==1){
    			 return ResponseData.error("用户名不存在，如果您有添加备用手机号，请尝试使用备用手机号登录。");
    		}else if(r%4==1) {
    			 return ResponseData.error("用户名不存在，如果您有添加备用手机号，请尝试使用备用手机号登录。");
    		}else {
    			return ResponseData.error("密码错误,请重新输入密码");
    		}
    		
    	}
    	
        String param = request.getParameter("param");
        LoginSubmitRequestParam reqData = JSONObject.parseObject(param, LoginSubmitRequestParam.class);

        String phoneNo = reqData.getPhoneNo();
        String password = reqData.getPassword();
        if (StringUtils.isBlank(phoneNo) || StringUtils.isBlank(password)) {
            return ResponseData.error("手机号或者密码为空");
        }
        Setting setting = SystemUtils.getSetting();
        Member member = memberService.findByUsername(phoneNo);
        
        if (member == null) {
        	//尝试原来的手机号去 查找用户ID
        	Long memberId = memberService.findByOrgUsername(phoneNo);
        	if(memberId!=null) {
        		member = memberService.get(memberId);
        	}
        	if (member == null) {
               return ResponseData.error("帐号或密码错误，请尝试用验证码登录");
        	}
        }
        
        if (!member.getIsEnabled()) {
            return ResponseData.error("该用户已被禁用!");
        }
        if (member.getIsLocked()) {
            if (ArrayUtils.contains(setting.getAccountLockTypes(), Setting.AccountLockType.member)) {
                int loginFailureLockTime = setting.getAccountLockTime();
                if (loginFailureLockTime == 0) {
                    return ResponseData.error("该账号被锁定！");
                }
                Date lockedDate = member.getLockedDate();
                Date unlockDate = DateUtils.addMinutes(lockedDate, loginFailureLockTime);
                if (new Date().after(unlockDate)) {
                    member.setLoginFailureCount(0);
                    member.setIsLocked(false);
                    member.setLockedDate(null);
                    memberService.save(member);
                } else {
                    return ResponseData.error("该账号被锁定！");
                }
            } else {
                member.setLoginFailureCount(0);
                member.setIsLocked(false);
                member.setLockedDate(null);
                memberService.save(member);
            }
        }

        if (!PasswordUtils.validatePassword(password, member.getPassword())) {
            int loginFailureCount = member.getLoginFailureCount() + 1;
            if (loginFailureCount >= setting.getAccountLockCount()) {
                member.setIsLocked(true);
                member.setLockedDate(new Date());
                member.setRmk(member.getRmk()+"连续"+loginFailureCount+"次登录失败账号被锁定");
            }
            member.setLoginFailureCount(loginFailureCount);
            memberService.save(member);
            if (ArrayUtils.contains(setting.getAccountLockTypes(), Setting.AccountLockType.member)) {
                return ResponseData.error("密码错误，请尝试用验证码登录");
            } else {
                return ResponseData.error("用户名或密码错误");
            }
        }
        if(reqData.getIsAgreeAuth() == false&&member.getIsAuth() == false)  {
        	MemberInfoResponseResult result = new MemberInfoResponseResult();
        	result.setIsAuth("0");
        	result.setIsMember("1");
        	result.setRealUsername(phoneNo);
    		return ResponseData.success("用户未授权", result);
        }
        
		member.setLoginIp(loginIp);
		member.setLoginDate(new Date());
		member.setLoginFailureCount(0);
		member.setIsAuth(true);
		memberService.save(member);
		String cacheDeviceToken = (String) RedisUtils.getHashKey("loginInfo" + member.getId(), "deviceToken");
		
		MemberInfoResponseResult result = memberService.getMemberInfo(member);
		
		if (StringUtils.isNotBlank(cacheDeviceToken) && !StringUtils.equals(cacheDeviceToken, reqData.getDeviceToken())) {
			result.setResultCode(1);
			result.setResultMessage("该账号已在其它设备登录,如非本人操作，请及时修改密码或与客服联系。");
			String lastLoginDevice = (String) RedisUtils.getHashKey("loginInfo"+ member.getId(), "deviceToken");
			String lastLoginIp = (String) RedisUtils.getHashKey("loginInfo"+ member.getId(), "loginIp");
			String lastLoginTime = (String) RedisUtils.getHashKey("loginInfo"+ member.getId(), "loginTime");
			result.setLastLoginDevice(lastLoginDevice);
			result.setLastLoginIp(lastLoginIp);
			result.setLastLoginTime(lastLoginTime);
		}
	
		String pushToken = request.getHeader("x-pushToken");
		RedisUtils.put("loginInfo" + member.getId(), "osType", reqData.getOsType());
		RedisUtils.put("loginInfo" + member.getId(), "osVersion", reqData.getOsVersion());
		RedisUtils.put("loginInfo" + member.getId(), "appVersion", reqData.getAppVersion());
		RedisUtils.put("loginInfo" + member.getId(), "ak", reqData.getAk());
		RedisUtils.put("loginInfo" + member.getId(), "deviceModel", reqData.getDeviceModel());
		RedisUtils.put("loginInfo" + member.getId(), "deviceToken", reqData.getDeviceToken());
		RedisUtils.put("loginInfo" + member.getId(), "channeId", reqData.getChanneId());
		RedisUtils.put("loginInfo" + member.getId(), "pushToken", pushToken);
		RedisUtils.put("loginInfo" + member.getId(), "loginIp", loginIp);		
		RedisUtils.put("loginInfo" + member.getId(), "loginTime", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		
		MemberLoginInfo loginInfo = new MemberLoginInfo();
		loginInfo.setMember(member);
		loginInfo.setOsType(reqData.getOsType());
		loginInfo.setOsVersion(reqData.getOsVersion());
		loginInfo.setAppVersion(reqData.getAppVersion());
		loginInfo.setAk(reqData.getAk());
		loginInfo.setDeviceModel(reqData.getDeviceModel());
		loginInfo.setDeviceToken(reqData.getDeviceToken());
		loginInfo.setChanneId(reqData.getChanneId());
		loginInfo.setPushToken(pushToken);
		loginInfo.setLoginIp(loginIp);
		memberLoginInfoService.save(loginInfo);
		

		HashMap<String, Object> payLoad = new HashMap<>();
		payLoad.put("id", member.getId());
		payLoad.put("deviceToken", reqData.getDeviceToken());
		String memberToken = JwtUtil.generToken(payLoad, Global.getTokenTimeout());
		result.setMemberToken(memberToken);
        return ResponseData.success("登录成功", result);
    }
    
    /**
     * 验证码登陆
     */
    @AccessLimit(maxCount = 10, seconds = 2)
    @RequestMapping(value = "/smsCodeSubmit", method = RequestMethod.POST)
    public @ResponseBody
    ResponseData smsCodeSubmit(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    	String loginIp = Global.getRemoteAddr(request);
    	if(StringUtils.contains(CommonData.IP_BLACKLIST, loginIp)) {
    		int r = (int)(1+Math.random()*(10-1+1));
    		if(r%3==1){
    			 return ResponseData.error("用户名不存在，如果您有添加备用手机号，请尝试使用备用手机号登录。");
    		}else if(r%4==1) {
    			 return ResponseData.error("用户名不存在，如果您有添加备用手机号，请尝试使用备用手机号登录。");
    		}else {
    			return ResponseData.error("密码错误,请重新输入密码");
    		}
    		
    	}
    	
        String param = request.getParameter("param");
        SmsCodeSubmitRequestParam reqData = JSONObject.parseObject(param, SmsCodeSubmitRequestParam.class);

        String phoneNo = reqData.getPhoneNo();
        String smsCode = reqData.getSmsCode();
        if (StringUtils.isBlank(phoneNo) || StringUtils.isBlank(smsCode)) {
            return ResponseData.error("手机号或者验证码为空");
        }
        
        String serverSmsCode = RedisUtils.get("smsCode" + phoneNo);
        if (StringUtils.isBlank(serverSmsCode) || !StringUtils.equals(serverSmsCode, smsCode)) {
        	return ResponseData.error("短信验证码错误 请重试");
        } 
        
        Setting setting = SystemUtils.getSetting();
        Member member = memberService.findByUsername(phoneNo);
        
        if (member == null) {
        	//尝试原来的手机号去 查找用户ID
        	Long memberId = memberService.findByOrgUsername(phoneNo);
        	if(memberId!=null) {
        		member = memberService.get(memberId);
        	}
        	if (member == null) {
               return ResponseData.error("帐号或密码错误，请尝试用验证码登录");
        	}
        }
        
        if (!member.getIsEnabled()) {
            return ResponseData.error("该用户已被禁用!");
        }
        if (member.getIsLocked()) {
            if (ArrayUtils.contains(setting.getAccountLockTypes(), Setting.AccountLockType.member)) {
                int loginFailureLockTime = setting.getAccountLockTime();
                if (loginFailureLockTime == 0) {
                    return ResponseData.error("该账号被锁定！");
                }
                Date lockedDate = member.getLockedDate();
                Date unlockDate = DateUtils.addMinutes(lockedDate, loginFailureLockTime);
                if (new Date().after(unlockDate)) {
                    member.setLoginFailureCount(0);
                    member.setIsLocked(false);
                    member.setLockedDate(null);
                    memberService.save(member);
                } else {
                    return ResponseData.error("该账号被锁定！");
                }
            } else {
                member.setLoginFailureCount(0);
                member.setIsLocked(false);
                member.setLockedDate(null);
                memberService.save(member);
            }
        }
        if(reqData.getIsAgreeAuth() == false&&member.getIsAuth() == false)  {
        	MemberInfoResponseResult result = new MemberInfoResponseResult();
        	result.setIsAuth("0");
    		return ResponseData.success("用户未授权", result);
        }
        
		member.setLoginIp(loginIp);
		member.setLoginDate(new Date());
		member.setLoginFailureCount(0);
		member.setIsAuth(true);
		memberService.save(member);
		String cacheDeviceToken = (String) RedisUtils.getHashKey("loginInfo" + member.getId(), "deviceToken");
		
		MemberInfoResponseResult result = memberService.getMemberInfo(member);
		
		if (StringUtils.isNotBlank(cacheDeviceToken) && !StringUtils.equals(cacheDeviceToken, reqData.getDeviceToken())) {
			result.setResultCode(1);
			result.setResultMessage("该账号已在其它设备登录,如非本人操作，请及时修改密码或与客服联系。");
			String lastLoginDevice = (String) RedisUtils.getHashKey("loginInfo"+ member.getId(), "deviceToken");
			String lastLoginIp = (String) RedisUtils.getHashKey("loginInfo"+ member.getId(), "loginIp");
			String lastLoginTime = (String) RedisUtils.getHashKey("loginInfo"+ member.getId(), "loginTime");
			result.setLastLoginDevice(lastLoginDevice);
			result.setLastLoginIp(lastLoginIp);
			result.setLastLoginTime(lastLoginTime);
		}
	
		String pushToken = request.getHeader("x-pushToken");
		RedisUtils.put("loginInfo" + member.getId(), "osType", reqData.getOsType());
		RedisUtils.put("loginInfo" + member.getId(), "osVersion", reqData.getOsVersion());
		RedisUtils.put("loginInfo" + member.getId(), "appVersion", reqData.getAppVersion());
		RedisUtils.put("loginInfo" + member.getId(), "ak", reqData.getAk());
		RedisUtils.put("loginInfo" + member.getId(), "deviceModel", reqData.getDeviceModel());
		RedisUtils.put("loginInfo" + member.getId(), "deviceToken", reqData.getDeviceToken());
		RedisUtils.put("loginInfo" + member.getId(), "channeId", reqData.getChanneId());
		RedisUtils.put("loginInfo" + member.getId(), "pushToken", pushToken);
		RedisUtils.put("loginInfo" + member.getId(), "loginIp", loginIp);		
		RedisUtils.put("loginInfo" + member.getId(), "loginTime", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		
		MemberLoginInfo loginInfo = new MemberLoginInfo();
		loginInfo.setMember(member);
		loginInfo.setOsType(reqData.getOsType());
		loginInfo.setOsVersion(reqData.getOsVersion());
		loginInfo.setAppVersion(reqData.getAppVersion());
		loginInfo.setAk(reqData.getAk());
		loginInfo.setDeviceModel(reqData.getDeviceModel());
		loginInfo.setDeviceToken(reqData.getDeviceToken());
		loginInfo.setChanneId(reqData.getChanneId());
		loginInfo.setPushToken(pushToken);
		loginInfo.setLoginIp(loginIp);
		memberLoginInfoService.save(loginInfo);
		

		HashMap<String, Object> payLoad = new HashMap<>();
		payLoad.put("id", member.getId());
		payLoad.put("deviceToken", reqData.getDeviceToken());
		String memberToken = JwtUtil.generToken(payLoad, Global.getTokenTimeout());
		result.setMemberToken(memberToken);
        return ResponseData.success("登录成功", result);
    }
    /**
     * 一键登录
     */
    @AccessLimit(maxCount = 10, seconds = 1800)
    @RequestMapping(value = "/directLogin")
    @ResponseBody
    public ResponseData directLogin(HttpServletRequest request) {
    	String loginIp = Global.getRemoteAddr(request);
    	if(StringUtils.contains(CommonData.IP_BLACKLIST, loginIp)) {
    		int r = (int)(1+Math.random()*(10-1+1));
    		if(r%3==1){
    			 return ResponseData.error("用户名不存在，如果您有添加备用手机号，请尝试使用备用手机号登录。");
    		}else if(r%4==1) {
    			 return ResponseData.error("用户名不存在，如果您有添加备用手机号，请尝试使用备用手机号登录。");
    		}else {
    			return ResponseData.error("密码错误,请重新输入密码");
    		}
    		
    	}
    	
        String param = request.getParameter("param");
        DirectLoginRequestParam reqData = JSONObject.parseObject(param, DirectLoginRequestParam.class);
        String osType = reqData.getOsType();
        String phoneNo = "";
        if(reqData.getFlag()) {
        	phoneNo = reqData.getPhoneNo();
        }else {
        	String appId = reqData.getAppId();
        	String accessToken = reqData.getAccessToken();
        	String telecom = reqData.getTelecom();
        	String timestamp = reqData.getTimestamp();
        	String randoms = reqData.getRandoms();
        	String version = reqData.getVersion();
        	String device = reqData.getDevice();
        	String sign = reqData.getSign();
        	JSONObject jsonObject = DirectLoginUtils.tokenExchangeMobileRequest(appId, accessToken, telecom, timestamp, randoms, version, device, sign);
        	if(jsonObject == null) {
        		return ResponseData.error("登陆失败,请稍后重试");
        	}
        	String code = jsonObject.getString("code");     //返回码 200000为成功
        	String message = jsonObject.getString("message");//返回消息
        	logger.debug("=====code:{}message:{}=====",code,message);
        	if(!StringUtils.equals("200000", code)) {
        		return ResponseData.error(message);
        	}
        	String dataStr = jsonObject.getString("data");
        	JSONObject dataObj = JSONObject.parseObject(dataStr);
        	String mobile = dataObj.getString("mobileName");
        	try {
        		if(StringUtils.equals("ios", osType)) {//手机号DES解密，解密秘钥为appId对应的appKey
        			phoneNo = DESForLogin.decryptDES(mobile, "p6c0l3lA");
        		}else {
        			phoneNo = DESForLogin.decryptDES(mobile, "7sWZWlq6");
        		}
        		logger.debug("=====phoneNo===="+phoneNo);
        	} catch (Exception e) {
        		logger.error(Exceptions.getStackTraceAsString(e));
        	} 
        }
        
        Setting setting = SystemUtils.getSetting();
        Member member = memberService.findByUsername(phoneNo);
        
        if (member == null) {
        	//尝试原来的手机号去 查找用户ID
        	Long memberId = memberService.findByOrgUsername(phoneNo);
        	if(memberId!=null) {
        		member = memberService.get(memberId);
        	}
        	if (member == null) {
        		MemberInfoResponseResult result = new MemberInfoResponseResult();
        		result.setIsMember("0");
        		result.setRealUsername(phoneNo);
        		return ResponseData.success("登录失败", result);
        	}
        }
        
        if (!member.getIsEnabled()) {
            return ResponseData.error("该用户已被禁用!");
        }
        if (member.getIsLocked()) {
            if (ArrayUtils.contains(setting.getAccountLockTypes(), Setting.AccountLockType.member)) {
                int loginFailureLockTime = setting.getAccountLockTime();
                if (loginFailureLockTime == 0) {
                    return ResponseData.error("该账号被锁定！");
                }
                Date lockedDate = member.getLockedDate();
                Date unlockDate = DateUtils.addMinutes(lockedDate, loginFailureLockTime);
                if (new Date().after(unlockDate)) {
                    member.setLoginFailureCount(0);
                    member.setIsLocked(false);
                    member.setLockedDate(null);
                    memberService.save(member);
                } else {
                    return ResponseData.error("该账号被锁定！");
                }
            } else {
                member.setLoginFailureCount(0);
                member.setIsLocked(false);
                member.setLockedDate(null);
                memberService.save(member);
            }
        }

        if(reqData.getIsAgreeAuth() == false&&member.getIsAuth() == false)  {
        	MemberInfoResponseResult result = new MemberInfoResponseResult();
        	result.setIsAuth("0");
        	result.setIsMember("1");
        	result.setRealUsername(phoneNo);
    		return ResponseData.success("用户未授权", result);
        }
        
		member.setLoginIp(loginIp);
		member.setLoginDate(new Date());
		member.setLoginFailureCount(0);
		member.setIsAuth(true);
		memberService.save(member);
		String cacheDeviceToken = (String) RedisUtils.getHashKey("loginInfo" + member.getId(), "deviceToken");
		
		MemberInfoResponseResult result = memberService.getMemberInfo(member);
		if (StringUtils.isNotBlank(cacheDeviceToken) && !StringUtils.equals(cacheDeviceToken, reqData.getDeviceToken())) {
			result.setResultCode(1);
			result.setResultMessage("该账号已在其它设备登录,如非本人操作，请及时修改密码或与客服联系。");
			String lastLoginDevice = (String) RedisUtils.getHashKey("loginInfo"+ member.getId(), "deviceToken");
			String lastLoginIp = (String) RedisUtils.getHashKey("loginInfo"+ member.getId(), "loginIp");
			String lastLoginTime = (String) RedisUtils.getHashKey("loginInfo"+ member.getId(), "loginTime");
			result.setLastLoginDevice(lastLoginDevice);
			result.setLastLoginIp(lastLoginIp);
			result.setLastLoginTime(lastLoginTime);
		}
	
		String pushToken = request.getHeader("x-pushToken");
		RedisUtils.put("loginInfo" + member.getId(), "osType", osType);
		RedisUtils.put("loginInfo" + member.getId(), "osVersion", reqData.getOsVersion());
		RedisUtils.put("loginInfo" + member.getId(), "appVersion", reqData.getAppVersion());
		RedisUtils.put("loginInfo" + member.getId(), "ak", reqData.getAk());
		RedisUtils.put("loginInfo" + member.getId(), "deviceModel", reqData.getDeviceModel());
		RedisUtils.put("loginInfo" + member.getId(), "deviceToken", reqData.getDeviceToken());
		RedisUtils.put("loginInfo" + member.getId(), "channeId", reqData.getChanneId());
		RedisUtils.put("loginInfo" + member.getId(), "pushToken", pushToken);
		RedisUtils.put("loginInfo" + member.getId(), "loginIp", loginIp);		
		RedisUtils.put("loginInfo" + member.getId(), "loginTime", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		
		MemberLoginInfo loginInfo = new MemberLoginInfo();
		loginInfo.setMember(member);
		loginInfo.setOsType(reqData.getOsType());
		loginInfo.setOsVersion(reqData.getOsVersion());
		loginInfo.setAppVersion(reqData.getAppVersion());
		loginInfo.setAk(reqData.getAk());
		loginInfo.setDeviceModel(reqData.getDeviceModel());
		loginInfo.setDeviceToken(reqData.getDeviceToken());
		loginInfo.setChanneId(reqData.getChanneId());
		loginInfo.setPushToken(pushToken);
		loginInfo.setLoginIp(loginIp);
		memberLoginInfoService.save(loginInfo);

		HashMap<String, Object> payLoad = new HashMap<>();
		payLoad.put("id", member.getId());
		payLoad.put("deviceToken", reqData.getDeviceToken());
		String memberToken = JwtUtil.generToken(payLoad, Global.getTokenTimeout());
		result.setMemberToken(memberToken);
		result.setIsMember("1");
        return ResponseData.success("登录成功", result);
    }

    /**
     * 微信登录
     */
    @AccessLimit(maxCount = 10, seconds = 1800)
    @RequestMapping(value = "/wechat")
    @ResponseBody
    public ResponseData wechat(HttpServletRequest request) {
		String param = request.getParameter("param");
        WechatLoginRequestParam reqData = JSONObject.parseObject(param, WechatLoginRequestParam.class);

        String openid = reqData.getOpenid();
        String unionid = reqData.getUnionid();
        String nickname = reqData.getNickname();
        String headimgurl = reqData.getHeadimgurl();
		
        //根据openid查微信信息记录
        WxUserInfo wxUserInfo = wxUserInfoService.findByOpenId(openid);
        //如果空
        if (wxUserInfo == null) {
            //保存微信信息
            wxUserInfo = new WxUserInfo();
            //设置appid
            WxAccount wxAccount = wxAccountService.findByCode("wyjtapp");
            wxUserInfo.setAccount(wxAccount);
            wxUserInfo.setOpenid(openid);
            wxUserInfo.setUnionid(unionid);
            wxUserInfo.setNickname(nickname.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", ""));
            wxUserInfo.setHeadImage(headimgurl);
            wxUserInfo.setIsMember(false);
            wxUserInfoService.save(wxUserInfo);
        }
        
		String pushToken = request.getHeader("x-pushToken");
		String loginIp = Global.getRemoteAddr(request);
		RedisUtils.put("loginInfo" + wxUserInfo.getId(), "osType", reqData.getOsType());
		RedisUtils.put("loginInfo" + wxUserInfo.getId(), "osVersion", reqData.getOsVersion());
		RedisUtils.put("loginInfo" + wxUserInfo.getId(), "appVersion", reqData.getAppVersion());
		RedisUtils.put("loginInfo" + wxUserInfo.getId(), "ak", reqData.getAk());
		RedisUtils.put("loginInfo" + wxUserInfo.getId(), "deviceModel", reqData.getDeviceModel());
		RedisUtils.put("loginInfo" + wxUserInfo.getId(), "deviceToken", reqData.getDeviceToken());
		RedisUtils.put("loginInfo" + wxUserInfo.getId(), "channeId", reqData.getChanneId());
		RedisUtils.put("loginInfo" + wxUserInfo.getId(), "pushToken", pushToken);
		RedisUtils.put("loginInfo" + wxUserInfo.getId(), "loginIp", loginIp);		
		RedisUtils.put("loginInfo" + wxUserInfo.getId(), "loginTime", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		
        WechatLoginResponseResult result =new WechatLoginResponseResult();
        WechatLoginResultResponseResult wechatLoginResultResponseResult = new WechatLoginResultResponseResult();
        //微信信息是否绑定了用户
        if (wxUserInfo.getIsMember()) {
            wechatLoginResultResponseResult.setLoginResult("success");
            result.setWechatLoginResultResponseResult(wechatLoginResultResponseResult);

            Member member = memberService.get(wxUserInfo.getMember().getId());
            if (member == null) {
                member = new Member();
            }
            if(reqData.getIsAgreeAuth() == false&&member.getIsAuth() == false)  {
            	MemberInfoResponseResult memberInfoResult = new MemberInfoResponseResult();
            	memberInfoResult.setIsAuth("0");
            	result.setMemberInfoResponseResult(memberInfoResult);
        		return ResponseData.success("用户未授权", result);
            }
            
            result.setMemberInfoResponseResult(memberService.appLogin(member,wxUserInfo.getId(),request,reqData));

        } else {
            wechatLoginResultResponseResult.setLoginResult("fail");
            wechatLoginResultResponseResult.setWxUserInfoId(wxUserInfo.getId().toString());
            result.setWechatLoginResultResponseResult(wechatLoginResultResponseResult);
        }

        return ResponseData.success("success", result);
    	
        
    }

    @RequestMapping(value="/registerProtocol")
    @ResponseBody
	public ModelAndView registerProtocol(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("app/registerProtocol");
		mv = H5Utils.addPlatform(request, mv);
		return mv;
	}

}