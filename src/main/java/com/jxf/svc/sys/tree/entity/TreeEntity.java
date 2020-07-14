package com.jxf.svc.sys.tree.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.alibaba.fastjson.annotation.JSONField;
import com.jxf.svc.converter.LongToStringSerializer;
import com.jxf.svc.sys.crud.entity.CrudEntity;
import com.jxf.svc.utils.Reflections;


/**
 * 数据Entity类
 * @author jxf
 * @version 2015-07-16
 */
public abstract class TreeEntity<T> extends CrudEntity<T> {

	@JSONField(serialize = false)
	private static final long serialVersionUID = 1L;

	protected T parent;	// 父级编号
	protected String parentIds; // 所有父级编号
	protected String name; 	// 机构名称
	protected Integer sort;		// 排序
	
	public TreeEntity() {
		super();
		this.sort = 30;
	}
	
	public TreeEntity(Long id) {
		super(id);
	}
	
	/**
	 * 父对象，只能通过子类实现，父类实现mybatis无法读取
	 * @return
	 */
	@NotNull
	public abstract T getParent();

	/**
	 * 父对象，只能通过子类实现，父类实现mybatis无法读取
	 * @return
	 */
	public abstract void setParent(T parent);

	@Length(min=1, max=2000)
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	@Length(min=1, max=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	@JSONField(serializeUsing = LongToStringSerializer.class)
	public Long getParentId() {
		Long id = null;
		if (parent != null){
			id = (Long) Reflections.getFieldValue(parent, "id");
		}
		return id!=null ? id : 0L;
	}
	
}
