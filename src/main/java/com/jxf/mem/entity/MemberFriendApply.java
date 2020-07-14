package com.jxf.mem.entity;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 好友申请记录Entity
 * @author XIAORONGDIAN
 * @version 2018-10-30
 */
public class MemberFriendApply extends CrudEntity<MemberFriendApply> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 状态
	 */
	public enum Status {

		/** 待同意 */
		pendingAgree,

		/** 已同意 */
		agreed,
		
		/** 已拒绝 */
		reject
	}
	
	/**
	 * 流量渠道
	 */
	public enum Channel {
		
		/** 普通 */
		common,
		
		/** 优放 */
		ufang 
	}
	
	/** 会员 */
	private Member member;		
	/** 好友(被申请人) */
	private Member friend;		
	/** 申请状态  */
	private Status status;		
	/** 同意时间 */
	private Date agreeDate;		

	private Channel channel;	
	/**验证消息*/
	private String note;
	
	public MemberFriendApply() {
		super();
	}

	public MemberFriendApply(Long id){
		super(id);
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getAgreeDate() {
		return agreeDate;
	}

	public void setAgreeDate(Date agreeDate) {
		this.agreeDate = agreeDate;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Member getFriend() {
		return friend;
	}

	public void setFriend(Member friend) {
		this.friend = friend;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}