package com.jxf.loan.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.svc.annotation.MyBatisDao;

import org.apache.ibatis.annotations.Param;

import com.jxf.loan.entity.NfsCrContract;

/**
 * 债转合同DAO接口
 * @author suHuimin
 * @version 2019-03-12
 */
@MyBatisDao
public interface NfsCrContractDao extends CrudDao<NfsCrContract> {
	
	NfsCrContract getCrContractByCrId(@Param("crId")Long crId);
}