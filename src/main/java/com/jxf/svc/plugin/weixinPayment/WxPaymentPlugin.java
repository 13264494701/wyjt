package com.jxf.svc.plugin.weixinPayment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
 * @类功能说明： 微信支付插件
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：huojiabao 
 * @创建时间：2016年8月9日 下午3:40:28 
 * @版本：V1.0 
 */
@Component("wxPaymentPlugin")
public class WxPaymentPlugin extends PaymentPlugin {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/** 商户号*/
	public static final String PAYMENT_ACCTID_ATTRIBUTE_NAME = "accountId";
	/** 密钥*/
	public static final String PAYMENT_KEY_ATTRIBUTE_NAME = "key";
	/** 微信退款请求接口地址*/
	public static final String PAYMENT_REFUND_REQUEST_URL_ATTRIBUTE_NAME = "refundUrl";
	
	@Override
	public String getName() {
		return "微信支付";
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
		PluginConfigAttr accountIdAttr = new PluginConfigAttr(PAYMENT_ACCTID_ATTRIBUTE_NAME, "商户号", true, 30);
		pluginConfigAttrs.add(accountIdAttr);
		PluginConfigAttr keyAttr = new PluginConfigAttr(PAYMENT_KEY_ATTRIBUTE_NAME, "密钥", true, 40,ShowType.PASSWORD);
		pluginConfigAttrs.add(keyAttr);
		PluginConfigAttr refundRequestUrlAttr = new PluginConfigAttr(PAYMENT_REFUND_REQUEST_URL_ATTRIBUTE_NAME, "退款请求地址", true, 16);
		pluginConfigAttrs.add(refundRequestUrlAttr);
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
		String accountId = config.getAttribute(PAYMENT_ACCTID_ATTRIBUTE_NAME);
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
	
	/**
	 * 生成签名
	 * 
	 * @param parameterMap
	 *            参数
	 * @return 签名
	 */
	private String generateSign(Map<String, ?> parameterMap) {
		PluginConfig pluginConfig = getShopPluginConfig();
		return DigestUtils.md5Hex(joinKeyValue(new TreeMap<String, Object>(parameterMap), null, "&key=" + pluginConfig.getAttribute("key"), "&", true, "sign")).toUpperCase();
	}

}
