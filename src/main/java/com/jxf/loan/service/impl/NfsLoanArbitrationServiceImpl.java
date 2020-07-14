package com.jxf.loan.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.loan.dao.NfsLoanArbitrationDao;
import com.jxf.loan.entity.NfsLoanArbitration;
import com.jxf.loan.entity.NfsLoanArbitration.PreservationProcess;
import com.jxf.loan.entity.NfsLoanArbitration.Status;
import com.jxf.loan.entity.NfsLoanArbitrationDetail;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRecord.ArbitrationStatus;
import com.jxf.loan.service.NfsLoanArbitrationDetailService;
import com.jxf.loan.service.NfsLoanArbitrationService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberAct;
import com.jxf.mem.service.MemberActService;
import com.jxf.mem.service.MemberService;
import com.jxf.nfs.constant.ActSubConstant;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.service.NfsActService;
import com.jxf.payment.entity.Payment;
import com.jxf.svc.config.Constant;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.StringUtils;

/**
 * 借条仲裁增删改查ServiceImpl
 * @author LIUHUAIXIN
 * @version 2018-11-07
 */
@Service("nfsLoanArbitrationService")
@Transactional(readOnly = true)
public class NfsLoanArbitrationServiceImpl extends CrudServiceImpl<NfsLoanArbitrationDao, NfsLoanArbitration> implements NfsLoanArbitrationService{

	@Autowired
	private NfsLoanArbitrationDao loanArbitrationDao;
	@Autowired
	private NfsLoanArbitrationDetailService arbitrationDetailService;
	@Autowired
	private NfsLoanRecordService nfsLoanRecordService;
	@Autowired
	private NfsActService actService;
	@Autowired
	private MemberActService memberActService;
	@Autowired
	private MemberService memberService;
	
	public NfsLoanArbitration get(Long id) {
		return super.get(id);
	}
	
	public List<NfsLoanArbitration> findList(NfsLoanArbitration nfsLoanArbitration) {
		return super.findList(nfsLoanArbitration);
	}
	
	public Page<NfsLoanArbitration> findPage(Page<NfsLoanArbitration> page, NfsLoanArbitration nfsLoanArbitration) {
		return super.findPage(page, nfsLoanArbitration);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsLoanArbitration nfsLoanArbitration) {
		
		if(nfsLoanArbitration.getIsNewRecord()) {
			nfsLoanArbitration.preInsert();
			nfsLoanArbitration.setPreservationProcess(PreservationProcess.noUpload);
			loanArbitrationDao.insert(nfsLoanArbitration);
		}else {
			nfsLoanArbitration.preUpdate();
			loanArbitrationDao.update(nfsLoanArbitration);		
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsLoanArbitration nfsLoanArbitration) {
		super.delete(nfsLoanArbitration);
	}

	public Page<NfsLoanArbitration> findInArbitration(Long loanerId,NfsLoanArbitration nfsLoanArbitration,Integer pageNo,Integer pageSize,int trxType){
		Page<NfsLoanArbitration> page = new Page<NfsLoanArbitration>(pageNo == null?1:pageNo, pageSize == null?20:pageSize);	
		nfsLoanArbitration.setPage(page);
		List<NfsLoanArbitration> loanList = loanArbitrationDao.findInArbitration(loanerId,nfsLoanArbitration,trxType);
		page.setList(loanList);
		return page;
	}
	
	public Page<NfsLoanArbitration> findArbitration(Long loanId,NfsLoanArbitration nfsLoanArbitration,Integer pageNo,Integer pageSize,int trxType){
		Page<NfsLoanArbitration> page = new Page<NfsLoanArbitration>(pageNo == null?1:pageNo, pageSize == null?20:pageSize);	
		nfsLoanArbitration.setPage(page);
		List<NfsLoanArbitration> loanList = loanArbitrationDao.findArbitration(loanId,nfsLoanArbitration,trxType);
		page.setList(loanList);
		return page;
	}
	
	public Page<NfsLoanArbitration> findEndOfArbitration(Long loanId,NfsLoanArbitration nfsLoanArbitration,Integer pageNo,Integer pageSize,int trxType){
		Page<NfsLoanArbitration> page = new Page<NfsLoanArbitration>(pageNo == null?1:pageNo, pageSize == null?20:pageSize);	
		nfsLoanArbitration.setPage(page);
		List<NfsLoanArbitration> loanList = loanArbitrationDao.findEndOfArbitration(loanId,nfsLoanArbitration,trxType);
		page.setList(loanList);
		return page;
	}

	public List<NfsLoanArbitration> findTransferCase() {
		List<NfsLoanArbitration> list = loanArbitrationDao.findTransferCase();
		return list;
	}

	@Override
	public Page<NfsLoanArbitration> findExecution(Long id, NfsLoanArbitration nfsLoanArbitration,Integer pageNo, Integer pageSize,int trxType) {
		Page<NfsLoanArbitration> page = new Page<NfsLoanArbitration>(pageNo == null?1:pageNo, pageSize == null?20:pageSize);	
		nfsLoanArbitration.setPage(page);
		List<NfsLoanArbitration> loanList = loanArbitrationDao.findExecution(id,nfsLoanArbitration,trxType);
		page.setList(loanList);
		return page;
	}

	@Override
	public List<NfsLoanArbitration> findMemberArbitrationList(NfsLoanArbitration nfsLoanArbitration) {
		return loanArbitrationDao.findMemberArbitrationList(nfsLoanArbitration);
	}

	@Override
	public NfsLoanArbitration getByLoanId(Long loanId) {
		return loanArbitrationDao.getByLoanId(loanId);
	}

	@Override
	@Transactional(readOnly = false)
	public int close(Long loanId) {
		NfsLoanArbitration nfsLoanArbitration = loanArbitrationDao.getByLoanId(loanId);
		nfsLoanArbitration.setStatus(Status.debit);
		NfsLoanRecord loan = nfsLoanRecordService.get(loanId);
		if(loan !=null) {
		//关闭仲裁log
		if(nfsLoanArbitration.getStatus().equals(Status.debit)) {
			NfsLoanArbitrationDetail arbitrationDetail = new NfsLoanArbitrationDetail();
			arbitrationDetail.setArbitrationId(nfsLoanArbitration.getId());
			arbitrationDetail.setStatus(NfsLoanArbitrationDetail.Status.debit);
			arbitrationDetail.setTask(NfsLoanArbitrationDetail.Task.debit);
			arbitrationDetail.setType(NfsLoanArbitrationDetail.Type.filedResult);
			arbitrationDetailService.save(arbitrationDetail);
			save(nfsLoanArbitration);
			return 1;
		}
		else {
			return 0;
		}
	}
		return 0;
	}

	@Override
	@Transactional(readOnly=false)
	public int payForApplyArbitration(Member member,NfsLoanArbitration arbitration) {
		
		NfsLoanRecord loanRecord = arbitration.getLoan();
		loanRecord.setArbitrationStatus(NfsLoanRecord.ArbitrationStatus.doing);
		nfsLoanRecordService.save(loanRecord);
		
		arbitration.setLoan(loanRecord);
		arbitration.setStatus(NfsLoanArbitration.Status.application);
		save(arbitration);

		NfsLoanArbitrationDetail nfsLoanArbitrationDetail = new NfsLoanArbitrationDetail();
		nfsLoanArbitrationDetail.setStatus(NfsLoanArbitrationDetail.Status.underReview);
		nfsLoanArbitrationDetail.setTask(NfsLoanArbitrationDetail.Task.manualReview);
		nfsLoanArbitrationDetail.setType(NfsLoanArbitrationDetail.Type.auditProcess);
		nfsLoanArbitrationDetail.setArbitrationId(arbitration.getId());
		arbitrationDetailService.save(nfsLoanArbitrationDetail);
		
		int code = actService.updateAct(TrxRuleConstant.ARBITRATION_PREPAY, StringUtils.toDecimal(arbitration.getFee()), member, arbitration.getId());
		if(code == Constant.UPDATE_FAILED) {
			throw new RuntimeException("用户["+ member.getId() +"]申请仲裁缴费扣款不成功，借条id:"+arbitration.getLoan().getId());
		}
		return Constant.UPDATE_SUCCESS;
	}

	@Override
	@Transactional(readOnly=false)
	public boolean payForApplyByWx(Payment payment) {
		Long orgId = payment.getOrgId();
		NfsLoanArbitration loanArbitration = get(orgId);
		if(payment.getStatus().equals(Payment.Status.failure)) {
			//删除该申请
			delete(loanArbitration);
			return false;
		}
		
		loanArbitration.setStatus(Status.application);
		save(loanArbitration);
		
		NfsLoanRecord loanRecord = nfsLoanRecordService.get(loanArbitration.getLoan());
		loanRecord.setArbitrationStatus(ArbitrationStatus.doing);
		nfsLoanRecordService.save(loanRecord);
		
		NfsLoanArbitrationDetail nfsLoanArbitrationDetail = new NfsLoanArbitrationDetail();
		nfsLoanArbitrationDetail.setStatus(NfsLoanArbitrationDetail.Status.underReview);
		nfsLoanArbitrationDetail.setTask(NfsLoanArbitrationDetail.Task.manualReview);
		nfsLoanArbitrationDetail.setType(NfsLoanArbitrationDetail.Type.auditProcess);
		nfsLoanArbitrationDetail.setArbitrationId(loanArbitration.getId());
		arbitrationDetailService.save(nfsLoanArbitrationDetail);
		
		BigDecimal fee = new BigDecimal(loanArbitration.getFee());
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
		int code = actService.updateAct(TrxRuleConstant.GXT_ARBITRATION_PREPAY, fee, member, orgId);
		if(code == Constant.UPDATE_FAILED) {
			throw new RuntimeException("用户["+ member.getId() +"]申请仲裁缴费微信账户更新失败");
		}
		return true;
	}
}