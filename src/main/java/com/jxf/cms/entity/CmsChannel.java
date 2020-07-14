package com.jxf.cms.entity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 频道信息Entity
 * @author JINXINFU
 * @version 2016-11-20
 */
public class CmsChannel extends CrudEntity<CmsChannel> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 频道名称
	private String alias; // 别名
	private String url;		// 频道链接
	private String keywords;		// 关键字
	private String description;		// 描述
	private String sort;		// 排序（升序）
	private Boolean inNav;		// 是否在导航中显示
	private Boolean allowComment;		// 是否允许评论
	private Boolean isAudit;		// 是否需要审核
	
	private Integer count;
	

	/** 路径前缀 */
	private static final String PATH_PREFIX = "channel";

	/** 路径后缀 */
	private static final String PATH_SUFFIX = ".jhtml";
	
	public CmsChannel() {
		super();
	}

	public CmsChannel(Long id){
		super(id);
	}
	public CmsChannel(Integer count){
		super();
		this.count = count;
	}
	
	@Length(min=1, max=64, message="频道名称长度必须介于 1 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	@Length(min=0, max=255, message="频道链接长度必须介于 0 和 255 之间")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Length(min=0, max=255, message="关键字长度必须介于 0 和 255 之间")
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	@Length(min=0, max=255, message="描述长度必须介于 0 和 255 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Length(min=0, max=11, message="排序（升序）长度必须介于 0 和 11 之间")
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	@NotNull(message="是否在导航中显示不能为空")
	public Boolean getInNav() {
		return inNav;
	}

	public void setInNav(Boolean inNav) {
		this.inNav = inNav;
	}
	
	@NotNull(message="是否允许评论不能为空")
	public Boolean getAllowComment() {
		return allowComment;
	}

	public void setAllowComment(Boolean allowComment) {
		this.allowComment = allowComment;
	}
	
	@NotNull(message="是否需要审核不能为空")
	public Boolean getIsAudit() {
		return isAudit;
	}

	public void setIsAudit(Boolean isAudit) {
		this.isAudit = isAudit;
	}
	
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	/**
	 * 获取路径
	 * 
	 * @return 路径
	 */
	public String getPath() {
		return getAlias() != null ? PATH_PREFIX + "/" + getAlias() + PATH_SUFFIX : null;
	}





}