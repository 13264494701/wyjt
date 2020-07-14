package com.jxf.web.admin.nfs;

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

import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.nfs.entity.NfsFundAddReduce;
import com.jxf.nfs.entity.NfsFundAddReduce.Status;
import com.jxf.nfs.service.NfsFundAddReduceService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.Exceptions;
import com.jxf.web.admin.sys.BaseController;
import com.jxf.web.model.ResponseData;

/**
 * 会员加减款记录Controller
 * @author suHuimin
 * @version 2019-01-26
 */
@Controller
@RequestMapping(value = "${adminPath}/fundAddReduce")
public class NfsFundAddReduceController extends BaseController {

	@Autowired
	private NfsFundAddReduceService nfsFundAddReduceService;
	@Autowired
	private MemberService memberService;
	
	@ModelAttribute
	public NfsFundAddReduce get(@RequestParam(required=false) Long id) {
		NfsFundAddReduce entity = null;
		if (id!=null){
			entity = nfsFundAddReduceService.get(id);
		}
		if (entity == null){
			entity = new NfsFundAddReduce();
		}
		return entity;
	}
	
	@RequiresPermissions("fundaddreduce:nfsFundAddReduce:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsFundAddReduce nfsFundAddReduce, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsFundAddReduce> page = nfsFundAddReduceService.findPage(new Page<NfsFundAddReduce>(request, response), nfsFundAddReduce); 
		model.addAttribute("page", page);
		return "admin/nfs/fundAddReduce/nfsFundAddReduceList";
	}
	
	@RequiresPermissions("fundaddreduce:nfsFundAddReduce:view")
	@RequestMapping(value ="getCheckedList")
	public String getCheckedList(NfsFundAddReduce nfsFundAddReduce, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsFundAddReduce> page = nfsFundAddReduceService.getCheckedList(new Page<NfsFundAddReduce>(request, response), nfsFundAddReduce); 
		model.addAttribute("page", page);
		return "admin/nfs/fundAddReduce/nfsFundAddReduceList";
	}
	
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("fundaddreduce:nfsFundAddReduce:view")
	@RequestMapping(value = "add")
	public String add(NfsFundAddReduce nfsFundAddReduce, Model model) {
		model.addAttribute("nfsFundAddReduce", nfsFundAddReduce);
		return "admin/nfs/fundAddReduce/nfsFundAddReduceAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("fundaddreduce:nfsFundAddReduce:view")
	@RequestMapping(value = "query")
	public String query(NfsFundAddReduce nfsFundAddReduce, Model model) {
		model.addAttribute("nfsFundAddReduce", nfsFundAddReduce);
		return "admin/nfs/fundAddReduce/nfsFundAddReduceQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("fundaddreduce:nfsFundAddReduce:view")
	@RequestMapping(value = "update")
	public String update(NfsFundAddReduce nfsFundAddReduce, Model model) {
		model.addAttribute("nfsFundAddReduce", nfsFundAddReduce);
		return "admin/nfs/fundAddReduce/nfsFundAddReduceUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("fundaddreduce:nfsFundAddReduce:edit")
	@RequestMapping(value = "save")
	public String save(NfsFundAddReduce nfsFundAddReduce, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsFundAddReduce)){
			return add(nfsFundAddReduce, model);
		}
		nfsFundAddReduceService.save(nfsFundAddReduce);
		addMessage(redirectAttributes, "保存会员加减款记录成功");
		return "redirect:"+Global.getAdminPath()+"/fundAddReduce/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("fundaddreduce:nfsFundAddReduce:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsFundAddReduce nfsFundAddReduce, RedirectAttributes redirectAttributes) {
		nfsFundAddReduceService.delete(nfsFundAddReduce);
		addMessage(redirectAttributes, "删除会员加减款记录成功");
		return "redirect:"+Global.getAdminPath()+"/fundAddReduce/?repage";
	}

	 /**
	 * 加减款审核
	 */
	@RequiresPermissions("fundaddreduce:nfsFundAddReduce:edit")
	@ResponseBody
	@RequestMapping(value = "applyCheck")
	public ResponseData applyCheck(Long fundAddReduceId,String status) {
		NfsFundAddReduce fundAddReduce = nfsFundAddReduceService.get(fundAddReduceId);
		if(!fundAddReduce.getStatus().equals(NfsFundAddReduce.Status.auditing)) {
			return ResponseData.error("记录状态已更新，请勿重复操作！");
		}
		Member member = memberService.get(fundAddReduce.getMember());
		fundAddReduce.setMember(member);
		if("pass".equals(status)) {
			fundAddReduce.setStatus(Status.passed);
		}else if("reject".equals(status)) {
			fundAddReduce.setStatus(Status.reject);
		}
		try {
			nfsFundAddReduceService.applyCheck(fundAddReduce);
		} catch (Exception e) {
			logger.error("会员{}加减款审核失败，异常信息：",member.getId(),Exceptions.getStackTraceAsString(e));
			return ResponseData.error("会员账户更新失败！");
		}
		return ResponseData.success("操作成功");
	}
	
	
}