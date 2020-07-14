package com.jxf.ufang.service;

import com.alibaba.fastjson.JSONObject;

import com.jxf.svc.model.HandleRsp;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.ufang.entity.UfangBrn;
import com.jxf.ufang.entity.UfangLoaneeRcOrder;
import com.jxf.ufang.entity.UfangUser;

/**
 * 风控订单Service
 * @author wo
 * @version 2019-07-14
 */
public interface UfangLoaneeRcOrderService extends CrudService<UfangLoaneeRcOrder> {

	/**
	 * 
	 * @param user
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Page<UfangLoaneeRcOrder> findMemberOrderPage(UfangUser user, UfangLoaneeRcOrder.Status status, Integer pageNo, Integer pageSize);
	
	
	HandleRsp queryRcData(UfangBrn brn,JSONObject dataJson);
	
}