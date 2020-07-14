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

import com.jxf.loan.entity.NfsLoanContract;

import com.jxf.loan.service.NfsLoanContractService;

import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;

import com.jxf.web.admin.sys.BaseController;

/**
 * 合同表Controller
 * @author lmy
 * @version 2019-01-08
 */
@Controller
@RequestMapping(value = "${adminPath}/contract/nfsLoanContract")
public class NfsLoanContractController extends BaseController {

	@Autowired
	private NfsLoanContractService loanContractService;

	
	@ModelAttribute
	public NfsLoanContract get(@RequestParam(required=false) Long id) {
		NfsLoanContract entity = null;
		if (id!=null){
			entity = loanContractService.get(id);
		}
		if (entity == null){
			entity = new NfsLoanContract();
		}
		return entity;
	}
	
	@RequiresPermissions("contract:nfsLoanContract:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsLoanContract nfsLoanContract, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsLoanContract> page = loanContractService.findPage(new Page<NfsLoanContract>(request, response), nfsLoanContract); 
		model.addAttribute("page", page);
		return "admin/loan/contract/nfsLoanContractList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("contract:nfsLoanContract:view")
	@RequestMapping(value = "add")
	public String add(NfsLoanContract nfsLoanContract, Model model) {
		model.addAttribute("nfsLoanContract", nfsLoanContract);
		return "admin/loan/contract/nfsLoanContractAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("contract:nfsLoanContract:view")
	@RequestMapping(value = "query")
	public String query(NfsLoanContract nfsLoanContract, Model model) {
		model.addAttribute("nfsLoanContract", nfsLoanContract);
		return "admin/loan/contract/nfsLoanContractQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("contract:nfsLoanContract:view")
	@RequestMapping(value = "update")
	public String update(NfsLoanContract nfsLoanContract, Model model) {
		model.addAttribute("nfsLoanContract", nfsLoanContract);
		return "admin/loan/contract/nfsLoanContractUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("contract:nfsLoanContract:edit")
	@RequestMapping(value = "save")
	public String save(NfsLoanContract nfsLoanContract, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsLoanContract)){
			return add(nfsLoanContract, model);
		}
		loanContractService.save(nfsLoanContract);
		addMessage(redirectAttributes, "保存合同表成功");
		return "redirect:"+Global.getAdminPath()+"admin/loan/contract/nfsLoanContract/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("contract:nfsLoanContract:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsLoanContract nfsLoanContract, RedirectAttributes redirectAttributes) {
		loanContractService.delete(nfsLoanContract);
		addMessage(redirectAttributes, "删除合同表成功");
		return "redirect:"+Global.getAdminPath()+"admin/loan/contract/nfsLoanContract/?repage";
	}		
}