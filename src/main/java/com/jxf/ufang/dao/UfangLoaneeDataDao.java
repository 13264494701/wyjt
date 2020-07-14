package com.jxf.ufang.dao;

import java.util.List;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.ufang.entity.UfangLoaneeData;


/**
 * 流量管理DAO接口
 * @author wo
 * @version 2018-11-24
 */
@MyBatisDao
public interface UfangLoaneeDataDao extends CrudDao<UfangLoaneeData> {
	
	UfangLoaneeData findByPhoneNo(String phoneNo);
	
    int selectWeekUpdateCount(String phoneNo);
    
    int updatesales(Long id);
    
    List<UfangLoaneeData> findListByEmpNo(UfangLoaneeData ufangLoaneeData);

}