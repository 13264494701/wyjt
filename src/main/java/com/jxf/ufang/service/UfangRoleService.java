package com.jxf.ufang.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.ufang.dao.UfangRoleDao;
import com.jxf.ufang.entity.UfangRole;
import com.jxf.ufang.util.UfangUserUtils;




/**
 * 字典Service
 * @author jxf
 * @version 2015-07-28
 */
@Service
@Transactional(readOnly = true)
public class UfangRoleService extends CrudServiceImpl<UfangRoleDao, UfangRole> {
	
	@Autowired
	private UfangRoleDao roleDao;
	
	public UfangRole getRole(Long id) {
		return roleDao.get(id);
	}

	public UfangRole getRoleByName(String name) {
		UfangRole r = new UfangRole();
		r.setRoleName(name);
		return roleDao.getByName(r);
	}
	
	public List<UfangRole> findRole(UfangRole role){
		return roleDao.findList(role);
	}
	
	public List<UfangRole> findAllRole(){
		return roleDao.findList(new UfangRole());
	}
	
	public List<UfangRole> findUserRoleList(){
		return UfangUserUtils.getRoleList();
	}

	
	@Transactional(readOnly = false)
	public void saveRole(UfangRole role) {
		if (role.getIsNewRecord()){
			role.preInsert();
			roleDao.insert(role);
		}else{
			role.preUpdate();
			roleDao.update(role);
		}
		// 更新角色与菜单关联
		roleDao.deleteRoleMenu(role);
		if (role.getMenuList().size() > 0){
			roleDao.insertRoleMenu(role);
		}
		// 清除用户角色缓存
		UfangUserUtils.removeCache(UfangUserUtils.CACHE_ROLE_LIST);
	}

	@Transactional(readOnly = false)
	public void delete(UfangRole role) {
		roleDao.deleteRoleMenu(role);
		super.delete(role);
		// 清除用户角色缓存
		UfangUserUtils.removeCache(UfangUserUtils.CACHE_ROLE_LIST);
	}

	
}
