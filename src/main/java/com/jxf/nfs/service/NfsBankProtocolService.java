package com.jxf.nfs.service;

import com.jxf.nfs.entity.NfsBankProtocol;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 签约协议Service
 * @author suhuimin
 * @version 2018-09-30
 */
public interface NfsBankProtocolService extends CrudService<NfsBankProtocol> {


	NfsBankProtocol getByMember(Long memberId);

	
	
}