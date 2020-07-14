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

import com.jxf.rc.entity.RcQuota;
import com.jxf.rc.service.RcQuotaService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;

/**
 * 会员额度评估Controller
 * @author SuHuimin
 * @version 2019-08-23
 */
@Controller
@RequestMapping(value = "${adminPath}/quota/quota/rcQuota")
public class RcQuotaController extends BaseController {

	@Autowired
	private RcQuotaService rcQuotaService;
	
	@ModelAttribute
	public RcQuota get(@RequestParam(required=false) Long id) {
		RcQuota entity = null;
		if (id!=null){
			entity = rcQuotaService.get(id);
		}
		if (entity == null){
			entity = new RcQuota();
		}
		return entity;
	}
	
	@RequiresPermissions("quota:quota:rcQuota:view")
	@RequestMapping(value = {"list", ""})
	public String list(RcQuota rcQuota, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RcQuota> page = rcQuotaService.findPage(new Page<RcQuota>(request, response), rcQuota); 
		model.addAttribute("page", page);
		return "rc/quota/quota/rcQuotaList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("quota:quota:rcQuota:view")
	@RequestMapping(value = "add")
	public String add(RcQuota rcQuota, Model model) {
		model.addAttribute("rcQuota", rcQuota);
		return "rc/quota/quota/rcQuotaAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("quota:quota:rcQuota:view")
	@RequestMapping(value = "query")
	public String query(RcQuota rcQuota, Model model) {
		model.addAttribute("rcQuota", rcQuota);
		return "rc/quota/quota/rcQuotaQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("quota:quota:rcQuota:view")
	@RequestMapping(value = "update")
	public String update(RcQuota rcQuota, Model model) {
		model.addAttribute("rcQuota", rcQuota);
		return "rc/quota/quota/rcQuotaUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("quota:quota:rcQuota:edit")
	@RequestMapping(value = "save")
	public String save(RcQuota rcQuota, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, rcQuota)){
			return add(rcQuota, model);
		}
		rcQuotaService.save(rcQuota);
		addMessage(redirectAttributes, "保存会员额度成功");
		return "redirect:"+Global.getAdminPath()+"/quota/quota/rcQuota/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("quota:quota:rcQuota:edit")
	@RequestMapping(value = "delete")
	public String delete(RcQuota rcQuota, RedirectAttributes redirectAttributes) {
		rcQuotaService.delete(rcQuota);
		addMessage(redirectAttributes, "删除会员额度成功");
		return "redirect:"+Global.getAdminPath()+"/quota/quota/rcQuota/?repage";
	}

}