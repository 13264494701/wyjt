package com.jxf.ufang.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.ufang.entity.UfangRole;

/**
 * 角色DAO接口
 * @author jxf
 * @version 2015-07-28
 */
@MyBatisDao
public interface UfangRoleDao extends CrudDao<UfangRole> {

	public UfangRole getByName(UfangRole role);
	

	/**
	 * 维护角色与菜单权限关系
	 * @param role
	 * @return
	 */
	public int deleteRoleMenu(UfangRole role);

	public int insertRoleMenu(UfangRole role);
	
	
}
