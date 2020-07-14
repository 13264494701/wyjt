package com.jxf.web.app.wyjt.loan;



import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.ebaoquan.rop.thirdparty.com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.constant.LoanConstant;
import com.jxf.loan.constant.RecordMessage;
import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanApply.LoanRole;
import com.jxf.loan.entity.NfsLoanApply.RepayType;
import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.entity.NfsLoanApplyDetail.AliveVideoStatus;
import com.jxf.loan.entity.NfsLoanApplyDetail.DisputeResolution;
import com.jxf.loan.entity.NfsLoanApplyDetail.PayStatus;
import com.jxf.loan.entity.NfsLoanApplyDetail.Status;
import com.jxf.loan.entity.NfsLoanDetailMessage;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRepayRecord;
import com.jxf.loan.service.NfsLoanApplyDetailService;
import com.jxf.loan.service.NfsLoanApplyService;
import com.jxf.loan.service.NfsLoanDetailMessageService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.utils.LoanUtils;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberActService;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.H5Utils;
import com.jxf.mem.utils.MemUtils;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.mms.service.SendMsgService;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Constant;
import com.jxf.svc.event.Event;
import com.jxf.svc.model.Notice;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.loan.ApplyChangeInterestRequestParam;
import com.jxf.web.model.wyjt.app.loan.CheckFriendRequestParam;
import com.jxf.web.model.wyjt.app.loan.CheckVideoPassRequestParam;
import com.jxf.web.model.wyjt.app.loan.LoanApplyRequestParam;
import com.jxf.web.model.wyjt.app.loan.LoanApplyResponseResult;
import com.jxf.web.model.wyjt.app.loan.LoanDetailForAppResponseResult;
import com.jxf.web.model.wyjt.app.loan.LoanNewApplyRequestParam;
import com.jxf.web.model.wyjt.app.loan.LoanNewApplyResponseResult;
import com.jxf.web.model.wyjt.app.loan.LoanPayRequestParam;
import com.jxf.web.model.wyjt.app.loan.MultiplyLoanRequestParam;
import com.jxf.web.model.wyjt.app.loan.PayInLoanCenterRequestParam;
import com.jxf.web.model.wyjt.app.loan.RepayRecordRequestParam;
import com.jxf.web.model.wyjt.app.loan.RepayRecordResponseResult;
import com.jxf.web.model.wyjt.app.loan.RepayRecordResponseResult.RepayRecord;
import com.jxf.web.model.wyjt.app.loan.UploadVerifyVideoRequestParam;


/**
 * Controller - 内容管理
 * 
 * @author JINXINFU
 * @version 2.0
 */
@Controller("wyjtAppLoanApplyController")
@RequestMapping(value="${wyjtApp}/loan")
public class LoanApplyController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(LoanApplyController.class);
	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsLoanApplyService loanApplyService;
	@Autowired
	private NfsLoanApplyDetailService loanApplyDetailService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private MemberActService memberActService;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private NfsLoanDetailMessageService nfsLoanDetailMessageService;
	@Autowired
	private SendMsgService sendMsgService;
	@Resource
    private ApplicationContext applicationContext;
	
	/**
	 * 还款计划试算
	 */
	@RequestMapping(value = "/getRepayRecord")
	public @ResponseBody
	ResponseData genRepayRecord(HttpServletRequest request) {
		
		String param = request.getParameter("param");
		RepayRecordRequestParam reqData = JSONObject.parseObject(param,RepayRecordRequestParam.class);
		BigDecimal amount = new BigDecimal(reqData.getAmount());
		BigDecimal intRate = new BigDecimal(reqData.getIntRate());
		NfsLoanApply.RepayType repayType = NfsLoanApply.RepayType.values()[reqData.getRepayType()];
		Integer term = reqData.getTerm();
		NfsLoanRecord loanRecord = LoanUtils.calInt(amount,intRate,repayType,term);
		RepayRecordResponseResult result = new RepayRecordResponseResult();
		result.setInterest(StringUtils.decimalToStr(loanRecord.getInterest(),2));
		for(NfsLoanRepayRecord nfsRepayRecord:loanRecord.getRepayRecordList()) {
			RepayRecord repayRecord = new RepayRecord();
			repayRecord.setPeriodsSeq(nfsRepayRecord.getPeriodsSeq());
			repayRecord.setExpectRepayPrn(StringUtils.decimalToStr(nfsRepayRecord.getExpectRepayPrn(),2));
			repayRecord.setExpectRepayInt(StringUtils.decimalToStr(nfsRepayRecord.getExpectRepayInt(),2));
			repayRecord.setExpectRepayAmt(StringUtils.decimalToStr(nfsRepayRecord.getExpectRepayAmt(),2));
			repayRecord.setExpectRepayDate(DateUtils.formatDate(nfsRepayRecord.getExpectRepayDate(), "yyyy-MM-dd"));
			repayRecord.setStatus(0);
			result.getRepayRecordList().add(repayRecord);
		}
		
		return ResponseData.success("还款计划试算成功",result);
	}
	/**
	 * 检查是否可以跟好友发生借条关系
	 */
	@RequestMapping(value = "/checkFriend")
	public @ResponseBody
	ResponseData checkFriend(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		CheckFriendRequestParam reqData = JSONObject.parseObject(param,CheckFriendRequestParam.class);
		String friendsId = reqData.getFriendsId();
		if(StringUtils.isBlank(friendsId)){
			return ResponseData.error("请选择一名好友");
		}
		ResponseData result = loanApplyService.checkFriend(member,friendsId);
		int code = result.getCode();
		String message = result.getMessage();
		if(code != 0){
			return ResponseData.error(message);
		}else{
			return ResponseData.success("可以打借条");
		}
	}
	
	/**
	 * 借款申请
	 */
	@RequestMapping(value = "/apply")
	public @ResponseBody
	ResponseData apply(HttpServletRequest request) {
		
		String param = request.getParameter("param");
		LoanApplyRequestParam reqData = JSONObject.parseObject(param,LoanApplyRequestParam.class);
		LoanApplyResponseResult result = new LoanApplyResponseResult();
		Member member = memberService.getCurrent();
		Integer verifiedList = member.getVerifiedList();
		//邮箱
		Boolean verifiedEmail = VerifiedUtils.isVerified(verifiedList, 23);
		if(!verifiedEmail){
			return ResponseData.error("您还未填写邮箱，暂时无法申请借款");
		}
		
		NfsLoanApply  loanApply = new NfsLoanApply();
		Integer loanRole = reqData.getLoanRole();
		loanApply.setMember(member);
		loanApply.setLoanRole(NfsLoanApply.LoanRole.values()[loanRole]);
		loanApply.setLoanType(NfsLoanApply.LoanType.values()[reqData.getLoanType()]);
		loanApply.setLoanPurp(NfsLoanApply.LoanPurp.values()[reqData.getLoanPurp()]);
		loanApply.setRepayType(NfsLoanApply.RepayType.values()[reqData.getRepayType()]);
		BigDecimal amount = StringUtils.toDecimal(reqData.getAmount());
		loanApply.setAmount(amount);
		loanApply.setRemainAmount(amount);
		if(StringUtils.isNotEmpty(reqData.getIntRate())){
			loanApply.setIntRate(StringUtils.toDecimal(reqData.getIntRate()));
		}else{
			loanApply.setIntRate(BigDecimal.ZERO);
		}
		loanApply.setTerm(reqData.getTerm());
		loanApply.setChannel(NfsLoanApply.Channel.andriod);
		NfsLoanRecord loanRecord = LoanUtils.calInt(loanApply.getAmount(),loanApply.getIntRate(),loanApply.getRepayType(),loanApply.getTerm());
		loanApply.setInterest(loanRecord.getInterest());
		loanApply.setRepayAmt(loanRecord.getDueRepayAmount());
		
		Date parseDate = DateUtils.parseDate("1900-01-01 00:00:01");
		loanApply.setLoanStart(parseDate);
		loanApply.setTrxType(NfsLoanApply.TrxType.online);
		loanApplyService.save(loanApply);	
		String partnersStr = reqData.getPartners();
		
		String[] partners = partnersStr.split("\\|");
		if(partners.length > 1){
			result.setType(0);
		}else{
			result.setType(1);
		}
		NfsLoanApplyDetail detail = null;
		
		/*//如果是我发起的 多人借款 剩余可借额度放缓存
		if(reqData.getLoanType() == 1 && loanRole == 0){
			RedisUtils.put("memberOftenUse" + member.getId(), "applyRemainAmount" + loanApply.getId(),reqData.getAmount());
		}*/
		boolean isMultiplayer =  partners.length > 1;//是多人借款
		for(String partnerId:partners) {
			Member partner = memberService.get(Long.valueOf(partnerId));
			detail = new NfsLoanApplyDetail();
			detail.setApply(loanApply);
			detail.setMember(partner);
			detail.setAliveVideoStatus(NfsLoanApplyDetail.AliveVideoStatus.notUpload);
			detail.setPayStatus(PayStatus.noPay);
			detail.setIntStatus(NfsLoanApplyDetail.IntStatus.primary);
			if(isMultiplayer){
				detail.setAmount(BigDecimal.ZERO);
			}else{
				detail.setAmount(amount);
			}
			detail.setStatus(NfsLoanApplyDetail.Status.pendingAgree);
			detail.setLoanRole(NfsLoanApply.LoanRole.values()[1^reqData.getLoanRole()]);
			detail.setTrxType(NfsLoanApply.TrxType.online);
			loanApplyDetailService.save(detail);
			
			//发起人是借款人发对话 发起人是放款人走H5
			NfsLoanDetailMessage loanDetailMessage = new NfsLoanDetailMessage();
			loanDetailMessage.setDetail(detail);
			loanDetailMessage.setMember(member);
			loanDetailMessage.setMessageId(RecordMessage.CHAT_1101);
			loanDetailMessage.setType(RecordMessage.BORROWER_INITIATE_LOAN);
			nfsLoanDetailMessageService.save(loanDetailMessage);
			
			/**发送会员消息*/
			if(isMultiplayer){
				//发送会员消息
				Map<String, Object> tradDetail = new HashMap<String,Object>();
				tradDetail.put("name", member.getName());
				tradDetail.put("loanAmount", StringUtils.decimalToStr(loanApply.getAmount(), 2));
				tradDetail.put("time", DateUtils.formatDateForMessage(new Date()));
				tradDetail.put("status", "申请待确认");
				tradDetail.put("colorStatus", "1");
				tradDetail.put("type", "0");
				String jsonString = JSON.toJSONString(tradDetail);
				
				Map<String,Object> messageMap = new HashMap<String,Object>();
				messageMap.put("name", member.getName());
				memberMessageService.sendMessage(MemberMessage.Type.multiplayerLoan,MemberMessage.Group.appLoanMessage, partner,
						messageMap,detail.getId(),"1",jsonString);
			}else{
//				MemberMessage sendMessage = memberMessageService.sendMessage(MemberMessage.Type.loanApplication,detail.getId());
				
				Event event = new Event(this, MemberMessage.Type.loanApplication, detail.getId());
				applicationContext.publishEvent(event);
				
				//推送
//				sendMsgService.beforeSendAppMsg(sendMessage);
				
				Notice notice = new Notice();
				notice.setNoticeType(1);
				notice.setNoticeId(loanApply.getId().toString());
				notice.setNoticeMessage("有新借条申请");		    			
				RedisUtils.leftPush("memberNotice"+partner.getId(), notice);
				
			}
						
		}
		if(partners.length > 1){
			result.setLoanId(loanApply.getId().toString());
		}else{
			result.setLoanId(detail.getId().toString());
		}
		return ResponseData.success("借条申请已发送给好友",result);
	}
	/**
	 * 新借款申请弹窗
	 */
	@RequestMapping(value = "/newApply")
	public @ResponseBody
	ResponseData newApply(HttpServletRequest request) {
		
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		LoanNewApplyRequestParam reqData = JSONObject.parseObject(param,LoanNewApplyRequestParam.class);
        Long applyId = Long.valueOf(reqData.getApplyId());
        NfsLoanApply loanApply = loanApplyService.get(applyId);
		NfsLoanApplyDetail applyDetail = loanApplyDetailService.getByApplyIdAndMemberId(applyId,member.getId());
		

		LoanNewApplyResponseResult result = new LoanNewApplyResponseResult();
		result.setApplyId(applyDetail.getId().toString());
		result.setLoanRole(loanApply.getLoanRole().ordinal());
		result.setFriendName(loanApply.getMember().getName());
		result.setFriendHeadImage(loanApply.getMember().getHeadImage());
		result.setFriendCreditRank(member.getMemberRank().getRankNo());
		result.setFriendCreditDesc(MemUtils.getMemRankName(member.getMemberRank().getRankNo()));
		result.setAmount(StringUtils.decimalToStr(loanApply.getAmount(), 2));
		result.setInterest(StringUtils.decimalToStr(loanApply.getInterest(), 2));
		result.setTerm(loanApply.getTerm());
		result.setRepayType(loanApply.getRepayType().equals(NfsLoanApply.RepayType.oneTimePrincipalAndInterest)?"全额":"分期");
		result.setType(LoanConstant.TYPE_DETAIL);
		
		return ResponseData.success("查询新借款申请弹窗成功", result);
	}
	
	/**
	 * 借款给好友 跳转到支付页面
	 * @return
	 */
	@RequestMapping("toLoanPay")
	public ModelAndView toLoanPay(HttpServletRequest request) {
		String param = request.getParameter("param");
		LoanPayRequestParam reqData = JSONObject.parseObject(param,LoanPayRequestParam.class);
		String friendId = reqData.getFriendId();
		String amountStr = reqData.getAmount();
		String interest = reqData.getInterest();
		String intRate = reqData.getIntRate();
		String repayType = reqData.getRepayType();
		String termStr = reqData.getTerm();
		String periods = reqData.getPeriods();
		BigDecimal amount = new BigDecimal(amountStr);
		Member loaner = memberService.getCurrent();
		Member loanee = memberService.get(Long.valueOf(friendId));
		BigDecimal curBal = memberService.getCulBal(loaner);
		BigDecimal avlBal = memberService.getAvlBal(loaner);
		boolean isEnough = avlBal.compareTo(amount) >= 0;
		ModelAndView mv = new ModelAndView("app/loanPay/payForLoanToFriend");
		String repayDate = DateUtils.getDateStr(CalendarUtil.addDay(new Date(), Integer.valueOf(termStr)-1), "yyyy-MM-dd");
		mv.addObject("repayDate", repayDate);
		mv.addObject("loaner", loaner);
		mv.addObject("loanee", loanee);
		if("0".equals(repayType)) {
			mv.addObject("term", termStr);
		}else {
			mv.addObject("term", termStr);
			mv.addObject("periods", periods);
		}
		mv.addObject("repayType", repayType);
		mv.addObject("interest", StringUtils.decimalToStr(new BigDecimal(interest), 2));
		mv.addObject("intRate", intRate);
		mv.addObject("payTime", DateUtils.getDate());
		mv.addObject("curBal", StringUtils.decimalToStr(curBal, 2));
		mv.addObject("avlBal", StringUtils.decimalToStr(avlBal, 2));
		mv.addObject("isEnough", isEnough);
		mv.addObject("amount", StringUtils.decimalToStr(amount, 2));
		mv.addObject("isPayPsw",VerifiedUtils.isVerified(loaner.getVerifiedList(), 22));
		String memberToken = request.getHeader("x-memberToken");
		mv.addObject("memberToken", memberToken);
		mv =H5Utils.addPlatform(loaner,mv);
		return mv;
	}
	
	/**
	 * 借条中心放款支付H5页面跳转
	 */
	@RequestMapping("payInLoanCenter")
	public ModelAndView payInLoanCenter(HttpServletRequest request) {
		String param = request.getParameter("param");
		PayInLoanCenterRequestParam reqData = JSONObject.parseObject(param,PayInLoanCenterRequestParam.class);
		String detailId = reqData.getDetailId();
		ModelAndView mv = new ModelAndView("app/loanPay/payForAgreeLoan");
		NfsLoanApplyDetail detail = loanApplyDetailService.get(Long.valueOf(detailId));
		NfsLoanApply apply = loanApplyService.get(detail.getApply());
		Member loaner = memberService.getCurrent();
		Member loanee = apply.getLoanRole().equals(NfsLoanApply.LoanRole.loanee)? apply.getMember():detail.getMember();
		loanee = memberService.get(loanee);
		BigDecimal amount = apply.getAmount();
		BigDecimal avlBal = memberService.getAvlBal(loaner);
		boolean isEnough = avlBal.compareTo(amount) >= 0 ? true : false;
		RepayType type = apply.getRepayType();
		String repayType = RepayType.oneTimePrincipalAndInterest.equals(type) ? "0" : "1";
		Integer term = apply.getTerm();
		Integer periods = term/30;
		String loanType = apply.getLoanType().equals(NfsLoanApply.LoanType.multiple) ? "1" : "0";
		String repayDate = DateUtils.getDateStr(CalendarUtil.addDay(new Date(), apply.getTerm()-1), "yyyy-MM-dd");
		ResponseData checkResult = loanApplyService.checkFriend(loaner, loanee.getId()+"");
		if(checkResult.getCode() == -1) {
			String errMsg = checkResult.getMessage();
			if(errMsg.contains("已将您移除好友列表,请重新添加后再操作")) {
				mv.addObject("errCode", -50);
				mv.addObject("errString", checkResult.getMessage());
			}else {
				mv.addObject("errCode", -100);
				mv.addObject("errString", checkResult.getMessage());
			}
		}
		mv.addObject("loanee", loanee);
		mv.addObject("loaner", loaner);
		mv.addObject("detailId", detailId);
		mv.addObject("avlBal", StringUtils.decimalToStr(avlBal, 2));
		mv.addObject("isEnough", isEnough);
		mv.addObject("term", term);
		mv.addObject("periods", periods);
		mv.addObject("repayType", repayType);
		mv.addObject("loanType", loanType);
		mv.addObject("repayDate", repayDate);
		mv.addObject("apply", apply);
		mv.addObject("interest", StringUtils.decimalToStr(apply.getInterest(), 2));
		mv.addObject("intRate", apply.getIntRate());
		mv.addObject("amount", StringUtils.decimalToStr(amount, 2));
		mv.addObject("payTime", DateUtils.getDate());
		mv.addObject("isPayPsw",VerifiedUtils.isVerified(loaner.getVerifiedList(), 22));
		String memberToken = request.getHeader("x-memberToken");
		mv.addObject("memberToken", memberToken);
		mv =H5Utils.addPlatform(loaner,mv);
		return mv;
	}
	
	/**
	 * 多人借款放款人支付H5页面跳转
	 * @param request
	 * @return
	 */
	@RequestMapping(value="toMultiplayerLoanPayPage")
	public ModelAndView toMultiplayerLoanPayPage(HttpServletRequest request) {
		String param = request.getParameter("param");
		MultiplyLoanRequestParam reqData = JSONObject.parseObject(param,MultiplyLoanRequestParam.class);
		String amountStr = reqData.getAmount();//付款金额
		String detailId = reqData.getDetailId();
		ModelAndView mv = new ModelAndView("app/loanPay/payForAgreeLoan");
		Member loaner = memberService.getCurrent();
		NfsLoanApplyDetail applyDetail = loanApplyDetailService.get(Long.valueOf(detailId));
		NfsLoanApply loanApply = loanApplyService.get(applyDetail.getApply());
		Member loanee = loanApply.getMember();
		BigDecimal amount = StringUtils.toDecimal(amountStr);
		applyDetail.setAmount(amount);
		loanApplyDetailService.save(applyDetail);//更新下detail里的出借金额
		BigDecimal avlBal = memberActService.getAvlBal(loaner);
		boolean isEnough = avlBal.compareTo(amount) >= 0 ? true:false;
		mv =H5Utils.addPlatform(loaner,mv);
		mv.addObject("loanee", loanee);
		mv.addObject("loaner", loaner);
		mv.addObject("avlBal", StringUtils.decimalToStr(avlBal, 2));
		mv.addObject("isEnough", isEnough);
		mv.addObject("detailId", detailId);
		mv.addObject("amount", StringUtils.decimalToStr(amount, 2));
		mv.addObject("term", loanApply.getTerm());
		mv.addObject("payTime", DateUtils.getDate());
		String repayDate = DateUtils.getDateStr(CalendarUtil.addDay(new Date(), loanApply.getTerm()-1), "yyyy-MM-dd");
		mv.addObject("repayDate", repayDate);
		String repayType = RepayType.oneTimePrincipalAndInterest.equals(loanApply.getRepayType())? "0" : "1";
		mv.addObject("repayType", repayType);
		ResponseData checkResult = loanApplyService.checkFriend(loaner, loanee.getId()+"");
		if(checkResult != null && checkResult.getCode() == -1) {
			String errMsg = checkResult.getMessage();
			if(errMsg.contains("已将您移除好友列表,请重新添加后再操作")) {
				mv.addObject("errCode", -50);
				mv.addObject("errString", checkResult.getMessage());
			}else {
				mv.addObject("errCode", -100);
				mv.addObject("errString", checkResult.getMessage());
			}
		}
		BigDecimal oneHundred = new BigDecimal(100);
		BigDecimal oneYearDays = new BigDecimal(360);
		BigDecimal term = new BigDecimal(loanApply.getTerm());
		//多人借款利息计算
		MathContext mc = new MathContext(10);
		BigDecimal interest = loanApply.getIntRate().divide(oneHundred,mc).divide(oneYearDays,mc).multiply(amount,mc).multiply(term,mc);
		mv.addObject("interest", StringUtils.decimalToStr(interest, 2));
		mv.addObject("intRate", loanApply.getIntRate());
		mv.addObject("loanType", "1");
		mv.addObject("isPayPsw",VerifiedUtils.isVerified(loaner.getVerifiedList(), 22));
		mv.addObject("memberToken", request.getHeader("x-memberToken"));
		return mv;
	}
	
	/**
	 * 	借款给好友立即支付
	 */
	@RequestMapping(value = "/payForLendLoan")
	public ModelAndView payForLendLoan(HttpServletRequest request) {
		Member loaner = memberService.getCurrent();
		String friendId = request.getParameter("loanee");
		String amountStr = request.getParameter("amount");
		String intRateStr = request.getParameter("intRate");
		String interestStr = request.getParameter("interest");
		String termStr = request.getParameter("term");
		String repayTypeStr = request.getParameter("repayType");
		String disputeSolveType = request.getParameter("disputeSolveType");//0->仲裁,1->诉讼
		String isVideo = request.getParameter("isVideo");//0->否，1->是		要求录视频
		ModelAndView mv = new ModelAndView("app/loanPay/loanerPayResult");
		mv =H5Utils.addPlatform(loaner,mv);
		if((friendId == null || friendId.trim().length() == 0) || (amountStr == null || amountStr.trim().length() == 0)
				|| (intRateStr == null || intRateStr.trim().length() == 0) || (interestStr == null || interestStr.trim().length() == 0)
				||(termStr == null || termStr.trim().length() == 0) || (disputeSolveType == null || disputeSolveType.trim().length() == 0)
				|| (isVideo == null || isVideo.trim().length() == 0)) {
			mv.addObject("code", -1);
			mv.addObject("message", "参数错误");
			return mv;
		}
		BigDecimal amount = new BigDecimal(amountStr);
		Member loanee = memberService.get(Long.valueOf(friendId));
		int term = Integer.valueOf(termStr);
		BigDecimal intRate = new BigDecimal(intRateStr);
		RepayType repayType = null;
		NfsLoanApply loanApply = new NfsLoanApply();
		if("0".equals(repayTypeStr)) {
			repayType = NfsLoanApply.RepayType.oneTimePrincipalAndInterest;
		}else if("1".equals(repayTypeStr)) {
			repayType = NfsLoanApply.RepayType.principalAndInterestByMonth;
		}
		loanApply.setMember(loaner);
		if(NfsLoanApply.Channel.andriod.equals(mv.getModel().get("appPlatform"))) {
			loanApply.setChannel(NfsLoanApply.Channel.andriod);
		}else if (NfsLoanApply.Channel.ios.equals(mv.getModel().get("appPlatform"))) {
			loanApply.setChannel(NfsLoanApply.Channel.ios);
		}else {
			loanApply.setChannel(NfsLoanApply.Channel.ufang);
		}
		loanApply.setIntRate(intRate);
		loanApply.setAmount(amount);
		loanApply.setIsNewRecord(true);
		loanApply.setLoanType(NfsLoanApply.LoanType.single);
		loanApply.setLoanPurp(NfsLoanApply.LoanPurp.turnover);
		loanApply.setLoanRole(NfsLoanApply.LoanRole.loaner);
		loanApply.setRepayType(repayType);
		loanApply.setTerm(term);
		loanApply.setBeginDate(new Date());
		NfsLoanRecord loanRecord = LoanUtils.calInt(amount, intRate, repayType, term);
		loanApply.setInterest(loanRecord.getInterest());
		loanApply.setRepayAmt(loanRecord.getDueRepayAmount());
		loanApply.setTrxType(NfsLoanApply.TrxType.online);
		try {
			Date loanStart = DateUtils.parse("1900-01-01 00:00:01");
			loanApply.setLoanStart(loanStart);
		} catch (ParseException e1) {
			logger.error(Exceptions.getStackTraceAsString(e1));
		}
		
		NfsLoanApplyDetail detail = new NfsLoanApplyDetail();
		if(Integer.valueOf(isVideo) == 0) {
			detail.setAliveVideoStatus(NfsLoanApplyDetail.AliveVideoStatus.notUpload);
		}else {
			detail.setAliveVideoStatus(NfsLoanApplyDetail.AliveVideoStatus.pendingUpload);
		}
		if(Integer.valueOf(disputeSolveType) == 0) {
			detail.setDisputeResolution(NfsLoanApplyDetail.DisputeResolution.arbitration);
		}else {
			detail.setDisputeResolution(NfsLoanApplyDetail.DisputeResolution.prosecution);
		}
		detail.setTrxType(NfsLoanApply.TrxType.online);
		detail.setAmount(amount);
		detail.setPayStatus(PayStatus.noPay);
		detail.setIntStatus(NfsLoanApplyDetail.IntStatus.primary);
		detail.setIsNewRecord(true);
		detail.setLoanRole(LoanRole.loanee);
		detail.setMember(loanee);
		detail.setStatus(NfsLoanApplyDetail.Status.pendingAgree);
		ResponseData result = loanApplyService.checkFriend(loaner, friendId);
		if(result.getCode() != 0) {
			mv.addObject("code", -1);
			mv.addObject("message", result.getMessage());
			return mv;
		}
		Map<String, String> checkResult = loanApplyService.preCheckOfCreateLoan(loanApply, detail);
		if(checkResult != null) {
			mv.addAllObjects(checkResult);
			return mv;
		}
		int respCode = Constant.UPDATE_FAILED;
		try {
			respCode = loanApplyService.payForLoanToFriend(loanApply,detail,loaner);
		} catch (Exception e) {
			logger.error("放款人{}主动放款申请扣款失败！异常信息：{}", loaner.getId(),Exceptions.getStackTraceAsString(e));
			respCode = Constant.UPDATE_FAILED;
		}
		if(respCode == Constant.UPDATE_FAILED) {
			mv.addObject("code", -1);
			mv.addObject("message", "账户更新异常，操作失败！");
			return mv;
		}
		//发送会员消息
		if(Integer.valueOf(isVideo) == 0) {
			memberMessageService.sendMessage(MemberMessage.Type.lendApplication, detail.getId());
		}else {
			memberMessageService.sendMessage(MemberMessage.Type.recordVideo, detail.getId());
		}
		Notice notice = new Notice();
		notice.setNoticeType(1);
		notice.setNoticeId(loanApply.getId().toString());
		notice.setNoticeMessage("有新借条申请");		    			
		RedisUtils.leftPush("memberNotice"+loanee.getId(), notice);
		
		//生成对话
		if(StringUtils.equals(isVideo, "1")) {
			NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
			nfsLoanDetailMessage.setDetail(detail);
			nfsLoanDetailMessage.setMember(loaner);
			nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_1205);
			nfsLoanDetailMessage.setType(RecordMessage.SEND_REMIND);
			nfsLoanDetailMessageService.save(nfsLoanDetailMessage);
		}else {
			NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
			nfsLoanDetailMessage.setDetail(detail);
			nfsLoanDetailMessage.setMember(loaner);
			nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_1202);
			nfsLoanDetailMessage.setType(RecordMessage.SEND_REMIND);
			nfsLoanDetailMessageService.save(nfsLoanDetailMessage);
		}
		mv.addObject("code", 0);
		mv.addObject("amount", amount);
		mv.addObject("type", LoanConstant.TYPE_DETAIL);//0:apply;1:detail;2record
		mv.addObject("loanId", detail.getId());
		mv.addObject("message", "支付成功，请等待好友确认");
		return mv;
	}
	/**
	 * 借款人申请借款放款人要求录视频
	 * @param request
	 * @return
	 */
	@RequestMapping(value="loanerRequireVideo")
	public ModelAndView loanerRequireVideo(HttpServletRequest request) {
		String applyDetailId = request.getParameter("detailId");
		String disputeSolveType = request.getParameter("disputeSolveType");
		ModelAndView mv = new ModelAndView("app/loanPay/loanerPayResult");
		Member loaner = memberService.getCurrent();
		mv =H5Utils.addPlatform(loaner,mv);
		NfsLoanApplyDetail detail = loanApplyDetailService.get(Long.valueOf(applyDetailId));
		Status detailStatus = detail.getStatus();
		if(!detailStatus.equals(NfsLoanApplyDetail.Status.pendingAgree)){
			mv.addObject("code", -1);
			mv.addObject("message", "借条申请状态已更新，请勿重复操作！");
			return mv;
		}
		if(!detail.getAliveVideoStatus().equals(AliveVideoStatus.notUpload)) {
			mv.addObject("code", -1);
			mv.addObject("message", "视频审核中，请勿重复操作！");
			return mv;
		}
		BigDecimal amount = detail.getAmount();//本金
		BigDecimal avlBal = memberActService.getAvlBal(loaner);
		if(avlBal.compareTo(amount)<0){
			mv.addObject("code", -1);
			mv.addObject("message", "可用余额不足");
			return mv;
		}
		detail.setDisputeResolution(disputeSolveType.equals("0")?DisputeResolution.arbitration:DisputeResolution.prosecution);
		detail.setAliveVideoStatus(NfsLoanApplyDetail.AliveVideoStatus.pendingUpload);
		detail.setProgress("待录制视频;FFAE38");
		try {
			int code = loanApplyService.loanerRequireVideo(detail);
			if(code == Constant.UPDATE_SUCCESS) {
				mv.addObject("code", 0);
				mv.addObject("amount", amount);
				mv.addObject("type", LoanConstant.TYPE_DETAIL);
				mv.addObject("loanId", detail.getId());
				mv.addObject("message", "借款支付成功，请耐心等待借款人录视频！");
			}else {
				mv.addObject("code", -1);
				mv.addObject("message", "账户更新异常，操作失败！");
			}
		} catch (Exception e) {
			logger.error("放款人{}要求录制视频时扣款失败！异常信息：{}", loaner.getId(),Exceptions.getStackTraceAsString(e));
			mv.addObject("code", -1);
			mv.addObject("message", "账户更新异常，操作失败！");
		}
	
		return mv;
	}
	
	/**
	 * 	借款人申请借款放款人同意支付 不需要录视频
	 */
	@RequestMapping(value = "/payForAgreeLoan")
	public ModelAndView payForAgreeLoan(HttpServletRequest request) {
		String applyDetailId = request.getParameter("detailId");
		String disputeSolveType = request.getParameter("disputeSolveType");
		Member loaner = memberService.getCurrent();
		ModelAndView mv = new ModelAndView("app/loanPay/loanerPayResult");
		mv =H5Utils.addPlatform(loaner,mv);
		NfsLoanApplyDetail applyDetail = loanApplyDetailService.get(Long.valueOf(applyDetailId));
		applyDetail.setAliveVideoStatus(AliveVideoStatus.notUpload);
		applyDetail.setDisputeResolution(disputeSolveType.equals("0")? DisputeResolution.arbitration:DisputeResolution.prosecution);
		NfsLoanApply loanApply = loanApplyService.get(applyDetail.getApply());
		Map<String, String> checkResult = loanApplyService.preCheckOfCreateLoan(loanApply, applyDetail);
		if(checkResult != null) {
			mv.addAllObjects(checkResult);
			return mv;
		}
		try {
			NfsLoanRecord loanRecord= loanRecordService.createLoanRecord(applyDetail,TrxRuleConstant.LOANER_AGREE_LOAN);
			if(loanRecord == null) {
				mv.addObject("code", -1);
				mv.addObject("message", "借条申请异常，支付失败！");
				return mv;
			}
			mv.addObject("code", 0);
			mv.addObject("amount", applyDetail.getAmount());
			mv.addObject("type", LoanConstant.TYPE_RECORD);
			mv.addObject("loanId", loanRecord.getId());
			mv.addObject("message", "借款支付成功");
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
			mv.addObject("code", -1);
			mv.addObject("message", "账户更新异常，借款支付失败！");
		}
		return mv;
	}
	/**
	 * 多人借款 部分出借
	 * @param request
	 * @return
	 */
	@RequestMapping(value="payForMultiplayerLoan")
	public ModelAndView payForMultiplayerLoan(HttpServletRequest request) {
		String applyDetailId = request.getParameter("detailId");
		String disputeSolveType = request.getParameter("disputeSolveType");
		String amountStr = request.getParameter("amount");
		ModelAndView mv = new ModelAndView("app/loanPay/loanerPayResult");
		Member loaner = memberService.getCurrent();
		BigDecimal amount = new BigDecimal(amountStr);
		if(loaner == null) {
			mv.addObject("code", -1);
			mv.addObject("message", "登录状态已过期，请重新登录");
			return mv;
		}
		mv =H5Utils.addPlatform(loaner,mv);
		NfsLoanApplyDetail detail = loanApplyDetailService.get(Long.valueOf(applyDetailId));
		detail.setAliveVideoStatus(NfsLoanApplyDetail.AliveVideoStatus.notUpload);
		detail.setDisputeResolution(disputeSolveType.equals("0")?DisputeResolution.arbitration:DisputeResolution.prosecution);
		NfsLoanApply loanApply = loanApplyService.get(detail.getApply());
		Map<String, String> checkResult = loanApplyService.preCheckOfCreateLoan(loanApply, detail);
		if(checkResult != null) {
			mv.addAllObjects(checkResult);
			return mv;
		}
		try {
			NfsLoanRecord loanRecord= loanRecordService.createLoanRecord(detail,TrxRuleConstant.LOANER_MULTI_LOAN);
			if(loanRecord == null) {
				mv.addObject("code", -1);
				mv.addObject("message", "借条申请异常，支付失败！");
				return mv;
			}
			mv.addObject("code", 0);
			mv.addObject("amount", amount);
			mv.addObject("type", LoanConstant.TYPE_RECORD);
			mv.addObject("loanId", loanRecord.getId());
			mv.addObject("message", "借款支付成功");
		} catch (Exception e) {
			logger.error("多人借款，放款人{}部分出借金额{}账户更新异常。异常信息：{}",loaner.getId(),amount,Exceptions.getStackTraceAsString(e));
			mv.addObject("code", -1);
			mv.addObject("message", "账户异常，借款支付失败！");
		}
		return mv;
	}
	
	/**
	 * 	申请修改利息
	 */
	@RequestMapping(value = "/applyChangeInterest")
	public @ResponseBody ResponseData applyChangeInterest(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		ApplyChangeInterestRequestParam reqData = JSONObject.parseObject(param, ApplyChangeInterestRequestParam.class);
		
		String detailId = reqData.getLoanId();
		String interest = reqData.getInterest();
		int flag = loanApplyDetailService.applyChangeInterest(member,Long.valueOf(detailId),interest);
		if(flag == Constant.UPDATE_SUCCESS) {
			LoanDetailForAppResponseResult result = loanRecordService.getDetail(detailId, 1,member);
			return ResponseData.success("申请修改利率",result);
		}else {
			return ResponseData.error("申请修改利息失败！");
		}
	}
	
	/**
	 * 	同意/拒绝修改利息
	 */
	@RequestMapping(value = "/replyChangeInterest")
	public @ResponseBody ResponseData replyChangeInterest(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String detailId = request.getParameter("loanId");
		int isAgree = Integer.parseInt(request.getParameter("isAgree"));
		NfsLoanApplyDetail nfsLoanApplyDetail = loanApplyDetailService.get(Long.valueOf(detailId));
		NfsLoanApply nfsLoanApply = nfsLoanApplyDetail.getApply();
		nfsLoanApply = loanApplyService.get(nfsLoanApply.getId());
		String interest = "";
		LoanRole loanRole = nfsLoanApplyDetail.getLoanRole();
		if(loanRole.equals(LoanRole.loaner)){//取放款人Id
			interest = (String)RedisUtils.getHashKey("memberOftenUse" + nfsLoanApplyDetail.getMember().getId(),
					"applyChangeInterest" + detailId);	
		}else{
			interest = (String)RedisUtils.getHashKey("memberOftenUse" + nfsLoanApply.getMember().getId(),
					"applyChangeInterest" + detailId);	
		}
		int flag = loanApplyDetailService.replyChangeInterest(member, nfsLoanApply,nfsLoanApplyDetail, isAgree,new BigDecimal(interest));
		if(flag == Constant.UPDATE_SUCCESS) {
			if(isAgree == 1) {
				LoanDetailForAppResponseResult result = loanRecordService.getDetail(detailId, 1,member);
				return ResponseData.success("同意修改利率", result);
			}else {
				LoanDetailForAppResponseResult result = loanRecordService.getDetail(detailId, 1,member);
				return ResponseData.success("拒绝修改利率", result);
			}
		}else {
			return ResponseData.error("借条状态已更新，操作失败");
		}
	}
	
	/**
	 * 	待放款--> 残忍拒绝
	 */
	@RequestMapping(value = "/rejectApply")
	public @ResponseBody ResponseData rejectApply(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String detailId = request.getParameter("loanId");
		
		int flag = loanApplyDetailService.rejectApply(member, Long.valueOf(detailId));
		if(flag == Constant.UPDATE_SUCCESS) {
			LoanDetailForAppResponseResult result = loanRecordService.getDetail(detailId, 1,member);
			return ResponseData.success("已拒绝放款", result);
		}else {
			return ResponseData.error("借条状态已更新，操作失败");
		}
	}
	/**
	 * 	借款人拒绝主动放款
	 */
	@RequestMapping(value = "/refusePayment")
	public @ResponseBody ResponseData refusePayment(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String detailId = request.getParameter("loanId");
		try {
			int code = loanApplyDetailService.refusePayment(detailId,member);
			if(code == Constant.UPDATE_SUCCESS) {
				LoanDetailForAppResponseResult result = loanRecordService.getDetail(detailId, 1,member);
				return ResponseData.success("已拒绝放款", result);
			}else {
				return ResponseData.error("借条状态已更新,操作失败");
			}
		} catch (Exception e) {
			logger.error("借款人{}主动拒绝放款，账户更新异常。异常信息：{}",member.getId(),Exceptions.getStackTraceAsString(e));
			return ResponseData.error("账户异常,操作失败");
		}
	}
	/**
	 * 	借款人取消借款
	 */
	@RequestMapping(value = "/cancelBorrow")
	public @ResponseBody ResponseData cancelBorrow(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String detailId = request.getParameter("loanId");
		NfsLoanApplyDetail applyDetail = loanApplyDetailService.get(Long.valueOf(detailId));
		Status detailStatus = applyDetail.getStatus();
		if(!detailStatus.equals(Status.pendingAgree)){
			return ResponseData.error("借条申请状态已更新，操作失败");
		}
		try {
			int flag = loanApplyDetailService.cancelBorrow(applyDetail,member);
			if(flag == Constant.UPDATE_SUCCESS) {
				LoanDetailForAppResponseResult result = loanRecordService.getDetail(detailId, 1,member);
				return ResponseData.success("取消借款", result);
			}else {
				return ResponseData.error("借条申请状态已更新，操作失败");
			}
		} catch (Exception e) {
			logger.error("借款人{}取消借款，账户更新异常。异常信息：{}",member.getId(),Exceptions.getStackTraceAsString(e));
			return ResponseData.error("账户更新异常，操作失败");
		}
	}
	/**
	 * 保存审核视频地址
	 */
	@RequestMapping(value = "/uploadVerifyVideo")
	public @ResponseBody ResponseData uploadVerifyVideo(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		UploadVerifyVideoRequestParam reqData = JSONObject.parseObject(param, UploadVerifyVideoRequestParam.class);
		String detailId = reqData.getLoanId();
		if(StringUtils.isBlank(detailId)) {
			logger.warn("借条ID不能为空");
			logger.warn("x-osType:{}",request.getHeader("x-osType"));
			logger.warn("x-osVersion:{}",request.getHeader("x-osVersion"));
			logger.warn("x-deviceToken:{}",request.getHeader("x-deviceToken"));
			logger.warn("x-deviceModel:{}",request.getHeader("x-deviceModel"));
			logger.warn("x-appVersion:{}",request.getHeader("x-appVersion"));
			logger.warn("x-ak:{}",request.getHeader("x-ak"));
			logger.warn("x-pushToken:{}",request.getHeader("x-pushToken"));
			logger.warn("x-channeId:{}",request.getHeader("x-channeId"));
			return ResponseData.error("借条ID不能为空");
		}
		String verifyVideoUrl = reqData.getVerifyVideoUrl();
		int flag = loanApplyDetailService.uploadVerifyVideo(member, Long.valueOf(detailId), verifyVideoUrl);
		if(flag == Constant.UPDATE_SUCCESS) {
			//返回详情
			LoanDetailForAppResponseResult result = loanRecordService.getDetail(detailId, 1,member);
			return ResponseData.success("视频保存成功", result);
		}else {
			return ResponseData.error("借条状态已更新，操作失败");
		}
		
	}
	/**
	 * 审核视频
	 */
	@RequestMapping(value = "/checkVideoPass")
	public @ResponseBody ResponseData checkVideoPass(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		CheckVideoPassRequestParam reqData = JSONObject.parseObject(param, CheckVideoPassRequestParam.class);
		LoanDetailForAppResponseResult result = new LoanDetailForAppResponseResult();
		
		String detailId = reqData.getLoanId();
		Integer isPass = reqData.getIsPass();
		NfsLoanApplyDetail applyDetail = loanApplyDetailService.get(Long.parseLong(detailId));
		if(!applyDetail.getStatus().equals(NfsLoanApplyDetail.Status.pendingAgree)) {
			return ResponseData.error("借条申请状态已更新，请不要重复操作。");
		}	
		NfsLoanApply nfsLoanApply = loanApplyService.get(applyDetail.getApply());
		
		Member loanee;
		if(nfsLoanApply.getLoanRole().equals(NfsLoanApply.LoanRole.loanee)){//借款人主动借款
			loanee = nfsLoanApply.getMember();
		}else{
			loanee = applyDetail.getMember();
		}
		
		if(isPass == 0){//视频审核 未通过
			int flag = loanApplyDetailService.verifyVideoNotPass(member, applyDetail, loanee);
			if(flag == Constant.UPDATE_SUCCESS) {
				result = loanRecordService.getDetail(detailId, 1,member);
			}else {
				return ResponseData.error("借条状态已更新，操作失败");
			}
		}else{
			//视频审合通过
			applyDetail.setAliveVideoStatus(NfsLoanApplyDetail.AliveVideoStatus.passed);
			try {
				NfsLoanRecord loanRecord= loanRecordService.createLoanRecord(applyDetail,TrxRuleConstant.LOANER_PASS_VIDEO);
				if(loanRecord == null) {
					return ResponseData.error("借条申请异常，操作失败！");
				}
				result = loanRecordService.getDetail(loanRecord.getId().toString(), 2,member);
			} catch (Exception e) {
				logger.error(Exceptions.getStackTraceAsString(e));
				return ResponseData.error("账户异常，操作失败！");
			}
		}
		return ResponseData.success("审核视频结束", result);
	}
}