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

import com.jxf.report.entity.ReportWdrlDaily;
import com.jxf.report.service.ReportWdrlDailyService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;


/**
 * 提现统计Controller
 * @author wo
 * @version 2019-03-25
 */
@Controller
@RequestMapping(value = "${adminPath}/reportWdrlDaily")
public class ReportWdrlDailyController extends BaseController {

	@Autowired
	private ReportWdrlDailyService reportWdrlDailyService;
	
	@ModelAttribute
	public ReportWdrlDaily get(@RequestParam(required=false) Long id) {
		ReportWdrlDaily entity = null;
		if (id!=null){
			entity = reportWdrlDailyService.get(id);
		}
		if (entity == null){
			entity = new ReportWdrlDaily();
		}
		return entity;
	}
	
	@RequiresPermissions("wdrl:reportWdrlDaily:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReportWdrlDaily reportWdrlDaily, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ReportWdrlDaily> page = reportWdrlDailyService.findPage(new Page<ReportWdrlDaily>(request, response), reportWdrlDaily); 
		model.addAttribute("page", page);
		return "admin/report/wdrl/reportWdrlDailyList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("wdrl:reportWdrlDaily:view")
	@RequestMapping(value = "add")
	public String add(ReportWdrlDaily reportWdrlDaily, Model model) {
		model.addAttribute("reportWdrlDaily", reportWdrlDaily);
		return "report/wdrl/reportWdrlDailyAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("wdrl:reportWdrlDaily:view")
	@RequestMapping(value = "query")
	public String query(ReportWdrlDaily reportWdrlDaily, Model model) {
		model.addAttribute("reportWdrlDaily", reportWdrlDaily);
		return "report/wdrl/reportWdrlDailyQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("wdrl:reportWdrlDaily:view")
	@RequestMapping(value = "update")
	public String update(ReportWdrlDaily reportWdrlDaily, Model model) {
		model.addAttribute("reportWdrlDaily", reportWdrlDaily);
		return "report/wdrl/reportWdrlDailyUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("wdrl:reportWdrlDaily:edit")
	@RequestMapping(value = "save")
	public String save(ReportWdrlDaily reportWdrlDaily, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, reportWdrlDaily)){
			return add(reportWdrlDaily, model);
		}
		reportWdrlDailyService.save(reportWdrlDaily);
		addMessage(redirectAttributes, "保存提现统计成功");
		return "redirect:"+Global.getAdminPath()+"/reportWdrlDaily/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("wdrl:reportWdrlDaily:edit")
	@RequestMapping(value = "delete")
	public String delete(ReportWdrlDaily reportWdrlDaily, RedirectAttributes redirectAttributes) {
		reportWdrlDailyService.delete(reportWdrlDaily);
		addMessage(redirectAttributes, "删除提现统计成功");
		return "redirect:"+Global.getAdminPath()+"/reportWdrlDaily/?repage";
	}

}