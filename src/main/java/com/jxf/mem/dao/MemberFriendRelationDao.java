package com.jxf.mem.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.mem.entity.MemberFriendRelation;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 好友关系表DAO接口
 * @author XIAORONGDIAN
 * @version 2018-10-11
 */
@MyBatisDao
public interface MemberFriendRelationDao extends CrudDao<MemberFriendRelation> {

	/**
	 * 根据好友ID删除
	 * @param memberId
	 * @param friendId
	 */
	void deleteByFriendId(@Param("memberId")Long memberId, @Param("friendId")Long friendId);

	/**
	 * 通过ID查好友关系
	 * @param memberId
	 * @param friendId
	 * @return
	 */
	MemberFriendRelation getByMemberIdAndFriendId(@Param("memberId")Long memberId,@Param("friendId") Long friendId);

	/**
	 * 检查是否为好友关系
	 * @param memberId
	 * @param friendId
	 * @return
	 */
	Boolean checkFriendRelation(@Param("memberId")Long memberId,@Param("friendId") Long friendId);
	
	/**
	 * 通过 手机号 姓名 模糊查询 好友列表
	 * @param friendRelation
	 * @return
	 */
	List<MemberFriendRelation> findListByVagueSearch(MemberFriendRelation friendRelation);

	int getFriendCount(Long memberId);

	
}