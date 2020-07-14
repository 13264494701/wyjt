package com.jxf.pay.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jxf.payment.entity.Payment;
import com.jxf.wx.account.entity.WxAccount;


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
public interface WxPayService {
	/**
	 * 函数功能说明 系统支付处理
	 * lixin  2016年8月17日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param record     
	 * @return void    
	 * @throws
	 */
	public Map<String, String> payUnifiedOrder(Payment payment,WxAccount wxAccount);
	
	/**
	 * 函数功能说明 回调处理
	 * lixin  2016年8月17日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param request
	 * @参数： @return     
	 * @return boolean    
	 * @throws
	 */
	public boolean wxNotifyProcess(HttpServletRequest request);
}
