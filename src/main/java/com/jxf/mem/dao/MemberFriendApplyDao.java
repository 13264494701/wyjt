package com.jxf.mem.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.mem.entity.MemberFriendApply;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 好友申请记录DAO接口
 * @author XIAORONGDIAN
 * @version 2018-10-30
 */
@MyBatisDao
public interface MemberFriendApplyDao extends CrudDao<MemberFriendApply> {

	/**
	 * 给APP展示好友申请 如果是已添加的展示最近15天
	 * @param apply
	 * @return
	 */
	List<MemberFriendApply> findListForApp(@Param("apply") MemberFriendApply apply);


}