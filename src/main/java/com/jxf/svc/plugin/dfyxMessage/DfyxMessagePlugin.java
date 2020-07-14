package com.jxf.svc.plugin.dfyxMessage;


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


@Component("dfyxMessagePlugin")
public class DfyxMessagePlugin extends MessagePlugin {
	
	@Autowired
	private PluginConfigService pluginConfigService;

	/** "短信接口调用url"属性名称 */
	public static final String URL_ATTRIBUTE_NAME = "url";
	
	/** "账号名称"属性名称 */
	public static final String USERNAME_ATTRIBUTE_NAME = "username";
	
	/** "密码名称"属性名称 */
	public static final String PWD_ATTRIBUTE_NAME = "pwd";
	
	/** "手机号码"属性名称 */
	public static final String PHONENO_ATTRIBUTE_NAME = "mobile";
	
	/** "短信内容"属性名称 */
	public static final String CONTENT_ATTRIBUTE_NAME = "content";
	
	
	@Override
	public String getName() {

		return "东方易信";
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
	 * 获取密码键名
	 * 
	 * @return 密码键名
	 */
	public String getPasswordKey() {

		return PWD_ATTRIBUTE_NAME;
	}
	/**
	 * 获取密码
	 * 
	 * @return 密码
	 */
	public String getPasswordValue() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ?pluginConfig.getAttribute(PWD_ATTRIBUTE_NAME) : null;
	}
	
	/**
	 * 获取手机号码键名
	 * 
	 * @return 手机号码键名
	 */
	public String getPhoneNoKey() {

		return PHONENO_ATTRIBUTE_NAME;
	}
	/**
	 * 获取短信内容键名
	 * 
	 * @return 短信内容键名
	 */
	public String getContentKey() {

		return CONTENT_ATTRIBUTE_NAME;
	}
	
	@Override
	public List<PluginConfigAttr> getPluginConfigAttrs() {
		pluginConfigAttrs = new ArrayList<PluginConfigAttr>();
		pluginConfigAttrs.add(new PluginConfigAttr(URL_ATTRIBUTE_NAME, "短信接口调用url", true, 10));
		pluginConfigAttrs.add(new PluginConfigAttr(USERNAME_ATTRIBUTE_NAME, "账号名称", true, 15));
		pluginConfigAttrs.add(new PluginConfigAttr(PWD_ATTRIBUTE_NAME, "密码名称", true, 17,ShowType.PASSWORD));
		
		PluginConfig pluginConfig = pluginConfigService.findByPluginId(getId());
		Map<String,String> attributesMap =pluginConfig.getAttributeMap();
		for (PluginConfigAttr pluginConfigAttr : pluginConfigAttrs) {
			pluginConfigAttr.setValue(attributesMap.get(pluginConfigAttr.getField()));
		}
		
		return pluginConfigAttrs;
	}


}
