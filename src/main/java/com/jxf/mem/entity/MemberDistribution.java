package com.jxf.mem.entity;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 三级分销Entity
 * @author huojiayuan
 * @version 2016-05-25
 */
public class MemberDistribution extends CrudEntity<MemberDistribution> {
	
	private static final long serialVersionUID = 1L;
	private Member member;		// 会员
	private Integer levelFirst;		// 一级会员
	private Integer levelSecond;		// 二级会员
	private Integer levelThird;		// 三级会员
	private BigDecimal reward;		// 佣金奖励
	private Boolean isOpen;  //是否开启三级分销

	
	public MemberDistribution() {
		super();
	}

	public MemberDistribution(Long id){
		super(id);
	}
	
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@NotNull(message="一级会员不能为空")
	public Integer getLevelFirst() {
		return levelFirst;
	}

	public void setLevelFirst(Integer levelFirst) {
		this.levelFirst = levelFirst;
	}
	
	@NotNull(message="二级会员不能为空")
	public Integer getLevelSecond() {
		return levelSecond;
	}

	public void setLevelSecond(Integer levelSecond) {
		this.levelSecond = levelSecond;
	}
	
	@NotNull(message="三级会员不能为空")
	public Integer getLevelThird() {
		return levelThird;
	}

	public void setLevelThird(Integer levelThird) {
		this.levelThird = levelThird;
	}
	
	public BigDecimal getReward() {
		return reward;
	}

	public void setReward(BigDecimal reward) {
		this.reward = reward;
	}
	public Boolean getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Boolean isOpen) {
		this.isOpen = isOpen;
	}
	
}