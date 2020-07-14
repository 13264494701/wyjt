package com.jxf.cms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.cms.entity.CmsIcon;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 图标DAO接口
 * @author HUOJIABAO
 * @version 2016-07-23
 */
@MyBatisDao
public interface CmsIconDao extends CrudDao<CmsIcon> {
	
	CmsIcon getByPositionNo(@Param("positionNo")String positionNo);
	
	
	List<CmsIcon> findListByPageNo(@Param("pageNo")String pageNo);
	
}