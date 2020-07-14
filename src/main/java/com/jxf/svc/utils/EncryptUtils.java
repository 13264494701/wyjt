package com.jxf.svc.utils;

import com.jxf.mem.utils.VerifiedUtils;

/**
 * 加密字符串
 * @author jxf
 * @version 1.0 2015-07-10
 */
public class EncryptUtils {
	/**
	 * 类型
	 */
	public enum Type {

		/** 身份证 */
		ID,

		/** 银行卡 */
		CARD,
		
		/** 手机号 */
		PHONE,
	
		/** 邮箱 */
		EMAIL,
		
		/** QQ */
		QQ,
		
		/** 微信号 */
		WX,
		
		/** 姓名 */
		NAME
	}
	
	public static String encryptString(String orgStr, Type type) {
		if(StringUtils.isBlank(orgStr)) {
			return "";
		}
		switch (type) {
			case ID:
				return StringUtils.replacePattern(orgStr, "(?<=[\\d]{4})\\d(?=[\\d]{4})", "*");
			case PHONE:
				return StringUtils.replacePattern(orgStr, "(?<=[\\d]{5})\\d(?=[\\d]{2})", "*");
			case CARD:
				return StringUtils.replacePattern(orgStr, "(?<=[\\d]{4})\\d(?=[\\d]{4})", "*");
			case EMAIL:
				return StringUtils.replacePattern(orgStr,"(\\d{2})\\d{4}(\\w{2})", "*");
			case QQ:
				return StringUtils.replacePattern(orgStr, "(?<=[\\d]{2})\\d(?=[\\d]{2})", "*");
			case WX:case NAME:
				return StringUtils.replacePattern(orgStr,"(\\d{2})\\d{4}(\\w{2})", "*");
			default:
				return StringUtils.replacePattern(orgStr,"(\\d{2})\\d{4}(\\w{2})", "*");
		}
	}
	
	
	public static void main(String[] args) {
		System.out.println(VerifiedUtils.isVerified(77594633, 2));
	}
	
}
