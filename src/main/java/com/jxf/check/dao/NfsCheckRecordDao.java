package com.jxf.check.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.check.entity.NfsCheckRecord;

/**
 * 审核记录DAO接口
 * @author suHuimin
 * @version 2019-01-26
 */
@MyBatisDao
public interface NfsCheckRecordDao extends CrudDao<NfsCheckRecord> {
	
}