package com.jxf.svc.plugin.lxhlMessage;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jxf.svc.plugin.MessagePlugin;
import com.jxf.svc.plugin.PluginConfig;
import com.jxf.svc.plugin.PluginConfigAttr;
import com.jxf.svc.plugin.PluginConfigService;
import com.jxf.svc.plugin.PluginConfigAttr.ShowType;
@Component("lxhlMessagePlugin")
public class LxhlMessagePlugin extends MessagePlugin {
	

	@Autowired
	private PluginConfigService pluginConfigService;

	/** "短信接口调用url"属性名称 */
	public static final String URL_ATTRIBUTE_NAME = "url";
	
	/** "账号名称"属性名称 */
	public static final String USERNAME_ATTRIBUTE_NAME = "CorpID";
	
	/** "密码名称"属性名称 */
	public static final String PASSWORD_ATTRIBUTE_NAME = "Pwd";
	
	/** "手机号码"属性名称 */
	public static final String PHONENO_ATTRIBUTE_NAME = "Mobile";
	
	/** "短信内容"属性名称 */
	public static final String CONTENT_ATTRIBUTE_NAME = "Content";
	
	@Override
	public String getName() {
		return "领先互联";
	}

	@Override
	public String getVersion() {
		return "1.0";
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
		return null;
	}


	@Override
	public Map<String, Object> getParameterMap(String sn, String description,
			HttpServletRequest request) {
		return null;
	}


	
	/**
	 * 获取短信接口调用url
	 * 
	 * @return 短信接口调用url
	 */
	public String getUrl() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttribute(URL_ATTRIBUTE_NAME) : null;
	}

	/**
	 * 获取账号名称
	 * 
	 * @return 账号名称
	 */
	public String getUsernameKey() {

		return USERNAME_ATTRIBUTE_NAME;
	}
	/**
	 * 获取账号名称
	 * 
	 * @return 账号名称
	 */
	public String getUsernameValue() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttribute(USERNAME_ATTRIBUTE_NAME) : null;
	}

	/**
	 * 获取密码名称
	 * 
	 * @return 密码名称
	 */
	public String getPasswordKey() {

		return PASSWORD_ATTRIBUTE_NAME;
	}
	/**
	 * 获取密码名称
	 * 
	 * @return 密码名称
	 */
	public String getPasswordValue() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ?pluginConfig.getAttribute(PASSWORD_ATTRIBUTE_NAME) : null;
	}
	
	@Override
	public String getPhoneNoKey() {

		return PHONENO_ATTRIBUTE_NAME;
	}

	@Override
	public String getContentKey() {

		return CONTENT_ATTRIBUTE_NAME;
	}
	
	@Override
	public List<PluginConfigAttr> getPluginConfigAttrs() {
		pluginConfigAttrs = new ArrayList<PluginConfigAttr>();
		pluginConfigAttrs.add(new PluginConfigAttr(URL_ATTRIBUTE_NAME, "短信接口调用url", true, 10));
		pluginConfigAttrs.add(new PluginConfigAttr(USERNAME_ATTRIBUTE_NAME, "账号名称", true, 15));
		pluginConfigAttrs.add(new PluginConfigAttr(PASSWORD_ATTRIBUTE_NAME, "密码名称", true, 17,ShowType.PASSWORD));
		
		PluginConfig pluginConfig = pluginConfigService.findByPluginId(getId());
		Map<String,String> attributesMap =pluginConfig.getAttributeMap();
		for (PluginConfigAttr pluginConfigAttr : pluginConfigAttrs) {
			pluginConfigAttr.setValue(attributesMap.get(pluginConfigAttr.getField()));
		}
		
		return pluginConfigAttrs;
	}



}
