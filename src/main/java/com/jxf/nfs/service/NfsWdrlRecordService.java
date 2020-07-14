package com.jxf.nfs.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberCard;
import com.jxf.nfs.entity.NfsWdrlRecord;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;


/**
 * 提现记录Service
 * @author gaobo
 * @version 2018-10-23
 */
public interface NfsWdrlRecordService extends CrudService<NfsWdrlRecord> {
	
	/**
	 * 	验证支付密码
	 * @param member
	 * @param payNum
	 * @return
	 */
	boolean checkPayPassword(Member member, String payNum);

	/**
	 * 获取提现金额
	 * @param member
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	BigDecimal getWdrlAmount(Member member, Date startTime, Date endTime);
	
	/**
	 * 9:30-17:30 单笔提现金额 >= 3W 需要审核
	 * 17:30-次日9:30累计提现  > 3W 禁止提现
	 * @param nfsWdrlRecord 
	 * @param member
	 * @param money 
	 * @param memberCard 
	 * @param data
	 * @return
	 */
	boolean checkMoney(NfsWdrlRecord nfsWdrlRecord, Member member, BigDecimal money, MemberCard memberCard, Map<String, Object> data);
	/**
	 * 9:30-17:30 单笔提现金额 >= 3W 需要审核
	 * 17:30-次日9:30累计提现  > 3W 禁止提现
	 * @param member
	 * @param money 
	 * @return
	 */
	Map<String, Object> checkMoney( Member member, BigDecimal money);
	
	/**
	 * 生成提现记录
	 * @param member
	 * @param memberCard
	 * @param money
	 * @return
	 */
    NfsWdrlRecord createWdrlRecord(Member member, MemberCard memberCard, BigDecimal money,NfsWdrlRecord.Status status);

	
	/**
	 * 查询客服已审核的提现申请   
	 * -> 审核中的状态：auditing(待审核)，retrial(待复审)
	 * @param z
	 * @return
	 */
	Page<NfsWdrlRecord> findAuditedRecord(Page<NfsWdrlRecord> page,NfsWdrlRecord nfsWdrlRecord);
	
	/**
	 * 查询待审核的提现申请   待审核，待复审，疑似重复订单
	 * @param 
	 * @return
	 */
	Page<NfsWdrlRecord> findPendingAuditRecord(Page<NfsWdrlRecord> page,NfsWdrlRecord nfsWdrlRecord);
	
	
	/**
	 * 查询审核中的提现申请   
	 * -> 审核中的状态：auditing(待审核)，retrial(待复审)，pending(待打款)，已提交，疑似重复订单，重复订单发送
	 * @param 
	 * @return
	 */
	List<NfsWdrlRecord> findAuditingRecord(NfsWdrlRecord nfsWdrlRecord);
	
	/**
	 * 查询提现失败的提现申请   
	 * -> 审核中的状态：打款失败 7failure, 已拒绝 8 refuse, 已取消  9cancel
	 * @param 
	 * @return
	 */
	List<NfsWdrlRecord> findFailedRecord(NfsWdrlRecord nfsWdrlRecord);
	
	/**
	 * 查询前500条指定状态的提现记录
	 * 
	 * @param nfsWdrlRecord
	 * @return
	 */
	List<NfsWdrlRecord> findListByStatus(NfsWdrlRecord nfsWdrlRecord);
	
	/**
	 * 根据memberId查询借款人的提现记录
	 * @param memberId
	 * @param loanTime
	 * @return
	 */
	List<NfsWdrlRecord> getWdrlRecordByMemberId(Long memberId,Date loanTime);
	
	/**
	 * 提现提交
	 * @param member
	 * @param memberCard
	 * @param amount
	 * @param flag
	 * @return
	 */
	NfsWdrlRecord withdrawSubmit(NfsWdrlRecord wdrlRecord,int flag) throws Exception;
	
	/***
	 * 审核通过
	 * @param wdrlRecord
	 * @return
	 */
	int checkPass(NfsWdrlRecord wdrlRecord);
	
	/***
	 * 拒绝提现
	 * @param wdrlRecord
	 * @return
	 */
	int refuse(NfsWdrlRecord wdrlRecord);
	
	/***
	 * 提现失败
	 * @param wdrlRecord
	 * @param rmk 失败原因
	 * @return
	 */
	int failure(NfsWdrlRecord wdrlRecord,String rmk);
	/***
	 * @description 富友提现失败
	 * @param wdrlRecord
	 * @param rmk 失败原因
	 * @return
	 */
	int failureForFuiou(NfsWdrlRecord wdrlRecord);
	
	/***
	 * 提现取消
	 * @param wdrlRecord
	 * @return
	 */
	int cancel(NfsWdrlRecord wdrlRecord);
	/***
	 * 成功确认
	 * @param wdrlRecord
	 * @return
	 */
	int successConfirm(NfsWdrlRecord wdrlRecord);
	
	/**
	 * 查询发送失败的订单
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<NfsWdrlRecord> findFailedSendOrder(Date startTime,Date endTime,int type);

	/**
	 * 查询已发送给连连但是没返回码的订单
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<NfsWdrlRecord> findSubmitedNoRespCodeOrder(Date startTime,Date endTime,int type);
	/**
	 * 查找已发送连连的提现订单
	 * @param nfsWdrlRecord
	 */
	List<NfsWdrlRecord> findSubmitedNoThirdOrderNoRecord(NfsWdrlRecord nfsWdrlRecord);
	/**
	 *   校验提现是否需要审核
	 * @param amount
	 * @param member
	 * @return
	 */
	 int isNeedCheck(BigDecimal amount, Member member);
}