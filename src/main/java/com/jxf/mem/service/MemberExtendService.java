package com.jxf.mem.service;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberExtend;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 会员扩展信息Service
 * @author XIAORONGDIAN
 * @version 2018-10-13
 */
public interface MemberExtendService extends CrudService<MemberExtend> {

	/**
	 * 根据memberId查扩展信息
	 * @param id
	 * @return
	 */
	MemberExtend getByMember(Member member);

	
	
}