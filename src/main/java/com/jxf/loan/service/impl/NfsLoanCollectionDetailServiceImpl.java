package com.jxf.loan.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.loan.entity.NfsLoanCollectionDetail;
import com.jxf.loan.dao.NfsLoanCollectionDetailDao;
import com.jxf.loan.service.NfsLoanCollectionDetailService;
/**
 * 催收明细ServiceImpl
 * @author Administrator
 * @version 2018-12-24
 */
@Service("nfsLoanCollectionDetailService")
@Transactional(readOnly = true)
public class NfsLoanCollectionDetailServiceImpl extends CrudServiceImpl<NfsLoanCollectionDetailDao, NfsLoanCollectionDetail> implements NfsLoanCollectionDetailService{

	public NfsLoanCollectionDetail get(Long id) {
		return super.get(id);
	}
	
	public List<NfsLoanCollectionDetail> findList(NfsLoanCollectionDetail nfsLoanCollectionDetail) {
		return super.findList(nfsLoanCollectionDetail);
	}
	
	public Page<NfsLoanCollectionDetail> findPage(Page<NfsLoanCollectionDetail> page, NfsLoanCollectionDetail nfsLoanCollectionDetail) {
		return super.findPage(page, nfsLoanCollectionDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsLoanCollectionDetail nfsLoanCollectionDetail) {
		super.save(nfsLoanCollectionDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsLoanCollectionDetail nfsLoanCollectionDetail) {
		super.delete(nfsLoanCollectionDetail);
	}
	
}