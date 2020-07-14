package com.jxf.web.model.wyjt.app.act;
/**
 * @作者: xiaorongdian
 * @创建时间 :2018年12月19日 下午9:25:09
 * @功能说明:和好友的转账记录详情
 */
public class TransferDetailResponseResult {

	/**
	 * 转账时间
	 */
	private  String time;
	/**
	 * 转账金额
	 */
	private String amount;
	/**
	 * 交易类型:转账给XX,收到xx的转账
	 */
	private String transferType;
	/**
	 * 当前状态
	 */
	private String currentStates;
	/**
	 * 交易编号  transferID
	 */
	private String transferId;
	/**
	 * 备注
	 */
	private String rmk;
	/**
	 * 表示资金是进帐还是出帐 0进账 1出账
	 */
	private Integer type;
	
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getTransferType() {
		return transferType;
	}
	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}
	public String getCurrentStates() {
		return currentStates;
	}
	public void setCurrentStates(String currentStates) {
		this.currentStates = currentStates;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getTransferId() {
		return transferId;
	}
	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}
	
}
