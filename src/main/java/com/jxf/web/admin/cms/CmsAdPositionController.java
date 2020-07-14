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

import com.jxf.cms.entity.CmsAdPosition;
import com.jxf.cms.service.CmsAdPositionService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;

/**
 * 广告位置Controller
 * @author JINXINFU
 * @version 2016-04-25
 */
@Controller
@RequestMapping(value = "${adminPath}/adPosition")
public class CmsAdPositionController extends BaseController {

	@Autowired
	private CmsAdPositionService adPositionService;
	
	@ModelAttribute
	public CmsAdPosition get(@RequestParam(required=false) Long id) {
		CmsAdPosition entity = null;
		if (id!=null){
			entity = adPositionService.get(id);
		}
		if (entity == null){
			entity = new CmsAdPosition();
		}
		return entity;
	}
	
	@RequiresPermissions("cms:adPosition:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsAdPosition adsCmsAdPosition, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsAdPosition> page = adPositionService.findPage(new Page<CmsAdPosition>(request, response), adsCmsAdPosition); 
		model.addAttribute("page", page);
		return "admin/cms/adposition/cmsAdPositionList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("cms:adPosition:view")
	@RequestMapping(value = "add")
	public String add(CmsAdPosition cmsAdPosition, Model model) {
		model.addAttribute("cmsAdPosition", cmsAdPosition);
		return "admin/cms/adposition/cmsAdPositionAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("cms:adPosition:view")
	@RequestMapping(value = "query")
	public String query(CmsAdPosition cmsAdPosition, Model model) {
		model.addAttribute("cmsAdPosition", cmsAdPosition);
		return "admin/cms/adposition/cmsAdPositionQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("cms:adPosition:view")
	@RequestMapping(value = "update")
	public String update(CmsAdPosition cmsAdPosition, Model model) {
		model.addAttribute("cmsAdPosition", cmsAdPosition);
		return "admin/cms/adposition/cmsAdPositionUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("cms:adPosition:edit")
	@RequestMapping(value = "save")
	public String save(CmsAdPosition adsCmsAdPosition, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, adsCmsAdPosition)){
			return add(adsCmsAdPosition, model);
		}
		adPositionService.save(adsCmsAdPosition);
		addMessage(redirectAttributes, "保存广告位置成功");
		return "redirect:"+Global.getAdminPath()+"/adPosition/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("cms:adPosition:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsAdPosition adsCmsAdPosition, RedirectAttributes redirectAttributes) {
		adPositionService.delete(adsCmsAdPosition);
		addMessage(redirectAttributes, "删除广告位置成功");
		return "redirect:"+Global.getAdminPath()+"/adPosition/?repage";
	}

}