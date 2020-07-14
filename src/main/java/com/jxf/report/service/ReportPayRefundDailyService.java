package com.jxf.report.service;


import com.jxf.report.entity.ReportPayRefundDaily;
import com.jxf.svc.sys.crud.service.CrudService;


/**
 * 缴费退费统计Service
 * @author wo
 * @version 2019-02-28
 */
public interface ReportPayRefundDailyService extends CrudService<ReportPayRefundDaily> {

	ReportPayRefundDaily payCount(int daysAgo);
	
	ReportPayRefundDaily refundCount(int daysAgo);
    
    int deleteByDate(String date);
	
}