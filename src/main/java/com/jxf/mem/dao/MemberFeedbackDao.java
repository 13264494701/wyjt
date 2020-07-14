package com.jxf.mem.dao;

import com.jxf.mem.entity.MemberFeedback;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 保存用户反馈意见DAO接口
 * @author suhuimin
 * @version 2018-11-01
 */
@MyBatisDao
public interface MemberFeedbackDao extends CrudDao<MemberFeedback> {
	
}