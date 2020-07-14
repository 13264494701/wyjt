package com.jxf.web.model.wyjt.app.member;


/**
 * @作者: xiaorongdian
 * @创建时间 :2018年10月29日 下午4:55:34
 * @功能说明:
 */
public class AddMoreInfoRequestParam   {
	 
	/** 会员ID*/
	private Long memberId;
	
	/** 性别 0男 1女*/
	private Integer gender;
	 
	/** 昵称*/
	private String nickname;
	
	/** 邮箱*/
	private String email;
	
	/** 常用住址*/
	private String addr;
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
}
