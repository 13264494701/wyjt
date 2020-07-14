package com.jxf.web.ufang.loan;

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

import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.service.NfsLoanApplyDetailService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;


/**
 * 借贷对象Controller
 * @author wo
 * @version 2018-10-18
 */
@Controller("ufangLoanApplyDetailController")
@RequestMapping(value = "${ufangPath}/loanApplyDetail")
public class UfangLoanApplyDetailController extends BaseController {

	@Autowired
	private NfsLoanApplyDetailService loanApplyDetailService;
	
	@ModelAttribute
	public NfsLoanApplyDetail get(@RequestParam(required=false) Long id) {
		NfsLoanApplyDetail entity = null;
		if (id!=null){
			entity = loanApplyDetailService.get(id);
		}
		if (entity == null){
			entity = new NfsLoanApplyDetail();
		}
		return entity;
	}
	
	@RequiresPermissions("loan:loanApplyDetail:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsLoanApplyDetail loanApplyDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsLoanApplyDetail> page = loanApplyDetailService.findPage(new Page<NfsLoanApplyDetail>(request, response), loanApplyDetail); 
		model.addAttribute("page", page);
		return "ufang/loan/apply/detail/loanApplyDetailList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("loan:loanApplyDetail:view")
	@RequestMapping(value = "add")
	public String add(NfsLoanApplyDetail loanApplyDetail, Model model) {
		model.addAttribute("loanApplyDetail", loanApplyDetail);
		return "ufang/loan/apply/detail/loanApplyDetailAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("loan:loanApplyDetail:view")
	@RequestMapping(value = "query")
	public String query(NfsLoanApplyDetail loanApplyDetail, Model model) {
		model.addAttribute("loanApplyDetail", loanApplyDetail);
		return "ufang/loan/apply/detail/loanApplyDetailQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("loan:loanApplyDetail:view")
	@RequestMapping(value = "update")
	public String update(NfsLoanApplyDetail loanApplyDetail, Model model) {
		model.addAttribute("loanApplyDetail", loanApplyDetail);
		return "ufang/loan/apply/detail/loanApplyDetailUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("loan:loanApplyDetail:edit")
	@RequestMapping(value = "save")
	public String save(NfsLoanApplyDetail loanApplyDetail, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, loanApplyDetail)){
			return add(loanApplyDetail, model);
		}
		loanApplyDetailService.save(loanApplyDetail);
		addMessage(redirectAttributes, "保存借贷对象成功");
		return "redirect:"+Global.getAdminPath()+"/loanApplyDetail/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("loan:loanApplyDetail:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsLoanApplyDetail loanApplyDetail, RedirectAttributes redirectAttributes) {
		loanApplyDetailService.delete(loanApplyDetail);
		addMessage(redirectAttributes, "删除借贷对象成功");
		return "redirect:"+Global.getAdminPath()+"/loanApplyDetail/?repage";
	}

}