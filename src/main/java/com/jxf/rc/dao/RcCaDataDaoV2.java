package com.jxf.rc.dao;


import com.jxf.rc.entity.RcCaDataV2;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;


/**
 * 信用报告DAO接口
 * @author wo
 * @version 2019-08-12
 */
@MyBatisDao
public interface RcCaDataDaoV2 extends CrudDao<RcCaDataV2>{
	
	RcCaDataV2 getByPhoneNoAndType(RcCaDataV2 caData);
}