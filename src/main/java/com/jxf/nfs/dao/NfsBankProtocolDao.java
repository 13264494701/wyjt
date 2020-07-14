package com.jxf.nfs.dao;

import com.jxf.nfs.entity.NfsBankProtocol;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 签约协议DAO接口
 * @author suhuimin
 * @version 2018-09-30
 */
@MyBatisDao
public interface NfsBankProtocolDao extends CrudDao<NfsBankProtocol> {


	NfsBankProtocol getByMember(Long memberId);
	
}