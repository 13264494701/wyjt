package com.jxf.mem.entity;


import org.hibernate.validator.constraints.Length;

import com.jxf.nfs.entity.NfsBankInfo;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 银行卡Entity
 * @author wo
 * @version 2018-09-29
 */
public class MemberCard extends CrudEntity<MemberCard> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 卡片种类
	 */
	public enum CardType {

		/** 储蓄卡 */
		DC,

		/** 信用卡 */
		CC,

		/** 准贷记卡 */
		SCC,

		/** 预付费卡 */
		PC
	}
	/**
	 * 状态
	 */
	public enum Status {

		/** 绑定 */
		binded,

		/** 解绑 */
		unbind
	}
	
	/** 会员 */
	private Member member;		
	/** 银行卡号 */
	private String cardNo;		
	/** 卡片种类 */
	private CardType cardType;		
	/** 银行 */
	private NfsBankInfo bank;				
	/** 状态 */
	private Status status;	
	/** 银行卡预留手机号 */
	private String phoneNo;
	
	
	public MemberCard() {
		super();
	}

	public MemberCard(Long id){
		super(id);
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	@Length(min=1, max=32, message="银行卡号长度必须介于 1 和 32 之间")
	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	

	public CardType getCardType() {
		return cardType;
	}

	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}
	

	public NfsBankInfo getBank() {
		return bank;
	}

	public void setBank(NfsBankInfo bank) {
		this.bank = bank;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	
}