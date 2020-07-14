package com.jxf.web.model.wyjt.app.member;
/**
 * @作者: xiaorongdian
 * @创建时间 :2018年10月31日 下午3:38:47
 * @功能说明:开始认证 获得人脸SDK参数
 */
public class VideoVerifyResponseResult {
	
	/**
	 * 视频认证ID
	 */
	private String orderId;
	
	/**
	 * 加密签名秘钥
	 */
	private String encryptKey;
	
	/**
	 * 异步通知地址
	 */
	private String notifyUrl;
	
	/**
	 * 身份证号 实名认证时为空 修改支付密码 和 转账必传
	 */
	private String idNo;
	/**
	 * 电话
	 */
	private String username;
	
	
	/**业务处理结果代码*/
	private Integer resultCode;//0->成功;-1->失败
	/**业务处理结果描述*/
	private String resultMessage;//弹框提示信息
	/**实名认证状态 跳转用 0:初始 1-人脸认证 2人脸+交易密码3：人脸+交易密码+邮箱*/
	private String identityStatus;
	
	public String getEncryptKey() {
		return encryptKey;
	}

	public void setEncryptKey(String encryptKey) {
		this.encryptKey = encryptKey;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIdentityStatus() {
		return identityStatus;
	}

	public void setIdentityStatus(String identityStatus) {
		this.identityStatus = identityStatus;
	}
	
}
