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

import com.jxf.cms.entity.CmsArticleComment;
import com.jxf.cms.service.CmsArticleCommentService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;


/**
 * 文章评论Controller
 * @author JINXINFU
 * @version 2017-01-01
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsArticleComment")
public class CmsArticleCommentController extends BaseController {

	@Autowired
	private CmsArticleCommentService cmsArticleCommentService;
	
	@ModelAttribute
	public CmsArticleComment get(@RequestParam(required=false) Long id) {
		CmsArticleComment entity = null;
		if (id!=null){
			entity = cmsArticleCommentService.get(id);
		}
		if (entity == null){
			entity = new CmsArticleComment();
		}
		return entity;
	}
	
	@RequiresPermissions("cms:cmsArticleComment:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsArticleComment cmsArticleComment, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsArticleComment> page = cmsArticleCommentService.findPage(new Page<CmsArticleComment>(request, response), cmsArticleComment); 
		model.addAttribute("page", page);
		return "cms/comment/cmsArticleCommentList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("cms:cmsArticleComment:view")
	@RequestMapping(value = "add")
	public String add(CmsArticleComment cmsArticleComment, Model model) {
		model.addAttribute("cmsArticleComment", cmsArticleComment);
		return "cms/comment/cmsArticleCommentAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("cms:cmsArticleComment:view")
	@RequestMapping(value = "query")
	public String query(CmsArticleComment cmsArticleComment, Model model) {
		model.addAttribute("cmsArticleComment", cmsArticleComment);
		return "cms/comment/cmsArticleCommentQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("cms:cmsArticleComment:view")
	@RequestMapping(value = "update")
	public String update(CmsArticleComment cmsArticleComment, Model model) {
		model.addAttribute("cmsArticleComment", cmsArticleComment);
		return "cms/comment/cmsArticleCommentUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("cms:cmsArticleComment:edit")
	@RequestMapping(value = "save")
	public String save(CmsArticleComment cmsArticleComment, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsArticleComment)){
			return add(cmsArticleComment, model);
		}
		cmsArticleCommentService.save(cmsArticleComment);
		addMessage(redirectAttributes, "保存文章评论成功");
		return "redirect:"+Global.getAdminPath()+"/cms/cmsArticleComment/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("cms:cmsArticleComment:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsArticleComment cmsArticleComment, RedirectAttributes redirectAttributes) {
		cmsArticleCommentService.delete(cmsArticleComment);
		addMessage(redirectAttributes, "删除文章评论成功");
		return "redirect:"+Global.getAdminPath()+"/cms/cmsArticleComment/?repage";
	}

}