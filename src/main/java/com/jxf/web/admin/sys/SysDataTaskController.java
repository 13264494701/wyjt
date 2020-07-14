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
import com.jxf.svc.sys.data.entity.SysDataTask;
import com.jxf.svc.sys.data.service.SysDataTaskService;


/**
 * 批次任务Controller
 * @author wo
 * @version 2019-01-12
 */
@Controller
@RequestMapping(value = "${adminPath}/sysDataTask")
public class SysDataTaskController extends BaseController {

	@Autowired
	private SysDataTaskService sysDataTaskService;
	
	@ModelAttribute
	public SysDataTask get(@RequestParam(required=false) Long id) {
		SysDataTask entity = null;
		if (id!=null){
			entity = sysDataTaskService.get(id);
		}
		if (entity == null){
			entity = new SysDataTask();
		}
		return entity;
	}
	
	@RequiresPermissions("task:sysDataTask:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysDataTask sysDataTask, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysDataTask> page = sysDataTaskService.findPage(new Page<SysDataTask>(request, response), sysDataTask); 
		model.addAttribute("page", page);
		model.addAttribute("dataId", sysDataTask.getData().getId());
		return "admin/sys/data/task/sysDataTaskList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("task:sysDataTask:view")
	@RequestMapping(value = "add")
	public String add(SysDataTask sysDataTask, Model model) {
		model.addAttribute("sysDataTask", sysDataTask);
		return "admin/sys/data/task/sysDataTaskAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("task:sysDataTask:view")
	@RequestMapping(value = "query")
	public String query(SysDataTask sysDataTask, Model model) {
		model.addAttribute("sysDataTask", sysDataTask);
		return "admin/sys/data/task/sysDataTaskQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("task:sysDataTask:view")
	@RequestMapping(value = "update")
	public String update(SysDataTask sysDataTask, Model model) {
		model.addAttribute("sysDataTask", sysDataTask);
		return "admin/sys/data/task/sysDataTaskUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("task:sysDataTask:edit")
	@RequestMapping(value = "save")
	public String save(SysDataTask sysDataTask, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sysDataTask)){
			return add(sysDataTask, model);
		}
		sysDataTaskService.save(sysDataTask);
		addMessage(redirectAttributes, "保存批次任务成功");
		return "redirect:"+Global.getAdminPath()+"/sysDataTask/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("task:sysDataTask:edit")
	@RequestMapping(value = "delete")
	public String delete(SysDataTask sysDataTask, RedirectAttributes redirectAttributes) {
		sysDataTaskService.delete(sysDataTask);
		addMessage(redirectAttributes, "删除批次任务成功");
		return "redirect:"+Global.getAdminPath()+"/sysDataTask/?repage";
	}

}