package com.jxf.ufang.service;


import java.math.BigDecimal;

import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.ufang.entity.UfangBrn;
import com.jxf.ufang.entity.UfangBrnAct;


/**
 * 机构账户Service
 * @author jinxinfu
 * @version 2018-06-29
 */
public interface UfangBrnActService extends CrudService<UfangBrnAct> {

	UfangBrnAct getBrnAct(UfangBrn brn,String subNo);
	
	/**
	 *   更新ufang机构账户- 乐观锁重试机制
	 * @param brn
	 * @param trxAmt
	 * @return
	 */
	int updateUfangBrnAct(UfangBrnAct ufangBrnAct,BigDecimal trxAmt);
	
}