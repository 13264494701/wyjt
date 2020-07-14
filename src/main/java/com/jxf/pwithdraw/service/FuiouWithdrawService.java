package com.jxf.pwithdraw.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jxf.nfs.entity.NfsWdrlRecord;
import com.jxf.pwithdraw.entity.fuiou.FuiouPaymentRequestBean;
import com.jxf.pwithdraw.entity.fuiou.FuiouQueryPaymentRequestBean;



/**
 * @类功能说明： 富友代付接口
 * @作者：Richard.Su 
 * @创建时间：2019年4月12日 上午10:15:58 
 * @版本：V1.0
 */
public interface FuiouWithdrawService {
	/**
	 *  发送付款交易请求
	 * @param paymentRequestBean 请求数据
	 * @return
	 */
	String sendData(FuiouPaymentRequestBean fuiouPaymentRequestBean);
	
	/**
	 * 处理APP端发送富友提现请求
	 * @param wdrlRecord
	 */
	void sendFuiouPaymentRequestForAPP(NfsWdrlRecord wdrlRecord);
	
	/**
	 * 富友提现回调处理
	 * @param request
	 */
	boolean successNotifyProcess(HttpServletRequest request);
	
	/**
	 * 富友提现退款回调处理
	 * @param request
	 */
	boolean refundNotifyProcess(HttpServletRequest request);
	
	/**
	 * 发送交易查询请求
	 * @return
	 */
	String queryOrder(FuiouQueryPaymentRequestBean fuiouQueryPaymentRequestBean);
	
	/**
	 * @description 代付订单查询
	 * @param wdrlRecord
	 */
	Map<String, String> queryFuiouPayment(NfsWdrlRecord wdrlRecord);
	/**
	 * 查询富友账户余额
	 * @return
	 */
	Map<String, String> queryAccount();
	
}
