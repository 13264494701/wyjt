package com.jxf.task.tasks.trans;



import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jxf.transplantation.temp.message.ImportUpdateMessageUtils3;



/**
 * 导入好友数据
 *
 * @author wo
 */
@DisallowConcurrentExecution
public class ImportUpdateMemberMsgTask3 implements Job {


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

    	ImportUpdateMessageUtils3.importMessage();
   

    }
    

}