package com.jxf.web.model.wyjt.app.loan;
/**
 * @作者: xiaorongdian
 * @创建时间 :2018年12月8日 上午10:46:38
 * @功能说明:详情页发送对话
 */
public class SendMessageRequestParam {

	/** 借条ID(申请 时候是detailId 借条是recordId ) */
	private String loanId;
	
	/** 借条类型 1 detail 2 record */
	private Integer type;
	
	/** 对话方式 1:固定对话;2:输入对话 */
	private Integer chatType; 
	
	/** 固定对话：对话编码，输入对话：对话内容 */
	private String content;

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public Integer getChatType() {
		return chatType;
	}

	public void setChatType(Integer chatType) {
		this.chatType = chatType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
}
