package com.jxf.rc.service;

import com.jxf.rc.entity.RcQuota;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 会员额度评估Service
 * @author SuHuimin
 * @version 2019-08-23
 */
public interface RcQuotaService extends CrudService<RcQuota> {
	
	RcQuota getByMemberId(Long memberId);
	RcQuota getByPhoneNo(String phoneNo);
	
}