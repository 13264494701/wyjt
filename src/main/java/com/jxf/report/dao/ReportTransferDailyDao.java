package com.jxf.report.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.report.entity.ReportTransferDaily;
import com.jxf.svc.annotation.MyBatisDao;


/**
 * 转账统计DAO接口
 * @author wo
 * @version 2019-03-25
 */
@MyBatisDao
public interface ReportTransferDailyDao extends CrudDao<ReportTransferDaily> {
	
	ReportTransferDaily transferCount(int daysAgo);
    
    int deleteByDate(String date);
}