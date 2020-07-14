package com.jxf.fee.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;

import org.apache.ibatis.annotations.Param;

import com.jxf.fee.entity.NfsFeeRule;
import com.jxf.svc.annotation.MyBatisDao;


/**
 * 收费规则DAO接口
 * @author wo
 * @version 2019-01-05
 */
@MyBatisDao
public interface NfsFeeRuleDao extends CrudDao<NfsFeeRule> {
	
	NfsFeeRule getByTrxCode(@Param("trxCode")String trxCode);
}