package com.jxf.cms.service;

import com.jxf.cms.entity.Seo;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * SEOService
 * @author huojiayuan
 * @version 2016-06-13
 */
public interface SeoService extends CrudService<Seo> {

	/**
	 * 查找SEO设置
	 * 
	 * @param seoType
	 *            类型
	 * @param useCache
	 *            是否使用缓存
	 * @return SEO设置
	 */
	Seo findBySeoType(Seo.Type seoType, boolean useCache);
	
}