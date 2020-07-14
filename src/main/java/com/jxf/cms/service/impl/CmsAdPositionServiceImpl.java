package com.jxf.cms.service.impl;

import java.util.List;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.cms.dao.CmsAdPositionDao;
import com.jxf.cms.entity.CmsAdPosition;
import com.jxf.cms.service.CmsAdPositionService;
import com.jxf.cms.utils.CmsUtils;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 广告位置ServiceImpl
 * @author JINXINFU
 * @version 2016-04-25
 */
@Service("adPositionService")
@Transactional(readOnly = true)
public class CmsAdPositionServiceImpl extends CrudServiceImpl<CmsAdPositionDao, CmsAdPosition> implements CmsAdPositionService{

	@Autowired
	private CmsAdPositionDao adPositionDao;
	
	@Override
	public CmsAdPosition get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<CmsAdPosition> findList(CmsAdPosition adPosition) {
		return super.findList(adPosition);
	}
	
	@Override
	public Page<CmsAdPosition> findPage(Page<CmsAdPosition> page, CmsAdPosition adPosition) {
		return super.findPage(page, adPosition);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(CmsAdPosition adPosition) {
		adPosition.setTemplate(StringEscapeUtils.unescapeHtml4(adPosition.getTemplate()));
		if(adPosition.getIsNewRecord()){
			adPosition.setPositionNo(CmsUtils.genNewPositionNo());
			adPosition.preInsert();
			dao.insert(adPosition);
		}else{
			adPosition.preUpdate();
			dao.update(adPosition);
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(CmsAdPosition adPosition) {
		super.delete(adPosition);
	}
	
	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "adPosition", condition = "#useCache")
	public CmsAdPosition getByPositionNo(String positionNo,boolean useCache) {
		return adPositionDao.getByPositionNo(positionNo);
	}
}