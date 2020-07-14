package com.jxf.web.admin.loan;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanCollection;
import com.jxf.loan.entity.NfsLoanCollection.Status;
import com.jxf.loan.entity.NfsLoanCollectionDetail;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRecord.CollectionStatus;
import com.jxf.loan.service.NfsLoanCollectionDetailService;
import com.jxf.loan.service.NfsLoanCollectionService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.MemUtils;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.service.NfsActService;
import com.jxf.svc.config.Global;
import com.jxf.svc.excel.ExportExcel;
import com.jxf.svc.model.Message;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.admin.sys.BaseController;


/**
 * 	借条催收Controller
 * @author gaobo
 * @version 2018-11-07
 */
@Controller("adminLoanCollectionController")
@RequestMapping(value = "${adminPath}/nfsLoanCollection")
public class NfsLoanCollectionController extends BaseController {

	@Autowired
	private NfsLoanRecordService loanRecordService;
	
	@Autowired
	private NfsLoanCollectionService loanCollectionService;
	
	@Autowired
	private NfsLoanCollectionDetailService loanCollectionDetailService;
	
	@Autowired
	private NfsActService nfsActService;
	
	@Autowired
	private MemberService memberService;
	@ModelAttribute
	public NfsLoanCollection get(@RequestParam(required=false) Long id) {
		NfsLoanCollection entity = null;
		if (id!=null){
			entity = loanCollectionService.get(id);
		}
		if (entity == null){
			entity = new NfsLoanCollection();
		}
		return entity;
	}
	
	@RequiresPermissions("loan:nfsLoanCollection:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsLoanCollection nfsLoanCollection, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(nfsLoanCollection.getLoan()!=null) {
			Member loaneeMember = nfsLoanCollection.getLoan().getLoanee();
			Member loanerMember = nfsLoanCollection.getLoan().getLoaner();
			if(loaneeMember != null && StringUtils.isNotBlank(loaneeMember.getUsername())){
				loaneeMember = memberService.findByUsername(loaneeMember.getUsername());
				if(loaneeMember != null) {
					Long loaneeId = loaneeMember.getId();
					loaneeMember = new Member();
					loaneeMember.setId(loaneeId);
					nfsLoanCollection.getLoan().setLoanee(loaneeMember);
				}
			}
			if(loanerMember != null && StringUtils.isNotBlank(loanerMember.getUsername())){
				loanerMember = memberService.findByUsername(loanerMember.getUsername());
				if(loanerMember != null) {
					Long loanerId = loanerMember.getId();
					loanerMember = new Member();
					loanerMember.setId(loanerId);
					nfsLoanCollection.getLoan().setLoaner(loanerMember);
				}
			}
		}
		Page<NfsLoanCollection> page = loanCollectionService.findPage(new Page<NfsLoanCollection>(request, response),nfsLoanCollection); 
		List<NfsLoanCollection> list = page.getList();
		for (NfsLoanCollection nfsLoanCollection2 : list) {
			NfsLoanRecord loan = nfsLoanCollection2.getLoan();
			Member loanee = loan.getLoanee();
			loan.setLoanee(MemUtils.mask(loanee));
			Member loaner = loan.getLoaner();
			loan.setLoaner(MemUtils.mask(loaner));
			nfsLoanCollection2.setLoan(loan);
		}
		page.setList(list);
		model.addAttribute("page", page);
		return "admin/loan/collection/nfsLoanCollectionList";
	}
	/**
	 * 	修改页面跳转
	 */
	@RequiresPermissions("loan:nfsLoanCollection:view")
	@RequestMapping(value = "update")
	public String update(NfsLoanCollection nfsLoanCollection, Model model) {
		model.addAttribute("nfsLoanCollection", nfsLoanCollection);
		return "admin/loan/collection/nfsLoanCollectionUpdate";
	}
	
	/**
	 * 后台修改
	 */
	@RequiresPermissions("loan:nfsLoanCollection:edit")
	@RequestMapping(value = "editCollection")
	@ResponseBody
	public Message editCollection(NfsLoanCollection nfsLoanCollection, Model model, RedirectAttributes redirectAttributes) {
		try {
			NfsLoanRecord loan = loanRecordService.get(nfsLoanCollection.getLoan().getId());
			if(nfsLoanCollection.getStatus().equals(Status.fail)||nfsLoanCollection.getStatus().equals(Status.refuse)) {
				BigDecimal fee = nfsLoanCollection.getFee();
				nfsActService.updateAct(TrxRuleConstant.COLLECTION_FAILURE_REFUND, fee, nfsLoanCollection.getLoan().getLoaner(), nfsLoanCollection.getId());
			}
			//已受理log
			if(nfsLoanCollection.getStatus().equals(Status.accepted)) {
				NfsLoanCollectionDetail nfsLoanCollectionDetail = new NfsLoanCollectionDetail();
				nfsLoanCollectionDetail.setCollectionId(nfsLoanCollection.getId());
				nfsLoanCollectionDetail.setStatus(com.jxf.loan.entity.NfsLoanCollectionDetail.Status.accepted);
				nfsLoanCollectionDetail.setRmk(nfsLoanCollection.getRmk());
				if(NfsLoanApply.RepayType.oneTimePrincipalAndInterest.equals(loan.getRepayType())) {
					nfsLoanCollectionDetail.setType(NfsLoanCollectionDetail.Type.fullAmount);
				}else {
					nfsLoanCollectionDetail.setType(NfsLoanCollectionDetail.Type.staging);
				}
				loanCollectionDetailService.save(nfsLoanCollectionDetail);
			}
			//审核中log
			if(nfsLoanCollection.getStatus().equals(Status.auditing)) {
				NfsLoanCollectionDetail nfsLoanCollectionDetail = new NfsLoanCollectionDetail();
				nfsLoanCollectionDetail.setCollectionId(nfsLoanCollection.getId());
				nfsLoanCollectionDetail.setStatus(com.jxf.loan.entity.NfsLoanCollectionDetail.Status.underReview);
				nfsLoanCollectionDetail.setRmk(nfsLoanCollection.getRmk());
				if(NfsLoanApply.RepayType.oneTimePrincipalAndInterest.equals(loan.getRepayType())) {
					nfsLoanCollectionDetail.setType(NfsLoanCollectionDetail.Type.fullAmount);
				}else {
					nfsLoanCollectionDetail.setType(NfsLoanCollectionDetail.Type.staging);
				}
				loanCollectionDetailService.save(nfsLoanCollectionDetail);
			}
			//催收中log
			if(nfsLoanCollection.getStatus().equals(Status.collection)) {
				NfsLoanCollectionDetail nfsLoanCollectionDetail = new NfsLoanCollectionDetail();
				nfsLoanCollectionDetail.setCollectionId(nfsLoanCollection.getId());
				nfsLoanCollectionDetail.setStatus(com.jxf.loan.entity.NfsLoanCollectionDetail.Status.collection);
				nfsLoanCollectionDetail.setRmk(nfsLoanCollection.getRmk());
				if(NfsLoanApply.RepayType.oneTimePrincipalAndInterest.equals(loan.getRepayType())) {
					nfsLoanCollectionDetail.setType(NfsLoanCollectionDetail.Type.fullAmount);
				}else {
					nfsLoanCollectionDetail.setType(NfsLoanCollectionDetail.Type.staging);
				}
				loanCollectionDetailService.save(nfsLoanCollectionDetail);
			}
			//催收失败log
			if(nfsLoanCollection.getStatus().equals(Status.fail)) {
				NfsLoanCollectionDetail nfsLoanCollectionDetail = new NfsLoanCollectionDetail();
				nfsLoanCollectionDetail.setCollectionId(nfsLoanCollection.getId());
				nfsLoanCollectionDetail.setStatus(com.jxf.loan.entity.NfsLoanCollectionDetail.Status.collectionFailure);
				nfsLoanCollectionDetail.setRmk(nfsLoanCollection.getRmk());
				if(NfsLoanApply.RepayType.oneTimePrincipalAndInterest.equals(loan.getRepayType())) {
					nfsLoanCollectionDetail.setType(NfsLoanCollectionDetail.Type.fullAmount);
				}else {
					nfsLoanCollectionDetail.setType(NfsLoanCollectionDetail.Type.staging);
				}
				loanCollectionDetailService.save(nfsLoanCollectionDetail);
				loan.setCollectionStatus(CollectionStatus.end);
				loanRecordService.save(loan);			
			}
			//拒绝受理log
			if(nfsLoanCollection.getStatus().equals(Status.refuse)) {
				NfsLoanCollectionDetail nfsLoanCollectionDetail = new NfsLoanCollectionDetail();
				nfsLoanCollectionDetail.setCollectionId(nfsLoanCollection.getId());
				nfsLoanCollectionDetail.setStatus(com.jxf.loan.entity.NfsLoanCollectionDetail.Status.refuseToAccept);
				nfsLoanCollectionDetail.setRmk(nfsLoanCollection.getRmk());
				if(NfsLoanApply.RepayType.oneTimePrincipalAndInterest.equals(loan.getRepayType())) {
					nfsLoanCollectionDetail.setType(NfsLoanCollectionDetail.Type.fullAmount);
				}else {
					nfsLoanCollectionDetail.setType(NfsLoanCollectionDetail.Type.staging);
				}
				loanCollectionDetailService.save(nfsLoanCollectionDetail);
				loan.setCollectionStatus(CollectionStatus.end);
				loanRecordService.save(loan);
			}
			//催收成功log
			if(nfsLoanCollection.getStatus().equals(Status.success)) {
				NfsLoanCollectionDetail nfsLoanCollectionDetail = new NfsLoanCollectionDetail();
				nfsLoanCollectionDetail.setCollectionId(nfsLoanCollection.getId());
				nfsLoanCollectionDetail.setStatus(com.jxf.loan.entity.NfsLoanCollectionDetail.Status.successfulCollection);
				nfsLoanCollectionDetail.setRmk(nfsLoanCollection.getRmk());
				if(NfsLoanApply.RepayType.oneTimePrincipalAndInterest.equals(loan.getRepayType())) {
					nfsLoanCollectionDetail.setType(NfsLoanCollectionDetail.Type.fullAmount);
				}else {
					nfsLoanCollectionDetail.setType(NfsLoanCollectionDetail.Type.staging);
				}
				loanCollectionDetailService.save(nfsLoanCollectionDetail);
				loan.setCollectionStatus(CollectionStatus.end);
				loanRecordService.save(loan);
			}
			loanCollectionService.save(nfsLoanCollection);
			return Message.success("修改成功");
		}catch(Exception e) {
			return Message.error("修改失败");
		}
	}
	 
	/**
	 * 催收详情
	 */
	@RequiresPermissions("loan:nfsLoanCollection:view")
	@RequestMapping(value = "collectionDetailList")
	public String collectionDetailList(Long id, HttpServletRequest request, HttpServletResponse response, Model model) {
		NfsLoanCollectionDetail nfsLoanCollectionDetail = new NfsLoanCollectionDetail();
		nfsLoanCollectionDetail.setCollectionId(id);
		Page<NfsLoanCollectionDetail> page =  loanCollectionDetailService.findPage(new Page<NfsLoanCollectionDetail>(request, response), nfsLoanCollectionDetail);
		model.addAttribute("page", page);
		return "admin/loan/collection/nfsLoanCollectionDetailList";
	}
	
	/**
	 * 导出催收数据
	 * @param nfsLoanCollection
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("loan:nfsLoanCollection:edit")
    @RequestMapping(value = "exportData", method=RequestMethod.POST)
    public String exportData(NfsLoanCollection nfsLoanCollection, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            if(nfsLoanCollection.getBeginTime()==null||nfsLoanCollection.getEndTime()==null) {
				addMessage(redirectAttributes, "起始时间和结束时间不能为空");
				return "redirect:"+Global.getAdminPath()+"/nfsLoanCollection?repage";
            }
			int days = DateUtils.getDifferenceOfTwoDate(nfsLoanCollection.getBeginTime(),nfsLoanCollection.getEndTime());
			if(Math.abs(days)>3){
				addMessage(redirectAttributes, "导出数据的时间跨度不能超过3天");
				return "redirect:"+Global.getAdminPath()+"/nfsLoanCollection?repage";
			}
            List<NfsLoanCollection> collectionList = loanCollectionService.findList(nfsLoanCollection);
            
            String fileName = "催收数据"+DateUtils.getDate("yyyy-MM-dd")+".xlsx";
            ExportExcel exprotExcel = new ExportExcel("催收数据", NfsLoanCollection.class);
            exprotExcel.setDataList(collectionList);
            exprotExcel.write(response, fileName);
            exprotExcel.dispose();
            return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出催收数据失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/nfsLoanCollection?repage";
    }
}