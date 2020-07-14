package com.jxf.ufang.entity;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.alibaba.fastjson.annotation.JSONField;
import com.jxf.svc.converter.LongToStringSerializer;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 菜单Entity
 * @author jxf
 * @version 2015-07-28
 */
public class UfangMenu extends CrudEntity<UfangMenu> {

	private static final long serialVersionUID = 1L;
	private UfangMenu parent;	// 父级菜单
	private String parentIds; // 所有父级编号
	private String name; 	// 名称
	private String href; 	// 链接
	private String target; 	// 目标（ mainFrame、_blank、_self、_parent、_top）
	private String icon; 	// 图标
	private Integer sort; 	// 排序
	private Boolean isShow; 	// 是否在菜单中显示（1：显示；0：不显示）
	private String permission; // 权限标识
	
	public UfangMenu(){
		super();
		this.sort = 30;
		this.isShow = true;
	}
	
	public UfangMenu(Long id){
		super(id);
	}
	
	@NotNull
	public UfangMenu getParent() {
		return parent;
	}

	public void setParent(UfangMenu parent) {
		this.parent = parent;
	}

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

	@Length(min=0, max=2000)
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	@Length(min=0, max=20)
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
	@Length(min=0, max=100)
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	@NotNull
	public Integer getSort() {
		return sort;
	}
	
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public Boolean getIsShow() {
		return isShow;
	}

	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}

	@Length(min=0, max=200)
	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	@JSONField(serializeUsing = LongToStringSerializer.class)
	public Long getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : 0L;
	}

	@JSONField(serialize = false)
	public static void sortList(List<UfangMenu> list, List<UfangMenu> sourcelist, Long parentId, boolean cascade){
		for (int i=0; i<sourcelist.size(); i++){
			UfangMenu e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getId()!=null
					&& e.getParent().getId().equals(parentId)){
				list.add(e);
				if (cascade){
					// 判断是否还有子节点, 有则继续获取子节点
					for (int j=0; j<sourcelist.size(); j++){
						UfangMenu child = sourcelist.get(j);
						if (child.getParent()!=null && child.getParent().getId()!=null
								&& child.getParent().getId().equals(e.getId())){
							sortList(list, sourcelist, e.getId(), true);
							break;
						}
					}
				}
			}
		}
	}

	@JSONField(serialize = false)
	public static Long getRootId(){
		return 1L;
	}

	@Override
	public String toString() {
		return name;
	}
}