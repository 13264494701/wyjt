package com.jxf.web.app.wyjt.smarket;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.rc.service.RcCaDataService;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.ufang.entity.UfangLoanMarket;
import com.jxf.ufang.entity.UfangLoanMarketApplyer;
import com.jxf.ufang.entity.UfangLoanMarketApplyer.Channel;
import com.jxf.ufang.entity.UfangLoanMarketApplyer.OperatorStatus;
import com.jxf.ufang.entity.UfangLoanMarketApplyer.RealNameStatus;
import com.jxf.ufang.entity.UfangLoanMarketApplyer.SesameStatus;
import com.jxf.ufang.service.UfangLoanMarketApplyerService;
import com.jxf.ufang.service.UfangLoanMarketService;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.smarket.SmarketApplyRequestParam;
import com.jxf.web.model.wyjt.app.smarket.SmarketApplyResponseResult;
import com.jxf.web.model.wyjt.app.smarket.SmarketDetailRequestParam;
import com.jxf.web.model.wyjt.app.smarket.SmarketDetailResponseResult;

import com.jxf.web.model.wyjt.app.smarket.SmarketListResponseResult;
import com.jxf.web.model.wyjt.app.smarket.SmarketListResponseResult.Market;


/**
 *  贷款超市
 * @author wo
 * @version 2019-3-7
 */
@Controller("wyjtAppSmarketController")
@RequestMapping(value = "${wyjtApp}/smarket")
public class SmarketController extends BaseController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private UfangLoanMarketService ufangLoanMarketService;
    @Autowired
    private UfangLoanMarketApplyerService ufangLoanMarketApplyerService;
	@Autowired
	private RcCaDataService rcCaDataService;

	
	
	/**
	 * 列表页面
	 * @return
	 */
	@RequestMapping(value = "list")
	@ResponseBody
	public ResponseData list(HttpServletRequest request,HttpServletResponse response) {
		
		SmarketListResponseResult result = new SmarketListResponseResult();
		
		UfangLoanMarket loanMarket = new UfangLoanMarket();
		loanMarket.setIsMarketable(true);
		Page<UfangLoanMarket> page = ufangLoanMarketService.findPage(new Page<UfangLoanMarket>(request, response), loanMarket); 
		
		for(UfangLoanMarket market:page.getList()) {
			Market m = new SmarketListResponseResult().new Market();
			m.setMarketId(market.getId().toString());
			m.setName(market.getName());
			m.setLogo(Global.getConfig("domain")+market.getLogo());
			m.setMinLoanAmt(market.getMinLoanAmt());
			m.setMaxLoanAmt(market.getMaxLoanAmt());
			m.setDisplayLoanQuantity(market.getDisplayLoanQuantity());
			result.getMarketList().add(m);
		}	
		return ResponseData.success("贷款列表查询成功",result);
	}
	
	/**
	 * 贷超详情
	 * @return
	 */
	@RequestMapping(value = "detail")
	@ResponseBody
	public ResponseData detail(HttpServletRequest request) {
		
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		SmarketDetailRequestParam reqData = JSONObject.parseObject(param,SmarketDetailRequestParam.class);

		SmarketDetailResponseResult result = new SmarketDetailResponseResult();
		Long marketId = Long.valueOf(reqData.getMarketId());
		UfangLoanMarket market = ufangLoanMarketService.get(marketId);
		result.setMarketId(market.getId().toString());
		result.setName(market.getName());
		result.setLogo(Global.getConfig("domain")+market.getLogo());
		result.setCheckTerm(market.getCheckTerm());
		result.setMinLoanAmt(market.getMinLoanAmt());
		result.setMaxLoanAmt(market.getMaxLoanAmt());
		
		result.setMinIntRate(market.getMinIntRate());
		result.setMaxIntRate(market.getMaxIntRate());
		
		result.setMinLoanTerm(market.getMinLoanTerm());
		result.setMaxLoanTerm(market.getMaxLoanTerm());
		
		result.setDisplayLoanQuantity(market.getDisplayLoanQuantity());
		result.setNeedsIdentify(market.getNeedsIdentify()?1:0);
		result.getIdentifyList().add(new SmarketDetailResponseResult().new Identify(0, ""));
		
		if(!VerifiedUtils.isVerified(member.getVerifiedList(),6)) {
			String yysUrl ="https://amazing.shujumohe.com/box/yys?box_token=5EDD5857BDBD4FA7826D41259F234A83&arr_pass_hide=real_name,identity_code,passback_params&real_name="
					+ member.getName() + "&identity_code=" + member.getIdNo() + "&passback_params="+ member.getId();
	         result.getIdentifyList().add(new SmarketDetailResponseResult().new Identify(1, yysUrl));
		}else {
			 result.getIdentifyList().add(new SmarketDetailResponseResult().new Identify(1, ""));
		}

		if(!VerifiedUtils.isVerified(member.getVerifiedList(),4)) {
			String zmToken = rcCaDataService.getZmToken(member.getName(), member.getUsername(), member.getIdNo(),member.getId()+"");
			String zmfUrl ="https://prod.gxb.io/v2/auth/sesame_multiple?returnUrl=https%3a%2f%2fprod.51jt.com%2fcallback%2fgxb%2freturnUrl&token=" + zmToken; 
			result.getIdentifyList().add(new SmarketDetailResponseResult().new Identify(2, zmfUrl));
		}else {
			result.getIdentifyList().add(new SmarketDetailResponseResult().new Identify(2, ""));
		}
	
		return ResponseData.success("查询详情成功",result);
	}
	
	/**
	 * 贷款申请
	 * @return
	 */
	@RequestMapping(value = "apply")
	@ResponseBody
	public ResponseData apply(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		SmarketApplyRequestParam reqData = JSONObject.parseObject(param,SmarketApplyRequestParam.class);
		Long marketId = Long.valueOf(reqData.getMarketId());
		      
		UfangLoanMarket market = ufangLoanMarketService.get(marketId);
		SmarketApplyResponseResult result = new SmarketApplyResponseResult();
		result.setMarketId(reqData.getMarketId());
		result.setRedirectType(market.getRedirectType().ordinal());
		result.setUrl(market.getRedirectUrl());
		
        //每周一个用户只能提交一次
        if (ufangLoanMarketApplyerService.selectWeekUpdateCount(marketId,member.getUsername()) > 0) {
        	return ResponseData.success("借款申请提交成功",result);
        }
				
		UfangLoanMarketApplyer ufangLoanMarketApplyer = new UfangLoanMarketApplyer();
		ufangLoanMarketApplyer.setUfangLoanMarket(market);//优放贷
		ufangLoanMarketApplyer.setPhoneNo(member.getUsername());
		UfangLoanMarketApplyer applyer = ufangLoanMarketApplyerService.getByPhoneNoAndMarketId(ufangLoanMarketApplyer);

		if(applyer==null) {
			applyer = new UfangLoanMarketApplyer();
			applyer.setUfangLoanMarket(market);
			applyer.setPhoneNo(member.getUsername());
			applyer.setName(member.getName());
			applyer.setChannel(Channel.app);
			applyer.setIdNo(member.getIdNo());
			applyer.setCardNo(null);
			applyer.setAppRegister("1");
			applyer.setMember(member);
			applyer.setApplyTimes(1);
			applyer.setSesameStatus(SesameStatus.unauth);//初始化申请人设为未认证下面会更新
			applyer.setOperatorStatus(OperatorStatus.unauth);//初始化申请人设为未认证下面会更新

		}
		
		applyer = ufangLoanMarketApplyerService.getApplyerAuthStatus(applyer,member);
		if(market.getNeedsIdentify()&&applyer.getRealNameStatus().equals(RealNameStatus.unauth)) {
			return ResponseData.error("您还没有实名认证,请认证后再操作");
		}
		if(market.getNeedsIdentify()&&applyer.getSesameStatus().equals(SesameStatus.unauth)) {
			return ResponseData.error("芝麻分未认证或认证已失效，请认证后再操作");
		}
		if(market.getNeedsIdentify()&&applyer.getOperatorStatus().equals(OperatorStatus.unauth)) {
			Integer verifiedList = member.getVerifiedList();
			verifiedList = VerifiedUtils.removeVerified(verifiedList, 6);
			member.setVerifiedList(verifiedList);
			RedisUtils.put("memberInfo" + member.getId(), "operatorStatus", "0");
			memberService.save(member);
			
			return ResponseData.error("运营商未认证或认证已失效，请认证后再操作");
		}
		String applyIp = Global.getRemoteAddr(request);
		applyer.setApplyIp(applyIp);
		applyer.setApplyArea("");
		applyer.setApplyTimes(applyer.getApplyTimes()== null?0:applyer.getApplyTimes() + 1);
		applyer.setPushStatus(UfangLoanMarketApplyer.PushStatus.pendingPush);
		ufangLoanMarketApplyerService.save(applyer);
		return ResponseData.success("借款申请提交成功",result);
	}
}