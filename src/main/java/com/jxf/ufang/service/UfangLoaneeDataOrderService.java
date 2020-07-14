package com.jxf.ufang.service;

import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.ufang.entity.UfangLoaneeDataOrder;
import com.jxf.ufang.entity.UfangUser;

import java.util.List;
import java.util.Map;

/**
 * 流量订单Service
 * @author wo
 * @version 2018-11-24
 */
public interface UfangLoaneeDataOrderService extends CrudService<UfangLoaneeDataOrder> {

	
    List<UfangLoaneeDataOrder> findListByEmpNo(UfangLoaneeDataOrder ufangLoaneeDataOrder);
    
    List<UfangLoaneeDataOrder> findListByCompanyNo(UfangLoaneeDataOrder ufangLoaneeDataOrder);
    
    Map<String,String> countByEmpNo(UfangUser user,int daysAgo);
	
}