package com.jxf.ufang.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.ufang.entity.UfangFundAddReduce;

import java.util.List;

import com.jxf.svc.annotation.MyBatisDao;

/**
 * 优放机构加减款记录DAO接口
 * @author suHuimin
 * @version 2019-01-26
 */
@MyBatisDao
public interface UfangFundAddReduceDao extends CrudDao<UfangFundAddReduce> {
	/**
	 * 获取已审核记录列表
	 * @param ufangFundAddReduce
	 * @return
	 */
	List<UfangFundAddReduce> getCheckedList(UfangFundAddReduce ufangFundAddReduce);
}