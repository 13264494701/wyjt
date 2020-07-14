package com.jxf.nfs.dao;

import com.jxf.nfs.entity.NfsTrxRule;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 业务规则DAO接口
 * @author XIAORONGDIAN
 * @version 2018-09-10
 */
@MyBatisDao
public interface NfsTrxRuleDao extends CrudDao<NfsTrxRule> {
	
	NfsTrxRule getByTrxCode(String trxCode);
}