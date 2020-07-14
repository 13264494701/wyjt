package com.jxf.cms.dao;

import org.apache.ibatis.annotations.Param;

import com.jxf.cms.entity.CmsChannel;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 频道信息DAO接口
 * @author JINXINFU
 * @version 2016-11-20
 */
@MyBatisDao
public interface CmsChannelDao extends CrudDao<CmsChannel> {
	
	public CmsChannel getByAlias(@Param("alias")String alias);
	
}