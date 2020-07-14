package com.jxf.loan.signature.youdun;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @Description: 合同摘要工具类
 * @Author: shihao
 * @Date: 2018/12/24 14:06
 * @Version: 1.0
 **/
public class DigestUtil {

    private final static String DEFAULT_ALGORITHM = "SHA-256";

    public static String digest(byte[] date) {
        return digest(date, DEFAULT_ALGORITHM);
    }

    public static String digest(byte[] data, String digestAlgorithm) {
        // 计算合同摘要
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(digestAlgorithm);
            messageDigest.update(data);
            return Hex.encode(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("NoSuchAlgorithmException:" + digestAlgorithm);
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 2进制与16进制相互转化工具
     */
    private static final class Hex {
        private static final char[] TO_TABLE = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        private static final int[] FROM_TABLE = new int[256];

        private Hex() {
        }

        public static String encode(byte[] src) {
            StringBuffer buff = new StringBuffer(src.length * 2);
            byte[] var2 = src;
            int var3 = src.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                byte b = var2[var4];
                buff.append(TO_TABLE[b >>> 4 & 15]);
                buff.append(TO_TABLE[b & 15]);
            }

            return buff.toString();
        }

        public static byte[] decode(String src) {
            byte[] rtv = new byte[src.length() / 2];
            int i = 0;

            for (int j = 0; i < src.length(); ++j) {
                rtv[j] = (byte) ((FROM_TABLE[src.charAt(i++)] << 4 | FROM_TABLE[src.charAt(i++)]) & 255);
            }

            return rtv;
        }

        static {
            Arrays.fill(FROM_TABLE, -1);

            for (int i = 0; i < TO_TABLE.length; FROM_TABLE[TO_TABLE[i] & 255] = i++) {
                ;
            }

        }
    }

    /**
     * 获得文件的byte数组
     *
     * @return
     */
    public static byte[] fileConvertToByteArray(File file) {
        byte[] data = null;

        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[1024 * 8];
            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            data = baos.toByteArray();
            fis.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    /**
     * 输入流中获得字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024 * 8];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer,0,len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
