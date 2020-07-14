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

import com.jxf.loan.entity.NfsLoanRecordData;
import com.jxf.loan.service.NfsLoanRecordDataService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;


/**
 * 借条数据Controller
 * @author wo
 * @version 2019-04-28
 */
@Controller
@RequestMapping(value = "${adminPath}/loanRecordData")
public class NfsLoanRecordDataController extends BaseController {

	@Autowired
	private NfsLoanRecordDataService loanRecordDataService;
	
	@ModelAttribute
	public NfsLoanRecordData get(@RequestParam(required=false) Long id) {
		NfsLoanRecordData entity = null;
		if (id!=null){
			entity = loanRecordDataService.get(id);
		}
		if (entity == null){
			entity = new NfsLoanRecordData();
		}
		return entity;
	}
	
	@RequiresPermissions("loan:nfsLoanRecordData:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsLoanRecordData nfsLoanRecordData, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsLoanRecordData> page = loanRecordDataService.findPage(new Page<NfsLoanRecordData>(request, response), nfsLoanRecordData); 
		model.addAttribute("page", page);
		return "admin/loan/data/nfsLoanRecordDataList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("loan:nfsLoanRecordData:view")
	@RequestMapping(value = "add")
	public String add(NfsLoanRecordData nfsLoanRecordData, Model model) {
		model.addAttribute("nfsLoanRecordData", nfsLoanRecordData);
		return "admin/loan/data/nfsLoanRecordDataAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("loan:nfsLoanRecordData:view")
	@RequestMapping(value = "query")
	public String query(NfsLoanRecordData nfsLoanRecordData, Model model) {
		model.addAttribute("nfsLoanRecordData", nfsLoanRecordData);
		return "nfs/loan/nfsLoanRecordDataQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("loan:nfsLoanRecordData:view")
	@RequestMapping(value = "update")
	public String update(NfsLoanRecordData nfsLoanRecordData, Model model) {
		model.addAttribute("nfsLoanRecordData", nfsLoanRecordData);
		return "admin/loan/data/nfsLoanRecordDataUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("loan:nfsLoanRecordData:edit")
	@RequestMapping(value = "save")
	public String save(NfsLoanRecordData nfsLoanRecordData, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsLoanRecordData)){
			return add(nfsLoanRecordData, model);
		}
		loanRecordDataService.save(nfsLoanRecordData);
		addMessage(redirectAttributes, "保存借条数据成功");
		return "redirect:"+Global.getAdminPath()+"/loanRecordData/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("loan:nfsLoanRecordData:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsLoanRecordData nfsLoanRecordData, RedirectAttributes redirectAttributes) {
		loanRecordDataService.delete(nfsLoanRecordData);
		addMessage(redirectAttributes, "删除借条数据成功");
		return "redirect:"+Global.getAdminPath()+"/loanRecordData/?repage";
	}

}