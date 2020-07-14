package com.jxf.mem.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.dao.MemberPointDetailDao;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberPointDetail;
import com.jxf.mem.service.MemberPointDetailService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
/**
 * 积分明细ServiceImpl
 * @author zhj
 * @version 2016-05-13
 */
@Service("memberPointDetailService")
@Transactional(readOnly = true)
public class MemberPointDetailServiceImpl extends CrudServiceImpl<MemberPointDetailDao, MemberPointDetail> implements MemberPointDetailService{

	@Autowired
	private MemberPointDetailDao memberPointDetailDao;
	
	public MemberPointDetail get(Long id) {
		return super.get(id);
	}
	
	public List<MemberPointDetail> findList(MemberPointDetail shopMemberPointDetail) {
		return super.findList(shopMemberPointDetail);
	}
	
	public Page<MemberPointDetail> findPage(Page<MemberPointDetail> page, MemberPointDetail shopMemberPointDetail) {
		page.setPageSize(10);
		page.setOrderBy("a.create_time DESC");
		return super.findPage(page, shopMemberPointDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(MemberPointDetail shopMemberPointDetail) {
		super.save(shopMemberPointDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(MemberPointDetail shopMemberPointDetail) {
		super.delete(shopMemberPointDetail);
	}
	@Override
	public Page<MemberPointDetail> findMemberPointPage(Member member,
			Integer pageNo, Integer pageSize) {
		Page<MemberPointDetail> page = new Page<MemberPointDetail>(pageNo == null?1:pageNo, pageSize == null?10:pageSize);	
		MemberPointDetail memberPointDetail = new MemberPointDetail();	
		memberPointDetail.setPage(page);
		memberPointDetail.setMember(member);
		List<MemberPointDetail> memberPointDetailList = memberPointDetailDao.findList(memberPointDetail);
		page.setList(memberPointDetailList);
		return page;
	}
}