package com.jxf.cms.entity;

import org.hibernate.validator.constraints.Length;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * SEOEntity
 * @author huojiayuan
 * @version 2016-06-13
 */
public class Seo extends CrudEntity<Seo> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 类型
	 */
	public enum Type {

		/** 首页 */
		index,

		/** 文章列表 */
		articleList,

		/** 文章搜索 */
		articleSearch,

		/** 文章内容 */
		articleContent,

		/** 商品列表 */
		goodsList,

		/** 商品搜索 */
		goodsSearch,

		/** 商品内容 */
		goodsContent,

		/** 品牌列表 */
		brandList,

		/** 品牌内容 */
		brandContent
	}

	private String name;		// 页面名称
	private Type type;		// 页面类型
	private String title;		// 页面标题
	private String keywords;		// 页面关键词
	private String description;		// 页面描述
	
	public Seo() {
		super();
	}

	public Seo(Long id){
		super(id);
	}
	
	@Length(min=1, max=64, message="页面类型长度必须介于 1 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	@Length(min=0, max=255, message="页面标题长度必须介于 0 和 255 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=0, max=255, message="页面关键词长度必须介于 0 和 255 之间")
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	@Length(min=0, max=255, message="页面描述长度必须介于 0 和 255 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}