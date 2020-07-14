package com.jxf.mem.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.mem.entity.MemberPhonebook;

/**
 * 手机通讯录DAO接口
 * @author wo
 * @version 2019-03-07
 */
@MyBatisDao
public interface MemberPhonebookDao extends CrudDao<MemberPhonebook> {
	
}