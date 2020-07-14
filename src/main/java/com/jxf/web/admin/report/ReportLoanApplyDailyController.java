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

import com.jxf.report.entity.ReportLoanApplyDaily;
import com.jxf.report.service.ReportLoanApplyDailyService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;

/**
 * 申请统计Controller
 * @author XIAORONGDIAN
 * @version 2019-04-04
 */
@Controller
@RequestMapping(value = "${adminPath}/reportLoanApplyDaily")
public class ReportLoanApplyDailyController extends BaseController {

	@Autowired
	private ReportLoanApplyDailyService reportLoanApplyDailyService;
	
	@ModelAttribute
	public ReportLoanApplyDaily get(@RequestParam(required=false) Long id) {
		ReportLoanApplyDaily entity = null;
		if (id!=null){
			entity = reportLoanApplyDailyService.get(id);
		}
		if (entity == null){
			entity = new ReportLoanApplyDaily();
		}
		return entity;
	}
	
	@RequiresPermissions("report:reportLoanApplyDaily:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReportLoanApplyDaily reportLoanApplyDaily, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ReportLoanApplyDaily> page = reportLoanApplyDailyService.findPage(new Page<ReportLoanApplyDaily>(request, response), reportLoanApplyDaily); 
		model.addAttribute("page", page);
		return "admin/report/loan/reportLoanApplyDailyList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("report:reportLoanApplyDaily:view")
	@RequestMapping(value = "add")
	public String add(ReportLoanApplyDaily reportLoanApplyDaily, Model model) {
		model.addAttribute("reportLoanApplyDaily", reportLoanApplyDaily);
		return "report/report/reportLoanApplyDailyAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("report:reportLoanApplyDaily:view")
	@RequestMapping(value = "query")
	public String query(ReportLoanApplyDaily reportLoanApplyDaily, Model model) {
		model.addAttribute("reportLoanApplyDaily", reportLoanApplyDaily);
		return "report/report/reportLoanApplyDailyQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("report:reportLoanApplyDaily:view")
	@RequestMapping(value = "update")
	public String update(ReportLoanApplyDaily reportLoanApplyDaily, Model model) {
		model.addAttribute("reportLoanApplyDaily", reportLoanApplyDaily);
		return "report/report/reportLoanApplyDailyUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("report:reportLoanApplyDaily:edit")
	@RequestMapping(value = "save")
	public String save(ReportLoanApplyDaily reportLoanApplyDaily, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, reportLoanApplyDaily)){
			return add(reportLoanApplyDaily, model);
		}
		reportLoanApplyDailyService.save(reportLoanApplyDaily);
		addMessage(redirectAttributes, "保存申请统计成功");
		return "redirect:"+Global.getAdminPath()+"/report/reportLoanApplyDaily/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("report:reportLoanApplyDaily:edit")
	@RequestMapping(value = "delete")
	public String delete(ReportLoanApplyDaily reportLoanApplyDaily, RedirectAttributes redirectAttributes) {
		reportLoanApplyDailyService.delete(reportLoanApplyDaily);
		addMessage(redirectAttributes, "删除申请统计成功");
		return "redirect:"+Global.getAdminPath()+"/report/reportLoanApplyDaily/?repage";
	}

}