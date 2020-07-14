package com.jxf.web.h5.gxt;


import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.svc.annotation.AccessLimit;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.config.Setting;
import com.jxf.svc.security.PasswordUtils;
import com.jxf.svc.security.jwt.JwtUtil;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.svc.utils.SystemUtils;
import com.jxf.web.app.BaseController;
import com.jxf.web.model.CommonData;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.member.MemberInfoResponseResult;
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
@Controller("gxtH5LoginController")
@RequestMapping(value = "${gxtH5}/login")
public class LoginController extends BaseController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private WxUserInfoService wxUserInfoService;
    
 
    /**
     * 	密码登录
     */
    @AccessLimit(maxCount = 10, seconds = 7200)
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public @ResponseBody
    ResponseData submit(HttpServletRequest request, String phoneNo, String password, String openid) {
    	
        if (StringUtils.isBlank(phoneNo) || StringUtils.isBlank(password)) {
            return ResponseData.error("手机号或者密码为空");
        }
        
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
    	
        Setting setting = SystemUtils.getSetting();
        Member member = memberService.findByUsername(phoneNo);
        
        if (member == null) {
        	//尝试原来的手机号去 查找用户ID
        	Long memberId = memberService.findByOrgUsername(phoneNo);
        	if(memberId!=null) {
        		member = memberService.get(memberId);
        	}
        	if (member == null) {
               return ResponseData.error("用户名不存在，如果您有添加备用手机号，请尝试使用备用手机号登录。");
        	}
        }
        
        WxUserInfo userInfo = wxUserInfoService.findByOpenId(openid);
        if(userInfo == null) {
        	return ResponseData.error("用户未授权,请重新授权");
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
                return ResponseData.error("密码错误，若连续" + setting.getAccountLockCount() + "次密码错误账号将被锁定");
            } else {
                return ResponseData.error("用户名或密码错误");
            }
        }
		member.setLoginIp(loginIp);
		member.setLoginDate(new Date());
		member.setLoginFailureCount(0);
		member.setHeadImage(userInfo.getHeadImage());
		memberService.save(member);
		
		userInfo.setIsMember(true);
		userInfo.setMember(member);
		wxUserInfoService.save(userInfo);
		
		MemberInfoResponseResult result = memberService.getMemberInfo(member);
		RedisUtils.put("loginInfo" + member.getId(), "loginIp", loginIp);		
		RedisUtils.put("loginInfo" + member.getId(), "loginTime", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));

		HashMap<String, Object> payLoad = new HashMap<>();
		payLoad.put("id", member.getId());
		String memberToken = JwtUtil.generToken(payLoad, Global.getTokenTimeout());
		result.setMemberToken(memberToken);
        return ResponseData.success("登录成功", result);
    }

    /**
     * 	验证码登录
     */
    @AccessLimit(maxCount = 10, seconds = 7200)
    @RequestMapping(value = "/smsCodeSubmit", method = RequestMethod.POST)
    public @ResponseBody
    ResponseData smsCodeSubmit(HttpServletRequest request, String phoneNo, String smsCode, String openid) {
    	
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
               return ResponseData.error("用户名不存在，如果您有添加备用手机号，请尝试使用备用手机号登录。");
        	}
        }
        
        WxUserInfo userInfo = wxUserInfoService.findByOpenId(openid);
        if(userInfo == null) {
        	return ResponseData.error("用户未授权,请重新授权");
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
    	
        String loginIp = Global.getRemoteAddr(request);
		member.setLoginIp(loginIp);
		member.setLoginDate(new Date());
		member.setLoginFailureCount(0);
		member.setHeadImage(userInfo.getHeadImage());
		memberService.save(member);
		
		userInfo.setIsMember(true);
		userInfo.setMember(member);
		wxUserInfoService.save(userInfo);
		
		MemberInfoResponseResult result = memberService.getMemberInfo(member);
		RedisUtils.put("loginInfo" + member.getId(), "loginIp", loginIp);		
		RedisUtils.put("loginInfo" + member.getId(), "loginTime", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));

		HashMap<String, Object> payLoad = new HashMap<>();
		payLoad.put("id", member.getId());
		String memberToken = JwtUtil.generToken(payLoad, Global.getTokenTimeout());
		result.setMemberToken(memberToken);
        return ResponseData.success("登录成功", result);
    }
   
    /**
     * 	退出登录
     */
    @AccessLimit(maxCount = 1, seconds = 1)
    @RequestMapping(value = "/loginOut", method = RequestMethod.POST)
    public @ResponseBody
    ResponseData loginOut(HttpServletRequest request) {
    	String token = request.getHeader("token");
    	return ResponseData.success("成功退出");
    }
    
    
    
    
    
    
    
    
    
    
    
    
    

}