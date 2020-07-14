package com.jxf.web.wx;


import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.security.PasswordUtils;
import com.jxf.web.app.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

/**
 * @类功能说明： 密码
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @公司名称：
 * @作者：wo
 * @创建时间：2016年12月21日 下午4:07:01
 * @版本：V1.0
 */
@Controller("wyjtWxPasswdController")
@RequestMapping(value = "${wyjtWx}/password")
public class PasswdController extends BaseController {


    @Autowired
    private MemberService memberService;


    /**
     * 找回登录密码页面
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("uuid", UUID.randomUUID());
        return "wx/wx-forget";
    }

    /**
     * 找回登录密码-1
     *
     * @return
     */
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public String check(Model model, String phoneNo, String dxyzm) {

        String code = RedisUtils.get("smsCode" + phoneNo);
        if (!StringUtils.equals(dxyzm, code)) {
            model.addAttribute("msg", "验证码错误,请重试!");
            return "wx/wx-forget";
        }
        model.addAttribute("phoneNo", phoneNo);
        model.addAttribute("dxyzm", dxyzm);
        return "wx/wx-forget2";

    }

    /**
     * 找回登录密码-2
     *
     * @return
     */
    @RequestMapping(value = "/change", method = RequestMethod.POST)
    public String change(Model model, String password, String phoneNo, String dxyzm) {

        String code = RedisUtils.get("smsCode" + phoneNo);
        if (!StringUtils.equals(dxyzm, code)) {
            model.addAttribute("msg", "验证码错误,请重试!");
            return "wx/wx-forget";
        }
        Member member = memberService.findByUsername(phoneNo);
        member.setPassword(PasswordUtils.entryptPassword(password));
        memberService.save(member);
        model.addAttribute("msg", "找回登录密码成功");
        return "wx/wx-login";
    }


}