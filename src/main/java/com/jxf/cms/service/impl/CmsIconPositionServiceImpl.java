package com.jxf.cms.service.impl;

import java.util.List;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.cms.dao.CmsIconPositionDao;
import com.jxf.cms.entity.CmsIconPosition;
import com.jxf.cms.service.CmsIconPositionService;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 图标位置ServiceImpl
 * @author HUOJIABAO
 * @version 2016-07-23
 */
@Service("cmsIconPositionService")
@Transactional(readOnly = true)
public class CmsIconPositionServiceImpl extends CrudServiceImpl<CmsIconPositionDao, CmsIconPosition> implements CmsIconPositionService{

	@Autowired
	private CmsIconPositionDao iconPositionDao;
	
	@Override
	public CmsIconPosition get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<CmsIconPosition> findList(CmsIconPosition cmsIconPosition) {
		return super.findList(cmsIconPosition);
	}
	
	@Override
	public Page<CmsIconPosition> findPage(Page<CmsIconPosition> page, CmsIconPosition cmsIconPosition) {
		page.setOrderBy("a.position_no ASC");
		return super.findPage(page, cmsIconPosition);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(CmsIconPosition cmsIconPosition) {
		cmsIconPosition.setTemplate(StringEscapeUtils.unescapeHtml4(cmsIconPosition.getTemplate()));
		if(cmsIconPosition.getIsNewRecord()){
//			cmsIconPosition.setPositionNo(IconPositionUtils.genNewPositionNo());
			cmsIconPosition.preInsert();
			dao.insert(cmsIconPosition);
		}else{
			cmsIconPosition.preUpdate();
			dao.update(cmsIconPosition);
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(CmsIconPosition cmsIconPosition) {
		super.delete(cmsIconPosition);
	}
	
	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "iconPosition", condition = "#useCache")
	public CmsIconPosition getByPositionNo(String positionNo,boolean useCache) {
		return iconPositionDao.getByPositionNo(positionNo);
	}
}