package com.jxf.mem.utils;


/**
 * 认证工具类
 * @author 
 *
 */
public class VerifiedUtils {
	
	/**
	 * 32位二进制长度分别代表是否认证  例:00 0000000000 0000000000 0000000101 = 5  代表手机已认证   视频已认证                                                                    
	 * 从右往左下标代表意思:
	 * 0位:手机认证
	 * 1位:身份证认证
	 * 2位:视频认证
	 * 3位:银行卡认证
	 * 4位:芝麻信用
	 * 5位:淘宝
	 * 6位:运营商  
	 * 7位:银行卡账单
	 * 8位:学信网
	 * 9位:社保
	 * 10位:公积金
	 * 11位：是否允许发起借款
	 * 21位:已设置登录密码
	 * 22位:已设置支付密码
	 * 23位:已设置邮箱地址
	 * 24位:三个紧急联系人填全
	 * 25位:好友人数超过标准值
	 * 26位:是否阅读协议
	 * 数据库以十进制存储
	 */
	
	/***
	 * 是否认证
	 * @param verifiedList
	 * @param verifiedIndex [0,n-1]
	 * @return
	 */
	public static Boolean isVerified(int verifiedList,int verifiedIndex){
				
		return (verifiedList&(1<< verifiedIndex))==(1<< verifiedIndex);		
	}
	
	/**
	 * 添加认证
	 * @param verifiedList
	 * @param verifiedIndex [0,n-1]
	 * @return
	 */
	public static Integer addVerified(int verifiedList,int verifiedIndex){
				
		return verifiedList|(1<< verifiedIndex);		
	}
	
	/**
	 * 移除认证
	 * @param verifiedList
	 * @param verifiedIndex [0,n-1]
	 * @return
	 */
	public static Integer removeVerified(int verifiedList,int verifiedIndex){
				
		return isVerified(verifiedList,verifiedIndex)?verifiedList^(1<< verifiedIndex):verifiedList;		
	}
	
	/**
	 * 验证实名认证
	 * @param verifiedList
	 * @param verifiedIndex
	 * @return
	 */
	public static boolean isVerified(int verifiedList){
		
		return isVerified(verifiedList, 1)&&isVerified(verifiedList, 2)&&isVerified(verifiedList, 22)&&isVerified(verifiedList, 23);		
	}
	
	
	 public static void main(String[] args) throws Exception {
		 Integer verifiedList = 100661903;
//		 verifiedList = addVerified(verifiedList,1);
//		 verifiedList = addVerified(verifiedList,2);
		 verifiedList = removeVerified(verifiedList,3);
//		 verifiedList = addVerified(verifiedList,4);
//		 verifiedList = addVerified(verifiedList,21);
//		 verifiedList = addVerified(verifiedList,22);

		 System.out.print(isVerified(verifiedList,3));
	 }
	
}
