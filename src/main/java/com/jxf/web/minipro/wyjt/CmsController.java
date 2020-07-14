package com.jxf.web.minipro.wyjt;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.cms.entity.CmsArticle;
import com.jxf.cms.entity.CmsChannel;
import com.jxf.cms.entity.CmsNotice;
import com.jxf.cms.service.CmsArticleContentService;
import com.jxf.cms.service.CmsArticleService;
import com.jxf.cms.service.CmsChannelService;
import com.jxf.cms.service.CmsNoticeService;
import com.jxf.svc.persistence.Page;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;


/**
 * Controller - 内容管理
 * 
 * @author JINXINFU
 * @version 2.0
 */
@Controller("wyjtMiniproCmsController")
@RequestMapping(value="${wyjtMinipro}/cms")
public class CmsController extends BaseController {
	
	@Autowired
	private CmsArticleService articleService;
	@Autowired
	private CmsArticleContentService articleContentService;
	@Autowired
	private CmsChannelService channelService;
	@Autowired
	private CmsNoticeService noticeService;

	/**
	 * 频道列表
	 */
	@RequestMapping(value = "/channel/list", method = RequestMethod.GET)
	public @ResponseBody
	ResponseData channellist() {
		Map<String, Object> data = new HashMap<String, Object>();

		CmsChannel channel = new CmsChannel();
		channel.setInNav(true);
		List<CmsChannel> channelList = channelService.findList(channel);		
		data.put("list", channelList);
		return ResponseData.success("查询频道列表成功", data);
	}
	
	/**
	 * 文章列表
	 */
	@RequestMapping(value = "/article/list", method = RequestMethod.GET)
	public @ResponseBody
	ResponseData articlelist(Long channelId,  Integer pageNo, Integer pageSize) {

		CmsChannel channel = channelService.get(channelId);
		CmsArticle article = new CmsArticle();
		article.setChannel(channel);
		article.setIsPub(true);
		Page<CmsArticle> page = articleService.findPage(article, pageNo, pageSize);		
	
		return ResponseData.success("查询内容列表成功", page);
	}
	/**
	 * 文章详情
	 */
	@RequestMapping(value = "/article/detail", method = RequestMethod.GET)
	public @ResponseBody
	ResponseData articledetail(Long id,Integer pageNo) {

		CmsArticle article = articleService.get(id);
		article.setArticleContent(articleContentService.get(id));

		Map<String, Object> result = new HashMap<String , Object>();
		result.put("article", article);
		return ResponseData.success("查询内容详情成功", result);
	}
	
	/**
	 * 函数功能说明 通知列表
	 * zhuhuijie  2016年5月26日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param goodsNo
	 * @参数： @param model
	 * @参数： @return     
	 * @return String    
	 * @throws
	 */
	@RequestMapping(value="/notice/list")
	public String noticeList(CmsNotice cmsNotice ,Integer pageNo,Model model){

		cmsNotice.setIsPub(true);
		Page<CmsNotice> page =  noticeService.findNoticePage(cmsNotice,pageNo,10);
		model.addAttribute("page", page);
		return "mb/cms/notice/list";
	}

	/**
	 * 函数功能说明 通知正文
	 * zhuhuijie  2016年5月26日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param id
	 * @参数： @param model
	 * @参数： @return     
	 * @return String    
	 * @throws
	 */
	@RequestMapping(value="/notice/view")
	public String view(Long id,Model model){

		CmsNotice notice =  noticeService.get(id);
		model.addAttribute("notice", notice);
		return "mb/cms/notice/view";
	}
}