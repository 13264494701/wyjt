package com.jxf.svc.sys.app.dao;

import com.jxf.svc.sys.app.entity.AppInst;
import com.jxf.svc.sys.crud.dao.CrudDao;

import org.apache.ibatis.annotations.Param;

import com.jxf.svc.annotation.MyBatisDao;

/**
 * 应用激活DAO接口
 * @author wo
 * @version 2019-07-09
 */
@MyBatisDao
public interface AppInstDao extends CrudDao<AppInst> {
	
	Boolean idfaExist(@Param("idfa")String idfa);
}