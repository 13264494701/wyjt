package com.jxf.svc.plugin;

import java.util.List;




/**
 * Service - 插件
 * 
 * @author JINXINFU
 * @version 2.0
 */
public interface PluginService {

	/**
	 * 获取支付插件
	 * 
	 * @return 支付插件
	 */
	List<PaymentPlugin> getPaymentPlugins();
	/**
	 * 获取存储插件
	 * 
	 * @return 存储插件
	 */
	List<StoragePlugin> getStoragePlugins();

	/**
	 * 获取登录插件
	 * 
	 * @return 登录插件
	 */
	List<LoginPlugin> getLoginPlugins();
	
	/**
	 * 获取短信插件
	 * 
	 * @return 短信插件
	 */
	List<MessagePlugin> getMessagePlugins();


	/**
	 * 获取支付插件
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @return 支付插件
	 */
	List<PaymentPlugin> getPaymentPlugins(boolean isEnabled);
	/**
	 * 获取存储插件
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @return 存储插件
	 */
	List<StoragePlugin> getStoragePlugins(boolean isEnabled);

	/**
	 * 获取登录插件
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @return 登录插件
	 */
	List<LoginPlugin> getLoginPlugins(boolean isEnabled);
	
	/**
	 * 获取短信插件
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @return 短信插件
	 */
	List<MessagePlugin> getMessagePlugins(boolean isEnabled);

	/**
	 * 获取支付插件
	 * 
	 * @param id
	 *            ID
	 * @return 支付插件
	 */
	PaymentPlugin getPaymentPlugin(String id);
	
	/**
	 * 获取存储插件
	 * 
	 * @param id
	 *            ID
	 * @return 存储插件
	 */
	StoragePlugin getStoragePlugin(String id);

	/**
	 * 获取登录插件
	 * 
	 * @param id
	 *            ID
	 * @return 登录插件
	 */
	LoginPlugin getLoginPlugin(String id);
	/**
	 * 获取短信插件
	 * 
	 * @param id
	 *            ID
	 * @return 短信插件
	 */
	MessagePlugin getMessagePlugin(String id);
	
	/**
	 * 获取所有插件
	 */
	public List<Plugin> getPlugins();
	
	/**
	 * 根据 plugin Id获取plugin
	 */
	public Plugin getPlugin(String id);
	
	/**
	 * 根据类型获取可用插件
	 */
	public List<Plugin> getPlugins(Plugin.Type pluginType,final boolean isEnabled);
	
	
	/**
	 * 根据类型获取插件
	 */
	public List<Plugin> getPluginsByType(Plugin.Type pluginType);

}