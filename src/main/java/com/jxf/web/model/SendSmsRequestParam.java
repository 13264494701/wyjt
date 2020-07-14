package com.jxf.web.model;

/***
 * 
 * @author wo
 *
 */
public class SendSmsRequestParam {


	/**短信模板代码
		
		wyjtAppRegister, 无忧借条APP注册		 
		wyjtAppFindPasswd,无忧借条APP找回密码 	 
		wyjtAppChangePasswd,无忧借条APP修改密码 
		wyjtAppChangePhoneNo,无忧借条APP修改手机号码
	*/
	private String tmplCode;//前端传递对应枚举字符串即可
	/**手机号码*/
	private String phoneNo;
	/**图形验证码*/
	private String imageCaptcha;
	/**图形验证码索引*/
	private String captchaId;
	
	public String getTmplCode() {
		return tmplCode;
	}
	public void setTmplCode(String tmplCode) {
		this.tmplCode = tmplCode;
	}
	
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getImageCaptcha() {
		return imageCaptcha;
	}
	public void setImageCaptcha(String imageCaptcha) {
		this.imageCaptcha = imageCaptcha;
	}
	public String getCaptchaId() {
		return captchaId;
	}
	public void setCaptchaId(String captchaId) {
		this.captchaId = captchaId;
	}


}
