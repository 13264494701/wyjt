package com.jxf.mem.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.dao.MemberPointRuleDao;
import com.jxf.mem.entity.MemberPointRule;
import com.jxf.mem.service.MemberPointRuleService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 积分规则ServiceImpl
 * @author wo
 * @version 2018-08-12
 */
@Service("memberPointRuleService")
@Transactional(readOnly = true)
public class MemberPointRuleServiceImpl extends CrudServiceImpl<MemberPointRuleDao, MemberPointRule> implements MemberPointRuleService{

	@Override
	public MemberPointRule get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<MemberPointRule> findList(MemberPointRule memberPointRule) {
		return super.findList(memberPointRule);
	}
	
	@Override
	public Page<MemberPointRule> findPage(Page<MemberPointRule> page, MemberPointRule memberPointRule) {
		return super.findPage(page, memberPointRule);
	}
	
	@Transactional(readOnly = false)
	public void save(MemberPointRule memberPointRule) {
		super.save(memberPointRule);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(MemberPointRule memberPointRule) {
		super.delete(memberPointRule);
	}

	@Override
	@Transactional(readOnly = false)
	public void enableRule(String id, String sts) {
		dao.enableRule(id, sts);		
	}
	
}