package com.jxf.report.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;

import com.jxf.report.entity.ReportLoanAmtDaily;
import com.jxf.svc.annotation.MyBatisDao;


/**
 * 借条金额统计DAO接口
 * @author wo
 * @version 2019-07-10
 */
@MyBatisDao
public interface ReportLoanAmtDailyDao extends CrudDao<ReportLoanAmtDaily> {
	
	ReportLoanAmtDaily loanAmtCount(int daysAgo);
    
    int deleteByDate(String date);
}