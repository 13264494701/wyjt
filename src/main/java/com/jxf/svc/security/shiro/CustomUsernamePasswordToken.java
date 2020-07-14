package com.jxf.svc.security.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 用户和密码（包含验证码）令牌类
 * @author jxf
 * @version 2015-07-28
 */
public class CustomUsernamePasswordToken extends UsernamePasswordToken {

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
	/**验证码*/
	private String captcha;
	/**用户类型*/
	private UserType userType;
	
	public CustomUsernamePasswordToken() {
		super();
	}

	public CustomUsernamePasswordToken(String username, char[] password,
			boolean rememberMe, String host, String captcha, UserType userType) {
		super(username, password, rememberMe, host);
		this.captcha = captcha;
		this.userType = userType;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}


	
}