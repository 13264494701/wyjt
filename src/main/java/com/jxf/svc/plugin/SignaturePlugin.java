package com.jxf.svc.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Plugin - 支付
 * 
 * @author JINXINFU
 * @version 2.0
 */
public abstract class SignaturePlugin  extends Plugin {

	/** 插件名称 */
	public static final String SIGNATURE_PLUGIN_NAME = "pluginName";
	/** 服务地址 */
	public static final String SIGNATURE_SERVICE_URL = "serviceUrl";
	/** 商户号 */
	public static final String SIGNATURE_APP_KEY = "appKey";

	/** 秘钥 */
	public static final String SIGNATURE_APP_SECRET = "appSecret";

	/** 沙箱地址 */
	public static final String SIGNATURE_SEND_SERVICE_URL = "sendServiceUrl";

	

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
	 * 获取签章名称
	 * 
	 * @return 签章名称
	 */
	public String getPluginName() {
		PluginConfig pluginConfig = getShopPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttribute(SIGNATURE_PLUGIN_NAME) : null;
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
		SignaturePlugin other = (SignaturePlugin) obj;
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
	 * @param signaturePlugin
	 *            支付插件
	 * @return 比较结果
	 */
	public int compareTo(SignaturePlugin signaturePlugin) {
		if (signaturePlugin == null) {
			return 1;
		}
		return new CompareToBuilder().append(getOrder(), signaturePlugin.getOrder()).append(getId(), signaturePlugin.getId()).toComparison();
	}
	@Override
	public Plugin.Type getPluginType() {
		return Plugin.Type.signature;
	}

	@Override
	public List<PluginConfigAttr> getPluginConfigAttrs() {
		pluginConfigAttrs = new ArrayList<PluginConfigAttr>();
		PluginConfigAttr PluginNameAttr = new PluginConfigAttr(SIGNATURE_PLUGIN_NAME, "插件名称", true, 10);
		pluginConfigAttrs.add(PluginNameAttr);
		PluginConfigAttr serviceUrlAttr = new PluginConfigAttr(SIGNATURE_SERVICE_URL, "服务地址", true, 15);
		pluginConfigAttrs.add(serviceUrlAttr);
		PluginConfigAttr sendUrlAttr = new PluginConfigAttr(SIGNATURE_SEND_SERVICE_URL, "沙箱地址", true, 17);
		pluginConfigAttrs.add(sendUrlAttr);
		PluginConfigAttr appKeyAttr = new PluginConfigAttr(SIGNATURE_APP_KEY, "商户号", true, 18);
		pluginConfigAttrs.add(appKeyAttr);
		PluginConfigAttr secretAttr = new PluginConfigAttr(SIGNATURE_APP_SECRET, "秘钥", true, 19,PluginConfigAttr.ShowType.PASSWORD);
		pluginConfigAttrs.add(secretAttr);
		
		PluginConfig pluginConfig = pluginConfigService.findByPluginId(getId());
		Map<String,String> attributesMap =pluginConfig.getAttributeMap();
		for (PluginConfigAttr pluginConfigAttr : pluginConfigAttrs) {
			pluginConfigAttr.setValue(attributesMap.get(pluginConfigAttr.getField()));
		}
		
		return pluginConfigAttrs;
	}
}