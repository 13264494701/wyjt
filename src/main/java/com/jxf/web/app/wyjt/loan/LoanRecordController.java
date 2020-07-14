
package com.jxf.web.app.wyjt.loan;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.constant.LoanConstant;
import com.jxf.loan.constant.RecordMessage;
import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanApply.RepayType;
import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.entity.NfsLoanApplyDetail.AliveVideoStatus;
import com.jxf.loan.entity.NfsCrAuction;
import com.jxf.loan.entity.NfsLoanArbitration;
import com.jxf.loan.entity.NfsLoanCollection;
import com.jxf.loan.entity.NfsLoanContract;
import com.jxf.loan.entity.NfsLoanDetailMessage;
import com.jxf.loan.entity.NfsLoanOperatingRecord;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRecord.AuctionStatus;
import com.jxf.loan.entity.NfsLoanRepayRecord;
import com.jxf.loan.service.NfsCrAuctionService;
import com.jxf.loan.service.NfsLoanApplyDetailService;
import com.jxf.loan.service.NfsLoanApplyService;
import com.jxf.loan.service.NfsLoanArbitrationService;
import com.jxf.loan.service.NfsLoanCollectionService;
import com.jxf.loan.service.NfsLoanContractService;
import com.jxf.loan.service.NfsLoanDetailMessageService;
import com.jxf.loan.service.NfsLoanOperatingRecordService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.service.NfsLoanRepayRecordService;
import com.jxf.loan.utils.LoanUtils;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.MemUtils;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.svc.annotation.AccessLimit;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.loan.FindByUsernameOrNameRequestParam;
import com.jxf.web.model.wyjt.app.loan.FindByUsernameOrNameResponseResult;
import com.jxf.web.model.wyjt.app.loan.LoanCertificateRequestParam;
import com.jxf.web.model.wyjt.app.loan.LoanCertificateResponseResult;
import com.jxf.web.model.wyjt.app.loan.LoanDetailForAppRequestParam;
import com.jxf.web.model.wyjt.app.loan.LoanDetailForAppResponseResult;
import com.jxf.web.model.wyjt.app.loan.LoanFundDetail;
import com.jxf.web.model.wyjt.app.loan.LoanListForAppRequestParam;
import com.jxf.web.model.wyjt.app.loan.LoanListForAppResponseResult;
import com.jxf.web.model.wyjt.app.loan.LoanListForAppResponseResult.LoanForApp;
import com.jxf.web.model.wyjt.app.loan.LoanMoreInforRequestParam;
import com.jxf.web.model.wyjt.app.loan.LoanMoreInforResponseResult;
import com.jxf.web.model.wyjt.app.loan.LoanMoreInforResponseResult.HistoryRecord;
import com.jxf.web.model.wyjt.app.loan.LoanPeriodizationDetail;



/**
 * Controller - 内容管理
 * 
 * @author JINXINFU
 * @version 2.0
 */
@Controller("wyjtAppLoanRecordController")
@RequestMapping(value="${wyjtApp}/loan")
public class LoanRecordController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(LoanRecordController.class);
	
	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsLoanApplyService loanApplyService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsLoanDetailMessageService loanDetailMessageService;
	@Autowired
	private NfsLoanApplyDetailService loanApplyDetailService;
	@Autowired
	private NfsLoanCollectionService loanCollectionService;
	@Autowired
	private NfsLoanArbitrationService loanArbitrationService;

    @Autowired
    private NfsCrAuctionService crAuctionService;
	@Autowired
	private NfsLoanRepayRecordService loanRepayRecordService;
	@Autowired
	private NfsLoanOperatingRecordService loanOperatingRecordService;

	@Autowired
	private NfsLoanContractService loanContractService;

	
	/**
	 * 借条详情
	 */
	@RequestMapping(value = "/loanDetailForApp")
	public @ResponseBody
	ResponseData getDetail(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		LoanDetailForAppRequestParam reqData = JSONObject.parseObject(param,LoanDetailForAppRequestParam.class);
		String loanId = reqData.getLoanId();
		Integer type = reqData.getType();
		if(StringUtils.isBlank(loanId)) {
			logger.error("借条详情查询失败,借条ID不能为空,当前用户为{}",member.getId());
			return ResponseData.error("借条详情查询失败,借条ID不能为空");
		}
		LoanDetailForAppResponseResult result  = new LoanDetailForAppResponseResult();
		try {
			result = loanRecordService.getDetail(loanId, type,member);
		} catch (Exception e) {
			logger.error("报错了问题ID{}",loanId);
			logger.error(Exceptions.getStackTraceAsString(e));
			return ResponseData.error("后端程序错误!");
		}
		return ResponseData.success("详情查询成功", result);
	}
	
	/**
	 * 根据姓名电话查找借条
	 */
	@RequestMapping(value = "/findByUsernameOrName")
	public @ResponseBody
	ResponseData findByUsernameOrName(HttpServletRequest request) {
		String param = request.getParameter("param");
		FindByUsernameOrNameRequestParam reqData = JSONObject.parseObject(param,FindByUsernameOrNameRequestParam.class);
		String usernameOrName = reqData.getUsernameOrName();
		
		if(StringUtils.isBlank(usernameOrName)){//输入是空
			return ResponseData.error("输入内容为空");
		}
		
		Member member = memberService.getCurrent();
		Long memberId = member.getId();
		member = new Member();
		member.setId(memberId);
		Member friend = new Member();
		
		if(usernameOrName.length() == 11){//输入的是电话
			boolean matches = Pattern.matches("\\d+",usernameOrName);
			if(!matches){
				return ResponseData.error("请输入正确的电话号!");
			}
			friend.setUsername(usernameOrName);
			Member findByUsername = memberService.findByUsername(usernameOrName);//根据电话号查ID
			if(findByUsername != null){
				friend = new Member();
				friend.setId(findByUsername.getId());//电话可能跟借条保存的不一致所以取ID是准的
			}
		}else{
			friend.setName(usernameOrName);
		}
		
		NfsLoanApplyDetail detail = new NfsLoanApplyDetail();
		NfsLoanApply apply = new NfsLoanApply();
		apply.setTrxType(NfsLoanApply.TrxType.online);
		NfsLoanRecord loanRecord = new NfsLoanRecord();
		
		FindByUsernameOrNameResponseResult result = new FindByUsernameOrNameResponseResult();
		List<LoanForApp> meOweWhoList = result.getMeOweWhoList();//我欠谁钱
		List<LoanForApp> whoOweMeList = result.getWhoOweMeList();//谁欠我钱
		
		/**
		 * 我欠好友的
		 */
		//申请
		apply.setMember(member);
		apply.setLoanRole(NfsLoanApply.LoanRole.loanee);
		detail.setLoanRole(NfsLoanApply.LoanRole.loaner);
		detail.setMember(friend);
		apply.setDetail(detail);
		apply.setLoanType(NfsLoanApply.LoanType.single);
		List<NfsLoanApply> meOweWhoApplyList = new ArrayList<NfsLoanApply>();
		List<NfsLoanApply> meOweWhoApplyPartOne = loanApplyService.findSingleLoanApplyListForApp(apply);
	
		apply.setMember(friend);
		apply.setLoanRole(NfsLoanApply.LoanRole.loaner);
		detail.setLoanRole(NfsLoanApply.LoanRole.loanee);
		detail.setMember(member);
		apply.setDetail(detail);
		List<NfsLoanApply> meOweWhoApplyPartTwo = loanApplyService.findSingleLoanApplyListForApp(apply);
		
		if(meOweWhoApplyPartOne != null && meOweWhoApplyPartOne.size() > 0){
			meOweWhoApplyList.addAll(meOweWhoApplyPartOne);
		} 
		if(meOweWhoApplyPartTwo != null && meOweWhoApplyPartTwo.size() > 0){
			meOweWhoApplyList.addAll(meOweWhoApplyPartTwo);
		}
		
		if(meOweWhoApplyPartOne != null){
			for (NfsLoanApply nfsLoanApply : meOweWhoApplyPartOne) {
				LoanForApp loanForApp = new LoanForApp();
				loanForApp.setAmount(nfsLoanApply.getDetail().getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				loanForApp.setIconStatus(0);
				if(nfsLoanApply.getInterest() != null){
					loanForApp.setInterest(nfsLoanApply.getInterest().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				} 
				loanForApp.setIsOverdue(0);
				loanForApp.setIsToday(0);
				loanForApp.setLoanId(nfsLoanApply.getId().toString());
				
				if(nfsLoanApply.getMember().equals(member)){
					loanForApp.setPartnerHeadImage(nfsLoanApply.getDetail().getMember().getHeadImage());
					loanForApp.setPartnerName(nfsLoanApply.getDetail().getMember().getName());
				}else{
					loanForApp.setPartnerHeadImage(nfsLoanApply.getMember().getHeadImage());
					loanForApp.setPartnerName(nfsLoanApply.getMember().getName());
				}
				
				NfsLoanApplyDetail nfsLoanApplyDetail = nfsLoanApply.getDetail();
				nfsLoanApplyDetail = loanApplyDetailService.get(nfsLoanApplyDetail);
				
				String progress = loanApplyDetailService.getDetailProgress(nfsLoanApplyDetail, nfsLoanApply,member);
				
				loanForApp.setProgress(progress);
				Date createTime = nfsLoanApply.getCreateTime();
				Date repayDate = DateUtils.addCalendarByDate(createTime, nfsLoanApply.getTerm());
				loanForApp.setRepayDate(DateUtils.formatDate(repayDate, "yyyy-MM-dd"));
				loanForApp.setRepayType(nfsLoanApply.getRepayType().ordinal());
				loanForApp.setType(LoanConstant.TYPE_DETAIL);
				meOweWhoList.add(loanForApp);
			}
		}
	
		
		//借条
		loanRecord.setLoanee(member);
		loanRecord.setLoaner(friend);
		List<NfsLoanRecord> findmeOweWhoList = loanRecordService.findList(loanRecord);
		if(findmeOweWhoList != null){
			for (NfsLoanRecord nfsLoanRecord : findmeOweWhoList) {
				LoanForApp loanForApp = new LoanForApp();
				
				loanForApp.setAmount(nfsLoanRecord.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				loanForApp.setIconStatus(0);
				if(nfsLoanRecord.getInterest() != null){
					loanForApp.setInterest(nfsLoanRecord.getInterest().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				} 
				Date dueRepayDate = nfsLoanRecord.getDueRepayDate();
				long pastDays = DateUtils.pastDays(dueRepayDate);
				if(pastDays <= 0){
					loanForApp.setIsOverdue(0);
				}else{
					loanForApp.setIsOverdue(1);
				}
				String year = DateUtils.getYear(dueRepayDate);
				String day = DateUtils.getDay(dueRepayDate);
				String nowYear = DateUtils.getYear();
				String nowDay = DateUtils.getDay();
				if(Integer.parseInt(year) == Integer.parseInt(nowYear) && Integer.parseInt(day) == Integer.parseInt(nowDay)){
					loanForApp.setIsToday(1);
				}else{
					loanForApp.setIsToday(0);
				}
				if(nfsLoanRecord.getLoanee().getId().equals(member.getId())){
					loanForApp.setPartnerHeadImage(nfsLoanRecord.getLoaner().getHeadImage());
					loanForApp.setPartnerName(nfsLoanRecord.getLoaner().getName());
				}else{
					loanForApp.setPartnerHeadImage(nfsLoanRecord.getLoanee().getHeadImage());
					loanForApp.setPartnerName(nfsLoanRecord.getLoanee().getName());
				}
				
				String progress = loanRecordService.getRecordProgress(nfsLoanRecord,member);
				
				loanForApp.setProgress(progress);
				
				loanForApp.setRepayDate(DateUtils.formatDate(nfsLoanRecord.getDueRepayDate(), "yyyy-MM-dd"));
				loanForApp.setRepayType(nfsLoanRecord.getRepayType().ordinal());
				loanForApp.setType(LoanConstant.TYPE_RECORD);
				loanForApp.setLoanId(nfsLoanRecord.getId().toString());
				meOweWhoList.add(loanForApp);
			}
		}
		
		
		if(meOweWhoList.size() > 0){
			result.setMeOweWhoList(meOweWhoList);
			result.setMeOweWhoNum(meOweWhoList.size());
		}else{
			result.setMeOweWhoNum(0);
		}
		
		/**
		 * 好友欠我的
		 */
		//申请
		apply.setMember(member);
		apply.setLoanRole(NfsLoanApply.LoanRole.loaner);
		detail.setLoanRole(NfsLoanApply.LoanRole.loanee);
		detail.setMember(friend);
		apply.setLoanType(NfsLoanApply.LoanType.single);
		apply.setDetail(detail);
		
		List<NfsLoanApply> whoOweMeApplyList = new ArrayList<NfsLoanApply>();
		List<NfsLoanApply> whoOweMeListApplyPartOne = loanApplyService.findSingleLoanApplyListForApp(apply);
		
		apply.setMember(friend);
		apply.setLoanRole(NfsLoanApply.LoanRole.loanee);
		detail.setLoanRole(NfsLoanApply.LoanRole.loaner);
		detail.setMember(member);
		apply.setDetail(detail);
		List<NfsLoanApply> whoOweMeListApplyPartTwo = loanApplyService.findSingleLoanApplyListForApp(apply);
		
		if(whoOweMeListApplyPartOne != null && whoOweMeListApplyPartOne.size() > 0){
			whoOweMeApplyList.addAll(whoOweMeListApplyPartOne);
		}
		if(whoOweMeListApplyPartTwo != null && whoOweMeListApplyPartTwo.size() > 0){
			whoOweMeApplyList.addAll(whoOweMeListApplyPartTwo);
		}
		
		if(whoOweMeApplyList != null){
			for (NfsLoanApply loanApply : whoOweMeApplyList) {
				LoanForApp loanForApp = new LoanForApp();
				loanForApp.setAmount(loanApply.getDetail().getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				loanForApp.setIconStatus(0);
				if(loanApply.getInterest() != null){
					loanForApp.setInterest(loanApply.getInterest().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				} 
				loanForApp.setIsOverdue(0);
				loanForApp.setIsToday(0);
				
				if(loanApply.getMember().getId().equals(member.getId())){
					loanForApp.setPartnerHeadImage(loanApply.getDetail().getMember().getHeadImage());
					loanForApp.setPartnerName(loanApply.getDetail().getMember().getName());
				}else{
					loanForApp.setPartnerHeadImage(loanApply.getMember().getHeadImage());
					loanForApp.setPartnerName(loanApply.getMember().getName());
				}
				NfsLoanApplyDetail loanApplyDetail = loanApply.getDetail();
				loanApplyDetail = loanApplyDetailService.get(loanApplyDetail);
				loanForApp.setLoanId(loanApplyDetail.getId().toString());
				
				String progress = loanApplyDetailService.getDetailProgress(loanApplyDetail, loanApply,member);
				
				loanForApp.setProgress(progress);
				
				Date createTime = loanApply.getCreateTime();
				Date repayDate = DateUtils.addCalendarByDate(createTime, loanApply.getTerm());
				loanForApp.setRepayDate(DateUtils.formatDate(repayDate, "yyyy-MM-dd"));
				loanForApp.setRepayType(loanApply.getRepayType().ordinal());
				loanForApp.setType(LoanConstant.TYPE_DETAIL);
				whoOweMeList.add(loanForApp);
			}
		}
		
		
		//借条
		loanRecord.setLoaner(member);
		loanRecord.setLoanee(friend);
		List<NfsLoanRecord> findwhoOweMeListList = loanRecordService.findWhoOweMeList(loanRecord);
		if(findwhoOweMeListList != null){
		for (NfsLoanRecord loan : findwhoOweMeListList) {
			LoanForApp loanForApp = new LoanForApp();
			
			loanForApp.setAmount(loan.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			loanForApp.setIconStatus(0);
			if(loan.getInterest() != null){
				loanForApp.setInterest(loan.getInterest().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			} 
			Date dueRepayDate = loan.getDueRepayDate();
			long pastDays = DateUtils.pastDays(dueRepayDate);
			if(pastDays <= 0){
				loanForApp.setIsOverdue(0);
			}else{
				loanForApp.setIsOverdue(1);
			}
			String year = DateUtils.getYear(dueRepayDate);
			String day = DateUtils.getDay(dueRepayDate);
			String nowYear = DateUtils.getYear();
			String nowDay = DateUtils.getDay();
			if(Integer.parseInt(year) == Integer.parseInt(nowYear) && Integer.parseInt(day) == Integer.parseInt(nowDay)){
				loanForApp.setIsToday(1);
			}else{
				loanForApp.setIsToday(0);
			}
			if(loan.getLoanee().getId().equals(member.getId())){
				loanForApp.setPartnerHeadImage(loan.getLoaner().getHeadImage());
				loanForApp.setPartnerName(loan.getLoaner().getName());
			}else{
				loanForApp.setPartnerHeadImage(loan.getLoanee().getHeadImage());
				loanForApp.setPartnerName(loan.getLoanee().getName());
			}
			
			String progress = loanRecordService.getRecordProgress(loan,member);
			
			loanForApp.setProgress(progress);
		
			loanForApp.setRepayDate(DateUtils.formatDate(loan.getDueRepayDate(), "yyyy-MM-dd"));
			loanForApp.setRepayType(loan.getRepayType().ordinal());
			loanForApp.setType(LoanConstant.TYPE_RECORD);
			loanForApp.setLoanId(loan.getId().toString());
			whoOweMeList.add(loanForApp);
			}
		}
		
		
		if(whoOweMeList.size() > 0){
			result.setWhoOweMeList(whoOweMeList);
			result.setWhoOweMeNum(whoOweMeList.size());
		}else{
			result.setWhoOweMeNum(0);
		}
		return ResponseData.success("查询成功", result);
	}
	
	
	
   /**
	*   查询借款申请列表给APP展示用(单人多人 都是这一个)
	*	0:借条列表排序 待确认/待放款/已失效/多人借款：按照借条操作时间倒叙排列；  实现了update_time倒叙
	*	1:待收款/待还款：今日还款 借条操作时间倒叙、剩余还款日由小到大排序；          实现了due_repay_date正序
	*	2:已逾期：按照今日逾期、按照逾期天数由小到大排序；                                         实现了due_repay_date倒叙
	*	3:已还款：按照约定的还款时间倒叙排列；                                                              实现了due_repay_date倒叙
	*	4:搜索列表 排序 先显示今日还款、今日逾期 再显示操作的借条，再显示剩余还款的时长由少到多，再显示已完成的借条，最后显示已失效的借条 TODO 待优化
    *	5:高级搜索排序 今日还款、今日逾期、逾期天数、操作时间、剩余还款（未还）、还款时间（已还）、失效                                                          TODO 待优化
	*/
	@RequestMapping(value = "/loanListForApp")
	public @ResponseBody
	ResponseData loanListForApp(HttpServletRequest request) {
		String appVersion = request.getHeader("x-appVersion");//版本
		String osType = request.getHeader("x-osType");//平台
		
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		LoanListForAppRequestParam reqData = JSONObject.parseObject(param,LoanListForAppRequestParam.class);
		String usernameOrName = reqData.getUsernameOrName();
		Member find = new Member();
		if (StringUtils.isNotBlank(usernameOrName)) {
			if (usernameOrName.length() == 11) {
				boolean matches = Pattern.matches("\\d+", usernameOrName);
				if (!matches) {
					return ResponseData.error("请输入正确的电话号!");
				}
				find = memberService.findByUsername(usernameOrName);// 根据电话号查ID
			} else {
				find.setName(usernameOrName);
			}
		}
		//TODO 需和前端确定版本
		LoanListForAppResponseResult result = new LoanListForAppResponseResult();
		result = (LoanListForAppResponseResult) loanListForAppV1(reqData,member,find);
		return ResponseData.success("查询借款申请列表成功", result);
	}
	
	/**
	 * 借条列表 - 适用于2019年6月-7月新发版本
	 * @param request
	 * @return
	 */
	private Object loanListForAppV1(LoanListForAppRequestParam reqData,Member member,Member find) {
		LoanListForAppResponseResult result = new LoanListForAppResponseResult();
		String beginDate = reqData.getBeginDate();
		String endDate = reqData.getEndDate();
		String maxAmount = reqData.getMaxAmount();
		String minAmount = reqData.getMinAmount();
		Integer loanType = reqData.getLoanType();// 0->单人，1->多人
		Integer pageNo = reqData.getPageNo();
		Integer repayType = reqData.getRepayType();
		Integer loanRole = reqData.getLoanRole();// 0->借款人，1->放款人
		Integer status = reqData.getStatus();
		Integer orderBy = reqData.getOrderBy();
		
		Long memberId = member.getId();
		//Integer subStatus = reqData.getSubStatus();

		NfsLoanApply loanApply = new NfsLoanApply();
		loanApply.setTrxType(NfsLoanApply.TrxType.online);
		if(StringUtils.isNotBlank(beginDate)){
			loanApply.setBeginDate(DateUtils.parseDate(beginDate));
		}
		if(StringUtils.isNotBlank(endDate)){
			Date end = DateUtils.parseDate(endDate);
			loanApply.setEndDate(DateUtils.addCalendarByDate(end, 1));
		}
		if(loanRole != null){
			loanApply.setLoanRole(NfsLoanApply.LoanRole.values()[loanRole]);
		}
		if(loanType != null){
			loanApply.setLoanType(NfsLoanApply.LoanType.values()[loanType]);
		}
		if(StringUtils.isNotBlank(maxAmount)){
			loanApply.setMaxAmount(new BigDecimal(maxAmount));
		}
		if(StringUtils.isNotBlank(minAmount)){
			loanApply.setMinAmount(new BigDecimal(minAmount));
		}
		if(repayType != null && repayType != -1){
			loanApply.setRepayType(NfsLoanApply.RepayType.values()[repayType]);
		}
		Page<NfsLoanApply> pageApply = null;
		List<NfsLoanApply> findList = null;
		List<LoanForApp> resultList = result.getLoanList();

		//查apply
		if(loanType == 0){//单人
			String orderByStr = null;
			if(orderBy != null) {
				if(orderBy == 1){
					orderByStr = " c.amount ";
				}else if(orderBy == 2){
					orderByStr = " c.amount desc ";
				}else if(orderBy == 3){
					orderByStr = " date_add(a.create_time, interval a.term day) "; 
				}else if(orderBy == 4){
					orderByStr = " date_add(a.create_time, interval a.term day) desc "; 
				}
			}
			NfsLoanApplyDetail detail = new NfsLoanApplyDetail();
			detail.setTrxType(NfsLoanApply.TrxType.online);
			detail.setMember(find);
			if(status == 0){
				detail.setStatus(NfsLoanApplyDetail.Status.values()[status]);
				loanApply.setDetail(detail);
				loanApply.setMember(member);
				pageApply = loanApplyService.findMemberSingleLoanApplyListForApp(loanApply,pageNo,20,orderByStr);	
			}else if(status == 4){
				detail.setStatus(null);
				loanApply.setDetail(detail);
				loanApply.setMember(member);
				pageApply = loanApplyService.findMemberSingleLoanApplyListForApp(loanApply,pageNo,20,orderByStr);	
			}
		}else{//多人
			String orderByStr = null;
			if(orderBy == 1){
				orderByStr = " a.amount ";
			}else if(orderBy == 2){
				orderByStr = " a.amount desc ";
			}else if(orderBy == 3){
				orderByStr = " a.due_repay_date "; 
			}else if(orderBy == 4){
				orderByStr = " a.due_repay_date desc "; 
			}
			pageApply = new Page<NfsLoanApply>();
			pageApply.setPageNo(pageNo);
			pageApply.setPageSize(20);
			if(StringUtils.isNotBlank(orderByStr)) {
				pageApply.setOrderBy(orderByStr);
			}
			NfsLoanApplyDetail detail = new NfsLoanApplyDetail();
			detail.setTrxType(NfsLoanApply.TrxType.online);
			detail.setMember(find);
			if(loanRole == 1){//我收到的申请
				loanApply.setLoanRole(NfsLoanApply.LoanRole.loanee);
				detail.setStatus(NfsLoanApplyDetail.Status.pendingAgree);
				detail.setMember(member);
				loanApply.setDetail(detail);
				detail.setAliveVideoStatus(AliveVideoStatus.notUpload);
				pageApply = loanApplyService.findMultipleLoanApplyPage(pageApply,loanApply);	
			}else{ //我发起的申请
				loanApply.setMember(member);
				loanApply.setLoanRole(NfsLoanApply.LoanRole.loanee);
				loanApply.setRemainAmount(BigDecimal.ZERO);
				List<NfsLoanApply> findMultipleList = loanApplyService.findList(loanApply);	
				pageApply.setList(findMultipleList);
			}
		}
		if(pageApply != null){
			findList = pageApply.getList();
		}
		if(findList != null && findList.size() > 0){
			for (NfsLoanApply nfsLoanApply : findList) {
				String amountStr;
				LoanForApp loanForApp = new LoanForApp();
				Long applyId;
				NfsLoanApplyDetail detail = null;
				if(loanType == 1 && loanRole == 0){//我发起的多人借款
					 applyId = nfsLoanApply.getId();
					 loanForApp.setPartnerHeadImage(member.getHeadImage());
					 loanForApp.setPartnerName(member.getName());
					 loanForApp.setType(LoanConstant.TYPE_APPLY);
					 amountStr = StringUtils.decimalToStr(nfsLoanApply.getRemainAmount(), 2);
				}else{
					detail = nfsLoanApply.getDetail();
					detail = loanApplyDetailService.get(detail);
					 applyId = detail.getId();
					 Member applyMember = nfsLoanApply.getMember();
					 if(memberId.equals(applyMember.getId())){//我是发起者
						 loanForApp.setPartnerHeadImage(detail.getMember().getHeadImage());
						 loanForApp.setPartnerName(detail.getMember().getName());
					 }else{
						 loanForApp.setPartnerHeadImage(applyMember.getHeadImage());
						 loanForApp.setPartnerName(applyMember.getName());
					 }
					 loanForApp.setType(LoanConstant.TYPE_DETAIL);
					 if(loanType == 1){//我收到的多人借款
						 amountStr = StringUtils.decimalToStr(nfsLoanApply.getRemainAmount(), 2);
					 }else{
						 amountStr = StringUtils.decimalToStr(detail.getAmount(), 2);
					 }
				}
				loanForApp.setLoanId(applyId.toString());
				loanForApp.setAmount(amountStr);
				if(nfsLoanApply.getInterest() != null){
					if(loanType == 0){
						loanForApp.setInterest(nfsLoanApply.getInterest().setScale(2, BigDecimal.ROUND_HALF_UP)+"");
					}else{
						String intRateStr = StringUtils.decimalToStr(nfsLoanApply.getIntRate(), 1);
						loanForApp.setInterest(intRateStr);
					}
				}else{
					loanForApp.setInterest("0");
				}
				
				Integer term = nfsLoanApply.getTerm();
				Date createTime = nfsLoanApply.getCreateTime();
				Date repayDate = DateUtils.addCalendarByDate(createTime, term-1);
				loanForApp.setRepayDate(DateUtils.formatDate(repayDate, "yyyy-MM-dd"));
				
				loanForApp.setRepayType(nfsLoanApply.getRepayType().ordinal());
				loanForApp.setIconStatus(0);
				
				//获取进度
				String progress = "";
				if(loanType == 1 && loanRole == 0){//我发起的多人借款
				}else{
					progress = loanApplyDetailService.getDetailProgress(detail,nfsLoanApply,member);
				}
				loanForApp.setProgress(progress);
				resultList.add(loanForApp);
			}
		}
		
		if(loanType == 0 && (status == 1 || status == 2 || status == 3)){//多人不查record
			//查record
			NfsLoanRecord loanRecord = new NfsLoanRecord();	
			if(StringUtils.isNotBlank(beginDate)){
				loanRecord.setBeginDueRepayDate(DateUtils.parseDate(beginDate));
			}
			
			if(StringUtils.isNotBlank(endDate)){
				loanRecord.setEndDueRepayDate(DateUtils.parseDate(endDate));
			}
			
			if(loanRole == 0){
				loanRecord.setLoanee(member);
				loanRecord.setLoaner(find);
			}else{
				loanRecord.setLoaner(member);
				loanRecord.setLoanee(find);
			}
			
			if(StringUtils.isNotBlank(maxAmount)){
				loanRecord.setMaxAmount(new BigDecimal(maxAmount));
			}
			if(StringUtils.isNotBlank(minAmount)){
				loanRecord.setMinAmount(new BigDecimal(minAmount));
			}
			
			if(repayType != -1){
				loanRecord.setRepayType(NfsLoanApply.RepayType.values()[repayType]);
			}
			
			Page<NfsLoanRecord> pageRecord = null;
			loanRecord.setTrxType(NfsLoanApply.TrxType.online);
			Page<NfsLoanRecord> page = new Page<NfsLoanRecord>();
			String orderByStr = null;
			if(orderBy != null) {
				if(orderBy == 1){
					orderByStr = " a.amount ";
				}else if(orderBy == 2){
					orderByStr = " a.amount desc ";
				}else if(orderBy == 3){
					orderByStr = " a.due_repay_date "; 
				}else if(orderBy == 4){
					orderByStr = " a.due_repay_date desc "; 
				}
			}
			if(StringUtils.isNotBlank(orderByStr)) {
				page.setOrderBy(orderByStr);
				loanRecord.setPage(page);
			}
			if(status == 1){//剩余还款日由小到大排序；
				loanRecord.setStatus(NfsLoanRecord.Status.values()[0]);
				pageRecord = loanRecordService.findToPaidPage(loanRecord, reqData.getPageNo(), 20);		
			}else if(status == 2){//按照逾期天数由小到大排序；
				loanRecord.setStatus(NfsLoanRecord.Status.values()[2]);
				pageRecord = loanRecordService.findClosedPage(loanRecord, reqData.getPageNo(), 20);		
			}else if(status == 3){//按照还款时间倒叙排列； 
				loanRecord.setStatus(NfsLoanRecord.Status.values()[1]);
				pageRecord = loanRecordService.findClosedPage(loanRecord, reqData.getPageNo(), 20);		
			}else{
				pageRecord = loanRecordService.findPage(loanRecord, reqData.getPageNo(), 20);		
			}
					
			
			for(NfsLoanRecord everyLoanRecord:pageRecord.getList()) {
				LoanForApp loan = new LoanForApp();
				String amountStr = StringUtils.decimalToStr(everyLoanRecord.getAmount(), 2);
				loan.setAmount(amountStr);
				loan.setInterest(everyLoanRecord.getInterest().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				loan.setLoanId(everyLoanRecord.getId().toString());
				Member partner = null;
				if(loanRole == 0){
					partner = everyLoanRecord.getLoaner();
				}else{
					partner = everyLoanRecord.getLoanee();
				}
				loan.setPartnerHeadImage(partner.getHeadImage());
				loan.setPartnerName(partner.getName());
				
				Date dueRepayDate = everyLoanRecord.getDueRepayDate();
				String formatDueRepayDate = DateUtils.formatDate(dueRepayDate, "yyyy-MM-dd");
				loan.setRepayDate(formatDueRepayDate);
				String nowDateStr = DateUtils.getDate();
				
				if(StringUtils.equals(nowDateStr, formatDueRepayDate)){
					loan.setIsToday(1);
				}else{
					loan.setIsToday(0);
				}
				if(!everyLoanRecord.getStatus().equals(NfsLoanRecord.Status.overdue)){
					loan.setIsOverdue(0);
				}else{
					loan.setIsOverdue(1);
				}
				
				loan.setRepayType(everyLoanRecord.getRepayType().ordinal());
				//0不显示 1进行中 2成功 3失败
				loan.setIconStatus(0);
				//催收
				NfsLoanCollection nfsLoanCollection = new NfsLoanCollection();
				nfsLoanCollection.setLoan(everyLoanRecord);
				List<NfsLoanCollection> findCollectionList = loanCollectionService.findList(nfsLoanCollection);
				if(findCollectionList != null && findCollectionList.size() > 0){
					nfsLoanCollection = findCollectionList.get(0);
					com.jxf.loan.entity.NfsLoanCollection.Status collectionStatus = nfsLoanCollection.getStatus();
					if(collectionStatus.equals(NfsLoanCollection.Status.success)){
						loan.setIconStatus(2);
					}else if(collectionStatus.equals(NfsLoanCollection.Status.fail) || collectionStatus.equals(NfsLoanCollection.Status.refuse)){
						loan.setIconStatus(3);
					}else{
						loan.setIconStatus(1);
					}
				}
				
				//催收
				NfsLoanArbitration nfsLoanArbitration = new NfsLoanArbitration();
				nfsLoanArbitration.setLoan(everyLoanRecord);
				List<NfsLoanArbitration> findArbitrationList = loanArbitrationService.findList(nfsLoanArbitration);
				if(findArbitrationList != null && findArbitrationList.size() > 0){
					nfsLoanArbitration = findArbitrationList.get(0);
					com.jxf.loan.entity.NfsLoanArbitration.Status arbitrationStatus = nfsLoanArbitration.getStatus();
					if(arbitrationStatus.equals(NfsLoanArbitration.Status.arbitration)){
						loan.setIconStatus(2);
					}else if(arbitrationStatus.equals(NfsLoanArbitration.Status.arbitrationFailure) || arbitrationStatus.equals(NfsLoanArbitration.Status.auditFailure)
							|| arbitrationStatus.equals(NfsLoanArbitration.Status.failureToFile)){
						loan.setIconStatus(3);
					}else{
						loan.setIconStatus(1);
					}
				}
				
				String progress = loanRecordService.getRecordProgress(everyLoanRecord , member);
				loan.setProgress(progress);
				loan.setType(LoanConstant.TYPE_RECORD);
				resultList.add(loan);
			}
		}
		result.setLoanList(resultList);
		return result;
	}
	
	/**
	 *  借条列表 - 初始版本
	 * @param request
	 * @return
	 */
	private LoanListForAppResponseResult loanListForAppV0(LoanListForAppRequestParam reqData,Member member) {
		LoanListForAppResponseResult result = new LoanListForAppResponseResult();
		String beginDate = reqData.getBeginDate();
		String endDate = reqData.getEndDate();
		String maxAmount = reqData.getMaxAmount();
		String minAmount = reqData.getMinAmount();
		Integer loanType = reqData.getLoanType();// 0->单人，1->多人
		Integer pageNo = reqData.getPageNo();
		Integer repayType = reqData.getRepayType();
		Integer loanRole = reqData.getLoanRole();// 0->借款人，1->放款人
		Integer status = reqData.getStatus();
		//Integer subStatus = reqData.getSubStatus();

		Long memberId = member.getId();
		
		NfsLoanApply loanApply = new NfsLoanApply();
		loanApply.setTrxType(NfsLoanApply.TrxType.online);
		if(StringUtils.isNotBlank(beginDate)){
			loanApply.setBeginDate(DateUtils.parseDate(beginDate));
		}
		if(StringUtils.isNotBlank(endDate)){
			Date end = DateUtils.parseDate(endDate);
			loanApply.setEndDate(DateUtils.addCalendarByDate(end, 1));
		}
		if(loanRole != null){
			loanApply.setLoanRole(NfsLoanApply.LoanRole.values()[loanRole]);
		}
		if(loanType != null){
			loanApply.setLoanType(NfsLoanApply.LoanType.values()[loanType]);
		}
		if(StringUtils.isNotBlank(maxAmount)){
			loanApply.setMaxAmount(new BigDecimal(maxAmount));
		}
		if(StringUtils.isNotBlank(minAmount)){
			loanApply.setMinAmount(new BigDecimal(minAmount));
		}
		if(repayType != null && repayType != -1){
			loanApply.setRepayType(NfsLoanApply.RepayType.values()[repayType]);
		}
		Page<NfsLoanApply> pageApply = null;
		List<NfsLoanApply> findList = null;
		List<LoanForApp> resultList = result.getLoanList();
		//查apply
		if(loanType == 0){//单人
			NfsLoanApplyDetail detail = new NfsLoanApplyDetail();
			detail.setTrxType(NfsLoanApply.TrxType.online);
			if(status == 0){
				detail.setStatus(NfsLoanApplyDetail.Status.values()[status]);
				loanApply.setDetail(detail);
				loanApply.setMember(member);
				pageApply = loanApplyService.findMemberSingleLoanApplyListForApp(loanApply,pageNo,20,null);	
			}else if(status == 4){
				detail.setStatus(null);
				loanApply.setDetail(detail);
				loanApply.setMember(member);
				pageApply = loanApplyService.findMemberSingleLoanApplyListForApp(loanApply,pageNo,20,null);	
			}
		}else{//多人
			pageApply = new Page<NfsLoanApply>();
			pageApply.setPageNo(pageNo);
			pageApply.setPageSize(20);
			NfsLoanApplyDetail detail = new NfsLoanApplyDetail();
			detail.setTrxType(NfsLoanApply.TrxType.online);
			if(loanRole == 1){//我收到的申请
				loanApply.setLoanRole(NfsLoanApply.LoanRole.loanee);
				detail.setStatus(NfsLoanApplyDetail.Status.pendingAgree);
				detail.setMember(member);
				loanApply.setDetail(detail);
				detail.setAliveVideoStatus(AliveVideoStatus.notUpload);
				pageApply = loanApplyService.findMultipleLoanApplyPage(pageApply,loanApply);	
			}else{ //我发起的申请
				loanApply.setMember(member);
				loanApply.setLoanRole(NfsLoanApply.LoanRole.loanee);
				loanApply.setRemainAmount(BigDecimal.ZERO);
				List<NfsLoanApply> findMultipleList = loanApplyService.findList(loanApply);	
				pageApply.setList(findMultipleList);
			}
		}
		if(pageApply != null){
			findList = pageApply.getList();
		}
		if(findList != null && findList.size() > 0){
			for (NfsLoanApply nfsLoanApply : findList) {
				String amountStr;
				LoanForApp loanForApp = new LoanForApp();
				Long applyId;
				NfsLoanApplyDetail detail = null;
				if(loanType == 1 && loanRole == 0){//我发起的多人借款
					 applyId = nfsLoanApply.getId();
					 loanForApp.setPartnerHeadImage(member.getHeadImage());
					 loanForApp.setPartnerName(member.getName());
					 loanForApp.setType(LoanConstant.TYPE_APPLY);
					 amountStr = StringUtils.decimalToStr(nfsLoanApply.getRemainAmount(), 2);
				}else{
					detail = nfsLoanApply.getDetail();
					detail = loanApplyDetailService.get(detail);
					 applyId = detail.getId();
					 Member applyMember = nfsLoanApply.getMember();
					 if(memberId.equals(applyMember.getId())){//我是发起者
						 loanForApp.setPartnerHeadImage(detail.getMember().getHeadImage());
						 loanForApp.setPartnerName(detail.getMember().getName());
					 }else{
						 loanForApp.setPartnerHeadImage(applyMember.getHeadImage());
						 loanForApp.setPartnerName(applyMember.getName());
					 }
					 loanForApp.setType(LoanConstant.TYPE_DETAIL);
					 if(loanType == 1){//我收到的多人借款
						 amountStr = StringUtils.decimalToStr(nfsLoanApply.getRemainAmount(), 2);
					 }else{
						 amountStr = StringUtils.decimalToStr(detail.getAmount(), 2);
					 }
				}
				loanForApp.setLoanId(applyId.toString());
				loanForApp.setAmount(amountStr);
				if(nfsLoanApply.getInterest() != null){
					if(loanType == 0){
						loanForApp.setInterest(nfsLoanApply.getInterest().setScale(2, BigDecimal.ROUND_HALF_UP)+"");
					}else{
						String intRateStr = StringUtils.decimalToStr(nfsLoanApply.getIntRate(), 1);
						loanForApp.setInterest(intRateStr);
					}
				}else{
					loanForApp.setInterest("0");
				}
				
				Integer term = nfsLoanApply.getTerm();
				Date createTime = nfsLoanApply.getCreateTime();
				Date repayDate = DateUtils.addCalendarByDate(createTime, term-1);
				loanForApp.setRepayDate(DateUtils.formatDate(repayDate, "yyyy-MM-dd"));
				
				loanForApp.setRepayType(nfsLoanApply.getRepayType().ordinal());
				loanForApp.setIconStatus(0);
				
				//获取进度
				String progress = "";
				if(loanType == 1 && loanRole == 0){//我发起的多人借款
				}else{
					progress = loanApplyDetailService.getDetailProgress(detail,nfsLoanApply,member);
				}
				loanForApp.setProgress(progress);
				resultList.add(loanForApp);
			}
		}
		
		if(loanType == 0 && (status == 1 || status == 2 || status == 3)){//多人不查record
			//查record
			NfsLoanRecord loanRecord = new NfsLoanRecord();	
			if(StringUtils.isNotBlank(beginDate)){
				loanRecord.setBeginTime(DateUtils.parseDate(beginDate));
			}
			
			if(StringUtils.isNotBlank(endDate)){
				Date end = DateUtils.parseDate(endDate);
				loanRecord.setEndTime(DateUtils.addCalendarByDate(end, 1));
			}
			
			if(loanRole == 0){
				loanRecord.setLoanee(member);
			}else{
				loanRecord.setLoaner(member);
			}
			
			if(StringUtils.isNotBlank(maxAmount)){
				loanRecord.setMaxAmount(new BigDecimal(maxAmount));
			}
			if(StringUtils.isNotBlank(minAmount)){
				loanRecord.setMinAmount(new BigDecimal(minAmount));
			}
			
			if(repayType != -1){
				loanRecord.setRepayType(NfsLoanApply.RepayType.values()[repayType]);
			}
			
			Page<NfsLoanRecord> pageRecord = null;
			loanRecord.setTrxType(NfsLoanApply.TrxType.online);
			if(status == 1){//剩余还款日由小到大排序；
				loanRecord.setStatus(NfsLoanRecord.Status.values()[0]);
				pageRecord = loanRecordService.findToPaidPage(loanRecord, reqData.getPageNo(), 20);		
			}else if(status == 2){//按照逾期天数由小到大排序；
				loanRecord.setStatus(NfsLoanRecord.Status.values()[2]);
				pageRecord = loanRecordService.findClosedPage(loanRecord, reqData.getPageNo(), 20);		
			}else if(status == 3){//按照还款时间倒叙排列； 
				loanRecord.setStatus(NfsLoanRecord.Status.values()[1]);
				pageRecord = loanRecordService.findClosedPage(loanRecord, reqData.getPageNo(), 20);		
			}else{
				pageRecord = loanRecordService.findPage(loanRecord, reqData.getPageNo(), 20);		
			}
					
			
			for(NfsLoanRecord everyLoanRecord:pageRecord.getList()) {
				LoanForApp loan = new LoanForApp();
				String amountStr = StringUtils.decimalToStr(everyLoanRecord.getAmount(), 2);
				loan.setAmount(amountStr);
				loan.setInterest(everyLoanRecord.getInterest().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				loan.setLoanId(everyLoanRecord.getId().toString());
				Member partner = null;
				if(loanRole == 0){
					partner = everyLoanRecord.getLoaner();
				}else{
					partner = everyLoanRecord.getLoanee();
				}
				loan.setPartnerHeadImage(partner.getHeadImage());
				loan.setPartnerName(partner.getName());
				
				Date dueRepayDate = everyLoanRecord.getDueRepayDate();
				String formatDueRepayDate = DateUtils.formatDate(dueRepayDate, "yyyy-MM-dd");
				loan.setRepayDate(formatDueRepayDate);
				String nowDateStr = DateUtils.getDate();
				
				if(StringUtils.equals(nowDateStr, formatDueRepayDate)){
					loan.setIsToday(1);
				}else{
					loan.setIsToday(0);
				}
				if(!everyLoanRecord.getStatus().equals(NfsLoanRecord.Status.overdue)){
					loan.setIsOverdue(0);
				}else{
					loan.setIsOverdue(1);
				}
				
				loan.setRepayType(everyLoanRecord.getRepayType().ordinal());
				//0不显示 1进行中 2成功 3失败
				loan.setIconStatus(0);
				//催收
				NfsLoanCollection nfsLoanCollection = new NfsLoanCollection();
				nfsLoanCollection.setLoan(everyLoanRecord);
				List<NfsLoanCollection> findCollectionList = loanCollectionService.findList(nfsLoanCollection);
				if(findCollectionList != null && findCollectionList.size() > 0){
					nfsLoanCollection = findCollectionList.get(0);
					com.jxf.loan.entity.NfsLoanCollection.Status collectionStatus = nfsLoanCollection.getStatus();
					if(collectionStatus.equals(NfsLoanCollection.Status.success)){
						loan.setIconStatus(2);
					}else if(collectionStatus.equals(NfsLoanCollection.Status.fail) || collectionStatus.equals(NfsLoanCollection.Status.refuse)){
						loan.setIconStatus(3);
					}else{
						loan.setIconStatus(1);
					}
				}
				
				//催收
				NfsLoanArbitration nfsLoanArbitration = new NfsLoanArbitration();
				nfsLoanArbitration.setLoan(everyLoanRecord);
				List<NfsLoanArbitration> findArbitrationList = loanArbitrationService.findList(nfsLoanArbitration);
				if(findArbitrationList != null && findArbitrationList.size() > 0){
					nfsLoanArbitration = findArbitrationList.get(0);
					com.jxf.loan.entity.NfsLoanArbitration.Status arbitrationStatus = nfsLoanArbitration.getStatus();
					if(arbitrationStatus.equals(NfsLoanArbitration.Status.arbitration)){
						loan.setIconStatus(2);
					}else if(arbitrationStatus.equals(NfsLoanArbitration.Status.arbitrationFailure) || arbitrationStatus.equals(NfsLoanArbitration.Status.auditFailure)
							|| arbitrationStatus.equals(NfsLoanArbitration.Status.failureToFile)){
						loan.setIconStatus(3);
					}else{
						loan.setIconStatus(1);
					}
				}
				
				String progress = loanRecordService.getRecordProgress(everyLoanRecord , member);
				loan.setProgress(progress);
				loan.setType(LoanConstant.TYPE_RECORD);
				resultList.add(loan);
			}
		}
		result.setLoanList(resultList);
		return result;
	}
	
	
	
	/**
	 * 我要催款
	 */
	@RequestMapping(value = "/dept")
	public @ResponseBody
	ResponseData dept(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String loanId = request.getParameter("loanId");
		NfsLoanRecord nfsLoanRecord = loanRecordService.get(Long.parseLong(loanId));
		NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
		nfsLoanDetailMessage.setMember(member);
		NfsLoanApplyDetail loanDetail = nfsLoanRecord.getLoanApplyDetail();
		nfsLoanDetailMessage.setDetail(loanDetail);
		nfsLoanDetailMessage.setType(RecordMessage.SEND_REMIND);
		nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_2201);
		
		List<NfsLoanDetailMessage> findList = loanDetailMessageService.findList(nfsLoanDetailMessage);
		
		if(findList.size() >= 3 ){
			return ResponseData.error("已经催款3次");
		}
		//发消息
		loanDetailMessageService.save(nfsLoanDetailMessage);
		
		//返回详情
		LoanDetailForAppResponseResult result = loanRecordService.getDetail(loanId,2,member);
		
		return ResponseData.success("催款成功", result);
	}
	/**
	 * 	放款人主动放款，借款人确认收款
	 */
	@RequestMapping(value = "/confirmLendApplication")
	public @ResponseBody
	ResponseData confirmLendApplication(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String detailId = request.getParameter("loanId");
		if(StringUtils.isBlank(detailId)) {
			return ResponseData.error("收款失败,请联系客服！");
		}
		
		NfsLoanApplyDetail applyDetail = loanApplyDetailService.get(Long.parseLong(detailId));
		if(!applyDetail.getStatus().equals(NfsLoanApplyDetail.Status.pendingAgree)) {
			return ResponseData.error("借条申请状态已更新，请不要重复操作。");
		}	
		try {
			NfsLoanRecord loanRecord= loanRecordService.createLoanRecord(applyDetail,TrxRuleConstant.LOANEE_AGREE_ACTIVE_LOAN);
			if(loanRecord == null) {
				return ResponseData.error("借条申请异常，生成借条失败！");
			}
			LoanDetailForAppResponseResult result = loanRecordService.getDetail(loanRecord.getId().toString(), 2,member);
			return ResponseData.success("确认收款", result);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
			return ResponseData.error("账户异常，生成借条失败！");
		}
	}
	/**
	 * 查看电子借条
	 */
	@AccessLimit(maxCount = 2, seconds = 1)
	@RequestMapping(value = "/loanCertificate")
	public @ResponseBody
	ResponseData loanCertificate(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		//实名身份认证
		if(!VerifiedUtils.isVerified(member.getVerifiedList(), 1)){
			logger.warn("用户{}未实名认证,无法查看电子借条",member.getId());
			return ResponseData.error("身份不合法");
		}
		String param = request.getParameter("param");
		LoanCertificateRequestParam reqData = JSONObject.parseObject(param,LoanCertificateRequestParam.class);
		String loanId = reqData.getLoanId();
		Integer maskFlag = reqData.getChannel();
		//检查一下是否是detailId
//		NfsLoanApplyDetail loanApplyDetail = loanApplyDetailService.get(Long.parseLong(loanId));
		NfsLoanRecord loanRecord =  loanRecordService.get(Long.valueOf(loanId));
//		if(loanApplyDetail != null){
//			loanRecord = loanRecordService.findByApplyDetailId(loanApplyDetail.getId());
//			logger.warn("查看电子借条detailId{}",loanId);
//		}else{
//			loanRecord = loanRecordService.get(Long.valueOf(loanId));
//		}
			
		LoanCertificateResponseResult result = new LoanCertificateResponseResult();		
		result.setLoanNo("YXB-" + loanRecord.getId());
		result.setLoanPurp(loanRecord.getLoanPurp().getName());
		result.setAmount(StringUtils.decimalToStr(loanRecord.getAmount(), 2));
		result.setInterest(StringUtils.decimalToStr(loanRecord.getInterest(), 2));
		result.setDisputeResolution(loanRecord.getDisputeResolution().ordinal()==0?LoanConstant.DISPUTE_RESOLUTION_0:LoanConstant.DISPUTE_RESOLUTION_1);
			
		if(loanRecord.getStatus().equals(NfsLoanRecord.Status.overdue)){
			BigDecimal overdueDays = new BigDecimal(DateUtils.getDistanceOfTwoDate(loanRecord.getDueRepayDate(), new Date()));
			BigDecimal overdueInterest = LoanUtils.calOverdueInterest(loanRecord.getDueRepayAmount(), overdueDays);
			result.setOverdueInterest(StringUtils.decimalToStr(overdueInterest, 2));
		}else{
			result.setOverdueInterest(StringUtils.decimalToStr(loanRecord.getOverdueInterest() == null ? BigDecimal.ZERO : loanRecord.getOverdueInterest(), 2));
		}
		result.setRepayType(loanRecord.getRepayType().ordinal());
		result.setTerm(loanRecord.getTerm());
		
		Member loanee = loanRecord.getLoanee();
		Member loaner = loanRecord.getLoaner();
		if(maskFlag == 1){
			loanee = MemUtils.maskAll(loanee);			
			loaner = MemUtils.maskAll(loaner);		
		}
		result.setLoaneeName(loanee.getName());
		result.setLoaneeEmail(loanee.getEmail());
		result.setLoaneePhoneNo(loanee.getUsername());
		result.setLoaneeIdNo(loanee.getIdNo());
		
		result.setLoanerName(loaner.getName());
		result.setLoanerEmail(loaner.getEmail());
		result.setLoanerPhoneNo(loaner.getUsername());
		result.setLoanerIdNo(loaner.getIdNo());

	 	NfsLoanContract loanContract = new NfsLoanContract();	
	 	loanContract.setLoanId(Long.valueOf(loanId));
	 	loanContract.setStatus(NfsLoanContract.Status.signatured);
	 	loanContract.setCount(1);
    	List<NfsLoanContract> contracts = loanContractService.findList(loanContract);
    	String contractUrl = contracts.size()>0?Global.getConfig("domain")+contracts.get(0).getContractUrl():"";
		result.setContractUrl(contractUrl);
		
		return ResponseData.success("查询电子借条成功", result);
	}
	
	/**
	 * 	更多信息
	 */
	@RequestMapping(value = "/moreInfor")
	public @ResponseBody ResponseData moreInfor(HttpServletRequest request) {
		String param = request.getParameter("param");
		LoanMoreInforRequestParam reqData = JSONObject.parseObject(param, LoanMoreInforRequestParam.class);
		LoanMoreInforResponseResult result = new LoanMoreInforResponseResult();
		
		String recordId = reqData.getLoanId();
		Long loanId = Long.parseLong(recordId);
		//刷新问题 传来的可能是detailId
		NfsLoanRecord loanRecord = loanRecordService.findByApplyDetailId(loanId);
		
		if(loanRecord == null){
			loanRecord = loanRecordService.get(Long.valueOf(recordId));
		} 
		if(loanRecord == null){
			return ResponseData.error("借条状态异常!");
		}

		if(loanRecord.getAuctionStatus().equals(AuctionStatus.auctioned)){//已转让的借条
			NfsCrAuction nfsCrAuction = new NfsCrAuction();
			nfsCrAuction.setLoanRecord(loanRecord);
			nfsCrAuction.setStatus(NfsCrAuction.Status.successed);
			List<NfsCrAuction> crAuctionList = crAuctionService.findList(nfsCrAuction);
			if(crAuctionList != null && crAuctionList.size() > 0){
				Long crBuyerId = crAuctionList.get(0).getCrBuyer().getId();
				Member nowCreditor = memberService.get(crBuyerId);
				result.setNowCreditor(nowCreditor.getName());
			}
			result.setShowCreditor(1);
		}
		result.setLoanId(loanRecord.getId().toString());
		result.setTradeId(recordId);//交易号
		result.setName(loanRecord.getLoanee().getName());
		result.setAmount(com.jxf.svc.utils.StringUtils.decimalToStr(loanRecord.getAmount(),2));
		result.setInterest(com.jxf.svc.utils.StringUtils.decimalToStr(loanRecord.getInterest(),2));
		result.setAskTime(DateUtils.formatDate(loanRecord.getCreateTime()));
		result.setCompleteDate(DateUtils.formatDate(loanRecord.getDueRepayDate()));
		result.setRepayType(loanRecord.getRepayType().ordinal());
		
		result.setLoanPurp(loanRecord.getLoanPurp().getName());
		
		List<Object> loanFundDetails = new ArrayList<Object>();
		NfsLoanRepayRecord nfsLoanRepayRecord = new NfsLoanRepayRecord();
		nfsLoanRepayRecord.setLoan(loanRecord);
		List<NfsLoanRepayRecord> list = loanRepayRecordService.findList(nfsLoanRepayRecord);
		BigDecimal overdueInterest = BigDecimal.ZERO;
		RepayType repayType = loanRecord.getRepayType();
		if (repayType.equals(RepayType.principalAndInterestByMonth)) {//分期
			for (NfsLoanRepayRecord repayRecord : list) {
				    NfsLoanRepayRecord.Status status = repayRecord.getStatus();
					String expectRepayAmt = repayRecord.getExpectRepayAmt().toString();
					String expectRepayPrn = repayRecord.getExpectRepayPrn().toString();
					String expectRepayInt = repayRecord.getExpectRepayInt().toString();
					String expectRepayDate = DateUtils.formatDate(repayRecord.getExpectRepayDate());
					Integer periodsSeq = repayRecord.getPeriodsSeq();
					if(status == NfsLoanRepayRecord.Status.done) {
						loanFundDetails.add(new LoanPeriodizationDetail("已还",expectRepayAmt, expectRepayPrn, expectRepayInt,
								expectRepayDate, "第"+periodsSeq.toString()+"期"));
					}else if(status == NfsLoanRepayRecord.Status.pending) {
						loanFundDetails.add(new LoanPeriodizationDetail("待还",expectRepayAmt, expectRepayPrn, expectRepayInt,
								expectRepayDate, "第"+periodsSeq.toString()+"期"));
					}else {
						BigDecimal maxIntRate = new BigDecimal(24);
						MathContext mc = new MathContext(10);
						BigDecimal onehundred = new BigDecimal(100);
						BigDecimal yearDays = new BigDecimal(360);
						
						loanFundDetails.add(new LoanPeriodizationDetail("逾期",expectRepayAmt, expectRepayPrn, expectRepayInt,
								expectRepayDate, "第"+periodsSeq.toString()+"期"));
						Date repayDate = repayRecord.getExpectRepayDate();
						int overdueDays = CalendarUtil.getIntervalDays(new Date(),repayDate);//过去的天数
						BigDecimal overdueDaysBig = new BigDecimal(overdueDays);
						//（分期某期应还本金 + 分期某期应还利息） * 24% / 360 * 逾期天数
						BigDecimal currentOverdueInt = repayRecord.getExpectRepayAmt().multiply(maxIntRate,mc)
								.divide(onehundred,mc).divide(yearDays,mc).multiply(overdueDaysBig,mc);
						overdueInterest = overdueInterest.add(currentOverdueInt);
					}
			}
			
			if(overdueInterest.compareTo(BigDecimal.ZERO) > 0){//有逾期利息
				loanFundDetails.add(new LoanPeriodizationDetail("待还",overdueInterest.setScale(2, BigDecimal.ROUND_HALF_UP).toString(), 
						"", "", DateUtils.formatDate(new Date()), "逾期利息"));
			}
		} else {
			loanFundDetails.add(new LoanFundDetail("借条本金", "+" + com.jxf.svc.utils.StringUtils.decimalToStr(loanRecord.getAmount(),2) + "元", 
					DateUtils.formatDate(loanRecord.getDueRepayDate()), 0));
			loanFundDetails.add(new LoanFundDetail("利息","+" + com.jxf.svc.utils.StringUtils.decimalToStr(loanRecord.getInterest(),2) + "元", 
					DateUtils.formatDate(loanRecord.getDueRepayDate()), 0));
			
			if(loanRecord.getStatus().equals(NfsLoanRecord.Status.overdue)){
				BigDecimal overdueAmount = loanRecord.getDueRepayAmount();//应还金额就是逾期本金
				Date repayDate = loanRecord.getDueRepayDate();
				int overdueDays = CalendarUtil.getIntervalDays(new Date(),repayDate);
				BigDecimal overdueDaysBig = new BigDecimal(overdueDays);
				overdueInterest = overdueInterest.add(LoanUtils.calOverdueInterest(overdueAmount,overdueDaysBig));
			}
			
			if(overdueInterest.compareTo(BigDecimal.ZERO) > 0){//有逾期利息
				loanFundDetails.add(new LoanFundDetail("逾期利息","+" + overdueInterest.setScale(2, BigDecimal.ROUND_HALF_UP) + "元", 
						DateUtils.formatDate(new Date()), 0));
			}
		}
		result.setLoanFundDetails(loanFundDetails); // 资金详情
		
		//借条历史记录
		List<HistoryRecord> historyRecords = new ArrayList<HistoryRecord>();
		
		//全额还款/生成借条/延期/部分还款纪录
		NfsLoanOperatingRecord operatingRecord = new NfsLoanOperatingRecord();
		operatingRecord.setOldRecord(loanRecord);
		List<NfsLoanOperatingRecord> operatingRecordList = loanOperatingRecordService.findList(operatingRecord);//按updateTime倒序
		if(operatingRecordList != null && operatingRecordList.size() > 0){
			int size = operatingRecordList.size();
			if(size > 3){
				result.setShowMore(1);
			}
			for (int i = 0; i < size; i++) {
				if(i == 3){
					break;
				}
				NfsLoanOperatingRecord loanOperatingRecord = operatingRecordList.get(i);
				Date updateTime = loanOperatingRecord.getUpdateTime();//最后更新日期
				
				HistoryRecord historyRecord = new LoanMoreInforResponseResult().new HistoryRecord();
				historyRecord.setName(loanOperatingRecord.getType().getName());
				historyRecord.setTime(DateUtils.formatDate(updateTime, "yyyy-MM-dd"));
				historyRecord.setHistoryId(loanOperatingRecord.getId().toString());
				historyRecords.add(historyRecord);
			}
			result.setHistoryRecords(historyRecords);
		}
		
		return ResponseData.success("成功查询更多信息", result);
	}
}