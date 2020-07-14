package com.jxf.web.wx;


import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.svc.annotation.AccessLimit;
import com.jxf.svc.config.Global;
import com.jxf.svc.config.Setting;
import com.jxf.svc.security.PasswordUtils;
import com.jxf.svc.security.jwt.JwtUtil;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.SystemUtils;
import com.jxf.web.app.BaseController;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;


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
@Controller("wyjtWxLoginController")
@RequestMapping(value = "${wyjtWx}/login")
public class LoginController extends BaseController {

    @Autowired
    private MemberService memberService;

    /**
     * 注册页面
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(HttpSession session) {
    	
    	return "ufangDebt/index";
    	
//        Member member = (Member) session.getAttribute("wxCurrentMember");
//        if (member != null) {
//            return "/wx/otherLoan";
//        }
//        return "wx/wx-login";
    }

    /**
     * 登录提交
     */
    @AccessLimit(maxCount = 60, seconds = 1)
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public String submit(HttpServletRequest request, String phoneNo, String password, Model model) {

        String agent = request.getHeader("user-agent").toUpperCase();
        //如果是微信扫的
        if (!agent.contains("MICROMESSENGER")) {
            model.addAttribute("msg", "请在微信公众号中打开");
            return "/wx/wx-login";
        }

        System.out.println(phoneNo);
        System.out.println(password);

        if (com.jxf.svc.utils.StringUtils.isEmpty(phoneNo) || com.jxf.svc.utils.StringUtils.isEmpty(password)) {
            model.addAttribute("phoneNo", phoneNo);
            model.addAttribute("msg", "手机号或者密码为空");
            return "/wx/wx-login";
        }
        Setting setting = SystemUtils.getSetting();
        Member member = memberService.findByUsername(phoneNo);
        if (member == null) {
            model.addAttribute("phoneNo", phoneNo);
            model.addAttribute("msg", "登录失败[002],请联系客服");
            return "/wx/wx-login";
        }
        if (!member.getIsEnabled()) {
            model.addAttribute("phoneNo", phoneNo);
            model.addAttribute("msg", "该账号已被禁用");
            return "/wx/wx-login";
        }
        if (member.getIsLocked()) {
            if (ArrayUtils.contains(setting.getAccountLockTypes(), Setting.AccountLockType.member)) {
                int loginFailureLockTime = setting.getAccountLockTime();
                if (loginFailureLockTime == 0) {
                    model.addAttribute("phoneNo", phoneNo);
                    model.addAttribute("msg", "该账号已被锁定");
                    return "/wx/wx-login";
                }
                Date lockedDate = member.getLockedDate();
                Date unlockDate = DateUtils.addMinutes(lockedDate, loginFailureLockTime);
                if (new Date().after(unlockDate)) {
                    member.setLoginFailureCount(0);
                    member.setIsLocked(false);
                    member.setLockedDate(null);
                    memberService.save(member);
                } else {
                    model.addAttribute("phoneNo", phoneNo);
                    model.addAttribute("msg", "该账号已被锁定");
                    return "/wx/wx-login";
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
                model.addAttribute("phoneNo", phoneNo);
                model.addAttribute("msg", "密码错误，若连续" + setting.getAccountLockCount() + "次密码错误账号将被锁定");
                return "/wx/wx-login";
            } else {
                model.addAttribute("phoneNo", phoneNo);
                model.addAttribute("msg", "用户名或密码错误");
                return "/wx/wx-login";
            }
        }
        String loginIp = Global.getRemoteAddr(request);
        member.setLoginIp(loginIp);
        member.setLoginDate(new Date());
        member.setLoginFailureCount(0);
        memberService.save(member);

        HashMap<String, Object> payLoad = new HashMap<>();
        payLoad.put("id", member.getId());
        payLoad.put("deviceToken", "");
        String memberToken = JwtUtil.generToken(payLoad, Global.getTokenTimeout());
        model.addAttribute("memberToken", memberToken);

        return "/wx/otherLoan";
    }

}