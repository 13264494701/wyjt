package com.jxf.web.home;

import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.svc.annotation.AccessLimit;
import com.jxf.svc.config.Global;
import com.jxf.svc.config.Setting;
import com.jxf.svc.model.Message;
import com.jxf.svc.security.PasswordUtils;

import com.jxf.svc.security.jwt.JwtUtil;
import com.jxf.svc.security.rsa.RSAService;
import com.jxf.svc.utils.StringUtils;
import com.jxf.svc.utils.SystemUtils;





/**
 * 
 * @类功能说明： 会员登录
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：HUOJIABAO 
 * @创建时间：2018年1月23日 下午4:07:01 
 * @版本：V1.0
 */
@Controller("homeLoginController")
@RequestMapping(value="/home/login")
public class LoginController extends BaseController {

	@Autowired
	private RSAService rsaService;
	@Autowired
	private MemberService memberService;


	/**
	 * 公钥
	 */
	@RequestMapping(value = "/public_key", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, String> publicKey(HttpServletRequest request) {
		RSAPublicKey publicKey = rsaService.generateKey(request);
		Map<String, String> data = new HashMap<String, String>();
		data.put("modulus", Base64.encodeBase64String(publicKey.getModulus().toByteArray()));
		data.put("exponent", Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray()));
		return data;
	}
	
	/**
	 * 登录页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String index(String redirectUrl, HttpServletRequest request, ModelMap model) {
		Setting setting = SystemUtils.getSetting();
		if (StringUtils.equalsIgnoreCase(redirectUrl, setting.getSiteUrl()) || StringUtils.startsWithIgnoreCase(redirectUrl, request.getContextPath() + "/") || StringUtils.startsWithIgnoreCase(redirectUrl, setting.getSiteUrl() + "/")) {
			model.addAttribute("redirectUrl", redirectUrl);
		}

		return "home/login";
	}

	/**
	 * 登录提交
	 */
    @AccessLimit(maxCount = 2, seconds = 1)
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public @ResponseBody
	Message submit(HttpServletRequest request, HttpServletResponse response,Model model) {
				
		String username = request.getParameter("username");
		String password = request.getParameter("password");;
		
		 if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
	            return Message.error("手机号或者密码为空");
	        }
	        Setting setting = SystemUtils.getSetting();
	        Member member = memberService.findByUsername(username);
	        if (member == null) {
	            return Message.error("用户名不存在");
	        }
	        if (!member.getIsEnabled()) {
	            return Message.error("该账号已被禁用");
	        }
	        if (member.getIsLocked()) {
	            if (ArrayUtils.contains(setting.getAccountLockTypes(), Setting.AccountLockType.member)) {
	                int loginFailureLockTime = setting.getAccountLockTime();
	                if (loginFailureLockTime == 0) {
	                    return Message.error("该账号已被锁定");
	                }
	                Date lockedDate = member.getLockedDate();
	                Date unlockDate = DateUtils.addMinutes(lockedDate, loginFailureLockTime);
	                if (new Date().after(unlockDate)) {
	                    member.setLoginFailureCount(0);
	                    member.setIsLocked(false);
	                    member.setLockedDate(null);
	                    memberService.save(member);
	                } else {
	                    return Message.error("该账号已被锁定");
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
	            }
	            member.setLoginFailureCount(loginFailureCount);
	            memberService.save(member);
	            if (ArrayUtils.contains(setting.getAccountLockTypes(), Setting.AccountLockType.member)) {	             
	                return Message.error("密码错误，若连续" + setting.getAccountLockCount() + "次密码错误账号将被锁定");
	            } else {
	                return Message.error("用户名或密码错误");
	            }
	        }
	        String loginIp = Global.getRemoteAddr(request);
	        member.setLoginIp(loginIp);
	        member.setLoginDate(new Date());
	        member.setLoginFailureCount(0);
	        memberService.save(member);

	        HashMap<String, Object> payLoad = new HashMap<>();
	        payLoad.put("id", member.getId());
	        String memberToken = JwtUtil.generToken(payLoad, Global.getTokenTimeout());
	 
	        return Message.success(memberToken);
	}


}