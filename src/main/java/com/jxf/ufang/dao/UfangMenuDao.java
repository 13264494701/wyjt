package com.jxf.ufang.dao;

import java.util.List;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.ufang.entity.UfangMenu;

/**
 * 优放菜单DAO接口
 * @author wo
 * @version 2018-11-26
 */
@MyBatisDao
public interface UfangMenuDao extends CrudDao<UfangMenu> {

	public List<UfangMenu> findByParentIdsLike(UfangMenu menu);

	public List<UfangMenu> findByUserId(Long userId);
	
	public List<UfangMenu> findByUfangUserId(Long userId);
	
	public int updateParentIds(UfangMenu menu);
	
	public int updateSort(UfangMenu menu);
	
}
