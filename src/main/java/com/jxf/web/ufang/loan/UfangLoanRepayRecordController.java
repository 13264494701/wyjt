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

import com.jxf.loan.entity.NfsLoanRepayRecord;
import com.jxf.loan.service.NfsLoanRepayRecordService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;


/**
 * 还款计划Controller
 * @author wo
 * @version 2018-11-15
 */
@Controller("ufangLoanRepayRecordController")
@RequestMapping(value = "${ufangPath}/loanRepayRecord")
public class UfangLoanRepayRecordController extends BaseController {

	@Autowired
	private NfsLoanRepayRecordService nfsLoanRepayRecordService;
	
	@ModelAttribute
	public NfsLoanRepayRecord get(@RequestParam(required=false) Long id) {
		NfsLoanRepayRecord entity = null;
		if (id!=null){
			entity = nfsLoanRepayRecordService.get(id);
		}
		if (entity == null){
			entity = new NfsLoanRepayRecord();
		}
		return entity;
	}
	
	@RequiresPermissions("repay:nfsLoanRepayRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsLoanRepayRecord nfsLoanRepayRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsLoanRepayRecord> page = nfsLoanRepayRecordService.findPage(new Page<NfsLoanRepayRecord>(request, response), nfsLoanRepayRecord); 
		model.addAttribute("page", page);
		return "loan/repay/nfsLoanRepayRecordList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("repay:nfsLoanRepayRecord:view")
	@RequestMapping(value = "add")
	public String add(NfsLoanRepayRecord nfsLoanRepayRecord, Model model) {
		model.addAttribute("nfsLoanRepayRecord", nfsLoanRepayRecord);
		return "loan/repay/nfsLoanRepayRecordAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("repay:nfsLoanRepayRecord:view")
	@RequestMapping(value = "query")
	public String query(NfsLoanRepayRecord nfsLoanRepayRecord, Model model) {
		model.addAttribute("nfsLoanRepayRecord", nfsLoanRepayRecord);
		return "loan/repay/nfsLoanRepayRecordQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("repay:nfsLoanRepayRecord:view")
	@RequestMapping(value = "update")
	public String update(NfsLoanRepayRecord nfsLoanRepayRecord, Model model) {
		model.addAttribute("nfsLoanRepayRecord", nfsLoanRepayRecord);
		return "loan/repay/nfsLoanRepayRecordUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("repay:nfsLoanRepayRecord:edit")
	@RequestMapping(value = "save")
	public String save(NfsLoanRepayRecord nfsLoanRepayRecord, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsLoanRepayRecord)){
			return add(nfsLoanRepayRecord, model);
		}
		nfsLoanRepayRecordService.save(nfsLoanRepayRecord);
		addMessage(redirectAttributes, "保存还款计划成功");
		return "redirect:"+Global.getAdminPath()+"/loanRepayRecord/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("repay:nfsLoanRepayRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsLoanRepayRecord nfsLoanRepayRecord, RedirectAttributes redirectAttributes) {
		nfsLoanRepayRecordService.delete(nfsLoanRepayRecord);
		addMessage(redirectAttributes, "删除还款计划成功");
		return "redirect:"+Global.getAdminPath()+"/loanRepayRecord/?repage";
	}

}