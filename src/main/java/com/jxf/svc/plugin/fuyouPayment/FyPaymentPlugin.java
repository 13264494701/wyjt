package com.jxf.svc.plugin.fuyouPayment;

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
 * @类功能说明： 富友支付插件
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：suhuimin 
 * @创建时间：2018年9月27日 下午3:40:28 
 * @版本：V1.0 
 */
@Component("fyPaymentPlugin")
public class FyPaymentPlugin extends PaymentPlugin {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/** 商户号*/
	public static final String PAYMENT_MCHNTCD_ATTRIBUTE_NAME = "MCHNTCD";
	/** 生产密钥*/
	public static final String PAYMENT_KEY_ATTRIBUTE_NAME = "key";
	/** 发送短信验证码接口请求地址*/
	public static final String PAYMENT_BINDMSG_REQUEST_URL_ATTRIBUTE_NAME = "bindMsgUrl";
	/** 协议卡绑定接口请求地址*/
	public static final String PAYMENT_BINDCOMMIT_REQUEST_URL_ATTRIBUTE_NAME = "bindCommitUrl";
	/** 协议卡解除绑定接口请求地址*/
	public static final String PAYMENT_UNBIND_REQUEST_URL_ATTRIBUTE_NAME = "unbindUrl";
	/** 协议卡查询接口请求地址*/
	public static final String PAYMENT_BINDQUERY_REQUEST_URL_ATTRIBUTE_NAME = "bindQueryUrl";
	/** 协议支付接口请求地址*/
	public static final String PAYMENT_ORDER_REQUEST_URL_ATTRIBUTE_NAME = "orderUrl";
	/** 订单结果查询(富友订单号)接口请求地址*/
	public static final String PAYMENT_QUERYORDER_REQUEST_URL_ATTRIBUTE_NAME = "queryOrderUrl";
	/** 订单结果查询(商户订单号)接口请求地址*/
	public static final String PAYMENT_CHECKRESULT_REQUEST_URL_ATTRIBUTE_NAME = "checkResultUrl";
	/** 商户支持卡bin接口请求地址*/
	public static final String PAYMENT_CARDBINQUERY_REQUEST_URL_ATTRIBUTE_NAME = "cardBinQueryUrl";
	/** 富友公钥*/
	public static final String PAYMENT_PUBLICKEY_ATTRIBUTE_NAME = "publicKey";
	/** 笔笔验订单结果查询(商户订单号)接口请求地址*/
	public static final String PAYMENT_BIBIYANCHECKRESULT_REQUEST_URL_ATTRIBUTE_NAME = "bibiyanCheckResultUrl";
	/** 商户账户详情查询接口请求地址*/
	public static final String PAYMENT_QUERYTRADERACCOUNT_REQUEST_URL_ATTRIBUTE_NAME = "queryTraderAccount";
	/** 商户密钥2*/
	public static final String PAYMENT_MCTKEY_REQUEST_URL_ATTRIBUTE_NAME = "mctKey";
	
	@Override
	public String getName() {
		return "富友支付";
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
		PluginConfigAttr mchntcdAttr = new PluginConfigAttr(PAYMENT_MCHNTCD_ATTRIBUTE_NAME, "商户号", true, 11);
		pluginConfigAttrs.add(mchntcdAttr);
		PluginConfigAttr keyAttr = new PluginConfigAttr(PAYMENT_KEY_ATTRIBUTE_NAME, "密钥", true, 12,ShowType.PASSWORD);
		pluginConfigAttrs.add(keyAttr);
		PluginConfigAttr bindMsgRequestUrlAttr = new PluginConfigAttr(PAYMENT_BINDMSG_REQUEST_URL_ATTRIBUTE_NAME, "短信验证码接口请求地址", true, 40);
		pluginConfigAttrs.add(bindMsgRequestUrlAttr);
		PluginConfigAttr bindCommitRequestUrlAttr = new PluginConfigAttr(PAYMENT_BINDCOMMIT_REQUEST_URL_ATTRIBUTE_NAME, "协议卡绑定接口请求地址", true, 41);
		pluginConfigAttrs.add(bindCommitRequestUrlAttr);
		PluginConfigAttr unbindRequestUrlAttr = new PluginConfigAttr(PAYMENT_UNBIND_REQUEST_URL_ATTRIBUTE_NAME, "协议卡解绑接口请求地址", true, 42);
		pluginConfigAttrs.add(unbindRequestUrlAttr);
		PluginConfigAttr bindQueryRequestUrlAttr = new PluginConfigAttr(PAYMENT_BINDQUERY_REQUEST_URL_ATTRIBUTE_NAME, "协议卡查询接口请求地址", true, 43);
		pluginConfigAttrs.add(bindQueryRequestUrlAttr);
		PluginConfigAttr orderRequestUrlAttr = new PluginConfigAttr(PAYMENT_ORDER_REQUEST_URL_ATTRIBUTE_NAME, "协议支付接口请求地址", true, 44);
		pluginConfigAttrs.add(orderRequestUrlAttr);
		PluginConfigAttr queryOrderRequestUrlAttr = new PluginConfigAttr(PAYMENT_QUERYORDER_REQUEST_URL_ATTRIBUTE_NAME, "订单结果查询(富友订单号)接口请求地址", true, 45);
		pluginConfigAttrs.add(queryOrderRequestUrlAttr);
		PluginConfigAttr checkResultRequestUrlAttr = new PluginConfigAttr(PAYMENT_CHECKRESULT_REQUEST_URL_ATTRIBUTE_NAME, "订单结果查询(商户订单号)接口请求地址", true, 46);
		pluginConfigAttrs.add(checkResultRequestUrlAttr);
		PluginConfigAttr cardBinQueryRequestUrlAttr = new PluginConfigAttr(PAYMENT_CARDBINQUERY_REQUEST_URL_ATTRIBUTE_NAME, "商户支持卡bin接口请求地址", true, 47);
		pluginConfigAttrs.add(cardBinQueryRequestUrlAttr);
		PluginConfigAttr publicKeyAttr = new PluginConfigAttr(PAYMENT_PUBLICKEY_ATTRIBUTE_NAME, "富友公钥", true, 48,ShowType.TEXTAREA);
		pluginConfigAttrs.add(publicKeyAttr);
		PluginConfigAttr bibiyanResultAttr = new PluginConfigAttr(PAYMENT_BIBIYANCHECKRESULT_REQUEST_URL_ATTRIBUTE_NAME, "笔笔验订单查询接口地址（商户订单号）", true, 49,ShowType.TEXTAREA);
		pluginConfigAttrs.add(bibiyanResultAttr);
		PluginConfigAttr queryTraderAccountUrl = new PluginConfigAttr(PAYMENT_QUERYTRADERACCOUNT_REQUEST_URL_ATTRIBUTE_NAME, "商户账户查询", true, 50,ShowType.TEXTAREA);
		pluginConfigAttrs.add(queryTraderAccountUrl);
		PluginConfigAttr mctKey2 = new PluginConfigAttr(PAYMENT_MCTKEY_REQUEST_URL_ATTRIBUTE_NAME, "商户密钥2", true, 51,ShowType.TEXTAREA);
		pluginConfigAttrs.add(mctKey2);
		
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
		String accountId = config.getAttribute(PAYMENT_MCHNTCD_ATTRIBUTE_NAME);
		String key = config.getAttribute(PAYMENT_KEY_ATTRIBUTE_NAME);
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
