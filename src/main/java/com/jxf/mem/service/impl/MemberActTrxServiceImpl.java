package com.jxf.mem.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.mem.dao.MemberActTrxDao;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.StringUtils;
/**
 * 账户交易记录ServiceImpl
 * @author zhj
 * @version 2016-05-13
 */
@Service("memberActTrxService")
@Transactional(readOnly = true)
public class MemberActTrxServiceImpl extends CrudServiceImpl<MemberActTrxDao, MemberActTrx> implements MemberActTrxService{

	@Autowired
	private MemberActTrxDao memberActTrxDao;
	
	
	public MemberActTrx get(Long id) {
		return super.get(id);
	}
	
	public List<MemberActTrx> findList(MemberActTrx memberActTrx) {
		return super.findList(memberActTrx);
	}
	
	public Page<MemberActTrx> findPage(Page<MemberActTrx> page, MemberActTrx memberActTrx) {
		return super.findPage(page, memberActTrx);
	}
	
	@Transactional(readOnly = false)
	public void save(MemberActTrx memberActTrx) {
		super.save(memberActTrx);
	}
	
	@Transactional(readOnly = false)
	public void delete(MemberActTrx memberActTrx) {
		super.delete(memberActTrx);
	}

	

	@Override
	public Page<MemberActTrx> findMemberActTrxPage(Member member,Integer pageNo, Integer pageSize) {

		Page<MemberActTrx> page = new Page<MemberActTrx>(pageNo == null?1:pageNo, pageSize == null?10:pageSize);	
		MemberActTrx memberActTrx = new MemberActTrx();	
		memberActTrx.setPage(page);
		memberActTrx.setMember(member);
		List<MemberActTrx> memberActTrxList = memberActTrxDao.findList(memberActTrx);
		page.setList(memberActTrxList);
		return page;
	}

	@Override
	public BigDecimal sumAmount(Member member, String trxCode) {
		
		MemberActTrx memberActTrx = new MemberActTrx();
		memberActTrx.setMember(member);
		memberActTrx.setTrxCode(trxCode);
		return memberActTrxDao.sumAmount(memberActTrx);
	}

	@Override
	public Page<MemberActTrx> findActTrxListPage(MemberActTrx memberActTrx,
			Integer pageNo, Integer pageSize) {
		Page<MemberActTrx> page = new Page<MemberActTrx>(pageNo == null?1:pageNo, pageSize == null?20:pageSize);	
		memberActTrx.setPage(page);
		List<MemberActTrx> memberActTrxList = memberActTrxDao.findList(memberActTrx);
		page.setList(memberActTrxList);
		return page;
	}

	@Override
	public MemberActTrx countTotalAccountByGroup(MemberActTrx memberActTrx) {
		return memberActTrxDao.countTotalAccountByGroup(memberActTrx);
	}

	/**
	 * 根据用户和借条查询仲裁证据流水
	 * @param member
	 * @param loanRecord
	 * @return
	 */
	public List<MemberActTrx> getTrxByLoanArbitrationProof(Member member,NfsLoanRecord loanRecord) {
		
		MemberActTrx memberActTrx = new MemberActTrx();
		memberActTrx.setMember(member);
		memberActTrx.setOrgId(loanRecord.getId());
		Date beginTime = CalendarUtil.addDay(loanRecord.getCreateTime(), -1);		
		Date endTime = CalendarUtil.addDay(loanRecord.getCreateTime(), 2);
		
		memberActTrx.setBeginTime(beginTime);
		memberActTrx.setEndTime(endTime);
		
//		List<MemberActTrx> loanTrxList = memberActTrxDao.getTrxByMemberAndLoan(memberActTrx);		
//		if(loanTrxList!=null&&loanTrxList.size()>0){
//			
//			List<MemberActTrx> extraTrxList = memberActTrxDao.getExtraTrx(memberActTrx);
//			loanTrxList.addAll(extraTrxList);
//			return loanTrxList;
//		}
		return memberActTrxDao.getTrxByMemberAndSubNo(memberActTrx);
	}
	/**
	 * 根据原业务id查询用户的资金明细
	 */
	@Override
	public List<MemberActTrx> findListByOrgId(Long orgId) {
		return memberActTrxDao.findListByOrgId(orgId);
	}

	@Override
	public String getImage(MemberActTrx trx) {
		String imageUrl =  Global.getConfig("domain");
		String trxCode = trx.getTrxCode();
		String drc = trx.getDrc();
		
		if(StringUtils.equals(drc, "C") && (StringUtils.equals(trxCode, TrxRuleConstant.LOAN_DONE_AVLAMT)
				|| StringUtils.equals(trxCode, TrxRuleConstant.FROZEN_LOANER_FUNDS) 
				|| StringUtils.equals(trxCode, TrxRuleConstant.LOAN_DONE_FROZENAMT)
				|| StringUtils.equals(trxCode, TrxRuleConstant.UNFROZEN_LOANER_FUNDS)
				|| StringUtils.equals(trxCode, TrxRuleConstant.MEMBER_BAL_REDUCE)
				|| StringUtils.equals(trxCode, TrxRuleConstant.REPAY_FULL_AVLBAL)
				|| StringUtils.equals(trxCode, TrxRuleConstant.LOANEE_APPLY_PARTIAL)
				|| StringUtils.equals(trxCode, TrxRuleConstant.CANCEL_PARTIAL_REPAY)
				|| StringUtils.equals(trxCode, TrxRuleConstant.LOANER_APPROVE_PARTIAL_REPAYMENT)
				|| StringUtils.equals(trxCode, TrxRuleConstant.REFUSE_PARTIAL_PAYMENT)
				|| StringUtils.equals(trxCode, TrxRuleConstant.LINE_DOWN_PAYMENT)
				|| StringUtils.equals(trxCode, TrxRuleConstant.LOANEE_APPROVE_PARTIAL_REPAYMENT)
				)) {
			imageUrl =  imageUrl+Global.getConfig("messageIcon.loanC");
		}else if(StringUtils.equals(drc, "D") && (StringUtils.equals(trxCode, TrxRuleConstant.LOAN_DONE_AVLAMT)   
				|| StringUtils.equals(trxCode, TrxRuleConstant.LOAN_DONE_REPAY)
				|| StringUtils.equals(trxCode, TrxRuleConstant.FROZEN_LOANER_FUNDS)
				|| StringUtils.equals(trxCode, TrxRuleConstant.LOAN_DONE_FROZENAMT)
				|| StringUtils.equals(trxCode, TrxRuleConstant.UNFROZEN_LOANER_FUNDS)
				|| StringUtils.equals(trxCode, TrxRuleConstant.MEMBER_BAL_ADD)
				|| StringUtils.equals(trxCode, TrxRuleConstant.REPAY_FULL_AVLBAL)
				|| StringUtils.equals(trxCode, TrxRuleConstant.LOANEE_APPLY_PARTIAL)
				|| StringUtils.equals(trxCode, TrxRuleConstant.CANCEL_PARTIAL_REPAY)
				|| StringUtils.equals(trxCode, TrxRuleConstant.LOANER_APPROVE_PARTIAL_REPAYMENT)
				|| StringUtils.equals(trxCode, TrxRuleConstant.REFUSE_PARTIAL_PAYMENT)
				|| StringUtils.equals(trxCode, TrxRuleConstant.LOANEE_APPROVE_PARTIAL_REPAYMENT)
				)) {
			imageUrl = imageUrl+Global.getConfig("messageIcon.loanD");
		}else if(StringUtils.equals(trxCode, TrxRuleConstant.COLLECTION_FAILURE_REFUND) 
			|| StringUtils.equals(trxCode, TrxRuleConstant.ARBITRATION_EXECUTION_REFUND)
			|| StringUtils.equals(trxCode, TrxRuleConstant.MEMBER_WITHDRAWALS_REFUND_LOAN)
			|| StringUtils.equals(trxCode, TrxRuleConstant.MEMBER_WITHDRAWALS_REFUND_AVL)
			|| StringUtils.equals(trxCode, TrxRuleConstant.MEMBER_WITHDRAWALS_REFUND_FEE)
			|| StringUtils.equals(trxCode, TrxRuleConstant.GXT_CANCEL_LOAN_REFUND_LOANACT)
			|| StringUtils.equals(trxCode, TrxRuleConstant.GXT_CANCEL_LOAN_REFUND_AVLACT)
			|| StringUtils.equals(trxCode, TrxRuleConstant.GXT_CANCEL_LOAN_REFUND_WXPAY)
			|| StringUtils.equals(trxCode, TrxRuleConstant.GXT_REFUSE_LOAN_REFUND_LOANACT)
			|| StringUtils.equals(trxCode, TrxRuleConstant.GXT_REFUSE_LOAN_REFUND_AVLACT)
			|| StringUtils.equals(trxCode, TrxRuleConstant.GXT_REFUSE_LOAN_REFUND_WXPAY)
			|| StringUtils.equals(trxCode, TrxRuleConstant.GXT_TIMEOUT_LOAN_REFUND_LOANACT)
			|| StringUtils.equals(trxCode, TrxRuleConstant.GXT_TIMEOUT_LOAN_REFUND_AVLACT)
			|| StringUtils.equals(trxCode, TrxRuleConstant.GXT_TIMEOUT_LOAN_REFUND_WXPAY)
			|| StringUtils.equals(trxCode, TrxRuleConstant.GXT_CANCEL_DELAY_REFUND_LOANACT)
			|| StringUtils.equals(trxCode, TrxRuleConstant.GXT_CANCEL_DELAY_REFUND_AVLACT)
			|| StringUtils.equals(trxCode, TrxRuleConstant.GXT_CANCEL_DELAY_REFUND_WXPAY)
			|| StringUtils.equals(trxCode, TrxRuleConstant.GXT_REFUSE_DELAY_REFUND_LOANACT)
			|| StringUtils.equals(trxCode, TrxRuleConstant.GXT_REFUSE_DELAY_REFUND_AVLACT)
			|| StringUtils.equals(trxCode, TrxRuleConstant.GXT_REFUSE_DELAY_REFUND_WXPAY)
			|| StringUtils.equals(trxCode, TrxRuleConstant.GXT_ARBITRATION_REFUND)
			|| StringUtils.equals(trxCode, TrxRuleConstant.GXT_EXECUTION_REFUND)
			){
			imageUrl = imageUrl+Global.getConfig("messageIcon.refund");	
		}else if(StringUtils.equals(trxCode, TrxRuleConstant.ARBITRATION_REFUND) ) {
			imageUrl = imageUrl+Global.getConfig("messageIcon.shenhe");
		}else if(StringUtils.equals(trxCode, TrxRuleConstant.MEMBER_WITHDRAWALS)
				||StringUtils.equals(trxCode, TrxRuleConstant.MEMBER_RECHARGE)
				||StringUtils.equals(trxCode, TrxRuleConstant.MEMBER_WITHDRAWALS_AVL)) {
			imageUrl = imageUrl+Global.getConfig("messageIcon.tixian");
		}else if(StringUtils.equals(trxCode, TrxRuleConstant.MEMBER_TRANSFER_LOANACT) 
				||StringUtils.equals(trxCode, TrxRuleConstant.MEMBER_TRANSFER_AVLACT)
				) {
			imageUrl = imageUrl+Global.getConfig("messageIcon.transferTo");
		}else if(StringUtils.equals(drc, "C") && (StringUtils.equals(trxCode, TrxRuleConstant.ARBITRATION_PREPAY) 
				|| StringUtils.equals(trxCode, TrxRuleConstant.ARBITRATION_EXECUTION_PREPAY) 
				|| StringUtils.equals(trxCode, TrxRuleConstant.ONE_YUAN_RECORD)
				|| StringUtils.equals(trxCode, TrxRuleConstant.INDIVIDUAL_MEMBER_CERTIFICATION)
				|| StringUtils.equals(trxCode, TrxRuleConstant.COLLECTION_PREPAY)
				|| StringUtils.equals(trxCode, TrxRuleConstant.LEGAL_FARE)
				|| StringUtils.equals(trxCode, TrxRuleConstant.LOAN_DONE_FEE))
				|| StringUtils.equals(trxCode, TrxRuleConstant.CR_BAL_ADD)
				|| StringUtils.equals(trxCode, TrxRuleConstant.GXT_ARBITRATION_PREPAY)
				|| StringUtils.equals(trxCode, TrxRuleConstant.GXT_EXECUTION_PREPAY)
				) {
			imageUrl = imageUrl+Global.getConfig("messageIcon.zhongcai");
		}else if(StringUtils.equals(trxCode, TrxRuleConstant.CR_PAY_LOANBAL)
				|| StringUtils.equals(trxCode, TrxRuleConstant.CR_PAY_AVLBAL) 
				|| StringUtils.equals(trxCode, TrxRuleConstant.CR_PAY_PENDINGRECEIVE) 
				|| StringUtils.equals(trxCode, TrxRuleConstant.CR_PAY_FEE)) {
			imageUrl = imageUrl+Global.getConfig("messageIcon.zhaizhuan");
		}else if(StringUtils.equals(trxCode, TrxRuleConstant.GXT_LOAN_DONE_LOANACT) 
				||StringUtils.equals(trxCode, TrxRuleConstant.GXT_LOAN_DONE_AVLACT) 
				||StringUtils.equals(trxCode, TrxRuleConstant.GXT_LOAN_DONE_WXPAY)
				||StringUtils.equals(trxCode, TrxRuleConstant.GXT_LOAN_DELAY_LOANACT)
				||StringUtils.equals(trxCode, TrxRuleConstant.GXT_LOAN_DELAY_AVLACT)
				||StringUtils.equals(trxCode, TrxRuleConstant.GXT_LOAN_DELAY_WXPAY)
				
				) {
			imageUrl = imageUrl+Global.getConfig("messageIcon.gxtLoan");
		}
		return imageUrl;
	}

	@Override
	public Page<MemberActTrx> findAllActTrxListPage(MemberActTrx memberActTrx, Integer pageNo, Integer pageSize) {
		Page<MemberActTrx> page = new Page<MemberActTrx>(pageNo == null?1:pageNo, pageSize == null?20:pageSize);	
		memberActTrx.setPage(page);
		List<MemberActTrx> memberActTrxList = memberActTrxDao.findAllForAppList(memberActTrx);
		page.setList(memberActTrxList);
		return page;
	}

	@Override
	public List<MemberActTrx> findLN040FailedList(MemberActTrx memberActTrx) {
		return dao.findLN040FailedList(memberActTrx);
	}

	@Override
	public Page<MemberActTrx> findActTrxPage(Page<MemberActTrx> page, MemberActTrx memberActTrx) {
		memberActTrx.setPage(page);
		List<MemberActTrx> memberActTrxList = memberActTrxDao.findActTrxList(memberActTrx);
		page.setList(memberActTrxList);
		return page;
	}
	
	@Override
	public Page<MemberActTrx> findLoanTrxListPage(MemberActTrx memberActTrx, Integer pageNo, Integer pageSize) {
		Page<MemberActTrx> page = new Page<MemberActTrx>(pageNo == null?1:pageNo, pageSize == null?20:pageSize);	
		memberActTrx.setPage(page);
		List<MemberActTrx> memberActTrxList = memberActTrxDao.findLoanTrxListPage(memberActTrx);
		page.setList(memberActTrxList);
		return page;
	}

	@Override
	public Page<MemberActTrx> findCollectionTrxListPage( MemberActTrx memberActTrx, Integer pageNo, Integer pageSize) {
		Page<MemberActTrx> page = new Page<MemberActTrx>(pageNo == null?1:pageNo, pageSize == null?20:pageSize);	
		memberActTrx.setPage(page);
		List<MemberActTrx> memberActTrxList = memberActTrxDao.findCollectionTrxListPage(memberActTrx);
		page.setList(memberActTrxList);
		return page;
	}

	@Override
	public List<MemberActTrx> findByTrxCodesList(MemberActTrx memberActTrx) {
		return memberActTrxDao.findByTrxCodesList(memberActTrx);
	}

	@Override
	public MemberActTrx countTotalByTrxCodes(MemberActTrx memberActTrx) {
		return memberActTrxDao.countTotalByTrxCodes(memberActTrx);
	}

	@Override
	public List<MemberActTrx> findListExceptToPaidAndDueIn(MemberActTrx memberActTrx) {
		return memberActTrxDao.findListExceptToPaidAndDueIn(memberActTrx);
	}


}