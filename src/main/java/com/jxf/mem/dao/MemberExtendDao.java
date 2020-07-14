package com.jxf.mem.dao;

import org.apache.ibatis.annotations.Param;

import com.jxf.mem.entity.MemberExtend;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 会员扩展信息DAO接口
 * @author XIAORONGDIAN
 * @version 2018-10-13
 */
@MyBatisDao
public interface MemberExtendDao extends CrudDao<MemberExtend> {

	/**
	 * 根据memberId查会员扩展信息
	 * @param memberId
	 * @return
	 */
	
	MemberExtend getByMemberId(@Param("memberId") Long memberId);
	
}