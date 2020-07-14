package com.jxf.loan.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.loan.entity.NfsLoanArbitrationDetail;
import com.jxf.loan.dao.NfsLoanArbitrationDetailDao;
import com.jxf.loan.service.NfsLoanArbitrationDetailService;
/**
 * 仲裁明细ServiceImpl
 * @author Administrator
 * @version 2018-12-24
 */
@Service("nfsLoanArbitrationDetailService")
@Transactional(readOnly = true)
public class NfsLoanArbitrationDetailServiceImpl extends CrudServiceImpl<NfsLoanArbitrationDetailDao, NfsLoanArbitrationDetail> implements NfsLoanArbitrationDetailService{

	public NfsLoanArbitrationDetail get(Long id) {
		return super.get(id);
	}
	
	public List<NfsLoanArbitrationDetail> findList(NfsLoanArbitrationDetail nfsLoanArbitrationDetail) {
		return super.findList(nfsLoanArbitrationDetail);
	}
	
	public Page<NfsLoanArbitrationDetail> findPage(Page<NfsLoanArbitrationDetail> page, NfsLoanArbitrationDetail nfsLoanArbitrationDetail) {
		return super.findPage(page, nfsLoanArbitrationDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsLoanArbitrationDetail nfsLoanArbitrationDetail) {
		super.save(nfsLoanArbitrationDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsLoanArbitrationDetail nfsLoanArbitrationDetail) {
		super.delete(nfsLoanArbitrationDetail);
	}
	
}