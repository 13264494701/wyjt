package com.jxf.web.minipro;


import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.mms.consts.MmsConstant;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.mms.msg.impl.SendSmsMsgServiceImpl;
import com.jxf.mms.msg.utils.VerifyCodeUtils;
import com.jxf.mms.record.entity.MmsSmsRecord;
import com.jxf.mms.record.service.MmsSmsRecordService;

import com.jxf.svc.cache.CacheConstant;
import com.jxf.svc.cache.CacheUtils;
import com.jxf.svc.model.JsonpRsp;
import com.jxf.svc.model.Message;
import com.jxf.svc.security.rsa.RSAService;
import com.jxf.svc.servlet.ValidateCodeServlet;
import com.jxf.svc.sys.area.entity.Area;
import com.jxf.svc.sys.area.service.AreaService;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.ImageCaptchaUtils;
import com.jxf.svc.utils.JsonpUtils;
import com.jxf.web.model.ResponseData;




/**
 *
 * @类功能说明： 公用
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator
 * @创建时间：2016年12月19日 上午10:56:53
 * @版本：V1.0
 */
@Controller("miniproCommonController")
@RequestMapping(value="${commonMinipro}")
public class CommonController {

	@Autowired
	private RSAService rsaService;
	@Autowired
	private AreaService areaService;

	@Autowired
	private SendSmsMsgService sendSmsMsgService;
	@Autowired
	private MmsSmsRecordService mmsSmsRecordService;
	@Autowired
	private SendSmsMsgServiceImpl sendSmsMsgServiceImpl;



	/**
	 * 公钥
	 */
	@RequestMapping(value = "/public_key", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, String> publicKey(HttpServletRequest request) {
		RSAPublicKey publicKey = rsaService.generateKey(request);
		Map<String, String> data = new HashMap<String, String>();
		data.put("modulus", Base64.encodeBase64String(publicKey.getModulus().toByteArray()));
		data.put("exponent", Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray()));
		return data;
	}

	/**
	 * 地区
	 */
	@RequestMapping(value = "/area", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> area(Long parentId) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		Area parent = areaService.get(parentId);
		Collection<Area> areas = parent != null ? areaService.getChildren(parent) : areaService.getRoot();
		for (Area area : areas) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("name", area.getName());
			item.put("value", area.getId());
			data.add(item);
		}
		return data;
	}
	/**
	 * 获取图形验证码
	 * @throws IOException
	 */
	@RequestMapping(value="getImageCaptcha")
	public void getImageCaptcha(HttpServletRequest request, HttpServletResponse response,HttpSession session) throws IOException {
		ImageCaptchaUtils.createImageCaptcha(request, response);
	}
	/**
	 * 验证图形验证码
	 */
	@RequestMapping(value="checkImageCaptcha")
	public void checkImageCaptcha(HttpServletRequest req, HttpServletResponse response,HttpSession session) {
		JsonpRsp jsonpRsp = new JsonpRsp();
		String checkcode = req.getParameter("image_captcha");
		String code = (String)session.getAttribute(ValidateCodeServlet.VALIDATE_CODE);
		if(!checkcode.toUpperCase().equals(code)){
			jsonpRsp.setObj("验证码错误，请重新填写！");
			jsonpRsp.setErrno(-1);
		}else{
			jsonpRsp.setErrno(0);
		}
		JsonpUtils.callback(req, response, jsonpRsp);
	}
	/**
	 * 验证图形验证码并发送短信验证码
	 */
	@RequestMapping(value = "/sendSmsVerifyCode", method = RequestMethod.POST)
	public @ResponseBody
	ResponseData sendSmsVerifyCode(HttpServletRequest request,String msgType,String phoneNo,String imageCaptcha,String captchaId) {
		Map<String, Object> data = new HashMap<String, Object>();

		String verfyImageCaptcha = (String) CacheUtils.get(CacheConstant.TEMP_CACHE,CacheConstant.TEMP_CACHE+captchaId);
		if(StringUtils.isNotBlank(verfyImageCaptcha)&&verfyImageCaptcha.equalsIgnoreCase(imageCaptcha)) {

			data.put("resultCode", "00");
			//生成验证码
			String smsCode = VerifyCodeUtils.genSmsValidCode();
			//将验证码存入缓存中
			CacheUtils.put(CacheConstant.TEMP_CACHE, CacheConstant.TEMP_CACHE+phoneNo, smsCode);

			sendSmsMsgServiceImpl.sendSms(msgType, phoneNo,smsCode);
		}else {
			data.put("resultCode", "30");
			data.put("resultMsg", "图形验证码错误 请重试");
			return ResponseData.success("图形验证码错误 请重试",data);
		}
		return ResponseData.success("发送短信验证码成功",data);
	}
	/**
	 * 发送短信验证码
	 */
	@ResponseBody
	@RequestMapping(value="sendSMSCaptcha")
	public Message sendSMSCaptcha(HttpServletRequest req, HttpServletResponse response,HttpSession session){

		String phoneNo = req.getParameter("phoneNo");

		String msgType = req.getParameter("msgType");
		String smsCode = VerifyCodeUtils.genSmsValidCode();


		sendSmsMsgService.sendSms(msgType,phoneNo,smsCode);
		return Message.success("发送成功");
	
	}
	/**
	 * 验证短信验证码
	 */
	@RequestMapping(value="checkSMSCaptcha")
	public void checkSMSCaptcha(HttpServletRequest req, HttpServletResponse response,HttpSession session){

		JsonpRsp jsonpRsp = new JsonpRsp();
		String phoneNo = req.getParameter("mobile");
		String captcha = req.getParameter("captcha");
		String msgType = req.getParameter("msgType");
		MmsSmsRecord smsRecord = mmsSmsRecordService.getSendedVerifyCode(msgType,phoneNo, captcha);
		if(smsRecord == null){
			jsonpRsp.setErrno(-1);
			jsonpRsp.setObj("短信验证码错误");
		}else{
			if(CalendarUtil.addSecond(smsRecord.getSendTime(), MmsConstant.SMS_OUTTIME).before(new Date())){
				jsonpRsp.setErrno(-1);
				jsonpRsp.setObj("短信验证码过期");
			}else{
				jsonpRsp.setErrno(0);
				jsonpRsp.setObj(smsRecord.getId());
			}
		}
		JsonpUtils.callback(req, response, jsonpRsp);
	}
	/**
	 * 错误提示
	 */
	@RequestMapping("/error")
	public String error() {
		return "/common/error";
	}

	/**
	 * 资源不存在
	 */
	@RequestMapping("/resource_not_found")
	public String resourceNotFound() {
		return "/common/resource_not_found";
	}



}