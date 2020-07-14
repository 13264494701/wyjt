package com.jxf.mms.tmpl.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.mms.tmpl.entity.MmsMsgTmpl;

/**
 * 消息模板DAO接口
 * @author wo
 * @version 2018-10-28
 */
@MyBatisDao
public interface MmsMsgTmplDao extends CrudDao<MmsMsgTmpl> {
	
	MmsMsgTmpl getTmplByCode(String code);
}