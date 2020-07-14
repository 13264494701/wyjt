package com.jxf.svc.sys.util;

import com.jxf.svc.init.SpringContextHolder;
import com.jxf.svc.sys.brn.dao.BrnDao;

public class BrnUtils {
	
	private static BrnDao brnDao = SpringContextHolder.getBean(BrnDao.class);
	
	public static String getBrnNamByNo(String brnNo){
		
		String brnName = brnDao.getBrnNamByNo(brnNo);
		return brnName;	
	}
}
