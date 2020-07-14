package com.jxf.rc.entity;

import java.util.HashMap;
import java.util.Map;

public class RcConstant {

	/** 我的页面没有额度的提示语*/
	public static final String REMINDER_OF_NO_QUOTA_IN_MEMBERINFO_PAGE ="额度评估：还没评估？快来测测额度吧";
	/** 我的页面没有额度的提示语*/
	public static final String REMINDER_OF_QUOTA_IN_MEMBERINFO_PAGE ="额度评估：%s 元";
	/** 好友详情页面没有额度的提示语*/
	public static final String REMINDER_OF_NO_QUOTA_IN_FRIENDDETAIL_PAGE ="额度评估：未评估";
	/** 额度评估前的提示语*/
	public static final String REMINDER_OF_BEFORE_ASSESMENT ="还没评估？快来测测你的额度值吧";
	/** 额度评估提示语*/
	public static final String REMINDER_OF_QUOTA = "请保持良好的借还款行为，逾期将影响您的额度评估哦";
	/** 禁止再次进行额度评估提示语*/
	public static final String REMINDER_OF_FORBIDEN_ASSESMENT ="一周内最多评估一次，本周您已完成一次评估，请下周再来评估吧";
	/** 额度评估周期时间 单位：天*/
	public static final Integer QUOTA_CYCLE_TIME = 7;
	
	public static Map<String, Integer> scoreToQuotaMap = new HashMap<String,Integer>();
	
	static {
		scoreToQuotaMap.put("0-80", 20000);
		scoreToQuotaMap.put("80-100", 15000);
		scoreToQuotaMap.put("100-120", 14000);
		scoreToQuotaMap.put("120-140", 14000);
		scoreToQuotaMap.put("140-160", 13000);
		scoreToQuotaMap.put("160-180", 12000);
		scoreToQuotaMap.put("180-200", 12000);
		scoreToQuotaMap.put("200-220", 11000);
		scoreToQuotaMap.put("220-240", 10000);
		scoreToQuotaMap.put("240-260", 10000);
		scoreToQuotaMap.put("260-280", 9000);
		scoreToQuotaMap.put("280-300", 8500);
		scoreToQuotaMap.put("300-320", 8000);
		scoreToQuotaMap.put("320-340", 7000);
		scoreToQuotaMap.put("340-360", 6500);
		scoreToQuotaMap.put("360-380", 6000);
		scoreToQuotaMap.put("380-400", 5000);
		scoreToQuotaMap.put("400-420", 4500);
		scoreToQuotaMap.put("420-440", 4000);
		scoreToQuotaMap.put("440-460", 3500);
		scoreToQuotaMap.put("460-480", 3000);
		scoreToQuotaMap.put("480-500", 2000);
		scoreToQuotaMap.put("500-560", 1000);
		scoreToQuotaMap.put("560-760", 500);
		scoreToQuotaMap.put("760-10000", 0);
	}
	public static int getQuotaByScore (int score) {
		for (Map.Entry<String, Integer> entry : scoreToQuotaMap.entrySet()) {
			String scoreLocation = entry.getKey();
			String [] scoreArray = scoreLocation.split("-");
			Integer lowScore = Integer.valueOf(scoreArray[0]);
			Integer highScore = Integer.valueOf(scoreArray[1]);
			if(score > lowScore && score <= highScore) {
				return entry.getValue();
			}
		}
		return 0;
	}
	
	
	public static void main(String[] args) {
		System.out.println(getQuotaByScore(275));
	}
	
}
