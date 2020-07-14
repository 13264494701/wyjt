package com.jxf.mem.service;


import java.util.List;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberFriendRelation;
import com.jxf.mem.entity.MemberLoanReport;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 好友关系表Service
 * @author XIAORONGDIAN
 * @version 2018-10-11
 */
public interface MemberFriendRelationService extends CrudService<MemberFriendRelation> {


	/**
	 * 根据好友ID删除
	 * @param memberId
	 * @param friendId
	 */
	void deleteByFriendId(Long memberId, Long friendId);

	/**
	 * 通过俩人ID查好友关系 如果要查你是不是该好友的好友 memberId=该好友id friendId=你自己的id
	 * @param memberId
	 * @param friendId
	 * @return
	 */
	MemberFriendRelation findByMemberIdAndFriendId(Long memberId, Long friendId);
	
	/**
	 * 模糊查询 好友列表
	 * @param member
	 * @param friend 
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<MemberFriendRelation> vagueSearchfriendList(MemberFriendRelation memberFriendRelation);

	void changeTooManyFriendsStatus(Member member);

	/**
	 * 好友列表
	 * @param member
	 * @param timestamp 
	 * @return
	 */
	List<MemberFriendRelation> findMemberFriendRelation(Member member,String timestamp);

	/**
	 * 检查是否为好友关系
	 * @param memberId
	 * @param friendId
	 * @return
	 */
	Boolean checkFriendRelation(Long memberId, Long friendId);
	/**
	 * 获取好友信用报告  UFang
	 * @param memberLoanReport
	 */
	void getFriendCreditRecord(MemberLoanReport memberLoanReport);	
	
}