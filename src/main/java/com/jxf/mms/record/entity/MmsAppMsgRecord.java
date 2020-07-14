package com.jxf.mms.record.entity;


import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 应用消息Entity
 * @author JINXINFU
 * @version 2016-04-08
 */
public class MmsAppMsgRecord extends CrudEntity<MmsAppMsgRecord> {
	
	private static final long serialVersionUID = 1L;
	
	private String memberId;
	private String title;
	private String mode;
	private String para;
	private String msgId;
	
	public MmsAppMsgRecord() {
		super();
	}

	public MmsAppMsgRecord(Long id){
		super(id);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
}