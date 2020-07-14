package com.jxf.web.admin.rc;

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
import com.jxf.rc.entity.RcCaYysDetails;
import com.jxf.rc.service.RcCaYysDetailsService;

/**
 * 信用档案运营商通话与花费详情Controller
 * @author lmy
 * @version 2018-12-17
 */
@Controller
@RequestMapping(value = "${adminPath}/ca/rcCaYysDetails")
public class RcCaYysDetailsController extends BaseController {

	@Autowired
	private RcCaYysDetailsService rcCaYysDetailsService;
	
	@ModelAttribute
	public RcCaYysDetails get(@RequestParam(required=false) Long id) {
		RcCaYysDetails entity = null;
		if (id!=null){
			entity = rcCaYysDetailsService.get(id);
		}
		if (entity == null){
			entity = new RcCaYysDetails();
		}
		return entity;
	}
	
	@RequiresPermissions("ca:rcCaYysDetails:view")
	@RequestMapping(value = {"list", ""})
	public String list(RcCaYysDetails rcCaYysDetails, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RcCaYysDetails> page = rcCaYysDetailsService.findPage(new Page<RcCaYysDetails>(request, response), rcCaYysDetails); 
		model.addAttribute("page", page);
		return "admin/rc/ca/rcCaYysDetailsList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("ca:rcCaYysDetails:view")
	@RequestMapping(value = "add")
	public String add(RcCaYysDetails rcCaYysDetails, Model model) {
		model.addAttribute("rcCaYysDetails", rcCaYysDetails);
		return "admin/rc/ca/rcCaYysDetailsAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("ca:rcCaYysDetails:view")
	@RequestMapping(value = "query")
	public String query(RcCaYysDetails rcCaYysDetails, Model model) {
		model.addAttribute("rcCaYysDetails", rcCaYysDetails);
		return "admin/rc/ca/rcCaYysDetailsQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("ca:rcCaYysDetails:view")
	@RequestMapping(value = "update")
	public String update(RcCaYysDetails rcCaYysDetails, Model model) {
		model.addAttribute("rcCaYysDetails", rcCaYysDetails);
		return "admin/rc/ca/rcCaYysDetailsUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("ca:rcCaYysDetails:edit")
	@RequestMapping(value = "save")
	public String save(RcCaYysDetails rcCaYysDetails, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, rcCaYysDetails)){
			return add(rcCaYysDetails, model);
		}
		rcCaYysDetailsService.save(rcCaYysDetails);
		addMessage(redirectAttributes, "保存信用档案运营商通话与花费详情成功");
		return "redirect:"+Global.getAdminPath()+"/ca/rcCaYysDetails/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("ca:rcCaYysDetails:edit")
	@RequestMapping(value = "delete")
	public String delete(RcCaYysDetails rcCaYysDetails, RedirectAttributes redirectAttributes) {
		rcCaYysDetailsService.delete(rcCaYysDetails);
		addMessage(redirectAttributes, "删除信用档案运营商通话与花费详情成功");
		return "redirect:"+Global.getAdminPath()+"/ca/rcCaYysDetails/?repage";
	}

}