package com.jxf.task.tasks.update;



import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jxf.transplantation.temp.message.UpdateMessageUtils4;



/**
 * 更新会员消息 新增字段
 *
 * @author xrd
 */
@DisallowConcurrentExecution
public class UpdateMemberMessageTask implements Job {


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
	
    	UpdateMessageUtils4.importMessage("1", "340936284790984704");
    		
    }
    

}