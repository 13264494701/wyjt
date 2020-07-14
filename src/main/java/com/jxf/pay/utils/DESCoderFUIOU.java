package com.jxf.pay.utils;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.jxf.svc.security.MD5Utils;
import com.jxf.svc.utils.Encodes;


public class DESCoderFUIOU {

	private static  String  CHATSET="UTF-8" ;
	//加密
	public static String desEncrypt(String input, String keystr) throws Exception {

		try {
			byte[] datasource = input.getBytes(CHATSET);
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(keystr.getBytes((CHATSET)));
			//创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			//Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES");
			//用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
			//现在，获取数据并加密
			//正式执行加密操作
			return Encodes.encodeBase64(cipher.doFinal(datasource)) ;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;

	}

	//解密
	public static String desDecrypt(String cipherText, String strkey) throws Exception {

		// DES算法要求有一个可信任的随机数源
		SecureRandom random = new SecureRandom();
		// 创建一个DESKeySpec对象
		DESKeySpec desKey = new DESKeySpec(strkey.getBytes(CHATSET));
		// 创建一个密匙工厂
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		// 将DESKeySpec对象转换成SecretKey对象
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance("DES");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, random);
		// 真正开始解密操作
		   
		    //b = decoder.decodeBuffer(src);
		return new String( cipher.doFinal(Encodes.decodeBase64(cipherText)),CHATSET);

	}

/*	private static String removeBR(String str) {
		StringBuffer sf = new StringBuffer(str);

		for (int i = 0; i < sf.length(); ++i) {
			if (sf.charAt(i) == '\n') {
				sf = sf.deleteCharAt(i);
			}
		}
		for (int i = 0; i < sf.length(); ++i)
			if (sf.charAt(i) == '\r') {
				sf = sf.deleteCharAt(i);
			}

		return sf.toString();
	}*/

	public static String padding(String str) {
		byte[] oldByteArray;
		try {
			oldByteArray = str.getBytes(CHATSET);
			int numberToPad = 8 - oldByteArray.length % 8;
			byte[] newByteArray = new byte[oldByteArray.length + numberToPad];
			System.arraycopy(oldByteArray, 0, newByteArray, 0, oldByteArray.length);
			for (int i = oldByteArray.length; i < newByteArray.length; ++i) {
				newByteArray[i] = 0;
			}
			return new String(newByteArray, CHATSET);
		} catch (UnsupportedEncodingException e) {
			System.out.println("Crypter.padding UnsupportedEncodingException");
		}
		return null;
	}

	public static byte[] removePadding(byte[] oldByteArray) {
		int numberPaded = 0;
		for (int i = oldByteArray.length; i >= 0; --i) {
			if (oldByteArray[(i - 1)] != 0) {
				numberPaded = oldByteArray.length - i;
				break;
			}
		}

		byte[] newByteArray = new byte[oldByteArray.length - numberPaded];
		System.arraycopy(oldByteArray, 0, newByteArray, 0, newByteArray.length);

		return newByteArray;
	}

	public static String getKey(String name) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(MD5Utils.EncoderByMd5(MD5Utils.EncoderByMd5(name + "fuiou"))).append(MD5Utils.EncoderByMd5(name + "20160112")).append(MD5Utils.EncoderByMd5(name + "mpay")).append(MD5Utils.EncoderByMd5(MD5Utils.EncoderByMd5(name)));
		return buffer.toString();

	}

	/**
	 * 
	* @Title: getKeyLength8
	* @Description: (获取固定长度8的key)
	* @param @param key
	* @param @return    设定文件
	* @return String    返回类型
	* @throws
	 */
	public static String getKeyLength8(String key) {
		key = key == null ? "" : key.trim();
		int tt = key.length() % 64;

		String temp = "";
		for (int i = 0; i < 64 - tt; i++) {
			temp += "D";
		}
		return key + temp;

	}

}
