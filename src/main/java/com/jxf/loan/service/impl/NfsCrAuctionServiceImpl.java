package com.jxf.loan.service.impl;


import java.math.BigDecimal;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.alibaba.fastjson.JSON;

import com.jxf.loan.constant.LoanConstant;
import com.jxf.loan.constant.RecordMessage;
import com.jxf.loan.dao.NfsCrAuctionDao;
import com.jxf.loan.entity.NfsCrAuction;
import com.jxf.loan.entity.NfsCrAuction.Status;

import com.jxf.loan.entity.NfsLoanDetailMessage;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsCrAuctionService;
import com.jxf.loan.service.NfsCrContractService;

import com.jxf.loan.service.NfsLoanDetailMessageService;
import com.jxf.loan.service.NfsLoanRecordService;

import com.jxf.loan.utils.LoanUtils;
import com.jxf.mem.entity.Member;

import com.jxf.mem.entity.MemberMessage;

import com.jxf.mem.service.MemberActService;

import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.service.impl.MemberMessageServiceImpl;
import com.jxf.mem.utils.MemUtils;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.mms.msg.impl.SendSmsMsgServiceImpl;
import com.jxf.mms.service.impl.SendMsgServiceImpl;
import com.jxf.nfs.constant.ActSubConstant;
import com.jxf.nfs.constant.TrxRuleConstant;

import com.jxf.nfs.service.NfsActService;

import com.jxf.svc.config.Constant;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.area.entity.Area;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.DateUtils;

import com.jxf.svc.utils.Exceptions;

import com.jxf.svc.utils.StringUtils;

import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.auction.LoanCrAuctionListRequestParam;
import com.jxf.web.model.wyjt.app.auction.LoanCrAuctionListResponseResult;
import com.jxf.web.model.wyjt.app.auction.LoanCrAuctionListResponseResult.CrAuction;



import com.jxf.web.model.wyjt.app.auction.MinePurchaseListRequestParam;
import com.jxf.web.model.wyjt.app.auction.MineTransferListRequestParam;
import com.jxf.web.model.wyjt.app.auction.PayAuctionRequestParam;
import com.jxf.web.model.wyjt.app.auction.PurchaseDetailResponseResult;
import com.jxf.web.model.wyjt.app.auction.ReleaseAuctionResponseResult;

/**
 * 债权买卖ServiceImpl
 * @author wo
 * @version 2018-12-25
 */
@Service("nfsCrAuctionService")
@Transactional(readOnly = true)
public class NfsCrAuctionServiceImpl extends CrudServiceImpl<NfsCrAuctionDao, NfsCrAuction> implements NfsCrAuctionService{

	@Autowired
	private NfsCrAuctionDao crAuctionDao;
	@Autowired
	private NfsActService actService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberActService memberActService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private SendSmsMsgService sendSmsMsgService;
	@Autowired
	private MemberMessageServiceImpl memberMessageServiceImpl;
	@Autowired
	private SendSmsMsgServiceImpl sendSmsMsgServiceImpl;
	@Autowired
	private SendMsgServiceImpl sendMsgServiceImpl;

	@Autowired
	private NfsLoanDetailMessageService loanDetailMessageService;
	@Autowired
	private NfsCrContractService crContractService;

	
	
	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	
	public NfsCrAuction get(Long id) {
		return super.get(id);
	}
	
	public List<NfsCrAuction> findList(NfsCrAuction nfsCrAuction) {
		return super.findList(nfsCrAuction);
	}
	
	public Page<NfsCrAuction> findPage(Page<NfsCrAuction> page, NfsCrAuction nfsCrAuction) {
		return super.findPage(page, nfsCrAuction);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsCrAuction nfsCrAuction) {
		if(nfsCrAuction.getIsNewRecord()) {
			nfsCrAuction.preInsert();
			nfsCrAuction.setProofStatus(NfsCrAuction.ProofStatus.pendingApply);
			crAuctionDao.insert(nfsCrAuction);
		}else {
			nfsCrAuction.preUpdate();
			crAuctionDao.update(nfsCrAuction);
		}

	}
	
	@Transactional(readOnly = false)
	public void delete(NfsCrAuction nfsCrAuction) {
		super.delete(nfsCrAuction);
	}

	@Override
	public Page<NfsCrAuction> findPage(NfsCrAuction crAuction, Integer pageNo, Integer pageSize) {
	
		Page<NfsCrAuction> page = new Page<NfsCrAuction>(pageNo == null  ? 1 : pageNo , pageSize == null ? 20 : pageSize);	
		crAuction.setPage(page);
		List<NfsCrAuction> crAuctionList = crAuctionDao.findList(crAuction);
		page.setList(crAuctionList);
		
		return page;
	}

	@Override
	@Transactional(readOnly = false)
	public int payCrAuction(NfsCrAuction crAuction,NfsLoanRecord loanRecord) {
		Member buyer = crAuction.getCrBuyer();
		Member seller = crAuction.getCrSeller();
		BigDecimal sellPrice = crAuction.getCrSellPrice();
		// 更新债权拍卖记录状态
		crAuction.setStatus(NfsCrAuction.Status.successed);
		crAuction.setEndTime(new Date());
		crAuction.setCrBuyer(buyer);
		crAuction.setCrBuyPrice(sellPrice);
		crAuction.setRmk("转让金额已入账");
		crAuction.preUpdate();
		int updateLines = crAuctionDao.update(crAuction);
		if (updateLines == 0) {
			logger.error("会员{}购买借条{}更新债转记录失败！", buyer.getId(), loanRecord.getId());
			throw new RuntimeException("会员[ID:" + buyer.getId() + "]购买借条[ID:" + loanRecord.getId() + "]更新债转记录失败！");
		}
		// 更新借条记录状态
		loanRecord.setAuctionStatus(NfsLoanRecord.AuctionStatus.auctioned);
		loanRecordService.save(loanRecord);
		
		//先更新账户余额
		BigDecimal loanActBal = memberActService.getMemberAct(buyer, ActSubConstant.MEMBER_LOAN_BAL).getCurBal();
		BigDecimal avlActBal = memberActService.getMemberAct(buyer, ActSubConstant.MEMBER_AVL_BAL).getCurBal();
		BigDecimal pendingReceiveAmount = loanRecord.getDueRepayAmount();
		if(loanActBal.add(avlActBal).compareTo(sellPrice) < 0) {
			return Constant.UPDATE_FAILED;
		}
		Long orgId = crAuction.getId();
		int updateCode = Constant.UPDATE_FAILED;
		//更新买入人和卖出人的可用余额和借款账户余额
		if(loanActBal.compareTo(sellPrice) >= 0) {
			//借款账户金额足够
			 updateCode = actService.updateAct(TrxRuleConstant.CR_PAY_LOANBAL, sellPrice, buyer, seller, orgId);
			if(updateCode == Constant.UPDATE_FAILED) {
				throw new RuntimeException("会员[ID:"+ buyer.getId() + "]购买借条[ID:" + loanRecord.getId() + "]全部使用借款账户余额付款时更新账户余额异常！");
			}
		}else {
			//借款账户金额不足
			if(loanActBal.compareTo(BigDecimal.ZERO) == 0) {
				//借款账户金额为0 全部用可用余额付款
				updateCode = actService.updateAct(TrxRuleConstant.CR_PAY_AVLBAL, sellPrice, buyer, seller, orgId);
				if(updateCode == Constant.UPDATE_FAILED) {
					throw new RuntimeException("会员[ID:"+ buyer.getId() + "]购买借条[ID:" + loanRecord.getId() + "]全部使用可用余额付款时更新账户余额异常！");
				}
			}else {
				//扣除借款账户金额
				updateCode = actService.updateAct(TrxRuleConstant.CR_PAY_LOANBAL, loanActBal, buyer, seller, orgId);
				if(updateCode == Constant.UPDATE_FAILED) {
					throw new RuntimeException("会员[ID:"+ buyer.getId() + "]购买借条[ID:" + loanRecord.getId() + "]使用借款账户余额付款时更新账户余额异常！");
				}
				//从可用余额账户扣款金额
				BigDecimal amountFromAvlBal = sellPrice.subtract(loanActBal);
				updateCode = actService.updateAct(TrxRuleConstant.CR_PAY_AVLBAL, amountFromAvlBal, buyer, seller, orgId);
				if(updateCode == Constant.UPDATE_FAILED) {
					throw new RuntimeException("会员[ID:"+ buyer.getId() + "]购买借条[ID:" + loanRecord.getId() + "]使用可用余额付款时更新账户余额异常！");
				}
			}
		}
		//更新买入人和卖出人的待收账户余额
		updateCode = actService.updateAct(TrxRuleConstant.CR_PAY_PENDINGRECEIVE, pendingReceiveAmount, buyer, seller, orgId);
		if(updateCode == Constant.UPDATE_FAILED) {
			throw new RuntimeException("会员[ID:"+ buyer.getId() + "]购买借条[ID:" + loanRecord.getId() + "]更新待收账户余额异常！");
		}
		

		//创建合同
		crContractService.createContract(crAuction);
		
		return Constant.UPDATE_SUCCESS;
	}
	
	@Override
	public Integer checkStatus(NfsLoanRecord nfsLoanRecord) {
		NfsCrAuction nfsCrAuction = new NfsCrAuction();
		nfsCrAuction.setLoanRecord(nfsLoanRecord);
		List<NfsCrAuction> list = crAuctionDao.findList(nfsCrAuction);
		for (NfsCrAuction auction : list) {
			Status status = auction.getStatus();
			if(status == Status.successed) {
				return 1;
			}else if(status == Status.auditFailed) {
				return 2;
			}
		}
		return 0;
	}
	
	/**
	 * 	申请转让
	 */
	@Override
	@Transactional(readOnly = false)
	public ResponseData applyCrAuction(NfsLoanRecord loanRecord, String price, ReleaseAuctionResponseResult result) {
		Member member = memberService.get(loanRecord.getLoanee());
		Area area = member.getArea();
		if(area == null||area.getId() == null) {
			return ResponseData.error("借条归属地不存在,无法债权转让");
		}
		
		loanRecord.setAuctionStatus(NfsLoanRecord.AuctionStatus.auction);
		loanRecordService.save(loanRecord);
		
		NfsCrAuction auction = new NfsCrAuction();
		auction.setLoanRecord(loanRecord);
		auction.setCrSeller(loanRecord.getLoaner());
		auction.setCrSellPrice(new BigDecimal(price));
		auction.setArea(area);
		auction.setStatus(NfsCrAuction.Status.audit);
		auction.setRmk("等待平台审核");
		auction.setIsPub(false);
		save(auction);
		try {
			//发送会员消息
			MemberMessage message = memberMessageServiceImpl.sendMessage(MemberMessage.Type.applyAuctionImsLoanee,loanRecord.getId());
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("name", loanRecord.getLoaner().getName());
			map.put("money", loanRecord.getAmount());
			// 短信
			sendSmsMsgService.sendCollectionSms("applyAuctionImsLoanee", loanRecord.getLoaneePhoneNo(), map);
			
			//推送
			sendMsgServiceImpl.beforeSendAppMsg(message);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}
		
		result = getCrAuctionDetail(auction.getId().toString());
		return ResponseData.success("申请转让借条成功", result);
	}

	@Override
	public Map<String, String> buyCrOperationLegalCheck(NfsLoanRecord loanRecord, NfsCrAuction crAuction) {
		Map<String, String> resultMap = new HashMap<String, String>();
		NfsCrAuction.Status crStatus = crAuction.getStatus();
		NfsLoanRecord.Status loanStatus = loanRecord.getStatus();
		resultMap.put("success", "true");
		//校验债权记录状态
		if(!NfsCrAuction.Status.forsale.equals(crStatus)) {
			resultMap.put("success", "false");
			resultMap.put("message", "该债权记录状态已更新，请确认后再操作！");
			return resultMap;
		}
		if(crAuction.getCrSellPrice().compareTo(BigDecimal.ONE) < 0 ) {
			resultMap.put("success", "false");
			resultMap.put("message", "债权转让金额必须大于一元！");
			return resultMap;
		}
		if(!crAuction.getIsPub()) {
			resultMap.put("success", "false");
			resultMap.put("message", "该债权记录状态已更新，请确认后再操作！");
			return resultMap;
		}
		//不能购买与自己有关的借条
		Long loaneeId = loanRecord.getLoanee().getId();
		Long loanerId = loanRecord.getLoaner().getId();
		Long buyerId = crAuction.getCrBuyer().getId();
		if(loaneeId.longValue() == buyerId.longValue() || loanerId.longValue() == buyerId.longValue()) {
			resultMap.put("success", "false");
			resultMap.put("message", "不能购买自己的未还逾期借条！");
			return resultMap;
		}
		//校验借条记录状态
		if(!loanRecord.getAuctionStatus().equals(NfsLoanRecord.AuctionStatus.auction)) {
			resultMap.put("success", "false");
			resultMap.put("message", "借条状态已更新，请确认后再操作！");
			return resultMap;
		}
		if(!NfsLoanRecord.Status.overdue.equals(loanStatus)) {
			resultMap.put("success", "false");
			resultMap.put("message", "该借条状态已更新，请确认后再操作！");
			return resultMap;
		}
		NfsLoanRecord.DelayStatus loanDelayStatus = loanRecord.getDelayStatus();
		if(!NfsLoanRecord.DelayStatus.initial.equals(loanDelayStatus)) {
			resultMap.put("success", "false");
			resultMap.put("message", "该借条处于延期待确认状态，不能转让！");
			return resultMap;
		}
		NfsLoanRecord.PartialStatus loanPartialStatus = loanRecord.getPartialStatus();
		if(!NfsLoanRecord.PartialStatus.initial.equals(loanPartialStatus)) {
			resultMap.put("success", "false");
			resultMap.put("message", "该借条处于部分还款待确认状态，不能转让！");
			return resultMap;
		}
		NfsLoanRecord.LineDownStatus loanLineDownStatus = loanRecord.getLineDownStatus();
		if(NfsLoanRecord.LineDownStatus.loaneeLineDownRepayment.equals(loanLineDownStatus)) {
			resultMap.put("success", "false");
			resultMap.put("message", "该借条处于线下还款待确认状态，不能转让！");
			return resultMap;
		}

		//仲裁、催收状态校验
		if(!loanRecord.getArbitrationStatus().equals(NfsLoanRecord.ArbitrationStatus.initial)) {
			resultMap.put("success", "false");
			resultMap.put("message", "该借条已经申请仲裁，不能转让！");
			return resultMap;
		}
		//只要申请过催收就不能进行转让
		if(!loanRecord.getCollectionStatus().equals(NfsLoanRecord.CollectionStatus.initial)) {
			resultMap.put("success", "false");
			resultMap.put("message", "该借条已经申请催收，不能转让！");
			return resultMap;
		}
		return resultMap;
	}

	/**
	 * 	获取可购入借条列表
	 */
	@Override
	public ResponseData findCrAuctionPage(Member member, LoanCrAuctionListRequestParam reqData,
			LoanCrAuctionListResponseResult result) {
		String cityId = reqData.getCityId();
		Area area = new Area();
		area.setId(Long.parseLong(cityId));
		Integer pageNo = reqData.getPageNo();
		Integer pageSize = reqData.getPageSize();
		Page<NfsCrAuction> page = new Page<NfsCrAuction>(pageNo == null?1:pageNo, pageSize == null?10:pageSize);
		
		NfsLoanRecord loanRecord = new NfsLoanRecord();
		loanRecord.setLoanee(member);
		
		NfsCrAuction nfsCrAuction = new NfsCrAuction();
		nfsCrAuction.setPage(page);
		nfsCrAuction.setStatus(NfsCrAuction.Status.forsale);
		nfsCrAuction.setArea(area);
		nfsCrAuction.setCrSeller(member);
		nfsCrAuction.setLoanRecord(loanRecord);
		nfsCrAuction.setIsPub(true);
		nfsCrAuction.setOverdueTab(reqData.getOverdueTab());
		nfsCrAuction.setPriceTab(reqData.getPriceTab());
		List<NfsCrAuction> list = crAuctionDao.findCrAuctionList(nfsCrAuction);
		for (NfsCrAuction auction : list) {
			NfsLoanRecord record = auction.getLoanRecord();
			int overdueDays = CalendarUtil.getIntervalDays(new Date(),record.getDueRepayDate());//过去的天数
			BigDecimal overdueInterest = LoanUtils.calOverdueInterest(record.getAmount(), new BigDecimal(overdueDays));
			BigDecimal dueRepayAmount =  record.getDueRepayAmount().add(overdueInterest);
			
			CrAuction crAuction = new LoanCrAuctionListResponseResult().new CrAuction();
			crAuction.setLoanId(record.getId().toString());
			crAuction.setAuctionId(auction.getId().toString());
			crAuction.setDueRepayAmount(StringUtils.decimalToStr(dueRepayAmount, 2));
			crAuction.setDueRepayDate(DateUtils.formatDate(record.getDueRepayDate(), "yyyy-MM-dd"));
			crAuction.setOverDueDays(overdueDays);
			crAuction.setPrice(StringUtils.decimalToStr(auction.getCrSellPrice(), 2));
			crAuction.setCityId(auction.getArea().getId().toString());
			crAuction.setCityName(auction.getArea().getName());
			result.getCrAuctionList().add(crAuction);
		}
		return ResponseData.success("查询拍卖中可购入列表成功",result);
	}
	/**
	 * 	我买入的借条列表
	 */
	@Override
	public ResponseData findPurchaseList(Member member, MinePurchaseListRequestParam reqData,
			LoanCrAuctionListResponseResult result) {
		Integer pageNo = reqData.getPageNo();
		Integer pageSize = reqData.getPageSize();
		Page<NfsCrAuction> page = new Page<NfsCrAuction>(pageNo == null?1:pageNo, pageSize == null?10:pageSize);
		
		NfsCrAuction nfsCrAuction = new NfsCrAuction();
		nfsCrAuction.setPage(page);
		nfsCrAuction.setCrBuyer(member);
		nfsCrAuction.setUnPayTab(reqData.getUnPayTab());
		nfsCrAuction.setPayedTab(reqData.getPayedTab());
		List<NfsCrAuction> list = crAuctionDao.findList(nfsCrAuction);
		for (NfsCrAuction auction : list) {
			NfsLoanRecord record = auction.getLoanRecord();
			Member loanee = memberService.get(record.getLoanee().getId());
			int overdueDays = 0;
			
			CrAuction crAuction = new LoanCrAuctionListResponseResult().new CrAuction();
			crAuction.setLoanId(record.getId().toString());
			crAuction.setAuctionId(auction.getId().toString());
			crAuction.setPrice(StringUtils.decimalToStr(auction.getCrBuyPrice(), 2));
			crAuction.setAmount(StringUtils.decimalToStr(record.getAmount(), 2));
			crAuction.setInterest(StringUtils.decimalToStr(record.getInterest(), 2));
			crAuction.setRealName(record.getLoaneeName());
			crAuction.setIdNo(record.getLoanee().getIdNo());
			crAuction.setUsername(record.getLoaneePhoneNo());
			crAuction.setEmail(record.getLoanee().getEmail());
			crAuction.setAddr(loanee.getAddr());
			crAuction.setDueRepayDate(DateUtils.formatDate(record.getDueRepayDate(), "yyyy-MM-dd"));
			if(record.getStatus() == NfsLoanRecord.Status.repayed) {
				overdueDays = CalendarUtil.getIntervalDays(record.getCompleteDate(),record.getDueRepayDate());
				crAuction.setCompleteDate(DateUtils.formatDate(record.getCompleteDate(), "yyyy-MM-dd"));
			}else {
				overdueDays = CalendarUtil.getIntervalDays(new Date(),record.getDueRepayDate());//过去的天数
				crAuction.setCompleteDate("");
			}
			BigDecimal overdueInterest = LoanUtils.calOverdueInterest(record.getAmount(), new BigDecimal(overdueDays));
			BigDecimal dueRepayAmount =  record.getAmount().add(record.getInterest()).add(overdueInterest);
			crAuction.setOverDueDays(overdueDays);
			crAuction.setOverdueInterest(StringUtils.decimalToStr(overdueInterest, 2));
			crAuction.setDueRepayAmount(StringUtils.decimalToStr(dueRepayAmount, 2));
			crAuction.setCityId(auction.getArea().getId().toString());
			crAuction.setCityName(auction.getArea().getName());
			crAuction.setIsRepay(record.getStatus() == NfsLoanRecord.Status.repayed?1:0);
			result.getCrAuctionList().add(crAuction);
		}
		return ResponseData.success("查询拍卖中我买入的借条列表成功", result);
	}
	/**
	 * 我转让的列表
	 */
	@Override
	public ResponseData findAuctionList(Member member, MineTransferListRequestParam reqData,
			LoanCrAuctionListResponseResult result) {
		Integer pageNo = reqData.getPageNo();
		Integer pageSize = reqData.getPageSize();
		Page<NfsCrAuction> page = new Page<NfsCrAuction>(pageNo == null?1:pageNo, pageSize == null?10:pageSize);
		
		NfsCrAuction nfsCrAuction = new NfsCrAuction();
		nfsCrAuction.setPage(page);
		nfsCrAuction.setCrSeller(member);
		nfsCrAuction.setAuctionTab(reqData.getAuctionTab());
		nfsCrAuction.setAuctionedTab(reqData.getAuctionedTab());
		List<NfsCrAuction> list = crAuctionDao.findList(nfsCrAuction);
		for (NfsCrAuction auction : list) {
			NfsLoanRecord loanRecord = auction.getLoanRecord();
			int overdueDays = 0;
			
			CrAuction crAuction = new LoanCrAuctionListResponseResult().new CrAuction();
			crAuction.setLoanId(loanRecord.getId().toString());
			crAuction.setAuctionId(auction.getId().toString());
			crAuction.setPrice(StringUtils.decimalToStr(auction.getCrSellPrice(), 2));
			crAuction.setAmount(StringUtils.decimalToStr(loanRecord.getAmount(), 2));
			crAuction.setInterest(StringUtils.decimalToStr(loanRecord.getInterest(), 2));
			crAuction.setRealName(loanRecord.getLoaneeName());
			crAuction.setPartnerHeadImage(loanRecord.getLoanee().getHeadImage());
			crAuction.setDueRepayDate(DateUtils.formatDate(loanRecord.getDueRepayDate(), "yyyy-MM-dd"));

			crAuction.setApplyTime(DateUtils.formatDate(auction.getCreateTime(), "yyyy-MM-dd"));
			if(auction.getStatus().equals(NfsCrAuction.Status.unsale)) {
				crAuction.setReleaseTime(DateUtils.formatDate(auction.getUpdateTime(), "yyyy-MM-dd"));
			}
			
			if(auction.getStatus().equals(NfsCrAuction.Status.successed)) {
				crAuction.setSuccessedTime(DateUtils.formatDate(auction.getUpdateTime(), "yyyy-MM-dd"));
			}
			
			crAuction.setAuctionStatus(auction.getStatus().ordinal());
			
			if(loanRecord.getStatus() == NfsLoanRecord.Status.repayed) {
				overdueDays = CalendarUtil.getIntervalDays(loanRecord.getCompleteDate(),loanRecord.getDueRepayDate());
			}else {
				overdueDays = CalendarUtil.getIntervalDays(new Date(),loanRecord.getDueRepayDate());//过去的天数
			}
			BigDecimal overdueInterest = LoanUtils.calOverdueInterest(loanRecord.getAmount(), new BigDecimal(overdueDays));
			BigDecimal dueRepayAmount =  loanRecord.getAmount().add(loanRecord.getInterest()).add(overdueInterest);
			crAuction.setOverDueDays(overdueDays);
			crAuction.setOverdueStr("已逾期"+overdueDays+"日");
			crAuction.setDueRepayAmount(StringUtils.decimalToStr(dueRepayAmount, 2));
			crAuction.setOverdueInterest(StringUtils.decimalToStr(overdueInterest, 2));
			crAuction.setRmk(auction.getRmk());
			result.getCrAuctionList().add(crAuction);
		}
		return ResponseData.success("查询拍卖中我转让的借条列表成功", result);
		
	}

	/**
	 * 取消转让
	 */
	@Override
	@Transactional(readOnly = false)
	public ResponseData releaseAuction(Member member, PayAuctionRequestParam reqData,
			ReleaseAuctionResponseResult result) {
		
		String auctionId = reqData.getAuctionId();
		NfsCrAuction auction = crAuctionDao.get(Long.parseLong(auctionId));
		if(auction.getStatus() != NfsCrAuction.Status.forsale&&auction.getStatus() != NfsCrAuction.Status.audit) {
			return ResponseData.error("借条状态已更新,取消失败");
		}
		auction.setStatus(NfsCrAuction.Status.unsale);
		auction.setUpdateTime(new Date());
		auction.setIsPub(false);
		auction.setRmk("取消转让");
		save(auction);
		
		NfsLoanRecord record = loanRecordService.get(auction.getLoanRecord());
		record.setAuctionStatus(NfsLoanRecord.AuctionStatus.initial);
		loanRecordService.save(record);
		
		result = getCrAuctionDetail(auctionId);
		return ResponseData.success("取消转让成功", result);
	}
	/**
	 * 转让详情
	 */
	@Override
	public ReleaseAuctionResponseResult getCrAuctionDetail(String auctionId) {
		ReleaseAuctionResponseResult result = new ReleaseAuctionResponseResult();
		NfsCrAuction auction = crAuctionDao.get(Long.parseLong(auctionId));
		NfsLoanRecord loanRecord = auction.getLoanRecord();
		int overdueDays = 0;
		
		result.setAuctionId(auctionId);
		result.setRealName(loanRecord.getLoaneeName());
		result.setPartnerHeadImage(loanRecord.getLoanee().getHeadImage());
		result.setAmount(StringUtils.decimalToStr(loanRecord.getAmount(), 2));
		result.setInterest(StringUtils.decimalToStr(loanRecord.getInterest(), 2));
		result.setDueRepayDate(DateUtils.formatDate(loanRecord.getDueRepayDate(), "yyyy-MM-dd"));
		result.setPrice(StringUtils.decimalToStr(auction.getCrSellPrice(), 2));
		result.setApplyTime(DateUtils.formatDate(auction.getCreateTime(), "yyyy-MM-dd"));	
		if(auction.getStatus().equals(NfsCrAuction.Status.unsale)) {
		    result.setReleaseTime(DateUtils.formatDate(auction.getUpdateTime(), "yyyy-MM-dd"));
		}
		if(auction.getStatus().equals(NfsCrAuction.Status.successed)) {
			result.setSuccessedTime(DateUtils.formatDate(auction.getUpdateTime(), "yyyy-MM-dd"));
		}
		
		result.setAuctionStatus(auction.getStatus().ordinal());
		
		if(loanRecord.getStatus() == NfsLoanRecord.Status.repayed) {
			overdueDays = CalendarUtil.getIntervalDays(loanRecord.getCompleteDate(),loanRecord.getDueRepayDate());
		}else {
			overdueDays = CalendarUtil.getIntervalDays(new Date(),loanRecord.getDueRepayDate());//过去的天数
		}
		BigDecimal overdueInterest = LoanUtils.calOverdueInterest(loanRecord.getAmount(), new BigDecimal(overdueDays));
		result.setOverdueStr("已逾期"+overdueDays+"日");
		result.setOverdueInterest(StringUtils.decimalToStr(overdueInterest, 2));
		result.setRmk(auction.getRmk());
		return result;
	}

	/**
	 * 	买入详情
	 */
	@Override
	public PurchaseDetailResponseResult getCrPurchaseDetail(String auctionId) {
		PurchaseDetailResponseResult result = new PurchaseDetailResponseResult();
		NfsCrAuction auction = crAuctionDao.get(Long.parseLong(auctionId));
		NfsLoanRecord loanRecord = auction.getLoanRecord();
		Member loanee = memberService.get(loanRecord.getLoanee());
		int overdueDays = 0;
		
		result.setAuctionId(auctionId);
		result.setLoanId(loanRecord.getId().toString());
		result.setPrice(StringUtils.decimalToStr(auction.getCrSellPrice(), 2));
		result.setAmount(StringUtils.decimalToStr(loanRecord.getAmount(), 2));
		result.setInterest(StringUtils.decimalToStr(loanRecord.getInterest(), 2));
		result.setRealName(loanRecord.getLoaneeName());
		result.setIdNo(loanRecord.getLoanee().getIdNo());
		result.setUsername(loanRecord.getLoaneePhoneNo());
		result.setEmail(loanRecord.getLoanee().getEmail());
		result.setAddr(loanee.getAddr());
		result.setDueRepayDate(DateUtils.formatDate(loanRecord.getDueRepayDate(), "yyyy-MM-dd"));
		if(loanRecord.getStatus() == NfsLoanRecord.Status.repayed) {
			overdueDays = CalendarUtil.getIntervalDays(loanRecord.getCompleteDate(),loanRecord.getDueRepayDate());
			result.setCompleteDate(DateUtils.formatDate(loanRecord.getCompleteDate(), "yyyy-MM-dd"));
		}else {
			overdueDays = CalendarUtil.getIntervalDays(new Date(),loanRecord.getDueRepayDate());//过去的天数
			result.setCompleteDate("");
		}
		BigDecimal overdueInterest = LoanUtils.calOverdueInterest(loanRecord.getAmount(), new BigDecimal(overdueDays));
		BigDecimal dueRepayAmount =  loanRecord.getAmount().add(loanRecord.getInterest()).add(overdueInterest);
		result.setOverDueDays(overdueDays);
		result.setDueRepayAmount(StringUtils.decimalToStr(dueRepayAmount, 2));
		result.setOverdueInterest(StringUtils.decimalToStr(overdueInterest, 2));
		result.setIsRepay(loanRecord.getStatus() == NfsLoanRecord.Status.repayed?1:0);
		return result;
	}
	
	@Override
	@Transactional(readOnly = false)
	public void sendMessageAfterPaySucceed(NfsCrAuction crAuction,NfsLoanRecord loanRecord) {
		// TODO 先在这里写一个异步发送的，后面再做统一的接口处理
		Member buyer = crAuction.getCrBuyer();
		Member seller = crAuction.getCrSeller();  
		sendMessageTask(buyer, seller, crAuction, loanRecord);
	}

	private void sendMessageTask( Member buyer,Member seller,  NfsCrAuction crAuction,NfsLoanRecord loanRecord) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				//发送会员消息
				MemberMessage loanerMessage = memberMessageService.sendMessage(MemberMessage.Type.successedAuctionImsLoaner,crAuction.getId());
				
				Map<String,Object> loanerMap = new HashMap<String,Object>();
				loanerMap.put("money", StringUtils.decimalToStr(crAuction.getCrSellPrice(),2));
				sendSmsMsgServiceImpl.sendCollectionSms("successedAuctionImsLoaner", loanRecord.getLoaner().getUsername(), loanerMap);
				sendMsgServiceImpl.beforeSendAppMsg(loanerMessage);
				
				//发送会员消息
				MemberMessage loaneeMessage = memberMessageService.sendMessage(MemberMessage.Type.successedAuctionImsLoanee,loanRecord.getId());
				
				Map<String,Object> loaneeMap = new HashMap<String,Object>();
				loaneeMap.put("money", StringUtils.decimalToStr(loanRecord.getAmount(),2));
				loaneeMap.put("name",seller.getName());
				loaneeMap.put("newLoaner",buyer.getName());
				sendSmsMsgServiceImpl.sendCollectionSms("successedAuctionImsLoanee", loanRecord.getLoaneePhoneNo(), loaneeMap);
				sendMsgServiceImpl.beforeSendAppMsg(loaneeMessage);
				
				//转让成功发气泡
				Map<String, String> dialogueMap = new HashMap<String, String>();
				dialogueMap.put(LoanConstant.CREDITOR, buyer.getName());//新债权人姓名
				dialogueMap.put(LoanConstant.PHONE_NO, buyer.getUsername());//新债权人电话
				dialogueMap.put(LoanConstant.ID_NO, MemUtils.maskIdNo(buyer).getIdNo());//新债权人身份证
				dialogueMap.put(LoanConstant.TIME, DateUtils.formatDate(new Date(), "yyyy-MM-dd"));//转让时间
				
				NfsLoanDetailMessage loanDetailMessage = new NfsLoanDetailMessage();
				loanDetailMessage.setDetail(loanRecord.getLoanApplyDetail());
				loanDetailMessage.setMember(loanRecord.getLoaner());
				loanDetailMessage.setNote(JSON.toJSONString(dialogueMap));
				loanDetailMessage.setMessageId(RecordMessage.CHAT_10);
				loanDetailMessage.setType(RecordMessage.LENDER_PAID_REPAYMENT);
				loanDetailMessageService.save(loanDetailMessage);
				
			}
		});
	}
	/**
	 * 	获取待审核列表
	 */
	@Override
	public Page<NfsCrAuction> findAuditPage(Page<NfsCrAuction> page, NfsCrAuction nfsCrAuction) {
		
		nfsCrAuction.setPage(page);
		nfsCrAuction.setStatus(NfsCrAuction.Status.audit);
		List<NfsCrAuction> crAuctionList = crAuctionDao.findAuditList(nfsCrAuction);
		page.setList(crAuctionList);
		return page;
	}
	
	/**
	 * 	获取已审核列表
	 */
	@Override
	public Page<NfsCrAuction> findAuditedPage(Page<NfsCrAuction> page, NfsCrAuction nfsCrAuction) {
		nfsCrAuction.setPage(page);
		List<NfsCrAuction> crAuctionList = crAuctionDao.findAuditedList(nfsCrAuction);
		page.setList(crAuctionList);
		return page;
	}

	@Override
	public List<NfsCrAuction> findOvertimeAuctionList() {
	
		return crAuctionDao.findOvertimeAuctionList();
	}

	@Override
	public List<NfsCrAuction> findOverOneDayList() {
		
		return crAuctionDao.findOverOneDayList();
	}

	@Override
	public Member getCrBuyer(NfsLoanRecord loanRecord) {
		NfsCrAuction crAuction = new NfsCrAuction();
		crAuction.setLoanRecord(loanRecord);
		crAuction = crAuctionDao.getCrBuyerByLoan(crAuction);
		return crAuction!=null?crAuction.getCrBuyer():null;
	}

	@Override
	public NfsCrAuction getSuccessedAuctionByLoan(NfsLoanRecord record) {
		NfsCrAuction crAuction = new NfsCrAuction();
		crAuction.setLoanRecord(record);
		return crAuctionDao.getCrBuyerByLoan(crAuction);
	}
}