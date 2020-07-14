package com.jxf.task.tasks.trans;



import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jxf.transplantation.temp.message.ImportUpdateMessageUtils6;



/**
 * 导入会员消息数据
 *
 * @author gaobo
 */
@DisallowConcurrentExecution
public class ImportUpdateMemberMsgTask6 implements Job {


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

    	ImportUpdateMessageUtils6.importMessage();
   

    }
    

}