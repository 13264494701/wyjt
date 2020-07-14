package com.jxf.nfs.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;

import com.jxf.nfs.entity.NfsBrnAct;


/**
 * 机构账户DAO接口
 * @author jinxinfu
 * @version 2018-06-29
 */
@MyBatisDao
public interface NfsBrnActDao extends CrudDao<NfsBrnAct> {
	
	NfsBrnAct getByCompanyAndSubNo(NfsBrnAct nfsBrnAct);
	
	int updateActBal(@Param("nfsBrnAct")NfsBrnAct nfsBrnAct,@Param("trxAmt")BigDecimal trxAmt);

}