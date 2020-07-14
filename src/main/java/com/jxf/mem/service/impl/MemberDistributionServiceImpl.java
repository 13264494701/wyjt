package com.jxf.mem.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.dao.MemberDistributionDao;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberDistribution;
import com.jxf.mem.service.MemberDistributionService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 三级分销ServiceImpl
 * @author huojiayuan
 * @version 2016-05-25
 */
@Service("memberDistributionService")
@Transactional(readOnly = true)
public class MemberDistributionServiceImpl extends CrudServiceImpl<MemberDistributionDao, MemberDistribution> implements MemberDistributionService{

	@Autowired
	private MemberDistributionDao memberDistributionDao;
	
	@Override
	public MemberDistribution get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<MemberDistribution> findList(MemberDistribution memberDistribution) {
		return super.findList(memberDistribution);
	}
	
	@Override
	public Page<MemberDistribution> findPage(Page<MemberDistribution> page, MemberDistribution memberDistribution) {
		page.setOrderBy("a.member_no ASC");
		return super.findPage(page, memberDistribution);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(MemberDistribution memberDistribution) {
		super.save(memberDistribution);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(MemberDistribution memberDistribution) {
		super.delete(memberDistribution);
	}
	/**
	 * 函数功能说明 根据会员编号查询
	 * zhuhuijie  2016年6月8日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param memberNo
	 * @参数： @return     
	 * @return MemberDistribution    
	 * @throws
	 */
	@Override
	public MemberDistribution getByMember(Member member){
		return memberDistributionDao.getByMember(member);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void initMemDist(Member member){
		MemberDistribution dist = new MemberDistribution();
		dist.setMember(member);
		dist.setLevelFirst(0);
		dist.setLevelSecond(0);
		dist.setLevelThird(0);
		dist.setReward(new BigDecimal("0.00"));
		dist.setIsOpen(false);
		save(dist);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void open(Member member){
		memberDistributionDao.open(member.getId());
	}
	
	@Override
	@Transactional(readOnly = false)
	public void close(Member member){
		memberDistributionDao.close(member.getId());
	}
	
}