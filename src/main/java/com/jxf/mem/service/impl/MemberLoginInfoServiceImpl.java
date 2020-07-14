package com.jxf.mem.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.dao.MemberLoginInfoDao;
import com.jxf.mem.entity.MemberLoginInfo;
import com.jxf.mem.service.MemberLoginInfoService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
/**
 * 会员登陆信息ServiceImpl
 * @author gaobo
 * @version 2019-05-31
 */
@Service("memMemberLoginInfoService")
@Transactional(readOnly = true)
public class MemberLoginInfoServiceImpl extends CrudServiceImpl<MemberLoginInfoDao, MemberLoginInfo> implements MemberLoginInfoService{

	@Autowired
	private MemberLoginInfoDao memberLoginInfoDao;
	
    @Override
	public MemberLoginInfo get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<MemberLoginInfo> findList(MemberLoginInfo memberLoginInfo) {
		return super.findList(memberLoginInfo);
	}
	
	@Override
	public Page<MemberLoginInfo> findPage(Page<MemberLoginInfo> page, MemberLoginInfo memberLoginInfo) {
		return super.findPage(page, memberLoginInfo);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(MemberLoginInfo memberLoginInfo) {
		super.save(memberLoginInfo);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(MemberLoginInfo memberLoginInfo) {
		super.delete(memberLoginInfo);
	}

	@Override
	public MemberLoginInfo getLoginInfoByMemberId(Long memberId) {
		
		return memberLoginInfoDao.getLoginInfoByMemberId(memberId);
	}
	
}