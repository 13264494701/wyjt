package com.jxf.svc.utils;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class WxLoginUtils {
	//微信小程序解密
	public static class AES {
		public static boolean initialized = false;

		/**
		 * AES解密
		 * 
		 * @param content
		 *            密文
		 * @return
		 * @throws InvalidAlgorithmParameterException
		 * @throws NoSuchProviderException
		 */
		public static byte[] decrypt(byte[] content, byte[] keyByte,
				byte[] ivByte) throws InvalidAlgorithmParameterException {
			initialize();
			try {
				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
				Key sKeySpec = new SecretKeySpec(keyByte, "AES");

				cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte));// 初始化
				byte[] result = cipher.doFinal(content);
				return result;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			} catch (NoSuchProviderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		public static void initialize() {
			if (initialized)
			{ return; }
			Security.addProvider(new BouncyCastleProvider());
			initialized = true;
		}

		// 生成iv
		public static AlgorithmParameters generateIV(byte[] iv)
				throws Exception {
			AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
			params.init(new IvParameterSpec(iv));
			return params;
		}

	}
	
	public static Map<String, Object> getWxUserInfo( String encryptedData,String iv ,String session_key){

		Map<String, Object> data = new HashMap<String, Object>();
		try {
			byte[] resultByte = AES.decrypt(Encodes.decodeBase64(encryptedData),Encodes.decodeBase64(session_key), Encodes.decodeBase64(iv));
			if (null != resultByte && resultByte.length > 0) {
				String userInfo = new String(resultByte, "UTF-8");
				data.put("status", "1");
				data.put("msg", "解密成功");
				data.put("userInfo", userInfo);
			} else {
				data.put("status", "0");
				data.put("msg", "解密失败");
			}
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return data;
	}
}
