package com.jxf.report.service;

import com.jxf.report.entity.ReportLoaneeDataDaily;
import com.jxf.svc.sys.crud.service.CrudService;


/**
 * 流量统计Service
 * @author wo
 * @version 2019-02-28
 */
public interface ReportLoaneeDataDailyService extends CrudService<ReportLoaneeDataDaily> {

	ReportLoaneeDataDaily dataCount(int daysAgo);
    
    int deleteByDate(String date);
	
}