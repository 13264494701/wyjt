package com.jxf.mem.entity;

import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 会员账户Entity
 * @author zhj
 * @version 2016-05-12
 */
public class MemberAct extends CrudEntity<MemberAct> {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 状态
	 */
	public enum Status {

		/** 已启用 */
		enabled,

		/** 已冻结 */
		disabled
	}

	/**会员 */
	private Member member; 
	/**账户名称 */
	private String name;	 
	/**账户科目 */
	private String subNo;		
	/**币种 */
	private String currCode;    
	/**账户余额 */
	private BigDecimal curBal;	 
	/** 是否默认*/
	private Boolean isDefault;	
	/** 账户状态*/
	private Status status;		

	/**交易金额 */
	private BigDecimal trxAmt;
	
	public MemberAct() {
		super();
	}

	public MemberAct(Long id){
		super(id);
	}
	
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	@Length(min=1, max=8, message="账户科目长度必须介于 1 和 8 之间")
	public String getSubNo() {
		return subNo;
	}

	public void setSubNo(String subNo) {
		this.subNo = subNo;
	}
	
	public String getCurrCode() {
		return currCode;
	}

	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public BigDecimal getCurBal() {
		return curBal;
	}

	public void setCurBal(BigDecimal curBal) {
		this.curBal = curBal;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public BigDecimal getTrxAmt() {
		return trxAmt;
	}

	public void setTrxAmt(BigDecimal trxAmt) {
		this.trxAmt = trxAmt;
	}




	
}