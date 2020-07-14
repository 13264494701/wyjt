package com.jxf.ufang.service;

import java.math.BigDecimal;

import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.entity.UfangUserAct;

/**
 * 机构账户Service
 * @author jinxinfu
 * @version 2018-06-29
 */
public interface UfangUserActService extends CrudService<UfangUserAct> {

	UfangUserAct getUserAct(UfangUser user,String subNo);
	
	/**
	 *   更新优放用户账户- 乐观锁重试机制
	 * @param brn
	 * @param trxAmt
	 * @return
	 */
	int updateUfangUserAct(UfangUserAct ufangUserAct,BigDecimal trxAmt);
}