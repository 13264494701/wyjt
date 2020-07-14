package com.jxf.ufang.service;

import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.ufang.entity.UfangFundAddReduce;

/**
 * 优放机构加减款记录Service
 * @author suHuimin
 * @version 2019-01-26
 */
public interface UfangFundAddReduceService extends CrudService<UfangFundAddReduce> {
	/**
	 * 获取已审核记录列表
	 * @param ufangFundAddReduce
	 * @return
	 */
	Page<UfangFundAddReduce> getCheckedList(Page<UfangFundAddReduce> page,UfangFundAddReduce ufangFundAddReduce);
	/*
	 * 优放机构加减款审核
	 */
	int ufangBrnAddReduceApplyCheck(UfangFundAddReduce ufangFundAddReduce);
}