package com.jxf.svc.plugin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 插件配置ServiceImpl
 * @author HUOJIABAO
 * @version 2016-06-08
 */
@Service("pluginConfigService")
@Transactional(readOnly = true)
public class PluginConfigServiceImpl extends CrudServiceImpl<PluginConfigDao, PluginConfig> implements PluginConfigService{

	@Autowired
	private PluginConfigDao pluginConfigDao;
	
	public PluginConfig get(Long id) {
		return super.get(id);
	}
	
	public List<PluginConfig> findList(PluginConfig pluginConfig) {
		return super.findList(pluginConfig);
	}
	
	public Page<PluginConfig> findPage(Page<PluginConfig> page, PluginConfig pluginConfig) {
		return super.findPage(page, pluginConfig);
	}
	
	@Transactional(readOnly = false)
	public void save(PluginConfig pluginConfig) {
		super.save(pluginConfig);
	}
	
	@Transactional(readOnly = false)
	public void delete(PluginConfig pluginConfig) {
		super.delete(pluginConfig);
	}

	@Transactional(readOnly = true)
	public boolean pluginIdExists(String pluginId) {
		return pluginConfigDao.pluginIdExists(pluginId);
	}

	@Transactional(readOnly = true)
	@Cacheable("pluginConfig")
	public PluginConfig findByPluginId(String pluginId) {
		return pluginConfigDao.findByPluginId(pluginId);
	}

	@CacheEvict(value = "pluginConfig", allEntries = true)
	public void deleteByPluginId(String pluginId) {
		PluginConfig pluginConfig = pluginConfigDao.findByPluginId(pluginId);
		pluginConfigDao.delete(pluginConfig);
	}


}