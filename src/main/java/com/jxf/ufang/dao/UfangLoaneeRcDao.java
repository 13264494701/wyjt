package com.jxf.ufang.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.ufang.entity.UfangLoaneeRc;

import org.apache.ibatis.annotations.Param;



import com.jxf.svc.annotation.MyBatisDao;


/**
 * 借款人风控数据DAO接口
 * @author wo
 * @version 2019-04-28
 */
@MyBatisDao
public interface UfangLoaneeRcDao extends CrudDao<UfangLoaneeRc> {
	
	UfangLoaneeRc getByPhoneNo(@Param("phoneNo")String phoneNo);
}