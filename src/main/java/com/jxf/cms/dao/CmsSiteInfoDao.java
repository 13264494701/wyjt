package com.jxf.cms.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.cms.entity.CmsSiteInfo;

/**
 * 站点信息DAO接口
 * @author JINXINFU
 * @version 2016-11-20
 */
@MyBatisDao
public interface CmsSiteInfoDao extends CrudDao<CmsSiteInfo> {
	
}