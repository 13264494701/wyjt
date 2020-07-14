package com.jxf.web.app.wyjt.loan;



import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.ebaoquan.rop.thirdparty.com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.constant.RecordMessage;
import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.entity.NfsLoanApplyDetail.AliveVideoStatus;
import com.jxf.loan.entity.NfsLoanDetailMessage;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanApplyDetailService;
import com.jxf.loan.service.NfsLoanApplyService;
import com.jxf.loan.service.NfsLoanDetailMessageService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.admin.sys.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.loan.LoanDetailForAppResponseResult;
import com.jxf.web.model.wyjt.app.loan.SendMessageRequestParam;

/**
 * 借条详情对话记录Controller
 * @author XIAORONGDIAN
 * @version 2018-12-03
 */
@Controller("wyjtLoanDetailMessageController")
@RequestMapping(value = "${wyjtApp}/loan")
public class LoanDetailMessageController extends BaseController {

	@Autowired
	private NfsLoanDetailMessageService loanDetailMessageService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsLoanApplyDetailService nfsLoanApplyDetailService;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private NfsLoanApplyService loanApplyService;
	
	/**
	 * 	详情页发送对话
	 */
	@RequestMapping(value = "/sendMessage")
	public @ResponseBody ResponseData sendMessage(HttpServletRequest request) {
		String param = request.getParameter("param");
		SendMessageRequestParam reqData = JSONObject.parseObject(param, SendMessageRequestParam.class);
		String loanId = reqData.getLoanId();
		Integer type = reqData.getType();
		Integer chatType = reqData.getChatType();
		String content = reqData.getContent();
		Member member = memberService.getCurrent2();
		ResponseData result = loanDetailMessageService.sendMessage(member,loanId,type,chatType,content);
		if(result.getCode() != 0){
			return ResponseData.error(result.getMessage());
		}
		LoanDetailForAppResponseResult detail = loanRecordService.getDetail(loanId,type,member);
		return ResponseData.success("对话发送成功", detail);
	}
	
	/**
	 * 	发送提醒消息
	 */
	@RequestMapping(value = "/sendReminderMessage")
	public @ResponseBody
	ResponseData sendReminderMessage(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		LoanDetailForAppResponseResult result  = null;
		int note = Integer.parseInt(request.getParameter("note"));
		String id = "";
		Integer type = 0;
		String loanId = request.getParameter("loanId");
		Long detailId = null;
		if(RecordMessage.CHAT_1203 == note) {
			NfsLoanApplyDetail nfsLoanApplyDetail = nfsLoanApplyDetailService.get(Long.parseLong(loanId));
			//发送会员消息
			memberMessageService.sendMessage(MemberMessage.Type.reminderCollection,Long.parseLong(loanId));
			id = nfsLoanApplyDetail.getId().toString();
			detailId = nfsLoanApplyDetail.getId();
			type = 1;
		}else if(RecordMessage.CHAT_1205 == note) {
			NfsLoanApplyDetail nfsLoanApplyDetail = nfsLoanApplyDetailService.get(Long.parseLong(loanId));
			AliveVideoStatus aliveVideoStatus = nfsLoanApplyDetail.getAliveVideoStatus();
			if(aliveVideoStatus.equals(AliveVideoStatus.pendingReUpload)){
				memberMessageService.sendMessage(MemberMessage.Type.rerecordVideo,Long.parseLong(loanId));
			}else{
				memberMessageService.sendMessage(MemberMessage.Type.recordVideo, Long.parseLong(loanId));
			}
			id = nfsLoanApplyDetail.getId().toString();
			detailId = nfsLoanApplyDetail.getId();
			type = 1;
		}else if(RecordMessage.CHAT_1107 == note) {
			NfsLoanApplyDetail nfsLoanApplyDetail = nfsLoanApplyDetailService.get(Long.parseLong(loanId));
			memberMessageService.sendMessage(MemberMessage.Type.confirmVideo,Long.parseLong(loanId));
			
			id = nfsLoanApplyDetail.getId().toString();
			detailId = nfsLoanApplyDetail.getId();
			type = 1;
		}else if(RecordMessage.CHAT_2221 == note) {
			NfsLoanRecord loanRecord = loanRecordService.get(Long.parseLong(loanId));
			//发送会员消息
			memberMessageService.sendMessage(MemberMessage.Type.loanerReminderPartialRepayment,Long.parseLong(loanId));
			id = loanRecord.getId().toString();
			detailId = loanRecord.getLoanApplyDetail().getId();
			type = 2;
		}else if(RecordMessage.CHAT_2222 == note) {
			NfsLoanRecord loanRecord = loanRecordService.get(Long.parseLong(loanId));
			//发送会员消息
			memberMessageService.sendMessage(MemberMessage.Type.loanerReminderDelayApplication,Long.parseLong(loanId));
			id = loanRecord.getId().toString();
			detailId = loanRecord.getLoanApplyDetail().getId();
			type = 2;
		}else if(RecordMessage.CHAT_1102 == note) {
			NfsLoanApplyDetail nfsLoanApplyDetail = nfsLoanApplyDetailService.get(Long.parseLong(loanId));
			memberMessageService.sendMessage(MemberMessage.Type.remindBorrowers,Long.parseLong(loanId));
			id = nfsLoanApplyDetail.getId().toString();
			detailId = nfsLoanApplyDetail.getId();
			type = 1;
		}else if(RecordMessage.CHAT_2112 == note) {
			NfsLoanRecord loanRecord = loanRecordService.get(Long.parseLong(loanId));
			//发送会员消息
			memberMessageService.sendMessage(MemberMessage.Type.loaneeReminderPartialRepayment,Long.parseLong(loanId));
			id = loanRecord.getId().toString();
			detailId = loanRecord.getLoanApplyDetail().getId();
			type = 2;
		}else if(RecordMessage.CHAT_2102 == note) {
			NfsLoanRecord loanRecord = loanRecordService.get(Long.parseLong(loanId));
			//发送会员消息
			memberMessageService.sendMessage(MemberMessage.Type.loaneeReminderDelayApplication,Long.parseLong(loanId));
			id = loanRecord.getId().toString();
			type = 2;
			detailId = loanRecord.getLoanApplyDetail().getId();
		}else if(RecordMessage.CHAT_2104 == note) {
			NfsLoanRecord loanRecord = loanRecordService.get(Long.parseLong(loanId));
			//发送会员消息
			memberMessageService.sendMessage(MemberMessage.Type.lineDownRepayment,Long.parseLong(loanId));
			id = loanRecord.getId().toString();
			detailId = loanRecord.getLoanApplyDetail().getId();
			type = 2;
		}
		
		//发对话
		NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
		NfsLoanApplyDetail nfsLoanApplyDetail = new NfsLoanApplyDetail();
		nfsLoanApplyDetail.setId(detailId);
		nfsLoanDetailMessage.setDetail(nfsLoanApplyDetail);
		nfsLoanDetailMessage.setMember(member);
		nfsLoanDetailMessage.setMessageId(note);
		nfsLoanDetailMessage.setType(RecordMessage.SEND_REMIND);
		loanDetailMessageService.save(nfsLoanDetailMessage);
		
		result = loanRecordService.getDetail(id,type,member);
		return ResponseData.success("对话发送成功", result);
	}
	
	
	
	
	
	
}