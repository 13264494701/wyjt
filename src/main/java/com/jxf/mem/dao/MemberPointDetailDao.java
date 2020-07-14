package com.jxf.mem.dao;

import com.jxf.mem.entity.MemberPointDetail;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 积分明细DAO接口
 * @author zhj
 * @version 2016-05-13
 */
@MyBatisDao
public interface MemberPointDetailDao extends CrudDao<MemberPointDetail> {
	
}