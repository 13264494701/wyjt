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
import com.jxf.loan.entity.NfsLoanArbitrationDetail;
import com.jxf.loan.service.NfsLoanArbitrationDetailService;

/**
 * 仲裁明细Controller
 * @author Administrator
 * @version 2018-12-24
 */
@Controller
@RequestMapping(value = "${adminPath}/loanArbitrationDetail")
public class NfsLoanArbitrationDetailController extends BaseController {

	@Autowired
	private NfsLoanArbitrationDetailService loanArbitrationDetailService;
	
	@ModelAttribute
	public NfsLoanArbitrationDetail get(@RequestParam(required=false) Long id) {
		NfsLoanArbitrationDetail entity = null;
		if (id!=null){
			entity = loanArbitrationDetailService.get(id);
		}
		if (entity == null){
			entity = new NfsLoanArbitrationDetail();
		}
		return entity;
	}
	
	@RequiresPermissions("arbitration:nfsLoanArbitrationDetail:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsLoanArbitrationDetail nfsLoanArbitrationDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsLoanArbitrationDetail> page = loanArbitrationDetailService.findPage(new Page<NfsLoanArbitrationDetail>(request, response), nfsLoanArbitrationDetail); 
		model.addAttribute("page", page);
		return "admin/loan/arbitration/detail/nfsLoanArbitrationDetailList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("arbitration:nfsLoanArbitrationDetail:view")
	@RequestMapping(value = "add")
	public String add(NfsLoanArbitrationDetail nfsLoanArbitrationDetail, Model model) {
		model.addAttribute("nfsLoanArbitrationDetail", nfsLoanArbitrationDetail);
		return "admin/loan/arbitration/detail/nfsLoanArbitrationDetailAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("arbitration:nfsLoanArbitrationDetail:view")
	@RequestMapping(value = "query")
	public String query(NfsLoanArbitrationDetail nfsLoanArbitrationDetail, Model model) {
		model.addAttribute("nfsLoanArbitrationDetail", nfsLoanArbitrationDetail);
		return "admin/loan/arbitration/detail/nfsLoanArbitrationDetailQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("arbitration:nfsLoanArbitrationDetail:view")
	@RequestMapping(value = "update")
	public String update(NfsLoanArbitrationDetail nfsLoanArbitrationDetail, Model model) {
		model.addAttribute("nfsLoanArbitrationDetail", nfsLoanArbitrationDetail);
		return "admin/loan/arbitration/detail/nfsLoanArbitrationDetailUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("arbitration:nfsLoanArbitrationDetail:edit")
	@RequestMapping(value = "save")
	public String save(NfsLoanArbitrationDetail nfsLoanArbitrationDetail, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsLoanArbitrationDetail)){
			return add(nfsLoanArbitrationDetail, model);
		}
		loanArbitrationDetailService.save(nfsLoanArbitrationDetail);
		addMessage(redirectAttributes, "保存仲裁明细成功");
		return "redirect:"+Global.getAdminPath()+"/loanArbitrationDetail/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("arbitration:nfsLoanArbitrationDetail:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsLoanArbitrationDetail nfsLoanArbitrationDetail, RedirectAttributes redirectAttributes) {
		loanArbitrationDetailService.delete(nfsLoanArbitrationDetail);
		addMessage(redirectAttributes, "删除仲裁明细成功");
		return "redirect:"+Global.getAdminPath()+"/loanArbitrationDetail/?repage";
	}

}