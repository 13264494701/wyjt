package com.jxf.web.minipro.wyjt;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.svc.config.Global;
import com.jxf.svc.security.jwt.JwtUtil;
import com.jxf.svc.utils.HtmlUtils;
import com.jxf.svc.utils.WxLoginUtils;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.wx.account.entity.WxAccount;
import com.jxf.wx.account.service.WxAccountService;
import com.jxf.wx.user.entity.WxUserInfo;
import com.jxf.wx.user.service.WxUserInfoService;

/**
 * @作者: xiaorongdian
 * @创建时间 :2018年7月12日 上午11:12:56
 * @功能说明:登陆
 */
@Controller("wyjtMiniproLoginWxController")
@RequestMapping(value="${wyjtMinipro}/weixin")
public class LoginWxController extends BaseController {

	@Autowired
	private WxUserInfoService wxUserInfoService;
	@Autowired
	private WxAccountService wxAccountService; 
	@Autowired
	private MemberService memberService;
	
	
	/**
	 * 微信授权登陆
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public synchronized ResponseData login(HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		String code = request.getParameter("code");
		String encryptedData = request.getParameter("encryptedData");
		String iv = request.getParameter("iv");
		String referrerIdStr = request.getParameter("referrerId");
		Long referrerId = StringUtils.isNotBlank(referrerIdStr)?Long.parseLong(referrerIdStr):0;
		Map<String, Object> data = new HashMap<String, Object>();
			if (code == null) {
				return ResponseData.error("用户信息获取失败请重新授权");
			}
			WxAccount wxAccount = wxAccountService.findByCode("wyjt");
			String url = "https://api.weixin.qq.com/sns/jscode2session?";
			String APPID = wxAccount.getAppid();
			String SECRET = wxAccount.getSecret();
			String grant_type = "authorization_code";
			StringBuffer bf = new StringBuffer();
			bf.append("appid=");
			bf.append(APPID);
			bf.append("&");
			bf.append("secret=");
			bf.append(SECRET);
			bf.append("&");
			//版本号
			bf.append("js_code=");
			bf.append(code);
			bf.append("&");
			bf.append("grant_type=");
			bf.append(grant_type);
			String html = HtmlUtils.getContent(url + bf.toString());
			if (html.contains("errcode")) {
				return ResponseData.error("该授权已经过期请重新登陆");
			}
			Map<String, Object> mapFromJson = JSON.parseObject(html,Map.class);

			String openid = mapFromJson.get("openid").toString();
			String sessionKey = mapFromJson.get("session_key").toString();
			
			Map<String, Object> reMap1 = WxLoginUtils.getWxUserInfo(encryptedData, iv, sessionKey);//解密后的明文
			
			if (reMap1.get("status").equals(0)) {
				return ResponseData.error("该授权已经过期请重新登陆");
			}
			String html1 = reMap1.get("userInfo").toString();
			Map<String, Object> reMap = JSON.parseObject(html1,Map.class);
			if (reMap != null) {
				String unionId = reMap.get("unionId").toString();//每个人唯一的号 我们用这个 一个企业好多小程序时用
				String nickName = reMap.get("nickName").toString();//昵称
				String headImage = reMap.get("avatarUrl").toString();
				
				WxUserInfo wxUserInfo = wxUserInfoService.findByOpenId(openid);

				if (null==wxUserInfo) {//如果是新用户
					wxUserInfo = new WxUserInfo();
					wxUserInfo.setAccount(wxAccount);
					wxUserInfo.setOpenid(openid);
					wxUserInfo.setUnionid(unionId);
					wxUserInfo.setNickname(nickName);
					wxUserInfo.setHeadImage(headImage);
					wxUserInfo.setIsMember(false);
					wxUserInfo.setReferrer(new WxUserInfo(referrerId));
					wxUserInfoService.save(wxUserInfo);			
				}
				boolean isMember = wxUserInfo.getIsMember();
				data.put("isVerified", isMember);
				data.put("nickName", wxUserInfo.getNickname());
				data.put("headImage", wxUserInfo.getHeadImage());
				data.put("wxUserId", wxUserInfo.getId()+"");//Long类型长度过长前端JS存在精度损失，故转换成String类型
				if(isMember) {
					Member member = memberService.get(wxUserInfo.getMember().getId());
					if(StringUtils.isNotBlank(member.getPayPassword())) {
						data.put("payPwStatus", "1");
					}else {
						data.put("payPwStatus", "0");
					}
				}else {
					data.put("payPwStatus", "0");
				}
			    HashMap<String, Object> payLoad = new HashMap<>();
			    payLoad.put("id", wxUserInfo.getId());
				String token = JwtUtil.generToken(payLoad,Global.getTokenTimeout());
				response.setHeader("token",token); 		
			} else {
				return ResponseData.error("登陆异常，请您重新登陆");
			}
		return ResponseData.success("成功", data);
	}
}
