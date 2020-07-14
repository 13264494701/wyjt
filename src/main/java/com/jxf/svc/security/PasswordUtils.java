package com.jxf.svc.security;

import com.jxf.svc.config.Global;
import com.jxf.svc.utils.Encodes;
import com.jxf.svc.utils.StringUtils;

public class PasswordUtils {
	
	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword) {
		byte[] salt = Digests.generateSalt(Global.SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, Global.HASH_INTERATIONS);
		return Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword);
	}
	
	/**
	 * 验证密码
	 * @param plainPassword 明文密码
	 * @param password 密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		if(StringUtils.isBlank(plainPassword)||StringUtils.isBlank(password)||password.length()<16) {
			return false;
		}
		byte[] salt = Encodes.decodeHex(password.substring(0,16));
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, Global.HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword));
	}
    public static void main(String args[])
    {
    	String enPassword1 = MD5Utils.EncoderByMd5("cpbao.com_友信宝" + MD5Utils.EncoderByMd5("123456").toLowerCase()).toLowerCase();
    	String enPassword2 = entryptPassword(enPassword1); 
    	System.out.println(enPassword2);
    }

}
