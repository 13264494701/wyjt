package com.jxf.web.admin.nfs;

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

import com.jxf.nfs.entity.NfsTrxRule;
import com.jxf.nfs.service.NfsTrxRuleService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;


/**
 * 交易规则Controller
 * @author wo
 * @version 2018-09-20
 */
@Controller
@RequestMapping(value = "${adminPath}/nfsTrxRule")
public class NfsTrxRuleController extends BaseController {

	@Autowired
	private NfsTrxRuleService nfsTrxRuleService;
	
	@ModelAttribute
	public NfsTrxRule get(@RequestParam(required=false) Long id) {
		NfsTrxRule entity = null;
		if (id!=null){
			entity = nfsTrxRuleService.get(id);
		}
		if (entity == null){
			entity = new NfsTrxRule();
		}
		return entity;
	}
	
	@RequiresPermissions("trx:nfsTrxRule:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsTrxRule nfsTrxRule, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsTrxRule> page = nfsTrxRuleService.findPage(new Page<NfsTrxRule>(request, response), nfsTrxRule); 
		model.addAttribute("page", page);
		return "admin/nfs/trxRule/nfsTrxRuleList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("trx:nfsTrxRule:view")
	@RequestMapping(value = "add")
	public String add(NfsTrxRule nfsTrxRule, Model model) {
		model.addAttribute("nfsTrxRule", nfsTrxRule);
		return "admin/nfs/trxRule/nfsTrxRuleAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("trx:nfsTrxRule:view")
	@RequestMapping(value = "query")
	public String query(NfsTrxRule nfsTrxRule, Model model) {
		model.addAttribute("nfsTrxRule", nfsTrxRule);
		return "admin/nfs/trxRule/nfsTrxRuleQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("trx:nfsTrxRule:view")
	@RequestMapping(value = "update")
	public String update(NfsTrxRule nfsTrxRule, Model model) {
		model.addAttribute("nfsTrxRule", nfsTrxRule);
		return "admin/nfs/trxRule/nfsTrxRuleUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("trx:nfsTrxRule:edit")
	@RequestMapping(value = "save")
	public String save(NfsTrxRule nfsTrxRule, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsTrxRule)){
			return add(nfsTrxRule, model);
		}
		nfsTrxRuleService.save(nfsTrxRule);
		addMessage(redirectAttributes, "保存交易规则成功");
		return "redirect:"+Global.getAdminPath()+"/nfsTrxRule/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("trx:nfsTrxRule:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsTrxRule nfsTrxRule, RedirectAttributes redirectAttributes) {
		nfsTrxRuleService.delete(nfsTrxRule);
		addMessage(redirectAttributes, "删除交易规则成功");
		return "redirect:"+Global.getAdminPath()+"/nfsTrxRule/?repage";
	}

}