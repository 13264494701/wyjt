package com.jxf.rc.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.FileUtils;
import com.jxf.rc.entity.RcSjmh;
import com.jxf.rc.entity.RcSjmh.ChannelType;
import com.jxf.rc.entity.RcSjmh.DataStatus;
import com.jxf.rc.entity.RcSjmh.ReportStatus;
import com.jxf.rc.dao.RcSjmhDao;
import com.jxf.rc.service.RcSjmhService;
/**
 * 风控 数据魔盒ServiceImpl
 * @author Administrator
 * @version 2018-10-16
 */
@Service("rcSjmhService")
@Transactional(readOnly = true)
public class RcSjmhServiceImpl extends CrudServiceImpl<RcSjmhDao, RcSjmh> implements RcSjmhService{

	@Autowired
	private RcSjmhDao rcSjmhDao;

	public RcSjmh get(Long id) {
		return super.get(id);
	}
	
	public List<RcSjmh> findList(RcSjmh rcSjmh) {
		return super.findList(rcSjmh);
	}
	
	@Override
	public List<RcSjmh> findListByEmpNo(RcSjmh rcSjmh) {

		return rcSjmhDao.findListByEmpNo(rcSjmh);
	}
	
	public Page<RcSjmh> findPage(Page<RcSjmh> page, RcSjmh rcSjmh) {
		return super.findPage(page, rcSjmh);
	}
	
	public Page<RcSjmh> findPageByEmpNo(Page<RcSjmh> page, RcSjmh rcSjmh) {
		rcSjmh.setPage(page);
		page.setList(rcSjmhDao.findListByEmpNo(rcSjmh));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(RcSjmh rcSjmh) {
		
		if(rcSjmh.getIsNewRecord()) {
			rcSjmh.preInsert();
			if(rcSjmh.getProdType().equals(RcSjmh.ProdType.ufang)) {
				rcSjmh.setOrgId(rcSjmh.getId());
			}
			rcSjmhDao.insert(rcSjmh);
		}else {
			rcSjmh.preUpdate();
			rcSjmhDao.update(rcSjmh);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(RcSjmh rcSjmh) {
		super.delete(rcSjmh);
	}

	@Override
	public RcSjmh findByTaskId(String taskId) {
		return rcSjmhDao.findByTaskId(taskId);
	}

	public String getTaskReuslt(String task_id,String queryUrl) {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpClient httpClient = new HttpClient();
		// 设置连接超时时间(单位毫秒)
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(60 * 1000);
		// 设置读取超时时间(单位毫秒)
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(60 * 1000);
		PostMethod postmethod = new PostMethod(queryUrl);
		postmethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		postmethod.addParameter("partner_code", Global.getConfig("partnerCode"));
		postmethod.addParameter("partner_key", Global.getConfig("partnerKey"));
		postmethod.addParameter("task_id", task_id);
		// 设置读取超时时间(单位毫秒)
		String responseString = "";
		int statusCode;
		try {
			statusCode = httpClient.executeMethod(postmethod);
			if (statusCode != HttpStatus.SC_OK) {
				map.put("errCode", statusCode);
				map.put("errString", "请求失败");
			} else {
				// 获取返回值
				responseString = postmethod.getResponseBodyAsString();
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return responseString;
	}
	
	@Override
	@Transactional(readOnly = false)
	public void saveTaskData(RcSjmh rcSjmh) {

		if(rcSjmh.getIsNewRecord()) {
			rcSjmh.preInsert();
			String path = "/sjmh/task_data/"+DateUtils.getDate("yyyy/MM/dd")+"/";
			String dir = Global.getConfig("uploadPath") + path ;
			FileUtils.createDirectory(dir);
			String taskDataFileName = rcSjmh.getId()+".txt";
			String taskData = rcSjmh.getTaskData();

			FileUtils.writeToFile(dir+taskDataFileName,taskData,false);
			String taskDataPath = path+taskDataFileName;
			rcSjmh.setDataPath(taskDataPath);
			rcSjmhDao.insert(rcSjmh);
		}else {
			
			String path = "/sjmh/task_data/"+DateUtils.getDate("yyyy/MM/dd")+"/";
			String dir = Global.getConfig("uploadPath") + path ;
			FileUtils.createDirectory(dir);
			String taskDataFileName = rcSjmh.getId()+".txt";
			String taskData = rcSjmh.getTaskData();

			FileUtils.writeToFile(dir+taskDataFileName,taskData,false);
			String taskDataPath = path+taskDataFileName;
			rcSjmh.setDataPath(taskDataPath);
			
			rcSjmh.preUpdate();
			rcSjmhDao.update(rcSjmh);
		}	
	}
	
	@Override
	@Transactional(readOnly = false)
	public void saveTaskReport(RcSjmh rcSjmh) {

		if(rcSjmh.getIsNewRecord()) {
			rcSjmh.preInsert();
			String path = "/sjmh/report_data/"+DateUtils.getDate("yyyy/MM/dd")+"/";
			String dir = Global.getConfig("uploadPath") + path ;
			FileUtils.createDirectory(dir);
			String taskReportFileName = rcSjmh.getId()+".txt";
			String taskReport = rcSjmh.getReportData();

			FileUtils.writeToFile(dir+taskReportFileName,taskReport,false);
			String taskReportPath = path+taskReportFileName;
			rcSjmh.setReportPath(taskReportPath);
			rcSjmhDao.insert(rcSjmh);
		}else {
			
			String path = "/sjmh/report_data/"+DateUtils.getDate("yyyy/MM/dd")+"/";
			String dir = Global.getConfig("uploadPath") + path ;
			FileUtils.createDirectory(dir);
			String taskReportFileName = rcSjmh.getId()+".txt";
			String taskReport = rcSjmh.getReportData();

			FileUtils.writeToFile(dir+taskReportFileName,taskReport,false);
			String taskReportPath = path+taskReportFileName;
			rcSjmh.setReportPath(taskReportPath);
			
			rcSjmh.preUpdate();
			rcSjmhDao.update(rcSjmh);
		}
		
	}

	@Override
	public RcSjmh findByPhoneNoChannelTypeDataStatus(String phoneNo, ChannelType channelType,DataStatus dataStatus) {
		RcSjmh rcSjmh = new RcSjmh();
		rcSjmh.setPhoneNo(phoneNo);
		rcSjmh.setChannelType(channelType);
		rcSjmh.setDataStatus(dataStatus);
		return rcSjmhDao.findByPhoneNoChannelTypeDataStatus(rcSjmh);
	}
	
	@Override
	public RcSjmh findByPhoneNoChannelTypeReportStatus(String phoneNo, ChannelType channelType,ReportStatus reportStatus) {
		RcSjmh rcSjmh = new RcSjmh();
		rcSjmh.setPhoneNo(phoneNo);
		rcSjmh.setChannelType(channelType);
		rcSjmh.setReportStatus(reportStatus);
		return rcSjmhDao.findByPhoneNoChannelTypeReportStatus(rcSjmh);
	}

}