package com.jxf.svc.model;




/**
 * 通知
 * 
 * @author JINXINFU
 * @version 2.0
 */
public class Notice {


	/** 通知类型 */
	private Integer noticeType;//0->无;1->有新借贷申请;2->有新好友申请;3->有新消息 
	
	/** 通知业务ID */
	private String noticeId;
	
	/** 通知消息内容 */
	private String noticeMessage;

	/**
	 * 构造方法
	 */
	public Notice() {
	}

	public Integer getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(Integer noticeType) {
		this.noticeType = noticeType;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public String getNoticeMessage() {
		return noticeMessage;
	}

	public void setNoticeMessage(String noticeMessage) {
		this.noticeMessage = noticeMessage;
	}

}