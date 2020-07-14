package com.jxf.web.app.wyjt.loan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanApply.RepayType;
import com.jxf.loan.entity.NfsLoanApply.TrxType;
import com.jxf.loan.entity.NfsLoanArbitration;
import com.jxf.loan.entity.NfsLoanArbitrationExecution;
import com.jxf.loan.entity.NfsLoanArbitrationExecution.ExecutionStatus;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRecord.Status;
import com.jxf.loan.service.NfsLoanArbitrationExecutionService;
import com.jxf.loan.service.NfsLoanArbitrationService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.utils.LoanUtils;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.entity.MemberMessage.Type;
import com.jxf.mem.service.MemberActService;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.H5Utils;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.execution.ArbitrationExecutionApplyRequestParam;
import com.jxf.web.model.wyjt.app.execution.ArbitrationExecutionApplyResponseResult;
import com.jxf.web.model.wyjt.app.execution.ArbitrationExecutionListRequestParam;
import com.jxf.web.model.wyjt.app.execution.ArbitrationExecutionListResponseResult;
import com.jxf.web.model.wyjt.app.execution.ArbitrationExecutionListResponseResult.ArbitrationExecution;
import com.jxf.web.model.wyjt.app.execution.ArbitrationExecutionPayRequestParam;

/**
 * 强执
 * @author liuhuaixin
 *
 */
@Controller("wyjtAppLoanArbitrationExecutionController")
@RequestMapping(value="${wyjtApp}/arbitrationExecution")
public class LoanArbitrationExecutionController {
	private static final Logger logger = LoggerFactory.getLogger(LoanArbitrationExecutionController.class);
	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsLoanArbitrationService loanArbitrationService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsLoanArbitrationExecutionService loanArbitrationExecutionService;
	@Autowired
	private MemberActService memberActService;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private SendSmsMsgService sendSmsMsgService;
	
	
	@RequestMapping(value ="/apply")
	@ResponseBody
	public ResponseData apply(HttpServletRequest request) {
		try {
		String param = request.getParameter("param");
		ArbitrationExecutionApplyRequestParam reqData = JSONObject.parseObject(param,ArbitrationExecutionApplyRequestParam.class);

		NfsLoanArbitration loanArbitration = loanArbitrationService.get(Long.parseLong(reqData.getArbitrationId()));
		if(loanArbitration.getStrongStatus().equals(NfsLoanArbitration.StrongStatus.appliedStrong)) {
			return ResponseData.error("此借条已经申请过强执，无法再次申请");
		}
		NfsLoanRecord loanRecord = loanRecordService.get(loanArbitration.getLoan());
		if(loanRecord == null) {
			return ResponseData.error("此借条有误");
		}
		int code = loanArbitrationExecutionService.apply(loanRecord,loanArbitration);
		if(code==0){
			return ResponseData.error("创建强执失败");
		}else {
			//放款人
			String loanerPhoneNo = loanRecord.getLoaner().getUsername();
			sendSmsMsgService.sendMessage("EnforcementAcceptanceSmsLoaner", loanerPhoneNo, null);
			
			//站内信
			memberMessageService.sendMessage(Type.EnforcementAcceptanceImsLoaner,loanRecord.getId());
			
			ArbitrationExecutionApplyResponseResult applyResponseResult = new ArbitrationExecutionApplyResponseResult();
			applyResponseResult.setNote("您的强执申请将在4个工作日内受理\n受理后，请在代缴费中及时缴纳强执相关费用\n如不缴纳费用，该申请将在15日后撤销"+"|"+reqData.getArbitrationId());
			return ResponseData.success("创建成功",applyResponseResult);
		}
		}catch(Exception e){
			logger.error(Exceptions.getStackTraceAsString(e));
		}
		return ResponseData.error("创建强执失败");
	}
	
	@RequestMapping(value ="/list")
	@ResponseBody
	public ResponseData list(HttpServletRequest request) {
		try {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		ArbitrationExecutionListRequestParam reqData = JSONObject.parseObject(param,ArbitrationExecutionListRequestParam.class);
		Integer pageNo = reqData.getPageNo();
		Integer pageSize = reqData.getPageSize();
		String type =reqData.getType();
		TrxType trxType = TrxType.online;
		if(type.equals("1")) {
			NfsLoanArbitration nfsLoanArbitration = new NfsLoanArbitration();
			NfsLoanRecord loanRecord = new NfsLoanRecord();
			loanRecord.setLoaner(member);
			Page<NfsLoanArbitration> page = loanArbitrationService.findExecution(loanRecord.getLoaner().getId(),nfsLoanArbitration,pageNo, pageSize,0);
			ArbitrationExecutionListResponseResult result = new ArbitrationExecutionListResponseResult();
			ArrayList<ArbitrationExecution> loanList = new ArrayList<ArbitrationExecution>();
			for(NfsLoanArbitration everyLoanArbitration:page.getList()) {
				NfsLoanRecord nfsLoanRecord = loanRecordService.get(everyLoanArbitration.getLoan().getId());
				if(nfsLoanRecord.getStatus().equals(Status.repayed)) {
					continue;
				}
				ArbitrationExecution arbitrationExecution = new ArbitrationExecution();
				arbitrationExecution.setArbitratId(everyLoanArbitration.getId().toString());
				arbitrationExecution.setImgUrl(everyLoanArbitration.getLoan().getLoanee().getHeadImage());
				arbitrationExecution.setLoanId(everyLoanArbitration.getLoan().getId().toString());
				RepayType repayType = everyLoanArbitration.getLoan().getRepayType();
				if(repayType.equals(NfsLoanApply.RepayType.oneTimePrincipalAndInterest)) {
				arbitrationExecution.setDescript("0");
				}else {
					arbitrationExecution.setDescript("1");
				}
				// 计算可申请天数 字加上色值拼接带回 ；代表前面字后面色值 | 代表前面字色值 后面另一个字加色值
				int day = DateUtils.getDifferenceOfTwoDate(DateUtils.addCalendarByDate(everyLoanArbitration.getRuleTime(),720),new Date());
				//逾期天数
				int day1 = DateUtils.getDistanceOfTwoDate(new Date(),everyLoanArbitration.getLoan().getDueRepayDate());
				arbitrationExecution.setStatus("申请强执;ffffff|可在;000000|" + day + ";ff0000|天内申请;000000");
				arbitrationExecution.setMoney(StringUtils.decimalToStr(everyLoanArbitration.getLoan().getAmount(),2));
				arbitrationExecution.setInterest(StringUtils.decimalToStr(everyLoanArbitration.getLoan().getInterest(),2));
				arbitrationExecution.setSurplusDay(day);
				arbitrationExecution.setDay(day1);
				arbitrationExecution.setMode(1 + "");
				arbitrationExecution.setRepayDate(DateUtils.formatDate(everyLoanArbitration.getLoan().getDueRepayDate(), "yyyy-MM-dd"));
				arbitrationExecution.setRuleTime(DateUtils.formatDate(everyLoanArbitration.getRuleTime(), "yyyy-MM-dd"));
				arbitrationExecution.setLoanee_name(everyLoanArbitration.getLoan().getLoanee().getName());
				// 统计待还金额
		 		BigDecimal decMoney =everyLoanArbitration.getLoan().getDueRepayAmount();
				// 计算逾期利息
		    	BigDecimal overMoney=new BigDecimal(0.0);
		    	int days=DateUtils.getDistanceOfTwoDate(new Date(),everyLoanArbitration.getLoan().getDueRepayDate());
		    	BigDecimal overdueDaysBig = new BigDecimal(days);
		    	//计算利息 默认给24
		        overMoney=LoanUtils.calOverdueInterest(everyLoanArbitration.getLoan().getDueRepayAmount(),overdueDaysBig);
			    //总金额
			    BigDecimal totalMoney=decMoney.add(overMoney);
			    arbitrationExecution.setOverMoney(StringUtils.decimalToStr(totalMoney, 2));
				loanList.add(arbitrationExecution);
			}
			result.setArbitrationList(loanList);
			return ResponseData.success("成功查询全部可申请强执借条",result);
		}else if(type.equals("2")) {
			NfsLoanArbitrationExecution nfsLoanArbitrationExecution = new NfsLoanArbitrationExecution();
			NfsLoanRecord loanRecord = new NfsLoanRecord();
			loanRecord.setLoaner(member);
			loanRecord.setTrxType(trxType);
			nfsLoanArbitrationExecution.setStatus(ExecutionStatus.executionApplication);/**0*/
			nfsLoanArbitrationExecution.setLoanerId(loanRecord.getLoaner().getId());
			Page<NfsLoanArbitrationExecution> page = loanArbitrationExecutionService.findExecution(nfsLoanArbitrationExecution,pageNo, pageSize);
			ArbitrationExecutionListResponseResult result = new ArbitrationExecutionListResponseResult();
			ArrayList<ArbitrationExecution> loanList = new ArrayList<ArbitrationExecution>();
			if(page.getList().size()>0) {
			for(NfsLoanArbitrationExecution everyLoanArbitrationExecution:page.getList()) {
				NfsLoanRecord nfsLoanRecord = loanRecordService.get(everyLoanArbitrationExecution.getLoan().getId());
				if(nfsLoanRecord.getStatus().equals(Status.repayed)) {
					continue;
				}
				ArbitrationExecution arbitrationExecution = new ArbitrationExecution();
				arbitrationExecution.setArbitratId(everyLoanArbitrationExecution.getArbitrationId().toString());
				arbitrationExecution.setExecutionId(everyLoanArbitrationExecution.getId().toString());
				Member loanee = memberService.get(everyLoanArbitrationExecution.getLoaneeId());
				if(loanee==null) {
					return ResponseData.error("借款人查询失败");
				}
				arbitrationExecution.setImgUrl(loanee.getHeadImage());
				arbitrationExecution.setLoanId(everyLoanArbitrationExecution.getLoan().getId().toString());
				RepayType repayType = nfsLoanRecord.getRepayType();
				if(repayType.equals(NfsLoanApply.RepayType.oneTimePrincipalAndInterest)) {
				arbitrationExecution.setDescript("0");
				}else {
					arbitrationExecution.setDescript("1");
				}
				// 计算可申请天数 字加上色值拼接带回 ；代表前面字后面色值 | 代表前面字色值 后面另一个字加色值
				int day = DateUtils.getDistanceOfTwoDate(DateUtils.addCalendarByDate(everyLoanArbitrationExecution.getRulingtime(),720),new Date());
				//逾期天数
				int day1 = DateUtils.getDistanceOfTwoDate(new Date(),nfsLoanRecord.getDueRepayDate());
				arbitrationExecution.setStatus("申请已提交，待受理请耐心等待;000000");
				arbitrationExecution.setMoney(StringUtils.decimalToStr(everyLoanArbitrationExecution.getAmount(),2));
				arbitrationExecution.setInterest(StringUtils.decimalToStr(everyLoanArbitrationExecution.getInterest(),2));
				arbitrationExecution.setSurplusDay(day);
				arbitrationExecution.setDay(day1);
				arbitrationExecution.setRepayDate(DateUtils.formatDate(nfsLoanRecord.getDueRepayDate(), "yyyy-MM-dd"));
				arbitrationExecution.setMode(3 + "");
				arbitrationExecution.setRuleTime(DateUtils.formatDateTime(everyLoanArbitrationExecution.getRulingtime()));
				arbitrationExecution.setLoanee_name(everyLoanArbitrationExecution.getLoaneeName());
				NfsLoanRecord loan = loanRecordService.get(everyLoanArbitrationExecution.getLoan().getId());
				// 统计待还金额
		 		BigDecimal decMoney =loan.getDueRepayAmount();
				// 计算逾期利息
		    	BigDecimal overMoney=new BigDecimal(0.0);
		    	int days=DateUtils.getDistanceOfTwoDate(new Date(),loan.getDueRepayDate());
		    	BigDecimal overdueDaysBig = new BigDecimal(days);
		    	//计算利息 默认给24
		        overMoney=LoanUtils.calOverdueInterest(loan.getDueRepayAmount(),overdueDaysBig);
			    //总金额
			    BigDecimal totalMoney=decMoney.add(overMoney);
			    arbitrationExecution.setOverMoney(StringUtils.decimalToStr(totalMoney, 2));
				loanList.add(arbitrationExecution);
			}
			}
			result.setArbitrationList(loanList);
			return ResponseData.success("成功查询全部待受理强执借条",result);
		}else if(type.equals("3")) {
			NfsLoanArbitrationExecution nfsLoanArbitrationExecution = new NfsLoanArbitrationExecution();
			NfsLoanRecord loanRecord = new NfsLoanRecord();
			loanRecord.setLoaner(member);
			loanRecord.setTrxType(trxType);
			nfsLoanArbitrationExecution.setStatus(ExecutionStatus.executionPayment);/**2*/
			nfsLoanArbitrationExecution.setLoanerId(loanRecord.getLoaner().getId());
			Page<NfsLoanArbitrationExecution> page = loanArbitrationExecutionService.findExecution(nfsLoanArbitrationExecution,pageNo, pageSize);
			ArbitrationExecutionListResponseResult result = new ArbitrationExecutionListResponseResult();
			ArrayList<ArbitrationExecution> loanList = new ArrayList<ArbitrationExecution>();
			if(page.getList().size()>0) {
			for(NfsLoanArbitrationExecution everyLoanArbitrationExecution:page.getList()) {
				NfsLoanRecord nfsLoanRecord = loanRecordService.get(everyLoanArbitrationExecution.getLoan().getId());
				if(nfsLoanRecord.getStatus().equals(Status.repayed)) {
					continue;
				}
				ArbitrationExecution arbitrationExecution = new ArbitrationExecution();
				arbitrationExecution.setArbitratId(everyLoanArbitrationExecution.getArbitrationId().toString());
				arbitrationExecution.setExecutionId(everyLoanArbitrationExecution.getId().toString());
				Member loanee = memberService.get(everyLoanArbitrationExecution.getLoaneeId());
				if(loanee==null) {
					return ResponseData.error("借款人查询失败");
				}
				arbitrationExecution.setImgUrl(loanee.getHeadImage());
				arbitrationExecution.setLoanId(everyLoanArbitrationExecution.getLoan().getId().toString());
				RepayType repayType = nfsLoanRecord.getRepayType();
				if(repayType.equals(NfsLoanApply.RepayType.oneTimePrincipalAndInterest)) {
				arbitrationExecution.setDescript("0");
				}else {
					arbitrationExecution.setDescript("1");
				}
				arbitrationExecution.setStatus("缴纳强执费;ffffff|缴费后开始强执;000000");
				arbitrationExecution.setMoney(StringUtils.decimalToStr(everyLoanArbitrationExecution.getAmount(),2));
				arbitrationExecution.setInterest(StringUtils.decimalToStr(everyLoanArbitrationExecution.getInterest(),2));
				arbitrationExecution.setMode(2 + "");
				arbitrationExecution.setRepayDate(DateUtils.formatDate(nfsLoanRecord.getDueRepayDate(), "yyyy-MM-dd"));
				arbitrationExecution.setRuleTime(DateUtils.formatDateTime(everyLoanArbitrationExecution.getRulingtime()));
				arbitrationExecution.setLoanee_name(everyLoanArbitrationExecution.getLoaneeName());
				NfsLoanRecord loan = loanRecordService.get(everyLoanArbitrationExecution.getLoan().getId());
				// 统计待还金额
		 		BigDecimal decMoney =loan.getDueRepayAmount();
				// 计算逾期利息
		    	BigDecimal overMoney=new BigDecimal(0.0);
		    	int days=DateUtils.getDistanceOfTwoDate(new Date(),loan.getDueRepayDate());
		    	BigDecimal overdueDaysBig = new BigDecimal(days);
		    	//计算利息 默认给24
		        overMoney=LoanUtils.calOverdueInterest(loan.getDueRepayAmount(),overdueDaysBig);
			    //总金额
			    BigDecimal totalMoney=decMoney.add(overMoney);
			    arbitrationExecution.setOverMoney(StringUtils.decimalToStr(totalMoney, 2));
				loanList.add(arbitrationExecution);
			}
			}
			result.setArbitrationList(loanList);
			return ResponseData.success("成功查询全部待缴费强执借条",result);
		}else if(type.equals("4")) {
			NfsLoanArbitrationExecution nfsLoanArbitrationExecution = new NfsLoanArbitrationExecution();
			NfsLoanRecord loanRecord = new NfsLoanRecord();
			loanRecord.setLoaner(member);
			loanRecord.setTrxType(trxType);
			nfsLoanArbitrationExecution.setStatus(ExecutionStatus.executionProcessing);/**3*/
			nfsLoanArbitrationExecution.setLoanerId(loanRecord.getLoaner().getId());
			Page<NfsLoanArbitrationExecution> page = loanArbitrationExecutionService.findExecution(nfsLoanArbitrationExecution,pageNo, pageSize);
			ArbitrationExecutionListResponseResult result = new ArbitrationExecutionListResponseResult();
			ArrayList<ArbitrationExecution> loanList = new ArrayList<ArbitrationExecution>();
			if(page.getList().size()>0) {
			for(NfsLoanArbitrationExecution everyLoanArbitrationExecution:page.getList()) {
				NfsLoanRecord nfsLoanRecord = loanRecordService.get(everyLoanArbitrationExecution.getLoan().getId());
				if(nfsLoanRecord.getStatus().equals(Status.repayed)) {
					continue;
				}
				ArbitrationExecution arbitrationExecution = new ArbitrationExecution();
				arbitrationExecution.setArbitratId(everyLoanArbitrationExecution.getArbitrationId().toString());
				arbitrationExecution.setExecutionId(everyLoanArbitrationExecution.getId().toString());
				Member loanee = memberService.get(everyLoanArbitrationExecution.getLoaneeId());
				if(loanee==null) {
					return ResponseData.error("借款人查询失败");
				}
				arbitrationExecution.setImgUrl(loanee.getHeadImage());
				arbitrationExecution.setLoanId(everyLoanArbitrationExecution.getLoan().getId().toString());
				RepayType repayType = nfsLoanRecord.getRepayType();
				if(repayType.equals(NfsLoanApply.RepayType.oneTimePrincipalAndInterest)) {
				arbitrationExecution.setDescript("0");
				}else {
					arbitrationExecution.setDescript("1");
				}
				arbitrationExecution.setStatus("强执进行中;000000");
				arbitrationExecution.setMoney(StringUtils.decimalToStr(everyLoanArbitrationExecution.getAmount(),2));
				arbitrationExecution.setInterest(StringUtils.decimalToStr(everyLoanArbitrationExecution.getInterest(),2));
				arbitrationExecution.setMode(3 + "");
				arbitrationExecution.setRepayDate(DateUtils.formatDate(nfsLoanRecord.getDueRepayDate(), "yyyy-MM-dd"));
				arbitrationExecution.setRuleTime(DateUtils.formatDateTime(everyLoanArbitrationExecution.getRulingtime()));
				arbitrationExecution.setLoanee_name(everyLoanArbitrationExecution.getLoaneeName());
				NfsLoanRecord loan = loanRecordService.get(everyLoanArbitrationExecution.getLoan().getId());
				// 统计待还金额
		 		BigDecimal decMoney =loan.getDueRepayAmount();
				// 计算逾期利息
		    	BigDecimal overMoney=new BigDecimal(0.0);
		    	int days=DateUtils.getDistanceOfTwoDate(new Date(),loan.getDueRepayDate());
		    	BigDecimal overdueDaysBig = new BigDecimal(days);
		    	//计算利息 默认给24
		        overMoney=LoanUtils.calOverdueInterest(loan.getDueRepayAmount(),overdueDaysBig);
			    //总金额
			    BigDecimal totalMoney=decMoney.add(overMoney);
			    arbitrationExecution.setOverMoney(StringUtils.decimalToStr(totalMoney, 2));
				loanList.add(arbitrationExecution);
			}
			}
			result.setArbitrationList(loanList);
			return ResponseData.success("成功查询全部进行中强执借条",result);
		}else if(type.equals("5")) {
			/**状态4，5*/
			NfsLoanArbitrationExecution nfsLoanArbitrationExecution = new NfsLoanArbitrationExecution();
			NfsLoanRecord loanRecord = new NfsLoanRecord();
			loanRecord.setLoaner(member);
			loanRecord.setTrxType(trxType);
			Page<NfsLoanArbitrationExecution> page = loanArbitrationExecutionService.findExecuted(loanRecord.getLoaner().getId(),nfsLoanArbitrationExecution,pageNo, pageSize);
			ArbitrationExecutionListResponseResult result = new ArbitrationExecutionListResponseResult();
			ArrayList<ArbitrationExecution> loanList = new ArrayList<ArbitrationExecution>();
			if(page.getList().size()>0) {
			for(NfsLoanArbitrationExecution everyLoanArbitrationExecution:page.getList()) {
				NfsLoanRecord nfsLoanRecord = loanRecordService.get(everyLoanArbitrationExecution.getLoan().getId());
				/*if(nfsLoanRecord.getStatus().equals(Status.repayed)) {
					continue;
				}*/
				ArbitrationExecution arbitrationExecution = new ArbitrationExecution();
				arbitrationExecution.setArbitratId(everyLoanArbitrationExecution.getArbitrationId().toString());
				arbitrationExecution.setExecutionId(everyLoanArbitrationExecution.getId().toString());
				Member loanee = memberService.get(everyLoanArbitrationExecution.getLoaneeId());
				if(loanee==null) {
					return ResponseData.error("借款人查询失败");
				}
				arbitrationExecution.setImgUrl(loanee.getHeadImage());
				arbitrationExecution.setLoanId(everyLoanArbitrationExecution.getLoan().getId().toString());
				RepayType repayType = nfsLoanRecord.getRepayType();
				if(repayType.equals(NfsLoanApply.RepayType.oneTimePrincipalAndInterest)) {
				arbitrationExecution.setDescript("0");
				}else {
					arbitrationExecution.setDescript("1");
				}
				arbitrationExecution.setStatus("强执已结束;000000");
				arbitrationExecution.setMoney(StringUtils.decimalToStr(everyLoanArbitrationExecution.getAmount(),2));
				arbitrationExecution.setInterest(StringUtils.decimalToStr(everyLoanArbitrationExecution.getInterest(),2));
				arbitrationExecution.setMode(3 + "");
				arbitrationExecution.setRepayDate(DateUtils.formatDate(nfsLoanRecord.getDueRepayDate(), "yyyy-MM-dd"));
				arbitrationExecution.setRuleTime(DateUtils.formatDateTime(everyLoanArbitrationExecution.getRulingtime()));
				arbitrationExecution.setLoanee_name(everyLoanArbitrationExecution.getLoaneeName());
				NfsLoanRecord loan = loanRecordService.get(everyLoanArbitrationExecution.getLoan().getId());
				// 统计待还金额
		 		BigDecimal decMoney =loan.getDueRepayAmount();
				// 计算逾期利息
		    	BigDecimal overMoney=new BigDecimal(0.0);
		    	int days=DateUtils.getDistanceOfTwoDate(new Date(),loan.getDueRepayDate());
		    	BigDecimal overdueDaysBig = new BigDecimal(days);
		    	//计算利息 默认给24
		        overMoney=LoanUtils.calOverdueInterest(loan.getDueRepayAmount(),overdueDaysBig);
			    //总金额
			    BigDecimal totalMoney=decMoney.add(overMoney);
			    arbitrationExecution.setOverMoney(StringUtils.decimalToStr(totalMoney, 2));
				loanList.add(arbitrationExecution);
			}
			}
			result.setArbitrationList(loanList);
			return ResponseData.success("成功查询全部已结束强执借条",result);
		}
		}catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
			return ResponseData.error("查询失败");
		}
		return ResponseData.error("查询失败");
	}
	
	
	
	/**
	 * 	强执支付页面跳转
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "executionPay")
	public ModelAndView executionPay(HttpServletRequest request,Model model) {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		ArbitrationExecutionPayRequestParam reqData = JSONObject.parseObject(param,ArbitrationExecutionPayRequestParam.class);
		ModelAndView mv = new ModelAndView("app/arbitrationExecution/payEnforcement");
		mv = H5Utils.addPlatform(member, mv);
		String executionId = reqData.getExecutionId();
		NfsLoanArbitrationExecution nfsLoanArbitrationExecution = loanArbitrationExecutionService.get(Long.valueOf(executionId));
		if(member == null) {
			mv.addObject("code", -1);
			mv.addObject("message", "登录状态已过期，请重新登录！");
			return mv;
		}
		if(nfsLoanArbitrationExecution == null) {
			mv.addObject("code", -1);
			mv.addObject("message", "强执信息有误");
			return mv;
		}
		ExecutionStatus executionStatus = nfsLoanArbitrationExecution.getStatus();
		if(!executionStatus.equals(ExecutionStatus.executionPayment)){
			mv.addObject("code", -1);
			mv.addObject("message", "该状态下不能进行支付");
			return mv;
	    }
		if(nfsLoanArbitrationExecution.getDelFlag().equals("1")){
			mv.addObject("code", -1);
			mv.addObject("message", "该状态下不能进行支付");
			return mv;
		}
		Date date = new Date();
		//逾期时长
	    int delayDays = DateUtils.getDistanceOfTwoDate(date, nfsLoanArbitrationExecution.getDueRepayDate());
	    //账户余额
	    BigDecimal avlBal = memberActService.getAvlBal(member);
	    //计算逾期利息
	    BigDecimal overMoney=new BigDecimal(0.0);
	    NfsLoanRecord loan = loanRecordService.get(nfsLoanArbitrationExecution.getLoan().getId());
    	int days=DateUtils.getDistanceOfTwoDate(new Date(),loan.getDueRepayDate());
    	BigDecimal overdueDaysBig = new BigDecimal(days);
 	   	overMoney=LoanUtils.calOverdueInterest(loan.getDueRepayAmount(),overdueDaysBig);
	    BigDecimal loanMoney=nfsLoanArbitrationExecution.getAmount().add(nfsLoanArbitrationExecution.getInterest().add(overMoney));
	    boolean isEnough = false;
		if(avlBal.compareTo(nfsLoanArbitrationExecution.getFee()) >= 0) {
			isEnough = true;
		}
	    String memberToken = request.getHeader("x-memberToken");
	    mv.addObject("memberToken", memberToken);
	    mv.addObject("isEnough", isEnough);
	    mv.addObject("loanerId", member.getId());
	    mv.addObject("forceId", executionId);
	    mv.addObject("loaneeName", nfsLoanArbitrationExecution.getLoaneeName());
	    mv.addObject("delayDays", delayDays);
	    mv.addObject("avlBal", avlBal);
	    mv.addObject("loanMoney", StringUtils.decimalToStr(loanMoney, 2));
	    mv.addObject("forceMoney", StringUtils.decimalToStr(nfsLoanArbitrationExecution.getFee(), 2));
		return mv;
	}
	/**
	 * 强执费用支付
	 * @param request
	 * @return
	 */
	@RequestMapping(value="payForExecution")
	public ModelAndView payForExecution(HttpServletRequest request) {
		String executionId = request.getParameter("forceId");
		String memberToken = request.getHeader("x-memberToken");
		ModelAndView mv = new ModelAndView("app/arbitrationExecution/payEnforcementResult");
		Member member = memberService.getCurrent();

		mv = H5Utils.addPlatform(member, mv);
		mv.addObject("memberToken", memberToken);
		NfsLoanArbitrationExecution execution = loanArbitrationExecutionService.get(Long.parseLong(executionId));
		NfsLoanRecord loanRecord = loanRecordService.get(execution.getLoan());
		if(NfsLoanRecord.Status.repayed.equals(loanRecord.getStatus()) && loanRecord.getCompleteDate() != null) {
			mv.addObject("code", -1);
			mv.addObject("message", "借条已还清，不能申请强执！");
		}
		BigDecimal amount = execution.getFee();
		BigDecimal avlBal = memberActService.getAvlBal(member);
		if(avlBal.compareTo(amount) < 0) {
			mv.addObject("code", -1);
			mv.addObject("message", "账户余额不足，请先充值再操作！");
			return mv;
		}
		if(!execution.getStatus().equals(NfsLoanArbitrationExecution.ExecutionStatus.executionPayment)) {
			mv.addObject("code", -1);
			mv.addObject("message", "强执申请状态已更新，请勿重复操作！");
			return mv;
		}

		try {
			int code = loanArbitrationExecutionService.payForExecutionApply(member, execution);
			if(code== 0) {
				memberMessageService.sendMessage(MemberMessage.Type.EnforcementIngImsLoaner,loanRecord.getId());
				
				sendSmsMsgService.sendMessage("EnforcementIngSmsLoaner", member.getUsername(), null);
				mv.addObject("arbRecordId", execution.getArbitrationId());
				mv.addObject("forceId", execution.getId());
				mv.addObject("loanId", loanRecord.getId());
				mv.addObject("code", 0);
				mv.addObject("message", "强执服务费预支付成功！");
			}
			return mv;
			
		} catch (Exception e) {
			logger.error("会员{}申请强执缴费账户更新异常，异常信息：{}",member.getId(),Exceptions.getStackTraceAsString(e));
			mv.addObject("code",-1);
			mv.addObject("message", "账户异常，操作失败！");
			return mv;
		}
	}
	
}
