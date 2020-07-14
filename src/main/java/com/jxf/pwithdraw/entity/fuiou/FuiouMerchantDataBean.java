package com.jxf.pwithdraw.entity.fuiou;
/**
 * @description fuiou withdraw data
 * @author Administrator
 *
 */
public class FuiouMerchantDataBean{
	/**
	 * 商户id
	 */
	public static final String MERID = "0001000F1778471";
	/**
	 * 密钥
	 */
	public static final String KEY = "5jsw4j3eb05mpwy72slbyoqphc56yoin";
	/**
	 * 代付请求地址
	 */
	public static final String PAY_REQUEST_URL= "https://fht-api.fuioupay.com/req.do";
	/**
	 * 查询请求地址
	 */
	public static final String QUERY_REQUEST_URL= "https://fht-api.fuioupay.com/qry.do";
	/**
	 * 代付账户余额查询地址
	 */
	public static final String ACCOUNT_QUERY_URL= "https://fht-api.fuioupay.com/t0zj_qryAcnt.do";
	/**
	 * 代付回调通知地址
	 */
	public static final String NOTIFY_URL= "https://prod.51jt.com/callback/fuiou/withdrawSuccessNotify";
//	public static final String NOTIFY_URL= "https://test.yxinbao.com/callback/fuiou/withdrawSuccessNotify";
	/**
	 * 退款通知地址
	 */
	public static final String REFUND_URL= "https://prod.51jt.com/callback/fuiou/withdrawRefundNotify";
//	public static final String REFUND_URL= "https://test.yxinbao.com/callback/fuiou/withdrawRefundNotify";
	/**
	 * 交易请求类型：代付
	 */
	public static final String PAY_REQUEST_TYPE= "payforreq";
	/**
	 * 交易请求版本号
	 */
	public static final String PAY_REQUEST_VER= "1.0";
	/**
	 * 查询请求版本号
	 */
	public static final String QUERY_REQUEST_VER= "1.1";
	/**
	 * 业务代码
	 */
	public static final String BUSINESS_CODE= "AP01";
	/**
	 * 交易来源
	 */
	public static final String SRC_MODULE_CD= "HMP";
	
}
