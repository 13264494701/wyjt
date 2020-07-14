package com.jxf.cms.service;

import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.cms.entity.CmsChannel;

/**
 * 频道信息Service
 * @author JINXINFU
 * @version 2016-11-20
 */
public interface CmsChannelService extends CrudService<CmsChannel> {

	public CmsChannel getByAlias(String alias);
	
}