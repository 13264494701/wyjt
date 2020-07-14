package com.jxf.web.model.wyjt.app.member;

public class SingleCreditReportRequestParam {
	
	private String userId;
    /**
     * type 1信用记录 2 淘宝 3运营商 4芝麻分 5学信网 6 社保 7公积金 8网银
     */
	private int type;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	

}
