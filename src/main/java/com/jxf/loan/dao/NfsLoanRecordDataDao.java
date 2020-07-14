package com.jxf.loan.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.loan.entity.NfsLoanRecordData;
import com.jxf.svc.annotation.MyBatisDao;


/**
 * 借条数据DAO接口
 * @author wo
 * @version 2019-04-28
 */
@MyBatisDao
public interface NfsLoanRecordDataDao extends CrudDao<NfsLoanRecordData> {
	
}