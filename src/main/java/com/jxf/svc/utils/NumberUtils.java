package com.jxf.svc.utils;

/**
 * 日期工具类, 继承org.apache.commons.lang3.time.DateUtils类
 * 
 * @author jxf
 * @version 1.0 2015-07-10
 */
public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils{
	/**
	   *         生成任意长度的随机数
     *
     * @param verifyCodeLength 随机数长度
     * @return String 随机数
     */
    public static String getRandomNumByLen(int verifyCodeLength) {

        int rad2 = (int) (Math.random() * 11);
        rad2 = rad2 > 10 ? 15 : rad2 + verifyCodeLength;
        double radnum = Math.random();
        int rad3 = (int) (radnum * Math.pow(10, rad2) % Math.pow(10, verifyCodeLength));

        String code = "" + rad3;
        while (code.length() < verifyCodeLength) {
            code = "0" + code;
        }
        return code;
    }
}
