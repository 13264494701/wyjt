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
import com.jxf.rc.entity.RcCaSourceData;
import com.jxf.rc.service.RcCaSourceDataService;

/**
 * 信用报告原始数据表Controller
 * @author lmy
 * @version 2018-12-17
 */
@Controller
@RequestMapping(value = "${adminPath}/ca/rcCaSourceData")
public class RcCaSourceDataController extends BaseController {

	@Autowired
	private RcCaSourceDataService rcCaSourceDataService;
	
	@ModelAttribute
	public RcCaSourceData get(@RequestParam(required=false) Long id) {
		RcCaSourceData entity = null;
		if (id!=null){
			entity = rcCaSourceDataService.get(id);
		}
		if (entity == null){
			entity = new RcCaSourceData();
		}
		return entity;
	}
	
	@RequiresPermissions("ca:rcCaSourceData:view")
	@RequestMapping(value = {"list", ""})
	public String list(RcCaSourceData rcCaSourceData, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RcCaSourceData> page = rcCaSourceDataService.findPage(new Page<RcCaSourceData>(request, response), rcCaSourceData); 
		model.addAttribute("page", page);
		return "admin/rc/ca/rcCaSourceDataList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("ca:rcCaSourceData:view")
	@RequestMapping(value = "add")
	public String add(RcCaSourceData rcCaSourceData, Model model) {
		model.addAttribute("rcCaSourceData", rcCaSourceData);
		return "admin/rc/ca/rcCaSourceDataAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("ca:rcCaSourceData:view")
	@RequestMapping(value = "query")
	public String query(RcCaSourceData rcCaSourceData, Model model) {
		model.addAttribute("rcCaSourceData", rcCaSourceData);
		return "admin/rc/ca/rcCaSourceDataQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("ca:rcCaSourceData:view")
	@RequestMapping(value = "update")
	public String update(RcCaSourceData rcCaSourceData, Model model) {
		model.addAttribute("rcCaSourceData", rcCaSourceData);
		return "admin/rc/ca/rcCaSourceDataUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("ca:rcCaSourceData:edit")
	@RequestMapping(value = "save")
	public String save(RcCaSourceData rcCaSourceData, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, rcCaSourceData)){
			return add(rcCaSourceData, model);
		}
		rcCaSourceDataService.save(rcCaSourceData);
		addMessage(redirectAttributes, "保存信用报告原始数据表成功");
		return "redirect:"+Global.getAdminPath()+"/ca/rcCaSourceData/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("ca:rcCaSourceData:edit")
	@RequestMapping(value = "delete")
	public String delete(RcCaSourceData rcCaSourceData, RedirectAttributes redirectAttributes) {
		rcCaSourceDataService.delete(rcCaSourceData);
		addMessage(redirectAttributes, "删除信用报告原始数据表成功");
		return "redirect:"+Global.getAdminPath()+"/ca/rcCaSourceData/?repage";
	}

}