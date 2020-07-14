package com.jxf.mem.entity;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 会员等级Entity
 * @author JINXINFU
 * @version 2016-04-25
 */
public class MemberRank extends CrudEntity<MemberRank> {
	
	private static final long serialVersionUID = 1L;
	private String rankNo;		// 等级编号
	private String rankName;		// 等级名称
	private BigDecimal consumeAmount;		// 消费金额
	private BigDecimal discounts;		// 优惠折扣
	private Boolean isDefault;		// 是否默认
	private Boolean isSpecial;		// 是否特殊

	
	public MemberRank() {
		super();
	}

	public MemberRank(Long id){
		super(id);
	}

	@Length(min=1, max=3, message="等级编号长度必须介于 1 和 3 之间")
	public String getRankNo() {
		return rankNo;
	}

	public void setRankNo(String rankNo) {
		this.rankNo = rankNo;
	}
	
	@Length(min=1, max=64, message="等级名称长度必须介于 1 和 64 之间")
	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}
	public BigDecimal getConsumeAmount() {
		return consumeAmount;
	}

	public void setConsumeAmount(BigDecimal consumeAmount) {
		this.consumeAmount = consumeAmount;
	}
	
	public BigDecimal getDiscounts() {
		return discounts;
	}

	public void setDiscounts(BigDecimal discounts) {
		this.discounts = discounts;
	}
	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	public Boolean getIsSpecial() {
		return isSpecial;
	}

	public void setIsSpecial(Boolean isSpecial) {
		this.isSpecial = isSpecial;
	}
		
}