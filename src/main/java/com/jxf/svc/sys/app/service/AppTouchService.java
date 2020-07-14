package com.jxf.svc.sys.app.service;

import com.jxf.svc.sys.app.entity.AppTouch;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 应用曝光Service
 * @author wo
 * @version 2019-07-09
 */
public interface AppTouchService extends CrudService<AppTouch> {

	AppTouch getByIdfa(String idfa);
	
}