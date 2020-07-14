package com.jxf.mem.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.dao.MemberRankDao;
import com.jxf.mem.entity.MemberRank;
import com.jxf.mem.service.MemberRankService;
import com.jxf.mem.utils.MemUtils;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 会员等级ServiceImpl
 * @author JINXINFU
 * @version 2016-04-25
 */
@Service("memberRankService")
@Transactional(readOnly = true)
public class MemberRankServiceImpl extends CrudServiceImpl<MemberRankDao, MemberRank> implements MemberRankService{
    
	@Autowired
	private MemberRankDao memberRankDao;
	
	@Override
	public MemberRank get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<MemberRank> findList(MemberRank shopMemberRank) {
		return super.findList(shopMemberRank);
	}
	
	@Override
	public Page<MemberRank> findPage(Page<MemberRank> page, MemberRank shopMemberRank) {
		page.setOrderBy("a.rank_no ASC");
		return super.findPage(page, shopMemberRank);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(MemberRank shopMemberRank) {
		if (shopMemberRank.getIsNewRecord()){
			shopMemberRank.preInsert();
			shopMemberRank.setRankNo(MemUtils.genNewRankNo());
			shopMemberRank.setIsDefault(false);
			memberRankDao.insert(shopMemberRank);
		}else{
			shopMemberRank.preUpdate();
			memberRankDao.update(shopMemberRank);
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(MemberRank shopMemberRank) {
		super.delete(shopMemberRank);
	}
	
	@Override
	public MemberRank findDefault() {
		return memberRankDao.findDefault();
	}

	@Override
	@Transactional(readOnly = false)
	public void setDefault(MemberRank memberRank) {
		
		memberRankDao.setDefault(memberRank);
	}

	@Override
	public String getRankNameByNo(String rankNo) {
		
		return memberRankDao.getMemRankByNo(rankNo).getRankName();
	}
	
}