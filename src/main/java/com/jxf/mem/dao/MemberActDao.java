package com.jxf.mem.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.mem.entity.MemberAct;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;


/**
 * 会员账户DAO接口
 * @author zhj
 * @version 2016-05-12
 */
@MyBatisDao
public interface MemberActDao extends CrudDao<MemberAct> {
	/**
	 *  设置会员默认账户
	 * @param memberAct
	 */
	void setDefault(MemberAct memberAct);
	/**
	 * 取会员默认账户
	 * @param memberAct
	 * @return
	 */
	MemberAct getMemberDefaultAct(MemberAct memberAct);
	

	int updateActSts(MemberAct memberAct);

	int updateActBal(MemberAct memberAct);
	
	
	List<MemberAct> sumMemberAct();
	/**
	 * 获取账户金额(可用+借款)
	 * @param memberId
	 * @return
	 */
	BigDecimal getCurBal(@Param("memberId") Long memberId);
	
	int deleteByMemberId(@Param("memberId") Long memberId);

}