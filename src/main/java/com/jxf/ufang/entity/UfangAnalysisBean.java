package com.jxf.ufang.entity;

import java.util.Date;

/**
 * 借贷分析 
 * @author Administrator
 *
 */
public class UfangAnalysisBean {

	
	private Long id;
	
	/** 查询 id*/
	private Long userId;
	
	/** 好友 id*/
	private Long memberId;
	
	private Object content;
	
	private Date createTime;

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	
	
	
}
