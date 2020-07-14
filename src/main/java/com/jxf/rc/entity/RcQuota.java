package com.jxf.rc.entity;

import org.hibernate.validator.constraints.Length;

import com.jxf.mem.entity.Member;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 会员额度评估Entity
 * @author SuHuimin
 * @version 2019-08-23
 */
public class RcQuota extends CrudEntity<RcQuota> {
	
	private static final long serialVersionUID = 1L;
			/** 会员 */
	private Member member;
			/** 额度 */
	private Integer quota;		
			/** 综合评分 */
	private Integer score;		
			/** 有盾返回数据 */
	private String content;
			/** 发送有盾的订单Id */
	private Long orderId;
	
	public RcQuota() {
		super();
	}

	public RcQuota(Long id){
		super(id);
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@Length(min=0, max=11, message="额度长度必须介于 0 和 11 之间")
	public Integer getQuota() {
		return quota;
	}

	public void setQuota(Integer quota) {
		this.quota = quota;
	}
	
	@Length(min=0, max=11, message="综合评分长度必须介于 0 和 11 之间")
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
}