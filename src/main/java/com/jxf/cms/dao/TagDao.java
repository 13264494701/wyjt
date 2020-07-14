package com.jxf.cms.dao;


import com.jxf.cms.entity.Tag;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 标签DAO接口
 * @author JINXINFU
 * @version 2016-04-26
 */
@MyBatisDao
public interface TagDao extends CrudDao<Tag> {

	public Tag getByTagCode(String tagCode);
	
}