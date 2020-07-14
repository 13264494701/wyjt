package com.jxf.loan.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.ebaoquan.rop.thirdparty.com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.fee.service.NfsFeeRuleService;
import com.jxf.loan.constant.LoanConstant;
import com.jxf.loan.constant.RecordMessage;
import com.jxf.loan.dao.NfsLoanApplyDetailDao;
import com.jxf.loan.dao.NfsLoanRecordDao;
import com.jxf.loan.entity.NfsCrAuction;
import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanApply.LoanRole;
import com.jxf.loan.entity.NfsLoanApply.LoanType;
import com.jxf.loan.entity.NfsLoanApply.RepayType;
import com.jxf.loan.entity.NfsLoanApply.TrxType;
import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.entity.NfsLoanApplyDetail.AliveVideoStatus;
import com.jxf.loan.entity.NfsLoanApplyDetail.DisputeResolution;
import com.jxf.loan.entity.NfsLoanApplyDetail.IntStatus;
import com.jxf.loan.entity.NfsLoanArbitration;
import com.jxf.loan.entity.NfsLoanCollection;
import com.jxf.loan.entity.NfsLoanContract;
import com.jxf.loan.entity.NfsLoanContract.SignatureType;
import com.jxf.loan.entity.NfsLoanDetailMessage;
import com.jxf.loan.entity.NfsLoanOperatingRecord;
import com.jxf.loan.entity.NfsLoanPartialAndDelay;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRecord.ArbitrationStatus;
import com.jxf.loan.entity.NfsLoanRecord.AuctionStatus;
import com.jxf.loan.entity.NfsLoanRecord.CollectionStatus;
import com.jxf.loan.entity.NfsLoanRecord.DelayStatus;
import com.jxf.loan.entity.NfsLoanRecord.LineDownStatus;
import com.jxf.loan.entity.NfsLoanRecord.PartialStatus;
import com.jxf.loan.entity.NfsLoanRecord.Status;
import com.jxf.loan.entity.NfsLoanRepayRecord;
import com.jxf.loan.entity.NfsLoanReport;
import com.jxf.loan.service.NfsCrAuctionService;
import com.jxf.loan.service.NfsLoanApplyDetailService;
import com.jxf.loan.service.NfsLoanApplyService;
import com.jxf.loan.service.NfsLoanArbitrationExecutionService;
import com.jxf.loan.service.NfsLoanArbitrationService;
import com.jxf.loan.service.NfsLoanCollectionService;
import com.jxf.loan.service.NfsLoanContractService;
import com.jxf.loan.service.NfsLoanDetailMessageService;
import com.jxf.loan.service.NfsLoanOperatingRecordService;
import com.jxf.loan.service.NfsLoanPartialAndDelayService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.service.NfsLoanRepayRecordService;
import com.jxf.loan.utils.LoanUtils;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberLoanReport;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.entity.MemberPointRule;
import com.jxf.mem.service.MemberActService;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberPointService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.MemUtils;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.service.NfsActService;
import com.jxf.svc.config.Constant;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.persistence.sequence.utils.SequenceUtils;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.gxt.CreditInfoResponseResult;
import com.jxf.web.model.gxt.LoanDetailForGxtResponseResult;
import com.jxf.web.model.gxt.LoanDetailForGxtResponseResult.AfterDelayInfo;
import com.jxf.web.model.gxt.LoanDetailForGxtResponseResult.LoanInfo;
import com.jxf.web.model.gxt.LoanDetailForGxtResponseResult.OverDueInfo;
import com.jxf.web.model.gxt.LoanDetailForGxtResponseResult.TradeInfo;
import com.jxf.web.model.gxt.LoanDetailForGxtResponseResult.UserInfo;
import com.jxf.web.model.wyjt.app.auction.CrTransferListRequestParam;
import com.jxf.web.model.wyjt.app.auction.CrTransferListResponseResult;
import com.jxf.web.model.wyjt.app.auction.CrTransferListResponseResult.CrTransferList;
import com.jxf.web.model.wyjt.app.loan.LoanDetailForAppResponseResult;
import com.jxf.web.model.wyjt.app.loan.LoanDetailForAppResponseResult.Detail;
import com.jxf.web.model.wyjt.app.loan.LoanDetailForAppResponseResult.LoanMessage;


/**
 * 借条记录ServiceImpl
 * 
 * @author wo
 * @version 2018-10-10
 */
@Service("nfsLoanRecordService")
@Transactional(readOnly = true)
public class NfsLoanRecordServiceImpl extends CrudServiceImpl<NfsLoanRecordDao, NfsLoanRecord>
		implements NfsLoanRecordService {
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberActService memberActService;
	@Autowired
	private NfsLoanRecordDao loanRecordDao;
	@Autowired
	private NfsLoanApplyDetailDao applyDetailDao;
	@Autowired
	private NfsLoanApplyDetailService loanApplyDetailService;
	@Autowired
	private NfsLoanCollectionService loanCollectionService;
	@Autowired
	private NfsLoanArbitrationService loanArbitrationService;
	@Autowired
	private NfsLoanApplyService loanApplyService;
	@Autowired
	private NfsLoanDetailMessageService loanDetailMessageService;
	@Autowired
	private NfsActService actService;
	@Autowired
	private NfsFeeRuleService feeRuleService;
	@Autowired
	private NfsLoanContractService loanContractService;
	@Autowired
	private NfsLoanRepayRecordService loanRepayRecordService;
	@Autowired
	private MemberPointService memberPointService;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private SendSmsMsgService sendSmsMsgService;
	@Autowired
	private NfsLoanOperatingRecordService loanOperatingRecordService;
	@Autowired
	private NfsLoanArbitrationExecutionService loanArbitrationExecutionService;
    @Autowired
    private NfsLoanPartialAndDelayService nfsLoanPartialAndDelayService;
    @Autowired
    private NfsCrAuctionService crAuctionService;

	public NfsLoanRecord get(Long id) {
		return super.get(id);
	}

	public List<NfsLoanRecord> findList(NfsLoanRecord nfsLoanRecord) {
		return super.findList(nfsLoanRecord);
	}

	public Page<NfsLoanRecord> findPage(Page<NfsLoanRecord> page, NfsLoanRecord nfsLoanRecord) {
		return super.findPage(page, nfsLoanRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsLoanRecord loanRecord) {
		if (loanRecord.getIsNewRecord()) {
			String loanNo = DateUtils.getDate("yyyyMMdd") + SequenceUtils.getSequence("WYJT_LOAN", 8);
			loanRecord.setLoanNo(loanNo);			
			loanRecord.setRepayedTerm(0);
			loanRecord.setStatus(NfsLoanRecord.Status.penddingRepay);
			loanRecord.setPartialStatus(NfsLoanRecord.PartialStatus.initial);
			loanRecord.setDelayStatus(NfsLoanRecord.DelayStatus.initial);
			loanRecord.setLineDownStatus(NfsLoanRecord.LineDownStatus.initial);
			loanRecord.setArbitrationStatus(NfsLoanRecord.ArbitrationStatus.initial);
			loanRecord.setCollectionStatus(NfsLoanRecord.CollectionStatus.initial);
			loanRecord.setCollectionTimes(0);
			loanRecord.setAuctionStatus(NfsLoanRecord.AuctionStatus.initial);
			loanRecord.preInsert();
			loanRecordDao.insert(loanRecord);
		} else {
			loanRecord.preUpdate();
			loanRecordDao.update(loanRecord);
		}
	}

	@Transactional(readOnly = false)
	public void delete(NfsLoanRecord nfsLoanRecord) {
		super.delete(nfsLoanRecord);
	}
	
	@Override
	@Transactional(readOnly = false)
	public NfsLoanRecord createLoanRecord(NfsLoanApplyDetail applyDetail,Integer trxType) {
		
		NfsLoanApply loanApply = loanApplyService.get(applyDetail.getApply());
		Member loaner = loanApply.getLoanRole().equals(NfsLoanApply.LoanRole.loaner)?loanApply.getMember():applyDetail.getMember();
		Member loanee = loanApply.getLoanRole().equals(NfsLoanApply.LoanRole.loanee)?loanApply.getMember():applyDetail.getMember();
		loaner = memberService.get(loaner);
		loanee = memberService.get(loanee);
		BigDecimal amount = applyDetail.getAmount();//本金
		
		//变更detail
		applyDetail.setStatus(NfsLoanApplyDetail.Status.success);
		applyDetail.setProgress("");
		applyDetail.preUpdate();
		int updateLines = applyDetailDao.update(applyDetail);
		if(updateLines == 0) {
			return null;
		}
		BigDecimal fee = feeRuleService.getFee(TrxRuleConstant.LOAN_DONE_FEE, amount);
		//生成借款单
		NfsLoanRecord loanRecord = LoanUtils.calInt(amount, loanApply.getIntRate(), loanApply.getRepayType(), loanApply.getTerm());
		loanRecord.setLoaner(loaner);
		loanRecord.setLoanee(loanee);
		loanRecord.setLoanType(loanApply.getLoanType());
		loanRecord.setLoanPurp(loanApply.getLoanPurp());
		loanRecord.setRepayType(loanApply.getRepayType());
		loanRecord.setAmount(amount);
		loanRecord.setIntRate(loanApply.getIntRate());
		loanRecord.setTerm(loanApply.getTerm());
		loanRecord.setFee(fee);
		loanRecord.setChannel(loanApply.getChannel());
		loanRecord.setLoanApplyDetail(applyDetail);
		loanRecord.setProgress("");
		loanRecord.setDisputeResolution(applyDetail.getDisputeResolution());
		loanRecord.setRequireAliveVideo(0);
		loanRecord.setTrxType(NfsLoanApply.TrxType.online);
		Date loanStart = DateUtils.parseDate("1900-01-01 00:00:01");
		loanRecord.setLoanStart(loanStart);
		loanRecord.setLoaneeDelete(false);
		loanRecord.setLoanerDelete(false);
		save(loanRecord);

		
		int code = 0;
		if(trxType == TrxRuleConstant.LOANER_AGREE_LOAN||trxType ==TrxRuleConstant.LOANER_MULTI_LOAN) {
			code = actService.updateAct(TrxRuleConstant.LOAN_DONE_AVLAMT, amount, loaner, loanee, loanRecord.getId());
			if(code == Constant.UPDATE_FAILED) {
				throw new RuntimeException("借款申请单DetailId: " + applyDetail.getId() + " 可用余额账户更新失败，生成借条失败！");
			}
		}else if(trxType ==TrxRuleConstant.LOANER_PASS_VIDEO||trxType == TrxRuleConstant.LOANEE_AGREE_ACTIVE_LOAN) {
			code = actService.updateAct(TrxRuleConstant.LOAN_DONE_FROZENAMT, amount, loaner, loanee, loanRecord.getId());
			if(code == Constant.UPDATE_FAILED) {
				throw new RuntimeException("借款申请单DetailId: " + applyDetail.getId() + " 冻结账户更新失败，生成借条失败！");
			}
		}
		if(trxType ==TrxRuleConstant.LOANER_MULTI_LOAN) {
			BigDecimal interest = LoanUtils.getBigDecimalInterest(loanApply.getIntRate(), amount, loanApply.getTerm());
			code = actService.updateAct(TrxRuleConstant.LOAN_DONE_REPAY, amount.add(interest), loaner, loanee, loanRecord.getId());
			if(code == Constant.UPDATE_FAILED) {
				throw new RuntimeException("借款申请单DetailId: " + applyDetail.getId() + " 多人借款待收待还账户更新失败，生成借条失败！");
			}
		}else {
			code = actService.updateAct(TrxRuleConstant.LOAN_DONE_REPAY, amount.add(loanApply.getInterest()), loaner, loanee, loanRecord.getId());
			if(code == Constant.UPDATE_FAILED) {
				throw new RuntimeException("借款申请单DetailId: " + applyDetail.getId() + " 待收待还账户更新失败，生成借条失败！");
			}
		}
		//收手续费
		code = actService.updateAct(TrxRuleConstant.LOAN_DONE_FEE,fee,loanee, loanRecord.getId());
		if(code == Constant.UPDATE_FAILED) {
			throw new RuntimeException("借款申请单DetailId: " + applyDetail.getId() + " 借款账户收取手续费更新失败，生成借条失败！");
		}
		
		//还款计划保存待优化
		List<NfsLoanRepayRecord> repayRecords = loanRecord.getRepayRecordList();
		for (NfsLoanRepayRecord loanRepayRecord : repayRecords) {
			loanRepayRecord.setLoan(loanRecord);
			loanRepayRecordService.save(loanRepayRecord);
		}
		//保存操作记录---生成借条
		NfsLoanOperatingRecord operatingRecord = new NfsLoanOperatingRecord();
		operatingRecord.setOldRecord(loanRecord);
		operatingRecord.setNowRecord(loanRecord);
		operatingRecord.setInitiator(loanApply.getLoanRole().getName());
		operatingRecord.setType(NfsLoanOperatingRecord.Type.create);
		loanOperatingRecordService.save(operatingRecord);
		
		//生成电子合同
		loanContractService.createContract(loanRecord,SignatureType.youdun);
		//信誉积分
		memberPointService.updateMemberPoint(loaner, MemberPointRule.Type.agreeLoan, amount);
		//手续费对话
		NfsLoanDetailMessage feeMessage = new NfsLoanDetailMessage();
		feeMessage.setDetail(applyDetail);
		Member member = new Member();
		member.setId(1L);
		feeMessage.setMember(member);
		feeMessage.setMessageId(RecordMessage.CHAT_7);
		feeMessage.setType(RecordMessage.FORMALITIES_FEE);
		feeMessage.setNote(fee+"");
		loanDetailMessageService.save(feeMessage);	
		
		if(trxType == TrxRuleConstant.LOANER_AGREE_LOAN) {
			memberMessageService.sendMessage(MemberMessage.Type.successfulPaymentOne,loanRecord.getId());
			
			//发短信
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("name", loaner.getName());
			sendSmsMsgService.sendMessage("successfulPaymentOne", loanee.getUsername(), map);
			
			//生成对话
			NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
			nfsLoanDetailMessage.setDetail(applyDetail);
			nfsLoanDetailMessage.setMember(loaner);
			nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_1201);
			nfsLoanDetailMessage.setType(RecordMessage.LENDER_PAID_REPAYMENT);
			loanDetailMessageService.save(nfsLoanDetailMessage);
		}else if(trxType == TrxRuleConstant.LOANER_MULTI_LOAN) {
			//更改剩余可借余额
			loanApply.setRemainAmount(loanApply.getRemainAmount().subtract(amount));
			loanApplyService.save(loanApply);
			
			//更改收到的借款申请消息类型
			MemberMessage message = new MemberMessage();
			message.setOrgId(applyDetail.getId());
			message.setOrgType(LoanConstant.TYPE_DETAIL + "");
			message.setType(MemberMessage.Type.multiplayerLoan);
			message.setMember(loaner);
			List<MemberMessage> messages = memberMessageService.findList(message);
			if(messages != null && messages.size() > 0) {
				MemberMessage message2 = messages.get(0);
				message2.setType(MemberMessage.Type.multiplayersuccessfulPayment);
				memberMessageService.save(message2);
			}
			
			//发送会员消息
			Map<String, Object> tradDetail = new HashMap<String,Object>();
			tradDetail.put("name", loaner.getName());
			tradDetail.put("loanAmount", StringUtils.decimalToStr(loanApply.getAmount(), 2));
			tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
			tradDetail.put("status", "借条已达成");
			tradDetail.put("colorStatus", "2");
			tradDetail.put("type", "0");
			String jsonString = JSON.toJSONString(tradDetail);
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("name", loaner.getName());
			map.put("money", StringUtils.decimalToStr(loanApply.getAmount(), 2));
			map.put("borrowMoney", StringUtils.decimalToStr(amount, 2));
			map.put("remainMoney", StringUtils.decimalToStr(loanApply.getRemainAmount(), 2));
			memberMessageService.sendMessage(MemberMessage.Type.multiplayersuccessfulPayment,MemberMessage.Group.appLoanMessage,  
					loanApply.getMember(), map,loanApply.getId(),"0",jsonString);
			
			//生成对话
			NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
			nfsLoanDetailMessage.setDetail(applyDetail);
			nfsLoanDetailMessage.setMember(loaner);
			nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_1201);
			nfsLoanDetailMessage.setType(RecordMessage.LENDER_PAID_REPAYMENT);
			loanDetailMessageService.save(nfsLoanDetailMessage);
			
		}else if(trxType == TrxRuleConstant.LOANER_PASS_VIDEO) {
			memberMessageService.sendMessage(MemberMessage.Type.successfulAudit,loanRecord.getId());
			
			//发短信
			Map<String,Object> sendMessageMap = new HashMap<String,Object>();
			sendMessageMap.put("name", loaner.getName());
			loanee = memberService.get(loanee);
			sendSmsMsgService.sendMessage(MemberMessage.Type.successfulAudit.name(), loanee.getUsername(), sendMessageMap);
			
			//收到钱的对话
			NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
			nfsLoanDetailMessage.setMember(loaner);
			nfsLoanDetailMessage.setDetail(applyDetail);
			nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_1206);
			nfsLoanDetailMessage.setType(RecordMessage.LENDER_PAID_REPAYMENT);
			loanDetailMessageService.save(nfsLoanDetailMessage);
		}else if(trxType == TrxRuleConstant.LOANEE_AGREE_ACTIVE_LOAN) {
			//生成对话
			NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
			nfsLoanDetailMessage.setDetail(applyDetail);
			nfsLoanDetailMessage.setMember(loanee);
			nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_1104);
			nfsLoanDetailMessage.setType(RecordMessage.LENDER_PAID_REPAYMENT);
			loanDetailMessageService.save(nfsLoanDetailMessage);
			
			//发送会员消息
			memberMessageService.sendMessage(MemberMessage.Type.confirmLendApplication,loanRecord.getId());
		}
		return loanRecord;

	}
	
	@Override
	@Transactional(readOnly = false)
	public NfsLoanRecord createLoanRecordForOffLine(NfsLoanApplyDetail applyDetail) {
		
		NfsLoanApply loanApply = loanApplyService.get(applyDetail.getApply());
		Member loaner = loanApply.getLoanRole().equals(NfsLoanApply.LoanRole.loaner)?loanApply.getMember():applyDetail.getMember();
		Member loanee = loanApply.getLoanRole().equals(NfsLoanApply.LoanRole.loanee)?loanApply.getMember():applyDetail.getMember();
		loaner = memberService.get(loaner);
		loanee = memberService.get(loanee);
		
		BigDecimal amount = applyDetail.getAmount();//本金
		
		//变更detail
		applyDetail.setStatus(NfsLoanApplyDetail.Status.success);
		applyDetail.setProgress("");
		applyDetail.preUpdate();
		int updateLines = applyDetailDao.update(applyDetail);
		if(updateLines == 0) {
			return null;
		}
		BigDecimal fee = new BigDecimal(Global.getConfig("gxt.loanDoneFee"));
		//生成借款单
		Date loanStart = loanApply.getLoanStart();//借款日期
		int term = loanApply.getTerm();
		BigDecimal intRate = loanApply.getIntRate();
		NfsLoanApply.RepayType repayType = loanApply.getRepayType();
		
		NfsLoanRecord loanRecord = LoanUtils.calIntForOffline(amount, intRate, repayType, term,loanStart);
		
		loanRecord.setLoaner(loaner);
		loanRecord.setLoanee(loanee);
		loanRecord.setLoanType(loanApply.getLoanType());
		loanRecord.setLoanPurp(loanApply.getLoanPurp());
		loanRecord.setRepayType(repayType);
		loanRecord.setAmount(amount);
		loanRecord.setIntRate(intRate);
		loanRecord.setTerm(term);
		loanRecord.setFee(fee);
		loanRecord.setChannel(loanApply.getChannel());
		loanRecord.setLoanApplyDetail(applyDetail);
		loanRecord.setProgress("");
		loanRecord.setDisputeResolution(applyDetail.getDisputeResolution());
		loanRecord.setRequireAliveVideo(0);
		loanRecord.setLoanStart(loanStart);
		loanRecord.setTrxType(NfsLoanApply.TrxType.offline);
		loanRecord.setLoaneeDelete(false);
		loanRecord.setLoanerDelete(false);
		save(loanRecord);

		//还款计划保存待优化
		List<NfsLoanRepayRecord> repayRecords = loanRecord.getRepayRecordList();
		for (NfsLoanRepayRecord loanRepayRecord : repayRecords) {
			loanRepayRecord.setLoan(loanRecord);
			loanRepayRecordService.save(loanRepayRecord);
		}
		//保存操作记录---生成借条
		NfsLoanOperatingRecord operatingRecord = new NfsLoanOperatingRecord();
		operatingRecord.setOldRecord(loanRecord);
		operatingRecord.setNowRecord(loanRecord);
		operatingRecord.setInitiator(loanApply.getLoanRole().getName());
		operatingRecord.setType(NfsLoanOperatingRecord.Type.create);
		loanOperatingRecordService.save(operatingRecord);
		
		// 增加待收待还
		int code = actService.updateAct(TrxRuleConstant.GXT_LOAN_DONE_REPAY_RECEIVE, loanRecord.getDueRepayAmount(),loaner, loanee, loanRecord.getId());
		if (code == Constant.UPDATE_FAILED) {
			logger.error("公信堂会员{}同意补借条申请{}待收待还账户更新失败！", loanee.getId(), applyDetail.getId());
			throw new RuntimeException("公信堂会员" + loanee.getId() + "同意补借条申请" + applyDetail.getId() + "待收待还账户更新失败！");
		}
		//生成电子合同
		loanContractService.createContract(loanRecord,SignatureType.youdun);
		//信誉积分
		memberPointService.updateMemberPoint(loaner, MemberPointRule.Type.agreeLoan, amount);
		
		LoanRole loanRole = applyDetail.getLoanRole();
		
		//发消息
		if(loanRole.equals(LoanRole.loanee)){
			memberMessageService.sendMessage(MemberMessage.Type.agreeApplicationLoanee,loanRecord.getId());
		}else{
			memberMessageService.sendMessage(MemberMessage.Type.agreeApplicationLoaner,loanRecord.getId());
		}
		return loanRecord;
	}
	
	
	
	public Page<NfsLoanRecord> findPageForUfang(Page<NfsLoanRecord> page, NfsLoanRecord loanRecord) {	
		loanRecord.setPage(page);
		List<NfsLoanRecord> loanList = loanRecordDao.findListForUfang(loanRecord);
		page.setList(loanList);
		return page;
	}
	
	@Override
	public Page<NfsLoanRecord> findPage(NfsLoanRecord loanRecord, Integer pageNo, Integer pageSize) {

		Page<NfsLoanRecord> page = new Page<NfsLoanRecord>(pageNo == null ? 1 : pageNo,
				pageSize == null ? 20 : pageSize);
		String orderBy = loanRecord.getPage().getOrderBy();
		page.setOrderBy(orderBy);
		loanRecord.setPage(page);
		List<NfsLoanRecord> loanList = loanRecordDao.findList(loanRecord);
		page.setList(loanList);
		return page;
	}

	@Override
	public NfsLoanRecord listCase(Long id) {
		return loanRecordDao.listCase(id);
	}

	@Override
	public void loanReport(MemberLoanReport memberLoanReport) {
		Long id = memberLoanReport.getId();
		Integer type = memberLoanReport.getType();

		if(type == null) {
			type = 7;
		}
		
		NfsLoanReport daihuan = loanRecordDao.daihuan(0, id, type);
		memberLoanReport.setPendingReceiveAmt(daihuan.getAmount() == null ? new BigDecimal("0") : daihuan.getAmount());
		memberLoanReport.setPendingReceiveQuantity(daihuan.getQuantity());

		NfsLoanReport daishou = loanRecordDao.daishou(0, id, type);
		memberLoanReport
				.setPendingRepaymentAmt(daishou.getAmount() == null ? new BigDecimal("0") : daishou.getAmount());
		memberLoanReport.setPendingRepaymentQuantity(daishou.getQuantity());

		NfsLoanReport yuqidaihuan = loanRecordDao.daihuan(2, id, type);
		memberLoanReport.setOverduePendingRepayAmt(
				yuqidaihuan.getAmount() == null ? new BigDecimal("0") : yuqidaihuan.getAmount());
		memberLoanReport.setOverduePendingRepayQuantity(yuqidaihuan.getQuantity());

		NfsLoanReport yuqiyihuan = loanRecordDao.yuqiyihuan(2, id, type);
		memberLoanReport
				.setOverdueRepayedAmt(yuqiyihuan.getAmount() == null ? new BigDecimal("0") : yuqiyihuan.getAmount());
		memberLoanReport.setOverdueRepayedQuantity(yuqiyihuan.getQuantity());

		NfsLoanReport jieru = loanRecordDao.jieru(id, type);
		memberLoanReport.setLoanInAmt(jieru.getAmount() == null ? new BigDecimal("0") : jieru.getAmount());
		memberLoanReport.setLoanInQuantity(jieru.getQuantity());

		NfsLoanReport jiechu = loanRecordDao.jiechu(id, type);
		memberLoanReport.setLoanOutAmt(jiechu.getAmount() == null ? new BigDecimal("0") : jiechu.getAmount());
		memberLoanReport.setLoanOutQuantity(jiechu.getQuantity());

		NfsLoanReport yihuan = loanRecordDao.yihuan(1, id, type);
		memberLoanReport.setOnTimeRepayedAmt(yihuan.getAmount() == null ? new BigDecimal("0") : yihuan.getAmount());
		memberLoanReport.setOnTimeRepayedQuantity(yihuan.getQuantity());

		NfsLoanReport yihuanyuqi = loanRecordDao.yihuanyuqi(1, id, type);
		memberLoanReport
				.setDelayRepayedAmt(yihuanyuqi.getAmount() == null ? new BigDecimal("0") : yihuanyuqi.getAmount());
		memberLoanReport.setDelayRepayedQuantity(yihuanyuqi.getQuantity());

	}

	@Override
	public LoanDetailForAppResponseResult getDetail(String loanId,Integer type,Member member) {
		LoanDetailForAppResponseResult result = new LoanDetailForAppResponseResult();
		
		NfsLoanApplyDetail applyDetail = null;
		NfsLoanRecord loanRecord = null;
		NfsLoanApply nfsLoanApply = null;

		String buttomId = "";
		result.setLoanStatus(2);
		boolean isLoaner = true;
		
		//处理刷新机制
		if(type == 1){//detail此时可能生成record了
			//判断是否已经生成借款单
			NfsLoanApplyDetail nfsLoanApplyDetail = loanApplyDetailService.get(Long.parseLong(loanId));
			com.jxf.loan.entity.NfsLoanApplyDetail.Status detailStatus = nfsLoanApplyDetail.getStatus();
			if(detailStatus.equals(com.jxf.loan.entity.NfsLoanApplyDetail.Status.success)){//生成借条了
				NfsLoanRecord record = loanRecordService.findByApplyDetailId(Long.parseLong(loanId));
				Long recordId = record.getId();
				loanId = recordId.toString();
				type = 2;
			}
		}
		
		if(type == 0){
			/**
			 * 查apply
			 */
			nfsLoanApply = loanApplyService.get(Long.valueOf(loanId));
			LoanRole loanRole = nfsLoanApply.getLoanRole();
			if(loanRole.equals(LoanRole.loanee)){
				isLoaner = false;
			}
			NfsLoanApplyDetail nfsLoanApplyDetail = new NfsLoanApplyDetail();
			nfsLoanApplyDetail.setApply(nfsLoanApply);
			List<NfsLoanApplyDetail> findApplyDetailList = loanApplyDetailService.findList(nfsLoanApplyDetail);
			List<Detail> detailList = result.getDetailList();
			if (findApplyDetailList != null && findApplyDetailList.size() > 0) {
				for (NfsLoanApplyDetail loanApplyDetail : findApplyDetailList) {
					Detail detail = new LoanDetailForAppResponseResult().new Detail();
					detail.setHeadImage(loanApplyDetail.getMember().getHeadImage());
					detail.setMemberName(loanApplyDetail.getMember().getName());
					if (loanApplyDetail.getStatus().equals(NfsLoanApplyDetail.Status.success)) {//借到钱了才显示
						NfsLoanRecord record = loanRecordService.findByApplyDetailId(loanApplyDetail.getId());
						detail.setAmount(record.getAmount().setScale(0, BigDecimal.ROUND_HALF_UP).toString());
						detail.setLoanId(record.getId().toString());
						detail.setType(LoanConstant.TYPE_RECORD);
						String progress = loanRecordService.getRecordProgress(record,member);
						detail.setProgress(progress);
						detailList.add(detail);
					} 
				}
			}
			result.setAmount(nfsLoanApply.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());

			result.setHadBorrowAmount(nfsLoanApply.getAmount().subtract(nfsLoanApply.getRemainAmount())
					.setScale(2, BigDecimal.ROUND_HALF_UP).intValue()+"");// 已借金额

			result.setRemainAmount(nfsLoanApply.getRemainAmount().setScale(0, BigDecimal.ROUND_HALF_UP).intValue() + "");// 剩余可借额度

			result.setIntRate(nfsLoanApply.getIntRate().setScale(1, BigDecimal.ROUND_HALF_UP).toString());// 年化利率
			Date createTime = nfsLoanApply.getCreateTime();// 创建时间
			Integer term = nfsLoanApply.getTerm();
			Date dueRepayDate = DateUtils.addCalendarByDate(createTime, term-1);
			result.setDueRepayDate(DateUtils.formatDate(dueRepayDate, "yyyy-MM-dd"));
			result.setDetailList(detailList);
			result.setType(LoanConstant.TYPE_APPLY);
		}else if(type == 1){
			/**
			 * 查detail
			 */
			applyDetail = loanApplyDetailService.get(Long.valueOf(loanId));
			IntStatus intStatus = applyDetail.getIntStatus();
			result.setTradeCode(loanId);// 交易码
			NfsLoanApply loanApply = applyDetail.getApply();
			loanApply = loanApplyService.get(loanApply.getId());
			Member applyMember = loanApply.getMember();
			if (member.getId().equals(applyDetail.getMember().getId())) {// 名字和ID
				applyMember = memberService.get(applyMember);
				result.setRealName(applyMember.getName());
				applyMember = MemUtils.maskIdNo(applyMember);
				result.setIdNo(applyMember.getIdNo());
				result.setFriendId(applyMember.getId().toString());
				result.setLoaneeHeadImage(applyMember.getHeadImage());//我收到的多人借款需要借款人头像
			} else {
				Member detailMember = memberService.get(applyDetail.getMember());
				result.setRealName(detailMember.getName());
				detailMember = MemUtils.maskIdNo(detailMember);
				result.setIdNo(detailMember.getIdNo());
				result.setFriendId(detailMember.getId().toString());
			}

			BigDecimal amount = loanApply.getAmount();// 借款金额
			result.setAmount(StringUtils.decimalToStr(amount, 2));
			Date createTime = applyDetail.getCreateTime();// 申请时间
			result.setCreateDate(DateUtils.formatDate(createTime, "yyyy-MM-dd"));

			BigDecimal interest = loanApply.getInterest();// 利息
			if (interest != null) {
				result.setInterest(interest.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			} else {
				result.setInterest("0");
			}
			BigDecimal avlBal = memberActService.getAvlBal(member);

			result.setAvailableAmount(avlBal.setScale(2, BigDecimal.ROUND_HALF_UP).intValue() + "");

			result.setLoanPurp(loanApply.getLoanPurp().getName());// 用途
			
			LoanType loanType = loanApply.getLoanType();
			
			if (loanType.equals(LoanType.multiple)) {

				BigDecimal remainAmount = loanApply.getRemainAmount();
				result.setRemainAmount(remainAmount.setScale(0, BigDecimal.ROUND_HALF_UP).intValue() + "");
			}
			RepayType repayType = loanApply.getRepayType();
			if(repayType.equals(RepayType.principalAndInterestByMonth)){
				result.setShowPlanButton(1);
			}
			/**
			 * 底部按钮
			 */
			LoanRole detailLoanRole = applyDetail.getLoanRole();
			LoanRole applyLoanRole = loanApply.getLoanRole();
			
			isLoaner = (detailLoanRole.equals(NfsLoanApply.LoanRole.loaner)
					&& applyDetail.getMember().getId().equals(member.getId()))
					|| (applyLoanRole.equals(NfsLoanApply.LoanRole.loaner)
							&& loanApply.getMember().getId().equals(member.getId()));
			/**
			 * 借款人已发起借款申请，等待确认付款 同意修改利息，等待放款人付款 拒绝修改利息，等待放款人付款
			 */
			if (applyDetail.getStatus().equals(NfsLoanApplyDetail.Status.pendingAgree)) {
				buttomId = isLoaner ? "2001|2021" : "2006|2017";
				result.setLoanStatus(1);
				if (isLoaner) {
					if (intStatus.equals(NfsLoanApplyDetail.IntStatus.primary) 
							&& loanApply.getRepayType().equals(RepayType.oneTimePrincipalAndInterest) 
							&& applyDetail.getMember().getId().equals(member.getId())) {
						result.setShowModifyRate(1); // 显示修改利息按钮
					}
					if(loanApply.getMember().getId().equals(member.getId())){//主动放款
						result.setShowButton(0);
					}else{
						result.setShowButton(2);
					}
					result.setLoanStatus(3);
				} else {
					result.setLoanStatus(16);
				}

				/**
				 * 放款人已发起借款申请并支付，等待借款人确认
				 */
				if (applyLoanRole.equals(NfsLoanApply.LoanRole.loaner)) {
					buttomId = isLoaner ? "2015" : "2004|2009";
				}
				/**
				 * 放款人修改利息，等待借款人确认
				 */
				if (intStatus.equals(NfsLoanApplyDetail.IntStatus.changed)) {
					buttomId = isLoaner ? "2001" : "2031|2032";
				}
				/**
				 * 放款人已付款，等待上传视频
				 */
				AliveVideoStatus aliveVideoStatus = applyDetail.getAliveVideoStatus();
				if (aliveVideoStatus.equals(NfsLoanApplyDetail.AliveVideoStatus.pendingUpload)) {
					result.setVideoStatus(0);
					buttomId = isLoaner ? "2016" : "2006|2007";
				} else if (aliveVideoStatus.equals(NfsLoanApplyDetail.AliveVideoStatus.pendingReUpload)) {
					/**
					 * 视频审核未通过，等待重新上传
					 */
					result.setVideoStatus(2);
					buttomId = isLoaner ? "2020" : "2006|2007";
				} else if (aliveVideoStatus.equals(NfsLoanApplyDetail.AliveVideoStatus.hasUpload)) {
					/**
					 * 视频已上传，等待审核
					 */
					result.setVideoStatus(1);
					buttomId = isLoaner ? "2008" : "2018";
					String videoUrl = applyDetail.getId()<=7517375?Global.getConfig("domain")+applyDetail.getVideoUrl():applyDetail.getVideoUrl();
					result.setVideoReviewUrl(videoUrl);
				}
			}
			if (!applyDetail.getAliveVideoStatus().equals(AliveVideoStatus.passed)) {
				Date applyCreateTime = loanApply.getCreateTime();
				Integer term = loanApply.getTerm();
				Member loaner = null;
				Member loanee = null;
				if (detailLoanRole.equals(NfsLoanApply.LoanRole.loaner)) {
					loaner = memberService.get(applyDetail.getMember().getId());
					loanee = memberService.get(loanApply.getMember().getId());
				} else {
					loaner = memberService.get(loanApply.getMember().getId());
					loanee = memberService.get(applyDetail.getMember().getId());
				}

				Date repayLatestDate = DateUtils.addCalendarByDate(applyCreateTime, term-1);
				String[] dateymd = new SimpleDateFormat("yyyy-M-d").format(repayLatestDate).split("-");
				result.setVideoReadStr(LoanConstant.NEED_VIDEO_CHECK.replace("${loanee}", loanee.getName())
						.replace("${loaner}", loaner.getName())
						.replace("${amount}", applyDetail.getAmount().setScale(0, BigDecimal.ROUND_HALF_UP).toString())
						.replace("${interset}",
								loanApply.getInterest() == null ? "0.00"
										: loanApply.getInterest().setScale(2, BigDecimal.ROUND_HALF_UP).toString())
						.replace("${year}", dateymd[0]).replace("${mon}", dateymd[1]).replace("${day}", dateymd[2]));

				result.setDueRepayDate(DateUtils.formatDate(repayLatestDate, "yyyy-MM-dd"));
			}
			result.setIntRate(loanApply.getIntRate().setScale(1, BigDecimal.ROUND_HALF_UP).toString());// 年化利率
			result.setType(LoanConstant.TYPE_DETAIL);
			//进度
			String progress = loanApplyDetailService.getDetailProgress(applyDetail, loanApply,member);
			result.setProgress(progress);
		}else{
			/**
			 * type = 2 查record
			 */
			Long recordId = Long.valueOf(loanId);
			loanRecord = loanRecordService.get(recordId);
			RepayType repayType = loanRecord.getRepayType();//还款方式
			AuctionStatus auctionStatus = loanRecord.getAuctionStatus();//债转状态
			result.setTradeCode(loanId);// 交易码
			result.setProgress(loanRecord.getProgress());// 进度
			Member loaner = loanRecord.getLoaner();
			if(!loaner.getId().equals(member.getId())){//我是放款人
				isLoaner = false;
			} 
			
			BigDecimal amount = loanRecord.getAmount();// 借款金额
			result.setAmount(StringUtils.decimalToStr(amount, 2));
			Date createTime = loanRecord.getCreateTime();// 申请时间
			result.setCreateDate(DateUtils.formatDate(createTime, "yyyy-MM-dd"));

			BigDecimal interest = loanRecord.getInterest();// 利息
			result.setInterest(interest.setScale(2, BigDecimal.ROUND_HALF_UP).toString());

			// 消息
			applyDetail = loanRecord.getLoanApplyDetail();
			Long applyDetailId = applyDetail.getId();
			applyDetail = loanApplyDetailService.get(applyDetailId);
			Status recordStatus = loanRecord.getStatus();
			PartialStatus partialStatus = loanRecord.getPartialStatus();// 部分还款状态
			DelayStatus delayStatus = loanRecord.getDelayStatus();// 延期状态
			LineDownStatus lineDownStatus = loanRecord.getLineDownStatus();// 线下还款状态
			Date loanDueRepayDate = loanRecord.getDueRepayDate();
			result.setDueRepayDate(DateUtils.formatDate(loanDueRepayDate, "yyyy-MM-dd"));
			Status status = loanRecord.getStatus();
			
			String progress = loanRecordService.getRecordProgress(loanRecord,member);
			result.setProgress(progress);
			String videoUrl = applyDetail.getId()<=7517375?Global.getConfig("domain")+applyDetail.getVideoUrl():applyDetail.getVideoUrl();
			result.setVideoReviewUrl(videoUrl);
			
			DisputeResolution disputeResolution = applyDetail.getDisputeResolution();//争议解决方式
			if(auctionStatus.equals(AuctionStatus.auctioned)){
				NfsCrAuction nfsCrAuction = new NfsCrAuction();
				nfsCrAuction.setLoanRecord(loanRecord);
				nfsCrAuction.setStatus(NfsCrAuction.Status.successed);
				List<NfsCrAuction> findList = crAuctionService.findList(nfsCrAuction);
				if(findList != null && findList.size() > 0){
					Long crBuyerId = findList.get(0).getCrBuyer().getId();
					Member nowCreditor = memberService.get(crBuyerId);
					result.setNowCreditor(nowCreditor.getName());
					result.setShowAuction(1);
					result.setHasAuctioned(1);
					if(nowCreditor.getId().equals(member.getId())){
						isLoaner = true;
					}
				}
			}else if(auctionStatus.equals(AuctionStatus.auction)){
				result.setShowAuction(1);
			}
			
			if (isLoaner) {// 借贷对象和证件号
				Member loanee = loanRecord.getLoanee();
				result.setRealName(loanee.getName());
				loanee = MemUtils.maskIdNo(loanee);
				result.setIdNo(loanee.getIdNo());
				result.setFriendId(loanee.getId().toString());
			} else {
				result.setRealName(loaner.getName());
				result.setFriendId(loaner.getId().toString());
				loaner = MemUtils.maskIdNo(loaner);
				result.setIdNo(loaner.getIdNo());
			}
			
			/**
			 * 底部按钮
			 */
			if (!status.equals(Status.repayed)) {
				/**
				 * 待还
				 */
				Long days = (long) 0;
				Integer overdueCount = 0;
				Date dueRepayDate = loanRecord.getDueRepayDate();// 到期日
				days = DateUtils.pastDays(dueRepayDate);// 过去的天数
				
				result.setShowButton(1);
				result.setLoanStatus(6);
				
				if (lineDownStatus.equals(NfsLoanRecord.LineDownStatus.loaneeLineDownRepayment)) {// 全额线下还款，待确认
					buttomId = isLoaner ? "2010|2012" : "2040|2017";
				} else if (partialStatus.equals(NfsLoanRecord.PartialStatus.loaneeApplyPartial) 
						&& (status.equals(Status.penddingRepay) || status.equals(Status.overdue) && days <= 15) ) {// 借款人申请部分还款，待确认
					buttomId = isLoaner ? "2035|2036" : "2017|2037";
				} else if (partialStatus.equals(NfsLoanRecord.PartialStatus.loanerApplyPartial)
						&& (status.equals(Status.penddingRepay) || status.equals(Status.overdue) && days <= 15)) {// 放款人申请部分还款，待确认
					buttomId = isLoaner ? "2017|2042" : "2044|2046";
				} else if (delayStatus.equals(NfsLoanRecord.DelayStatus.loaneeApplyDelay)
						&& (status.equals(Status.penddingRepay) || status.equals(Status.overdue) && days <= 15)) {// 借款人申请延期，待确认
					buttomId = isLoaner ? "2005|2022" : "2017|2028";
				} else if (delayStatus.equals(NfsLoanRecord.DelayStatus.loanerApplyDelay)
						&& (status.equals(Status.penddingRepay) || status.equals(Status.overdue) && days <= 15)) {// 放款人申请延期，待确认
					buttomId = isLoaner ? "2017|2043" : "2045|2047";
				} else{
				
					if (loanRecord.getRepayType().equals(NfsLoanApply.RepayType.oneTimePrincipalAndInterest)) {// 全额
					} else if (loanRecord.getRepayType().equals(NfsLoanApply.RepayType.principalAndInterestByMonth)) {// 分期
						overdueCount = loanRepayRecordService.countOverdueTimes(loanRecord.getId());
					}
					if (days > 0 && status.equals(Status.overdue)) {
						result.setOverdueDays(Integer.parseInt(days.toString()));
					}
					/**
					 * 查该借条催收仲裁状态
					 */
					Integer collectionTimes = loanRecord.getCollectionTimes();//催收次数
					NfsLoanCollection nowCollection = null;
					if(collectionTimes < 3){
						nowCollection  = loanCollectionService.findNowCollection(loanRecord);
					}else{
						nowCollection  = loanCollectionService.findThirdCollection(loanRecord);
					}
					NfsLoanArbitration nfsLoanArbitration = new NfsLoanArbitration();// 仲裁
					nfsLoanArbitration.setLoan(loanRecord);
					List<NfsLoanArbitration> findArbitrationList = loanArbitrationService
							.findList(nfsLoanArbitration);

					Boolean isCollectioned = nowCollection != null;
					Boolean isArbitrationed = findArbitrationList != null && findArbitrationList.size() > 0;
					if (isLoaner) {
						result.setCloseLoan(1);
						StringBuffer urgeRecordUrl = new StringBuffer();
						urgeRecordUrl.append(Global.getConfig("domain"));
						urgeRecordUrl.append(Global.getWyjtAppPath());
						
						StringBuffer arbitrationRecordUrl = new StringBuffer();
						arbitrationRecordUrl.append(Global.getConfig("domain"));
						arbitrationRecordUrl.append(Global.getWyjtAppPath());
						if (isCollectioned && isArbitrationed || collectionTimes == 3 && isArbitrationed ) {
							if(disputeResolution.equals(DisputeResolution.arbitration)){
								buttomId = "2038|2039"; //"催收进度" "仲裁进度" 
							}else{
								//buttomId = "2038"; //"催收进度" 
								buttomId = "2038|2048";//"催收进度" "自助诉讼"
							}
							
							result.setUrgeRecordUrl(
									urgeRecordUrl.append("/collection/goColSchedule?collectionId=" 
											+ nowCollection.getId()).toString());//催收流程页面
							result.setArbitrationRecordUrl(
									arbitrationRecordUrl.append("/arbitration/goArbSchedule?arbRecordId=" 
											+ findArbitrationList.get(0).getId()).toString()); // 仲裁流程界面
						} else if (isCollectioned || collectionTimes == 3) {
							if(disputeResolution.equals(DisputeResolution.arbitration)){
								buttomId = "2038|2034";//"催收进度" "申请仲裁" 
							}else{
								//buttomId = "2038";//"催收进度"
								buttomId = "2038|2048";//"催收进度" "自助诉讼"
							}
							result.setUrgeRecordUrl(urgeRecordUrl.append("/collection/goColSchedule?collectionId=" 
									+ nowCollection.getId()).toString());
							com.jxf.loan.entity.NfsLoanCollection.Status collectionStatus = nowCollection.getStatus();
							if(collectionStatus.equals(com.jxf.loan.entity.NfsLoanCollection.Status.fail) 
									|| collectionStatus.equals(com.jxf.loan.entity.NfsLoanCollection.Status.refuse)
									|| collectionStatus.equals(com.jxf.loan.entity.NfsLoanCollection.Status.success)){
								result.setIsCollectioned(0);
							}else{
								result.setIsCollectioned(1);
							}
						} else if (isArbitrationed && collectionTimes < 3) {
							if(disputeResolution.equals(DisputeResolution.arbitration)){
								if(days > 365) {
									buttomId = "2039";//"申请催收" "仲裁进度" 
								}else {
									buttomId = "2033|2039";//"申请催收" "仲裁进度" 
								}
							}else{
								if(days > 365) {
									buttomId = "2048";//"申请催收" "自助诉讼"
								}else {
									buttomId = "2033|2048";//"申请催收" "自助诉讼"
								}
							}
							result.setArbitrationRecordUrl(arbitrationRecordUrl.append("/arbitration/goArbSchedule?arbRecordId=" 
									+ findArbitrationList.get(0).getId()).toString()); // 仲裁流程界面
							NfsLoanArbitration loanArbitration = findArbitrationList.get(0);
							if(loanArbitration.getStatus().equals(NfsLoanArbitration.Status.application) 
									||loanArbitration.getStatus().equals(NfsLoanArbitration.Status.inArbitration)
									||loanArbitration.getStatus().equals(NfsLoanArbitration.Status.review)){
								result.setIsArbitration(1);
							}else{
								result.setIsArbitration(0);
							}
						} else if ((days > 0|| repayType.equals(RepayType.principalAndInterestByMonth) && overdueCount > 0) 
								&& collectionTimes < 3) {
							if(disputeResolution.equals(DisputeResolution.arbitration)){
								if(days > 365) {
									buttomId = "2034";//"申请仲裁" 
								}else {
									buttomId = "2033|2034";//"申请催收" "申请仲裁" 
								}
							}else{
								if(days > 365) {
									buttomId = "2048";//"申请催收" "自助诉讼"
								}else {
									buttomId = "2033|2048";//"申请催收" "自助诉讼"
								}
							}
						} else {
							buttomId = repayType.equals(RepayType.principalAndInterestByMonth) ? "2027" : "2041|2027";
						}
					} else {
						if (isCollectioned || isArbitrationed) {
							buttomId = "2024";
						} else if (repayType.equals(RepayType.oneTimePrincipalAndInterest) && days > -6 && days <= 15 
								&& auctionStatus.equals(AuctionStatus.initial)) {
							buttomId = "2014|2025";
						} else {
							buttomId = "2025";
						}
						
						if (repayType.equals(RepayType.oneTimePrincipalAndInterest) && nowCollection == null
								&& (findArbitrationList == null || findArbitrationList.size() < 1) && days <= 15 
								&& auctionStatus.equals(AuctionStatus.initial)) {
							result.setCanPartRepay(1);
						} else {
							result.setCanPartRepay(0);
						}
					}
					if (days > 0) {
						result.setLoanStatus(15);
					}
				}
				result.setVideoStatus(StringUtils.isNotEmpty(applyDetail.getVideoUrl()) ? 3 : 4);

				if(auctionStatus.equals(AuctionStatus.auctioned)){//转让成功
					buttomId = "2025";
					result.setCanPartRepay(0);
				}else if(auctionStatus.equals(AuctionStatus.auction)){
					result.setCanPartRepay(0);
				}
			} else {
				Date completeDate = loanRecord.getCompleteDate();
				Integer overDueDays = DateUtils.getDistanceOfTwoDate(loanDueRepayDate, completeDate);
				result.setOverdueDays(overDueDays);
				
				buttomId = isLoaner ? "" : "2003";
				result.setLoanStatus(recordStatus.equals(NfsLoanRecord.Status.repayed) ? 14 : 18);
				result.setShowButton(recordStatus.equals(NfsLoanRecord.Status.repayed) ? 1 : 0);
				result.setCompleteDate(DateUtils.formatDate(completeDate, "yyyy-MM-dd"));
			}
			result.setLoanCenterStatus(1);
			result.setIntRate(loanRecord.getIntRate().setScale(1, BigDecimal.ROUND_HALF_UP).toString());// 年化利率
			result.setType(LoanConstant.TYPE_RECORD);
		}
		
		result.setIsLoaner(isLoaner?1:0);

		result.setContentTempList(RecordMessage.tempList);// 左下角模板消息
		result.setLoanId(loanId);

		/**
		 * 原来的状态码 100:借款人已发起借款申请，等待确认付款 150:同意修改利息，等待放款人付款 160:拒绝修改利息，等待放款人付款
		 * 
		 * 200:放款人已发起借款申请并支付，等待借款人确认
		 * 
		 * 300:放款人修改利息，等待借款人确认
		 * 
		 * 400:放款人已付款，等待上传视频
		 * 
		 * 420:视频审核未通过，等待重新上传
		 * 
		 * 440:视频已上传，等待审核
		 * 
		 * 500:还款中 510小程序取消部分还款 515小程序取消延期还款 520:手续费 530小程序拒绝部分还款 535小程序拒绝延期还款
		 * 540:全额线下还款，待确认 560:借款人申请部分还款，待确认 570:放款人申请部分还款，待确认 580:借款人申请延期，待确认
		 * 590:放款人申请延期，待确认
		 * 
		 * 590:放款人申请延期，待确认 700:超时 800:借款人拒绝借款 820:放款人拒绝借款 840:放款人/借款人，取消借款/放款
		 * 
		 * 900:确认线下还款 910:放款人主动关闭借款单 920:同意部分还款，生成新的借条 940:同意延期，生成新的借条 960:分期还款(记录日志使用)
		 * 980:已全额还款（线上）
		 */
		/*********************** end **********************/

		result.setBottomButtonID(buttomId);

		if (applyDetail != null) {
			// 借条的对话消息
			NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
			nfsLoanDetailMessage.setDetail(applyDetail);
			List<NfsLoanDetailMessage> findMessageList = loanDetailMessageService.findList(nfsLoanDetailMessage);

			if (findMessageList != null && findMessageList.size() > 0) {
				ArrayList<LoanMessage> messageList = new ArrayList<LoanMessage>();
				LoanMessage loanMessage = null;
				Member detailMember = applyDetail.getMember();
				NfsLoanApply apply = applyDetail.getApply();
				apply = loanApplyService.get(apply);
				Member applyMember = apply.getMember();
				Member otherOne = null;
				if (member.getId().equals(detailMember.getId())) {
					otherOne = applyMember;
				} else {
					otherOne = detailMember;
				}
				for (NfsLoanDetailMessage message : findMessageList) {
					loanMessage = new LoanDetailForAppResponseResult().new LoanMessage();
					loanMessage.setMemberId(message.getMember().getId().toString());
					Date createTime = message.getCreateTime();
					loanMessage.setTime(DateUtils.formatDate(createTime, "yyyy-MM-dd"));
					if (message.getMember().getId().equals(member.getId())) {// 发送人是自己
						loanMessage.setDisplayMode("2");
						loanMessage.setName(member.getName());
						loanMessage.setImgUrl(member.getHeadImage());
					} else if (otherOne.getId().equals(message.getMember().getId())) {// 发送人是另一个人
						loanMessage.setDisplayMode("1");
						loanMessage.setName(otherOne.getName());
						loanMessage.setImgUrl(otherOne.getHeadImage());
					} else {// 发送人是系统
						loanMessage.setDisplayMode("1");
						loanMessage.setName(LoanConstant.SYS_NAME);
						loanMessage.setImgUrl(LoanConstant.SYS_HEAD_IMAGE);
					}
					// 文字聊天内容（包括手动输入和固定对话）
					if (message.getType() == RecordMessage.SEND_MSG) {
						loanMessage.setDialogueMode("1");
						loanMessage.setVar1(message.getNote());
					}else if (message.getMessageId() == null) {
						continue;
					} 
					// 申请延期
					else if (message.getMessageId() == RecordMessage.CHAT_3) {
						Map<String, Object> map = JSON.parseObject(message.getNote());
						loanMessage.setDialogueMode("3");
						String amountStr = (String) map.get(LoanConstant.OLD_AMOUNT);
						String delayInterestStr = (String) map.get(LoanConstant.DELAY_INTEREST);
						String oldInterestStr = (String)map.get(LoanConstant.OLD_INTEREST);
						loanMessage.setVar1(amountStr); // 应还本金
						loanMessage.setVar2(delayInterestStr); // 延期利息
						loanMessage.setVar3(((String) map.get(LoanConstant.LAST_REPAY_DATE)).substring(0, 10)); // 延至时间
						
						BigDecimal amountFromMap = new BigDecimal(amountStr);
						BigDecimal delayInterestFromMap = new BigDecimal(delayInterestStr == null?"0":delayInterestStr);
						BigDecimal oldInterestFromMap = new BigDecimal(oldInterestStr == null?"0":oldInterestStr);
						
						loanMessage.setVar4(StringUtils.decimalToStr(amountFromMap.add(delayInterestFromMap).add(oldInterestFromMap), 2)); // 应还总额
						loanMessage.setVar5(amountStr); // 应还本金
						loanMessage.setVar6(oldInterestStr); // 原利息
					} // 协商利息
					else if (message.getMessageId() == RecordMessage.CHAT_4) {
						Map<String, Object> map = JSON.parseObject(message.getNote());
						loanMessage.setDialogueMode("4");
						loanMessage.setVar1((String) map.get(LoanConstant.OLD_INTEREST)); // 原利息
						loanMessage.setVar2((String) map.get(LoanConstant.NEW_INTEREST)); // 新利息
					}
					// 部分还款
					else if (message.getMessageId() == RecordMessage.CHAT_6) {
						Map<String, Object> map = JSON.parseObject(message.getNote());
						loanMessage.setDialogueMode("6");
						loanMessage.setVar1((String)map.get(LoanConstant.OLD_AMOUNT)); // 应还本金
						loanMessage.setVar2((String)map.get(LoanConstant.OLD_INTEREST)); // 利息
						loanMessage.setVar3((String) map.get(LoanConstant.PARTIAL_PAYMENT)); // 部分还款金额
						loanMessage.setVar4((String) map.get(LoanConstant.DELAY_INTEREST)); // 延期利息
						loanMessage.setVar5(((String) map.get(LoanConstant.LAST_REPAY_DATE)).substring(0, 10)); // 还款时间
					}
					// 服务费
					else if (message.getMessageId() == RecordMessage.CHAT_7) {
						loanMessage.setDialogueMode("7");
						loanMessage.setVar1(StringUtils.decimalToStr(new BigDecimal(message.getNote()), 2)); // 手续费金额
					}
					// 放款人申请部分还款
					else if (message.getMessageId() == RecordMessage.CHAT_8) {
						Map<String, Object> map = JSON.parseObject(message.getNote());
						Integer partialPayment = Integer.parseInt((String) map.get(LoanConstant.PARTIAL_PAYMENT));
						loanMessage.setDialogueMode("8");
						loanMessage.setVar1((String)map.get(LoanConstant.OLD_AMOUNT)); // 应还本金
						loanMessage.setVar2((String)map.get(LoanConstant.OLD_INTEREST)); // 利息
						loanMessage.setVar3(partialPayment.toString()); // 部分还款金额
						loanMessage.setVar4((String) map.get(LoanConstant.DELAY_INTEREST)); // 延期利息
						loanMessage.setVar5((String) map.get(LoanConstant.REMAIN_PAYMENT)); // 剩余要还金额
						loanMessage.setVar6(((String) map.get(LoanConstant.LAST_REPAY_DATE))); // 还款时间
					}
					// 放款人申请延期还款
					else if (message.getMessageId() == RecordMessage.CHAT_9) {
						Map<String, Object> map = JSON.parseObject(message.getNote());
						String delayInterest = (String)map.get(LoanConstant.DELAY_INTEREST);
						if(StringUtils.equals(delayInterest, "")){
							delayInterest = "0";
						}
						loanMessage.setDialogueMode("9");
						loanMessage.setVar1((String)map.get(LoanConstant.OLD_AMOUNT));// 应还本金
						loanMessage.setVar2((String)map.get(LoanConstant.OLD_INTEREST)); // 原利息
						loanMessage.setVar3(delayInterest); // 延期利息
						loanMessage.setVar4((String)map.get(LoanConstant.CURRENT_REPAY_DATE)); // 当前还款时间
						loanMessage.setVar5(((String) map.get(LoanConstant.LAST_REPAY_DATE)).substring(0, 10)); // 延长后还款时间
					}// 放款人转让成功
					else if (message.getMessageId() == RecordMessage.CHAT_10) {
						Map<String, Object> map = JSON.parseObject(message.getNote());
						
						loanMessage.setDialogueMode("10");
						loanMessage.setVar1((String)map.get(LoanConstant.CREDITOR));// 新债权人
						loanMessage.setVar2((String)map.get(LoanConstant.PHONE_NO)); // 新债权人电话
						loanMessage.setVar3((String)map.get(LoanConstant.ID_NO)); // 新债权人身份证
						loanMessage.setVar4((String)map.get(LoanConstant.TIME)); // 转让时间
					} else if (message.getMessageId() == 0) {
						continue;
					}
					// 图片消息
					else {
						loanMessage.setDialogueMode("2");
						loanMessage.setVar1(message.getMessageId() + "");
					}
					messageList.add(loanMessage);

				}
				result.setDialogContent(messageList);
			}

		}
		return result;
	}

	public Page<NfsLoanRecord> findApplyForArbitration(NfsLoanRecord loanRecord, Integer pageNo,
			Integer pageSize) {
		Page<NfsLoanRecord> page = new Page<NfsLoanRecord>(pageNo == null ? 1 : pageNo,
				pageSize == null ? 20 : pageSize);
		loanRecord.setPage(page);
		List<NfsLoanRecord> loanList = loanRecordDao.findApplyForArbitration(loanRecord);
		page.setList(loanList);
		return page;
	}

	@Override
	public NfsLoanRecord findMyandFriendLoan(Member merber, Member friend) {
		NfsLoanRecord loanRecord = null ;
		NfsLoanRecord record = new NfsLoanRecord() ;
		record.setLoaner(merber);
		record.setLoanee(friend);
		record.setStatus(NfsLoanRecord.Status.penddingRepay);
		List<NfsLoanRecord> findList = findList(record);
		if (findList.size() <= 0) {
			record.setStatus(NfsLoanRecord.Status.overdue);
			List<NfsLoanRecord> findList2 = findList(record);
			if (findList2.size() > 0) {
				loanRecord = findList2.get(0);
			}
		} else {
			loanRecord = findList.get(0);
		}
		return loanRecord;
	}

	@Override
	public NfsLoanRecord findByApplyDetailId(Long detailId) {
		return loanRecordDao.findByApplyDetailId(detailId);
	}

	@Override
	public Page<NfsLoanRecord> findCollection(NfsLoanRecord loanRecord, Integer pageNo, Integer pageSize) {
		Page<NfsLoanRecord> page = new Page<NfsLoanRecord>(pageNo == null ? 1 : pageNo,
				pageSize == null ? 20 : pageSize);
		loanRecord.setPage(page);
		List<NfsLoanRecord> loanList = loanRecordDao.findCollection(loanRecord);
		page.setList(loanList);
		return page;
	}

	@Override
	public String getRecordProgress(NfsLoanRecord record , Member member) {
		Date dueRepayDate = record.getDueRepayDate();
		NfsLoanRecord.Status recordStatus = record.getStatus();//借条状态
		 LineDownStatus lineDownStatus = record.getLineDownStatus();//线下还款状态
		 PartialStatus partialStatus = record.getPartialStatus();//部分还款状态
		 DelayStatus delayStatus = record.getDelayStatus();//延期申请状态
		 int pastDays = CalendarUtil.getIntervalDays(new Date(),dueRepayDate);//过去的天数
		 RepayType repayType = record.getRepayType();
		 String formatNowDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
		 String formatDueRepayDate = DateUtils.formatDate(dueRepayDate, "yyyy-MM-dd");
		 String progress = "未知状态;757575";
		boolean isLender =  member.getId().equals(record.getLoaner().getId());
		 if(repayType.equals(RepayType.oneTimePrincipalAndInterest)){//全额
			 if (recordStatus.equals(NfsLoanRecord.Status.penddingRepay) && lineDownStatus.equals(LineDownStatus.initial)
					 && delayStatus.equals(DelayStatus.initial) && partialStatus.equals(PartialStatus.initial)) {
				 if (pastDays >= 30) {
					 progress = "30;ED2E24|日以上还款;757575";
				 } else if (pastDays > 0) {
					 progress = pastDays + 1 + ";ED2E24|日内还款;757575";
				 } 
				 if (formatNowDate.equals(formatDueRepayDate)) {
					 progress = "今日还款;FFAE38";
				 } 
				 if (isLender) {
					 progress = progress.replace("还款", "收款");
				 }
			 } else if ((recordStatus.equals(NfsLoanRecord.Status.penddingRepay) 
					 || recordStatus.equals(NfsLoanRecord.Status.overdue)) 
					 && lineDownStatus.equals(LineDownStatus.loaneeLineDownRepayment)) {
				 progress = "线下还款待确认;FFAE38";
			 } else if ((recordStatus.equals(NfsLoanRecord.Status.penddingRepay) 
					 || recordStatus.equals(NfsLoanRecord.Status.overdue)) 
					 && (partialStatus.equals(PartialStatus.loaneeApplyPartial) 
					|| partialStatus.equals(PartialStatus.loanerApplyPartial))) {
				 progress = "部分还款待确认;FFAE38";
			 } else if ((recordStatus.equals(NfsLoanRecord.Status.penddingRepay) 
					 || recordStatus.equals(NfsLoanRecord.Status.overdue)) 
					 && (delayStatus.equals(DelayStatus.loaneeApplyDelay) || delayStatus.equals(DelayStatus.loanerApplyDelay))) {
				 progress = "延期待确认;FFAE38";
			 } else if (recordStatus.equals(NfsLoanRecord.Status.repayed)) {
				 progress = "已完成;7FC153";
			 }else if(recordStatus.equals(NfsLoanRecord.Status.overdue)){
				 if (pastDays-1 == 0) {
					 progress = "今日逾期;FFAE38";
				 } else if (pastDays >1) {
					 progress = "已逾期;757575|" + pastDays + ";ED2E24|日;757575";
				 } 
			 }
		 }else if(repayType.equals(RepayType.principalAndInterestByMonth)){//分期 显示距离下次还款的天数 如果逾期显示距离最远逾期天数
			//查还款计划
			 NfsLoanRepayRecord  loanRepayRecord = new NfsLoanRepayRecord();
			 loanRepayRecord.setLoan(record);
			 List<NfsLoanRepayRecord> repayRecordList = loanRepayRecordService.findList(loanRepayRecord);
			 int size = repayRecordList.size();
			 Date nowDate = new Date();
			 boolean getDate = false;//查到下次还款时间
			 boolean getOverdueDate = false;//查到最早一期逾期
			 Date earliestOverdueDate = dueRepayDate;
			 Date nextDueRepayDate = dueRepayDate;
			 for (int i = 0; i < size; i++) {
				 NfsLoanRepayRecord repayRecord = repayRecordList.get(i);
				 Date expectRepayDate = repayRecord.getExpectRepayDate();
				 if(!getDate){
					 if(expectRepayDate.getTime() > nowDate.getTime()){
						 getDate = true;
						 nextDueRepayDate = expectRepayDate;//最近一次还款时间
					 }
				 }
				 if(!getOverdueDate){
					 if(repayRecord.getStatus().equals(NfsLoanRepayRecord.Status.overdue)){
						 getOverdueDate = true;
						 earliestOverdueDate = expectRepayDate;//最早逾期日期
					 }
				 }
			}
			 String nextDueRepayDateStr = DateUtils.formatDate(nextDueRepayDate, "yyyy-MM-dd");
			 if (recordStatus.equals(NfsLoanRecord.Status.penddingRepay)) {
				 pastDays = CalendarUtil.getIntervalDays(new Date(),nextDueRepayDate);//过去的天数
				 if (formatNowDate.equals(nextDueRepayDateStr)) {
					 progress = "今日还款;FFAE38";
				 }else{
					 progress = pastDays + 1 + ";ED2E24|日内还款;757575";
				 }
				 if (isLender) {
					 progress = progress.replace("还款", "收款");
				 }
			 } else if (recordStatus.equals(NfsLoanRecord.Status.repayed)) {
				 progress = "已完成;7FC153";
			 }else if(recordStatus.equals(NfsLoanRecord.Status.overdue)){
				 pastDays = CalendarUtil.getIntervalDays(new Date(),earliestOverdueDate);//过去的天数
				 if (pastDays-1 == 0) {
					 progress = "今日逾期;FFAE38";
				 } else if (pastDays >1) {
					 progress = "已逾期;757575|" + pastDays + ";ED2E24|日;757575";
				 } 
			 }
		 }
		return progress;
	}


	@Override
	public Integer countOverdueMoreThan15daysRecord(Member member) {
		NfsLoanRecord nfsLoanRecord = new NfsLoanRecord();
		nfsLoanRecord.setLoanee(member);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		// 将时分秒,毫秒域清零
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		nfsLoanRecord.setDueRepayDate(DateUtils.addCalendarByDate(cal.getTime(), -16));
		return loanRecordDao.countOverdueMoreThan15daysRecord(nfsLoanRecord);
	}

	@Override
	public Page<NfsLoanRecord> findToPaidPage(NfsLoanRecord loanRecord,
			Integer pageNo, Integer pageSize) {
		Page<NfsLoanRecord> page = new Page<NfsLoanRecord>(pageNo == null ? 1 : pageNo,
				pageSize == null ? 20 : pageSize);
		String orderBy = loanRecord.getPage().getOrderBy();
		page.setOrderBy(orderBy);
		loanRecord.setPage(page);
		List<NfsLoanRecord> loanList = loanRecordDao.findToPaidList(loanRecord);
		page.setList(loanList);
		return page;
	}

	@Override
	public Page<NfsLoanRecord> findClosedPage(NfsLoanRecord loanRecord,
			Integer pageNo, Integer pageSize) {
		Page<NfsLoanRecord> page = new Page<NfsLoanRecord>(pageNo == null ? 1 : pageNo,
				pageSize == null ? 20 : pageSize);
		String orderBy = loanRecord.getPage().getOrderBy();
		page.setOrderBy(orderBy);
		loanRecord.setPage(page);
		List<NfsLoanRecord> loanList = loanRecordDao.findClosedList(loanRecord);
		page.setList(loanList);
		return page;
	}

	@Override
	@Transactional(readOnly = false)
	public void closeLoanLineDown(NfsLoanRecord loanRecord) {
		
		BigDecimal overdueDays = new BigDecimal(
				DateUtils.getDistanceOfTwoDate(loanRecord.getDueRepayDate(), new Date()));
		BigDecimal overdueInterest = LoanUtils.calOverdueInterest(loanRecord.getDueRepayAmount(), overdueDays);
		
		BigDecimal dueRepayAmount = loanRecord.getDueRepayAmount();
		loanRecord.setCompleteDate(new Date());
		loanRecord.setStatus(NfsLoanRecord.Status.repayed);
		loanRecord.setOverdueInterest(overdueInterest);
		loanRecord.setLineDownStatus(NfsLoanRecord.LineDownStatus.completed);	
		loanRecord.setDueRepayAmount(BigDecimal.ZERO);
		loanRecord.setDueRepayTerm(0);
		
		//变更还款计划
		NfsLoanRepayRecord loanRepayRecord = new NfsLoanRepayRecord();
		loanRepayRecord.setLoan(loanRecord);
		List<NfsLoanRepayRecord> repayList = loanRepayRecordService.findList(loanRepayRecord);
		for (NfsLoanRepayRecord repayRecord : repayList) {
			repayRecord.setStatus(NfsLoanRepayRecord.Status.done);
			loanRepayRecordService.save(repayRecord);
		}
		loanRecord.setRepayedTerm(repayList.size());
		
		//关闭仲裁强执催收
		loanRecord = loanRecordService.closeArbitrationAndCollection(loanRecord);
		loanRecordService.save(loanRecord);
		
		//生成借条记录 --- 线下全额还款
		NfsLoanOperatingRecord operatingRecord = new NfsLoanOperatingRecord();
		operatingRecord.setType(NfsLoanOperatingRecord.Type.lineDown);
		operatingRecord.setInitiator("放款人");
		operatingRecord.setOldRecord(loanRecord);
		NfsLoanRecord nowRecord = new NfsLoanRecord();
		nowRecord.setAmount(BigDecimal.ZERO);
		nowRecord.setInterest(BigDecimal.ZERO);
		nowRecord.setStatus(NfsLoanRecord.Status.repayed);
		operatingRecord.setNowRecord(loanRecord);
		loanOperatingRecordService.save(operatingRecord);
		TrxType trxType = loanRecord.getTrxType();
		if(!loanRecord.getAuctionStatus().equals(AuctionStatus.auctioned) && trxType.equals(TrxType.online)){//非已转让借条 线上交易
			//发对话
			NfsLoanDetailMessage loanDetailMessage = new NfsLoanDetailMessage();
			loanDetailMessage.setDetail(loanRecord.getLoanApplyDetail());
			loanDetailMessage.setMember(loanRecord.getLoaner());
			loanDetailMessage.setMessageId(RecordMessage.CHAT_2220);
			loanDetailMessage.setType(RecordMessage.LENDER_CLOSE_LOAN);
			loanDetailMessageService.save(loanDetailMessage);
		}
		
		//变更账户 修改总待收和总待还
		Member payee = loanRecord.getLoaner();//收款人
		if(loanRecord.getAuctionStatus().equals(AuctionStatus.auctioned)){//借条已经债转,变更收款人
			payee = crAuctionService.getCrBuyer(loanRecord);
		}
		int code = actService.updateAct(TrxRuleConstant.LINE_DOWN_PAYMENT, dueRepayAmount,loanRecord.getLoanee() ,payee, loanRecord.getId());
		if(code == Constant.UPDATE_FAILED) {
			logger.error("更新账户失败，线下还款确认失败！借条Id{}",loanRecord.getId());
			throw new RuntimeException("更新账户失败，线下还款确认失败！借条Id"+loanRecord.getId()); 
		}
		int days = DateUtils.getDifferenceOfTwoDate(loanRecord.getDueRepayDate(),new Date());
		if (days > -15) {
			memberPointService.updateMemberPoint(memberService.get(loanRecord.getLoanee().getId()), MemberPointRule.Type.repay15, loanRecord.getDueRepayAmount());
		} else if (days > -30) {
			memberPointService.updateMemberPoint(memberService.get(loanRecord.getLoanee().getId()), MemberPointRule.Type.repay30, loanRecord.getDueRepayAmount());
		} else if (days > -45) {
			memberPointService.updateMemberPoint(memberService.get(loanRecord.getLoanee().getId()), MemberPointRule.Type.repay45, loanRecord.getDueRepayAmount());
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public String replyLineDown(NfsLoanRecord loanRecord,String isAgree) {
		TrxType trxType = loanRecord.getTrxType();
		String message = "成功";
		if(Integer.parseInt(isAgree) == 0){
			message = "拒绝";
			loanRecord.setLineDownStatus(NfsLoanRecord.LineDownStatus.initial);
			loanRecordService.save(loanRecord);
			if(trxType.equals(TrxType.online)){
				//发送会员消息
				memberMessageService.sendMessage(MemberMessage.Type.refuseLineDownRepayment,loanRecord.getId());
				//发对话
				NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
				nfsLoanDetailMessage.setDetail(loanRecord.getLoanApplyDetail());
				nfsLoanDetailMessage.setMember(loanRecord.getLoaner());
				nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_2205);
				nfsLoanDetailMessage.setType(RecordMessage.LENDER_PAID_REPAYMENT);
				loanDetailMessageService.save(nfsLoanDetailMessage);
			}else if(trxType.equals(TrxType.offline)){
				//发送会员消息
				memberMessageService.sendMessage(MemberMessage.Type.refuseRepaymentApplication,loanRecord.getId());
			}
		}else{
			message = "同意";
			loanRecord.setCompleteDate(new Date());
			BigDecimal overdueDays = new BigDecimal(
					DateUtils.getDistanceOfTwoDate(loanRecord.getDueRepayDate(), new Date()));
			BigDecimal overdueInterest = LoanUtils.calOverdueInterest(loanRecord.getDueRepayAmount(), overdueDays);
			loanRecord.setOverdueInterest(overdueInterest);
			loanRecord.setStatus(NfsLoanRecord.Status.repayed);
			loanRecord.setLineDownStatus(NfsLoanRecord.LineDownStatus.completed);
			BigDecimal dueRepayAmount = loanRecord.getDueRepayAmount();
			loanRecord.setDueRepayAmount(BigDecimal.ZERO);
			loanRecord.setDueRepayTerm(0);
			
			//变更还款计划
			NfsLoanRepayRecord nfsLoanRepayRecord = new NfsLoanRepayRecord();
			nfsLoanRepayRecord.setLoan(loanRecord);
			List<NfsLoanRepayRecord> repayList = loanRepayRecordService.findList(nfsLoanRepayRecord);
			for (NfsLoanRepayRecord repayRecord : repayList) {
				repayRecord.setStatus(NfsLoanRepayRecord.Status.done);
				loanRepayRecordService.save(repayRecord);
			}
			loanRecord.setRepayedTerm(repayList.size());
			//关闭仲裁强执催收
			loanRecord = loanRecordService.closeArbitrationAndCollection(loanRecord);
			loanRecordService.save(loanRecord);
			if(trxType.equals(TrxType.online)){
				//生成对话
				NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
				nfsLoanDetailMessage.setDetail(loanRecord.getLoanApplyDetail());
				nfsLoanDetailMessage.setMember(loanRecord.getLoaner());
				nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_2206);
				nfsLoanDetailMessage.setType(RecordMessage.LENDER_CONFIRM_LINEDOWN);
				loanDetailMessageService.save(nfsLoanDetailMessage);
				//发送会员消息
				memberMessageService.sendMessage(MemberMessage.Type.confirmReceiveRepayment,loanRecord.getId());
			}else if(trxType.equals(TrxType.offline)){
				//发送会员消息
				memberMessageService.sendMessage(MemberMessage.Type.agreeRepaymentApplication,loanRecord.getId());
			}
			
			
			//生成借条记录 --- 线下全额还款
			NfsLoanOperatingRecord operatingRecord = new NfsLoanOperatingRecord();
			operatingRecord.setType(NfsLoanOperatingRecord.Type.lineDown);
			operatingRecord.setInitiator("借款人");
			operatingRecord.setOldRecord(loanRecord);
			NfsLoanRecord nowRecord = new NfsLoanRecord();
			nowRecord.setAmount(BigDecimal.ZERO);
			nowRecord.setInterest(BigDecimal.ZERO);
			nowRecord.setStatus(NfsLoanRecord.Status.repayed);
			operatingRecord.setNowRecord(loanRecord);
			loanOperatingRecordService.save(operatingRecord);

			//变更账户修改总待收和总待还
			Member payee = loanRecord.getLoaner();//收款人
			if(loanRecord.getAuctionStatus().equals(AuctionStatus.auctioned)){//借条已经债转,变更收款人
				payee = crAuctionService.getCrBuyer(loanRecord);
			}
			int code = actService.updateAct(TrxRuleConstant.LINE_DOWN_PAYMENT, dueRepayAmount, loanRecord.getLoanee(),payee, loanRecord.getId());
			if(code == Constant.UPDATE_FAILED) {
				logger.error("更新账户失败，线下还款确认失败！借条Id{}",loanRecord.getId());
				throw new RuntimeException("更新账户失败，线下还款确认失败！借条Id"+loanRecord.getId());  
			}
		
			int days = DateUtils.getDifferenceOfTwoDate(loanRecord.getDueRepayDate(),new Date());
			if (days > -15) {
				memberPointService.updateMemberPoint(memberService.get(loanRecord.getLoanee().getId()), MemberPointRule.Type.repay15, loanRecord.getDueRepayAmount());
			} else if (days > -30) {
				memberPointService.updateMemberPoint(memberService.get(loanRecord.getLoanee().getId()), MemberPointRule.Type.repay30, loanRecord.getDueRepayAmount());
			} else if (days > -45) {
				memberPointService.updateMemberPoint(memberService.get(loanRecord.getLoanee().getId()), MemberPointRule.Type.repay45, loanRecord.getDueRepayAmount());
			}
		}		
		return message;
	}

	@Override
	public Page<NfsLoanRecord> findContractByMemberId(NfsLoanRecord loanRecord,Page<NfsLoanRecord> page) {
		loanRecord.setPage(page);
		List<NfsLoanRecord> loanList = loanRecordDao.findContractByMemberId(loanRecord);
		page.setList(loanList);
		return page;
	}

	@Override
	public int countLoaneeLoan(NfsLoanRecord loanRecord) {
		return loanRecordDao.loaneeCountLoan(loanRecord);
	}
	@Override
	public BigDecimal sumLoaneeLoan(NfsLoanRecord loanRecord) {
		return loanRecordDao.loaneeSumLoan(loanRecord);
	}

	@Override
	public List<NfsLoanRecord> findPeriodRepayLoanList() {
		return loanRecordDao.findPeriodRepayLoanList();
	}


	@Override
	@Transactional(readOnly = false)
	public int returnStatusToInitial(NfsLoanRecord record,BigDecimal partialAmount,NfsLoanPartialAndDelay partialAndDelay) {
		record.setPartialStatus(NfsLoanRecord.PartialStatus.initial);
		record.setDelayStatus(NfsLoanRecord.DelayStatus.initial);
		loanRecordService.save(record);
		int updateAct = actService.updateAct(TrxRuleConstant.REFUSE_PARTIAL_PAYMENT, partialAmount, record.getLoanee(), record.getId());
		if(updateAct  == Constant.UPDATE_FAILED){
			logger.error("会员账户变更操作失败，会员id{}",record.getLoanee().getId());
			throw new RuntimeException("会员账户加款操作失败!");
		}
		partialAndDelay.setStatus(NfsLoanPartialAndDelay.Status.reject);
		nfsLoanPartialAndDelayService.save(partialAndDelay);
		
		return updateAct;
	}
	
	
	public void filter(List<NfsLoanRecord> loans) {
		CollectionUtils.filter(loans, new Predicate() {
			public boolean evaluate(Object object) {
				NfsLoanRecord loan = (NfsLoanRecord) object;
				loan.setStatus(NfsLoanRecord.Status.overdue);
				int overdueCount = loanRecordService.countLoaneeLoan(loan);
				return overdueCount==0;
			}
		});
	}

	@Override
	public ResponseData findCrTransferPage(Member member, CrTransferListRequestParam reqData,
			CrTransferListResponseResult result) {

		Integer pageNo = reqData.getPageNo();
		Integer pageSize = reqData.getPageSize();
		Page<NfsLoanRecord> page = new Page<NfsLoanRecord>(pageNo == null?1:pageNo, pageSize == null?10:pageSize);
		
		NfsLoanRecord loanRecord = new NfsLoanRecord();
		loanRecord.setPage(page);
		loanRecord.setLoaner(member);
		loanRecord.setStatus(NfsLoanRecord.Status.overdue);
		loanRecord.setAuctionStatus(NfsLoanRecord.AuctionStatus.initial);
		loanRecord.setPartialStatus(NfsLoanRecord.PartialStatus.initial);
		loanRecord.setDelayStatus(NfsLoanRecord.DelayStatus.initial);
		loanRecord.setLineDownStatus(NfsLoanRecord.LineDownStatus.initial);
		loanRecord.setArbitrationStatus(NfsLoanRecord.ArbitrationStatus.initial);
		loanRecord.setCollectionStatus(NfsLoanRecord.CollectionStatus.initial);
		loanRecord.setRepayType(NfsLoanApply.RepayType.oneTimePrincipalAndInterest);
		loanRecord.setOverdueTab(reqData.getOverdueTab());
		loanRecord.setAmountTab(reqData.getAmountTab());
		loanRecord.setTrxType(NfsLoanApply.TrxType.online);
		List<NfsLoanRecord> list = loanRecordDao.findAuctionList(loanRecord);
		
		for (NfsLoanRecord loan : list) {
			Date dueRepayDate = loan.getDueRepayDate();
			String dueRepayStr = DateUtils.formatDate(dueRepayDate, "yyyy-MM-dd");
			int overdueDays = CalendarUtil.getIntervalDays(new Date(),dueRepayDate);//过去的天数
			BigDecimal overdueInterest = LoanUtils.calOverdueInterest(loan.getAmount(), new BigDecimal(overdueDays));
			
			String progress = loanRecordService.getRecordProgress(loan , member);
			CrTransferList transferList = new CrTransferList();
			transferList.setLoanId(loan.getId().toString());
			transferList.setPartnerName(loan.getLoaneeName());
			transferList.setPartnerHeadImage(loan.getLoanee().getHeadImage());
			transferList.setRepayType(loan.getRepayType().ordinal());
			transferList.setAmount(StringUtils.decimalToStr(loan.getAmount(), 2));
			transferList.setInterest(StringUtils.decimalToStr(loan.getInterest(), 2));
			transferList.setOverdueInterest(StringUtils.decimalToStr(overdueInterest, 2));
			transferList.setRepayDate(dueRepayStr);
			transferList.setProgress(progress);
			String nowDateStr = DateUtils.getDate();
			if(StringUtils.equals(dueRepayStr, nowDateStr)) {
				transferList.setIsToday(1);
				transferList.setIsOverdue(0);
			}else {
				transferList.setIsToday(0);
				transferList.setIsOverdue(1);
			}
			
			transferList.setOverdueDays(overdueDays);
			result.getCrTransferList().add(transferList);
		}
		return ResponseData.success("查询拍卖中可转让借条列表成功", result);

	}

	@Override
	public List<NfsLoanRecord> findWhoOweMeList(NfsLoanRecord record) {

		return loanRecordDao.findWhoOweMeList(record);
	}

	@Override
	public int loanerCountLoan(NfsLoanRecord loanRecord) {

		return loanRecordDao.loanerCountLoan(loanRecord);
	}

	@Override
	public BigDecimal loanerSumLoan(NfsLoanRecord loanRecord) {

		return loanRecordDao.loanerSumLoan(loanRecord);
	}

	@Override
	public List<Long> findUfangUserLoanCount(Map<String, Object> paramMap) {
		return loanRecordDao.findUfangUserLoanCount(paramMap);
	}

	@Override
	public BigDecimal sumOverdueLoanAmount(List<Long> loanIds) {
		return loanRecordDao.sumOverdueLoanAmount(loanIds);
	}

	@Override
	public List<NfsLoanRecord> getUfangUserLoanList(Map<String, Object> paramMap) {
		return loanRecordDao.getUfangUserLoanList(paramMap);
	}
	
    /**
	 * 关闭仲裁催收强执
	 */
	@Transactional(readOnly = false)
	public NfsLoanRecord closeArbitrationAndCollection(NfsLoanRecord loanRecord) {
		if(loanRecord.getArbitrationStatus().equals(ArbitrationStatus.doing)) {
			NfsLoanArbitration arbitration = loanArbitrationService.getByLoanId(loanRecord.getId());
			if(arbitration.getStrongStatus().equals(NfsLoanArbitration.StrongStatus.appliedStrong)) {
				loanArbitrationExecutionService.close(arbitration.getId());
			}
			loanArbitrationService.close(loanRecord.getId());
			loanRecord.setArbitrationStatus(NfsLoanRecord.ArbitrationStatus.end);
		}
		if(loanRecord.getCollectionStatus().equals(CollectionStatus.doing)) {
			loanCollectionService.close(loanRecord.getId());
			loanRecord.setCollectionStatus(NfsLoanRecord.CollectionStatus.end);	
		}
		return loanRecord;
	}



	@Override
	public LoanDetailForGxtResponseResult getGxtLoanDetail(String loanId,Integer type, Member member) {
		LoanDetailForGxtResponseResult result = new LoanDetailForGxtResponseResult();
		
		LoanInfo loanInfo = result.loanInfo;
		TradeInfo tradeInfo = result.tradeInfo;
		OverDueInfo overDueInfo = result.overDueInfo;
		AfterDelayInfo afterDelayInfo = result.afterDelayInfo;
		UserInfo userInfo = result.userInfo;
		
		NfsLoanApplyDetail applyDetail = null;
		NfsLoanRecord loanRecord = null;

		Integer loanRole = 2;//当前用户角色 0借款人 1放款人 2无关人士
		Integer initiator = 0;//当前用户是否是发起者 0否 1是
		
		//处理刷新机制
		if(type == 1){//detail此时可能生成record了
			//判断是否已经生成借款单
			NfsLoanApplyDetail nfsLoanApplyDetail = loanApplyDetailService.get(Long.parseLong(loanId));
			NfsLoanApplyDetail.Status detailStatus = nfsLoanApplyDetail.getStatus();
			if(detailStatus.equals(NfsLoanApplyDetail.Status.success)){//生成借条了
				NfsLoanRecord record = loanRecordService.findByApplyDetailId(Long.parseLong(loanId));
				Long recordId = record.getId();
				loanId = recordId.toString();
				type = 2;
			}
		}
		
		if(type == 1){
			/**
			 * 查detail
			 */
			applyDetail = loanApplyDetailService.get(Long.valueOf(loanId));
			result.setLoanId(loanId);
			NfsLoanApply loanApply = applyDetail.getApply();
			loanApply = loanApplyService.get(loanApply.getId());
			if(loanApply.getMember().equals(member)){
				initiator = 1;
			}else{
				initiator = 0;
			}
			
			LoanRole detailLoanRole = applyDetail.getLoanRole();
			LoanRole applyLoanRole = loanApply.getLoanRole();
			
			Long id = applyDetail.getMember().getId();
			if(!id.equals(0L)){
				if((detailLoanRole.equals(NfsLoanApply.LoanRole.loanee) && applyDetail.getMember().getId().equals(member.getId()))
						|| (applyLoanRole.equals(NfsLoanApply.LoanRole.loanee) && loanApply.getMember().getId().equals(member.getId()))){
					loanRole = 0;
				}else if((detailLoanRole.equals(NfsLoanApply.LoanRole.loaner) && applyDetail.getMember().getId().equals(member.getId()))
						|| (applyLoanRole.equals(NfsLoanApply.LoanRole.loaner) && loanApply.getMember().getId().equals(member.getId()))){
					loanRole = 1;
				}else{
					loanRole = 2;
				}
			}else{
				if(member!=null){
					if((detailLoanRole.equals(NfsLoanApply.LoanRole.loanee) && StringUtils.equals(applyDetail.getMember().getName(), member.getName()))
							|| (applyLoanRole.equals(NfsLoanApply.LoanRole.loanee) && loanApply.getMember().getId().equals(member.getId()))){
						loanRole = 0;
					}else if((detailLoanRole.equals(NfsLoanApply.LoanRole.loaner) && StringUtils.equals(applyDetail.getMember().getName(), member.getName()))
							|| (applyLoanRole.equals(NfsLoanApply.LoanRole.loaner) && loanApply.getMember().getId().equals(member.getId()))){
						loanRole = 1;
					}else{
						loanRole = 2;
					}
				}
			}
			
			if(applyLoanRole.equals(NfsLoanApply.LoanRole.loaner)){
				result.setLoanerName(loanApply.getMember().getName());
				result.setLoanerHeadimage(loanApply.getMember().getHeadImage());
				result.setLoanerId(loanApply.getMember().getId().toString());
				
				result.setLoaneeName(applyDetail.getMember().getName());
				result.setLoaneeHeadimage(applyDetail.getMember().getHeadImage()==null?Global.getConfig("domain")+Global.getConfig("default.headImage"):applyDetail.getMember().getHeadImage());
			}else{
				result.setLoanerName(applyDetail.getMember().getName());
				result.setLoanerHeadimage(applyDetail.getMember().getHeadImage()==null?Global.getConfig("domain")+Global.getConfig("default.headImage"):applyDetail.getMember().getHeadImage());
				
				result.setLoaneeName(loanApply.getMember().getName());
				result.setLoaneeId(loanApply.getMember().getId().toString());
				result.setLoaneeHeadimage(loanApply.getMember().getHeadImage());
			}
			/**
			 * 	0:待确认
				1:已取消
				2:已拒绝
				3:已超时
			 */
			NfsLoanApplyDetail.Status status = applyDetail.getStatus();
			if(status.equals(NfsLoanApplyDetail.Status.pendingAgree)){
				result.setProgress("待确认");
				result.setStatus(0);
				Date createTime = applyDetail.getCreateTime();
				long pastHour = DateUtils.pastHour(createTime);
				if(pastHour < 24){
					result.setRemainStatus(1);
					result.setRemainTime(2);
				}else if(24 <= pastHour && pastHour < 48){
					result.setRemainStatus(1);
					result.setRemainTime(1);
				}else if(48 <= pastHour && pastHour <= 72){
					result.setRemainStatus(2);
					result.setRemainTime(72-(int) pastHour);
				}
			}else if(status.equals(NfsLoanApplyDetail.Status.canceled)){
				result.setProgress("已取消");
				result.setStatus(1);
				tradeInfo.setCompleteDate(DateUtils.formatDate(applyDetail.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
			}else if(status.equals(NfsLoanApplyDetail.Status.reject)){
				result.setProgress("已拒绝");
				result.setStatus(2);
				tradeInfo.setCompleteDate(DateUtils.formatDate(applyDetail.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
			}else if(status.equals(NfsLoanApplyDetail.Status.expired)){
				result.setProgress("已超时");
				result.setStatus(3);
				tradeInfo.setCompleteDate(DateUtils.formatDate(applyDetail.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
			} 
			
			loanInfo.setAmount(StringUtils.decimalToStr(loanApply.getAmount(), 2));
			Date loanStart = loanApply.getLoanStart();
			loanInfo.setBeginDate(DateUtils.formatDate(loanStart, "yyyy-MM-dd"));
			
			Date dueRepayDate = DateUtils.addCalendarByDate(loanStart, loanApply.getTerm()-1);
			loanInfo.setDueRepayDate(DateUtils.formatDate(dueRepayDate, "yyyy-MM-dd"));
			
			BigDecimal interest = loanApply.getInterest();// 利息
			if (interest != null) {
				loanInfo.setInterest(interest.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			} else {
				loanInfo.setInterest("0");
			}
			
			loanInfo.setLoanPurp(loanApply.getLoanPurp().getName());// 用途
			
			loanInfo.setDisputeResolution(applyDetail.getDisputeResolution().ordinal());
			loanInfo.setRwk(applyDetail.getRmk());
			tradeInfo.setApplyDate(DateUtils.formatDate(loanApply.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			
			result.setLoan_type(applyDetail.getId()+"_"+LoanConstant.TYPE_DETAIL);
		}else{
			/**
			 * type = 2 查record
			 */
			Long recordId = Long.valueOf(loanId);
			loanRecord = loanRecordService.get(recordId);
			
			result.setLoanId(loanId);
			Member loaner = loanRecord.getLoaner();
			Member loanee = loanRecord.getLoanee();
			if(loanee.equals(member)){
				loanRole = 0;
			}else if(loaner.equals(member)){
				loanRole = 1;
			}else{
				loanRole = 2;
			}
			
			result.setLoanerName(loaner.getName());
			result.setLoanerId(loanRecord.getLoaner().getId().toString());
			result.setLoanerHeadimage(loaner.getHeadImage()==null?Global.getConfig("domain")+Global.getConfig("default.headImage"):loaner.getHeadImage());
			
			result.setLoaneeName(loanRecord.getLoaneeName());
			result.setLoaneeId(loanRecord.getLoanee().getId().toString());
			result.setLoaneeHeadimage(loanRecord.getLoanee().getHeadImage()==null?Global.getConfig("domain")+Global.getConfig("default.headImage"):loanRecord.getLoanee().getHeadImage());
			
			/**
			 *  4:今日还款/今日收款
				5:距离还款/收款日30天之内
				6:距离还款/收款日30天以上
				7:延期待确认
				8:还款/收款待确认
				9:今日逾期
				10:已逾期未超过15天
				11:已逾期超过15天
				12:已完成
			**/
			Integer loanStatus = loanRecordService.getGxtLoanRecordProgress(loanRecord);
			result.setStatus(loanStatus);
			int pastDays = CalendarUtil.getIntervalDays(new Date(),loanRecord.getDueRepayDate());
			if(loanStatus == 5){
				pastDays ++; //过去的天数
			}
			if(loanStatus == 5 || loanStatus == 10){
				result.setDays(pastDays);
			}
			
			loanInfo.setDueRepayAmount(StringUtils.decimalToStr(loanRecord.getDueRepayAmount(), 2));// 应还金额
			
			loanInfo.setAmount(StringUtils.decimalToStr(loanRecord.getAmount(), 2));
			loanInfo.setBeginDate(DateUtils.formatDate(loanRecord.getLoanStart(), "yyyy-MM-dd"));
			loanInfo.setDueRepayDate(DateUtils.formatDate(loanRecord.getDueRepayDate(), "yyyy-MM-dd"));
			loanInfo.setInterest(loanRecord.getInterest().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			loanInfo.setLoanPurp(loanRecord.getLoanPurp().getName());
			
			DisputeResolution disputeResolution = loanRecord.getDisputeResolution();
			loanInfo.setDisputeResolution(disputeResolution.ordinal());
			
			NfsLoanContract nowContract = loanContractService.getCurrentContractByLoanId(recordId);
			loanInfo.setContractUrl(Global.getConfig("domain") + nowContract.getContractUrl());
			
			
			// 消息
			applyDetail = loanRecord.getLoanApplyDetail();
			Long applyDetailId = applyDetail.getId();
			applyDetail = loanApplyDetailService.get(applyDetailId);
			
			loanInfo.setRwk(applyDetail.getRmk());
			
			tradeInfo.setApplyDate(DateUtils.formatDate(applyDetail.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			tradeInfo.setReachDate(DateUtils.formatDate(loanRecord.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			
			if(applyDetail.getApply().getMember().equals(member)){
				initiator = 1;
			}else{
				initiator = 0;
			}
			DelayStatus delayStatus = loanRecord.getDelayStatus();// 延期状态
			if(!delayStatus.equals(DelayStatus.initial)){
				NfsLoanPartialAndDelay loanPartialAndDelay = new NfsLoanPartialAndDelay();
				loanPartialAndDelay.setLoan(loanRecord);
				loanPartialAndDelay.setStatus(NfsLoanPartialAndDelay.Status.confirm);
				loanPartialAndDelay.setType(NfsLoanPartialAndDelay.Type.delay);
				List<NfsLoanPartialAndDelay> findDelayList = nfsLoanPartialAndDelayService.findList(loanPartialAndDelay);
				if(findDelayList != null && findDelayList.size() > 0){
					loanPartialAndDelay = findDelayList.get(0);
					afterDelayInfo.setAmount(StringUtils.decimalToStr(loanPartialAndDelay.getRemainAmount(), 2));
					afterDelayInfo.setDueRepayDate(DateUtils.formatDate(loanPartialAndDelay.getNowRepayDate(), "yyyy-MM-dd"));
					afterDelayInfo.setInterest(StringUtils.decimalToStr(loanPartialAndDelay.getDelayInterest().add(loanRecord.getInterest()), 2));
					afterDelayInfo.setLoanStart(DateUtils.formatDate(loanRecord.getLoanStart(), "yyyy-MM-dd"));
					afterDelayInfo.setDelayId(loanPartialAndDelay.getId().toString());
				}
				if(loanPartialAndDelay.getMember().equals(member)){
					initiator = 1;
				}else{
					initiator = 0;
				}
			}
			
			LineDownStatus lineDownStatus = loanRecord.getLineDownStatus();// 线下还款状态
			Date loanDueRepayDate = loanRecord.getDueRepayDate();
			loanInfo.setDueRepayDate(DateUtils.formatDate(loanDueRepayDate, "yyyy-MM-dd"));
			
			Status status = loanRecord.getStatus();
			
			if (!status.equals(Status.repayed)) {
				Long days = (long) 0;
				Date dueRepayDate = loanRecord.getDueRepayDate();// 到期日
				days = DateUtils.pastDays(dueRepayDate);// 过去的天数
				
				if(status.equals(Status.overdue)){
					result.setProgress("已逾期");
					overDueInfo.setOverdueDays(days + "");
					BigDecimal overdueDays = new BigDecimal(
							DateUtils.getDistanceOfTwoDate(loanRecord.getDueRepayDate(), new Date()));
					BigDecimal overdueInterest = LoanUtils.calOverdueInterest(loanRecord.getDueRepayAmount(), overdueDays);
					
					overDueInfo.setOverdueInterest(StringUtils.decimalToStr(overdueInterest, 2));
					loanInfo.setDueRepayAmount(StringUtils.decimalToStr(loanRecord.getDueRepayAmount().add(overdueInterest), 2));
				}else{
					if(loanRecord.getLoanee().equals(member)) {
						result.setProgress("待还款");
					}else {
						result.setProgress("待收款");
					}
				}
				if (!(lineDownStatus.equals(NfsLoanRecord.LineDownStatus.loaneeLineDownRepayment) 
						||(delayStatus.equals(NfsLoanRecord.DelayStatus.loaneeApplyDelay)
						&& (status.equals(Status.penddingRepay) || status.equals(Status.overdue) && days <= 15)) 
						||(delayStatus.equals(NfsLoanRecord.DelayStatus.loanerApplyDelay)
						&& (status.equals(Status.penddingRepay) || status.equals(Status.overdue) && days <= 15)))) {//不是这些状态
					/**
					 * 查该借条仲裁状态
					 */
					NfsLoanArbitration nfsLoanArbitration = new NfsLoanArbitration();// 仲裁
					nfsLoanArbitration.setLoan(loanRecord);
					List<NfsLoanArbitration> findArbitrationList = loanArbitrationService
							.findList(nfsLoanArbitration);
					Boolean isArbitrationed = findArbitrationList != null && findArbitrationList.size() > 0
							&& !findArbitrationList.get(0).getStatus().equals(NfsLoanArbitration.Status.waitingPay) ;
					if (isArbitrationed) {
						result.setArbitrationStatus(1);
					}
					if (loanRole == 1) {
						StringBuffer urgeRecordUrl = new StringBuffer();
						urgeRecordUrl.append(Global.getConfig("domain"));
						urgeRecordUrl.append(Global.getWyjtAppPath());
						
						StringBuffer arbitrationRecordUrl = new StringBuffer();
						arbitrationRecordUrl.append(Global.getConfig("domain"));
						arbitrationRecordUrl.append(Global.getWyjtAppPath());
						if (isArbitrationed) {
							result.setArbitrationRecordUrl(
									arbitrationRecordUrl.append("/arbitration/goArbSchedule?arbRecordId=" 
											+ findArbitrationList.get(0).getId()).toString()); // 仲裁流程界面
						}  
					} 
				}
			} else {
				result.setProgress("已还款");
				Date completeDate = loanRecord.getCompleteDate();
				Integer overDueDays = DateUtils.getDifferenceOfTwoDate(completeDate, loanDueRepayDate);
				if(overDueDays > 0){
					overDueInfo.setOverdueDays(overDueDays+"");
					overDueInfo.setOverdueInterest(StringUtils.decimalToStr(loanRecord.getOverdueInterest(), 2));
				}
				tradeInfo.setCompleteDate(DateUtils.formatDate(completeDate, "yyyy-MM-dd HH:mm:ss"));
			}
			result.setLoan_type(loanRecord.getId()+"_"+LoanConstant.TYPE_RECORD);
		}
		
		result.setLoanId(loanId);

		result.setLoanInfo(loanInfo);
		result.setTradeInfo(tradeInfo);
		result.setOverDueInfo(overDueInfo);
		result.setAfterDelayInfo(afterDelayInfo);
		
		if(member != null){
			BigDecimal curBal = memberActService.getCurBal(member.getId());
			Integer verifiedList = member.getVerifiedList();
			if(VerifiedUtils.isVerified(verifiedList, 1) && VerifiedUtils.isVerified(verifiedList, 2)){
				userInfo.setName(member.getName());
				userInfo.setRealIdentityStatus(1);
			}else{
				userInfo.setRealIdentityStatus(0);
			}
			userInfo.setCurBal(StringUtils.decimalToStr(curBal, 2));
			userInfo.setLoanRole(loanRole);
			userInfo.setInitiator(initiator);
			userInfo.setUsername(member.getUsername());
			
			result.setUserInfo(userInfo);
		}
		
		return result;
	}

	@Override
	public Integer getGxtLoanRecordProgress(NfsLoanRecord loanRecord) {
		Date dueRepayDate = loanRecord.getDueRepayDate();
		NfsLoanRecord.Status recordStatus = loanRecord.getStatus();//借条状态
		 LineDownStatus lineDownStatus = loanRecord.getLineDownStatus();//线下还款状态
		 PartialStatus partialStatus = loanRecord.getPartialStatus();//部分还款状态
		 DelayStatus delayStatus = loanRecord.getDelayStatus();//延期申请状态
		 int pastDays = CalendarUtil.getIntervalDays(new Date(),dueRepayDate);//过去的天数
		 String formatNowDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
		 String formatDueRepayDate = DateUtils.formatDate(dueRepayDate, "yyyy-MM-dd");
		 Integer progress = 0;
		
		 if (recordStatus.equals(NfsLoanRecord.Status.penddingRepay) && lineDownStatus.equals(LineDownStatus.initial)
				 && delayStatus.equals(DelayStatus.initial) && partialStatus.equals(PartialStatus.initial)) {
			 if (pastDays >= 30) {
				 progress = 6;
			 } else if (pastDays > 0) {
				 progress = 5;
			 } 
			 if (formatNowDate.equals(formatDueRepayDate)) {
				 progress = 4;
			 } 
		 } else if ((recordStatus.equals(NfsLoanRecord.Status.penddingRepay) 
				 || recordStatus.equals(NfsLoanRecord.Status.overdue)) 
				 && lineDownStatus.equals(LineDownStatus.loaneeLineDownRepayment)) {
			 progress = 8;
		 } else if ((recordStatus.equals(NfsLoanRecord.Status.penddingRepay) 
				 || recordStatus.equals(NfsLoanRecord.Status.overdue)) 
				 && (delayStatus.equals(DelayStatus.loaneeApplyDelay) || delayStatus.equals(DelayStatus.loanerApplyDelay))) {
			 progress = 7;
		 } else if (recordStatus.equals(NfsLoanRecord.Status.repayed)) {
			 progress = 12;
		 }else if(recordStatus.equals(NfsLoanRecord.Status.overdue)){
			 if (pastDays-1 == 0) {
				 progress = 9;
			 } else if (pastDays >1 && pastDays <= 15) {
				 progress = 10;
			 }else if (pastDays >15){ 
				 progress = 11; 
			 }
		 }
		return progress;
	}

	@Override
	public CreditInfoResponseResult getLoanInfo(Member member) {
		CreditInfoResponseResult result = new CreditInfoResponseResult();
		result.setHeadImage(member.getHeadImage());
		result.setName(member.getName());
		result.setGender(member.getGender().ordinal());
		result.setRealIdentityStatus(VerifiedUtils.isVerified(member.getVerifiedList(), 1)==true?1:0);
		result.setBankCardStatus(VerifiedUtils.isVerified(member.getVerifiedList(), 3)==true?1:0);
		
		Map<String,Object> borrowDetailMap1 = getBorrowDetail(member,7);//一周
		Map<String,Object> borrowDetailMap2 = getBorrowDetail(member,30);//一月
		Map<String,Object> borrowDetailMap3 = getBorrowDetail(member,180);//六个月
		
		Map<String,Object> overdueDetailMap1 = getOverdueDetail(member,7,1);//一周
		Map<String,Object> overdueDetailMap2 = getOverdueDetail(member,30,1);//一月
		Map<String,Object> overdueDetailMap3 = getOverdueDetail(member,180,1);//六个月
		
		Map<String,Object> loanDetailMap1 = getLoanDetail(member,7);//一周
		Map<String,Object> loanDetailMap2 = getLoanDetail(member,30);//一月
		Map<String,Object> loanDetailMap3 = getLoanDetail(member,180);//六个月
		
		result.setBorrowDetailMap1(borrowDetailMap1);
		result.setBorrowDetailMap2(borrowDetailMap2);
		result.setBorrowDetailMap3(borrowDetailMap3);
		result.setOverdueDetailMap1(overdueDetailMap1);
		result.setOverdueDetailMap2(overdueDetailMap2);
		result.setOverdueDetailMap3(overdueDetailMap3);
		result.setLoanDetailMap1(loanDetailMap1);
		result.setLoanDetailMap2(loanDetailMap2);
		result.setLoanDetailMap3(loanDetailMap3);
		return result;
	}

	private Map<String, Object> getLoanDetail(Member member, int days) {
		Map<String, Object> map = new HashMap<>();
		BigDecimal totalBorrowMoney = BigDecimal.ZERO;//借入累计金额
		BigDecimal maxBorrowMoney = BigDecimal.ZERO;//借入最大金额
		int borrowNum = 0;//借入累计笔数
		BigDecimal totalLendMoney = BigDecimal.ZERO;//借出累计金额
		BigDecimal maxLendMoney = BigDecimal.ZERO;//借出最大金额
		int lendNum = 0;//借出累计笔数
		int timeType = 1;
		Date beginDate = DateUtils.addCalendarByMonth(new Date(), -days);
		List<BigDecimal> borrowAmountList = new ArrayList<BigDecimal>();
		// 借入
		NfsLoanRecord nfsLoanRecord = new NfsLoanRecord();
		nfsLoanRecord.setLoanee(member);
		// 借入
		List<NfsLoanRecord> borrowList = loanRecordDao.findLoanList(nfsLoanRecord, beginDate, timeType);
		if (borrowList.size() > 0) {
			for (NfsLoanRecord record : borrowList) {
				borrowAmountList.add(record.getAmount().add(record.getInterest()));
				totalBorrowMoney = record.getAmount().add(record.getInterest()).add(totalBorrowMoney);
			}
			borrowNum = borrowList.size();
		}
		if(borrowAmountList.size() > 0) {
			for(int i=0;i<borrowAmountList.size()-1;i++){
				for(int j=0;j<borrowAmountList.size()-1-i;j++){
					if(borrowAmountList.get(j).compareTo(borrowAmountList.get(j+1)) == -1) {
						BigDecimal tmp = borrowAmountList.get(j);
						borrowAmountList.set(j, borrowAmountList.get(j+1));
						borrowAmountList.set(j+1, tmp);
					}
				}
				
			}
			maxBorrowMoney = borrowAmountList.get(0);
		}
		
		List<BigDecimal> lendAmountList = new ArrayList<BigDecimal>();
		// 借出
		NfsLoanRecord loanRecord = new NfsLoanRecord();
		loanRecord.setLoaner(member);
		// 借出
		List<NfsLoanRecord> lendList = loanRecordDao.findLoanList(loanRecord, beginDate, timeType);
		if (lendList.size() > 0) {
			for (NfsLoanRecord record : lendList) {
				lendAmountList.add(record.getAmount().add(record.getInterest()));
				totalLendMoney = record.getAmount().add(record.getInterest()).add(totalLendMoney);
			}
			lendNum = lendList.size();
		}
		if(lendAmountList.size() > 0) {
			for(int i=0;i<lendAmountList.size()-1;i++){
				for(int j=0;j<lendAmountList.size()-1-i;j++){
					if(lendAmountList.get(j).compareTo(lendAmountList.get(j+1)) == -1) {
						BigDecimal tmp = lendAmountList.get(j);
						lendAmountList.set(j, lendAmountList.get(j+1));
						lendAmountList.set(j+1, tmp);
					}
				}
				
			}
			maxLendMoney = lendAmountList.get(0);
		}

		map.put("totalBorrowMoney", totalBorrowMoney);
		map.put("maxBorrowMoney", maxBorrowMoney);
		map.put("borrowNum", borrowNum);
		map.put("totalLendMoney", totalLendMoney);
		map.put("maxLendMoney", maxLendMoney);
		map.put("lendNum", lendNum);
		return map;
	}

	private Map<String, Object> getBorrowDetail(Member member, int days) {
		Map<String, Object> map = new HashMap<>();
		BigDecimal overdueMoney = BigDecimal.ZERO; // 逾期待还金额
		int overdueNum = 0; // 逾期待还个数
		BigDecimal penddingRepayMoney = BigDecimal.ZERO; // 未逾期待还金额
		int penddingRepayNum = 0; // 未逾期待还个数
		BigDecimal overRepayMoney = BigDecimal.ZERO; // 逾期已还金额
		int overRepayNum = 0; // 逾期已还个数
		BigDecimal repayMoney = BigDecimal.ZERO; // 未逾期已还金额
		int repayNum = 0; // 未逾期已还个数
		BigDecimal delayMoney = BigDecimal.ZERO; // 延期金额
		int delayNum = 0; // 延期个数
		


		int timeType = 1;
		Date beginDate = DateUtils.addCalendarByMonth(new Date(), -days);
		NfsLoanRecord nfsLoanRecord = new NfsLoanRecord();
		nfsLoanRecord.setLoanee(member);
		// 逾期待还
		List<NfsLoanRecord> overdueList = loanRecordDao.findBorrowOverNotReturnList(nfsLoanRecord,beginDate, timeType);
		if (overdueList.size() > 0) {
			for (NfsLoanRecord record : overdueList) {
				overdueMoney = record.getAmount().add(record.getInterest()).add(overdueMoney);
			}
			overdueNum = overdueList.size();
		}
		//未逾期待还
		List<NfsLoanRecord> penddingRepayList = loanRecordDao.penddingRepayList(nfsLoanRecord,beginDate, timeType);
		if(penddingRepayList.size() > 0) {
			for (NfsLoanRecord record : penddingRepayList) {
				penddingRepayMoney = record.getAmount().add(record.getInterest()).add(penddingRepayMoney);
			}
			penddingRepayNum = penddingRepayList.size();
		}
		// 统计逾期已还金额
		List<NfsLoanRecord> overRepayList = loanRecordDao.findBorrowandLendOverList(nfsLoanRecord, beginDate,timeType);
		if (overRepayList.size() > 0) {
			for (NfsLoanRecord record : overRepayList) {
				overRepayMoney = record.getAmount().add(record.getInterest()).add(overRepayMoney);
			}
			overRepayNum = overRepayList.size();
		}
		// 统计未逾期已还金额
		List<NfsLoanRecord> repayList = loanRecordDao.findBorrowOnTimeReturnList(nfsLoanRecord, beginDate,timeType);
		if (repayList.size() > 0) {
			for (NfsLoanRecord record : repayList) {
				repayMoney = record.getAmount().add(record.getInterest()).add(repayMoney);
			}
			repayNum = repayList.size();
		}
		//统计申请延期记录
		NfsLoanOperatingRecord operatingRecord = new NfsLoanOperatingRecord();
		operatingRecord.setOldRecord(nfsLoanRecord);
		operatingRecord.setType(NfsLoanOperatingRecord.Type.delay);
		operatingRecord.setBeginTime(beginDate);
		operatingRecord.setEndTime(new Date());
		List<NfsLoanOperatingRecord> delayList = loanOperatingRecordService.findList(operatingRecord);
		if(delayList.size() > 0) {
			for (NfsLoanOperatingRecord record : delayList) {
				NfsLoanRecord oldRecord = record.getOldRecord();
				delayMoney = oldRecord.getAmount().add(oldRecord.getInterest()).add(delayMoney);
			}
			delayNum = delayList.size();
		}
		
		map.put("overdueMoney", overdueMoney);
		map.put("overdueNum", overdueNum);
		map.put("penddingRepayMoney", penddingRepayMoney);
		map.put("penddingRepayNum", penddingRepayNum);
		map.put("overRepayMoney", overRepayMoney);
		map.put("overRepayNum", overRepayNum);
		map.put("repayMoney", repayMoney);
		map.put("repayNum", repayNum);
		map.put("delayMoney", delayMoney);
		map.put("delayNum", delayNum);
		return map;
	}

	public Map<String, Object> getOverdueDetail(Member member, int days, int timeType) {
		Map<String, Object> map = new HashMap<>();
		BigDecimal totalLoanAmt = BigDecimal.ZERO; // 总金额
		BigDecimal overdueRepayAmt = BigDecimal.ZERO; // 逾期已还金额
		BigDecimal overdueAmt = BigDecimal.ZERO; // 逾期未还金额
		int totalCount = 0; // 总次数
		int overdueRepayCount= 0; // 逾期已还次数
		int overdueCount = 0; // 逾期未还次数
	
		Date date = DateUtils.addCalendarByDate(new Date(), -days);
		// 借入
		NfsLoanRecord nfsLoanRecord = new NfsLoanRecord();
		nfsLoanRecord.setLoanee(member);
		// 借入
		List<NfsLoanRecord> loanList = loanRecordDao.findLoanList(nfsLoanRecord, date, timeType);
		if (loanList.size() > 0) {
			for (NfsLoanRecord record : loanList) {
				totalLoanAmt = record.getAmount().add(record.getInterest()).add(totalLoanAmt);
			}
			totalCount = loanList.size();
		}

		// 统计逾期已还金额
		List<NfsLoanRecord> overRepayList = loanRecordDao.findBorrowandLendOverList(nfsLoanRecord, date,timeType);
		if (overRepayList.size() > 0) {
			for (NfsLoanRecord record : overRepayList) {
				overdueRepayAmt = record.getAmount().add(record.getInterest()).add(overdueRepayAmt);
			}
			overdueRepayCount = overRepayList.size();
		}
		// 逾期未还
		List<NfsLoanRecord> overList = loanRecordDao.findBorrowOverNotReturnList(nfsLoanRecord,date, timeType);
		if (overList.size() > 0) {
			for (NfsLoanRecord record : overList) {
				overdueAmt = record.getAmount().add(record.getInterest()).add(overdueAmt);
			}
			overdueCount = overList.size();
		}

		// 金额百分比
		BigDecimal totalAmtRatio = BigDecimal.ZERO;
		BigDecimal overRepayAmtRatio = BigDecimal.ZERO;
		BigDecimal overdueAmtRatio = BigDecimal.ZERO;
		if (totalLoanAmt.intValue() != 0) {
			totalAmtRatio = overdueAmt.add(overdueRepayAmt).divide(totalLoanAmt, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(1,
					BigDecimal.ROUND_HALF_UP);
			overRepayAmtRatio = overdueRepayAmt.divide(totalLoanAmt, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(1,
					BigDecimal.ROUND_HALF_UP);
			overdueAmtRatio = overdueAmt.divide(totalLoanAmt, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(1,
					BigDecimal.ROUND_HALF_UP);
		}

		// 次数百分比
		double totalCountRatio = 0;
		double overRepayCountRatio = 0;
		double overdueCountRatio = 0;
		BigDecimal totalRatio = BigDecimal.ZERO;
		BigDecimal overRepayRatio = BigDecimal.ZERO;
		BigDecimal overdueRatio = BigDecimal.ZERO;
		if (totalCount != 0) {
			totalCountRatio = (double) (overdueCount+overdueRepayCount) / totalCount;
			totalRatio = new BigDecimal(totalCountRatio * 100).setScale(1,BigDecimal.ROUND_HALF_UP);
			overRepayCountRatio = (double) (overdueRepayCount) / totalCount;
			overRepayRatio = new BigDecimal(overRepayCountRatio * 100).setScale(1,BigDecimal.ROUND_HALF_UP);
			overdueCountRatio = (double) (overdueCount) / totalCount;
			overdueRatio = new BigDecimal(overdueCountRatio * 100).setScale(1,BigDecimal.ROUND_HALF_UP);
		}
		
		map.put("totalMoneyRatio", totalAmtRatio);
		map.put("totalRatio", totalRatio);
		map.put("overRepayMoneyRatio", overRepayAmtRatio);
		map.put("overRepayRatio", overRepayRatio);
		map.put("overdueMoneyRatio", overdueAmtRatio);
		map.put("overdueRatio", overdueRatio);
		return map;
	}
	
	public Map<String, Object> getLoanInfo(Member member, int days) {
	
		BigDecimal remainRepayMoney = new BigDecimal("0"); // 待还金额
		String remainRepayNum = "0"; // 待还次数
		BigDecimal remainCollectMoney = new BigDecimal("0"); // 待收金额
		String remainCollectNum = "0"; // 待收次数
		BigDecimal overRepayMoney = new BigDecimal("0"); // 逾期已还金额
		String overRepayNum = "0"; // 逾期已还次数
		BigDecimal overMoney = new BigDecimal("0"); // 逾期未还金额
		String overNum = "0"; // 逾期未还金额
		BigDecimal lendMoney = new BigDecimal("0"); // 借出金额
		String lendNum = "0"; // 借出次数
		BigDecimal borrowMoney = new BigDecimal("0"); // 借入金额
		String borrowNum = "0"; // 借入次数
		String onTimeNum = "0"; // 按时还款
		BigDecimal onTimeMoney = new BigDecimal("0"); // 按时还款
		String beforeTimeNum = "0"; // 延期还款次数
		BigDecimal beforeTimeMoney = new BigDecimal("0"); // 延期还款

		Date date = DateUtils.addCalendarByDate(new Date(), -days);
		// 时间的判断（六月前的是0，其余为1）
		int timeType = 1;

		NfsLoanRecord nfsLoanRecord1 = new NfsLoanRecord();
		nfsLoanRecord1.setLoanee(member);
		NfsLoanRecord nfsLoanRecord2 = new NfsLoanRecord();
		nfsLoanRecord2.setLoaner(member);
		// 借入
		List<NfsLoanRecord> findborrowandLendList3 = loanRecordDao.findLoanList(nfsLoanRecord1, date, timeType);
		if (findborrowandLendList3.size() > 0) {
			BigDecimal count = new BigDecimal(0);
			for (NfsLoanRecord re : findborrowandLendList3) {
				count = re.getAmount().add(re.getInterest()).add(count);
			}
			borrowNum = findborrowandLendList3.size() + "";
			borrowMoney = count;
		}
		// 待还
		List<NfsLoanRecord> findborrowandLendList = loanRecordDao.findBorrowandLendList(nfsLoanRecord1, date,timeType);
		if (findborrowandLendList.size() > 0) {
			BigDecimal count = new BigDecimal(0);
			for (NfsLoanRecord re : findborrowandLendList) {
				count = re.getDueRepayAmount().add(count);
			}
			remainRepayNum = findborrowandLendList.size() + "";
			remainRepayMoney = count;
		}
		// 统计逾期已还金额
		List<NfsLoanRecord> findborrowandLendList1 = loanRecordDao.findBorrowandLendOverList(nfsLoanRecord1, date,timeType);
		if (findborrowandLendList1.size() > 0) {
			BigDecimal count = new BigDecimal(0);
			for (NfsLoanRecord re : findborrowandLendList1) {
				count = re.getAmount().add(re.getInterest()).add(count);
			}
			overRepayNum = findborrowandLendList1.size() + "";
			overRepayMoney = count;
		}
		// 逾期未还
		List<NfsLoanRecord> findborrowandLendList2 = loanRecordDao.findBorrowOverNotReturnList(nfsLoanRecord1,date, timeType);
		if (findborrowandLendList2.size() > 0) {
			BigDecimal count = new BigDecimal(0);
			for (NfsLoanRecord re : findborrowandLendList2) {
				count = re.getAmount().add(re.getInterest()).add(count);
			}
			overMoney = count;
			overNum = findborrowandLendList2.size() + "";
		}
		// 总借出
		List<NfsLoanRecord> findborrowandLendList5 = loanRecordDao.findLoanList(nfsLoanRecord2, date, timeType);
		if (findborrowandLendList5.size() > 0) {
			BigDecimal count = new BigDecimal(0);
			for (NfsLoanRecord re : findborrowandLendList5) {
				count = re.getAmount().add(re.getInterest()).add(count);
			}
			lendNum = findborrowandLendList5.size() + "";
			lendMoney = count;
		}
		// 待收
		List<NfsLoanRecord> findborrowandLendList6 = loanRecordDao.findBorrowandLendList(nfsLoanRecord2, date,timeType);
		if (findborrowandLendList6.size() > 0) {
			BigDecimal count = new BigDecimal(0);
			for (NfsLoanRecord re : findborrowandLendList6) {
				count = re.getDueRepayAmount().add(count);
			}
			remainCollectNum = findborrowandLendList6.size() + "";
			remainCollectMoney = count;
		}
		// 统计按时还款

		List<NfsLoanRecord> findborrowandLendList7 = loanRecordDao.findBorrowOnTimeReturnList(nfsLoanRecord1, date,timeType);
		if (findborrowandLendList7.size() > 0) {
			BigDecimal count = new BigDecimal(0);
			for (NfsLoanRecord re : findborrowandLendList7) {
				count = re.getAmount().add(re.getInterest()).add(count);
			}
			onTimeMoney = count;
			onTimeNum = findborrowandLendList7.size() + "";
		}
		NfsLoanOperatingRecord nfsLoanOperatingRecord = new NfsLoanOperatingRecord();
		nfsLoanOperatingRecord.setOldRecord(nfsLoanRecord1);
		// 延期 findBorrowDelyList
		List<NfsLoanOperatingRecord> findborrowandLendList8 = loanOperatingRecordService.findBorrowDelyList(nfsLoanOperatingRecord, date, timeType);
		if (findborrowandLendList8.size() > 0) {
			BigDecimal count = new BigDecimal(0);
			for (NfsLoanOperatingRecord re : findborrowandLendList8) {
				count = re.getNowRecord().getAmount().add(re.getNowRecord().getInterest()).add(count);
			}
			beforeTimeNum = findborrowandLendList8.size() + "";
			beforeTimeMoney = count;
		}
		remainRepayMoney = remainRepayMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
		remainCollectMoney = remainCollectMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
		lendMoney = lendMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
		overRepayMoney = overRepayMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
		overMoney = overMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
		onTimeMoney = onTimeMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
		beforeTimeMoney = beforeTimeMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
		borrowMoney = borrowMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
		Map<String, Object> map = new HashMap<>();

		
		map.put("borrowAmt", borrowMoney);
		map.put("borrowCnt", borrowNum);
		map.put("lendAmt", lendMoney);
		map.put("lendCnt", lendNum);
		
		map.put("pendingRepayAmt", remainRepayMoney);
		map.put("pendingRepayCnt", remainRepayNum);
		map.put("pendingReceiveAmt", remainCollectMoney);
		map.put("pendingReceiveCnt", remainCollectNum);
		
		map.put("overdueRepayAmt", overRepayMoney);
		map.put("overdueRepayCnt", overRepayNum);
		map.put("overdueAmt", overMoney);
		map.put("overdueCnt", overNum);
		
		map.put("repayOnTimeAmt", onTimeMoney);
		map.put("repayOnTimeCnt", onTimeNum);
		map.put("delayLoanAmt", beforeTimeMoney);
		map.put("delayLoanCnt", beforeTimeNum);
			
		return map;
	}

}