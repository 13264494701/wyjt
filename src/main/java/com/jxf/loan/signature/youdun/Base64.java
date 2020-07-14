package com.jxf.loan.signature.youdun;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import com.jxf.svc.utils.Encodes;

public class Base64 {

    /**
     * 加签工具
     *
     * @param prikeyvalue
     * @param sign_str
     * @return
     */
    public static String SHA1WithRSASign(String prikeyvalue, String sign_str) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
            		Encodes.decodeBase64(prikeyvalue));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey myprikey = keyf.generatePrivate(priPKCS8);
            // 用私钥对信息生成数字签名
            java.security.Signature signet = java.security.Signature
                    .getInstance("SHA1withRSA");
            signet.initSign(myprikey);
            signet.update(sign_str.getBytes("UTF-8"));
            byte[] signed = signet.sign(); // 对信息的数字签名
            return new String(org.apache.commons.codec.binary.Base64.encodeBase64(signed));
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 验签工具
     *
     * @param pubkeyvalue
     * @param oid_str
     * @param signed_str
     * @return
     */
    public static boolean checkSHA1WithRSA(String pubkeyvalue, String oid_str, String signed_str) {
        try {
            X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(
            		Encodes.decodeBase64(pubkeyvalue));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
            byte[] signed = Encodes.decodeBase64(signed_str);// 这是SignatureData输出的数字签名?
            java.security.Signature signetcheck = java.security.Signature
                    .getInstance("SHA1withRSA");
            signetcheck.initVerify(pubKey);
            signetcheck.update(oid_str.getBytes("UTF-8"));
            return signetcheck.verify(signed);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
