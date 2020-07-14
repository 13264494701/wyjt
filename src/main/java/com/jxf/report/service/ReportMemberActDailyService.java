package com.jxf.report.service;

import com.jxf.report.entity.ReportMemberActDaily;
import com.jxf.svc.sys.crud.service.CrudService;


/**
 * 账户统计Service
 * @author wo
 * @version 2019-02-22
 */
public interface ReportMemberActDailyService extends CrudService<ReportMemberActDaily> {

	ReportMemberActDaily sumMemberAct(ReportMemberActDaily memberActDaily);
	
    int deleteByDate(String date);
	
}