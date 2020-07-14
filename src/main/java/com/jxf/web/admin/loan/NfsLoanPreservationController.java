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

import com.jxf.loan.entity.NfsLoanPreservation;
import com.jxf.loan.service.NfsLoanPreservationService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;

/**
 * 业务保全Controller
 * @author SuHuimin
 * @version 2019-07-01
 */
@Controller
@RequestMapping(value = "${adminPath}/loan/preservation/nfsLoanPreservation")
public class NfsLoanPreservationController extends BaseController {

	@Autowired
	private NfsLoanPreservationService nfsLoanPreservationService;
	
	@ModelAttribute
	public NfsLoanPreservation get(@RequestParam(required=false) Long id) {
		NfsLoanPreservation entity = null;
		if (id!=null){
			entity = nfsLoanPreservationService.get(id);
		}
		if (entity == null){
			entity = new NfsLoanPreservation();
		}
		return entity;
	}
	
	@RequiresPermissions("loan:preservation:nfsLoanPreservation:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsLoanPreservation nfsLoanPreservation, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsLoanPreservation> page = nfsLoanPreservationService.findPage(new Page<NfsLoanPreservation>(request, response), nfsLoanPreservation); 
		model.addAttribute("page", page);
		return "loan/loan/preservation/nfsLoanPreservationList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("loan:preservation:nfsLoanPreservation:view")
	@RequestMapping(value = "add")
	public String add(NfsLoanPreservation nfsLoanPreservation, Model model) {
		model.addAttribute("nfsLoanPreservation", nfsLoanPreservation);
		return "loan/loan/preservation/nfsLoanPreservationAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("loan:preservation:nfsLoanPreservation:view")
	@RequestMapping(value = "query")
	public String query(NfsLoanPreservation nfsLoanPreservation, Model model) {
		model.addAttribute("nfsLoanPreservation", nfsLoanPreservation);
		return "loan/loan/preservation/nfsLoanPreservationQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("loan:preservation:nfsLoanPreservation:view")
	@RequestMapping(value = "update")
	public String update(NfsLoanPreservation nfsLoanPreservation, Model model) {
		model.addAttribute("nfsLoanPreservation", nfsLoanPreservation);
		return "loan/loan/preservation/nfsLoanPreservationUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("loan:preservation:nfsLoanPreservation:edit")
	@RequestMapping(value = "save")
	public String save(NfsLoanPreservation nfsLoanPreservation, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsLoanPreservation)){
			return add(nfsLoanPreservation, model);
		}
		nfsLoanPreservationService.save(nfsLoanPreservation);
		addMessage(redirectAttributes, "保存业务保全成功");
		return "redirect:"+Global.getAdminPath()+"/loan/preservation/nfsLoanPreservation/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("loan:preservation:nfsLoanPreservation:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsLoanPreservation nfsLoanPreservation, RedirectAttributes redirectAttributes) {
		nfsLoanPreservationService.delete(nfsLoanPreservation);
		addMessage(redirectAttributes, "删除业务保全成功");
		return "redirect:"+Global.getAdminPath()+"/loan/preservation/nfsLoanPreservation/?repage";
	}

}