package com.jxf.task.tasks;

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

import com.jxf.loan.entity.NfsCrAuction;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsCrAuctionService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.mms.service.impl.SendMsgServiceImpl;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;

@DisallowConcurrentExecution
public class SendAuctionNoticeTask implements Job{

	private static final Logger logger = LoggerFactory.getLogger(SendAuctionNoticeTask.class);
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private  SendSmsMsgService sendSmsMsgService;//短信
	@Autowired
	private MemberMessageService memberMessageService;//站内信
	@Autowired
	private SendMsgServiceImpl sendMsgServiceImpl;//推送
	@Autowired
	private NfsCrAuctionService nfsCrAuctionService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		Date startDate = new Date();
		Map<String,Object> map = new HashMap<>();
		logger.debug("开始时间:" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		List<NfsCrAuction> auctionList = nfsCrAuctionService.findOvertimeAuctionList();
		for (NfsCrAuction auction : auctionList) {
			NfsLoanRecord record = loanRecordService.get(auction.getLoanRecord());
			record.setAuctionStatus(NfsLoanRecord.AuctionStatus.initial);
			loanRecordService.save(record);
			
			auction.setStatus(NfsCrAuction.Status.failed);
			auction.setIsPub(false);
			auction.setRmk("72小时未交易成功");
			nfsCrAuctionService.save(auction);
			
			try {
				//发送会员消息
				MemberMessage message = memberMessageService.sendMessage(MemberMessage.Type.outDateAuctionImsLoaner,auction.getId());
				
				// 短信
				map.put("money", auction.getLoanRecord().getAmount());
				sendSmsMsgService.sendCollectionSms("outDateAuctionImsLoaner", auction.getLoanRecord().getLoaner().getUsername(), map);
				
				//推送
				sendMsgServiceImpl.beforeSendAppMsg(message);
			} catch (Exception e) {
				logger.error(Exceptions.getStackTraceAsString(e));
			}
		}
		logger.debug("流拍的债转借条一共{}条",auctionList.size());
		
		List<NfsCrAuction> overOneDayList = nfsCrAuctionService.findOverOneDayList();
		for (NfsCrAuction nfsCrAuction : overOneDayList) {
			nfsCrAuction.setStatus(NfsCrAuction.Status.forsale);
			nfsCrAuction.setIsPub(true);
			nfsCrAuction.setRmk("等待购买人购买");
			nfsCrAuction.setBeginTime(new Date());
			nfsCrAuctionService.save(nfsCrAuction);
		}
		logger.debug("超过24小时自动通过审核的借条一共{}条",overOneDayList.size());
		logger.debug("用时:" + DateUtils.pastMinutes(startDate) + "分钟");
		
	}

	
}
