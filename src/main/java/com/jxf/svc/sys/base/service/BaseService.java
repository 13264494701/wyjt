package com.jxf.svc.sys.base.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;



/**
 * Service基类
 * @author jxf
 * @version 1.0 2015-07-10
 */
@Transactional(readOnly = true)
public abstract class BaseService {
	

	/**
	 * 日志对象
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());
}
