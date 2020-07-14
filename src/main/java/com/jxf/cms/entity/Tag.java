package com.jxf.cms.entity;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 标签Entity
 * @author JINXINFU
 * @version 2016-04-26
 */
public class Tag extends CrudEntity<Tag> {
	
	private static final long serialVersionUID = 1L;
	private String tagName;		// 标签名称
	private String tagCode;		// 标签代码
	private String tagType;		// 标签类型
	private String displayOrder;		// 展示顺序
	private String icon;		// 图标

	
	
	public Tag() {
		super();
	}

	public Tag(Long id){
		super(id);
	}

	@Length(min=1, max=64, message="标签名称长度必须介于 1 和 64 之间")
	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	@Length(min=1, max=16, message="标签代码长度必须介于 1 和 16之间")
	public String getTagCode() {
		return tagCode;
	}

	public void setTagCode(String tagCode) {
		this.tagCode = tagCode;
	}
	@Length(min=1, max=11, message="标签类型长度必须介于 1 和 11 之间")
	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}
	
	@Length(min=0, max=11, message="展示顺序长度必须介于 0 和 11 之间")
	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	@Length(min=0, max=255, message="图标长度必须介于 0 和 255 之间")
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
}