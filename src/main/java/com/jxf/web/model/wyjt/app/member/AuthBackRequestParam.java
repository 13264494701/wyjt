package com.jxf.web.model.wyjt.app.member;

public class AuthBackRequestParam {

	private String phoneNo;
	
	private Integer type;//芝麻分 0 运营商 1 淘宝 2 学信网 3 社保 4 公积金 5 网银 6

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	
	
}
