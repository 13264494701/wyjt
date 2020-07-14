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
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.mem.service.MemberService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;

import com.jxf.web.admin.sys.BaseController;


/**
 * 交易流水Controller
 * @author wo
 * @version 2018-09-19
 */
@Controller
@RequestMapping(value = "${adminPath}/memberActTrx")
public class MemberActTrxController extends BaseController {

	@Autowired
	private MemberActTrxService memberActTrxService;
	@Autowired
	private MemberService memberService;
	
	@ModelAttribute
	public MemberActTrx get(@RequestParam(required=false) Long id) {
		MemberActTrx entity = null;
		if (id!=null){
			entity = memberActTrxService.get(id);
		}
		if (entity == null){
			entity = new MemberActTrx();
		}
		return entity;
	}
	
	@RequiresPermissions("act:memberAct:view")
	@RequestMapping(value = {"list", ""})
	public String list(MemberActTrx memberActTrx, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MemberActTrx> page = new Page<MemberActTrx>(request, response);
		page.setPageSize(5);
		page = memberActTrxService.findPage(page, memberActTrx);
		model.addAttribute("page", page);
		model.addAttribute("memberActTrx", memberActTrx);
		return "admin/mem/act/trx/memberActTrxList";
	}
	
	@RequiresPermissions("act:memberAct:view")
	@RequestMapping(value = "allSubTrxList")
	public String allSubTrxList(MemberActTrx memberActTrx, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MemberActTrx> page = new Page<MemberActTrx>(request, response);
		page.setPageSize(15);
		Member member = memberService.get(memberActTrx.getMember());
		page = memberActTrxService.findActTrxPage(page, memberActTrx);
		for(MemberActTrx trx:page.getList()) {
			trx.setMember(member);		
		}
		model.addAttribute("page", page);
		model.addAttribute("memberActTrx", memberActTrx);
		return "admin/mem/act/trx/memberActTrxAllSubList";
	}
	
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("act:memberAct:view")
	@RequestMapping(value = "add")
	public String add(MemberActTrx memberActTrx, Model model) {
		model.addAttribute("memberActTrx", memberActTrx);
		return "admin/mem/act/trx/memberActTrxAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("act:memberAct:view")
	@RequestMapping(value = "query")
	public String query(MemberActTrx memberActTrx, Model model) {
		model.addAttribute("memberActTrx", memberActTrx);
		return "admin/mem/act/trx/memberActTrxQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("act:memberAct:view")
	@RequestMapping(value = "update")
	public String update(MemberActTrx memberActTrx, Model model) {
		model.addAttribute("memberActTrx", memberActTrx);
		return "admin/mem/act/trx/memberActTrxUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("act:memberAct:edit")
	@RequestMapping(value = "save")
	public String save(MemberActTrx memberActTrx, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, memberActTrx)){
			return add(memberActTrx, model);
		}
		memberActTrxService.save(memberActTrx);
		addMessage(redirectAttributes, "保存交易流水成功");
		return "redirect:"+Global.getAdminPath()+"/memberActTrx/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("act:memberAct:edit")
	@RequestMapping(value = "delete")
	public String delete(MemberActTrx memberActTrx, RedirectAttributes redirectAttributes) {
		memberActTrxService.delete(memberActTrx);
		addMessage(redirectAttributes, "删除交易流水成功");
		return "redirect:"+Global.getAdminPath()+"/memberActTrx/?repage";
	}	

}