package com.jxf.web.admin.cms;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jxf.cms.service.CategoryService;
import com.jxf.web.admin.sys.BaseController;

/**
 * 内容管理Controller
 * @author jxf
 * @version 1.0 2015-07-10
 */
@Controller
@RequestMapping(value = "${adminPath}/cms")
public class CmsController extends BaseController {

	@Autowired
	private CategoryService categoryService;
	
	@RequiresPermissions("cms:view")
	@RequestMapping(value = "")
	public String index() {
		return "cms/cmsIndex";
	}
	
	@RequiresPermissions("cms:view")
	@RequestMapping(value = "tree")
	public String tree(Model model) {
		model.addAttribute("categoryList", categoryService.findByUser());
		return "cms/cmsTree";
	}
	
	@RequiresPermissions("cms:view")
	@RequestMapping(value = "none")
	public String none() {
		return "cms/cmsNone";
	}

}
