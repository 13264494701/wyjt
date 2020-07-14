package com.jxf.ufang.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.ufang.entity.UfangUserAct;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;




/**
 * 优放账户DAO接口
 * @author jinxinfu
 * @version 2018-06-29
 */
@MyBatisDao
public interface UfangUserActDao extends CrudDao<UfangUserAct> {
	
	UfangUserAct getByUserAndSubNo(UfangUserAct ufangUserAct);
	
	int updateActBal(@Param("ufangUserAct")UfangUserAct ufangBrnAct,@Param("trxAmt")BigDecimal trxAmt);

}