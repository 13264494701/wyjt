package com.jxf.cms.entity;

import org.hibernate.validator.constraints.Length;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 广告Entity
 * @author HUOJIABAO
 * @version 2016-06-21
 */
public class CmsAd extends CrudEntity<CmsAd> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 广告类型
	 */
	public enum Type {

		/** 图片 */
		image,

		/** 文本 */
	    text
	}
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
        outer,
        /**
         * 原生页面
         */
        original,
        /**
         * 第三方链接
         */
        link
        
    }
	private String positionNo;		// 广告位置	
	private String title;		// 广告标题
	private Type type;		// 广告类型
	private String imagePath;		// 图片路径
	private String textContent;		// 文本内容
	private Integer sort;	// 展示顺序
	private Boolean isEnabled;  //是否启用
	/** 跳转类型*/
	private RedirectType redirectType;

	
	public CmsAd() {
		super();
	}

	public CmsAd(Long id){
		super(id);
	}

	@Length(min=1, max=4, message="广告位置长度必须介于 1 和 4 之间")
	public String getPositionNo() {
		return positionNo;
	}

	public void setPositionNo(String positionNo) {
		this.positionNo = positionNo;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	


	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}


	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public RedirectType getRedirectType() {
		return redirectType;
	}

	public void setRedirectType(RedirectType redirectType) {
		this.redirectType = redirectType;
	}





}