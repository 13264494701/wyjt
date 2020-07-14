package com.jxf.rc.entity;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 信用档案运营商通话与花费详情Entity
 * @author lmy
 * @version 2018-12-17
 */
public class RcCaYysDetails extends CrudEntity<RcCaYysDetails> {
	
	private static final long serialVersionUID = 1L;
			/** member */
	private Long memberId;		
			/** 内容 */
	private String content;		
	
	public RcCaYysDetails() {
		super();
	}

	public RcCaYysDetails(Long id){
		super(id);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	
}