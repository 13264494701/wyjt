package com.jxf.wx.access.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jxf.wx.access.entity.AccessToken;
import com.jxf.wx.account.entity.WxAccount;
import com.jxf.wx.api.exception.WeixinException;
import com.jxf.wx.api.response.BaseResponse;
import com.jxf.wx.api.response.GetTokenResponse;
import com.jxf.wx.api.response.BaseResponse.ResultType;
import com.jxf.wx.api.utils.NetWorkCenter;
import com.alibaba.fastjson.JSON;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.wx.access.dao.AccessTokenDao;

/**
 * access_tokenService
 * @author zhj
 * @version 2015-11-11
 */
@Service
@Transactional(readOnly = true)
public class AccessTokenService extends CrudServiceImpl<AccessTokenDao, AccessToken> {

	private static final Logger logger = LoggerFactory.getLogger(AccessTokenService.class);
	
	public AccessToken get(Long id) {
		return super.get(id);
	}
	
	public List<AccessToken> findList(AccessToken accessToken) {
		return super.findList(accessToken);
	}
	
	public Page<AccessToken> findPage(Page<AccessToken> page, AccessToken accessToken) {
		return super.findPage(page, accessToken);
	}
	
	@Transactional(readOnly = false)
	public void save(AccessToken accessToken) {
		super.save(accessToken);
	}
	
	@Transactional(readOnly = false)
	public void delete(AccessToken accessToken) {
		super.delete(accessToken);
	}
	
	public synchronized String getAccessToken(WxAccount wxAccount) {
		
		GetTokenResponse response = null;
		String appid = wxAccount.getAppid();
		String secret = wxAccount.getSecret();
		
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret;            
		/** 调用核心接口 获取accessToken*/
		BaseResponse rsp = NetWorkCenter.get(url);
		if(rsp != null) {
			if(ResultType.SUCCESS.getCode().toString().equals(rsp.getErrcode())) {
				response = JSON.parseObject(rsp.getErrmsg(), GetTokenResponse.class);
			}else {
				throw new WeixinException("调用接口凭证accessToken获取出错，错误信息:"
						+ rsp.getErrcode() + ","
						+ rsp.getErrmsg());
			}
		}else {
			logger.error("调用接口异常");
		}
		
		return response.getAccessToken();
		
	}
}