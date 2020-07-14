package com.jxf.cms.dao;

import com.jxf.cms.entity.Site;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;



/**
 * 站点DAO接口
 * @author jxf
 * @version 1.0 2015-07-10
 */
@MyBatisDao
public interface SiteDao extends CrudDao<Site> {

}
