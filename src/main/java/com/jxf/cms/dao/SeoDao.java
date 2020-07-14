package com.jxf.cms.dao;

import com.jxf.cms.entity.Seo;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * SEODAO接口
 * @author huojiayuan
 * @version 2016-06-13
 */
@MyBatisDao
public interface SeoDao extends CrudDao<Seo> {
	
	/**
	 * 查找SEO设置
	 * 
	 * @param seo
	 * 
	 * @return SEO设置
	 */
	Seo findBySeoType(Seo shopSeo);
	
}