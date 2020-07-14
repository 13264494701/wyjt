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

import com.jxf.loan.entity.NfsCrContract;
import com.jxf.loan.service.NfsCrContractService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;

/**
 * 债转合同Controller
 * @author suHuimin
 * @version 2019-03-12
 */
@Controller
@RequestMapping(value = "${adminPath}/auction/nfsCrContract")
public class NfsCrContractController extends BaseController {

	@Autowired
	private NfsCrContractService nfsCrContractService;
	
	@ModelAttribute
	public NfsCrContract get(@RequestParam(required=false) Long id) {
		NfsCrContract entity = null;
		if (id!=null){
			entity = nfsCrContractService.get(id);
		}
		if (entity == null){
			entity = new NfsCrContract();
		}
		return entity;
	}
	
	@RequiresPermissions("auction:nfsCrContract:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsCrContract nfsCrContract, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsCrContract> page = nfsCrContractService.findPage(new Page<NfsCrContract>(request, response), nfsCrContract); 
		model.addAttribute("page", page);
		return "loan/auction/nfsCrContractList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("auction:nfsCrContract:view")
	@RequestMapping(value = "add")
	public String add(NfsCrContract nfsCrContract, Model model) {
		model.addAttribute("nfsCrContract", nfsCrContract);
		return "loan/auction/nfsCrContractAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("auction:nfsCrContract:view")
	@RequestMapping(value = "query")
	public String query(NfsCrContract nfsCrContract, Model model) {
		model.addAttribute("nfsCrContract", nfsCrContract);
		return "loan/auction/nfsCrContractQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("auction:nfsCrContract:view")
	@RequestMapping(value = "update")
	public String update(NfsCrContract nfsCrContract, Model model) {
		model.addAttribute("nfsCrContract", nfsCrContract);
		return "loan/auction/nfsCrContractUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("auction:nfsCrContract:edit")
	@RequestMapping(value = "save")
	public String save(NfsCrContract nfsCrContract, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsCrContract)){
			return add(nfsCrContract, model);
		}
		nfsCrContractService.save(nfsCrContract);
		addMessage(redirectAttributes, "保存债转合同成功");
		return "redirect:"+Global.getAdminPath()+"/auction/nfsCrContract/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("auction:nfsCrContract:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsCrContract nfsCrContract, RedirectAttributes redirectAttributes) {
		nfsCrContractService.delete(nfsCrContract);
		addMessage(redirectAttributes, "删除债转合同成功");
		return "redirect:"+Global.getAdminPath()+"/auction/nfsCrContract/?repage";
	}

}