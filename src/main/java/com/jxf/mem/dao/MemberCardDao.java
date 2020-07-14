package com.jxf.mem.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberCard;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;


/**
 * 会员银行卡DAO接口
 * @author JINXINFU
 * @version 2017-02-22
 */
@MyBatisDao
public interface MemberCardDao extends CrudDao<MemberCard> {
	
	/**
	 * 根据memberId调取成员银行卡信息
	 */
	List<MemberCard> getCardByMemberId(Long memberId);

	int getChangeCardCount(@Param("member")Member member, @Param("beginDate")String beginDate,@Param("endDate") String endDate);


}