package com.jxf.mem.service;

import java.util.List;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberFriendApply;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 好友申请记录Service
 * @author XIAORONGDIAN
 * @version 2018-10-30
 */
public interface MemberFriendApplyService extends CrudService<MemberFriendApply> {


	/**
	 * 用户好友申请列表
	 * @param apply
	 * @return
	 */
	List<MemberFriendApply> findMemberFriendApply(MemberFriendApply apply);
	/**
	 * 用户好友申请列表
	 * @param drc
	 * @return
	 */
	Page<MemberFriendApply> findMemberFriendApplyPage(Member member, Integer drc ,Integer pageNo,Integer pageSize);
	
}