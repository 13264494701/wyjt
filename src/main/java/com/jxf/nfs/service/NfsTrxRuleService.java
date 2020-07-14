package com.jxf.nfs.service;

import com.jxf.nfs.entity.NfsTrxRule;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 业务规则Service
 * @author XIAORONGDIAN
 * @version 2018-09-10
 */
public interface NfsTrxRuleService extends CrudService<NfsTrxRule> {

	NfsTrxRule getByTrxCode(String trxCode);
	
}