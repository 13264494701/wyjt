package com.jxf.mem.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.dao.MemberExtendDao;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberExtend;
import com.jxf.mem.service.MemberExtendService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
/**
 * 会员扩展信息ServiceImpl
 * @author XIAORONGDIAN
 * @version 2018-10-13
 */
@Service("memberExtendService")
@Transactional(readOnly = true)
public class MemberExtendServiceImpl extends CrudServiceImpl<MemberExtendDao, MemberExtend> implements MemberExtendService{

	@Autowired
	private MemberExtendDao memberExtendDao;
	
	@Override
	public MemberExtend get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<MemberExtend> findList(MemberExtend memberExtend) {
		return super.findList(memberExtend);
	}
	
	@Override
	public Page<MemberExtend> findPage(Page<MemberExtend> page, MemberExtend memberExtend) {
		return super.findPage(page, memberExtend);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(MemberExtend memberExtend) {
		super.save(memberExtend);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(MemberExtend memberExtend) {
		super.delete(memberExtend);
	}

	@Override
	public MemberExtend getByMember(Member member) {
		return memberExtendDao.getByMemberId(member.getId());
	}
	
}