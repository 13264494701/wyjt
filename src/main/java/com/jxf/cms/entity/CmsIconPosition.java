package com.jxf.cms.entity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 图标位置Entity
 * @author HUOJIABAO
 * @version 2016-07-23
 */
public class CmsIconPosition extends CrudEntity<CmsIconPosition> {
	
	private static final long serialVersionUID = 1L;
	private String positionNo;		// 图标位编号
	private String positionName;		// 图标位名称
	private Integer height;		// 高度
	private Integer width;		// 宽度
	private String template;		// 模板
	private String description;		// 描述
	
	public CmsIconPosition() {
		super();
	}

	public CmsIconPosition(Long id){
		super(id);
	}

	@Length(min=1, max=4, message="图标位编号长度必须介于 1 和 4 之间")
	public String getPositionNo() {
		return positionNo;
	}

	public void setPositionNo(String positionNo) {
		this.positionNo = positionNo;
	}
	
	@Length(min=1, max=64, message="图标位名称长度必须介于 1 和 64 之间")
	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	
	@NotNull(message="高度不能为空")
	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}
	
	@NotNull(message="宽度不能为空")
	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
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