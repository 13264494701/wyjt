package com.jxf.pay.entity;

/**
 * @description 富友充值商户号信息
 * @author Administrator
 *
 */
public class LianlianMerchantDataBean{
	/**
	 * SDK充值请求地址
	 */
	public static final String PAY_REQUEST_URL= "https://payserverapi.lianlianpay.com/v1/paycreatebill";
	/**
	 * 充值订单结果查询请求地址
	 */
	public static final String QUERY_PAY_RESULT_REQUEST_URL= "https://queryapi.lianlianpay.com/orderquery.htm";
}
