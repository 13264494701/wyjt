package com.jxf.mem.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.dao.MemberCancellationDao;
import com.jxf.mem.entity.MemberCancellation;
import com.jxf.mem.service.MemberCancellationService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
/**
 * 会员注销申请ServiceImpl
 * @author SuHuimin
 * @version 2019-06-19
 */
@Service("memberCancellationService")
@Transactional(readOnly = true)
public class MemberCancellationServiceImpl extends CrudServiceImpl<MemberCancellationDao, MemberCancellation> implements MemberCancellationService{

    @Override
	public MemberCancellation get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<MemberCancellation> findList(MemberCancellation memberCancellation) {
		return super.findList(memberCancellation);
	}
	
	@Override
	public Page<MemberCancellation> findPage(Page<MemberCancellation> page, MemberCancellation memberCancellation) {
		return super.findPage(page, memberCancellation);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(MemberCancellation memberCancellation) {
		super.save(memberCancellation);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(MemberCancellation memberCancellation) {
		super.delete(memberCancellation);
	}
	
}