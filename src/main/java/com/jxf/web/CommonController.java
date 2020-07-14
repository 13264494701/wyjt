package com.jxf.web;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.entity.NfsLoanPreservation;
import com.jxf.loan.service.NfsLoanPreservationService;
import com.jxf.loan.signature.youdun.YouDunESignature;
import com.jxf.loan.signature.youdun.YouDunConstant.NodeType;
import com.jxf.loan.signature.youdun.preservation.UdPreservationInfo;
import com.jxf.loan.signature.youdun.preservation.YouDunPreservation;
import com.jxf.svc.captcha.Captcha;
import com.jxf.svc.captcha.GifCaptcha;
import com.jxf.svc.captcha.SpecCaptcha;
import com.jxf.svc.security.rsa.RSAService;
import com.jxf.svc.sys.area.entity.Area;
import com.jxf.svc.sys.area.service.AreaService;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.ImageCaptchaUtils;
import com.jxf.svc.utils.SnowFlake;


/**
 *
 * @类功能说明： 公用 @类修改者： @修改日期： @修改说明： @公司名称：
 * 
 * @作者：wo
 * @创建时间：2018年10月25日 上午10:56:53 @版本：V1.0
 */
@Controller("commonController")
@RequestMapping(value = "${common}")
public class CommonController {

	private static final Logger log = LoggerFactory.getLogger(CommonController.class);

	@Autowired
	private RSAService rsaService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private NfsLoanPreservationService loanPreservationService;
	

	@RequestMapping(value = "/publicKey", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> publicKey(HttpServletRequest request) {
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
	public @ResponseBody List<Map<String, Object>> area(Long parentId) {
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
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "getImageCaptcha")
	public void getImageCaptcha(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {
		ImageCaptchaUtils.createImageCaptcha(request, response);
	}

	/**
	 * 获取验证码（Gif版本）
	 * 
	 * @param response
	 */
	@RequestMapping(value = "getGifCaptcha", method = RequestMethod.GET)
	public void getGifCode(HttpServletResponse response, HttpServletRequest request) {
		try {
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("image/gif");
			/**
			 * gif格式动画验证码 宽，高，位数。
			 */
			Captcha captcha = new GifCaptcha(146, 33, 4);
			// 输出
			captcha.out(response.getOutputStream());
			HttpSession session = request.getSession(true);
			// 存入Session
			session.setAttribute("_code", captcha.text().toLowerCase());
			log.debug(captcha.text().toLowerCase());
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
	}

	/**
	 * 获取验证码（jpg版本）
	 * 
	 * @param response
	 */
	@RequestMapping(value = "getJPGCaptcha", method = RequestMethod.GET)
	public void getJPGCode(HttpServletResponse response, HttpServletRequest request) {
		try {
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("image/jpg");
			/**
			 * jgp格式验证码 宽，高，位数。
			 */
			Captcha captcha = new SpecCaptcha(146, 33, 4);
			// 输出
			captcha.out(response.getOutputStream());
			HttpSession session = request.getSession(true);
			// 存入Session
			session.setAttribute("_code", captcha.text().toLowerCase());
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
	}
	
	/**
	 * 上传平台认证信息
	 * @return
	 */
	@RequestMapping(value="uploadPlatformInfo")
	public void uploadPlatformInfo() {
		Long proofChainId = SnowFlake.getId();
		Long platformPartnerOrderId = SnowFlake.getId();
		UdPreservationInfo platformIdentifyUploadInfo = new UdPreservationInfo();
		platformIdentifyUploadInfo.setProofChainId(proofChainId.toString());
		platformIdentifyUploadInfo.setPartnerOrderId(platformPartnerOrderId.toString());
		platformIdentifyUploadInfo.setParentOrderId(platformPartnerOrderId.toString());
		platformIdentifyUploadInfo.setNodeType(NodeType.identificationInfo);
		platformIdentifyUploadInfo.setPreservationBuilderData(null);
		JSONObject platformIdentifyResponse = YouDunPreservation.uploadInfo(platformIdentifyUploadInfo);
		log.info("平台认证上传返回结果：{}",platformIdentifyResponse);
		Boolean platformIdentifyResult = platformIdentifyResponse.getBoolean("success");
		if(platformIdentifyResult) {
			JSONObject platformIdentifyData = platformIdentifyResponse.getJSONObject("data");
			String platformIdentifyPreCode = platformIdentifyData.getString("pre_code");
			NfsLoanPreservation platformPreservation = new NfsLoanPreservation();
			platformPreservation.setProofChainId(proofChainId);
			platformPreservation.setBusinessId(proofChainId);
			platformPreservation.setNodeType(NfsLoanPreservation.NodeType.IDENTIFY);
			platformPreservation.setParentOrderId(platformPartnerOrderId);
			platformPreservation.setPartnerOrderId(platformPartnerOrderId);
			platformPreservation.setPrecode(platformIdentifyPreCode);
			loanPreservationService.save(platformPreservation);
			log.info("有盾业务保全上传平台认证信息成功");
		}else {
			String errorCode = platformIdentifyResponse.getString("errorCode");
			String message = platformIdentifyResponse.getString("message");
			log.error("上传平台认证信息返回结果失败:errorCode{},message:{}",  errorCode, message);
		}
	}
	
	@RequestMapping(value="queryContract")
	public void queryContract(String code) {
		YouDunESignature.querySign(code);
	}
	
}