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
import com.jxf.rc.entity.RcCaData;

import com.jxf.rc.service.RcCaDataService;

/**
 * 信用报告数据表Controller
 * @author lmy
 * @version 2018-12-17
 */
@Controller
@RequestMapping(value = "${adminPath}/ca/rcCaData")
public class RcCaDataController extends BaseController {

	@Autowired
	private RcCaDataService rcCaDataService;
	
	@ModelAttribute
	public RcCaData get(@RequestParam(required=false) Long id) {
		RcCaData entity = null;
		if (id!=null){
			entity = rcCaDataService.get(id);
		}
		if (entity == null){
			entity = new RcCaData();
		}
		return entity;
	}
	
	@RequiresPermissions("ca:rcCaData:view")
	@RequestMapping(value = {"list", ""})
	public String list(RcCaData rcCaData, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Page<RcCaData> page = rcCaDataService.findPage(new Page<RcCaData>(request, response), rcCaData); 
		model.addAttribute("page", page);
		return "admin/rc/ca/rcCaDataList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("ca:rcCaData:view")
	@RequestMapping(value = "add")
	public String add(RcCaData rcCaData, Model model) {
		model.addAttribute("rcCaData", rcCaData);
		return "admin/rc/ca/rcCaDataAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("ca:rcCaData:view")
	@RequestMapping(value = "query")
	public String query(RcCaData rcCaData, Model model) {
		model.addAttribute("rcCaData", rcCaData);
		return "admin/rc/ca/rcCaDataQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("ca:rcCaData:view")
	@RequestMapping(value = "update")
	public String update(RcCaData rcCaData, Model model) {
		model.addAttribute("rcCaData", rcCaData);
		return "admin/rc/ca/rcCaDataUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("ca:rcCaData:edit")
	@RequestMapping(value = "save")
	public String save(RcCaData rcCaData, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, rcCaData)){
			return add(rcCaData, model);
		}
		rcCaDataService.save(rcCaData);
		addMessage(redirectAttributes, "保存信用报告数据表成功");
		return "redirect:"+Global.getAdminPath()+"/ca/rcCaData/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("ca:rcCaData:edit")
	@RequestMapping(value = "delete")
	public String delete(RcCaData rcCaData, RedirectAttributes redirectAttributes) {
		/*rcCaDataService.delete(rcCaData);
		addMessage(redirectAttributes, "删除信用报告数据表成功");*/
		return "redirect:"+Global.getAdminPath()+"/ca/rcCaData/?repage";
	}

}