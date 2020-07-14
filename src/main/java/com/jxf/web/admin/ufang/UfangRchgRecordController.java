package com.jxf.web.admin.ufang;

import com.jxf.svc.persistence.Page;
import com.jxf.ufang.entity.UfangRchgRecord;
import com.jxf.ufang.service.UfangRchgRecordService;
import com.jxf.web.admin.sys.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * 优放充值记录Controller
 *
 * @author Administrator
 * @version 2018-12-07
 */
@Controller("adminUfangRchgRecordController")
@RequestMapping(value = "${adminPath}/recharge")
public class UfangRchgRecordController extends BaseController {

    @Autowired
    private UfangRchgRecordService ufangRchgRecordService;

    @RequiresPermissions("recharge:ufangRchgRecord:view")
    @RequestMapping(value = {"list", ""})
    public String list(UfangRchgRecord ufangRchgRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
        ufangRchgRecord.setStatus(UfangRchgRecord.Status.success);
        BigDecimal amount = new BigDecimal("0");
        Page<UfangRchgRecord> page = ufangRchgRecordService.findPage(new Page<UfangRchgRecord>(request, response), ufangRchgRecord);
        List<UfangRchgRecord> ufangRchgRecordList = page.getList();
        for (int i = 0; i < ufangRchgRecordList.size(); i++) {
            amount = amount.add(ufangRchgRecordList.get(i).getAmount());
        }
        model.addAttribute("amount", amount);
        model.addAttribute("page", page);
        return "admin/recharge/ufangRchgRecordList";
    }

}