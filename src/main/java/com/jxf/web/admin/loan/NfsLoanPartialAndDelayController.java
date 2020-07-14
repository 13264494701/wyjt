package com.jxf.web.admin.loan;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.loan.entity.NfsLoanPartialAndDelay;
import com.jxf.loan.service.NfsLoanPartialAndDelayService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.web.admin.sys.BaseController;

/**
 * 部分还款和延期Controller
 * @author XIAORONGDIAN
 * @version 2018-12-11
 */
@Controller
@RequestMapping(value = "${adminPath}/loanPartialAndDelay")
public class NfsLoanPartialAndDelayController extends BaseController {

	@Autowired
	private NfsLoanPartialAndDelayService loanPartialAndDelayService;
	
	@ModelAttribute
	public NfsLoanPartialAndDelay get(@RequestParam(required=false) Long id) {
		NfsLoanPartialAndDelay entity = null;
		if (id!=null){
			entity = loanPartialAndDelayService.get(id);
		}
		if (entity == null){
			entity = new NfsLoanPartialAndDelay();
		}
		return entity;
	}
	
	@RequiresPermissions("loan:loanPartialAndDelay:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsLoanPartialAndDelay loanPartialAndDelay, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsLoanPartialAndDelay> page = loanPartialAndDelayService.findPage(new Page<NfsLoanPartialAndDelay>(request, response), loanPartialAndDelay); 
		if(!StringUtils.isBlank(request.getParameter("beginTime"))){
			model.addAttribute("beginTime", DateUtils.parseDate(request.getParameter("beginTime")));
		}
		if(!StringUtils.isBlank(request.getParameter("endTime"))){
			model.addAttribute("endTime", DateUtils.parseDate(request.getParameter("endTime")));
		}
		model.addAttribute("page", page);
		return "admin/loan/partialAndDelay/loanPartialAndDelayList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("loan:loanPartialAndDelay:view")
	@RequestMapping(value = "add")
	public String add(NfsLoanPartialAndDelay loanPartialAndDelay, Model model) {
		model.addAttribute("loanPartialAndDelay", loanPartialAndDelay);
		return "admin/loan/partialAndDelay/loanPartialAndDelayAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("loan:loanPartialAndDelay:view")
	@RequestMapping(value = "query")
	public String query(NfsLoanPartialAndDelay loanPartialAndDelay, Model model) {
		model.addAttribute("loanPartialAndDelay", loanPartialAndDelay);
		return "admin/loan/partialAndDelay/loanPartialAndDelayQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("loan:loanPartialAndDelay:view")
	@RequestMapping(value = "update")
	public String update(NfsLoanPartialAndDelay loanPartialAndDelay, Model model) {
		model.addAttribute("loanPartialAndDelay", loanPartialAndDelay);
		return "admin/loan/partialAndDelay/loanPartialAndDelayUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("loan:loanPartialAndDelay:edit")
	@RequestMapping(value = "save")
	public String save(NfsLoanPartialAndDelay loanPartialAndDelay, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, loanPartialAndDelay)){
			return add(loanPartialAndDelay, model);
		}
		try {
			loanPartialAndDelayService.save(loanPartialAndDelay);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
			addMessage(redirectAttributes, "保存部分还款和延期失败");
			return "redirect:"+Global.getAdminPath()+"/loanPartialAndDelay/?repage";
		}
		addMessage(redirectAttributes, "保存部分还款和延期成功");
		return "redirect:"+Global.getAdminPath()+"/loanPartialAndDelay/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("loan:loanPartialAndDelay:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsLoanPartialAndDelay loanPartialAndDelay, RedirectAttributes redirectAttributes) {
		loanPartialAndDelayService.delete(loanPartialAndDelay);
		addMessage(redirectAttributes, "删除部分还款和延期成功");
		return "redirect:"+Global.getAdminPath()+"/loanPartialAndDelay/?repage";
	}

}