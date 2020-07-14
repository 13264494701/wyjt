package com.jxf.mem.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.mem.entity.MemberCancellation;

/**
 * 会员注销申请DAO接口
 * @author SuHuimin
 * @version 2019-06-19
 */
@MyBatisDao
public interface MemberCancellationDao extends CrudDao<MemberCancellation> {
	
}