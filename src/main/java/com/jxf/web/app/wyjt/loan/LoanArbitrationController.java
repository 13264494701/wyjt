package com.jxf.web.app.wyjt.loan;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.jxf.loan.constant.LoanConstant;
import com.jxf.loan.constant.RecordMessage;
import com.jxf.loan.entity.NfsLoanApply.TrxType;
import com.jxf.loan.entity.NfsLoanArbitration;
import com.jxf.loan.entity.NfsLoanArbitration.StrongStatus;
import com.jxf.loan.entity.NfsLoanArbitrationDetail;
import com.jxf.loan.entity.NfsLoanContract;
import com.jxf.loan.entity.NfsLoanDetailMessage;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRecord.ArbitrationStatus;
import com.jxf.loan.entity.NfsLoanRecord.AuctionStatus;
import com.jxf.loan.entity.NfsLoanRecord.Status;
import com.jxf.loan.service.NfsLoanArbitrationDetailService;
import com.jxf.loan.service.NfsLoanArbitrationService;
import com.jxf.loan.service.NfsLoanContractService;
import com.jxf.loan.service.NfsLoanDetailMessageService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.utils.LoanUtils;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberAct;
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.mem.entity.MemberMessage.Type;
import com.jxf.mem.service.MemberActService;
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.H5Utils;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.nfs.constant.ActSubConstant;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.arbitration.ArbitrationDetailRequestParam;
import com.jxf.web.model.wyjt.app.arbitration.ArbitrationListRequestParam;
import com.jxf.web.model.wyjt.app.arbitration.ArbitrationListResponseResult;
import com.jxf.web.model.wyjt.app.arbitration.ArbitrationListResponseResult.Arbitration;
import com.jxf.web.model.wyjt.app.loan.ApplyArbitrationRequestParam;

/**
 * 仲裁
 * @author liuhuaixin
 *
 */
@Controller("wyjtAppLoanArbitrationController")
@RequestMapping(value="${wyjtApp}/arbitration")
public class LoanArbitrationController {
	private static final Logger logger = LoggerFactory.getLogger(LoanArbitrationController.class);

	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberActService memberActService;
	@Autowired
	private MemberActTrxService memberActTrxService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsLoanContractService loanContractService;
	@Autowired
	private NfsLoanArbitrationService loanArbitrationService;
	@Autowired
	private NfsLoanArbitrationDetailService loanArbitrationDetailService;
	@Autowired
	private NfsLoanDetailMessageService loanDetailMessageService;
	@Autowired
	private SendSmsMsgService sendSmsMsgService;
	@Autowired
	private MemberMessageService memberMessageService;
	
	
	@RequestMapping(value ="/list")
	@ResponseBody
	public ResponseData list(HttpServletRequest request) {
		try {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		ArbitrationListRequestParam reqData = JSONObject.parseObject(param,ArbitrationListRequestParam.class);
		Integer pageNo = reqData.getPageNo();
		Integer pageSize = reqData.getPageSize();
		String type =reqData.getType();
		int trxType = 0;
		if(type.equals("1")) {
			NfsLoanRecord loanRecord = new NfsLoanRecord();
			loanRecord.setLoaner(member);
			loanRecord.setTrxType(TrxType.online);
			Page<NfsLoanRecord> page = loanRecordService.findApplyForArbitration(loanRecord,pageNo, pageSize);
			ArbitrationListResponseResult result = new ArbitrationListResponseResult();
			ArrayList<Arbitration> loanList = new ArrayList<Arbitration>();
			if(page.getList().size()>0) {
			for(NfsLoanRecord everyLoanRecord:page.getList()) {
				if(everyLoanRecord.getStatus().equals(Status.repayed)) {
					continue;
				}
				Arbitration arbitration = new Arbitration();
				//获取借条金额
				//将借条金额存到response
				arbitration.setAmount(StringUtils.decimalToStr(everyLoanRecord.getAmount(),2));
				//讲借条利息存到response
				arbitration.setInterest(StringUtils.decimalToStr(everyLoanRecord.getInterest(),2));
				//将借条id存到response
				arbitration.setLoanId(everyLoanRecord.getId().toString());
				Member partner = null;
				partner = everyLoanRecord.getLoanee();
				//将借款人头像存到response
				arbitration.setPartnerHeadImage(partner.getHeadImage());
				//将借款人姓名存到response
				arbitration.setPartnerName(partner.getName());
				arbitration.setType(LoanConstant.TYPE_RECORD);
				Date date = new Date();
				arbitration.setDay(DateUtils.getDistanceOfTwoDate(date, everyLoanRecord.getDueRepayDate()));
				arbitration.setProgress(everyLoanRecord.getProgress());
				arbitration.setRepayDate(DateUtils.formatDate(everyLoanRecord.getDueRepayDate(), "yyyy-MM-dd"));
				arbitration.setRepayType(everyLoanRecord.getRepayType().ordinal());
				// 判断状态
				String loanStatus = "";
				if (arbitration.getDay() == 1) {
					loanStatus = "今日逾期;FFAE38";
				} else if (arbitration.getDay() > 1) {
					loanStatus = "已逾期;757575|" + (arbitration.getDay()) + ";ED2E24|日;757575";
				} else {
					loanStatus = "逾期已超过;757575|15;ED2E24|天;757575";
				}
				String loanMode = "0"; // 今日角标 1：显示；0：不显示
				String overduePayment = "0"; // 逾期图标 1：显示；0：不显示
					if (arbitration.getDay() == 0 || arbitration.getDay() == 1) {
						loanMode = "1";
					} else if (arbitration.getDay() > 0) {
						overduePayment = "1";
					}
				arbitration.setLoanStatus(loanStatus);
				arbitration.setLoanMode(loanMode);
				arbitration.setOverduePayment(overduePayment);
				loanList.add(arbitration);
			}
			}
			result.setArbitrationList(loanList);;
			return ResponseData.success("成功查询全部可申请仲裁借条",result);
		}else if(type.equals("2")) {
			NfsLoanArbitration arbitration = new NfsLoanArbitration();
			Long loanerId = member.getId();
			Page<NfsLoanArbitration> page = loanArbitrationService.findInArbitration(loanerId,arbitration,pageNo,pageSize,trxType);
			ArbitrationListResponseResult result = new ArbitrationListResponseResult();
			ArrayList<Arbitration> loanList = new ArrayList<Arbitration>();
			if(page.getList().size()>0) {
			for(NfsLoanArbitration everyLoanArbitration:page.getList()) {
				NfsLoanRecord everyLoanRecord = loanRecordService.get(everyLoanArbitration.getLoan().getId());
				if(everyLoanRecord.getStatus().equals(Status.repayed)) {
					continue;
				}
				Arbitration arbitrationList = new Arbitration();
				arbitrationList.setAmount(StringUtils.decimalToStr(everyLoanRecord.getAmount(),2));
				arbitrationList.setInterest(StringUtils.decimalToStr(everyLoanArbitration.getLoan().getInterest(),2));
				arbitrationList.setLoanId(everyLoanArbitration.getLoan().getId().toString());
				arbitrationList.setArbitrationId(everyLoanArbitration.getId().toString());
				Member partner = null;
				partner = everyLoanArbitration.getLoan().getLoanee();
				arbitrationList.setPartnerHeadImage(partner.getHeadImage());
				arbitrationList.setPartnerName(partner.getName());
				arbitrationList.setType(LoanConstant.TYPE_RECORD);
				Date date2 = new Date();
				arbitrationList.setDay(DateUtils.getDistanceOfTwoDate(date2, everyLoanRecord.getDueRepayDate()));
				arbitrationList.setProgress(everyLoanArbitration.getLoan().getProgress());
				arbitrationList.setRepayDate(DateUtils.formatDate(everyLoanArbitration.getLoan().getDueRepayDate(), "yyyy-MM-dd"));
				arbitrationList.setRepayType(everyLoanArbitration.getLoan().getRepayType().ordinal());
				arbitrationList.setCreateDate(DateUtils.formatDate(everyLoanArbitration.getCreateTime(),"yyyy-MM-dd"));
				arbitrationList.setArbitrationStstus("仲裁中;000000");
				arbitrationList.setSpeedStatus(1);
				loanList.add(arbitrationList);
			}
			}
			result.setArbitrationList(loanList);
			return ResponseData.success("成功查询全部仲裁中借条",result);
		}else if(type.equals("3")){
			NfsLoanArbitration arbitration = new NfsLoanArbitration();
			Long loanerId = member.getId();
			Page<NfsLoanArbitration> page = loanArbitrationService.findArbitration(loanerId,arbitration,pageNo,pageSize,trxType);
			ArbitrationListResponseResult result = new ArbitrationListResponseResult();
			ArrayList<Arbitration> loanList = new ArrayList<Arbitration>();
			if(page.getList().size()>0) {
			for(NfsLoanArbitration everyLoanArbitration:page.getList()) {
				NfsLoanRecord everyLoanRecord = loanRecordService.get(everyLoanArbitration.getLoan().getId());
				if(everyLoanRecord.getStatus().equals(Status.repayed)) {
					continue;
				}
				Arbitration arbitrationList = new Arbitration();
				arbitrationList.setAmount(StringUtils.decimalToStr(everyLoanRecord.getAmount(),2));
				arbitrationList.setInterest(StringUtils.decimalToStr(everyLoanArbitration.getLoan().getInterest(),2));
				arbitrationList.setLoanId(everyLoanArbitration.getLoan().getId().toString());
				arbitrationList.setArbitrationId(everyLoanArbitration.getId().toString());
				Member partner = null;
				partner = everyLoanArbitration.getLoan().getLoanee();
				arbitrationList.setPartnerHeadImage(partner.getHeadImage());
				arbitrationList.setPartnerName(partner.getName());
				arbitrationList.setType(LoanConstant.TYPE_RECORD);
				Date date2 = new Date();
				arbitrationList.setDay(DateUtils.getDistanceOfTwoDate(date2, everyLoanRecord.getDueRepayDate()));
				arbitrationList.setProgress(everyLoanArbitration.getLoan().getProgress());
				arbitrationList.setRepayDate(DateUtils.formatDate(everyLoanArbitration.getLoan().getDueRepayDate(), "yyyy-MM-dd"));
				arbitrationList.setRepayType(everyLoanArbitration.getLoan().getRepayType().ordinal());
				arbitrationList.setCreateDate(DateUtils.formatDate(everyLoanArbitration.getCreateTime(),"yyyy-MM-dd"));
				int day = DateUtils.getDistanceOfTwoDate(DateUtils.addCalendarByDate(everyLoanArbitration.getRuleTime(),720),new Date());
				arbitrationList.setSurplusDay(day);
				arbitrationList.setRuleTime(DateUtils.formatDate(everyLoanArbitration.getRuleTime(), "yyyy-MM-dd"));
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
			    arbitrationList.setOverMoney(StringUtils.decimalToStr(totalMoney, 2));
				arbitrationList.setArbitrationStstus("仲裁出裁决;000000");
				arbitrationList.setSpeedStatus(1);
				StrongStatus strongStatus = everyLoanArbitration.getStrongStatus();
				/** 已申请 appliedStrong,未申请 notApplyStrong*/
				if(strongStatus.equals(NfsLoanArbitration.StrongStatus.appliedStrong)) {
					arbitrationList.setApplyType("0");
				}else {
					arbitrationList.setApplyType("1");
				}
				loanList.add(arbitrationList);
			}
			}
			result.setArbitrationList(loanList);
			return ResponseData.success("成功查询全部仲裁出裁决借条",result);
		}else if(type.equals("4")){
			NfsLoanArbitration arbitration = new NfsLoanArbitration();
			Long loanerId = member.getId();
			Page<NfsLoanArbitration> page = loanArbitrationService.findEndOfArbitration(loanerId,arbitration,pageNo,pageSize,trxType);
			ArbitrationListResponseResult result = new ArbitrationListResponseResult();
			ArrayList<Arbitration> loanList = new ArrayList<Arbitration>();
			if(page.getList().size()>0) {
			for(NfsLoanArbitration everyLoanArbitration:page.getList()) {
				Arbitration arbitrationList = new Arbitration();
				NfsLoanRecord everyLoanRecord = loanRecordService.get(everyLoanArbitration.getLoan().getId());
				/*if(everyLoanRecord.getStatus().equals(Status.repayed)) {
					continue;
				}*/
				arbitrationList.setAmount(StringUtils.decimalToStr(everyLoanRecord.getAmount(),2));
				arbitrationList.setInterest(StringUtils.decimalToStr(everyLoanArbitration.getLoan().getInterest(),2));
				arbitrationList.setLoanId(everyLoanArbitration.getLoan().getId().toString());
				arbitrationList.setArbitrationId(everyLoanArbitration.getId().toString());
				Member partner = null;
				partner = everyLoanArbitration.getLoan().getLoanee();
				arbitrationList.setPartnerHeadImage(partner.getHeadImage());
				arbitrationList.setPartnerName(partner.getName());
				arbitrationList.setType(LoanConstant.TYPE_RECORD);
				Date date2 = new Date();
				arbitrationList.setDay(DateUtils.getDistanceOfTwoDate(date2, everyLoanRecord.getDueRepayDate()));
				arbitrationList.setProgress(everyLoanArbitration.getLoan().getProgress());
				arbitrationList.setRepayDate(DateUtils.formatDate(everyLoanArbitration.getLoan().getDueRepayDate(), "yyyy-MM-dd"));
				arbitrationList.setRepayType(everyLoanArbitration.getLoan().getRepayType().ordinal());
				arbitrationList.setCreateDate(DateUtils.formatDate(everyLoanArbitration.getCreateTime(),"yyyy-MM-dd"));
				arbitrationList.setArbitrationStstus("仲裁已完成;000000");
				arbitrationList.setSpeedStatus(2);
				loanList.add(arbitrationList);
			}
			}
			result.setArbitrationList(loanList);
			return ResponseData.success("成功查询全部仲裁已完成借条",result);
		}
		}catch(Exception e){
			e.printStackTrace();
			return ResponseData.error("查询失败");
		}
		return ResponseData.error("查询失败1");
	}
	
	/**
	 * 	申请仲裁页面跳转
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "apply")
	public ModelAndView arbitrationPay(HttpServletRequest request,Model model) {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		ApplyArbitrationRequestParam reqData = JSONObject.parseObject(param,ApplyArbitrationRequestParam.class);
		ModelAndView mv = new ModelAndView("app/applyCollectionResult");
		ModelAndView mv2 = new ModelAndView("app/arbitration/arbitrationPay");
		mv = H5Utils.addPlatform(member, mv);
		mv2 = H5Utils.addPlatform(member, mv2);
		Long loanId = Long.parseLong(reqData.getLoanId());
		NfsLoanContract loanContract = loanContractService.getCurrentContractByLoanId(loanId);
		if(loanContract.getId()<351359313404628992L){
			mv.addObject("success", false);
			mv.addObject("errStr", "业务升级中");
			return mv;
 		}
			
		NfsLoanRecord loanRecord = loanRecordService.get(loanId);
		boolean ageBoolean = false;
		String idNo = loanRecord.getLoanee().getIdNo();
		//获取年份
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
		//获取月份
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MMdd");
		String createTime = simpleDateFormat.format(loanRecord.getCreateTime());
		String createMMdd = simpleDateFormat1.format(loanRecord.getCreateTime());
		//截取借款人出生日期
		String loaneeAge = idNo.substring(6, 10);
		String loaneeMMdd = idNo.substring(10, 14);
		int age = Integer.parseInt(createTime) - Integer.parseInt(loaneeAge);
		if(age>23) {
			ageBoolean = true;
		}else if(age == 23) {
			if (Integer.parseInt(createMMdd) > Integer.parseInt(loaneeMMdd)) {
				ageBoolean = true;
			}
		}else {
			ageBoolean = false;
		}
		
		if(loanRecord.getArbitrationStatus().equals(NfsLoanRecord.ArbitrationStatus.doing)) {
			mv.addObject("success", false);
			mv.addObject("errStr", "此借款单已申请过仲裁，无法再次申请。");
			return mv;
		}
		if(loanRecord.getCollectionStatus().equals(NfsLoanRecord.CollectionStatus.doing)) {
			mv.addObject("success", false);
			mv.addObject("errStr", "该借条正在催收中");
			return mv;
		}
		BigDecimal decTotalMoney = BigDecimal.ZERO;
		decTotalMoney = loanRecord.getDueRepayAmount();
		if(decTotalMoney.compareTo(new BigDecimal(100)) < 0){
			mv.addObject("success", false);
			mv.addObject("errStr", "争议金额低于100元无法申请该服务");
			return mv;
 		}
		String loaneeName = reqData.getLoaneeName();
		String overDueDuration = reqData.getOverDueDuration();
		String accountBalance = reqData.getAccountBalance();
	    // 计算逾期利息
    	BigDecimal overMoney= BigDecimal.ZERO;
    	int days=DateUtils.getDistanceOfTwoDate(new Date(),loanRecord.getDueRepayDate());
    	BigDecimal overdueDaysBig = new BigDecimal(days);
    	//计算利息 默认给24
        overMoney=LoanUtils.calOverdueInterest(loanRecord.getDueRepayAmount(),overdueDaysBig);
	    //总金额
	    BigDecimal totalMoney=decTotalMoney.add(overMoney);
	    /**
         * 仲裁费 2018/10/20日后使用
         * @param totalMoney
         * @return
         * <=1000  140
         * 1k-5w  40+超过1000部分的4%+100
         * 5w-10w 2000+超过5w部分的3%+100
         * 10w-20w 3500+超过10w部分的2%+100
         * 20w-50w 5500+超过10w部分的1%+100
         * 500001 - 100W 8500 + 超过50W元部分的0.5%
   		 * 1000001以上 11000 + 超过100W元部分的0.25%
         */
	    double totalMoneyD = totalMoney.doubleValue();
        BigDecimal slmoney = new BigDecimal(0.0);//仲裁受理费
        BigDecimal LawyerMoney = new BigDecimal(0.0);//律师代理执行费
        if(totalMoneyD<=1000){
        	slmoney = new BigDecimal(140);
        }
        if(totalMoneyD>1000  && totalMoneyD <= 50000){
        	LawyerMoney = new BigDecimal(100);
        	slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(1000));
        	slmoney = slmoney.multiply(new BigDecimal(0.04));
        	slmoney = slmoney.add(LawyerMoney).add(new BigDecimal(40)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        if(totalMoneyD>50000  && totalMoneyD <= 100000){
        	LawyerMoney = new BigDecimal(100);
        	slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(50000));
        	slmoney = slmoney.multiply(new BigDecimal(0.03));
        	slmoney = slmoney.add(LawyerMoney).add(new BigDecimal(2000)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        if(totalMoneyD>100000  &&totalMoneyD <= 200000){
        	LawyerMoney = new BigDecimal(100);
        	slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(100000));
        	slmoney = slmoney.multiply(new BigDecimal(0.02));
        	slmoney = slmoney.add(LawyerMoney).add(new BigDecimal(3500)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        if(totalMoneyD>200000  &&totalMoneyD <= 500000){
        	LawyerMoney = new BigDecimal(100);
        	slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(200000));
        	slmoney = slmoney.multiply(new BigDecimal(0.01));
        	slmoney = slmoney.add(LawyerMoney).add(new BigDecimal(5500)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        if(totalMoneyD>500000  &&totalMoneyD <= 1000000){
        	LawyerMoney = new BigDecimal(100);
        	slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(500000));
        	slmoney = slmoney.multiply(new BigDecimal(0.005));
        	slmoney = slmoney.add(LawyerMoney).add(new BigDecimal(8500)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        if(totalMoneyD>1000000){
        	LawyerMoney = new BigDecimal(100);
        	slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(1000000));
        	slmoney = slmoney.multiply(new BigDecimal(0.0025));
        	slmoney = slmoney.add(LawyerMoney).add(new BigDecimal(11000)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
	   /* //计算仲裁费用
	    double totalMoneyD = new BigDecimal(totalMoney).doubleValue();
        BigDecimal slmoney = new BigDecimal(0.0);//仲裁受理费
        BigDecimal LawyerMoney = new BigDecimal(0.0);//律师代理执行费
        if(totalMoneyD>0 && totalMoneyD <= 5000){
        	slmoney = new BigDecimal(totalMoneyD);
        	slmoney = slmoney.multiply(new BigDecimal(0.03));
        	slmoney.add(LawyerMoney).add(new BigDecimal(40)).setScale(0, BigDecimal.ROUND_HALF_UP);
        }
        if(totalMoneyD>5000  && totalMoneyD <= 10000){
        	slmoney = new BigDecimal(totalMoneyD);
        	slmoney = slmoney.multiply(new BigDecimal(0.025));
        	slmoney.add(LawyerMoney).add(new BigDecimal(200)).setScale(0, BigDecimal.ROUND_HALF_UP);
        }
        if(totalMoneyD>10000  &&totalMoneyD <= 30000){
        	slmoney = new BigDecimal(totalMoneyD);
        	slmoney = slmoney.multiply(new BigDecimal(0.02));
        	slmoney.add(LawyerMoney).add(new BigDecimal(400)).setScale(0, BigDecimal.ROUND_HALF_UP);
        }
        if(totalMoneyD>30000  &&totalMoneyD <= 50000){
        	slmoney = new BigDecimal(totalMoneyD);
        	slmoney = slmoney.multiply(new BigDecimal(0.018));
        	slmoney.add(LawyerMoney).add(new BigDecimal(600)).setScale(0, BigDecimal.ROUND_HALF_UP);
        }
        if(totalMoneyD>50000  &&totalMoneyD <=100000){
        	slmoney = new BigDecimal(totalMoneyD);
        	slmoney = slmoney.multiply(new BigDecimal(0.015));
        	slmoney.add(LawyerMoney).add(new BigDecimal(1500)).setScale(0, BigDecimal.ROUND_HALF_UP);
        }
        if(totalMoneyD>100000  &&totalMoneyD <=200000){
        	slmoney = new BigDecimal(totalMoneyD);
        	slmoney = slmoney.multiply(new BigDecimal(0.013));
        	slmoney.add(LawyerMoney).add(new BigDecimal(2000)).setScale(0, BigDecimal.ROUND_HALF_UP);
        }
        if(totalMoneyD>200000  &&totalMoneyD <=500000){
        	slmoney = new BigDecimal(totalMoneyD);
        	slmoney = slmoney.multiply(new BigDecimal(0.01));
        	slmoney.add(LawyerMoney).add(new BigDecimal(4000)).setScale(0, BigDecimal.ROUND_HALF_UP);
        }
        if(totalMoneyD>500000  &&totalMoneyD <=1000000){
        	BigDecimal shengMoney=new BigDecimal(totalMoneyD).multiply(new BigDecimal(0.008));
        	slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(500000));
        	slmoney = slmoney.multiply(new BigDecimal(0.003));
        	slmoney.add(LawyerMoney).add(shengMoney).add(new BigDecimal(4000)).setScale(0, BigDecimal.ROUND_HALF_UP);
        }
        if(totalMoneyD>1000000){
        	BigDecimal shengMoney=new BigDecimal(totalMoneyD).multiply(new BigDecimal(0.005));
        	slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(1000000));
        	slmoney = slmoney.multiply(new BigDecimal(0.002));
        	slmoney.add(LawyerMoney).add(shengMoney).add(new BigDecimal(10750)).setScale(0, BigDecimal.ROUND_HALF_UP);
        }*/
        slmoney = slmoney.setScale(2,BigDecimal.ROUND_HALF_UP);
        String memberToken = request.getHeader("x-memberToken");
        model.addAttribute("memberToken",memberToken);
        model.addAttribute("loan", loanRecord);
        //借款人姓名
		model.addAttribute("loaneeName",loaneeName);
		//逾期时长
		model.addAttribute("overDueDuration",overDueDuration);
		//账户余额
		model.addAttribute("accountBalance",accountBalance);
		//逾期金额
		model.addAttribute("amount",totalMoney);
		//仲裁费用
		model.addAttribute("slmoney",slmoney);
		//判断年龄是否大于23
		model.addAttribute("ageBoolean",ageBoolean);
		return mv2;
	}
	@RequestMapping(value="payForArbPay")
	public ModelAndView payForArbPay(HttpServletRequest request) {
		String loanId = request.getParameter("loanId");
		String slmoneyStr = request.getParameter("slmoney");
		String overdueAmountStr = request.getParameter("overdueAmount"); 
		ModelAndView mv = new ModelAndView("app/arbitration/payArbiResult");
		Member member = memberService.getCurrent();
		mv.addObject("loanId", loanId);
		if(member == null ) {
			mv.addObject("code", -1);
			mv.addObject("message", "登录状态已过期，请重新登录");
			return mv;
		}
		mv = H5Utils.addPlatform(member, mv);
		BigDecimal amount = StringUtils.toDecimal(overdueAmountStr);
		BigDecimal slmoney = StringUtils.toDecimal(slmoneyStr);
		BigDecimal avlBal = memberActService.getAvlBal(member); 
		if(avlBal.compareTo(slmoney) < 0) {
			mv.addObject("code", -1);
			mv.addObject("message", "账户余额不足，请充值后再操作!");
			return mv;
		}
		
		NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(loanId));
		if(loanRecord.getArbitrationStatus().equals(ArbitrationStatus.doing)) {
			mv.addObject("code", -1);
			mv.addObject("message", "您已申请过仲裁，请勿重复申请！");
			return mv;
		}
		if(!loanRecord.getAuctionStatus().equals(AuctionStatus.initial)) {
			mv.addObject("code", -1);
			mv.addObject("message", "转让中/转让成功的借条暂不支持申请仲裁!");
			return mv;
		}
		
		NfsLoanArbitration arbitration = new NfsLoanArbitration();
		arbitration.setApplyAmount(amount);
		arbitration.setFee(slmoneyStr);
		arbitration.setLoan(loanRecord);
		arbitration.setMember(member);
		arbitration.setStatus(NfsLoanArbitration.Status.application);
		arbitration.setChannel(NfsLoanArbitration.Channel.zhangsan);
		arbitration.setStrongStatus(NfsLoanArbitration.StrongStatus.notApplyStrong);
		
		try {
			int code = loanArbitrationService.payForApplyArbitration(member, arbitration);
			if(code == 0) {
				//借款人
				String loaneePhoneNo = loanRecord.getLoanee().getUsername();
				
				sendSmsMsgService.sendMessage("applicationArbitrationSmsLoanee", loaneePhoneNo, null);
				//放款人
				String loanerPhoneNo = loanRecord.getLoaner().getUsername();
				sendSmsMsgService.sendMessage("applicationArbitrationSmsLoaner", loanerPhoneNo, null);
				
				//站内信
				memberMessageService.sendMessage(Type.applicationArbitrationImsLoanee,loanRecord.getId());
				//生成对话
				NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
				nfsLoanDetailMessage.setDetail(loanRecord.getLoanApplyDetail());
				nfsLoanDetailMessage.setMember(member);
				nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_5005);
				nfsLoanDetailMessage.setType(RecordMessage.SEND_REMIND);
				loanDetailMessageService.save(nfsLoanDetailMessage);
				
				mv.addObject("memberToken", request.getHeader("x-memberToken"));
				mv.addObject("arbRecordId", arbitration.getId());
				mv.addObject("code", 0);
				mv.addObject("message", "仲裁申请预缴费成功！");
			}
			return mv;
		} catch (Exception e) {
			logger.error("会员{}申请仲裁缴费账户更新异常，异常信息：{}",member.getId(),Exceptions.getStackTraceAsString(e));
			mv.addObject("code", -1);
			mv.addObject("message", "账户异常，操作失败！");
			return mv;
		}
	}
	
	/**
	 * 查看进度
	 */
	@RequestMapping(value="goArbSchedule")
	public ModelAndView goArbSchedule(HttpServletRequest request) {
		String arbRecordId = request.getParameter("arbRecordId");
		ModelAndView mv = new ModelAndView("app/arbitration/lawsuitSchedule");
		ModelAndView errMv = new ModelAndView("app/loanPay/payResult");
		Member member = memberService.getCurrent();
		if(member == null ) {
			errMv.addObject("code", -1);
			errMv.addObject("message", "登录状态已过期，请重新登录！");
			return errMv;
		}
		mv = H5Utils.addPlatform(member, mv);
		NfsLoanArbitration arbitration  = loanArbitrationService.get(Long.valueOf(arbRecordId));
		NfsLoanArbitrationDetail temp = new NfsLoanArbitrationDetail();
		temp.setArbitrationId(Long.valueOf(arbRecordId));
		List<NfsLoanArbitrationDetail> arbitrationDetails = loanArbitrationDetailService.findList(temp);
		mv.addObject("detail", arbitrationDetails);
		for (NfsLoanArbitrationDetail nfsLoanArbitrationDetail : arbitrationDetails) {
			if(!NfsLoanArbitrationDetail.Status.refundHasArrived.equals(nfsLoanArbitrationDetail.getStatus())) {
				continue;
			}
			MemberAct param = new MemberAct();
			param.setMember(member);
			param.setSubNo(ActSubConstant.MEMBER_AVL_BAL);
			MemberActTrx trxParam = new MemberActTrx();
			trxParam.setOrgId(arbitration.getId());
			trxParam.setTrxCode(TrxRuleConstant.ARBITRATION_PREPAY);
			trxParam.setDrc("C");
			trxParam.setMember(member);
			List<MemberActTrx> actTrxs = memberActTrxService.findList(trxParam);
			MemberActTrx memberActTrx = actTrxs == null ? new MemberActTrx(): actTrxs.get(0);
			mv.addObject("actTrxId", memberActTrx.getId() == null ? "":memberActTrx.getId());
		}
		return mv;
	}
	
	/**
	 * 查看进度app
	 */
	@RequestMapping(value="goArbSchedules")
	public ModelAndView goArbSchedules(HttpServletRequest request) {
		String params = request.getParameter("param");
		ModelAndView mv = new ModelAndView("app/arbitration/lawsuitSchedule");
		ModelAndView errMv = new ModelAndView("app/loanPay/payResult");
		ArbitrationDetailRequestParam reqData = JSONObject.parseObject(params,ArbitrationDetailRequestParam.class);
		String arbitrationId = reqData.getArbitrationId();
		Member member = memberService.getCurrent();
		if(member == null ) {
			errMv.addObject("code", -1);
			errMv.addObject("message", "登录状态已过期，请重新登录！");
			return errMv;
		}
		mv = H5Utils.addPlatform(member, mv);
		NfsLoanArbitration arbitration  = loanArbitrationService.get(Long.valueOf(arbitrationId));
		NfsLoanArbitrationDetail temp = new NfsLoanArbitrationDetail();
		temp.setArbitrationId(Long.valueOf(arbitrationId));
		List<NfsLoanArbitrationDetail> arbitrationDetails = loanArbitrationDetailService.findList(temp);
		mv.addObject("detail", arbitrationDetails);
		for (NfsLoanArbitrationDetail nfsLoanArbitrationDetail : arbitrationDetails) {
			if(!NfsLoanArbitrationDetail.Status.refundHasArrived.equals(nfsLoanArbitrationDetail.getStatus())) {
				continue;
			}
			MemberAct param = new MemberAct();
			param.setMember(member);
			param.setSubNo(ActSubConstant.MEMBER_AVL_BAL);
			MemberActTrx actTrx = new MemberActTrx();
			actTrx.setOrgId(arbitration.getId());
			actTrx.setTrxCode(TrxRuleConstant.ARBITRATION_PREPAY);
			actTrx.setDrc("C");
			actTrx.setMember(member);
			List<MemberActTrx> actTrxs = memberActTrxService.findList(actTrx);
			MemberActTrx memberActTrx = actTrxs == null ? new MemberActTrx(): actTrxs.get(0);
			mv.addObject("actTrxId", memberActTrx.getId() == null ? "":memberActTrx.getId());
		}
		return mv;
	}
	
}
