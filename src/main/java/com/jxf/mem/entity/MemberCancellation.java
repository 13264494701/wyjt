package com.jxf.mem.entity;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 会员注销申请Entity
 * @author SuHuimin
 * @version 2019-06-19
 */
public class MemberCancellation extends CrudEntity<MemberCancellation> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 注销申请状态
	 */
	public enum Status {

		/** 待审核 */
		review,

		/** 注销成功 */
		success,
		
		/** 注销失败 */
		fail
	}
	
			/** 会员 */
	private Member member;		
			/** 注销申请状态 */
	private Status status;		
			/** 注销原因 */
	private String reason;		
	
	public MemberCancellation() {
		super();
	}

	public MemberCancellation(Long id){
		super(id);
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	@Length(min=0, max=255, message="注销原因长度必须介于 0 和 255 之间")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}