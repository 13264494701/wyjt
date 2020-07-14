package com.jxf.mem.service;

import java.util.List;
import java.util.Map;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.entity.MemberMessage.Type;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 会员消息Service
 * @author gaobo
 * @version 2018-10-19
 */
public interface MemberMessageService extends CrudService<MemberMessage> {

	/**
	 * 	批量删除会员消息
	 * @param list
	 */
	void deleteMessages(List<String> list);
	
	/**
	 * 标记全部消息为已读
	 * @param member
	 */
	void setRead(Member member);

	/**
	 * 发送会员消息 
	 * @param type 消息类型
	 * @param group 消息分组
	 * @param member 收到短信的会员  
	 * @param map 内指定模板参数值 如果没有参数设为 null (发消息的人)
	 * @param orgId 原业务ID (多人我发起的：applyId； 单人/多人我收到 的申请 ：detailId； 单人借条：recordId)
	 * @param orgType 原业务ID类型  0：apply; 1:detail; 2:record; 3 其他
	 * @return 备注
	 */
	MemberMessage sendMessage(MemberMessage.Type type,MemberMessage.Group group, Member member, Map<String, Object> map,Long orgId,String orgType,String rmk);

	/**
	 * 标记单个消息为已读
	 * @param id
	 */
	void updateReadById(long id);

	/**
	 * 获取未读消息数
	 * @param member 
	 */
	int getCountsUnRead(Member member);

	/**
	 * 根据标题 获取图标地址
	 * @param integer
	 * @return
	 */
	String getImageUrl(String titleValue);

	/**
	 * 处理LN040没有orgId
	 * @return
	 */
	int UpdateNoOrgId();

	MemberMessage sendMessageForAdmin(String content, String title, Member member);
	
	/**
	 * 获取gxt交易消息
	 * @param pageNo
	 * @param pageSize
	 * @param member
	 * @return
	 */
	Page<MemberMessage> findTransactionPage(Integer pageNo, Integer pageSize, Member member);
	/**
	 * 获取gxt借条消息
	 * @param pageNo
	 * @param pageSize
	 * @param member
	 * @return
	 */
	Page<MemberMessage> findLoanPage(Integer pageNo, Integer pageSize, Member member);
	/**
	 * 获取gxt仲裁强执消息
	 * @param pageNo
	 * @param pageSize
	 * @param member
	 * @return
	 */
	Page<MemberMessage> findArbitrationPage(Integer pageNo, Integer pageSize, Member member);
	/**
	 * 查询全部gxt消息
	 * @param pageNo
	 * @param pageSize
	 * @param member
	 * @return
	 */
	Page<MemberMessage> findGxtPages(Integer pageNo, Integer pageSize, Member member);
	/**
	 * 标记已读(不同的group)
	 * @param message
	 */
	void setReadByGroup(Member member, Integer type);
	/**
	 * 查询app所有消息
	 * @param pageNo
	 * @param pageSize
	 * @param member
	 * @return
	 */
	Page<MemberMessage> findAppPages(Integer pageNo, Integer pageSize, Member member);
	/**
	 * 查询app借条消息
	 * @param pageNo
	 * @param pageSize
	 * @param member
	 * @return
	 */
	Page<MemberMessage> findAppLoanPage(Integer pageNo, Integer pageSize, Member member);
	/**
	 * 查询app服务消息
	 * @param pageNo
	 * @param pageSize
	 * @param member
	 * @return
	 */
	Page<MemberMessage> findAppServicePage(Integer pageNo, Integer pageSize, Member member);
	/**
	 * 查询app 交易消息
	 * @param pageNo
	 * @param pageSize
	 * @param member
	 * @return
	 */
	Page<MemberMessage> findAppTransactionPage(Integer pageNo, Integer pageSize, Member member);

	/**
	 * 分页查询会员消息
	 * @param pageNo
	 * @param pageSize
	 * @param member 
	 * @return
	 */
	Page<MemberMessage> findPage(Integer pageNo, Integer pageSize, Member member);
	
	/**
	 * 分页查询未读会员消息
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Page<MemberMessage> findUnReadPage(Integer pageNo, Integer pageSize,Member member);

	MemberMessage sendMessage(Type type, Long id);

}