package com.jxf.mem.service;

import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberPhonebook;

/**
 * 手机通讯录Service
 * @author wo
 * @version 2019-03-07
 */
public interface MemberPhonebookService extends CrudService<MemberPhonebook> {

	
	MemberPhonebook getByMember(Member member);
	
}