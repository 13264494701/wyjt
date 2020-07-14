package com.jxf.web.admin.ufang;




import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;


import com.jxf.ufang.entity.UfangLoaneeRcOrder;
import com.jxf.ufang.service.UfangLoaneeRcOrderService;
import com.jxf.ufang.util.UfangUserUtils;
import com.jxf.web.admin.sys.BaseController;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 风控订单Controller
 *
 * @author wo
 * @version 2019-08-24
 */
@Controller("adminPathLoaneeRcOrderController")
@RequestMapping(value = "${adminPath}/ufangLoaneeRcOrder")
public class UfangLoaneeRcOrderController extends BaseController {

    @Autowired
    private UfangLoaneeRcOrderService ufangLoaneeRcOrderService;

    @ModelAttribute
    public UfangLoaneeRcOrder get(@RequestParam(required = false) Long id) {
        UfangLoaneeRcOrder entity = null;
        if (id != null) {
            entity = ufangLoaneeRcOrderService.get(id);
        }
        if (entity == null) {
            entity = new UfangLoaneeRcOrder();
        }
        return entity;
    }

    @RequiresPermissions("order:ufangLoaneeRcOrder:view")
    @RequestMapping(value = {"list", ""})
    public String list(UfangLoaneeRcOrder ufangLoaneeRcOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
   
        Page<UfangLoaneeRcOrder> page = ufangLoaneeRcOrderService.findPage(new Page<UfangLoaneeRcOrder>(request, response), ufangLoaneeRcOrder);
        model.addAttribute("page", page);
        return "admin/ufang/order/rc/ufangLoaneeRcOrderList";
    }


    /**
     * 添加页面跳转
     */
    @RequiresPermissions("order:ufangLoaneeRcOrder:view")
    @RequestMapping(value = "add")
    public String add(UfangLoaneeRcOrder ufangLoaneeRcOrder, Model model) {
        model.addAttribute("ufangLoaneeRcOrder", ufangLoaneeRcOrder);
        return "ufang/order/ufangLoaneeRcOrderAdd";
    }

    /**
     * 查看页面跳转
     */
    @RequiresPermissions("order:ufangLoaneeRcOrder:view")
    @RequestMapping(value = "query")
    public String query(UfangLoaneeRcOrder ufangLoaneeRcOrder, Model model) {
        model.addAttribute("ufangLoaneeRcOrder", ufangLoaneeRcOrder);
        return "ufang/order/ufangLoaneeRcOrderQuery";
    }

    /**
     * 修改页面跳转
     */
    @RequiresPermissions("order:ufangLoaneeRcOrder:view")
    @RequestMapping(value = "update")
    public String update(UfangLoaneeRcOrder ufangLoaneeRcOrder, Model model) {
        model.addAttribute("ufangLoaneeRcOrder", ufangLoaneeRcOrder);
        return "ufang/order/ufangLoaneeRcOrderUpdate";
    }

    /**
     * 新增与修改的提交
     */
    @RequiresPermissions("order:ufangLoaneeRcOrder:edit")
    @RequestMapping(value = "save")
    public String save(UfangLoaneeRcOrder ufangLoaneeRcOrder, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, ufangLoaneeRcOrder)) {
            return add(ufangLoaneeRcOrder, model);
        }
        ufangLoaneeRcOrderService.save(ufangLoaneeRcOrder);
        addMessage(redirectAttributes, "保存流量订单成功");
        return "redirect:" + Global.getAdminPath() + "/ufangLoaneeRcOrder/?repage";
    }

    /**
     * 真删除提交
     */
    @RequiresPermissions("order:ufangLoaneeRcOrder:edit")
    @RequestMapping(value = "delete")
    public String delete(UfangLoaneeRcOrder ufangLoaneeRcOrder, RedirectAttributes redirectAttributes) {
        ufangLoaneeRcOrderService.delete(ufangLoaneeRcOrder);
        addMessage(redirectAttributes, "删除流量订单成功");
        return "redirect:" + Global.getAdminPath() + "/ufangLoaneeRcOrder/?repage";
    }


	

}