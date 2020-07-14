package com.jxf.report.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.report.entity.ReportMemberActDaily;
import com.jxf.svc.annotation.MyBatisDao;


/**
 * 账户统计DAO接口
 * @author wo
 * @version 2019-02-22
 */
@MyBatisDao
public interface ReportMemberActDailyDao extends CrudDao<ReportMemberActDaily> {
	
    int deleteByDate(String date);
    
}