package com.jxf.mem.entity;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 信用报告申请Entity
 * @author wo
 * @version 2018-12-17
 */
public class MemberFriendCa extends CrudEntity<MemberFriendCa> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 方向
	 */
	public enum Drc {

		/** 申请查看 */
		apply,

		/** 主动发送 */
		send
	}
	/**
	 * 类型
	 */
	public enum Type {

		/** 免费 */
		free,

		/** 新颜（一元） */
		oenMoney
	}
	/**
	 * 状态
	 */
	public enum Status {

		/** 申请中 */
		apply,

		/** 同意 */
		agree,
		/**
		 * 拒绝
		 */
		refuse
	}
	/** 会员 */
	private Member member;		
	/** 好友 */
	private Member friend;		
	/** 方向 */
	private Drc drc;		
	/** 类型 */
	private Type type;		
	/** 状态 */
	private Status status;		
	
	public MemberFriendCa() {
		super();
	}

	public MemberFriendCa(Long id){
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
	

	public Drc getDrc() {
		return drc;
	}

	public void setDrc(Drc drc) {
		this.drc = drc;
	}
	

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}


	
}