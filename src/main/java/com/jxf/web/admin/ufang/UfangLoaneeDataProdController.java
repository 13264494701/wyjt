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
import com.jxf.ufang.entity.UfangLoaneeDataProd;
import com.jxf.ufang.service.UfangLoaneeDataProdService;
import com.jxf.web.admin.sys.BaseController;

/**
 * 借款人数据产品表Controller
 * @author gaobo
 * @version 2019-07-19
 */
@Controller
@RequestMapping(value = "${adminPath}/ufang/ufangLoaneeDataProd")
public class UfangLoaneeDataProdController extends BaseController {

	@Autowired
	private UfangLoaneeDataProdService ufangLoaneeDataProdService;
	
	@ModelAttribute
	public UfangLoaneeDataProd get(@RequestParam(required=false) Long id) {
		UfangLoaneeDataProd entity = null;
		if (id!=null){
			entity = ufangLoaneeDataProdService.get(id);
		}
		if (entity == null){
			entity = new UfangLoaneeDataProd();
		}
		return entity;
	}
	
	@RequiresPermissions("ufang:ufangLoaneeDataProd:view")
	@RequestMapping(value = {"list", ""})
	public String list(UfangLoaneeDataProd ufangLoaneeDataProd, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UfangLoaneeDataProd> page = ufangLoaneeDataProdService.findPage(new Page<UfangLoaneeDataProd>(request, response), ufangLoaneeDataProd); 
		model.addAttribute("page", page);
		return "admin/ufang/loaneeDataProd/ufangLoaneeDataProdList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("ufang:ufangLoaneeDataProd:view")
	@RequestMapping(value = "add")
	public String add(UfangLoaneeDataProd ufangLoaneeDataProd, Model model) {
		model.addAttribute("ufangLoaneeDataProd", ufangLoaneeDataProd);
		return "admin/ufang/loaneeDataProd/ufangLoaneeDataProdAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("ufang:ufangLoaneeDataProd:view")
	@RequestMapping(value = "query")
	public String query(UfangLoaneeDataProd ufangLoaneeDataProd, Model model) {
		model.addAttribute("ufangLoaneeDataProd", ufangLoaneeDataProd);
		return "admin/ufang/loaneeDataProd/ufangLoaneeDataProdQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("ufang:ufangLoaneeDataProd:view")
	@RequestMapping(value = "update")
	public String update(UfangLoaneeDataProd ufangLoaneeDataProd, Model model) {
		model.addAttribute("ufangLoaneeDataProd", ufangLoaneeDataProd);
		return "admin/ufang/loaneeDataProd/ufangLoaneeDataProdUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("ufang:ufangLoaneeDataProd:edit")
	@RequestMapping(value = "save")
	public String save(UfangLoaneeDataProd ufangLoaneeDataProd, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, ufangLoaneeDataProd)){
			return add(ufangLoaneeDataProd, model);
		}
		ufangLoaneeDataProdService.save(ufangLoaneeDataProd);
		addMessage(redirectAttributes, "保存借款人数据产品表成功");
		return "redirect:"+Global.getAdminPath()+"/ufang/loaneeDataProd/ufangLoaneeDataProd/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("ufang:ufangLoaneeDataProd:edit")
	@RequestMapping(value = "delete")
	public String delete(UfangLoaneeDataProd ufangLoaneeDataProd, RedirectAttributes redirectAttributes) {
		ufangLoaneeDataProdService.delete(ufangLoaneeDataProd);
		addMessage(redirectAttributes, "删除借款人数据产品表成功");
		return "redirect:"+Global.getAdminPath()+"/ufang/loaneeDataProd/ufangLoaneeDataProd/?repage";
	}

}