package com.jxf.web.ufang;

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
import com.jxf.ufang.entity.ZZCUpload;
import com.jxf.ufang.service.ZZCUploadService;
import com.jxf.web.admin.sys.BaseController;

/**
 * 中智诚上报Controller
 * @author XIAORONGDIAN
 * @version 2019-04-22
 */
@Controller
@RequestMapping(value = "${adminPath}/ufang/zZCUpload")
public class ZZCUploadController extends BaseController {

	@Autowired
	private ZZCUploadService zZCUploadService;
	
	@ModelAttribute
	public ZZCUpload get(@RequestParam(required=false) Long id) {
		ZZCUpload entity = null;
		if (id!=null){
			entity = zZCUploadService.get(id);
		}
		if (entity == null){
			entity = new ZZCUpload();
		}
		return entity;
	}
	
	@RequiresPermissions("ufang:zZCUpload:view")
	@RequestMapping(value = {"list", ""})
	public String list(ZZCUpload zZCUpload, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ZZCUpload> page = zZCUploadService.findPage(new Page<ZZCUpload>(request, response), zZCUpload); 
		model.addAttribute("page", page);
		return "ufang/ufang/zZCUploadList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("ufang:zZCUpload:view")
	@RequestMapping(value = "add")
	public String add(ZZCUpload zZCUpload, Model model) {
		model.addAttribute("zZCUpload", zZCUpload);
		return "ufang/ufang/zZCUploadAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("ufang:zZCUpload:view")
	@RequestMapping(value = "query")
	public String query(ZZCUpload zZCUpload, Model model) {
		model.addAttribute("zZCUpload", zZCUpload);
		return "ufang/ufang/zZCUploadQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("ufang:zZCUpload:view")
	@RequestMapping(value = "update")
	public String update(ZZCUpload zZCUpload, Model model) {
		model.addAttribute("zZCUpload", zZCUpload);
		return "ufang/ufang/zZCUploadUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("ufang:zZCUpload:edit")
	@RequestMapping(value = "save")
	public String save(ZZCUpload zZCUpload, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, zZCUpload)){
			return add(zZCUpload, model);
		}
		zZCUploadService.save(zZCUpload);
		addMessage(redirectAttributes, "保存中智诚上报成功");
		return "redirect:"+Global.getAdminPath()+"/ufang/zZCUpload/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("ufang:zZCUpload:edit")
	@RequestMapping(value = "delete")
	public String delete(ZZCUpload zZCUpload, RedirectAttributes redirectAttributes) {
		zZCUploadService.delete(zZCUpload);
		addMessage(redirectAttributes, "删除中智诚上报成功");
		return "redirect:"+Global.getAdminPath()+"/ufang/zZCUpload/?repage";
	}

}