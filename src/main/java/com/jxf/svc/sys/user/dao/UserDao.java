package com.jxf.svc.sys.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.svc.sys.role.entity.Role;
import com.jxf.svc.sys.user.entity.User;


/**
 * 用户DAO接口
 * @author jxf
 * @version 2015-07-27
 */
@MyBatisDao
public interface UserDao extends CrudDao<User> {
	
	/**
	 * 根据登录名称查询用户
	 * @param loginName
	 * @return
	 */
	public User getByLoginName(User user);
	
	/**
	 * 通过BrnId获取用户列表，仅返回用户id和name（树查询用户时用）
	 * @param user
	 * @return
	 */
	public List<User> findUserByBrnId(User user);
	
	/**
	 * 查询全部用户数目
	 * @return
	 */
	public long findAllCount(User user);
	
	/**
	 * 更新用户密码
	 * @param user
	 * @return
	 */
	public int updatePasswordById(User user);
	
	
	/**
	 * 更新登录信息，如：登录IP、登录时间
	 * @param user
	 * @return
	 */
	public int updateLoginInfo(User user);
	
	/**
	 * 插入用户角色关联数据
	 * @param user
	 * @return
	 */
	public int insertUserRole(User user);
	/**
	 * 查找用户角色关联数据
	 * @param user
	 * @return
	 */
	public List<Role> findUserRoleList(User user);
	/**
	 * 删除用户角色关联数据
	 * @param user
	 * @return
	 */
	public int deleteUserRole(User user);
	

	
	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	public int updateUserInfo(User user);
	
	
	/**
	 * 查询机构下的最大用户号
	 * @param office
	 * @return
	 */
	public String getChildMaxUserNo(String office);
	
	
	/**
	 * !-- 查询某机构下的用户数 
	 * @param brn
	 * @return
	 */

	public int getCountBrnUser(@Param("delFlag")String delFlag,@Param("brnId")Long brnId);


	/**
	 * 查询指定机构最大员工号
	 * @param office
	 * @return
	 */
	public String getMaxEmpNo(Long brnId);
	
	/**
	 * 根据用户号查找用户名 
	 * @param empNo
	 * @return
	 */
	public String getEmpNamByEmpNo(String empNo);

}
