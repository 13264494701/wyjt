package com.jxf.rc.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jxf.rc.entity.RcTianji;
import com.jxf.rc.entity.RcTianji.ChannelType;
import com.jxf.rc.entity.RcTianji.DataStatus;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 天机Service
 * @author suhuimin
 * @version 2019-07-31
 */
public interface RcTianjiService extends CrudService<RcTianji> {
	/**
	 * 抓取数据完成后获取原始数据
	 * @param searchId    数据查询Id  h5接入：回调参数中的search_id参数作为user_id sdk接入：回调参数中的session的值
	 * @param accountType 数据抓取类型
	 * @return
	 */
	JSONObject getCrawlData(String searchId, String accountType, String method,String  outUniqueId);
	
	/**
	 * 获取数据报告详情
	 * @param searchId    数据查询Id
	 * @param accountType 数据抓取类型
	 * @return
	 */
	JSONObject getReportDetail(String userId, String accountType, String outUniqueId);

	RcTianji findByTaskId(String taskId);

	RcTianji findByPhoneNoChannelTypeDataStatus(String phoneNo, ChannelType channelType, DataStatus dataStatus);

	void saveTaskData(RcTianji rcTianji);

	void saveTaskReport(RcTianji rcTianji);

	List<RcTianji> findListByEmpNo(RcTianji rcTianji);

	Page<RcTianji> findPageByEmpNo(Page<RcTianji> page, RcTianji rcTianji);

}