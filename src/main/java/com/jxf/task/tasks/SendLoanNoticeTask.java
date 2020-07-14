package com.jxf.task.tasks;

import java.util.Date;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.service.NfsLoanApplyDetailService;
import com.jxf.loan.service.NfsLoanApplyService;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.svc.utils.DateUtils;

@DisallowConcurrentExecution
public class SendLoanNoticeTask implements Job{

	private static final Logger logger = LoggerFactory.getLogger(SendLoanNoticeTask.class);

	@Autowired
	private NfsLoanApplyDetailService nfsLoanApplyDetailService;
	@Autowired
	private NfsLoanApplyService loanApplyService;
	@Autowired
	private MemberMessageService memberMessageService;//站内信
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		Date startDate = new Date();
		logger.debug("开始时间:" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		List<NfsLoanApplyDetail> list = nfsLoanApplyDetailService.findLoanOutTimeList();
		
		for (NfsLoanApplyDetail detail : list) {
			nfsLoanApplyDetailService.loanApplyOutTime(detail);
			NfsLoanApply apply = loanApplyService.get(detail.getApply());
			if(apply.getLoanRole().equals(NfsLoanApply.LoanRole.loanee)) {
				memberMessageService.sendMessage(MemberMessage.Type.applicationTimeoutLoaner,detail.getId());
			}else {
				memberMessageService.sendMessage(MemberMessage.Type.applicationTimeoutLoanee,detail.getId());
			}
		}
		
		logger.debug("用时:" + DateUtils.pastMinutes(startDate) + "分钟");
		
	}

	
}
