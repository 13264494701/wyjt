package com.jxf.test;





import java.math.BigDecimal;
import java.util.Date;

import com.jxf.loan.utils.LoanUtils;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.StringUtils;



public class BitSetTest {

	public static void main(String[] args) throws Exception {
		

		Date start = CalendarUtil.StringToDate("2019-01-15 15:32:35");
		Date end = CalendarUtil.StringToDate("2019-01-31");
		int terms = CalendarUtil.getIntervalDays(start, end) + 1;
		BigDecimal intRate = BigDecimal.ZERO;
		intRate = getIntRate("1617","17",terms);//反推利率
		
		System.out.println(intRate);

		
		
		
		
	 }
	
	private static BigDecimal getIntRate(String totalMoney, String totalInterest,
			Integer terms) {
		BigDecimal intRate = LoanUtils.getIntRate(new BigDecimal(totalMoney), terms, new BigDecimal(totalInterest));
		return intRate.multiply(new BigDecimal("100"));
	}
}
