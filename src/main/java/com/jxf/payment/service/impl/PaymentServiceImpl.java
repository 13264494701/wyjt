package com.jxf.payment.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.persistence.sequence.utils.SequenceUtils;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.entity.NfsLoanArbitration;
import com.jxf.loan.entity.NfsLoanPartialAndDelay;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanApplyDetailService;
import com.jxf.loan.service.NfsLoanArbitrationExecutionService;
import com.jxf.loan.service.NfsLoanArbitrationService;
import com.jxf.loan.service.NfsLoanPartialAndDelayService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.payment.dao.PaymentDao;
import com.jxf.payment.entity.Payment;
import com.jxf.payment.entity.Payment.Type;
import com.jxf.payment.service.PaymentService;

/**
 * 支付记录ServiceImpl
 * 
 * @author HUOJIABAO
 * @version 2016-06-08
 */
@Service("paymentService")
@Transactional(readOnly = true)
public class PaymentServiceImpl extends CrudServiceImpl<PaymentDao, Payment> implements PaymentService {
	
	@Autowired
	private PaymentDao paymentDao;
	@Autowired
	private NfsLoanApplyDetailService applyDetailService;
	@Autowired
	private NfsLoanPartialAndDelayService loanPartialAndDelayService;
	@Autowired
	private NfsLoanArbitrationService loanArbitrationService;
	@Autowired
	private NfsLoanArbitrationExecutionService arbitrationExecutionService;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private MemberActTrxService memberActTrxService;
	
	public Payment get(Long id) {
		return super.get(id);
	}

	public List<Payment> findList(Payment payment) {
		return super.findList(payment);
	}

	public Page<Payment> findPage(Page<Payment> page, Payment payment) {
		return super.findPage(page, payment);
	}

	@Transactional(readOnly = false)
	public void save(Payment payment) {
		if (payment.getIsNewRecord()) {
			payment.setPaymentNo(DateUtils.getDate("yyyyMMdd") + SequenceUtils.getSequence("WYJT_NFS_PAYMENT", 8));
			payment.preInsert();
			payment.setVersion(0);
			paymentDao.insert(payment);
		} else {
			payment.preUpdate();
			paymentDao.update(payment);
		}

	}

	@Transactional(readOnly = false)
	public void delete(Payment payment) {
		super.delete(payment);
	}

	/**
	 * 根据编号查找支付记录
	 * 
	 * @param paymentNo
	 *            编号(忽略大小写)
	 * @return 支付记录，若不存在则返回null
	 */
	public Payment getByPaymentNo(String paymentNo) {
		return paymentDao.findByPaymentNo(paymentNo);
	}

	/**
	 * 支付处理
	 * 
	 * @param paymentRecord
	 *            支付记录
	 */
	public void handle(Payment payment) {

	}

	@Override
	@Transactional(readOnly = false)
	public void payFinishProcess(Payment payment) {
		save(payment);
		Long orgTrxId = 0L;
		MemberActTrx memberActTrx = new MemberActTrx();
		try {
			switch (payment.getType()) {
			case recharge:
				break;
			case loanDone:
				applyDetailService.loaneePayForApplyByWx(payment);
				NfsLoanApplyDetail loanApplyDetail = applyDetailService.get(payment.getOrgId());
				
				memberActTrx.setTrxCode(TrxRuleConstant.GXT_LOAN_DONE_WXPAY);
				memberActTrx.setOrgId(loanApplyDetail.getId());
				List<MemberActTrx> list = memberActTrxService.findList(memberActTrx);
				if(!Collections3.isEmpty(list)) {
					orgTrxId = list.get(0).getId();
				}
				memberMessageService.sendMessage(MemberMessage.Type.payFeeForLoan,orgTrxId);
				break;
			case loanDelay:
				loanPartialAndDelayService.loaneePayForDelayByWx(payment);
				NfsLoanPartialAndDelay partialAndDelay = loanPartialAndDelayService.get(payment.getOrgId());
				//发送会员消息
				memberMessageService.sendMessage(MemberMessage.Type.delayApplicationLoanee,partialAndDelay.getId());

				memberMessageService.sendMessage(MemberMessage.Type.delayApplicationLoaner,partialAndDelay.getId());
				break;
			case arbitration:
				loanArbitrationService.payForApplyByWx(payment);
				NfsLoanArbitration loanArbitration = loanArbitrationService.get(payment.getOrgId());
				NfsLoanRecord loanRecord = loanRecordService.get(loanArbitration.getLoan());
				
				memberActTrx.setTrxCode(TrxRuleConstant.GXT_ARBITRATION_PREPAY);
				memberActTrx.setOrgId(loanArbitration.getId());
				List<MemberActTrx> list1 = memberActTrxService.findList(memberActTrx);
				if(!Collections3.isEmpty(list1)) {
					orgTrxId = list1.get(0).getId();
				}
				
				memberMessageService.sendMessage(MemberMessage.Type.acceptanceArbitrationLoanee,loanRecord.getId());
				memberMessageService.sendMessage(MemberMessage.Type.acceptanceArbitrationLoaner,loanRecord.getId());
				memberMessageService.sendMessage(MemberMessage.Type.payArbitrationFee,orgTrxId);
				
				break;
			case execution:
				arbitrationExecutionService.payForApplyByWx(payment);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
			// 微信账户余额更新失败，借条没有生成，钱已经从微信扣了 ，这种异常先不处理
		}
	}

	@Override
	public Payment getByTypeAndOrgId(Type type, Long orgId) {

		Payment payment = new Payment();
		payment.setType(type);
		payment.setOrgId(orgId);
		List<Payment> findList = findList(payment);
		if(findList != null && findList.size() > 0){
			return findList(payment).get(0);
		}else{
			return null;
		}
		
	}
}