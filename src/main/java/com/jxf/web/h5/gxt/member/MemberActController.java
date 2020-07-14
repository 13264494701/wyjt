package com.jxf.web.h5.gxt.member;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jxf.fee.service.NfsFeeRuleService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.mem.entity.MemberCard;
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.mem.service.MemberCardService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.entity.NfsRchgRecord;
import com.jxf.nfs.entity.NfsWdrlRecord;
import com.jxf.nfs.entity.NfsWdrlRecord.Status;
import com.jxf.nfs.service.NfsRchgRecordService;
import com.jxf.nfs.service.NfsWdrlRecordService;
import com.jxf.pay.entity.FuiouPayMerchantDataBean;
import com.jxf.pay.entity.LianlianMerchantDataBean;
import com.jxf.pay.utils.DESCoderFUIOU;
import com.jxf.payment.entity.Payment;
import com.jxf.payment.service.PaymentService;
import com.jxf.pwithdraw.utils.TraderRSAUtil;
import com.jxf.svc.config.Global;
import com.jxf.svc.plugin.PluginConfig;
import com.jxf.svc.plugin.fuyouPayment.FyPaymentPlugin;
import com.jxf.svc.plugin.lianlianPayment.LianlianPaymentPlugin;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.HttpUtils;
import com.jxf.svc.utils.MD5Code;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.member.ActTrxDetailResponseResult;
import com.jxf.web.model.wyjt.app.member.ActTrxListResponseResult;
import com.jxf.web.model.wyjt.app.member.ActTrxListResponseResult.Trx;

/**
 *  账户流水与转账
 * @author suhuimin
 * @version 2018-10-31
 */
@Controller("gxtH5MemberActController")
@RequestMapping(value = "${gxtH5}/member")
public class MemberActController extends BaseController {
	@Autowired
	private MemberService memberService;
	@Autowired 
	private MemberActTrxService memberActTrxService;
	@Autowired
	private MemberCardService memberCardService;
	@Autowired
	private NfsWdrlRecordService nfsWdrlRecordService;
	@Autowired
	private NfsRchgRecordService nfsRchgRecordService;
	@Autowired
	private FyPaymentPlugin fyPaymentPlugin;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private NfsFeeRuleService feeRuleService;
	@Autowired
	private LianlianPaymentPlugin lianlianPaymentPlugin;
	
	/**
	 * 账户流水
	 * @return
	 */
	@RequestMapping(value = "actTrxList")
	@ResponseBody
	public ResponseData actTrxList(HttpServletRequest request,Integer pageNo,Integer type,String startTime
			,String endTime,String startMoney,String endMoney) {
		Member member = memberService.getCurrent2();
		
		Date startTimeQuery = null;
		Date endTimeQuery = null;
		BigDecimal startMoneyQuery = null;
		BigDecimal endMoneyQuery = null;
			
		if(StringUtils.isNotBlank(startTime)){
			startTimeQuery = CalendarUtil.StringToDate(startTime+" 00:00:00");
		}
		if(StringUtils.isNotBlank(endTime)){
			endTimeQuery = CalendarUtil.StringToDate(endTime+" 23:59:59");
		}
		if(StringUtils.isNotBlank(startMoney)){
			startMoneyQuery = new BigDecimal(startMoney);
		}
		if(StringUtils.isNotBlank(endMoney)){
			endMoneyQuery = new BigDecimal(endMoney);
		}
		MemberActTrx memberActTrx = new MemberActTrx();
		
		memberActTrx.setMember(member);
		memberActTrx.setBeginTime(startTimeQuery);
		memberActTrx.setEndTime(endTimeQuery);
		memberActTrx.setMinAmount(startMoneyQuery);
		memberActTrx.setMaxAmount(endMoneyQuery);
		memberActTrx.setPageStart((pageNo-1)*20);
		/**
		 * type 0 全部  1充值提现 2服务费 3退款
		 */
		List<MemberActTrx> memberActTrxList = new ArrayList<MemberActTrx>();
		Map<String, String> sqlMap = memberActTrx.getSqlMap();
		if(type == 1){
			String[] trxCodeList = {"MB010","MB020","MB021","MB022"};
			for (int i = 0; i < trxCodeList.length; i++) {
				sqlMap.put("trxCodeList"+i,trxCodeList[i]);
			}
			memberActTrx.setSqlMap(sqlMap);
			memberActTrxList = memberActTrxService.findByTrxCodesList(memberActTrx);
		}else if(type == 2){
			String[] trxCodeList = {"GL010","GL011","GD020","GD021","GR010","GR011","GR020","GR021","GR030","GR031","GR040","GR041"};
			for (int i = 0; i < trxCodeList.length; i++) {
				sqlMap.put("trxCodeList"+i,trxCodeList[i]);
			}
			memberActTrx.setSqlMap(sqlMap);
			memberActTrxList = memberActTrxService.findByTrxCodesList(memberActTrx);
		}else if(type == 3){
			String[] trxCodeList = {"MB023","MB024","MB025","AR040","AR020","CL020","AR040"};
			for (int i = 0; i < trxCodeList.length; i++) {
				sqlMap.put("trxCodeList"+i,trxCodeList[i]);
			}
			memberActTrx.setDrc("D");
			memberActTrx.setSqlMap(sqlMap);
			memberActTrxList = memberActTrxService.findByTrxCodesList(memberActTrx);
		}else{
			String[] trxCodeList = {"GL012","GD022","GR012","GR022","GR052","GR032","GR042","GA010","GA020","GE010","GE020"};
			for (int i = 0; i < trxCodeList.length; i++) {
				sqlMap.put("trxCodeList"+i,trxCodeList[i]);
			}
			memberActTrx.setSqlMap(sqlMap);
			memberActTrxList = memberActTrxService.findListExceptToPaidAndDueIn(memberActTrx);
		}
		
		MemberActTrx countTotalMemberActTrx = new MemberActTrx();
		if(memberActTrxList != null && memberActTrxList.size()>0){
			memberActTrx.setPage(null);
			if(type == 0) {
				memberActTrx.setSqlMap(null);
			}
			countTotalMemberActTrx = memberActTrxService.countTotalByTrxCodes(memberActTrx); 
		}
		ActTrxListResponseResult result = new ActTrxListResponseResult();
		
		if(countTotalMemberActTrx != null){
			BigDecimal totalIncome = countTotalMemberActTrx.getTotalIncome();
			BigDecimal totalExpenditure = countTotalMemberActTrx.getTotalExpenditure();
			if(totalIncome != null){
				result.setTotalIncome(StringUtils.decimalToStr(totalIncome, 2));
			}else{
				result.setTotalIncome("0");
			}
			if(totalExpenditure != null){
				result.setTotalExpenditure(StringUtils.decimalToStr(totalExpenditure, 2));
			}else{
				result.setTotalExpenditure("0");
			}
		}
		
		List<Trx> trxList = result.getTrxList();
		for (MemberActTrx actTrx : memberActTrxList) {
			Trx trx = new ActTrxListResponseResult().new Trx();
			DecimalFormat decimalFormat = new DecimalFormat("0.00");
			trx.setAmount(decimalFormat.format(actTrx.getTrxAmt()));
			trx.setTrxTime(DateUtils.formatDateTime(actTrx.getCreateTime()));
			trx.setDrc(actTrx.getDrc().equals("D")?1:0);
			trx.setTitle(actTrx.getTitle());
			trx.setTrxId(String.valueOf(actTrx.getId()));
			String image = memberActTrxService.getImage(actTrx);
			trx.setImgUrl(image);
			trxList.add(trx);
		}
		result.setTrxList(trxList);
		result.setStartTime(startTime);
		result.setEndTime(endTime);
		return ResponseData.success("查询流水成功",result);
	}
	
	/**
	 * 流水详情
	 * @return
	 */
	@RequestMapping(value = "actTrxDetail")
	@ResponseBody
	public ResponseData actTrxDetail(HttpServletRequest request,String trxId) {
		
		MemberActTrx memberActTrx = memberActTrxService.get(Long.valueOf(trxId));
		ActTrxDetailResponseResult result = new ActTrxDetailResponseResult();
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		result.setAmount(decimalFormat.format(memberActTrx.getTrxAmt()));
		result.setTrxTime(DateUtils.formatDateTime(memberActTrx.getCreateTime()));
		result.setDrc(memberActTrx.getDrc().equals("D")?1:0);
		result.setRmk(memberActTrx.getRmk());
		result.setTitle(memberActTrx.getTitle());
		String image = memberActTrxService.getImage(memberActTrx);
		result.setImgUrl(image);
		
		result.setIdNo(memberActTrx.getId().toString());
		result.setType(memberActTrx.getTitle());
		return ResponseData.success("查询详情成功",result);
	}
	
	/**
	 * 获取账户可提现余额
	 * @return
	 */
	@RequestMapping(value = "getCurBalance")
	@ResponseBody
	public ResponseData getCurBalance(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		BigDecimal curBal = memberService.getCulBal(member);
		Map<String, String> data = new HashMap<String, String>();
		data.put("balance", StringUtils.decimalToStr(curBal, 2));
		return ResponseData.success("请求成功",data);
	}
	
	
	/**
	 * 公信堂提现订单提交
	 * @return
	 */
	@RequestMapping(value = "submitCashOrder")
	@ResponseBody
	public ResponseData submitCashOrder(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String amountStr = request.getParameter("amount");
		String pwd = request.getParameter("pwd");
		ResponseData checkPayPwd = memberService.checkPayPwd(pwd, member);
		if(checkPayPwd == null) {
			return ResponseData.error("系统错误，请联系客服处理！");
		}
		if (checkPayPwd.getCode() != 0) {
			return ResponseData.error(checkPayPwd.getMessage());
		}
		MemberCard memberCard = memberCardService.getCardByMember(member);
		if(memberCard == null) {
			return ResponseData.error("您还没有绑定银行卡，暂时不能提现，请先去绑定银行卡！");
		}	
		BigDecimal curBal = memberService.getCulBal(member);
		BigDecimal amount = new BigDecimal(amountStr);
		if (curBal.compareTo(amount) < 0) {
			return ResponseData.error("已超过提现额度");
		}
		int flag = nfsWdrlRecordService.isNeedCheck(amount, member);
		try {
			NfsWdrlRecord wdrlRecord = new NfsWdrlRecord();
			BigDecimal fee = feeRuleService.getFee(TrxRuleConstant.MEMBER_WITHDRAWALS_FEE,null);
			wdrlRecord.setMember(member);
			wdrlRecord.setAmount(amount);
			wdrlRecord.setFee(fee.toString());
			wdrlRecord.setType(NfsWdrlRecord.Type._default);
			wdrlRecord.setBankId(memberCard.getBank().getId());
			wdrlRecord.setBankName(memberCard.getBank().getName());
			wdrlRecord.setCardNo(memberCard.getCardNo());
			if(memberService.lockExists(member)) {
				wdrlRecord.setStatus(Status.retrial);
			}else {
				wdrlRecord.setStatus(NfsWdrlRecord.Status.auditing);
			}
			wdrlRecord.setCheckTime(new Date());
			wdrlRecord.setSource(NfsWdrlRecord.Source.GXT);
			wdrlRecord = nfsWdrlRecordService.withdrawSubmit(wdrlRecord, flag);
			if(wdrlRecord != null && wdrlRecord.getId() != null){
				Map<String, String> data = new HashMap<String,String>();
				String createTime = DateUtils.getDateStr(new Date(), "yyyy-MM-dd HH:mm:ss");
				data.put("amount", StringUtils.decimalToStr(amount, 2));
				data.put("createTime", createTime);
				data.put("tardeType", "提现");
				data.put("tradeResult", "申请成功");
				return ResponseData.success("请求成功",data);
			}else{
				return ResponseData.error("提现申请失败");
			}
			
		} catch (Exception e) {
			//扣款失败
			logger.error(Exceptions.getStackTraceAsString(e));
			return ResponseData.error("提现申请失败");
		}
	}
	
	/**
	 * 获取富友充值信息
	 * @return
	 */
	@RequestMapping(value = "getRechargeOrderData")
	@ResponseBody
	public ResponseData getRechargeOrderData(HttpServletRequest request) {
		String amount = request.getParameter("amount");
		if (StringUtils.isBlank(amount)) {
			return ResponseData.error("操作失败：充值金额为零");
		}
		if (new BigDecimal(amount).doubleValue() < 2) {
			return ResponseData.error("操作失败：充值金额需要大于2元");
		}
		Member member = memberService.getCurrent();
		Integer verifiedList = member.getVerifiedList();
		if (!(VerifiedUtils.isVerified(verifiedList, 1) && VerifiedUtils.isVerified(verifiedList, 2))) {
			return ResponseData.error("操作失败：您尚未进行实名认证，不能进行此操作");
		}
		MemberCard card = memberCardService.getCardByMember(member);
		if (card == null) {
			// 正常情况下不会走到这一步
			return ResponseData.error("操作失败：您尚未绑定银行卡，不能进行此操作");
		}
		NfsRchgRecord rchgRecord = new NfsRchgRecord();
		rchgRecord.setAmount(new BigDecimal(amount));
		rchgRecord.setMember(member);
		rchgRecord.setChannel(NfsRchgRecord.Channel.gxt);
		rchgRecord.setType(NfsRchgRecord.Type.fuiou);
		rchgRecord.setStatus(NfsRchgRecord.Status.wait);
		rchgRecord.setCard(card);
		rchgRecord.setBankName(card.getBank().getName());
		nfsRchgRecordService.save(rchgRecord);

		Payment payment = new Payment();
		payment.setType(Payment.Type.recharge);
		payment.setPrincipalId(member.getId());
		payment.setChannel(Payment.Channel.wyjt);
		payment.setOrgId(rchgRecord.getId());
		payment.setType(Payment.Type.recharge);
		payment.setStatus(Payment.Status.wait);
		payment.setPaymentFee(new BigDecimal("0.00"));
		payment.setPaymentPluginId(fyPaymentPlugin.getId());
		payment.setPaymentPluginName(fyPaymentPlugin.getName());
		PluginConfig config = fyPaymentPlugin.getPluginConfig();
		Map<String, String> configAttr = config.getAttributeMap();
		String mchntcd = configAttr.get(FyPaymentPlugin.PAYMENT_MCHNTCD_ATTRIBUTE_NAME);
		String key = configAttr.get(FyPaymentPlugin.PAYMENT_KEY_ATTRIBUTE_NAME);
		payment.setMchId(mchntcd);
		payment.setPaymentAmount(new BigDecimal(amount));
		payment.setRemoteAddr(Global.getRemoteAddr(request));
		paymentService.save(payment);
		rchgRecord.setPayment(payment);
		nfsRchgRecordService.save(rchgRecord);// 更新充值单号

		String mchntorderId = payment.getPaymentNo();
		String name = member.getName();
		String idNo = member.getIdNo();
		String cardNo = card.getCardNo();
		String idType = "0";
		String version = "2.0";
		String type = "10";// 交易类型
		String backUrl = Global.getConfig("domain") + "/callback/fuiou/fuiouPayNotifyForApp";// 回调地址
		String reUrl = FuiouPayMerchantDataBean.FAIL_RETURN_URL;
		String homeUrl = FuiouPayMerchantDataBean.HOME_RETURN_URL;
		String logoTp = "0";
		String userId = member.getId() + "";

		String amt = StringUtils.StrTOInt(StringUtils.decimalToStr(new BigDecimal(amount), 2)) + "";
		StringBuffer bf = new StringBuffer();
		bf.append("<ORDER>");

		bf.append("<VERSION>");
		bf.append(version);
		bf.append("</VERSION>");

		bf.append("<LOGOTP>");
		bf.append(logoTp);
		bf.append("</LOGOTP>");

		bf.append("<MCHNTCD>");
		bf.append(mchntcd);
		bf.append("</MCHNTCD>");

		bf.append("<TYPE>");
		bf.append(type);
		bf.append("</TYPE>");

		bf.append("<MCHNTORDERID>");
		bf.append(mchntorderId + "");
		bf.append("</MCHNTORDERID>");

		bf.append("<USERID>");
		bf.append(userId);
		bf.append("</USERID>");

		bf.append("<AMT>");
		bf.append(amt);
		bf.append("</AMT>");

		bf.append("<BANKCARD>");
		bf.append(cardNo);
		bf.append("</BANKCARD>");

		bf.append("<NAME>");
		bf.append(name);
		bf.append("</NAME>");

		bf.append("<IDTYPE>");
		bf.append("0");
		bf.append("</IDTYPE>");

		bf.append("<IDNO>");
		bf.append(idNo);
		bf.append("</IDNO>");

		bf.append("<BACKURL>");
		bf.append(backUrl);
		bf.append("</BACKURL>");

		bf.append("<HOMEURL>");
		bf.append(homeUrl);
		bf.append("</HOMEURL>");

		bf.append("<REURL>");
		bf.append(reUrl);
		bf.append("</REURL>");

		bf.append("<SIGNTP>");
		bf.append("md5");
		bf.append("</SIGNTP>");

		String signM = type + "|" + version + "|" + mchntcd + "|" + mchntorderId + "|" + userId + "|" + amt + "|"
				+ cardNo + "|" + backUrl + "|" + name + "|" + idNo + "|" + idType + "|" + logoTp + "|" + homeUrl + "|"
				+ reUrl + "|" + key;
		
		signM = MD5Code.MD5Encode(signM, "UTF-8");

		bf.append("<SIGN>");
		bf.append(signM);
		bf.append("</SIGN>");
		bf.append("</ORDER>");
		String xml = bf.toString();
		logger.info("富友充值单号[{}]待发送的数据[{}]", payment.getPaymentNo(), xml);
		Map<String, String> data = new HashMap<String,String>();
		try {
			String fm = DESCoderFUIOU.desEncrypt(xml, DESCoderFUIOU.getKeyLength8(key));
			data.put("FM", fm);
			data.put("VERSION", version);
			data.put("ENCTP", "1");
			data.put("LOGOTP", logoTp);
			data.put("MCHNTCD", mchntcd);
			data.put("MCHNTCD", mchntcd);
			data.put("requestUrl", FuiouPayMerchantDataBean.PAY_REQUEST_URL);
		} catch (Exception e) {
			logger.error("富友充值报文加密异常：{}",Exceptions.getStackTraceAsString(e));
		}
		return ResponseData.success("请求成功",data);
	}
	
	@RequestMapping(value="queryRechargeOrder")
	@ResponseBody
	public ResponseData queryRechargeOrder(HttpServletRequest	request) {
		String orderId = request.getParameter("orderId");
		Payment payment = paymentService.getByPaymentNo(orderId);
		if(payment == null) {
			return ResponseData.error("没有对应的支付单");
		}
		NfsRchgRecord rchgRecord = nfsRchgRecordService.get(payment.getOrgId());
		if(rchgRecord == null) {
			return ResponseData.error("没有找到对应的充值记录");
		}
		Member member = memberService.getCurrent2();
		if(!rchgRecord.getMember().getId().equals(member.getId())) {
			logger.error("接口可能被攻击！");
			return ResponseData.error("没有权限查看该记录");
		}
		Map<String, String> data = new HashMap<String,String>();
		data.put("amount", StringUtils.decimalToStr(rchgRecord.getAmount(), 2));
		data.put("tradeTime", DateUtils.getDateStr(rchgRecord.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
		return ResponseData.success("请求成功",data);
	}
	
	/**
	 * lianlian充值-获取lianlian充值参数
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="gainLianlianRechargeDataForH5")
	@ResponseBody
	public ResponseData gainLianlianRechargeDataForH5(HttpServletRequest request) {
		String amount = request.getParameter("amount");
		if(StringUtils.isBlank(amount)) {
			return ResponseData.error("操作失败：充值金额为零");
		}
		if (new BigDecimal(amount).doubleValue() < 2) {
			return ResponseData.error("操作失败：充值金额需要大于2元");
		}
		
		Member member = memberService.getCurrent();
		Integer verifiedList = member.getVerifiedList();
		if(!(VerifiedUtils.isVerified(verifiedList, 1) && VerifiedUtils.isVerified(verifiedList, 2))) {
			return ResponseData.error("操作失败：您尚未进行实名认证，不能进行此操作");
		}
		MemberCard card = memberCardService.getCardByMember(member);
		if(card == null) {
			return ResponseData.error("操作失败：您尚未绑定银行卡，不能进行此操作");
		}
		
		NfsRchgRecord rchgRecord = new NfsRchgRecord();
		rchgRecord.setAmount(new BigDecimal(amount));
		rchgRecord.setMember(member);
		rchgRecord.setChannel(NfsRchgRecord.Channel.app);
		rchgRecord.setType(NfsRchgRecord.Type.lianlian);
		rchgRecord.setStatus(NfsRchgRecord.Status.wait);
		rchgRecord.setCard(card);
		rchgRecord.setBankName(card.getBank().getName());
		nfsRchgRecordService.save(rchgRecord);
		
		Payment payment = new Payment();
		payment.setType(Payment.Type.recharge);
		payment.setPrincipalId(member.getId());
		payment.setChannel(Payment.Channel.wyjt);
		payment.setOrgId(rchgRecord.getId());
		payment.setType(Payment.Type.recharge);
		payment.setStatus(Payment.Status.wait);
		payment.setPaymentFee(new BigDecimal("0.00"));
		payment.setPaymentPluginId(lianlianPaymentPlugin.getId());
		payment.setPaymentPluginName(lianlianPaymentPlugin.getName());
		PluginConfig config = lianlianPaymentPlugin.getPluginConfig();
		Map<String,String> configAttr = config.getAttributeMap();
		String oid_partner = configAttr.get(LianlianPaymentPlugin.PAYMENT_OID_PARTNER_ATTRIBUTE_NAME);
		String key = configAttr.get(LianlianPaymentPlugin.PAYMENT_BUSINESS_PRIVATE_KEY_ATTRIBUTE_NAME);
		payment.setMchId(oid_partner);
		payment.setPaymentAmount(new BigDecimal(amount));
		payment.setRemoteAddr(Global.getRemoteAddr(request));
		paymentService.save(payment);
		rchgRecord.setPayment(payment);
		//更新充值单号
		nfsRchgRecordService.save(rchgRecord);
		
		TreeMap<String, String> paramMap = new TreeMap<String,String>();
		paramMap.put("oid_partner", oid_partner);
		paramMap.put("no_order", payment.getPaymentNo());
		paramMap.put("dt_order", DateUtils.getAllNumTime(rchgRecord.getCreateTime()));
		paramMap.put("user_id", member.getId().toString());
		paramMap.put("api_version", "1.0");
		paramMap.put("sign_type", "RSA");
		paramMap.put("time_stamp", DateUtils.getAllNumTime());
		paramMap.put("busi_partner", "101001");
		paramMap.put("notify_url", Global.getConfig("domain") + "/callback/lianlian/rechargeNotify");
		paramMap.put("money_order", amount);
		//支付产品标识 5：新认证收款
		paramMap.put("flag_pay_product", "5");
		//应用渠道标识 3：H5
		paramMap.put("flag_chnl", "3");
		paramMap.put("id_type", "0");
		paramMap.put("id_no", member.getIdNo());
		paramMap.put("acct_name", member.getName());
		paramMap.put("card_no", card.getCardNo());
		
		String risk_item = "";
		JSONObject risk_itemJson = new JSONObject();
		//商品类目
		risk_itemJson.put("frms_ware_category", "2012");
		risk_itemJson.put("user_info_mercht_userno", member.getId().toString());
		risk_itemJson.put("user_info_bind_phone", member.getUsername());
		risk_itemJson.put("user_info_dt_register", DateUtils.getAllNumTime(member.getCreateTime()));
		risk_itemJson.put("goods_name", "用户充值");
		risk_itemJson.put("user_info_full_name", member.getName());
		risk_itemJson.put("user_info_id_no", member.getIdNo());
		risk_itemJson.put("user_info_identify_state", "1");
		//实名认证方式 1：银行卡认证 2：现场认证  3：身份证远程认证 4：其它认证 
		risk_itemJson.put("user_info_identify_type", "3");
		risk_itemJson.put("user_info_id_type", "0");
		risk_item = risk_itemJson.toString();
		paramMap.put("risk_item", risk_item);
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for (Map.Entry<String, String> entry : paramMap.entrySet()) {
			count++;
			sb.append(entry.getKey() + "=" + entry.getValue());
			if(count != paramMap.size()) {
				sb.append("&");
			}
		}
		String signStr = sb.toString();
		String sign = TraderRSAUtil.sign(key, signStr);
		paramMap.put("sign", sign);
		try {
			String response = HttpUtils.doPostForWithdraw(LianlianMerchantDataBean.PAY_REQUEST_URL,JSONObject.toJSONString(paramMap).toString());
			JSONObject resp = JSONObject.parseObject(response);
			String ret_code = resp.getString("ret_code");
			if(StringUtils.equals(ret_code, "0000")) {
				String gateway_url = resp.getString("gateway_url");
				Map<String, String> data = new HashMap<String,String>();
				data.put("gatewayUrl", gateway_url);
				return ResponseData.success("获取成功",data);
			}else {
				logger.error("连连充值请求返回失败：{}",response);
				return ResponseData.error(resp.getString("ret_msg"));
			}
		} catch (Exception e) {
			logger.error("会员：{}连连充值请求异常：{}",member.getId(),Exceptions.getStackTraceAsString(e));
			return ResponseData.error("网络错误");
		}
	}
}