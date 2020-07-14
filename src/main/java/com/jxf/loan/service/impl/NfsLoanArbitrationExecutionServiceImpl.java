package com.jxf.loan.service.impl;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.loan.dao.NfsLoanArbitrationDao;
import com.jxf.loan.dao.NfsLoanArbitrationExecutionDao;
import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanArbitration;
import com.jxf.loan.entity.NfsLoanArbitrationExecution;
import com.jxf.loan.entity.NfsLoanArbitrationExecution.ExecutionStatus;
import com.jxf.loan.entity.NfsLoanArbitrationExecutionDetail;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanArbitrationExecutionDetailService;
import com.jxf.loan.service.NfsLoanArbitrationExecutionService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.utils.LoanUtils;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberAct;
import com.jxf.mem.service.MemberActService;
import com.jxf.mem.service.MemberService;
import com.jxf.nfs.constant.ActSubConstant;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.service.NfsActService;
import com.jxf.payment.entity.Payment;
import com.jxf.payment.entity.Payment.Status;
import com.jxf.svc.config.Constant;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.DateUtils;

/**
 * 强执ServiceImpl
 * @author LIUHUAIXIN
 * @version 2018-12-20
 */
@Service("nfsLoanArbitrationExecutionService")
@Transactional(readOnly = true)
public class NfsLoanArbitrationExecutionServiceImpl extends CrudServiceImpl<NfsLoanArbitrationExecutionDao, NfsLoanArbitrationExecution> implements NfsLoanArbitrationExecutionService{

	@Autowired
	private NfsLoanArbitrationExecutionDao loanArbitrationExecutionDao;
	@Autowired
	private NfsLoanArbitrationExecutionDetailService loanArbitrationExecutionDetailService;
	@Autowired
	private NfsLoanArbitrationDao loanArbitrationDao;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsActService actService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberActService memberActService;
	
	
	public NfsLoanArbitrationExecution get(Long id) {
		return super.get(id);
	}
	
	public List<NfsLoanArbitrationExecution> findList(NfsLoanArbitrationExecution nfsLoanArbitrationExecution) {
		return super.findList(nfsLoanArbitrationExecution);
	}
	
	public Page<NfsLoanArbitrationExecution> findPage(Page<NfsLoanArbitrationExecution> page, NfsLoanArbitrationExecution nfsLoanArbitrationExecution) {
		page.setOrderBy("a.paytime DESC");
		return super.findPage(page, nfsLoanArbitrationExecution);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsLoanArbitrationExecution nfsLoanArbitrationExecution) {
		super.save(nfsLoanArbitrationExecution);
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsLoanArbitrationExecution nfsLoanArbitrationExecution) {
		super.delete(nfsLoanArbitrationExecution);
	}
	
	public Page<NfsLoanArbitrationExecution> findExecution(NfsLoanArbitrationExecution nfsLoanArbitrationExecution,Integer pageNo, Integer pageSize) {
		Page<NfsLoanArbitrationExecution> page = new Page<NfsLoanArbitrationExecution>(pageNo == null?1:pageNo, pageSize == null?20:pageSize);	
		nfsLoanArbitrationExecution.setPage(page);
		List<NfsLoanArbitrationExecution> loanList = loanArbitrationExecutionDao.findExecution(nfsLoanArbitrationExecution);
		page.setList(loanList);
		return page;
	}
	
	public Page<NfsLoanArbitrationExecution> findExecuted(Long id, NfsLoanArbitrationExecution nfsLoanArbitrationExecution,Integer pageNo, Integer pageSize) {
		Page<NfsLoanArbitrationExecution> page = new Page<NfsLoanArbitrationExecution>(pageNo == null?1:pageNo, pageSize == null?20:pageSize);	
		nfsLoanArbitrationExecution.setPage(page);
		List<NfsLoanArbitrationExecution> loanList = loanArbitrationExecutionDao.findExecuted(id,nfsLoanArbitrationExecution);
		page.setList(loanList);
		return page;
	}
	
	@Override
	@Transactional(readOnly = false)
	public int apply(NfsLoanRecord loanRecord, NfsLoanArbitration loanArbitration) {

		loanArbitration.setStrongStatus(NfsLoanArbitration.StrongStatus.appliedStrong);
		int code = loanArbitrationDao.update(loanArbitration);
		if (code == 0) {
			return 0;
		} else {
			// 统计待还金额
	 		BigDecimal decMoney =loanRecord.getDueRepayAmount();
			// 计算逾期利息
	    	BigDecimal overMoney=new BigDecimal(0.0);
	    	int days=DateUtils.getDistanceOfTwoDate(new Date(),loanRecord.getDueRepayDate());
	    	BigDecimal overdueDaysBig = new BigDecimal(days);
	    	//计算利息 默认给24
	        overMoney=LoanUtils.calOverdueInterest(loanRecord.getDueRepayAmount(),overdueDaysBig);
		    //总金额
		    BigDecimal totalMoney=decMoney.add(overMoney);
			NfsLoanArbitrationExecution loanArbitrationExecution = new NfsLoanArbitrationExecution();
			loanArbitrationExecution.setLoanNo(loanRecord.getLoanNo());
			loanArbitrationExecution.setLoaneeId(loanRecord.getLoanee().getId());
			loanArbitrationExecution.setLoaneeName(loanRecord.getLoanee().getName());
			loanArbitrationExecution.setLoanerId(loanRecord.getLoaner().getId());
			loanArbitrationExecution.setLoanerName(loanRecord.getLoaner().getName());
			loanArbitrationExecution.setAmount(loanRecord.getAmount());
			loanArbitrationExecution.setInterest(loanRecord.getInterest());
			loanArbitrationExecution.setIntRate(loanRecord.getIntRate());
			loanArbitrationExecution.setTerm(loanRecord.getTerm());
			loanArbitrationExecution.setRepayedTerm(loanRecord.getRepayedTerm());
			loanArbitrationExecution.setDueRepayTerm(loanRecord.getDueRepayTerm());
			loanArbitrationExecution.setDueRepayDate(loanRecord.getDueRepayDate());
			loanArbitrationExecution.setRulingtime(loanArbitration.getRuleTime());
			loanArbitrationExecution.setDueRepayAmount(totalMoney);
			loanArbitrationExecution.setCompleteDate(loanRecord.getCompleteDate());
			loanArbitrationExecution.setStatus(NfsLoanArbitrationExecution.ExecutionStatus.executionApplication);
			loanArbitrationExecution.setFee(new BigDecimal(0));
			loanArbitrationExecution.setLoan(loanRecord);
			loanArbitrationExecution.setArbitrationId(loanArbitration.getId());
			save(loanArbitrationExecution);

			// 插入detail
			NfsLoanArbitrationExecutionDetail executionDetail = new NfsLoanArbitrationExecutionDetail();
			executionDetail.setExecutionId(loanArbitrationExecution.getId());
			executionDetail.setStatus(NfsLoanArbitrationExecutionDetail.Status.executionApplication);
			if (NfsLoanApply.RepayType.oneTimePrincipalAndInterest.equals(loanRecord.getRepayType())) {
				executionDetail.setType(NfsLoanArbitrationExecutionDetail.Type.fullAmount);
			} else {
				executionDetail.setType(NfsLoanArbitrationExecutionDetail.Type.staging);
			}
			loanArbitrationExecutionDetailService.save(executionDetail);

			return 1;
		}
	}

	@Override
	@Transactional(readOnly = false)
	public NfsLoanArbitrationExecution applyForGxt(NfsLoanRecord loanRecord, NfsLoanArbitration loanArbitration,BigDecimal fee) {

		loanArbitration.setStrongStatus(NfsLoanArbitration.StrongStatus.appliedStrong);
		int code = loanArbitrationDao.update(loanArbitration);
		if (code == 0) {
			return null;
		} else {
			// 统计待还金额
	 		BigDecimal decMoney =loanRecord.getDueRepayAmount();
			// 计算逾期利息
	    	BigDecimal overMoney=new BigDecimal(0.0);
	    	int days=DateUtils.getDistanceOfTwoDate(new Date(),loanRecord.getDueRepayDate());
	    	BigDecimal overdueDaysBig = new BigDecimal(days);
	    	//计算利息 默认给24
	        overMoney=LoanUtils.calOverdueInterest(loanRecord.getDueRepayAmount(),overdueDaysBig);
		    //总金额
		    BigDecimal totalMoney=decMoney.add(overMoney);
			NfsLoanArbitrationExecution loanArbitrationExecution = new NfsLoanArbitrationExecution();
			loanArbitrationExecution.setLoanNo(loanRecord.getLoanNo());
			loanArbitrationExecution.setLoaneeId(loanRecord.getLoanee().getId());
			loanArbitrationExecution.setLoaneeName(loanRecord.getLoanee().getName());
			loanArbitrationExecution.setLoanerId(loanRecord.getLoaner().getId());
			loanArbitrationExecution.setLoanerName(loanRecord.getLoaner().getName());
			loanArbitrationExecution.setAmount(loanRecord.getAmount());
			loanArbitrationExecution.setInterest(loanRecord.getInterest());
			loanArbitrationExecution.setIntRate(loanRecord.getIntRate());
			loanArbitrationExecution.setTerm(loanRecord.getTerm());
			loanArbitrationExecution.setRepayedTerm(loanRecord.getRepayedTerm());
			loanArbitrationExecution.setDueRepayTerm(loanRecord.getDueRepayTerm());
			loanArbitrationExecution.setDueRepayDate(loanRecord.getDueRepayDate());
			loanArbitrationExecution.setRulingtime(loanArbitration.getRuleTime());
			loanArbitrationExecution.setDueRepayAmount(totalMoney);
			loanArbitrationExecution.setCompleteDate(loanRecord.getCompleteDate());
			loanArbitrationExecution.setStatus(NfsLoanArbitrationExecution.ExecutionStatus.executionPayment);
			loanArbitrationExecution.setFee(fee);
			loanArbitrationExecution.setLoan(loanRecord);
			loanArbitrationExecution.setArbitrationId(loanArbitration.getId());
			save(loanArbitrationExecution);

			// 插入detail
			NfsLoanArbitrationExecutionDetail executionDetail = new NfsLoanArbitrationExecutionDetail();
			executionDetail.setExecutionId(loanArbitrationExecution.getId());
			executionDetail.setStatus(NfsLoanArbitrationExecutionDetail.Status.executionPayment);
			if (NfsLoanApply.RepayType.oneTimePrincipalAndInterest.equals(loanRecord.getRepayType())) {
				executionDetail.setType(NfsLoanArbitrationExecutionDetail.Type.fullAmount);
			} else {
				executionDetail.setType(NfsLoanArbitrationExecutionDetail.Type.staging);
			}
			loanArbitrationExecutionDetailService.save(executionDetail);
			return loanArbitrationExecution;
		}
	}
	
	
	@Override
	@Transactional(readOnly = false)
	public int close(Long arbitrationId) {
		NfsLoanArbitrationExecution loanArbitrationExecution = loanArbitrationExecutionDao.findByArbitrationId(arbitrationId);
		loanArbitrationExecution.setStatus(ExecutionStatus.debit);
		NfsLoanRecord loan = loanRecordService.get(loanArbitrationExecution.getLoan());
		if(loan != null){
		//借条关闭强执log
		if(loanArbitrationExecution.getStatus().equals(ExecutionStatus.debit)) {
			NfsLoanArbitrationExecutionDetail arbitrationExecutionDetail = new NfsLoanArbitrationExecutionDetail();
			arbitrationExecutionDetail.setExecutionId(loanArbitrationExecution.getId());
			arbitrationExecutionDetail.setStatus(NfsLoanArbitrationExecutionDetail.Status.debit);
			if(NfsLoanApply.RepayType.oneTimePrincipalAndInterest.equals(loan.getRepayType())) {
				arbitrationExecutionDetail.setType(NfsLoanArbitrationExecutionDetail.Type.fullAmount);
			}else {
				arbitrationExecutionDetail.setType(NfsLoanArbitrationExecutionDetail.Type.staging);
			}
			loanArbitrationExecutionDetailService.save(arbitrationExecutionDetail);
			save(loanArbitrationExecution);
			return 1;
		}
		else {
			return 0;
		}
		}
		return 0;
	}

	@Override
	@Transactional(readOnly = false)
	public int payForExecutionApply(Member member, NfsLoanArbitrationExecution execution) {

		NfsLoanRecord loanRecord = loanRecordService.get(execution.getLoan());
		NfsLoanArbitrationExecutionDetail executionDetail = new NfsLoanArbitrationExecutionDetail();
		executionDetail.setExecutionId(execution.getId());
		if(NfsLoanApply.RepayType.oneTimePrincipalAndInterest.equals(loanRecord.getRepayType())) {
			executionDetail.setType(NfsLoanArbitrationExecutionDetail.Type.fullAmount);
		}else {
			executionDetail.setType(NfsLoanArbitrationExecutionDetail.Type.staging);
		}
		executionDetail.setStatus(NfsLoanArbitrationExecutionDetail.Status.executionProcessing);
		loanArbitrationExecutionDetailService.save(executionDetail);
		
		int code = actService.updateAct(TrxRuleConstant.ARBITRATION_EXECUTION_PREPAY, execution.getFee(), member, execution.getId());
		if(code == Constant.UPDATE_FAILED) {
			throw new RuntimeException("会员["+ member.getId() +"]申请强执缴费时更新账户余额异常,强执id:"+execution.getId());
		}
		//仲裁强执状态变更
		execution.setPaytime(new Date());
		execution.setStatus(NfsLoanArbitrationExecution.ExecutionStatus.executionProcessing);
		save(execution);
		return Constant.UPDATE_SUCCESS;
	}

	@Override
	@Transactional(readOnly = false)
	public Boolean payForApplyByWx(Payment payment) {
		Long orgId = payment.getOrgId();
		NfsLoanArbitrationExecution arbitrationExecution = get(orgId);
		if(!arbitrationExecution.getStatus().equals(ExecutionStatus.executionPayment)) {
			return false;
		}
		if(payment.getStatus().equals(Status.failure)) {
			//支付失败就还是待缴费状态，进入待缴费中继续缴费
			return false;
		}
		arbitrationExecution.setStatus(ExecutionStatus.executionProcessing);
		arbitrationExecution.setPaytime(new Date());
		save(arbitrationExecution);
		
		NfsLoanRecord loanRecord = loanRecordService.get(arbitrationExecution.getLoan());
		NfsLoanArbitrationExecutionDetail nfsLoanArbitrationExecutionDetail = new NfsLoanArbitrationExecutionDetail();
		nfsLoanArbitrationExecutionDetail.setExecutionId(arbitrationExecution.getId());
		if(NfsLoanApply.RepayType.oneTimePrincipalAndInterest.equals(loanRecord.getRepayType())) {
			nfsLoanArbitrationExecutionDetail.setType(NfsLoanArbitrationExecutionDetail.Type.fullAmount);
		}else {
			nfsLoanArbitrationExecutionDetail.setType(NfsLoanArbitrationExecutionDetail.Type.staging);
		}
		nfsLoanArbitrationExecutionDetail.setStatus(NfsLoanArbitrationExecutionDetail.Status.executionProcessing);
		loanArbitrationExecutionDetailService.save(nfsLoanArbitrationExecutionDetail);
		
		Member member = memberService.get(payment.getPrincipalId());
		MemberAct memberAct = memberActService.getMemberAct(member, ActSubConstant.MEMBER_WEIXIN_PAYMENT);
		if (memberAct == null) {
			//生成微信账户
			MemberAct memberAct2 = new MemberAct();
			memberAct2.setMember(member);
			memberAct2.setCurBal(BigDecimal.ZERO);
			memberAct2.setCurrCode("CNY");
			memberAct2.setName("微信账户");
			memberAct2.setSubNo("0007");
			memberAct2.setStatus(MemberAct.Status.enabled);
			memberAct2.setIsDefault(false);
			memberActService.save(memberAct2);
		}
		BigDecimal fee = arbitrationExecution.getFee();
		int code = actService.updateAct(TrxRuleConstant.GXT_EXECUTION_PREPAY, fee, member, orgId);
		if(code == Constant.UPDATE_FAILED) {
			throw new RuntimeException("用户["+ member.getId() +"]申请强执缴费微信账户更新失败");
		}
		return true;
	}

	@Override
	public NfsLoanArbitrationExecution findByArbitrationId(Long arbitrationId) {
		return loanArbitrationExecutionDao.findByArbitrationId(arbitrationId);
	}
}