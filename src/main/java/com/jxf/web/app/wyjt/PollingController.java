package com.jxf.web.app.wyjt;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.mem.service.MemberService;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Global;
import com.jxf.svc.model.Notice;
import com.jxf.svc.security.jwt.JwtUtil;
import com.jxf.web.app.BaseController;
import com.jxf.web.model.ResponseData;

import com.jxf.web.model.wyjt.app.PollingNoticeResponseResult;


/**
 * 
 * @类功能说明：客户端轮询
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：
 * @作者：wo 
 * @创建时间：2018年11月06日 下午4:07:01 
 * @版本：V1.0
 */
@Controller("wyjtAppPollingController")
@RequestMapping(value="${wyjtApp}/polling")
public class PollingController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(PollingController.class);
	
	@Autowired
	private MemberService memberService;

	/**
	 * 客户端轮询
	 */

	@RequestMapping(value = "/notice", method = RequestMethod.POST)
	public @ResponseBody
	ResponseData polling(HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		
        Long memberId = memberService.getCurrent2().getId();
        if(memberId==null) {
        	log.error("当前用户的token:{}",request.getHeader("x-memberToken"));
        	return ResponseData.error("当前用户token为空,请重新登录");
        }

		PollingNoticeResponseResult result = new PollingNoticeResponseResult();
		
		Object noticeObj = RedisUtils.rightPop("memberNotice"+memberId);		
		if(noticeObj!=null) {
			Notice notice = (Notice)noticeObj;
			result.setNoticeType(notice.getNoticeType());
			result.setNoticeId(notice.getNoticeId());
			result.setNoticeContent(notice.getNoticeMessage());
		}
		Object newMsgCountObj = RedisUtils.getHashKey("memberInfo"+memberId, "newMsgCount");

		if(newMsgCountObj!=null) {
			Integer newMsgCount = (Integer)newMsgCountObj;
			result.setNewMsgCount(newMsgCount);
		}

		//更新memberToken
		String cacheDeviceToken = (String) RedisUtils.getHashKey("loginInfo"+memberId, "deviceToken");
	    HashMap<String, Object> payLoad = new HashMap<>();
	    payLoad.put("id", memberId);
	    payLoad.put("deviceToken", cacheDeviceToken);
		String memberToken = JwtUtil.generToken(payLoad,Global.getTokenTimeout());
		result.setMemberToken(memberToken);
		return ResponseData.success("轮询消息回复", result);
	}

}