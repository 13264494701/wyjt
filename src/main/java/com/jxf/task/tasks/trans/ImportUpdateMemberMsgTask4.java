package com.jxf.task.tasks.trans;



import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jxf.transplantation.temp.message.ImportUpdateMessageUtils4;



/**
 * 导入好友数据
 *
 * @author wo
 */
@DisallowConcurrentExecution
public class ImportUpdateMemberMsgTask4 implements Job {


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

    	ImportUpdateMessageUtils4.importMessage();
   

    }
    

}