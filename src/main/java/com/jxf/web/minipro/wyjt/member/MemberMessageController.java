package com.jxf.web.minipro.wyjt.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.wx.user.entity.WxUserInfo;
import com.jxf.wx.user.service.WxUserInfoService;

/**
 * 会员消息Controller
 * @author gaobo
 * @version 2018-10-19
 */
@Controller("wyjtMiniproMemberMessageController")
@RequestMapping(value = "${wyjtMinipro}/member")
public class MemberMessageController extends BaseController {

	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private WxUserInfoService wxUserInfoService;
	@Autowired
	private MemberService memberService;
	
	/**
	 * 查看会员消息
	 */
	@RequestMapping(value = "/allMessage", method = RequestMethod.POST)
	public @ResponseBody ResponseData allMessage() {	
		
		Map<String,List<MemberMessage>> data = new HashMap<String,List<MemberMessage>>();
		WxUserInfo wxUserInfo = wxUserInfoService.getCurrent();
		Member member = memberService.get(wxUserInfo.getMember().getId());
		MemberMessage memberMessage = new MemberMessage();
		memberMessage.setMember(member);
		List<MemberMessage> list = memberMessageService.findList(memberMessage);
		data.put("list", list);
		return ResponseData.success("成功查询全部消息",data);
	}
	

}