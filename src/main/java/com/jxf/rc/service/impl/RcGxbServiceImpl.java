package com.jxf.rc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.FileUtils;
import com.jxf.rc.entity.RcGxb;
import com.jxf.rc.dao.RcGxbDao;
import com.jxf.rc.service.RcGxbService;
/**
 * 风控 公信宝ServiceImpl
 * @author Administrator
 * @version 2018-10-16
 */
@Service("rcGxbService")
@Transactional(readOnly = true)
public class RcGxbServiceImpl extends CrudServiceImpl<RcGxbDao, RcGxb> implements RcGxbService{

	@Autowired
	private RcGxbDao rcGxbDao;

	public RcGxb get(Long id) {
		return super.get(id);
	}
	
	public List<RcGxb> findList(RcGxb rcGxb) {
		return super.findList(rcGxb);
	}
	
	public Page<RcGxb> findPage(Page<RcGxb> page, RcGxb rcGxb) {
		return super.findPage(page, rcGxb);
	}
	
	@Transactional(readOnly = false)
	public void save(RcGxb rcGxb) {
		super.save(rcGxb);
	}
	
	@Transactional(readOnly = false)
	public void delete(RcGxb rcGxb) {
		super.delete(rcGxb);
	}

	@Override
	public RcGxb findByToken(String token) {
		return rcGxbDao.findByToken(token);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void saveTaskData(RcGxb rcGxb) {

		if(rcGxb.getIsNewRecord()) {
			rcGxb.preInsert();
			String path = "/gxb/task_data/"+DateUtils.getDate("yyyy/MM/dd")+"/";
			String dir = Global.getConfig("uploadPath") + path ;
			FileUtils.createDirectory(dir);
			String taskDataFileName = rcGxb.getId()+".txt";
			String taskData = rcGxb.getTaskData();

			FileUtils.writeToFile(dir+taskDataFileName,taskData,false);
			String taskDataPath = path+taskDataFileName;
			rcGxb.setDataPath(taskDataPath);
			rcGxbDao.insert(rcGxb);
		}else {
			
			String path = "/gxb/task_data/"+DateUtils.getDate("yyyy/MM/dd")+"/";
			String dir = Global.getConfig("uploadPath") + path ;
			FileUtils.createDirectory(dir);
			String taskDataFileName = rcGxb.getId()+".txt";
			String taskData = rcGxb.getTaskData();

			FileUtils.writeToFile(dir+taskDataFileName,taskData,false);
			String taskDataPath = path+taskDataFileName;
			rcGxb.setDataPath(taskDataPath);
			
			rcGxb.preUpdate();
			rcGxbDao.update(rcGxb);
		}	
	}

	@Override
	@Transactional(readOnly = false)
	public void saveTaskReport(RcGxb rcGxb) {
		if(rcGxb.getIsNewRecord()) {
			rcGxb.preInsert();
			String path = "/gxb/report_data/"+DateUtils.getDate("yyyy/MM/dd")+"/";
			String dir = Global.getConfig("uploadPath") + path ;
			FileUtils.createDirectory(dir);
			String taskReportFileName = rcGxb.getId()+".txt";
			String taskReport = rcGxb.getReportData();

			FileUtils.writeToFile(dir+taskReportFileName,taskReport,false);
			String taskReportPath = path+taskReportFileName;
			rcGxb.setReportPath(taskReportPath);
			rcGxbDao.insert(rcGxb);
		}else {
			
			String path = "/gxb/report_data/"+DateUtils.getDate("yyyy/MM/dd")+"/";
			String dir = Global.getConfig("uploadPath") + path ;
			FileUtils.createDirectory(dir);
			String taskReportFileName = rcGxb.getId()+".txt";
			String taskReport = rcGxb.getReportData();

			FileUtils.writeToFile(dir+taskReportFileName,taskReport,false);
			String taskReportPath = path+taskReportFileName;
			rcGxb.setReportPath(taskReportPath);
			
			rcGxb.preUpdate();
			rcGxbDao.update(rcGxb);
		}
	}

	@Override
	public List<RcGxb> findListWithoutEmpNo(RcGxb rcGxb) {
		return rcGxbDao.findAllList(rcGxb);
	}
}