package com.jxf.cms.service;

import java.util.List;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.cms.entity.CmsArticle;

/**
 * 文章Service
 * @author JINXINFU
 * @version 2016-11-25
 */
public interface CmsArticleService extends CrudService<CmsArticle> {

	void setStatic(Long id);
	/**
	 * 文章发布
	 * @param cmsArticle
	 */
	void pubArticle(CmsArticle cmsArticle);

	/**
	 * 撤回发布
	 * @param cmsArticle
	 */
	void unpubArticle(CmsArticle cmsArticle);
	/**
	 * 设置置顶
	 * @param cmsArticle
	 */
	void topArticle(CmsArticle cmsArticle);
	/**
	 * 
	 * @param aid
	 */
	void addLikes(String aid);
	/**
	 * 
	 * @param aid
	 */
	void disLike(String aid);
	/**
	 * 
	 * @param dataOriginal
	 */
	CmsArticle getByOriginal(String dataOriginal);
	
	/***
	 * 
	 * 函数功能说明              分页获取文章列表
	 * Administrator  2016年11月28日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param cmsArticle
	 * @参数： @param pageNo
	 * @参数： @param pageSize
	 * @参数： @return     
	 * @return Page<CmsArticle>    
	 * @throws
	 */
	Page<CmsArticle> findPage(CmsArticle cmsArticle, Integer pageNo, Integer pageSize);
	/***
	 * 
	 * 函数功能说明   随机获取文章
	 * JINXINFU  2017年1月16日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param cmsArticle
	 * @参数： @return     
	 * @return List<CmsArticle>    
	 * @throws
	 */
	List<CmsArticle> findListByRand(CmsArticle cmsArticle);
}