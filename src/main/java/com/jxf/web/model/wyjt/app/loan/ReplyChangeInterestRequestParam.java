package com.jxf.web.model.wyjt.app.loan;

/***
 *  同意/拒绝修改利息
 * @author gaobo	
 *
 */
public class ReplyChangeInterestRequestParam {

	/** 借款Id */
	private String applyId;
	
	/** 类型 */
	private Integer type;// 0->同意 , 1->拒绝

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	} 
	
	
	
	
	
	
	
	
	
	
}
