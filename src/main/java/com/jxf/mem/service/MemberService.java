package com.jxf.mem.service;

import java.math.BigDecimal;
import java.util.List;






import com.jxf.mem.entity.Member;
import com.jxf.svc.model.HandleRsp;
import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.gxt.MemberInfoGxtResponseResult;
import com.jxf.web.model.wyjt.app.WechatLoginRequestParam;
import com.jxf.web.model.wyjt.app.member.MemberInfoResponseResult;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;

/**
 * 会员管理Service
 * @author HUO
 * @version 2016-04-25
 */
public interface MemberService extends CrudService<Member> {

	/**
	 * app会员登录
	 * @param member
	 * @param request
	 *
	 * @return MemberInfoResponseResult
	 */
    MemberInfoResponseResult appLogin(Member member, Long wxUserInfoId, HttpServletRequest request, WechatLoginRequestParam wechatLoginRequestParam);

	/**
	 * 判断会员是否登录
	 * 
	 * @return 会员是否登录
	 */
	boolean isAuthenticated();
	
	/**
	 * 获取当前登录会员
	 * 
	 * @return 当前登录会员，若不存在则返回null
	 */
	Member getCurrent();
	
	Member getCurrent2();
	
	
	/**
	 * 获取当前登录用户名
	 * 
	 * @return 当前登录用户名，若不存在则返回null
	 */
	String getCurrentUsername();
	
	/**
	 * 根据E-mail查找会员
	 * 
	 * @param email
	 *            E-mail(忽略大小写)
	 * @return 会员，若不存在则返回null
	 */
	List<Member> findListByEmail(String email);
	/**
	 * 根据用户名查找会员
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 会员，若不存在则返回null
	 */
	Member findByUsername(String username);
	/**
	 * 判断用户名是否存在
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 用户名是否存在
	 */
	boolean usernameExists(String username);
	/**
	 * 判断E-mail是否存在
	 * 
	 * @param email
	 *            E-mail(忽略大小写)
	 * @return E-mail是否存在
	 */
	boolean emailExists(String email);
	/**
	 * 判断E-mail是否唯一
	 * 
	 * @param previousEmail
	 *            修改前E-mail(忽略大小写)
	 * @param currentEmail
	 *            当前E-mail(忽略大小写)
	 * @return E-mail是否唯一
	 */
	boolean emailUnique(String previousEmail, String currentEmail);
	

	
	void initMember(Member member,Long referrerId);
	/**
	 * 重置支付密码
	 * @param memberId
	 * @param payPw1
	 */
	void resetPayPw(Long memberId, String payPw1);
	/**
	 * 通过身份证号查询用户
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
	 * 获取用户信息
	 * @param member
	 * @return
	 */
	MemberInfoResponseResult getMemberInfo(Member member);
	
	
	
	//String quryZhongZC(String name, String moblie, String idCard, String url, int type);
	
	/**
	 *  校验支付密码
	 * @param pwd
	 * @param member
	 * @return
	 */
	ResponseData checkPayPwd(String pwd,Member member);

	/**
	 * 更新用户名
	 * @param oldUsername
	 * @param newUsername
	 * @return
	 */
	HandleRsp updateUsername(String oldUsername,String newUsername);
	
	/**
	 * 更新信用评级
	 * @param member
	 * @return
	 */
	int updateRankNo(Member member);
	
	/**
	 * 获取用户可用余额 
	 */
	BigDecimal getAvlBal(Member member);
	
	/**
	 * 获取用户当前余额 
	 */
	BigDecimal getCulBal(Member member);
	
	
	Long findByOrgUsername(@Param("username")String username);
	
	Boolean lockExists(Member member);

	/***
	 * 根据username查原注册手机号
	 * @param username
	 * @return
	 */
	String getSpareMobile(String username);

	ResponseData dealPhoneNo(Member member, String type, String phoneNo);
	/**
	 * 获取用户信息给公信堂
	 * @param member
	 * @return
	 */
	MemberInfoGxtResponseResult getMemberInfoGxt(Member member);
	
	/**
	 * 插入数据到历史表
	 * @param member
	 * @return
	 */
	int insertHis(Member member);
	
	
}