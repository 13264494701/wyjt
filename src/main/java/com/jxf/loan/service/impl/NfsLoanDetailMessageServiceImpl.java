package com.jxf.loan.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.loan.constant.RecordMessage;
import com.jxf.loan.dao.NfsLoanDetailMessageDao;
import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.entity.NfsLoanDetailMessage;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanApplyDetailService;
import com.jxf.loan.service.NfsLoanDetailMessageService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.model.ResponseData;
/**
 * 借条详情对话记录ServiceImpl
 * @author XIAORONGDIAN
 * @version 2018-12-03
 */
@Service("nfsLoanDetailMessageService")
@Transactional(readOnly = true)
public class NfsLoanDetailMessageServiceImpl extends CrudServiceImpl<NfsLoanDetailMessageDao, NfsLoanDetailMessage> implements NfsLoanDetailMessageService{
	@Autowired
	private NfsLoanDetailMessageService loanDetailMessageService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsLoanApplyDetailService loanApplyDetailService;
	
	public NfsLoanDetailMessage get(Long id) {
		return super.get(id);
	}
	
	public List<NfsLoanDetailMessage> findList(NfsLoanDetailMessage nfsLoanDetailMessage) {
		return super.findList(nfsLoanDetailMessage);
	}
	
	public Page<NfsLoanDetailMessage> findPage(Page<NfsLoanDetailMessage> page, NfsLoanDetailMessage nfsLoanDetailMessage) {
		return super.findPage(page, nfsLoanDetailMessage);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsLoanDetailMessage nfsLoanDetailMessage) {
		super.save(nfsLoanDetailMessage);
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsLoanDetailMessage nfsLoanDetailMessage) {
		super.delete(nfsLoanDetailMessage);
	}

	@Override
	@Transactional(readOnly = false)
	public ResponseData sendMessage(Member member,String loanId, Integer type, Integer chatType,String content) {
		
		int messageId = RecordMessage.CHAT_1;
		NfsLoanApplyDetail loanApplyDetail = null;
		Integer loanRole = null;
		if(type == 2){//是recordId
			NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(loanId));
			loanApplyDetail = loanRecord.getLoanApplyDetail();
			if(member.equals(loanRecord.getLoanee())){//借款人
				loanRole = 0;
			}else{
				loanRole = 1;
			}
		}else{//是detailId
			loanApplyDetail = loanApplyDetailService.get(Long.valueOf(loanId));
			if(member.equals(loanApplyDetail.getMember())){//借款人
				loanRole = loanApplyDetail.getLoanRole().ordinal();
			}else{
				loanRole = 1^loanApplyDetail.getLoanRole().ordinal();
			}
		}
		// 校验参数
		if ((chatType != 1 && chatType != 2) || StringUtils.isBlank(content)) {
			return ResponseData.error("参数错误!");
		}
		String note = "";
		if (chatType == 1) {
			messageId = Integer.parseInt(content);
			note = RecordMessage.getMsg(messageId);
			if (note.length() == 0) {
				return ResponseData.error("参数错误!");
			}
		}else{
			note = content;
		}
		
		NfsLoanDetailMessage loanDetailMessage = new NfsLoanDetailMessage();
		loanDetailMessage.setDetail(loanApplyDetail);
		loanDetailMessage.setMember(member);
		loanDetailMessage.setNote(note);
		loanDetailMessage.setMessageId(messageId);
		loanDetailMessage.setType(RecordMessage.SEND_MSG);
		loanDetailMessageService.save(loanDetailMessage);
		HashMap<String, Object> result = new HashMap<String,Object>();
		result.put("loanRole", loanRole);
		return ResponseData.success("对话发送成功",result);
	}
	
}