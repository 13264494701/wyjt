package com.jxf.cms.entity;

import org.hibernate.validator.constraints.Length;

import java.util.List;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 广告位置Entity
 * @author JINXINFU
 * @version 2016-04-25
 */
public class CmsAdPosition extends CrudEntity<CmsAdPosition> {
	
	private static final long serialVersionUID = 1L;
	private String positionNo;	// 广告位编号
	private String positionName;		// 名称
	private String height;		// 高度
	private String width;		// 宽度
	private String template;		// 模板
	private String description;		// 描述

	/** 广告 */
	private CmsAd ad;
	private List<CmsAd> ads;
	
	public CmsAd getAd() {
		return ad;
	}

	public void setAd(CmsAd ad) {
		this.ad = ad;
	}
	public List<CmsAd> getAds() {
		return ads;
	}

	public void setAds(List<CmsAd> ads) {
		this.ads = ads;
	}

	public CmsAdPosition() {
		super();
	}

	public CmsAdPosition(Long id){
		super(id);
	}
	
	@Length(min=1, max=4, message="编号长度必须介于 1 和 4 之间")
	public String getPositionNo() {
		return positionNo;
	}

	public void setPositionNo(String positionNo) {
		this.positionNo = positionNo;
	}	
	
	@Length(min=1, max=64, message="名称长度必须介于 1 和 64 之间")
	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	
	@Length(min=1, max=11, message="高度长度必须介于 1 和 11 之间")
	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}
	
	@Length(min=1, max=11, message="宽度长度必须介于 1 和 11 之间")
	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}
	
	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
	
	@Length(min=0, max=255, message="描述长度必须介于 0 和 255 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}