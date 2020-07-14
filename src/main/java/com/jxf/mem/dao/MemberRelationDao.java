package com.jxf.mem.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

import com.jxf.mem.entity.MemberRelation;


/**
 * 会员关系DAO接口
 * @author huojiayuan
 * @version 2016-05-24
 */
@MyBatisDao
public interface MemberRelationDao extends CrudDao<MemberRelation> {
	/**
	 * 函数功能说明 
	 * wo  2016年6月8日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param memberRelation
	 * @参数： @return     
	 * @return String    
	 * @throws
	 */
	Long getHigherMemberId(MemberRelation memberRelation);
	
}