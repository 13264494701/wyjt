package com.jxf.web.h5.gxt.member;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.text.StringEscapeUtils;
import org.ebaoquan.rop.thirdparty.org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.cms.entity.CmsNotice;
import com.jxf.cms.service.CmsNoticeService;
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
import com.jxf.web.model.gxt.GxtMessageListResponseResult;
import com.jxf.web.model.gxt.GxtMessageListResponseResult.GxtMessage;

/**
 * 会员消息Controller
 * @author gaobo
 * @version 2019-05-08
 */
@Controller("gxtH5MemberMessageController")
@RequestMapping(value = "${gxtH5}/message")
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
	public @ResponseBody ResponseData list(Integer pageNo, Integer pageSize, Integer tabIndex) {	
		Member member = memberService.getCurrent();
		GxtMessageListResponseResult result = new GxtMessageListResponseResult();
		
		Page<MemberMessage> pageMessage = null;
		Page<CmsNotice> pageNotice = null;
		
		if(tabIndex == 0 ) {
			pageMessage = memberMessageService.findGxtPages(pageNo, pageSize, member);
		}else if(tabIndex == 1) {
			pageMessage = memberMessageService.findTransactionPage(pageNo, pageSize, member);
		}else if(tabIndex == 2) {
			pageMessage = memberMessageService.findLoanPage(pageNo, pageSize, member);
		}else if(tabIndex == 3) {
			pageMessage = memberMessageService.findArbitrationPage(pageNo, pageSize, member);
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
					val = orgId+"_"+orgType;
				}
				String imageUrl = memberMessageService.getImageUrl(message.getTitleValue());
				result.getList().add(new GxtMessage(	
						message.getId().toString(),
						message.isRead() == true?1:0,
						message.getType().ordinal(),
						message.getTitle(),
						message.getContent(),
						DateUtils.formatDateTime(message.getCreateTime()),
						imageUrl,
						message.getRmk(),
						val));
			}
		}
		if(pageNotice != null) {
			for (CmsNotice cmsNotice : pageNotice.getList()) {
				result.getList().add(new GxtMessage(	
						cmsNotice.getId().toString(),
						0,
						999,
						StringEscapeUtils.escapeHtml4(cmsNotice.getTitle()),
						StringEscapeUtils.escapeHtml4(cmsNotice.getContent()),
						DateUtils.formatDateTime(cmsNotice.getCreateTime()),
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
		List<MemberMessage> list = memberMessageDao.findGxtLists(memberMessage);
		
		for (MemberMessage message : list) {
			if(message.getGroups().equals(MemberMessage.Group.gxtTransactionMessage)&&message.isRead() == false) {
				transactionStatus = 1;
				continue;
			}
			if(message.getGroups().equals(MemberMessage.Group.gxtLoanMessage)&&message.isRead() == false) {
				loanStatus = 1;
				continue;
			}
			if(message.getGroups().equals(MemberMessage.Group.gxtArbitrationMessage)&&message.isRead() == false) {
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
	ResponseData setAllMessageRead(Integer type){
		Member member = memberService.getCurrent();
		
		memberMessageService.setReadByGroup(member, type);
		
		RedisUtils.put("memberInfo"+member.getId(),"newMsgCount",0);
		return ResponseData.success("成功标记已读");
	}
	
	/**
	 * 标记单个消息为已读
	 */
	@RequestMapping(value = "/setMessageRead")
	public @ResponseBody
	ResponseData setMessageRead(String id){
		Member member = memberService.getCurrent();
		
		memberMessageService.updateReadById(Long.parseLong(id));
		RedisUtils.increment("memberInfo"+member.getId(),"newMsgCount",-1);
		return ResponseData.success("成功标记已读");
	}
	
	/**
	 * 	删除会员消息
	 */
	@RequestMapping(value = "/delete")
	public @ResponseBody ResponseData delete(String ids) {	
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
		//删除
		if(list != null&&list.size() != 0) {
			memberMessageService.deleteMessages(list);
		}
		return ResponseData.success("成功删除会员消息");
	}
	
	
	
	
	
	
	
}