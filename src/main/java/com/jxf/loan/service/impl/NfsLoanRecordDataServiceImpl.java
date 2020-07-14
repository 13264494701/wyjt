package com.jxf.loan.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.loan.dao.NfsLoanRecordDataDao;
import com.jxf.loan.entity.NfsLoanRecordData;
import com.jxf.loan.service.NfsLoanRecordDataService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 借条数据ServiceImpl
 * @author wo
 * @version 2019-04-28
 */
@Service("nfsLoanRecordDataService")
@Transactional(readOnly = true)
public class NfsLoanRecordDataServiceImpl extends CrudServiceImpl<NfsLoanRecordDataDao, NfsLoanRecordData> implements NfsLoanRecordDataService{

    @Override
	public NfsLoanRecordData get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<NfsLoanRecordData> findList(NfsLoanRecordData nfsLoanRecordData) {
		return super.findList(nfsLoanRecordData);
	}
	
	@Override
	public Page<NfsLoanRecordData> findPage(Page<NfsLoanRecordData> page, NfsLoanRecordData nfsLoanRecordData) {
		return super.findPage(page, nfsLoanRecordData);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(NfsLoanRecordData nfsLoanRecordData) {
		super.save(nfsLoanRecordData);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(NfsLoanRecordData nfsLoanRecordData) {
		super.delete(nfsLoanRecordData);
	}
	
}