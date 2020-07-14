/**    
 * @文件名称: WXPayServiceImpl.java 
 * @类路径: com.jxf.pay.service.impl 
 * @描述: TODO 
 * @作者：李新 
 * @时间：2016年8月10日 上午10:18:59 
 * @版本：V1.0    
 */ 
package com.jxf.pay.service.impl;


import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import com.jxf.svc.config.Global;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jxf.pay.entity.RefundOrder;
import com.jxf.pay.service.WxRefundService;
import com.jxf.payment.entity.Refund;
import com.jxf.payment.service.RefundService;
import com.jxf.svc.utils.HttpsRequest;
import com.jxf.svc.utils.MD5Code;
import com.jxf.svc.utils.StringUtils;
import com.jxf.svc.utils.XMLParser;
import com.jxf.wx.account.entity.WxAccount;
import com.jxf.wx.account.service.WxAccountService;
/**
 * 微信支付退款
 *
 * @author lixin
 * @since 1.2
 */
@Service("wxRefundService")
public class WxRefundServiceImpl implements WxRefundService {

	
	@Autowired
	private RefundService refundService;
	@Autowired
	private WxAccountService wxAccountService;
	
	protected Logger log = LoggerFactory.getLogger(getClass());
	
	/**
     * 退款申请
     *
     * @param payment 支付对象
     * @return 调用结果
     */
	@Override
	public Boolean payRefundApply(Refund refund) {

		log.debug("微信支付退款接口.....");
        try {

        	String refundOrderRsp = new HttpsRequest().sendPost(refund.getRequestUrl(),RefundOrderReqXmlObj(refund),refund.getMchId(),Global.getConfig("cert.key"));
        	Map<String, String> refundOrderRspMap = XMLParser.readStringXmlOut(refundOrderRsp);
        	log.info("微信支付退款接口返回：{}",refundOrderRspMap.toString());
        	String return_code = refundOrderRspMap.get("return_code");
        	if(StringUtils.isNotBlank(return_code) && return_code.equals("SUCCESS")){
        		refund.setRefundId(refundOrderRspMap.get("refund_id"));
        		String result_code = refundOrderRspMap.get("result_code");
        		if(StringUtils.isNotBlank(result_code) && result_code.equals("SUCCESS")) {
        			refund.setStatus(Refund.Status.success);
        		}else {
        			log.error("微信退款请求:{}返回失败：{}",refund.getId(),refundOrderRspMap.toString());
        			refund.setStatus(Refund.Status.failure);
        			return false;
        		}
        	}else{
        		log.error("微信支付退款提交出错{}！",return_code);
        		refund.setStatus(Refund.Status.other);
        		return false;
        	}
        } catch (ConnectException ce) {
        	log.error("连接超时："+ce.getMessage());
        	refund.setStatus(Refund.Status.other);
        	return false;
        } catch (Exception e) {
        	log.error("https请求异常："+e.getMessage());
        	refund.setStatus(Refund.Status.other);
        	return false;
        }
        return true;
	}
    
    //统一下单参数整理
    private  RefundOrder RefundOrderReqXmlObj(Refund refund) {
    	
    	WxAccount wxAccount = wxAccountService.findByCode("gxt");
    	
    	RefundOrder refundOrder =new RefundOrder();
    	refundOrder.setAppid(wxAccount.getAppid());
    	refundOrder.setMch_id(refund.getMchId());
    	refundOrder.setNonce_str(StringUtils.getRandomStringByLength(32));
    	refundOrder.setOut_trade_no(refund.getPayment().getPaymentNo());
    	refundOrder.setOut_refund_no(refund.getId() + "");
    	refundOrder.setTotal_fee(StringUtils.StrTOInt(refund.getPayment().getPaymentAmount().toString()));
    	refundOrder.setRefund_fee(StringUtils.StrTOInt(refund.getRefundAmount().toString()));
    	refundOrder.setNotify_url(refund.getNotifyUrl());
    	
    	//签名
    	String signTemp = "appid="+refundOrder.getAppid()
    			+"&mch_id="+refundOrder.getMch_id()
    			+"&nonce_str="+refundOrder.getNonce_str()
    			+"&notify_url="+refundOrder.getNotify_url()
    			+"&out_refund_no="+refundOrder.getOut_refund_no()
    			+"&out_trade_no="+refundOrder.getOut_trade_no()
    			+"&refund_fee="+refundOrder.getRefund_fee()
    			+"&total_fee="+refundOrder.getTotal_fee()
    			+"&key="+refund.getKey(); //商户号对应的秘钥
    	log.debug("签名字符串={}",signTemp);
    	String characterEncoding = "UTF-8"; 
    	String sign=MD5Code.MD5Encode(signTemp, characterEncoding).toUpperCase();
		log.debug("签名后的值"+sign);
    	refundOrder.setSign(sign);
        return refundOrder;
       }

    
    //微信支付结果回调方法
	@Override
	public boolean wxNotifyProcess(HttpServletRequest request) {

		log.info("============微信支付异步返回===========");
		// 获取微信POST过来反馈信息  
        String inputLine;  
        String notityXml = "";  
        try {  
  
            while ((inputLine = request.getReader().readLine()) != null) {  
                notityXml += inputLine;  
            }  
            
            request.getReader().close();  
            
        } catch (Exception e) {  
        	log.error("xml获取失败：" + e);  
        }  
        log.debug("收到微信异步回调：{}",notityXml);  
        if(StringUtils.isEmpty(notityXml)){  
        	log.error("xml为空：");  
        }  
        Map<String, String> result = new HashMap<>();
        result = XMLParser.readStringXmlOut(notityXml);
		String paymentNo = result.get("out_trade_no");
		String refundNo = result.get("out_refund_no");
		String respCode = result.get("return_code");
		String respMsg = result.get("return_msg");
		log.info("响应支付单:[{}]",paymentNo);
		log.info("响应退款单:[{}]",refundNo);
		log.info("respCode:[{}]",respCode);
		log.info("respMsg:[{}]",respMsg);
		Refund refund = refundService.getByRefundNo(refundNo);
		if(refund.getStatus().equals(Refund.Status.success)){
			return true;
		}
		
		if(StringUtils.equals("SUCCESS", respCode)){
				log.info("mac 验证成功");
				refund.setStatus(Refund.Status.success);
				refund.setRespCode(respCode);
				refund.setRespMsg(respMsg);
				refundService.refundFinishProcess(refund);
		        return true;
		}else{
			refund.setStatus(Refund.Status.failure);
			refund.setRespCode(respCode);
			refund.setRespMsg(respMsg);
			refundService.refundFinishProcess(refund);
	        return false;
		}
	}
}
