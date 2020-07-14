package com.jxf.svc.sys.data.dao;

import org.apache.ibatis.annotations.Param;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.svc.sys.data.entity.SysDataImport;

/**
 * 数据导入DAO接口
 * @author wo
 * @version 2019-01-13
 */
@MyBatisDao
public interface SysDataImportDao extends CrudDao<SysDataImport> {
	
	SysDataImport getByHandler(@Param("handler")String handler);
}