package com.jxf.rc.dao;

import com.jxf.rc.entity.RcCaData;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;


/**
 * 信用报告DAO接口
 * @author wo
 * @version 2019-08-09
 */
@MyBatisDao
public interface RcCaDataDao extends CrudDao<RcCaData>{
	
	RcCaData getByPhoneNoAndType(RcCaData caData);
}