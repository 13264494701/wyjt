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
import com.jxf.ufang.entity.UfangLoaneeDataOrder;
import com.jxf.ufang.service.UfangLoaneeDataOrderService;
import com.jxf.web.admin.sys.BaseController;

/**
 * 流量订单Controller
 * @author wo
 * @version 2018-11-24
 */
@Controller("adminUfangLoaneeDataOrderController")
@RequestMapping(value = "${adminPath}/ufangLoaneeDataOrder")
public class UfangLoaneeDataOrderController extends BaseController {

	@Autowired
	private UfangLoaneeDataOrderService ufangLoaneeDataOrderService;
	
	@ModelAttribute
	public UfangLoaneeDataOrder get(@RequestParam(required=false) Long id) {
		UfangLoaneeDataOrder entity = null;
		if (id!=null){
			entity = ufangLoaneeDataOrderService.get(id);
		}
		if (entity == null){
			entity = new UfangLoaneeDataOrder();
		}
		return entity;
	}
	
	@RequiresPermissions("order:ufangLoaneeDataOrder:view")
	@RequestMapping(value = {"list", ""})
	public String list(UfangLoaneeDataOrder ufangLoaneeDataOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UfangLoaneeDataOrder> page = ufangLoaneeDataOrderService.findPage(new Page<UfangLoaneeDataOrder>(request, response), ufangLoaneeDataOrder); 
		model.addAttribute("page", page);
		return "admin/ufang/order/ufangLoaneeDataOrderList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("order:ufangLoaneeDataOrder:view")
	@RequestMapping(value = "add")
	public String add(UfangLoaneeDataOrder ufangLoaneeDataOrder, Model model) {
		model.addAttribute("ufangLoaneeDataOrder", ufangLoaneeDataOrder);
		return "admin/ufang/order/ufangLoaneeDataOrderAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("order:ufangLoaneeDataOrder:view")
	@RequestMapping(value = "query")
	public String query(UfangLoaneeDataOrder ufangLoaneeDataOrder, Model model) {
		model.addAttribute("ufangLoaneeDataOrder", ufangLoaneeDataOrder);
		return "admin/ufang/order/ufangLoaneeDataOrderQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("order:ufangLoaneeDataOrder:view")
	@RequestMapping(value = "update")
	public String update(UfangLoaneeDataOrder ufangLoaneeDataOrder, Model model) {
		model.addAttribute("ufangLoaneeDataOrder", ufangLoaneeDataOrder);
		return "admin/ufang/order/ufangLoaneeDataOrderUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("order:ufangLoaneeDataOrder:edit")
	@RequestMapping(value = "save")
	public String save(UfangLoaneeDataOrder ufangLoaneeDataOrder, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, ufangLoaneeDataOrder)){
			return add(ufangLoaneeDataOrder, model);
		}
		ufangLoaneeDataOrderService.save(ufangLoaneeDataOrder);
		addMessage(redirectAttributes, "保存流量订单成功");
		return "redirect:"+Global.getAdminPath()+"/ufangLoaneeDataOrder/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("order:ufangLoaneeDataOrder:edit")
	@RequestMapping(value = "delete")
	public String delete(UfangLoaneeDataOrder ufangLoaneeDataOrder, RedirectAttributes redirectAttributes) {
		ufangLoaneeDataOrderService.delete(ufangLoaneeDataOrder);
		addMessage(redirectAttributes, "删除流量订单成功");
		return "redirect:"+Global.getAdminPath()+"/ufangLoaneeDataOrder/?repage";
	}

}