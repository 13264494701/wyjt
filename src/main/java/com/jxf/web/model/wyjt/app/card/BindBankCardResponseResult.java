package com.jxf.web.model.wyjt.app.card;

public class BindBankCardResponseResult {

	/**处理结果代码*/
	private Integer resultCode;//0->绑定成功
	/**处理结果描述*/
	private String resultMessage;
	
	public Integer getResultCode() {
		return resultCode;
	}
	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	
	
}
