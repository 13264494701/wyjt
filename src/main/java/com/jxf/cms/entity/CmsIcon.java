package com.jxf.cms.entity;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 图标Entity
 * @author HUOJIABAO
 * @version 2016-07-23
 */
public class CmsIcon extends CrudEntity<CmsIcon> {
	
	private static final long serialVersionUID = 1L;
	
    /**
     * 跳转方式
     */
    public enum RedirectType {
        /**
         * 内部
         */
        inner,
        /**
         * 外部
         */
        outer
    }
    
	private String positionNo;		// 图标位置
	private String iconName;		// 图标名称
	private String imagePath;		// 图片路径
	
	/** 跳转类型*/
	private RedirectType redirectType;

	
	public CmsIcon() {
		super();
	}

	public CmsIcon(Long id){
		super(id);
	}

	@Length(min=1, max=4, message="图标位置长度必须介于 1 和 4 之间")
	public String getPositionNo() {
		return positionNo;
	}

	public void setPositionNo(String positionNo) {
		this.positionNo = positionNo;
	}
	
	@Length(min=1, max=64, message="图标名称长度必须介于 1 和 64 之间")
	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
	
	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public RedirectType getRedirectType() {
		return redirectType;
	}

	public void setRedirectType(RedirectType redirectType) {
		this.redirectType = redirectType;
	}
	
}