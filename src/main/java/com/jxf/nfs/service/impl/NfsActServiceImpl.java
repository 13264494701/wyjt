package com.jxf.nfs.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.loan.entity.NfsCrAuction;
import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanApply.LoanRole;
import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.entity.NfsLoanApplyDetail.AliveVideoStatus;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsCrAuctionService;
import com.jxf.loan.service.NfsLoanApplyDetailService;
import com.jxf.loan.service.NfsLoanApplyService;
import com.jxf.loan.service.NfsLoanRecordService;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberAct;
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.mem.service.MemberActService;
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.mem.service.MemberService;
import com.jxf.nfs.entity.NfsActSub.TrxRole;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.entity.NfsBrnAct;
import com.jxf.nfs.entity.NfsBrnActTrx;
import com.jxf.nfs.entity.NfsTrxRuleItem;
import com.jxf.nfs.service.NfsActService;
import com.jxf.nfs.service.NfsBrnActService;
import com.jxf.nfs.service.NfsBrnActTrxService;
import com.jxf.nfs.service.NfsTrxRuleItemService;
import com.jxf.svc.config.Constant;
import com.jxf.svc.sys.brn.entity.Brn;
import com.jxf.svc.sys.brn.service.BrnService;
import com.jxf.svc.utils.StringUtils;
import com.jxf.ufang.entity.UfangBrn;
import com.jxf.ufang.entity.UfangBrnAct;
import com.jxf.ufang.entity.UfangBrnActTrx;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.entity.UfangUserAct;
import com.jxf.ufang.entity.UfangUserActTrx;
import com.jxf.ufang.service.UfangBrnActService;
import com.jxf.ufang.service.UfangBrnActTrxService;
import com.jxf.ufang.service.UfangUserActService;
import com.jxf.ufang.service.UfangUserActTrxService;


/**
 * 账户ServiceImpl
 * @author jinxinfu
 * @version 2018-06-29
 */
@Service("nfsActService")
@Transactional(readOnly = true)
public class NfsActServiceImpl  implements NfsActService{

	@Autowired
	private NfsTrxRuleItemService trxRuleItemService;
	@Autowired
	private MemberActService memberActService;
	@Autowired
	private MemberActTrxService memberActTrxService;
	@Autowired
	private UfangBrnActService ufangBrnActService;
	@Autowired
	private UfangBrnActTrxService ufangBrnActTrxService;
	@Autowired
	private UfangUserActService ufangUserActService;
	@Autowired
	private UfangUserActTrxService ufangUserActTrxService;
	@Autowired
	private NfsLoanApplyDetailService detailService;
	@Autowired
	private NfsLoanApplyService loanApplyService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private BrnService brnService;
	@Autowired
	private NfsBrnActService nfsBrnActService;
	@Autowired
	private NfsBrnActTrxService nfsBrnActTrxService;
	@Autowired
	private NfsCrAuctionService crAuctionService;
	
	@Override
	@Transactional(readOnly = false)
	public int updateAct(String trxCode, BigDecimal amount, Member member,Long orgId) {
		NfsTrxRuleItem ruleItem = new NfsTrxRuleItem();
		ruleItem.setTrxCode(trxCode);
		List<NfsTrxRuleItem> ruleList = trxRuleItemService.findList(ruleItem);	
		Collections.sort(ruleList);//规则排序，先减后增
		for (NfsTrxRuleItem rule : ruleList) {
			if(rule.getTrxRole().equals(TrxRole.member)) {		
				MemberAct memberAct = memberActService.getMemberAct(member,rule.getSubNo());
				BigDecimal trxAmt = rule.getTrxAmt(amount);
				int result = memberActService.updateAct(memberAct,trxAmt);
							
				if(result == Constant.UPDATE_FAILED) {
					return Constant.UPDATE_FAILED;
				}
				MemberActTrx memberActTrx = new MemberActTrx();
				memberActTrx = getTitleAndRmk(memberActTrx, member, orgId, rule, amount);
				memberActTrx.setTrxCode(trxCode);
				memberActTrx.setMember(member);
				memberActTrx.setSubNo(rule.getSubNo());
				memberActTrx.setDrc(rule.getDrc());
				memberActTrx.setTrxAmt(amount);		
				memberActTrx.setCurBal(memberAct.getCurBal());
				memberActTrx.setCurrCode("CNY");
				memberActTrx.setOrgId(orgId);			
				memberActTrx.setStatus(MemberActTrx.Status.SUCC);
				memberActTrx.setTrxGroup(rule.getTrxGroup());
				memberActTrxService.save(memberActTrx);

			}else if(rule.getTrxRole().equals(TrxRole.inner)) {
				Brn brn = brnService.get(1L);
				NfsBrnAct nfsBrnAct = nfsBrnActService.getBrnAct(brn,rule.getSubNo());
				BigDecimal trxAmt = rule.getTrxAmt(amount);
				nfsBrnActService.updateBrnActBal(nfsBrnAct, trxAmt);
				
				NfsBrnActTrx nfsBrnActTrx = new NfsBrnActTrx();
				nfsBrnActTrx.setTrxCode(trxCode);
				nfsBrnActTrx.setCompany(brn);
				nfsBrnActTrx.setSubNo(rule.getSubNo());
				nfsBrnActTrx.setDrc(rule.getDrc());
				nfsBrnActTrx.setTrxAmt(amount.abs());		
				nfsBrnActTrx.setCurBal(nfsBrnAct.getCurBal());
				nfsBrnActTrx.setCurrCode("CNY");
				nfsBrnActTrx.setOrgId(orgId);			
				nfsBrnActTrx.setStatus(NfsBrnActTrx.Status.SUCC);
				nfsBrnActTrxService.save(nfsBrnActTrx);
			}

		}
		return Constant.UPDATE_SUCCESS;
	}

	@Override
	@Transactional(readOnly = false)
	public int updateAct(String trxCode, BigDecimal amount, Member payer, Member payee,Long orgId) {

		NfsTrxRuleItem ruleItem = new NfsTrxRuleItem();
		ruleItem.setTrxCode(trxCode);
		List<NfsTrxRuleItem> ruleList = trxRuleItemService.findList(ruleItem);	
		Collections.sort(ruleList);//规则排序，先减后增
		for (NfsTrxRuleItem rule : ruleList) {
			if(rule.getTrxRole().equals(TrxRole.payer)) {							
				MemberAct loanerAct = memberActService.getMemberAct(payer,rule.getSubNo());
				
				BigDecimal trxAmt = rule.getTrxAmt(amount);
				int result = memberActService.updateAct(loanerAct,trxAmt);
				
				if(result == Constant.UPDATE_FAILED) {
					return Constant.UPDATE_FAILED;
				}
				MemberActTrx memberActTrx = new MemberActTrx();
				memberActTrx = getTitleAndRmk(memberActTrx, payer, payee, orgId, rule, amount);
				memberActTrx.setTrxCode(trxCode);
				memberActTrx.setMember(payer);
				memberActTrx.setSubNo(rule.getSubNo());
				memberActTrx.setDrc(rule.getDrc());
				memberActTrx.setTrxAmt(amount.abs());		
				memberActTrx.setCurBal(loanerAct.getCurBal());
				memberActTrx.setCurrCode("CNY");
				memberActTrx.setOrgId(orgId);			
				memberActTrx.setStatus(MemberActTrx.Status.SUCC);
				memberActTrx.setTrxGroup(rule.getTrxGroup());
				memberActTrxService.save(memberActTrx);

			}else if(rule.getTrxRole().equals(TrxRole.payee)) {
				MemberAct loaneeAct = memberActService.getMemberAct(payee,rule.getSubNo());
				
				BigDecimal trxAmt = rule.getTrxAmt(amount);
				memberActService.updateAct(loaneeAct,trxAmt);
				
				MemberActTrx memberActTrx = new MemberActTrx();	
				memberActTrx = getTitleAndRmk(memberActTrx, payer, payee, orgId, rule, amount);
				memberActTrx.setTrxCode(trxCode);
				memberActTrx.setMember(payee);
				memberActTrx.setSubNo(rule.getSubNo());
				memberActTrx.setDrc(rule.getDrc());
				memberActTrx.setTrxAmt(amount.abs());		
				memberActTrx.setCurBal(loaneeAct.getCurBal());
				memberActTrx.setCurrCode("CNY");
				memberActTrx.setOrgId(orgId);			
				memberActTrx.setStatus(MemberActTrx.Status.SUCC);
				memberActTrx.setTrxGroup(rule.getTrxGroup());
				memberActTrxService.save(memberActTrx);		
				
			}else if(rule.getTrxRole().equals(TrxRole.inner)) {
				Brn brn = brnService.get(1L);
				NfsBrnAct nfsBrnAct = nfsBrnActService.getBrnAct(brn,rule.getSubNo());
				BigDecimal trxAmt = rule.getTrxAmt(amount);
				nfsBrnActService.updateBrnActBal(nfsBrnAct, trxAmt);
				
				NfsBrnActTrx nfsBrnActTrx = new NfsBrnActTrx();	
				nfsBrnActTrx.setTrxCode(trxCode);
				nfsBrnActTrx.setCompany(brn);
				nfsBrnActTrx.setSubNo(rule.getSubNo());
				nfsBrnActTrx.setDrc(rule.getDrc());
				nfsBrnActTrx.setTrxAmt(amount.abs());		
				nfsBrnActTrx.setCurBal(nfsBrnAct.getCurBal());
				nfsBrnActTrx.setCurrCode("CNY");
				nfsBrnActTrx.setOrgId(orgId);			
				nfsBrnActTrx.setStatus(NfsBrnActTrx.Status.SUCC);
				nfsBrnActTrxService.save(nfsBrnActTrx);
			}

		}
		return Constant.UPDATE_SUCCESS;
	}

	@Override
	@Transactional(readOnly = false)
	public int updateAct(String trxCode, BigDecimal amount, UfangBrn ufangBrn,Long orgId) {
		
		NfsTrxRuleItem ruleItem = new NfsTrxRuleItem();
		ruleItem.setTrxCode(trxCode);
		List<NfsTrxRuleItem> ruleList = trxRuleItemService.findList(ruleItem);	
		Collections.sort(ruleList);//规则排序，先减后增
		for (NfsTrxRuleItem rule : ruleList) {
			
			if(rule.getTrxRole().equals(TrxRole.ufangBrn)){//优放机构
				UfangBrnAct ufangBrnAct = ufangBrnActService.getBrnAct(ufangBrn,rule.getSubNo());
				BigDecimal trxAmt = rule.getTrxAmt(amount);
				ufangBrnActService.updateUfangBrnAct(ufangBrnAct,trxAmt);
				
				UfangBrnActTrx ufangBrnActTrx = new UfangBrnActTrx();	
				ufangBrnActTrx.setTrxCode(trxCode);
				ufangBrnActTrx.setCompany(ufangBrn);
				ufangBrnActTrx.setSubNo(rule.getSubNo());
				ufangBrnActTrx.setDrc(rule.getDrc());
				ufangBrnActTrx.setTrxAmt(amount.abs());		
				ufangBrnActTrx.setCurBal(ufangBrnAct.getCurBal());
				ufangBrnActTrx.setCurrCode("CNY");
				ufangBrnActTrx.setOrgId(orgId);			
				ufangBrnActTrx.setStatus(UfangBrnActTrx.Status.SUCC);
				ufangBrnActTrxService.save(ufangBrnActTrx);
			}else if(rule.getTrxRole().equals(TrxRole.inner)) {
				Brn brn = brnService.get(1L);
				NfsBrnAct nfsBrnAct = nfsBrnActService.getBrnAct(brn,rule.getSubNo());
				BigDecimal trxAmt = rule.getTrxAmt(amount);
				nfsBrnActService.updateBrnActBal(nfsBrnAct, trxAmt);
				
				NfsBrnActTrx nfsBrnActTrx = new NfsBrnActTrx();	
				nfsBrnActTrx.setTrxCode(trxCode);
				nfsBrnActTrx.setCompany(brn);
				nfsBrnActTrx.setSubNo(rule.getSubNo());
				nfsBrnActTrx.setDrc(rule.getDrc());
				nfsBrnActTrx.setTrxAmt(amount.abs());		
				nfsBrnActTrx.setCurBal(nfsBrnAct.getCurBal());
				nfsBrnActTrx.setCurrCode("CNY");
				nfsBrnActTrx.setOrgId(orgId);			
				nfsBrnActTrx.setStatus(NfsBrnActTrx.Status.SUCC);
				nfsBrnActTrxService.save(nfsBrnActTrx);
			}

		}		
		return 0;
	}

	@Override
	@Transactional(readOnly = false)
	public int updateAct(String trxCode, BigDecimal amount, UfangUser ufangUser,Long orgId) {
		NfsTrxRuleItem ruleItem = new NfsTrxRuleItem();
		ruleItem.setTrxCode(trxCode);
		List<NfsTrxRuleItem> ruleList = trxRuleItemService.findList(ruleItem);	
		Collections.sort(ruleList);//规则排序，先减后增
		for (NfsTrxRuleItem rule : ruleList) {
			
			if(rule.getTrxRole().equals(TrxRole.ufangUser)) {		
				
				UfangUserAct ufangUserAct = ufangUserActService.getUserAct(ufangUser,rule.getSubNo());
				BigDecimal trxAmt = rule.getTrxAmt(amount);
				ufangUserActService.updateUfangUserAct(ufangUserAct, trxAmt);
				
				UfangUserActTrx ufangUserActTrx = new UfangUserActTrx();	
				ufangUserActTrx.setTrxCode(trxCode);
				ufangUserActTrx.setUser(ufangUser);
				ufangUserActTrx.setSubNo(rule.getSubNo());
				ufangUserActTrx.setDrc(rule.getDrc());
				ufangUserActTrx.setTrxAmt(amount.abs());		
				ufangUserActTrx.setCurBal(ufangUserAct.getCurBal());
				ufangUserActTrx.setCurrCode("CNY");
				ufangUserActTrx.setOrgId(orgId);			
				ufangUserActTrx.setStatus(UfangUserActTrx.Status.SUCC);
				ufangUserActTrxService.save(ufangUserActTrx);
			}

		}
		return 0;
	}
	
	@Override
	@Transactional(readOnly = false)
	public int updateAct(String trxCode, BigDecimal amount, UfangBrn ufangBrn, UfangUser ufangUser,Long orgId) {
		
		NfsTrxRuleItem ruleItem = new NfsTrxRuleItem();
		ruleItem.setTrxCode(trxCode);
		List<NfsTrxRuleItem> ruleList = trxRuleItemService.findList(ruleItem);	
		Collections.sort(ruleList);//规则排序，先减后增
		for (NfsTrxRuleItem rule : ruleList) {
			
			if(rule.getTrxRole().equals(TrxRole.ufangBrn)){//优放机构
				UfangBrnAct ufangBrnAct = ufangBrnActService.getBrnAct(ufangBrn,rule.getSubNo());
				BigDecimal trxAmt = rule.getTrxAmt(amount);
				ufangBrnActService.updateUfangBrnAct(ufangBrnAct,trxAmt);
				
				UfangBrnActTrx ufangBrnActTrx = new UfangBrnActTrx();	
				ufangBrnActTrx.setTrxCode(trxCode);
				ufangBrnActTrx.setCompany(ufangBrn);
				ufangBrnActTrx.setSubNo(rule.getSubNo());
				ufangBrnActTrx.setDrc(rule.getDrc());
				ufangBrnActTrx.setTrxAmt(amount.abs());		
				ufangBrnActTrx.setCurBal(ufangBrnAct.getCurBal());
				ufangBrnActTrx.setCurrCode("CNY");
				ufangBrnActTrx.setOrgId(orgId);			
				ufangBrnActTrx.setStatus(UfangBrnActTrx.Status.SUCC);
				ufangBrnActTrxService.save(ufangBrnActTrx);
			}else if(rule.getTrxRole().equals(TrxRole.ufangUser)) {
				
				UfangUserAct ufangUserAct = ufangUserActService.getUserAct(ufangUser,rule.getSubNo());
				BigDecimal trxAmt = rule.getTrxAmt(amount);
				ufangUserActService.updateUfangUserAct(ufangUserAct, trxAmt);
				
				UfangUserActTrx ufangUserActTrx = new UfangUserActTrx();	
				ufangUserActTrx.setTrxCode(trxCode);
				ufangUserActTrx.setUser(ufangUser);
				ufangUserActTrx.setSubNo(rule.getSubNo());
				ufangUserActTrx.setDrc(rule.getDrc());
				ufangUserActTrx.setTrxAmt(amount.abs());		
				ufangUserActTrx.setCurBal(ufangUserAct.getCurBal());
				ufangUserActTrx.setCurrCode("CNY");
				ufangUserActTrx.setOrgId(orgId);			
				ufangUserActTrx.setStatus(UfangUserActTrx.Status.SUCC);
				ufangUserActTrxService.save(ufangUserActTrx);
			}

		}
		
		return 0;
	}

	private MemberActTrx getTitleAndRmk(MemberActTrx memberActTrx,Member member,Long orgId,NfsTrxRuleItem rule,BigDecimal amount) {
		Member loanee = null;
		Member loaner = null;
		NfsLoanApplyDetail detail = detailService.get(orgId);
		if(detail != null) {
			NfsLoanApply apply = loanApplyService.get(detail.getApply());
			if(apply.getLoanRole().equals(LoanRole.loanee)) {
				loanee = memberService.get(apply.getMember());
				loaner = memberService.get(detail.getMember());
			}else {
				loanee = memberService.get(detail.getMember());
				loaner = memberService.get(apply.getMember());
			}
			if(rule.getTitle().contains("loanee")){
				rule.setTitle(rule.getTitle().replace("${loanee}", loanee.getName()));
			}
			if(rule.getTitle().contains("loaner")){
				rule.setTitle(rule.getTitle().replace("${loaner}", loaner.getName()));
			}
			if(rule.getRmk().contains("loanee")) {
				rule.setRmk(rule.getRmk().replace("${loanee}", loanee.getName()+"[ID:"+loanee.getId()+"]"));
			}
			if(rule.getRmk().contains("loaner")) {
				rule.setRmk(rule.getRmk().replace("${loaner}", loaner.getName()+"[ID:"+loaner.getId()+"]"));
			}
		}else {
			NfsLoanRecord loanRecord = loanRecordService.get(orgId);
			if(loanRecord != null) {
				loaner = memberService.get(loanRecord.getLoaner());
				loanee = memberService.get(loanRecord.getLoanee());
				
				if(rule.getTitle().contains("loanee")){
					rule.setTitle(rule.getTitle().replace("${loanee}", loanee.getName()));
				}
				if(rule.getTitle().contains("loaner")){
					rule.setTitle(rule.getTitle().replace("${loaner}", loaner.getName()));
				}
				if(rule.getRmk().contains("loanee")) {
					rule.setRmk(rule.getRmk().replace("${loanee}", loanee.getName()+"[ID:"+loanee.getId()+"]"));
				}
				if(rule.getRmk().contains("loaner")) {
					rule.setRmk(rule.getRmk().replace("${loaner}", loaner.getName()+"[ID:"+loaner.getId()+"]"));
				}
			}
		}
		if(rule.getTitle().contains("payer")){
			rule.setTitle(rule.getTitle().replace("${payer}", member.getName()));
		}
		if(rule.getRmk().contains("payer")) {
			rule.setRmk(rule.getRmk().replace("${payer}", member.getName()+"[ID:"+member.getId()+"]"));
		}
		if(rule.getRmk().contains("amount")) {
			rule.setRmk(rule.getRmk().replace("${amount}", StringUtils.decimalToStr(amount, 2)));
		}
		if(rule.getRmk().contains("businessId")) {
			rule.setRmk(rule.getRmk().replace("${businessId}", orgId+""));
		}
		if(rule.getRmk().contains("#")) {
			String rmk = rule.getRmk();
			if(AliveVideoStatus.notUpload.equals(detail.getAliveVideoStatus())) {
				rmk = rmk.substring(0, rmk.lastIndexOf("#"));
			}else {
				rmk = rmk.substring(rmk.lastIndexOf("#")+1);
			}
			rule.setRmk(rmk);
		}
		memberActTrx.setTitle(rule.getTitle());
		memberActTrx.setRmk(rule.getRmk());
		return memberActTrx;
	}

	private MemberActTrx getTitleAndRmk(MemberActTrx memberActTrx,Member payer,Member payee,Long orgId,NfsTrxRuleItem rule,BigDecimal amount) {
				
		if(rule.getTitle().contains("payer")){
			rule.setTitle(rule.getTitle().replace("${payer}", payer.getName()));
		}
		if(rule.getTitle().contains("payee")){
			rule.setTitle(rule.getTitle().replace("${payee}", payee.getName()));
		}
		if(rule.getRmk().contains("payer")) {
			rule.setRmk(rule.getRmk().replace("${payer}", payer.getName()+"[ID:"+payer.getId()+"]"));
		}
		if(rule.getRmk().contains("payee")) {
			rule.setRmk(rule.getRmk().replace("${payee}", payee.getName()+"[ID:"+payee.getId()+"]"));
		}
		if(rule.getRmk().contains("amount")) {
			rule.setRmk(rule.getRmk().replace("${amount}", StringUtils.decimalToStr(amount, 2)));
		}
		if(rule.getRmk().contains("businessId")) {
			rule.setRmk(rule.getRmk().replace("${businessId}", orgId+""));
		}
		if(rule.getRmk().contains("#")) {
			String rmk = rule.getRmk();
			NfsLoanApplyDetail detail = detailService.get(orgId);
			if(AliveVideoStatus.notUpload.equals(detail.getAliveVideoStatus())) {
				rmk = rmk.substring(0, rmk.lastIndexOf("#"));
			}else {
				rmk = rmk.substring(rmk.lastIndexOf("#")+1);
			}
			rule.setRmk(rmk);
		}
		String trxCode = rule.getTrxCode();
		String prefixCode = StringUtils.substring(trxCode, 0, 2);
		if(StringUtils.equals(prefixCode, TrxRuleConstant.CR_PAY_LOANBAL.substring(0, 2))) {
			//orgId是债转id
			NfsCrAuction crAuction = crAuctionService.get(Long.valueOf(orgId));
			rule.setRmk(rule.getRmk().replace("${loanId}", crAuction.getLoanRecord().getId()+""));
		}
		memberActTrx.setTitle(rule.getTitle());
		memberActTrx.setRmk(rule.getRmk());
		return memberActTrx;
	}

}