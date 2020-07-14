package com.jxf.ufang.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.ufang.entity.UfangBrnAct;


/**
 * 优放账户DAO接口
 * @author jinxinfu
 * @version 2018-06-29
 */
@MyBatisDao
public interface UfangBrnActDao extends CrudDao<UfangBrnAct> {
	
	UfangBrnAct getByCompanyAndSubNo(UfangBrnAct ufangBrnAct);
		
}