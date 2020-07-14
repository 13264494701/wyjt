package com.jxf.nfs.service;

import com.jxf.nfs.entity.NfsFundAddReduce;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 会员加减款记录Service
 * @author suHuimin
 * @version 2019-01-26
 */
public interface NfsFundAddReduceService extends CrudService<NfsFundAddReduce> {
	/**
	 * 获取已审核记录列表
	 * @param page
	 * @param fundAddReduce
	 * @return
	 */
	Page<NfsFundAddReduce> getCheckedList(Page<NfsFundAddReduce> page, NfsFundAddReduce fundAddReduce);
	/**
	 * 加减款审核
	 * @return
	 */
	int applyCheck(NfsFundAddReduce fundAddReduce);
}