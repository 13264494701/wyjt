package com.jxf.web.admin.loan;

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

import com.jxf.loan.entity.NfsLoanOperatingRecord;
import com.jxf.loan.service.NfsLoanOperatingRecordService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;

/**
 * 借条操作记录Controller
 * @author XIAORONGDIAN
 * @version 2018-12-18
 */
@Controller
@RequestMapping(value = "${adminPath}/loanOperatingRecord")
public class NfsLoanOperatingRecordController extends BaseController {

	@Autowired
	private NfsLoanOperatingRecordService loanOperatingRecordService;
	
	@ModelAttribute
	public NfsLoanOperatingRecord get(@RequestParam(required=false) Long id) {
		NfsLoanOperatingRecord entity = null;
		if (id!=null){
			entity = loanOperatingRecordService.get(id);
		}
		if (entity == null){
			entity = new NfsLoanOperatingRecord();
		}
		return entity;
	}
	
	@RequiresPermissions("loan:operatingRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsLoanOperatingRecord loanOperatingRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsLoanOperatingRecord> page = new Page<NfsLoanOperatingRecord>(request, response);
		page.setPageSize(10);
		page = loanOperatingRecordService.findPage(page, loanOperatingRecord); 
		model.addAttribute("page", page);
		model.addAttribute("loanRecord", loanOperatingRecord.getOldRecord());
		return "admin/loan/operatingRecord/loanOperatingRecordList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("loan:operatingRecord:view")
	@RequestMapping(value = "add")
	public String add(NfsLoanOperatingRecord loanOperatingRecord, Model model) {
		model.addAttribute("loanOperatingRecord", loanOperatingRecord);
		return "admin/loan/operatingRecord/loanOperatingRecordAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("loan:operatingRecord:view")
	@RequestMapping(value = "query")
	public String query(NfsLoanOperatingRecord loanOperatingRecord, Model model) {
		model.addAttribute("loanOperatingRecord", loanOperatingRecord);
		return "admin/loan/operatingRecord/loanOperatingRecordQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("loan:operatingRecord:view")
	@RequestMapping(value = "update")
	public String update(NfsLoanOperatingRecord loanOperatingRecord, Model model) {
		model.addAttribute("loanOperatingRecord", loanOperatingRecord);
		return "admin/loan/operatingRecord/loanOperatingRecordUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("loan:operatingRecord:edit")
	@RequestMapping(value = "save")
	public String save(NfsLoanOperatingRecord loanOperatingRecord, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, loanOperatingRecord)){
			return add(loanOperatingRecord, model);
		}
		loanOperatingRecordService.save(loanOperatingRecord);
		addMessage(redirectAttributes, "保存借条操作记录成功");
		return "redirect:"+Global.getAdminPath()+"/loanOperatingRecord/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("loan:operatingRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsLoanOperatingRecord loanOperatingRecord, RedirectAttributes redirectAttributes) {
		loanOperatingRecordService.delete(loanOperatingRecord);
		addMessage(redirectAttributes, "删除借条操作记录成功");
		return "redirect:"+Global.getAdminPath()+"/loanOperatingRecord/?repage";
	}

}