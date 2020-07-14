package com.jxf.web.model.wyjt.app.act;
/**
 * @作者: xiaorongdian
 * @创建时间 :2018年12月19日 下午8:06:35
 * @功能说明:获取跟好友的转账记录页面
 */
public class TransferPageRequestParam {

	/** 好友ID */
	private String friendId;

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}
}
