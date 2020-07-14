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

import com.jxf.report.entity.ReportLoaneeDataDaily;
import com.jxf.report.service.ReportLoaneeDataDailyService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;


/**
 * 流量统计Controller
 * @author wo
 * @version 2019-02-28
 */
@Controller
@RequestMapping(value = "${adminPath}/reportLoaneeDataDaily")
public class ReportLoaneeDataDailyController extends BaseController {

	@Autowired
	private ReportLoaneeDataDailyService reportLoaneeDataDailyService;
	
	@ModelAttribute
	public ReportLoaneeDataDaily get(@RequestParam(required=false) Long id) {
		ReportLoaneeDataDaily entity = null;
		if (id!=null){
			entity = reportLoaneeDataDailyService.get(id);
		}
		if (entity == null){
			entity = new ReportLoaneeDataDaily();
		}
		return entity;
	}
	
	@RequiresPermissions("loaneedata:reportLoaneeDataDaily:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReportLoaneeDataDaily reportLoaneeDataDaily, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ReportLoaneeDataDaily> page = reportLoaneeDataDailyService.findPage(new Page<ReportLoaneeDataDaily>(request, response), reportLoaneeDataDaily); 
		model.addAttribute("page", page);
		return "admin/report/loaneedata/reportLoaneeDataDailyList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("loaneedata:reportLoaneeDataDaily:view")
	@RequestMapping(value = "add")
	public String add(ReportLoaneeDataDaily reportLoaneeDataDaily, Model model) {
		model.addAttribute("reportLoaneeDataDaily", reportLoaneeDataDaily);
		return "report/loaneedata/reportLoaneeDataDailyAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("loaneedata:reportLoaneeDataDaily:view")
	@RequestMapping(value = "query")
	public String query(ReportLoaneeDataDaily reportLoaneeDataDaily, Model model) {
		model.addAttribute("reportLoaneeDataDaily", reportLoaneeDataDaily);
		return "report/loaneedata/reportLoaneeDataDailyQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("loaneedata:reportLoaneeDataDaily:view")
	@RequestMapping(value = "update")
	public String update(ReportLoaneeDataDaily reportLoaneeDataDaily, Model model) {
		model.addAttribute("reportLoaneeDataDaily", reportLoaneeDataDaily);
		return "report/loaneedata/reportLoaneeDataDailyUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("loaneedata:reportLoaneeDataDaily:edit")
	@RequestMapping(value = "save")
	public String save(ReportLoaneeDataDaily reportLoaneeDataDaily, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, reportLoaneeDataDaily)){
			return add(reportLoaneeDataDaily, model);
		}
		reportLoaneeDataDailyService.save(reportLoaneeDataDaily);
		addMessage(redirectAttributes, "保存流量统计成功");
		return "redirect:"+Global.getAdminPath()+"/reportLoaneeDataDaily/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("loaneedata:reportLoaneeDataDaily:edit")
	@RequestMapping(value = "delete")
	public String delete(ReportLoaneeDataDaily reportLoaneeDataDaily, RedirectAttributes redirectAttributes) {
		reportLoaneeDataDailyService.delete(reportLoaneeDataDaily);
		addMessage(redirectAttributes, "删除流量统计成功");
		return "redirect:"+Global.getAdminPath()+"/reportLoaneeDataDaily/?repage";
	}

}