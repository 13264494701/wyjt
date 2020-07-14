package com.jxf.web.model.wyjt.app.act;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者: xiaorongdian
 * @创建时间 :2018年12月20日 上午10:12:43
 * @功能说明:我的转账记录
 */
public class TransferListResponseResult {

	/** 转账列表 */
	private List<Transfer> transferList = new ArrayList<Transfer>();
	
public class Transfer{
	/**
	 * 转账id
	 */
	private String transferId;
	/**
	 * 好友姓名
	 */
	private String friendName;
	/**
	 * 好友头像
	 */
	private String friendHendImage;
	/**
	 * 转账金额
	 */
	private String amount;
	/**
	 * 转账时间
	 */
	private String time;
	/**
	 * 转账类型 1转入 2转出
	 */
	private int transferType;
	
	
	public String getTransferId() {
		return transferId;
	}
	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	public String getFriendHendImage() {
		return friendHendImage;
	}
	public void setFriendHendImage(String friendHendImage) {
		this.friendHendImage = friendHendImage;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getTransferType() {
		return transferType;
	}
	public void setTransferType(int transferType) {
		this.transferType = transferType;
	}
}

public List<Transfer> getTransferList() {
	return transferList;
}

public void setTransferList(List<Transfer> transferList) {
	this.transferList = transferList;
}
	
}
