package com.jxf.web.app.wyjt;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.constant.LoanConstant;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.mms.msg.utils.VerifyCodeUtils;
import com.jxf.svc.annotation.AccessLimit;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.config.Setting;
import com.jxf.svc.security.PasswordUtils;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.SystemUtils;
import com.jxf.web.app.BaseController;
import com.jxf.web.model.CommonData;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.RegisterSubmitRequestParam;
import com.jxf.web.model.wyjt.app.WechatBindRequestParam;
import com.jxf.web.model.wyjt.app.WechatLoginResponseResult;
import com.jxf.web.model.wyjt.app.WechatLoginResultResponseResult;
import com.jxf.wx.user.entity.WxUserInfo;
import com.jxf.wx.user.service.WxUserInfoService;


/**
 * Controller - 会员注册
 *
 * @author JINXINFU
 * @version 2.0
 */
@Controller("wyjtAppRegisterController")
@RequestMapping(value = "${wyjtApp}/register")
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
    ResponseData submit(HttpServletRequest request, HttpServletResponse response) {
		String registerIp = Global.getRemoteAddr(request);
        String param = request.getParameter("param");
        RegisterSubmitRequestParam reqData = JSONObject.parseObject(param, RegisterSubmitRequestParam.class);

        String phoneNo = reqData.getPhoneNo();
        if (StringUtils.isBlank(phoneNo) || StringUtils.isNotBlank(phoneNo) && phoneNo.length() != 11) {
            log.info("该手机号格式不正确");
            return ResponseData.error("该手机号格式不正确");
        }

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

        String password = reqData.getPassword();
        Member member = new Member();
        member.setUsername(phoneNo);
        member.setNickname(phoneNo);
        member.setPassword(PasswordUtils.entryptPassword(password));
        member.setVerifiedList(1);//手机号认证通过
        member.setRegChannel(Member.RegChannel.app);
        member.setRegisterIp(registerIp);
        member.setLoginIp(registerIp);
        member.setIsAuth(true);
        memberService.initMember(member, null);
        
        sendSmsMsgService.sendNetLoanSms("wyjtHtmlRegister", phoneNo, null);
        
        if(StringUtils.isNoneBlank(reqData.getWxUserInfoId())) {
        	Long wxUserInfoId = Long.valueOf(reqData.getWxUserInfoId());
        	//绑定
            WxUserInfo wxUserInfo = wxUserInfoService.get(wxUserInfoId);
            wxUserInfo.setIsMember(true);
            wxUserInfo.setMember(member);
            wxUserInfoService.save(wxUserInfo);
        }
        return ResponseData.success("注册成功");
    }
    /**
     * 新版本用
     * @param request
     * @return
     */
    @RequestMapping(value = "/bind")
    @ResponseBody
    public ResponseData bindCheck(HttpServletRequest request) {
    	String param = request.getParameter("param");
        WechatBindRequestParam reqData = JSONObject.parseObject(param, WechatBindRequestParam.class);
        Long wxUserInfoId = Long.valueOf(reqData.getWxUserInfoId());
        String phoneNo = reqData.getPhoneNo();
        
        String smsVerifyCode = RedisUtils.get("smsCode" + phoneNo);
        if (StringUtils.isBlank(smsVerifyCode) || !StringUtils.equals(smsVerifyCode, reqData.getSmsCode())) {
           return ResponseData.error("短信验证码错误");
        }
        
        Member member = memberService.findByUsername(phoneNo);
        if(member == null) {
        	return ResponseData.error("绑定失败");
        }
        //绑定
        WxUserInfo wxUserInfo = wxUserInfoService.get(wxUserInfoId);
        wxUserInfo.setIsMember(true);
        wxUserInfo.setMember(member);
        wxUserInfoService.save(wxUserInfo);

        return ResponseData.success("绑定成功");
    }

   

}