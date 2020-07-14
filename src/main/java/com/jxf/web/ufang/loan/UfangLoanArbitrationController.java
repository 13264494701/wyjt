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

import com.jxf.loan.entity.NfsLoanArbitration;
import com.jxf.loan.service.NfsLoanArbitrationService;
import com.jxf.svc.persistence.Page;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.util.UfangUserUtils;
import com.jxf.web.admin.sys.BaseController;

/**
 * 借条仲裁增删改查Controller
 * @author LIUHUAIXIN
 * @version 2018-11-07
 */
@Controller("ufangLoanArbitrationController")
@RequestMapping(value = "${ufangPath}/loanArbitration")
public class UfangLoanArbitrationController extends BaseController {

	@Autowired
	private NfsLoanArbitrationService nfsLoanArbitrationService;
	
	@ModelAttribute
	public NfsLoanArbitration get(@RequestParam(required=false) Long id) {
		NfsLoanArbitration entity = null;
		if (id!=null){
			entity = nfsLoanArbitrationService.get(id);
		}
		if (entity == null){
			entity = new NfsLoanArbitration();
		}
		return entity;
	}
	
	@RequiresPermissions("loan:nfsLoanArbitration:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsLoanArbitration nfsLoanArbitration, HttpServletRequest request, HttpServletResponse response, Model model) {
		UfangUser ufangUser = UfangUserUtils.getUser();	
		if (ufangUser.getBindStatus().equals(UfangUser.BindStatus.binded)&&ufangUser.getMember()!=null&&ufangUser.getMember().getId()!=null) {
			nfsLoanArbitration.setMember(ufangUser.getMember());
			Page<NfsLoanArbitration> page = nfsLoanArbitrationService.findPage(new Page<NfsLoanArbitration>(request, response), nfsLoanArbitration);
			model.addAttribute("page", page);
		}
		return "ufang/loan/arbitration/nfsLoanArbitrationList";
	}
}