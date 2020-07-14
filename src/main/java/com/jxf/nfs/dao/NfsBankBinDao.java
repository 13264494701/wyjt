package com.jxf.nfs.dao;

import org.apache.ibatis.annotations.Param;

import com.jxf.nfs.entity.NfsBankBin;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 卡BINDAO接口
 * @author wo
 * @version 2018-09-29
 */
@MyBatisDao
public interface NfsBankBinDao extends CrudDao<NfsBankBin> {
	
	NfsBankBin getByCardBin(@Param("cardBin")String cardBin,@Param("length")Integer length);

}