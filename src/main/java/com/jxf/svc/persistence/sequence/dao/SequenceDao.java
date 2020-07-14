package com.jxf.svc.persistence.sequence.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.persistence.sequence.entity.Sequence;
import com.jxf.svc.sys.crud.dao.CrudDao;


/**
 * 用户DAO接口
 * @author jxf
 * @version 2015-07-27
 */
@MyBatisDao
public interface SequenceDao extends CrudDao<Sequence> {
	
	/**
	 * 根据表名获取mysql数据库自增值
	 * @param table_name
	 * @return
	 */
	public String getMysqlSequence(String table_name) ;
	
	/**
	 * 根据序列名获取oracle数据库自增值
	 * @param table_name
	 * @return
	 */
	public String getOraclelSequence(String table_name) ;


}
