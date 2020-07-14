package com.jxf.svc.plugin;

import java.util.List;

/**
 * @类功能说明： 封装form 参数属性信息
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：zhuhuijie 
 * @创建时间：2016年6月17日 下午5:50:44 
 * @版本：V1.0
 */
public class PluginConfigVo {
	private String id;
	private List<PluginConfigAttr> pluginConfigAttrs;
	private Integer sort;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<PluginConfigAttr> getPluginConfigAttrs() {
		return pluginConfigAttrs;
	}

	public void setPluginConfigAttrs(List<PluginConfigAttr> pluginConfigAttrs) {
		this.pluginConfigAttrs = pluginConfigAttrs;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
}
