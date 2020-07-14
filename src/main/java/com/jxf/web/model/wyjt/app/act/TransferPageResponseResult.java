package com.jxf.web.model.wyjt.app.act;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者: xiaorongdian
 * @创建时间 :2018年12月19日 下午8:13:03
 * @功能说明:查询跟好友的转账页
 */
public class TransferPageResponseResult {

	/** 好友姓名 */
	private String memberName;
	/** 左边头像(好友头像) */
	private String leftHeadImage;
	/** 右边头像(我头像) */
	private String rightHeadImage;
	
	/** 好友电话 */
	private String friendUsername;
	
	/**
	 * 列表数据
	 */
	private List<TransferPageItem> transferPageItems = new ArrayList<TransferPageItem>();
	
public class TransferPageItem {
	
	/** 转账记录id */  
	private String transferId;
	/** 时间 */
	private String time;
	/** 转账金额 */
	private Integer amount;
	/** 备注 */
	private String note;
	/** 状态 */
	private String status;
	/**
	 * 数据位置  0 右边   1 左边
	 */
	protected int location;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public int getLocation() {
		return location;
	}
	public void setLocation(int location) {
		this.location = location;
	}
	public String getTransferId() {
		return transferId;
	}
	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}
	
}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getLeftHeadImage() {
		return leftHeadImage;
	}

	public void setLeftHeadImage(String leftHeadImage) {
		this.leftHeadImage = leftHeadImage;
	}

	public String getRightHeadImage() {
		return rightHeadImage;
	}

	public void setRightHeadImage(String rightHeadImage) {
		this.rightHeadImage = rightHeadImage;
	}

	public List<TransferPageItem> getTransferPageItems() {
		return transferPageItems;
	}

	public void setTransferPageItems(List<TransferPageItem> transferPageItems) {
		this.transferPageItems = transferPageItems;
	}

	public String getFriendUsername() {
		return friendUsername;
	}

	public void setFriendUsername(String friendUsername) {
		this.friendUsername = friendUsername;
	}

}
