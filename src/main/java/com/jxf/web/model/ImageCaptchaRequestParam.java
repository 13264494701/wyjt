package com.jxf.web.model;

/***
 * 
 * @author wo
 *
 */
public class ImageCaptchaRequestParam {

	/**图形验证码索引*/
	private String captchaId;
	
	/**时间戳*/
	private String timestamp;
	

	public String getCaptchaId() {
		return captchaId;
	}
	public void setCaptchaId(String captchaId) {
		this.captchaId = captchaId;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}


}
