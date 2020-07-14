package com.jxf.nfs.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.jxf.mem.entity.Member;
import com.jxf.svc.sys.crud.entity.CrudEntity;
import com.jxf.svc.utils.enumUtils.BaseCodeEnum;

/**
 * 转账Entity
 * @author XIAORONGDIAN
 * @version 2018-11-09
 */
public class NfsTransferRecord extends CrudEntity<NfsTransferRecord> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 状态
	 */
	public enum Status implements BaseCodeEnum{

		/** 待审核 */
		pendingReview("人脸认证待审核", 0),

		/** 审核失败 */
		failure("人脸认证审核失败", 1),
		
		/** 已收款 */
		alreadyReceived("已成功收款", 2);
		
		private int code;
		private String name;
		Status(String name,int code) {
			this.code = code; 
			this.name = name; 
		}
		 
		@Override
		public int getCode() { return this.code; }
		@Override
		public String getName() { return this.name; }
	}
	
	
	/** 会员 */
	private Member member;		
	/** 好友 */
	private Member friend;	
	/** 金额 */
	private BigDecimal amount;	
	/** 状态 */
	private Status status;		
	/** 失败原因 */
	private String failReason;		
	
	/** 开始日期   */
	private Date beginTime;
	
	/** 结束日期  */
	private Date endTime;
	
	public NfsTransferRecord() {
		super();
	}

	public NfsTransferRecord(Long id){
		super(id);
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Member getFriend() {
		return friend;
	}

	public void setFriend(Member friend) {
		this.friend = friend;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}




	
}