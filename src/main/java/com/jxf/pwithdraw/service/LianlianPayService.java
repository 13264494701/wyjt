package com.jxf.pwithdraw.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jxf.nfs.entity.NfsWdrlRecord;
import com.jxf.pwithdraw.entity.ConfirmPaymentRequestBean;
import com.jxf.pwithdraw.entity.PaymentRequestBean;
import com.jxf.pwithdraw.entity.QueryPaymentRequestBean;



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
public interface LianlianPayService {
	/**
	 *  发送付款交易请求
	 * @param paymentRequestBean 请求数据
	 * @return
	 */
	String sendData(PaymentRequestBean paymentRequestBean);
	/**
	 * 	付款单确认
	 * @param paymentRequestBean 请求数据
	 * @return
	 */
	String confirm(ConfirmPaymentRequestBean paymentRequestBean);
	/**
	 * 付款结果查询
	 * @param queryRequestBean 查询请求数据
	 * @return
	 */
	String queryOrder(QueryPaymentRequestBean queryRequestBean);
	/**
	 * 连连支付付款结果异步通知
	 * @param request
	 * @param member 
	 * @return
	 */
	boolean notifyProcess(HttpServletRequest request);
	/**
	 * 处理app端发送连连提现请求
	 * @param wdrlRecord
	 */
	void sendLianLianPaymentRequestForAPP(NfsWdrlRecord wdrlRecord);
	/**
	 * 处理需要查询的连连提现订单
	 * @param orderId
	 */
	void queryLianLianPayMent(String orderId);
	/**
	 * 确认订单
	 * @param orderId 订单号
	 * @param confirm_code 确认码
	 */
	void confirmLianLianPayment(String orderId, String confirm_code);
	
	/**
	 *   充值回调
	 * @param request
	 */
	boolean rechargeNotify(HttpServletRequest request);
	/**
	 * 根据订单号查询充值订单
	 * @return
	 */
	Map<String, String> queryRchgRecordByOrderNo(String orderNo);
	
}
