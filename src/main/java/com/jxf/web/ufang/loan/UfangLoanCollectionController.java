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

import com.jxf.loan.entity.NfsLoanCollection;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanCollectionService;
import com.jxf.svc.persistence.Page;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.util.UfangUserUtils;
import com.jxf.web.admin.sys.BaseController;


/**
 * 	借条催收Controller
 * @author gaobo
 * @version 2018-11-07
 */
@Controller("ufangLoanCollectionController")
@RequestMapping(value = "${ufangPath}/nfsLoanCollection")
public class UfangLoanCollectionController extends BaseController {

	@Autowired
	private NfsLoanCollectionService nfsLoanCollectionService;
	
	@ModelAttribute
	public NfsLoanCollection get(@RequestParam(required=false) Long id) {
		NfsLoanCollection entity = null;
		if (id!=null){
			entity = nfsLoanCollectionService.get(id);
		}
		if (entity == null){
			entity = new NfsLoanCollection();
		}
		return entity;
	}
	
	@RequiresPermissions("loan:nfsLoanCollection:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsLoanCollection loanCollection, HttpServletRequest request, HttpServletResponse response, Model model) {
		UfangUser ufangUser = UfangUserUtils.getUser();
		if (ufangUser.getBindStatus().equals(UfangUser.BindStatus.binded)&&ufangUser.getMember()!=null&&ufangUser.getMember().getId()!=null) {
			NfsLoanRecord loanRecord = new NfsLoanRecord();
			loanRecord.setLoaner(ufangUser.getMember());
			loanCollection.setLoan(loanRecord);
			Page<NfsLoanCollection> page = nfsLoanCollectionService.findPage(new Page<NfsLoanCollection>(request, response),loanCollection);
			model.addAttribute("page", page); 
		}
		return "ufang/loan/collection/nfsLoanCollectionList";
	}
}