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

import com.jxf.cms.entity.CmsSiteInfo;
import com.jxf.cms.service.CmsSiteInfoService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;


/**
 * 站点信息Controller
 * @author JINXINFU
 * @version 2016-11-20
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsSiteInfo")
public class CmsSiteInfoController extends BaseController {

	@Autowired
	private CmsSiteInfoService cmsSiteInfoService;
	
	@ModelAttribute
	public CmsSiteInfo get(@RequestParam(required=false) Long id) {
		CmsSiteInfo entity = null;
		if (id!=null){
			entity = cmsSiteInfoService.get(id);
		}
		if (entity == null){
			entity = new CmsSiteInfo();
		}
		return entity;
	}
	
	@RequiresPermissions("cms:cmsSiteInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsSiteInfo cmsSiteInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsSiteInfo> page = cmsSiteInfoService.findPage(new Page<CmsSiteInfo>(request, response), cmsSiteInfo); 
		model.addAttribute("page", page);
		return "cms/siteinfo/cmsSiteInfoList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("cms:cmsSiteInfo:view")
	@RequestMapping(value = "add")
	public String add(CmsSiteInfo cmsSiteInfo, Model model) {
		model.addAttribute("cmsSiteInfo", cmsSiteInfo);
		return "cms/siteinfo/cmsSiteInfoAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("cms:cmsSiteInfo:view")
	@RequestMapping(value = "query")
	public String query(CmsSiteInfo cmsSiteInfo, Model model) {
		model.addAttribute("cmsSiteInfo", cmsSiteInfo);
		return "cms/siteinfo/cmsSiteInfoQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("cms:cmsSiteInfo:view")
	@RequestMapping(value = "update")
	public String update(CmsSiteInfo cmsSiteInfo, Model model) {
		model.addAttribute("cmsSiteInfo", cmsSiteInfo);
		return "cms/siteinfo/cmsSiteInfoUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("cms:cmsSiteInfo:edit")
	@RequestMapping(value = "save")
	public String save(CmsSiteInfo cmsSiteInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsSiteInfo)){
			return add(cmsSiteInfo, model);
		}
		cmsSiteInfoService.save(cmsSiteInfo);
		addMessage(redirectAttributes, "保存站点信息成功");
		return "redirect:"+Global.getAdminPath()+"/cms/cmsSiteInfo/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("cms:cmsSiteInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsSiteInfo cmsSiteInfo, RedirectAttributes redirectAttributes) {
		cmsSiteInfoService.delete(cmsSiteInfo);
		addMessage(redirectAttributes, "删除站点信息成功");
		return "redirect:"+Global.getAdminPath()+"/cms/cmsSiteInfo/?repage";
	}

}