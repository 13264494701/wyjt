package com.jxf.web.app.wyjt.member;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberExtend;
import com.jxf.mem.service.MemberExtendService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.VerifiedUtils;

import com.jxf.rc.service.RcCaDataService;

import com.jxf.svc.cache.RedisUtils;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.member.EmergencyListRequestParam;
import com.jxf.web.model.wyjt.app.member.EmergencyListResponseResult;

/**
 * Controller - 会员扩展
 * 
 * @author xrd
 */
@Controller("wyjtAppMemberExtendController")
@RequestMapping(value="${wyjtApp}/member")
public class MemberExtendController extends BaseController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberExtendService memberExtendService;
	@Autowired
	private RcCaDataService rcCaDataService;

	
	/**
	 * 获取紧急联系人
	 */
	@RequestMapping(value = "/getEmergencyList", method = RequestMethod.POST)
	public @ResponseBody
	ResponseData getEmergencyList(HttpServletRequest request,HttpServletResponse response){
		Member member = memberService.getCurrent2();
		MemberExtend memberExtend = memberExtendService.getByMember(member);
		EmergencyListResponseResult result = new EmergencyListResponseResult();
		if(memberExtend != null && memberExtend.getEcp1() != null){	
			result.setEcp1(memberExtend.getEcp1());
			result.setEcp2(memberExtend.getEcp2());
			result.setEcp3(memberExtend.getEcp3());
			result.setEcpPhoneNo1(memberExtend.getEcpPhoneNo1());
			result.setEcpPhoneNo2(memberExtend.getEcpPhoneNo2());
			result.setEcpPhoneNo3(memberExtend.getEcpPhoneNo3());
			return ResponseData.success("紧急联系人查询成功", result);
		}else{
			return ResponseData.error("您还没有填写紧急联系人");
		}
	}
	/**
	 * 保存紧急联系人
	 */
	@RequestMapping(value = "/saveEmergencyList", method = RequestMethod.POST)
	public @ResponseBody
	ResponseData saveEmergencyList(HttpServletRequest request){
		
		String param = request.getParameter("param");
		EmergencyListRequestParam reqData = JSONObject.parseObject(param,EmergencyListRequestParam.class);
		
		String ecp1 = reqData.getEcp1();
		String ecp2 = reqData.getEcp2();
		String ecp3 = reqData.getEcp3();
		String ecpPhoneNo1 = reqData.getEcpPhoneNo1();
		String ecpPhoneNo2 = reqData.getEcpPhoneNo2();
		String ecpPhoneNo3 = reqData.getEcpPhoneNo3();
		if(StringUtils.isBlank(ecp1)|| StringUtils.isBlank(ecp2) || StringUtils.isBlank(ecp3) || 
				StringUtils.isBlank(ecpPhoneNo1) || StringUtils.isBlank(ecpPhoneNo2) || StringUtils.isBlank(ecpPhoneNo3)){
		   return ResponseData.error("紧急联系人信息填写不全");
		} 
		if(StringUtils.contains(ecp1, " ")||StringUtils.contains(ecp2, " ")||StringUtils.contains(ecp3, " ")
				||StringUtils.contains(ecpPhoneNo1, " ")||StringUtils.contains(ecpPhoneNo2, " ")||StringUtils.contains(ecpPhoneNo3, " ")){
			return ResponseData.error("紧急联系人信息包含空格");
		}
		Member member = memberService.getCurrent();
		MemberExtend memberExtend = memberExtendService.getByMember(member);
		if(memberExtend == null){
			memberExtend = new MemberExtend();
			memberExtend.setMember(member);
		}
		memberExtend.setEcp1(ecp1);
		memberExtend.setEcpPhoneNo1(ecpPhoneNo1);
		memberExtend.setEcp2(ecp2);
		memberExtend.setEcpPhoneNo2(ecpPhoneNo2);
		memberExtend.setEcp3(ecp3);
		memberExtend.setEcpPhoneNo3(ecpPhoneNo3);
		memberExtendService.save(memberExtend);
		Integer verifiedList = member.getVerifiedList();
		Integer addVerified = VerifiedUtils.addVerified(verifiedList, 24);
		member.setVerifiedList(addVerified);
		memberService.save(member);
		RedisUtils.put("memberInfo"+member.getId(),"emergencyStatus",1);
		rcCaDataService.createEmergencyContact(member,ecp1,ecpPhoneNo1,ecp2,ecpPhoneNo2,ecp3,ecpPhoneNo3);
		return ResponseData.success("紧急联系人保存成功");
	}
	

}