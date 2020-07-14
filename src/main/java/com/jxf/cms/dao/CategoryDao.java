package com.jxf.cms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.cms.entity.Category;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.tree.dao.TreeDao;


/**
 * 栏目DAO接口
 * @author jxf
 * @version 1.0 2015-07-10
 */
@MyBatisDao
public interface CategoryDao extends TreeDao<Category> {
	
	public List<Category> findByParentId(Category category);
	
	public Category getByAlias(@Param("alias")String alias);	
	
}
