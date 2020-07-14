package com.jxf.rc.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

import com.jxf.rc.entity.RcGxb;

/**
 * 风控 公信宝DAO接口
 * @author Administrator
 * @version 2018-10-16
 */
@MyBatisDao
public interface RcGxbDao extends CrudDao<RcGxb> {

    RcGxb findByToken(String token);

}