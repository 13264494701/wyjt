package com.jxf.web.callback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.service.NfsActService;
import com.jxf.payment.entity.Payment;
import com.jxf.payment.service.PaymentService;
import com.jxf.pwithdraw.service.LianlianPayService;
import com.jxf.pwithdraw.utils.SignUtil;
import com.jxf.pwithdraw.utils.TraderRSAUtil;
import com.jxf.svc.config.Constant;
import com.jxf.svc.plugin.PluginConfig;
import com.jxf.svc.plugin.lianlianPayment.LianlianPaymentPlugin;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.JSONUtil;
import com.jxf.ufang.entity.UfangBrn;
import com.jxf.ufang.entity.UfangRchgRecord;
import com.jxf.ufang.entity.UfangUser;
import com.jxf.ufang.service.UfangBrnService;
import com.jxf.ufang.service.UfangRchgRecordService;
import com.jxf.ufang.service.UfangUserService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;



/**
 * @author Administrator
 */
@RestController
@RequestMapping(value = "/callback/lianlian")
public class LianLianCallbackController {

	private static final Logger log = LoggerFactory.getLogger(LianLianCallbackController.class);
	
    @Autowired
    private LianlianPaymentPlugin lianlianPaymentPlugin;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UfangRchgRecordService ufangRchgRecordService;

    @Autowired
    private UfangUserService ufangUserService;
	@Autowired
	private NfsActService actService;
    @Autowired
    private UfangBrnService ufangBrnService;
    @Autowired
    private LianlianPayService lianlianPayService;

    @PostMapping(value = "")
    public Map<String,String> callback(@RequestBody String request) {

        Map<String, String> map = new HashMap<>(16);
  
        if (StringUtils.isBlank(request)) {
        	log.error("连连回调接收为空");
            map.put("ret_code", "9999");
            map.put("ret_msg", "交易失败");
            return map;
        }
        Map<String, Object> result = JSONUtil.toMap(request);
        String sign = (String) result.get("sign");
        JSONObject jsonObject = JSONObject.parseObject(request);
        try {
            if (!checkSign(sign, jsonObject)) {
            	log.error("连连回调验签失败");
                map.put("ret_code", "9999");
                map.put("ret_msg", "交易失败0");
                return map;
            }
        } catch (Exception e) {
        	log.error("连连回调验签失败,{}",Exceptions.getStackTraceAsString(e));
            map.put("ret_code", "9999");
            map.put("ret_msg", "交易失败1");
            return map;
        }


        Payment payment = paymentService.get(Long.parseLong((String) result.get("no_order")));
        if (Payment.Status.success.equals(payment.getStatus())) {
        	log.error("连连回调对应支付已成功");
            map.put("ret_code", "9999");
            map.put("ret_msg", "交易失败3");
            return map;
        }
        BigDecimal amount = new BigDecimal((String) result.get("money_order"));
        UfangRchgRecord ufangRchgRecord = ufangRchgRecordService.get(payment.getOrgId());
        if (amount.compareTo(payment.getPaymentAmount()) != 0) {
            log.error("连连回调支付金额不一致");
            map.put("ret_code", "9999");
            map.put("ret_msg", "交易失败2");
            return map;
        }
        
        payment.setStatus(Payment.Status.success);
        payment.setThirdPaymentNo((String) result.get("oid_paybill"));
        paymentService.save(payment);
        UfangUser ufangUser = ufangUserService.get(ufangRchgRecord.getUser());
        UfangBrn ufangBrn = ufangBrnService.get(ufangUser.getBrn().getParent());
        int code = actService.updateAct(TrxRuleConstant.UFANG_RECHARGE, amount, ufangBrn,ufangRchgRecord.getId());
		if(code == Constant.UPDATE_FAILED) {
			log.error("优放机构[{}]账户更新失败,金额[{}],充值记录编号[{}]",ufangBrn.getId(),amount,ufangRchgRecord.getId());
            map.put("ret_code", "9999");
            map.put("ret_msg", "交易失败2");
            return map;
		}
        ufangRchgRecord.setStatus(UfangRchgRecord.Status.success);
		ufangRchgRecord.setPayment(payment);
        ufangRchgRecordService.save(ufangRchgRecord);
        int t = 0;
        if (amount.compareTo(new BigDecimal("10000")) >= 0) {
            t = 100;
            if (amount.compareTo(new BigDecimal("20000")) >= 0) {
                t = 300;
                if (amount.compareTo(new BigDecimal("30000")) >= 0) {
                    t = 500;
                    if (amount.compareTo(new BigDecimal("40000")) >= 0) {
                        t = 800;
                        if (amount.compareTo(new BigDecimal("50000")) >= 0) {
                            t = 2000;
                        }
                    }
                }
            }
        }
        ufangBrnService.updateFreeData(t, ufangBrn.getBrnNo());
        map.put("ret_code", "0000");
        map.put("ret_msg", "交易成功");
      
        return map;
    }


    private boolean checkSign(String sign, JSONObject jsonObject) {

        PluginConfig config = lianlianPaymentPlugin.getPluginConfig();
        Map<String, String> configAttr = config.getAttributeMap();
        String key = configAttr.get(LianlianPaymentPlugin.PAYMENT_LLPUBLIC_KEY_ATTRIBUTE_NAME);

        return TraderRSAUtil.checksign(key, SignUtil.genSignData(jsonObject), sign);
    }
    
    /**
     * APP连连提现回调
     * @param request
     * @return
     */
    @RequestMapping(value="llnotifyForApp")
    @ResponseBody
	public String notifyProcess(HttpServletRequest request) {
		Boolean result = lianlianPayService.notifyProcess(request);
		if(result) {
			Map<String, String> jsonMap = new HashMap<String,String>(); 
			jsonMap.put("ret_code", "0000");
			jsonMap.put("ret_msg", "交易成功");
			return JSON.toJSONString(jsonMap);
		}else {
			Map<String, String> jsonMap = new HashMap<String,String>(); 
			jsonMap.put("ret_code", "9999");
			jsonMap.put("ret_msg", "未知异常");
			return JSON.toJSONString(jsonMap);
		}
	}
    
    /**
     * APP连连充值回调
     * @param request
     * @return
     */
    @RequestMapping(value="rechargeNotify")
    @ResponseBody
	public String rechargeNotify(HttpServletRequest request) {
		Boolean result = lianlianPayService.rechargeNotify(request);
		if(result) {
			Map<String, String> jsonMap = new HashMap<String,String>(); 
			jsonMap.put("ret_code", "0000");
			jsonMap.put("ret_msg", "交易成功");
			return JSON.toJSONString(jsonMap);
		}else {
			Map<String, String> jsonMap = new HashMap<String,String>(); 
			jsonMap.put("ret_code", "9999");
			jsonMap.put("ret_msg", "未知异常");
			return JSON.toJSONString(jsonMap);
		}
	}
    
    
}