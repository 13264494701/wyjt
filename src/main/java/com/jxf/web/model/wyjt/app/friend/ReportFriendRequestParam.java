package com.jxf.web.model.wyjt.app.friend;

/**
 * @作者: wo
 * @创建时间 :2018年10月30日 下午4:12:48
 * @功能说明:
 */
public class ReportFriendRequestParam{

	/** 好友的ID */
	private String friendId;
	
	/** 投诉类型 */
	private Integer type;//0->密码泄露;1->电话骚扰;2->欺诈;3->暴力催收;4->其它
	/** 投诉标题 */
	private String title;
	/** 投诉内容 */
	private String content;
	
	/** 图片证据 */
	private String images;//用|分割多张图片地址

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}


}
