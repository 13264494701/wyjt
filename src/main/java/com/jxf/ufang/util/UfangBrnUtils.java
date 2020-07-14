package com.jxf.ufang.util;

import com.jxf.svc.init.SpringContextHolder;
import com.jxf.ufang.dao.UfangBrnDao;

public class UfangBrnUtils {
	
	private static UfangBrnDao brnDao = SpringContextHolder.getBean(UfangBrnDao.class);
	
	public static String getBrnNamByNo(String brnNo){
		
		String brnName = brnDao.getBrnNamByNo(brnNo);
		return brnName;	
	}
}
