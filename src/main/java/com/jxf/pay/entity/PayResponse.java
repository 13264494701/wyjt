package com.jxf.pay.entity;
/**
 * @类功能说明： 响应信息
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：zhuhuijie 
 * @创建时间：2016年6月22日 上午8:16:04 
 * @版本：V1.0
 */
public class PayResponse {
	private Boolean status = false;
	private String respCode;
	private String respMessage;
	private String paySts;
	
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespMessage() {
		return respMessage;
	}
	public void setRespMessage(String respMessage) {
		this.respMessage = respMessage;
	}
	public String getPaySts() {
		return paySts;
	}
	public void setPaySts(String paySts) {
		this.paySts = paySts;
	}
	
}
