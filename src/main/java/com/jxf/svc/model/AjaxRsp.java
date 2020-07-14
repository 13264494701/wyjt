package com.jxf.svc.model;
/**
 * ajax 传输返回结果
 * @author zhuhuijie
 *
 */
public class AjaxRsp {
	private boolean status = false;//状态
	private String code;    // 状态码
	private String message; // 提示信息
	private Object result;  //结果
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
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
	
}
