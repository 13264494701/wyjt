package com.jxf.mem.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.svc.annotation.MyBatisDao;

import java.util.List;

import com.jxf.mem.entity.MemberFriendCa;

/**
 * 信用报告申请DAO接口
 * @author wo
 * @version 2018-12-17
 */
@MyBatisDao
public interface MemberFriendCaDao extends CrudDao<MemberFriendCa> {
	
	/**
	 * 信用报告申请记录
	 * @param memberFriendCa
	 * @return
	 */
	List<MemberFriendCa> findCredictBaoGaoRecode(MemberFriendCa memberFriendCa);
	
	
}