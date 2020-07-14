package com.jxf.web.admin.report;

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

import com.jxf.report.entity.ReportLoanDaily;
import com.jxf.report.service.ReportLoanDailyService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;


/**
 * 借条统计Controller
 * @author wo
 * @version 2019-02-28
 */
@Controller
@RequestMapping(value = "${adminPath}/reportLoanDaily")
public class ReportLoanDailyController extends BaseController {

	@Autowired
	private ReportLoanDailyService reportLoanDailyService;
	
	@ModelAttribute
	public ReportLoanDaily get(@RequestParam(required=false) Long id) {
		ReportLoanDaily entity = null;
		if (id!=null){
			entity = reportLoanDailyService.get(id);
		}
		if (entity == null){
			entity = new ReportLoanDaily();
		}
		return entity;
	}
	
	@RequiresPermissions("loan:reportLoanDaily:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReportLoanDaily reportLoanDaily, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ReportLoanDaily> page = reportLoanDailyService.findPage(new Page<ReportLoanDaily>(request, response), reportLoanDaily); 
		model.addAttribute("page", page);
		return "admin/report/loan/reportLoanDailyList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("loan:reportLoanDaily:view")
	@RequestMapping(value = "add")
	public String add(ReportLoanDaily reportLoanDaily, Model model) {
		model.addAttribute("reportLoanDaily", reportLoanDaily);
		return "admin/report/loan/reportLoanDailyAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("loan:reportLoanDaily:view")
	@RequestMapping(value = "query")
	public String query(ReportLoanDaily reportLoanDaily, Model model) {
		model.addAttribute("reportLoanDaily", reportLoanDaily);
		return "report/loan/reportLoanDailyQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("loan:reportLoanDaily:view")
	@RequestMapping(value = "update")
	public String update(ReportLoanDaily reportLoanDaily, Model model) {
		model.addAttribute("reportLoanDaily", reportLoanDaily);
		return "report/loan/reportLoanDailyUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("loan:reportLoanDaily:edit")
	@RequestMapping(value = "save")
	public String save(ReportLoanDaily reportLoanDaily, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, reportLoanDaily)){
			return add(reportLoanDaily, model);
		}
		reportLoanDailyService.save(reportLoanDaily);
		addMessage(redirectAttributes, "保存借条统计成功");
		return "redirect:"+Global.getAdminPath()+"/reportLoanDaily/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("loan:reportLoanDaily:edit")
	@RequestMapping(value = "delete")
	public String delete(ReportLoanDaily reportLoanDaily, RedirectAttributes redirectAttributes) {
		reportLoanDailyService.delete(reportLoanDaily);
		addMessage(redirectAttributes, "删除借条统计成功");
		return "redirect:"+Global.getAdminPath()+"/reportLoanDaily/?repage";
	}

}