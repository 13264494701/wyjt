package com.jxf.mem.entity;


import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 用户反馈意见Entity
 * @author suhuimin
 * @version 2018-11-01
 */
public class MemberFeedback extends CrudEntity<MemberFeedback> {
	
	private static final long serialVersionUID = 1L;
	/** 用户 */
	private Member member;		
	/** 用户意见 */
	private String note;		
	
	public MemberFeedback() {
		super();
	}

	public MemberFeedback(Long id){
		super(id);
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}


	
}