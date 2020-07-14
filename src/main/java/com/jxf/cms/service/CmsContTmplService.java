package com.jxf.cms.service;

import com.jxf.cms.entity.CmsContTmpl;
import com.jxf.svc.sys.crud.service.CrudService;


/**
 * 合同模板Service
 * @author huojiayuan
 * @version 2016-12-01
 */
public interface CmsContTmplService extends CrudService<CmsContTmpl> {

	CmsContTmpl getRegContract();
	
	void setStatic(CmsContTmpl contTmpl);
}