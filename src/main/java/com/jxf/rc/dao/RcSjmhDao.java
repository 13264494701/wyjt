package com.jxf.rc.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

import java.util.List;

import com.jxf.rc.entity.RcSjmh;


/**
 * 风控 数据魔盒DAO接口
 * @author Administrator
 * @version 2018-10-16
 */
@MyBatisDao
public interface RcSjmhDao extends CrudDao<RcSjmh> {

    RcSjmh findByOrgId(String orgId);
    
    RcSjmh findByTaskId(String taskId);
    
    RcSjmh findByPhoneNoChannelTypeDataStatus(RcSjmh rcSjmh);
    
    RcSjmh findByPhoneNoChannelTypeReportStatus(RcSjmh rcSjmh);
    
    List <RcSjmh> findListByEmpNo(RcSjmh rcSjmh);
	
}