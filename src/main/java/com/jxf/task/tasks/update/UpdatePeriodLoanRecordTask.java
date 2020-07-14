package com.jxf.task.tasks.update;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRepayRecord;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.service.NfsLoanRepayRecordService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 修复分期借条任务
 *
 * @author wo
 */
@DisallowConcurrentExecution
public class UpdatePeriodLoanRecordTask implements Job {
	private static Log log = LogFactory.getLog(UpdatePeriodLoanRecordTask.class);
    @Autowired
    private NfsLoanRecordService loanService;
	@Autowired
	private NfsLoanRepayRecordService loanRepayRecordService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	updateLoanRecord();
    }
   
    private void updateLoanRecord() {
    	List<NfsLoanRecord> loanRecords = loanService.findPeriodRepayLoanList();
    	log.error("开始处理未还分期借条数："+loanRecords.size());
    	int count = 0;
    	for (NfsLoanRecord nfsLoanRecord : loanRecords) {
    		try {
    			//获取当前借条的分期计划
        		NfsLoanRepayRecord param = new NfsLoanRepayRecord();
        		param.setLoan(nfsLoanRecord);
        		List<NfsLoanRepayRecord> repayRecords = loanRepayRecordService.findList(param);
        		BigDecimal dueRepayAmount = nfsLoanRecord.getDueRepayAmount();
        		int dueRepayTerm = repayRecords.size();//未还期数
        		int repayedTerm = 0;//已还期数
        		boolean overdueFlag = false;
        		boolean changeDueRepayDateFlag = true;
        		for (NfsLoanRepayRecord repayRecord : repayRecords) {
        			if(repayRecord.getStatus().equals(NfsLoanRepayRecord.Status.pending) && changeDueRepayDateFlag) {
        				nfsLoanRecord.setDueRepayDate(repayRecord.getExpectRepayDate());
        				changeDueRepayDateFlag = false;
        			}
    				if(NfsLoanRepayRecord.Status.done.equals(repayRecord.getStatus())) {
    					//已还款
    					dueRepayAmount = dueRepayAmount.subtract(repayRecord.getExpectRepayAmt());
    					dueRepayTerm = dueRepayTerm - 1;
    					repayedTerm = repayedTerm + 1;
    				}else if(repayRecord.getExpectRepayDate().before(new Date()) && repayRecord.getStatus().equals(NfsLoanRepayRecord.Status.pending)){
    					repayRecord.setStatus(NfsLoanRepayRecord.Status.overdue);
    					overdueFlag = true;
    					loanRepayRecordService.save(repayRecord);
    				}
    			}
        		nfsLoanRecord.setDueRepayAmount(dueRepayAmount);
        		nfsLoanRecord.setDueRepayTerm(dueRepayTerm);
        		nfsLoanRecord.setRepayedTerm(repayedTerm);
        		nfsLoanRecord.setStatus(overdueFlag ? NfsLoanRecord.Status.overdue : NfsLoanRecord.Status.penddingRepay);
        		loanService.save(nfsLoanRecord);
    		} catch (Exception e) {
    			log.error("借条"+nfsLoanRecord.getId()+"更新失败！");
    		}
    		count ++;
    	}
    	log.error("未还分期借条处理条数：count={}"+count);
    	NfsLoanRecord loanRecord2 = new NfsLoanRecord();
    	loanRecord2.setRepayType(NfsLoanApply.RepayType.principalAndInterestByMonth);
    	loanRecord2.setStatus(NfsLoanRecord.Status.repayed);
    	List<NfsLoanRecord> loanRecords2 = loanService.findList(loanRecord2);
    	log.error("已还分期借条数："+loanRecords2.size());
    	int count2 = 0;
    	for (NfsLoanRecord nfsLoanRecord : loanRecords2) {
			nfsLoanRecord.setDueRepayTerm(0);
			loanService.save(nfsLoanRecord);
			count2 ++;
    	}
    	log.error("更新已还分期借条数："+count2);
    	
    }
}