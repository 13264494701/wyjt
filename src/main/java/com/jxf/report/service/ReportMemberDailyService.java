package com.jxf.report.service;

import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.report.entity.ReportMemberDaily;

/**
 * 用户统计Service
 *
 * @author Administrator
 * @version 2018-09-06
 */
public interface ReportMemberDailyService extends CrudService<ReportMemberDaily> {

    ReportMemberDaily memberCount(int daysAgo);
    
    int deleteByDate(String date);
}