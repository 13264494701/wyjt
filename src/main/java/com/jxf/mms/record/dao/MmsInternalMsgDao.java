package com.jxf.mms.record.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.mms.record.entity.MmsInternalMsg;

/**
 * 站内消息DAO接口
 * @author zhj
 * @version 2016-02-16
 */
@MyBatisDao
public interface MmsInternalMsgDao extends CrudDao<MmsInternalMsg> {
	
}