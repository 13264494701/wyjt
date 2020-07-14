package com.jxf.loan.service;

import com.jxf.loan.entity.NfsLoanPreservation;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 业务保全Service
 * @author SuHuimin
 * @version 2019-07-01
 */
public interface NfsLoanPreservationService extends CrudService<NfsLoanPreservation> {

	NfsLoanPreservation getPreservationByMemberId(Long memberId);
	
}