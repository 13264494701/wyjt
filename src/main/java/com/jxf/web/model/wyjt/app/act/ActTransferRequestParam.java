package com.jxf.web.model.wyjt.app.act;
/**
 * @作者: xiaorongdian
 * @创建时间 :2018年11月9日 下午2:15:08
 * @功能说明:转账
 */
public class ActTransferRequestParam {

	/** 好友ID */
	private String friendId;
	
	/** 转账金额 */
	private String amount;
	
	/** 备注 */
	private String rmk;
	
	/** 支付密码 */
	private String payPwd;
	

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getRmk() {
		return rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}
	
	
}
