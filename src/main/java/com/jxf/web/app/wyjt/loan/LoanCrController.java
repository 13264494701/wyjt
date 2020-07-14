package com.jxf.web.app.wyjt.loan;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.entity.NfsCrAuction;
import com.jxf.loan.entity.NfsCrContract;
import com.jxf.loan.entity.NfsLoanContract;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsCrAuctionService;
import com.jxf.loan.service.NfsCrContractService;
import com.jxf.loan.service.NfsLoanContractService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.utils.LoanUtils;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.H5Utils;
import com.jxf.mem.utils.MemUtils;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.svc.config.Constant;
import com.jxf.svc.config.Global;
import com.jxf.svc.sys.area.entity.Area;
import com.jxf.svc.sys.area.service.AreaService;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.NumberToChineseUtils;
import com.jxf.svc.utils.PingYinUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.admin.sys.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.auction.ApplyCrAuctionRequestParam;
import com.jxf.web.model.wyjt.app.auction.CityGroupResponseResult;
import com.jxf.web.model.wyjt.app.auction.CityListResponseResult;
import com.jxf.web.model.wyjt.app.auction.CityListResponseResult.City;
import com.jxf.web.model.wyjt.app.auction.ContractDownloadUrlRequestParam;
import com.jxf.web.model.wyjt.app.auction.ContractDownloadUrlResponseResult;
import com.jxf.web.model.wyjt.app.auction.CrTransferListRequestParam;
import com.jxf.web.model.wyjt.app.auction.CrTransferListResponseResult;
import com.jxf.web.model.wyjt.app.auction.LoanCrAuctionListRequestParam;
import com.jxf.web.model.wyjt.app.auction.LoanCrAuctionListResponseResult;
import com.jxf.web.model.wyjt.app.auction.MinePurchaseListRequestParam;
import com.jxf.web.model.wyjt.app.auction.MineTransferListRequestParam;
import com.jxf.web.model.wyjt.app.auction.PayAuctionRequestParam;
import com.jxf.web.model.wyjt.app.auction.PurchaseDetailResponseResult;
import com.jxf.web.model.wyjt.app.auction.ReleaseAuctionResponseResult;
import com.jxf.web.model.wyjt.app.auction.SearchCityListRequestParam;
import com.jxf.web.model.wyjt.app.auction.SearchCityListResponseResult;


/**
 * 借条债权中心
 * @author wo
 * @version 2018-11-07
 */
@Controller("wyjtLoanCrController")
@RequestMapping(value = "${wyjtApp}/auction")
public class LoanCrController extends BaseController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsCrAuctionService crAuctionService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private NfsLoanContractService loanContractService;
	@Autowired
	private NfsCrContractService crContractService;
	
	/**
	 * 	获取可转让的借条列表
	 */
	@RequestMapping(value = "/crTransferList")
	public @ResponseBody
	ResponseData crTransferList(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		CrTransferListRequestParam reqData = JSONObject.parseObject(param,CrTransferListRequestParam.class);
		CrTransferListResponseResult result = new CrTransferListResponseResult();
		
		ResponseData responseData = loanRecordService.findCrTransferPage(member,reqData,result);
		
		return responseData;
	}
	
	/**
	 * 	申请转让借条
	 */
	@RequestMapping(value = "/applyCrAuction")
	public @ResponseBody
	ResponseData applyCrAuction(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		ApplyCrAuctionRequestParam reqData = JSONObject.parseObject(param,ApplyCrAuctionRequestParam.class);
		ReleaseAuctionResponseResult result = new ReleaseAuctionResponseResult();
		
		Long loanId = Long.parseLong(reqData.getLoanId());
		String price = reqData.getPrice();
		String payPwd = reqData.getPayPwd();
		NfsLoanRecord loanRecord = loanRecordService.get(loanId);
		
		//验证支付密码
		ResponseData checkPwd = memberService.checkPayPwd(payPwd, member);
		if(checkPwd.getCode() != 0){
			return checkPwd;
		}
		//验证借款人实名认证
		Member loanee = memberService.get(loanRecord.getLoanee());
		if(!(VerifiedUtils.isVerified(loanee.getVerifiedList(), 1)&&VerifiedUtils.isVerified(loanee.getVerifiedList(), 2))) {
			return ResponseData.error("借款人未实名认证，不能进行债转");
		}
		//验证转让金额
		BigDecimal amount = loanRecord.getAmount();
		BigDecimal interest = loanRecord.getInterest();
		BigDecimal overdueDays = new BigDecimal(
				DateUtils.getDistanceOfTwoDate(loanRecord.getDueRepayDate(), new Date()));
		BigDecimal overdueInterest = LoanUtils.calOverdueInterest(loanRecord.getDueRepayAmount(), overdueDays);
		
		BigDecimal sumAmount = amount.add(interest).add(overdueInterest);//应付金额
		if(sumAmount.compareTo(new BigDecimal(price)) == -1) {
			return ResponseData.error("转让金额不能大于借条应还金额");
		}
		if(StringUtils.equals(price, "0")) {
			return ResponseData.error("转让金额不能为0");
		}
		if(loanRecord.getIntRate().compareTo(new BigDecimal(24))>0) {
			return ResponseData.error("该借条利率年化超过24%，不能债权转让");
		}

//		NfsLoanContract loanContract = loanContractService.getCurrentContractByLoanId(loanRecord.getId());
//		if(loanContract==null||!StringUtils.contains(loanContract.getContractUrl(), "junziqian_signed")) {
//			return ResponseData.error("当前借条合同暂不支持债转");
//		}
			
		//判断借条状态
		if(!loanRecord.getStatus().equals(NfsLoanRecord.Status.overdue)) {
			return ResponseData.error("该借条未逾期，不能债权转让");
		}
		if(!loanRecord.getAuctionStatus().equals(NfsLoanRecord.AuctionStatus.initial)) {
			return ResponseData.error("该借条已经转让，不能重复债转");
		}

		if(!loanRecord.getPartialStatus().equals(NfsLoanRecord.PartialStatus.initial)) {
			return ResponseData.error("该借条存在部分还款待确认，不能债权转让");
		}

		if(!loanRecord.getDelayStatus().equals(NfsLoanRecord.DelayStatus.initial)) {
			return ResponseData.error("该借条存在延期申请待确认，不能债权转让");
		}
		if(!loanRecord.getLineDownStatus().equals(NfsLoanRecord.LineDownStatus.initial)) {
			return ResponseData.error("该借条存在线下还款待确认，不能债权转让");
		}

		if(!loanRecord.getArbitrationStatus().equals(NfsLoanRecord.ArbitrationStatus.initial)) {
			return ResponseData.error("该借条已被仲裁，不能债权转让");
		}

		if(!loanRecord.getCollectionStatus().equals(NfsLoanRecord.CollectionStatus.initial)) {
			return ResponseData.error("该借条已被申请催收，不能债权转让");
		}
		//判断是否已经转让成功
		Integer flag = crAuctionService.checkStatus(loanRecord);
		if(flag == 1) {
			return ResponseData.error("该借条已被转让，不能重复债转");
		}else if(flag == 2) {
			return ResponseData.error("该借条审核失败，不能再进行债转");
		}
		//申请转让
		ResponseData responseData = crAuctionService.applyCrAuction(loanRecord, price, result);
		return responseData;
	}
	
	/**
	 * 	获取可购入的借条列表
	 */
	@RequestMapping(value = "/crAuctionList")
	public @ResponseBody
	ResponseData crAuctionList(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		LoanCrAuctionListRequestParam reqData = JSONObject.parseObject(param,LoanCrAuctionListRequestParam.class);
		LoanCrAuctionListResponseResult result = new LoanCrAuctionListResponseResult();
		
		ResponseData responseData = crAuctionService.findCrAuctionPage(member,reqData,result);
		
		return responseData;
	}
	
	/**
	 * 	城市列表
	 */
	@RequestMapping(value = "/cityList")
	public @ResponseBody
	ResponseData cityList() {
		Member member = memberService.getCurrent();
		CityListResponseResult result = new CityListResponseResult();
		CityGroupResponseResult a = new CityGroupResponseResult();
		a.setTap("A");
		CityGroupResponseResult b = new CityGroupResponseResult();
		b.setTap("B");
		CityGroupResponseResult c = new CityGroupResponseResult();
		c.setTap("C");
		CityGroupResponseResult d = new CityGroupResponseResult();
		d.setTap("D");
		CityGroupResponseResult e = new CityGroupResponseResult();
		e.setTap("E");
		CityGroupResponseResult f = new CityGroupResponseResult();
		f.setTap("F");
		CityGroupResponseResult g = new CityGroupResponseResult();
		g.setTap("G");
		CityGroupResponseResult h = new CityGroupResponseResult();
		h.setTap("H");
		CityGroupResponseResult j = new CityGroupResponseResult();
		j.setTap("J");
		CityGroupResponseResult k = new CityGroupResponseResult();
		k.setTap("K");
		CityGroupResponseResult l = new CityGroupResponseResult();
		l.setTap("L");
		CityGroupResponseResult m = new CityGroupResponseResult();
		m.setTap("M");
		CityGroupResponseResult n = new CityGroupResponseResult();
		n.setTap("N");
		CityGroupResponseResult p = new CityGroupResponseResult();
		p.setTap("P");
		CityGroupResponseResult q = new CityGroupResponseResult();
		q.setTap("Q");
		CityGroupResponseResult r = new CityGroupResponseResult();
		r.setTap("R");
		CityGroupResponseResult s = new CityGroupResponseResult();
		s.setTap("S");
		CityGroupResponseResult t = new CityGroupResponseResult();
		t.setTap("T");
		CityGroupResponseResult w = new CityGroupResponseResult();
		w.setTap("W");
		CityGroupResponseResult x = new CityGroupResponseResult();
		x.setTap("X");
		CityGroupResponseResult y = new CityGroupResponseResult();
		y.setTap("Y");
		CityGroupResponseResult z = new CityGroupResponseResult();
		z.setTap("Z");
		
		//定位城市
		Area localArea = areaService.get(member.getArea());
		if(localArea != null) {
			String localName = localArea.getName();
			result.setLocalName(localName);
		}
		
		Area area = new Area();
		area.setType("2");
		List<Area> areaList = areaService.findAreaList(area);
		//热门城市
		for (Area ar : areaList) {
			String name = ar.getName();
			String id = ar.getId().toString();
			if("北京,上海,广州,深圳,成都,重庆,杭州,天津".contains(name)){
				City city = result.new City();
				city.setCityId(id);
				city.setName(name);
				result.getPopCityList().add(city);
			}
		}
		
 		//城市列表 i o u v 不做声母
		for(Area ar:areaList) {
			City city = result.new City();
			String cityId = ar.getId().toString();
			String cityName = ar.getName();
			city.setCityId(cityId);
			city.setName(cityName);
			
			String spell = PingYinUtils.getPingYin(cityName);
			spell = spell.substring(0, 1);
			if(StringUtils.endsWithIgnoreCase(spell, "a")) {
				a.getList().add(city);
			}
			if(StringUtils.endsWithIgnoreCase(spell, "b")) {
				b.getList().add(city);
			}
			if(StringUtils.endsWithIgnoreCase(spell, "c")) {
				c.getList().add(city);			
			}
			if(StringUtils.endsWithIgnoreCase(spell, "d")) {
				d.getList().add(city);
			}
			if(StringUtils.endsWithIgnoreCase(spell, "e")) {
				e.getList().add(city);
			}
			if(StringUtils.endsWithIgnoreCase(spell, "f")) {
				f.getList().add(city);
			}
			if(StringUtils.endsWithIgnoreCase(spell, "g")) {
				g.getList().add(city);
			}
			if(StringUtils.endsWithIgnoreCase(spell, "h")) {
				h.getList().add(city);
			}
			if(StringUtils.endsWithIgnoreCase(spell, "j")) {
				j.getList().add(city);
			}
			if(StringUtils.endsWithIgnoreCase(spell, "k")) {
				k.getList().add(city);
			}
			if(StringUtils.endsWithIgnoreCase(spell, "l")) {
				l.getList().add(city);
			}
			if(StringUtils.endsWithIgnoreCase(spell, "m")) {
				m.getList().add(city);
			}
			if(StringUtils.endsWithIgnoreCase(spell, "n")) {
				n.getList().add(city);
			}
			if(StringUtils.endsWithIgnoreCase(spell, "p")) {
				p.getList().add(city);
			}
			if(StringUtils.endsWithIgnoreCase(spell, "q")) {
				q.getList().add(city);
			}
			if(StringUtils.endsWithIgnoreCase(spell, "r")) {
				r.getList().add(city);
			}
			if(StringUtils.endsWithIgnoreCase(spell, "s")) {
				s.getList().add(city);
			}
			if(StringUtils.endsWithIgnoreCase(spell, "t")) {
				t.getList().add(city);
			}
			if(StringUtils.endsWithIgnoreCase(spell, "w")) {
				w.getList().add(city);
			}
			if(StringUtils.endsWithIgnoreCase(spell, "x")) {
				x.getList().add(city);
			}
			if(StringUtils.endsWithIgnoreCase(spell, "y")) {
				y.getList().add(city);
			}
			if(StringUtils.endsWithIgnoreCase(spell, "z")) {
				z.getList().add(city);
			}
		}
		result.getCityList().add(a);
		result.getCityList().add(b);
		result.getCityList().add(c);
		result.getCityList().add(d);
		result.getCityList().add(e);
		result.getCityList().add(f);
		result.getCityList().add(g);
		result.getCityList().add(h);
		result.getCityList().add(j);
		result.getCityList().add(k);
		result.getCityList().add(l);
		result.getCityList().add(m);
		result.getCityList().add(n);
		result.getCityList().add(p);
		result.getCityList().add(q);
		result.getCityList().add(r);
		result.getCityList().add(s);
		result.getCityList().add(t);
		result.getCityList().add(w);
		result.getCityList().add(x);
		result.getCityList().add(y);
		result.getCityList().add(z);
		return ResponseData.success("获取地区列表成功",result);
	}
	
	/**
	 * 	模糊查询城市列表
	 */
	@RequestMapping(value = "/searchCityList")
	public @ResponseBody
	ResponseData searchCityList(HttpServletRequest request) {
		String param = request.getParameter("param");
		SearchCityListRequestParam reqData = JSONObject.parseObject(param,SearchCityListRequestParam.class);
		SearchCityListResponseResult result = new SearchCityListResponseResult();		
		
		Area area = new Area();
		area.setName(reqData.getNameStr());
		area.setType("2");
		List<Area> areaList = areaService.findAreaList(area);
		for (Area ar : areaList) {
			 City city = new CityListResponseResult().new City();
			 city.setCityId(ar.getId().toString());
			 city.setName(ar.getName());
			 result.getCityList().add(city);
		}
		return ResponseData.success("模糊查询城市列表成功", result);
	}
	/**
	 * 跳转到购买借条页
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/buyCrAuctionPage")
	public ModelAndView buyCrAuctionPage(HttpServletRequest request){
		Member member = memberService.getCurrent();
		ModelAndView mv = new ModelAndView("app/cr/payForCrAuction");
		ModelAndView errMv = new ModelAndView("app/cr/failedResult");
		if(member == null) {
			errMv.addObject("code", -1);
			errMv.addObject("message", "登录状态已过期，请重新登录");
			return errMv;
		}
		mv = H5Utils.addPlatform(member, mv);
		String param = request.getParameter("param");
		PayAuctionRequestParam reqData = JSONObject.parseObject(param,PayAuctionRequestParam.class);
		String auctionId = reqData.getAuctionId();
		if(StringUtils.isBlank(auctionId)) {
			errMv.addObject("code", -1);
			errMv.addObject("message", "没有对应的债权记录！");
		}
		NfsCrAuction crAuction = crAuctionService.get(Long.valueOf(auctionId));
		NfsLoanRecord loanRecord = crAuction.getLoanRecord();
		Integer overdueDays = DateUtils.getDistanceOfTwoDate(loanRecord.getDueRepayDate(), new Date());
		BigDecimal overdueInterest = LoanUtils.calOverdueInterest(loanRecord.getDueRepayAmount(), new BigDecimal(overdueDays));
		BigDecimal curBal = memberService.getCulBal(member);
		BigDecimal sellPrice = crAuction.getCrSellPrice();
		Boolean isEnough = false;
		if(curBal.compareTo(sellPrice) >= 0) {
			isEnough = true;
		}
		Boolean isPayPsw = VerifiedUtils.isVerified(member.getVerifiedList(), 22);
		String memberToken = request.getHeader("x-memberToken");
		mv.addObject("crAuction", crAuction);
		mv.addObject("loanRecord", loanRecord);
		mv.addObject("overdueDays", overdueDays);
		mv.addObject("overdueInterest", overdueInterest);
		mv.addObject("curBal", curBal);
		mv.addObject("isEnough", isEnough);
		mv.addObject("isPayPsw", isPayPsw);
		mv.addObject("memberToken", memberToken);
		return mv;
	}
	
	
	/**
	 * 	购买债转借条
	 */
	@RequestMapping(value = "/payAuction")
	public ModelAndView payAuction(HttpServletRequest request) {
		Member buyer = memberService.getCurrent();
		String crAuctionId = request.getParameter("crAuctionId");
		ModelAndView mv = new ModelAndView("app/cr/payForCrAuctionResult");
		ModelAndView errMv = new ModelAndView("app/cr/failedResult");
		if(buyer == null) {
			errMv.addObject("code", -1);
			errMv.addObject("message", "登录状态已过期，请重新登录");
			return errMv;
		}
		mv = H5Utils.addPlatform(buyer, mv);
		errMv = H5Utils.addPlatform(buyer, errMv);
		if(StringUtils.isBlank(crAuctionId)) {
			errMv.addObject("code", -1);
			errMv.addObject("message", "没有对应的债权记录！");
			return errMv;
		}
		//validity checking
		NfsCrAuction crAuction = crAuctionService.get(Long.valueOf(crAuctionId));
		crAuction.setCrBuyer(buyer);
		NfsLoanRecord loanRecord = loanRecordService.get(crAuction.getLoanRecord());
		Map<String, String> resultMap = crAuctionService.buyCrOperationLegalCheck(loanRecord, crAuction);
		if(StringUtils.equals(resultMap.get("success"), "false")) {
			errMv.addObject("code", -1);
			errMv.addObject("message", resultMap.get("message"));
			return errMv;
		}
		
		Member seller = memberService.get(loanRecord.getLoaner());
		crAuction.setCrSeller(seller);
		try {
			int updateCode = crAuctionService.payCrAuction(crAuction, loanRecord);
			if(updateCode == Constant.UPDATE_FAILED) {
				errMv.addObject("code", -1);
				errMv.addObject("message", "账户余额不足！");
				return errMv;
			}
			//购买成功  发消息对话短信
			crAuctionService.sendMessageAfterPaySucceed(crAuction, loanRecord);
			
			Integer overdueDays = DateUtils.getDistanceOfTwoDate(loanRecord.getDueRepayDate(), new Date());
			BigDecimal overdueInterest = LoanUtils.calOverdueInterest(loanRecord.getAmount(), new BigDecimal(overdueDays));
			String memberToken = request.getHeader("x-memberToken");
			mv.addObject("crAuction", crAuction);
			mv.addObject("loanRecord", loanRecord);
			mv.addObject("overdueDays", overdueDays);
			mv.addObject("overdueInterest", overdueInterest);
			mv.addObject("memberToken", memberToken);
			return mv;
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
			errMv.addObject("code", -1);
			errMv.addObject("message", "账户/债转记录更新失败，请稍后再进行操作！");
			return errMv;
		}
	}
	
	/**
	 * 	我买入的借条列表
	 */
	@RequestMapping(value = "/purchaseList")
	public @ResponseBody
	ResponseData crPurchaseList(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		MinePurchaseListRequestParam reqData = JSONObject.parseObject(param,MinePurchaseListRequestParam.class);
		LoanCrAuctionListResponseResult result = new LoanCrAuctionListResponseResult();
		
		ResponseData responseData = crAuctionService.findPurchaseList(member, reqData, result);
		return responseData;
	}
	
	/**
	 * 	我转让的借条列表
	 */
	@RequestMapping(value = "/auctionList")
	public @ResponseBody
	ResponseData auctionList(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		MineTransferListRequestParam reqData = JSONObject.parseObject(param,MineTransferListRequestParam.class);
		LoanCrAuctionListResponseResult result = new LoanCrAuctionListResponseResult();
		
		ResponseData responseData = crAuctionService.findAuctionList(member, reqData, result);
		return responseData;
	}
	
	
	/**
	 * 	转让详情  消息跳转用
	 */
	@RequestMapping(value = "/getAuctionDetail")
	public @ResponseBody
	ResponseData getAuctionDetail(HttpServletRequest request) {
		String param = request.getParameter("param");
		PayAuctionRequestParam reqData = JSONObject.parseObject(param,PayAuctionRequestParam.class);
		
		String auctionId = reqData.getAuctionId();
		ReleaseAuctionResponseResult result = crAuctionService.getCrAuctionDetail(auctionId);
		return ResponseData.success("查询转让详情成功", result);
	}

	/**
	 * 	买入详情  消息跳转用
	 */
	@RequestMapping(value = "/getPurchaseDetail")
	public @ResponseBody
	ResponseData getPurchaseDetail(HttpServletRequest request) {
		String param = request.getParameter("param");
		PayAuctionRequestParam reqData = JSONObject.parseObject(param,PayAuctionRequestParam.class);
		
		String auctionId = reqData.getAuctionId();
		PurchaseDetailResponseResult result = crAuctionService.getCrPurchaseDetail(auctionId);
		return ResponseData.success("查询买入详情成功", result);
	}
	
	
	/**
	 * 	取消转让
	 */
	@RequestMapping(value = "/releaseAuction")
	public @ResponseBody
	ResponseData removeAuction(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		PayAuctionRequestParam reqData = JSONObject.parseObject(param,PayAuctionRequestParam.class);
		ReleaseAuctionResponseResult result = new ReleaseAuctionResponseResult();
		
		ResponseData responseData = crAuctionService.releaseAuction(member, reqData, result);
		return responseData;
	}
	
	@RequestMapping(value="/seeCrAuctionAgreementBeforePay")
	public ModelAndView seeCrAuctionAgreementBeforePay(HttpServletRequest request) {
		String auctionId = request.getParameter("auctionId");
		Member buyer = memberService.getCurrent();
		if(buyer == null) {
			ModelAndView errMv = new ModelAndView("app/cr/failedResult");
			errMv.addObject("code", -1);
			errMv.addObject("message", "登录状态已过期，请重新登录");
			return errMv;
		}
		NfsCrAuction crAuction = crAuctionService.get(Long.valueOf(auctionId));
		ModelAndView mv = new ModelAndView("app/cr/claimsTrsAgreementForPay");
		mv = H5Utils.addPlatform(buyer, mv);
		Member seller = memberService.get(crAuction.getCrSeller().getId());
		NfsLoanRecord loanRecord = loanRecordService.get(crAuction.getLoanRecord());
		String amountCHNum = NumberToChineseUtils.getChineseNumber(StringUtils.decimalToStr(loanRecord.getAmount(), 2));
		String dueRepayCHNum = NumberToChineseUtils.getChineseNumber(StringUtils.decimalToStr(loanRecord.getDueRepayAmount(), 2));
		String sellPriceCHNum = NumberToChineseUtils.getChineseNumber(StringUtils.decimalToStr(crAuction.getCrSellPrice(), 2));
		String creditCode = Constant.COM_CREDIT_CODE;
		seller = MemUtils.maskIdNo(seller);
		seller = MemUtils.maskName(seller);
		buyer = MemUtils.maskIdNo(buyer);
		Member returnSeller = new Member();
		returnSeller.setName(seller.getName());
		returnSeller.setIdNo(seller.getIdNo());
		Member returnBuyer = new Member();
		returnBuyer.setName(buyer.getName());
		returnBuyer.setIdNo(buyer.getIdNo());
		Member returnLoanee = new Member();
		returnLoanee.setName(loanRecord.getLoaneeName());
		loanRecord.setLoanee(returnLoanee);
		loanRecord.setLoaner(null);
		mv.addObject("seller", returnSeller);
		mv.addObject("buyer", returnBuyer);
		mv.addObject("loanRecord", loanRecord);
		mv.addObject("amountCHNum", amountCHNum);
		mv.addObject("dueRepayCHNum", dueRepayCHNum);
		mv.addObject("sellPriceCHNum", sellPriceCHNum);
		mv.addObject("createTime", DateUtils.getDateStr(loanRecord.getCreateTime(), "yyyy年MM月dd日"));
		mv.addObject("dueRepayDate", DateUtils.getDateStr(loanRecord.getDueRepayDate(), "yyyy年MM月dd日"));
		if(crAuction.getStatus().equals(NfsCrAuction.Status.successed)) {
			mv.addObject("endTime", DateUtils.getDateStr(crAuction.getUpdateTime(), "yyyy年MM月dd日"));
		}else {
			mv.addObject("endTime", DateUtils.getDateStr(new Date(), "yyyy年MM月dd日"));
		}
		mv.addObject("sellPrice", crAuction.getCrSellPrice());
		mv.addObject("creditCode", creditCode);
		return mv;
	}
	
	
	@RequestMapping(value="/seeCrAuctionAgreement")
	public ModelAndView seeCrAuctionAgreement(HttpServletRequest request) {
		String param = request.getParameter("param");
		PayAuctionRequestParam reqData = JSONObject.parseObject(param,PayAuctionRequestParam.class);
		String auctionId = reqData.getAuctionId();
		Member buyer = memberService.getCurrent();
		if(buyer == null) {
			ModelAndView errMv = new ModelAndView("app/cr/failedResult");
			errMv.addObject("code", -1);
			errMv.addObject("message", "登录状态已过期，请重新登录");
			return errMv;
		}
		NfsCrAuction crAuction = crAuctionService.get(Long.valueOf(auctionId));
		ModelAndView mv = null;
		if(crAuction.getStatus().equals(NfsCrAuction.Status.forsale)) {
			mv = new ModelAndView("app/cr/claimsTrsAgreementForPay");
		}else {
			mv = new ModelAndView("app/cr/claimsTrsAgreementForSee");
		}
		mv = H5Utils.addPlatform(buyer, mv);
		Member seller = memberService.get(crAuction.getCrSeller().getId());
		NfsLoanRecord loanRecord = loanRecordService.get(crAuction.getLoanRecord());
		String amountCHNum = NumberToChineseUtils.getChineseNumber(StringUtils.decimalToStr(loanRecord.getAmount(), 2));
		String dueRepayCHNum = NumberToChineseUtils.getChineseNumber(StringUtils.decimalToStr(loanRecord.getDueRepayAmount(), 2));
		String sellPriceCHNum = NumberToChineseUtils.getChineseNumber(StringUtils.decimalToStr(crAuction.getCrSellPrice(), 2));
		String creditCode = Constant.COM_CREDIT_CODE;
		if(!crAuction.getStatus().equals(NfsCrAuction.Status.successed)) {
			seller = MemUtils.maskName(seller);
			seller = MemUtils.maskIdNo(seller);
			buyer = MemUtils.maskIdNo(buyer);
		}
		mv.addObject("seller", seller);
		mv.addObject("buyer", buyer);
		mv.addObject("loanRecord", loanRecord);
		mv.addObject("amountCHNum", amountCHNum);
		mv.addObject("dueRepayCHNum", dueRepayCHNum);
		mv.addObject("sellPriceCHNum", sellPriceCHNum);
		mv.addObject("createTime", DateUtils.getDateStr(loanRecord.getCreateTime(), "yyyy年MM月dd日"));
		mv.addObject("dueRepayDate", DateUtils.getDateStr(loanRecord.getDueRepayDate(), "yyyy年MM月dd日"));
		if(crAuction.getStatus().equals(NfsCrAuction.Status.successed)) {
			mv.addObject("endTime", DateUtils.getDateStr(crAuction.getUpdateTime(), "yyyy年MM月dd日"));
		}else {
			mv.addObject("endTime", DateUtils.getDateStr(new Date(), "yyyy年MM月dd日"));
		}
		mv.addObject("sellPrice", crAuction.getCrSellPrice());
		mv.addObject("creditCode", creditCode);
		return mv;
	}

	@RequestMapping(value="/seeLoanAgreement")
	public ModelAndView seeLoanAgreement(HttpServletRequest request) {
		String param = request.getParameter("param");
		PayAuctionRequestParam reqData = JSONObject.parseObject(param,PayAuctionRequestParam.class);
		String auctionId = reqData.getAuctionId();
		
		Member buyer = memberService.getCurrent();
		if(buyer == null) {
			ModelAndView errMv = new ModelAndView("app/cr/failedResult");
			errMv.addObject("code", -1);
			errMv.addObject("message", "登录状态已过期，请重新登录");
			return errMv;
		}
		ModelAndView mv = new ModelAndView("app/cr/agreementForCr");
		mv = H5Utils.addPlatform(buyer, mv);
		NfsCrAuction crAuction = crAuctionService.get(Long.valueOf(auctionId));
		NfsLoanRecord loanRecord = loanRecordService.get(crAuction.getLoanRecord());
		String startYear = DateUtils.getDateStr(loanRecord.getCreateTime(), "yyyy");
		String startMonth = DateUtils.getDateStr(loanRecord.getCreateTime(), "MM");
		String startDay = DateUtils.getDateStr(loanRecord.getCreateTime(), "dd");
		String dueRepayYear = DateUtils.getDateStr(loanRecord.getDueRepayDate(), "yyyy");
		String dueRepayMonth = DateUtils.getDateStr(loanRecord.getDueRepayDate(), "MM");
		String dueRepayDay = DateUtils.getDateStr(loanRecord.getDueRepayDate(), "dd");
		mv.addObject("loanRecord", loanRecord);
		mv.addObject("startYear", startYear);
		mv.addObject("startMonth", startMonth);
		mv.addObject("startDay", startDay);
		mv.addObject("dueRepayYear", dueRepayYear);
		mv.addObject("dueRepayMonth", dueRepayMonth);
		mv.addObject("dueRepayDay", dueRepayDay);
		mv.addObject("createTime", DateUtils.getDateStr(loanRecord.getCreateTime(), "yyyy年MM月dd日"));
		return mv;
	}
	
	/**
	 * 	获取债转合同下载地址
	 */
	@RequestMapping(value = "/getContractDownloadUrl")
	public @ResponseBody
	ResponseData getCrContractDownloadUrl(HttpServletRequest request) {
		String param = request.getParameter("param");
		ContractDownloadUrlRequestParam reqData = JSONObject.parseObject(param,ContractDownloadUrlRequestParam.class);
		String auctionId = reqData.getAuctionId();
		String type = reqData.getType();
		String downloadUrl = "";
		NfsCrAuction crAuction = crAuctionService.get(Long.valueOf(auctionId));
		if(StringUtils.equals(type, "1")) {
			//下载债转合同
			NfsCrContract crContract = crContractService.getCrContractByCrId(crAuction.getId());
			downloadUrl = crContract.getContractUrl();
		}else {
			//下载借条合同
			NfsLoanRecord loanRecord = loanRecordService.get(crAuction.getLoanRecord());
			NfsLoanContract nfsLoanContract = new NfsLoanContract();
			nfsLoanContract.setLoanId(loanRecord.getId());
			List<NfsLoanContract> loanContracts = loanContractService.getContractByLoanId(nfsLoanContract);
			NfsLoanContract loanContract = loanContracts.get(0);
			downloadUrl = loanContract.getContractUrl();
		}
		downloadUrl = Global.getConfig("domain") + downloadUrl;
		ContractDownloadUrlResponseResult result = new ContractDownloadUrlResponseResult();
		result.setDownloadUrl(downloadUrl);
		return ResponseData.success("查询合同下载地址成功", result);
	}

}