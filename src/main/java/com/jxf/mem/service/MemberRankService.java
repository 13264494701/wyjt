package com.jxf.mem.service;

import com.jxf.mem.entity.MemberRank;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 会员等级Service
 * @author JINXINFU
 * @version 2016-04-25
 */
public interface MemberRankService extends CrudService<MemberRank> {

	
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
	String getRankNameByNo(String rankNo);
}