package com.jxf.svc.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Plugin - 存储
 * 
 * @author JINXINFU
 * @version 2.0
 */
public abstract class StoragePlugin extends Plugin {

	@Autowired
	private PluginConfigService pluginConfigService;

	/** "存储方式名称"属性名称 */
	public static final String STORAGE_NAME_ATTRIBUTE_NAME = "storageName";
	/** "请求接口地址"属性名称 */
	public static final String REQUEST_URL_ATTRIBUTE_NAME = "requestUrl";

	/** "LOGO"属性名称 */
	public static final String LOGO_ATTRIBUTE_NAME = "logo";

	/** "描述"属性名称 */
	public static final String DESCRIPTION_ATTRIBUTE_NAME = "description";
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
	public PluginConfig getPluginConfig() {
		return pluginConfigService.findByPluginId(getId());
	}

	/**
	 * 获取是否已启用
	 * 
	 * @return 是否已启用
	 */
	public boolean getIsEnabled() {
		PluginConfig pluginConfig = getPluginConfig();
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
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttribute(name) : null;
	}

	/**
	 * 获取排序
	 * 
	 * @return 排序
	 */
	public Integer getOrder() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getDisplayOrder() : null;
	}

	/**
	 * 文件上传
	 * 
	 * @param path
	 *            上传路径
	 * @param file
	 *            上传文件
	 * @param contentType
	 *            文件类型
	 */
	public abstract void upload(String path, File file, String contentType);

	/**
	 * 获取访问URL
	 * 
	 * @param path
	 *            上传路径
	 * @return 访问URL
	 */
	public abstract String getUrl(String path);

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
		StoragePlugin other = (StoragePlugin) obj;
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
	 * 
	 * @param storagePlugin
	 *            存储插件
	 * @return 比较结果
	 */
	public int compareTo(StoragePlugin storagePlugin) {
		if (storagePlugin == null) {
			return 1;
		}
		return new CompareToBuilder().append(getOrder(), storagePlugin.getOrder()).append(getId(), storagePlugin.getId()).toComparison();
	}
	@Override
	public Plugin.Type getPluginType() {
		return Plugin.Type.storage;
	}

	@Override
	public List<PluginConfigAttr> getPluginConfigAttrs() {
		pluginConfigAttrs = new ArrayList<PluginConfigAttr>();
		PluginConfigAttr storageNameAttr = new PluginConfigAttr(STORAGE_NAME_ATTRIBUTE_NAME, "存储方式", true, 10);
		pluginConfigAttrs.add(storageNameAttr);
		PluginConfigAttr requestUrlAttr = new PluginConfigAttr(REQUEST_URL_ATTRIBUTE_NAME, "请求地址", true, 15);
		pluginConfigAttrs.add(requestUrlAttr);
		PluginConfigAttr logoAttr = new PluginConfigAttr(LOGO_ATTRIBUTE_NAME, "LOGO", false, 20,PluginConfigAttr.ShowType.IMAGESELECT);
		pluginConfigAttrs.add(logoAttr);
		PluginConfigAttr descriptionAttr = new PluginConfigAttr(DESCRIPTION_ATTRIBUTE_NAME, "描述",false, 1000,PluginConfigAttr.ShowType.TEXTAREA);
		pluginConfigAttrs.add(descriptionAttr);
		return pluginConfigAttrs;
	}
}