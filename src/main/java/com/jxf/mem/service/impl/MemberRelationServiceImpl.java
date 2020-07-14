package com.jxf.mem.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.dao.MemberDistributionDao;
import com.jxf.mem.dao.MemberRelationDao;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberRelation;
import com.jxf.mem.entity.MemberRelation.Level;
import com.jxf.mem.service.MemberRelationService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.google.common.collect.Lists;

/**
 * 会员关系ServiceImpl
 * @author huojiayuan
 * @version 2016-05-24
 */
@Service("memberRelationService")
@Transactional(readOnly = true)
public class MemberRelationServiceImpl extends CrudServiceImpl<MemberRelationDao, MemberRelation> implements MemberRelationService{
	@Autowired
	private MemberDistributionDao memberDistributionDao;
	@Autowired
	private MemberRelationDao memberRelationDao;
	
	@Override
	public MemberRelation get(Long id) {
		return super.get(id);
	}
	
	@Override
	public List<MemberRelation> findList(MemberRelation memberRelation) {
		return super.findList(memberRelation);
	}
	
	@Override
	public Page<MemberRelation> findPage(Page<MemberRelation> page, MemberRelation memberRelation) {
		return super.findPage(page, memberRelation);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(MemberRelation memberRelation) {
		super.save(memberRelation);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(MemberRelation memberRelation) {
		super.delete(memberRelation);
	}
	/**
	 * 构建上下级关系
	 */
	@Override
	@Transactional(readOnly = false)
	public void buildRelations(Member higherMember,Member lowerMember) {
		// 一级关系建立
		saveRelation(higherMember, lowerMember, MemberRelation.Level.first);
		Member firstLevelMember = higherMember;
		memberDistributionDao.updateLevelNum(firstLevelMember.getId(), 1, 1);
		
		Long secondLevelMemberId = getHigherMember(firstLevelMember, MemberRelation.Level.first);
		if(secondLevelMemberId!=null){
			saveRelation(new Member(secondLevelMemberId), lowerMember, MemberRelation.Level.second);
			memberDistributionDao.updateLevelNum(secondLevelMemberId, 2, 1);

			Long thirdLevelMemberId = getHigherMember(new Member(secondLevelMemberId), MemberRelation.Level.first);
			if(thirdLevelMemberId!=null){
				saveRelation(new Member(thirdLevelMemberId), lowerMember, MemberRelation.Level.third);
				memberDistributionDao.updateLevelNum(thirdLevelMemberId, 3, 1);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void saveRelation(Member higherMember,Member lowerMember,MemberRelation.Level level){
		MemberRelation memberRelation = new MemberRelation();
		memberRelation.setMember(higherMember);
		memberRelation.setLowerMember(lowerMember);
		memberRelation.setLevel(level);
		save(memberRelation);
		//获取奖励规则
//		RewardRule rewardRule = new RewardRule();
//		rewardRule.setMemberRelation(level);
//		List<RewardRule> ruleList = rewardRuleService.findList(rewardRule);	
//		//业绩登记
//		for(RewardRule rule:ruleList){
//			savePerformanceRecord(higherMemNo,lowerMemNo,rule);
//		}
	}

	/**
	 * 获取下级会员的会员编号集合 000-为全部下级会员
	 */
	@Override
	public List<String> findlowerMembers(Member higherMember,MemberRelation.Level level) {
		MemberRelation relation = new MemberRelation();
		relation.setMember(higherMember);
//		if(!StringUtils.equals(level, "000")){
//			relation.setLevel(level);
//		}
//		List<MemberRelation> relations =  findList(relation);
		List<String>  memNos = Lists.newArrayList();
//		for (MemberRelation memberRelation : relations) {
//			memNos.add(memberRelation.getLowerMember());
//		}
		return memNos;
	}

	@Override
	public Page<MemberRelation> findMemberSharePage(Member member, Integer pageNo, Integer pageSize) {		
		Page<MemberRelation> page = new Page<MemberRelation>(pageNo == null?1:pageNo, pageSize == null?10:pageSize);	
		MemberRelation memberRelation = new MemberRelation();	
		memberRelation.setPage(page);
		memberRelation.setMember(member);
		memberRelation.setLevel(MemberRelation.Level.first);
		List<MemberRelation> memberRelationList = memberRelationDao.findList(memberRelation);
		page.setList(memberRelationList);
		return page;
	}


	private Long getHigherMember(Member lowerMember, Level level) {
	
		MemberRelation memberRelation = new MemberRelation();
		memberRelation.setLowerMember(lowerMember);
		memberRelation.setLevel(level);
		Long higherMemberId = memberRelationDao.getHigherMemberId(memberRelation);
		return higherMemberId;
	}
	
}