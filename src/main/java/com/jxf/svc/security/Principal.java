package com.jxf.svc.security;

import java.io.Serializable;

import com.jxf.svc.sys.util.UserUtils;


/**
 * 授权用户信息
 */
public class Principal implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 用户类型
	 */
	public enum UserType {

		/** 平台管理员 */
		admin,

		/** 优放 */
		ufang
	}
	
	/**用户ID*/
	private Long id;
	/**用户登录名*/
	private String loginName;
	/**用户类型*/
	private UserType userType;
	
	public Principal(Long id, String loginName) {
		this.id = id;
		this.loginName = loginName;
	}
	
	public Principal(Long id, String loginName,UserType userType) {
		this.id = id;
		this.loginName = loginName;
		this.userType = userType;
	}

	public Long getId() {
		return id;
	}

	public String getLoginName() {
		return loginName;
	}
	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	/**
	 * 获取SESSIONID
	 */
	public String getSessionid() {
		try{
			return (String) UserUtils.getSession().getId();
		}catch (Exception e) {
			return "";
		}
	}
	
	@Override
	public String toString() {
		return id+"";
	}


}
