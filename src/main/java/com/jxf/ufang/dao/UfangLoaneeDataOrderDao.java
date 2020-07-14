package com.jxf.ufang.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.ufang.entity.UfangLoaneeDataOrder;

import java.util.List;

/**
 * 流量订单DAO接口
 * @author wo
 * @version 2018-11-24
 */
@MyBatisDao
public interface UfangLoaneeDataOrderDao extends CrudDao<UfangLoaneeDataOrder> {

    List<UfangLoaneeDataOrder> findListByEmpNo(UfangLoaneeDataOrder ufangLoaneeDataOrder);
    List<UfangLoaneeDataOrder> findListByCompanyNo(UfangLoaneeDataOrder ufangLoaneeDataOrder);
	
}