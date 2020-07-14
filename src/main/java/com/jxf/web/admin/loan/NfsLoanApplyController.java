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

import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.service.NfsLoanApplyDetailService;
import com.jxf.loan.service.NfsLoanApplyService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;


/**
 * 借款申请Controller
 * @author wo
 * @version 2018-09-26
 */
@Controller
@RequestMapping(value = "${adminPath}/loanApply")
public class NfsLoanApplyController extends BaseController {

	@Autowired
	private NfsLoanApplyService loanApplyService;
	@Autowired
	private NfsLoanApplyDetailService loanApplyDetailService;
	
	@ModelAttribute
	public NfsLoanApply get(@RequestParam(required=false) Long id) {
		NfsLoanApply entity = null;
		if (id!=null){
			entity = loanApplyService.get(id);
		}
		if (entity == null){
			entity = new NfsLoanApply();
		}
		return entity;
	}
	
	@RequiresPermissions("loan:loanApply:view")
	@RequestMapping(value = "singleLoanApplyList")
	public String singleLoanApplyList(NfsLoanApply nfsLoanApply, HttpServletRequest request, HttpServletResponse response, Model model) {
		

		Page<NfsLoanApply> page = loanApplyService.findSingleLoanApplyPage(new Page<NfsLoanApply>(request, response), nfsLoanApply); 
		model.addAttribute("page", page);
		return "admin/loan/apply/singleLoanApplyList";
	}
	
	@RequiresPermissions("loan:loanApply:view")
	@RequestMapping(value = "multipleLoanApplyList")
	public String multipleLoanApplyList(NfsLoanApply nfsLoanApply, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Page<NfsLoanApply> page = loanApplyService.findMultipleLoanApplyPage(new Page<NfsLoanApply>(request, response), nfsLoanApply); 
		model.addAttribute("page", page);
		return "admin/loan/apply/multipleLoanApplyList";
	}
	
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("loan:loanApply:view")
	@RequestMapping(value = "add")
	public String add(NfsLoanApply nfsLoanApply, Model model) {
		model.addAttribute("nfsLoanApply", nfsLoanApply);
		return "admin/loan/apply/loan/nfsLoanApplyAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("loan:loanApply:view")
	@RequestMapping(value = "query")
	public String query(NfsLoanApply nfsLoanApply, Model model) {
		nfsLoanApply.setDetail(loanApplyDetailService.get(nfsLoanApply.getDetail()));
		model.addAttribute("nfsLoanApply", nfsLoanApply);
		return "admin/loan/apply/nfsLoanApplyQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("loan:loanApply:view")
	@RequestMapping(value = "update")
	public String update(NfsLoanApply nfsLoanApply, Model model) {
		model.addAttribute("nfsLoanApply", nfsLoanApply);
		return "admin/loan/apply/nfsLoanApplyUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("loan:loanApply:edit")
	@RequestMapping(value = "save")
	public String save(NfsLoanApply nfsLoanApply, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsLoanApply)){
			return add(nfsLoanApply, model);
		}
		loanApplyService.save(nfsLoanApply);
		addMessage(redirectAttributes, "保存借款申请成功");
		return "redirect:"+Global.getAdminPath()+"/loanApply/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("loan:loanApply:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsLoanApply nfsLoanApply, RedirectAttributes redirectAttributes) {
		loanApplyService.delete(nfsLoanApply);
		addMessage(redirectAttributes, "删除借款申请成功");
		return "redirect:"+Global.getAdminPath()+"/loanApply/?repage";
	}
}