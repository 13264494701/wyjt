package com.jxf.task.tasks;



import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRepayRecord;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.service.NfsLoanRepayRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberAct;
import com.jxf.mem.entity.MemberVideoVerify;
import com.jxf.mem.service.MemberActService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.service.MemberVideoVerifyService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.nfs.constant.ActSubConstant;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.DateUtils;
import com.jxf.ufang.service.UfangLoaneeDataService;

import java.math.BigDecimal;
import java.util.Calendar;
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
 * @description 流量处理
 * @author wo
 */
@DisallowConcurrentExecution
public class MemberToLoaneeDataTask implements Job {

	private static final Logger log = LoggerFactory.getLogger(MemberToLoaneeDataTask.class);
	
    @Autowired
    private UfangLoaneeDataService ufangLoaneeDataService;
    @Autowired
    private MemberVideoVerifyService videoVerifyService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsLoanRepayRecordService loanRepayRecordService;
    @Autowired
    private MemberActService memberActService;
    
    @Autowired
    private MemberService memberService;
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	
//    	process1();
//    	process2();
//    	process3();
//    	process5();
    	
    	process6();

    }
    
    
    private void process1() {
    	
		Date beginDate = CalendarUtil.addDay(new Date(), 3);
		Date endDate = CalendarUtil.addDay(new Date(), 3);
		if (beginDate != null) {
			Calendar calendar = DateUtils.toCalendar(beginDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
			beginDate = calendar.getTime();
		}
		if (endDate != null) {
			Calendar calendar = DateUtils.toCalendar(endDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
			endDate = calendar.getTime();
		}
		
    	NfsLoanRecord loanRecord = new NfsLoanRecord();
		loanRecord.setBeginDueRepayDate(beginDate);
		loanRecord.setEndDueRepayDate(endDate);
		loanRecord.setStatus(NfsLoanRecord.Status.penddingRepay);
    	List<NfsLoanRecord> loanRecordList = loanRecordService.findList(loanRecord);
    	for(NfsLoanRecord loan : loanRecordList) {
    		

    		//过滤负债超过10W的用户
    		MemberAct act = memberActService.getMemberAct(loan.getLoanee(),ActSubConstant.MEMBER_PENDING_REPAYMENT);
    		if(act.getCurBal().compareTo(new BigDecimal(100000))>0) {continue;}
    		
    		//过滤没有借条或者有逾期借条的用户
    		int loanCnt = loanRecordService.countLoaneeLoan(loan);
    		if(loanCnt==0) {continue;}
    		
    		loan.setStatus(NfsLoanRecord.Status.overdue);
    		int overdueLoanCnt = loanRecordService.countLoaneeLoan(loan);
    		if(overdueLoanCnt>0) {continue;}
    			
    		ufangLoaneeDataService.pushLoaneeData(loan.getLoanee(),new BigDecimal(10));
    	}
    	
    }
    
    private void process2() {
    	

		Date beginDate = CalendarUtil.addDay(new Date(), -5);
		Date endDate = CalendarUtil.addDay(new Date(), -5);
		if (beginDate != null) {
			Calendar calendar = DateUtils.toCalendar(beginDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
			beginDate = calendar.getTime();
		}
		if (endDate != null) {
			Calendar calendar = DateUtils.toCalendar(endDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
			endDate = calendar.getTime();
		}
		
    	NfsLoanRecord loanRecord = new NfsLoanRecord();
		loanRecord.setBeginDueRepayDate(beginDate);
		loanRecord.setEndDueRepayDate(endDate);
		loanRecord.setStatus(NfsLoanRecord.Status.overdue);
    	List<NfsLoanRecord> loanRecordList = loanRecordService.findList(loanRecord);
    	for(NfsLoanRecord loan : loanRecordList) {
    		
    		//过滤负债超过10W的用户
    		MemberAct act = memberActService.getMemberAct(loan.getLoanee(),ActSubConstant.MEMBER_PENDING_REPAYMENT);
    		if(act.getCurBal().compareTo(new BigDecimal(100000))>0) {continue;}
    		
    		//过滤逾期总额超过3000元的用户
    		loan.setStatus(NfsLoanRecord.Status.overdue);
    		BigDecimal overdueLoanAmt = loanRecordService.sumLoaneeLoan(loan);
    		if(overdueLoanAmt.compareTo(new BigDecimal(3000))>0) {continue;}

    			
    		ufangLoaneeDataService.pushLoaneeData(loan.getLoanee(),new BigDecimal(5));
    	}
    	
    }
    
    private void process3() {
    	

		Date beginDate = CalendarUtil.addDay(new Date(), -1);
		Date endDate = CalendarUtil.addDay(new Date(), -1);
		if (beginDate != null) {
			Calendar calendar = DateUtils.toCalendar(beginDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
			beginDate = calendar.getTime();
		}
		if (endDate != null) {
			Calendar calendar = DateUtils.toCalendar(endDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
			endDate = calendar.getTime();
		}
		
		NfsLoanRepayRecord repayRecord = new NfsLoanRepayRecord();
		repayRecord.setBeginRepayDate(beginDate);
		repayRecord.setEndRepayDate(endDate);
		repayRecord.setStatus(NfsLoanRepayRecord.Status.done);
    	List<NfsLoanRepayRecord> repayRecordList = loanRepayRecordService.findList(repayRecord);
    	for(NfsLoanRepayRecord repay : repayRecordList) {
    		NfsLoanRecord loanRecord = loanRecordService.get(repay.getLoan());
    		//过滤负债超过10W的用户
    		MemberAct act = memberActService.getMemberAct(loanRecord.getLoanee(),ActSubConstant.MEMBER_PENDING_REPAYMENT);
    		if(act.getCurBal().compareTo(new BigDecimal(50000))>0) {continue;}
    		
    		//过滤没有借条或者有逾期借条的用户
    		int loanCnt = loanRecordService.countLoaneeLoan(loanRecord);
    		if(loanCnt==0) {continue;}
    		
    		loanRecord.setStatus(NfsLoanRecord.Status.overdue);
    		int overdueLoanCnt = loanRecordService.countLoaneeLoan(loanRecord);
    		if(overdueLoanCnt>0) {continue;}

    			
    		ufangLoaneeDataService.pushLoaneeData(loanRecord.getLoanee(),new BigDecimal(10));
    	}
    	
    }
    
    private void process5() {
    	
    	Date beginDate = CalendarUtil.addDay(new Date(), -1);
    	Date endDate = CalendarUtil.addDay(new Date(), -1);
		if (beginDate != null) {
			Calendar calendar = DateUtils.toCalendar(beginDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
			beginDate = calendar.getTime();
		}
		if (endDate != null) {
			Calendar calendar = DateUtils.toCalendar(endDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
			endDate = calendar.getTime();
		}
    	
    	MemberVideoVerify videoVerify = new MemberVideoVerify();
    	videoVerify.setType(MemberVideoVerify.Type.realIdentity);
    	videoVerify.setStatus(MemberVideoVerify.Status.verified);
    	videoVerify.setBeginTime(beginDate);
    	videoVerify.setEndTime(endDate);
    	List<MemberVideoVerify> videoVerifyList = videoVerifyService.findList(videoVerify);
    	for(MemberVideoVerify verify : videoVerifyList) {
    		
    		//过滤负债不为0的用户
    		MemberAct act = memberActService.getMemberAct(verify.getMember(),ActSubConstant.MEMBER_PENDING_REPAYMENT);
    		if(act.getCurBal().compareTo(BigDecimal.ZERO)>0) {continue;}
    		
    		ufangLoaneeDataService.pushLoaneeData(verify.getMember(),new BigDecimal(5));
    		
    	}
    	
    }
        private void process6() {
        			
    		Member member = new Member();
    		member.setIsAuth(true);
        	List<Member> memberList = memberService.findList(member);
        	for(Member mem : memberList) {
        		//过滤未实名的用户
        		if(!VerifiedUtils.isVerified(mem.getVerifiedList(), 1)) {
        			continue;
        		}
        		//过滤没有借条或者有逾期借条的用户
        		NfsLoanRecord loanRecord = new NfsLoanRecord();
        		loanRecord.setLoanee(mem);
        		int loanCnt = loanRecordService.countLoaneeLoan(loanRecord);
        		if(loanCnt==0) {continue;}
        		
        		loanRecord.setStatus(NfsLoanRecord.Status.overdue);
        		int overdueLoanCnt = loanRecordService.countLoaneeLoan(loanRecord);
        		if(overdueLoanCnt>0) {continue;}
        			
        		ufangLoaneeDataService.pushLoaneeData(mem,new BigDecimal(10));
        	}
        	
        }
    	
   

}