package com.jxf.web.model.wyjt.app.loan;
/**
 * @作者: xiaorongdian
 * @创建时间 :2018年11月28日 下午2:24:37
 * @功能说明:检查跟该好友是否可以产生借条关系
 */
public class CheckFriendRequestParam {

	/** 好友id 多个好友用 | 拼接 */
	private String friendsId;

	public String getFriendsId() {
		return friendsId;
	}

	public void setFriendsId(String friendsId) {
		this.friendsId = friendsId;
	}

}
