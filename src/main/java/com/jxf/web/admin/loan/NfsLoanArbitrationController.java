package com.jxf.web.admin.loan;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanApply.RepayType;
import com.jxf.loan.entity.NfsLoanApply.TrxType;
import com.jxf.loan.entity.NfsLoanArbitration;
import com.jxf.loan.entity.NfsLoanArbitration.PreservationProcess;
import com.jxf.loan.entity.NfsLoanArbitration.Status;
import com.jxf.loan.entity.NfsLoanArbitrationDetail;
import com.jxf.loan.entity.NfsLoanContract;
import com.jxf.loan.entity.NfsLoanOperatingRecord;
import com.jxf.loan.entity.NfsLoanPreservation;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanArbitrationDetailService;
import com.jxf.loan.service.NfsLoanArbitrationService;
import com.jxf.loan.service.NfsLoanContractService;
import com.jxf.loan.service.NfsLoanOperatingRecordService;
import com.jxf.loan.service.NfsLoanPreservationService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.signature.youdun.YouDunConstant.NodeType;
import com.jxf.loan.signature.youdun.preservation.PreservationBuilderData;
import com.jxf.loan.signature.youdun.preservation.UdPreservationInfo;
import com.jxf.loan.signature.youdun.preservation.YouDunPreservation;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.mem.entity.MemberCard;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.entity.MemberMessage.Type;
import com.jxf.mem.entity.MemberVideoVerify;
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.mem.service.MemberCardService;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.service.MemberVideoVerifyService;
import com.jxf.mem.utils.MemUtils;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.entity.NfsRchgRecord;
import com.jxf.nfs.entity.NfsWdrlRecord;
import com.jxf.nfs.service.NfsActService;
import com.jxf.nfs.service.NfsRchgRecordService;
import com.jxf.nfs.service.NfsWdrlRecordService;
import com.jxf.payment.entity.Payment;
import com.jxf.payment.entity.Refund;
import com.jxf.payment.service.RefundService;
import com.jxf.svc.config.Constant;
import com.jxf.svc.config.Global;
import com.jxf.svc.font.MyFontsProvider;
import com.jxf.svc.model.Message;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.SnowFlake;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.admin.sys.BaseController;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 借条仲裁增删改查Controller
 * @author LIUHUAIXIN
 * @version 2018-11-07
 */
@Controller
@RequestMapping(value = "${adminPath}/loanArbitration")
public class NfsLoanArbitrationController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(NfsLoanArbitrationController.class);
	
	@Autowired
	private NfsLoanArbitrationService nfsLoanArbitrationService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private MemberCardService memberCardService;
	@Autowired
	private MemberActTrxService memberActTrxService;
	@Autowired
	private NfsRchgRecordService rchgRecordService;
	@Autowired
	private NfsWdrlRecordService wdrlRecordService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsLoanArbitrationDetailService arbitrationDetailService;
	@Autowired
	private SendSmsMsgService sendSmsMsgService;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private NfsActService nfsActService;
	@Autowired
	private MemberVideoVerifyService memberVideoVerifyService;
	@Autowired
	private RefundService refundService;
	@Autowired
	private NfsLoanPreservationService loanPreservationService;
	@Autowired
	private NfsLoanContractService loanContractService;
	@Autowired
	private NfsLoanOperatingRecordService loanOperatingRecordService;
	
	
//	@Autowired
//	private NfsLoanApplyService nfsLoanApplyService;
//	
//	@Autowired
//	private NfsLoanApplyDetailService nfsLoanApplyDetailService;
	
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
		if(nfsLoanArbitration.getLoan()!=null) {
			Member loaneeMember = nfsLoanArbitration.getLoan().getLoanee();
			Member loanerMember = nfsLoanArbitration.getLoan().getLoaner();
			if(loaneeMember != null && loaneeMember.getUsername() != null && !StringUtils.equals(loaneeMember.getUsername(), "")){
				loaneeMember = memberService.findByUsername(loaneeMember.getUsername());
				if(loaneeMember != null) {
					Long loaneeId = loaneeMember.getId();
					loaneeMember = new Member();
					loaneeMember.setId(loaneeId);
					nfsLoanArbitration.getLoan().setLoanee(loaneeMember);
				}
			}
			if(loanerMember != null && loanerMember.getUsername() != null && !StringUtils.equals(loanerMember.getUsername(), "")){
				loanerMember = memberService.findByUsername(loanerMember.getUsername());
				if(loanerMember != null) {
					Long loanerId = loanerMember.getId();
					loanerMember = new Member();
					loanerMember.setId(loanerId);
					nfsLoanArbitration.getLoan().setLoaner(loanerMember);
				}
			}
			nfsLoanArbitration.setLoan(nfsLoanArbitration.getLoan());
		}
		Page<NfsLoanArbitration> page = nfsLoanArbitrationService.findPage(new Page<NfsLoanArbitration>(request, response), nfsLoanArbitration); 
		List<NfsLoanArbitration> list = page.getList();
		for (NfsLoanArbitration nfsLoanArbitration2 : list) {
			NfsLoanRecord loan = nfsLoanArbitration2.getLoan();
			Member loanee = loan.getLoanee();
			loan.setLoanee(MemUtils.mask(loanee));
			Member loaner = loan.getLoaner();
			loan.setLoaner(MemUtils.mask(loaner));
			nfsLoanArbitration2.setLoan(loan);
		}
		page.setList(list);
		model.addAttribute("page", page);
		return "admin/loan/arbitration/nfsLoanArbitrationList";
	}
	
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("loan:nfsLoanArbitration:view")
	@RequestMapping(value = "add")
	public String add(NfsLoanArbitration nfsLoanArbitration, Model model) {
		model.addAttribute("nfsLoanArbitration", nfsLoanArbitration);
		return "admin/loan/arbitration/nfsLoanArbitrationAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("loan:nfsLoanArbitration:view")
	@RequestMapping(value = "query")
	public String query(NfsLoanArbitration nfsLoanArbitration, Model model) {
		model.addAttribute("nfsLoanArbitration", nfsLoanArbitration);
		return "admin/loan/arbitration/nfsLoanArbitrationQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("loan:nfsLoanArbitration:view")
	@RequestMapping(value = "update")
	public String update(NfsLoanArbitration nfsLoanArbitration, Model model) {
		model.addAttribute("nfsLoanArbitration", nfsLoanArbitration);
		return "admin/loan/arbitration/nfsLoanArbitrationUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("loan:nfsLoanArbitration:edit")
	@RequestMapping(value = "save")
	public String save(NfsLoanArbitration nfsLoanArbitration, Model model, RedirectAttributes redirectAttributes) {
		NfsLoanRecord loan = loanRecordService.get(nfsLoanArbitration.getLoan().getId());
		if (!beanValidator(model, nfsLoanArbitration)){
			return add(nfsLoanArbitration, model);
		}
		
		Member loaner = memberService.get(loan.getLoaner());
		Member loanee = memberService.get(loan.getLoanee());
		//仲裁申请中log
		if(nfsLoanArbitration.getStatus().equals(Status.application)) {
			NfsLoanArbitrationDetail arbitrationDetail = new NfsLoanArbitrationDetail();
			arbitrationDetail.setArbitrationId(nfsLoanArbitration.getId());
			arbitrationDetail.setStatus(NfsLoanArbitrationDetail.Status.agentArbitrationApplication);
			arbitrationDetail.setTask(NfsLoanArbitrationDetail.Task.theAuditHasBeenApproved);
			arbitrationDetail.setRmk(nfsLoanArbitration.getRmk());
			arbitrationDetail.setType(NfsLoanArbitrationDetail.Type.auditResult);
			arbitrationDetailService.save(arbitrationDetail);
			
			if(loan.getTrxType().equals(NfsLoanApply.TrxType.offline)) {
				memberMessageService.sendMessage(MemberMessage.Type.acceptanceArbitrationLoaner,loan.getId());
				
				memberMessageService.sendMessage(MemberMessage.Type.acceptanceArbitrationLoanee,loan.getId());
			}
		}
		//仲裁已缴费log
		if(nfsLoanArbitration.getStatus().equals(Status.paid)) {
			NfsLoanArbitrationDetail arbitrationDetail = new NfsLoanArbitrationDetail();
			arbitrationDetail.setArbitrationId(nfsLoanArbitration.getId());
			arbitrationDetail.setStatus(NfsLoanArbitrationDetail.Status.paid);
			arbitrationDetail.setTask(NfsLoanArbitrationDetail.Task.paid);
			arbitrationDetail.setRmk(nfsLoanArbitration.getRmk());
			arbitrationDetail.setType(NfsLoanArbitrationDetail.Type.arbitrationProcess);
			arbitrationDetailService.save(arbitrationDetail);
			if(loan.getTrxType().equals(NfsLoanApply.TrxType.online)) {
				//放款人
				String loanerPhoneNo = loan.getLoaner().getUsername();
				sendSmsMsgService.sendMessage("paymentArbitrationSmsLoaner", loanerPhoneNo, null);
				
				//站内信
				memberMessageService.sendMessage(Type.paymentArbitrationImsLoaner,loan.getId());
			}
		}
		//仲裁出裁决log
		if(nfsLoanArbitration.getStatus().equals(Status.arbitration)) {
			NfsLoanArbitrationDetail arbitrationDetail = new NfsLoanArbitrationDetail();
			arbitrationDetail.setArbitrationId(nfsLoanArbitration.getId());
			arbitrationDetail.setStatus(NfsLoanArbitrationDetail.Status.arbitrationHasBeenDecided);
			arbitrationDetail.setTask(NfsLoanArbitrationDetail.Task.arbitralAwardEnforcement);
			arbitrationDetail.setRmk(nfsLoanArbitration.getRmk());
			arbitrationDetail.setType(NfsLoanArbitrationDetail.Type.arbitrationResult);
			arbitrationDetailService.save(arbitrationDetail);
			
			NfsLoanArbitrationDetail loanArbitrationDetail = new NfsLoanArbitrationDetail();
			loanArbitrationDetail.setArbitrationId(nfsLoanArbitration.getId());
			loanArbitrationDetail.setStatus(NfsLoanArbitrationDetail.Status.arbitrationHasBeenDecided);
			loanArbitrationDetail.setTask(NfsLoanArbitrationDetail.Task.ArbitrationHasPronouncedTheVerdict);
			loanArbitrationDetail.setRmk(nfsLoanArbitration.getRmk());
			loanArbitrationDetail.setType(NfsLoanArbitrationDetail.Type.arbitrationResult);
			arbitrationDetailService.save(loanArbitrationDetail);
			
			if(loan.getTrxType().equals(NfsLoanApply.TrxType.offline)) {
				memberMessageService.sendMessage(MemberMessage.Type.acceptanceVerdictLoaner,loan.getId());
				
				memberMessageService.sendMessage(MemberMessage.Type.acceptanceVerdictLoanee,loan.getId());
			}else {
				//放款人
				String loanerPhoneNo = loaner.getUsername();
				sendSmsMsgService.sendMessage("awardArbitrationSmsLoaner", loanerPhoneNo, null);
				
				//站内信
				memberMessageService.sendMessage(Type.awardArbitrationImsLoaner,loan.getId());
			}
		}
		//仲裁失败log
		if(nfsLoanArbitration.getStatus().equals(Status.arbitrationFailure)) {
			NfsLoanArbitrationDetail arbitrationDetail = new NfsLoanArbitrationDetail();
			arbitrationDetail.setArbitrationId(nfsLoanArbitration.getId());
			arbitrationDetail.setStatus(NfsLoanArbitrationDetail.Status.arbitrationFailure);
			arbitrationDetail.setTask(NfsLoanArbitrationDetail.Task.arbitrationFailure);
			arbitrationDetail.setType(NfsLoanArbitrationDetail.Type.arbitrationResult);
			arbitrationDetailService.save(arbitrationDetail);
			
			NfsLoanArbitrationDetail loanArbitrationDetail = new NfsLoanArbitrationDetail();
			loanArbitrationDetail.setArbitrationId(nfsLoanArbitration.getId());
			loanArbitrationDetail.setStatus(NfsLoanArbitrationDetail.Status.arbitrationHasBeenDecided);
			loanArbitrationDetail.setTask(NfsLoanArbitrationDetail.Task.arbitrationHasInformedTheReasonForTheFailure);
			loanArbitrationDetail.setRmk(nfsLoanArbitration.getRmk());
			loanArbitrationDetail.setType(NfsLoanArbitrationDetail.Type.arbitrationResult);
			arbitrationDetailService.save(loanArbitrationDetail);
		}
		//审核失败log
		if(nfsLoanArbitration.getStatus().equals(Status.auditFailure)) {
			NfsLoanArbitration nfsLoanArbitration2 = nfsLoanArbitrationService.get(nfsLoanArbitration.getId());
			if(nfsLoanArbitration2.getStatus().equals(Status.auditFailure)) {
				addMessage(redirectAttributes, "仲裁状态已改变，请勿重复操作");
				return "redirect:"+Global.getAdminPath()+"/loanArbitration/?repage";
			}
			NfsLoanArbitrationDetail arbitrationDetail = new NfsLoanArbitrationDetail();
			arbitrationDetail.setArbitrationId(nfsLoanArbitration.getId());
			arbitrationDetail.setStatus(NfsLoanArbitrationDetail.Status.theAuditFailed);
			arbitrationDetail.setTask(NfsLoanArbitrationDetail.Task.auditDataIsAbnormal);
			arbitrationDetail.setRmk(nfsLoanArbitration.getRmk());
			arbitrationDetail.setType(NfsLoanArbitrationDetail.Type.auditResult);
			arbitrationDetailService.save(arbitrationDetail);
			
			NfsLoanArbitrationDetail loanArbitrationDetail = new NfsLoanArbitrationDetail();
			loanArbitrationDetail.setArbitrationId(nfsLoanArbitration.getId());
			loanArbitrationDetail.setStatus(NfsLoanArbitrationDetail.Status.arbitrationFailure);
			loanArbitrationDetail.setTask(NfsLoanArbitrationDetail.Task.theAuditFailed);
			loanArbitrationDetail.setRmk(nfsLoanArbitration.getRmk());
			loanArbitrationDetail.setType(NfsLoanArbitrationDetail.Type.auditResult);
			arbitrationDetailService.save(loanArbitrationDetail);
			
			loan.setArbitrationStatus(NfsLoanRecord.ArbitrationStatus.end);
			loanRecordService.save(loan);
			
			BigDecimal refundAmount =new BigDecimal(nfsLoanArbitration.getRefundFee()); 
			NfsLoanRecord loanRecord = loanRecordService.get(nfsLoanArbitration.getLoan());
			Long orgTrxId1 = 0L;
			Refund refund = new Refund();
			refund.setOrgId(nfsLoanArbitration.getId().toString());
			refund.setStatus(Refund.Status.success);
			List<Refund> refunds = refundService.findList(refund);
			if(!Collections3.isEmpty(refunds)){
				addMessage(redirectAttributes, "仲裁退费已成功，请勿重复退费！");
				return "redirect:"+Global.getAdminPath()+"/loanArbitration/?repage";
			}
			if (loanRecord.getTrxType().equals(TrxType.offline)) {
				List<MemberActTrx> list = memberActTrxService.findListByOrgId(nfsLoanArbitration.getId());
				for (MemberActTrx memberActTrx : list) {
					if (memberActTrx.getTrxCode().equals(TrxRuleConstant.GXT_ARBITRATION_PREPAY)) {
						// 调用微信退款接口 添加微信退款流水
						boolean result = refundService.wxRefund(loaner, Payment.Type.arbitration, nfsLoanArbitration.getId(),refundAmount);
						if (result) {
							// 生成退款流水
							int code = nfsActService.updateAct(TrxRuleConstant.GXT_ARBITRATION_REFUND, refundAmount,loaner, nfsLoanArbitration.getId());
							if (code == Constant.UPDATE_FAILED) {
								throw new RuntimeException("放款人： "+ loaner.getId() + "仲裁:"+nfsLoanArbitration.getId()+"立案失败，退还微信账户更新失败！");
							}
						} else {
							throw new RuntimeException("放款人： "+ loaner.getId() + "仲裁:"+nfsLoanArbitration.getId()+"立案失败，退款请求失败！");
						}
					}else if(memberActTrx.getTrxCode().equals(TrxRuleConstant.ARBITRATION_PREPAY)){
						nfsActService.updateAct(TrxRuleConstant.ARBITRATION_REFUND, refundAmount, loaner, nfsLoanArbitration.getId());
					}else {
						logger.error("仲裁申请交易项目为：{}",memberActTrx.getTrxCode());
					}
				}
				MemberActTrx memberActTrx1 = new MemberActTrx();
				memberActTrx1.setOrgId(nfsLoanArbitration.getId());
				memberActTrx1.setTrxCode(TrxRuleConstant.ARBITRATION_REFUND);
				List<MemberActTrx> list1 = memberActTrxService.findList(memberActTrx1);
				if(!Collections3.isEmpty(list1)) {
					orgTrxId1 = list1.get(0).getId();
				}else {
					memberActTrx1.setTrxCode(TrxRuleConstant.GXT_ARBITRATION_REFUND);
					List<MemberActTrx> list2 = memberActTrxService.findList(memberActTrx1);
					if(!Collections3.isEmpty(list2)) {
						orgTrxId1 = list2.get(0).getId();
					}
				}
			}else {
				nfsActService.updateAct(TrxRuleConstant.ARBITRATION_REFUND, refundAmount, loaner, nfsLoanArbitration.getId());
				MemberActTrx memberActTrx1 = new MemberActTrx();
				memberActTrx1.setOrgId(nfsLoanArbitration.getId());
				memberActTrx1.setTrxCode(TrxRuleConstant.ARBITRATION_REFUND);
				List<MemberActTrx> list1 = memberActTrxService.findList(memberActTrx1);
				if(!Collections3.isEmpty(list1)) {
					orgTrxId1 = list1.get(0).getId();
				}
			}
			memberMessageService.sendMessage(MemberMessage.Type.arbitrationFailure,orgTrxId1);
		}
		//立案失败log
		if(nfsLoanArbitration.getStatus().equals(Status.failureToFile)) {
			NfsLoanArbitrationDetail arbitrationDetail = new NfsLoanArbitrationDetail();
			arbitrationDetail.setArbitrationId(nfsLoanArbitration.getId());
			arbitrationDetail.setStatus(NfsLoanArbitrationDetail.Status.failureToFile);
			arbitrationDetail.setTask(NfsLoanArbitrationDetail.Task.arbitrationFailedToPassTheCase);
			arbitrationDetail.setRmk(nfsLoanArbitration.getRmk());
			arbitrationDetail.setType(NfsLoanArbitrationDetail.Type.filedResult);
			arbitrationDetailService.save(arbitrationDetail);

			NfsLoanArbitrationDetail loanArbitrationDetail = new NfsLoanArbitrationDetail();
			loanArbitrationDetail.setArbitrationId(nfsLoanArbitration.getId());
			loanArbitrationDetail.setStatus(NfsLoanArbitrationDetail.Status.arbitrationHasBeenDecided);
			loanArbitrationDetail.setTask(NfsLoanArbitrationDetail.Task.filingFailed);
			loanArbitrationDetail.setRmk(nfsLoanArbitration.getRmk());
			loanArbitrationDetail.setType(NfsLoanArbitrationDetail.Type.filedResult);
			arbitrationDetailService.save(loanArbitrationDetail);
			//立案失败不退款
			
		}
		//仲裁中log
		if(nfsLoanArbitration.getStatus().equals(Status.inArbitration)) {
			NfsLoanArbitrationDetail arbitrationDetail = new NfsLoanArbitrationDetail();
			arbitrationDetail.setArbitrationId(nfsLoanArbitration.getId());
			arbitrationDetail.setStatus(NfsLoanArbitrationDetail.Status.applicating);
			arbitrationDetail.setTask(NfsLoanArbitrationDetail.Task.theAuditHasBeenApproved);
			arbitrationDetail.setRmk(nfsLoanArbitration.getRmk());
			arbitrationDetail.setType(NfsLoanArbitrationDetail.Type.arbitrationProcess);
			arbitrationDetailService.save(arbitrationDetail);
			
			if(loan.getTrxType().equals(NfsLoanApply.TrxType.offline)) {
				memberMessageService.sendMessage(MemberMessage.Type.waitingCourt,loan.getId());
			}
			
		}
		//审核中log
		if(nfsLoanArbitration.getStatus().equals(Status.review)) {
			NfsLoanArbitrationDetail arbitrationDetail = new NfsLoanArbitrationDetail();
			arbitrationDetail.setArbitrationId(nfsLoanArbitration.getId());
			arbitrationDetail.setStatus(NfsLoanArbitrationDetail.Status.underReview);
			arbitrationDetail.setTask(NfsLoanArbitrationDetail.Task.manualReview);
			arbitrationDetail.setRmk(nfsLoanArbitration.getRmk());
			arbitrationDetail.setType(NfsLoanArbitrationDetail.Type.auditProcess);
			arbitrationDetailService.save(arbitrationDetail);
			
		}
		//仲裁中log
		if(nfsLoanArbitration.getStatus().equals(Status.inArbitration)) {
			NfsLoanArbitrationDetail arbitrationDetail = new NfsLoanArbitrationDetail();
			arbitrationDetail.setArbitrationId(nfsLoanArbitration.getId());
			arbitrationDetail.setStatus(NfsLoanArbitrationDetail.Status.applicating);
			arbitrationDetail.setTask(NfsLoanArbitrationDetail.Task.arbitrationTrialIsInProgress);
			arbitrationDetail.setRmk(nfsLoanArbitration.getRmk());
			arbitrationDetail.setType(NfsLoanArbitrationDetail.Type.arbitrationProcess);
			arbitrationDetailService.save(arbitrationDetail);
			
			if(loan.getTrxType().equals(TrxType.online)) {
				//借款人
				String loaneePhoneNo = loanee.getUsername();
				sendSmsMsgService.sendMessage("acceptArbitrationSmsLoanee", loaneePhoneNo, null);
				//放款人
				String loanerPhoneNo = loaner.getUsername();
				sendSmsMsgService.sendMessage("acceptArbitrationSmsLoaner", loanerPhoneNo, null);
				
				//站内信
				memberMessageService.sendMessage(Type.acceptArbitrationImsLoanee,loan.getId());
				
				//站内信
				memberMessageService.sendMessage(Type.acceptArbitrationImsLoaner,loan.getId());
			}
		}
		//借条关闭log
		if(nfsLoanArbitration.getStatus().equals(Status.debit)) {
			NfsLoanArbitrationDetail arbitrationDetail = new NfsLoanArbitrationDetail();
			arbitrationDetail.setArbitrationId(nfsLoanArbitration.getId());
			arbitrationDetail.setStatus(NfsLoanArbitrationDetail.Status.debit);
			arbitrationDetail.setTask(NfsLoanArbitrationDetail.Task.debit);
			arbitrationDetail.setRmk(nfsLoanArbitration.getRmk());
			arbitrationDetail.setType(NfsLoanArbitrationDetail.Type.filedResult);
			arbitrationDetailService.save(arbitrationDetail);
			
		}
		nfsLoanArbitrationService.save(nfsLoanArbitration);
		addMessage(redirectAttributes, "保存借条仲裁成功");
		return "redirect:"+Global.getAdminPath()+"/loanArbitration/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("loan:nfsLoanArbitration:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsLoanArbitration nfsLoanArbitration, RedirectAttributes redirectAttributes) {
		nfsLoanArbitrationService.delete(nfsLoanArbitration);
		addMessage(redirectAttributes, "删除借条仲裁成功");
		return "redirect:"+Global.getAdminPath()+"/loanArbitration/?repage";
	}
	
	
	/**
	 * 证据调取
	 */
	@RequiresPermissions("loan:nfsLoanArbitration:view")
	@RequestMapping(value = "loanCaseData")
	public String loanCaseData(Long loanId, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		
		NfsLoanRecord loanRecord = loanRecordService.listCase(loanId);
		if(loanRecord==null) {
			addMessage(redirectAttributes, "该借条不存在");
			return "admin/loan/arbitration/loanCaseData";
		}
		BigDecimal loanerCurBal = memberService.getCulBal(loanRecord.getLoaner());
		BigDecimal loaneeCurBal = memberService.getCulBal(loanRecord.getLoanee());
		
		MemberCard memberLoanerCard = memberCardService.getCardByMemberId(loanRecord.getLoaner().getId());
		MemberCard memberLoaneeCard = memberCardService.getCardByMemberId(loanRecord.getLoanee().getId());
		
		
		List<MemberActTrx> loanerActTrxList = memberActTrxService.getTrxByLoanArbitrationProof(loanRecord.getLoaner(),loanRecord);
		List<MemberActTrx> loaneeActTrxList = memberActTrxService.getTrxByLoanArbitrationProof(loanRecord.getLoanee(),loanRecord);
		List<NfsRchgRecord> nfsRchgRecordList = rchgRecordService.getRchgRecordByMemberId(loanRecord.getLoaner().getId(),loanRecord.getCreateTime());
		List<NfsWdrlRecord> nfsWlrdRecordList = wdrlRecordService.getWdrlRecordByMemberId(loanRecord.getLoanee().getId(),loanRecord.getCreateTime());
		model.addAttribute("loanRecord", loanRecord);
		model.addAttribute("loanerCard", memberLoanerCard);
		model.addAttribute("loaneeCard", memberLoaneeCard);
		model.addAttribute("loanerActTrxList", loanerActTrxList);
		model.addAttribute("loaneeActTrxList", loaneeActTrxList);
		model.addAttribute("nfsRchgRecordList", nfsRchgRecordList);
		model.addAttribute("nfsWlrdRecordList", nfsWlrdRecordList);
		model.addAttribute("loanerCurBal", StringUtils.decimalToStr(loanerCurBal, 2));
		model.addAttribute("loaneeCurBal", StringUtils.decimalToStr(loaneeCurBal, 2));
		
		return "admin/loan/arbitration/loanCaseData";
	}
	
	/**
	 * 转出pdf
	 */
	@RequiresPermissions("loan:nfsLoanArbitration:view")
	@RequestMapping(value="exportPdf")
	public void exportPdf(Long loanId, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		try {
			
			NfsLoanRecord loanRecord = loanRecordService.listCase(loanId);
			BigDecimal loanerCurBal = memberService.getCulBal(loanRecord.getLoaner());
			BigDecimal loaneeCurBal = memberService.getCulBal(loanRecord.getLoanee());
			
			MemberCard memberLoanerCard = memberCardService.getCardByMemberId(loanRecord.getLoaner().getId());
			MemberCard memberLoaneeCard = memberCardService.getCardByMemberId(loanRecord.getLoanee().getId());
			List<MemberActTrx> loanerActTrxList = memberActTrxService.getTrxByLoanArbitrationProof(loanRecord.getLoaner(),loanRecord);
			List<MemberActTrx> loaneeActTrxList = memberActTrxService.getTrxByLoanArbitrationProof(loanRecord.getLoanee(),loanRecord);
			List<NfsRchgRecord> nfsRchgRecordList = rchgRecordService.getRchgRecordByMemberId(loanRecord.getLoaner().getId(),loanRecord.getCreateTime());
			List<NfsWdrlRecord> nfsWlrdRecordList = wdrlRecordService.getWdrlRecordByMemberId(loanRecord.getLoanee().getId(),loanRecord.getCreateTime());
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("nfsLoanRecord", loanRecord);
			map.put("memberLoanerCard", memberLoanerCard);
			map.put("memberLoaneeCard", memberLoaneeCard);
			map.put("memberLoanerActTrxList", loanerActTrxList);
			map.put("memberLoaneeActTrxList", loaneeActTrxList);
			map.put("nfsRchgRecordList", nfsRchgRecordList);
			map.put("nfsWlrdRecordList", nfsWlrdRecordList);
			map.put("loanerCurBal", StringUtils.decimalToStr(loanerCurBal, 2));
			map.put("loaneeCurBal", StringUtils.decimalToStr(loaneeCurBal, 2));
			
						
			Template template = freeMarkerConfigurer.getConfiguration().getTemplate("/ftl/loanCaseData.ftl");
			StringWriter result = new StringWriter();
			try {
				template.process(map, result);
			} catch (TemplateException e) {
				logger.error(Exceptions.getStackTraceAsString(e));
			}
			response.setContentType("application/pdf");
			OutputStream os = response.getOutputStream();
			Document document = new Document(new RectangleReadOnly(842F, 1000F));
			PdfWriter writer = PdfWriter.getInstance(document, os);			

			document.open();
		    // 使用字体提供器，并将其设置为unicode字体样式
		    MyFontsProvider fontProvider = new MyFontsProvider();
		    fontProvider.addFontSubstitute("lowagie", "garamond");
		    fontProvider.setUseUnicode(true);
		    CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
		    HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
		    htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
		    XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, 
			        new ByteArrayInputStream(result.toString().getBytes("Utf-8")),  
			        Charset.forName("UTF-8"),fontProvider);  
			document.close();
			response.flushBuffer();
		} catch (IOException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}
		catch (DocumentException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}
	
	}
	

	/**
	 * 证据调取
	 */
	@RequiresPermissions("loan:nfsLoanArbitration:view")
	@RequestMapping(value = "loanCard")
	public String loanCard(Long id, HttpServletRequest request, HttpServletResponse response, Model model) {
		NfsLoanRecord nfsLoanRecord = loanRecordService.listCase(id);
		model.addAttribute("nfsLoanRecord", nfsLoanRecord);
		return "admin/loan/arbitration/loanCard";
	}
	
	/**
	 * 转出pdf
	 */
	@RequiresPermissions("loan:nfsLoanArbitration:view")
	@RequestMapping(value="exportCardPdf")
	public void exportCardPdf(Long id, HttpServletRequest request, HttpServletResponse response, Model model) {
		Document document = new Document(new RectangleReadOnly(842F, 1000F));
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			NfsLoanRecord nfsLoanRecord = loanRecordService.listCase(id);
			map.put("nfsLoanRecord", nfsLoanRecord);
						
			Template template = freeMarkerConfigurer.getConfiguration().getTemplate("/ftl/loanCard.ftl");
			StringWriter result = new StringWriter();
			try {
				template.process(map, result);
			} catch (TemplateException e) {
				logger.error(Exceptions.getStackTraceAsString(e));
			}
			response.setContentType("application/pdf");
			OutputStream os = response.getOutputStream();
			PdfWriter writer = PdfWriter.getInstance(document, os);			

			document.open();
		    // 使用字体提供器，并将其设置为unicode字体样式
		    MyFontsProvider fontProvider = new MyFontsProvider();
		    fontProvider.addFontSubstitute("lowagie", "garamond");
		    fontProvider.setUseUnicode(true);
		    CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
		    HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
		    htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
		    XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, 
			        new ByteArrayInputStream(result.toString().getBytes("Utf-8")),  
			        Charset.forName("UTF-8"),fontProvider);  
			document.close();
			response.flushBuffer();
		} catch (IOException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}
		catch (DocumentException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}
	
	}
	
	/**
	 * 调取仲裁详情记录
	 */
	@RequiresPermissions("loan:nfsLoanArbitration:view")
	@RequestMapping("arbitrationDetailList")
	public String arbitrationDetailList(Long id,HttpServletRequest request,HttpServletResponse response, Model model) {
		NfsLoanArbitrationDetail nfsLoanArbitrationDetail = new NfsLoanArbitrationDetail();
		nfsLoanArbitrationDetail.setArbitrationId(id);
		Page<NfsLoanArbitrationDetail> page = arbitrationDetailService.findPage(new Page<NfsLoanArbitrationDetail>(request, response), nfsLoanArbitrationDetail);
		model.addAttribute("page", page);
		return "admin/loan/arbitration/detail/nfsLoanArbitrationDetailList";
	}
	
	/**
	 * 调取身份证正反面照片
	 */
	@RequiresPermissions("loan:nfsLoanArbitration:view")
	@RequestMapping("IdCardPhoto")
	public String IdCardPhoto(Long memberId,HttpServletRequest request,HttpServletResponse response, Model model) {
		MemberVideoVerify memberVideoVerify = memberVideoVerifyService.getMemberVideoVerifyByMemberId(memberId);
		model.addAttribute("memberVideoVerify", memberVideoVerify);
		model.addAttribute("urlfont", Global.getConfig("domain")+memberVideoVerify.getIdcardFrontPhoto());
		model.addAttribute("urlback", Global.getConfig("domain")+memberVideoVerify.getIdcardBackPhoto());
		return "admin/loan/arbitration/IdCardPhoto";
	}
	
	/**
	 * 转出pdf
	 */
	@RequiresPermissions("loan:nfsLoanArbitration:view")
	@RequestMapping(value="exportIdCardPdf")
	public void exportIdCardPdf(Long id, HttpServletRequest request, HttpServletResponse response, Model model) {
		Document document = new Document(new RectangleReadOnly(842F, 1000F));
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			MemberVideoVerify memberVideoVerify = memberVideoVerifyService.getMemberVideoVerifyByMemberId(id);
			map.put("memberVideoVerify", memberVideoVerify);
			map.put("urlfont", Global.getConfig("domain")+memberVideoVerify.getIdcardFrontPhoto());
			map.put("urlback", Global.getConfig("domain")+memberVideoVerify.getIdcardBackPhoto());
			Template template = freeMarkerConfigurer.getConfiguration().getTemplate("/ftl/IdCardPhoto.ftl");
			StringWriter result = new StringWriter();
			try {
				template.process(map, result);
			} catch (TemplateException e) {
				logger.error(Exceptions.getStackTraceAsString(e));
			}
			response.setContentType("application/pdf");
			OutputStream os = response.getOutputStream();
			PdfWriter writer = PdfWriter.getInstance(document, os);			

			document.open();
		    // 使用字体提供器，并将其设置为unicode字体样式
		    MyFontsProvider fontProvider = new MyFontsProvider();
		    fontProvider.addFontSubstitute("lowagie", "garamond");
		    fontProvider.setUseUnicode(true);
		    CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
		    HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
		    htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
		    XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, 
			        new ByteArrayInputStream(result.toString().getBytes("Utf-8")),  
			        Charset.forName("UTF-8"),fontProvider);  
			document.close();
			response.flushBuffer();
		} catch (IOException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}
		catch (DocumentException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}
	}
	
	/**
	 * 查看借条当前电子合同
	 */
	@RequiresPermissions("loan:nfsLoanArbitration:view")
	@RequestMapping("checkContract")
	public String checkContract(Long loanId,HttpServletRequest request,HttpServletResponse response, Model model) {
		NfsLoanContract loanContract = loanContractService.getCurrentContractByLoanId(loanId);
		String contractUrl = Global.getConfig("domain") + loanContract.getContractUrl();
		return "redirect:"+contractUrl;
	}
	
	
	/**
	 * 上传案件页面跳转
	 */
	@RequiresPermissions("loan:nfsLoanArbitration:view")
	@RequestMapping(value = "uploadCase")
	public String uploadCase(NfsLoanArbitration nfsLoanArbitration, Model model) {
		nfsLoanArbitration = nfsLoanArbitrationService.get(nfsLoanArbitration);
		NfsLoanRecord loanRecord = loanRecordService.get(nfsLoanArbitration.getLoan());
		nfsLoanArbitration.setLoan(loanRecord);
		model.addAttribute("nfsLoanArbitration", nfsLoanArbitration);
		return "admin/loan/arbitration/uploadCase";
	}
	
	/**
	 * 上传仲裁凭证页面跳转
	 */
	@RequiresPermissions("loan:nfsLoanArbitration:view")
	@RequestMapping(value = "uploadImages")
	public String uploadImages(String loanId, Model model) {
		NfsLoanArbitration nfsLoanArbitration = nfsLoanArbitrationService.getByLoanId(Long.valueOf(loanId));
		model.addAttribute("nfsLoanArbitration", nfsLoanArbitration);
		return "admin/loan/arbitration/uploadImages";
	}
	
	/**
	 * 查看仲裁凭证
	 */
	@RequiresPermissions("loan:nfsLoanArbitration:view")
	@RequestMapping("seeImages")
	public String seeImages(Long id,HttpServletRequest request,HttpServletResponse response, Model model) {
		NfsLoanArbitration loanArbitration = nfsLoanArbitrationService.get(id);
		List<String> imageUrls = new ArrayList<String>();
		if(loanArbitration != null && StringUtils.isNotBlank(loanArbitration.getImages())) {
			String[] imageUrlArrays = loanArbitration.getImages().split(",");
			imageUrls = Arrays.asList(imageUrlArrays);
		}
		model.addAttribute("imageUrlList", imageUrls);
		return "admin/loan/arbitration/arbitrationImages";
	}
	
	
	/**
	 * 上传仲裁凭证
	 */
	@RequiresPermissions("loan:nfsLoanArbitration:edit")
	@RequestMapping(value = "uploadArbitrationImages")
	@ResponseBody
	public Message uploadArbitrationImages(String iamges,String id) {
		NfsLoanArbitration nfsLoanArbitration = nfsLoanArbitrationService.get(Long.valueOf(id));
		
	    String images2 = nfsLoanArbitration.getImages();
		if(StringUtils.isNotBlank(images2)) {
			images2 = images2 + "," + iamges;
		}else {
			images2 = iamges;
		}
		nfsLoanArbitration.setImages(images2);
		nfsLoanArbitrationService.save(nfsLoanArbitration);
		return Message.success("上传成功");
	}
	
	/**
	 * 上传有盾仲裁案件
	 * @param loanRecord
	 * @return
	 */
	@RequiresPermissions("loan:nfsLoanArbitration:edit")
	@RequestMapping(value="uploadLoanToUdPreservation")
	@ResponseBody
	public Message uploadLoanToUdPreservation(NfsLoanArbitration nfsLoanArbitration,HttpServletRequest request) {
		nfsLoanArbitrationService.save(nfsLoanArbitration);
		NfsLoanRecord loanRecord = loanRecordService.get(nfsLoanArbitration.getLoan());
		Member loanee = memberService.get(loanRecord.getLoanee());
		Member loaner = memberService.get(loanRecord.getLoaner());
		//校验身份证照片
		MemberVideoVerify loaneeVideoVerify = memberVideoVerifyService.getMemberVideoVerifyByMemberId(loanee.getId());
		if (loaneeVideoVerify != null) {
			if (StringUtils.isBlank(loaneeVideoVerify.getIdcardFrontPhoto())
					|| StringUtils.isBlank(loaneeVideoVerify.getIdcardBackPhoto())) {
				return Message.error("借款人身份证照片不全，请用户重新认证后再提交！");
			}
		} else {
			return Message.error("借款人身份证照片不全,请用户重新认证后再提交！");
		}
		
		MemberVideoVerify loanerVideoVerify = memberVideoVerifyService.getMemberVideoVerifyByMemberId(loaner.getId());
		if (loanerVideoVerify != null) {
			if (StringUtils.isBlank(loanerVideoVerify.getIdcardFrontPhoto())
					|| StringUtils.isBlank(loanerVideoVerify.getIdcardBackPhoto())) {
				return Message.error("放款人身份证照片丢失，请用户重新认证后再提交！");
			}
		} else {
			return Message.error("放款人身份证照片不全,请用户重新认证后再提交！");
		}
		
		//校验借款人提现信息
		NfsWdrlRecord loaneeWdrlRecord = null;
		if (loanRecord.getTrxType().equals(TrxType.online)) {
			NfsWdrlRecord nfsWdrlRecord = new NfsWdrlRecord();
			nfsWdrlRecord.setMember(loanee);
			List<NfsWdrlRecord> wdrlRecordList = wdrlRecordService.findList(nfsWdrlRecord);
			for (NfsWdrlRecord nfsWdrlRecord2 : wdrlRecordList) {
				// 提现时间
				Date withdrawTime = nfsWdrlRecord2.getCreateTime();
				// 出借时间
				Date loanTime = loanRecord.getCreateTime();
				if (withdrawTime.after(loanTime)) {
					loaneeWdrlRecord = nfsWdrlRecord2;
					break;
				}
				continue;
			}
			if (loaneeWdrlRecord == null) {
				logger.error("未找到该借条借款人的提现记录，请联系技术人员处理");
				return Message.error("未找到该借条借款人的提现记录，请联系技术人员处理");
			}
		}
		PreservationProcess preservationProcess = PreservationProcess.noUpload;
		
		logger.info("有盾业务保全开始上传借条[{}]", loanRecord.getId());
		try {
			// =================开始上传借款人认证信息==========================
			MemberCard loaneeCard = memberCardService.getCardByMember(loanee);
			loaneeCard.setMember(loanee);
			Long proofChainIdL = 0L;
			String proofChainId = "";
			Long loaneePartnerOrderId = null;
			NfsLoanPreservation loaneePreservation = loanPreservationService.getPreservationByMemberId(loanee.getId());
			if (loaneePreservation == null) {
				proofChainIdL = SnowFlake.getId();
				proofChainId = String.valueOf(proofChainIdL);
				loaneePartnerOrderId = SnowFlake.getId();
				UdPreservationInfo loaneeIdentifyInfo = new UdPreservationInfo();
				loaneeIdentifyInfo.setProofChainId(proofChainId);
				loaneeIdentifyInfo.setPartnerOrderId(loaneePartnerOrderId.toString());
				loaneeIdentifyInfo.setParentOrderId(loaneePartnerOrderId.toString());
				loaneeIdentifyInfo.setNodeType(NodeType.identificationInfo);
				PreservationBuilderData loaneeInfoBuilderData = new PreservationBuilderData();
				loaneeInfoBuilderData.setMember(loanee);
				loaneeInfoBuilderData.setMemberCard(loaneeCard);
				loaneeInfoBuilderData.setMemberVideoVerify(loaneeVideoVerify);
				loaneeIdentifyInfo.setPreservationBuilderData(loaneeInfoBuilderData);
				
				JSONObject loaneeIdentifyResponse = YouDunPreservation.uploadInfo(loaneeIdentifyInfo);
				logger.debug("借款人认证上传返回结果：{}", loaneeIdentifyResponse);
				Boolean loaneeIdentifyResult = loaneeIdentifyResponse.getBoolean("success");
				if (loaneeIdentifyResult) {
					JSONObject loaneeIdentifyData = loaneeIdentifyResponse.getJSONObject("data");
					String loaneeIdentifyPreCode = loaneeIdentifyData.getString("pre_code");
					NfsLoanPreservation loanPreservation = new NfsLoanPreservation();
					loanPreservation.setProofChainId(proofChainIdL);
					loanPreservation.setBusinessId(loanee.getId());
					loanPreservation.setNodeType(NfsLoanPreservation.NodeType.IDENTIFY);
					loanPreservation.setParentOrderId(loaneePartnerOrderId);
					loanPreservation.setPartnerOrderId(loaneePartnerOrderId);
					loanPreservation.setPrecode(loaneeIdentifyPreCode);
					loanPreservationService.save(loanPreservation);
				} else {
					nfsLoanArbitration = nfsLoanArbitrationService.get(nfsLoanArbitration);
					nfsLoanArbitration.setPreservationProcess(preservationProcess);
					nfsLoanArbitrationService.save(nfsLoanArbitration);
					String errorCode = loaneeIdentifyResponse.getString("errorCode");
					String message = loaneeIdentifyResponse.getString("message");
					logger.error("借条{}仲裁上传借款人认证信息订单号：{}，返回结果失败:errorCode{},message:{}", loaneePartnerOrderId,loanRecord.getId(), errorCode,message);
					return Message.error("借款人认证信息上传错误: " + message);
				}
			} else {
				proofChainIdL = loaneePreservation.getProofChainId();
				proofChainId = String.valueOf(proofChainIdL);
				loaneePartnerOrderId = loaneePreservation.getPartnerOrderId();
			}
			preservationProcess = PreservationProcess.loaneeIdentify;
			
			// =================开始上传放款人认证信息==========================
			MemberCard loanerCard = memberCardService.getCardByMember(loaner);
			loanerCard.setMember(loaner);
			NfsLoanPreservation loanerPreservation = loanPreservationService.getPreservationByMemberId(loaner.getId());
			if (loanerPreservation == null) {
				Long loanerPartnerOrderId = SnowFlake.getId();
				UdPreservationInfo loanerIdentifyInfo = new UdPreservationInfo();
				Long loanerProofChainId = SnowFlake.getId();
				loanerIdentifyInfo.setProofChainId(String.valueOf(loanerProofChainId));
				loanerIdentifyInfo.setPartnerOrderId(loanerPartnerOrderId.toString());
				loanerIdentifyInfo.setParentOrderId(loanerPartnerOrderId.toString());
				loanerIdentifyInfo.setNodeType(NodeType.identificationInfo);
				PreservationBuilderData loanerInfoBuilderData = new PreservationBuilderData();
				loanerInfoBuilderData.setMember(loaner);
				loanerInfoBuilderData.setMemberCard(loanerCard);
				loanerInfoBuilderData.setMemberVideoVerify(loanerVideoVerify);
				loanerIdentifyInfo.setPreservationBuilderData(loanerInfoBuilderData);
				
				JSONObject loanerIdentifyResponse = YouDunPreservation.uploadInfo(loanerIdentifyInfo);
				logger.debug("放款人认证上传返回结果：{}", loanerIdentifyResponse);
				Boolean loanerIdentifyResult = loanerIdentifyResponse.getBoolean("success");
				if (loanerIdentifyResult) {
					JSONObject loanerIdentifyData = loanerIdentifyResponse.getJSONObject("data");
					String loanerIdentifyPreCode = loanerIdentifyData.getString("pre_code");
					loanerPreservation = new NfsLoanPreservation();
					loanerPreservation.setProofChainId(loanerProofChainId);
					loanerPreservation.setBusinessId(loaner.getId());
					loanerPreservation.setNodeType(NfsLoanPreservation.NodeType.IDENTIFY);
					loanerPreservation.setParentOrderId(loanerPartnerOrderId);
					loanerPreservation.setPartnerOrderId(loanerPartnerOrderId);
					loanerPreservation.setPrecode(loanerIdentifyPreCode);
					loanPreservationService.save(loanerPreservation);
				} else {
					nfsLoanArbitration = nfsLoanArbitrationService.get(nfsLoanArbitration);
					nfsLoanArbitration.setPreservationProcess(preservationProcess);
					nfsLoanArbitrationService.save(nfsLoanArbitration);
					String errorCode = loanerIdentifyResponse.getString("errorCode");
					String message = loanerIdentifyResponse.getString("message");
					logger.error("借条{}申请有盾仲裁，上传放款人认证订单号：{},信息返回结果失败:errorCode{},message:{}", loanerPartnerOrderId,loanRecord.getId(), errorCode,
							message);
					return Message.error("放款人认证信息上传错误: " + message);
				}
			}
			preservationProcess = PreservationProcess.loanerIdentify;
			
			loanRecord.setLoanee(loanee);
			loanRecord.setLoaner(loaner);

			// =================开始上传合同节点信息==========================
			NfsLoanContract nfsLoanContract = new NfsLoanContract();
			nfsLoanContract.setLoanId(loanRecord.getId());
			List<NfsLoanContract> contractList = loanContractService.getContractByLoanId(nfsLoanContract);
			// 取首个合同
			NfsLoanContract firstLoanContract = contractList.get(contractList.size() - 1);
			Long contractPartnerOrderId = SnowFlake.getId();
			
			UdPreservationInfo contractUploadInfo = new UdPreservationInfo();
			contractUploadInfo.setProofChainId(proofChainId);
			contractUploadInfo.setPartnerOrderId(contractPartnerOrderId.toString());
			contractUploadInfo.setParentOrderId(loaneePartnerOrderId.toString());
			contractUploadInfo.setNodeType(NodeType.contractInfo);
			PreservationBuilderData contractBuilderData = new PreservationBuilderData();
			contractBuilderData.setLoanRecord(loanRecord);
			contractBuilderData.setLoanContract(firstLoanContract);
			contractUploadInfo.setPreservationBuilderData(contractBuilderData);
			
			JSONObject contractResponse = YouDunPreservation.uploadInfo(contractUploadInfo);
			Boolean contractResult = contractResponse.getBoolean("success");
			if (contractResult) {
				JSONObject contractResponseData = contractResponse.getJSONObject("data");
				String contractPreCode = contractResponseData.getString("pre_code");
				NfsLoanPreservation contractPreservation = new NfsLoanPreservation();
				contractPreservation.setProofChainId(proofChainIdL);
				contractPreservation.setBusinessId(loanRecord.getId());
				contractPreservation.setNodeType(NfsLoanPreservation.NodeType.CONTRACT);
				contractPreservation.setParentOrderId(loaneePartnerOrderId);
				contractPreservation.setPartnerOrderId(contractPartnerOrderId);
				contractPreservation.setPrecode(contractPreCode);
				loanPreservationService.save(contractPreservation);
				logger.info("有盾业务保全上传借条[{}]合同信息成功", loanRecord.getId());
			} else {
				nfsLoanArbitration = nfsLoanArbitrationService.get(nfsLoanArbitration);
				nfsLoanArbitration.setPreservationProcess(preservationProcess);
				nfsLoanArbitrationService.save(nfsLoanArbitration);
				String errorCode = contractResponse.getString("errorCode");
				String message = contractResponse.getString("message");
				logger.error("借条{}申请有盾仲裁，上传合同信息订单号：{}，返回结果失败:errorCode{},message:{}", contractPartnerOrderId,loanRecord.getId(), errorCode, message);
				return Message.error("借款合同上传失败");
			}
			preservationProcess = PreservationProcess.contract;
			
			// =================开始上传付款节点信息==========================
			Long loanPartnerOrderId = SnowFlake.getId();
			
			UdPreservationInfo loanInfo = new UdPreservationInfo();
			loanInfo.setProofChainId(proofChainId);
			loanInfo.setPartnerOrderId(loanPartnerOrderId.toString());
			loanInfo.setParentOrderId(contractPartnerOrderId.toString());
			loanInfo.setNodeType(NodeType.loanInfo);
			PreservationBuilderData loanBuilderData = new PreservationBuilderData(); 
			if (loanRecord.getTrxType().equals(TrxType.online)) {
				loanBuilderData.setWdrlRecord(loaneeWdrlRecord);
			} else {
				loanBuilderData.setLoanArbitration(nfsLoanArbitration);
			}
			loanBuilderData.setLoanRecord(loanRecord);
			loanBuilderData.setLoanContract(firstLoanContract);
			loanBuilderData.setMemberCard(loanerCard);
			loanInfo.setPreservationBuilderData(loanBuilderData);
			
			JSONObject loanResponse = YouDunPreservation.uploadInfo(loanInfo);
			Boolean loanResponseResult = loanResponse.getBoolean("success");
			if (loanResponseResult) {
				JSONObject loanResponseData = loanResponse.getJSONObject("data");
				String loanPreCode = loanResponseData.getString("pre_code");
				NfsLoanPreservation loanPreservation = new NfsLoanPreservation();
				loanPreservation.setProofChainId(proofChainIdL);
				loanPreservation.setBusinessId(loanRecord.getId());
				loanPreservation.setNodeType(NfsLoanPreservation.NodeType.PAY);
				loanPreservation.setParentOrderId(contractPartnerOrderId);
				loanPreservation.setPartnerOrderId(loanPartnerOrderId);
				loanPreservation.setPrecode(loanPreCode);
				loanPreservationService.save(loanPreservation);
				logger.info("有盾业务保全上传借条[{}]付款节点信息成功", loanRecord.getId());
			} else {
				nfsLoanArbitration = nfsLoanArbitrationService.get(nfsLoanArbitration);
				nfsLoanArbitration.setPreservationProcess(preservationProcess);
				nfsLoanArbitrationService.save(nfsLoanArbitration);
				String errorCode = loanResponse.getString("errorCode");
				String message = loanResponse.getString("message");
				logger.error("借条{}申请有盾仲裁，上传付款节点信息订单：{}，返回结果失败:errorCode{},message:{}", loanPartnerOrderId,loanRecord.getId(), errorCode, message);
				return Message.error("付款节点信息上传失败！");
			}
			preservationProcess = PreservationProcess.loan;
			
			// =================开始上传还款节点信息===========================
			// 所有的还款操作记录
			List<NfsLoanOperatingRecord> repayOperationRecordList = new ArrayList<NfsLoanOperatingRecord>();
			if (loanRecord.getRepayType().equals(RepayType.oneTimePrincipalAndInterest)) {
				// 全额还款借条
				// 查询部分还款操作记录
				NfsLoanOperatingRecord nfsLoanOperatingRecord = new NfsLoanOperatingRecord();
				NfsLoanRecord oldLoanRecord = new NfsLoanRecord(loanRecord.getId());
				oldLoanRecord.setLoanee(loanee);
				oldLoanRecord.setLoaner(loaner);
				nfsLoanOperatingRecord.setOldRecord(oldLoanRecord);
				nfsLoanOperatingRecord.setType(NfsLoanOperatingRecord.Type.partial);
				List<NfsLoanOperatingRecord> partialRepayList = loanOperatingRecordService
						.findList(nfsLoanOperatingRecord);
				repayOperationRecordList.addAll(partialRepayList);
				//查询部分还款加延期操作记录
				nfsLoanOperatingRecord.setType(NfsLoanOperatingRecord.Type.partialAndDelay);
				List<NfsLoanOperatingRecord> partialAndDelayRepayList = loanOperatingRecordService
						.findList(nfsLoanOperatingRecord);
				repayOperationRecordList.addAll(partialAndDelayRepayList);
			} else {
				//分期还款借条
				NfsLoanOperatingRecord nfsLoanOperatingRecord = new NfsLoanOperatingRecord();
				NfsLoanRecord oldLoanRecord = new NfsLoanRecord(loanRecord.getId());
				oldLoanRecord.setLoanee(loanee);
				oldLoanRecord.setLoaner(loaner);
				nfsLoanOperatingRecord.setOldRecord(oldLoanRecord);
				nfsLoanOperatingRecord.setType(NfsLoanOperatingRecord.Type.principalAndInterestByMonth);
				List<NfsLoanOperatingRecord> stagesRepayList = loanOperatingRecordService
						.findList(nfsLoanOperatingRecord);
				repayOperationRecordList.addAll(stagesRepayList);
			}
			if (!Collections3.isEmpty(repayOperationRecordList)) {
				for (NfsLoanOperatingRecord operatingRecord : repayOperationRecordList) {
					// 根据还款操作记录查询当时的借款合同 取和操作记录同一天的合同
					NfsLoanContract loanContract = null;
					for (NfsLoanContract contract : contractList) {
						String contractCreateTime = DateUtils.getDateStr(contract.getCreateTime(), "yyyyMMdd");
						String repayTime = DateUtils.getDateStr(operatingRecord.getCreateTime(), "yyyyMMdd");
						if (contractCreateTime.compareTo(repayTime) == 0) {
							loanContract = contract;
							break;
						}
						continue;
					}
					Long repayPartnerOrderId = SnowFlake.getId();
					UdPreservationInfo repayInfo = new UdPreservationInfo();
					repayInfo.setProofChainId(proofChainId);
					repayInfo.setPartnerOrderId(repayPartnerOrderId.toString());
					repayInfo.setParentOrderId(contractPartnerOrderId.toString());
					repayInfo.setNodeType(NodeType.repayInfo);
					PreservationBuilderData repayBuilderData = new PreservationBuilderData();
					repayBuilderData.setOperatingRecord(operatingRecord);
					repayBuilderData.setMember(loanee);
					repayBuilderData.setMemberCard(loaneeCard);
					repayBuilderData.setLoanContract(loanContract);
					repayInfo.setPreservationBuilderData(repayBuilderData);
					
					JSONObject repayResponse = YouDunPreservation.uploadInfo(repayInfo);
					logger.debug("还款节点上传返回结果：{}", repayResponse);
					Boolean repayResponseResult = repayResponse.getBoolean("success");
					if (repayResponseResult) {
						JSONObject repayResponseData = repayResponse.getJSONObject("data");
						String repayPreCode = repayResponseData.getString("pre_code");
						NfsLoanPreservation repayPreservation = new NfsLoanPreservation();
						repayPreservation.setProofChainId(proofChainIdL);
						repayPreservation.setBusinessId(loanRecord.getId());
						repayPreservation.setNodeType(NfsLoanPreservation.NodeType.REPAY);
						repayPreservation.setParentOrderId(contractPartnerOrderId);
						repayPreservation.setPartnerOrderId(repayPartnerOrderId);
						repayPreservation.setPrecode(repayPreCode);
						loanPreservationService.save(repayPreservation);
						logger.info("有盾业务保全上传借条[{}]还款节点信息成功", loanRecord.getId());
					} else {
						nfsLoanArbitration = nfsLoanArbitrationService.get(nfsLoanArbitration);
						nfsLoanArbitration.setPreservationProcess(preservationProcess);
						nfsLoanArbitrationService.save(nfsLoanArbitration);
						String errorCode = repayResponse.getString("errorCode");
						String message = repayResponse.getString("message");
						logger.error("借条{}申请有盾仲裁，上传还款节点信息订单号：{}，返回结果失败:errorCode{},message:{}",repayPartnerOrderId,loanRecord.getId(), errorCode,
								message);
						return Message.error("还款节点信息上传失败！");
					}
				}
			} else {
				logger.info("借条：[{}]没有还款操作记录，无需上传还款节点信息", loanRecord.getId());
			}
			preservationProcess = PreservationProcess.repay;
			
			// =================开始上传延期合同节点信息===========================
			List<NfsLoanOperatingRecord> delayOperationRecordList = new ArrayList<NfsLoanOperatingRecord>();
			// 查询所有的延期操作
			NfsLoanOperatingRecord nfsLoanOperatingRecord = new NfsLoanOperatingRecord();
			NfsLoanRecord oldLoanRecord = new NfsLoanRecord(loanRecord.getId());
			oldLoanRecord.setLoanee(loanee);
			oldLoanRecord.setLoaner(loaner);
			nfsLoanOperatingRecord.setOldRecord(oldLoanRecord);
			nfsLoanOperatingRecord.setType(NfsLoanOperatingRecord.Type.delay);
			List<NfsLoanOperatingRecord> delayList = loanOperatingRecordService.findList(nfsLoanOperatingRecord);
			delayOperationRecordList.addAll(delayList);
			// 查询所有的部分还款延期操作
			nfsLoanOperatingRecord.setType(NfsLoanOperatingRecord.Type.partialAndDelay);
			List<NfsLoanOperatingRecord> partialAndDelayList = loanOperatingRecordService
					.findList(nfsLoanOperatingRecord);
			delayOperationRecordList.addAll(partialAndDelayList);
			if (!Collections3.isEmpty(delayOperationRecordList)) {
				// 遍历延期操作，分别上传延期合同
				for (NfsLoanOperatingRecord delayOperationRecord : delayOperationRecordList) {
					NfsLoanContract loanContract = null;
					String lastContractCode = "";
					for (int i = 0; i < contractList.size() - 1; i++) {
						// 最后一条合同不是续签合同
						String delayCreateTime = DateUtils.getDateStr(delayOperationRecord.getCreateTime(), "yyyyMMdd");
						String nowContractTime = DateUtils.getDateStr(contractList.get(i).getCreateTime(), "yyyyMMdd");
						if (delayCreateTime.compareTo(nowContractTime) == 0) {
							loanContract = contractList.get(i);
							lastContractCode = contractList.get(i + 1).getId().toString();
							break;
						}
						continue;
					}
					if (loanContract == null || StringUtils.isBlank(lastContractCode)) {
						logger.error("借条[{}]延期操作没有生成新的延期合同！", loanRecord.getId());
						return Message.error("延期合同缺失，请联系技术人员处理！");
					}
					if(StringUtils.isBlank(loanContract.getContractUrl()) || !loanContract.getStatus().equals(NfsLoanContract.Status.signatured)) {
						return Message.error("借条延期合同错误，请联系技术人员处理后再上传！");
					}
					loanRecord.setDueRepayDate(delayOperationRecord.getNowRecord().getDueRepayDate());
					Long renewalPartnerOrderId = SnowFlake.getId();
					UdPreservationInfo renewalInfo = new UdPreservationInfo();
					renewalInfo.setProofChainId(proofChainId);
					renewalInfo.setPartnerOrderId(renewalPartnerOrderId.toString());
					renewalInfo.setParentOrderId(contractPartnerOrderId.toString());
					renewalInfo.setNodeType(NodeType.renewalInfo);
					PreservationBuilderData renewalBuilderData = new PreservationBuilderData();
					renewalBuilderData.setLoanContract(loanContract);
					renewalBuilderData.setLoanRecord(loanRecord);
					renewalBuilderData.setOperatingRecord(delayOperationRecord);
					renewalBuilderData.setLastContractCode(lastContractCode);
					renewalInfo.setPreservationBuilderData(renewalBuilderData);
					
					JSONObject renewalResponse = YouDunPreservation.uploadInfo(renewalInfo);
					logger.debug("延期合同节点上传返回结果：{}", renewalResponse);
					Boolean renewalResponseResult = renewalResponse.getBoolean("success");
					if (renewalResponseResult) {
						JSONObject renewalResponseData = renewalResponse.getJSONObject("data");
						String renewalPreCode = renewalResponseData.getString("pre_code");
						NfsLoanPreservation renewalPreservation = new NfsLoanPreservation();
						renewalPreservation.setProofChainId(proofChainIdL);
						renewalPreservation.setBusinessId(loanRecord.getId());
						renewalPreservation.setNodeType(NfsLoanPreservation.NodeType.RENEWAL);
						renewalPreservation.setParentOrderId(contractPartnerOrderId);
						renewalPreservation.setPartnerOrderId(renewalPartnerOrderId);
						renewalPreservation.setPrecode(renewalPreCode);
						loanPreservationService.save(renewalPreservation);
						logger.info("有盾业务保全上传借条[{}]延期合同节点信息成功", loanRecord.getId());
					} else {
						nfsLoanArbitration = nfsLoanArbitrationService.get(nfsLoanArbitration);
						nfsLoanArbitration.setPreservationProcess(preservationProcess);
						nfsLoanArbitrationService.save(nfsLoanArbitration);
						String errorCode = renewalResponse.getString("errorCode");
						String message = renewalResponse.getString("message");
						logger.error("借条{}申请有盾仲裁，上传延期合同信息返回结果失败:errorCode{},message:{}", loanRecord.getId(), errorCode,message);
						return Message.error("续签节点信息上传失败！");
					}
				}
			} else {
				logger.info("借条[{}]没有延期操作，不用上传延期合同信息", loanRecord.getId());
			}
			preservationProcess = PreservationProcess.success;
			nfsLoanArbitration = nfsLoanArbitrationService.get(nfsLoanArbitration);
			nfsLoanArbitration.setPreservationProcess(preservationProcess);
			nfsLoanArbitrationService.save(nfsLoanArbitration);
		} catch (Exception e) {
			nfsLoanArbitrationService.save(nfsLoanArbitration);
			String name = PreservationProcess.values()[preservationProcess.ordinal()+1].name();
			logger.error("借条【{}】上传案件[{}]异常：{}", loanRecord.getId(), name, Exceptions.getStackTraceAsString(e));
			return Message.error("案件上传[{}]出错，请联系技术人员处理！", name);
		}
		return Message.success("上传仲裁案件成功！");
	}
	
}