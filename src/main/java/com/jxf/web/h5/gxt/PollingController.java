package com.jxf.web.h5.gxt;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.mem.service.MemberService;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Global;
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
@Controller("gxtH5PollingController")
@RequestMapping(value="${gxtH5}/polling")
public class PollingController extends BaseController {

	@Autowired
	private MemberService memberService;

	/**
	 * 客户端轮询
	 */

	@RequestMapping(value = "/notice", method = RequestMethod.POST)
	public @ResponseBody
	ResponseData polling(HttpServletRequest request) {
		PollingNoticeResponseResult result = new PollingNoticeResponseResult();
        Long memberId = memberService.getCurrent2().getId();
		
		Object newMsgCountObj = RedisUtils.getHashKey("memberInfo"+memberId, "newMsgCount");

		if(newMsgCountObj!=null) {
			Integer newMsgCount = (Integer)newMsgCountObj;
			result.setNewMsgCount(newMsgCount);
		}

		//更新memberToken
	    HashMap<String, Object> payLoad = new HashMap<>();
	    payLoad.put("id", memberId);
		String memberToken = JwtUtil.generToken(payLoad,Global.getTokenTimeout());
		result.setMemberToken(memberToken);
		return ResponseData.success("轮询消息回复", result);
	}

}