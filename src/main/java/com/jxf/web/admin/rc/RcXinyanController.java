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
import com.jxf.rc.entity.RcXinyan;
import com.jxf.rc.service.RcXinyanService;

/**
 * 新颜雷达报告Controller
 * @author lmy
 * @version 2018-12-18
 */
@Controller
@RequestMapping(value = "${adminPath}/ca/rcXinyan")
public class RcXinyanController extends BaseController {

	@Autowired
	private RcXinyanService rcXinyanService;
	
	@ModelAttribute
	public RcXinyan get(@RequestParam(required=false) Long id) {
		RcXinyan entity = null;
		if (id!=null){
			entity = rcXinyanService.get(id);
		}
		if (entity == null){
			entity = new RcXinyan();
		}
		return entity;
	}
	
	@RequiresPermissions("ca:rcXinyan:view")
	@RequestMapping(value = {"list", ""})
	public String list(RcXinyan rcXinyan, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RcXinyan> page = rcXinyanService.findPage(new Page<RcXinyan>(request, response), rcXinyan); 
		model.addAttribute("page", page);
		return "admin/rc/ca/rcXinyanList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("ca:rcXinyan:view")
	@RequestMapping(value = "add")
	public String add(RcXinyan rcXinyan, Model model) {
		model.addAttribute("rcXinyan", rcXinyan);
		return "admin/rc/ca/rcXinyanAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("ca:rcXinyan:view")
	@RequestMapping(value = "query")
	public String query(RcXinyan rcXinyan, Model model) {
		model.addAttribute("rcXinyan", rcXinyan);
		return "admin/rc/ca/rcXinyanQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("ca:rcXinyan:view")
	@RequestMapping(value = "update")
	public String update(RcXinyan rcXinyan, Model model) {
		model.addAttribute("rcXinyan", rcXinyan);
		return "admin/rc/ca/rcXinyanUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("ca:rcXinyan:edit")
	@RequestMapping(value = "save")
	public String save(RcXinyan rcXinyan, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, rcXinyan)){
			return add(rcXinyan, model);
		}
		rcXinyanService.save(rcXinyan);
		addMessage(redirectAttributes, "保存新颜雷达报告成功");
		return "redirect:"+Global.getAdminPath()+"/ca/rcXinyan/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("ca:rcXinyan:edit")
	@RequestMapping(value = "delete")
	public String delete(RcXinyan rcXinyan, RedirectAttributes redirectAttributes) {
		rcXinyanService.delete(rcXinyan);
		addMessage(redirectAttributes, "删除新颜雷达报告成功");
		return "redirect:"+Global.getAdminPath()+"/ca/rcXinyan/?repage";
	}

}