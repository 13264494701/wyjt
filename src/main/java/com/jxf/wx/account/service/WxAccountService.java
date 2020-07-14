package com.jxf.wx.account.service;

import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.wx.account.entity.WxAccount;

/**
 * 微信公众号账号Service
 * @author gaobo
 * @version 2018-10-16
 */
public interface WxAccountService extends CrudService<WxAccount> {

	/**
	 * 根据code 查询公众号账号
	 * @param string
	 * @return
	 */
	WxAccount findByCode(String string);

	
	
}