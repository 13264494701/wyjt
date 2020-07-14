package com.jxf.svc.sys.app.service;

import com.jxf.svc.sys.app.entity.AppInst;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 应用激活Service
 * @author wo
 * @version 2019-07-09
 */
public interface AppInstService extends CrudService<AppInst> {

	
	Boolean idfaExist(String idfa);
}