package com.jxf.cms.dao;

import java.util.List;

import com.jxf.cms.entity.CmsAd;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 广告管理DAO接口
 * @author JINXINFU
 * @version 2016-04-25
 */
@MyBatisDao
public interface CmsAdDao extends CrudDao<CmsAd> {
	
	List<CmsAd> findAdsByPositionNo(String positionNo);
}