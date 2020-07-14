package com.jxf.ufang.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.ufang.entity.UfangRole;
import com.jxf.ufang.entity.UfangUser;


/**
 * 用户DAO接口
 * @author wo
 * @version 2018-11-10
 */
@MyBatisDao
public interface UfangUserDao extends CrudDao<UfangUser> {
	
	/**
	 * 根据登录名称查询用户
	 * @param username
	 * @return
	 */
	public UfangUser getByUsername(@Param("username")String username);
	
	/**
	 * 通过BrnId获取用户列表，仅返回用户id和name（树查询用户时用）
	 * @param user
	 * @return
	 */
	public List<UfangUser> findUserByBrnId(UfangUser user);
	
	
	/**
	 * 更新用户密码
	 * @param user
	 * @return
	 */
	public int updatePasswordById(UfangUser user);
	
	
	/**
	 * 更新登录信息，如：登录IP、登录时间
	 * @param user
	 * @return
	 */
	public int updateLoginInfo(UfangUser user);

	/**
	 * 查找用户角色关联数据
	 * @param user
	 * @return
	 */
	public List<UfangRole> findUserRoleList(UfangUser user);
	/**
	 * 删除用户角色关联数据
	 * @param user
	 * @return
	 */
	public int deleteUserRole(UfangUser user);
	
	/**
	 * 插入用户角色关联数据
	 * @param user
	 * @return
	 */
	public int insertUserRole(UfangUser user);



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
	
	
	/**
	 * !-- 查询某机构下的用户数 
	 * @param brn
	 * @return
	 */
	public int getCountBrnUser(Long brnId);

	/***
	 * 根据编号查User
	 * @param userEmpNo
	 * @return
	 */
	public UfangUser findByEmpNo(@Param("userEmpNo")String userEmpNo);

}
