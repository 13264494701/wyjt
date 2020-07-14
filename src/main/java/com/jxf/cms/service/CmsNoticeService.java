package com.jxf.cms.service;


import com.jxf.cms.entity.CmsNotice;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 通知Service
 * @author wo
 * @version 2018-10-04
 */
public interface CmsNoticeService extends CrudService<CmsNotice> {

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
	
	
	Page<CmsNotice> findNoticePage(CmsNotice notice, Integer pageNo, Integer pageSize);
}