package com.jxf.web.admin.sys;



import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.jxf.svc.config.Global;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.data.entity.SysDataImport;
import com.jxf.svc.sys.data.service.SysDataImportService;




/**
 * 数据导入Controller
 * @author wo
 * @version 2019-01-13
 */
@Controller
@RequestMapping(value = "${adminPath}/sysDataImport")
public class SysDataImportController extends BaseController {

	@Autowired
	private SysDataImportService sysDataImportService;
	
	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	
	@ModelAttribute
	public SysDataImport get(@RequestParam(required=false) Long id) {
		SysDataImport entity = null;
		if (id!=null){
			entity = sysDataImportService.get(id);
		}
		if (entity == null){
			entity = new SysDataImport();
		}
		return entity;
	}
	
	@RequiresPermissions("data:sysDataImport:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysDataImport sysDataImport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysDataImport> page = sysDataImportService.findPage(new Page<SysDataImport>(request, response), sysDataImport); 
		model.addAttribute("page", page);
		return "admin/sys/data/sysDataImportList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("data:sysDataImport:view")
	@RequestMapping(value = "add")
	public String add(SysDataImport sysDataImport, Model model) {
		model.addAttribute("sysDataImport", sysDataImport);
		return "admin/sys/data/sysDataImportAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("data:sysDataImport:view")
	@RequestMapping(value = "query")
	public String query(SysDataImport sysDataImport, Model model) {
		model.addAttribute("sysDataImport", sysDataImport);
		return "admin/sys/data/sysDataImportQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("data:sysDataImport:view")
	@RequestMapping(value = "update")
	public String update(SysDataImport sysDataImport, Model model) {
		model.addAttribute("sysDataImport", sysDataImport);
		return "admin/sys/data/sysDataImportUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("data:sysDataImport:edit")
	@RequestMapping(value = "save")
	public String save(SysDataImport sysDataImport, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sysDataImport)){
			return add(sysDataImport, model);
		}
		sysDataImportService.save(sysDataImport);
		addMessage(redirectAttributes, "保存数据导入成功");
		return "redirect:"+Global.getAdminPath()+"/sysDataImport/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("data:sysDataImport:edit")
	@RequestMapping(value = "delete")
	public String delete(SysDataImport sysDataImport, RedirectAttributes redirectAttributes) {
		sysDataImportService.delete(sysDataImport);
		addMessage(redirectAttributes, "删除数据导入成功");
		return "redirect:"+Global.getAdminPath()+"/sysDataImport/?repage";
	}
	
	
	@RequiresPermissions("data:sysDataImport:edit")
	@RequestMapping(value = "createTask")
	public String createTask(SysDataImport sysDataImport, RedirectAttributes redirectAttributes){

		sysDataImportService.createTask(sysDataImport);
		addMessage(redirectAttributes, "数据导入任务创建成功");
		return "redirect:"+Global.getAdminPath()+"/sysDataImport/?repage";
	}
	
	@RequiresPermissions("data:sysDataImport:edit")
	@RequestMapping(value = "clearTask")
	public String clearTask(SysDataImport sysDataImport, RedirectAttributes redirectAttributes){

		sysDataImportService.clearTask(sysDataImport);
		addMessage(redirectAttributes, "清空任务成功");
		return "redirect:"+Global.getAdminPath()+"/sysDataImport/?repage";
	}
	
	

}