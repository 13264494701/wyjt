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
import com.jxf.rc.entity.RcTxxy;
import com.jxf.rc.dao.RcTxxyDao;
import com.jxf.rc.service.RcTxxyService;
/**
 * 风控 天下信用ServiceImpl
 * @author wo
 * @version 2019-6-16
 */
@Service("rcTxxyService")
@Transactional(readOnly = true)
public class RcTxxyServiceImpl extends CrudServiceImpl<RcTxxyDao, RcTxxy> implements RcTxxyService{

	@Autowired
	private RcTxxyDao rcTxxyDao;

	public RcTxxy get(Long id) {
		return super.get(id);
	}
	
	public List<RcTxxy> findList(RcTxxy rcTxxy) {
		return super.findList(rcTxxy);
	}
	
	public Page<RcTxxy> findPage(Page<RcTxxy> page, RcTxxy rcTxxy) {
		return super.findPage(page, rcTxxy);
	}
	
	@Transactional(readOnly = false)
	public void save(RcTxxy rcTxxy) {
		
		if(rcTxxy.getIsNewRecord()) {
			rcTxxy.preInsert();
			rcTxxyDao.insert(rcTxxy);
		}else {
			rcTxxy.preUpdate();
			rcTxxyDao.update(rcTxxy);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(RcTxxy rcTxxy) {
		super.delete(rcTxxy);
	}
	@Override
	public RcTxxy findByReportNo(String reportNo) {

		return rcTxxyDao.findByReportNo(reportNo);
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
	public void saveTaskReport(RcTxxy rcTxxy) {

		if(rcTxxy.getIsNewRecord()) {
			rcTxxy.preInsert();
			String path = "/txxy/report_data/"+DateUtils.getDate("yyyy/MM/dd")+"/";
			String dir = Global.getConfig("uploadPath") + path ;
			FileUtils.createDirectory(dir);
			String taskReportFileName = rcTxxy.getId()+".txt";
			String taskReport = rcTxxy.getReportData();

			FileUtils.writeToFile(dir+taskReportFileName,taskReport,false);
			String taskReportPath = path+taskReportFileName;
			rcTxxy.setReportPath(taskReportPath);
			rcTxxyDao.insert(rcTxxy);
		}else {
			
			String path = "/txxy/report_data/"+DateUtils.getDate("yyyy/MM/dd")+"/";
			String dir = Global.getConfig("uploadPath") + path ;
			FileUtils.createDirectory(dir);
			String taskReportFileName = rcTxxy.getId()+".txt";
			String taskReport = rcTxxy.getReportData();

			FileUtils.writeToFile(dir+taskReportFileName,taskReport,false);
			String taskReportPath = path+taskReportFileName;
			rcTxxy.setReportPath(taskReportPath);
			
			rcTxxy.preUpdate();
			rcTxxyDao.update(rcTxxy);
		}
		
	}






}