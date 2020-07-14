package com.jxf.nfs.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.nfs.entity.NfsBrnActTrx;


/**
 * 账户交易DAO接口
 * @author jinxinfu
 * @version 2018-07-01
 */
@MyBatisDao
public interface NfsBrnActTrxDao extends CrudDao<NfsBrnActTrx> {
	/**
	 * 根据原业务id查询账户流水
	 * @param businessId
	 * @return
	 */
	List<NfsBrnActTrx>  findListByBusinessId(@Param("businessId")Long businessId);
}