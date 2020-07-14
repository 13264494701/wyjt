package com.jxf.web.minipro.gxt;


import java.util.HashMap;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;



/**
 * Controller - 首页
 * 
 * @author wo
 * @version 2.0
 */
@Controller("gxtMiniproIndexController")
@RequestMapping(value="${gxtMinipro}/index")
public class IndexController extends BaseController {


	/**
	 * 
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public @ResponseBody
	ResponseData index() {
		
		Map<String, Object> data = new HashMap<String, Object>();	
		data.put("loanCount", 2300);
		data.put("loanUnit", "万");
        data.put("memberCount", 710);
        data.put("memberUnit", "万");
        
				
		return ResponseData.success("加载首页数据成功", data);
	}

}