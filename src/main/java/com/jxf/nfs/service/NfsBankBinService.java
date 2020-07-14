package com.jxf.nfs.service;

import com.jxf.nfs.entity.NfsBankBin;
import com.jxf.svc.sys.crud.service.CrudService;


/**
 * 卡BINService
 * @author wo
 * @version 2018-09-29
 */
public interface NfsBankBinService extends CrudService<NfsBankBin> {

	NfsBankBin getByCardNo(String cardNo);
	
	Boolean checkCardNo(String cardNo);
}