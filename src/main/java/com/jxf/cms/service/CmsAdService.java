package com.jxf.cms.service;

import java.util.List;

import com.jxf.cms.entity.CmsAd;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 广告管理Service
 * @author JINXINFU
 * @version 2016-04-25
 */
public interface CmsAdService extends CrudService<CmsAd> {

	
	List<CmsAd> findListByPosition(String positionNo);
}