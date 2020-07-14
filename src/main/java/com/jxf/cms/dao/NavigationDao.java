package com.jxf.cms.dao;

import java.util.List;

import com.jxf.cms.entity.Navigation;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 导航DAO接口
 * @author huojiayuan
 * @version 2016-06-09
 */
@MyBatisDao
public interface NavigationDao extends CrudDao<Navigation> {
	
	/**
	 * 查找导航
	 * 
	 * @param position
	 *            导航位置
	 * @param useCache
	 *            是否使用缓存
	 * @return 导航
	 */
	List<Navigation> findListByPosition(String position);
	
}