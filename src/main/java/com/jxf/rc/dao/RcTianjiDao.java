package com.jxf.rc.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;

import java.util.List;

import com.jxf.rc.entity.RcTianji;
import com.jxf.svc.annotation.MyBatisDao;

/**
 * 天机DAO接口
 * @author suhuimin
 * @version 2019-07-31
 */
@MyBatisDao
public interface RcTianjiDao extends CrudDao<RcTianji> {
	RcTianji findByOrgId(String orgId);
	    
	RcTianji findByTaskId(String taskId);
	    
	RcTianji findByPhoneNoChannelTypeDataStatus(RcTianji rcTianji);
	    
	List <RcTianji> findListByEmpNo(RcTianji rcTianji);
}