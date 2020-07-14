package com.jxf.nfs.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.mem.entity.Member;
import com.jxf.nfs.entity.NfsWdrlRecord;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;


/**
 * 提现记录DAO接口
 * @author gaobo
 * @version 2018-10-23
 */
@MyBatisDao
public interface NfsWdrlRecordDao extends CrudDao<NfsWdrlRecord> {

	/**
	 * 获取提现金额
	 * @param member
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	BigDecimal getWdrlAmount(@Param("member")Member member, @Param("startTime") Date startTime,@Param("endTime") Date endTime);

	
	/**
	 * 查询待审核的提现申请   待审核，待复审，疑似重复订单
	 * @param 
	 * @return
	 */
	List<NfsWdrlRecord> findPendingAuditRecord(NfsWdrlRecord nfsWdrlRecord);
	
	/**
	 * 查询客服已审核的提现申请   
	 * @param z
	 * @return
	 */
	List<NfsWdrlRecord> findAuditedRecord(NfsWdrlRecord nfsWdrlRecord);
	
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
	List<NfsWdrlRecord> getWdrlRecordByMemberId(@Param("memberId")Long memberId,@Param("loanTime")Date loanTime);
	
	/**
	 * 查询发送失败的订单
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<NfsWdrlRecord> findFailedSendOrder(@Param("startTime") Date startTime,@Param("endTime") Date endTime,@Param("type") int type);

	/**
	 * 查询已发送给连连但是没返回码的订单
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<NfsWdrlRecord> findSubmitedNoRespCodeOrder(@Param("startTime") Date startTime,@Param("endTime") Date endTime,@Param("type") int type);
	/**
	 * 查找已发送连连的提现订单
	 * @param nfsWdrlRecord
	 */
	List<NfsWdrlRecord> findSubmitedNoThirdOrderNoRecord(NfsWdrlRecord nfsWdrlRecord);
}
