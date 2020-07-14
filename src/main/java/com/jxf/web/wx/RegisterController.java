package com.jxf.web.wx;


import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.security.PasswordUtils;
import com.jxf.web.app.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;


/**
 * Controller - 会员注册
 *
 * @author JINXINFU
 * @version 2.0
 */
@Controller("wyjtWxRegisterController")
@RequestMapping(value = "${wyjtWx}/register")
public class RegisterController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private MemberService memberService;

    /**
     * 注册页面
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("uuid", UUID.randomUUID());
        return "wx/wx-reg";
    }

    /**
     * 注册提交
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public String submit(HttpServletRequest request, String phoneNo, String dxyzm, String password, Model model) {

        if (StringUtils.isBlank(phoneNo) || StringUtils.isNotBlank(phoneNo) && phoneNo.length() != 11) {
            model.addAttribute("msg", "该手机号格式不正确");
            return "wx/wx-reg";
        }

        String smsVerifyCode = RedisUtils.get("smsCode" + phoneNo);
        if (StringUtils.isBlank(smsVerifyCode) || !StringUtils.equals(smsVerifyCode, dxyzm)) {
            model.addAttribute("msg", "短信验证码错误");
            return "wx/wx-reg";
        }

        if (memberService.usernameExists(phoneNo)) {
            model.addAttribute("msg", "注册失败[001],请联系客服");
            return "wx/wx-reg";
        }

        Member member = new Member();
        member.setUsername(phoneNo);
        member.setNickname(phoneNo);
        member.setPassword(PasswordUtils.entryptPassword(password));
        member.setVerifiedList(1);//手机号认证通过
        String loginIp = Global.getRemoteAddr(request);
        member.setRegisterIp(loginIp);
        member.setLoginIp(loginIp);
        memberService.initMember(member, null);
        model.addAttribute("msg", "注册成功");
        return "wx/wx-login";
    }

}