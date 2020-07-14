
package com.jxf.web.h5.gxt.loan;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jxf.svc.security.Principal;
import com.jxf.svc.security.jwt.JwtUtil;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.svc.annotation.AccessLimit;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Constant;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanApply.LoanRole;
import com.jxf.loan.entity.NfsLoanApply.TrxType;
import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.entity.NfsLoanPartialAndDelay;
import com.jxf.loan.entity.NfsLoanPartialAndDelay.PayStatus;
import com.jxf.loan.entity.NfsLoanApplyDetail.Status;
import com.jxf.loan.entity.NfsLoanArbitration;
import com.jxf.loan.entity.NfsLoanContract;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRecord.DelayStatus;
import com.jxf.loan.entity.NfsLoanRecord.LineDownStatus;
import com.jxf.loan.service.NfsLoanApplyDetailService;
import com.jxf.loan.service.NfsLoanApplyService;
import com.jxf.loan.service.NfsLoanArbitrationService;
import com.jxf.loan.service.NfsLoanContractService;
import com.jxf.loan.service.NfsLoanPartialAndDelayService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.utils.LoanUtils;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.gxt.CreditInfoResponseResult;
import com.jxf.web.model.gxt.LoanDetailForGxtResponseResult;
import com.jxf.web.model.gxt.LoanListForGxtResponseResult;
import com.jxf.web.model.gxt.LoanListForGxtResponseResult.LoanForGxt;



/**
 * 公信堂借条
 * 
 * @author XRD
 */
@Controller("gxtH5LoanRecordController")
@RequestMapping(value="${gxtH5}/loan")
public class LoanRecordController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(LoanRecordController.class);

	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsLoanApplyService loanApplyService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsLoanApplyDetailService loanApplyDetailService;
	@Autowired
	private NfsLoanArbitrationService nfsLoanArbitrationService;
	@Autowired
	private NfsLoanPartialAndDelayService loanPartialAndDelayService;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private NfsLoanContractService loanContractService;
	
	/**
	 * 借条详情
	 * loanId ID
	 * type 1detail 2record
	 */
	@RequestMapping(value = "/loanDetailForGxt")
	public @ResponseBody
	ResponseData loanDetailForGxt(HttpServletRequest request,String loan_type,String memberToken) {
		Long id = null;
		if (!StringUtils.isBlank(memberToken)) {		         
			ResponseData responseData= JwtUtil.verifyToken(memberToken);
			if(responseData.getCode()==Constant.JWT_SUCCESS){
				JSONObject payload = (JSONObject) JSON.toJSON(responseData.getResult());
				id = payload.getLong("id");
			
				//检查用户是否在锁定列表中
				if(RedisUtils.isMember("lockedlist", String.valueOf(id))) {
					return ResponseData.error("用户已被锁定,请联系管理员解锁！");
				}
				HttpSession session = request.getSession();
				session.setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(id,memberToken));
			}else{
				return ResponseData.error(responseData.getMessage());
			}
		}
		Member member = memberService.get(id);
		
		if(StringUtils.isBlank(loan_type)) {
			logger.error("借条详情查询失败,借条ID不能为空,当前用户为{}",member.getId());
			return ResponseData.error("借条详情查询失败,借条ID不能为空");
		}
		LoanDetailForGxtResponseResult result  = new LoanDetailForGxtResponseResult();
		String[] split = loan_type.split("_");
		
		try {
			result = loanRecordService.getGxtLoanDetail(split[0],Integer.parseInt(split[1]),member);
		} catch (Exception e) {
			logger.error("报错了问题ID{}",split[0]);
			logger.error(Exceptions.getStackTraceAsString(e));
			return ResponseData.error("后端程序错误!");
		}
		return ResponseData.success("详情查询成功",result);
	}
	
   /**
	*   查询借款申请列表给公信堂展示用
	*	0:借条列表排序 待确认/待放款：按照借条操作时间倒叙排列；  实现了update_time倒叙
	*	1:待收款/待还款：今日还款 借条操作时间倒叙、剩余还款日由小到大排序；          实现了due_repay_date正序
	*	2:已逾期：按照今日逾期、按照逾期天数由小到大排序；                                         实现了due_repay_date倒叙
	*	3:已还款：按照约定的还款时间倒叙排列；                                                              实现了due_repay_date倒叙
	*	loanRole 0借款人 1放款人
	*	status 0待确认/待放款 1待还/待收 2逾期 3已还  4已失效
	*	orderBy 0综合 1金额正序 2金额倒叙 3还款时间正序 4还款时间倒序
	*	usernameOrName 用户名或电话
	*	maxAmount 最大金额
	*	minAmount 最小金额
	*	beginDate 开始时间
	*	endDate 结束日期
	*	pageNo 页码
	*/
	@RequestMapping(value = "/loanListForGxt", method = RequestMethod.POST)
	public @ResponseBody
	ResponseData loanListForApp(HttpServletRequest request,Integer loanRole,Integer status,Integer orderBy
			,String usernameOrName,String maxAmount,String minAmount,String beginDate,String endDate
			,Integer pageNo) {
		Member member = memberService.getCurrent();
		Long memberId = member.getId();
		LoanListForGxtResponseResult result = new LoanListForGxtResponseResult();
		List<LoanForGxt> resultList = new ArrayList<LoanForGxt>();
		Member find = new Member();
		if(StringUtils.isNotBlank(usernameOrName)){
			if(usernameOrName.length() == 11){//输入的是电话
				boolean matches = Pattern.matches("\\d+",usernameOrName);
				if(!matches){
					return ResponseData.error("请输入正确的电话号!");
				}
				find = memberService.findByUsername(usernameOrName);//根据电话号查ID
			}else{
				find.setName(usernameOrName);
			}
		}
		if(status == 0 || status == 4){//查apply
			NfsLoanApply loanApply = new NfsLoanApply();
			loanApply.setTrxType(NfsLoanApply.TrxType.offline);
			List<NfsLoanApply> findList = null;
			if(StringUtils.isNotBlank(beginDate)){
				loanApply.setBeginDate(DateUtils.parseDate(beginDate));
			}
			if(StringUtils.isNotBlank(endDate)){
				Date end = DateUtils.parseDate(endDate);
				loanApply.setEndDate(end);
			}
			if(loanRole != null){
				loanApply.setLoanRole(NfsLoanApply.LoanRole.values()[loanRole]);
			}
			if(StringUtils.isNotBlank(maxAmount)){
				loanApply.setMaxAmount(new BigDecimal(maxAmount));
			}
			if(StringUtils.isNotBlank(minAmount)){
				loanApply.setMinAmount(new BigDecimal(minAmount));
			}
			NfsLoanApplyDetail applyDetail = new NfsLoanApplyDetail();
			applyDetail.setTrxType(NfsLoanApply.TrxType.offline);
			applyDetail.setMember(find);
			if(loanRole == LoanRole.loanee.ordinal()) {
				applyDetail.setPayStatus(NfsLoanApplyDetail.PayStatus.success);
			}
			loanApply.setDetail(applyDetail);
			loanApply.setMember(member);
			
			if(status == 0){
				applyDetail.setStatus(NfsLoanApplyDetail.Status.values()[status]);
			}else if(status == 4){
				applyDetail.setStatus(null);
			}
			Page<NfsLoanApply> page = new Page<NfsLoanApply>(pageNo == null  ? 1 : pageNo ,  20);	
			
			/**
			 * 1金额正序 2金额倒叙 3还款时间正序 4还款时间倒序
			 */
			if(orderBy == 1){
				page.setOrderBy(" c.amount ");
			}else if(orderBy == 2){
				page.setOrderBy(" c.amount desc ");
			}else if(orderBy == 3){
				page.setOrderBy(" date_add(a.loan_start, interval a.term day) "); 
			}else if(orderBy == 4){
				page.setOrderBy(" date_add(a.loan_start, interval a.term day) desc "); 
			}
			loanApply.setPage(page);
			Page<NfsLoanApply> pageApply = loanApplyService.findLoanApplyListForGxt(loanApply);	
			
			if(pageApply != null){
				findList = pageApply.getList();
			}
			if(findList != null && findList.size() > 0){
				for (NfsLoanApply nfsLoanApply : findList) {
					LoanForGxt loanForGxt = new LoanForGxt();
					NfsLoanApplyDetail detail = nfsLoanApply.getDetail();
					detail = loanApplyDetailService.get(detail);
					
					Long applyId = detail.getId();
					Member applyMember = nfsLoanApply.getMember();
					
					if(memberId.equals(applyMember.getId())){//我是发起者
						loanForGxt.setPartnerHeadImage(detail.getMember().getHeadImage()==null?
								Global.getConfig("domain")+Global.getConfig("default.headImage")
								:detail.getMember().getHeadImage());
						loanForGxt.setPartnerName(detail.getMember().getName());
						
					}else{
						loanForGxt.setPartnerHeadImage(applyMember.getHeadImage()==null?
								Global.getConfig("domain")+Global.getConfig("default.headImage")
								:applyMember.getHeadImage());
						loanForGxt.setPartnerName(applyMember.getName());
					}
					String amountStr = StringUtils.decimalToStr(detail.getAmount(), 2);
					loanForGxt.setLoanId(applyId.toString());
					loanForGxt.setLoan_type(applyId.toString()+"_1");
					
					loanForGxt.setAmount(amountStr);
					
					if(nfsLoanApply.getInterest() != null){
						loanForGxt.setInterest(nfsLoanApply.getInterest().setScale(2, BigDecimal.ROUND_HALF_UP)+"");
					}else{
						loanForGxt.setInterest("0");
					}
					
					Integer term = nfsLoanApply.getTerm();
					Date loanStart = nfsLoanApply.getLoanStart();
					Date repayDate = DateUtils.addCalendarByDate(loanStart, term-1);
					loanForGxt.setRepayDate(DateUtils.formatDate(repayDate, "yyyy-MM-dd"));
					
					loanForGxt.setIconStatus(0);
					
					/**
					 * 	0:待确认
						1:已取消
						2:已拒绝
						3:已超时
					 */
					NfsLoanApplyDetail.Status detailStatus = detail.getStatus();
					if(detailStatus.equals(NfsLoanApplyDetail.Status.pendingAgree)){
						loanForGxt.setStatus(0);
					}else if(detailStatus.equals(NfsLoanApplyDetail.Status.canceled)){
						loanForGxt.setStatus(1);
					}else if(detailStatus.equals(NfsLoanApplyDetail.Status.reject)){
						loanForGxt.setStatus(2);
					}else if(detailStatus.equals(NfsLoanApplyDetail.Status.expired)){
						loanForGxt.setStatus(3);
					} 
					loanForGxt.setLoanStart(DateUtils.formatDate(nfsLoanApply.getLoanStart(), "yyyy-MM-dd"));
					resultList.add(loanForGxt);
				}
			}
		}else{//查record
			NfsLoanRecord loanRecord = new NfsLoanRecord();	
			loanRecord.setTrxType(TrxType.offline);
			if(StringUtils.isNotBlank(beginDate)){
				loanRecord.setBeginDueRepayDate(DateUtils.parseDate(beginDate));
			}
			if(StringUtils.isNotBlank(endDate)){
				Date end = DateUtils.parseDate(endDate);
				loanRecord.setEndDueRepayDate(DateUtils.addCalendarByDate(end, 1));
			}
			if(loanRole == 0){
				loanRecord.setLoanee(member);
				loanRecord.setLoaneeDelete(false);
				loanRecord.setLoaner(find);
			}else{
				loanRecord.setLoaner(member);
				loanRecord.setLoanerDelete(false);
				loanRecord.setLoanee(find);
			}
			if(StringUtils.isNotBlank(maxAmount)){
				loanRecord.setMaxAmount(new BigDecimal(maxAmount));
			}
			if(StringUtils.isNotBlank(minAmount)){
				loanRecord.setMinAmount(new BigDecimal(minAmount));
			}
			
			
			Page<NfsLoanRecord> pageRecord = loanRecord.getPage();	
			
			/**
			 * 1金额正序 2金额倒叙 3还款时间正序 4还款时间倒序
			 */
			if(orderBy == 1){
				pageRecord.setOrderBy(" a.amount ");
			}else if(orderBy == 2){
				pageRecord.setOrderBy(" a.amount desc ");
			}else if(orderBy == 3){
				pageRecord.setOrderBy(" a.due_repay_date "); 
			}else if(orderBy == 4){
				pageRecord.setOrderBy(" a.due_repay_date desc "); 
			}
			loanRecord.setPage(pageRecord);
			
			if(status == 1){//剩余还款日由小到大排序；
				loanRecord.setStatus(NfsLoanRecord.Status.values()[0]);
				pageRecord = loanRecordService.findToPaidPage(loanRecord, pageNo, 20);		
			}else if(status == 2){//按照逾期天数由小到大排序；
				loanRecord.setStatus(NfsLoanRecord.Status.values()[2]);
				pageRecord = loanRecordService.findClosedPage(loanRecord, pageNo, 20);		
			}else if(status == 3){//按照还款时间倒叙排列； 
				loanRecord.setStatus(NfsLoanRecord.Status.values()[1]);
				pageRecord = loanRecordService.findClosedPage(loanRecord, pageNo, 20);		
			}else{
				pageRecord = loanRecordService.findPage(loanRecord, pageNo, 20);		
			}
			
			for(NfsLoanRecord record:pageRecord.getList()) {
				LoanForGxt loan = new LoanForGxt();
				String amountStr = StringUtils.decimalToStr(record.getAmount(), 2);
				loan.setAmount(amountStr);
				loan.setInterest(record.getInterest().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				loan.setLoanId(record.getId().toString());
				loan.setLoan_type(record.getId().toString()+"_2");
				Member partner = null;
				if(loanRole == 0){
					partner = record.getLoaner();
				}else{
					partner = record.getLoanee();
				}
				loan.setPartnerHeadImage(partner.getHeadImage()==null?
						Global.getConfig("domain")+Global.getConfig("default.headImage")
						:partner.getHeadImage());
				loan.setPartnerName(partner.getName());
				
				Date dueRepayDate = record.getDueRepayDate();
				String formatDueRepayDate = DateUtils.formatDate(dueRepayDate, "yyyy-MM-dd");
				loan.setRepayDate(formatDueRepayDate);
				
				//0:不显示 1仲裁 2强执 
				loan.setIconStatus(0);
				
				//仲裁
				NfsLoanArbitration nfsLoanArbitration = new NfsLoanArbitration();
				nfsLoanArbitration.setLoan(record);
				List<NfsLoanArbitration> findArbitrationList = nfsLoanArbitrationService.findList(nfsLoanArbitration);
				if(findArbitrationList != null && findArbitrationList.size() > 0){
					nfsLoanArbitration = findArbitrationList.get(0);
					com.jxf.loan.entity.NfsLoanArbitration.Status arbitrationStatus = nfsLoanArbitration.getStatus();
					if(arbitrationStatus.equals(NfsLoanArbitration.Status.arbitrationFailure) || arbitrationStatus.equals(NfsLoanArbitration.Status.auditFailure)
							|| arbitrationStatus.equals(NfsLoanArbitration.Status.failureToFile)){
						loan.setIconStatus(0);
					}else{
						loan.setIconStatus(1);
					}
				}
				
				/**
				 *  4:今日还款/今日收款
					5:距离还款/收款日30天之内
					6:距离还款/收款日30天以上
					7:延期待确认
					8:还款/收款待确认
					9:今日逾期
					10:已逾期未超过15天
					11:已逾期超过15天
					12:已完成
				**/
				Integer progress = loanRecordService.getGxtLoanRecordProgress(record);
				loan.setStatus(progress);
				
				int pastDays = CalendarUtil.getIntervalDays(new Date(),record.getDueRepayDate());
				if(progress == 5){
					pastDays ++; //过去的天数
				}
				if(progress == 5 || progress == 10){
					loan.setDays(pastDays);
				}
				
				loan.setLoanStart(DateUtils.formatDate(record.getLoanStart(), "yyyy-MM-dd"));
				resultList.add(loan);
			}
		}
		result.setLoanList(resultList);
		return ResponseData.success("查询借款申请列表成功", result);
	}
	/**
	 * 放款人同意借款人的申请
	 * loanId detail的ID
	 * pwd 交易密码
	 * disputeResolution 争议解决方式 0仲裁 1诉讼
	 */
	@RequestMapping(value = "/loanerAgreeApply", method = RequestMethod.POST)
	public @ResponseBody
	ResponseData loanerAgreeApply(HttpServletRequest request,String loanId,String pwd,Integer disputeResolution) {
		if(StringUtils.isBlank(loanId)) {
			return ResponseData.error("交易失败,请联系客服！");
		}
		Member member = memberService.getCurrent();
		if(member.getId()==2597123L) {
			return ResponseData.error("您既往行为触发平台风控规则,无法继续使用补借条功能,如有疑问请联系客服！");
		}
		
		ResponseData checkPayPwd = memberService.checkPayPwd(pwd, member);
		if(checkPayPwd == null) {
			return ResponseData.error("系统错误，请联系客服处理！");
		}
		if (checkPayPwd.getCode() != 0) {
			return ResponseData.error(checkPayPwd.getMessage());
		}
//		//校验是否是申请上对应的人
		NfsLoanApplyDetail applyDetail = loanApplyDetailService.get(Long.parseLong(loanId));
		if(!StringUtils.equals(member.getName(), applyDetail.getMember().getName())){
			return ResponseData.error("您无法操作与本人无关的借条");
		}
		Status status = applyDetail.getStatus();
		if(!applyDetail.getStatus().equals(NfsLoanApplyDetail.Status.pendingAgree)) {
			String statusName = getStatusName(status);
			return ResponseData.error("借条"+statusName+"，不能进行此操作");
		}	
		try {
			applyDetail.setDisputeResolution(NfsLoanApplyDetail.DisputeResolution.values()[disputeResolution]);
			applyDetail.setMember(member);
			NfsLoanRecord loanRecord = loanRecordService.createLoanRecordForOffLine(applyDetail);
			if(loanRecord == null) {
				return ResponseData.error("借条申请异常，生成借条失败！");
			}
			//LoanDetailForGxtResponseResult result = loanRecordService.getGxtLoanDetail(loanRecord.getId().toString(), 2,member);
			Map<String, String> data = new HashMap<String,String>();
			data.put("loan_type", loanRecord.getId() + "_2");
			return ResponseData.success("放款人同意借款人的申请成功",data);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
			return ResponseData.error("账户异常，生成借条失败！");
		}
	}
	
	/**
	 * 删除申请/借条
	 * loanId 申请/借条 ID
	 * type 1 detail 2 record
	 */
	@RequestMapping(value = "/deleteLoan", method = RequestMethod.POST)
	public @ResponseBody
	ResponseData deleteLoan(HttpServletRequest request,String loan_type) {
		Member member = memberService.getCurrent();
		if(StringUtils.isBlank(loan_type)) {
			return ResponseData.error("交易失败,请联系客服！");
		}
		String[] split = loan_type.split("_");
		int type = Integer.parseInt(split[1]);
		String loanId = split[0];
		if( type== 1){//detail
			NfsLoanApplyDetail applyDetail = loanApplyDetailService.get(Long.parseLong(loanId));
			Status detailStatus = applyDetail.getStatus();
			
			if(detailStatus.equals(NfsLoanApplyDetail.Status.pendingAgree) ||
				detailStatus.equals(NfsLoanApplyDetail.Status.success)) {
				return ResponseData.error("状态异常,不能删除");
			}
			
			if(member.equals(applyDetail.getMember())){
				loanApplyDetailService.delete(applyDetail);
			}else if(member.equals(applyDetail.getApply().getMember())){//我是发起者
				loanApplyService.delete(applyDetail.getApply());
			}else{
				return ResponseData.error("您无法操作与本人无关的借条");
			}
		}else if(type == 2){//record
			NfsLoanRecord loanRecord = loanRecordService.get(Long.parseLong(loanId));
			NfsLoanRecord.Status status = loanRecord.getStatus();
			if(!status.equals(NfsLoanRecord.Status.repayed)){
				return ResponseData.error("借条还未结束,不能删除");
			}
			if(member.equals(loanRecord.getLoanee())){
				loanRecord.setLoaneeDelete(true);
			}else if(member.equals(loanRecord.getLoaner())){
				loanRecord.setLoanerDelete(true);
			}else{
				return ResponseData.error("您无法操作与本人无关的借条");
			}
			loanRecordService.save(loanRecord);
		}
		return ResponseData.success("删除成功");
	}
	/**
	 * 借款人申请销借条
	 * loanId ID
	 */
	@RequestMapping(value = "/loaneeApplyCloseLoan")
	public @ResponseBody
	ResponseData loaneeApplyCloseLoan(HttpServletRequest request,String loanId) {
		if(StringUtils.isBlank(loanId)) {
			return ResponseData.error("交易失败,请联系客服！");
		}
		Member member = memberService.getCurrent();
		NfsLoanRecord loanRecord = loanRecordService.get(Long.parseLong(loanId));
		NfsLoanRecord.Status status = loanRecord.getStatus();
		LineDownStatus lineDownStatus = loanRecord.getLineDownStatus();
		if(!member.equals(loanRecord.getLoanee())){
			return ResponseData.error("您无法操作与本人无关的借条");
		}
		if(status.equals(NfsLoanRecord.Status.repayed)){
			return ResponseData.error("借条状态为已还,请刷新后操作");
		}
		if(lineDownStatus.equals(LineDownStatus.loaneeLineDownRepayment)){
			return ResponseData.error("请勿重复操作.");
		}
		loanRecord.setLineDownStatus(LineDownStatus.loaneeLineDownRepayment);
		loanRecordService.save(loanRecord);
		
		//发送会员消息
		memberMessageService.sendMessage(MemberMessage.Type.repaymentApplication,loanRecord.getId());
		
		memberMessageService.sendMessage(MemberMessage.Type.repaymentApplicationLineDown,loanRecord.getId());
		HashMap<String, Object> result = new HashMap<String,Object>();
		result.put("loan_type", loanId + "_2");
		return ResponseData.success("申请还款成功",result);
	}
	/**
	 * 借款人取消申请销借条
	 * loanId ID
	 */
	@RequestMapping(value = "/loaneeCancelApplyClose")
	public @ResponseBody
	ResponseData loaneeCancelApplyClose(HttpServletRequest request,String loanId) {
		if(StringUtils.isBlank(loanId)) {
			return ResponseData.error("交易失败,请联系客服！");
		}
		Member member = memberService.getCurrent();
		NfsLoanRecord loanRecord = loanRecordService.get(Long.parseLong(loanId));
		NfsLoanRecord.Status status = loanRecord.getStatus();
		LineDownStatus lineDownStatus = loanRecord.getLineDownStatus();
		if(!member.equals(loanRecord.getLoanee())){
			return ResponseData.error("您无法操作与本人无关的借条");
		}
		if(status.equals(NfsLoanRecord.Status.repayed)){
			return ResponseData.error("借条状态为已还,请刷新后操作");
		}
		if(!lineDownStatus.equals(LineDownStatus.loaneeLineDownRepayment)){
			return ResponseData.error("状态异常");
		}
		loanRecord.setLineDownStatus(LineDownStatus.initial);
		loanRecordService.save(loanRecord);
		
		//发送会员消息
		memberMessageService.sendMessage(MemberMessage.Type.cancelRepaymentApplication,loanRecord.getId());
		
		HashMap<String, String> result = new HashMap<String,String>();
		result.put("loan_type", loanRecord.getId()+"_2");
		return ResponseData.success("申请还款成功",result);
	}
	/**
	 * 放款人拒绝关借条
	 * loanId ID
	 */
	@RequestMapping(value = "/loanerRejectCloseLoan")
	public @ResponseBody
	ResponseData loanerRejectCloseLoan(HttpServletRequest request,String loanId) {
		if(StringUtils.isBlank(loanId)) {
			return ResponseData.error("交易失败,请联系客服！");
		}
		Member member = memberService.getCurrent();
		NfsLoanRecord loanRecord = loanRecordService.get(Long.parseLong(loanId));
		NfsLoanRecord.Status status = loanRecord.getStatus();
		LineDownStatus lineDownStatus = loanRecord.getLineDownStatus();
		if(!member.equals(loanRecord.getLoaner())){
			return ResponseData.error("您无法操作与本人无关的借条");
		}
		if(status.equals(NfsLoanRecord.Status.repayed)){
			return ResponseData.error("借条状态为已还,请刷新后操作");
		}
		if(!lineDownStatus.equals(LineDownStatus.loaneeLineDownRepayment)){
			return ResponseData.error("借条的状态已变更");
		}
		loanRecordService.replyLineDown(loanRecord, "0");
		
		HashMap<String, String> result = new HashMap<String,String>();
		result.put("loan_type", loanRecord.getId()+"_2");
		return ResponseData.success("申请还款成功",result);
	}
	/**
	 * 放款人主动销借条
	 * loanId ID
	 */
	@RequestMapping(value = "/loanerCloseLoan")
	public @ResponseBody
	ResponseData loanerCloseLoan(HttpServletRequest request,String loanId,String pwd) {
		if(StringUtils.isBlank(loanId)) {
			return ResponseData.error("交易失败,请联系客服！");
		}
		Member member = memberService.getCurrent();
		
		ResponseData checkPayPwd = memberService.checkPayPwd(pwd, member);
		if(checkPayPwd == null) {
			return ResponseData.error("系统错误，请联系客服处理！");
		}
		if (checkPayPwd.getCode() != 0) {
			return ResponseData.error(checkPayPwd.getMessage());
		}
		NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(loanId));
		
		if(!member.equals(loanRecord.getLoaner())){
			return ResponseData.error("当前用户不是放款人,无法主动销借条");
		}
		LineDownStatus lineDownStatus = loanRecord.getLineDownStatus();
		if(!lineDownStatus.equals(LineDownStatus.initial)){
			return ResponseData.error("借条的状态已变更");		
		}
		
		if(loanRecord.getStatus().equals(NfsLoanRecord.Status.repayed)){
			return ResponseData.error("借条已关闭");
		}
		//变更借条状态和账户变更 放一个事务里
		loanRecordService.closeLoanLineDown(loanRecord);
		
		//发送会员消息
		memberMessageService.sendMessage(MemberMessage.Type.activeElimination,loanRecord.getId());
		
		HashMap<String, String> result = new HashMap<String,String>();
		result.put("loan_type", loanRecord.getId()+"_2");
		return ResponseData.success("已成功销借条" , result);		
	}
	/**
	 * 放款人同意销借条
	 * loanId ID
	 */
	@RequestMapping(value = "/loanerAgreeCloseLoan")
	public @ResponseBody
	ResponseData loanerAgreeCloseLoan(HttpServletRequest request,String loanId,String pwd) {
		if(StringUtils.isBlank(loanId)) {
			return ResponseData.error("交易失败,请联系客服！");
		}
		Member member = memberService.getCurrent();
		
		ResponseData checkPayPwd = memberService.checkPayPwd(pwd, member);
		if(checkPayPwd == null) {
			return ResponseData.error("系统错误，请联系客服处理！");
		}
		if (checkPayPwd.getCode() != 0) {
			return ResponseData.error(checkPayPwd.getMessage());
		}
		NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(loanId));
		if(!member.equals(loanRecord.getLoaner())){
			return ResponseData.error("当前用户不是放款人,无法主动销借条");
		}
		LineDownStatus lineDownStatus = loanRecord.getLineDownStatus();
		if(!lineDownStatus.equals(LineDownStatus.loaneeLineDownRepayment)){
			return ResponseData.error("借条的状态已变更");		
		}
		
		if(loanRecord.getStatus().equals(NfsLoanRecord.Status.repayed)){
			return ResponseData.error("借条已关闭");
		}
		//变更借条状态和账户变更 放一个事务里
		loanRecordService.closeLoanLineDown(loanRecord);
		
		//发送会员消息
		memberMessageService.sendMessage(MemberMessage.Type.agreeRepaymentApplication,loanRecord.getId());
		
		HashMap<String, String> result = new HashMap<String,String>();
		result.put("loan_type", loanRecord.getId()+"_2");
		return ResponseData.success("已成功销借条" , result);		
	}
	
	/**
	 * 放款人同意延期
	 * loanId ID
	 */
	@RequestMapping(value = "/loanerAgreeDelay")
	public @ResponseBody
	ResponseData loanerAgreeDelay(HttpServletRequest request,String loanId,String pwd) {
		if(StringUtils.isBlank(loanId)) {
			return ResponseData.error("交易失败,请联系客服！");
		}
		Member member = memberService.getCurrent();
		
		ResponseData checkPayPwd = memberService.checkPayPwd(pwd, member);
		if(checkPayPwd == null) {
			return ResponseData.error("系统错误，请联系客服处理！");
		}
		if (checkPayPwd.getCode() != 0) {
			return ResponseData.error(checkPayPwd.getMessage());
		}
		NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(loanId));
		HashMap<String, String> result = new HashMap<String,String>();
		result.put("loan_type", loanRecord.getId()+"_2");
		
		DelayStatus delayStatus = loanRecord.getDelayStatus();
		if(delayStatus.equals(DelayStatus.initial)){
			return ResponseData.error("状态已变更");
		}
		
		NfsLoanPartialAndDelay loanPartialRepayApply = new NfsLoanPartialAndDelay();
		loanPartialRepayApply.setLoan(loanRecord);
		loanPartialRepayApply.setStatus(NfsLoanPartialAndDelay.Status.confirm);
		List<NfsLoanPartialAndDelay> findList = loanPartialAndDelayService.findList(loanPartialRepayApply);
		
		if(findList != null && findList.size() > 0){
			loanPartialRepayApply = findList.get(0);
		}else{
			//LoanDetailForGxtResponseResult detail = loanRecordService.getGxtLoanDetail(loanRecord.getId().toString(),2,member);
			return ResponseData.success("申请已结束",result);
		}
		try {
			//放一个事务里
			ResponseData loanerAgree = loanPartialAndDelayService.answerDelay(loanRecord,member,loanPartialRepayApply,1);
			if(loanerAgree.getCode() != 0 ){
				return ResponseData.error(loanerAgree.getMessage());
			}else{
				//返回详情
				//LoanDetailForGxtResponseResult detail = loanRecordService.getGxtLoanDetail(loanRecord.getId().toString(),2,member);
				
				return ResponseData.success("同意延期成功",result);
			}
		}catch(Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
			return ResponseData.error("操作失败，请稍候重试！");
			
		}
	}
	/**
	 * 放款人/借款人 拒绝延期
	 * loanId ID
	 */
	@RequestMapping(value = "/rejectDelay")
	public @ResponseBody
	ResponseData rejectDelay(HttpServletRequest request,String loanId) {
		if(StringUtils.isBlank(loanId)) {
			return ResponseData.error("交易失败,请联系客服！");
		}
		Member member = memberService.getCurrent();
		
		NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(loanId));
		NfsLoanPartialAndDelay loanPartialRepayApply = new NfsLoanPartialAndDelay();
		loanPartialRepayApply.setLoan(loanRecord);
		loanPartialRepayApply.setStatus(NfsLoanPartialAndDelay.Status.confirm);
		List<NfsLoanPartialAndDelay> findList = loanPartialAndDelayService.findList(loanPartialRepayApply);
		
		if(!Collections3.isEmpty(findList)){
			loanPartialRepayApply = findList.get(0);
		}else{
			LoanDetailForGxtResponseResult detail = loanRecordService.getGxtLoanDetail(loanRecord.getId().toString(),2,member);
			return ResponseData.success("申请已结束",detail);
		}
		try {
			//放一个事务里
			ResponseData loanerAgree = loanPartialAndDelayService.answerDelay(loanRecord,member,loanPartialRepayApply,0);
			if(loanerAgree.getCode() != 0 ){
				return ResponseData.error(loanerAgree.getMessage());
			}else{
				//返回详情
				//LoanDetailForGxtResponseResult detail = loanRecordService.getGxtLoanDetail(loanRecord.getId().toString(),2,member);
				Map<String, Object> result = new HashMap<String,Object>();
				result.put("loan_type", loanRecord.getId()+"_2");
				return ResponseData.success("拒绝延期成功",result);
			}
		}catch(Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
			return ResponseData.error("操作失败，请稍候重试！");
			
		}
	}
	/**
	 * 申请延期
	 * loanId ID
	 */
	@RequestMapping(value = "/applyDelay")
	public @ResponseBody
	ResponseData applyDelay(HttpServletRequest request,String loanId,String repayDateAfterDelay,String delayIntRate,String pwd) {
		
		Member member = memberService.getCurrent();

		if(!StringUtils.isNumeric(delayIntRate)){
			return ResponseData.error("延期利率应为整数");
		}
		BigDecimal big_delayIntRate = new BigDecimal(delayIntRate);
		if(big_delayIntRate.compareTo(new BigDecimal(24)) > 0){
			return ResponseData.error("延期期间利率不能超过24%");
		}
		NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(loanId));

		ResponseData checkPayPwd = memberService.checkPayPwd(pwd, member);
		if(checkPayPwd == null) {
			return ResponseData.error("系统错误，请联系客服处理！");
		}
		if (checkPayPwd.getCode() != 0) {
			return ResponseData.error(checkPayPwd.getMessage());
		}
		BigDecimal amount = loanRecord.getAmount();//本期本金
		BigDecimal interest = loanRecord.getInterest();//本期利息
		BigDecimal intRate = loanRecord.getIntRate();//本期利率
		
		//延期校验
		if(StringUtils.isNotBlank(repayDateAfterDelay)){
			Date repayLatestDate = DateUtils.parseDate(repayDateAfterDelay);
			if (repayLatestDate.getTime() < loanRecord.getDueRepayDate().getTime()) {
				return ResponseData.error("延期时长必须大于还款日");
			}
			if (DateUtils.addCalendarByMonth(loanRecord.getDueRepayDate(), 12).getTime() < repayLatestDate.getTime()) {
				return ResponseData.error("最长延期时长为一年");
			}
			int term = DateUtils.getDifferenceOfTwoDate(repayLatestDate, loanRecord.getLoanStart()) + 1;
			if(term > 1095){
				return ResponseData.error("借条时长最多3年");
			}
			if (!loanRecord.getDelayStatus().equals(NfsLoanRecord.DelayStatus.initial)) {
				return ResponseData.error("已申请延期等待对方确认,请不要重复操作");
			}
		}
		
		NfsLoanPartialAndDelay nfsLoanPartialAndDelay = new NfsLoanPartialAndDelay();
		nfsLoanPartialAndDelay.setLoan(loanRecord);
		nfsLoanPartialAndDelay.setPayStatus(PayStatus.waitingPay);
		nfsLoanPartialAndDelay.setMember(member);
		List<NfsLoanPartialAndDelay> payingList = loanPartialAndDelayService.findList(nfsLoanPartialAndDelay);
		Long oldApplyId = null;
		if(!Collections3.isEmpty(payingList)) {
			oldApplyId = payingList.get(0).getId();
		}
		
		NfsLoanPartialAndDelay loanDelayApply = new NfsLoanPartialAndDelay();
		loanDelayApply.setMember(member);		
		loanDelayApply.setMemberRole(NfsLoanApply.LoanRole.loanee);
		//借款人申请，等服务费支付成功后更新延期状态为待确认
		loanDelayApply.setStatus(NfsLoanPartialAndDelay.Status.other);
		if(member.getId().longValue() == loanRecord.getLoaner().getId().longValue()) {
			loanDelayApply.setMemberRole(NfsLoanApply.LoanRole.loaner);
			loanDelayApply.setStatus(NfsLoanPartialAndDelay.Status.confirm);
			loanRecord.setDelayStatus(DelayStatus.loanerApplyDelay);
		}
		loanDelayApply.setLoan(loanRecord);
		
		Date repayLatestDate = DateUtils.parseDate(repayDateAfterDelay);
		int delayDays = DateUtils.getDistanceOfTwoDate(repayLatestDate, loanRecord.getDueRepayDate());
		loanDelayApply.setDelayDays(delayDays);
		BigDecimal nowAmount = amount.add(interest);//延期本金
	
		
		loanRecordService.save(loanRecord);
		
		//计算延期期间的还款计划
		NfsLoanRecord afterDelayRecord = LoanUtils.calInt(nowAmount, big_delayIntRate, NfsLoanApply.RepayType.oneTimePrincipalAndInterest, delayDays);
		if(oldApplyId != null) {
			loanDelayApply.setId(oldApplyId);
			loanDelayApply.setIsNewRecord(false);
		}
		loanDelayApply.setDelayRate(big_delayIntRate);
		loanDelayApply.setDelayInterest(afterDelayRecord.getInterest());
		loanDelayApply.setOldInterest(interest);
		loanDelayApply.setOldRate(intRate);
		loanDelayApply.setOldAmount(amount);
		loanDelayApply.setPartialAmount(BigDecimal.ZERO);
		loanDelayApply.setRemainAmount(nowAmount);
		loanDelayApply.setNowRepayDate(DateUtils.parseDate(repayDateAfterDelay));
		loanDelayApply.setType(NfsLoanPartialAndDelay.Type.delay);
		loanDelayApply.setNowInterest(afterDelayRecord.getInterest());
		loanDelayApply.setPayStatus(PayStatus.waitingPay);
		loanPartialAndDelayService.save(loanDelayApply);
		
		//发送会员消息
		memberMessageService.sendMessage(MemberMessage.Type.delayApplicationLoanee,loanDelayApply.getId());

		memberMessageService.sendMessage(MemberMessage.Type.delayApplicationLoaner,loanDelayApply.getId());
		
		Map<String, Object> result = new HashMap<String,Object>();
		result.put("applyId", loanDelayApply.getId()+"");
		return ResponseData.success("申请延期成功",result);
	}
	/**
	 * 取消延期
	 * loanId ID
	 */
	@RequestMapping(value = "/cancelDelay")
	public @ResponseBody
	ResponseData cancelDelay(HttpServletRequest request,String loanId) {
		Member member = memberService.getCurrent();
		NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(loanId));
		Map<String, Object> result = new HashMap<String,Object>();
		result.put("loan_type", loanRecord.getId() + "_2");
		
		DelayStatus delayStatus = loanRecord.getDelayStatus();
		if(delayStatus.equals(DelayStatus.initial)){
			return ResponseData.success("状态已变更",result);
		}
		NfsLoanPartialAndDelay nfsLoanPartialRepayApply = new NfsLoanPartialAndDelay();
		nfsLoanPartialRepayApply.setLoan(loanRecord);
		nfsLoanPartialRepayApply.setStatus(NfsLoanPartialAndDelay.Status.confirm);
		List<NfsLoanPartialAndDelay> findList = loanPartialAndDelayService.findList(nfsLoanPartialRepayApply);
		NfsLoanPartialAndDelay partialAndDelay = null;
		if(findList != null && findList.size() > 0){
			partialAndDelay = findList.get(0);
		}else{
			return ResponseData.success("申请已结束",result);
		}
		if(!StringUtils.equals(partialAndDelay.getMember().getName(), member.getName())) {
			return ResponseData.error("不能操作不属于自己的借条");
		}
		try {
			LoanRole memberRole = partialAndDelay.getMemberRole();
			if (memberRole.equals(LoanRole.loanee)) {
				// 借款人申请 需退还服务费

				int code = loanPartialAndDelayService.loaneeCancelDelay(partialAndDelay);
				if (code == Constant.UPDATE_FAILED) {
					return ResponseData.error("退还服务费异常，请联系客服");
				}

			} else {
				partialAndDelay.setStatus(NfsLoanPartialAndDelay.Status.canceled);
				loanPartialAndDelayService.save(partialAndDelay);
			}
		} catch (Exception e) {
			logger.error("延期申请：{}取消退还服务费时异常：{}",partialAndDelay.getId(),Exceptions.getStackTraceAsString(e));
			return ResponseData.error("退还服务费异常，请联系客服");
		}
		loanRecord.setDelayStatus(NfsLoanRecord.DelayStatus.initial);
		loanRecordService.save(loanRecord);
		
		//消息
		memberMessageService.sendMessage(MemberMessage.Type.cancelDelayApplicationLoaner,partialAndDelay.getId());
		return ResponseData.success("已取消",result);
	}
	
	/**
	 * 	用户借贷详情
	 * @param request
	 * @return
	 */
	@AccessLimit(maxCount = 1, seconds = 1)
	@RequestMapping(value="loanInfo")
	@ResponseBody
	public ResponseData loanInfo(String id) {
		Member member = memberService.get(Long.valueOf(id));
		if(member == null) {
			return ResponseData.error("没有权限查看,请稍后再试");
		}
		if(!VerifiedUtils.isVerified(member.getVerifiedList(), 1)) {
			return ResponseData.error("该用户未进行实名认证,请先去实名认证");
		}
		CreditInfoResponseResult result = loanRecordService.getLoanInfo(member);
				
		return ResponseData.success("成功查询用户详情", result);
	}

	@RequestMapping(value = "getDownloadUrl")
	@ResponseBody
	public ResponseData getDownloadUrl(HttpServletRequest request) {
		String loanId = request.getParameter("loanId");
		Map<String, String> data = new HashMap<String,String>();
		String exampleUrl = "https://prod.51jt.com/mb/gxt/gxtAgreement.html";
		data.put("downloadUrl", exampleUrl);
		if(StringUtils.isBlank(loanId)) {
			return ResponseData.success("请求成功",data);
		}
		Member member = memberService.getCurrent2();
		NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(loanId));
		if(loanRecord == null) {
			return ResponseData.success("请求成功",data);
		}
		if(!member.getId().equals(loanRecord.getLoanee().getId()) && !member.getId().equals(loanRecord.getLoaner().getId())) {
			return ResponseData.success("请求成功",data);
		}
		NfsLoanContract loanContract = new NfsLoanContract();
		loanContract.setLoanId(Long.valueOf(loanId));
		List<NfsLoanContract> list = loanContractService.getContractByLoanId(loanContract);
		if(Collections3.isEmpty(list)) {
			return ResponseData.success("请求成功",data);
		}
		NfsLoanContract nfsLoanContract = list.get(0);
		String contractUrl = nfsLoanContract.getContractUrl();
		if(StringUtils.isBlank(contractUrl)) {
			return ResponseData.success("请求成功", data);
		}
		contractUrl = Global.getConfig("domain") + contractUrl;
		data.put("downloadUrl", contractUrl);
		return ResponseData.success("请求成功", data);
	}
	
	@RequestMapping(value = "getLoanContractData")
	@ResponseBody
	public ResponseData getLoanContractData(HttpServletRequest request) {
		String loanId = request.getParameter("loanId");
		
		Member member = memberService.getCurrent2();

		Map<String, String> data = new HashMap<String,String>();
		
		NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(loanId));

		Member loaner = memberService.get(loanRecord.getLoaner());
		Member loanee = memberService.get(loanRecord.getLoanee());

		if(!member.equals(loanee) && !member.equals(loaner)) {
			return ResponseData.error("不能查看不属于自己的借款合同");
		}
		
		data.put("loanerName", loaner.getName());
		data.put("loaneeName", loanee.getName());
		data.put("loanerIdNo", loaner.getIdNo());
		data.put("loaneeIdNo", loanee.getIdNo());
		data.put("loanerPhoneNo", loaner.getUsername());
		data.put("loaneePhoneNo", loanee.getUsername());
		data.put("loanerEmail", loaner.getEmail());
		data.put("loaneeEmail", loanee.getEmail());
		
		data.put("loanNo", loanRecord.getLoanNo());
		data.put("amount", StringUtils.decimalToStr(loanRecord.getAmount(), 2));
		data.put("loanUse", NfsLoanContract.LoanPurposeType.values()[loanRecord.getLoanPurp().ordinal()].getTypeName());
		data.put("term", loanRecord.getTerm() + "");
		data.put("repayType", "到期还本付息");
		data.put("intRate", StringUtils.decimalToStr(loanRecord.getIntRate(), 2));
		data.put("intrest", StringUtils.decimalToStr(loanRecord.getInterest(), 2));
		data.put("loanStart", DateUtils.getDateStr(loanRecord.getLoanStart(), "yyyy-MM-dd"));
		data.put("repayDate", DateUtils.getDateStr(loanRecord.getDueRepayDate(), "yyyy-MM-dd"));
		data.put("repayAmount", StringUtils.decimalToStr(loanRecord.getDueRepayAmount(), 2));
		data.put("loanDoneFee", Global.getConfig("gxt.loanDoneFee"));
		data.put("loanDelayFee", Global.getConfig("gxt.loanDelayFee"));
		
		return ResponseData.success("请求成功", data);
	}
	
	private String getStatusName(Status status) {
		String name = "";
		switch (status) {
		case pendingAgree:
			name = "待确认";
			break;
		case success:
			name = "已成功";
			break;
		case reject:
			name = "已拒绝";
			break;
		case canceled:
			name = "已取消";
			break;
		case expired:
			name = "已过期 ";
			break;
		default:
			name = "状态异常";
			break;
		}
		return name;
	}
}