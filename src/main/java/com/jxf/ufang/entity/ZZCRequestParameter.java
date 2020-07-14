package com.jxf.ufang.entity;
/**
 * @作者: xiaorongdian
 * @创建时间 :2019年3月21日 下午4:23:37
 * @功能说明:请求中智诚的参数
 */
public class ZZCRequestParameter {
	
	private String name ;
	private String pid ;
	private String mobile ;
	private String loan_type ;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getLoan_type() {
		return loan_type;
	}
	public void setLoan_type(String loan_type) {
		this.loan_type = loan_type;
	}
	
}
