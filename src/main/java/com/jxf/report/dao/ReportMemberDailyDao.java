package com.jxf.report.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.report.entity.ReportMemberDaily;

/**
 * 用户统计DAO接口
 *
 * @author Administrator
 * @version 2018-09-06
 */
@MyBatisDao
public interface ReportMemberDailyDao extends CrudDao<ReportMemberDaily> {

    ReportMemberDaily memberCount(int daysAgo);
    
    int deleteByDate(String date);
}