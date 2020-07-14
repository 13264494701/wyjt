package com.jxf.cms.service.impl;

import java.util.List;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.cms.entity.CmsArticle;
import com.jxf.cms.entity.CmsArticleContent;
import com.jxf.cms.dao.CmsArticleContentDao;
import com.jxf.cms.dao.CmsArticleDao;
import com.jxf.cms.service.CmsArticleService;
/**
 * 文章ServiceImpl
 * @author JINXINFU
 * @version 2016-11-25
 */
@Service("cmsArticleService")
@Transactional(readOnly = true)
public class CmsArticleServiceImpl extends CrudServiceImpl<CmsArticleDao, CmsArticle> implements CmsArticleService{

	@Autowired
	private CmsArticleDao articleDao;
	@Autowired
	private CmsArticleContentDao articleContentDao;

	
	public CmsArticle get(Long id) {
		return super.get(id);
	}
	
	public List<CmsArticle> findList(CmsArticle cmsArticle) {
		return super.findList(cmsArticle);
	}
	
	public Page<CmsArticle> findPage(Page<CmsArticle> page, CmsArticle cmsArticle) {
		return super.findPage(page, cmsArticle);
	}
	
	@Transactional(readOnly = false)
	public void save(CmsArticle cmsArticle) {
		cmsArticle.setTitle(StringEscapeUtils.unescapeHtml4(cmsArticle.getTitle()));
		cmsArticle.setCopyfrom(StringEscapeUtils.unescapeHtml4(cmsArticle.getCopyfrom()));
		
		CmsArticleContent articleContent = cmsArticle.getArticleContent();		
		if (articleContent!=null&&articleContent.getContent()!=null){
			articleContent.setContent(StringEscapeUtils.unescapeHtml4(articleContent.getContent()));
		}
		
		if(cmsArticle.getIsNewRecord()){
			cmsArticle.preInsert();
			cmsArticle.setIsPub(false);
			cmsArticle.setIsStatic(false);
			cmsArticle.setDisplayHits(0);
			cmsArticle.setRealHits(0);
			cmsArticle.setLikes(0);
			cmsArticle.setDislikes(0);
			articleDao.insert(cmsArticle);
			articleContent.setId(cmsArticle.getId());
			articleContentDao.insert(articleContent);
		}else{			
			cmsArticle.preUpdate();
			cmsArticle.setIsStatic(false);
			articleDao.update(cmsArticle);
			articleContent.setId(cmsArticle.getId());
			articleContentDao.update(articleContent);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(CmsArticle cmsArticle) {
		super.delete(cmsArticle);
	}

	@Override
	@Transactional(readOnly = false)
	public void setStatic(Long id) {		
		articleDao.setStatic(id);		
	}
	
	@Override
	@Transactional(readOnly = false)
	public void pubArticle(CmsArticle cmsArticle) {
		articleDao.pubArticle(cmsArticle.getId());		
	}

	@Override
	@Transactional(readOnly = false)
	public void unpubArticle(CmsArticle cmsArticle) {
		articleDao.unpubArticle(cmsArticle.getId());		
	}
	@Override
	@Transactional(readOnly = false)
	public void topArticle(CmsArticle cmsArticle) {

		articleDao.topArticle(cmsArticle);
	}
	@Override
	@Transactional(readOnly = false)
	public void addLikes(String aid) {
		
		articleDao.addLikes(aid);
	}
	@Override
	@Transactional(readOnly = false)
	public void disLike(String aid) {
		
		articleDao.disLike(aid);
	}
	@Override
	public Page<CmsArticle> findPage(CmsArticle cmsArticle, Integer pageNo,Integer pageSize) {
		Page<CmsArticle> page = new Page<CmsArticle>(pageNo == null?1:pageNo, pageSize == null?5:pageSize);	
		cmsArticle.setPage(page);
		List<CmsArticle> articles = findList(cmsArticle);
		page.setList(articles);
		return page;
	}

	@Override
	public CmsArticle getByOriginal(String dataOriginal) {
		
		return articleDao.getByOriginal(dataOriginal);
	}

	@Override
	public List<CmsArticle> findListByRand(CmsArticle cmsArticle) {
		
		return articleDao.findListByRand(cmsArticle);
	}


	
}