package com.jxf.web.h5.gxt;


import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.svc.annotation.AccessLimit;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.security.PasswordUtils;
import com.jxf.svc.security.jwt.JwtUtil;
import com.jxf.svc.utils.DateUtils;
import com.jxf.web.app.BaseController;
import com.jxf.web.model.CommonData;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.member.MemberInfoResponseResult;
import com.jxf.wx.user.entity.WxUserInfo;
import com.jxf.wx.user.service.WxUserInfoService;


/**
 * Controller - 会员注册
 *
 * @author JINXINFU
 * @version 2.0
 */
@Controller("gxtH5RegisterController")
@RequestMapping(value = "${gxtH5}/register")
public class RegisterController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private WxUserInfoService wxUserInfoService;

    @Autowired
    private SendSmsMsgService sendSmsMsgService;


    /**
     * 注册提交
     */
    @AccessLimit(maxCount = 1, seconds = 1)
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public @ResponseBody
    ResponseData submit(HttpServletRequest request, HttpServletResponse response, String phoneNo, String password, String openid) {
		
        if (StringUtils.isBlank(phoneNo) || StringUtils.isNotBlank(phoneNo) && phoneNo.length() != 11) {
            log.info("该手机号格式不正确");
            return ResponseData.error("该手机号格式不正确");
        }
        String registerIp = Global.getRemoteAddr(request);
        if(StringUtils.contains(CommonData.IP_BLACKLIST, registerIp)) {
    		int r = (int)(1+Math.random()*(10-1+1));
    		if(r%3==1){
    			 return ResponseData.error("账号已存在");
    		}else if(r%4==1) {
    			 return ResponseData.error("短信验证码错误");
    		}else {
    			return ResponseData.error("注册成功");
    		}
    		
    	}
        if (memberService.usernameExists(phoneNo)) {
            log.error("账号已存在");
            return ResponseData.error("账号已存在");
        }

        WxUserInfo userInfo = wxUserInfoService.findByOpenId(openid);
        if(userInfo == null) {
        	return ResponseData.error("用户未授权,请重新授权");
        }
        
       
        Member member = new Member();
        member.setUsername(phoneNo);
        member.setNickname(phoneNo);
        member.setPassword(PasswordUtils.entryptPassword(password));
        member.setVerifiedList(1);//手机号认证通过
        member.setLoginDate(new Date());
		member.setLoginFailureCount(0);
		member.setRegChannel(Member.RegChannel.gxt);
        member.setRegisterIp(registerIp);
        member.setLoginIp(registerIp);
        member.setHeadImage(userInfo.getHeadImage());
        member.setIsAuth(false);
        memberService.initMember(member, null);
        
        sendSmsMsgService.sendMessage("wyjtHtmlRegister", phoneNo, null);
        
		userInfo.setIsMember(true);
		userInfo.setMember(member);
		wxUserInfoService.save(userInfo);
        
        MemberInfoResponseResult result = memberService.getMemberInfo(member);
		RedisUtils.put("loginInfo" + member.getId(), "loginIp", registerIp);		
		RedisUtils.put("loginInfo" + member.getId(), "loginTime", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));

		HashMap<String, Object> payLoad = new HashMap<>();
		payLoad.put("id", member.getId());
		String memberToken = JwtUtil.generToken(payLoad, Global.getTokenTimeout());
		result.setMemberToken(memberToken);
        return ResponseData.success("注册成功", result);
    }

    /**
     * 用户注册及使用协议
     * 2018年10月15日
     *
     * @return String
     */
    @RequestMapping(value = "/getRegisterAgreement", method = RequestMethod.GET)
    public String getRegisterAgreement() {
        return "../../XXX";
    }



}