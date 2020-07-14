package com.jxf.mem.service;


import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberPointDetail;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 积分明细Service
 * @author zhj
 * @version 2016-05-13
 */
public interface MemberPointDetailService extends CrudService<MemberPointDetail> {

	
	Page<MemberPointDetail> findMemberPointPage(Member member, Integer pageNo, Integer pageSize);

}