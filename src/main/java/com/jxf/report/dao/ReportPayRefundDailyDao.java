package com.jxf.report.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;

import com.jxf.report.entity.ReportPayRefundDaily;
import com.jxf.svc.annotation.MyBatisDao;


/**
 * 缴费退费统计DAO接口
 * @author wo
 * @version 2019-02-28
 */
@MyBatisDao
public interface ReportPayRefundDailyDao extends CrudDao<ReportPayRefundDaily> {
	
	ReportPayRefundDaily payCount(int daysAgo);
	
	ReportPayRefundDaily refundCount(int daysAgo);
    
    int deleteByDate(String date);
}