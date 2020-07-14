package com.jxf.mem.dao;

import com.jxf.mem.entity.MmsSmsUserRecord;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 发送短信记录表DAO接口
 * @author gaobo
 * @version 2019-03-19
 */
@MyBatisDao
public interface MmsSmsUserRecordDao extends CrudDao<MmsSmsUserRecord> {
	
}