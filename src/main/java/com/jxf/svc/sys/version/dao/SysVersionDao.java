package com.jxf.svc.sys.version.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.svc.sys.version.entity.SysVersion;
import com.jxf.svc.annotation.MyBatisDao;


/**
 * 系统版本DAO接口
 * @author wo
 * @version 2019-01-07
 */
@MyBatisDao
public interface SysVersionDao extends CrudDao<SysVersion> {
	
	SysVersion getByType(SysVersion sysVersion);
}