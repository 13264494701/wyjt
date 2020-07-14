package com.jxf.cms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.cms.dao.CmsArticleCommentReplyDao;
import com.jxf.cms.entity.CmsArticleCommentReply;
import com.jxf.cms.service.CmsArticleCommentReplyService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 评论回复ServiceImpl
 * @author JINXINFU
 * @version 2017-01-05
 */
@Service("cmsArticleCommentReplyService")
@Transactional(readOnly = true)
public class CmsArticleCommentReplyServiceImpl extends CrudServiceImpl<CmsArticleCommentReplyDao, CmsArticleCommentReply> implements CmsArticleCommentReplyService{

	public CmsArticleCommentReply get(Long id) {
		return super.get(id);
	}
	
	public List<CmsArticleCommentReply> findList(CmsArticleCommentReply cmsArticleCommentReply) {
		return super.findList(cmsArticleCommentReply);
	}
	
	public Page<CmsArticleCommentReply> findPage(Page<CmsArticleCommentReply> page, CmsArticleCommentReply cmsArticleCommentReply) {
		return super.findPage(page, cmsArticleCommentReply);
	}
	
	@Transactional(readOnly = false)
	public void save(CmsArticleCommentReply cmsArticleCommentReply) {
		super.save(cmsArticleCommentReply);
	}
	
	@Transactional(readOnly = false)
	public void delete(CmsArticleCommentReply cmsArticleCommentReply) {
		super.delete(cmsArticleCommentReply);
	}
	
}