package com.jxf.mem.entity;


import java.math.BigDecimal;
import java.util.Date;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 账户交易记录Entity
 * @author zhj
 * @version 2016-05-13
 */
public class MemberActTrx extends CrudEntity<MemberActTrx> {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 交易状态
	 */
	public enum Status {
		
		/**登记*/
		REGIST,
		
		/**成功*/
		SUCC,

		/**失败*/
		FAIL,
		
		/**其它*/
		OTHERS
		
	}
	/**
	 * 组(给前台展示)
	 */
	public enum Group {
		
		/** 充值提现记录 */
		rechargeRecord,
		
		/** 转账 */
		transfer,
		
		/** 借款记录 */
		loan,
		
		/** 还款记录 */
		repayment,
		
		/** 法律仲裁 */
		arbitration,
		
		/** 催收 */
		collection,
		
		/**	不显示*/
		other,
		
		/**	全部中显示*/
		all,
		
	}
	
	/** 交易代码 {TrxRuleConstant}*/
	private String trxCode;		
	
	/** 会员*/
	private Member member;	
	
	/** 科目编号 */
	private String subNo;
	
	/** 记账方向 D入账 C出账 */
	private String drc;	
	
	/** 交易金额 */
	private BigDecimal trxAmt;		
	/** 账户余额 */
	private BigDecimal curBal;	
	/** 币种 */
	private String currCode;	
	
	private Group trxGroup; //组(给前台展示)
	
	private String title;		// 标题

	private Status status;
	
	/** 原业务ID */
	private Long orgId;
	
	/** 开始日期   */
	private Date beginTime;
	
	/** 结束日期  */
	private Date endTime;
	
	/** 最小金额  */
	private BigDecimal minAmount; 
	
	/** 最大金额  */
	private BigDecimal maxAmount;
	
	/** 总收入 */
	private BigDecimal totalIncome;
	
	/** 总支出 */
	private BigDecimal totalExpenditure;
	/** 本页开始下标 */
	private Integer pageStart;
	
	public MemberActTrx() {
		super();
	}

	public MemberActTrx(Long id){
		super(id);
	}
	

	public String getDrc() {
		return drc;
	}

	public void setDrc(String drc) {
		this.drc = drc;
	}


	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public BigDecimal getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}

	public BigDecimal getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}

	public BigDecimal getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}

	public BigDecimal getTotalExpenditure() {
		return totalExpenditure;
	}

	public void setTotalExpenditure(BigDecimal totalExpenditure) {
		this.totalExpenditure = totalExpenditure;
	}

	public Group getTrxGroup() {
		return trxGroup;
	}

	public void setTrxGroup(Group trxGroup) {
		this.trxGroup = trxGroup;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getSubNo() {
		return subNo;
	}

	public void setSubNo(String subNo) {
		this.subNo = subNo;
	}

	public String getTrxCode() {
		return trxCode;
	}

	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}

	public BigDecimal getTrxAmt() {
		return trxAmt;
	}

	public void setTrxAmt(BigDecimal trxAmt) {
		this.trxAmt = trxAmt;
	}

	public BigDecimal getCurBal() {
		return curBal;
	}

	public void setCurBal(BigDecimal curBal) {
		this.curBal = curBal;
	}

	public String getCurrCode() {
		return currCode;
	}

	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Integer getPageStart() {
		return pageStart;
	}

	public void setPageStart(Integer pageStart) {
		this.pageStart = pageStart;
	}





}