package com.jxf.mem.service;

import com.jxf.mem.entity.MemberLoginInfo;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 会员登陆信息Service
 * @author gaobo
 * @version 2019-05-31
 */
public interface MemberLoginInfoService extends CrudService<MemberLoginInfo> {

	MemberLoginInfo getLoginInfoByMemberId(Long memberId);

	
	
}