package com.jxf.cms.entity;


import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 文章内容Entity
 * @author JINXINFU
 * @version 2016-11-25
 */
public class CmsArticleContent extends CrudEntity<CmsArticleContent> {
	
	private static final long serialVersionUID = 1L;
	private String content;		// 文章内容
	
	public CmsArticleContent() {
		super();
	}

	public CmsArticleContent(Long id){
		super(id);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}