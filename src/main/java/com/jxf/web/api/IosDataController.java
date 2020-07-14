package com.jxf.web.api;



import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.svc.sys.app.entity.AppTouch;
import com.jxf.svc.sys.app.service.AppInstService;
import com.jxf.svc.sys.app.service.AppTouchService;
import com.jxf.web.admin.sys.BaseController;
import com.jxf.web.model.ResponseData;

/**
 * 给第三方查询idfa Controller
 * @author wo
 * @version 2019-06-17
 */
@Controller
@RequestMapping(value = "${wyjtApi}/idfa")
public class IosDataController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(IosDataController.class);
	
	@Autowired
	private AppInstService appInstService;
	@Autowired
	private AppTouchService appTouchService;
	
    @RequestMapping(value = "/query")
    public @ResponseBody
    ResponseData auth(HttpServletRequest request, HttpServletResponse response, String appId, String idfa) {
    	
    	if(!StringUtils.equals(appId, "1321795027")) {
    		return ResponseData.error("appId不匹配");
    	}
    	Map<String, String> data = new HashMap<String,String>();
    	Boolean flag = appInstService.idfaExist(idfa);
    	data.put("idfa", idfa);
    	data.put("flag", flag?"1":"0");
    	return ResponseData.success("查询成功",data);
    }
    
    @RequestMapping(value = "/touch")
    public @ResponseBody
    ResponseData touch(String appId, String idfa,String ip,String callbackUrl) {
    	
    	if(!StringUtils.equals(appId, "1321795027")) {
    		return ResponseData.error("appId不匹配");
    	}
    	Boolean flag = appInstService.idfaExist(idfa);
    	if(flag) {
    		return ResponseData.error("该设备在平台已经注册");
    	}
    	AppTouch appTouch = new AppTouch();
    	appTouch.setIdfa(idfa);
    	appTouch.setCallbackUrl(StringEscapeUtils.unescapeHtml4(callbackUrl));
    	appTouch.setReqIp(ip);
    	appTouch.setIsInst(false);
    	appTouchService.save(appTouch);
    	
    	return ResponseData.success("点击成功");
    }
    
    
    @RequestMapping(value = "/inst")
    public @ResponseBody
    ResponseData inst(String appId, String idfa,String ip) {
    	
    	if(!StringUtils.equals(appId, "1321795027")) {
    		return ResponseData.error("appId不匹配");
    	}
    	Boolean flag = appInstService.idfaExist(idfa);
    	if(!flag) {
    		return ResponseData.error("该设备在平台未激活");
    	}  	
    	
    	AppTouch appTouch = appTouchService.getByIdfa(idfa);
    	appTouch.setIsInst(true);
    	appTouchService.save(appTouch);
    	return ResponseData.success("激活成功");
    }

}