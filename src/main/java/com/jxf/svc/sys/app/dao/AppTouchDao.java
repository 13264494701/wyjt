package com.jxf.svc.sys.app.dao;

import com.jxf.svc.sys.app.entity.AppTouch;
import com.jxf.svc.sys.crud.dao.CrudDao;

import org.apache.ibatis.annotations.Param;

import com.jxf.svc.annotation.MyBatisDao;


/**
 * 应用曝光DAO接口
 * @author wo
 * @version 2019-07-09
 */
@MyBatisDao
public interface AppTouchDao extends CrudDao<AppTouch> {
	
	
	AppTouch getByIdfa(@Param("idfa")String idfa);
}