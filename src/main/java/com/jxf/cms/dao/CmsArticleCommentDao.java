package com.jxf.cms.dao;

import org.apache.ibatis.annotations.Param;

import com.jxf.cms.entity.CmsArticleComment;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;


/**
 * 文章评论DAO接口
 * @author JINXINFU
 * @version 2017-01-01
 */
@MyBatisDao
public interface CmsArticleCommentDao extends CrudDao<CmsArticleComment> {
	
	void addLikes(@Param("cid")String cid);
}