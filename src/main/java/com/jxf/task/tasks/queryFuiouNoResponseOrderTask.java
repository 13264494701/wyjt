package com.jxf.task.tasks;

import com.jxf.nfs.entity.NfsWdrlRecord;
import com.jxf.nfs.service.NfsWdrlRecordService;
import com.jxf.pwithdraw.service.FuiouWithdrawService;
import com.jxf.svc.utils.Exceptions;

import java.util.Calendar;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 连连提现请求定时任务
 *
 * @author suHuimin
 */
@DisallowConcurrentExecution
public class queryFuiouNoResponseOrderTask implements Job {
	private static final Logger logger = LoggerFactory.getLogger(queryFuiouNoResponseOrderTask.class);
    @Autowired
    private NfsWdrlRecordService wdrlRecordService;
    @Autowired
    private FuiouWithdrawService fuiouWithdrawService;
    
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
		
  
    	logger.info("提现-处理富友未返回结果订单");
		Calendar cal = Calendar.getInstance();
	 	cal.add(Calendar.DATE, -5);   
		NfsWdrlRecord nfsWdrlRecord = new NfsWdrlRecord();
		nfsWdrlRecord.setCreateTime(cal.getTime());
		nfsWdrlRecord.setStatus(NfsWdrlRecord.Status.submited);
		List<NfsWdrlRecord> list = wdrlRecordService.findListByStatus(nfsWdrlRecord);
		for(NfsWdrlRecord wdrlRecord :list){
				try{
					fuiouWithdrawService.queryFuiouPayment(wdrlRecord);
				}catch (Exception e){
					logger.error("提现记录ID: {}查询异常{}" , wdrlRecord.getId() , Exceptions.getStackTraceAsString(e));
				}
		}
	}
    

}