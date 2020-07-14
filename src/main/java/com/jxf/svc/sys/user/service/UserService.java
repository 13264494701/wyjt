package com.jxf.svc.sys.user.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.cache.CacheUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.security.PasswordUtils;
import com.jxf.svc.sys.brn.entity.Brn;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.svc.sys.dict.dao.DictDao;
import com.jxf.svc.sys.dict.entity.Dict;
import com.jxf.svc.sys.user.dao.UserDao;
import com.jxf.svc.sys.user.entity.User;
import com.jxf.svc.sys.util.UserUtils;




/**
 * 用户Service
 * @author jxf
 * @version 2015-07-28
 */
@Service
@Transactional(readOnly = true)
public class UserService extends CrudServiceImpl<DictDao, Dict> {
	
	
	@Autowired
	private UserDao userDao;

	/**
	 * 获取用户
	 * @param id
	 * @return
	 */
	public User getUser(Long id) {
		return UserUtils.get(id);
	}

	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return
	 */
	public User getUserByLoginName(String loginName) {
		return UserUtils.getByLoginName(loginName);
	}
	
	public Page<User> findUser(Page<User> page, User user) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		user.getSqlMap().put("dsf", UserUtils.dataScopeFilter("c1", "a"));
		// 设置分页参数
		user.setPage(page);
		// 执行分页查询
		page.setOrderBy("a.emp_no ASC");
		page.setList(userDao.findList(user));
		return page;
	}
	
	/**
	 * 无分页查询人员列表
	 * @param user
	 * @return
	 */
	public List<User> findUser(User user){
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		user.getSqlMap().put("dsf", UserUtils.dataScopeFilter("c1", "a"));
		List<User> list = userDao.findList(user);
		return list;
	}

	/**
	 * 通过部门ID获取用户列表，仅返回用户id和name（树查询用户时用）
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<User> findUserByBrnId(Long brnId) {
		List<User> list = (List<User>)CacheUtils.get(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_BRN_ID_ + brnId);
		if (list == null){
			User user = new User();
			user.setBrn(new Brn(brnId));
			list = userDao.findUserByBrnId(user);
			CacheUtils.put(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_BRN_ID_ + brnId, list);
		}
		return list;
	}
	
	/**
	 * 用户添加
	 * @param user
	 */
	@Transactional(readOnly = false)
	public void saveUser(User user) {
		if (user.getIsNewRecord()){
			user.preInsert();
			user.setEmpNo(UserUtils.genNewEmpNo(user.getBrn()));
			user.setLoginName(user.getEmpNo());
			user.setIsEnabled(true);
			user.setIsLocked(false);
			user.setMask(1);
			userDao.insert(user);
		}else{
			// 清除原用户机构用户缓存
			User oldUser = userDao.get(user.getId());
			if (oldUser.getBrn() != null && oldUser.getBrn().getId() != null){
				CacheUtils.remove(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_BRN_ID_ + oldUser.getBrn().getId());
			}
			// 更新用户数据
			user.preUpdate();
			userDao.update(user);
		}
		if (user.getId()!=null){
			// 更新用户与角色关联
			userDao.deleteUserRole(user);
			userDao.insertUserRole(user);

			// 清除用户缓存
			UserUtils.clearCache(user);
		}
	}
	
	@Transactional(readOnly = false)
	public void updateUserInfo(User user) {
		user.preUpdate();
		userDao.updateUserInfo(user);
		// 清除用户缓存
		UserUtils.clearCache(user);
	}
	
	@Transactional(readOnly = false)
	public void deleteUser(User user) {
		userDao.delete(user);
		// 清除用户缓存
		UserUtils.clearCache(user);
	}
	
	/**
	 * 用户修改
	 * @param user
	 */
	@Transactional(readOnly = false)
	public void updateUser(User user) {
		// 清除原用户机构用户缓存
		User oldUser = userDao.get(user.getId());
		if (oldUser.getBrn() != null && oldUser.getBrn().getId() != null){
				CacheUtils.remove(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_BRN_ID_ + oldUser.getBrn().getId());
		}
		// 更新用户数据
		user.preUpdate();
		// 修改用户数据
		userDao.update(user);
		if (user.getId()!=null){
			// 删除用户与角色关联
			userDao.deleteUserRole(user);
			// 新增用户与角色关联
			userDao.insertUserRole(user);
			// 清除用户缓存
			UserUtils.clearCache(user);
		}
	}

	/**
	 * 修改密码
	 * @param id
	 * @param loginName
	 * @param newPassword
	 */
	@Transactional(readOnly = false)
	public void updatePasswordById(Long id,String newPassword) {
		User user = new User(id);
		user.setPassword(PasswordUtils.entryptPassword(newPassword));
		userDao.updatePasswordById(user);
		// 清除用户缓存
		UserUtils.clearCache(user);
	}
	
	@Transactional(readOnly = false)
	public void updateUserLoginInfo(User user) {
		// 保存上次登录信息
		user.setOldLoginIp(user.getLoginIp());
		user.setOldLoginDate(user.getLoginDate());
		// 更新本次登录信息
		user.setLoginIp(UserUtils.getSession().getHost());
		user.setLoginDate(new Date());
		userDao.updateLoginInfo(user);
	}
	/**
	 *  查询某机构下的用户数 
	 * @return
	 */
	public int  getCountBrnUser(String del_flag,Long brnId){
		return userDao.getCountBrnUser(del_flag, brnId);
		
	}
}
