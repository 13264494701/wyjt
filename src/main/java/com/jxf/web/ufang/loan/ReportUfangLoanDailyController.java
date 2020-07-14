package com.jxf.web.ufang.loan;

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

import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.ufang.entity.ReportUfangLoanDaily;
import com.jxf.ufang.entity.UfangBrn;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.service.ReportUfangLoanDailyService;
import com.jxf.ufang.service.UfangBrnService;
import com.jxf.ufang.util.UfangUserUtils;
import com.jxf.web.admin.sys.BaseController;

/**
 * 借条统计Controller
 * @author suHuimin
 * @version 2019-03-25
 */
@Controller
@RequestMapping(value = "${ufangPath}/reportUfangLoanDaily")
public class ReportUfangLoanDailyController extends BaseController {

	@Autowired
	private ReportUfangLoanDailyService reportUfangLoanDailyService;
	@Autowired
	private UfangBrnService ufangBrnService;
	
	@ModelAttribute
	public ReportUfangLoanDaily get(@RequestParam(required=false) Long id) {
		ReportUfangLoanDaily entity = null;
		if (id!=null){
			entity = reportUfangLoanDailyService.get(id);
		}
		if (entity == null){
			entity = new ReportUfangLoanDaily();
		}
		return entity;
	}
	
	@RequiresPermissions("report:ufangLoanDaily:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReportUfangLoanDaily reportUfangLoanDaily, HttpServletRequest request, HttpServletResponse response, Model model) {
		UfangUser user = UfangUserUtils.getUser();
		UfangBrn ufangBrn = ufangBrnService.get(user.getBrn());
		reportUfangLoanDaily.setUfangBrn(ufangBrn.getParent());
		Page<ReportUfangLoanDaily> page = reportUfangLoanDailyService.findPage(new Page<ReportUfangLoanDaily>(request, response), reportUfangLoanDaily); 
		model.addAttribute("page", page);
		return "ufang/loan/report/reportUfangLoanDailyList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("report:ufangLoanDaily:view")
	@RequestMapping(value = "add")
	public String add(ReportUfangLoanDaily reportUfangLoanDaily, Model model) {
		model.addAttribute("reportUfangLoanDaily", reportUfangLoanDaily);
		return "ufang/loan/report/reportUfangLoanDailyAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("report:ufangLoanDaily:view")
	@RequestMapping(value = "query")
	public String query(ReportUfangLoanDaily reportUfangLoanDaily, Model model) {
		model.addAttribute("reportUfangLoanDaily", reportUfangLoanDaily);
		return "ufang/loan/report/reportUfangLoanDailyQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("report:ufangLoanDaily:view")
	@RequestMapping(value = "update")
	public String update(ReportUfangLoanDaily reportUfangLoanDaily, Model model) {
		model.addAttribute("reportUfangLoanDaily", reportUfangLoanDaily);
		return "ufang/loan/report/reportUfangLoanDailyUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("report:ufangLoanDaily:edit")
	@RequestMapping(value = "save")
	public String save(ReportUfangLoanDaily reportUfangLoanDaily, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, reportUfangLoanDaily)){
			return add(reportUfangLoanDaily, model);
		}
		reportUfangLoanDailyService.save(reportUfangLoanDaily);
		addMessage(redirectAttributes, "保存借条统计成功");
		return "redirect:"+Global.getAdminPath()+"/statistics/reportUfangLoanDaily/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("report:ufangLoanDaily:edit")
	@RequestMapping(value = "delete")
	public String delete(ReportUfangLoanDaily reportUfangLoanDaily, RedirectAttributes redirectAttributes) {
		reportUfangLoanDailyService.delete(reportUfangLoanDaily);
		addMessage(redirectAttributes, "删除借条统计成功");
		return "redirect:"+Global.getAdminPath()+"/statistics/reportUfangLoanDaily/?repage";
	}

}