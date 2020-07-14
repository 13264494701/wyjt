package com.jxf.task.tasks.trans;

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
import com.jxf.transplantation.temp.creditArchives.ImportCaUtils;



@DisallowConcurrentExecution
public class ImportCaTask implements Job {

	
	@Autowired
	private SysDataImportService dataImportService;
	@Autowired
	private SysDataTaskService sysDataTaskService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {    	
    	
    	importSouceCreditArchives();
    	importCreditArchives();
    	importXinYanCreditArchives();

    }
    
    private void importSouceCreditArchives() {
    	// 原始数据
    	SysDataImport dataImport = dataImportService.getByHandler("souceca");
    	SysDataTask task = new SysDataTask();
    	task.setData(dataImport);
    	task.setStatus(SysDataTask.Status.waitDo);
    	task.setIsOn(false); 
    	List<SysDataTask> taskList = sysDataTaskService.findList(task);
    	
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
    		int quantity = ImportCaUtils.importSouceCreditArchives(startId,endId);    		
    		dataTask.setQuantity(quantity);
    		dataTask.setStatus(SysDataTask.Status.closed);
    		dataTask.setIsOn(false);
    		sysDataTaskService.save(dataTask); 		
    		dataImport.setImportQuantity(dataImport.getImportQuantity()+quantity);
    		dataImportService.save(dataImport);
    	}
    }
    
    private void importCreditArchives() {
    	// 原始数据
    	SysDataImport dataImport = dataImportService.getByHandler("ca");
    	SysDataTask task = new SysDataTask();
    	task.setData(dataImport);
    	task.setStatus(SysDataTask.Status.waitDo);
    	task.setIsOn(false); 
    	List<SysDataTask> taskList = sysDataTaskService.findList(task);
    	
    	for(SysDataTask dataTask:taskList) {

    		dataTask.setStatus(SysDataTask.Status.doing);
    		dataTask.setIsOn(true);   		
    		sysDataTaskService.save(dataTask);
    		Long startId = dataTask.getStartId();
    		Long endId =dataTask.getEndId();
    		int quantity = ImportCaUtils.importCreditArchives(startId,endId);    		
    		dataTask.setQuantity(quantity);
    		dataTask.setStatus(SysDataTask.Status.closed);
    		dataTask.setIsOn(false);
    		sysDataTaskService.save(dataTask); 		
    		dataImport.setImportQuantity(dataImport.getImportQuantity()+quantity);
    		dataImportService.save(dataImport);
    	}
    }
    
    private void importXinYanCreditArchives() {
    	// 原始数据
    	SysDataImport dataImport = dataImportService.getByHandler("xinyanca");
    	SysDataTask task = new SysDataTask();
    	task.setData(dataImport);
    	task.setStatus(SysDataTask.Status.waitDo);
    	task.setIsOn(false); 
    	List<SysDataTask> taskList = sysDataTaskService.findList(task);
    	
    	for(SysDataTask dataTask:taskList) {

    		dataTask.setStatus(SysDataTask.Status.doing);
    		dataTask.setIsOn(true);   		
    		sysDataTaskService.save(dataTask);
    		Long startId = dataTask.getStartId();
    		Long endId =dataTask.getEndId();
    		int quantity = ImportCaUtils.importXinYanCreditArchives(startId,endId);    		
    		dataTask.setQuantity(quantity);
    		dataTask.setStatus(SysDataTask.Status.closed);
    		dataTask.setIsOn(false);
    		sysDataTaskService.save(dataTask); 		
    		dataImport.setImportQuantity(dataImport.getImportQuantity()+quantity);
    		dataImportService.save(dataImport);
    	}
    }
    
}
