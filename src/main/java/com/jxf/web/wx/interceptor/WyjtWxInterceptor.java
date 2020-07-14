package com.jxf.web.wx.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jxf.mem.entity.Member;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Constant;
import com.jxf.svc.security.Principal;
import com.jxf.svc.security.jwt.JwtUtil;
import com.jxf.web.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
//import com.jxf.web.model.wyjt.app.OtherDeviceLoginResponseResult;

public class WyjtWxInterceptor implements HandlerInterceptor {

	private static final Logger log = LoggerFactory.getLogger(WyjtWxInterceptor.class);

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
			String memberToken = request.getHeader("x-memberToken");	
			if (StringUtils.isBlank(memberToken)) {
				log.warn("=========================token 为空");	
				responseMessage(response, response.getWriter(), ResponseData.unauthorized());
				return false;
			}else{		         
				ResponseData responseData= JwtUtil.verifyToken(memberToken);
				if(responseData.getCode()==Constant.JWT_SUCCESS){
					JSONObject payload = (JSONObject) JSON.toJSON(responseData.getResult());
					Long id = payload.getLong("id");
					String deviceToken= payload.getString("deviceToken");
//					String cacheDeviceToken = (String) RedisUtils.getHashKey("loginInfo"+id, "deviceToken");
					String cacheDeviceToken = "";
					if(StringUtils.equals(deviceToken,cacheDeviceToken)) {
						HttpSession session = request.getSession();
						session.setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(id,memberToken));
						return true;	
					}else {
						//其它设备登录
//						OtherDeviceLoginResponseResult result = new OtherDeviceLoginResponseResult();
						String loginDevice = (String) RedisUtils.getHashKey("loginInfo"+id, "deviceToken");
						String loginIp = (String) RedisUtils.getHashKey("loginInfo"+id, "loginIp");
						String loginTime = (String) RedisUtils.getHashKey("loginInfo"+id, "loginTime");
//						result.setLoginDevice(loginDevice);
//						result.setLoginIp(loginIp);
//						result.setLoginTime(loginTime);
//						responseMessage(response, response.getWriter(), ResponseData.otherDeviceLogin("该账号在其它设备登录,如非本人操作，请及时修改密码或与客服联系。",result));
						responseData.setCode(101);
						responseData.setMessage("该账号在其它设备登录,如非本人操作，请及时修改密码或与客服联系。");
						responseData.setLoginDevice(loginDevice);
						responseData.setLoginIp(loginIp);
						responseData.setLoginTime(loginTime);	
						responseMessage(response, response.getWriter(), responseData);
						log.warn("Token验证失败[{}]","该账号在其它设备登录,如非本人操作，请及时修改密码或与客服联系。");
						return false;
					}

				}else{
					responseMessage(response, response.getWriter(), responseData);
					log.warn("Token验证失败{}",responseData.getMessage());	
					return false;
				}
			}		
	}
	
	//请求不通过，返回错误信息给客户端
    private void responseMessage(HttpServletResponse response, PrintWriter out, ResponseData responseData) {

        response.setContentType("application/json; charset=utf-8");  
        String json = JSONObject.toJSONString(responseData);
        out.print(json);
        out.flush();
        out.close();
    }

}
