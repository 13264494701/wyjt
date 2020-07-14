package com.jxf.svc.sys.data.service;

import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.svc.sys.data.entity.SysDataImport;


/**
 * 数据导入Service
 * @author wo
 * @version 2019-01-13
 */
public interface SysDataImportService extends CrudService<SysDataImport> {

	SysDataImport getByHandler(String handler);
	
	void createTask(SysDataImport sysDataImport);
	
	void clearTask(SysDataImport sysDataImport);
	
}