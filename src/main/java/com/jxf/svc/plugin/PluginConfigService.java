package com.jxf.svc.plugin;

import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 插件配置Service
 * @author HUOJIABAO
 * @version 2016-06-08
 */
public interface PluginConfigService extends CrudService<PluginConfig> {
	
	/**
	 * 判断插件ID是否存在
	 * 
	 * @param pluginId
	 *            插件ID
	 * @return 插件ID是否存在
	 */
	boolean pluginIdExists(String pluginId);

	/**
	 * 根据插件ID查找插件配置
	 * 
	 * @param pluginId
	 *            插件ID
	 * @return 插件配置，若不存在则返回null
	 */
	PluginConfig findByPluginId(String pluginId);

	/**
	 * 根据插件ID删除插件配置
	 * 
	 * @param pluginId
	 *            插件ID
	 */
	void deleteByPluginId(String pluginId);

}