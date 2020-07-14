package com.jxf.web.model;



public class ResponseData {

	/** 返回代码 */
	private  int code;
	/** 返回信息 */
	private  String message;
	/** 结果对象 */
	private Object result;
	
	/** 登录设备 */
	private String loginDevice;
	
	/** 登录IP */
	private String loginIp;
	
	/** 登录时间 */
	private String loginTime;

	public ResponseData() {
		  super();
		  this.code = 0;
		  this.result = new Object();
	}
	public ResponseData(int code, String message) {
		  this.code = code;
	      this.message = message;
	      this.result = new Object();
	}
	
	public ResponseData(int code, String message, Object result) {
		  this.code = code;
	      this.message = message;
	      this.result = result;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	
	public static ResponseData success(String message) {
        return new ResponseData(0, message);
    }
	public static ResponseData success(String message,Object result) {

		return new ResponseData(0, message,result);
	}

	public static ResponseData warn(String message) {
		return new ResponseData(-2,message);
	}
	public static ResponseData error(String message) {
		return new ResponseData(-1, message);
	}
	
	public static ResponseData unauthorized() {
		return new ResponseData(100, "尚未登录,请先登录");
	}
	
	public static ResponseData againChange(String message) {
		return new ResponseData(200, message);
	}
	
	public static ResponseData otherDeviceLogin(String message,Object result) {
		return new ResponseData(101, message,result);
	}
	
	public static ResponseData forbidden() {
		return new ResponseData(403, "被禁止");
	}
	
	public static ResponseData serverInternalError() {
		return new ResponseData(500, "系统错误");
	}
	
	
	public String getLoginDevice() {
		return loginDevice;
	}

	public void setLoginDevice(String loginDevice) {
		this.loginDevice = loginDevice;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}



}
