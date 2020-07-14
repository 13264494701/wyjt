package com.jxf.svc.sys.version.service;

import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.svc.sys.version.entity.SysVersion;

/**
 * 系统版本Service
 * @author wo
 * @version 2019-01-07
 */
public interface SysVersionService extends CrudService<SysVersion> {

	SysVersion getByType(SysVersion.Type type);
	
}