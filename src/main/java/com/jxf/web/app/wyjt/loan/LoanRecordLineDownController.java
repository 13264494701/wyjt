package com.jxf.web.app.wyjt.loan;




import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.constant.RecordMessage;
import com.jxf.loan.entity.NfsCrAuction;
import com.jxf.loan.entity.NfsLoanDetailMessage;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRecord.AuctionStatus;
import com.jxf.loan.entity.NfsLoanRecord.LineDownStatus;
import com.jxf.loan.service.NfsCrAuctionService;
import com.jxf.loan.service.NfsLoanDetailMessageService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.mms.service.SendMsgService;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.web.admin.sys.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.loan.CloseLoanRequestParam;
import com.jxf.web.model.wyjt.app.loan.LoanDetailForAppResponseResult;

/**
 * 借条线下还款
 * @author xrd
 * @version 2018-11-07
 */
@Controller("wyjtLoanRecordLineDownController")
@RequestMapping(value = "${wyjtApp}/loan")
public class LoanRecordLineDownController extends BaseController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsLoanDetailMessageService loanDetailMessageService;
    @Autowired
    private NfsCrAuctionService crAuctionService;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private SendSmsMsgService sendSmsMsgService;
    @Autowired
	private SendMsgService sendMsgService;
    
	/**
	 * 放款人主动销借条
	 */
	@RequestMapping(value = "/answerLineDown")
	public @ResponseBody
	ResponseData closeLoanLineDown(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		CloseLoanRequestParam reqData = JSONObject.parseObject(param, CloseLoanRequestParam.class);
		String loanId = reqData.getLoanId();
		String payPwd = reqData.getPayPwd();

		NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(loanId));
		
		Date addOneDay = DateUtils.addDays(loanRecord.getCreateTime(), 1);
		if(addOneDay.getTime()>System.currentTimeMillis()){//借条生成不到24小时
			return ResponseData.error("24小时内无法执行此操作");
		}

		AuctionStatus auctionStatus = loanRecord.getAuctionStatus();
		NfsCrAuction crAuction = new NfsCrAuction();
		if(auctionStatus.equals(AuctionStatus.auctioned)){//债转借条
			crAuction.setLoanRecord(loanRecord);
			crAuction.setStatus(NfsCrAuction.Status.successed);
			List<NfsCrAuction> crAuctionList = crAuctionService.findList(crAuction);
			if(crAuctionList != null && crAuctionList.size() > 0){
				crAuction = crAuctionList.get(0);
				if(!member.equals(crAuction.getCrBuyer())){
					return ResponseData.error("当前用户不是借条的债权人,无法主动销借条");
				}
			}
		}else{
			if(!member.equals(loanRecord.getLoaner())){
				return ResponseData.error("当前用户不是放款人,无法主动销借条");
			}
		}
		if(loanRecord.getStatus().equals(NfsLoanRecord.Status.repayed)){
			return ResponseData.error("借条已关闭");
		}
		ResponseData checkPwd = memberService.checkPayPwd(payPwd, member);
		if(checkPwd.getCode() != 0){
			return ResponseData.error(checkPwd.getMessage());
		}
		//变更借条状态和账户变更 放一个事务里
		loanRecordService.closeLoanLineDown(loanRecord);
		
		if(!auctionStatus.equals(AuctionStatus.initial)){//转让中/已转让的借条
			crAuction.setLoanRecord(loanRecord);
			List<NfsCrAuction> crAuctionList = crAuctionService.findList(crAuction);
			if(crAuctionList != null && crAuctionList.size() > 0){
				crAuction = crAuctionList.get(0);
			}
			try {
				//通知借款人   站内信
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("money", loanRecord.getAmount());
				MemberMessage memberMessage = null;
				if(auctionStatus.equals(AuctionStatus.auction)){
					//发送会员消息
					memberMessage = memberMessageService.sendMessage(MemberMessage.Type.repayedAcutionImsLoaner,crAuction.getId());
					
					// 短信
					sendSmsMsgService.sendCollectionSms("repayedAcutionImsLoaner", member.getUsername(), map);
					crAuction.setStatus(NfsCrAuction.Status.unsale);
					crAuction.setIsPub(false);
					crAuctionService.save(crAuction);
				}else{
					//发送会员消息
					memberMessage = memberMessageService.sendMessage(MemberMessage.Type.paySuccessedAuctionImsLoaner,crAuction.getId());
					// 短信
					sendSmsMsgService.sendCollectionSms("paySuccessedAuctionImsLoaner", member.getUsername(), null);
				}
				//推送
				sendMsgService.beforeSendAppMsg(memberMessage);
			} catch (Exception e) {
				logger.error(Exceptions.getStackTraceAsString(e));
			}
		}
		LoanDetailForAppResponseResult detail = loanRecordService.getDetail(loanId,2,member);
		return ResponseData.success("线下还款已处理",detail);		

	}
	
	/**
	 * 放款人同意/拒绝借款人的线下还款申请
	 */
	@RequestMapping(value = "/replyLineDown")
	public @ResponseBody
	ResponseData replyLineDown(HttpServletRequest request) {
		
		Member member = memberService.getCurrent2();
		String loanId = request.getParameter("loanId");
		String isAgree = request.getParameter("isAgree");
		NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(loanId));
		if(loanRecord.getStatus().equals(NfsLoanRecord.Status.repayed)){
			return ResponseData.error("借条已关闭");
		}
		if(!loanRecord.getLineDownStatus().equals(NfsLoanRecord.LineDownStatus.loaneeLineDownRepayment)){
			return ResponseData.error("线下还款申请已结束");
		} 	
		if(!member.equals(loanRecord.getLoaner())){
			return ResponseData.error("当前用户不是放款人,无权限此操作");
		}
		String message = loanRecordService.replyLineDown(loanRecord,isAgree);
		LoanDetailForAppResponseResult detail = loanRecordService.getDetail(loanId,2,member);
			
		return ResponseData.success("线下还款已" + message,detail);
	}
	
	/**
	 * 借款人取消线下还款
	 */
	@RequestMapping(value = "/cancelLineDown")
	public @ResponseBody
	ResponseData cancelLineDown(HttpServletRequest request) {
		Member member = memberService.getCurrent2();
		String loanId = request.getParameter("loanId");
		NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(loanId));

		LoanDetailForAppResponseResult detail =  loanRecordService.getDetail(loanId,2,member);
		if(!loanRecord.getLineDownStatus().equals(LineDownStatus.loaneeLineDownRepayment)){		
			return ResponseData.success("线下还款申请状态已变更,请确认后再操作",detail);
		}
		loanRecord.setLineDownStatus(LineDownStatus.initial);
		loanRecordService.save(loanRecord);
		//发对话
		NfsLoanDetailMessage message = new NfsLoanDetailMessage();
		message.setDetail(loanRecord.getLoanApplyDetail());
		message.setMessageId(RecordMessage.CHAT_2115);
		message.setType(RecordMessage.LENDER_PAID_REPAYMENT);
		message.setMember(member);
		loanDetailMessageService.save(message);
		
		return ResponseData.success("线下还款已取消",detail);
	}
	
}