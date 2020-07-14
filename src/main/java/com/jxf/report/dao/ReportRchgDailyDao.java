package com.jxf.report.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.report.entity.ReportRchgDaily;
import com.jxf.svc.annotation.MyBatisDao;


/**
 * 充值统计DAO接口
 * @author wo
 * @version 2019-03-25
 */
@MyBatisDao
public interface ReportRchgDailyDao extends CrudDao<ReportRchgDaily> {
	
	ReportRchgDaily rchgCount(int daysAgo);
    
    int deleteByDate(String date);
}