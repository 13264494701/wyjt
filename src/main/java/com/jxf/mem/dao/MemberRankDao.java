package com.jxf.mem.dao;

import org.apache.ibatis.annotations.Param;

import com.jxf.mem.entity.MemberRank;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 会员等级DAO接口
 * @author JINXINFU
 * @version 2016-04-25
 */
@MyBatisDao
public interface MemberRankDao extends CrudDao<MemberRank> {
	
	/**
	 * 查找默认会员等级
	 * 
	 * @return 默认会员等级，若不存在则返回null
	 */
	MemberRank findDefault();
	
	/**
	 * 设置默认会员等级
	 * 
	 * @return 默认会员等级
	 */
	void setDefault(MemberRank memberRank);
	
	/**
	 * 根据等级编号获取等级
	 * @param rankNo
	 * @return
	 */
	MemberRank getMemRankByNo(@Param("rankNo")String rankNo);
	
	/**
	 * 
	 * 函数功能说明 获取最大的等级编号
	 * HUOJIABAO  2016年5月24日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @return     
	 * @return String    
	 * @throws
	 */
	String getMaxRankNo();
	
}