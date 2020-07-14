package com.jxf.web.app.wyjt;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.svc.config.Global;
import com.jxf.web.app.BaseController;
import com.jxf.web.model.wyjt.app.LoanButtonRequestParam;
/**
 * Controller - 借条按钮请求调动中心
 * 
 * @author gaobo
 * @version 2.0
 */
@Controller("wyjtAppLoanButtonDispatchController")
@RequestMapping(value="${wyjtApp}/loan")
public class LoanButtonDispatchController extends BaseController {

	
	@Autowired
	private NfsLoanRecordService loanRecordService;
	/**
	 * 调度转发
	 */
	@RequestMapping(value = "button")
	public String dispatch(HttpServletRequest request) {
		
		String param = request.getParameter("param");
		LoanButtonRequestParam reqData = JSONObject.parseObject(param, LoanButtonRequestParam.class);
		
		String loanId = reqData.getLoanId();
		int code = reqData.getCode();
		StringBuffer uri = new StringBuffer();
		uri.append(Global.getWyjtAppPath());
		switch (code) {
			case 2001://放款人拒绝借款
				return "redirect:"+uri.toString()+"/loan/rejectApply?loanId="+loanId;
			case 2004://借款人拒绝收款
				return "redirect:"+uri.toString()+"/loan/refusePayment?loanId="+loanId;
			case 2005://放款人拒绝延期
				return "redirect:"+uri.toString()+"/loan/answerPartialPayOrDelay?loanId="+loanId+"&isAgree=0";
			case 2006://借款人取消借款
				return "redirect:"+uri.toString()+"/loan/cancelBorrow?loanId="+loanId;
			case 2009://借款人确认收款
				return "redirect:"+uri.toString()+"/loan/confirmLendApplication?loanId="+loanId;
			case 2010://放款人未收到线下还款
				return "redirect:"+uri.toString()+"/loan/replyLineDown?loanId="+loanId+"&isAgree=0";
			case 2012://放款人已收到线下还款
				return "redirect:"+uri.toString()+"/loan/replyLineDown?loanId="+loanId+"&isAgree=1";
			case 2015://放款人提醒收款
				return "redirect:"+uri.toString()+"/loan/sendReminderMessage?note=1203&loanId="+loanId;
			case 2016://放款人提醒录制视频
				return "redirect:"+uri.toString()+"/loan/sendReminderMessage?note=1205&loanId="+loanId;
			case 2017://提醒借款/提醒同意延期/提醒同意部分还款/提醒确认线下还款
				NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(loanId));
				if(loanRecord != null) {
					if(loanRecord.getDelayStatus() == NfsLoanRecord.DelayStatus.loaneeApplyDelay) {
						return "redirect:"+uri.toString()+"/loan/sendReminderMessage?note=2102&loanId="+loanId;
					}else if(loanRecord.getDelayStatus() == NfsLoanRecord.DelayStatus.loanerApplyDelay) {
						return "redirect:"+uri.toString()+"/loan/sendReminderMessage?note=2222&loanId="+loanId;
					}else if(loanRecord.getPartialStatus() == NfsLoanRecord.PartialStatus.loaneeApplyPartial) {
						return "redirect:"+uri.toString()+"/loan/sendReminderMessage?note=2112&loanId="+loanId;
					}else if(loanRecord.getPartialStatus() == NfsLoanRecord.PartialStatus.loanerApplyPartial) {
						return "redirect:"+uri.toString()+"/loan/sendReminderMessage?note=2221&loanId="+loanId;
					}else if(loanRecord.getLineDownStatus() == NfsLoanRecord.LineDownStatus.loaneeLineDownRepayment) {
						return "redirect:"+uri.toString()+"/loan/sendReminderMessage?note=2104&loanId="+loanId;
					}
				}else {
					return "redirect:"+uri.toString()+"/loan/sendReminderMessage?note=1102&loanId="+loanId;
				}
			case 2018://提醒审核视频
				return "redirect:"+uri.toString()+"/loan/sendReminderMessage?note=1107&loanId="+loanId;
			case 2020://放款人提醒重新录制视频
				return "redirect:"+uri.toString()+"/loan/sendReminderMessage?note=1205&loanId="+loanId;
			case 2022://放款人同意延期
				return "redirect:"+uri.toString()+"/loan/answerPartialPayOrDelay?loanId="+loanId+"&isAgree=1";
			case 2027://放款人催款
				return "redirect:"+uri.toString()+"/loan/dept?loanId="+loanId;
			case 2028://借款人取消延期
				return "redirect:"+uri.toString()+"/loan/cancelPartialPayOrDelay?loanId="+loanId;
			case 2031://借款人拒绝修改利息
				return "redirect:"+"/app/wyjt/loan/replyChangeInterest?isAgree=0&loanId="+loanId;
			case 2032://借款人同意修改利息
				return "redirect:"+"/app/wyjt/loan/replyChangeInterest?isAgree=1&loanId="+loanId;
			case 2035://放款人同意部分还款
				return "redirect:"+uri.toString()+"/loan/answerPartialPayOrDelay?loanId="+loanId+"&isAgree=1";
			case 2036://放款人拒绝部分还款
				return "redirect:"+uri.toString()+"/loan/answerPartialPayOrDelay?loanId="+loanId+"&isAgree=0";
			case 2037://借款人取消部分还款
				return "redirect:"+uri.toString()+"/loan/cancelPartialPayOrDelay?loanId="+loanId;
			case 2040://借款人取消线下还款
				return "redirect:"+uri.toString()+"/loan/cancelLineDown?loanId="+loanId;
			case 2042://放款人取消部分还款
				return "redirect:"+uri.toString()+"/loan/cancelPartialPayOrDelay?loanId="+loanId;
			case 2043://放款人取消延期
				return "redirect:"+uri.toString()+"/loan/cancelPartialPayOrDelay?loanId="+loanId;
			case 2044://借款人拒绝部分还款
				return "redirect:"+uri.toString()+"/loan/answerPartialPayOrDelay?loanId="+loanId+"&isAgree=0";
			case 2045://借款人拒绝延期
				return "redirect:"+uri.toString()+"/loan/answerPartialPayOrDelay?loanId="+loanId+"&isAgree=0";
			case 2047://借款人同意延期
				return "redirect:"+uri.toString()+"/loan/answerPartialPayOrDelay?loanId="+loanId+"&isAgree=1";
			default:
				break;
		}
		return "";
		
	}
	
}
