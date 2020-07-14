package com.jxf.task.tasks.update;



import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jxf.transplantation.temp.member.UpdateMemberVideoVerify;



/**
 * 更新会员消息 新增字段
 *
 * @author xrd
 */
@DisallowConcurrentExecution
public class UpdateMemberVideoVerifyTask implements Job {


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
	
    	UpdateMemberVideoVerify task = new UpdateMemberVideoVerify();
    	task.updateMemberVideoVerify();
    		
    }
    

}