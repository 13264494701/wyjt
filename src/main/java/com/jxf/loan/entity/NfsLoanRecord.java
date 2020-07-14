package com.jxf.loan.entity;

import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.jxf.loan.entity.NfsLoanApply.Channel;
import com.jxf.loan.entity.NfsLoanApply.LoanPurp;
import com.jxf.loan.entity.NfsLoanApply.LoanType;
import com.jxf.loan.entity.NfsLoanApply.RepayType;
import com.jxf.loan.entity.NfsLoanApply.TrxType;
import com.jxf.loan.entity.NfsLoanApplyDetail.DisputeResolution;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.svc.annotation.ExcelField;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 借条记录Entity
 * @author wo
 * @version 2018-10-10
 */
public class NfsLoanRecord extends CrudEntity<NfsLoanRecord> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 借条状态
	 */
	public enum Status {

	    /** 待还*/
		penddingRepay,
		
		/** 已还*/
		repayed,
		
		/** 逾期*/
		overdue
		
	}
	
	/**
	 *	拍卖状态
	 */
	public enum AuctionStatus {
		/** 初始状态 */
		initial,
		
		/** 转让中*/
		auction,
		
		/** 转让成功*/
		auctioned
	}
	
	/**
	 * 部分还款状态
	 */
	public enum PartialStatus {
		/** 初始状态 */
		initial,
		
		/** 借款人部分还款待确认*/
		loaneeApplyPartial,
		
		/** 放款人要求部分还款待确认*/
		loanerApplyPartial
	}
	/**
	 * 延期状态
	 */
	public enum DelayStatus {
		
		/** 初始状态 */
		initial,
		
		/** 借款人申请延期待确认*/
		loaneeApplyDelay,
		
		/** 放款人申请延期待确认*/
		loanerApplyDelay
		
	}
	/**
	 * 线下还款状态
	 */
	public enum LineDownStatus {
		/** 初始状态 */
		initial,
		
		/** 借款人发起线下还款待确认*/
		loaneeLineDownRepayment,
		
		/** 已完成线下还款*/
		completed
		
	}
	
	/**
	 * 仲裁状态
	 */
	public enum ArbitrationStatus{
		/** 未仲裁 */
		initial,
		/** 仲裁中 */
		doing,
		/** 仲裁结束 */
		end
	}
	
	/**
	 * 催收状态
	 */
	public enum CollectionStatus{
		/** 未催收 */
		initial,
		/** 催收中 */
		doing,
		/** 催收结束 */
		end
	}
	/**
	 * 删除状态
	 */
	public enum DeleteStatus{
		/** 未删除 */
		no,
		/** 已删除 */
		yes
	}
	
	/** 借条编号 */
	private String loanNo;		
	
	/** 放款人 */
	private Member loaner;		
	
	/** 借款人 */
	private Member loanee;		
	
	/** 借款类型*/
	private LoanType loanType;
	
	/** 交易类型*/
	private TrxType trxType;
	
	/** 借款用途 */
	private LoanPurp loanPurp;	
	
	/** 还款方式 */
	private RepayType repayType;	

	/** 借款金额 */
	private BigDecimal amount;	
	
	/** 借款利率 */
	private BigDecimal intRate;	
	
	/** 利息 */
	private BigDecimal interest;	
	
	/** 手续费 */
	private BigDecimal fee;
	
	/** 借条开始时间 */
	private Date loanStart;	

	/** 借款期限 */
	private Integer term;	
	
	/** 已还期数 */
	private Integer repayedTerm;		
	/** 未还期数 */
	private Integer dueRepayTerm;	
	
	/** 本期到期日 */
	private Date dueRepayDate;		
	/** 应还金额/分期借条剩余应还总额 */
	private BigDecimal dueRepayAmount;	
	
	/** 结清日期 */
	private Date completeDate;		
	
	/** 逾期利息 */
	private BigDecimal overdueInterest;
	
	/** 借条状态 */
	private Status status;		

	/** 部分还款状态 */
	private PartialStatus partialStatus;
	/** 延期状态 */
	private DelayStatus delayStatus;
	/** 拍卖状态*/
	private AuctionStatus auctionStatus;
	/** 线下还款状态 */
	private LineDownStatus lineDownStatus;
	
	/** 借款人删除 */
	private Boolean loaneeDelete;
	
	/** 放款人删除 */
	private Boolean loanerDelete;
	
	/** 催收次数 */
	private Integer collectionTimes;
	
	/** 仲裁状态 */
	private ArbitrationStatus arbitrationStatus;
	
	/** 催收状态 */
	private CollectionStatus collectionStatus;
	
	/** 当前催收 */
	private NfsLoanCollection currentCollection;
	
	/** 当前仲裁 */
	private NfsLoanArbitration currentArbitration;
	
	/** 借条进度*/
	private String progress;
	
	/** 申请渠道 */
	private Channel channel;	
	/** 申请详情 */
	private NfsLoanApplyDetail loanApplyDetail;		
	

	/** 当前时间 */
	private Date nowDate;
	
	/** 是否要求录制视频 */
	private Integer requireAliveVideo;//0->否，1->是		
	/** 争议解决方式 */
	private DisputeResolution disputeResolution;//0->仲裁,1->诉讼
	
	/** 还款计划 */
    private List<NfsLoanRepayRecord> repayRecordList = new ArrayList<NfsLoanRepayRecord>();
    /** 借条资金流水 */
    private List<MemberActTrx> loanTrxList = new ArrayList<MemberActTrx>();
    /** 操作记录 */
    private List<NfsLoanOperatingRecord> loanOperatingRecordList = new ArrayList<NfsLoanOperatingRecord>();
	
    /** 列表查询条件 开始 */
	/** 开始时间 */
	private Date beginTime;	
	/** 结束时间*/
	private Date endTime;	
	
	/** 开始时间 */
	private Date beginUpdateTime;	
	/** 结束时间*/
	private Date endUpdateTime;	
	
	/** 最大金额 */
	private BigDecimal maxAmount;	
	
	/** 最小金额 */
	private BigDecimal minAmount;
	
	/** 开始到期日期 */
	private Date beginDueRepayDate;	
	/** 结束到期日期*/
	private Date endDueRepayDate;	
	
	/** 开始结清时间 */
	private Date beginCompleteDate;	
	/** 结束结清时间*/
	private Date endCompleteDate;	
	
	/** 最大期限*/
	private Integer maxTerm;
	/** 最小期限*/
	private Integer minTerm;
	
	private Integer overdueTab;//逾期天数 排序专用 默认0 不排序  1 正序 2 倒叙
	
	private Integer amountTab;//借款金额 排序专用 默认0 不排序  1 正序 2 倒叙
	
	/** 列表查询条件 结束 */
	
	/**合同地址*/
	private String contractUrl;
	
	public String getContractUrl() {
		return contractUrl;
	}

	public void setContractUrl(String contractUrl) {
		this.contractUrl = contractUrl;
	}

	public NfsLoanRecord() {
		super();
	}

	public NfsLoanRecord(Long id){
		super(id);
	}

	@Length(min=1, max=16, message="借条编号长度必须介于 1 和 16 之间")
	public String getLoanNo() {
		return loanNo;
	}

	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}
	
	public Member getLoaner() {
		return loaner;
	}

	public void setLoaner(Member loaner) {
		this.loaner = loaner;
	}

	public Member getLoanee() {
		return loanee;
	}

	public void setLoanee(Member loanee) {
		this.loanee = loanee;
	}
	

	public LoanType getLoanType() {
		return loanType;
	}

	public void setLoanType(LoanType loanType) {
		this.loanType = loanType;
	}

	public LoanPurp getLoanPurp() {
		return loanPurp;
	}

	public void setLoanPurp(LoanPurp loanPurp) {
		this.loanPurp = loanPurp;
	}

	public RepayType getRepayType() {
		return repayType;
	}

	public void setRepayType(RepayType repayType) {
		this.repayType = repayType;
	}
	
	@ExcelField(title="借款金额", align=2, sort=50)
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public BigDecimal getIntRate() {
		return intRate;
	}

	public void setIntRate(BigDecimal intRate) {
		this.intRate = intRate;
	}
	
	public Integer getTerm() {
		return term;
	}

	public void setTerm(Integer term) {
		this.term = term;
	}
	

	public Integer getRepayedTerm() {
		return repayedTerm;
	}

	public void setRepayedTerm(Integer repayedTerm) {
		this.repayedTerm = repayedTerm;
	}
	

	public Integer getDueRepayTerm() {
		return dueRepayTerm;
	}

	public void setDueRepayTerm(Integer dueRepayTerm) {
		this.dueRepayTerm = dueRepayTerm;
	}
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getDueRepayDate() {
		return dueRepayDate;
	}

	public void setDueRepayDate(Date dueRepayDate) {
		this.dueRepayDate = dueRepayDate;
	}
	
	public BigDecimal getDueRepayAmount() {
		return dueRepayAmount;
	}

	public void setDueRepayAmount(BigDecimal dueRepayAmount) {
		this.dueRepayAmount = dueRepayAmount;
	}
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="还款时间", align=2, sort=90)
	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}
	
	@ExcelField(title="还款状态",  dictType="loanStatus", align=2, sort=100)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}


	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public Date getNowDate() {
		return nowDate;
	}

	public void setNowDate(Date nowDate) {
		this.nowDate = nowDate;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}


	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public Integer getCollectionTimes() {
		return collectionTimes;
	}

	public void setCollectionTimes(Integer collectionTimes) {
		this.collectionTimes = collectionTimes;
	}

	public List<NfsLoanRepayRecord> getRepayRecordList() {
		return repayRecordList;
	}

	public void setRepayRecordList(List<NfsLoanRepayRecord> repayRecordList) {
		this.repayRecordList = repayRecordList;
	}


	public Integer getRequireAliveVideo() {
		return requireAliveVideo;
	}

	public void setRequireAliveVideo(Integer requireAliveVideo) {
		this.requireAliveVideo = requireAliveVideo;
	}

	public DisputeResolution getDisputeResolution() {
		return disputeResolution;
	}

	public void setDisputeResolution(DisputeResolution disputeResolution) {
		this.disputeResolution = disputeResolution;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public BigDecimal getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}

	public BigDecimal getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}

	public NfsLoanApplyDetail getLoanApplyDetail() {
		return loanApplyDetail;
	}

	public void setLoanApplyDetail(NfsLoanApplyDetail loanApplyDetail) {
		this.loanApplyDetail = loanApplyDetail;
	}

	public ArbitrationStatus getArbitrationStatus() {
		return arbitrationStatus;
	}

	public void setArbitrationStatus(ArbitrationStatus arbitrationStatus) {
		this.arbitrationStatus = arbitrationStatus;
	}

	public CollectionStatus getCollectionStatus() {
		return collectionStatus;
	}

	public void setCollectionStatus(CollectionStatus collectionStatus) {
		this.collectionStatus = collectionStatus;
	}

	public PartialStatus getPartialStatus() {
		return partialStatus;
	}

	public void setPartialStatus(PartialStatus partialStatus) {
		this.partialStatus = partialStatus;
	}

	public DelayStatus getDelayStatus() {
		return delayStatus;
	}

	public void setDelayStatus(DelayStatus delayStatus) {
		this.delayStatus = delayStatus;
	}

	public LineDownStatus getLineDownStatus() {
		return lineDownStatus;
	}

	public void setLineDownStatus(LineDownStatus lineDownStatus) {
		this.lineDownStatus = lineDownStatus;
	}

	public BigDecimal getOverdueInterest() {
		return overdueInterest;
	}

	public void setOverdueInterest(BigDecimal overdueInterest) {
		this.overdueInterest = overdueInterest;
	}

	public NfsLoanCollection getCurrentCollection() {
		return currentCollection;
	}

	public void setCurrentCollection(NfsLoanCollection currentCollection) {
		this.currentCollection = currentCollection;
	}

	public NfsLoanArbitration getCurrentArbitration() {
		return currentArbitration;
	}

	public void setCurrentArbitration(NfsLoanArbitration currentArbitration) {
		this.currentArbitration = currentArbitration;
	}

	public Date getBeginCompleteDate() {
		return beginCompleteDate;
	}

	public void setBeginCompleteDate(Date beginCompleteDate) {
		this.beginCompleteDate = beginCompleteDate;
	}

	public Date getEndCompleteDate() {
		return endCompleteDate;
	}

	public void setEndCompleteDate(Date endCompleteDate) {
		this.endCompleteDate = endCompleteDate;
	}

	public Integer getMaxTerm() {
		return maxTerm;
	}

	public void setMaxTerm(Integer maxTerm) {
		this.maxTerm = maxTerm;
	}

	public Integer getMinTerm() {
		return minTerm;
	}

	public void setMinTerm(Integer minTerm) {
		this.minTerm = minTerm;
	}

	@ExcelField(title="借款人姓名", align=2, sort=10)
	public String getLoaneeName() {
		return getLoanee().getName();
	}
	
	@ExcelField(title="借款人手机号码", align=2, sort=20)
	public String getLoaneePhoneNo() {
		return getLoanee().getUsername();
	}
	@ExcelField(title="借款天数", align=2, sort=60)
	public String getLoanTerm() {
		return getTerm().toString();
	}

	public Date getBeginDueRepayDate() {
		return beginDueRepayDate;
	}

	public void setBeginDueRepayDate(Date beginDueRepayDate) {
		this.beginDueRepayDate = beginDueRepayDate;
	}

	public Date getEndDueRepayDate() {
		return endDueRepayDate;
	}

	public void setEndDueRepayDate(Date endDueRepayDate) {
		this.endDueRepayDate = endDueRepayDate;
	}

	public AuctionStatus getAuctionStatus() {
		return auctionStatus;
	}

	public void setAuctionStatus(AuctionStatus auctionStatus) {
		this.auctionStatus = auctionStatus;
	}

	public Integer getOverdueTab() {
		return overdueTab;
	}

	public void setOverdueTab(Integer overdueTab) {
		this.overdueTab = overdueTab;
	}

	public Integer getAmountTab() {
		return amountTab;
	}

	public void setAmountTab(Integer amountTab) {
		this.amountTab = amountTab;
	}

	public List<MemberActTrx> getLoanTrxList() {
		return loanTrxList;
	}

	public void setLoanTrxList(List<MemberActTrx> loanTrxList) {
		this.loanTrxList = loanTrxList;
	}

	public List<NfsLoanOperatingRecord> getLoanOperatingRecordList() {
		return loanOperatingRecordList;
	}

	public void setLoanOperatingRecordList(
			List<NfsLoanOperatingRecord> loanOperatingRecordList) {
		this.loanOperatingRecordList = loanOperatingRecordList;
	}

	public TrxType getTrxType() {
		return trxType;
	}

	public void setTrxType(TrxType trxType) {
		this.trxType = trxType;
	}

	public Date getLoanStart() {
		return loanStart;
	}

	public void setLoanStart(Date loanStart) {
		this.loanStart = loanStart;
	}

	public Boolean getLoaneeDelete() {
		return loaneeDelete;
	}

	public void setLoaneeDelete(Boolean loaneeDelete) {
		this.loaneeDelete = loaneeDelete;
	}

	public Boolean getLoanerDelete() {
		return loanerDelete;
	}

	public void setLoanerDelete(Boolean loanerDelete) {
		this.loanerDelete = loanerDelete;
	}

	public Date getBeginUpdateTime() {
		return beginUpdateTime;
	}

	public void setBeginUpdateTime(Date beginUpdateTime) {
		this.beginUpdateTime = beginUpdateTime;
	}

	public Date getEndUpdateTime() {
		return endUpdateTime;
	}

	public void setEndUpdateTime(Date endUpdateTime) {
		this.endUpdateTime = endUpdateTime;
	}



}