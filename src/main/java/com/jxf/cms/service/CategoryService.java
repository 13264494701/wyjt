package com.jxf.cms.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.cms.entity.Category;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;


/**
 * 栏目Service
 * @author jxf
 * @version 1.0 2015-07-10
 */
@Service
@Transactional(readOnly = true)
public interface CategoryService extends  CrudService<Category> {


	
	public List<Category> findByUser();
	
	public List<Category> findByParentId(Long parentId);
	
	public Page<Category> find(Page<Category> page, Category category);
	
	
	/**
	 * 查找根节点
	 * 
	 * @param categoryId
	 *           栏目ID
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @param useCache
	 *            是否使用缓存
	 * @return 下级文章分类
	 */
	List<Category> findRoots(Integer count, boolean useCache);
	
	/**
	 * 查找下级文章栏目
	 * 
	 * @param articleCategoryId
	 *            文章分类ID
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @param useCache
	 *            是否使用缓存
	 * @return 下级文章分类
	 */
	List<Category> findChildren(Long categoryId, boolean recursive, Integer count, boolean useCache);


    List<Category> findParents(Long categoryId, boolean recursive, Integer count, boolean useCache);


	List<Category> findTree();
	
	public Category getByAlias(String alias);
	
}
