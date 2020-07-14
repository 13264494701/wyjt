package com.jxf.task.tasks;


import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.entity.NfsLoanPartialAndDelay;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanApplyDetailService;
import com.jxf.loan.service.NfsLoanPartialAndDelayService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;

import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 冻结资金超时解冻
 *
 * @author wo
 */
@DisallowConcurrentExecution
public class LoanApplyTask implements Job {
	
	private static final Logger logger = LoggerFactory.getLogger(LoanApplyTask.class);
	
    @Autowired
    private NfsLoanApplyDetailService loanApplyDetailService;
    @Autowired
    private NfsLoanRecordService loanRecordService;
    @Autowired
    private NfsLoanPartialAndDelayService loanPartialAndDelayService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
		//超24小时的申请变成已超时
    	NfsLoanApplyDetail loanApplyDetail = new NfsLoanApplyDetail();
		loanApplyDetail.setStatus(NfsLoanApplyDetail.Status.pendingAgree);
		loanApplyDetail.setTrxType(NfsLoanApply.TrxType.online);
		List<NfsLoanApplyDetail> loanApplyDetails = loanApplyDetailService.findList(loanApplyDetail);
		if(loanApplyDetails != null && loanApplyDetails.size() > 0) {
			for (NfsLoanApplyDetail detail : loanApplyDetails) {
				long pastHour = DateUtils.pastHour(detail.getCreateTime());
				if(pastHour>24) {
					loanApplyDetailService.changeStatusToExpired(detail);
				}
			}
		}
		//超过24小时的部分还款待确认 变回原状态	
		NfsLoanRecord nfsLoanRecord = new NfsLoanRecord();	
		nfsLoanRecord.setStatus(NfsLoanRecord.Status.penddingRepay);
		nfsLoanRecord.setPartialStatus(NfsLoanRecord.PartialStatus.loaneeApplyPartial);
		List<NfsLoanRecord> recordList = loanRecordService.findList(nfsLoanRecord);
		for (NfsLoanRecord record : recordList) {
			NfsLoanPartialAndDelay partialAndDelay = loanPartialAndDelayService.findNearestPartialAndDelayByRecordId(record.getId());
			if(!partialAndDelay.getStatus().equals(NfsLoanPartialAndDelay.Status.confirm)){
				return ;
			}
			long pastHour = DateUtils.pastHour(partialAndDelay.getCreateTime());
			if(pastHour > 24){
				try {
					loanRecordService.returnStatusToInitial(record,partialAndDelay.getPartialAmount(),partialAndDelay);
				} catch (Exception e) {
					logger.error(Exceptions.getStackTraceAsString(e));
				}
			}
		}	
    }

}