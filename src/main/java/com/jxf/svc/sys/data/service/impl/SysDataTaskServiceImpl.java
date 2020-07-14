package com.jxf.svc.sys.data.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.sys.data.dao.SysDataTaskDao;
import com.jxf.svc.sys.data.entity.SysDataTask;
import com.jxf.svc.sys.data.service.SysDataTaskService;

/**
 * 批次任务ServiceImpl
 * @author wo
 * @version 2019-01-12
 */
@Service("sysDataTaskService")
@Transactional(readOnly = true)
public class SysDataTaskServiceImpl extends CrudServiceImpl<SysDataTaskDao, SysDataTask> implements SysDataTaskService{

	public SysDataTask get(Long id) {
		return super.get(id);
	}
	
	public List<SysDataTask> findList(SysDataTask sysDataTask) {
		return super.findList(sysDataTask);
	}
	
	public Page<SysDataTask> findPage(Page<SysDataTask> page, SysDataTask sysDataTask) {
		page.setPageSize(10);
		return super.findPage(page, sysDataTask);
	}
	
	@Transactional(readOnly = false)
	public void save(SysDataTask sysDataTask) {
		super.save(sysDataTask);
	}
	
	@Transactional(readOnly = false)
	public void delete(SysDataTask sysDataTask) {
		super.delete(sysDataTask);
	}
	
}