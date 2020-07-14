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
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

import com.jxf.svc.utils.Exceptions;

import com.jxf.rc.entity.RcCaSourceData;
import com.jxf.rc.dao.RcCaSourceDataDao;
import com.jxf.rc.service.RcCaSourceDataService;
import com.jxf.rc.utils.ThirdPartyUtils;

/**
 * 信用报告原始数据表ServiceImpl
 * 
 * @author lmy
 * @version 2018-12-17
 */
@Service("rcCaSourceDataService")
@Transactional(readOnly = false)
public class RcCaSourceDataServiceImpl extends CrudServiceImpl<RcCaSourceDataDao, RcCaSourceData>
		implements RcCaSourceDataService {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	public RcCaSourceData get(Long id) {
		return super.get(id);
	}

	public List<RcCaSourceData> findList(RcCaSourceData rcCaSourceData) {
		
		Sort sort = new Sort(Sort.Direction.DESC, "createTime");
		Criteria criteria = Criteria.where("memberId").is(rcCaSourceData.getMemberId());
		if(rcCaSourceData.getType() != null){
			criteria.andOperator(Criteria.where("type").is(rcCaSourceData.getType()));
		}
		Query query = new Query().addCriteria(criteria);
		query.limit(30);
		List<RcCaSourceData> rcCaSourceDataList = mongoTemplate.find(query.with(sort), RcCaSourceData.class);
		
		if(rcCaSourceDataList != null && rcCaSourceDataList.size()>0) {
			return rcCaSourceDataList;
		}
		
		return super.findList(rcCaSourceData);
	}

	public Page<RcCaSourceData> findPage(Page<RcCaSourceData> page, RcCaSourceData rcCaSourceData) {
		return super.findPage(page, rcCaSourceData);
	}

	@Transactional(readOnly = false)
	public void save(RcCaSourceData rcCaSourceData) {
		super.save(rcCaSourceData);
	}

	@Transactional(readOnly = false)
	public void delete(RcCaSourceData rcCaSourceData) {
		super.delete(rcCaSourceData);
	}

	@Override
	public RcCaSourceData createShuJuMofangSouceData(RcCaSourceData rcCaSourceData) {

		rcCaSourceData.preInsert();
		rcCaSourceData.setStatus(RcCaSourceData.Status.notfinish_arrange);
		
		mongoTemplate.save(rcCaSourceData);
		
		return rcCaSourceData;
	}





	// 查询社保/公积金结果
	public String getsbInformation(String task_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpClient httpClient = new HttpClient();
		// 设置连接超时时间(单位毫秒)
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(60 * 1000);
		// 设置读取超时时间(单位毫秒)
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(60 * 1000);
		PostMethod postmethod = new PostMethod(ThirdPartyUtils.shebaoQueryUrl);
		postmethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		postmethod.addParameter("partner_code", Global.getConfig("partnerCode"));
		postmethod.addParameter("partner_key", Global.getConfig("partnerKey"));
		postmethod.addParameter("task_id", task_id);
		// 设置读取超时时间(单位毫秒)
		String responseString = "";
		int statusCode;
		try {
			statusCode = httpClient.executeMethod(postmethod);
			// 判断请求是否成功
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
	// 查询运营商
	public String getyyInformation(String task_id){
		Map<String, Object> map = new HashMap<String, Object>();
		HttpClient httpClient = new HttpClient();
		// 设置连接超时时间(单位毫秒)
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(60 * 1000);
		// 设置读取超时时间(单位毫秒)
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(60 * 1000);
		PostMethod postmethod = new PostMethod(ThirdPartyUtils.yunyingshangQueryUrl);
		postmethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		postmethod.addParameter("partner_code", Global.getConfig("partnerCode"));
		postmethod.addParameter("partner_key", Global.getConfig("partnerKey"));
		postmethod.addParameter("task_id", task_id);
		// 设置读取超时时间(单位毫秒)
		String responseString = "";
		int statusCode;
		try {
			statusCode = httpClient.executeMethod(postmethod);
			// 判断请求是否成功
			if (statusCode != HttpStatus.SC_OK) {
				map.put("errCode", statusCode);
				map.put("errString", "请求失败");
			} else {
				// 获取返回值
				responseString = postmethod.getResponseBodyAsString();
			}
		} catch (HttpException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		} catch (IOException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}

		return responseString;
	}

	// 淘宝
	public String gettbInformation(String task_id){
		Map<String, Object> map = new HashMap<String, Object>();
		HttpClient httpClient = new HttpClient();
		// 设置连接超时时间(单位毫秒)
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(60 * 1000);
		// 设置读取超时时间(单位毫秒)
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(60 * 1000);
		PostMethod postmethod = new PostMethod(ThirdPartyUtils.taobaoQueryUrl);
		postmethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		postmethod.addParameter("partner_code", Global.getConfig("partnerCode"));
		postmethod.addParameter("partner_key", Global.getConfig("partnerKey"));
		postmethod.addParameter("task_id", task_id);
		// 设置读取超时时间(单位毫秒)
		String responseString = "";
		int statusCode;
		try {
			statusCode = httpClient.executeMethod(postmethod);
			// 判断请求是否成功
			if (statusCode != HttpStatus.SC_OK) {
				map.put("errCode", statusCode);
				map.put("errString", "请求失败");
			} else {
				// 获取返回值
				responseString = postmethod.getResponseBodyAsString();
			}
		} catch (HttpException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		} catch (IOException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}

		return responseString;
	}

	// 网银获取数据
		public String getwyInformation(String task_id){
			Map<String, Object> map = new HashMap<String, Object>();
			HttpClient httpClient = new HttpClient();
			// 设置连接超时时间(单位毫秒)
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(60 * 1000);
			// 设置读取超时时间(单位毫秒)
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(60 * 1000);
			PostMethod postmethod = new PostMethod(ThirdPartyUtils.wangyinQueryUrl);
			postmethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			postmethod.addParameter("partner_code", Global.getConfig("partnerCode"));
			postmethod.addParameter("partner_key", Global.getConfig("partnerKey"));
			postmethod.addParameter("task_id", task_id);
			// 设置读取超时时间(单位毫秒)
			String responseString = "";
			int statusCode;
			try {
				statusCode = httpClient.executeMethod(postmethod);
				// 判断请求是否成功
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

	// 查询运营商报告
	public String getyyBgInformation(String task_id){
		Map<String, Object> map = new HashMap<String, Object>();
		HttpClient httpClient = new HttpClient();
		// 设置连接超时时间(单位毫秒)
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(60 * 1000);
		// 设置读取超时时间(单位毫秒)
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(60 * 1000);
		PostMethod postmethod = new PostMethod(ThirdPartyUtils.yunyingshangReportQueryUrl);
		postmethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		
		postmethod.addParameter("partner_code", Global.getConfig("partnerCode"));
		postmethod.addParameter("partner_key", Global.getConfig("partnerKey"));
		
		postmethod.addParameter("task_id", task_id);
		// 设置读取超时时间(单位毫秒)
		String responseString = "";
		int statusCode;
		try {
			statusCode = httpClient.executeMethod(postmethod);
			// 判断请求是否成功
			if (statusCode != HttpStatus.SC_OK) {
				map.put("errCode", statusCode);
				map.put("errString", "请求失败");
			} else {
				// 获取返回值
				responseString = postmethod.getResponseBodyAsString();
			}
		} catch (HttpException e) {
			logger.error("task_id:{}查询数据异常：{}",task_id,Exceptions.getStackTraceAsString(e));
		} catch (IOException e) {
			logger.error("task_id:{}查询数据异常：{}",task_id,Exceptions.getStackTraceAsString(e));
		}
		return responseString;
	}


	// 京东获取数据
		public String getjdInformation(String task_id) {
			Map<String, Object> map = new HashMap<String, Object>();
			HttpClient httpClient = new HttpClient();
			// 设置连接超时时间(单位毫秒)
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(60 * 1000);
			// 设置读取超时时间(单位毫秒)
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(60 * 1000);
			PostMethod postmethod = new PostMethod(ThirdPartyUtils.jingdongQueryUrl);
			postmethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			postmethod.addParameter("partner_code", Global.getConfig("partnerCode"));
			postmethod.addParameter("partner_key", Global.getConfig("partnerKey"));
			postmethod.addParameter("task_id", task_id);
			// 设置读取超时时间(单位毫秒)
			String responseString = "";
			int statusCode;
			try {
				statusCode = httpClient.executeMethod(postmethod);
				// 判断请求是否成功
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

}