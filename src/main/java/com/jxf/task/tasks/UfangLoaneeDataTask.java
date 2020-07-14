package com.jxf.task.tasks;




import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.transplantation.temp.UpdateUfangLoaneeDataUtils;


/**
 * 处理重复流量数据
 *
 * @author wo
 */
@DisallowConcurrentExecution
public class UfangLoaneeDataTask implements Job {
	
	private static final Logger logger = LoggerFactory.getLogger(UfangLoaneeDataTask.class);
	


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
		//处理重复流量数据	
    	UpdateUfangLoaneeDataUtils.updateLoaneeData();

    }

}