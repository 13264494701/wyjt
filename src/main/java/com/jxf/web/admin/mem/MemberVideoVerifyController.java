package com.jxf.web.admin.mem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberVideoVerify;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.service.MemberVideoVerifyService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;

/**
 * 视频认证Controller
 * @author XIAORONGDIAN
 * @version 2018-10-10
 */
@Controller
@RequestMapping(value = "${adminPath}/memberVideoVerify")
public class MemberVideoVerifyController extends BaseController {

	@Autowired
	private MemberVideoVerifyService memberVideoVerifyService;
	
	@Autowired
	private MemberService memberService;
	
	@ModelAttribute
	public MemberVideoVerify get(@RequestParam(required=false) Long id) {
		MemberVideoVerify entity = null;
		if (id!=null){
			entity = memberVideoVerifyService.get(id);
		}
		if (entity == null){
			entity = new MemberVideoVerify();
		}
		return entity;
	}
	
	@RequiresPermissions("mem:videoVerify:view")
	@RequestMapping(value = {"list", ""})
	public String list(MemberVideoVerify memberVideoVerify, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MemberVideoVerify> page = memberVideoVerifyService.findPage(new Page<MemberVideoVerify>(request, response), memberVideoVerify); 
		model.addAttribute("page", page);
		return "admin/mem/videoVerify/memberVideoVerifyList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("mem:videoVerify:view")
	@RequestMapping(value = "add")
	public String add(MemberVideoVerify memberVideoVerify, Model model) {
		model.addAttribute("memberVideoVerify", memberVideoVerify);
		return "admin/mem/videoVerify/memberVideoVerifyAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("mem:videoVerify:view")
	@RequestMapping(value = "query")
	public String query(MemberVideoVerify memberVideoVerify, Model model) {
		model.addAttribute("memberVideoVerify", memberVideoVerify);
		return "admin/mem/videoVerify/memberVideoVerifyQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("mem:videoVerify:view")
	@RequestMapping(value = "update")
	public String update(MemberVideoVerify memberVideoVerify, Model model) {
		model.addAttribute("memberVideoVerify", memberVideoVerify);
		return "admin/mem/videoVerify/memberVideoVerifyUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("mem:videoVerify:edit")
	@RequestMapping(value = "save")
	public String save(MemberVideoVerify memberVideoVerify, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, memberVideoVerify)){
			return add(memberVideoVerify, model);
		}
		memberVideoVerifyService.save(memberVideoVerify);
		addMessage(redirectAttributes, "保存视频认证成功");
		return "redirect:"+Global.getAdminPath()+"/memberVideoVerify/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("mem:videoVerify:edit")
	@RequestMapping(value = "delete")
	public String delete(MemberVideoVerify memberVideoVerify, RedirectAttributes redirectAttributes) {
		memberVideoVerifyService.delete(memberVideoVerify);
		addMessage(redirectAttributes, "删除视频认证成功");
		return "redirect:"+Global.getAdminPath()+"/memberVideoVerify/?repage";
	}
	/**
	 * 清除认证
	 */
	@RequiresPermissions("mem:videoVerify:edit")
	@RequestMapping(value = "clear")
	public String clear(MemberVideoVerify memberVideoVerify, RedirectAttributes redirectAttributes) {
		memberVideoVerifyService.clear(memberVideoVerify);
		addMessage(redirectAttributes, "清除视频认证成功");
		return "redirect:"+Global.getAdminPath()+"/memberVideoVerify/?repage";
	}
	
	/**
	 * 根据用户id查询实名信息
	 * @param memberVideoVerify
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("mem:videoVerify:view")
	@RequestMapping(value = {"getMemberVideoVerifyByMemberId"})
	public String getMemberVideoVerifyByMemberId(MemberVideoVerify memberVideoVerify, HttpServletRequest request, HttpServletResponse response, Long memberId,Model model) {
		Member member = memberService.get(memberId);
		memberVideoVerify.setMember(member);
		Page<MemberVideoVerify> page = memberVideoVerifyService.findPage(new Page<MemberVideoVerify>(request, response), memberVideoVerify); 
		model.addAttribute("page", page);
		return "admin/mem/videoVerify/memberVideoVerifyList";
	}

}