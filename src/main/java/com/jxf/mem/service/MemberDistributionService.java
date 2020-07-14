package com.jxf.mem.service;


import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberDistribution;
import com.jxf.svc.sys.crud.service.CrudService;


/**
 * 三级分销Service
 * @author huojiayuan
 * @version 2016-05-25
 */
public interface MemberDistributionService extends CrudService<MemberDistribution> {

	/**
	 * 函数功能说明 根据会员编号查询
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
	 * 函数功能说明 初始化会员分销
	 * zhuhuijie  2016年6月12日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param member     
	 * @return void    
	 * @throws
	 */
	public void initMemDist(Member member);
	
	public void open(Member member);
	
	public void close(Member member);
	
	
}