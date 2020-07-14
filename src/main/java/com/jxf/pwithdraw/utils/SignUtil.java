package com.jxf.pwithdraw.utils;

import com.alibaba.fastjson.JSONObject;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.MD5Code;
import com.jxf.svc.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SignUtil {

	private static Logger logger = LoggerFactory.getLogger(SignUtil.class);


	public static String genRSASign(JSONObject reqObj,String key) {
		// 生成待签名串
		String sign_src = genSignData(reqObj);
		logger.debug("商户[" + reqObj.getString("oid_partner") + "]待签名原串" + sign_src);
		return TraderRSAUtil.sign(key, sign_src);
	}

	/**
	 * 生成待签名串
	 * 
	 * @param jsonObject
	 * @return
	 */
	public static String genSignData(JSONObject jsonObject) {
		StringBuffer content = new StringBuffer();

		// 按照key做首字母升序排列
		List<String> keys = new ArrayList<String>(jsonObject.keySet());
		Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			// sign 和ip_client 不参与签名
			if ("sign".equals(key)) {
				continue;
			}
			String value = (String) jsonObject.getString(key);
			// 空串不参与签名
			if (StringUtils.isEmpty(value)) {
				continue;
			}
			content.append((i == 0 ? "" : "&") + key + "=" + value);

		}
		String signSrc = content.toString();
		if (signSrc.startsWith("&")) {
			signSrc = signSrc.replaceFirst("&", "");
		}
		return signSrc;
	}

	/**
	 * @description 校验MD5签名字符串
	 * @param orgStr 未加密字符串
	 * @param resultStr 结果字符串
	 * @return
	 */
	public static boolean checkSignForMD5(String orgStr,String resultStr) {
		try {
			String encodeStr = MD5Code.MD5Encode(orgStr, "UTF-8");
			if(StringUtils.equals(encodeStr, resultStr)) {
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			logger.error("校验签名时出现错误：{}",Exceptions.getStackTraceAsString(e));
		}
		return false;
	}
	
}
