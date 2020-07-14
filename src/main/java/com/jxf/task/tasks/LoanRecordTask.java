package com.jxf.task.tasks;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.jxf.loan.entity.NfsLoanApply.RepayType;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRepayRecord;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.service.NfsLoanRepayRecordService;
import com.jxf.mem.entity.MemberPointRule;
import com.jxf.mem.service.MemberPointService;
import com.jxf.mem.service.MemberService;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.DateUtils;

import java.util.Date;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 统计任务
 *
 * @author wo
 */
@DisallowConcurrentExecution
public class LoanRecordTask implements Job {
	private static Log log = LogFactory.getLog(LoanRecordTask.class);
    @Autowired
    private NfsLoanRecordService loanService;
	@Autowired
	private MemberPointService memberPointService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsLoanRepayRecordService loanRepayRecordService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
			
    	proPenddingRepay();
    	
    	proOverdueStagesLoan();
    	
//    	proOverdue();
    }
    /**
     * 处理待还借条
     */
    private void proPenddingRepay() {
    	
    	Date date = new Date();
		NfsLoanRecord loanRecord = new NfsLoanRecord();
		loanRecord.setStatus(NfsLoanRecord.Status.penddingRepay);
		List<NfsLoanRecord> loans = loanService.findList(loanRecord);//注意数据量不要超过一定级别，否则会耗尽内存，可以考虑分页查询
		if(loans != null && loans.size() > 0) {
			for (NfsLoanRecord loan : loans) {
				//需要正负号
				int days = DateUtils.getDifferenceOfTwoDate(loan.getDueRepayDate(),date);
				if(days>30) {
					loan.setProgress("30;ED2E24|日以上还款;757575");	
				}else if(days<=30&&days>0) {
					loan.setProgress(days + ";ED2E24|日内还款;757575");	
				}else if(days==0) {
					loan.setProgress("今日还款;FFAE38");	
				}else if(days==-1) {
					loan.setStatus(NfsLoanRecord.Status.overdue);
					loan.setProgress("今日逾期;FFAE38");	
					NfsLoanRepayRecord param = new NfsLoanRepayRecord();
					param.setLoan(loan);
					List<NfsLoanRepayRecord> loanRepayRecords = loanRepayRecordService.findList(param);
					for (NfsLoanRepayRecord repayRecord : loanRepayRecords) {
						Date expectRepayDate = repayRecord.getExpectRepayDate();
						if(expectRepayDate.before(date) && 
								(repayRecord.getStatus().equals(NfsLoanRepayRecord.Status.pending) || repayRecord.getStatus().equals(NfsLoanRepayRecord.Status.partialDone)) ) {
							repayRecord.setStatus(NfsLoanRepayRecord.Status.overdue);
							loanRepayRecordService.save(repayRecord);
						}
					}
					log.info("借条编号：" + loan.getLoanNo() + " 当前已逾期");
				}else if(days<-1&&days>-15) {
					loan.setStatus(NfsLoanRecord.Status.overdue);//防止上逾期条件被击穿，导致无法再设置逾期，正常情况不会走入当前逻辑
					loan.setProgress("已逾期;757575|" + Math.abs(days) + ";ED2E24|日;757575");	
				}else if(days<-15) {
					loan.setStatus(NfsLoanRecord.Status.overdue);//防止上逾期条件被击穿，导致无法再设置逾期，正常情况不会走入当前逻辑
					loan.setProgress("逾期已超过;757575|15;ED2E24|天;757575");	
				}
				loanService.save(loan);

				if (days == -5) {
					memberPointService.updateMemberPoint(memberService.get(loan.getLoanee().getId()), MemberPointRule.Type.overDue5, loan.getDueRepayAmount());
				}
				if (days == -45) {
					memberPointService.updateMemberPoint(memberService.get(loan.getLoanee().getId()), MemberPointRule.Type.overDue45, loan.getDueRepayAmount());
				}
				
			}
		}
    }
    
    /**
     * 处理分期多期逾期 但是状态没有改变借条
     */
    private void proOverdueStagesLoan() {
    	NfsLoanRecord loanRecord = new NfsLoanRecord();
		loanRecord.setStatus(NfsLoanRecord.Status.overdue);
		loanRecord.setRepayType(RepayType.principalAndInterestByMonth);
		List<NfsLoanRecord> loans = loanService.findList(loanRecord);//注意数据量不要超过一定级别，否则会耗尽内存，可以考虑分页查询
		if(!Collections3.isEmpty(loans)) {
			for (NfsLoanRecord nfsLoanRecord : loans) {
				NfsLoanRepayRecord nfsLoanRepayRecord = new NfsLoanRepayRecord();
				nfsLoanRepayRecord.setLoan(nfsLoanRecord);
				List<NfsLoanRepayRecord> allLoanRepayRecords = loanRepayRecordService.findList(nfsLoanRepayRecord);
				for (NfsLoanRepayRecord repayRecord : allLoanRepayRecords) {
					String expectDate =DateUtils.getDateStr(repayRecord.getExpectRepayDate(), "yyyy-MM-dd");
					String actualDate = DateUtils.getDateStr(new Date(), "yyyy-MM-dd");
					if(actualDate.compareTo(expectDate) > 0 && !NfsLoanRepayRecord.Status.done.equals(repayRecord.getStatus())
							&& !NfsLoanRepayRecord.Status.overdue.equals(repayRecord.getStatus())) {
						
						repayRecord.setStatus(NfsLoanRepayRecord.Status.overdue);
						loanRepayRecordService.save(repayRecord);
					}
				}
			}
		}
    }
    
    
    
}