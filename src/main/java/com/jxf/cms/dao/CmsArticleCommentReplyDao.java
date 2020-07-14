package com.jxf.cms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.cms.entity.CmsArticleCommentReply;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;


/**
 * 评论回复DAO接口
 * @author JINXINFU
 * @version 2017-01-05
 */
@MyBatisDao
public interface CmsArticleCommentReplyDao extends CrudDao<CmsArticleCommentReply> {
	
	List<CmsArticleCommentReply> findByCommentId(@Param("commentId")String commentId);
}