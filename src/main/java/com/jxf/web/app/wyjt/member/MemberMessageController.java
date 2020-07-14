package com.jxf.web.app.wyjt.member;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.text.StringEscapeUtils;
import org.ebaoquan.rop.thirdparty.org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jxf.cms.entity.CmsNotice;
import com.jxf.cms.service.CmsNoticeService;
import com.jxf.loan.constant.LoanConstant;
import com.jxf.mem.dao.MemberMessageDao;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.DateUtils;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.gxt.CheckMessageResponseResult;
import com.jxf.web.model.wyjt.app.message.MessageDeleteRequestParam;
import com.jxf.web.model.wyjt.app.message.MessageListRequestParam;
import com.jxf.web.model.wyjt.app.message.MessageListResponseResult;
import com.jxf.web.model.wyjt.app.message.MessageListResponseResult.Message;
import com.jxf.web.model.wyjt.app.message.MessageReadRequestParam;
import com.jxf.web.model.wyjt.app.message.SetReadRequestParam;

/**
 * 会员消息Controller
 * @author gaobo
 * @version 2018-10-29
 */
@Controller("wyjtAppMemberMessageController")
@RequestMapping(value = "${wyjtApp}/message")
public class MemberMessageController extends BaseController {

	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private CmsNoticeService cmsNoticeService;
	@Autowired
	private MemberMessageDao memberMessageDao;
	
	/**
	 * 	查看会员消息
	 */
	@RequestMapping(value = "/list")
	public @ResponseBody ResponseData list(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		MessageListRequestParam reqData = JSONObject.parseObject(param,MessageListRequestParam.class);
		MessageListResponseResult result = new MessageListResponseResult();
		
		Integer pageNo = reqData.getPageNo();
		Integer pageSize = reqData.getPageSize();
		Integer tabIndex = reqData.getTabIndex();
		Page<MemberMessage> pageMessage = null;
		Page<CmsNotice> pageNotice = null;
		
		if(tabIndex == 0 ) {
			pageMessage = memberMessageService.findAppPages(pageNo, pageSize, member);
		}else if(tabIndex == 1) {
			pageMessage = memberMessageService.findAppLoanPage(pageNo, pageSize, member);
		}else if(tabIndex == 2) {
			pageMessage = memberMessageService.findAppServicePage(pageNo, pageSize, member);
		}else if(tabIndex == 3) {
			pageMessage = memberMessageService.findAppTransactionPage(pageNo, pageSize, member);
		}else {
			CmsNotice cmsNotice = new CmsNotice();
			cmsNotice.setPosition(CmsNotice.Position.message);
			cmsNotice.setIsPub(true);
			pageNotice = cmsNoticeService.findNoticePage(cmsNotice, pageNo, pageSize);
		}
		if(pageMessage != null) {
			for(MemberMessage message :pageMessage.getList()) {
				int orgType = Integer.parseInt(message.getOrgType());
				String orgId = message.getOrgId().toString();
				String val = "";
				if(orgType == 3) {
					val = orgId;
				}else {
					val = orgId+"|"+orgType;
				}
				String imageUrl = memberMessageService.getImageUrl(message.getTitleValue());
				result.getList().add(new Message(	
						message.getGroups().ordinal(),
						message.isRead() == true?1:0,
						message.getId().toString(),
						message.getType().ordinal(),
						message.getTitle(),
						message.getContent(),
						DateUtils.formatDateTime(message.getCreateTime()),
						imageUrl,
						val,
						message.getRmk()));
			}
		}
		if(pageNotice != null) {
			for (CmsNotice cmsNotice : pageNotice.getList()) {
				int isRead = 0;
				Date createTime = cmsNotice.getCreateTime();
				if(DateUtils.pastDays(createTime) > 2) {
					isRead = 1;
				}
				result.getList().add(new Message(	
						1,
						isRead,
						cmsNotice.getId().toString(),
						0,
						StringEscapeUtils.escapeHtml4(cmsNotice.getTitle()),
						StringEscapeUtils.escapeHtml4(cmsNotice.getContent()),
						DateUtils.formatDate(createTime),
						Global.getConfig("domain")+Global.getConfig("messageIcon.systemNotice"),
						"",
						""));
			}
		}
				
		return ResponseData.success("成功查询全部消息", result);
		
	}
	
	/**
	 * 查询是否有未读的消息
	 */
	@RequestMapping(value = "/checkMessage")
	public @ResponseBody
	ResponseData checkMessage(){
		Member member = memberService.getCurrent();
		CheckMessageResponseResult result = new CheckMessageResponseResult();
		Integer transactionStatus = 0;
		Integer loanStatus = 0;
		Integer arbitrationStatus = 0;
		MemberMessage memberMessage = new MemberMessage();
		memberMessage.setMember(member);
		List<MemberMessage> list = memberMessageDao.findAppLists(memberMessage);
		
		for (MemberMessage message : list) {
			if(message.getGroups().equals(MemberMessage.Group.appTransactionMessage)&&message.isRead() == false) {
				transactionStatus = 1;
				continue;
			}
			if(message.getGroups().equals(MemberMessage.Group.appLoanMessage)&&message.isRead() == false) {
				loanStatus = 1;
				continue;
			}
			if(message.getGroups().equals(MemberMessage.Group.appServiceMessage)&&message.isRead() == false) {
				arbitrationStatus = 1;
				continue;
			}
		}
		result.setTransactionStatus(transactionStatus);
		result.setLoanStatus(loanStatus);
		result.setArbitrationStatus(arbitrationStatus);
		return ResponseData.success("查询成功", result);
	}
	
	/**
	 * 标记全部消息为已读
	 */
	@RequestMapping(value = "/setAllMessageRead")
	public @ResponseBody
	ResponseData setAllMessageRead(HttpServletRequest request){
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		SetReadRequestParam reqData = JSONObject.parseObject(param,SetReadRequestParam.class);
		
		memberMessageService.setReadByGroup(member, reqData.getType());
		
		RedisUtils.put("memberInfo"+member.getId(),"newMsgCount",0);
		return ResponseData.success("成功标记已读");
		
	}
	
	/**
	 * 标记单个消息为已读
	 */
	@RequestMapping(value = "/setMessageRead")
	public @ResponseBody
	ResponseData setMessageRead(HttpServletRequest request){
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		MessageReadRequestParam reqData = JSONObject.parseObject(param,MessageReadRequestParam.class);
		String id = reqData.getId();
		
		memberMessageService.updateReadById(Long.parseLong(id));
		RedisUtils.increment("memberInfo"+member.getId(),"newMsgCount",-1);
		return ResponseData.success("成功标记已读");
	}
	
	/**
	 * 	删除会员消息
	 */
	@RequestMapping(value = "/delete")
	public @ResponseBody ResponseData delete(HttpServletRequest request) {	
		
		String param = request.getParameter("param");
		MessageDeleteRequestParam reqData = JSONObject.parseObject(param,MessageDeleteRequestParam.class);
		
		String ids = reqData.getIds();
		if(StringUtils.isBlank(ids)){
			return ResponseData.error("参数错误");
		}
		if(ids.substring(0,1).equals(",") || ids.substring(ids.length()-1,ids.length()).equals(",")){
			return ResponseData.error("参数错误");
		}
		String[] numbers = ids.split(",");
		for (String s : numbers) {
			if(Long.parseLong(s) <= 0){
				return ResponseData.error("参数错误");
			}
		}
		List<String> list = Arrays.asList(numbers);
//		for (String string : list) {
//			Long noticeId = Long.parseLong(string);
//			CmsNotice cmsNotice = cmsNoticeDao.get(noticeId);
//			if(cmsNotice != null) {
//				return ResponseData.error("系统公告不能删除");
//			}
//		}
		//删除
		if(list != null&&list.size() != 0) {
			memberMessageService.deleteMessages(list);
		}
		return ResponseData.success("成功删除会员消息");
	}
	
	/**
	 * 测试
	 */
//	@RequestMapping(value = "/test")
//	public @ResponseBody
//	ResponseData test(){
//		Member member = memberService.getCurrent();
//		
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("money", "10000");
//		
//		memberMessageService.sendMessage(MemberMessage.Type.enforcementChargesNotice, "申请仲裁", member, map, 100101L ,"1");
//		RedisUtils.increment("memberInfo"+member.getId(),"newMsgCount",1);
//		RedisUtils.put("memberInfo"+member.getId(),"newMsgCount",0);
//		return ResponseData.success("成功发送消息");
//	}
	
	
	
	
	
	
}