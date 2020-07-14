package com.jxf.nfs.service;

import java.math.BigDecimal;

import com.jxf.nfs.entity.NfsBrnAct;
import com.jxf.svc.sys.brn.entity.Brn;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 机构账户Service
 * @author jinxinfu
 * @version 2018-06-29
 */
public interface NfsBrnActService extends CrudService<NfsBrnAct> {

	NfsBrnAct getBrnAct(Brn brn,String subNo);
	
	int updateBrnActBal(NfsBrnAct nfsBrnAct,BigDecimal trxAmt);
	
}