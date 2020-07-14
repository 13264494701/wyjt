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

import com.jxf.report.entity.ReportPayRefundDaily;
import com.jxf.report.service.ReportPayRefundDailyService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;


/**
 * 缴费退费统计Controller
 * @author wo
 * @version 2019-02-28
 */
@Controller
@RequestMapping(value = "${adminPath}/reportPayRefundDaily")
public class ReportPayRefundDailyController extends BaseController {

	@Autowired
	private ReportPayRefundDailyService reportPayRefundDailyService;
	
	@ModelAttribute
	public ReportPayRefundDaily get(@RequestParam(required=false) Long id) {
		ReportPayRefundDaily entity = null;
		if (id!=null){
			entity = reportPayRefundDailyService.get(id);
		}
		if (entity == null){
			entity = new ReportPayRefundDaily();
		}
		return entity;
	}
	
	@RequiresPermissions("payRefund:reportPayRefundDaily:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReportPayRefundDaily reportPayRefundDaily, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ReportPayRefundDaily> page = reportPayRefundDailyService.findPage(new Page<ReportPayRefundDaily>(request, response), reportPayRefundDaily); 
		model.addAttribute("page", page);
		return "admin/report/payRefund/reportPayRefundDailyList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("payRefund:reportPayRefundDaily:view")
	@RequestMapping(value = "add")
	public String add(ReportPayRefundDaily reportPayRefundDaily, Model model) {
		model.addAttribute("reportPayRefundDaily", reportPayRefundDaily);
		return "admin/report/payRefund/reportPayRefundDailyAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("payRefund:reportPayRefundDaily:view")
	@RequestMapping(value = "query")
	public String query(ReportPayRefundDaily reportPayRefundDaily, Model model) {
		model.addAttribute("reportPayRefundDaily", reportPayRefundDaily);
		return "report/payRefund/reportPayRefundDailyQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("payRefund:reportPayRefundDaily:view")
	@RequestMapping(value = "update")
	public String update(ReportPayRefundDaily reportPayRefundDaily, Model model) {
		model.addAttribute("reportPayRefundDaily", reportPayRefundDaily);
		return "report/payRefund/reportPayRefundDailyUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("payRefund:reportPayRefundDaily:edit")
	@RequestMapping(value = "save")
	public String save(ReportPayRefundDaily reportPayRefundDaily, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, reportPayRefundDaily)){
			return add(reportPayRefundDaily, model);
		}
		reportPayRefundDailyService.save(reportPayRefundDaily);
		addMessage(redirectAttributes, "保存借条统计成功");
		return "redirect:"+Global.getAdminPath()+"/reportPayRefundDaily/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("payRefund:reportPayRefundDaily:edit")
	@RequestMapping(value = "delete")
	public String delete(ReportPayRefundDaily reportPayRefundDaily, RedirectAttributes redirectAttributes) {
		reportPayRefundDailyService.delete(reportPayRefundDaily);
		addMessage(redirectAttributes, "删除借条统计成功");
		return "redirect:"+Global.getAdminPath()+"/reportPayRefundDaily/?repage";
	}

}