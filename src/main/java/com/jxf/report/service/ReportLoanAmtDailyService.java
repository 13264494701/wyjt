package com.jxf.report.service;

import com.jxf.report.entity.ReportLoanAmtDaily;


import com.jxf.svc.sys.crud.service.CrudService;


/**
 * 借条金额统计Service
 * @author wo
 * @version 2019-07-10
 */
public interface ReportLoanAmtDailyService extends CrudService<ReportLoanAmtDaily> {

	ReportLoanAmtDaily loanAmtCount(int daysAgo);
    
    int deleteByDate(String date);
	
}