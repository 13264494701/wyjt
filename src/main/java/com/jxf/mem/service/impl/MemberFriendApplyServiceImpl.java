package com.jxf.mem.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.dao.MemberFriendApplyDao;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberFriendApply;
import com.jxf.mem.service.MemberFriendApplyService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
/**
 * 好友申请记录ServiceImpl
 * @author XIAORONGDIAN
 * @version 2018-10-30
 */
@Service("memberFriendApplyService")
@Transactional(readOnly = true)
public class MemberFriendApplyServiceImpl extends CrudServiceImpl<MemberFriendApplyDao, MemberFriendApply> implements MemberFriendApplyService{

	@Autowired
	private MemberFriendApplyDao friendApplyDao;
	
	@Override
	public MemberFriendApply get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<MemberFriendApply> findList(MemberFriendApply memberFriendApply) {
		return super.findList(memberFriendApply);
	}
	
	@Override
	public Page<MemberFriendApply> findPage(Page<MemberFriendApply> page, MemberFriendApply memberFriendApply) {
		page.setPageSize(5);
		return super.findPage(page, memberFriendApply);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(MemberFriendApply memberFriendApply) {
		super.save(memberFriendApply);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(MemberFriendApply memberFriendApply) {
		super.delete(memberFriendApply);
	}

	/**
	 * 用户好友申请列表
	 * @return
	 */
	@Override
	public List<MemberFriendApply> findMemberFriendApply(MemberFriendApply apply) {
		List<MemberFriendApply> applyList = friendApplyDao.findListForApp(apply);
		return applyList;
	}
	
	@Override
	public Page<MemberFriendApply> findMemberFriendApplyPage(Member member, Integer drc ,Integer pageNo,
			Integer pageSize) {

		Page<MemberFriendApply> page = new Page<MemberFriendApply>(pageNo == null?1:pageNo, pageSize == null?10:pageSize);	
		MemberFriendApply apply = new MemberFriendApply();	
		apply.setPage(page);
		if(drc == 0) {
			apply.setFriend(member);
		}else {
			apply.setMember(member);
		}
		List<MemberFriendApply> applyList = friendApplyDao.findList(apply);
		page.setList(applyList);
		return page;
	}
	
}