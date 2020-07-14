package com.jxf.task.tasks;

import com.jxf.loan.dao.NfsLoanRecordDataDao;
import com.jxf.loan.entity.NfsLoanPartialAndDelay;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRecordData;
import com.jxf.loan.service.NfsLoanPartialAndDelayService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.DateUtils;

import java.util.Date;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 统计任务
 *
 * @author wo
 */
@DisallowConcurrentExecution
public class LoanRecordDataTask implements Job {
	
	private static final Logger log = LoggerFactory.getLogger(LoanRecordDataTask.class);
	
    @Autowired
    private NfsLoanRecordService loanService;
    @Autowired
    private NfsLoanRecordDataDao loanDataDao;
	@Autowired
	private NfsLoanPartialAndDelayService loanPartialAndDelayService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
			
    	proPenddingRepay();
    	
    }
    /**
     * 处理借条
     */
    private void proPenddingRepay() {
    	
    	Date date = new Date();
		Date beginDate = CalendarUtil.StringToDate("2019/01/01", "yyyy/MM/dd");
		Date endDate = CalendarUtil.StringToDate("2019/04/20", "yyyy/MM/dd");
		
		NfsLoanRecord loanRecord = new NfsLoanRecord();
		loanRecord.setBeginTime(beginDate);
		loanRecord.setEndTime(endDate);	
		List<NfsLoanRecord> loans = loanService.findList(loanRecord);//注意数据量不要超过一定级别，否则会耗尽内存，可以考虑分页查询

		for (NfsLoanRecord loan : loans) {
			//需要正负号			
			NfsLoanRecordData loanData = new NfsLoanRecordData();
			loanData.setId(loan.getId());
			loanData.setLoanee(loan.getLoanee());
			loanData.setAmount(loan.getAmount());
			loanData.setIntRate(loan.getIntRate());
			loanData.setInterest(loan.getInterest());
			loanData.setTerm(loan.getTerm());
			loanData.setDueRepayDate(loan.getDueRepayDate());
			loanData.setCompleteDate(loan.getCompleteDate());
			if(loan.getStatus().equals(NfsLoanRecord.Status.repayed)) {
				
				NfsLoanPartialAndDelay partialAndDelay = loanPartialAndDelayService.findNearestPartialAndDelayByRecordId(loan.getId());
				if(partialAndDelay!=null&&partialAndDelay.getStatus().equals(NfsLoanPartialAndDelay.Status.agreed)){
					loanData.setRepayStatus(NfsLoanRecordData.RepayStatus.delayRepayed);
					loanData.setIsOk(true);
				}
				int num = DateUtils.getDifferenceOfTwoDate(loan.getDueRepayDate(),loan.getCompleteDate());
				if(num<0){
					loanData.setRepayStatus(NfsLoanRecordData.RepayStatus.overdueAndRepayed);
					loanData.setIsOk(num>-3?true:false);
				}else if(num==0) {
					loanData.setRepayStatus(NfsLoanRecordData.RepayStatus.repayedDueDate);
					loanData.setIsOk(true);
				}else {
					loanData.setRepayStatus(NfsLoanRecordData.RepayStatus.repayedAdvance);
					loanData.setIsOk(true);
				}
			}else if(loan.getStatus().equals(NfsLoanRecord.Status.overdue)) {
				int days = DateUtils.getDifferenceOfTwoDate(loan.getDueRepayDate(),date);
				loanData.setRepayStatus(NfsLoanRecordData.RepayStatus.overdueNotRepayed);
				loanData.setOverdueDays(-days);
				loanData.setIsOk(false);
			}else {
				continue;
			}
			log.warn("借条{}应还日期{}还款状态{}",loan.getId(),CalendarUtil.getDate(loan.getDueRepayDate()),loanData.getRepayStatus());
			loanData.setCreateBy(loan.getCreateBy());
			loanData.setCreateTime(loan.getCreateTime());
			loanData.setUpdateBy(loan.getUpdateBy());
			loanData.setUpdateTime(loan.getUpdateTime());
			loanDataDao.insert(loanData);
				
		}

    }
    
   
}