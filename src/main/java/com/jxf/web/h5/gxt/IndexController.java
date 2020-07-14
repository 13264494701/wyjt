package com.jxf.web.h5.gxt;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.cms.entity.CmsNotice;
import com.jxf.cms.service.CmsNoticeService;
import com.jxf.svc.utils.RandomUtils;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;



/**
 * Controller - 首页
 * 
 * @author SuHuimin
 * @version 1.0
 */
@Controller("gxtH5IndexController")
@RequestMapping(value="${gxtH5}/index")
public class IndexController extends BaseController {
	
	@Autowired
	private CmsNoticeService noticeService;
	/**
	 * 首页信息
	 */
	@RequestMapping(value = "info")
	public @ResponseBody
	ResponseData info(HttpServletRequest request) {
		Map<String, Object> data = new HashMap<String, Object>();
		CmsNotice notice = new CmsNotice();
		notice.setPosition(CmsNotice.Position.gxtMarquee);
		notice.setIsPub(true);
		List<CmsNotice> list = noticeService.findList(notice);
		CmsNotice cmsNotice = list.get(0);
		String title = cmsNotice.getTitle();
		String firstPart = title.substring(title.indexOf("**"),title.lastIndexOf("**")-1);
		String secondPart = title.substring(title.lastIndexOf("**")); 
		for(int i = 0;i < 10 ; i++ ) {
			String firstSurname = RandomUtils.getChineseSurname();
			String secondSurname = RandomUtils.getChineseSurname();
			StringBuilder newTitle = new StringBuilder();
			newTitle.append(firstSurname).append(firstPart).append(secondSurname).append(secondPart);
			data.put("notice"+i, newTitle.toString());
		}
		return ResponseData.success("请求成功",data);
	}
}