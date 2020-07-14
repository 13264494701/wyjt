package com.jxf.mem.service;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberRelation;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;

import java.util.List;


/**
 * 会员关系Service
 * @author huojiayuan
 * @version 2016-05-24
 */
public interface MemberRelationService extends CrudService<MemberRelation> {
	/**
	 * 函数功能说明 构建会员关系
	 * zhuhuijie  2016年6月8日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param higherMember
	 * @参数： @param lowerMember     
	 * @return void    
	 * @throws
	 */
	public void buildRelations(Member higherMember,Member lowerMember);
	
	/**
	 * 函数功能说明 获取下级会员号
	 * zhuhuijie  2016年6月8日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @param higherMember
	 * @参数： @param level     
	 * @return void    
	 * @throws
	 */
	public List<String> findlowerMembers(Member higherMember,MemberRelation.Level level);
	
	Page<MemberRelation> findMemberSharePage(Member member, Integer pageNo, Integer pageSize);
}