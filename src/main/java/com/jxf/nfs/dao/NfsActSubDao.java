package com.jxf.nfs.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.nfs.entity.NfsActSub;


/**
 * 账户科目DAO接口
 * @author wo
 * @version 2018-09-18
 */
@MyBatisDao
public interface NfsActSubDao extends CrudDao<NfsActSub> {
	
	String getSubName(NfsActSub nfsActSub);
}