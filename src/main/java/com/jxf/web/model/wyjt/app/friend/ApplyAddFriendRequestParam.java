package com.jxf.web.model.wyjt.app.friend;

/**
 * @作者: xiaorongdian
 * @创建时间 :2018年10月30日 下午2:20:10
 * @功能说明:
 */
public class ApplyAddFriendRequestParam {

	/** 好友ID */
	private String friendId;
	/** 验证消息 */
	private String note;
	
	
	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
