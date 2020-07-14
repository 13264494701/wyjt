package com.jxf.mem.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.dao.MemberFriendReportDao;
import com.jxf.mem.entity.MemberFriendReport;
import com.jxf.mem.service.MemberFriendReportService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
/**
 * 好友投诉ServiceImpl
 * @author gaobo
 * @version 2018-11-16
 */
@Service("memberFriendReportService")
@Transactional(readOnly = true)
public class MemberFriendReportServiceImpl extends CrudServiceImpl<MemberFriendReportDao, MemberFriendReport> implements MemberFriendReportService{

	@Override
	public MemberFriendReport get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<MemberFriendReport> findList(MemberFriendReport memberFriendReport) {
		return super.findList(memberFriendReport);
	}
	
	@Override
	public Page<MemberFriendReport> findPage(Page<MemberFriendReport> page, MemberFriendReport memberFriendReport) {
		return super.findPage(page, memberFriendReport);
	}
	
	@Override
	@Transactional(readOnly = false,rollbackFor=Exception.class)
	public void save(MemberFriendReport memberFriendReport) {
		super.save(memberFriendReport);
	}
	
	@Override
	@Transactional(readOnly = false,rollbackFor=Exception.class)
	public void delete(MemberFriendReport memberFriendReport) {
		super.delete(memberFriendReport);
	}
	
}