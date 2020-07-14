package com.jxf.mem.dao;

import com.jxf.mem.entity.MessageUserRecord;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 发送站内信记录表DAO接口
 * @author gaobo
 * @version 2019-03-19
 */
@MyBatisDao
public interface MessageUserRecordDao extends CrudDao<MessageUserRecord> {
	
}