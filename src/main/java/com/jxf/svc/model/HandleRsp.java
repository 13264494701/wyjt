package com.jxf.svc.model;


/***
 * 
 * @类功能说明： 调用处理返回结果
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：wo 
 * @创建时间：2016年11月17日 上午11:26:12 
 * @版本：V1.0
 */
public class HandleRsp {
	private boolean status = false;//状态
	private int code;    // 状态码
	private String message; // 提示信息
	private Object result;  //结果
	
	public HandleRsp() {
		  super();
		  this.status = true;
		  this.result = new Object();
	}
	public HandleRsp(boolean status, String message) {
		  this.status = status;
	      this.message = message;
	      this.result = new Object();
	}
	public HandleRsp(boolean status,int code, String message) {
		  this.status = status;
		  this.code = code;
	      this.message = message;
	      this.result = new Object();
	}
	public HandleRsp(boolean status,int code,  String message, Object result) {
		  this.status = status;
		  this.code = code;
	      this.message = message;
	      this.result = result;
	}
	
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
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
	
	public static HandleRsp success(String message) {
        return new HandleRsp(true, message);
    }
	public static HandleRsp success(String message,Object result) {

		return new HandleRsp(true, 0, message,result);
	}

	public static HandleRsp fail(String message) {
		return new HandleRsp(false,-1, message);
	}
	
	public static HandleRsp fail(int code,String message) {
		return new HandleRsp(false,code, message);
	}
	
}
