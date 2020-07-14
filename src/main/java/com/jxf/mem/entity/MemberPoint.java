package com.jxf.mem.entity;

import javax.validation.constraints.NotNull;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 会员积分Entity
 * @author JINXINFU
 * @version 2016-04-25
 */
public class MemberPoint extends CrudEntity<MemberPoint> {
	
	private static final long serialVersionUID = 1L;
	private Member member;		// 会员
	private Long balancePoints;		// 积分余额
	private Long totalPoints;		// 累计积分
	private Long reducePoints;		// 扣除积分

	
	public MemberPoint() {
		super();
	}

	public MemberPoint(Long id){
		super(id);
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	@NotNull(message="积分余额不能为空")
	public Long getBalancePoints() {
		return balancePoints;
	}

	public void setBalancePoints(Long balancePoints) {
		this.balancePoints = balancePoints;
	}
	
	@NotNull(message="累计积分不能为空")
	public Long getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(Long totalPoints) {
		this.totalPoints = totalPoints;
	}
	
	@NotNull(message="扣除积分不能为空")
	public Long getReducePoints() {
		return reducePoints;
	}

	public void setReducePoints(Long reducePoints) {
		this.reducePoints = reducePoints;
	}
		
}