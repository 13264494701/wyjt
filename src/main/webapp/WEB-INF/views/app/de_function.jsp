<%@page import="javax.crypto.Cipher"%>
<%@page import="java.security.Security"%>
<%@page import="java.security.Key"%>
<%@ page language="java" pageEncoding="UTF-8" %><%!
public static final String DESKEY = "0908yxinbaoSoho";

private static Key getKey(byte[] arrBTmp) throws Exception {
    // 创建一个空的8位字节数组（默认值为0）
    byte[] arrB = new byte[8];
    // 将原始字节数组转换为8位
    for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
        arrB[i] = arrBTmp[i];
    }
    // 生成密钥
    Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
    return key;
}

public static  byte[] encrypt(byte[] arrB) throws Exception {
	Security.addProvider(new com.sun.crypto.provider.SunJCE());
    Key key = getKey(DESKEY.getBytes());
    Cipher  encryptCipher = Cipher.getInstance("DES");
    encryptCipher.init(Cipher.ENCRYPT_MODE, key);
    return encryptCipher.doFinal(arrB);
}

public static String encrypt(String strIn) throws Exception {
    return byteArr2HexStr(encrypt(strIn.getBytes()));
}

public static String byteArr2HexStr(byte[] arrB) throws Exception {
    int iLen = arrB.length;
    // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
    StringBuffer sb = new StringBuffer(iLen * 2);
    for (int i = 0; i < iLen; i++) {
        int intTmp = arrB[i];
        // 把负数转换为正数
        while (intTmp < 0) {
            intTmp = intTmp + 256;
        }
        // 小于0F的数需要在前面补0
        if (intTmp < 16) {
            sb.append("0");
        }
        sb.append(Integer.toString(intTmp, 16));
    }
    return sb.toString();
}
 
 public static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;

        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
  }
 
 public static  String decrypt(String strIn)  {
	 
        try {
            return new String(decrypt(hexStr2ByteArr(strIn)));
        } catch (Exception e) {
            return "";
        }
 }
 
 public static byte[] decrypt(byte[] arrB) throws Exception {
	 Security.addProvider(new com.sun.crypto.provider.SunJCE());
     Key key = getKey(DESKEY.getBytes());
	 Cipher decryptCipher = Cipher.getInstance("DES");
     decryptCipher.init(Cipher.DECRYPT_MODE, key);
     return decryptCipher.doFinal(arrB);
 }
%>