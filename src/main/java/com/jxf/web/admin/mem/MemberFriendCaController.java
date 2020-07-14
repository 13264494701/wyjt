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

import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;
import com.jxf.mem.entity.MemberFriendCa;
import com.jxf.mem.service.MemberFriendCaService;

/**
 * 信用报告申请Controller
 * @author wo
 * @version 2018-12-17
 */
@Controller
@RequestMapping(value = "${adminPath}/ca/memberFriendCa")
public class MemberFriendCaController extends BaseController {

	@Autowired
	private MemberFriendCaService memberFriendCaService;
	
	@ModelAttribute
	public MemberFriendCa get(@RequestParam(required=false) Long id) {
		MemberFriendCa entity = null;
		if (id!=null){
			entity = memberFriendCaService.get(id);
		}
		if (entity == null){
			entity = new MemberFriendCa();
		}
		return entity;
	}
	
	@RequiresPermissions("ca:memberFriendCa:view")
	@RequestMapping(value = {"list", ""})
	public String list(MemberFriendCa memberFriendCa, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MemberFriendCa> page = memberFriendCaService.findPage(new Page<MemberFriendCa>(request, response), memberFriendCa); 
		model.addAttribute("page", page);
		return "admin/mem/ca/memberFriendCaList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("ca:memberFriendCa:view")
	@RequestMapping(value = "add")
	public String add(MemberFriendCa memberFriendCa, Model model) {
		model.addAttribute("memberFriendCa", memberFriendCa);
		return "admin/mem/ca/memberFriendCaAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("ca:memberFriendCa:view")
	@RequestMapping(value = "query")
	public String query(MemberFriendCa memberFriendCa, Model model) {
		model.addAttribute("memberFriendCa", memberFriendCa);
		return "admin/mem/ca/memberFriendCaQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("ca:memberFriendCa:view")
	@RequestMapping(value = "update")
	public String update(MemberFriendCa memberFriendCa, Model model) {
		model.addAttribute("memberFriendCa", memberFriendCa);
		return "admin/mem/ca/memberFriendCaUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("ca:memberFriendCa:edit")
	@RequestMapping(value = "save")
	public String save(MemberFriendCa memberFriendCa, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, memberFriendCa)){
			return add(memberFriendCa, model);
		}
		memberFriendCaService.save(memberFriendCa);
		addMessage(redirectAttributes, "保存信用报告申请成功");
		return "redirect:"+Global.getAdminPath()+"/ca/memberFriendCa/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("ca:memberFriendCa:edit")
	@RequestMapping(value = "delete")
	public String delete(MemberFriendCa memberFriendCa, RedirectAttributes redirectAttributes) {
		memberFriendCaService.delete(memberFriendCa);
		addMessage(redirectAttributes, "删除信用报告申请成功");
		return "redirect:"+Global.getAdminPath()+"/ca/memberFriendCa/?repage";
	}

}