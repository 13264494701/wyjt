package com.jxf.cms.entity;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.jxf.svc.sys.tree.entity.TreeEntity;

/**
 * 栏目Entity
 * @author jxf
 * @version 1.0 2015-07-10
 */
public class Category extends TreeEntity<Category> {

    public static final String DEFAULT_TEMPLATE = "frontList";

	private static final long serialVersionUID = 1L;
   
	private String alias; // 别名
	
    private Date beginDate;	// 开始时间
    private Date endDate;	// 结束时间
    private String cnt;//信息量
    private String hits;//点击量
	
	private List<Category> childList = Lists.newArrayList(); 	// 拥有子分类列表

	public Category(){
		super();
		this.sort = 30;
		this.delFlag = DEL_FLAG_NORMAL;

	}

	public Category(Long id){
		this();
		this.id = id;
	}
	

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getHits() {
		return hits;
	}

	public void setHits(String hits) {
		this.hits = hits;
	}
	
	@Override
	public Category getParent() {
		return parent;
	}
	@Override
	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<Category> getChildList() {
		return childList;
	}

	public void setChildList(List<Category> childList) {
		this.childList = childList;
	}

	public String getCnt() {
		return cnt;
	}

	public void setCnt(String cnt) {
		this.cnt = cnt;
	}
	
	public static void sortList(List<Category> list, List<Category> sourcelist, Long parentId){
		for (int i=0; i<sourcelist.size(); i++){
			Category e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getId()!=null
					&& e.getParent().getId().equals(parentId)){
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j=0; j<sourcelist.size(); j++){
					Category child = sourcelist.get(j);
					if (child.getParent()!=null && child.getParent().getId()!=null
							&& child.getParent().getId().equals(e.getId())){
						sortList(list, sourcelist, e.getId());
						break;
					}
				}
			}
		}
	}
	
	public String getIds() {
		return (this.getParentIds() !=null ? this.getParentIds().replaceAll(",", " ") : "") 
				+ (this.getId() != null ? this.getId() : "");
	}

	public boolean isRoot(){
		return isRoot(this.id);
	}
	
	public static boolean isRoot(Long id){
		return id != null && id==1L;
	}




}