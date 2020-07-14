package com.jxf.report.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.report.entity.ReportWdrlDaily;
import com.jxf.svc.annotation.MyBatisDao;


/**
 * 充值统计DAO接口
 * @author wo
 * @version 2019-03-25
 */
@MyBatisDao
public interface ReportWdrlDailyDao extends CrudDao<ReportWdrlDaily> {
	
	ReportWdrlDaily wdrlCount(int daysAgo);
    
    int deleteByDate(String date);
}