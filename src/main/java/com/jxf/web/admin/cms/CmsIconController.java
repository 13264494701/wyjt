package com.jxf.web.admin.cms;

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

import com.jxf.cms.entity.CmsIcon;
import com.jxf.cms.service.CmsIconService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;


/**
 * 图标Controller
 * @author HUOJIABAO
 * @version 2016-07-23
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/icon")
public class CmsIconController extends BaseController {

	@Autowired
	private CmsIconService iconService;
	
	@ModelAttribute
	public CmsIcon get(@RequestParam(required=false) Long id) {
		CmsIcon entity = null;
		if (id!=null){
			entity = iconService.get(id);
		}
		if (entity == null){
			entity = new CmsIcon();
		}
		return entity;
	}
	
	@RequiresPermissions("cms:icon:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsIcon icon, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsIcon> page = iconService.findPage(new Page<CmsIcon>(request, response), icon); 
		model.addAttribute("page", page);
		return "admin/cms/icon/iconList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("cms:icon:view")
	@RequestMapping(value = "add")
	public String add(CmsIcon icon, Model model) {
		model.addAttribute("icon", icon);
		return "admin/cms/icon/iconAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("cms:icon:view")
	@RequestMapping(value = "query")
	public String query(CmsIcon icon, Model model) {
		model.addAttribute("icon", icon);
		return "admin/cms/icon/iconQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("cms:icon:view")
	@RequestMapping(value = "update")
	public String update(CmsIcon icon, Model model) {
		model.addAttribute("icon", icon);
		return "admin/cms/icon/iconUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("cms:icon:edit")
	@RequestMapping(value = "save")
	public String save(CmsIcon icon, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, icon)){
			return add(icon, model);
		}
		iconService.save(icon);
		addMessage(redirectAttributes, "保存图标成功");
		return "redirect:"+Global.getAdminPath()+"/cms/icon/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("cms:icon:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsIcon icon, RedirectAttributes redirectAttributes) {
		iconService.delete(icon);
		addMessage(redirectAttributes, "删除图标成功");
		return "redirect:"+Global.getAdminPath()+"/cms/icon/?repage";
	}

}