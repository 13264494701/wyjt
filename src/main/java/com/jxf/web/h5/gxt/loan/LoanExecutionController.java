package com.jxf.web.h5.gxt.loan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.loan.entity.NfsLoanArbitration;
import com.jxf.loan.entity.NfsLoanArbitrationExecution;
import com.jxf.loan.entity.NfsLoanArbitrationExecution.ExecutionStatus;
import com.jxf.loan.entity.NfsLoanRecord.Status;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanArbitrationExecutionService;
import com.jxf.loan.service.NfsLoanArbitrationService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.utils.LoanUtils;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.gxt.ArbitrationExecutionListResponseResult;
import com.jxf.web.model.gxt.ArbitrationExecutionListResponseResult.ArbitrationExecution;
import com.jxf.wx.user.entity.WxUserInfo;
import com.jxf.wx.user.service.WxUserInfoService;

/**
 * 强执
 * @author liuhuaixin
 *
 */
@Controller("gxtH5LoanArbitrationExecutionController")
@RequestMapping(value="${gxtH5}/execution")
public class LoanExecutionController {
	private static final Logger logger = LoggerFactory.getLogger(LoanExecutionController.class);
	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsLoanArbitrationService loanArbitrationService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsLoanArbitrationExecutionService loanArbitrationExecutionService;
	@Autowired
	private WxUserInfoService wxUserInfoService;
	
	
	
	@RequestMapping(value = "/goToExecutionPage")
	@ResponseBody
	public ResponseData goToExecutionPage(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String loanId = request.getParameter("loanId");
		if (StringUtils.isBlank(loanId)) {
			logger.error("仲裁申请页面跳转请求参数错误：loanId:{}", loanId);
			return ResponseData.error("参数错误");
		}
		NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(loanId));
		if (loanRecord == null) {
			logger.error("仲裁申请页面跳转请求参数错误：loanId:{},找不到对应的借条", loanId);
			return ResponseData.error("参数错误");
		}
		if(!loanRecord.getLoaner().getId().equals(member.getId())) {
			logger.error("token用户和借条用户不匹配！");
			return ResponseData.error("不能操作不属于自己的借条");
		}
		NfsLoanArbitration loanArbitration = loanArbitrationService.getByLoanId(Long.parseLong(loanId));
		if(loanArbitration.getStrongStatus().equals(NfsLoanArbitration.StrongStatus.appliedStrong)) {
			ResponseData.error("此借条已经申请过强执，无法再次申请");
		}
		BigDecimal applyAmount = loanRecord.getDueRepayAmount();
		Map<String, Object> data = new HashMap<String, Object>();
		BigDecimal overdueDays = new BigDecimal(DateUtils.getDistanceOfTwoDate(loanRecord.getDueRepayDate(), new Date()));
		BigDecimal overdueIntrest = LoanUtils.calOverdueInterest(applyAmount, overdueDays);
		// 计算强执服务费
		BigDecimal totalMoney = applyAmount.add(overdueIntrest);
		BigDecimal applyfee = getExecutionFee(totalMoney);
		
		// 借条信息
		data.put("fee", StringUtils.decimalToStr(applyfee, 2));
		data.put("applyAmount", applyAmount);
		data.put("loaneeName", loanRecord.getLoanee().getName());
		data.put("amount", loanRecord.getAmount());
		data.put("intrest", loanRecord.getInterest());
		data.put("overdueIntrest", overdueIntrest);
		data.put("overdueDays", overdueDays);
		// 获取当前用户信息
		Integer verifiedList = member.getVerifiedList();
		data.put("isRealIdentity",VerifiedUtils.isVerified(verifiedList, 1) && VerifiedUtils.isVerified(verifiedList, 2));
		data.put("isPayPsw", VerifiedUtils.isVerified(verifiedList, 22));
		data.put("curBal", StringUtils.decimalToStr(memberService.getAvlBal(member), 2));
		data.put("name", member.getName());
		data.put("username", member.getUsername());
		WxUserInfo wxUserInfo = wxUserInfoService.findByMember(member.getId(), "gxt");
		data.put("headImage", wxUserInfo == null ? "" : wxUserInfo.getHeadImage());
		return ResponseData.success("请求成功", data);
	}
	
	@RequestMapping(value ="/apply")
	@ResponseBody
	public ResponseData apply(HttpServletRequest request) {
		try {
			String arbitrationId = request.getParameter("arbitrationId");
			NfsLoanArbitration loanArbitration = loanArbitrationService.get(Long.valueOf(arbitrationId));
			if (loanArbitration.getStrongStatus().equals(NfsLoanArbitration.StrongStatus.appliedStrong)) {
				return ResponseData.error("此借条已经申请过强执，无法再次申请");
			}
			NfsLoanRecord loanRecord = loanRecordService.get(loanArbitration.getLoan());
			if (loanRecord == null) {
				return ResponseData.error("没有找到对应的借条记录");
			}
			Member member = memberService.getCurrent2();
			if(!loanRecord.getLoaner().getId().equals(member.getId())) {
				logger.error("token用户和借条用户不匹配！");
				return ResponseData.error("不能操作不属于自己的借条");
			}
			BigDecimal fee = getExecutionFee(loanArbitration.getApplyAmount());
			NfsLoanArbitrationExecution arbitrationExecution = loanArbitrationExecutionService.applyForGxt(loanRecord,loanArbitration, fee);
			Map<String, String> data = new HashMap<String,String>();
			if (arbitrationExecution == null) {
				return ResponseData.error("创建强执失败");
			} else {
				data.put("executionId", arbitrationExecution.getId()+"");
				return ResponseData.success("请求成功", data);
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}
		return ResponseData.error("创建强执失败");
	}
	
	@RequestMapping(value ="/list")
	@ResponseBody
	public ResponseData list(HttpServletRequest request) {
		try {
		Member member = memberService.getCurrent();
		Integer pageNo = Integer.valueOf(request.getParameter("pageNo"));
		Integer pageSize = 20;
		String type = request.getParameter("executionType");
		int trxType = 1;
		if(type.equals("1")) {
			NfsLoanArbitration nfsLoanArbitration = new NfsLoanArbitration();
			NfsLoanRecord loanRecordParam = new NfsLoanRecord();
			loanRecordParam.setLoaner(member);
			Page<NfsLoanArbitration> page = loanArbitrationService.findExecution(loanRecordParam.getLoaner().getId(),nfsLoanArbitration,pageNo, pageSize,trxType);
			ArbitrationExecutionListResponseResult result = new ArbitrationExecutionListResponseResult();
			ArrayList<ArbitrationExecution> loanList = new ArrayList<ArbitrationExecution>();
			for(NfsLoanArbitration everyLoanArbitration:page.getList()) {
				NfsLoanRecord nfsLoanRecord = loanRecordService.get(everyLoanArbitration.getLoan().getId());
				if(nfsLoanRecord.getStatus().equals(Status.repayed)) {
					continue;
				}
				ArbitrationExecution arbitrationExecution = new ArbitrationExecution();
				arbitrationExecution.setArbitratId(everyLoanArbitration.getId().toString());
				arbitrationExecution.setHeadImageUrl(nfsLoanRecord.getLoanee().getHeadImage());
				arbitrationExecution.setLoanType(everyLoanArbitration.getLoan().getId().toString() + "_2");
				arbitrationExecution.setStatus("1");
				arbitrationExecution.setInterest(StringUtils.decimalToStr(everyLoanArbitration.getLoan().getInterest(),2));
				arbitrationExecution.setLoanStart(DateUtils.formatDate(nfsLoanRecord.getLoanStart(), "yyyy-MM-dd"));
				arbitrationExecution.setRepayDate(DateUtils.formatDate(everyLoanArbitration.getLoan().getDueRepayDate(), "yyyy-MM-dd"));
				arbitrationExecution.setLoaneeName(nfsLoanRecord.getLoanee().getName());
				arbitrationExecution.setAmount(StringUtils.decimalToStr(nfsLoanRecord.getAmount(), 2));
				loanList.add(arbitrationExecution);
			}
			result.setArbitrationList(loanList);
			return ResponseData.success("成功查询全部可申请强执借条",result);
		}else if(type.equals("2")) {
			NfsLoanArbitrationExecution nfsLoanArbitrationExecution = new NfsLoanArbitrationExecution();
			NfsLoanRecord loanRecordParam = new NfsLoanRecord();
			loanRecordParam.setLoaner(member);
			nfsLoanArbitrationExecution.setStatus(ExecutionStatus.executionPayment);/**2*/
			nfsLoanArbitrationExecution.setLoanerId(loanRecordParam.getLoaner().getId());
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
				arbitrationExecution.setHeadImageUrl(loanee.getHeadImage());
				arbitrationExecution.setLoanType(nfsLoanRecord.getId().toString() + "_2");
				arbitrationExecution.setStatus("2");
				arbitrationExecution.setInterest(StringUtils.decimalToStr(everyLoanArbitrationExecution.getInterest(),2));
				arbitrationExecution.setRepayDate(DateUtils.formatDate(nfsLoanRecord.getDueRepayDate(), "yyyy-MM-dd"));
				arbitrationExecution.setLoanStart(DateUtils.formatDate(nfsLoanRecord.getLoanStart(), "yyyy-MM-dd"));
				arbitrationExecution.setLoaneeName(nfsLoanRecord.getLoanee().getName());
				arbitrationExecution.setAmount(StringUtils.decimalToStr(nfsLoanRecord.getAmount(), 2));
				arbitrationExecution.setExecutionId(everyLoanArbitrationExecution.getId()+"");
				Map<String, Object> userInfo = new HashMap<String,Object>();
				userInfo.put("fee", StringUtils.decimalToStr(everyLoanArbitrationExecution.getFee(), 2));
				userInfo.put("isRealIdentity", VerifiedUtils.isVerified(member.getVerifiedList(), 1)&&VerifiedUtils.isVerified(member.getVerifiedList(), 2));
				userInfo.put("isPayPsw", VerifiedUtils.isVerified(member.getVerifiedList(), 22));
				userInfo.put("curBal", StringUtils.decimalToStr(memberService.getCulBal(member), 2));
				userInfo.put("username", member.getUsername());
				arbitrationExecution.setUserInfo(userInfo);
				loanList.add(arbitrationExecution);
			}
			}
			result.setArbitrationList(loanList);
			return ResponseData.success("成功查询全部待缴费强执借条",result);
		}else if(type.equals("3")) {
			NfsLoanArbitrationExecution nfsLoanArbitrationExecution = new NfsLoanArbitrationExecution();
			NfsLoanRecord loanRecordParam = new NfsLoanRecord();
			loanRecordParam.setLoaner(member);
			nfsLoanArbitrationExecution.setStatus(ExecutionStatus.executionProcessing);/**3*/
			nfsLoanArbitrationExecution.setLoanerId(loanRecordParam.getLoaner().getId());
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
				arbitrationExecution.setHeadImageUrl(loanee.getHeadImage());
				arbitrationExecution.setLoanType(nfsLoanRecord.getId().toString() + "_2");
				arbitrationExecution.setStatus("3");
				arbitrationExecution.setInterest(StringUtils.decimalToStr(everyLoanArbitrationExecution.getInterest(),2));
				arbitrationExecution.setLoanStart(DateUtils.formatDate(nfsLoanRecord.getLoanStart(), "yyyy-MM-dd"));
				arbitrationExecution.setRepayDate(DateUtils.formatDate(nfsLoanRecord.getDueRepayDate(), "yyyy-MM-dd"));
				arbitrationExecution.setLoaneeName(nfsLoanRecord.getLoanee().getName());
				arbitrationExecution.setAmount(StringUtils.decimalToStr(nfsLoanRecord.getAmount(), 2));
				arbitrationExecution.setExecutionId(everyLoanArbitrationExecution.getId()+"");
				loanList.add(arbitrationExecution);
			}
			}
			result.setArbitrationList(loanList);
			return ResponseData.success("成功查询全部进行中强执借条",result);
		}else if(type.equals("4")) {
			/**状态4，5*/
			NfsLoanArbitrationExecution nfsLoanArbitrationExecution = new NfsLoanArbitrationExecution();
			NfsLoanRecord loanRecordParam = new NfsLoanRecord();
			loanRecordParam.setLoaner(member);
			Page<NfsLoanArbitrationExecution> page = loanArbitrationExecutionService.findExecuted(loanRecordParam.getLoaner().getId(),nfsLoanArbitrationExecution,pageNo, pageSize);
			ArbitrationExecutionListResponseResult result = new ArbitrationExecutionListResponseResult();
			ArrayList<ArbitrationExecution> loanList = new ArrayList<ArbitrationExecution>();
			if(page.getList().size()>0) {
			for(NfsLoanArbitrationExecution everyLoanArbitrationExecution:page.getList()) {
				NfsLoanRecord nfsLoanRecord = loanRecordService.get(everyLoanArbitrationExecution.getLoan().getId());
				ArbitrationExecution arbitrationExecution = new ArbitrationExecution();
				arbitrationExecution.setArbitratId(everyLoanArbitrationExecution.getArbitrationId().toString());
				arbitrationExecution.setExecutionId(everyLoanArbitrationExecution.getId().toString());
				Member loanee = memberService.get(everyLoanArbitrationExecution.getLoaneeId());
				if(loanee==null) {
					return ResponseData.error("借款人查询失败");
				}
				arbitrationExecution.setHeadImageUrl(loanee.getHeadImage());
				arbitrationExecution.setLoanType(nfsLoanRecord.getId().toString() + "_2");
				arbitrationExecution.setStatus("4");
				arbitrationExecution.setInterest(StringUtils.decimalToStr(everyLoanArbitrationExecution.getInterest(),2));
				arbitrationExecution.setLoanStart(DateUtils.formatDate(nfsLoanRecord.getLoanStart(), "yyyy-MM-dd"));
				arbitrationExecution.setRepayDate(DateUtils.formatDate(nfsLoanRecord.getDueRepayDate(), "yyyy-MM-dd"));
				arbitrationExecution.setLoaneeName(nfsLoanRecord.getLoanee().getName());
				arbitrationExecution.setAmount(StringUtils.decimalToStr(nfsLoanRecord.getAmount(), 2));
				arbitrationExecution.setExecutionId(everyLoanArbitrationExecution.getId()+"");
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
		return ResponseData.error("查询失败1");
	}
	/**
	 * 获取强执费用
	 * @return
	 */
	private BigDecimal getExecutionFee(BigDecimal amount) {
		if(amount.compareTo(new BigDecimal(6000)) <= 0) {
			return new BigDecimal(200);
		}else if(amount.compareTo(new BigDecimal(6000)) > 0 && amount.compareTo(new BigDecimal(10000)) <=0){
			return new BigDecimal(300);
		}else if(amount.compareTo(new BigDecimal(10000)) > 0 && amount.compareTo(new BigDecimal(20000)) <=0){
			return new BigDecimal(400);
		}else if(amount.compareTo(new BigDecimal(20000)) > 0 && amount.compareTo(new BigDecimal(30000)) <=0){
			return new BigDecimal(500);
		}else if(amount.compareTo(new BigDecimal(30000)) > 0 && amount.compareTo(new BigDecimal(40000)) <=0){
			return new BigDecimal(600);
		}else if(amount.compareTo(new BigDecimal(40000)) > 0 && amount.compareTo(new BigDecimal(100000)) <=0){
			return new BigDecimal(700);
		}else if(amount.compareTo(new BigDecimal(100000)) > 0 && amount.compareTo(new BigDecimal(150000)) <=0){
			return new BigDecimal(800);
		}else {
			//TODO 具体费用怎么算需要评估
			return new BigDecimal(999999);
		}	
	}
	
	
}
