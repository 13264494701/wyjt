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
import com.jxf.cms.entity.CmsChannel;
import com.jxf.cms.service.CmsChannelService;

/**
 * 频道信息Controller
 * @author JINXINFU
 * @version 2016-11-20
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/channel")
public class CmsChannelController extends BaseController {

	@Autowired
	private CmsChannelService cmsChannelService;
	
	@ModelAttribute
	public CmsChannel get(@RequestParam(required=false) Long id) {
		CmsChannel entity = null;
		if (id!=null){
			entity = cmsChannelService.get(id);
		}
		if (entity == null){
			entity = new CmsChannel();
		}
		return entity;
	}
	
	@RequiresPermissions("cms:channel:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsChannel cmsChannel, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsChannel> page = cmsChannelService.findPage(new Page<CmsChannel>(request, response), cmsChannel); 
		model.addAttribute("page", page);
		return "cms/channel/cmsChannelList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("cms:channel:view")
	@RequestMapping(value = "add")
	public String add(CmsChannel cmsChannel, Model model) {
		cmsChannel.setInNav(true);
		cmsChannel.setAllowComment(true);
		cmsChannel.setIsAudit(false);
		model.addAttribute("cmsChannel", cmsChannel);
		return "cms/channel/cmsChannelAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("cms:channel:view")
	@RequestMapping(value = "query")
	public String query(CmsChannel cmsChannel, Model model) {
		model.addAttribute("cmsChannel", cmsChannel);
		return "cms/channel/cmsChannelQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("cms:channel:view")
	@RequestMapping(value = "update")
	public String update(CmsChannel cmsChannel, Model model) {
		model.addAttribute("cmsChannel", cmsChannel);
		return "cms/channel/cmsChannelUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("cms:channel:edit")
	@RequestMapping(value = "save")
	public String save(CmsChannel cmsChannel, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsChannel)){
			return add(cmsChannel, model);
		}
		cmsChannelService.save(cmsChannel);
		addMessage(redirectAttributes, "保存频道信息成功");
		return "redirect:"+Global.getAdminPath()+"/cms/channel/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("cms:channel:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsChannel cmsChannel, RedirectAttributes redirectAttributes) {
		cmsChannelService.delete(cmsChannel);
		addMessage(redirectAttributes, "删除频道信息成功");
		return "redirect:"+Global.getAdminPath()+"/cms/channel/?repage";
	}

}