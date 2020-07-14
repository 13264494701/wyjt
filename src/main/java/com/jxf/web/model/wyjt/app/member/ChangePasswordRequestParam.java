package com.jxf.web.model.wyjt.app.member;


public class ChangePasswordRequestParam   {
	

	/**旧密码*/
	private String oldPassword;
	/**新密码 */
	private String password;
	
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
