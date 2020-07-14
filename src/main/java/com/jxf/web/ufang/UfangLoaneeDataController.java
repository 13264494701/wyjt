package com.jxf.web.ufang;

import com.jxf.nfs.constant.ActSubConstant;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.service.NfsActService;
import com.jxf.svc.config.Constant;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.StringUtils;
import com.jxf.ufang.entity.UfangBrn;
import com.jxf.ufang.entity.UfangLoaneeData;
import com.jxf.ufang.entity.UfangLoaneeDataOrder;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.service.UfangBrnService;
import com.jxf.ufang.service.UfangLoaneeDataOrderService;
import com.jxf.ufang.service.UfangLoaneeDataService;
import com.jxf.ufang.service.UfangUserActService;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 流量管理Controller
 *
 * @author wo
 * @version 2018-11-24
 */
@Controller("ufangLoaneeDataController")
@RequestMapping(value = "${ufangPath}/ufangLoaneeData")
public class UfangLoaneeDataController extends UfangBaseController {

    @Autowired
    private UfangLoaneeDataService ufangLoaneeDataService;
    @Autowired
    private UfangLoaneeDataOrderService ufangLoaneeDataOrderService;
    @Autowired
    private NfsActService actService;
    @Autowired
    private UfangUserActService ufangUserActService;
    @Autowired
    private UfangBrnService ufangBrnService;

    @ModelAttribute
    public UfangLoaneeData get(@RequestParam(required = false) Long id) {
        UfangLoaneeData entity = null;
        if (id != null) {
            entity = ufangLoaneeDataService.get(id);
        }
        if (entity == null) {
            entity = new UfangLoaneeData();
        }
        return entity;
    }

    @RequiresPermissions("data:ufangLoaneeData:view")
    @RequestMapping(value = {"list", ""})
    public String list(UfangLoaneeData ufangLoaneeData, HttpServletRequest request, HttpServletResponse response, Model model) {
        UfangUser ufangUser = UfangUserUtils.getUser();
        ufangLoaneeData.setStatus(UfangLoaneeData.Status.fresh);
        ufangLoaneeData.setEmpNo(ufangUser.getEmpNo().substring(0, 4));
        if(StringUtils.equals(ufangLoaneeData.getProdCode(), "001")) {
        	Date beginApplyTime = CalendarUtil.addDay(new Date(), -7);
        	ufangLoaneeData.setBeginApplyTime(beginApplyTime);
        	ufangLoaneeData.setEndApplyTime(new Date());
        }
        Page<UfangLoaneeData> page = ufangLoaneeDataService.findPageByEmpNo(new Page<UfangLoaneeData>(request, response), ufangLoaneeData);
        List<UfangLoaneeData> ufangLoaneeDataList = page.getList();
        for (int i = 0; i < ufangLoaneeDataList.size(); i++) {
            UfangLoaneeData loaneeData = ufangLoaneeDataList.get(i);
            if (loaneeData.getBought() == null) {
                loaneeData.setName(StringUtils.replacePattern(loaneeData.getName(), "(?<=[\u4e00-\u9fa5]{1})[\u4e00-\u9fa5]", "*"));
                loaneeData.setPhoneNo(StringUtils.replacePattern(loaneeData.getPhoneNo(), "(?<=[\\d]{2})\\d(?=[\\d]{2})", "*"));
                loaneeData.setIdNo(StringUtils.replacePattern(loaneeData.getIdNo(), "(?<=[\\d]{2})\\d(?=[\\d]{2})", "*"));
                loaneeData.setQqNo(StringUtils.replacePattern(loaneeData.getQqNo(), "(?<=[\\d]{1})\\d(?=[\\d]{1})", "*"));
                loaneeData.setWeixinNo(StringUtils.replacePattern(loaneeData.getWeixinNo(), "(?<=[\\w]{1})\\w(?=[\\w]{1})", "*"));
                ufangLoaneeDataList.set(i, loaneeData);
            }
        }
        model.addAttribute("page", page);
		model.addAttribute("channel", ufangLoaneeData.getChannel());
		model.addAttribute("prodCode", ufangLoaneeData.getProdCode());
        return "ufang/data/ufangLoaneeDataList";
    }

    /**
     * 添加页面跳转
     */
    @RequiresPermissions("data:ufangLoaneeData:view")
    @RequestMapping(value = "add")
    public String add(UfangLoaneeData ufangLoaneeData, Model model) {
        model.addAttribute("ufangLoaneeData", ufangLoaneeData);
        return "ufang/data/ufangLoaneeDataAdd";
    }

    /**
     * 查看页面跳转
     */
    @RequiresPermissions("data:ufangLoaneeData:view")
    @RequestMapping(value = "query")
    public String query(UfangLoaneeData ufangLoaneeData, Model model) {

        UfangLoaneeData u = ufangLoaneeDataService.get(ufangLoaneeData);
        if (u.getBought() == null) {
            u.setName(StringUtils.replacePattern(u.getName(), "(?<=[\u4e00-\u9fa5]{1})[\u4e00-\u9fa5]", "*"));
            u.setPhoneNo(StringUtils.replacePattern(u.getPhoneNo(), "(?<=[\\d]{2})\\d(?=[\\d]{2})", "*"));
            u.setIdNo(StringUtils.replacePattern(u.getIdNo(), "(?<=[\\d]{2})\\d(?=[\\d]{2})", "*"));
            u.setQqNo(StringUtils.replacePattern(u.getQqNo(), "(?<=[\\d]{1})\\d(?=[\\d]{1})", "*"));
            u.setWeixinNo(StringUtils.replacePattern(u.getWeixinNo(), "(?<=[\\w]{1})\\w(?=[\\w]{1})", "*"));
        }
        model.addAttribute("ufangLoaneeData", u);
        return "ufang/data/ufangLoaneeDataQuery";
    }

    /**
     * 修改页面跳转
     */
    @RequiresPermissions("data:ufangLoaneeData:view")
    @RequestMapping(value = "update")
    public String update(UfangLoaneeData ufangLoaneeData, Model model) {
        model.addAttribute("ufangLoaneeData", ufangLoaneeData);
        return "ufang/data/ufangLoaneeDataUpdate";
    }

    /**
     * 新增与修改的提交
     */
    @RequiresPermissions("data:ufangLoaneeData:edit")
    @RequestMapping(value = "save")
    public String save(UfangLoaneeData ufangLoaneeData, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, ufangLoaneeData)) {
            return add(ufangLoaneeData, model);
        }
        ufangLoaneeDataService.save(ufangLoaneeData);
        addMessage(redirectAttributes, "保存流量管理成功");
        return "redirect:" + ufangPath + "/ufangLoaneeData/?repage";
    }

    /**
     * 真删除提交
     */
    @RequiresPermissions("data:ufangLoaneeData:edit")
    @RequestMapping(value = "delete")
    public String delete(UfangLoaneeData ufangLoaneeData, RedirectAttributes redirectAttributes) {
        ufangLoaneeDataService.delete(ufangLoaneeData);
        addMessage(redirectAttributes, "删除流量管理成功");
        return "redirect:" + ufangPath + "/ufangLoaneeData/?repage";
    }
   

    @RequiresPermissions("data:ufangLoaneeData:view")
    @RequestMapping(value = "buy")
    public String buy(Long id, String prodCode,RedirectAttributes redirectAttributes) {

        UfangUser ufangUser = UfangUserUtils.getUser();
        UfangLoaneeData ufangLoaneeData = ufangLoaneeDataService.get(id);
        //检查下公司其他人是否已购买该流量
        UfangLoaneeDataOrder dataOrder = new UfangLoaneeDataOrder();       
        dataOrder.setData(ufangLoaneeData);
        dataOrder.setUser(ufangUser);
        List<UfangLoaneeDataOrder> listByCompanyNo = ufangLoaneeDataOrderService.findListByCompanyNo(dataOrder);
        logger.info("订单数量："+listByCompanyNo.size());
        if(!Collections3.isEmpty(listByCompanyNo)) {
        	addMessage(redirectAttributes, "该流量已被公司其他员工购买，请勿重复购买");
        	return "redirect:/ufang/ufangLoaneeData?prodCode="+prodCode;
        }
        
        UfangBrn ufangBrn = ufangBrnService.get(ufangUser.getBrn().getParent());
        if (ufangBrn.getFreeData() > 0) {
            //免费流量减一
            ufangBrnService.updateFreeData(-1, ufangBrn.getBrnNo());
            UfangLoaneeDataOrder ufangLoaneeDataOrder = new UfangLoaneeDataOrder();
            ufangLoaneeDataOrder.setUser(ufangUser);
            ufangLoaneeDataOrder.setData(ufangLoaneeData);
            ufangLoaneeDataOrder.setDataGroup(UfangLoaneeDataOrder.Group.pendingFollow);
            ufangLoaneeDataOrder.setStatus(UfangLoaneeDataOrder.Status.success);
            ufangLoaneeDataOrder.setAmount(new BigDecimal("0"));
            ufangLoaneeDataOrderService.save(ufangLoaneeDataOrder);
            ufangLoaneeDataService.updatesales(ufangLoaneeData.getId());
            addMessage(redirectAttributes, "使用了1免费流量条数购买成功");
            return "redirect:/ufang/ufangLoaneeData?prodCode="+prodCode;
        }
        BigDecimal avlBal = ufangUserActService.getUserAct(ufangUser, ActSubConstant.UFANG_USER_AVL_BAL).getCurBal();
        if (avlBal.compareTo(ufangLoaneeData.getPrice()) < 0) {
            addMessage(redirectAttributes, "余额不足，请先充值");
            return "redirect:/ufang/ufangLoaneeData?prodCode="+prodCode;
        }
        UfangLoaneeDataOrder ufangLoaneeDataOrder = new UfangLoaneeDataOrder();
        ufangLoaneeDataOrder.setUser(ufangUser);
        ufangLoaneeDataOrder.setProdCode(ufangLoaneeData.getProdCode());
        ufangLoaneeDataOrder.setData(ufangLoaneeData);
        ufangLoaneeDataOrder.setDataGroup(UfangLoaneeDataOrder.Group.pendingFollow);
        ufangLoaneeDataOrder.setStatus(UfangLoaneeDataOrder.Status.success);
        ufangLoaneeDataOrder.setAmount(ufangLoaneeData.getPrice());
        ufangLoaneeDataOrderService.save(ufangLoaneeDataOrder);
        int code = actService.updateAct(TrxRuleConstant.UFANG_CONSUME, ufangLoaneeData.getPrice(), ufangUser, ufangLoaneeDataOrder.getId());
        if (code == Constant.UPDATE_FAILED) {
            addMessage(redirectAttributes, "账户处理失败");
            return "redirect:/ufang/ufangLoaneeData?prodCode="+prodCode;
        }
        ufangLoaneeDataService.updatesales(ufangLoaneeData.getId());
        addMessage(redirectAttributes, "使用了"+ufangLoaneeData.getPrice()+"元购买成功");

        return "redirect:/ufang/ufangLoaneeData?prodCode="+prodCode;
    }
    
    @RequiresPermissions("data:ufangLoaneeData:view")
    @RequestMapping(value = "batchBuy")
    @ResponseBody
    public String batchBuy(String[] buyList,String prodCode) {
    		
        UfangUser ufangUser = UfangUserUtils.getUser();
        
        //过滤本机构已购买的流量
        List<UfangLoaneeData> dataList = new ArrayList<UfangLoaneeData>();
        BigDecimal amount = BigDecimal.ZERO;
        for (int i = 0; i < buyList.length; i++) {
        	  
            UfangLoaneeData ufangLoaneeData = ufangLoaneeDataService.get(Long.parseLong(buyList[i]));
            //检查下公司其他人是否已购买该流量
            UfangLoaneeDataOrder dataOrder = new UfangLoaneeDataOrder();
            dataOrder.setData(ufangLoaneeData);
            dataOrder.setUser(ufangUser);
            List<UfangLoaneeDataOrder> listByCompanyNo = ufangLoaneeDataOrderService.findListByCompanyNo(dataOrder);
            if(!Collections3.isEmpty(listByCompanyNo)) {
            	continue;
            }
            amount = amount.add(ufangLoaneeData.getPrice());   
            dataList.add(ufangLoaneeData);
        }
           
        BigDecimal avlBal = ufangUserActService.getUserAct(ufangUser, ActSubConstant.UFANG_USER_AVL_BAL).getCurBal();
        
        if (avlBal.compareTo(amount) < 0) {
            return "余额不足";
        }
        
//        UfangBrn ufangBrn = ufangBrnService.get(ufangUser.getBrn().getParent());
//        Integer freeDataCnt = ufangBrn.getFreeData() >= dataList.size()?dataList.size():ufangBrn.getFreeData();
//        
//        ufangBrnService.updateFreeData(-freeDataCnt, ufangBrn.getBrnNo());  
        
        int code = actService.updateAct(TrxRuleConstant.UFANG_CONSUME, amount, ufangUser, 1L);
        if(code == Constant.UPDATE_FAILED) {
        	return "扣费失败";
        }
        //批量添加流量订单
        for (UfangLoaneeData ufangLoaneeData:dataList) {
            UfangLoaneeDataOrder ufangLoaneeDataOrder = new UfangLoaneeDataOrder();
            ufangLoaneeDataOrder.setUser(ufangUser);
            ufangLoaneeDataOrder.setProdCode(ufangLoaneeData.getProdCode());
            ufangLoaneeDataOrder.setData(ufangLoaneeData);
            ufangLoaneeDataOrder.setDataGroup(UfangLoaneeDataOrder.Group.pendingFollow);
            ufangLoaneeDataOrder.setStatus(UfangLoaneeDataOrder.Status.success);
            ufangLoaneeDataOrder.setAmount(ufangLoaneeData.getPrice());
            ufangLoaneeDataOrderService.save(ufangLoaneeDataOrder);
            ufangLoaneeDataService.updatesales(ufangLoaneeData.getId());
        }

//      return "批量购买" + dataList.size() + "条流量成功，使用了" + dataList.size() + "免费流量条数";
        return "批量购买" + dataList.size() + "条流量成功，花费" + StringUtils.decimalToStr(amount, 2) + "元";
    }

}