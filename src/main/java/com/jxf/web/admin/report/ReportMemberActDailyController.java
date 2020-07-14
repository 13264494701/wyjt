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

import com.jxf.report.entity.ReportMemberActDaily;
import com.jxf.report.service.ReportMemberActDailyService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;


/**
 * 账户统计Controller
 * @author wo
 * @version 2019-02-22
 */
@Controller
@RequestMapping(value = "${adminPath}/reportMemberActDaily")
public class ReportMemberActDailyController extends BaseController {

	@Autowired
	private ReportMemberActDailyService reportMemberActDailyService;
	
	@ModelAttribute
	public ReportMemberActDaily get(@RequestParam(required=false) Long id) {
		ReportMemberActDaily entity = null;
		if (id!=null){
			entity = reportMemberActDailyService.get(id);
		}
		if (entity == null){
			entity = new ReportMemberActDaily();
		}
		return entity;
	}
	
	@RequiresPermissions("report:memberActDaily:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReportMemberActDaily reportMemberActDaily, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ReportMemberActDaily> page = reportMemberActDailyService.findPage(new Page<ReportMemberActDaily>(request, response), reportMemberActDaily); 
		model.addAttribute("page", page);
		return "admin/report/memberact/reportMemberActDailyList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("report:memberActDaily:view")
	@RequestMapping(value = "add")
	public String add(ReportMemberActDaily reportMemberActDaily, Model model) {
		model.addAttribute("reportMemberActDaily", reportMemberActDaily);
		return "admin/report/memberact/reportMemberActDailyAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("report:memberActDaily:view")
	@RequestMapping(value = "query")
	public String query(ReportMemberActDaily reportMemberActDaily, Model model) {
		model.addAttribute("reportMemberActDaily", reportMemberActDaily);
		return "admin/report/memberact/reportMemberActDailyQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("report:memberActDaily:view")
	@RequestMapping(value = "update")
	public String update(ReportMemberActDaily reportMemberActDaily, Model model) {
		model.addAttribute("reportMemberActDaily", reportMemberActDaily);
		return "admin/report/memberact/reportMemberActDailyUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("memberact:reportMemberActDaily:edit")
	@RequestMapping(value = "save")
	public String save(ReportMemberActDaily reportMemberActDaily, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, reportMemberActDaily)){
			return add(reportMemberActDaily, model);
		}
		reportMemberActDailyService.save(reportMemberActDaily);
		addMessage(redirectAttributes, "保存账户统计成功");
		return "redirect:"+Global.getAdminPath()+"/reportMemberActDaily/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("memberact:reportMemberActDaily:edit")
	@RequestMapping(value = "delete")
	public String delete(ReportMemberActDaily reportMemberActDaily, RedirectAttributes redirectAttributes) {
		reportMemberActDailyService.delete(reportMemberActDaily);
		addMessage(redirectAttributes, "删除账户统计成功");
		return "redirect:"+Global.getAdminPath()+"/reportMemberActDaily/?repage";
	}

}