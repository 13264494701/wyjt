package com.jxf.loan.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.loan.dao.NfsLoanCollectionDao;
import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanCollection;
import com.jxf.loan.entity.NfsLoanCollection.Status;
import com.jxf.loan.entity.NfsLoanCollectionDetail;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanCollectionDetailService;
import com.jxf.loan.service.NfsLoanCollectionService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.service.NfsActService;
import com.jxf.svc.config.Constant;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
/**
 * 借条催收ServiceImpl
 * @author gaobo
 * @version 2018-11-07
 */
@Service("nfsLoanCollectionService")
@Transactional(readOnly = true)
public class NfsLoanCollectionServiceImpl extends CrudServiceImpl<NfsLoanCollectionDao, NfsLoanCollection> implements NfsLoanCollectionService{

	@Autowired
	private NfsLoanCollectionDao nfsLoanCollectionDao;
	
	@Autowired
	private NfsLoanCollectionDetailService nfsLoanCollectionDetailService;

	@Autowired
	private NfsLoanRecordService nfsLoanRecordService;
	
	@Autowired
	private NfsActService nfsActService;
	
	@Autowired
	private MemberService memberService;
	
	public NfsLoanCollection get(Long id) {
		return super.get(id);
	}
	
	public List<NfsLoanCollection> findList(NfsLoanCollection nfsLoanCollection) {
		return super.findList(nfsLoanCollection);
	}
	
	public Page<NfsLoanCollection> findPage(Page<NfsLoanCollection> page, NfsLoanCollection nfsLoanCollection) {
		return super.findPage(page, nfsLoanCollection);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsLoanCollection nfsLoanCollection) {
		super.save(nfsLoanCollection);
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsLoanCollection nfsLoanCollection) {
		super.delete(nfsLoanCollection);
	}


	@Override
	public Page<NfsLoanCollection> findInCollection(Long loanerId, NfsLoanCollection collection,
			Integer pageNo, Integer pageSize) {
		Page<NfsLoanCollection> page = new Page<NfsLoanCollection>(pageNo == null ? 1 : pageNo,
				pageSize == null ? 20 : pageSize);
		collection.setPage(page);
		List<NfsLoanCollection> loanList = nfsLoanCollectionDao.findInCollection(loanerId, collection);
		page.setList(loanList);
		return page;
	}

	@Override
	public Page<NfsLoanCollection> findEndOfCollection(Long loanerId, NfsLoanCollection collection,
			Integer pageNo, Integer pageSize) {
		Page<NfsLoanCollection> page = new Page<NfsLoanCollection>(pageNo == null ? 1 : pageNo,
				pageSize == null ? 20 : pageSize);
		collection.setPage(page);
		List<NfsLoanCollection> loanList = nfsLoanCollectionDao.findEndOfCollection(loanerId, collection);
		page.setList(loanList);
		return page;
	}

	@Override
	public NfsLoanCollection getCollectionByLoanId(Long loanId) {
		return nfsLoanCollectionDao.getCollectionByLoanId(loanId);
	}

	@Override
	public NfsLoanCollection findNowCollection(
			NfsLoanRecord loanRecord) {
		return nfsLoanCollectionDao.findNowCollection(loanRecord);
	}

	@Override
	public NfsLoanCollection findThirdCollection(NfsLoanRecord loanRecord) {
		return nfsLoanCollectionDao.findThirdCollection(loanRecord);
	}

	@Override
	@Transactional(readOnly = false)
	public int close(Long loanId) {
		List<NfsLoanCollection> collectionList = nfsLoanCollectionDao.findCollections(loanId);
		if(collectionList.size()>0) {
			NfsLoanRecord loan = nfsLoanRecordService.get(loanId);
			NfsLoanCollection collection = collectionList.get(0);
			if(Status.auditing.equals(collection.getStatus())) {
				collection.setStatus(Status.fail);
				//催收失败log
					NfsLoanCollectionDetail nfsLoanCollectionDetail = new NfsLoanCollectionDetail();
					nfsLoanCollectionDetail.setCollectionId(collection.getId());
					nfsLoanCollectionDetail.setStatus(com.jxf.loan.entity.NfsLoanCollectionDetail.Status.collectionFailure);
					if(NfsLoanApply.RepayType.oneTimePrincipalAndInterest.equals(loan.getRepayType())) {
						nfsLoanCollectionDetail.setType(NfsLoanCollectionDetail.Type.fullAmount);
					}else {
						nfsLoanCollectionDetail.setType(NfsLoanCollectionDetail.Type.staging);
					}
					nfsLoanCollectionDetailService.save(nfsLoanCollectionDetail);
					BigDecimal fee = collection.getFee();
					Member member = memberService.get(collection.getLoan().getLoaner());
					nfsActService.updateAct(TrxRuleConstant.COLLECTION_FAILURE_REFUND, fee, member, collection.getId());
					save(collection);
					return 1;
			}
		}else {
			return 0;
		}
		return 0;
	}

	@Override
	@Transactional(readOnly = false)
	public int payForApplyCollection(Member member, NfsLoanCollection collection, NfsLoanCollectionDetail collectionDetail) {
		NfsLoanRecord loanRecord = collection.getLoan();
		nfsLoanRecordService.save(loanRecord);
		save(collection);
		collectionDetail.setCollectionId(collection.getId());
		nfsLoanCollectionDetailService.save(collectionDetail);
		int code = nfsActService.updateAct(TrxRuleConstant.COLLECTION_PREPAY, collection.getFee(), member, collection.getId());
		if(code == Constant.UPDATE_FAILED) {
			throw new RuntimeException("会员"+ member.getId() +"在为借条"+ loanRecord.getId()+"催收预缴费支付时扣款失败") ;
		}
		return Constant.UPDATE_SUCCESS;
	}
}