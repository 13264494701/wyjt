package com.jxf.loan.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.loan.dao.NfsLoanPreservationDao;
import com.jxf.loan.entity.NfsLoanPreservation;
import com.jxf.loan.service.NfsLoanPreservationService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
/**
 * 业务保全ServiceImpl
 * @author SuHuimin
 * @version 2019-07-01
 */
@Service("nfsLoanPreservationService")
@Transactional(readOnly = true)
public class NfsLoanPreservationServiceImpl extends CrudServiceImpl<NfsLoanPreservationDao, NfsLoanPreservation> implements NfsLoanPreservationService{
	@Autowired
	private NfsLoanPreservationDao loanPreservationDao;
	
	
    @Override
	public NfsLoanPreservation get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<NfsLoanPreservation> findList(NfsLoanPreservation nfsLoanPreservation) {
		return super.findList(nfsLoanPreservation);
	}
	
	@Override
	public Page<NfsLoanPreservation> findPage(Page<NfsLoanPreservation> page, NfsLoanPreservation nfsLoanPreservation) {
		return super.findPage(page, nfsLoanPreservation);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(NfsLoanPreservation nfsLoanPreservation) {
		super.save(nfsLoanPreservation);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(NfsLoanPreservation nfsLoanPreservation) {
		super.delete(nfsLoanPreservation);
	}

	@Override
	public NfsLoanPreservation getPreservationByMemberId(Long memberId) {
		return loanPreservationDao.getPreservationByMemberId(memberId);
	}
	
}