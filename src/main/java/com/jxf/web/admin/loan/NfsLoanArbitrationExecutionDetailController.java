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

import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;
import com.jxf.loan.entity.NfsLoanArbitrationExecutionDetail;
import com.jxf.loan.service.NfsLoanArbitrationExecutionDetailService;

/**
 * 强执明细Controller
 * @author Administrator
 * @version 2018-12-27
 */
@Controller
@RequestMapping(value = "${adminPath}/loanArbitrationExecutionDetail")
public class NfsLoanArbitrationExecutionDetailController extends BaseController {

	@Autowired
	private NfsLoanArbitrationExecutionDetailService loanArbitrationExecutionDetailService;
	
	@ModelAttribute
	public NfsLoanArbitrationExecutionDetail get(@RequestParam(required=false) Long id) {
		NfsLoanArbitrationExecutionDetail entity = null;
		if (id!=null){
			entity = loanArbitrationExecutionDetailService.get(id);
		}
		if (entity == null){
			entity = new NfsLoanArbitrationExecutionDetail();
		}
		return entity;
	}
	
	@RequiresPermissions("loan:arbitrationExecution:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsLoanArbitrationExecutionDetail nfsLoanArbitrationExecutionDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsLoanArbitrationExecutionDetail> page = loanArbitrationExecutionDetailService.findPage(new Page<NfsLoanArbitrationExecutionDetail>(request, response), nfsLoanArbitrationExecutionDetail); 
		model.addAttribute("page", page);
		return "admin/loan/arbitrationExecution/detail/nfsLoanArbitrationExecutionDetailList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("loan:arbitrationExecution:view")
	@RequestMapping(value = "add")
	public String add(NfsLoanArbitrationExecutionDetail nfsLoanArbitrationExecutionDetail, Model model) {
		model.addAttribute("nfsLoanArbitrationExecutionDetail", nfsLoanArbitrationExecutionDetail);
		return "admin/loan/arbitrationExecution/detail/nfsLoanArbitrationExecutionDetailAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("loan:arbitrationExecution:view")
	@RequestMapping(value = "query")
	public String query(NfsLoanArbitrationExecutionDetail nfsLoanArbitrationExecutionDetail, Model model) {
		model.addAttribute("nfsLoanArbitrationExecutionDetail", nfsLoanArbitrationExecutionDetail);
		return "admin/loan/arbitrationExecution/detail/nfsLoanArbitrationExecutionDetailQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("loan:arbitrationExecution:view")
	@RequestMapping(value = "update")
	public String update(NfsLoanArbitrationExecutionDetail nfsLoanArbitrationExecutionDetail, Model model) {
		model.addAttribute("nfsLoanArbitrationExecutionDetail", nfsLoanArbitrationExecutionDetail);
		return "admin/loan/arbitrationExecution/detail/nfsLoanArbitrationExecutionDetailUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("loan:arbitrationExecution:edit")
	@RequestMapping(value = "save")
	public String save(NfsLoanArbitrationExecutionDetail nfsLoanArbitrationExecutionDetail, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsLoanArbitrationExecutionDetail)){
			return add(nfsLoanArbitrationExecutionDetail, model);
		}
		loanArbitrationExecutionDetailService.save(nfsLoanArbitrationExecutionDetail);
		addMessage(redirectAttributes, "保存强执明细成功");
		return "redirect:"+Global.getAdminPath()+"/loanArbitrationExecutionDetail/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("loan:arbitrationExecution:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsLoanArbitrationExecutionDetail nfsLoanArbitrationExecutionDetail, RedirectAttributes redirectAttributes) {
		loanArbitrationExecutionDetailService.delete(nfsLoanArbitrationExecutionDetail);
		addMessage(redirectAttributes, "删除强执明细成功");
		return "redirect:"+Global.getAdminPath()+"/loanArbitrationExecutionDetail/?repage";
	}

}