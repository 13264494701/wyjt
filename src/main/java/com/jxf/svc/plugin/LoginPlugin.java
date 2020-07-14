
package com.jxf.svc.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Plugin - 登录
 * 
 * @author JINXINFU
 * @version 2.0
 */
public abstract class LoginPlugin extends Plugin {

	/** "登录方式名称"属性名称 */
	public static final String LOGIN_METHOD_NAME_ATTRIBUTE_NAME = "loginMethodName";

	/** "请求接口地址"属性名称 */
	public static final String REQUEST_URL_ATTRIBUTE_NAME = "requestUrl";
	
	/** "LOGO"属性名称 */
	public static final String LOGO_ATTRIBUTE_NAME = "logo";

	/** "描述"属性名称 */
	public static final String DESCRIPTION_ATTRIBUTE_NAME = "description";

	/**
	 * 请求方法
	 */
	public enum RequestMethod {

		/** POST */
		post,

		/** GET */
		get
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
	 * 获取登录方式名称
	 * 
	 * @return 登录方式名称
	 */
	public String getLoginMethodName() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttribute(LOGIN_METHOD_NAME_ATTRIBUTE_NAME) : null;
	}

	/**
	 * 获取LOGO
	 * 
	 * @return LOGO
	 */
	public String getLogo() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttribute(LOGO_ATTRIBUTE_NAME) : null;
	}

	/**
	 * 获取描述
	 * 
	 * @return 描述
	 */
	public String getDescription() {
		PluginConfig pluginConfig = getPluginConfig();
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
	public abstract LoginPlugin.RequestMethod getRequestMethod();

	/**
	 * 获取请求字符编码
	 * 
	 * @return 请求字符编码
	 */
	public abstract String getRequestCharset();

	/**
	 * 获取请求参数
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return 请求参数
	 */
	public abstract Map<String, Object> getParameterMap(HttpServletRequest request);

	/**
	 * 验证通知是否合法
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return 通知是否合法
	 */
	public abstract boolean verifyNotify(HttpServletRequest request);

	/**
	 * 获取openID
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return openID
	 */
	public abstract String getOpenId(HttpServletRequest request);

	/**
	 * 获取E-mail
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return E-mail
	 */
	public abstract String getEmail(HttpServletRequest request);

	/**
	 * 获取昵称
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return 昵称
	 */
	public abstract String getNickname(HttpServletRequest request);

	/**
	 * 获取通知URL
	 * 
	 * @return 通知URL
	 */
	protected String getNotifyUrl() {

		return "/login/plugin_notify/" + getId() + ".jhtml";
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
		LoginPlugin other = (LoginPlugin) obj;
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
	 * @param loginPlugin
	 *            登录插件
	 * @return 比较结果
	 */
	public int compareTo(LoginPlugin loginPlugin) {
		if (loginPlugin == null) {
			return 1;
		}
		return new CompareToBuilder().append(getOrder(), loginPlugin.getOrder()).append(getId(), loginPlugin.getId()).toComparison();
	}
	@Override
	public Plugin.Type getPluginType() {
		return Plugin.Type.login;
	}

	@Override
	public List<PluginConfigAttr> getPluginConfigAttrs() {
		pluginConfigAttrs = new ArrayList<PluginConfigAttr>();
		PluginConfigAttr loginNameAttr = new PluginConfigAttr(LOGIN_METHOD_NAME_ATTRIBUTE_NAME, "登录方式", true, 10);
		pluginConfigAttrs.add(loginNameAttr);
		PluginConfigAttr requestUrlAttr = new PluginConfigAttr(REQUEST_URL_ATTRIBUTE_NAME, "请求地址", true, 15);
		pluginConfigAttrs.add(requestUrlAttr);
		PluginConfigAttr logoAttr = new PluginConfigAttr(LOGO_ATTRIBUTE_NAME, "LOGO", false, 20,PluginConfigAttr.ShowType.IMAGESELECT);
		pluginConfigAttrs.add(logoAttr);
		PluginConfigAttr descriptionAttr = new PluginConfigAttr(DESCRIPTION_ATTRIBUTE_NAME, "描述",false, 1000,PluginConfigAttr.ShowType.TEXTAREA);
		pluginConfigAttrs.add(descriptionAttr);
		return pluginConfigAttrs;
	}
}