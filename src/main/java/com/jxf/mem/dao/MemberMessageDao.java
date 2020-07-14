package com.jxf.mem.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 会员消息DAO接口
 * @author gaobo
 * @version 2018-10-19
 */
@MyBatisDao
public interface MemberMessageDao extends CrudDao<MemberMessage> {

	/**
	 * 	批量删除会员消息
	 * @param list
	 */
	void deleteMessages(List<String> list);

	/**
	 * 标记全部消息为已读
	 * @param member
	 */
	void setRead(@Param("member")Member member);

	
	/**
	 * 标记单个消息为已读
	 * @param id
	 */
	void updateReadById(long id);

	
	/**
	 * 获取未读消息数
	 */
	int getCountsUnRead(@Param("member")Member member);
	/**
	 * 处理LN040没有orgId
	 * @return
	 */
	int UpdateNoOrgId();

	List<MemberMessage> findGxtLists(MemberMessage memberMessage);
	/**
	 * 标记已读(不同的group)
	 * @param message
	 */
	void setReadByGroup(@Param("member")Member member, @Param("type")Integer type);
	/**
	 * 查询所有app消息
	 * @param memberMessage
	 * @return
	 */
	List<MemberMessage> findAppLists(MemberMessage memberMessage);

}