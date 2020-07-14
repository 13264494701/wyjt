package com.jxf.mms.tmpl.service;

import com.jxf.svc.sys.crud.service.CrudService;

import java.util.Map;
import com.jxf.mms.tmpl.entity.MmsMsgTmpl;

/**
 * 消息模板Service
 * @author wo
 * @version 2018-10-28
 */
public interface MmsMsgTmplService extends CrudService<MmsMsgTmpl> {

	MmsMsgTmpl getTmplByCode(String code);

	String process(String code, Map<String, Object> parameters);	
	
}