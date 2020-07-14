package com.jxf.loan.entity;

import com.jxf.mem.entity.Member;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 借条详情对话记录Entity 跟detail关联
 * @author XIAORONGDIAN
 * @version 2018-12-03
 */
public class NfsLoanDetailMessage extends CrudEntity<NfsLoanDetailMessage> {
	
	private static final long serialVersionUID = 1L;
	/** 借条详情ID不能为空 */
	private NfsLoanApplyDetail detail;		
	/** 用户 系统发的则为固定值 id=0 */
	private Member member;		
	/** 对话Id RecordMessage.CHAT_1 开始 *///{@link RecordMessage}
	private Integer messageId;		
	/** 业务类型RecordMessage.SEND_REMIND 开始 */ 
	private Integer type;		
	/** 
	 * 内容  
	 * */
	private String note;		
	
	public NfsLoanDetailMessage() {
		super();
	}

	public NfsLoanDetailMessage(Long id){
		super(id);
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public NfsLoanApplyDetail getDetail() {
		return detail;
	}

	public void setDetail(NfsLoanApplyDetail detail) {
		this.detail = detail;
	}

	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
}