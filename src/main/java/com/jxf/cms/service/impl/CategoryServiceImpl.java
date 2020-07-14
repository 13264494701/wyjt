package com.jxf.cms.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jxf.cms.dao.CategoryDao;
import com.jxf.cms.entity.Category;
import com.jxf.cms.service.CategoryService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.tree.service.impl.TreeServiceImpl;
import com.jxf.svc.sys.user.entity.User;
import com.jxf.svc.sys.util.UserUtils;
import com.jxf.svc.utils.StringUtils;

/**
 * 栏目Service
 * @author jxf
 * @version 1.0 2015-07-10
 */
@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl extends TreeServiceImpl<CategoryDao, Category> implements CategoryService  {

	@Autowired
	private CategoryDao categoryDao;
	
	public static final String CACHE_CATEGORY_LIST = "categoryList";
	
	private Category category = new Category();
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Category> findByUser(){
		
		List<Category> list = (List<Category>)UserUtils.getCache(CACHE_CATEGORY_LIST);
		if (list == null){
			User user = UserUtils.getUser();
			Category category = new Category();
			category.setParent(new Category());
			category.setCreateBy(user);
			list = dao.findList(category);
			// 将没有父节点的节点，找到父节点
			Set<Long> parentIdSet = Sets.newHashSet();
			for (Category e : list){
				if (e.getParent()!=null && e.getParent().getId()!=null){
					boolean isExistParent = false;
					for (Category e2 : list){
						if (e.getParent().getId().equals(e2.getId())){
							isExistParent = true;
							break;
						}
					}
					if (!isExistParent){
						parentIdSet.add(e.getParent().getId());
					}
				}
			}

			UserUtils.putCache(CACHE_CATEGORY_LIST, list);
		}

		return list;
	}

	@Override
	public List<Category> findByParentId(Long parentId){
		Category parent = new Category();
		parent.setId(parentId);
		category.setParent(parent);
		return dao.findByParentId(category);
	}
	
	@Override
	public Page<Category> find(Page<Category> page, Category category) {

		category.setPage(page);
		page.setList(dao.findList(category));
		return page;
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(Category category) {

		super.save(category);
		UserUtils.removeCache(CACHE_CATEGORY_LIST);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(Category category) {
		super.delete(category);
		UserUtils.removeCache(CACHE_CATEGORY_LIST);
	}
	
	/**
	 * 通过编号获取栏目列表
	 */
	public List<Category> findByIds(String ids) {
		List<Category> list = Lists.newArrayList();
		Long[] idss = StringUtils.splitToLong(ids);
		if (idss.length>0){
//			List<Category> l = dao.findByIdIn(idss);
//			for (Long id : idss){
//				for (Category e : l){
//					if (e.getId().equals(id)){
//						list.add(e);
//						break;
//					}
//				}
//			}
			for(Long id : idss){
				Category e = dao.get(id);
				if(null != e){
					//System.out.println("e.id:"+e.getId()+",e.name:"+e.getName());
					list.add(e);
				}
				//list.add(dao.get(id));
				
			}
		}
		return list;
	}
	
	@Override
	public Category getByAlias(String alias) {
		
		return categoryDao.getByAlias(alias);
	}

	@Override
	public List<Category> findRoots(Integer count, boolean useCache) {
		
		return null;
	}

	@Override
	public List<Category> findChildren(Long categoryId, boolean recursive,
			Integer count, boolean useCache) {
		
		return null;
	}

	@Override
	public List<Category> findParents(Long categoryId, boolean recursive,
			Integer count, boolean useCache) {
		
		return null;
	}

	@Override
	public List<Category> findTree() {
	
		return null;
	}
	
}
