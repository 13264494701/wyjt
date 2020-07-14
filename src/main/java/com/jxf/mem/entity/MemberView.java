package com.jxf.mem.entity;

import javax.validation.constraints.NotNull;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 会员视图Entity
 * @author wo
 * @version 2019-01-20
 */
public class MemberView extends CrudEntity<MemberView> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 状态
	 */
	public enum Status {

		/**初始  */
		initial,
		
		/**接管  */
		take,

		/** 复位 */
		reset
	}
	
	/** 会员 */
	private Member member;		
	
	/** 好友数量 */
	private Long friendQuantity;		
	/** 状态 */
	private Status status;		
	
	public MemberView() {
		super();
	}

	public MemberView(Long id){
		super(id);
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	@NotNull(message="好友数量不能为空")
	public Long getFriendQuantity() {
		return friendQuantity;
	}

	public void setFriendQuantity(Long friendQuantity) {
		this.friendQuantity = friendQuantity;
	}
	

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}


	
}