package com.jxf.web.admin.mms;

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
import com.jxf.mms.tmpl.entity.MmsMsgTmpl;
import com.jxf.mms.tmpl.service.MmsMsgTmplService;

/**
 * 消息模板Controller
 * @author wo
 * @version 2018-10-28
 */
@Controller
@RequestMapping(value = "${adminPath}/msgTmpl")
public class MmsMsgTmplController extends BaseController {

	@Autowired
	private MmsMsgTmplService mmsMsgTmplService;
	
	@ModelAttribute
	public MmsMsgTmpl get(@RequestParam(required=false) Long id) {
		MmsMsgTmpl entity = null;
		if (id!=null){
			entity = mmsMsgTmplService.get(id);
		}
		if (entity == null){
			entity = new MmsMsgTmpl();
		}
		return entity;
	}
	
	@RequiresPermissions("tmpl:mmsMsgTmpl:view")
	@RequestMapping(value = {"list", ""})
	public String list(MmsMsgTmpl mmsMsgTmpl, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MmsMsgTmpl> page = mmsMsgTmplService.findPage(new Page<MmsMsgTmpl>(request, response), mmsMsgTmpl); 
		model.addAttribute("page", page);
		return "admin/mms/tmpl/mmsMsgTmplList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("tmpl:mmsMsgTmpl:view")
	@RequestMapping(value = "add")
	public String add(MmsMsgTmpl mmsMsgTmpl, Model model) {
		model.addAttribute("mmsMsgTmpl", mmsMsgTmpl);
		return "admin/mms/tmpl/mmsMsgTmplAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("tmpl:mmsMsgTmpl:view")
	@RequestMapping(value = "query")
	public String query(MmsMsgTmpl mmsMsgTmpl, Model model) {
		model.addAttribute("mmsMsgTmpl", mmsMsgTmpl);
		return "admin/mms/tmpl/mmsMsgTmplQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("tmpl:mmsMsgTmpl:view")
	@RequestMapping(value = "update")
	public String update(MmsMsgTmpl mmsMsgTmpl, Model model) {
		model.addAttribute("mmsMsgTmpl", mmsMsgTmpl);
		return "admin/mms/tmpl/mmsMsgTmplUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("tmpl:mmsMsgTmpl:edit")
	@RequestMapping(value = "save")
	public String save(MmsMsgTmpl mmsMsgTmpl, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mmsMsgTmpl)){
			return add(mmsMsgTmpl, model);
		}
		mmsMsgTmplService.save(mmsMsgTmpl);
		addMessage(redirectAttributes, "保存消息模板成功");
		return "redirect:"+Global.getAdminPath()+"/msgTmpl/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("tmpl:mmsMsgTmpl:edit")
	@RequestMapping(value = "delete")
	public String delete(MmsMsgTmpl mmsMsgTmpl, RedirectAttributes redirectAttributes) {
		mmsMsgTmplService.delete(mmsMsgTmpl);
		addMessage(redirectAttributes, "删除消息模板成功");
		return "redirect:"+Global.getAdminPath()+"/msgTmpl/?repage";
	}

}