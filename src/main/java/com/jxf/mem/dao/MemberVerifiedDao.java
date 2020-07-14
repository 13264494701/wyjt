package com.jxf.mem.dao;

import org.apache.ibatis.annotations.Param;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.mem.entity.MemberVerified;

/**
 * 实名认证DAO接口
 * @author wo
 * @version 2018-09-28
 */
@MyBatisDao
public interface MemberVerifiedDao extends CrudDao<MemberVerified> {

	/**
	 * 根据会员ID 查已通过认证
	 * @param memberId
	 * @return
	 */
	MemberVerified getVerifiedByMemberId(@Param("memberId")Long memberId);
	
}