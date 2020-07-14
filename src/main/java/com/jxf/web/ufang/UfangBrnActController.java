package com.jxf.web.ufang;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.payment.entity.Payment;
import com.jxf.payment.service.PaymentService;
import com.jxf.pwithdraw.utils.SignUtil;
import com.jxf.svc.config.Global;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.plugin.PluginConfig;
import com.jxf.svc.plugin.lianlianPayment.LianlianPaymentPlugin;
import com.jxf.ufang.entity.LianLianPay;
import com.jxf.ufang.entity.UfangBrnAct;
import com.jxf.ufang.entity.UfangRchgRecord;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.service.UfangBrnActService;

import com.jxf.ufang.service.UfangRchgRecordService;
import com.jxf.ufang.util.UfangUserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 机构账户Controller
 * @author jinxinfu
 * @version 2018-06-29
 */
@Controller("ufangBrnActController")
@RequestMapping(value = "${ufangPath}/brnAct")
public class UfangBrnActController extends UfangBaseController {

	@Autowired
	private UfangBrnActService brnActService;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private UfangRchgRecordService ufangRchgRecordService;
	@Autowired
	private LianlianPaymentPlugin lianlianPaymentPlugin;
	
	@ModelAttribute
	public UfangBrnAct get(@RequestParam(required=false) Long id) {
		UfangBrnAct entity = null;
		if (id!=null){
			entity = brnActService.get(id);
		}
		if (entity == null){
			entity = new UfangBrnAct();
		}
		return entity;
	}

	@RequiresPermissions("ufang:brnAct:view")
	@RequestMapping(value = {"list", ""})
	public String list(UfangBrnAct ufangBrnAct, HttpServletRequest request, HttpServletResponse response, Model model) {
		ufangBrnAct.getSqlMap().put("dsf", UfangUserUtils.dataScopeFilter("c", ""));
		Page<UfangBrnAct> page = brnActService.findPage(new Page<UfangBrnAct>(request, response), ufangBrnAct); 
		model.addAttribute("page", page);
		return "ufang/brn/act/brnActList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("ufang:brnAct:view")
	@RequestMapping(value = "add")
	public String add(UfangBrnAct nfsBrnAct, Model model) {
		model.addAttribute("nfsBrnAct", nfsBrnAct);
		return "brn/nfsBrnActAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("ufang:brnAct:view")
	@RequestMapping(value = "query")
	public String query(UfangBrnAct nfsBrnAct, Model model) {
		model.addAttribute("nfsBrnAct", nfsBrnAct);
		return "brn/nfsBrnActQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("ufang:brnAct:view")
	@RequestMapping(value = "update")
	public String update(UfangBrnAct nfsBrnAct, Model model) {
		model.addAttribute("nfsBrnAct", nfsBrnAct);
		return "brn/nfsBrnActUpdate";
	}
	 


	@RequestMapping(value = "return")
	public String ret(Model model) {

		return "ufang/brn/act/return";
	}

	@RequiresPermissions("ufang:brnAct:view")
	@RequestMapping(value = "recharge")
	public String recharge(Model model) {
		
		model.addAttribute("dataPrice", TrxRuleConstant.UFANG_DATA_MORE_PRICE);
		return "ufang/brn/act/recharge";
	}

    @RequiresPermissions("ufang:brnAct:view")
    @RequestMapping(value = "rechargePage")
    public String rechargePage(String amount, HttpServletRequest request,Model model) {

		PluginConfig config = lianlianPaymentPlugin.getPluginConfig();
		Map<String, String> configAttr = config.getAttributeMap();
		String key = configAttr.get(LianlianPaymentPlugin.PAYMENT_BUSINESS_PRIVATE_KEY_1_ATTRIBUTE_NAME);
		String oidPartner = configAttr.get(LianlianPaymentPlugin.PAYMENT_OID_PARTNER_1_ATTRIBUTE_NAME);
		String returnUrl1 = configAttr.get(LianlianPaymentPlugin.PAYMENT_WEBPAY_RETURN_URL_1_ATTRIBUTE_NAME);
		String callbackUrl1 = configAttr.get(LianlianPaymentPlugin.PAYMENT_WEBPAY_CALLBACK_URL_1_ATTRIBUTE_NAME);
		UfangUser ufangUser = UfangUserUtils.getUser();

		//创建订单
		UfangRchgRecord ufangRchgRecord = new UfangRchgRecord();
		ufangRchgRecord.setUser(ufangUser);
		ufangRchgRecord.setAmount(new BigDecimal(amount));
		ufangRchgRecord.setType(UfangRchgRecord.Type.online);
		ufangRchgRecord.setChannel(UfangRchgRecord.Channel.pc);
		ufangRchgRecord.setStatus(UfangRchgRecord.Status.wait);
		ufangRchgRecordService.save(ufangRchgRecord);


		Payment payment = new Payment();
		payment.setChannel(Payment.Channel.ufang);
		payment.setType(Payment.Type.recharge);
		payment.setPrincipalId(ufangUser.getId());
		payment.setOrgId(ufangRchgRecord.getId());
		payment.setType(Payment.Type.recharge);
		payment.setStatus(Payment.Status.wait);
		payment.setPaymentFee(new BigDecimal("0.00"));
		payment.setPaymentPluginId(lianlianPaymentPlugin.getId());
		payment.setPaymentPluginName(lianlianPaymentPlugin.getName());
//		payment.setProtocolNo();
		payment.setMchId(oidPartner);
		payment.setPaymentAmount(new BigDecimal(amount));
		payment.setRemoteAddr(Global.getRemoteAddr(request));
		paymentService.save(payment);


		Long orderId = payment.getId();
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String time = dataFormat.format(date);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("frms_ware_category", "2039");
		jsonObject.put("user_info_mercht_userno", ufangUser.getId());
		jsonObject.put("user_info_dt_register", dataFormat.format(ufangUser.getCreateTime()));

		LianLianPay lianLianPay = new LianLianPay();
		lianLianPay.setVersion("1.0");
		//201807090002006830
		lianLianPay.setOid_partner(oidPartner);
		lianLianPay.setUser_id(String.valueOf(ufangUser.getId()));
		lianLianPay.setSign_type("RSA");
		lianLianPay.setBusi_partner("101001");
		lianLianPay.setNo_order(String.valueOf(orderId));
		lianLianPay.setDt_order(time);
		lianLianPay.setName_goods("优放充值");
		lianLianPay.setMoney_order(amount);
		lianLianPay.setNotify_url(callbackUrl1);
		lianLianPay.setUrl_return(returnUrl1);
		lianLianPay.setUserreq_ip(Global.getRemoteAddr(request));
		lianLianPay.setUrl_order("");
		lianLianPay.setInfo_order("");
		lianLianPay.setValid_order("10080");
		lianLianPay.setTimestamp(time);
		lianLianPay.setRisk_item(jsonObject.toJSONString());

		//签名
		//String key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANiVg/cgFu9ckGjKXOyNqjjSLpqXNDgMu4MxyXYNpco43bJE2tBL0LmaqUnrgBTjmhaWsVOg8IY6mddTGlvgrogbHuDeNo9H8khCrijLWe5Zn8fkYCfWESbezPsrrasm96V0t9eSycDeYoYYL6YSFkkmEq1KyWgvoJaGZ0ShF2OHAgMBAAECgYBB3SYmjvGqlQGtfGzJ20L6yKA3jufoa6bSfN+BMSFL4AM7ZUkNsyCkcO4udpmawKRpXiZLAlRi3YtPOgx4CFnKpu7POCTa9ajUnKVzVA26Me5+ln1lPSlQLZR0lHd4BsIkP82t78np2kUWx1oOgRYS7JIu/21BsI+aVpK96CCnsQJBAPVOGA5FhytyKmDSBRJ3G8Jhv02f8M9rwX7zNDGJOnHQjQYoCVIOA7c9pixKZwHw1IZDUWkYxwnt9SSNdRFaJD0CQQDiBtwX2L2UCc+pxy1JBMF8VgwEx+znRYebi8FDGeAYnzVil1YOfF3QtgjxTipx8L1eq/1L3PS4CV+qSFKYNa8TAkAlF9f/YHgeE7dgyKFHt4cVD57T6BmL4+lwfuoni26xAy4v/iHarI/XR5U3IVEONBKO8uL+l2aRk8/75QAMzaqpAkEA0oKtDhCxUGVq4ac+CBnD0veZLgK+JA/f4wDguIwq6QOvBuoIHmm6Pp6r6YJxibk7xibNPJDH43fnf4LoWhb1vQJBAJ7yoLgjnSHpuINynFAqTHc0aO9zT3BI0s6KLm1sQ56ac6X1ICH7nJ2Rt0qVYcYX+YTaRBFYGj0Bka4SdhDq8rg=";
		lianLianPay.setSign(SignUtil.genRSASign(JSON.parseObject(JSON.toJSONString(lianLianPay)),key));

		model.addAttribute("lianLianPay", lianLianPay);

		return "ufang/brn/act/lianlianpay";
	}
}