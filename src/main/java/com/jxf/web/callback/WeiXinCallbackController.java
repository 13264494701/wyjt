package com.jxf.web.callback;

import com.jxf.pay.service.WxPayService;
import com.jxf.svc.utils.Exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;



/**
 * @author Administrator
 */
@RestController
@RequestMapping(value = "/callback/wx")
public class WeiXinCallbackController {
	
	private static final Logger log = LoggerFactory.getLogger(WeiXinCallbackController.class);
	@Autowired
	private WxPayService wxPayService;
		
	/**
	 * 微信支付结果通知
	 */
	@RequestMapping(value = "payNotify")
	@ResponseBody
	public String payNotify(HttpServletRequest request) {
		try {
			wxPayService.wxNotifyProcess(request);
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return null;
	}
	
	/**
	 * 微信退款结果通知
	 */
	@RequestMapping(value = "refundNotify")
	@ResponseBody
	public String refundNotify(HttpServletRequest request) {
		try {
//			wxPayService.wxNotifyProcess(request);
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return null;
	}
	
	
	
}