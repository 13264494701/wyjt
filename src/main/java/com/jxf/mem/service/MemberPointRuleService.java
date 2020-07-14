package com.jxf.mem.service;

import com.jxf.mem.entity.MemberPointRule;
import com.jxf.svc.sys.crud.service.CrudService;


/**
 * 积分规则Service
 * @author wo
 * @version 2018-08-12
 */
public interface MemberPointRuleService extends CrudService<MemberPointRule> {

	void enableRule (String id,String sts);
	
}