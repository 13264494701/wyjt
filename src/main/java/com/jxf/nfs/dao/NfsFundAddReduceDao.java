package com.jxf.nfs.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;

import java.util.List;

import com.jxf.nfs.entity.NfsFundAddReduce;
import com.jxf.svc.annotation.MyBatisDao;

/**
 * 会员加减款记录DAO接口
 * @author suHuimin
 * @version 2019-01-26
 */
@MyBatisDao
public interface NfsFundAddReduceDao extends CrudDao<NfsFundAddReduce> {
	/**
	 * 获取已审核记录列表
	 * @param nfsFundAddReduce
	 * @return
	 */
	List<NfsFundAddReduce> getCheckedList(NfsFundAddReduce nfsFundAddReduce);
	
}