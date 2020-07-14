package com.jxf.web.admin.nfs;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.nfs.entity.NfsTrxRule;
import com.jxf.nfs.entity.NfsTrxRuleItem;
import com.jxf.nfs.service.NfsTrxRuleItemService;
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
@RequestMapping(value = "${adminPath}/nfsTrxRuleItem")
public class NfsTrxRuleItemController extends BaseController {

	@Autowired
	private NfsTrxRuleService nfsTrxRuleService;
	@Autowired
	private NfsTrxRuleItemService nfsTrxRuleItemService;
	
	@ModelAttribute
	public NfsTrxRuleItem get(@RequestParam(required=false) Long id) {
		NfsTrxRuleItem entity = null;
		if (id!=null){
			entity = nfsTrxRuleItemService.get(id);
		}
		if (entity == null){
			entity = new NfsTrxRuleItem();
		}
		return entity;
	}
	
	@RequiresPermissions("trx:nfsTrxRuleItem:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsTrxRuleItem nfsTrxRuleItem, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsTrxRuleItem> page = nfsTrxRuleItemService.findPage(new Page<NfsTrxRuleItem>(request, response), nfsTrxRuleItem); 
		model.addAttribute("page", page);
		model.addAttribute("trxCode", nfsTrxRuleItem.getTrxCode());
		return "admin/nfs/trxRule/item/nfsTrxRuleItemList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("trx:nfsTrxRuleItem:view")
	@RequestMapping(value = "add")
	public String add(NfsTrxRuleItem nfsTrxRuleItem, Model model) {
		NfsTrxRule trxRule = nfsTrxRuleService.getByTrxCode(nfsTrxRuleItem.getTrxCode());
		nfsTrxRuleItem.setName(trxRule.getName());
		model.addAttribute("nfsTrxRuleItem", nfsTrxRuleItem);
		return "admin/nfs/trxRule/item/nfsTrxRuleItemAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("trx:nfsTrxRuleItem:view")
	@RequestMapping(value = "query")
	public String query(NfsTrxRuleItem nfsTrxRuleItem, Model model) {
		NfsTrxRule trxRule = nfsTrxRuleService.getByTrxCode(nfsTrxRuleItem.getTrxCode());
		nfsTrxRuleItem.setName(trxRule.getName());
		model.addAttribute("nfsTrxRuleItem", nfsTrxRuleItem);
		return "admin/nfs/trxRule/item/nfsTrxRuleItemQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("trx:nfsTrxRuleItem:view")
	@RequestMapping(value = "update")
	public String update(NfsTrxRuleItem nfsTrxRuleItem, Model model) {
		NfsTrxRule trxRule = nfsTrxRuleService.getByTrxCode(nfsTrxRuleItem.getTrxCode());
		nfsTrxRuleItem.setName(trxRule.getName());
		nfsTrxRuleItem.setExpression(StringEscapeUtils.escapeHtml4(nfsTrxRuleItem.getExpression()));
		model.addAttribute("nfsTrxRuleItem", nfsTrxRuleItem);
		return "admin/nfs/trxRule/item/nfsTrxRuleItemUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("trx:nfsTrxRuleItem:edit")
	@RequestMapping(value = "save")
	public String save(NfsTrxRuleItem nfsTrxRuleItem, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsTrxRuleItem)){
			return add(nfsTrxRuleItem, model);
		}
		nfsTrxRuleItem.setExpression(StringEscapeUtils.unescapeHtml4(nfsTrxRuleItem.getExpression()));
		nfsTrxRuleItemService.save(nfsTrxRuleItem);
		addMessage(redirectAttributes, "保存交易规则成功");
		return "redirect:"+Global.getAdminPath()+"/nfsTrxRuleItem/?repage&trxCode="+nfsTrxRuleItem.getTrxCode();
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("trx:nfsTrxRuleItem:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsTrxRuleItem nfsTrxRuleItem, RedirectAttributes redirectAttributes) {
		nfsTrxRuleItemService.delete(nfsTrxRuleItem);
		addMessage(redirectAttributes, "删除交易规则成功");
		return "redirect:"+Global.getAdminPath()+"/nfsTrxRuleItem/?repage&trxCode="+nfsTrxRuleItem.getTrxCode();
	}

}