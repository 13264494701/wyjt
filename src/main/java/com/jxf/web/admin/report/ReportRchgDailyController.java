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

import com.jxf.report.entity.ReportRchgDaily;
import com.jxf.report.service.ReportRchgDailyService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;


/**
 * 充值统计Controller
 * @author wo
 * @version 2019-03-25
 */
@Controller
@RequestMapping(value = "${adminPath}/reportRchgDaily")
public class ReportRchgDailyController extends BaseController {

	@Autowired
	private ReportRchgDailyService reportRchgDailyService;
	
	@ModelAttribute
	public ReportRchgDaily get(@RequestParam(required=false) Long id) {
		ReportRchgDaily entity = null;
		if (id!=null){
			entity = reportRchgDailyService.get(id);
		}
		if (entity == null){
			entity = new ReportRchgDaily();
		}
		return entity;
	}
	
	@RequiresPermissions("rchg:reportRchgDaily:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReportRchgDaily reportRchgDaily, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ReportRchgDaily> page = reportRchgDailyService.findPage(new Page<ReportRchgDaily>(request, response), reportRchgDaily); 
		model.addAttribute("page", page);
		return "admin/report/rchg/reportRchgDailyList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("rchg:reportRchgDaily:view")
	@RequestMapping(value = "add")
	public String add(ReportRchgDaily reportRchgDaily, Model model) {
		model.addAttribute("reportRchgDaily", reportRchgDaily);
		return "report/rchg/reportRchgDailyAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("rchg:reportRchgDaily:view")
	@RequestMapping(value = "query")
	public String query(ReportRchgDaily reportRchgDaily, Model model) {
		model.addAttribute("reportRchgDaily", reportRchgDaily);
		return "report/rchg/reportRchgDailyQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("rchg:reportRchgDaily:view")
	@RequestMapping(value = "update")
	public String update(ReportRchgDaily reportRchgDaily, Model model) {
		model.addAttribute("reportRchgDaily", reportRchgDaily);
		return "report/rchg/reportRchgDailyUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("rchg:reportRchgDaily:edit")
	@RequestMapping(value = "save")
	public String save(ReportRchgDaily reportRchgDaily, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, reportRchgDaily)){
			return add(reportRchgDaily, model);
		}
		reportRchgDailyService.save(reportRchgDaily);
		addMessage(redirectAttributes, "保存充值统计成功");
		return "redirect:"+Global.getAdminPath()+"/reportRchgDaily/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("rchg:reportRchgDaily:edit")
	@RequestMapping(value = "delete")
	public String delete(ReportRchgDaily reportRchgDaily, RedirectAttributes redirectAttributes) {
		reportRchgDailyService.delete(reportRchgDaily);
		addMessage(redirectAttributes, "删除充值统计成功");
		return "redirect:"+Global.getAdminPath()+"/reportRchgDaily/?repage";
	}

}