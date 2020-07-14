package com.jxf.cms.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.cms.entity.CmsArticleContent;

/**
 * 文章内容DAO接口
 * @author JINXINFU
 * @version 2016-11-25
 */
@MyBatisDao
public interface CmsArticleContentDao extends CrudDao<CmsArticleContent> {
	
}