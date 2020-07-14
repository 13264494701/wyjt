package com.jxf.pay.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jxf.mem.entity.MemberVerified;
import com.jxf.nfs.entity.NfsBankProtocol;
import com.jxf.payment.entity.Payment;


/**
 * @类功能说明： 支付接口
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：lixin 
 * @创建时间：2016年8月10日 上午10:15:58 
 * @版本：V1.0
 */
public interface FuyouPayService {
	/**
	 * 发送签约短信验证码
	 * @return
	 */
	public Map<String, Object> sendContractSms(MemberVerified memberVerified,Payment payment);
	/**
	 * 协议绑卡
	 * @return
	 */
	public Map<String, Object> bindCommit(MemberVerified memberVerified,Payment payment,String vifyCode);
	/**
	 * 协议支付
	 * @return
	 */
	public Map<String, Object> newProPay(Payment payment);		
	/**
	 * 支付回调处理
	 * @param request
	 * @return
	 */
	public boolean fuiouNotifyProcess(HttpServletRequest request);
	/**
	 *解绑
	 * @param request
	 * @return
	 */
	public Map<String, Object> unBind(NfsBankProtocol protocol);
	/**
	 * 支付回调处理for app
	 * @param request
	 * @return
	 */
	boolean fuiouNotifyProcessforApp(HttpServletRequest request);
	
	/**
	 * 根据商户订单号查询支付结果
	 * @return
	 */
	 Map<String, String> queryPayResultByMchOrderNo(String orderNo);
}
