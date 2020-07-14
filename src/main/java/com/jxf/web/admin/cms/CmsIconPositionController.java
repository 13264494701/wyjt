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

import com.jxf.cms.entity.CmsIconPosition;
import com.jxf.cms.service.CmsIconPositionService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;


/**
 * 图标位置Controller
 * @author HUOJIABAO
 * @version 2016-07-23
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/iconPosition")
public class CmsIconPositionController extends BaseController {

	@Autowired
	private CmsIconPositionService iconPositionService;
	
	@ModelAttribute
	public CmsIconPosition get(@RequestParam(required=false) Long id) {
		CmsIconPosition entity = null;
		if (id!=null){
			entity = iconPositionService.get(id);
		}
		if (entity == null){
			entity = new CmsIconPosition();
		}
		return entity;
	}
	
	@RequiresPermissions("cms:iconPosition:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsIconPosition iconPosition, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsIconPosition> page = iconPositionService.findPage(new Page<CmsIconPosition>(request, response), iconPosition); 
		model.addAttribute("page", page);
		return "admin/cms/iconposition/iconPositionList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("cms:iconPosition:view")
	@RequestMapping(value = "add")
	public String add(CmsIconPosition iconPosition, Model model) {
		model.addAttribute("iconPosition", iconPosition);
		return "admin/cms/iconposition/iconPositionAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("cms:iconPosition:view")
	@RequestMapping(value = "query")
	public String query(CmsIconPosition iconPosition, Model model) {
		model.addAttribute("iconPosition", iconPosition);
		return "admin/cms/iconposition/iconPositionQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("cms:iconPosition:view")
	@RequestMapping(value = "update")
	public String update(CmsIconPosition iconPosition, Model model) {
		model.addAttribute("iconPosition", iconPosition);
		return "admin/cms/iconposition/iconPositionUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("cms:iconPosition:edit")
	@RequestMapping(value = "save")
	public String save(CmsIconPosition iconPosition, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, iconPosition)){
			return add(iconPosition, model);
		}
		iconPositionService.save(iconPosition);
		addMessage(redirectAttributes, "保存图标位置成功");
		return "redirect:"+Global.getAdminPath()+"/cms/iconPosition/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("cms:iconPosition:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsIconPosition iconPosition, RedirectAttributes redirectAttributes) {
		iconPositionService.delete(iconPosition);
		addMessage(redirectAttributes, "删除图标位置成功");
		return "redirect:"+Global.getAdminPath()+"/cms/iconPosition/?repage";
	}

}