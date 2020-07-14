package com.jxf.svc.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;



/**
 * Service - 插件
 * 
 * @author JINXINFU
 * @version 2.0
 */
@Service("pluginServiceImpl")
public class PluginServiceImpl implements PluginService {

	@Resource
	private List<PaymentPlugin> paymentPlugins = new ArrayList<PaymentPlugin>();	
	@Resource
	private List<StoragePlugin> storagePlugins = new ArrayList<StoragePlugin>();
	@Resource
	private List<LoginPlugin> loginPlugins = new ArrayList<LoginPlugin>();
	@Resource
	private List<MessagePlugin> messagePlugins = new ArrayList<MessagePlugin>();
	@Resource
	private Map<String, PaymentPlugin> paymentPluginMap = new HashMap<String, PaymentPlugin>();
	@Resource
	private Map<String, StoragePlugin> storagePluginMap = new HashMap<String, StoragePlugin>();
	@Resource
	private Map<String, LoginPlugin> loginPluginMap = new HashMap<String, LoginPlugin>();
	@Resource
	private Map<String, MessagePlugin> messagePluginMap = new HashMap<String, MessagePlugin>();
	@Resource
	private List<Plugin> plugins = new ArrayList<Plugin>();
	@Resource
	private Map<String, Plugin> pluginMap = new HashMap<String, Plugin>();
	
	/**
	 * 获取所有插件
	 */
	@Override
	public List<Plugin> getPlugins() {
		Collections.sort(plugins);
		return plugins;
	}
	/**
	 * 根据 plugin Id获取plugin
	 */
	@Override
	public Plugin getPlugin(String id) {
		return pluginMap.get(id);
	}
	
	public List<PaymentPlugin> getPaymentPlugins() {
		Collections.sort(paymentPlugins);
		return paymentPlugins;
	}
	
	public List<StoragePlugin> getStoragePlugins() {
		Collections.sort(storagePlugins);
		return storagePlugins;
	}

	public List<LoginPlugin> getLoginPlugins() {
		Collections.sort(loginPlugins);
		return loginPlugins;
	}
	@Override
	public List<MessagePlugin> getMessagePlugins() {

		Collections.sort(messagePlugins);
		return messagePlugins;
	}
	public List<PaymentPlugin> getPaymentPlugins(final boolean isEnabled) {
		List<PaymentPlugin> result = new ArrayList<PaymentPlugin>();
		CollectionUtils.select(paymentPlugins, new Predicate() {
			public boolean evaluate(Object object) {
				PaymentPlugin paymentPlugin = (PaymentPlugin) object;
				return paymentPlugin.getIsEnabled() == isEnabled;
			}
		}, result);
		Collections.sort(result);
		return result;
	}
	
	public List<StoragePlugin> getStoragePlugins(final boolean isEnabled) {
		List<StoragePlugin> result = new ArrayList<StoragePlugin>();
		CollectionUtils.select(storagePlugins, new Predicate() {
			public boolean evaluate(Object object) {
				StoragePlugin storagePlugin = (StoragePlugin) object;
				return storagePlugin.getIsEnabled() == isEnabled;
			}
		}, result);
		Collections.sort(result);
		return result;
	}

	public List<LoginPlugin> getLoginPlugins(final boolean isEnabled) {
		List<LoginPlugin> result = new ArrayList<LoginPlugin>();
		CollectionUtils.select(loginPlugins, new Predicate() {
			public boolean evaluate(Object object) {
				LoginPlugin loginPlugin = (LoginPlugin) object;
				return loginPlugin.getIsEnabled() == isEnabled;
			}
		}, result);
		Collections.sort(result);
		return result;
	}
	public List<MessagePlugin> getMessagePlugins(final boolean isEnabled) {
		List<MessagePlugin> result = new ArrayList<MessagePlugin>();
		CollectionUtils.select(messagePlugins, new Predicate() {
			public boolean evaluate(Object object) {
				MessagePlugin messagePlugin = (MessagePlugin) object;
				return messagePlugin.getIsEnabled() == isEnabled;
			}
		}, result);
		Collections.sort(result);
		return result;
	}
	public PaymentPlugin getPaymentPlugin(String id) {
		return paymentPluginMap.get(id);
	}
	
	public StoragePlugin getStoragePlugin(String id) {
		return storagePluginMap.get(id);
	}

	public LoginPlugin getLoginPlugin(String id) {
		return loginPluginMap.get(id);
	}
	@Override
	public MessagePlugin getMessagePlugin(String id) {
	
		return messagePluginMap.get(id);
	}
	/**
	 * 根据类型和可用状态查询
	 */
	@Override
	public List<Plugin> getPlugins(Plugin.Type pluginType,final boolean isEnabled) {
		List<Plugin> plugins = getPluginsByType(pluginType);
		List<Plugin> result = new ArrayList<Plugin>();
		for (Plugin Plugin : plugins) {
			if(Plugin.getIsEnabled() == isEnabled){
				result.add(Plugin);
			}
		}
		Collections.sort(result);
		return result;
	}
	@Override
	public List<Plugin> getPluginsByType(Plugin.Type pluginType) {
		List<Plugin> plugins = getPlugins();
		List<Plugin> typePlugins = Lists.newArrayList();
		for (Plugin Plugin : plugins) {
			if(Plugin.getPluginType().equals(pluginType)){
				typePlugins.add(Plugin);
			}
		}
		return typePlugins;
	}



}