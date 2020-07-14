package com.jxf.rc.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.svc.annotation.MyBatisDao;

import org.apache.ibatis.annotations.Param;

import com.jxf.rc.entity.RcCaSourceData;

/**
 * 信用报告原始数据表DAO接口
 * @author lmy
 * @version 2018-12-17
 */
@MyBatisDao
public interface RcCaSourceDataDao extends CrudDao<RcCaSourceData> {
	
	RcCaSourceData getSourceDataId(@Param("sourceDataId")String sourceDataId);
}