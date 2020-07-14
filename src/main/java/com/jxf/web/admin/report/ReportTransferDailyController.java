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

import com.jxf.report.entity.ReportTransferDaily;
import com.jxf.report.service.ReportTransferDailyService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;


/**
 * 转账统计Controller
 * @author wo
 * @version 2019-03-25
 */
@Controller
@RequestMapping(value = "${adminPath}/reportTransferDaily")
public class ReportTransferDailyController extends BaseController {

	@Autowired
	private ReportTransferDailyService reportTransferDailyService;
	
	@ModelAttribute
	public ReportTransferDaily get(@RequestParam(required=false) Long id) {
		ReportTransferDaily entity = null;
		if (id!=null){
			entity = reportTransferDailyService.get(id);
		}
		if (entity == null){
			entity = new ReportTransferDaily();
		}
		return entity;
	}
	
	@RequiresPermissions("transfer:reportTransferDaily:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReportTransferDaily reportTransferDaily, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ReportTransferDaily> page = reportTransferDailyService.findPage(new Page<ReportTransferDaily>(request, response), reportTransferDaily); 
		model.addAttribute("page", page);
		return "admin/report/transfer/reportTransferDailyList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("transfer:reportTransferDaily:view")
	@RequestMapping(value = "add")
	public String add(ReportTransferDaily reportTransferDaily, Model model) {
		model.addAttribute("reportTransferDaily", reportTransferDaily);
		return "report/transfer/reportTransferDailyAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("transfer:reportTransferDaily:view")
	@RequestMapping(value = "query")
	public String query(ReportTransferDaily reportTransferDaily, Model model) {
		model.addAttribute("reportTransferDaily", reportTransferDaily);
		return "report/transfer/reportTransferDailyQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("transfer:reportTransferDaily:view")
	@RequestMapping(value = "update")
	public String update(ReportTransferDaily reportTransferDaily, Model model) {
		model.addAttribute("reportTransferDaily", reportTransferDaily);
		return "report/transfer/reportTransferDailyUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("transfer:reportTransferDaily:edit")
	@RequestMapping(value = "save")
	public String save(ReportTransferDaily reportTransferDaily, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, reportTransferDaily)){
			return add(reportTransferDaily, model);
		}
		reportTransferDailyService.save(reportTransferDaily);
		addMessage(redirectAttributes, "保存转账统计成功");
		return "redirect:"+Global.getAdminPath()+"/reportTransferDaily/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("transfer:reportTransferDaily:edit")
	@RequestMapping(value = "delete")
	public String delete(ReportTransferDaily reportTransferDaily, RedirectAttributes redirectAttributes) {
		reportTransferDailyService.delete(reportTransferDaily);
		addMessage(redirectAttributes, "删除转账统计成功");
		return "redirect:"+Global.getAdminPath()+"/reportTransferDaily/?repage";
	}

}