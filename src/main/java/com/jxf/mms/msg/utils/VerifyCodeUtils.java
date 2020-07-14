package com.jxf.mms.msg.utils;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @类功能说明： 验证码工具类
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：HUOJIABAO 
 * @创建时间：2016年5月26日 下午4:24:08 
 * @版本：V1.0
 */
public class VerifyCodeUtils implements Serializable{
	
	private static final long serialVersionUID = -9116861922775262146L;
	private static Logger logger = LoggerFactory.getLogger(VerifyCodeUtils.class);
	private final static int OFFSET = 538309;
	public final static int MAX_ATTEMPTS = 10; // 安全性增强：最大尝试次数

	public static final String SMS_CODE = "smsCode";
	
	/**
	 * 随机生成一个短信验证码
	 * 
	 * @return 返回一个六位随机数验证码
	 */
	public static String genSmsValidCode() {
		long seed = System.currentTimeMillis() + OFFSET;
		SecureRandom secureRandom = null; // 安全随机类
		try {
			secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(seed);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		}

		String codeList = "1234567890"; // 验证码数字取值范围
		String sRand = ""; // 定义一个验证码字符串变量

		for (int i = 0; i < 6; i++) {
			int code = secureRandom.nextInt(codeList.length() - 1); // 随即生成一个0-9之间的整数
			String rand = codeList.substring(code, code + 1);
			sRand += rand; // 将生成的随机数拼成一个六位数验证码
		}
		return sRand; // 返回一个六位随机数验证码

	}

	/**
	 * 随机生成一个邮件验证码
	 * 
	 * @return 返回一个十位随机数验证码
	 */
	public static String genEmailValidCode() {
		long seed = System.currentTimeMillis() + OFFSET;
		SecureRandom secureRandom = null; // 安全随机类
		try {
			secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(seed);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		}

		String codeList = "ABCDEFGHIJKLMNOPQRSTUVWXYZabckefghijklmnopqrstuvwxyz1234567890"; // 验证码数字取值范围
		String sRand = new String(""); // 定义一个验证码字符串变量

		for (int i = 0; i < 10; i++) {
			int code = secureRandom.nextInt(codeList.length() - 1); // 随即生成一个整数
			String rand = codeList.substring(code, code + 1);
			sRand += rand; // 将生成的随机数拼成一个十位数验证码
		}
		return sRand; // 返回一个六位随机数验证码
	}
}
