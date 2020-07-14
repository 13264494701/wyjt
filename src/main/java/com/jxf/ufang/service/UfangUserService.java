package com.jxf.ufang.service;

import java.util.List;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.ufang.entity.UfangUser;






/**
 * 用户Service
 * @author jxf
 * @version 2015-07-28
 */

public interface UfangUserService extends CrudService<UfangUser> {
	
	/**
	 * 获取用户
	 * @param id
	 * @return
	 */
	public UfangUser getUser(Long id);
	/**
	 * 根据登录名获取用户
	 * @param username
	 * @return
	 */
	public UfangUser getUserByUsername(String username);
	
	
	
	
	public Page<UfangUser> findUser(Page<UfangUser> page, UfangUser user);
	
	/**
	 * 无分页查询人员列表
	 * @param user
	 * @return
	 */
	public List<UfangUser> findUser(UfangUser user);
	/**
	 * 通过部门ID获取用户列表，仅返回用户id和name（树查询用户时用）
	 * @param user
	 * @return
	 */

	public List<UfangUser> findUserByBrnId(Long brnId);
	
	/**
	 * 用户添加
	 * @param user
	 */
	public void saveUser(UfangUser user);
	

	public void deleteUser(UfangUser user);
	/**
	 * 用户修改
	 * @param user
	 */

	public void updateUser(UfangUser user);

	/**
	 * 修改密码
	 * @param id
	 * @param loginName
	 * @param newPassword
	 */

	public void updatePasswordById(Long id,String newPassword);
	

	public void updateUserLoginInfo(UfangUser user);
	/**
	 *  查询某机构下的用户数 
	 * @return
	 */
	public int  getCountBrnUser(Long brnId);
	/**
	 *  根据编号查User
	 * @return
	 */
	public UfangUser findByEmpNo(String userEmpNo);
}
