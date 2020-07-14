package com.jxf.web.callback;

import com.jxf.pay.service.FuyouPayService;
import com.jxf.pwithdraw.service.FuiouWithdrawService;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/callback/fuiou")
public class FuiouCallBackController {
	@Autowired
	private FuyouPayService fuyouPayService;
	@Autowired
	private FuiouWithdrawService fuiouWithdrawService;
	/**
	 * APP富友充值回调
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("fuiouPayNotifyForApp")
	@ResponseBody
	public String fuiouPayNotifyForApp(HttpServletRequest request) {
		boolean res = fuyouPayService.fuiouNotifyProcessforApp(request);
		if (res) {
			return "true";
		} else {
			return "false";
		}
	}
	/**
	 * 富友提现成功回调通知
	 * @param request
	 * @return
	 */
	@RequestMapping("withdrawSuccessNotify")
	@ResponseBody
	public String withdrawSuccessNotify(HttpServletRequest request) {
		boolean res = fuiouWithdrawService.successNotifyProcess(request);
		if(res) {
			return "1";
		}else {
			return "0";
		}
	}
	
	/**
	 * 富友提现失败退款回调通知
	 * @param request
	 * @return
	 */
	@RequestMapping("withdrawRefundNotify")
	@ResponseBody
	public String withdrawRefundNotify(HttpServletRequest request) {
		boolean res = fuiouWithdrawService.refundNotifyProcess(request);
		if(res) {
			return "1";
		}else {
			return "0";
		}
	}
	
}