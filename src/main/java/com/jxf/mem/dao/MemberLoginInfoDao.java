package com.jxf.mem.dao;

import com.jxf.mem.entity.MemberLoginInfo;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 会员登陆信息DAO接口
 * @author gaobo
 * @version 2019-05-31
 */
@MyBatisDao
public interface MemberLoginInfoDao extends CrudDao<MemberLoginInfo> {

	MemberLoginInfo getLoginInfoByMemberId(Long memberId);
	
}