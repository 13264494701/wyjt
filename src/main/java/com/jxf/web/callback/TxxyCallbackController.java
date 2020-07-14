package com.jxf.web.callback;

import java.security.PrivateKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.jxf.rc.entity.RcTxxy;
import com.jxf.rc.entity.RcTxxyModel;
import com.jxf.rc.service.RcTxxyService;
import com.jxf.rc.utils.SecurityUtil;
import com.jxf.svc.config.Global;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.RSAUtils;
import com.jxf.web.model.ResponseData;

/**
 * @author 天下信用回调
 */
@RestController
@RequestMapping(value = "/callback/txxy")
public class TxxyCallbackController {

	private static Logger log = LoggerFactory.getLogger(TxxyCallbackController.class);

	// pub_key
	static final String PUB_KEY = Global.getConfig("publicKeyString1");
	
	// pub_key
	static final String PRI_KEY = Global.getConfig("privateKeyString1");
	
	
	@Autowired
	private RcTxxyService rcTxxyService;

	@PostMapping(value = "")
	public ResponseData callback(@RequestBody RcTxxyModel rcTxxyModel) {
		if (log.isDebugEnabled()) {
			log.debug("天下信用回调成功");
			log.debug("sign={}", rcTxxyModel.getSign());
			log.debug("timestamp={}", rcTxxyModel.getTimestamp());
			log.debug("data={}", rcTxxyModel.getData());
			log.debug("randomKey={}", rcTxxyModel.getRandomKey());
		}

		try {
			PrivateKey privateKey = RSAUtils.generatePrivateKey(PRI_KEY);
			String aesKey = RSAUtils.decrypt(privateKey, rcTxxyModel.getRandomKey());
			String reportData = SecurityUtil.decryptByAES(rcTxxyModel.getData(), aesKey);

			JSONObject data_obj = JSONObject.parseObject(reportData);
			String reportNo = data_obj.getString("reportNo");
			String channel = data_obj.getString("channel");
			String phoneNo = data_obj.getString("mobile");
			String name = data_obj.getString("name");
			String idNo = data_obj.getString("idcNo");

			RcTxxy rcTxxy = new RcTxxy();
			rcTxxy.setReportNo(reportNo);
			rcTxxy.setChannelCode(channel);
			rcTxxy.setPhoneNo(phoneNo);
			rcTxxy.setRealName(name);
			rcTxxy.setIdNo(idNo);
			rcTxxy.setReportData(reportData);
			rcTxxy.setReportStatus(RcTxxy.ReportStatus.report_created);
			rcTxxyService.saveTaskReport(rcTxxy);
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
			return ResponseData.error("数据解密失败");
		}

		return ResponseData.success("操作成功");
	}
	
//	public static void main(String[] args) {
//		
//		PrivateKey privateKey = RSAUtils.generatePrivateKey(PRI_KEY);
//		String aesKey = RSAUtils.decrypt(privateKey, "h3eoUz9w1c09Nrvdz2gMj5NO1YsRJxINPwLRak1G9wjAYgC0xTzrq4lU4eRj4SrrYLv6owaX6QsCwHydoojukvU7JAhzRP8xtH4SyDEQwvOaiPYsQhLimtQ6qKrnZe5kTnMpihp1Rk34TO+6HpQ8ZhQTFmqqtqcmAZQ1PvWZ1CI=");
////		String reportData = SecurityUtil.AesDecrypt(data, aesKey);
//		
//		System.err.println(aesKey);
//		
//		
//	}

}