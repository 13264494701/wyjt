package com.jxf.web.admin.loan;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.alibaba.fastjson.JSON;
import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanArbitration;
import com.jxf.loan.entity.NfsLoanArbitrationExecution;
import com.jxf.loan.entity.NfsLoanArbitrationExecution.ExecutionStatus;
import com.jxf.loan.entity.NfsLoanArbitrationExecutionDetail;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanArbitrationExecutionDetailService;
import com.jxf.loan.service.NfsLoanArbitrationExecutionService;
import com.jxf.loan.service.NfsLoanArbitrationService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.service.NfsActService;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.admin.sys.BaseController;

/**
 * 强执Controller
 * @author LIUHUAIXIN
 * @version 2018-12-20
 */
@Controller
@RequestMapping(value = "${adminPath}/loanArbitrationExecution")
public class NfsLoanArbitrationExecutionController extends BaseController {

	@Autowired
	private NfsLoanArbitrationExecutionService loanArbitrationExecutionService;
	@Autowired
	private NfsLoanArbitrationExecutionDetailService arbitrationExecutionDetailService;
	@Autowired
	private NfsLoanArbitrationService loanArbitrationService;
	@Autowired
	private NfsLoanRecordService nfsLoanRecordService;
	@Autowired
	private SendSmsMsgService sendSmsMsgService;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberActTrxService memberActTrxService;
	@Autowired
	private NfsActService nfsActService;
	
	
	@ModelAttribute
	public NfsLoanArbitrationExecution get(@RequestParam(required=false) Long id) {
		NfsLoanArbitrationExecution entity = null;
		if (id!=null){
			entity = loanArbitrationExecutionService.get(id);
		}
		if (entity == null){
			entity = new NfsLoanArbitrationExecution();
		}
		return entity;
	}
	
	@RequiresPermissions("loan:arbitrationExecution:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsLoanArbitrationExecution loanArbitrationExecution, HttpServletRequest request, HttpServletResponse response, Model model) {
		/*if(loanArbitrationExecution.getLoan()!=null) {
			Member loaneeMember = loanArbitrationExecution.getLoan().getLoanee();
			Member loanerMember = loanArbitrationExecution.getLoan().getLoaner();
			if(loaneeMember != null && loaneeMember.getUsername() != null){
				loaneeMember = memberService.findByUsername(loaneeMember.getUsername());
				if(loaneeMember != null) {
					Long loaneeId = loaneeMember.getId();
					loaneeMember = new Member();
					loaneeMember.setId(loaneeId);
				}
			}
			if(loanerMember != null && loanerMember.getUsername() != null){
				loanerMember = memberService.findByUsername(loanerMember.getUsername());
				if(loanerMember != null) {
					Long loanerId = loanerMember.getId();
					loanerMember = new Member();
					loanerMember.setId(loanerId);
				}
			}
			loanArbitrationExecution.getLoan().setLoanee(loaneeMember);
			loanArbitrationExecution.getLoan().setLoaner(loanerMember);
			loanArbitrationExecution.setLoan(loanArbitrationExecution.getLoan());
		}*/
		Page<NfsLoanArbitrationExecution> page = loanArbitrationExecutionService.findPage(new Page<NfsLoanArbitrationExecution>(request, response), loanArbitrationExecution); 
		model.addAttribute("page", page);
		return "admin/loan/arbitrationExecution/loanArbitrationExecutionList";
	}
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("loan:arbitrationExecution:view")
	@RequestMapping(value = "add")
	public String add(NfsLoanArbitrationExecution loanArbitrationExecution, Model model) {
		model.addAttribute("loanArbitrationExecution", loanArbitrationExecution);
		return "admin/loan/arbitrationExecution/loanArbitrationExecutionAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("loan:arbitrationExecution:view")
	@RequestMapping(value = "query")
	public String query(NfsLoanArbitrationExecution loanArbitrationExecution, Model model) {
		model.addAttribute("loanArbitrationExecution", loanArbitrationExecution);
		return "admin/loan/arbitrationExecution/loanArbitrationExecutionQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("loan:arbitrationExecution:view")
	@RequestMapping(value = "update")
	public String update(NfsLoanArbitrationExecution loanArbitrationExecution, Model model) {
		model.addAttribute("loanArbitrationExecution", loanArbitrationExecution);
		return "admin/loan/arbitrationExecution/loanArbitrationExecutionUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("loan:arbitrationExecution:edit")
	@RequestMapping(value = "save")
	public String save(NfsLoanArbitrationExecution loanArbitrationExecution, Model model, RedirectAttributes redirectAttributes) {
		
		NfsLoanRecord loan = nfsLoanRecordService.get(loanArbitrationExecution.getLoan());
		if (!beanValidator(model, loanArbitrationExecution)){
			return add(loanArbitrationExecution, model);
		}
		
		//强执借条关闭log
		if(loanArbitrationExecution.getStatus().equals(ExecutionStatus.debit)) {
			NfsLoanArbitrationExecutionDetail arbitrationExecutionDetail = new NfsLoanArbitrationExecutionDetail();
			arbitrationExecutionDetail.setExecutionId(loanArbitrationExecution.getId());
			arbitrationExecutionDetail.setStatus(NfsLoanArbitrationExecutionDetail.Status.debit);
			arbitrationExecutionDetail.setRmk(loanArbitrationExecution.getRmk());
			if(NfsLoanApply.RepayType.oneTimePrincipalAndInterest.equals(loan.getRepayType())) {
				arbitrationExecutionDetail.setType(NfsLoanArbitrationExecutionDetail.Type.fullAmount);
			}else {
				arbitrationExecutionDetail.setType(NfsLoanArbitrationExecutionDetail.Type.staging);
			}
			arbitrationExecutionDetailService.save(arbitrationExecutionDetail);
		}
				
		//强执已失效log
		if(loanArbitrationExecution.getStatus().equals(ExecutionStatus.executionExpired)) {
			NfsLoanArbitration loanArbitration = loanArbitrationService.get(loanArbitrationExecution.getArbitrationId());
			loanArbitration.setStrongStatus(NfsLoanArbitration.StrongStatus.notApplyStrong);
			loanArbitrationService.save(loanArbitration);
			
			NfsLoanArbitrationExecutionDetail arbitrationExecutionDetail = new NfsLoanArbitrationExecutionDetail();
			arbitrationExecutionDetail.setExecutionId(loanArbitrationExecution.getId());
			arbitrationExecutionDetail.setStatus(NfsLoanArbitrationExecutionDetail.Status.executionExpired);
			arbitrationExecutionDetail.setRmk(loanArbitrationExecution.getRmk());
			if(NfsLoanApply.RepayType.oneTimePrincipalAndInterest.equals(loan.getRepayType())) {
				arbitrationExecutionDetail.setType(NfsLoanArbitrationExecutionDetail.Type.fullAmount);
			}else {
				arbitrationExecutionDetail.setType(NfsLoanArbitrationExecutionDetail.Type.staging);
			}
			arbitrationExecutionDetailService.save(arbitrationExecutionDetail);
			//放款人
			String loanerPhoneNo = loan.getLoaner().getUsername();
			sendSmsMsgService.sendMessage("EnforcementInvalidSmsLoaner", loanerPhoneNo, null);
			
			//站内信
			memberMessageService.sendMessage(MemberMessage.Type.EnforcementInvalidImsLoaner,loan.getId());
		}		
		
		//强执失败log
		if(loanArbitrationExecution.getStatus().equals(ExecutionStatus.executionFailure)) {
			NfsLoanArbitrationExecutionDetail arbitrationExecutionDetail = new NfsLoanArbitrationExecutionDetail();
			arbitrationExecutionDetail.setExecutionId(loanArbitrationExecution.getId());
			arbitrationExecutionDetail.setStatus(NfsLoanArbitrationExecutionDetail.Status.executionFailure);
			arbitrationExecutionDetail.setRmk(loanArbitrationExecution.getRmk());
			if(NfsLoanApply.RepayType.oneTimePrincipalAndInterest.equals(loan.getRepayType())) {
				arbitrationExecutionDetail.setType(NfsLoanArbitrationExecutionDetail.Type.fullAmount);
			}else {
				arbitrationExecutionDetail.setType(NfsLoanArbitrationExecutionDetail.Type.staging);
			}
			arbitrationExecutionDetailService.save(arbitrationExecutionDetail);
			BigDecimal fee = loanArbitrationExecution.getFee();
			Member member = memberService.get(loanArbitrationExecution.getLoanerId());
			
			nfsActService.updateAct(TrxRuleConstant.ARBITRATION_EXECUTION_REFUND, fee, member, loanArbitrationExecution.getId());
			
			Long orgTrxId1 = 0L;
			MemberActTrx memberActTrx1 = new MemberActTrx();
			memberActTrx1.setOrgId(loanArbitrationExecution.getId());
			memberActTrx1.setTrxCode(TrxRuleConstant.ARBITRATION_EXECUTION_REFUND);
			List<MemberActTrx> list1 = memberActTrxService.findList(memberActTrx1);
			if(!Collections3.isEmpty(list1)) {
				orgTrxId1 = list1.get(0).getId();
			}
			memberMessageService.sendMessage(MemberMessage.Type.strongFailure,orgTrxId1);
		}		
				
		//强执已结束log
		if(loanArbitrationExecution.getStatus().equals(ExecutionStatus.executionOver)) {
			NfsLoanArbitrationExecutionDetail arbitrationExecutionDetail = new NfsLoanArbitrationExecutionDetail();
			arbitrationExecutionDetail.setExecutionId(loanArbitrationExecution.getId());
			arbitrationExecutionDetail.setStatus(NfsLoanArbitrationExecutionDetail.Status.executionOver);
			arbitrationExecutionDetail.setRmk(loanArbitrationExecution.getRmk());
			if(NfsLoanApply.RepayType.oneTimePrincipalAndInterest.equals(loan.getRepayType())) {
				arbitrationExecutionDetail.setType(NfsLoanArbitrationExecutionDetail.Type.fullAmount);
			}else {
				arbitrationExecutionDetail.setType(NfsLoanArbitrationExecutionDetail.Type.staging);
			}
			arbitrationExecutionDetailService.save(arbitrationExecutionDetail);
			//放款人
			String loanerPhoneNo = loan.getLoaner().getUsername();
			sendSmsMsgService.sendMessage("EnforcementEndSmsLoaner", loanerPhoneNo, null);
			
			//站内信
			memberMessageService.sendMessage(MemberMessage.Type.EnforcementEndImsLoaner,loan.getId());
		}				
				
		//强执缴费中log
		if(loanArbitrationExecution.getStatus().equals(ExecutionStatus.executionPayment)) {
			NfsLoanArbitrationExecutionDetail arbitrationExecutionDetail = new NfsLoanArbitrationExecutionDetail();
			arbitrationExecutionDetail.setExecutionId(loanArbitrationExecution.getId());
			arbitrationExecutionDetail.setStatus(NfsLoanArbitrationExecutionDetail.Status.executionPayment);
			arbitrationExecutionDetail.setRmk(loanArbitrationExecution.getRmk());
			if(NfsLoanApply.RepayType.oneTimePrincipalAndInterest.equals(loan.getRepayType())) {
				arbitrationExecutionDetail.setType(NfsLoanArbitrationExecutionDetail.Type.fullAmount);
			}else {
				arbitrationExecutionDetail.setType(NfsLoanArbitrationExecutionDetail.Type.staging);
			}
			arbitrationExecutionDetailService.save(arbitrationExecutionDetail);
			//放款人
			String loanerPhoneNo = loan.getLoaner().getUsername();
			sendSmsMsgService.sendMessage("EnforcementPaySmsLoaner", loanerPhoneNo, null);
			
			//站内信
			memberMessageService.sendMessage(MemberMessage.Type.EnforcementPayImsLoaner,loan.getId());
		}		
		
		//强执进行中log
		if(loanArbitrationExecution.getStatus().equals(ExecutionStatus.executionProcessing)) {
			NfsLoanArbitrationExecutionDetail arbitrationExecutionDetail = new NfsLoanArbitrationExecutionDetail();
			arbitrationExecutionDetail.setExecutionId(loanArbitrationExecution.getId());
			arbitrationExecutionDetail.setStatus(NfsLoanArbitrationExecutionDetail.Status.executionProcessing);
			arbitrationExecutionDetail.setRmk(loanArbitrationExecution.getRmk());
			if(NfsLoanApply.RepayType.oneTimePrincipalAndInterest.equals(loan.getRepayType())) {
				arbitrationExecutionDetail.setType(NfsLoanArbitrationExecutionDetail.Type.fullAmount);
			}else {
				arbitrationExecutionDetail.setType(NfsLoanArbitrationExecutionDetail.Type.staging);
			}
			arbitrationExecutionDetailService.save(arbitrationExecutionDetail);
			//放款人
			String loanerPhoneNo = loan.getLoaner().getUsername();
			sendSmsMsgService.sendMessage("EnforcementIngSmsLoaner", loanerPhoneNo, null);
			//站内信
			memberMessageService.sendMessage(MemberMessage.Type.EnforcementIngImsLoaner,loan.getId());
		}		
		
		//强执拒绝受理log
		if(loanArbitrationExecution.getStatus().equals(ExecutionStatus.executionRefuseToAccept)) {
			NfsLoanArbitrationExecutionDetail arbitrationExecutionDetail = new NfsLoanArbitrationExecutionDetail();
			arbitrationExecutionDetail.setExecutionId(loanArbitrationExecution.getId());
			arbitrationExecutionDetail.setStatus(NfsLoanArbitrationExecutionDetail.Status.executionRefuseToAccept);
			arbitrationExecutionDetail.setRmk(loanArbitrationExecution.getRmk());
			if(NfsLoanApply.RepayType.oneTimePrincipalAndInterest.equals(loan.getRepayType())) {
				arbitrationExecutionDetail.setType(NfsLoanArbitrationExecutionDetail.Type.fullAmount);
			}else {
				arbitrationExecutionDetail.setType(NfsLoanArbitrationExecutionDetail.Type.staging);
			}
			arbitrationExecutionDetailService.save(arbitrationExecutionDetail);
		}		
		
		loanArbitrationExecutionService.save(loanArbitrationExecution);
		addMessage(redirectAttributes, "保存操作成功");
		return "redirect:"+Global.getAdminPath()+"/loanArbitrationExecution/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("loan:arbitrationExecution:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsLoanArbitrationExecution loanArbitrationExecution, RedirectAttributes redirectAttributes) {
		loanArbitrationExecutionService.delete(loanArbitrationExecution);
		addMessage(redirectAttributes, "删除操作成功");
		return "redirect:"+Global.getAdminPath()+"/loanArbitrationExecution/?repage";
	}

}