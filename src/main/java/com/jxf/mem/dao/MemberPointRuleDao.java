package com.jxf.mem.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

import org.apache.ibatis.annotations.Param;

import com.jxf.mem.entity.MemberPointRule;


/**
 * 积分规则DAO接口
 * @author wo
 * @version 2018-08-12
 */
@MyBatisDao
public interface MemberPointRuleDao extends CrudDao<MemberPointRule> {
	void enableRule(@Param("id")String id,@Param("sts") String sts);
}