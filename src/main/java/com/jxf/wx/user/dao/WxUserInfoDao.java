package com.jxf.wx.user.dao;

import org.apache.ibatis.annotations.Param;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.wx.user.entity.WxUserInfo;

/**
 * 微信用户信息DAO接口
 * @author gaobo
 * @version 2018-10-16
 */
@MyBatisDao
public interface WxUserInfoDao extends CrudDao<WxUserInfo> {
	
	/**
	 * 根据openid 查询用户信息
	 * @param openid
	 * @return
	 */
	WxUserInfo findByOpenId(String openid);
	/**
	 * 根据unionId 查询用户信息
	 * @param unionId
	 * @return
	 */
	WxUserInfo findByUnionId(String unionId);
	/**
	 * 根据memberId 查询用户信息
	 * @param memberId
	 * @return
	 */
	WxUserInfo findByMemberAndCode(@Param("memberId")Long memberId,@Param("code")String code);
	
}