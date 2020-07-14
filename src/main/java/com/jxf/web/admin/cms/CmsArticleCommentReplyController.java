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

import com.jxf.cms.entity.CmsArticleCommentReply;
import com.jxf.cms.service.CmsArticleCommentReplyService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;


/**
 * 评论回复Controller
 * @author JINXINFU
 * @version 2017-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsArticleCommentReply")
public class CmsArticleCommentReplyController extends BaseController {

	@Autowired
	private CmsArticleCommentReplyService cmsArticleCommentReplyService;
	
	@ModelAttribute
	public CmsArticleCommentReply get(@RequestParam(required=false) Long id) {
		CmsArticleCommentReply entity = null;
		if (id!=null){
			entity = cmsArticleCommentReplyService.get(id);
		}
		if (entity == null){
			entity = new CmsArticleCommentReply();
		}
		return entity;
	}
	
	@RequiresPermissions("cms:cmsArticleCommentReply:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsArticleCommentReply cmsArticleCommentReply, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsArticleCommentReply> page = cmsArticleCommentReplyService.findPage(new Page<CmsArticleCommentReply>(request, response), cmsArticleCommentReply); 
		model.addAttribute("page", page);
		return "jhxx/cms/cmsArticleCommentReplyList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("cms:cmsArticleCommentReply:view")
	@RequestMapping(value = "add")
	public String add(CmsArticleCommentReply cmsArticleCommentReply, Model model) {
		model.addAttribute("cmsArticleCommentReply", cmsArticleCommentReply);
		return "jhxx/cms/cmsArticleCommentReplyAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("cms:cmsArticleCommentReply:view")
	@RequestMapping(value = "query")
	public String query(CmsArticleCommentReply cmsArticleCommentReply, Model model) {
		model.addAttribute("cmsArticleCommentReply", cmsArticleCommentReply);
		return "jhxx/cms/cmsArticleCommentReplyQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("cms:cmsArticleCommentReply:view")
	@RequestMapping(value = "update")
	public String update(CmsArticleCommentReply cmsArticleCommentReply, Model model) {
		model.addAttribute("cmsArticleCommentReply", cmsArticleCommentReply);
		return "jhxx/cms/cmsArticleCommentReplyUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("cms:cmsArticleCommentReply:edit")
	@RequestMapping(value = "save")
	public String save(CmsArticleCommentReply cmsArticleCommentReply, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsArticleCommentReply)){
			return add(cmsArticleCommentReply, model);
		}
		cmsArticleCommentReplyService.save(cmsArticleCommentReply);
		addMessage(redirectAttributes, "保存评论回复成功");
		return "redirect:"+Global.getAdminPath()+"/cms/cmsArticleCommentReply/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("cms:cmsArticleCommentReply:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsArticleCommentReply cmsArticleCommentReply, RedirectAttributes redirectAttributes) {
		cmsArticleCommentReplyService.delete(cmsArticleCommentReply);
		addMessage(redirectAttributes, "删除评论回复成功");
		return "redirect:"+Global.getAdminPath()+"/cms/cmsArticleCommentReply/?repage";
	}

}