package com.jxf.ufang.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.jxf.svc.security.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.entity.Member;
import com.jxf.nfs.constant.ActSubConstant;
import com.jxf.svc.cache.CacheUtils;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.security.PasswordUtils;

import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

import com.jxf.svc.sys.util.UserUtils;
import com.jxf.ufang.dao.UfangUserDao;
import com.jxf.ufang.entity.UfangBrn;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.entity.UfangUserAct;
import com.jxf.ufang.service.UfangUserActService;
import com.jxf.ufang.service.UfangUserService;
import com.jxf.ufang.util.UfangUserUtils;




/**
 * 用户Service
 * @author jxf
 * @version 2015-07-28
 */
@Service("ufangUserService")
@Transactional(readOnly = true)
public class UfangUserServiceImpl extends CrudServiceImpl<UfangUserDao, UfangUser> implements UfangUserService{
	
	
	@Autowired
	private UfangUserDao userDao;
	
	@Autowired
	private UfangUserActService userActService;

	/**
	 * 获取用户
	 * @param id
	 * @return
	 */
	public UfangUser getUser(Long id) {
		return UfangUserUtils.get(id);
	}

	/**
	 * 根据登录名获取用户
	 * @param username
	 * @return
	 */
	public UfangUser getUserByUsername(String username) {
		return UfangUserUtils.getByUsername(username);
	}
	
	public Page<UfangUser> findUser(Page<UfangUser> page, UfangUser user) {

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
	public List<UfangUser> findUser(UfangUser user){

		List<UfangUser> list = userDao.findList(user);
		return list;
	}

	/**
	 * 通过部门ID获取用户列表，仅返回用户id和name（树查询用户时用）
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UfangUser> findUserByBrnId(Long brnId) {
		List<UfangUser> list = (List<UfangUser>)CacheUtils.get(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_BRN_ID_ + brnId);
		if (list == null){
			UfangUser user = new UfangUser();
			user.setBrn(new UfangBrn(brnId));
			list = userDao.findUserByBrnId(user);
			CacheUtils.put(UfangUserUtils.USER_CACHE, UfangUserUtils.USER_CACHE_LIST_BY_BRN_ID_ + brnId, list);
		}
		return list;
	}
	
	
	@Transactional(readOnly = false)
	public void save(UfangUser user) {
		super.save(user);
	}
	
	/**
	 * 用户添加
	 * @param user
	 */
	@Transactional(readOnly = false)
	public void saveUser(UfangUser user) {
		if(user.getIsNewRecord()){
			user.preInsert();
			user.setEmpNo(UfangUserUtils.genNewEmpNo(user.getBrn()));
			user.setMember(new Member());
			user.setBindStatus(UfangUser.BindStatus.unbind);
			user.setIsEnabled(true);
			user.setIsLocked(false);
			// 默认设置密码为6个1
			user.setPassword(PasswordUtils.entryptPassword(MD5Utils.EncoderByMd5("111111").toUpperCase()));
			user.setLoginIp(UserUtils.getSession().getHost());
			user.setLoginDate(new Date());
			userDao.insert(user);
			//创建默认账户
			UfangUserAct userAct = new UfangUserAct();
			userAct.setUser(user);;
			userAct.setSubNo(ActSubConstant.UFANG_USER_AVL_BAL);
			userAct.setCurBal(BigDecimal.ZERO);
			userAct.setCurrCode("CNY");
			userAct.setStatus(UfangUserAct.Status.enabled);
			userActService.save(userAct);	
		}else{
			// 清除原用户机构用户缓存
			UfangUser oldUser = userDao.get(user);
			if (oldUser.getBrn() != null && oldUser.getBrn().getId() != null){
				CacheUtils.remove(UfangUserUtils.USER_CACHE, UfangUserUtils.USER_CACHE_LIST_BY_BRN_ID_ + oldUser.getBrn().getId());
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
			UfangUserUtils.clearCache(user);
		}
	}
	

	
	@Transactional(readOnly = false)
	public void deleteUser(UfangUser user) {
		userDao.delete(user);
		// 清除用户缓存
		UfangUserUtils.clearCache(user);
	}
	
	/**
	 * 用户修改
	 * @param user
	 */
	@Transactional(readOnly = false)
	public void updateUser(UfangUser user) {
		// 清除原用户机构用户缓存
		UfangUser oldUser = userDao.get(user.getId());
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
			UfangUserUtils.clearCache(user);
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
		UfangUser user = userDao.get(id);;
		user.setPassword(PasswordUtils.entryptPassword(newPassword));
		userDao.updatePasswordById(user);
		// 清除用户缓存
		UfangUserUtils.clearCache(user);
	}
	
	@Transactional(readOnly = false)
	public void updateUserLoginInfo(UfangUser user) {
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
	public int  getCountBrnUser(Long brnId){
		return userDao.getCountBrnUser(brnId);
		
	}

	@Override
	public UfangUser findByEmpNo(String userEmpNo) {
		return userDao.findByEmpNo(userEmpNo);
	}
}
