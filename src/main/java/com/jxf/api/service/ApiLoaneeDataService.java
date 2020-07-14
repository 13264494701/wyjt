package com.jxf.api.service;

import com.jxf.api.entity.ApiLoaneeData;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 给第三方查询流量Service
 * @author XIAORONGDIAN
 * @version 2019-04-17
 */
public interface ApiLoaneeDataService extends CrudService<ApiLoaneeData> {

	/***
	 * 根据商户号查最近一次查询记录
	 * @param merchantNumber
	 * @return
	 */
	ApiLoaneeData getNearest(String merchantNumber);

	
	
}