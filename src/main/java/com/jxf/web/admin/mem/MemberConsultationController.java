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

import com.jxf.mem.entity.MemberConsultation;
import com.jxf.mem.service.MemberConsultationService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;


/**
 * 会员评论Controller
 * @author JINXINFU
 * @version 2016-10-07
 */
@Controller("adminMemberConsultationController")
@RequestMapping(value = "${adminPath}/member/consultation")
public class MemberConsultationController extends BaseController {

	@Autowired
	private MemberConsultationService consultationService;
	
	@ModelAttribute
	public MemberConsultation get(@RequestParam(required=false) Long id) {
		MemberConsultation entity = null;
		if (id!=null){
			entity = consultationService.get(id);
		}
		if (entity == null){
			entity = new MemberConsultation();
		}
		return entity;
	}
	
	@RequiresPermissions("mem:consultation:view")
	@RequestMapping(value = {"list", ""})
	public String list(MemberConsultation consultation, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MemberConsultation> page = consultationService.findPage(new Page<MemberConsultation>(request, response), consultation); 
		model.addAttribute("page", page);
		return "mem/review/consultationList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("mem:consultation:view")
	@RequestMapping(value = "add")
	public String add(MemberConsultation consultation, Model model) {
		model.addAttribute("consultation", consultation);
		return "mem/review/consultationAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("mem:consultation:view")
	@RequestMapping(value = "query")
	public String query(MemberConsultation consultation, Model model) {
		model.addAttribute("consultation", consultation);
		return "mem/review/consultationQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("mem:consultation:view")
	@RequestMapping(value = "update")
	public String update(MemberConsultation consultation, Model model) {
		model.addAttribute("consultation", consultation);
		return "mem/review/consultationUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("mem:consultation:edit")
	@RequestMapping(value = "save")
	public String save(MemberConsultation consultation, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, consultation)){
			return add(consultation, model);
		}
		consultationService.save(consultation);
		addMessage(redirectAttributes, "保存会员评论成功");
		return "redirect:"+Global.getAdminPath()+"/member/consultation/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("mem:consultation:edit")
	@RequestMapping(value = "delete")
	public String delete(MemberConsultation consultation, RedirectAttributes redirectAttributes) {
		consultationService.delete(consultation);
		addMessage(redirectAttributes, "删除会员评论成功");
		return "redirect:"+Global.getAdminPath()+"/member/consultation/?repage";
	}

}