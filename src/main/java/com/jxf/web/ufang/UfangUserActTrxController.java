package com.jxf.web.ufang;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jxf.ufang.entity.UfangUser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.jxf.svc.persistence.Page;

import com.jxf.ufang.entity.UfangUserActTrx;
import com.jxf.ufang.service.UfangUserActTrxService;



/**
 * 账户交易Controller
 * @author jinxinfu
 * @version 2018-07-01
 */
@Controller("ufangUserActTrxController")
@RequestMapping(value = "${ufangPath}/userActTrx")
public class UfangUserActTrxController extends UfangBaseController {

	@Autowired
	private UfangUserActTrxService ufangUserActTrxService;
	
	@ModelAttribute
	public UfangUserActTrx get(@RequestParam(required=false) Long id) {
		UfangUserActTrx entity = null;
		if (id!=null){
			entity = ufangUserActTrxService.get(id);
		}
		if (entity == null){
			entity = new UfangUserActTrx();
		}
		return entity;
	}
	
	@RequiresPermissions("ufang:userActTrx:view")
	@RequestMapping(value = {"list", ""})
	public String list(UfangUserActTrx ufangUserActTrx, HttpServletRequest request, HttpServletResponse response, Model model, Long id) {
		Page<UfangUserActTrx> page = new Page<UfangUserActTrx>(request, response);
		page.setPageSize(10);
		UfangUser user = new UfangUser();
		user.setId(id);
		ufangUserActTrx.setUser(user);
		page = ufangUserActTrxService.findPage(page, ufangUserActTrx);
		model.addAttribute("page", page);
		model.addAttribute("id", id);
		return "ufang/user/actTrx/userActTrxList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("ufang:userActTrx:view")
	@RequestMapping(value = "add")
	public String add(UfangUserActTrx ufangUserActTrx, Model model) {
		model.addAttribute("ufangUserActTrx", ufangUserActTrx);
		return "brn/trx/ufangUserActTrxAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("ufang:userActTrx:view")
	@RequestMapping(value = "query")
	public String query(UfangUserActTrx ufangUserActTrx, Model model) {
		model.addAttribute("ufangUserActTrx", ufangUserActTrx);
		return "brn/trx/ufangUserActTrxQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("ufang:userActTrx:view")
	@RequestMapping(value = "update")
	public String update(UfangUserActTrx ufangUserActTrx, Model model) {
		model.addAttribute("ufangUserActTrx", ufangUserActTrx);
		return "brn/trx/ufangUserActTrxUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("ufang:userActTrx:edit")
	@RequestMapping(value = "save")
	public String save(UfangUserActTrx ufangUserActTrx, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, ufangUserActTrx)){
			return add(ufangUserActTrx, model);
		}
		ufangUserActTrxService.save(ufangUserActTrx);
		addMessage(redirectAttributes, "保存账户交易成功");
		return "redirect:"+ufangPath+"/ufangUserActTrx/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("ufang:userActTrx:edit")
	@RequestMapping(value = "delete")
	public String delete(UfangUserActTrx ufangUserActTrx, RedirectAttributes redirectAttributes) {
		ufangUserActTrxService.delete(ufangUserActTrx);
		addMessage(redirectAttributes, "删除账户交易成功");
		return "redirect:"+ufangPath+"/ufangUserActTrx/?repage";
	}

}