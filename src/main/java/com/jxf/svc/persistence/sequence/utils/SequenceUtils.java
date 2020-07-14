package com.jxf.svc.persistence.sequence.utils;

import com.jxf.svc.config.Global;
import com.jxf.svc.init.SpringContextHolder;
import com.jxf.svc.persistence.sequence.dao.SequenceDao;
import com.jxf.svc.utils.StringUtils;



/**
 * 数据库字符串工具类
 * @author lx
 * @version 1.0 2015-08-05
 */
public class SequenceUtils  {
	
	private static SequenceDao sequencedao = ((SequenceDao)SpringContextHolder.getBean(SequenceDao.class));
	/**
	 * 获得数据库递增值
	 * @param table_name 表名
	 * @return
	 */
	public static String getSequence(String table_name) {
		String sequence = "";
		if(Global.getConfig("jdbc.type").equals("mysql")){
			sequence=sequencedao.getMysqlSequence(table_name);
		}else if(Global.getConfig("jdbc.type").equals("oracle")){
			sequence=sequencedao.getOraclelSequence(table_name);
		}
		return sequence;
	}

	/**
	 * 根据位数获取流水
	 * table_name表名
	 * scale 目标长度
	 * */
	public static String getSequence(String table_name, int scale) {
		String sequence = "";
		if(Global.getConfig("jdbc.type").equals("mysql")){
			String seq=sequencedao.getMysqlSequence(table_name);
			 sequence=StringUtils.addLeftStr(seq, "0", scale);
		}else if(Global.getConfig("jdbc.type").equals("oracle")){
			String seq=sequencedao.getOraclelSequence(table_name);
			 sequence=StringUtils.addLeftStr(seq, "0", scale);
		}
		return sequence;

	}
}