package com.jxf.cms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.cms.dao.CmsArticleCommentDao;
import com.jxf.cms.entity.CmsArticleComment;
import com.jxf.cms.service.CmsArticleCommentService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 文章评论ServiceImpl
 * @author JINXINFU
 * @version 2017-01-01
 */
@Service("cmsArticleCommentService")
@Transactional(readOnly = true)
public class CmsArticleCommentServiceImpl extends CrudServiceImpl<CmsArticleCommentDao, CmsArticleComment> implements CmsArticleCommentService{

	@Autowired
	private CmsArticleCommentDao articleCommentDao;
	
	public CmsArticleComment get(Long id) {
		return super.get(id);
	}
	
	public List<CmsArticleComment> findList(CmsArticleComment cmsArticleComment) {
		return super.findList(cmsArticleComment);
	}
	
	public Page<CmsArticleComment> findPage(Page<CmsArticleComment> page, CmsArticleComment cmsArticleComment) {
		return super.findPage(page, cmsArticleComment);
	}
	
	@Transactional(readOnly = false)
	public void save(CmsArticleComment cmsArticleComment) {
		super.save(cmsArticleComment);
	}
	
	@Transactional(readOnly = false)
	public void delete(CmsArticleComment cmsArticleComment) {
		super.delete(cmsArticleComment);
	}

	@Override
	public Page<CmsArticleComment> findPage(CmsArticleComment comment,
			Integer pageNo, Integer pageSize) {
		
		Page<CmsArticleComment> page = new Page<CmsArticleComment>(pageNo == null?1:pageNo, pageSize == null?5:pageSize);	
		comment.setPage(page);
		List<CmsArticleComment> comments = findList(comment);
		page.setList(comments);
		return page;
	}

	@Override
	@Transactional(readOnly = false)
	public void addLikes(String cid) {
		
		articleCommentDao.addLikes(cid);
	}
	
}