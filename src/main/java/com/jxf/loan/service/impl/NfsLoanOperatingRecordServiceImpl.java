package com.jxf.loan.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.loan.dao.NfsLoanOperatingRecordDao;
import com.jxf.loan.entity.NfsLoanOperatingRecord;
import com.jxf.loan.service.NfsLoanOperatingRecordService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
/**
 * 借条操作记录ServiceImpl
 * @author XIAORONGDIAN
 * @version 2018-12-18
 */
@Service("nfsLoanOperatingRecordService")
@Transactional(readOnly = true)
public class NfsLoanOperatingRecordServiceImpl extends CrudServiceImpl<NfsLoanOperatingRecordDao, NfsLoanOperatingRecord> implements NfsLoanOperatingRecordService{

	@Autowired
	private NfsLoanOperatingRecordDao loanOperatingRecordDao;
	
	public NfsLoanOperatingRecord get(Long id) {
		return super.get(id);
	}
	
	public List<NfsLoanOperatingRecord> findList(NfsLoanOperatingRecord nfsLoanOperatingRecord) {
		return super.findList(nfsLoanOperatingRecord);
	}
	
	public Page<NfsLoanOperatingRecord> findPage(Page<NfsLoanOperatingRecord> page, NfsLoanOperatingRecord nfsLoanOperatingRecord) {
		return super.findPage(page, nfsLoanOperatingRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsLoanOperatingRecord nfsLoanOperatingRecord) {
		super.save(nfsLoanOperatingRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsLoanOperatingRecord nfsLoanOperatingRecord) {
		super.delete(nfsLoanOperatingRecord);
	}

	@Override
	public List<NfsLoanOperatingRecord> findBorrowDelyList(NfsLoanOperatingRecord nfsLoanOperatingRecord, Date nowDate,
			int timeType) {

		return loanOperatingRecordDao.findBorrowDelyList(nfsLoanOperatingRecord, nowDate, timeType);
	}
	
}