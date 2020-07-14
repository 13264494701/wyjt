package com.jxf.nfs.dao;

import com.jxf.nfs.entity.NfsTrxRuleItem;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 业务规则DAO接口
 * @author wo
 * @version 2018-09-21
 */
@MyBatisDao
public interface NfsTrxRuleItemDao extends CrudDao<NfsTrxRuleItem> {
	
}