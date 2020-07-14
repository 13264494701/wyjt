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

import com.jxf.mem.entity.MemberVerified;
import com.jxf.mem.service.MemberVerifiedService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;


/**
 * 实名认证Controller
 * @author wo
 * @version 2018-09-28
 */
@Controller
@RequestMapping(value = "${adminPath}/memberVerified")
public class MemberVerifiedController extends BaseController {

	@Autowired
	private MemberVerifiedService memberVerifiedService;
	
	@ModelAttribute
	public MemberVerified get(@RequestParam(required=false) Long id) {
		MemberVerified entity = null;
		if (id!=null){
			entity = memberVerifiedService.get(id);
		}
		if (entity == null){
			entity = new MemberVerified();
		}
		return entity;
	}
	
	@RequiresPermissions("verified:memberVerified:view")
	@RequestMapping(value = {"list", ""})
	public String list(MemberVerified memberVerified, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MemberVerified> page = memberVerifiedService.findPage(new Page<MemberVerified>(request, response), memberVerified); 
		model.addAttribute("page", page);
		return "mem/verified/memberVerifiedList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("verified:memberVerified:view")
	@RequestMapping(value = "add")
	public String add(MemberVerified memberVerified, Model model) {
		model.addAttribute("memberVerified", memberVerified);
		return "mem/verified/memberVerifiedAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("verified:memberVerified:view")
	@RequestMapping(value = "query")
	public String query(MemberVerified memberVerified, Model model) {
		model.addAttribute("memberVerified", memberVerified);
		return "mem/verified/memberVerifiedQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("verified:memberVerified:view")
	@RequestMapping(value = "update")
	public String update(MemberVerified memberVerified, Model model) {
		model.addAttribute("memberVerified", memberVerified);
		return "mem/verified/memberVerifiedUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("verified:memberVerified:edit")
	@RequestMapping(value = "save")
	public String save(MemberVerified memberVerified, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, memberVerified)){
			return add(memberVerified, model);
		}
		memberVerifiedService.save(memberVerified);
		addMessage(redirectAttributes, "保存实名认证成功");
		return "redirect:"+Global.getAdminPath()+"/memberVerified/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("verified:memberVerified:edit")
	@RequestMapping(value = "delete")
	public String delete(MemberVerified memberVerified, RedirectAttributes redirectAttributes) {
		memberVerifiedService.delete(memberVerified);
		addMessage(redirectAttributes, "删除实名认证成功");
		return "redirect:"+Global.getAdminPath()+"/memberVerified/?repage";
	}

}