package com.jxf.svc.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MySecurity {
    public static final String SHA_1 = "SHA-1";
    public static final String MD5 = "MD5";

    public MySecurity() {
    }

    public String encode(String strSrc, String encodeType) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();

        try {
            if (encodeType == null || "".equals(encodeType)) {
                encodeType = "MD5";
            }

            md = MessageDigest.getInstance(encodeType);
            md.update(bt);
            strDes = this.bytes2Hex(md.digest());
            return strDes;
        } catch (NoSuchAlgorithmException var7) {
            return strSrc;
        }
    }

    public String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;

        for (int i = 0; i < bts.length; ++i) {
            tmp = Integer.toHexString(bts[i] & 255);
            if (tmp.length() == 1) {
                des = des + "0";
            }

            des = des + tmp;
        }

        return des;
    }

    public static void main(String[] args) {
        MySecurity te = new MySecurity();
        String strSrc = "可以加密汉字";
        System.out.println("Source String:" + strSrc);
        System.out.println("Encrypted String:");
        System.out.println("Use MD5:" + te.encode(strSrc, (String) null));
        System.out.println("Use MD5:" + te.encode(strSrc, "MD5"));
        System.out.println("Use SHA:" + te.encode(strSrc, "SHA-1"));
        System.out.println("Use SHA-256:" + te.encode(strSrc, "SHA-256"));
    }

}