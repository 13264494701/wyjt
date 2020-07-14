package com.jxf.web.model.wyjt.app.creditArchives;
/**
 * @作者: xiaorongdian
 * @创建时间 :2018年11月6日 上午10:37:49
 * @功能说明:同意/拒绝查看
 */
public class ReplyAuthRequestParam {

	/** 同意/拒绝  0拒绝 1同意*/
	private Integer isAgree;
	
	/** 好友ID */
	private String friendId;

	public Integer getIsAgree() {
		return isAgree;
	}

	public void setIsAgree(Integer isAgree) {
		this.isAgree = isAgree;
	}

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	
}
