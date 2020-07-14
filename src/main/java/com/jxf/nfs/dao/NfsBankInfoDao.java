package com.jxf.nfs.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.nfs.entity.NfsBankInfo;


/**
 * 银行编码DAO接口
 * @author wo
 * @version 2018-09-29
 */
@MyBatisDao
public interface NfsBankInfoDao extends CrudDao<NfsBankInfo> {

    NfsBankInfo getByAbbrName(String abbrName);
	
}