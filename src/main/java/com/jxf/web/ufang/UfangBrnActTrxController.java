package com.jxf.web.ufang;

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


import com.jxf.svc.persistence.Page;

import com.jxf.ufang.entity.UfangBrnActTrx;
import com.jxf.ufang.service.UfangBrnActTrxService;
import com.jxf.ufang.util.UfangUserUtils;



/**
 * 账户交易Controller
 * @author jinxinfu
 * @version 2018-07-01
 */
@Controller("ufangBrnActTrxController")
@RequestMapping(value = "${ufangPath}/brnActTrx")
public class UfangBrnActTrxController extends UfangBaseController {

	@Autowired
	private UfangBrnActTrxService brnActTrxService;
	
	@ModelAttribute
	public UfangBrnActTrx get(@RequestParam(required=false) Long id) {
		UfangBrnActTrx entity = null;
		if (id!=null){
			entity = brnActTrxService.get(id);
		}
		if (entity == null){
			entity = new UfangBrnActTrx();
		}
		return entity;
	}
	
	@RequiresPermissions("trx:brnActTrx:view")
	@RequestMapping(value = {"list", ""})
	public String list(UfangBrnActTrx ufangBrnActTrx, HttpServletRequest request, HttpServletResponse response, Model model) {
		ufangBrnActTrx.getSqlMap().put("dsf", UfangUserUtils.dataScopeFilter("c", ""));
		Page<UfangBrnActTrx> page = new Page<UfangBrnActTrx>(request, response);
		page.setPageSize(10);
		page = brnActTrxService.findPage(new Page<UfangBrnActTrx>(request, response), ufangBrnActTrx); 
		model.addAttribute("page", page);
		model.addAttribute("ufangBrnActTrx", ufangBrnActTrx);
		return "ufang/brn/actTrx/brnActTrxList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("trx:brnActTrx:view")
	@RequestMapping(value = "add")
	public String add(UfangBrnActTrx brnActTrx, Model model) {
		model.addAttribute("brnActTrx", brnActTrx);
		return "ufang/brn/actTrx/brnActTrxAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("trx:brnActTrx:view")
	@RequestMapping(value = "query")
	public String query(UfangBrnActTrx brnActTrx, Model model) {
		model.addAttribute("brnActTrx", brnActTrx);
		return "ufang/brn/actTrx/brnActTrxQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("trx:brnActTrx:view")
	@RequestMapping(value = "update")
	public String update(UfangBrnActTrx brnActTrx, Model model) {
		model.addAttribute("brnActTrx", brnActTrx);
		return "ufang/brn/actTrx/brnActTrxUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("trx:brnActTrx:edit")
	@RequestMapping(value = "save")
	public String save(UfangBrnActTrx brnActTrx, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, brnActTrx)){
			return add(brnActTrx, model);
		}
		brnActTrxService.save(brnActTrx);
		addMessage(redirectAttributes, "保存账户交易成功");
		return "redirect:"+ufangPath+"/brnActTrx/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("trx:brnActTrx:edit")
	@RequestMapping(value = "delete")
	public String delete(UfangBrnActTrx brnActTrx, RedirectAttributes redirectAttributes) {
		brnActTrxService.delete(brnActTrx);
		addMessage(redirectAttributes, "删除账户交易成功");
		return "redirect:"+ufangPath+"/brnActTrx/?repage";
	}

}