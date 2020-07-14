package com.jxf.web.admin.report;


import com.jxf.report.entity.ReportLoanAmtDaily;
import com.jxf.report.entity.ReportLoanDaily;
import com.jxf.report.entity.ReportLoaneeDataDaily;
import com.jxf.report.entity.ReportMemberDaily;
import com.jxf.report.entity.ReportPayRefundDaily;
import com.jxf.report.service.ReportLoanAmtDailyService;
import com.jxf.report.service.ReportLoanDailyService;
import com.jxf.report.service.ReportLoaneeDataDailyService;
import com.jxf.report.service.ReportMemberDailyService;
import com.jxf.report.service.ReportPayRefundDailyService;
import com.jxf.svc.config.Global;
import com.jxf.svc.utils.DateUtils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 报表生成Controller
 *
 * @author Administrator
 * @version 2018-09-06
 */
@Controller
@RequestMapping(value = "${adminPath}/report")
public class ReportController {


    @Autowired
    private ReportMemberDailyService reportMemberDailyService;
    @Autowired
    private ReportLoanDailyService reportLoanDailyService;
    @Autowired
    private ReportLoaneeDataDailyService loaneeDataDailyService;
    @Autowired
    private ReportLoanAmtDailyService reportLoanAmtDailyService;
    @Autowired
    private ReportPayRefundDailyService reportPayRefundDailyService;

    @RequestMapping(value = "/generate")
    public String generate(String date) {
        int daysAgo = (int) DateUtils.pastDays(DateUtils.parseDate(date));
        if (daysAgo > 0) {

            //用户
            ReportMemberDaily reportMemberDaily = reportMemberDailyService.memberCount(daysAgo);
            reportMemberDailyService.deleteByDate(reportMemberDaily.getDate());
            reportMemberDailyService.save(reportMemberDaily);
            
            //借条
            List<ReportLoanDaily> reportLoanDailyList = reportLoanDailyService.loanCount(daysAgo);
            if(reportLoanDailyList!=null &&reportLoanDailyList.size()>0) {
                reportLoanDailyService.deleteByDate(reportLoanDailyList.get(0).getDate());
                for(ReportLoanDaily reportLoanDaily:reportLoanDailyList ){
                    reportLoanDailyService.save(reportLoanDaily);	
                }
            }
            
            //借款人数据
            ReportLoaneeDataDaily loaneeDataDaily = loaneeDataDailyService.dataCount(daysAgo);
            loaneeDataDailyService.deleteByDate(loaneeDataDaily.getDate());
            loaneeDataDailyService.save(loaneeDataDaily);
            
            //借款交易金额统计
            ReportLoanAmtDaily reportLoanAmtDaily = reportLoanAmtDailyService.loanAmtCount(daysAgo);
            reportLoanAmtDailyService.deleteByDate(reportLoanAmtDaily.getDate());
            reportLoanAmtDailyService.save(reportLoanAmtDaily);
            
            //缴费统计
            ReportPayRefundDaily reportPayDaily = reportPayRefundDailyService.payCount(daysAgo);
            reportPayRefundDailyService.deleteByDate(reportPayDaily.getDate());
            reportPayRefundDailyService.save(reportPayDaily);
            
            //退费统计
            ReportPayRefundDaily reportRefundDaily = reportPayRefundDailyService.refundCount(daysAgo);
            reportPayRefundDailyService.save(reportRefundDaily);


            return "redirect:" + Global.getAdminPath() + "/report?msg=%e6%88%90%e5%8a%9f";
        }
        return "redirect:" + Global.getAdminPath() + "/report?msg=%e5%8f%aa%e8%83%bd%e7%94%9f%e6%88%90%e6%98%a8%e5%a4%a9%e4%b9%8b%e5%89%8d%e7%9a%84%e6%8a%a5%e8%a1%a8";
    }

    @RequestMapping(value = "")
    public String report(String msg, Model model) {
        model.addAttribute("msg", msg);
        return "admin/report/generate";
    }

}