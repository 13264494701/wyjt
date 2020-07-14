package com.jxf.ufang.util;

import java.math.BigDecimal;

public class UfangLoaneeDataUtils {
	
	public static BigDecimal calPrice(Integer zmf,Integer age){
		
		if(zmf>=700) {
			return new BigDecimal(10);
		}
		if(zmf>=650&&age<=45) {
			return new BigDecimal(10);
		}
		
		if(zmf>=650&&age>45) {
			return new BigDecimal(8);
		}
		
		if(zmf>=580) {
			return new BigDecimal(8);
		}
		
		if(zmf>=500) {
			return new BigDecimal(5);
		}
		
		return new BigDecimal(10);
	}
}
