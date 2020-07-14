package com.jxf.task.tasks;


import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.ufang.entity.UfangBrn;
import com.jxf.ufang.service.ReportUfangLoanDailyService;
import com.jxf.ufang.service.UfangBrnService;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 统计任务
 *
 * @author Administrator
 */
@DisallowConcurrentExecution
public class UfangReportTask implements Job {

	private static final Logger log = LoggerFactory.getLogger(UfangReportTask.class);
	
    @Autowired
    private ReportUfangLoanDailyService reportUfangLoanDailyService;
    @Autowired
    private UfangBrnService ufangBrnService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	//借条统计
    	UfangBrn ufangBrn = new UfangBrn();
    	ufangBrn.setGrade(2);
    	Date endTime = new Date();
    	String endDate = CalendarUtil.getDate(endTime);
    	try {
    		endTime = DateUtils.parse(endDate + " 00:00:02");
    	} catch (ParseException e) {
    		log.error("优放借条统计日期错误！异常：" + Exceptions.getStackTraceAsString(e));
    	}
    	List<UfangBrn> ufangBrns = ufangBrnService.findList(ufangBrn);
    	Date beginTime = CalendarUtil.addDay(endTime, -1);
       for (UfangBrn ufangBrn2 : ufangBrns) {
    	   reportUfangLoanDailyService.loanStatistics(ufangBrn2.getId(), beginTime, endTime);
       }

    }

}