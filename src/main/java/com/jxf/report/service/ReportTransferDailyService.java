package com.jxf.report.service;



import com.jxf.report.entity.ReportTransferDaily;
import com.jxf.svc.sys.crud.service.CrudService;


/**
 * 转账统计Service
 * @author wo
 * @version 2019-03-25
 */
public interface ReportTransferDailyService extends CrudService<ReportTransferDaily> {

	ReportTransferDaily transferCount(int daysAgo);
    
    int deleteByDate(String date);
	
}