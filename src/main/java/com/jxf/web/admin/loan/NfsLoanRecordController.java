package com.jxf.web.admin.loan;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.mem.service.MemberService;
import com.jxf.svc.utils.StringUtils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.entity.NfsLoanContract;
import com.jxf.loan.entity.NfsLoanOperatingRecord;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRepayRecord;
import com.jxf.loan.service.NfsLoanApplyDetailService;
import com.jxf.loan.service.NfsLoanContractService;
import com.jxf.loan.service.NfsLoanOperatingRecordService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.service.NfsLoanRepayRecordService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.web.admin.sys.BaseController;



/**
 * 借条记录Controller
 * @author wo
 * @version 2018-10-10
 */
@Controller
@RequestMapping(value = "${adminPath}/nfsLoanRecord")
public class NfsLoanRecordController extends BaseController {

	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsLoanRepayRecordService loanRepayRecordService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberActTrxService memberActTrxService;
	@Autowired
	private NfsLoanOperatingRecordService loanOperatingRecordService;
	@Autowired
	private NfsLoanApplyDetailService loanApplyDetailService;
	@Autowired
	private NfsLoanContractService loanContractService;

	
	@ModelAttribute
	public NfsLoanRecord get(@RequestParam(required=false) Long id) {
		NfsLoanRecord entity = null;
		if (id!=null){
			entity = loanRecordService.get(id);
		}
		if (entity == null){
			entity = new NfsLoanRecord();
		}
		return entity;
	}
	
	@RequiresPermissions("record:nfsLoanRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsLoanRecord nfsLoanRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		//有备用手机号的用户在新系统后台搜索借条不全 改成查ID再搜借条
//		Member loaneeMember = nfsLoanRecord.getLoanee();
//		Member loanerMember = nfsLoanRecord.getLoaner();
//		if(loaneeMember != null && loaneeMember.getName() != null && !StringUtils.equals(loaneeMember.getName(), "")){
//			model.addAttribute("loaneeName", loaneeMember.getName());
//		}
//		if(loanerMember != null && loanerMember.getName() != null && !StringUtils.equals(loanerMember.getName(), "")){
//			model.addAttribute("loanerName", loanerMember.getName());
//		}
//		if(loaneeMember != null && loaneeMember.getUsername() != null && !StringUtils.equals(loaneeMember.getUsername(), "")){
//			model.addAttribute("loaneeUsername", loaneeMember.getUsername());
//			Member find = memberService.findByUsername(loaneeMember.getUsername());
//			if(find != null){
//				Long loaneeId = find.getId();
//				loaneeMember = new Member();
//				loaneeMember.setId(loaneeId);
//			}
//		}
//		if(loanerMember != null && loanerMember.getUsername() != null && !StringUtils.equals(loanerMember.getUsername(), "")){
//			model.addAttribute("loanerUsername", loanerMember.getUsername());
//			Member find = memberService.findByUsername(loanerMember.getUsername());
//			if(find != null){
//				Long loanerId = find.getId();
//				loanerMember = new Member();
//				loanerMember.setId(loanerId);
//			}
//		}
//		nfsLoanRecord.setLoanee(loaneeMember);
//		nfsLoanRecord.setLoaner(loanerMember);
		
		Page<NfsLoanRecord> page = loanRecordService.findPage(new Page<NfsLoanRecord>(request, response), nfsLoanRecord);

		List<NfsLoanRecord> nfsLoanRecordList = page.getList();
		for (int i = 0; i < nfsLoanRecordList.size(); i++) {
			Member loaner = nfsLoanRecordList.get(i).getLoaner();
			Member loanee = nfsLoanRecordList.get(i).getLoanee();
			loaner.setUsername(StringUtils.replacePattern(loaner.getUsername(), "(?<=[\\d]{2})\\d(?=[\\d]{2})", "*"));
			loanee.setUsername(StringUtils.replacePattern(loanee.getUsername(), "(?<=[\\d]{2})\\d(?=[\\d]{2})", "*"));
			nfsLoanRecordList.get(i).setLoaner(loaner);
			nfsLoanRecordList.get(i).setLoanee(loanee);
		}

		model.addAttribute("page", page);
		return "admin/loan/record/nfsLoanRecordList";
	}

	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("record:nfsLoanRecord:view")
	@RequestMapping(value = "add")
	public String add(NfsLoanRecord nfsLoanRecord, Model model) {
		model.addAttribute("nfsLoanRecord", nfsLoanRecord);
		return "admin/loan/record/nfsLoanRecordAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("record:nfsLoanRecord:view")
	@RequestMapping(value = "query")
	public String query(NfsLoanRecord nfsLoanRecord, Model model) {
		NfsLoanApplyDetail nfsLoanApplyDetail = loanApplyDetailService.get(nfsLoanRecord.getLoanApplyDetail().getId());
		if(StringUtils.isNotBlank(nfsLoanApplyDetail.getVideoUrl())){
			if(nfsLoanApplyDetail.getId()<=7517375){
				nfsLoanApplyDetail.setVideoUrl(Global.getConfig("domain")+nfsLoanApplyDetail.getVideoUrl());
			}
		}
		nfsLoanRecord.setLoanApplyDetail(nfsLoanApplyDetail);
		
		//还款计划
		NfsLoanRepayRecord loanRepayRecord = new NfsLoanRepayRecord();
		loanRepayRecord.setLoan(nfsLoanRecord);
		List<NfsLoanRepayRecord> loanRepayRecordList = loanRepayRecordService.findList(loanRepayRecord);
		
		//流水
		List<MemberActTrx> trxList = memberActTrxService.findListByOrgId(nfsLoanRecord.getId());
		
		//操作记录
		NfsLoanOperatingRecord loanOperatingRecord = new NfsLoanOperatingRecord();
		loanOperatingRecord.setOldRecord(nfsLoanRecord);
		List<NfsLoanOperatingRecord> operatingList = loanOperatingRecordService.findList(loanOperatingRecord);
		
		//电子借条
		NfsLoanContract loanContract = loanContractService.getCurrentContractByLoanId(nfsLoanRecord.getId());
		String contractUrl = loanContract.getContractUrl();
		if(StringUtils.isBlank(contractUrl)){
			nfsLoanRecord.setContractUrl("正在生成...");
		}else{
			nfsLoanRecord.setContractUrl(Global.getConfig("domain") + loanContract.getContractUrl());
		}
		
		model.addAttribute("nfsLoanRecord", nfsLoanRecord);
		model.addAttribute("loanRepayRecordList", loanRepayRecordList);
		model.addAttribute("trxList", trxList);
		model.addAttribute("operatingList", operatingList);
		
		return "admin/loan/record/nfsLoanRecordQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("record:nfsLoanRecord:view")
	@RequestMapping(value = "update")
	public String update(NfsLoanRecord nfsLoanRecord, Model model) {
		model.addAttribute("nfsLoanRecord", nfsLoanRecord);
		return "admin/loan/record/nfsLoanRecordUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("record:nfsLoanRecord:edit")
	@RequestMapping(value = "save")
	public String save(NfsLoanRecord nfsLoanRecord, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsLoanRecord)){
			return add(nfsLoanRecord, model);
		}
		loanRecordService.save(nfsLoanRecord);
		addMessage(redirectAttributes, "保存借条记录成功");
		return "redirect:"+Global.getAdminPath()+"/nfsLoanRecord/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("record:nfsLoanRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsLoanRecord nfsLoanRecord, RedirectAttributes redirectAttributes) {
		loanRecordService.delete(nfsLoanRecord);
		addMessage(redirectAttributes, "删除借条记录成功");
		return "redirect:"+Global.getAdminPath()+"/nfsLoanRecord/?repage";
	}
	
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("record:nfsLoanRecord:view")
	@RequestMapping(value = "queryLoanRecordByDetailId")
	public String queryLoanRecordByDetailId(String id, Model model) {
		NfsLoanRecord param = new NfsLoanRecord();
		NfsLoanApplyDetail detail = new NfsLoanApplyDetail();
		detail.setId(Long.valueOf(id));
		param.setLoanApplyDetail(detail);
		List<NfsLoanRecord> loanRecords = loanRecordService.findList(param);
		NfsLoanRecord loanRecord = loanRecords.get(0);
		model.addAttribute("nfsLoanRecord", loanRecord);
		return "admin/loan/record/nfsLoanRecordQuery";
	}
	
	
	@RequiresPermissions("record:nfsLoanRecord:view")
	@RequestMapping(value = "loanersumloan")
	public String loanersumloan(NfsLoanRecord loanRecord, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {

		if(loanRecord.getLoaner()==null||StringUtils.isBlank(loanRecord.getLoaner().getUsername())) {
			addMessage(redirectAttributes, "放款人手机号码不能为空");
			return "admin/loan/record/loanersumloan";
		}
		Member loaner =  memberService.findByUsername(loanRecord.getLoaner().getUsername());
		loanRecord.setLoaner(loaner);
		Date beginDate = loanRecord.getBeginTime();
		Date endDate = loanRecord.getEndTime();
		Integer totalQuantity = loanRecordService.loanerCountLoan(loanRecord);
		BigDecimal totalAmount = loanRecordService.loanerSumLoan(loanRecord);
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("username", loanRecord.getLoaner().getUsername());
		model.addAttribute("totalQuantity", totalQuantity);
		model.addAttribute("totalAmount", totalAmount);
		
		return "admin/loan/record/loanersumloan";
	}
	

}