package com.jxf.svc.plugin.lianlianPayment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jxf.svc.plugin.PaymentPlugin;
import com.jxf.svc.plugin.PluginConfig;
import com.jxf.svc.plugin.PluginConfigAttr;
import com.jxf.svc.plugin.PluginConfigAttr.ShowType;
import com.jxf.svc.plugin.utils.PluginUtils;


/** 
 * @类功能说明：连连支付插件
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：suhuimin 
 * @创建时间：2018年9月28日 下午3:40:28 
 * @版本：V1.0 
 */
@Component("lianlianPaymentPlugin")
public class LianlianPaymentPlugin extends PaymentPlugin {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/** 商户号1*/
	public static final String PAYMENT_OID_PARTNER_1_ATTRIBUTE_NAME = "oidPartner1";
	/** 商户私钥1*/
	public static final String PAYMENT_BUSINESS_PRIVATE_KEY_1_ATTRIBUTE_NAME = "privateKey1";
	/** WEB收银台返回地址1*/
	public static final String PAYMENT_WEBPAY_RETURN_URL_1_ATTRIBUTE_NAME = "returnUrl1";
	/** WEB收银台回调地址1*/
	public static final String PAYMENT_WEBPAY_CALLBACK_URL_1_ATTRIBUTE_NAME = "callbackUrl1";


	
	/** 商户号*/
	public static final String PAYMENT_OID_PARTNER_ATTRIBUTE_NAME = "oidPartner";
	/** 商户私钥*/
	public static final String PAYMENT_BUSINESS_PRIVATE_KEY_ATTRIBUTE_NAME = "privateKey";
	/** 版本号*/
	public static final String PAYMENT_API_VERSION_ATTRIBUTE_NAME = "version";
	/** 签名方式*/
	public static final String PAYMENT_SIGN_TYPE_ATTRIBUTE_NAME = "signType";
	/** 小程序域名*/
	public static final String PAYMENT_MINIPROHOST_ATTRIBUTE_NAME = "miniproHost";
	/** 连连WAP提交地址*/
	public static final String PAYMENT_LIANLIANWAP_ATTRIBUTE_NAME = "lianlianWap";
	/** 连连银通公钥*/
	public static final String PAYMENT_LLPUBLIC_KEY_ATTRIBUTE_NAME = "llPublicKey";
	// 业务类型，连连支付根据商户业务为商户开设的业务类型； （101001：虚拟商品销售、109001：实物商品销售、108001：外部账户充值）
	public static final String PAYMENT_BUSI_PARTNER_ATTRIBUTE_NAME = "busiPartner";
	/** 用户已绑定银行卡列表查询*/
	public static final String PAYMENT_QUERY_USER_BANKCARD_URL_ATTRIBUTE_NAME = "queryUserBankCardUrl";
	/** 连连支付WEB收银台认证支付服务地址*/
	public static final String PAYMENT_WEBPAY_URL_ATTRIBUTE_NAME = "webPayUrl";
	/** 银行卡卡bin信息查询*/
	public static final String PAYMENT_QUERY_BANKCARD_URL_ATTRIBUTE_NAME = "queryCardBin";
	/** 付款交易请求地址*/
	public static final String PAYMENT_PAYMENT_ATTRIBUTE_NAME = "paymentRequestUrl";
	/** 确认付款请求地址*/
	public static final String PAYMENT_CONFIRMPAYMENT_ATTRIBUTE_NAME = "confirmPaymentRequestUrl";
	/** 付款结果查询请求地址*/
	public static final String PAYMENT_QUERYPAYMENT_ATTRIBUTE_NAME = "queryPayment";
	//mdk 钥匙
	public static final String PAYMENT_MD5_KEY_ATTRIBUTE_NAME = "mdk";
	//商户账户查询接口地址
	public static final String PAYMENT_QUERYTRADERACCOUNT_ATTRIBUTE_NAME = "queryTraderAccount";
	
	
	@Override
	public String getName() {
		return "连连支付";
	}
	
	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public RequestMethod getRequestMethod() {
		return RequestMethod.post;
	}
	@Override
	public String getRequestCharset() {
		return "utf-8";
	}



	@Override
	public String getAuthor() {

		return null;
	}

	@Override
	public String getSiteUrl() {

		return null;
	}

	@Override
	public String getInstallUrl() {

		return null;
	}

	@Override
	public String getUninstallUrl() {

		return null;
	}

	@Override
	public String getSettingUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getParameterMap(String sn, String description, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getSn(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNotifyMessage(NotifyMethod notifyMethod, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 获取配置属性
	 */
	public  List<PluginConfigAttr> getPluginConfigAttrs(){
		pluginConfigAttrs = new ArrayList<PluginConfigAttr>();
		pluginConfigAttrs = super.getPluginConfigAttrs();
		PluginConfigAttr oidPartnerAttr = new PluginConfigAttr(PAYMENT_OID_PARTNER_ATTRIBUTE_NAME, "商户号", true, 11);
		pluginConfigAttrs.add(oidPartnerAttr);
		PluginConfigAttr versionAttr = new PluginConfigAttr(PAYMENT_API_VERSION_ATTRIBUTE_NAME, "版本号", true, 13);
		pluginConfigAttrs.add(versionAttr);
		PluginConfigAttr paymentRequestUrlAttr = new PluginConfigAttr(PAYMENT_PAYMENT_ATTRIBUTE_NAME, "付款交易请求地址", true, 41);
		pluginConfigAttrs.add(paymentRequestUrlAttr);
		PluginConfigAttr confirmPaymentAttr = new PluginConfigAttr(PAYMENT_CONFIRMPAYMENT_ATTRIBUTE_NAME, "确认付款请求地址", true, 42);
		pluginConfigAttrs.add(confirmPaymentAttr);
		PluginConfigAttr queryPaymentAttr = new PluginConfigAttr(PAYMENT_QUERYPAYMENT_ATTRIBUTE_NAME, "付款结果查询请求地址", true, 43);
		pluginConfigAttrs.add(queryPaymentAttr);
		PluginConfigAttr signTypeAttr = new PluginConfigAttr(PAYMENT_SIGN_TYPE_ATTRIBUTE_NAME, "签名方式", true, 44);
		pluginConfigAttrs.add(signTypeAttr);
		PluginConfigAttr miniproHostAttr = new PluginConfigAttr(PAYMENT_MINIPROHOST_ATTRIBUTE_NAME, "小程序域名", true, 45);
		pluginConfigAttrs.add(miniproHostAttr);
		PluginConfigAttr lianlianWapAttr = new PluginConfigAttr(PAYMENT_LIANLIANWAP_ATTRIBUTE_NAME, "连连WAP提交地址", true, 46);
		pluginConfigAttrs.add(lianlianWapAttr);
		PluginConfigAttr busiPartnerAttr = new PluginConfigAttr(PAYMENT_BUSI_PARTNER_ATTRIBUTE_NAME, "业务类型", true, 47);
		pluginConfigAttrs.add(busiPartnerAttr);
		PluginConfigAttr queryUserBankCardUrlAttr = new PluginConfigAttr(PAYMENT_QUERY_USER_BANKCARD_URL_ATTRIBUTE_NAME, "用户已绑定银行卡列表查询", true, 48);
		pluginConfigAttrs.add(queryUserBankCardUrlAttr);
		PluginConfigAttr webPayUrlAttr = new PluginConfigAttr(PAYMENT_WEBPAY_URL_ATTRIBUTE_NAME, "连连支付WEB收银台认证支付服务地址", true, 49);
		pluginConfigAttrs.add(webPayUrlAttr);
		PluginConfigAttr queryCardBinAttr = new PluginConfigAttr(PAYMENT_QUERY_BANKCARD_URL_ATTRIBUTE_NAME, "银行卡卡bin信息查询", true, 50);
		pluginConfigAttrs.add(queryCardBinAttr);
		PluginConfigAttr mdkAttr = new PluginConfigAttr(PAYMENT_MD5_KEY_ATTRIBUTE_NAME, "mdk钥匙", true, 51);
		pluginConfigAttrs.add(mdkAttr);
		PluginConfigAttr publicKeyAttr = new PluginConfigAttr(PAYMENT_LLPUBLIC_KEY_ATTRIBUTE_NAME, "连连公钥", true, 52,ShowType.TEXTAREA);
		PluginConfigAttr privateKeyAttr = new PluginConfigAttr(PAYMENT_BUSINESS_PRIVATE_KEY_ATTRIBUTE_NAME, "密钥", true, 12,ShowType.TEXTAREA);
		pluginConfigAttrs.add(privateKeyAttr);
		pluginConfigAttrs.add(publicKeyAttr);

		PluginConfigAttr queryTraderAccountUrl = new PluginConfigAttr(PAYMENT_QUERYTRADERACCOUNT_ATTRIBUTE_NAME, "商户账户查询地址", true, 51);
		pluginConfigAttrs.add(queryTraderAccountUrl);
		
		PluginConfigAttr oidPartnerAttr1 = new PluginConfigAttr(PAYMENT_OID_PARTNER_1_ATTRIBUTE_NAME, "商户号1", true, 110);
		PluginConfigAttr privateKeyAttr1 = new PluginConfigAttr(PAYMENT_BUSINESS_PRIVATE_KEY_1_ATTRIBUTE_NAME, "密钥1", true, 120,ShowType.TEXTAREA);
		PluginConfigAttr returnUrl1 = new PluginConfigAttr(PAYMENT_WEBPAY_RETURN_URL_1_ATTRIBUTE_NAME, "WEB收银台返回地址1", true, 490);
		PluginConfigAttr callbackUrl1 = new PluginConfigAttr(PAYMENT_WEBPAY_CALLBACK_URL_1_ATTRIBUTE_NAME, "WEB收银台回调地址1", true, 491);

		pluginConfigAttrs.add(oidPartnerAttr1);
		pluginConfigAttrs.add(privateKeyAttr1);
		pluginConfigAttrs.add(returnUrl1);
		pluginConfigAttrs.add(callbackUrl1);
		
		Collections.sort(pluginConfigAttrs);
		PluginConfig config = getPluginConfig();
		Map<String,String> attributesMap =config.getAttributeMap();
		for (PluginConfigAttr pluginConfigAttr : pluginConfigAttrs) {
			pluginConfigAttr.setValue(attributesMap.get(pluginConfigAttr.getField()));
		}
		return pluginConfigAttrs;
	}
	
	@Override
	public boolean verifyNotify(PaymentPlugin.NotifyMethod notifyMethod, HttpServletRequest request) {
		PluginConfig config = getPluginConfig();
		String accountId = config.getAttribute(PAYMENT_OID_PARTNER_ATTRIBUTE_NAME);
		String key = config.getAttribute(PAYMENT_BUSINESS_PRIVATE_KEY_ATTRIBUTE_NAME);
		Map<String, String> parameterMap = new LinkedHashMap<String, String>();
		parameterMap.put("accountId", accountId);
		parameterMap.put("orderId", request.getParameter("orderId"));
		parameterMap.put("amount", request.getParameter("amount"));
		parameterMap.put("result_code", request.getParameter("result_code"));
		parameterMap.put("result_msg", request.getParameter("result_msg"));
		String signStr = PluginUtils.joinKeyValue(parameterMap, null, "&key=" + key, "&", false, "purpose");
		logger.info("签名字符串={}",signStr);
		String mac = DigestUtils.md5Hex(signStr).toUpperCase();
		if(mac.equals(request.getParameter("mac")) && "0000".equals(request.getParameter("result_code"))){
			return true;
		}
		return false;
	}
	
}
