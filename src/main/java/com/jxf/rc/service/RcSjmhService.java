package com.jxf.rc.service;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;

import java.util.List;

import com.jxf.rc.entity.RcSjmh;
import com.jxf.rc.entity.RcSjmh.ChannelType;
import com.jxf.rc.entity.RcSjmh.DataStatus;
import com.jxf.rc.entity.RcSjmh.ReportStatus;

/**
 * 风控 数据魔盒Service
 * @author Administrator
 * @version 2018-10-16
 */
public interface RcSjmhService extends CrudService<RcSjmh> {

    RcSjmh findByTaskId(String taskId);
    
    RcSjmh findByPhoneNoChannelTypeDataStatus(String phoneNo,ChannelType channelType,DataStatus dataStatus);
    
    RcSjmh findByPhoneNoChannelTypeReportStatus(String phoneNo,ChannelType channelType,ReportStatus reportStatus);
    
    void saveTaskData(RcSjmh rcSjmh);
    
    void saveTaskReport(RcSjmh rcSjmh);
    
    
    List <RcSjmh> findListByEmpNo(RcSjmh rcSjmh);
    
    Page<RcSjmh> findPageByEmpNo(Page<RcSjmh> page, RcSjmh rcSjmh);
    
	/**
	 * 查询任务结果
	 * @param task_id
	 * @param queryUrl
	 * @return
	 * @throws Exception
	 */
	String getTaskReuslt(String task_id,String queryUrl);
	
}