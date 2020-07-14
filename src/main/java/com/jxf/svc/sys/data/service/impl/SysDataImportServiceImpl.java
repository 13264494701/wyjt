package com.jxf.svc.sys.data.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.sys.data.dao.SysDataImportDao;
import com.jxf.svc.sys.data.entity.SysDataImport;
import com.jxf.svc.sys.data.entity.SysDataTask;
import com.jxf.svc.sys.data.service.SysDataImportService;
import com.jxf.svc.sys.data.service.SysDataTaskService;


/**
 * 数据导入ServiceImpl
 * @author wo
 * @version 2019-01-13
 */
@Service("sysDataImportService")
@Transactional(readOnly = true)
public class SysDataImportServiceImpl extends CrudServiceImpl<SysDataImportDao, SysDataImport> implements SysDataImportService{

	@Autowired
	private SysDataImportDao dataImportDao;
	@Autowired
	private SysDataTaskService sysDataTaskService;
	
	public SysDataImport get(Long id) {
		return super.get(id);
	}
	
	public List<SysDataImport> findList(SysDataImport sysDataImport) {
		return super.findList(sysDataImport);
	}
	
	public Page<SysDataImport> findPage(Page<SysDataImport> page, SysDataImport sysDataImport) {
		return super.findPage(page, sysDataImport);
	}
	
	@Transactional(readOnly = false)
	public void save(SysDataImport sysDataImport) {
		super.save(sysDataImport);
	}
	
	@Transactional(readOnly = false)
	public void delete(SysDataImport sysDataImport) {
		super.delete(sysDataImport);
	}

	@Override
	@Transactional(readOnly = false)
	public void createTask(SysDataImport dataImport) {
		
		Integer p = dataImport.getpQuantity();
		Long times = (dataImport.getMaxId()-dataImport.getMinId())/p+1;
	   	SysDataTask task = new SysDataTask();

    	long i = 0;
    	long j = 1;
    	while(i<times) {
    		task.setIsNewRecord(true);
    		task.setData(dataImport);
    		task.setStartId(dataImport.getMinId()+i*p);
    		task.setEndId(dataImport.getMinId()+j*p-1);
    		task.setQuantity(0);
    		task.setStatus(SysDataTask.Status.waitDo);
    		task.setIsOn(false);   		
    		sysDataTaskService.save(task);
    		i++;
    		j++;
    	}
		
	}

	@Override
	public SysDataImport getByHandler(String handler) {

		return dataImportDao.getByHandler(handler);
	}

	@Override
	@Transactional(readOnly = false)
	public void clearTask(SysDataImport sysDataImport) {
		
    	SysDataTask task = new SysDataTask();
    	task.setData(sysDataImport);
    	task.setIsOn(false); 
    	List<SysDataTask> taskList = sysDataTaskService.findList(task);  	
    	for(SysDataTask dataTask:taskList) {
    		sysDataTaskService.delete(dataTask);
    	}
		
	}
	
}