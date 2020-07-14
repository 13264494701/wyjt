package com.jxf.loan.service;

import java.util.Date;
import java.util.List;



import com.jxf.loan.entity.NfsLoanOperatingRecord;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 借条操作记录Service
 * @author XIAORONGDIAN
 * @version 2018-12-18
 */
public interface NfsLoanOperatingRecordService extends CrudService<NfsLoanOperatingRecord> {

	
	List<NfsLoanOperatingRecord> findBorrowDelyList(NfsLoanOperatingRecord nfsLoanOperatingRecord, Date nowDate, int timeType);
	
}