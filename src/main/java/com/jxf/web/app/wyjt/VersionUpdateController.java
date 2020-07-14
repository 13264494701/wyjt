package com.jxf.web.app.wyjt;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import com.alibaba.fastjson.JSONObject;
import com.jxf.svc.sys.version.entity.SysVersion;
import com.jxf.svc.sys.version.service.SysVersionService;

import com.jxf.web.app.BaseController;
import com.jxf.web.model.ResponseData;

import com.jxf.web.model.wyjt.app.VersionUpdateRequestParam;
import com.jxf.web.model.wyjt.app.VersionUpdateResponseResult;


/**
 * 
 * @类功能说明：客户端升级检查
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：
 * @作者：wo 
 * @创建时间：2019年01月07日 下午4:07:01 
 * @版本：V1.0
 */
@Controller("wyjtAppVersionUpdateController")
@RequestMapping(value="${wyjtApp}/version")
public class VersionUpdateController extends BaseController {

	
	@Autowired
	private SysVersionService sysVersionService;
	/**
	 * 客户端升级检查
	 */

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody
	ResponseData update(HttpServletRequest request, HttpServletResponse response,HttpSession session) {
       
		String param = request.getParameter("param");
		VersionUpdateRequestParam reqData = JSONObject.parseObject(param, VersionUpdateRequestParam.class);
		VersionUpdateResponseResult result = new VersionUpdateResponseResult();	
		String osType = reqData.getOsType();
		Double appVersion = Double.valueOf(reqData.getAppVersion());;
		
		SysVersion v = null;
		if(StringUtils.equals(osType, "android")) {
			 v = sysVersionService.getByType(SysVersion.Type.android);		
		}else if(StringUtils.equals(osType, "ios")){	
		     v = sysVersionService.getByType(SysVersion.Type.ios);
		}
		if(appVersion<Double.valueOf(v.getLastVersion())) {
				result.setNeedsUpdate(v.getNeedsUpdate()?1:0);
				result.setForce(v.getIsForce()?1:0);
				result.setContent(v.getContent());
				result.setUrl(v.getUrl());
	    }

		return ResponseData.success("客户端升级检查结果", result);
	}

}