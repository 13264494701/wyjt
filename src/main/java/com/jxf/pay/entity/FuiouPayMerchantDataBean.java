package com.jxf.pay.entity;

import com.jxf.svc.config.Global;

/**
 * @description 富友充值商户号信息
 * @author Administrator
 *
 */
public class FuiouPayMerchantDataBean{
	/**
	 * H5富友充值请求地址
	 */
	public static final String PAY_REQUEST_URL= "https://mpay.fuioupay.com:16128/h5pay/payAction.pay";
	/**
	 * 富友订单号查询请求地址
	 */
	public static final String FUIOU_ORDER_QUERY_URL= "https://mpay.fuioupay.com:16128/findPay/queryOrderId.pay";
	/**
	 * 商户订单号查询地址
	 */
	public static final String MCHNT_ORDER_QUERY_URL= "https://mpay.fuioupay.com:16128/checkInfo/checkResult.pay";
	/**
	 * 失败跳转页面
	 */
	public static final String FAIL_RETURN_URL= Global.getConfig("domain")+"/gxt/charge_err";
//	public static final String NOTIFY_URL= "https://test.yxinbao.com/callback/fuiou/withdrawSuccessNotify";
	/**
	 * 成功跳转页面
	 */
	public static final String HOME_RETURN_URL= Global.getConfig("domain")+"/gxt/charge_success";
//	public static final String REFUND_URL= "https://test.yxinbao.com/callback/fuiou/withdrawRefundNotify";

	
}
