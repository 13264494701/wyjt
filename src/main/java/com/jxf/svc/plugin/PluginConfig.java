package com.jxf.svc.plugin;

import org.hibernate.validator.constraints.Length;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 插件配置Entity
 * @author HUOJIABAO
 * @version 2016-06-08
 */
public class PluginConfig extends CrudEntity<PluginConfig> {
	
	private static final long serialVersionUID = 1L;
	private String pluginId;		// 插件ID
	private Integer displayOrder;		// 排序	
	private String attributes;		// 配置参数
	private Boolean isEnabled;		// 是否启用
	private Boolean isInstalled;   // 是否安装

			
	public PluginConfig() {
		super();
	}

	public PluginConfig(Long id){
		super(id);
	}
	
	public String getPluginId() {
		return pluginId;
	}

	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}
	
	@Length(min=0, max=11, message="排序长度必须介于 0 和 11 之间")
	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	
	@Length(min=1, max=1, message="是否启用长度必须介于 1 和 1 之间")
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	

	public Boolean getIsInstalled() {
		return isInstalled;
	}

	public void setIsInstalled(Boolean isInstalled) {
		this.isInstalled = isInstalled;
	}
	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	@SuppressWarnings("unchecked")
	public Map<String,String> getAttributeMap(){
		Map<String,String> attributesMap = new HashMap<String,String>();
		if(attributes != null && attributes != ""){
			attributesMap = JSON.parseObject(attributes, Map.class);
		}
		return attributesMap;
	}
	/**
	 * 获取属性值
	 * 
	 * @param name
	 *            属性名称
	 * @return 属性值
	 */
	public String getAttribute(String name){
		Map<String,String> attributesMap = getAttributeMap();
		return attributesMap.get(name);
	}	
}