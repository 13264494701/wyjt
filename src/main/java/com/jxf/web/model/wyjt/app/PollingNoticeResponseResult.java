package com.jxf.web.model.wyjt.app;



public class PollingNoticeResponseResult   {

	/** 会员鉴权唯一标识 */
	private String memberToken;
	
	/** 通知类型 */
	private Integer noticeType;//0->无;1->有新借贷申请;2->有新好友申请;3->有新消息 ;4信用升降级
	
	/** 通知业务ID */
	private String noticeId;
	
	/** 通知内容 */
	private String noticeContent;
	
	/** 新消息条数 */
	private Integer newMsgCount;
		
	

	public String getMemberToken() {
		return memberToken;
	}

	public void setMemberToken(String memberToken) {
		this.memberToken = memberToken;
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


	public String getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}

	public Integer getNewMsgCount() {
		return newMsgCount;
	}

	public void setNewMsgCount(Integer newMsgCount) {
		this.newMsgCount = newMsgCount;
	}

	
}
