package com.jxf.task.tasks;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import com.jxf.loan.entity.NfsLoanApply.TrxType;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.DateUtils;

@DisallowConcurrentExecution
public class SendRiskWarningTask implements Job{

	private static final Logger logger = LoggerFactory.getLogger(SendRiskWarningTask.class);

	@Autowired
	private  NfsLoanRecordService loanRecordService;
	@Autowired
	private  SendSmsMsgService sendSmsMsgService;//短信
	@Autowired
	private MemberMessageService memberMessageService;//站内信
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		Date startDate = new Date();
		logger.debug("开始时间:" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		beforeThreeDay();
		repayDay();
		overdueOneDay();
		overdueThreeDay();
		overdueSevenDay();
		overdueFifteenDay();
		
		logger.debug("用时:" + DateUtils.pastMinutes(startDate) + "分钟");
		
	}
	
	private void beforeThreeDay() {

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
    	NfsLoanRecord nfsLoanRecord = new NfsLoanRecord();
    	nfsLoanRecord.setBeginDueRepayDate(beginDate);
    	nfsLoanRecord.setEndDueRepayDate(endDate);
    	nfsLoanRecord.setStatus(NfsLoanRecord.Status.penddingRepay);
    	List<NfsLoanRecord> loanList = loanRecordService.findList(nfsLoanRecord);

		for (NfsLoanRecord loanRecord : loanList) {
			if(loanRecord.getTrxType().equals(TrxType.online)){
				//发送会员消息
				memberMessageService.sendMessage(MemberMessage.Type.repayDayBeforeThreeDay,loanRecord.getId());		
				sendSmsMsgService.sendNormalSms("repayDayBeforeThreeDay", loanRecord.getLoanee().getUsername(), null);
			}else{
				memberMessageService.sendMessage(MemberMessage.Type.repayDayBeforeTwoDayLoanee,loanRecord.getId());		
				memberMessageService.sendMessage(MemberMessage.Type.repayDayBeforeThreeDayLoaner,loanRecord.getId());		
			}
		}
		logger.debug("到期前3天的借条一共{}条",loanList.size());
	}
	
	private void repayDay() {
		Date beginDate = new Date();
		Date endDate = new Date();
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
    	NfsLoanRecord nfsLoanRecord = new NfsLoanRecord();
    	nfsLoanRecord.setBeginDueRepayDate(beginDate);
    	nfsLoanRecord.setEndDueRepayDate(endDate);
    	nfsLoanRecord.setStatus(NfsLoanRecord.Status.penddingRepay);
    	List<NfsLoanRecord> loanList = loanRecordService.findList(nfsLoanRecord);
    	
		for (NfsLoanRecord loanRecord : loanList) {
			if(loanRecord.getTrxType().equals(TrxType.online)){
				//发送会员消息
				memberMessageService.sendMessage(MemberMessage.Type.repayDay,loanRecord.getId());
				sendSmsMsgService.sendNormalSms("repayDay", loanRecord.getLoanee().getUsername(), null);
			}else{
				memberMessageService.sendMessage(MemberMessage.Type.repayTodayLoanee,loanRecord.getId());	
				memberMessageService.sendMessage(MemberMessage.Type.repayTodayLoaner,loanRecord.getId());
			}
		}
		logger.debug("到期当日的借条一共{}条",loanList.size());
	}
	
	private void overdueOneDay() {

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
    	NfsLoanRecord nfsLoanRecord = new NfsLoanRecord();
    	nfsLoanRecord.setBeginDueRepayDate(beginDate);
    	nfsLoanRecord.setEndDueRepayDate(endDate);
    	nfsLoanRecord.setStatus(NfsLoanRecord.Status.overdue);
    	List<NfsLoanRecord> loanList = loanRecordService.findList(nfsLoanRecord);

		for (NfsLoanRecord loanRecord : loanList) {
			TrxType trxType = loanRecord.getTrxType();
			if(trxType.equals(TrxType.online)){
				//发送会员消息
				memberMessageService.sendMessage(MemberMessage.Type.overdueOneDay,loanRecord.getId());		
				sendSmsMsgService.sendNormalSms("overdueOneDay", loanRecord.getLoanee().getUsername(), null);
			}else{
				memberMessageService.sendMessage(MemberMessage.Type.overdueTodayLoanee,loanRecord.getId());		
				memberMessageService.sendMessage(MemberMessage.Type.overdueTodayLoaner,loanRecord.getId());
			}
		}
		logger.debug("逾期1天的借条一共{}条",loanList.size());
	}
	
	private void overdueThreeDay() {

		Date beginDate = CalendarUtil.addDay(new Date(), -3);
		Date endDate = CalendarUtil.addDay(new Date(), -3);
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
    	NfsLoanRecord nfsLoanRecord = new NfsLoanRecord();
    	nfsLoanRecord.setBeginDueRepayDate(beginDate);
    	nfsLoanRecord.setEndDueRepayDate(endDate);
    	nfsLoanRecord.setStatus(NfsLoanRecord.Status.overdue);
    	List<NfsLoanRecord> loanList = loanRecordService.findList(nfsLoanRecord);

		for (NfsLoanRecord loanRecord : loanList) {
			TrxType trxType = loanRecord.getTrxType();
			if(trxType.equals(TrxType.online)){
				//发送会员消息
				memberMessageService.sendMessage(MemberMessage.Type.overdueThreeDay,loanRecord.getId());
				
				sendSmsMsgService.sendCollectionSms("overdueThreeDay", loanRecord.getLoaner().getUsername(), null);
			}
		}
		logger.debug("逾期3天的借条一共{}条",loanList.size());
	}

	
	private void overdueSevenDay() {

		Date beginDate = CalendarUtil.addDay(new Date(), -7);
		Date endDate = CalendarUtil.addDay(new Date(), -7);
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
    	NfsLoanRecord nfsLoanRecord = new NfsLoanRecord();
    	nfsLoanRecord.setBeginDueRepayDate(beginDate);
    	nfsLoanRecord.setEndDueRepayDate(endDate);
    	nfsLoanRecord.setStatus(NfsLoanRecord.Status.overdue);
    	List<NfsLoanRecord> loanList = loanRecordService.findList(nfsLoanRecord);

		for (NfsLoanRecord loanRecord : loanList) {
			if(loanRecord.getTrxType().equals(TrxType.online)){
				//发送会员消息
				memberMessageService.sendMessage(MemberMessage.Type.overdueSevenDay,loanRecord.getId());
				sendSmsMsgService.sendNormalSms("overdueSevenDay", loanRecord.getLoanee().getUsername(), null);
			}
		}
		logger.debug("逾期7天的借条一共{}条",loanList.size());
	}

	
	private void overdueFifteenDay() {
		
		Date beginDate = CalendarUtil.addDay(new Date(), -15);
		Date endDate = CalendarUtil.addDay(new Date(), -15);
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
    	NfsLoanRecord nfsLoanRecord = new NfsLoanRecord();
    	nfsLoanRecord.setBeginDueRepayDate(beginDate);
    	nfsLoanRecord.setEndDueRepayDate(endDate);
    	nfsLoanRecord.setStatus(NfsLoanRecord.Status.overdue);
    	List<NfsLoanRecord> loanList = loanRecordService.findList(nfsLoanRecord);

		for (NfsLoanRecord loanRecord : loanList) {
			if(loanRecord.getTrxType().equals(TrxType.online)){
				//发送会员消息
				memberMessageService.sendMessage(MemberMessage.Type.overdueFifteenDay,loanRecord.getId());
				
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("name", loanRecord.getLoanee().getName());
				sendSmsMsgService.sendCollectionSms("overdueFifteenDay", loanRecord.getLoaner().getUsername(), map);
			}
		}
		logger.debug("逾期15天以上的借条一共{}条",loanList.size());
	}


	
}
