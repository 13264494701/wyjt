package com.jxf.loan.entity;

import javax.validation.constraints.NotNull;

import com.jxf.mem.entity.Member;
import com.jxf.svc.sys.area.entity.Area;

import java.math.BigDecimal;
import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 债权买卖Entity
 * @author wo
 * @version 2018-12-25
 */
public class NfsCrAuction extends CrudEntity<NfsCrAuction> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 拍卖状态
	 */
	public enum Status {

	    /** 挂牌-0*/
		forsale,
		
		/** 审核中-1*/
		audit,
		
		/** 审核失败-2*/
		auditFailed,
		
	    /** 成交-3*/
		successed,
		
	    /** 撤销-4*/
		unsale,
		
		/** 流拍-5*/
		failed

	}
	
	/**
	 * 证据状态
	 */
	public enum ProofStatus {

	    /** 待申请-0*/
		pendingApply,
		
		/** 待生成-1*/
		pendingCreate,
		
		/** 已生成-2*/
		created,
	

	}
	
	/** 债权编号 */
	private NfsLoanRecord loanRecord;		
	/** 债权卖方 */
	private Member crSeller;		
	/** 债权挂牌价格 */
	private BigDecimal crSellPrice;		
	/** 债权买方 */
	private Member crBuyer;		
	/** 债权举牌价格 */
	private BigDecimal crBuyPrice;		
	/** 所属地区 */
	private Area area;		
	/** 拍卖状态 */
	private Status status;	
	/** 是否发布 */
	private Boolean isPub;
	/** 客服审核意见*/
	private String auditOpinion;
	
	private String zipPath;
	/** 证据状态*/
	private ProofStatus proofStatus;
	
	
	private Integer overdueTab;//逾期天数 排序专用 默认0 不排序  1 正序 2 倒叙
	
	private Integer priceTab;//转让金额 排序专用 默认0 不排序  1 正序 2 倒叙
	
	private Integer unPayTab;//未还款 排序专用 默认0 不排序  1 正序 2 倒叙
	
	private Integer payedTab;//已还款 排序专用 默认0 不排序  1 正序 2 倒叙
	
	private Integer auctionTab;//转让中 排序专用 默认0 不排序  1 正序 2 倒叙
	
	private Integer auctionedTab;//转让完成 排序专用 默认0 不排序  1 正序 2 倒叙
	
	/** 列表查询条件 开始 */
	/** 开始时间 */
	private Date beginTime;	
	/** 结束时间*/
	private Date endTime;
	
	/** 最大利息*/
	private BigDecimal maxInRate;
	/** 最小利息*/
	private BigDecimal minInRate;
	
	public NfsCrAuction() {
		super();
	}

	public NfsCrAuction(Long id){
		super(id);
	}
	public BigDecimal getCrSellPrice() {
		return crSellPrice;
	}

	public void setCrSellPrice(BigDecimal crSellPrice) {
		this.crSellPrice = crSellPrice;
	}
	
	public BigDecimal getCrBuyPrice() {
		return crBuyPrice;
	}

	public void setCrBuyPrice(BigDecimal crBuyPrice) {
		this.crBuyPrice = crBuyPrice;
	}
	
	@NotNull(message="所属地区不能为空")
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Boolean getIsPub() {
		return isPub;
	}

	public void setIsPub(Boolean isPub) {
		this.isPub = isPub;
	}

	public Integer getOverdueTab() {
		return overdueTab;
	}

	public void setOverdueTab(Integer overdueTab) {
		this.overdueTab = overdueTab;
	}

	public Integer getPriceTab() {
		return priceTab;
	}

	public void setPriceTab(Integer priceTab) {
		this.priceTab = priceTab;
	}

	public Integer getUnPayTab() {
		return unPayTab;
	}

	public void setUnPayTab(Integer unPayTab) {
		this.unPayTab = unPayTab;
	}

	public Integer getPayedTab() {
		return payedTab;
	}

	public void setPayedTab(Integer payedTab) {
		this.payedTab = payedTab;
	}

	public Integer getAuctionTab() {
		return auctionTab;
	}

	public void setAuctionTab(Integer auctionTab) {
		this.auctionTab = auctionTab;
	}

	public Integer getAuctionedTab() {
		return auctionedTab;
	}

	public void setAuctionedTab(Integer auctionedTab) {
		this.auctionedTab = auctionedTab;
	}

	public Member getCrSeller() {
		return crSeller;
	}

	public void setCrSeller(Member crSeller) {
		this.crSeller = crSeller;
	}

	public Member getCrBuyer() {
		return crBuyer;
	}

	public void setCrBuyer(Member crBuyer) {
		this.crBuyer = crBuyer;
	}

	public String getAuditOpinion() {
		return auditOpinion;
	}

	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion;
	}

	public NfsLoanRecord getLoanRecord() {
		return loanRecord;
	}

	public void setLoanRecord(NfsLoanRecord loanRecord) {
		this.loanRecord = loanRecord;
	}
	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public BigDecimal getMaxInRate() {
		return maxInRate;
	}

	public void setMaxInRate(BigDecimal maxInRate) {
		this.maxInRate = maxInRate;
	}

	public BigDecimal getMinInRate() {
		return minInRate;
	}

	public void setMinInRate(BigDecimal minInRate) {
		this.minInRate = minInRate;
	}

	public String getZipPath() {
		return zipPath;
	}

	public void setZipPath(String zipPath) {
		this.zipPath = zipPath;
	}

	public ProofStatus getProofStatus() {
		return proofStatus;
	}

	public void setProofStatus(ProofStatus proofStatus) {
		this.proofStatus = proofStatus;
	}


	
}