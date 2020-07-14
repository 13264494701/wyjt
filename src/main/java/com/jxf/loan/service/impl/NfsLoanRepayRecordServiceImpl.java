package com.jxf.loan.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.jxf.loan.constant.LoanConstant;
import com.jxf.loan.constant.RecordMessage;
import com.jxf.loan.dao.NfsCrAuctionDao;
import com.jxf.loan.dao.NfsLoanRepayRecordDao;
import com.jxf.loan.entity.NfsLoanDetailMessage;
import com.jxf.loan.entity.NfsLoanOperatingRecord;
import com.jxf.loan.entity.NfsLoanPartialAndDelay;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRepayRecord;
import com.jxf.loan.entity.NfsCrAuction;
import com.jxf.loan.entity.NfsLoanApply.LoanRole;
import com.jxf.loan.entity.NfsLoanContract.SignatureType;
import com.jxf.loan.entity.NfsLoanRecord.AuctionStatus;
import com.jxf.loan.entity.NfsLoanRecord.DelayStatus;
import com.jxf.loan.entity.NfsLoanRecord.PartialStatus;
import com.jxf.loan.entity.NfsLoanRecord.Status;
import com.jxf.loan.service.NfsCrAuctionService;
import com.jxf.loan.service.NfsLoanContractService;
import com.jxf.loan.service.NfsLoanDetailMessageService;
import com.jxf.loan.service.NfsLoanOperatingRecordService;
import com.jxf.loan.service.NfsLoanPartialAndDelayService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.service.NfsLoanRepayRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.entity.MemberPointRule;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberPointService;
import com.jxf.mem.service.MemberService;
import com.jxf.mms.msg.impl.SendSmsMsgServiceImpl;
import com.jxf.mms.service.SendMsgService;
import com.jxf.nfs.constant.TrxRuleConstant;

import com.jxf.nfs.service.NfsActService;
import com.jxf.svc.config.Constant;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.StringUtils;

/**
 * 还款计划ServiceImpl
 * @author wo
 * @version 2018-11-15
 */
@Service("nfsLoanRepayRecordService")
@Transactional(readOnly = true)
public class NfsLoanRepayRecordServiceImpl extends CrudServiceImpl<NfsLoanRepayRecordDao, NfsLoanRepayRecord> implements NfsLoanRepayRecordService{
	
	@Autowired
	private NfsLoanRepayRecordDao repayRecordDao;

	@Autowired
	private NfsActService actService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsLoanContractService loanContractService;
	@Autowired
	private NfsLoanDetailMessageService loanDetailMessageService;
	@Autowired
	private NfsLoanOperatingRecordService loanOperatingRecordService;
	@Autowired
	private NfsLoanPartialAndDelayService loanPartialAndDelayService;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private MemberPointService memberPointService;
	@Autowired
	private SendMsgService sendMsgService;
	@Autowired
	private SendSmsMsgServiceImpl sendSmsMsgServiceImpl;
	@Autowired
	private NfsCrAuctionService auctionService;
	@Autowired
	private NfsCrAuctionDao crAuctionDao;
	
	public NfsLoanRepayRecord get(Long id) {
		return super.get(id);
	}
	
	public List<NfsLoanRepayRecord> findList(NfsLoanRepayRecord nfsLoanRepayRecord) {
		return super.findList(nfsLoanRepayRecord);
	}
	
	public Page<NfsLoanRepayRecord> findPage(Page<NfsLoanRepayRecord> page, NfsLoanRepayRecord nfsLoanRepayRecord) {
		return super.findPage(page, nfsLoanRepayRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsLoanRepayRecord nfsLoanRepayRecord) {
		super.save(nfsLoanRepayRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsLoanRepayRecord nfsLoanRepayRecord) {
		super.delete(nfsLoanRepayRecord);
	}

	@Override
	public List<NfsLoanRepayRecord> getRepayRecordsByLoanId(Long loanId) {
		return repayRecordDao.getRepayRecordsByLoanId(loanId);
	}
	
	@Override
	public List<NfsLoanRepayRecord> findNotPendingRecordsByLoanId(Long loanId) {
		return repayRecordDao.findNotPendingRecordsByLoanId(loanId);
	}
	@Transactional(readOnly=false)
	@Override
	public int loaneeApplyPartialRepay(NfsLoanRecord loanRecord, NfsLoanPartialAndDelay partialAndDelay) {
			BigDecimal partialAmount = partialAndDelay.getPartialAmount();
			BigDecimal delayInterest = partialAndDelay.getDelayInterest();
			Member loanee = loanRecord.getLoanee();
			int code = actService.updateAct(TrxRuleConstant.LOANEE_APPLY_PARTIAL, partialAmount, loanee, loanRecord.getId());
			if(code == Constant.UPDATE_FAILED) {
				return Constant.UPDATE_FAILED;
			}
			loanPartialAndDelayService.save(partialAndDelay);
			loanRecordService.update(loanRecord);
			//发送会员消息
			MemberMessage sendMessage = memberMessageService.sendMessage(MemberMessage.Type.partialRepaymentApplication,partialAndDelay.getId());
			//推送
			sendMsgService.beforeSendAppMsg(sendMessage);
			
			//生成对话
			Map<String, String> dialogueMap = new HashMap<String, String>();
			int delayTerm = partialAndDelay.getDelayDays();
			if(delayTerm > 0) {
				dialogueMap.put(LoanConstant.LAST_REPAY_DATE, DateUtils.formatDate(partialAndDelay.getNowRepayDate(), "yyyy-MM-dd"));
			}else {
				dialogueMap.put(LoanConstant.LAST_REPAY_DATE, DateUtils.formatDate(loanRecord.getDueRepayDate(), "yyyy-MM-dd"));
			}
			dialogueMap.put(LoanConstant.PARTIAL_PAYMENT, StringUtils.decimalToStr(partialAmount, 2));
			dialogueMap.put(LoanConstant.DELAY_INTEREST, StringUtils.decimalToStr(delayInterest, 2));//延期利息
			dialogueMap.put(LoanConstant.CURRENT_REPAY_DATE, DateUtils.formatDate(loanRecord.getDueRepayDate(), "yyyy-MM-dd"));
			dialogueMap.put(LoanConstant.OLD_AMOUNT, StringUtils.decimalToStr(loanRecord.getAmount(), 2));
			dialogueMap.put(LoanConstant.OLD_INTEREST, StringUtils.decimalToStr(loanRecord.getInterest(), 2));
	
			BigDecimal dueRepayAmount = loanRecord.getDueRepayAmount();
			BigDecimal remainRepayAmount = dueRepayAmount.subtract(partialAmount).add(delayInterest).setScale(2, BigDecimal.ROUND_HALF_UP);
			dialogueMap.put(LoanConstant.REMAIN_PAYMENT, remainRepayAmount + "");
			
			//生成对话
			NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
			nfsLoanDetailMessage.setDetail(loanRecord.getLoanApplyDetail());
			nfsLoanDetailMessage.setMember(loanee);
			nfsLoanDetailMessage.setNote(JSON.toJSONString(dialogueMap));
			nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_6);
			nfsLoanDetailMessage.setType(RecordMessage.BORROWER_PART_REPAYMENT);
			loanDetailMessageService.save(nfsLoanDetailMessage);
			return 0;
	}
	
	@Transactional(readOnly=false)
	@Override
	public boolean agreePartialRepayAndDelay( NfsLoanRecord loanRecord,NfsLoanPartialAndDelay partialAndDelay,NfsLoanOperatingRecord operatingRecord) {
			Member loanee = loanRecord.getLoanee();
			Member loaner = loanRecord.getLoaner();
			//如果有延期利息，先变更待收待还
			BigDecimal delayInterest = partialAndDelay.getDelayInterest();
			if(delayInterest != null && delayInterest.compareTo(BigDecimal.ZERO) > 0) {
				int code = actService.updateAct(TrxRuleConstant.LOANEE_APPROVE_DELAY_PENDING_REPAY, delayInterest, loanee, loaner, loanRecord.getId());
				if(code == Constant.UPDATE_FAILED) {
					throw new RuntimeException("借款人同意放款人的延期申请变更待收待还失败！借条ID["+ loanRecord.getId() +"]" );
				}
			}
			
			BigDecimal repayAmount = partialAndDelay.getPartialAmount();
			int code = actService.updateAct(TrxRuleConstant.LOANEE_APPROVE_PARTIAL_REPAYMENT, repayAmount, loanee, loaner, loanRecord.getId());
			
			if(code == Constant.UPDATE_FAILED) {
				throw new RuntimeException("借款人["+ loanee.getId() +"]同意借条["+ loanRecord.getId()+"]部分还款操作账户更新异常。");
			}
			BigDecimal loanAmount = loanRecord.getAmount();
			BigDecimal loanInterest = loanRecord.getInterest();
			//更改record
			loanRecord.setPartialStatus(PartialStatus.initial);
			if(repayAmount.compareTo(loanInterest) >= 0) {
				//还完利息
				loanRecord.setInterest(BigDecimal.ZERO);
				loanRecord.setAmount(loanAmount.add(loanInterest).subtract(repayAmount));
			}else {
				//利息也没还够
				loanRecord.setInterest(loanInterest.subtract(repayAmount));
			}
			loanRecord.setDueRepayAmount(loanRecord.getDueRepayAmount().subtract(repayAmount));
			Date nextRepayDate = partialAndDelay.getNowRepayDate();
			Integer delayDays = partialAndDelay.getDelayDays();
			//更新还款记录
			NfsLoanRepayRecord param = new NfsLoanRepayRecord();
			param.setLoan(loanRecord);
			List<NfsLoanRepayRecord> records = findList(param);
			NfsLoanRepayRecord repayRecord = records.get(0);
			if(delayDays.intValue() != 0) {
				//延期
				loanRecord.setInterest(loanRecord.getInterest().add(partialAndDelay.getDelayInterest()));
				loanRecord.setDueRepayDate(nextRepayDate);
				loanRecord.setTerm(loanRecord.getTerm() + partialAndDelay.getDelayDays() );
				loanRecord.setDelayStatus(DelayStatus.initial);
				loanRecord.setIntRate(partialAndDelay.getDelayRate());
				loanRecord.setDueRepayAmount(loanRecord.getDueRepayAmount().add(partialAndDelay.getDelayInterest()));
				repayRecord.setExpectRepayDate(nextRepayDate);
				if(delayInterest != null && delayInterest.compareTo(BigDecimal.ZERO) > 0) {
					repayRecord.setExpectRepayAmt(repayRecord.getExpectRepayAmt().add(delayInterest));
					repayRecord.setExpectRepayInt(repayRecord.getExpectRepayInt().add(delayInterest));
				}
			}
			String oldActualRepayAmtStr = repayRecord.getActualRepayAmt();
			BigDecimal oldActualRepayAmt = StringUtils.toDecimal(oldActualRepayAmtStr);
			repayRecord.setActualRepayAmt(oldActualRepayAmt.add(repayAmount).toString());
			repayRecord.setActualRepayDate(new Date());
			
			//判断借条状态
			int distanceOfTwoDate = CalendarUtil.getIntervalDays2(partialAndDelay.getNowRepayDate(), new Date());//如果确认时借条状态有变更
			if(distanceOfTwoDate >= 0){
				loanRecord.setStatus(Status.penddingRepay);
				repayRecord.setStatus(NfsLoanRepayRecord.Status.partialDone);
			}else{
				repayRecord.setStatus(NfsLoanRepayRecord.Status.overdue);
				loanRecord.setStatus(Status.overdue);
			}
			save(repayRecord);

			
			loanRecordService.save(loanRecord);
			
			partialAndDelay.setStatus(NfsLoanPartialAndDelay.Status.agreed);
			loanPartialAndDelayService.save(partialAndDelay);
			
			operatingRecord.setNowRecord(loanRecord);
			operatingRecord.setRepaymentAmount(partialAndDelay.getPartialAmount());
			operatingRecord.setInitiator(LoanRole.loanee.getName());
			if( delayDays != 0) {
				operatingRecord.setType(NfsLoanOperatingRecord.Type.partialAndDelay);
			}else {
				operatingRecord.setType(NfsLoanOperatingRecord.Type.partial);
			}
			loanOperatingRecordService.save(operatingRecord);
			
			//合同实体
			loanContractService.createContract(loanRecord,SignatureType.youdun);
			
			//发送会员消息
			MemberMessage sendMessage = memberMessageService.sendMessage(MemberMessage.Type.confirmPartialRepaymentApplication,partialAndDelay.getId());
			//推送
			sendMsgService.beforeSendAppMsg(sendMessage);
					
			//TODO 这是放款人同意部分还款,借款人同意还没有code 以前就不对
			NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
			nfsLoanDetailMessage.setDetail(loanRecord.getLoanApplyDetail());
			nfsLoanDetailMessage.setMember(loanee);
			nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_2215);
			nfsLoanDetailMessage.setType(RecordMessage.LENDER_AGREE_PART);
			loanDetailMessageService.save(nfsLoanDetailMessage);
			
			memberPointService.updateMemberPoint(loanee, MemberPointRule.Type.staging, partialAndDelay.getPartialAmount());
			return true;
	}
	@Override
	public Integer countOverdueTimes(Long recordId) {
		return repayRecordDao.countOverdueTimes(recordId);
	}

	@Override
	public List<NfsLoanRepayRecord> findPendingRepayList(NfsLoanRepayRecord record) {
		
		return dao.findPendingRepayList(record);
	}
	
	@Transactional(readOnly=false)
	@Override
	public int repayAll(NfsLoanRecord loanRecord, BigDecimal dueRepayAmount,BigDecimal overdueInterest) {
			Member loanee = loanRecord.getLoanee();
		    BigDecimal shouldRepayAmount = dueRepayAmount.add(overdueInterest);
			Member loaner = memberService.get(loanRecord.getLoaner());
			NfsCrAuction crAuction = null;
			AuctionStatus beforeRepayStatus = loanRecord.getAuctionStatus();
			if(AuctionStatus.auction.equals(beforeRepayStatus)) {
				NfsCrAuction crAuctionParam = new NfsCrAuction();
				crAuctionParam.setLoanRecord(loanRecord);
				List<NfsCrAuction> crAuctions = auctionService.findList(crAuctionParam);
				crAuction = crAuctions.get(0);
				if(crAuction.getStatus().equals(NfsCrAuction.Status.successed)) {
					return Constant.UPDATE_FAILED;
				}
				crAuction.setStatus(NfsCrAuction.Status.failed);
				crAuction.setRmk("借款人已全额还款");
				crAuction.preUpdate();
				int updateLines = crAuctionDao.update(crAuction);
				if(updateLines == 0) {
					return Constant.UPDATE_FAILED;
				}
				loanRecord.setAuctionStatus(NfsLoanRecord.AuctionStatus.initial);
			}
			//如果是转让成功的借条还款给新的债权人
			if(AuctionStatus.auctioned.equals(beforeRepayStatus)) {
				NfsCrAuction crAuction1 = new NfsCrAuction();
				crAuction1.setLoanRecord(loanRecord);
				crAuction1.setStatus(NfsCrAuction.Status.successed);
				List<NfsCrAuction> crAuctions = auctionService.findList(crAuction1);
				if(crAuctions == null || crAuctions.size() == 0) {
					logger.error("已转让的借条记录ID:{}没有对应的已成交状态的债转记录,借款人还款失败！",loanRecord.getId());
					return Constant.UPDATE_FAILED;
				}
				crAuction = crAuctions.get(0);
				loaner = memberService.get(crAuction.getCrBuyer());
			}
			Integer code = Constant.UPDATE_FAILED;  
			code = actService.updateAct(TrxRuleConstant.REPAY_FULL_AVLBAL, shouldRepayAmount, loanee, loaner, loanRecord.getId());
			if(code == Constant.UPDATE_FAILED) {
				logger.error("借款单Id: {} 全部还款失败！借款人账户余额不足，扣款失败！",loanRecord.getId() );
				throw new RuntimeException("借款单Id: " + loanRecord.getId() + " 全部还款失败！借款人账户余额不足，扣款失败！");
			}
			code = actService.updateAct(TrxRuleConstant.REPAY_FULL_PENDING_REPAY, dueRepayAmount, loanee, loaner, loanRecord.getId());
			if(code == Constant.UPDATE_FAILED) {
				logger.error("借款单Id: {} 全部还款失败！待还待收账户更新失败！" , loanRecord.getId());
				throw new RuntimeException("借款单Id: " + loanRecord.getId() + " 全部还款失败！待还待收账户更新失败！");
			}
			
			//保存操作记录---全部还款
			NfsLoanOperatingRecord operatingRecord = new NfsLoanOperatingRecord();
			operatingRecord.setOldRecord(loanRecord);
			
			//更新借条
			loanRecord.setDueRepayAmount(BigDecimal.ZERO);
			loanRecord.setRepayedTerm(1);
			loanRecord.setDueRepayTerm(0);
			loanRecord.setStatus(Status.repayed);
			loanRecord.setCompleteDate(new Date());
			
			//关闭仲裁催收强执
			loanRecord = loanRecordService.closeArbitrationAndCollection(loanRecord);
			
			loanRecordService.save(loanRecord);
			
			//更新还款记录
			NfsLoanRepayRecord param = new NfsLoanRepayRecord();
			param.setLoan(loanRecord);
			List<NfsLoanRepayRecord> repayRecordsByLoanId = findList(param);
			NfsLoanRepayRecord repayRecord = repayRecordsByLoanId.get(0);
			BigDecimal oldActualRepayAmt = StringUtils.toDecimal(repayRecord.getActualRepayAmt());
			repayRecord.setActualRepayAmt(oldActualRepayAmt.add(shouldRepayAmount).toString());
			repayRecord.setActualRepayDate(new Date());
			repayRecord.setStatus(NfsLoanRepayRecord.Status.done);
			save(repayRecord);
			
			operatingRecord.setNowRecord(loanRecord);
			operatingRecord.setInitiator(LoanRole.loanee.getName());
			operatingRecord.setType(NfsLoanOperatingRecord.Type.totalDueAmount);
			loanOperatingRecordService.save(operatingRecord);
			//发送会员消息
			if(AuctionStatus.auctioned.equals(beforeRepayStatus)) {
				//发送会员消息
				memberMessageService.sendMessage(MemberMessage.Type.paySuccessedAuctionImsLoaner,crAuction.getId());
				
				sendSmsMsgServiceImpl.sendCollectionSms("paySuccessedAuctionImsLoaner", loaner.getUsername(), null);
				
			}else if(AuctionStatus.auction.equals(beforeRepayStatus)){
				//发送会员消息
				MemberMessage crMessage = memberMessageService.sendMessage(MemberMessage.Type.repayedAcutionImsLoaner,crAuction.getId());
				//发送会员消息
				MemberMessage message = memberMessageService.sendMessage(MemberMessage.Type.fullRepayment,loanRecord.getId());
				//推送
				sendMsgService.beforeSendAppMsg(crMessage);
				sendMsgService.beforeSendAppMsg(message);
			}else {
				//发送会员消息
				memberMessageService.sendMessage(MemberMessage.Type.fullRepayment,loanRecord.getId());
			}
			
			//生成对话
			NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
			nfsLoanDetailMessage.setDetail(loanRecord.getLoanApplyDetail());
			nfsLoanDetailMessage.setMember(loanee);
			nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_2109);
			nfsLoanDetailMessage.setType(RecordMessage.BORROWER_FULL_REPAYMENT);
			loanDetailMessageService.save(nfsLoanDetailMessage);
			
			int days = DateUtils.getDifferenceOfTwoDate( loanRecord.getDueRepayDate(),new Date());
			if (days > -15) {
				memberPointService.updateMemberPoint(loanee, MemberPointRule.Type.repay15, dueRepayAmount);
			} else if (days > -30) {
				memberPointService.updateMemberPoint(loanee, MemberPointRule.Type.repay30, dueRepayAmount);
			} else if (days > -45) {
				memberPointService.updateMemberPoint(loanee, MemberPointRule.Type.repay45, dueRepayAmount);
			}
			return Constant.UPDATE_SUCCESS;
	}
	@Transactional(readOnly=false)
	@Override
	public int stagesRepay(BigDecimal repayAmount,BigDecimal overdueInterest,NfsLoanRecord loanRecord,List<NfsLoanRepayRecord> shouldRepayRecords) {
			Member loanee = loanRecord.getLoanee();
			Member loaner = loanRecord.getLoaner();
			int repaySize = shouldRepayRecords.size();
			BigDecimal dueRepayAmount = repayAmount.subtract(overdueInterest);
			int code = actService.updateAct(TrxRuleConstant.REPAY_FULL_AVLBAL, repayAmount,loanee, loaner,loanRecord.getId());
			if(code == Constant.UPDATE_FAILED) {
				logger.error("可用余额账户更新失败！借条id{}",loanRecord.getId());
				throw new RuntimeException("账户更新失败！借条id"+loanRecord.getId());
			}
			int code2 = actService.updateAct(TrxRuleConstant.REPAY_FULL_PENDING_REPAY, dueRepayAmount, loanee, loaner, loanRecord.getId());
			if(code2 == Constant.UPDATE_FAILED) {
				logger.error("待收待还账户更新失败！借条id{}",loanRecord.getId());
				throw new RuntimeException("账户更新失败！借条id"+loanRecord.getId());
			}
			NfsLoanOperatingRecord operatingRecord = new NfsLoanOperatingRecord();
			operatingRecord.setType(NfsLoanOperatingRecord.Type.principalAndInterestByMonth);
			operatingRecord.setOldRecord(loanRecord);
			//更新loanRecord
			BigDecimal pendingRepayAmount = loanRecord.getDueRepayAmount().subtract(dueRepayAmount);//剩余待还金额
			loanRecord.setDueRepayAmount(pendingRepayAmount);
			int pendingRepayTerms = loanRecord.getDueRepayTerm() - repaySize; //剩余待还期数
			logger.info("分期借条{}剩余待还期数为{}",loanRecord.getId(),pendingRepayTerms);
			if(pendingRepayTerms != 0) {
				//未还清，取下一期还款计划的还款日期作为还款日期
				Integer nextSeqNum = shouldRepayRecords.get(shouldRepayRecords.size() - 1).getPeriodsSeq() + 1;
				NfsLoanRepayRecord repayRecord = new NfsLoanRepayRecord();
				repayRecord.setLoan(loanRecord);
				repayRecord.setPeriodsSeq(nextSeqNum);
				Date nextRepayDate = repayRecordDao.findList(repayRecord).get(0).getExpectRepayDate();
				loanRecord.setDueRepayDate(nextRepayDate);
			}
			loanRecord.setDueRepayTerm(pendingRepayTerms);
			loanRecord.setRepayedTerm(loanRecord.getRepayedTerm() == null ? repaySize : (loanRecord.getRepayedTerm() + repaySize));
			loanRecord.setStatus(NfsLoanRecord.Status.penddingRepay);
			if(pendingRepayTerms == 0) {
				//全部还清 修改状态和结清日期
				loanRecord.setCompleteDate(new Date());
				loanRecord.setStatus(NfsLoanRecord.Status.repayed);
				operatingRecord.setType(NfsLoanOperatingRecord.Type.totalDueAmount);
				
				//关闭仲裁强执催收
				loanRecord = loanRecordService.closeArbitrationAndCollection(loanRecord);
				
			}
			
			loanRecordService.save(loanRecord);
			
			//更新还款记录
			for (NfsLoanRepayRecord nfsLoanRepayRecord : shouldRepayRecords) {
				nfsLoanRepayRecord.setActualRepayAmt(nfsLoanRepayRecord.getExpectRepayAmt().toString());
				nfsLoanRepayRecord.setActualRepayDate(new Date());
				nfsLoanRepayRecord.setStatus(NfsLoanRepayRecord.Status.done);
				save(nfsLoanRepayRecord);
			}
			//更新操作记录
			operatingRecord.setNowRecord(loanRecord);
			operatingRecord.setInitiator(LoanRole.loanee.getName());
			operatingRecord.setRepaymentAmount(repayAmount);
			loanOperatingRecordService.save(operatingRecord);

			//发送会员消息
			memberMessageService.sendMessage(MemberMessage.Type.amortizationLoan,loanRecord.getId());
			NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
			nfsLoanDetailMessage.setDetail(loanRecord.getLoanApplyDetail());
			nfsLoanDetailMessage.setMember(loanee);
			nfsLoanDetailMessage.setType(RecordMessage.LENDER_PAID_REPAYMENT);
			//生成对话
			if(pendingRepayTerms != 0) {
				//没有还清
				 if(repaySize > 1){
					//还多期
					nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_2106);
				}else{
					//还1期
					nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_2105);
				}
			}else{
				//已还清
				nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_2109);
				nfsLoanDetailMessage.setType(RecordMessage.BORROWER_FULL_REPAYMENT);
			}
			loanDetailMessageService.save(nfsLoanDetailMessage);
			if (loanRecord.getDueRepayTerm() == 0) {
				memberPointService.updateMemberPoint(loanee, MemberPointRule.Type.stagingPayOff, dueRepayAmount);
			} else {
				memberPointService.updateMemberPoint(loanee, MemberPointRule.Type.staging, dueRepayAmount);
			}
			return Constant.UPDATE_SUCCESS;
	}


	@Override
	public BigDecimal sumLoanAmount(String loanIds) {
		return repayRecordDao.sumLoanAmount(loanIds);
	}
	
	
}