package com.jxf.mem.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.mem.entity.Member;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 会员管理DAO接口
 * @author HUO
 * @version 2016-04-25
 */
@MyBatisDao
public interface MemberDao extends CrudDao<Member> {

	
	/**
	 * 根据E-mail查找会员
	 * 
	 * @param email
	 *            E-mail(忽略大小写)
	 * @return 会员，若不存在则返回null
	 */
	List<Member> findListByEmail(@Param("email")String email);
	
	/**
	 * 根据用户名查找会员
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 会员，若不存在则返回null
	 */
	Member findByUsername(@Param("username")String username);
	/**
	 * 判断用户名是否存在
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 用户名是否存在
	 */
	String usernameExists(@Param("username")String username);
	/**
	 * 判断E-mail是否存在
	 * 
	 * @param email
	 *            E-mail(忽略大小写)
	 * @return E-mail是否存在
	 */
	String emailExists(@Param("email")String email);
	/**
	 * 重置支付密码
	 * 
	 */
	void resetPayPw(@Param("memberId")Long memberId,@Param("payPwd") String payPw1);
	/**
	 * 通过银行卡查询用户
	 * 
	 * @param idNo
	 * 
	 * @return 用户
	 */
	Member getByIdNo(String idNo);
	/**
	 * 通过安全码查询用户
	 * 
	 * @param safeKey
	 * 
	 * @return 用户
	 */
	Member getBySafeKey(String safeKey);

	/**
	 * 更新信用评级
	 * @param member
	 * @return
	 */
	int updateRankNo(Member member);
	
	/**
	 * 更新用户名
	 * @param member
	 * @return
	 */
	int updateUsername(Member member);
	
	
	Long findByOrgUsername(@Param("username")String username);
	
	
	String lockExists1(Member member);
	String lockExists2(Member member);
	String lockExists3(Member member);

	/***
	 * 根据username查原注册手机号
	 * @param username
	 * @return
	 */
	String getSpareMobile(@Param("username")String username);
	
	
	/**
	 * 插入数据到历史表
	 * @param member
	 * @return
	 */
	int insertHis(Member member);

}