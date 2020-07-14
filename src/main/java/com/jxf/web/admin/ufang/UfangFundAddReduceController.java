package com.jxf.web.admin.ufang;

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

import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.ufang.entity.UfangFundAddReduce;
import com.jxf.ufang.entity.UfangFundAddReduce.Status;
import com.jxf.ufang.service.UfangFundAddReduceService;
import com.jxf.web.admin.sys.BaseController;
import com.jxf.web.model.ResponseData;

/**
 * 优放机构加减款记录Controller
 * @author suHuimin
 * @version 2019-01-26
 */
@Controller
@RequestMapping(value = "${adminPath}/ufangFundAddReduce")
public class UfangFundAddReduceController extends BaseController {

	@Autowired
	private UfangFundAddReduceService ufangFundAddReduceService;
	
	@ModelAttribute
	public UfangFundAddReduce get(@RequestParam(required=false) Long id) {
		UfangFundAddReduce entity = null;
		if (id!=null){
			entity = ufangFundAddReduceService.get(id);
		}
		if (entity == null){
			entity = new UfangFundAddReduce();
		}
		return entity;
	}
	
	@RequiresPermissions("ufangbrnfundaddreducce:ufangFundAddReduce:view")
	@RequestMapping(value = {"list", ""})
	public String list(UfangFundAddReduce ufangFundAddReduce, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UfangFundAddReduce> page = ufangFundAddReduceService.findPage(new Page<UfangFundAddReduce>(request, response), ufangFundAddReduce); 
		model.addAttribute("page", page);
		return "admin/ufang/ufangFundAddReducce/ufangFundAddReduceList";
	}
	

	@RequiresPermissions("ufangbrnfundaddreducce:ufangFundAddReduce:view")
	@RequestMapping(value = "getCheckedList")
	public String getCheckedList(UfangFundAddReduce ufangFundAddReduce, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UfangFundAddReduce> page = ufangFundAddReduceService.getCheckedList(new Page<UfangFundAddReduce>(request, response), ufangFundAddReduce); 
		model.addAttribute("page", page);
		return "admin/ufang/ufangFundAddReducce/ufangFundAddReduceList";
	}
	
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("ufangbrnfundaddreducce:ufangFundAddReduce:view")
	@RequestMapping(value = "add")
	public String add(UfangFundAddReduce ufangFundAddReduce, Model model) {
		model.addAttribute("ufangFundAddReduce", ufangFundAddReduce);
		return "admin/ufang/ufangFundAddReducce/ufangFundAddReduceAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("ufangbrnfundaddreducce:ufangFundAddReduce:view")
	@RequestMapping(value = "query")
	public String query(UfangFundAddReduce ufangFundAddReduce, Model model) {
		model.addAttribute("ufangFundAddReduce", ufangFundAddReduce);
		return "admin/ufang/ufangFundAddReducce/ufangFundAddReduceQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("ufangbrnfundaddreducce:ufangFundAddReduce:view")
	@RequestMapping(value = "update")
	public String update(UfangFundAddReduce ufangFundAddReduce, Model model) {
		model.addAttribute("ufangFundAddReduce", ufangFundAddReduce);
		return "admin/ufang/ufangFundAddReducce/ufangFundAddReduceUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("ufangbrnfundaddreducce:ufangFundAddReduce:edit")
	@RequestMapping(value = "save")
	public String save(UfangFundAddReduce ufangFundAddReduce, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, ufangFundAddReduce)){
			return add(ufangFundAddReduce, model);
		}
		ufangFundAddReduceService.save(ufangFundAddReduce);
		addMessage(redirectAttributes, "保存优放机构加减款记录成功");
		return "redirect:"+Global.getAdminPath()+"/ufangFundAddReducce?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("ufangbrnfundaddreducce:ufangFundAddReduce:edit")
	@RequestMapping(value = "delete")
	public String delete(UfangFundAddReduce ufangFundAddReduce, RedirectAttributes redirectAttributes) {
		ufangFundAddReduceService.delete(ufangFundAddReduce);
		addMessage(redirectAttributes, "删除优放机构加减款记录成功");
		return "redirect:"+Global.getAdminPath()+"/ufangFundAddReducce/?repage";
	}
	/**
	 * 加减款审核
	 */
	@RequiresPermissions("ufangbrnfundaddreducce:ufangFundAddReduce:edit")
	@ResponseBody
	@RequestMapping(value = "applyCheck")
	public ResponseData applyCheck(Long ufangFundAddReduceId,String status) {
		UfangFundAddReduce ufangFundAddReduce = ufangFundAddReduceService.get(ufangFundAddReduceId);
		if(!ufangFundAddReduce.getStatus().equals(UfangFundAddReduce.Status.auditing)) {
			return ResponseData.error("记录状态已更新，请勿重复操作！");
		}
		if("pass".equals(status)) {
			ufangFundAddReduce.setStatus(Status.passed);
		}else if("reject".equals(status)) {
			ufangFundAddReduce.setStatus(Status.reject);
		}
		try {
			ufangFundAddReduceService.ufangBrnAddReduceApplyCheck(ufangFundAddReduce);
		} catch (Exception e) {
			logger.error("优放机构{}账户异常，加减款操作失败！",ufangFundAddReduce.getUfangBrn().getBrnNo());
			return ResponseData.error("机构账户异常，操作失败！");
		}
		return ResponseData.success("操作成功");
	}
}