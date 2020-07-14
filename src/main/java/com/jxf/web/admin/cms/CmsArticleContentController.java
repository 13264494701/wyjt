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

import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;
import com.jxf.cms.entity.CmsArticleContent;
import com.jxf.cms.service.CmsArticleContentService;

/**
 * 文章内容Controller
 * @author JINXINFU
 * @version 2016-11-25
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsArticleContent")
public class CmsArticleContentController extends BaseController {

	@Autowired
	private CmsArticleContentService cmsArticleContentService;
	
	@ModelAttribute
	public CmsArticleContent get(@RequestParam(required=false) Long id) {
		CmsArticleContent entity = null;
		if (id!=null){
			entity = cmsArticleContentService.get(id);
		}
		if (entity == null){
			entity = new CmsArticleContent();
		}
		return entity;
	}
	
	@RequiresPermissions("cms:cmsArticleContent:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsArticleContent cmsArticleContent, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsArticleContent> page = cmsArticleContentService.findPage(new Page<CmsArticleContent>(request, response), cmsArticleContent); 
		model.addAttribute("page", page);
		return "jhxx/cms/cmsArticleContentList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("cms:cmsArticleContent:view")
	@RequestMapping(value = "add")
	public String add(CmsArticleContent cmsArticleContent, Model model) {
		model.addAttribute("cmsArticleContent", cmsArticleContent);
		return "jhxx/cms/cmsArticleContentAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("cms:cmsArticleContent:view")
	@RequestMapping(value = "query")
	public String query(CmsArticleContent cmsArticleContent, Model model) {
		model.addAttribute("cmsArticleContent", cmsArticleContent);
		return "jhxx/cms/cmsArticleContentQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("cms:cmsArticleContent:view")
	@RequestMapping(value = "update")
	public String update(CmsArticleContent cmsArticleContent, Model model) {
		model.addAttribute("cmsArticleContent", cmsArticleContent);
		return "jhxx/cms/cmsArticleContentUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("cms:cmsArticleContent:edit")
	@RequestMapping(value = "save")
	public String save(CmsArticleContent cmsArticleContent, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsArticleContent)){
			return add(cmsArticleContent, model);
		}
		cmsArticleContentService.save(cmsArticleContent);
		addMessage(redirectAttributes, "保存文章内容成功");
		return "redirect:"+Global.getAdminPath()+"/cms/cmsArticleContent/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("cms:cmsArticleContent:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsArticleContent cmsArticleContent, RedirectAttributes redirectAttributes) {
		cmsArticleContentService.delete(cmsArticleContent);
		addMessage(redirectAttributes, "删除文章内容成功");
		return "redirect:"+Global.getAdminPath()+"/cms/cmsArticleContent/?repage";
	}

}