package com.jxf.web.admin.sys;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.jxf.svc.config.Global;
import com.jxf.svc.plugin.Plugin;
import com.jxf.svc.plugin.PluginConfig;
import com.jxf.svc.plugin.PluginConfigAttr;
import com.jxf.svc.plugin.PluginConfigService;
import com.jxf.svc.plugin.PluginConfigVo;
import com.jxf.svc.plugin.PluginService;
import com.jxf.svc.plugin.utils.PluginUtils;



@Controller
@RequestMapping(value = "${adminPath}/plugin")
public class PluginController extends BaseController{
	
	@Autowired
	private PluginService pluginService;
	@Autowired
	private PluginConfigService pluginConfigService;
	
	/**
	 * 函数功能说明 根据类型列出插件
	 * zhuhuijie  2016年6月16日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param pluginType
	 * @参数： @return     
	 * @return String    
	 * @throws
	 */
	@RequiresPermissions("plugins:view")
	@RequestMapping(value="list/{pluginType}")
	public String list(@PathVariable("pluginType")Plugin.Type pluginType,Model model){

		List<Plugin> plugins = pluginService.getPluginsByType( pluginType);
		model.addAttribute("plugins", plugins);
		return "admin/sys/plugins/pluginList";
	}
	
	@RequiresPermissions("plugins:view")
	@RequestMapping(value="setting/{pluginId}",method = RequestMethod.GET)
	public String settingPage(@PathVariable("pluginId")String pluginId,Model model){
		Plugin plugin  = pluginService.getPlugin(pluginId);
		List<PluginConfigAttr> arrts= plugin.getPluginConfigAttrs();
		for (PluginConfigAttr pluginConfigAttr : arrts) {
			System.out.println(pluginConfigAttr);
		}
		model.addAttribute("attrs", arrts);
		model.addAttribute("pluginId", plugin.getId());
		model.addAttribute("pluginType", plugin.getPluginType());
		model.addAttribute("order", plugin.getOrder());
		return "admin/sys/plugins/pluginSetting";
	}
	@RequiresPermissions("plugins:edit")
	@RequestMapping(value="setting",method = RequestMethod.POST)
	public String settingUpdate(PluginConfigVo config, Model model,RedirectAttributes redirectAttributes){
		Plugin plugin  = pluginService.getPlugin(config.getId());
		PluginConfig pluginConfig = pluginConfigService.findByPluginId(config.getId());
		Map<String,String> attributesMap= PluginUtils.configAttrsToMap(config.getPluginConfigAttrs());
		String attributes =  JSON.toJSONString(attributesMap);
		if(pluginConfig == null){
			pluginConfig = new PluginConfig();
			pluginConfig.setPluginId(plugin.getId());
		}
		pluginConfig.setDisplayOrder(config.getSort());
		pluginConfig.setAttributes(attributes);
		pluginConfig.setIsEnabled(true);
		pluginConfigService.save(pluginConfig);
		return "redirect:"+Global.getAdminPath()+"/plugin/list/" + plugin.getPluginType();
	}
	
	
	/**
	 * 函数功能说明 安装插件
	 * zhuhuijie  2016年6月16日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param pluginId
	 * @参数： @return     
	 * @return String    
	 * @throws
	 */
	@RequiresPermissions("plugins:edit")
	@RequestMapping(value="install/{pluginId}")
	public String install(@PathVariable("pluginId")String pluginId,RedirectAttributes redirectAttributes){
		Plugin plugin  = pluginService.getPlugin(pluginId);
		PluginConfig pluginConfig = pluginConfigService.findByPluginId(pluginId);
		if(pluginConfig == null){
			pluginConfig = new PluginConfig();
			pluginConfig.setPluginId(plugin.getId());
			pluginConfig.setAttributes(null);
			pluginConfig.setIsInstalled(true);
			pluginConfig.setIsEnabled(false);
		}else{
			if(pluginConfig.getIsInstalled()){
				addMessage(redirectAttributes, "失败，该插件已经安装");
			}else{
				pluginConfig.setIsInstalled(true);
				pluginConfig.setIsEnabled(true);
				
			}
		}
		pluginConfigService.save(pluginConfig);
		return "redirect:"+Global.getAdminPath()+"/plugin/list/" + plugin.getPluginType();
	}
	
	
	
	/**
	 * 函数功能说明 卸载插件
	 * zhuhuijie  2016年6月16日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param pluginId
	 * @参数： @return     
	 * @return String    
	 * @throws
	 */
	@RequiresPermissions("plugins:edit")
	@RequestMapping(value="uninstall/{pluginId}")
	public String uninstall(@PathVariable("pluginId")String pluginId,RedirectAttributes redirectAttributes){
		Plugin plugin  = pluginService.getPlugin(pluginId);
		PluginConfig pluginConfig = pluginConfigService.findByPluginId(pluginId);
		if(pluginConfig == null){
			addMessage(redirectAttributes, "失败，该插件尚未安装");
		}else{
			if(pluginConfig.getIsInstalled()){
				pluginConfig.setIsInstalled(false);
				pluginConfig.setIsEnabled(false);
				
			}else{
				addMessage(redirectAttributes, "失败，该插件已经卸载");
			}
			pluginConfigService.save(pluginConfig);
		}
		return "redirect:"+Global.getAdminPath()+"/plugin/list/" + plugin.getPluginType();
	}

}
