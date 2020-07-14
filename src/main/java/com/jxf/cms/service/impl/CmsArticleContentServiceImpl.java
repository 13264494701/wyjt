package com.jxf.cms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.cms.entity.CmsArticleContent;
import com.jxf.cms.dao.CmsArticleContentDao;
import com.jxf.cms.service.CmsArticleContentService;
/**
 * 文章内容ServiceImpl
 * @author JINXINFU
 * @version 2016-11-25
 */
@Service("cmsArticleContentService")
@Transactional(readOnly = true)
public class CmsArticleContentServiceImpl extends CrudServiceImpl<CmsArticleContentDao, CmsArticleContent> implements CmsArticleContentService{

	@Override
	public CmsArticleContent get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<CmsArticleContent> findList(CmsArticleContent cmsArticleContent) {
		return super.findList(cmsArticleContent);
	}
	
	@Override
	public Page<CmsArticleContent> findPage(Page<CmsArticleContent> page, CmsArticleContent cmsArticleContent) {
		return super.findPage(page, cmsArticleContent);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(CmsArticleContent cmsArticleContent) {
		super.save(cmsArticleContent);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(CmsArticleContent cmsArticleContent) {
		super.delete(cmsArticleContent);
	}
	
}