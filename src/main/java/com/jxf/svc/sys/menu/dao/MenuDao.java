package com.jxf.svc.sys.menu.dao;

import java.util.List;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.svc.sys.menu.entity.Menu;

/**
 * 菜单DAO接口
 * @author jxf
 * @version 2015-07-28
 */
@MyBatisDao
public interface MenuDao extends CrudDao<Menu> {

	public List<Menu> findByParentIdsLike(Menu menu);

	public List<Menu> findByUserId(Long userId);
	
	public List<Menu> findByUfangUserId(Long userId);
	
	public int updateParentIds(Menu menu);
	
	public int updateSort(Menu menu);
	
}
