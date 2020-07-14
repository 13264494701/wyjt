package com.jxf.svc.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.jxf.svc.utils.CalendarUtil.DateStyle;

/**
 * 日期工具类, 继承org.apache.commons.lang3.time.DateUtils类
 * 
 * @author jxf
 * @version 1.0 2015-07-10
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	public static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH", "yyyy-MM-dd HH:mm", "yyyy-MM",
			"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss",
			"yyyy.MM.dd HH:mm", "yyyy.MM","yyyyMMdd" };

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}
	/**
	 * 得到指定日期年份字符串 格式（yyyy）
	 */
	public static String getYear(Date date) {
		return formatDate(date, "yyyy");
	}
	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}
	/**
	 * 得到指定日期月份字符串 格式（MM）
	 */
	public static String getMonth(Date date) {
		return formatDate(date, "MM");
	}
	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}
	/**
	 * 得到指定日期当天字符串 格式（dd）
	 */
	public static String getDay(Date date) {
		return formatDate(date, "dd");
	}
	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}

	/**
	 * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
	 * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy.MM.dd",
	 * "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null) {
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * 
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		Date nowDate = new Date();
		long time1 = nowDate.getTime();
		long time2 = date.getTime();
		long t = time1 - time2;
		return t / (24 * 60 * 60 * 1000);
	}

	/**
	 * 获取过去的小时
	 * 
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = System.currentTimeMillis() - date.getTime();
		return t / (60 * 60 * 1000);
	}

	/**
	 * 获取过去的分钟
	 * 
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = System.currentTimeMillis() - date.getTime();
		return t / (60 * 1000);
	}

	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * 
	 * @param timeMillis
	 * @return
	 */
	public static String formatDateTime(long timeMillis) {
		long day = timeMillis / (24 * 60 * 60 * 1000);
		long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
		long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
		return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
	}

	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return 相差天数。如果失败则返回-1 
	 */
	public static int getDistanceOfTwoDate(Date before, Date after) {
        int num = -1;  
        Date dateTmp = CalendarUtil.StringToDate(CalendarUtil.getDate(before), DateStyle.YYYY_MM_DD);  
        Date otherDateTmp = CalendarUtil.StringToDate(CalendarUtil.getDate(after), DateStyle.YYYY_MM_DD);  
        if (dateTmp != null && otherDateTmp != null) {  
            long time = Math.abs(dateTmp.getTime() - otherDateTmp.getTime());  
            num = (int) (time / (24 * 60 * 60 * 1000));  
        }  
        return num;  
	}

	/**
	 * 获取实际日期与期望日期之间的差值
	 * 
	 * @param expectDate 期望日期
	 * @param actualDate 实际日期
	 * @return 差值 如果失败则返回-99999 
	 */
	public static int getDifferenceOfTwoDate(Date expectDate, Date actualDate) {
        int num = -99999;  
        Date dateTmp = CalendarUtil.StringToDate(CalendarUtil.getDate(expectDate), DateStyle.YYYY_MM_DD);  
        Date otherDateTmp = CalendarUtil.StringToDate(CalendarUtil.getDate(actualDate), DateStyle.YYYY_MM_DD);  
        if (dateTmp != null && otherDateTmp != null) {  
            long time = dateTmp.getTime() - otherDateTmp.getTime();  
            num = (int) (time / (24 * 60 * 60 * 1000));  
        }  
        return num;  
	}
	
	/**
	 * 以输入时间为准，向后滚动需要的天数后返回日期的格式字符串 返回日期格式 yyyyMMdd
	 *
	 * @param days:向后滚动的天数
	 * @param inputDate:需要滚动的基准日期格式为yyyyMMdd
	 *
	 */
	public static String rolDate(String inputDate, long days) {
		java.util.Date inday = bocmDateToCal(inputDate).getTime();
		long l = inday.getTime();
		long rol = l + days * 24 * 60 * 60 * 1000;
		java.util.Date rolDay = new java.util.Date(rol);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(rolDay);
		int i = calendar.get(Calendar.YEAR);
		int j = calendar.get(Calendar.MONTH) + 1;
		int k = calendar.get(Calendar.DATE);
		return "" + i + (j >= 10 ? "" + j : "0" + j) + (k >= 10 ? "" + k : "0" + k);

	}

	/**
	 * 以输入时间为准，向后滚动需要的年后返回日期的格式字符串 返回日期格式 yyyyMMdd
	 *
	 * @param days:向后滚动的年
	 * @param inputDate:需要滚动的基准日期格式为yyyyMMdd
	 *
	 */
	public static String rolYearDate(String inputDate, long years) {
		java.util.Date inday = bocmDateToCal(inputDate).getTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(inday);
		int i = calendar.get(Calendar.YEAR) + 1;
		int j = calendar.get(Calendar.MONTH) + 1;
		int k = calendar.get(Calendar.DATE);
		return "" + i + (j >= 10 ? "" + j : "0" + j) + (k >= 10 ? "" + k : "0" + k);

	}

	/**
	 * 以输入时间为准，向后滚动需要的月后返回日期的格式字符串 返回日期格式 yyyyMMdd
	 *
	 * @param days:向后滚动的月
	 * @param inputDate:需要滚动的基准日期格式为yyyyMMdd
	 *
	 */
	public static String rolMonthDate(String inputDate, int month) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		// Date now;
		String date = "";
		try {
			Date now = sdf.parse(inputDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(now);
			System.out.println(sdf.format(calendar.getTime()));
			calendar.add(Calendar.MONTH, month);
			date = sdf.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;

	}

	public static Calendar bocmDateToCal(String s) {
		int i = Integer.parseInt(s.substring(0, 4));
		int j = Integer.parseInt(s.substring(4, 6)) - 1;
		int k = Integer.parseInt(s.substring(6, 8));
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(i, j, k, 0, 0, 0);
		return calendar;
	}

	// 两个日期相差的秒数
	public static int calLastedTime() throws ParseException {
		long a = System.currentTimeMillis();
		Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse("19700101000000");
		long b = date.getTime();
		int c = (int) ((a - b) / 1000);
		return c;
	}
	/**
	 * 获取纯数字日期时间
	 * @return yyyyMMddHHmmss
	 */
	public static String getAllNumTime() {
		SimpleDateFormat sdf_all_num = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String allNumTime = sdf_all_num.format(date);
		return allNumTime;
	}
	/**
	 * 获取纯数字日期时间
	 * @return yyyyMMddHHmmss
	 */
	public static String getAllNumTime(Date date) {
		SimpleDateFormat sdf_all_num = new SimpleDateFormat("yyyyMMddHHmmss");
		if(date == null) {
			date = new Date();
		}
		String allNumTime = sdf_all_num.format(date);
		return allNumTime;
	}
	// 获取 Date
	public static Date parse(String source) throws ParseException {
		SimpleDateFormat format = null;
		if(source.contains("-")) {
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}else {
			format = new SimpleDateFormat("yyyyMMddHHmmss");
		}
		
		return format.parse(source);
	}
	
	// 获取当前时间戳
	public static long getTimestamp() {
		return System.currentTimeMillis();
	}
	/**
	 * 得到当前月份字符串 格式（HH）
	 */
	public static String getHour() {
		return formatDate(new Date(), "HH");
	}
	/**
	 * 得到指定日期当天字符串 格式（HH）
	 */
	public static String getHour(Date date) {
		return formatDate(date, "HH");
	}
	
	/***
	 * 获取增加几天
	 * @param time
	 * @param n
	 * @return
	 */
	public static Date addCalendarByDate(Date time ,int n){
		Calendar cal = Calendar.getInstance();
	 	cal.setTime(time);   
	 	cal.add(Calendar.DATE, n);//增加一天   
	 	return cal.getTime();
	}
	/***
	 * 获取指定日期的指定字符串格式
	 * @param time
	 * @param n
	 * @return
	 */
	public static String getDateStr(Date time,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String date = sdf.format(time);
		return date;
	}

	
	public static Date addCalendarByString235959(String Date,int n){
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf_ymd = new SimpleDateFormat("yyyy-MM-dd");
	 	try {
			cal.setTime(sdf_ymd.parse(Date));
		} catch (ParseException e) {}
	 	cal.add(Calendar.DATE, n);//增加一天   
	 	cal.set(Calendar.HOUR_OF_DAY, 23);
	 	cal.set(Calendar.MINUTE, 59);
	 	cal.set(Calendar.SECOND, 59);
	 	return cal.getTime();
	}
	
	/***
	 * 获取增加几月
	 * @param time
	 * @param n
	 * @return
	 */
	public static Date addCalendarByMonth(Date time ,int n){
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.add(Calendar.MONTH, n);//增加n月
		return cal.getTime();
	}
	
	
	/**
	 * 获取本月开始日期
	 * @return String
	 * **/
	public static String getMonthStart(){
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date time=cal.getTime();
		return new SimpleDateFormat("yyyy-MM-dd").format(time);
		
	}
	
	/**
	 * 获取本月最后一天
	 * @return String
	 * **/
	public static String getMonthEnd(){
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date time=cal.getTime();
		return new SimpleDateFormat("yyyy-MM-dd").format(time);

	}	
	
	/**
	 * 获取指定日期的某个时间点
	 * @param 要获取的时间点  格式为24小时制的时间格式，example:下午5点 17:00:00
	 * @return
	 */
	public static Date getDateTime(String date,String time) {
		String dateWithoutTimeStr =  CalendarUtil.getDate(date);
		 Date dateTime = CalendarUtil.StringToDate(dateWithoutTimeStr + " " + time);
		 return dateTime;
	}
	
	/**
	 * 获取指定日期的某个时间点
	 * @param 日期
	 * @param 要获取的时间点  格式为24小时制的时间格式，example:下午5点 17:00:00
	 * @return
	 */
	public static Date getDateTime(Date date,String time) {
		String dateWithoutTimeStr =  CalendarUtil.getDate(date);
		 Date dateTime = CalendarUtil.StringToDate(dateWithoutTimeStr + " " + time);
		 return dateTime;
	}
	public static void main(String[] args) {
		Date startDate = null;
		Date dueRepayDate = null;
		try {
			startDate = DateUtils.parse("2019-06-06 00:00:00");
			dueRepayDate = DateUtils.parse("2019-06-04 00:00:00");
			System.out.println(getDifferenceOfTwoDate(startDate, dueRepayDate));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获得 xxxx年xx月xx日 xx时xx分
	 * @param createTime
	 * @return
	 */
	public static String formatDateForMessage(Date time) {
		String formatDateTime = formatDateTime(time);
		String year = formatDateTime.substring(0, 4);
		String month = formatDateTime.substring(5, 7);
		String day = formatDateTime.substring(8, 10);
		String hourAndMinute = formatDateTime.substring(11, 16);
		return year+"年"+month+"月"+day+"日  "+hourAndMinute;
	}
}
