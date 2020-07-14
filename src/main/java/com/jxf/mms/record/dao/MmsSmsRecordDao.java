package com.jxf.mms.record.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.mms.record.entity.MmsSmsRecord;

/**
 * 短信记录DAO接口
 * @author JINXINFU
 * @version 2016-04-08
 */
@MyBatisDao
public interface MmsSmsRecordDao extends CrudDao<MmsSmsRecord> {
	
	public int countSmsHasSend(MmsSmsRecord mmsSmsRecord);
	
	public MmsSmsRecord getRecentRecord(MmsSmsRecord mmsSmsRecord);
}