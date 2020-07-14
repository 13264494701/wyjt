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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.cms.entity.CmsAd;
import com.jxf.cms.service.CmsAdService;
import com.jxf.svc.config.Global;
import com.jxf.svc.model.AjaxRsp;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.admin.sys.BaseController;


/**
 * 广告管理Controller
 * @author JINXINFU
 * @version 2016-04-25
 */
@Controller
@RequestMapping(value = "${adminPath}/ad")
public class CmsAdController extends BaseController {

	@Autowired
	private CmsAdService adService;
	
	@ModelAttribute
	public CmsAd get(@RequestParam(required=false) Long id) {
		CmsAd entity = null;
		if (id!=null){
			entity = adService.get(id);
		}
		if (entity == null){
			entity = new CmsAd();
		}
		return entity;
	}
	
	@RequiresPermissions("cms:ad:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsAd cmsAd, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsAd> page = adService.findPage(new Page<CmsAd>(request, response), cmsAd); 
		model.addAttribute("page", page);
		return "admin/cms/ad/cmsAdList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("cms:ad:view")
	@RequestMapping(value = "add")
	public String add(CmsAd cmsAd, Model model) {
		model.addAttribute("cmsAd", cmsAd);
		return "admin/cms/ad/cmsAdAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("cms:ad:view")
	@RequestMapping(value = "query")
	public String query(CmsAd cmsAd, Model model) {
		model.addAttribute("cmsAd", cmsAd);
		return "admin/cms/ad/cmsAdQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("cms:ad:view")
	@RequestMapping(value = "update")
	public String update(CmsAd cmsAd, Model model) {
		model.addAttribute("cmsAd", cmsAd);
		return "admin/cms/ad/cmsAdUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("cms:ad:edit")
	@RequestMapping(value = "save")
	public String save(CmsAd cmsAd, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsAd)){
			return add(cmsAd, model);
		}
		adService.save(cmsAd);
		addMessage(redirectAttributes, "保存广告管理成功");
		return "redirect:"+Global.getAdminPath()+"/ad/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("cms:ad:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsAd cmsAd, RedirectAttributes redirectAttributes) {
		adService.delete(cmsAd);
		addMessage(redirectAttributes, "删除广告管理成功");
		return "redirect:"+Global.getAdminPath()+"/ad/?repage";
	}
	/**
	 * 启用停用广告
	 * @param id
	 * @param sts
	 * @return
	 */
	@RequiresPermissions("cms:ad:edit")
	@RequestMapping(value = "turn")
	@ResponseBody
	public AjaxRsp turn(Long id,String sts){

		AjaxRsp rsp  = new AjaxRsp();
		CmsAd ad = adService.get(id);		
		if(StringUtils.equals("0", sts)){
			ad.setIsEnabled(false);;
			adService.save(ad);
			rsp.setStatus(true);
			rsp.setMessage("停用成功");
		}else{
			ad.setIsEnabled(true);;
			adService.save(ad);
			rsp.setStatus(true);
			rsp.setMessage("启用成功");		
		}
		return rsp;
	}
}