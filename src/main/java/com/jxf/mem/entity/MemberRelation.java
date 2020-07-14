package com.jxf.mem.entity;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 会员关系Entity
 * @author huojiayuan
 * @version 2016-05-24
 */
public class MemberRelation extends CrudEntity<MemberRelation> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 层级
	 */
	public enum Level {

		/** 一级 */
		first,

		/** 二级 */
		second,
		
		/** 三级 */
		third
	}
	private Member member;		  // 会员
	private Member lowerMember;	  // 下级会员
	private Level level;		  // 层级关系
	
	public MemberRelation() {
		super();
	}

	public MemberRelation(Long id){
		super(id);
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	

	public Member getLowerMember() {
		return lowerMember;
	}

	public void setLowerMember(Member lowerMember) {
		this.lowerMember = lowerMember;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	
}