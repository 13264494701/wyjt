package com.jxf.wx.account.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.wx.account.entity.WxAccount;

/**
 * 微信公众号账号DAO接口
 * @author gaobo
 * @version 2018-10-16
 */
@MyBatisDao
public interface WxAccountDao extends CrudDao<WxAccount> {

	/**
	 * 根据code 查询公众号账号
	 * @param string
	 * @return
	 */
	WxAccount findByCode(String string);
	
}