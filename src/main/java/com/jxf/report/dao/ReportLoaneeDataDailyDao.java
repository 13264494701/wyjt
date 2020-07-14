package com.jxf.report.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.report.entity.ReportLoaneeDataDaily;
import com.jxf.svc.annotation.MyBatisDao;

/**
 * 流量统计DAO接口
 * @author wo
 * @version 2019-02-28
 */
@MyBatisDao
public interface ReportLoaneeDataDailyDao extends CrudDao<ReportLoaneeDataDaily> {
	
	ReportLoaneeDataDaily dataCount(int daysAgo);
    
    int deleteByDate(String date);
    
}