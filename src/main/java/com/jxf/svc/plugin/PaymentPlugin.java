package com.jxf.svc.plugin;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jxf.payment.entity.Payment;
import com.jxf.payment.service.PaymentService;
import com.jxf.svc.config.Setting;
import com.jxf.svc.utils.SystemUtils;


/**
 * Plugin - 支付
 * 
 * @author JINXINFU
 * @version 2.0
 */
public abstract class PaymentPlugin  extends Plugin {

	/** "支付方式名称"属性名称 */
	public static final String PAYMENT_NAME_ATTRIBUTE_NAME = "paymentName";
	/** "请求接口地址"属性名称 */
	public static final String REQUEST_URL_ATTRIBUTE_NAME = "requestUrl";

	/** "手续费类型"属性名称 */
	public static final String FEE_TYPE_ATTRIBUTE_NAME = "feeType";

	/** "手续费"属性名称 */
	public static final String FEE_ATTRIBUTE_NAME = "fee";

	/** "LOGO"属性名称 */
	public static final String LOGO_ATTRIBUTE_NAME = "logo";

	/** "描述"属性名称 */
	public static final String DESCRIPTION_ATTRIBUTE_NAME = "description";

	/**
	 * 手续费类型
	 */
	public enum FeeType {

		/** 按比例收费 */
		scale,

		/** 固定收费 */
		fixed
	}

	/**
	 * 请求方法
	 */
	public enum RequestMethod {

		/** POST */
		post,

		/** GET */
		get
	}

	/**
	 * 通知方法
	 */
	public enum NotifyMethod {

		/** 通用 */
		general,

		/** 同步 */
		sync,

		/** 异步 */
		async
	}

	@Autowired
	private PluginConfigService pluginConfigService;
	@Autowired
	private PaymentService paymentService;

	/**
	 * 获取ID
	 * 
	 * @return ID
	 */
	public String getId() {
		return getClass().getAnnotation(Component.class).value();
	}

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	public abstract String getName();

	/**
	 * 获取版本
	 * 
	 * @return 版本
	 */
	public abstract String getVersion();

	/**
	 * 获取作者
	 * 
	 * @return 作者
	 */
	public abstract String getAuthor();

	/**
	 * 获取网址
	 * 
	 * @return 网址
	 */
	public abstract String getSiteUrl();

	/**
	 * 获取安装URL
	 * 
	 * @return 安装URL
	 */
	public abstract String getInstallUrl();

	/**
	 * 获取卸载URL
	 * 
	 * @return 卸载URL
	 */
	public abstract String getUninstallUrl();

	/**
	 * 获取设置URL
	 * 
	 * @return 设置URL
	 */
	public abstract String getSettingUrl();


	/**
	 * 获取插件配置
	 * 
	 * @return 插件配置
	 */
	public PluginConfig getShopPluginConfig() {
		return pluginConfigService.findByPluginId(getId());
	}

	/**
	 * 获取是否已启用
	 * 
	 * @return 是否已启用
	 */
	public boolean getIsEnabled() {
		PluginConfig pluginConfig = getShopPluginConfig();
		return pluginConfig != null ? pluginConfig.getIsEnabled() : false;
	}

	/**
	 * 获取属性值
	 * 
	 * @param name
	 *            属性名称
	 * @return 属性值
	 */
	public String getAttribute(String name) {
		PluginConfig pluginConfig = getShopPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttribute(name) : null;
	}

	/**
	 * 获取排序
	 * 
	 * @return 排序
	 */
	public Integer getOrder() {
		PluginConfig pluginConfig = getShopPluginConfig();
		return pluginConfig != null ? pluginConfig.getDisplayOrder() : null;
	}

	/**
	 * 获取支付方式名称
	 * 
	 * @return 支付方式名称
	 */
	public String getPaymentName() {
		PluginConfig pluginConfig = getShopPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttribute(PAYMENT_NAME_ATTRIBUTE_NAME) : null;
	}

	/**
	 * 获取手续费类型
	 * 
	 * @return 手续费类型
	 */
	public String getFeeType() {
		PluginConfig pluginConfig = getShopPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttribute(FEE_TYPE_ATTRIBUTE_NAME) : null;
	}

	/**
	 * 获取手续费
	 * 
	 * @return 手续费
	 */
	public BigDecimal getFee() {
		PluginConfig pluginConfig = getShopPluginConfig();
		return pluginConfig != null ? new BigDecimal(pluginConfig.getAttribute(FEE_ATTRIBUTE_NAME)) : null;
	}

	/**
	 * 获取LOGO
	 * 
	 * @return LOGO
	 */
	public String getLogo() {
		PluginConfig pluginConfig = getShopPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttribute(LOGO_ATTRIBUTE_NAME) : null;
	}

	/**
	 * 获取描述
	 * 
	 * @return 描述
	 */
	public String getDescription() {
		PluginConfig pluginConfig = getShopPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttribute(DESCRIPTION_ATTRIBUTE_NAME) : null;
	}

	/**
	 * 获取请求URL
	 * 
	 * @return 请求URL
	 */
	public abstract String getRequestUrl();

	/**
	 * 获取请求方法
	 * 
	 * @return 请求方法
	 */
	public abstract PaymentPlugin.RequestMethod getRequestMethod();

	/**
	 * 获取请求字符编码
	 * 
	 * @return 请求字符编码
	 */
	public abstract String getRequestCharset();

	/**
	 * 获取请求参数
	 * 
	 * @param sn
	 *            编号
	 * @param description
	 *            描述
	 * @param request
	 *            HttpServletRequest
	 * @return 请求参数
	 */
	public abstract Map<String, Object> getParameterMap(String sn, String description, HttpServletRequest request);

	/**
	 * 验证通知是否合法
	 * 
	 * @param notifyMethod
	 *            通知方法
	 * @param request
	 *            HttpServletRequest
	 * @return 通知是否合法
	 */
	public abstract boolean verifyNotify(PaymentPlugin.NotifyMethod notifyMethod, HttpServletRequest request);

	/**
	 * 获取编号
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return 编号
	 */
	public abstract String getSn(HttpServletRequest request);

	/**
	 * 获取通知返回消息
	 * 
	 * @param notifyMethod
	 *            通知方法
	 * @param request
	 *            HttpServletRequest
	 * @return 通知返回消息
	 */
	public abstract String getNotifyMessage(PaymentPlugin.NotifyMethod notifyMethod, HttpServletRequest request);

	/**
	 * 计算支付手续费
	 * 
	 * @param amount
	 *            金额
	 * @return 支付手续费
	 */
	public BigDecimal calculateFee(BigDecimal amount) {
		Setting setting = SystemUtils.getSetting();
		if (getFeeType().equals("001")) {
			return setting.setScale(amount.multiply(getFee()));
		} else {
			return setting.setScale(getFee());
		}
	}

	/**
	 * 计算支付金额
	 * 
	 * @param amount
	 *            金额
	 * @return 支付金额
	 */
	public BigDecimal calculateAmount(BigDecimal amount) {
		return amount.add(calculateFee(amount)).setScale(2, RoundingMode.UP);
	}

	/**
	 * 根据编号查找支付记录
	 * 
	 * @param paymentNo
	 *            编号(忽略大小写)
	 * @return 支付记录，若不存在则返回null
	 */
	protected Payment getPayment(Long paymentId) {
		return paymentService.get(paymentId);
	}

	/**
	 * 获取通知URL
	 * 
	 * @param notifyMethod
	 *            通知方法
	 * @return 通知URL
	 */
	protected String getNotifyUrl(PaymentPlugin.NotifyMethod notifyMethod) {
		Setting setting = SystemUtils.getSetting();
		if (notifyMethod != null) {
			return setting.getSiteUrl() + "/payment/plugin_notify/" + getId() + "/" + notifyMethod + ".jhtml";
		} else {
			return setting.getSiteUrl() + "/payment/plugin_notify/" + getId() + "/" + PaymentPlugin.NotifyMethod.general + ".jhtml";
		}
	}

	/**
	 * 连接Map键值对
	 * 
	 * @param map
	 *            Map
	 * @param prefix
	 *            前缀
	 * @param suffix
	 *            后缀
	 * @param separator
	 *            连接符
	 * @param ignoreEmptyValue
	 *            忽略空值
	 * @param ignoreKeys
	 *            忽略Key
	 * @return 字符串
	 */
	protected String joinKeyValue(Map<String, Object> map, String prefix, String suffix, String separator, boolean ignoreEmptyValue, String... ignoreKeys) {
		List<String> list = new ArrayList<String>();
		if (map != null) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				String key = entry.getKey();
				String value = ConvertUtils.convert(entry.getValue());
				if (StringUtils.isNotEmpty(key) && !ArrayUtils.contains(ignoreKeys, key) && (!ignoreEmptyValue || StringUtils.isNotEmpty(value))) {
					list.add(key + "=" + (value != null ? value : ""));
				}
			}
		}
		return (prefix != null ? prefix : "") + StringUtils.join(list, separator) + (suffix != null ? suffix : "");
	}

	/**
	 * 连接Map值
	 * 
	 * @param map
	 *            Map
	 * @param prefix
	 *            前缀
	 * @param suffix
	 *            后缀
	 * @param separator
	 *            连接符
	 * @param ignoreEmptyValue
	 *            忽略空值
	 * @param ignoreKeys
	 *            忽略Key
	 * @return 字符串
	 */
	protected String joinValue(Map<String, Object> map, String prefix, String suffix, String separator, boolean ignoreEmptyValue, String... ignoreKeys) {
		List<String> list = new ArrayList<String>();
		if (map != null) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				String key = entry.getKey();
				String value = ConvertUtils.convert(entry.getValue());
				if (StringUtils.isNotEmpty(key) && !ArrayUtils.contains(ignoreKeys, key) && (!ignoreEmptyValue || StringUtils.isNotEmpty(value))) {
					list.add(value != null ? value : "");
				}
			}
		}
		return (prefix != null ? prefix : "") + StringUtils.join(list, separator) + (suffix != null ? suffix : "");
	}

	/**
	 * 重写equals方法
	 * 
	 * @param obj
	 *            对象
	 * @return 是否相等
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		PaymentPlugin other = (PaymentPlugin) obj;
		return getId()==other.getId();
	}

	/**
	 * 重写hashCode方法
	 * 
	 * @return HashCode
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
	}

	/**
	 * 实现compareTo方法
	 * 
	 * @param paymentPlugin
	 *            支付插件
	 * @return 比较结果
	 */
	public int compareTo(PaymentPlugin paymentPlugin) {
		if (paymentPlugin == null) {
			return 1;
		}
		return new CompareToBuilder().append(getOrder(), paymentPlugin.getOrder()).append(getId(), paymentPlugin.getId()).toComparison();
	}
	@Override
	public Plugin.Type getPluginType() {
		return Plugin.Type.payment;
	}

	@Override
	public List<PluginConfigAttr> getPluginConfigAttrs() {
		pluginConfigAttrs = new ArrayList<PluginConfigAttr>();
		PluginConfigAttr paymentNameAttr = new PluginConfigAttr(PAYMENT_NAME_ATTRIBUTE_NAME, "支付方式", true, 10);
		pluginConfigAttrs.add(paymentNameAttr);
		PluginConfigAttr requestUrlAttr = new PluginConfigAttr(REQUEST_URL_ATTRIBUTE_NAME, "付款请求地址", true, 15);
		pluginConfigAttrs.add(requestUrlAttr);
		
		PluginConfigAttr feeTypeAttr = new PluginConfigAttr(FEE_TYPE_ATTRIBUTE_NAME, "手续费类型", true, 17,PluginConfigAttr.ShowType.SELECT);
		pluginConfigAttrs.add(feeTypeAttr);
		
		PluginConfigAttr feeAttr = new PluginConfigAttr(FEE_ATTRIBUTE_NAME, "手续费", true, 18);
		pluginConfigAttrs.add(feeAttr);
		
		PluginConfigAttr logoAttr = new PluginConfigAttr(LOGO_ATTRIBUTE_NAME, "LOGO", false, 200,PluginConfigAttr.ShowType.IMAGESELECT);
		pluginConfigAttrs.add(logoAttr);
		PluginConfigAttr descriptionAttr = new PluginConfigAttr(DESCRIPTION_ATTRIBUTE_NAME, "描述",false, 1000,PluginConfigAttr.ShowType.TEXTAREA);
		pluginConfigAttrs.add(descriptionAttr);
		
		PluginConfig pluginConfig = pluginConfigService.findByPluginId(getId());
		Map<String,String> attributesMap =pluginConfig.getAttributeMap();
		for (PluginConfigAttr pluginConfigAttr : pluginConfigAttrs) {
			pluginConfigAttr.setValue(attributesMap.get(pluginConfigAttr.getField()));
		}
		
		return pluginConfigAttrs;
	}
}