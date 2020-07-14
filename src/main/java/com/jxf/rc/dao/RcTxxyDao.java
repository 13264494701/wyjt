package com.jxf.rc.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

import com.jxf.rc.entity.RcTxxy;


/**
 * 风控 天下信用DAO接口
 * @author wo
 * @version 2019-6-16
 */
@MyBatisDao
public interface RcTxxyDao extends CrudDao<RcTxxy> {

    
	RcTxxy findByReportNo(String reportNo);
    

	
}