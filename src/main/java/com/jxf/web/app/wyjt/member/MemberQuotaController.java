package com.jxf.web.app.wyjt.member;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.signature.youdun.YouDunCloudPhase;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.rc.entity.RcConstant;
import com.jxf.rc.entity.RcQuota;
import com.jxf.rc.service.RcQuotaService;
import com.jxf.svc.config.Global;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.SnowFlake;
import com.jxf.svc.utils.StringUtils;
import com.jxf.ufang.entity.UfangLoanMarket;
import com.jxf.ufang.service.UfangLoanMarketService;
import com.jxf.web.app.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.member.MemberQuotaResponseResult;
import com.jxf.web.model.wyjt.app.member.MemberQuotaResponseResult.LoanMarket;


/**
 * 
 * @类功能说明： 额度
 * @作者：SuHuimin 
 * @创建时间：2016年12月21日 下午4:07:01 
 * @版本：V1.0
 */
@Controller("wyjtAppMemberQuotaController")
@RequestMapping(value="${wyjtApp}/member")
public class MemberQuotaController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(MemberQuotaController.class);
	@Autowired
	private MemberService memberService;
	@Autowired
	private RcQuotaService rcQuotaService;
	@Autowired
	private UfangLoanMarketService loanMarketService;
	
	/**
	 * 跳转进入额度评估页面
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value = "/goToQuotaPage", method = RequestMethod.POST)
	public ResponseData goToQuotaPage(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		Integer verifiedList = member.getVerifiedList();
		if(VerifiedUtils.isVerified(verifiedList, 1) && VerifiedUtils.isVerified(verifiedList, 2)) {
			MemberQuotaResponseResult result = new MemberQuotaResponseResult();
			RcQuota rcQuota = rcQuotaService.getByMemberId(member.getId());
			if(rcQuota != null) {
				result.setHasQuota(1);
				result.setQuota(rcQuota.getQuota().toString());
				result.setReminder(RcConstant.REMINDER_OF_QUOTA);
				result.setAssessmentTime(DateUtils.getDateStr(rcQuota.getUpdateTime(), "yyyy-MM-dd"));
				if(CalendarUtil.getIntervalDays(rcQuota.getUpdateTime(), new Date()) > RcConstant.QUOTA_CYCLE_TIME) {
					result.setCanAssessmentAgain(1);
				}else {
					result.setCanAssessmentAgain(0);
					result.setReminderOfForbidenAssessmetn(RcConstant.REMINDER_OF_FORBIDEN_ASSESMENT);
				}
				List<LoanMarket> recommendLoanMarketList = new ArrayList<LoanMarket>();
				UfangLoanMarket ufangLoanMarket1 = new UfangLoanMarket();
				ufangLoanMarket1.setIsMarketable(true);
				loanMarketService.findList(ufangLoanMarket1);
				List<UfangLoanMarket> loanMarkets = loanMarketService.findList(ufangLoanMarket1);
				for (UfangLoanMarket ufangLoanMarket : loanMarkets) {
					LoanMarket loanMarket = new MemberQuotaResponseResult().new LoanMarket();
					loanMarket.setIconUrl(Global.getConfig("domain") + ufangLoanMarket.getLogo());
					loanMarket.setLoanMarketId(ufangLoanMarket.getId().toString());
					loanMarket.setLoanMarketName(ufangLoanMarket.getName());
					loanMarket.setMinAmount(ufangLoanMarket.getMinLoanAmt());
					loanMarket.setMaxAmount(ufangLoanMarket.getMaxLoanAmt());
					loanMarket.setShowLoanCount(ufangLoanMarket.getDisplayLoanQuantity());
					recommendLoanMarketList.add(loanMarket);
				}
				result.setRecommendLoanMarketList(recommendLoanMarketList);
			}else {
				result.setHasQuota(0);
				result.setReminder0fBeforeAssessment(RcConstant.REMINDER_OF_BEFORE_ASSESMENT);
			}
			return ResponseData.success("请求成功",result);
		}else {
			return ResponseData.error("您还 没进行实名认证，认证后可立即进行额度评估");
		}
	}
	
	/**
	 *  s申请额度评估
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value = "/applyQuotaAssesment", method = RequestMethod.POST)
	public ResponseData applyQuotaAssesment(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		Integer verifiedList = member.getVerifiedList();
		if(VerifiedUtils.isVerified(verifiedList, 1) && VerifiedUtils.isVerified(verifiedList, 2)) {
			MemberQuotaResponseResult result = new MemberQuotaResponseResult();
			RcQuota rcQuota = rcQuotaService.getByMemberId(member.getId());
			//进行过额度评估就更新之前的
			if(rcQuota == null) {
				rcQuota = new RcQuota();
				rcQuota.setMember(member);
				rcQuotaService.save(rcQuota);
			}
			rcQuota.setOrderId(SnowFlake.getId());
			String response = YouDunCloudPhase.dataService(rcQuota);
			JSONObject data = JSONObject.parseObject(response);
			JSONObject header = data.getJSONObject("header");
			String successCode = "000000";
			if(!StringUtils.equals(successCode, header.getString("ret_code"))) {
				logger.error("会员：{}申请额度评估返回状态码{}",member.getId(),header.getString("ret_code"));
				return ResponseData.error("系统错误，请联系客服处理！");
			}
			JSONObject body = data.getJSONObject("body");
			//综合行为评分
			String comp_score = body.getString("comp_score");
			int quota = RcConstant.getQuotaByScore(Integer.valueOf(comp_score));
			rcQuota.setQuota(quota);
			rcQuota.setScore(Integer.valueOf(comp_score));
			rcQuota.setContent(body.toString());
			rcQuotaService.save(rcQuota);
			
			result.setHasQuota(1);
			result.setCanAssessmentAgain(0);
			result.setAssessmentTime(DateUtils.getDate("yyyy-MM-dd"));
			result.setQuota(String.valueOf(quota));
			result.setReminder(RcConstant.REMINDER_OF_QUOTA);
			result.setReminderOfForbidenAssessmetn(RcConstant.REMINDER_OF_FORBIDEN_ASSESMENT);
			UfangLoanMarket ufangLoanMarket1 = new UfangLoanMarket();
			ufangLoanMarket1.setIsMarketable(true);
			loanMarketService.findList(ufangLoanMarket1);
			List<UfangLoanMarket> loanMarkets = loanMarketService.findList(ufangLoanMarket1);
			List<LoanMarket> recommendLoanMarketList = new ArrayList<LoanMarket>();
			for (UfangLoanMarket ufangLoanMarket : loanMarkets) {
				LoanMarket loanMarket = new MemberQuotaResponseResult().new LoanMarket();
				loanMarket.setIconUrl(Global.getConfig("domain") + ufangLoanMarket.getLogo());
				loanMarket.setLoanMarketId(ufangLoanMarket.getId().toString());
				loanMarket.setLoanMarketName(ufangLoanMarket.getName());
				loanMarket.setMinAmount(ufangLoanMarket.getMinLoanAmt());
				loanMarket.setMaxAmount(ufangLoanMarket.getMaxLoanAmt());
				loanMarket.setShowLoanCount(ufangLoanMarket.getDisplayLoanQuantity());
				recommendLoanMarketList.add(loanMarket);
			}
			result.setRecommendLoanMarketList(recommendLoanMarketList);
			return ResponseData.success("请求成功",result);
		}else {
			return ResponseData.error("您还没进行实名认证，认证后可立即进行额度评估");
		}
	}
}