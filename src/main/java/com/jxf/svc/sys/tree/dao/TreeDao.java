package com.jxf.svc.sys.tree.dao;

import java.util.List;

import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.svc.sys.tree.entity.TreeEntity;

/**
 * DAO支持类实现
 * @author jxf
 * @version 2015-07-16
 * @param <T>
 */
public interface TreeDao<T extends TreeEntity<T>> extends CrudDao<T> {

	/**
	 * 找到所有子节点
	 * @param entity
	 * @return
	 */
	public List<T> findByParentIdsLike(T entity);

	/**
	 * 更新所有父节点字段
	 * @param entity
	 * @return
	 */
	public int updateParentIds(T entity);
	
}