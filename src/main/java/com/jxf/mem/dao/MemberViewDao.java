package com.jxf.mem.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;

import java.util.List;

import com.jxf.mem.entity.MemberView;
import com.jxf.svc.annotation.MyBatisDao;


/**
 * 会员视图DAO接口
 * @author wo
 * @version 2019-01-20
 */
@MyBatisDao
public interface MemberViewDao extends CrudDao<MemberView> {
	
	List<MemberView> findFriendCountList();
}