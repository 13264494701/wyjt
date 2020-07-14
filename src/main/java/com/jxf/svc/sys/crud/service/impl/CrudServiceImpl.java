package com.jxf.svc.sys.crud.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.base.service.BaseService;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.svc.sys.crud.entity.CrudEntity;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * Service基类
 * @author jxf
 * @version 1.0 2015-07-10
 */
@Transactional(readOnly = true)
public abstract class CrudServiceImpl<D extends CrudDao<T>, T extends CrudEntity<T>> extends BaseService implements CrudService<T>{
	
	/**
	 * 持久层对象
	 */
	@Autowired
	protected D dao;
	
	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public T get(Long id) {
		return dao.get(id);
	}
	
	/**
	 * 获取单条数据
	 * @param entity
	 * @return
	 */
	public T get(T entity) {
		return dao.get(entity);
	}
	
	/**
	 * 查询列表数据
	 * @param entity
	 * @return
	 */
	public List<T> findList(T entity) {
		return dao.findList(entity);
	}
	
	/**
	 * 查询列表数据
	 * @param entity
	 * @return
	 */
	public List<T> findAllList(T entity) {
		return dao.findAllList(entity);
	}
	
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<T> findPage(Page<T> page, T entity) {
		entity.setPage(page);
		page.setList(dao.findList(entity));
		return page;
	}

	/**
	 * 保存数据（插入或更新）
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void save(T entity) {
		if (entity.getIsNewRecord()){
			entity.preInsert();
			dao.insert(entity);
		}else{
			entity.preUpdate();
			dao.update(entity);
		}
	}
	/**
	 * 修改数据
	 */
	@Transactional(readOnly = false)
	public void update(T entity){
		entity.preUpdate();
		dao.update(entity);
	}
	/**
	 * 删除数据
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void delete(T entity) {
		entity.preUpdate();
		dao.delete(entity);
	}

}
