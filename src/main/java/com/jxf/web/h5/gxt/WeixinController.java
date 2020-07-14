package com.jxf.web.h5.gxt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.svc.utils.Encodes;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.RandomUtils;
import com.jxf.web.app.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.gxt.WeixinAuthResponseResult;
import com.jxf.web.model.gxt.WeixinJsSignatureResponseResult;
import com.jxf.wx.account.entity.WxAccount;
import com.jxf.wx.account.service.WxAccountService;
import com.jxf.wx.api.OauthAPI;
import com.jxf.wx.api.response.GetUserInfoResponse;
import com.jxf.wx.api.response.OauthGetTokenResponse;
import com.jxf.wx.api.response.BaseResponse.ResultType;
import com.jxf.wx.api.utils.JsApiUtil;
import com.jxf.wx.ticket.GxtJsApiTicket;
import com.jxf.wx.user.entity.WxUserInfo;
import com.jxf.wx.user.service.WxUserInfoService;



/**
 * Controller - 微信相关接口
 *
 * @author JINXINFU
 * @version 2.0
 */
@Controller("gxtH5WeixinController")
@RequestMapping(value = "${gxtH5}/wx")
public class WeixinController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(WeixinController.class);
    
	@Autowired
	private WxAccountService wxAccountService; 
    @Autowired
    private WxUserInfoService wxUserInfoService;
    /**
     * 微信授权
     */
    @RequestMapping(value = "/auth")
    public @ResponseBody
    ResponseData auth(HttpServletRequest request, HttpServletResponse response, String code) {
		
        if (StringUtils.isBlank(code)) {
            log.info("code不能为空");
            return ResponseData.error("code不能为空");
        }
        WxAccount wxAccount = wxAccountService.findByCode("gxt");
        OauthAPI oauthAPI = new OauthAPI(wxAccount.getAppid(),wxAccount.getSecret());
        OauthGetTokenResponse tokenRsp = oauthAPI.getToken(code); 
		if(tokenRsp == null) {
			logger.error("授权接口异常");
			return ResponseData.error("授权接口异常");
		}

		if(!ResultType.SUCCESS.getCode().toString().equals(tokenRsp.getErrcode())) {
			logger.error("调用接口凭证accessToken获取出错，错误信息:{}",tokenRsp.getErrmsg());
			return ResponseData.error(tokenRsp.getErrmsg());
		}

        GetUserInfoResponse userInfoRsp = oauthAPI.getUserInfo(tokenRsp.getAccessToken(), tokenRsp.getOpenid());
		if(userInfoRsp == null) {
			logger.error("获取用户信息接口异常");
			return ResponseData.error("获取用户信息接口异常");
		}
		if(!ResultType.SUCCESS.getCode().toString().equals(userInfoRsp.getErrcode())) {
			logger.error("获取用户信息接口出错，错误信息:{}",userInfoRsp.getErrmsg());
			return ResponseData.error(userInfoRsp.getErrmsg());
		}
		
		WxUserInfo wxUserInfo = wxUserInfoService.findByOpenId(userInfoRsp.getOpenid());
		if(wxUserInfo==null) {
			wxUserInfo = new WxUserInfo();
			wxUserInfo.setAccount(wxAccount);
			wxUserInfo.setOpenid(userInfoRsp.getOpenid());
			wxUserInfo.setUnionid(userInfoRsp.getUnionid());
			wxUserInfo.setNickname(userInfoRsp.getNickname());
			wxUserInfo.setHeadImage(userInfoRsp.getHeadimgurl());
			wxUserInfoService.save(wxUserInfo);
		}else {
			wxUserInfo.setNickname(userInfoRsp.getNickname());
			wxUserInfo.setHeadImage(userInfoRsp.getHeadimgurl());
			wxUserInfoService.save(wxUserInfo);
		}
		
        WeixinAuthResponseResult result = new WeixinAuthResponseResult();
        result.setOpenid(userInfoRsp.getOpenid());
        result.setUnionid(userInfoRsp.getUnionid());
        result.setNickname(userInfoRsp.getNickname());
        result.setHeadimgurl(userInfoRsp.getHeadimgurl());
        
        return ResponseData.success("授权成功",result);
    }






    @RequestMapping(value = "/url_sign")
    public @ResponseBody
    ResponseData url_sign(String url) {

    	WeixinJsSignatureResponseResult result = new WeixinJsSignatureResponseResult();

        try {
        	String jsApiTicket = GxtJsApiTicket.getJsapiticket();
            String nonceStr = RandomUtils.generateNumString(8);
            Long timestamp = System.currentTimeMillis() / 1000;
//            log.info("解码前URL={}",url);
//            log.info("解码后URL={}",Encodes.unescapeHtml(url));
            /*springMvc 传递参数，特殊字符会被转义或过滤掉，造成后台拿到的数据不正确*/
        	String sign = JsApiUtil.sign(jsApiTicket, nonceStr, timestamp, Encodes.unescapeHtml(url));
        	result.setAppId("wx61a1c7b0f189fe01");
        	result.setNonceStr(nonceStr);
        	result.setTimestamp(timestamp);
        	result.setSignature(sign);
        	
        } catch (Exception e) {
        	log.error("获取签名异常:", Exceptions.getStackTraceAsString(e));
        	return ResponseData.error("获取签名异常");
        }
    	
    	return ResponseData.success("签名成功",result);
    }
}