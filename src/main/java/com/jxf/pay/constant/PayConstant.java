package com.jxf.pay.constant;

/**
 * @类功能说明：静态变量
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：zhuhuijie 
 * @创建时间：2016年6月20日 下午3:44:10 
 * @版本：V1.0
 */
public class PayConstant {
	/**
	 * 支付类型
	 */
	public static final String  QUICK= "001";  // 快捷支付
	public static final String  NETWORK= "002"; // 网银支付
	
	/**
	 * 卡种
	 */
	public static final String  CARDTYPE_C= "C";  // 信用卡
	public static final String  CARDTYPE_D= "D"; // 储蓄卡
	
	/**支付等待*/
	public static final String  WAIT_PAY= "001";
	/**支付成功*/
	public static final String  SUCCESS_PAY= "002"; 
	/**支付失败*/
	public static final String  FAIL_PAY= "003"; 
}
