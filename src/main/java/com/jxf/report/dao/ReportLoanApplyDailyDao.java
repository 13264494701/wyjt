package com.jxf.report.dao;

import com.jxf.report.entity.ReportLoanApplyDaily;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.svc.annotation.MyBatisDao;

/**
 * 申请统计DAO接口
 * @author XIAORONGDIAN
 * @version 2019-04-04
 */
@MyBatisDao
public interface ReportLoanApplyDailyDao extends CrudDao<ReportLoanApplyDaily> {

	/**
	 * 根据日期删除
	 * @param date
	 */
	void deleteByDate(String date);

	/***
	 * 统计最近几天的数据
	 * @param daysAgo
	 * @return
	 */
	ReportLoanApplyDaily applyCount(int daysAgo);
	
}