package com.jxf.web.admin.loan;

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
import com.jxf.loan.entity.NfsLoanCollectionDetail;
import com.jxf.loan.service.NfsLoanCollectionDetailService;

/**
 * 催收明细Controller
 * @author Administrator
 * @version 2018-12-24
 */
@Controller
@RequestMapping(value = "${adminPath}/collection/nfsLoanCollectionDetail")
public class NfsLoanCollectionDetailController extends BaseController {

	@Autowired
	private NfsLoanCollectionDetailService nfsLoanCollectionDetailService;
	
	@ModelAttribute
	public NfsLoanCollectionDetail get(@RequestParam(required=false) Long id) {
		NfsLoanCollectionDetail entity = null;
		if (id!=null){
			entity = nfsLoanCollectionDetailService.get(id);
		}
		if (entity == null){
			entity = new NfsLoanCollectionDetail();
		}
		return entity;
	}
	
	@RequiresPermissions("collection:nfsLoanCollectionDetail:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsLoanCollectionDetail nfsLoanCollectionDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsLoanCollectionDetail> page = nfsLoanCollectionDetailService.findPage(new Page<NfsLoanCollectionDetail>(request, response), nfsLoanCollectionDetail); 
		model.addAttribute("page", page);
		return "loan/collection/nfsLoanCollectionDetailList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("collection:nfsLoanCollectionDetail:view")
	@RequestMapping(value = "add")
	public String add(NfsLoanCollectionDetail nfsLoanCollectionDetail, Model model) {
		model.addAttribute("nfsLoanCollectionDetail", nfsLoanCollectionDetail);
		return "loan/collection/nfsLoanCollectionDetailAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("collection:nfsLoanCollectionDetail:view")
	@RequestMapping(value = "query")
	public String query(NfsLoanCollectionDetail nfsLoanCollectionDetail, Model model) {
		model.addAttribute("nfsLoanCollectionDetail", nfsLoanCollectionDetail);
		return "loan/collection/nfsLoanCollectionDetailQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("collection:nfsLoanCollectionDetail:view")
	@RequestMapping(value = "update")
	public String update(NfsLoanCollectionDetail nfsLoanCollectionDetail, Model model) {
		model.addAttribute("nfsLoanCollectionDetail", nfsLoanCollectionDetail);
		return "loan/collection/nfsLoanCollectionDetailUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("collection:nfsLoanCollectionDetail:edit")
	@RequestMapping(value = "save")
	public String save(NfsLoanCollectionDetail nfsLoanCollectionDetail, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsLoanCollectionDetail)){
			return add(nfsLoanCollectionDetail, model);
		}
		nfsLoanCollectionDetailService.save(nfsLoanCollectionDetail);
		addMessage(redirectAttributes, "保存催收明细成功");
		return "redirect:"+Global.getAdminPath()+"/collection/nfsLoanCollectionDetail/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("collection:nfsLoanCollectionDetail:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsLoanCollectionDetail nfsLoanCollectionDetail, RedirectAttributes redirectAttributes) {
		nfsLoanCollectionDetailService.delete(nfsLoanCollectionDetail);
		addMessage(redirectAttributes, "删除催收明细成功");
		return "redirect:"+Global.getAdminPath()+"/collection/nfsLoanCollectionDetail/?repage";
	}

}