package com.jxf.mem.dao;

import com.jxf.mem.entity.MemberFriendReport;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 好友投诉DAO接口
 * @author gaobo
 * @version 2018-11-16
 */
@MyBatisDao
public interface MemberFriendReportDao extends CrudDao<MemberFriendReport> {
	
}