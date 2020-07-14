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
import com.jxf.ufang.entity.UfangLoanMarket;
import com.jxf.ufang.service.UfangLoanMarketService;
import com.jxf.web.admin.sys.BaseController;


/**
 * 贷超管理Controller
 * @author wo
 * @version 2019-03-07
 */
@Controller
@RequestMapping(value = "${adminPath}/ufangLoanMarket")
public class UfangLoanMarketController extends BaseController {

	@Autowired
	private UfangLoanMarketService ufangLoanMarketService;
	
	@ModelAttribute
	public UfangLoanMarket get(@RequestParam(required=false) Long id) {
		UfangLoanMarket entity = null;
		if (id!=null){
			entity = ufangLoanMarketService.get(id);
		}
		if (entity == null){
			entity = new UfangLoanMarket();
		}
		return entity;
	}
	
	@RequiresPermissions("smarket:ufangLoanMarket:view")
	@RequestMapping(value = {"list", ""})
	public String list(UfangLoanMarket ufangLoanMarket, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UfangLoanMarket> page = ufangLoanMarketService.findPage(new Page<UfangLoanMarket>(request, response), ufangLoanMarket); 
		model.addAttribute("page", page);
		return "admin/ufang/smarket/ufangLoanMarketList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("smarket:ufangLoanMarket:view")
	@RequestMapping(value = "add")
	public String add(UfangLoanMarket ufangLoanMarket, Model model) {
		model.addAttribute("ufangLoanMarket", ufangLoanMarket);
		return "admin/ufang/smarket/ufangLoanMarketAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("smarket:ufangLoanMarket:view")
	@RequestMapping(value = "query")
	public String query(UfangLoanMarket ufangLoanMarket, Model model) {
		model.addAttribute("ufangLoanMarket", ufangLoanMarket);
		return "admin/ufang/smarket/ufangLoanMarketQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("smarket:ufangLoanMarket:view")
	@RequestMapping(value = "update")
	public String update(UfangLoanMarket ufangLoanMarket, Model model) {
		model.addAttribute("ufangLoanMarket", ufangLoanMarket);
		return "admin/ufang/smarket/ufangLoanMarketUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("smarket:ufangLoanMarket:edit")
	@RequestMapping(value = "save")
	public String save(UfangLoanMarket ufangLoanMarket, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, ufangLoanMarket)){
			return add(ufangLoanMarket, model);
		}
		ufangLoanMarketService.save(ufangLoanMarket);
		addMessage(redirectAttributes, "保存贷超管理成功");
		return "redirect:"+Global.getAdminPath()+"/ufangLoanMarket/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("smarket:ufangLoanMarket:edit")
	@RequestMapping(value = "delete")
	public String delete(UfangLoanMarket ufangLoanMarket, RedirectAttributes redirectAttributes) {
		ufangLoanMarketService.delete(ufangLoanMarket);
		addMessage(redirectAttributes, "删除贷超管理成功");
		return "redirect:"+Global.getAdminPath()+"/ufangLoanMarket/?repage";
	}

}