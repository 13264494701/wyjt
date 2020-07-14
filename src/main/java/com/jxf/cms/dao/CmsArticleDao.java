package com.jxf.cms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.cms.entity.CmsArticle;

/**
 * 文章DAO接口
 * @author JINXINFU
 * @version 2016-11-25
 */
@MyBatisDao
public interface CmsArticleDao extends CrudDao<CmsArticle> {
	
	/**
	 * 查找文章列表
	 * 
	 * @param first
	 *            起始记录
	 * @param count
	 *            数量
	 * @return 实体对象集合
	 */
	public List<CmsArticle> findListByLimit(Integer first, Integer count);
	/**
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
	
	void setStatic(Long id);
	/**
	 * 发布文章
	 * @param id
	 */
	void pubArticle(Long id);
	/**
	 * 撤回文章
	 * @param id
	 */
	void unpubArticle(Long id);
	
	/**
	 * 设置置顶
	 * @param cmsArticle
	 */
	void topArticle(CmsArticle cmsArticle);
	
	
	void addLikes(@Param("aid")String aid);
	
	void disLike(@Param("aid")String aid);
	
	CmsArticle getByOriginal(String dataOriginal);
	
	List<CmsArticle> findMemberFavoriteArticleList(@Param("memberNo")String memberNo);
}