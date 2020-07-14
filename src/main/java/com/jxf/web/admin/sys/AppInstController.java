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
import com.jxf.svc.sys.app.entity.AppInst;
import com.jxf.svc.sys.app.service.AppInstService;


/**
 * 应用激活Controller
 * @author wo
 * @version 2019-07-09
 */
@Controller
@RequestMapping(value = "${adminPath}/appInst")
public class AppInstController extends BaseController {

	@Autowired
	private AppInstService appInstService;
	
	@ModelAttribute
	public AppInst get(@RequestParam(required=false) Long id) {
		AppInst entity = null;
		if (id!=null){
			entity = appInstService.get(id);
		}
		if (entity == null){
			entity = new AppInst();
		}
		return entity;
	}
	
	@RequiresPermissions("app:appInst:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppInst appInst, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AppInst> page = appInstService.findPage(new Page<AppInst>(request, response), appInst); 
		model.addAttribute("page", page);
		return "sys/app/appInstList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("app:appInst:view")
	@RequestMapping(value = "add")
	public String add(AppInst appInst, Model model) {
		model.addAttribute("appInst", appInst);
		return "sys/app/appInstAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("app:appInst:view")
	@RequestMapping(value = "query")
	public String query(AppInst appInst, Model model) {
		model.addAttribute("appInst", appInst);
		return "sys/app/appInstQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("app:appInst:view")
	@RequestMapping(value = "update")
	public String update(AppInst appInst, Model model) {
		model.addAttribute("appInst", appInst);
		return "sys/app/appInstUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("app:appInst:edit")
	@RequestMapping(value = "save")
	public String save(AppInst appInst, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, appInst)){
			return add(appInst, model);
		}
		appInstService.save(appInst);
		addMessage(redirectAttributes, "保存应用激活成功");
		return "redirect:"+Global.getAdminPath()+"/appInst/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("app:appInst:edit")
	@RequestMapping(value = "delete")
	public String delete(AppInst appInst, RedirectAttributes redirectAttributes) {
		appInstService.delete(appInst);
		addMessage(redirectAttributes, "删除应用激活成功");
		return "redirect:"+Global.getAdminPath()+"/appInst/?repage";
	}

}