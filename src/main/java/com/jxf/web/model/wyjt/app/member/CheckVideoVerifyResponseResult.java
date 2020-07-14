package com.jxf.web.model.wyjt.app.member;
/**
 * @作者: xiaorongdian
 * @创建时间 :2018年11月9日 上午11:15:17
 * @功能说明:检查人脸认证是否成功
 */
public class CheckVideoVerifyResponseResult {

	
	/**处理结果代码*/
	private Integer resultCode;//0->待认证;1->认证成功;2->认证失败;
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
