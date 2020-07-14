package com.jxf.wx.access.dao;


import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.wx.access.entity.AccessToken;

/**
 * access_tokenDAO接口
 * @author zhj
 * @version 2015-11-11
 */
@MyBatisDao
public interface AccessTokenDao extends CrudDao<AccessToken> {
	
	/**
	 * 根据微信公众号Id获取access_token
	 * @param acctId
	 * @return
	 */
	public AccessToken getByAcctId(String acctId);
}