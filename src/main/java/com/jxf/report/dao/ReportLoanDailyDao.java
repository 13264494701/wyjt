package com.jxf.report.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;

import java.util.List;

import com.jxf.report.entity.ReportLoanDaily;
import com.jxf.svc.annotation.MyBatisDao;


/**
 * 借条统计DAO接口
 * @author wo
 * @version 2019-02-28
 */
@MyBatisDao
public interface ReportLoanDailyDao extends CrudDao<ReportLoanDaily> {
	
	List<ReportLoanDaily> loanCount(int daysAgo);
    
    int deleteByDate(String date);
}