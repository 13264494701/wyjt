package com.jxf.report.service;


import com.jxf.report.entity.ReportWdrlDaily;
import com.jxf.svc.sys.crud.service.CrudService;


/**
 * 充值统计Service
 * @author wo
 * @version 2019-03-25
 */
public interface ReportWdrlDailyService extends CrudService<ReportWdrlDaily> {

	ReportWdrlDaily wdrlCount(int daysAgo);
    
    int deleteByDate(String date);
	
}