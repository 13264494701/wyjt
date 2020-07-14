package com.jxf.web.admin.mem;


import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberPoint;
import com.jxf.mem.entity.MemberVideoVerify;
import com.jxf.mem.service.MemberPointService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.service.MemberVideoVerifyService;
import com.jxf.mem.utils.MemUtils;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.mms.msg.utils.VerifyCodeUtils;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.model.AjaxRsp;
import com.jxf.svc.model.HandleRsp;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.security.MD5Utils;
import com.jxf.svc.security.PasswordUtils;

import com.jxf.svc.utils.StringUtils;
import com.jxf.web.admin.sys.BaseController;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;
import java.util.List;

/**
 * 
 * @类功能说明： 会员管理
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：HUOJIABAO 
 * @创建时间：2016年4月25日 下午4:36:38 
 * @版本：V1.0
 */
@Controller("adminMemberController")
@RequestMapping(value = "${adminPath}/member")
public class MemberController extends BaseController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberPointService memberPointService;
	@Autowired
	private MemberVideoVerifyService memberVideoVerifyService;
	@Autowired
    private SendSmsMsgService sendSmsMsgService;

	
	@ModelAttribute
	public Member get(@RequestParam(required=false) Long id) {
		Member entity = null;
		if (id!=null){
			entity = memberService.get(id);
		}
		if (entity == null){
			entity = new Member();
		}
		return entity;
	}
	
	@RequiresPermissions("mem:member:view")
	@RequestMapping(value = {"list", ""})
	public String list(Member member, HttpServletRequest request, HttpServletResponse response, Model model) {

		Page<Member> page = memberService.findPage(new Page<Member>(request, response), member);
		List<Member> memberList = page.getList();
		for (int i = 0; i < memberList.size(); i++) {
			MemUtils.mask(memberList.get(i));
		}
		page.setList(memberList);
		model.addAttribute("page", page);

		return "admin/mem/memberList";
	}
	
	
	@RequestMapping(value = {"sendSmsVerifyCode"})
	@ResponseBody
	public AjaxRsp sendSmsVerifyCode(String username, HttpServletRequest request, HttpServletResponse response) {
		
		//生成短信验证码
        String smsCode = VerifyCodeUtils.genSmsValidCode();
        //将短信验证码存入缓存中
		RedisUtils.setForTimeMIN("smsCode" + username, smsCode, 10);
        sendSmsMsgService.sendSms("wyjtAppFindPasswd", username, smsCode);
        AjaxRsp ajaxRsp = new AjaxRsp();
        ajaxRsp.setCode("0");
        ajaxRsp.setMessage("短信验证码发送成功");
		
		return ajaxRsp;
		
	}
	
	/***
	 * 查备用手机号
	 * @param member
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"getSpareMobile"})
	@ResponseBody
	public String getSpareMobile(String username, HttpServletRequest request, HttpServletResponse response) {
		String spareMobile = memberService.getSpareMobile(username);
		if(StringUtils.isNotBlank(spareMobile)){
			return spareMobile;
		}else{
			return "无备用手机号";
		}
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("mem:member:view")
	@RequestMapping(value = "add")
	public String add(Member member, Model model) {
		member.setGender(Member.Gender.male);
		member.setIsEnabled(true);
		member.setIsLocked(false);
		model.addAttribute("member", member);
		return "admin/mem/memberAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("mem:member:view")
	@RequestMapping(value = "query")
	public String query(Member member, Model model) {
		model.addAttribute("member", member);

		Integer verifiedList = member.getVerifiedList();
		model.addAttribute("realIdentityStatus", VerifiedUtils.isVerified(verifiedList, 1) && VerifiedUtils.isVerified(verifiedList, 2) ? 1 : 0);//实名认证
		model.addAttribute("bankCardStatus", VerifiedUtils.isVerified(verifiedList, 3) ? 1 : 0);//银行卡认证
		model.addAttribute("zhimafenStatus", VerifiedUtils.isVerified(verifiedList, 4) ? 1 : 0);//芝麻信用
		model.addAttribute("taobaoStatus", VerifiedUtils.isVerified(verifiedList, 5) ? 1 : 0);//淘宝授权
		model.addAttribute("yunyingshangStatus", VerifiedUtils.isVerified(verifiedList, 6) ? 1 : 0);//运营商授权
		model.addAttribute("bankTrxStatus", VerifiedUtils.isVerified(verifiedList, 7) ? 1 : 0);//银行卡账单
		model.addAttribute("xuexingwangStatus", VerifiedUtils.isVerified(verifiedList, 8) ? 1 : 0);//学信网
		model.addAttribute("shebaoStatus", VerifiedUtils.isVerified(verifiedList, 9) ? 1 : 0);//社保
		model.addAttribute("gongjijingStatus", VerifiedUtils.isVerified(verifiedList, 10) ? 1 : 0);//公积金
		model.addAttribute("payPwStatus", VerifiedUtils.isVerified(verifiedList, 22) ? 1 : 0);//支付密码已设置
		model.addAttribute("emailStatus", VerifiedUtils.isVerified(verifiedList, 23) ? 1 : 0);
		model.addAttribute("emergencyStatus", VerifiedUtils.isVerified(verifiedList, 24) ? 1 : 0);//绑定了紧急联系人
		model.addAttribute("tooManyFriends", VerifiedUtils.isVerified(verifiedList, 25) ? 1 : 0);

		return "admin/mem/memberQuery";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("mem:member:view")
	@RequestMapping(value = "queryLoginInfo")
	public String queryLoginInfo(Long memberId, Model model) {
	    logger.info("用户ID{}",memberId);
		String osType = (String) RedisUtils.getHashKey("loginInfo" + memberId, "osType");
		String osVersion = (String) RedisUtils.getHashKey("loginInfo" + memberId, "osVersion");
		String appVersion = (String) RedisUtils.getHashKey("loginInfo" + memberId, "appVersion");
		String ak = (String) RedisUtils.getHashKey("loginInfo" + memberId, "ak");
		String deviceModel = (String) RedisUtils.getHashKey("loginInfo" + memberId, "deviceModel");
		String deviceToken = (String) RedisUtils.getHashKey("loginInfo" + memberId, "deviceToken");
		String channeId = (String) RedisUtils.getHashKey("loginInfo" + memberId, "channeId");
		String pushToken = (String) RedisUtils.getHashKey("loginInfo" + memberId, "pushToken");
		String loginIp = (String) RedisUtils.getHashKey("loginInfo" + memberId, "loginIp");		
		String loginTime = (String) RedisUtils.getHashKey("loginInfo" + memberId, "loginTime");
		
		logger.info("系统类型{}",osType);
		logger.info("系统版本{}",osVersion);
		
		model.addAttribute("osType", osType);//系统类型
		model.addAttribute("osVersion", osVersion);//系统版本
		model.addAttribute("appVersion", appVersion);//应用版本
		model.addAttribute("ak", ak);//ak
		model.addAttribute("deviceModel", deviceModel);//设备型号
		model.addAttribute("deviceToken", deviceToken);//设备号
		model.addAttribute("channeId", channeId);//渠道号
		model.addAttribute("pushToken", pushToken);//推送码
		model.addAttribute("loginIp", loginIp);//登录IP
		model.addAttribute("loginTime", loginTime);//登录时间

		return "admin/mem/memberQueryLoginInfo";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("mem:member:view")
	@RequestMapping(value = "update")
	public String update(Member member, Model model) {
		model.addAttribute("member", member);
		return "admin/mem/memberUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("mem:member:edit")
	@RequestMapping(value = "save")
	public String save(Member member,String newImage, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, member)){
			return add(member, model);
		}

		if(!member.getIsEnabled()||member.getIsLocked()) {
			member.setLockedDate(new Date());
			RedisUtils.add("lockedlist", String.valueOf(member.getId()));
		}else {
			RedisUtils.remove("lockedlist", String.valueOf(member.getId()));
		}

		memberService.save(member);
		addMessage(redirectAttributes, "保存会员管理成功");
		return "redirect:"+Global.getAdminPath()+"/member/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("mem:member:edit")
	@RequestMapping(value = "delete")
	public String delete(Member member, RedirectAttributes redirectAttributes) {
		memberService.delete(member);
		addMessage(redirectAttributes, "删除会员管理成功");
		return "redirect:"+Global.getAdminPath()+"/member/?repage";
	}


	/**
	 * 
	 * 函数功能说明     会员积分
	 * HUOJIABAO  2016年6月14日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param Member
	 * @参数： @param request
	 * @参数： @param response
	 * @参数： @param model
	 * @参数： @return     
	 * @return String    
	 * @throws
	 */
	@RequiresPermissions("mem:member:view")
	@RequestMapping(value = "point")
	public String point(Member member, HttpServletRequest request, HttpServletResponse response, Model model) {

		MemberPoint MemberPoint = new MemberPoint();
		MemberPoint.setMember(member);
		Page<MemberPoint> page = memberPointService.findPage(new Page<MemberPoint>(request, response), MemberPoint); 
		model.addAttribute("member", member);
		model.addAttribute("page", page);
		return "admin/mem/memberPoint";
	}	
	
	/**
	 * 	消除认证
	 */
	@RequiresPermissions("mem:member:view")
	@RequestMapping(value = "deleteVerify")
	public String deleteVerify(Member member, Model model) {
		
		Integer verifiedList = member.getVerifiedList();		
		verifiedList = VerifiedUtils.removeVerified(verifiedList, 1);		
		verifiedList = VerifiedUtils.removeVerified(verifiedList, 2);
		member.setVerifiedList(verifiedList);
		memberService.save(member);
		
		MemberVideoVerify memberVideoVerify = memberVideoVerifyService.getMemberVideoVerifyByMemberId(member.getId());
		memberVideoVerify.setStatus(MemberVideoVerify.Status.reset);
		memberVideoVerifyService.update(memberVideoVerify);
		return "admin/mem/memberQuery";
	}
	
	/**
	 * 	获取用户
	 */
	@RequiresPermissions("mem:member:view")
	@RequestMapping(value = "findMemberByName")
	public String findMemberByName(Member member,HttpServletRequest request, HttpServletResponse response,String name, Model model) {
		member.setName(name);
		Page<Member> page = memberService.findPage(new Page<Member>(request, response), member);
		List<Member> memberList = page.getList();
		for (int i = 0; i < memberList.size(); i++) {
			MemUtils.mask(memberList.get(i));
		}
		page.setList(memberList);
		model.addAttribute("page", page);

		return "admin/mem/memberList";
	}
	
	/**
	 * 	获取用户通过id
	 */
	@RequiresPermissions("mem:member:view")
	@RequestMapping(value = "findMemberById")
	public String findMemberById(Member member,HttpServletRequest request, HttpServletResponse response,Long id, Model model) {
		member.setId(id);
		Page<Member> page = memberService.findPage(new Page<Member>(request, response), member);
		List<Member> memberList = page.getList();
		for (int i = 0; i < memberList.size(); i++) {
			MemUtils.mask(memberList.get(i));
		}
		page.setList(memberList);
		model.addAttribute("page", page);

		return "admin/mem/memberList";
	}
	
	@RequiresPermissions("mem:member:edit")
	@ResponseBody
	@RequestMapping("setMemberPassword")
	public AjaxRsp setMemberPassword(Long memberId, String password) {
		AjaxRsp rsp  = new AjaxRsp();
		Member member = memberService.get(memberId);
    	String enPassword1 = MD5Utils.EncoderByMd5("cpbao.com_友信宝" + MD5Utils.EncoderByMd5(password).toLowerCase()).toLowerCase();
		member.setPassword(PasswordUtils.entryptPassword(enPassword1));
		memberService.save(member);
		rsp.setStatus(true);
		rsp.setMessage("设置会员密码成功！");
		return rsp;
	}

	
	/**
	 * 修改用户手机号
	 * @param oldUsername
	 * @param newUsername
	 * @param model
	 * @return
	 */
	@RequiresPermissions("mem:member:view")
	@RequestMapping(value = "updateUsername", method = RequestMethod.GET)
	public String updateUsername(Member member, Model model) {
		model.addAttribute("member", member);
		return "admin/mem/updateUsername";
	}
	/**
	 * 修改用户手机号
	 * @param oldUsername
	 * @param newUsername
	 * @param model
	 * @return
	 */
	@RequiresPermissions("mem:member:edit")
	@RequestMapping(value = "updateUsername", method = RequestMethod.POST)
	public String updateUsername(String oldUsername,String newUsername, Model model) {
		HandleRsp rsp = memberService.updateUsername(oldUsername,newUsername);
		model.addAttribute("message", rsp.getMessage());
		return "redirect:"+Global.getAdminPath()+"/member/updateUsername?repage";
	}

}