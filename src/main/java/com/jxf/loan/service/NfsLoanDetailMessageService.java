package com.jxf.loan.service;

import com.jxf.loan.entity.NfsLoanDetailMessage;
import com.jxf.mem.entity.Member;
import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.web.model.ResponseData;

/**
 * 借条详情对话记录Service
 * @author XIAORONGDIAN
 * @version 2018-12-03
 */
public interface NfsLoanDetailMessageService extends CrudService<NfsLoanDetailMessage> {

	/**
	 * 只用来发对话和左下角固定对话
	 * @param loanId 借条ID(申请 时候是detailId 借条是recordId )
	 * @param type   1detail 2record
	 * @param chatType 对话方式   1:左下角模板对话;   2:普通对话
	 * @param note  如果是固定对话传对话编码 RecordMessage.CHAT_1 开始，如果是输入对话：传对话内容
	 * @return 
	 */
	ResponseData sendMessage(Member member,String loanId, Integer type, Integer chatType,
			String note);
}