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

import com.jxf.cms.entity.CmsContTmpl;
import com.jxf.cms.service.CmsContTmplService;
import com.jxf.cms.service.GenerateStaticService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;
import com.jxf.web.model.ResponseData;


/**
 * 合同模板Controller
 * @author huojiayuan
 * @version 2016-12-01
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/contTmpl")
public class CmsContTmplController extends BaseController {

	@Autowired
	private GenerateStaticService staticService;
	@Autowired
	private CmsContTmplService contTmplService;
	
	@ModelAttribute
	public CmsContTmpl get(@RequestParam(required=false) Long id) {
		CmsContTmpl entity = null;
		if (id!=null){
			entity = contTmplService.get(id);
		}
		if (entity == null){
			entity = new CmsContTmpl();
		}
		return entity;
	}
	
	@RequiresPermissions("cont:cmsContTmpl:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsContTmpl cmsContTmpl, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsContTmpl> page = contTmplService.findPage(new Page<CmsContTmpl>(request, response), cmsContTmpl); 
		model.addAttribute("page", page);
		return "admin/cms/cont/cmsContTmplList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("cont:cmsContTmpl:view")
	@RequestMapping(value = "add")
	public String add(CmsContTmpl cmsContTmpl, Model model) {
		model.addAttribute("cmsContTmpl", cmsContTmpl);
		return "admin/cms/cont/cmsContTmplAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("cont:cmsContTmpl:view")
	@RequestMapping(value = "query")
	public String query(CmsContTmpl cmsContTmpl, Model model) {
		model.addAttribute("cmsContTmpl", cmsContTmpl);
		return "admin/cms/cont/cmsContTmplQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("cont:cmsContTmpl:view")
	@RequestMapping(value = "update")
	public String update(CmsContTmpl cmsContTmpl, Model model) {
		model.addAttribute("cmsContTmpl", cmsContTmpl);
		return "admin/cms/cont/cmsContTmplUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("cont:cmsContTmpl:edit")
	@RequestMapping(value = "save")
	public String save(CmsContTmpl cmsContTmpl, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsContTmpl)){
			return add(cmsContTmpl, model);
		}
		contTmplService.save(cmsContTmpl);
		addMessage(redirectAttributes, "保存协议模板成功");
		return "redirect:"+Global.getAdminPath()+"/cms/contTmpl/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("cont:cmsContTmpl:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsContTmpl cmsContTmpl, RedirectAttributes redirectAttributes) {
		contTmplService.delete(cmsContTmpl);
		addMessage(redirectAttributes, "删除协议模板成功");
		return "redirect:"+Global.getAdminPath()+"/cms/contTmpl/?repage";
	}
	
	/**
	 * 生成静态页面
	 * @param id
	 * @return
	 */
	@RequiresPermissions("cont:cmsContTmpl:edit")
	@RequestMapping(value = "genstatic")
	@ResponseBody
	public ResponseData genstatic(Long id){
		CmsContTmpl contTmpl = contTmplService.get(id);		
		staticService.generate(contTmpl);
		contTmplService.setStatic(contTmpl);
		return ResponseData.success("生成静态页面成功");
	}

}