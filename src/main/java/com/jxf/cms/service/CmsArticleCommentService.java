package com.jxf.cms.service;

import com.jxf.cms.entity.CmsArticleComment;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;


/**
 * 文章评论Service
 * @author JINXINFU
 * @version 2017-01-01
 */
public interface CmsArticleCommentService extends CrudService<CmsArticleComment> {

	Page<CmsArticleComment> findPage(CmsArticleComment comment, Integer pageNo, Integer pageSize);
	
	void addLikes(String cid);
	
}