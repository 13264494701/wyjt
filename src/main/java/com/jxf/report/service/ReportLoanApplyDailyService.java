package com.jxf.report.service;

import com.jxf.report.entity.ReportLoanApplyDaily;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 申请统计Service
 * @author XIAORONGDIAN
 * @version 2019-04-04
 */
public interface ReportLoanApplyDailyService extends CrudService<ReportLoanApplyDaily> {

	/**
	 * 根据日期删除
	 */
	void deleteByDate(String date);

	
	ReportLoanApplyDaily applyCount(int daysAgo);

	
	
}