package com.jxf.web.admin.ufang;

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

import com.jxf.ufang.entity.UfangBrnActTrx;
import com.jxf.ufang.service.UfangBrnActTrxService;
import com.jxf.web.admin.sys.BaseController;


/**
 * 账户交易Controller
 * @author jinxinfu
 * @version 2018-07-01
 */
@Controller("adminUfangBrnActTrxController")
@RequestMapping(value = "${adminPath}/ufangBrnActTrx")
public class UfangBrnActTrxController extends BaseController {

	@Autowired
	private UfangBrnActTrxService nfsBrnActTrxService;
	
	@ModelAttribute
	public UfangBrnActTrx get(@RequestParam(required=false) Long id) {
		UfangBrnActTrx entity = null;
		if (id!=null){
			entity = nfsBrnActTrxService.get(id);
		}
		if (entity == null){
			entity = new UfangBrnActTrx();
		}
		return entity;
	}
	
	@RequiresPermissions("trx:nfsBrnActTrx:view")
	@RequestMapping(value = {"list", ""})
	public String list(UfangBrnActTrx ufangBrnActTrx, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UfangBrnActTrx> page = new Page<UfangBrnActTrx>(request, response);
		page.setPageSize(10);
		page = nfsBrnActTrxService.findPage(page, ufangBrnActTrx);
		model.addAttribute("page", page);
		model.addAttribute("ufangBrnActTrx", ufangBrnActTrx);
		return "admin/ufang/brn/actTrx/ufangBrnActTrxList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("trx:nfsBrnActTrx:view")
	@RequestMapping(value = "add")
	public String add(UfangBrnActTrx nfsBrnActTrx, Model model) {
		model.addAttribute("nfsBrnActTrx", nfsBrnActTrx);
		return "brn/trx/nfsBrnActTrxAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("trx:nfsBrnActTrx:view")
	@RequestMapping(value = "query")
	public String query(UfangBrnActTrx nfsBrnActTrx, Model model) {
		model.addAttribute("nfsBrnActTrx", nfsBrnActTrx);
		return "brn/trx/nfsBrnActTrxQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("trx:nfsBrnActTrx:view")
	@RequestMapping(value = "update")
	public String update(UfangBrnActTrx nfsBrnActTrx, Model model) {
		model.addAttribute("nfsBrnActTrx", nfsBrnActTrx);
		return "brn/trx/nfsBrnActTrxUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("trx:nfsBrnActTrx:edit")
	@RequestMapping(value = "save")
	public String save(UfangBrnActTrx nfsBrnActTrx, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsBrnActTrx)){
			return add(nfsBrnActTrx, model);
		}
		nfsBrnActTrxService.save(nfsBrnActTrx);
		addMessage(redirectAttributes, "保存账户交易成功");
		return "redirect:"+Global.getAdminPath()+"/nfsBrnActTrx/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("trx:nfsBrnActTrx:edit")
	@RequestMapping(value = "delete")
	public String delete(UfangBrnActTrx nfsBrnActTrx, RedirectAttributes redirectAttributes) {
		nfsBrnActTrxService.delete(nfsBrnActTrx);
		addMessage(redirectAttributes, "删除账户交易成功");
		return "redirect:"+Global.getAdminPath()+"/nfsBrnActTrx/?repage";
	}

}