package com.jxf.svc.sys.log.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.svc.sys.log.entity.Log;


/**
 * 日志DAO接口
 * @author jxf
 * @version 2015-07-28
 */
@MyBatisDao
public interface LogDao extends CrudDao<Log> {

}
