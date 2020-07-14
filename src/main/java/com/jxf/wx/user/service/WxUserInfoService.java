package com.jxf.wx.user.service;

import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.wx.user.entity.WxUserInfo;

/**
 * 微信用户信息Service
 * @author gaobo
 * @version 2018-10-16
 */
public interface WxUserInfoService extends CrudService<WxUserInfo> {
	
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
	WxUserInfo findByMember(Long memberId,String code);
	
	/**
	 * 获取当前微信用户信息
	 * @return
	 */
	WxUserInfo getCurrent();
	
}