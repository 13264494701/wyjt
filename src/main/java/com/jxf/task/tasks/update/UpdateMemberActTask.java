package com.jxf.task.tasks.update;



import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.jxf.svc.sys.data.entity.SysDataImport;
import com.jxf.svc.sys.data.entity.SysDataTask;
import com.jxf.svc.sys.data.service.SysDataImportService;
import com.jxf.svc.sys.data.service.SysDataTaskService;
import com.jxf.transplantation.temp.member.UpdateMemberAct;




/**
 * 更新所有用户待收待还金额   
 *
 * @author xrd
 */
@DisallowConcurrentExecution
public class UpdateMemberActTask implements Job {


	@Autowired
	private SysDataImportService dataImportService;
	@Autowired
	private SysDataTaskService sysDataTaskService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

    	SysDataImport dataImport = dataImportService.getByHandler("updateMemberAct");
    	SysDataTask task = new SysDataTask();
    	task.setData(dataImport);
    	task.setStatus(SysDataTask.Status.waitDo);
    	task.setIsOn(false); 
    	List<SysDataTask> taskList = sysDataTaskService.findList(task);
    	//变更以前的用户的待收待还
    	for(SysDataTask dataTask:taskList) {
    		
    		dataTask = sysDataTaskService.get(dataTask);
    		if(dataTask==null||dataTask.getStatus().equals(SysDataTask.Status.closed)) {
    			continue;
    		}
    		
    		dataTask.setStatus(SysDataTask.Status.doing);
    		dataTask.setIsOn(true);   		
    		sysDataTaskService.save(dataTask);
    		Long startId = dataTask.getStartId();
    		Long endId =dataTask.getEndId();
    		int quantity = UpdateMemberAct.updateAct(startId, endId);    
    		dataTask.setQuantity(quantity);
    		dataTask.setStatus(SysDataTask.Status.closed);
    		dataTask.setIsOn(false);
    		sysDataTaskService.save(dataTask);
    		dataImport.setImportQuantity(dataImport.getImportQuantity()+quantity);
    		dataImportService.save(dataImport);
    	}
    	//变更现在的
    	UpdateMemberAct.updateAct(292706959524761600L,317702014182232064L);
    	
    }
    

}