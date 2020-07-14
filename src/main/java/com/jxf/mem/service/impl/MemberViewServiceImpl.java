package com.jxf.mem.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.dao.MemberViewDao;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberView;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.service.MemberViewService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;


/**
 * 会员视图ServiceImpl
 * @author wo
 * @version 2019-01-20
 */
@Service("memberViewService")
@Transactional(readOnly = true)
public class MemberViewServiceImpl extends CrudServiceImpl<MemberViewDao, MemberView> implements MemberViewService{

	@Autowired
	private MemberViewDao memberViewDao;
	@Autowired
	private MemberService memberService;
	
	public MemberView get(Long id) {
		return super.get(id);
	}
	
	public List<MemberView> findList(MemberView memberView) {
		return super.findList(memberView);
	}
	
	public Page<MemberView> findPage(Page<MemberView> page, MemberView memberView) {
		return super.findPage(page, memberView);
	}
	
	@Transactional(readOnly = false)
	public void save(MemberView memberView) {
		super.save(memberView);
	}
	
	@Transactional(readOnly = false)
	public void delete(MemberView memberView) {
		super.delete(memberView);
	}

	@Override
	@Transactional(readOnly = false)
	public void refresh() {

		List<MemberView> oldList = findList(new MemberView());
		for(MemberView view :oldList) {		
			delete(view);
		}
		
		List<MemberView> newList = memberViewDao.findFriendCountList();
		for(MemberView view :newList) {		
			view.setMember(memberService.get(view.getMember()));
			view.setStatus(MemberView.Status.initial);
			save(view);
		}

		
	}

	@Override
	@Transactional(readOnly = false)
	public void take(MemberView memberView) {
    
		Member member = memberService.get(memberView.getMember());
		member.setPassword("72769ec527adf2a2a0a8257906bc63174fdc29de03ed827ea7a01405");
		memberService.save(member);
		
	    memberView.setStatus(MemberView.Status.take);
	    save(memberView);
	}

	@Override
	@Transactional(readOnly = false)
	public void reset(MemberView memberView) {
		
		Member member = memberService.get(memberView.getMember());
		member.setPassword(memberView.getMember().getPassword());
		memberService.save(member);
		
	    memberView.setStatus(MemberView.Status.reset);
	    save(memberView);
	}
	
}