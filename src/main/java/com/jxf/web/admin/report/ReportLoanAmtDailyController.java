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

import com.jxf.report.entity.ReportLoanAmtDaily;
import com.jxf.report.service.ReportLoanAmtDailyService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;


/**
 * 借条金额统计Controller
 * @author wo
 * @version 2019-07-10
 */
@Controller
@RequestMapping(value = "${adminPath}/reportLoanAmtDaily")
public class ReportLoanAmtDailyController extends BaseController {

	@Autowired
	private ReportLoanAmtDailyService reportLoanAmtDailyService;
	
	@ModelAttribute
	public ReportLoanAmtDaily get(@RequestParam(required=false) Long id) {
		ReportLoanAmtDaily entity = null;
		if (id!=null){
			entity = reportLoanAmtDailyService.get(id);
		}
		if (entity == null){
			entity = new ReportLoanAmtDaily();
		}
		return entity;
	}
	
	@RequiresPermissions("loan:reportLoanAmtDaily:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReportLoanAmtDaily reportLoanAmtDaily, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ReportLoanAmtDaily> page = reportLoanAmtDailyService.findPage(new Page<ReportLoanAmtDaily>(request, response), reportLoanAmtDaily); 
		model.addAttribute("page", page);
		return "admin/report/loan/reportLoanAmtDailyList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("loan:reportLoanAmtDaily:view")
	@RequestMapping(value = "add")
	public String add(ReportLoanAmtDaily reportLoanAmtDaily, Model model) {
		model.addAttribute("reportLoanAmtDaily", reportLoanAmtDaily);
		return "admin/report/loan/reportLoanAmtDailyAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("loan:reportLoanAmtDaily:view")
	@RequestMapping(value = "query")
	public String query(ReportLoanAmtDaily reportLoanAmtDaily, Model model) {
		model.addAttribute("reportLoanAmtDaily", reportLoanAmtDaily);
		return "report/loan/reportLoanAmtDailyQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("loan:reportLoanAmtDaily:view")
	@RequestMapping(value = "update")
	public String update(ReportLoanAmtDaily reportLoanAmtDaily, Model model) {
		model.addAttribute("reportLoanAmtDaily", reportLoanAmtDaily);
		return "report/loan/reportLoanAmtDailyUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("loan:reportLoanAmtDaily:edit")
	@RequestMapping(value = "save")
	public String save(ReportLoanAmtDaily reportLoanAmtDaily, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, reportLoanAmtDaily)){
			return add(reportLoanAmtDaily, model);
		}
		reportLoanAmtDailyService.save(reportLoanAmtDaily);
		addMessage(redirectAttributes, "保存借条统计成功");
		return "redirect:"+Global.getAdminPath()+"/reportLoanAmtDaily/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("loan:reportLoanAmtDaily:edit")
	@RequestMapping(value = "delete")
	public String delete(ReportLoanAmtDaily reportLoanAmtDaily, RedirectAttributes redirectAttributes) {
		reportLoanAmtDailyService.delete(reportLoanAmtDaily);
		addMessage(redirectAttributes, "删除借条统计成功");
		return "redirect:"+Global.getAdminPath()+"/reportLoanAmtDaily/?repage";
	}

}