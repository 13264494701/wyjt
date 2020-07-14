package com.jxf.svc.plugin;

import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public abstract class Plugin  implements Comparable<Plugin>{
	
	/**
	 * 类型
	 */
	public enum Type { 

		/** 存储插件 */
		storage,

		/** 登录插件 */
		login,

		/** 支付插件 */
		payment,
		
		/** 短信插件 */
		message,

		/** 风控插件 */
		riskControl,

		/** 签章插件 */
		signature
	}
		
	@Autowired
	private PluginConfigService pluginConfigService;
	
	public List<PluginConfigAttr> pluginConfigAttrs;
	
	/**
	 * 获取ID
	 * 
	 * @return ID
	 */
	public String getId() {
		return getClass().getAnnotation(Component.class).value();
	}
	/**
	 * 获取插件配置
	 * 
	 * @return 插件配置
	 */
	public PluginConfig getPluginConfig() {
		return pluginConfigService.findByPluginId(getId());
	}
	/**
	 * plugin 类型
	 */
	public abstract Plugin.Type getPluginType();
	
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
	 * 获取配置属性
	 */
	public abstract List<PluginConfigAttr> getPluginConfigAttrs();
	
	/**
	 * 获取属性值
	 * @param name
	 *            属性名称
	 * @return 属性值
	 */
	public String getAttribute(String name) {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttribute(name) : null;
	}

	/**
	 * 获取是否已启用
	 * 
	 * @return 是否已启用
	 */
	public boolean getIsEnabled() {
		//从数据库获取
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getIsEnabled() : false;
	}
	/**
	 * 获取排序
	 * 
	 * @return 排序
	 */
	public Integer getOrder() {
		// 数据库配置
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getDisplayOrder() : null;
	}
	/**
	 * 获取是否已安装
	 * 
	 * @return 是否已安装
	 */
	public boolean getIsInstalled() {
		//数据库查找此插件
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getIsInstalled() : false;
	}

	
	/**
	 * 重写equals方法
	 * 
	 * @param obj 对象
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
		Plugin other = (Plugin) obj;
		return new EqualsBuilder().append(getId(), other.getId()).isEquals();
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
	 * @param paymentPlugin 支付插件
	 * @return 比较结果
	 */
	public int compareTo(Plugin plugin) {
		if (plugin == null) {
			return 1;
		}
		return new CompareToBuilder().append(getOrder(), plugin.getOrder()).append(getId(), plugin.getId()).toComparison();
	}

}
