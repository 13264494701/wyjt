package com.jxf.cms.service.impl;

import java.util.List;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.cms.dao.CmsIconDao;
import com.jxf.cms.entity.CmsIcon;
import com.jxf.cms.service.CmsIconService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 图标ServiceImpl
 * @author HUOJIABAO
 * @version 2016-07-23
 */
@Service("cmsIconService")
@Transactional(readOnly = true)
public class CmsIconServiceImpl extends CrudServiceImpl<CmsIconDao, CmsIcon> implements CmsIconService{
    
	@Autowired
	private CmsIconDao iconDao;
	
	@Override
	public CmsIcon get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<CmsIcon> findList(CmsIcon cmsIcon) {
		return super.findList(cmsIcon);
	}
	
	@Override
	public Page<CmsIcon> findPage(Page<CmsIcon> page, CmsIcon cmsIcon) {
		return super.findPage(page, cmsIcon);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(CmsIcon cmsIcon) {
		cmsIcon.setRedirectUrl(StringEscapeUtils.unescapeHtml4(cmsIcon.getRedirectUrl()));
		super.save(cmsIcon);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(CmsIcon cmsIcon) {
		super.delete(cmsIcon);
	}
	
	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "icon", condition = "#useCache")
	public CmsIcon getByPositionNo(String positionNo,boolean useCache) {
		return iconDao.getByPositionNo(positionNo);
	}

	@Override
	public List<CmsIcon> findListByPageNo(String pageNo) {

		return iconDao.findListByPageNo(pageNo);
	}
}