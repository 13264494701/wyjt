package com.jxf.cms.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 通知Entity
 * @author wo
 * @version 2018-10-04
 */
public class CmsNotice extends CrudEntity<CmsNotice> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 展示位置
	 */
	public enum Position {

		/** 首页跑马灯 */
		marquee,

		/** 消息中心 */
		message,
		
		/** 公信堂首页跑马灯 */
		gxtMarquee,
		
		/** 优放首页跑马灯 */
		ufangMarquee
	}
	
	/** 展示位置*/
	private Position position;	
	
	/** 通知标题 */
	private String title;		
	/** 图片路径 */
	private String image;		
	/** 文本内容 */
	private String content;		
	/** 链接地址 */
	private String url;		
	/** 展示顺序 */
	private Integer sort;	
	/** 是否发布 */
	private Boolean isPub;	
	/** 开始日期 */
	private Date beginDate;		
	/** 结束日期 */
	private Date endDate;	
	

	
	public CmsNotice() {
		super();
	}

	public CmsNotice(Long id){
		super(id);
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
	
	@Length(min=1, max=127, message="通知标题长度必须介于 1 和 127 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=0, max=255, message="图片路径长度必须介于 0 和 255 之间")
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=0, max=255, message="链接地址长度必须介于 0 和 255 之间")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getIsPub() {
		return isPub;
	}

	public void setIsPub(Boolean isPub) {
		this.isPub = isPub;
	}


	
	
}