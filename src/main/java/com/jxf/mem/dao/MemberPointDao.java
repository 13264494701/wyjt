package com.jxf.mem.dao;

import org.apache.ibatis.annotations.Param;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberPoint;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 会员积分DAO接口
 * @author JINXINFU
 * @version 2016-04-25
 */
@MyBatisDao
public interface MemberPointDao extends CrudDao<MemberPoint> {

	/**
	 * 领取
	 * @param member 会员
	 * @param wineDrips 获得
	 */
	void addWineDrips(@Param("member")Member member,@Param("wineDrips") int wineDrips);
	
	
	
	
	int deleteByMemberId(@Param("memberId") Long memberId);
	

}