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

import com.jxf.mem.entity.MemberFeedback;
import com.jxf.mem.service.MemberFeedbackService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;

/**
 * 保存用户反馈意见Controller
 * @author suhuimin
 * @version 2018-11-01
 */
@Controller
@RequestMapping(value = "${adminPath}/feedback/memberFeedback")
public class MemberFeedbackController extends BaseController {

	@Autowired
	private MemberFeedbackService memberFeedbackService;
	
	@ModelAttribute
	public MemberFeedback get(@RequestParam(required=false) Long id) {
		MemberFeedback entity = null;
		if (id!=null){
			entity = memberFeedbackService.get(id);
		}
		if (entity == null){
			entity = new MemberFeedback();
		}
		return entity;
	}
	
	@RequiresPermissions("feedback:memberFeedback:view")
	@RequestMapping(value = {"list", ""})
	public String list(MemberFeedback memberFeedback, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MemberFeedback> page = memberFeedbackService.findPage(new Page<MemberFeedback>(request, response), memberFeedback); 
		model.addAttribute("page", page);
		return "admin/mem/feedback/memberFeedbackList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("feedback:memberFeedback:view")
	@RequestMapping(value = "add")
	public String add(MemberFeedback memberFeedback, Model model) {
		model.addAttribute("memberFeedback", memberFeedback);
		return "mem/feedback/memberFeedbackAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("feedback:memberFeedback:view")
	@RequestMapping(value = "query")
	public String query(MemberFeedback memberFeedback, Model model) {
		model.addAttribute("memberFeedback", memberFeedback);
		return "mem/feedback/memberFeedbackQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("feedback:MemberFeedback:view")
	@RequestMapping(value = "update")
	public String update(MemberFeedback MemberFeedback, Model model) {
		model.addAttribute("MemberFeedback", MemberFeedback);
		return "mem/feedback/memberFeedbackUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("feedback:MemberFeedback:edit")
	@RequestMapping(value = "save")
	public String save(MemberFeedback MemberFeedback, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, MemberFeedback)){
			return add(MemberFeedback, model);
		}
		memberFeedbackService.save(MemberFeedback);
		addMessage(redirectAttributes, "保存用户反馈意见成功");
		return "redirect:"+Global.getAdminPath()+"/feedback/memberFeedback/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("feedback:MemberFeedback:edit")
	@RequestMapping(value = "delete")
	public String delete(MemberFeedback memberFeedback, RedirectAttributes redirectAttributes) {
		memberFeedbackService.delete(memberFeedback);
		addMessage(redirectAttributes, "删除用户反馈意见成功");
		return "redirect:"+Global.getAdminPath()+"/feedback/memberFeedback/?repage";
	}

}