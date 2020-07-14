package com.jxf.svc.sys.data.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.svc.sys.data.entity.SysDataTask;
import com.jxf.svc.annotation.MyBatisDao;


/**
 * 批次任务DAO接口
 * @author wo
 * @version 2019-01-12
 */
@MyBatisDao
public interface SysDataTaskDao extends CrudDao<SysDataTask> {
	
}