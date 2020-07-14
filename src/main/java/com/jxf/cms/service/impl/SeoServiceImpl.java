package com.jxf.cms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.cms.dao.SeoDao;
import com.jxf.cms.entity.Seo;
import com.jxf.cms.service.SeoService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * SEOServiceImpl
 * @author huojiayuan
 * @version 2016-06-13
 */
@Service("shopSeoService")
@Transactional(readOnly = true)
public class SeoServiceImpl extends CrudServiceImpl<SeoDao, Seo> implements SeoService{

	@Autowired 
	private SeoDao seoDao;
	
	@Override
	public Seo get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<Seo> findList(Seo shopSeo) {
		return super.findList(shopSeo);
	}
	
	@Override
	public Page<Seo> findPage(Page<Seo> page, Seo shopSeo) {
		return super.findPage(page, shopSeo);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(Seo shopSeo) {
		super.save(shopSeo);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(Seo shopSeo) {
		super.delete(shopSeo);
	}
	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "seo", condition = "#useCache")
	public Seo findBySeoType(Seo.Type seoType, boolean useCache) {
		Seo seo = new Seo();
		seo.setType(seoType);
		return seoDao.findBySeoType(seo);
	}
}