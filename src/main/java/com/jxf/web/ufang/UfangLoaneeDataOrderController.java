package com.jxf.web.ufang;



import com.jxf.svc.excel.ExportExcel;
import com.jxf.svc.model.AjaxRsp;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.ufang.entity.LoaneeDataExport;
import com.jxf.ufang.entity.UfangLoaneeDataOrder;
import com.jxf.ufang.service.UfangLoaneeDataOrderService;
import com.jxf.ufang.util.UfangUserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 流量订单Controller
 *
 * @author wo
 * @version 2018-11-24
 */
@Controller("ufangLoaneeDataOrderController")
@RequestMapping(value = "${ufangPath}/ufangLoaneeDataOrder")
public class UfangLoaneeDataOrderController extends UfangBaseController {

    @Autowired
    private UfangLoaneeDataOrderService ufangLoaneeDataOrderService;

    @ModelAttribute
    public UfangLoaneeDataOrder get(@RequestParam(required = false) Long id) {
        UfangLoaneeDataOrder entity = null;
        if (id != null) {
            entity = ufangLoaneeDataOrderService.get(id);
        }
        if (entity == null) {
            entity = new UfangLoaneeDataOrder();
        }
        return entity;
    }

    @RequiresPermissions("order:ufangLoaneeDataOrder:view")
    @RequestMapping(value = {"list", ""})
    public String list(UfangLoaneeDataOrder ufangLoaneeDataOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
        ufangLoaneeDataOrder.getSqlMap().put("dsf", UfangUserUtils.dataScopeFilter("e", "a"));
        Page<UfangLoaneeDataOrder> page = ufangLoaneeDataOrderService.findPage(new Page<UfangLoaneeDataOrder>(request, response), ufangLoaneeDataOrder);
        model.addAttribute("page", page);
        return "ufang/order/ufangLoaneeDataOrderList";
    }


    /**
     * 添加页面跳转
     */
    @RequiresPermissions("order:ufangLoaneeDataOrder:view")
    @RequestMapping(value = "add")
    public String add(UfangLoaneeDataOrder ufangLoaneeDataOrder, Model model) {
        model.addAttribute("ufangLoaneeDataOrder", ufangLoaneeDataOrder);
        return "ufang/order/ufangLoaneeDataOrderAdd";
    }

    /**
     * 查看页面跳转
     */
    @RequiresPermissions("order:ufangLoaneeDataOrder:view")
    @RequestMapping(value = "query")
    public String query(UfangLoaneeDataOrder ufangLoaneeDataOrder, Model model) {
        model.addAttribute("ufangLoaneeDataOrder", ufangLoaneeDataOrder);
        return "ufang/order/ufangLoaneeDataOrderQuery";
    }

    /**
     * 修改页面跳转
     */
    @RequiresPermissions("order:ufangLoaneeDataOrder:view")
    @RequestMapping(value = "update")
    public String update(UfangLoaneeDataOrder ufangLoaneeDataOrder, Model model) {
        model.addAttribute("ufangLoaneeDataOrder", ufangLoaneeDataOrder);
        return "ufang/order/ufangLoaneeDataOrderUpdate";
    }

    /**
     * 新增与修改的提交
     */
    @RequiresPermissions("order:ufangLoaneeDataOrder:edit")
    @RequestMapping(value = "save")
    public String save(UfangLoaneeDataOrder ufangLoaneeDataOrder, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, ufangLoaneeDataOrder)) {
            return add(ufangLoaneeDataOrder, model);
        }
        ufangLoaneeDataOrderService.save(ufangLoaneeDataOrder);
        addMessage(redirectAttributes, "保存流量订单成功");
        return "redirect:" + ufangPath + "/ufangLoaneeDataOrder/?repage";
    }

    /**
     * 真删除提交
     */
    @RequiresPermissions("order:ufangLoaneeDataOrder:edit")
    @RequestMapping(value = "delete")
    public String delete(UfangLoaneeDataOrder ufangLoaneeDataOrder, RedirectAttributes redirectAttributes) {
        ufangLoaneeDataOrderService.delete(ufangLoaneeDataOrder);
        addMessage(redirectAttributes, "删除流量订单成功");
        return "redirect:" + ufangPath + "/ufangLoaneeDataOrder/?repage";
    }

	/**
	 * 添加备注
	 */
	@RequestMapping("addDataGroupRmk")
	@RequiresPermissions("order:ufangLoaneeDataOrder:edit")
	@ResponseBody
	public AjaxRsp addRmk(Long id,UfangLoaneeDataOrder.Group dataGroup,String rmk){
		AjaxRsp rsp  = new AjaxRsp();
		UfangLoaneeDataOrder ufangLoaneeDataOrder = ufangLoaneeDataOrderService.get(id);
		ufangLoaneeDataOrder.setDataGroup(dataGroup);
		ufangLoaneeDataOrder.setRmk(rmk);
		ufangLoaneeDataOrderService.save(ufangLoaneeDataOrder);
		rsp.setMessage("添加分类备注成功");
		rsp.setStatus(true);
		return rsp;
	}
	
    @RequestMapping(value = "exportData")
    public String exportData(UfangLoaneeDataOrder ufangLoaneeDataOrder, HttpServletResponse response, RedirectAttributes redirectAttributes) {
    	
        if(ufangLoaneeDataOrder.getBeginTime()==null||ufangLoaneeDataOrder.getEndTime()==null) {
			addMessage(redirectAttributes, "起始时间和结束时间不能为空");
			return "redirect:"+ufangPath+"/ufangLoaneeDataOrder/?repage";
        }
		int days = DateUtils.getDifferenceOfTwoDate(ufangLoaneeDataOrder.getBeginTime(),ufangLoaneeDataOrder.getEndTime());
		if(Math.abs(days)>30){
			addMessage(redirectAttributes, "导出数据的时间跨度不能超过30天");
			return "redirect:"+ufangPath+"/ufangLoaneeDataOrder/?repage";
		}
    	
        ufangLoaneeDataOrder.getSqlMap().put("dsf", UfangUserUtils.dataScopeFilter("e", "a"));
        List<UfangLoaneeDataOrder> ufangLoaneeDataOrderList = ufangLoaneeDataOrderService.findListByEmpNo(ufangLoaneeDataOrder);
        List<LoaneeDataExport> loaneeDataExportList = new ArrayList<>();
        for (int i = 0; i < ufangLoaneeDataOrderList.size(); i++) {
            UfangLoaneeDataOrder o = ufangLoaneeDataOrderList.get(i);
            LoaneeDataExport l = new LoaneeDataExport();
            l.setName(o.getData().getName());
            l.setPhoneNo(o.getData().getPhoneNo());
            l.setAge(o.getData().getAge());
            l.setZhimafen(o.getData().getZhimafen()+"");
            l.setQqNo(o.getData().getQqNo());
            l.setWeixinNo(o.getData().getWeixinNo());
            l.setApplyAmount(o.getData().getApplyAmount());
            l.setPrice(StringUtils.decimalToStr(o.getAmount(), 2));
            l.setCreateTime(DateUtils.formatDate(o.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            l.setCreateUser(o.getUser().getEmpNam());
            loaneeDataExportList.add(l);
        }
        String fileName = "流量数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
        ExportExcel exportExcel = new ExportExcel(null, LoaneeDataExport.class);
        exportExcel.setDataList(loaneeDataExportList);
        try {
            exportExcel.write(response, fileName);
            exportExcel.dispose();
            return null;
        } catch (IOException e) {
        	addMessage(redirectAttributes, "导出数据失败！失败信息："+e.getMessage());
        }
        
        return "redirect:"+ufangPath+"/ufangLoaneeDataOrder/?repage";
    }
}