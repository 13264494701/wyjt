package com.jxf.mem.entity;

import java.util.Date;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 好友关系表Entity
 * @author XIAORONGDIAN
 * @version 2018-10-11
 */
public class MemberFriendRelation extends CrudEntity<MemberFriendRelation> {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 免费信用档案授权状态
	 */
	public enum FreeCaAuthStatus {
		
		/** 未授权 */
		unauthorized,
		
		/** 已申请 */
		applied,
		
		/** 已授权 */
		authorized,
		
		/** 已拒绝 */
		reject,
		
		/** 已过期 */
		expired
		
	}
	/**
	 * 收费信用档案状态
	 */
	public enum ChargeCaStatus {
		
		/** 未付费*/
		unpay,
		
		/** 已付费*/
		payed,
		
		/** 已过期 */
		expired
		
	}
	/** 用户 */
	private Member member;		
	/** 好友 */
	private Member friend;		
	/** 未读消息条数 */
	private Integer unread;		
	
	/** 免费信用档案授权状态 */
	private FreeCaAuthStatus freeCaAuthStatus;
	/** 免费信用档案授权时间 */
	private Date freeCaAuthTime;
	
	/** 收费信用档案收费状态 */
	private ChargeCaStatus chargeCaStatus;
	/** 收费信用档案收费时间 */
	private Date chargeCaTime;

	
	
	public MemberFriendRelation() {
		super();
	}

	public MemberFriendRelation(Long id){
		super(id);
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

	public Integer getUnread() {
		return unread;
	}

	public void setUnread(Integer unread) {
		this.unread = unread;
	}

	public FreeCaAuthStatus getFreeCaAuthStatus() {
		return freeCaAuthStatus;
	}

	public void setFreeCaAuthStatus(FreeCaAuthStatus freeCaAuthStatus) {
		this.freeCaAuthStatus = freeCaAuthStatus;
	}

	public Date getFreeCaAuthTime() {
		return freeCaAuthTime;
	}

	public void setFreeCaAuthTime(Date freeCaAuthTime) {
		this.freeCaAuthTime = freeCaAuthTime;
	}

	public ChargeCaStatus getChargeCaStatus() {
		return chargeCaStatus;
	}

	public void setChargeCaStatus(ChargeCaStatus chargeCaStatus) {
		this.chargeCaStatus = chargeCaStatus;
	}

	public Date getChargeCaTime() {
		return chargeCaTime;
	}

	public void setChargeCaTime(Date chargeCaTime) {
		this.chargeCaTime = chargeCaTime;
	}


	
}