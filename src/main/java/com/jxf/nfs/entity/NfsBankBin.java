package com.jxf.nfs.entity;


import org.hibernate.validator.constraints.Length;

import com.jxf.mem.entity.MemberCard.CardType;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 卡BINEntity
 * @author wo
 * @version 2018-09-29
 */
public class NfsBankBin extends CrudEntity<NfsBankBin> {
	
	private static final long serialVersionUID = 1L;
	
	/** 银行 */
	private NfsBankInfo bank;		
	/** 卡BIN */
	private String cardBin;	
	/** 卡片种类 */
	private CardType cardType;		
	
	/** 卡号长度 */
	private Integer length;	
	
	public NfsBankBin() {
		super();
	}

	public NfsBankBin(Long id){
		super(id);
	}

	public NfsBankInfo getBank() {
		return bank;
	}

	public void setBank(NfsBankInfo bank) {
		this.bank = bank;
	}
	
	@Length(min=1, max=8, message="银行名称长度必须介于 1 和 8 之间")
	public String getCardBin() {
		return cardBin;
	}

	public void setCardBin(String cardBin) {
		this.cardBin = cardBin;
	}

	public CardType getCardType() {
		return cardType;
	}

	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}


	
}