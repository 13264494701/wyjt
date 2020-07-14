package com.jxf.ufang.service;

import java.util.Date;

import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.ufang.entity.ReportUfangLoanDaily;

/**
 * 借条统计Service
 * @author suHuimin
 * @version 2019-03-25
 */
public interface ReportUfangLoanDailyService extends CrudService<ReportUfangLoanDaily> {
	
	/**
	 * @description 优放机构借条统计
	 * @param brnId
	 * @param beginTime
	 * @param endTime
	 */
	boolean loanStatistics(Long brnId,Date beginTime,Date endTime);
	
}
