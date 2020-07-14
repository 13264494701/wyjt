package com.jxf.rc.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;

import org.apache.ibatis.annotations.Param;

import com.jxf.rc.entity.RcQuota;
import com.jxf.svc.annotation.MyBatisDao;

/**
 * 会员额度评估DAO接口
 * @author SuHuimin
 * @version 2019-08-23
 */
@MyBatisDao
public interface RcQuotaDao extends CrudDao<RcQuota> {
	
	RcQuota getByMemberId(@Param("memberId")Long memberId);
	RcQuota getByPhoneNo(@Param("phoneNo")String phoneNo);
}