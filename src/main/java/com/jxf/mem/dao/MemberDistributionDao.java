package com.jxf.mem.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberDistribution;

import org.apache.ibatis.annotations.Param;


/**
 * 三级分销DAO接口
 * @author huojiayuan
 * @version 2016-05-25
 */
@MyBatisDao
public interface MemberDistributionDao extends CrudDao<MemberDistribution> {
	/**
	 * 函数功能说明 根据会员查询
	 * zhuhuijie  2016年6月8日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param member
	 * @参数： @return     
	 * @return MemberDistribution    
	 * @throws
	 */
	public MemberDistribution getByMember(Member member);
	/**
	 * 函数功能说明 修改会员分销等级的会员数量
	 * zhuhuijie  2016年6月12日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param memNo
	 * @参数： @param levelNum
	 * @参数： @return     
	 * @return int    
	 * @throws
	 */
	public int updateLevelNum(@Param("memberId")Long memberId,@Param("level")Integer level,@Param("addNum")Integer addNum);
	
	public void open(Long memberId);
	public void close(Long memberId);
	
}