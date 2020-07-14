package com.jxf.report.service;


import com.jxf.report.entity.ReportRchgDaily;
import com.jxf.svc.sys.crud.service.CrudService;


/**
 * 充值统计Service
 * @author wo
 * @version 2019-03-25
 */
public interface ReportRchgDailyService extends CrudService<ReportRchgDaily> {

	ReportRchgDaily rchgCount(int daysAgo);
    
    int deleteByDate(String date);
	
}