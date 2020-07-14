package com.jxf.nfs.service;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.nfs.entity.NfsBrnActTrx;
import com.jxf.svc.sys.crud.service.CrudService;


/**
 * 账户交易Service
 * @author jinxinfu
 * @version 2018-07-01
 */
public interface NfsBrnActTrxService extends CrudService<NfsBrnActTrx> {
	/**
	 * 根据原业务id查询账户流水
	 * @param businessId
	 * @return
	 */
	List<NfsBrnActTrx>  findListByBusinessId(@Param("businessId")Long businessId);
	
}