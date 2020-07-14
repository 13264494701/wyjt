package com.jxf.cms.dao;

import com.jxf.cms.entity.CmsContTmpl;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 合同模板DAO接口
 * @author huojiayuan
 * @version 2016-12-01
 */
@MyBatisDao
public interface CmsContTmplDao extends CrudDao<CmsContTmpl> {

	public CmsContTmpl getContTmpl(CmsContTmpl cmsContTmpl);
	
}