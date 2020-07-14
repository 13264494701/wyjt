package com.jxf.svc.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


/**
 * @作者: xiaorongdian
 * @创建时间 :2018年10月10日 下午4:20:10
 * @功能说明:
 */
public class IdCardUtils {
	
    /**
     * 15位转18位身份证
     *
     * @param idCardNo_15
     * @return
     */
    public static String updateIdCard15To18(String idCardNo_15) {
        StringBuilder sb = new StringBuilder(idCardNo_15.substring(0, 6));
        sb.append(19);
        sb.append(idCardNo_15.substring(6, 15));
        sb.append(getVerifyBit(sb.toString()));
        return sb.toString();
    }
    
    /**
     * 根据前17位乘积和推断最后一个校验位
     *
     * @param idCardNo
     * @return
     */
    private static String getVerifyBit(String idCardNo) {
        int sum17 = getSum17(idCardNo);
        // 余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3 2。
        final String[] lastChar = new String[]{"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        int lastIndex = sum17 % 11;
        return lastChar[lastIndex];
    }
    
    /**
     * 计算前17位乘积和
     *
     * @param idCardNo
     * @return
     */
    private static int getSum17(String idCardNo) {
        String sub17 = "";
        if (StringUtils.isBlank(idCardNo) || !StringUtils.isNumeric(sub17 = idCardNo.substring(0, 17))) {
            return -1;
        }
        // 将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
        final int[] powers = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

        int sum = 0;
        int i = 0;
        for (char c : sub17.toCharArray()) {
            sum += Integer.parseInt(c + "") * powers[i++];
        }
        return sum;
    }
    
    /** 
     * 获取随机生成的身份证号码 
     *  
     * @author mingzijian 
     * @return 
     */  
    public static String getRandomID() {  
        String id = "420222199204179999";  
        // 随机生成省、自治区、直辖市代码 1-2  
        String provinces[] = { "11", "12", "13", "14", "15", "21", "22", "23",  
                "31", "32", "33", "34", "35", "36", "37", "41", "42", "43",  
                "44", "45", "46", "50", "51", "52", "53", "54", "61", "62",  
                "63", "64", "65", "71", "81", "82" };  
        String province = randomOne(provinces);  
        // 随机生成地级市、盟、自治州代码 3-4  
        String city = randomCityCode(18);  
        // 随机生成县、县级市、区代码 5-6  
        String county = randomCityCode(28);  
        // 随机生成出生年月 7-14  
        String birth = randomBirth(20, 50);  
        // 随机生成顺序号 15-17(随机性别)  
        String no = new Random().nextInt(899) + 100+"";    
        // 随机生成校验码 18  
        String checks[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",  
                "X" };  
        String check = randomOne(checks);  
        // 拼接身份证号码  
        id = province + city + county + birth + no + check;  
        return id;  
    }  
    /** 
     * 从String[] 数组中随机取出其中一个String字符串 
     *  
     * @author mingzijian 
     * @param s 
     * @return 
     */  
    public static String randomOne(String s[]) {  
        return s[new Random().nextInt(s.length - 1)];  
    }  
  
    /** 
     * 随机生成两位数的字符串（01-max）,不足两位的前面补0 
     *  
     * @author mingzijian 
     * @param max 
     * @return 
     */  
    public static String randomCityCode(int max) {  
        int i = new Random().nextInt(max) + 1;  
        return i > 9 ? i + "" : "0" + i;  
    }  
    /** 
     * 随机生成minAge到maxAge年龄段的人的生日日期 
     *  
     * @author mingzijian 
     * @param minAge 
     * @param maxAge 
     * @return 
     */  
    public static String randomBirth(int minAge, int maxAge) {  
        SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");// 设置日期格式  
        Calendar date = Calendar.getInstance();  
        date.setTime(new Date());// 设置当前日期  
        // 随机设置日期为前maxAge年到前minAge年的任意一天  
        int randomDay = 365 * minAge  
                + new Random().nextInt(365 * (maxAge - minAge));  
        date.set(Calendar.DATE, date.get(Calendar.DATE) - randomDay);  
        return dft.format(date.getTime());  
    } 
    
    
    /**
     * 获得年龄
     *
     * @return
     */
    public static int getAge(String idCardNo){
    	
    	Date birthDay = CalendarUtil.StringToDate(idCardNo.substring(6, 14),"yyyyMMdd");     
    	Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            }else{
                age--;
            }
        }
        return age;
    }
    /**
     * 获得指定时间段内的年龄
     *
     * @return
     */
    public static int getAge(String idCardNo,Date date){
    	 Date birthDay = CalendarUtil.StringToDate(idCardNo.substring(6, 14),"yyyyMMdd");
    	if(CalendarUtil.getDate(date).compareTo(CalendarUtil.getDate(birthDay)) <= 0) {
    		return 0;
    	}
    	int yearNow = CalendarUtil.getYear(date);
        int monthNow = CalendarUtil.getMonth(date);
        int dayOfMonthNow = CalendarUtil.getDay(date);
       
        Calendar cal = Calendar.getInstance();
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            }else{
                age--;
            }
        }
        return age;
    }
    
    
    /**
     * 获得年龄
     *
     * @return
     */
    public static int getAge(Date birthDay){
        
    	Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            }else{
                age--;
            }
        }
        return age;
    }
    
    public static void main(String args[])
    {
    	String idno = "320321199204044632";
    	int age = getAge(idno,new Date()); 
    	System.out.println(age);
    }

}
