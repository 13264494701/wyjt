package com.jxf.task.tasks;


import com.jxf.report.entity.ReportLoanAmtDaily;
import com.jxf.report.entity.ReportLoanApplyDaily;
import com.jxf.report.entity.ReportLoanDaily;
import com.jxf.report.entity.ReportLoaneeDataDaily;
import com.jxf.report.entity.ReportMemberActDaily;
import com.jxf.report.entity.ReportMemberDaily;
import com.jxf.report.entity.ReportPayRefundDaily;
import com.jxf.report.entity.ReportRchgDaily;
import com.jxf.report.entity.ReportTransferDaily;
import com.jxf.report.entity.ReportWdrlDaily;
import com.jxf.report.service.ReportLoanAmtDailyService;
import com.jxf.report.service.ReportLoanApplyDailyService;
import com.jxf.report.service.ReportLoanDailyService;
import com.jxf.report.service.ReportLoaneeDataDailyService;
import com.jxf.report.service.ReportMemberActDailyService;
import com.jxf.report.service.ReportMemberDailyService;
import com.jxf.report.service.ReportPayRefundDailyService;
import com.jxf.report.service.ReportRchgDailyService;
import com.jxf.report.service.ReportTransferDailyService;
import com.jxf.report.service.ReportWdrlDailyService;
import com.jxf.svc.utils.DateUtils;

import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 统计任务
 *
 * @author Administrator
 */
@DisallowConcurrentExecution
public class ReportTask implements Job {


    @Autowired
    private ReportMemberDailyService reportMemberDailyService;
    @Autowired
    private ReportMemberActDailyService reportMemberActDailyService;
    @Autowired
    private ReportLoanDailyService reportLoanDailyService;
    @Autowired
    private ReportLoaneeDataDailyService loaneeDataDailyService;
    
    @Autowired
    private ReportRchgDailyService rchgDailyService;
    
    
    @Autowired
    private ReportWdrlDailyService wdrlDailyService;
    
    @Autowired
    private ReportTransferDailyService transferDailyService;
    @Autowired
    private ReportLoanApplyDailyService reportLoanApplyDailyService;
    @Autowired
    private ReportLoanAmtDailyService reportLoanAmtDailyService;
    @Autowired
    private ReportPayRefundDailyService reportPayRefundDailyService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //用户
        ReportMemberDaily reportMemberDaily = reportMemberDailyService.memberCount(1);
        reportMemberDailyService.deleteByDate(reportMemberDaily.getDate());
        reportMemberDailyService.save(reportMemberDaily);
        //用户账户
        ReportMemberActDaily memberActDaily = new ReportMemberActDaily();
        memberActDaily.setDate(DateUtils.getDate("yyyy-MM-dd"));
        memberActDaily = reportMemberActDailyService.sumMemberAct(memberActDaily);
        reportMemberActDailyService.deleteByDate(memberActDaily.getDate());
        reportMemberActDailyService.save(memberActDaily);
        
        //借条
        List<ReportLoanDaily> reportLoanDailyList = reportLoanDailyService.loanCount(1);
        if(reportLoanDailyList!=null &&reportLoanDailyList.size()>0) {
            reportLoanDailyService.deleteByDate(reportLoanDailyList.get(0).getDate());
            for(ReportLoanDaily reportLoanDaily:reportLoanDailyList ){
                reportLoanDailyService.save(reportLoanDaily);	
            }
        }
        
        //借款人数据
        ReportLoaneeDataDaily loaneeDataDaily = loaneeDataDailyService.dataCount(1);
        loaneeDataDailyService.deleteByDate(loaneeDataDaily.getDate());
        loaneeDataDailyService.save(loaneeDataDaily);

        
        //充值统计
        ReportRchgDaily rchgDaily = rchgDailyService.rchgCount(1);
        rchgDailyService.deleteByDate(rchgDaily.getDate());
        rchgDailyService.save(rchgDaily);
        
        //提现统计
        ReportWdrlDaily wdrlDaily = wdrlDailyService.wdrlCount(1);
        wdrlDailyService.deleteByDate(wdrlDaily.getDate());
        wdrlDailyService.save(wdrlDaily);
        
        //转账统计
        ReportTransferDaily transferDaily = transferDailyService.transferCount(1);
        transferDailyService.deleteByDate(transferDaily.getDate());
        transferDailyService.save(transferDaily);
       
        //借款申请统计
        ReportLoanApplyDaily reportLoanApplyDaily = reportLoanApplyDailyService.applyCount(1);
        reportLoanApplyDailyService.deleteByDate(reportLoanApplyDaily.getDate());
        reportLoanApplyDailyService.save(reportLoanApplyDaily);
        
        //借款交易金额统计
        ReportLoanAmtDaily reportLoanAmtDaily = reportLoanAmtDailyService.loanAmtCount(1);
        reportLoanAmtDailyService.deleteByDate(reportLoanAmtDaily.getDate());
        reportLoanAmtDailyService.save(reportLoanAmtDaily);
        
        //缴费统计
        ReportPayRefundDaily reportPayDaily = reportPayRefundDailyService.payCount(1);
        reportPayRefundDailyService.deleteByDate(reportPayDaily.getDate());
        reportPayRefundDailyService.save(reportPayDaily);
        
        //退费统计
        ReportPayRefundDaily reportRefundDaily = reportPayRefundDailyService.refundCount(1);
        reportPayRefundDailyService.save(reportRefundDaily);

    }

}