package com.jxf.svc.sys.crud.service;

import java.util.List;
import com.jxf.svc.persistence.Page;

public interface CrudService<T> {
	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public T get(Long id);
	
	/**
	 * 获取单条数据
	 * @param entity
	 * @return
	 */
	public T get(T entity);
	/**
	 * 查询列表数据
	 * @param entity
	 * @return
	 */
	public List<T> findList(T entity);
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<T> findPage(Page<T> page, T entity);
	/**
	 * 保存数据（插入或更新）
	 * @param entity
	 */
	public void save(T entity);
	/**
	 * 修改数据
	 */
	public void update(T entity);
	/**
	 * 删除数据
	 * @param entity
	 */
	public void delete(T entity);
}
