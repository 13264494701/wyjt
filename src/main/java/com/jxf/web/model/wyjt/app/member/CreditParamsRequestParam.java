package com.jxf.web.model.wyjt.app.member;

public class CreditParamsRequestParam {

	private int authenType;//1->h5 2->sdk
	
	private int thirdApplyType;//第三方类型 1天机2公信宝
	
	private int type;//2 淘宝 3运营商 4芝麻分 5学信网 6 社保 7公积金  8网银

	public int getAuthenType() {
		return authenType;
	}

	public void setAuthenType(int authenType) {
		this.authenType = authenType;
	}

	public int getThirdApplyType() {
		return thirdApplyType;
	}

	public void setThirdApplyType(int thirdApplyType) {
		this.thirdApplyType = thirdApplyType;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
