package com.jxf.mem.service;


import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberPoint;
import com.jxf.mem.entity.MemberPointRule;
import com.jxf.svc.sys.crud.service.CrudService;

import java.math.BigDecimal;

/**
 * 会员积分Service
 * @author JINXINFU
 * @version 2016-04-25
 */
public interface MemberPointService extends CrudService<MemberPoint> {

	/***
	 * 
	 * @param memNo
	 * @return
	 */
	MemberPoint getByMember(Member member);
	
	/**
	 * 初始化会员积分
	 * @param memNo
	 */
	Boolean initMemPoint(Member member);
//	/**
//	 * 调整修改会员积分
//	 */
//	void updateMemberPoint(Member member, MemberPointRule.Type type,Binding binding);
	/**
	 * 调整修改会员积分
	 */
	void updateMemberPoint(Member member, MemberPointRule.Type type, BigDecimal amount);
	
}