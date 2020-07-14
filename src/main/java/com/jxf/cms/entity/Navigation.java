package com.jxf.cms.entity;

import org.hibernate.validator.constraints.Length;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 导航Entity
 * @author huojiayuan
 * @version 2016-06-09
 */
public class Navigation extends CrudEntity<Navigation> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String position;		// 位置
	private String displayOrder;		// 展示排序
	private String url;		// 链接地址
	private String isBlankTarget;		// 是否新窗口打开
	
	public Navigation() {
		super();
	}

	public Navigation(Long id){
		super(id);
	}

	@Length(min=1, max=64, message="名称长度必须介于 1 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=1, max=3, message="位置长度必须介于 1 和 3之间")
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
	@Length(min=0, max=11, message="展示排序长度必须介于 0 和 11 之间")
	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	@Length(min=1, max=255, message="链接地址长度必须介于 1 和 255 之间")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Length(min=1, max=1, message="是否新窗口打开长度必须介于 1 和 1 之间")
	public String getIsBlankTarget() {
		return isBlankTarget;
	}

	public void setIsBlankTarget(String isBlankTarget) {
		this.isBlankTarget = isBlankTarget;
	}
	
}