package com.jxf.web.admin.cms;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.cms.entity.CmsNotice;
import com.jxf.cms.service.CmsNoticeService;
import com.jxf.svc.config.Global;
import com.jxf.svc.model.Message;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.admin.sys.BaseController;


/**
 * 通知Controller
 * @author wo
 * @version 2018-10-04
 */
@Controller
@RequestMapping(value = "${adminPath}/cmsNotice")
public class CmsNoticeController extends BaseController {

	@Autowired
	private CmsNoticeService cmsNoticeService;
	
	@ModelAttribute
	public CmsNotice get(@RequestParam(required=false) Long id) {
		CmsNotice entity = null;
		if (id!=null){
			entity = cmsNoticeService.get(id);
		}
		if (entity == null){
			entity = new CmsNotice();
		}
		return entity;
	}
	
	@RequiresPermissions("notice:cmsNotice:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsNotice cmsNotice, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsNotice> page = cmsNoticeService.findPage(new Page<CmsNotice>(request, response), cmsNotice); 
		model.addAttribute("page", page);
		return "admin/cms/notice/cmsNoticeList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("notice:cmsNotice:view")
	@RequestMapping(value = "add")
	public String add(CmsNotice cmsNotice, Model model) {
		model.addAttribute("cmsNotice", cmsNotice);
		return "admin/cms/notice/cmsNoticeAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("notice:cmsNotice:view")
	@RequestMapping(value = "query")
	public String query(CmsNotice cmsNotice, Model model) {
		model.addAttribute("cmsNotice", cmsNotice);
		return "admin/cms/notice/cmsNoticeQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("notice:cmsNotice:view")
	@RequestMapping(value = "update")
	public String update(CmsNotice cmsNotice, Model model) {
		model.addAttribute("cmsNotice", cmsNotice);
		return "admin/cms/notice/cmsNoticeUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("notice:cmsNotice:edit")
	@RequestMapping(value = "save")
	public String save(CmsNotice cmsNotice, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsNotice)){
			return add(cmsNotice, model);
		}
		cmsNotice.setContent(StringEscapeUtils.unescapeHtml4(cmsNotice.getContent()));
		cmsNoticeService.save(cmsNotice);
		addMessage(redirectAttributes, "保存通知成功");
		return "redirect:"+Global.getAdminPath()+"/cmsNotice/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("notice:cmsNotice:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsNotice cmsNotice, RedirectAttributes redirectAttributes) {
		cmsNoticeService.delete(cmsNotice);
		addMessage(redirectAttributes, "删除通知成功");
		return "redirect:"+Global.getAdminPath()+"/cmsNotice/?repage";
	}
	/**
	 * 发布撤回通知
	 * @param id
	 * @param sts
	 * @return
	 */
	@RequiresPermissions("notice:cmsNotice:edit")
	@RequestMapping(value = "pub")
	@ResponseBody
	public Message pub(String id,String sts){


		if(StringUtils.equals("0", sts)){
			cmsNoticeService.unpub(id);
			return Message.success("撤回成功");
		}else{
			cmsNoticeService.pub(id);
			return Message.success("发布成功");
		}
	}
}