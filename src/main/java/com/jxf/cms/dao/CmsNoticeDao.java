package com.jxf.cms.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.cms.entity.CmsNotice;


/**
 * 通知DAO接口
 * @author wo
 * @version 2018-10-04
 */
@MyBatisDao
public interface CmsNoticeDao extends CrudDao<CmsNotice> {
	
	/**
	 * 发布通知
	 * @param id
	 */
	void pub(String id);
	/**
	 * 撤回通知
	 * @param id
	 */
	void unpub(String id);
}