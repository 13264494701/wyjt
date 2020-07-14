package com.jxf.ufang.service;



import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.ufang.entity.UfangLoaneeRc;


/**
 * 借款人风控数据Service
 * @author wo
 * @version 2019-04-28
 */
public interface UfangLoaneeRcService extends CrudService<UfangLoaneeRc> {

	UfangLoaneeRc getByPhoneNo(String phoneNo);
	
}