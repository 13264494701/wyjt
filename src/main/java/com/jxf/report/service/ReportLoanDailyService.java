package com.jxf.report.service;

import java.util.List;

import com.jxf.report.entity.ReportLoanDaily;

import com.jxf.svc.sys.crud.service.CrudService;


/**
 * 借条统计Service
 * @author wo
 * @version 2019-02-28
 */
public interface ReportLoanDailyService extends CrudService<ReportLoanDaily> {

	List<ReportLoanDaily> loanCount(int daysAgo);
    
    int deleteByDate(String date);
	
}