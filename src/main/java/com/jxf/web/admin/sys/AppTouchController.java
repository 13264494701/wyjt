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
import com.jxf.svc.sys.app.entity.AppTouch;
import com.jxf.svc.sys.app.service.AppTouchService;


/**
 * 应用曝光Controller
 * @author wo
 * @version 2019-07-09
 */
@Controller
@RequestMapping(value = "${adminPath}/appTouch")
public class AppTouchController extends BaseController {

	@Autowired
	private AppTouchService appTouchService;
	
	@ModelAttribute
	public AppTouch get(@RequestParam(required=false) Long id) {
		AppTouch entity = null;
		if (id!=null){
			entity = appTouchService.get(id);
		}
		if (entity == null){
			entity = new AppTouch();
		}
		return entity;
	}
	
	@RequiresPermissions("app:appTouch:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppTouch appTouch, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AppTouch> page = appTouchService.findPage(new Page<AppTouch>(request, response), appTouch); 
		model.addAttribute("page", page);
		return "sys/app/appTouchList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("app:appTouch:view")
	@RequestMapping(value = "add")
	public String add(AppTouch appTouch, Model model) {
		model.addAttribute("appTouch", appTouch);
		return "sys/app/appTouchAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("app:appTouch:view")
	@RequestMapping(value = "query")
	public String query(AppTouch appTouch, Model model) {
		model.addAttribute("appTouch", appTouch);
		return "sys/app/appTouchQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("app:appTouch:view")
	@RequestMapping(value = "update")
	public String update(AppTouch appTouch, Model model) {
		model.addAttribute("appTouch", appTouch);
		return "sys/app/appTouchUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("app:appTouch:edit")
	@RequestMapping(value = "save")
	public String save(AppTouch appTouch, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, appTouch)){
			return add(appTouch, model);
		}
		appTouchService.save(appTouch);
		addMessage(redirectAttributes, "保存应用曝光成功");
		return "redirect:"+Global.getAdminPath()+"/appTouch/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("app:appTouch:edit")
	@RequestMapping(value = "delete")
	public String delete(AppTouch appTouch, RedirectAttributes redirectAttributes) {
		appTouchService.delete(appTouch);
		addMessage(redirectAttributes, "删除应用曝光成功");
		return "redirect:"+Global.getAdminPath()+"/appTouch/?repage";
	}

}