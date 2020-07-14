package com.jxf.mem.service;

import com.jxf.mem.entity.MemberView;
import com.jxf.svc.sys.crud.service.CrudService;


/**
 * 会员视图Service
 * @author wo
 * @version 2019-01-20
 */
public interface MemberViewService extends CrudService<MemberView> {

	void refresh();
	
	void take(MemberView memberView);
	
	void reset(MemberView memberView);
	
}