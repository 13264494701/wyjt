package com.jxf.loan.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.loan.entity.NfsLoanArbitrationExecutionDetail;
import com.jxf.loan.dao.NfsLoanArbitrationExecutionDetailDao;
import com.jxf.loan.service.NfsLoanArbitrationExecutionDetailService;
/**
 * 强执明细ServiceImpl
 * @author Administrator
 * @version 2018-12-27
 */
@Service("nfsLoanArbitrationExecutionDetailService")
@Transactional(readOnly = true)
public class NfsLoanArbitrationExecutionDetailServiceImpl extends CrudServiceImpl<NfsLoanArbitrationExecutionDetailDao, NfsLoanArbitrationExecutionDetail> implements NfsLoanArbitrationExecutionDetailService{

	public NfsLoanArbitrationExecutionDetail get(Long id) {
		return super.get(id);
	}
	
	public List<NfsLoanArbitrationExecutionDetail> findList(NfsLoanArbitrationExecutionDetail nfsLoanArbitrationExecutionDetail) {
		return super.findList(nfsLoanArbitrationExecutionDetail);
	}
	
	public Page<NfsLoanArbitrationExecutionDetail> findPage(Page<NfsLoanArbitrationExecutionDetail> page, NfsLoanArbitrationExecutionDetail nfsLoanArbitrationExecutionDetail) {
		return super.findPage(page, nfsLoanArbitrationExecutionDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsLoanArbitrationExecutionDetail nfsLoanArbitrationExecutionDetail) {
		super.save(nfsLoanArbitrationExecutionDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsLoanArbitrationExecutionDetail nfsLoanArbitrationExecutionDetail) {
		super.delete(nfsLoanArbitrationExecutionDetail);
	}
	
}