package com.jxf.web.admin.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.version.entity.SysVersion;
import com.jxf.svc.sys.version.service.SysVersionService;


/**
 * 系统版本Controller
 * @author wo
 * @version 2019-01-07
 */
@Controller
@RequestMapping(value = "${adminPath}/sysVersion")
public class SysVersionController extends BaseController {

	@Autowired
	private SysVersionService sysVersionService;
	
	@ModelAttribute
	public SysVersion get(@RequestParam(required=false) Long id) {
		SysVersion entity = null;
		if (id!=null){
			entity = sysVersionService.get(id);
		}
		if (entity == null){
			entity = new SysVersion();
		}
		return entity;
	}
	
	@RequiresPermissions("version:sysVersion:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysVersion sysVersion, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysVersion> page = sysVersionService.findPage(new Page<SysVersion>(request, response), sysVersion); 
		model.addAttribute("page", page);
		return "admin/sys/version/sysVersionList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("version:sysVersion:view")
	@RequestMapping(value = "add")
	public String add(SysVersion sysVersion, Model model) {
		model.addAttribute("sysVersion", sysVersion);
		return "admin/sys/version/sysVersionAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("version:sysVersion:view")
	@RequestMapping(value = "query")
	public String query(SysVersion sysVersion, Model model) {
		model.addAttribute("sysVersion", sysVersion);
		return "admin/sys/version/sysVersionQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("version:sysVersion:view")
	@RequestMapping(value = "update")
	public String update(SysVersion sysVersion, Model model) {
		model.addAttribute("sysVersion", sysVersion);
		return "admin/sys/version/sysVersionUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("version:sysVersion:edit")
	@RequestMapping(value = "save")
	public String save(SysVersion sysVersion, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sysVersion)){
			return add(sysVersion, model);
		}
		sysVersionService.save(sysVersion);
		addMessage(redirectAttributes, "保存系统版本成功");
		return "redirect:"+Global.getAdminPath()+"/sysVersion/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("version:sysVersion:edit")
	@RequestMapping(value = "delete")
	public String delete(SysVersion sysVersion, RedirectAttributes redirectAttributes) {
		sysVersionService.delete(sysVersion);
		addMessage(redirectAttributes, "删除系统版本成功");
		return "redirect:"+Global.getAdminPath()+"/sysVersion/?repage";
	}

}