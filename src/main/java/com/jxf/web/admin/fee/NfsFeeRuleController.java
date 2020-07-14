package com.jxf.web.admin.fee;

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

import com.jxf.fee.entity.NfsFeeRule;
import com.jxf.fee.service.NfsFeeRuleService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;


/**
 * 收费规则Controller
 * @author wo
 * @version 2019-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/nfsFeeRule")
public class NfsFeeRuleController extends BaseController {

	@Autowired
	private NfsFeeRuleService feeRuleService;
	
	@ModelAttribute
	public NfsFeeRule get(@RequestParam(required=false) Long id) {
		NfsFeeRule entity = null;
		if (id!=null){
			entity = feeRuleService.get(id);
		}
		if (entity == null){
			entity = new NfsFeeRule();
		}
		return entity;
	}
	
	@RequiresPermissions("fee:nfsFeeRule:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsFeeRule nfsFeeRule, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsFeeRule> page = feeRuleService.findPage(new Page<NfsFeeRule>(request, response), nfsFeeRule); 
		model.addAttribute("page", page);
		return "admin/fee/rule/nfsFeeRuleList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("fee:nfsFeeRule:view")
	@RequestMapping(value = "add")
	public String add(NfsFeeRule nfsFeeRule, Model model) {
		model.addAttribute("nfsFeeRule", nfsFeeRule);
		return "admin/fee/rule/nfsFeeRuleAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("fee:nfsFeeRule:view")
	@RequestMapping(value = "query")
	public String query(NfsFeeRule nfsFeeRule, Model model) {
		model.addAttribute("nfsFeeRule", nfsFeeRule);
		return "admin/fee/rule/nfsFeeRuleQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("fee:nfsFeeRule:view")
	@RequestMapping(value = "update")
	public String update(NfsFeeRule nfsFeeRule, Model model) {
		model.addAttribute("nfsFeeRule", nfsFeeRule);
		return "admin/fee/rule/nfsFeeRuleUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("fee:nfsFeeRule:edit")
	@RequestMapping(value = "save")
	public String save(NfsFeeRule nfsFeeRule, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsFeeRule)){
			return add(nfsFeeRule, model);
		}
		feeRuleService.save(nfsFeeRule);
		addMessage(redirectAttributes, "保存收费规则成功");
		return "redirect:"+Global.getAdminPath()+"/nfsFeeRule/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("fee:nfsFeeRule:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsFeeRule nfsFeeRule, RedirectAttributes redirectAttributes) {
		feeRuleService.delete(nfsFeeRule);
		addMessage(redirectAttributes, "删除收费规则成功");
		return "redirect:"+Global.getAdminPath()+"/nfsFeeRule/?repage";
	}

}