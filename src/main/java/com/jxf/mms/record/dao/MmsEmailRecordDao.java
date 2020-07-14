package com.jxf.mms.record.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.mms.record.entity.MmsEmailRecord;

/**
 * 邮件记录DAO接口
 * @author JINXINFU
 * @version 2016-04-08
 */
@MyBatisDao
public interface MmsEmailRecordDao extends CrudDao<MmsEmailRecord> {
	
}