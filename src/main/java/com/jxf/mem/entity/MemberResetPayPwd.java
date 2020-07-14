package com.jxf.mem.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 修改支付密码Entity
 * @author XIAORONGDIAN
 * @version 2018-11-08
 */
public class MemberResetPayPwd extends CrudEntity<MemberResetPayPwd> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 状态
	 */
	public enum Status {

		/** 待审核 */
		pendingReview,

		/** 审核通过 */
		verified,
		
		/** 审核失败 */
		failure,
	}
	
	/** 会员 */
	private Member member;		
	/** 支付密码 */
	private String payPwd;		
	/** 认证状态 0待审核 1 认证成功 2认证失败  3微信小程序修改密码 */
	private Status status;		
	/** 失败原因 */
	private String failReason;	
	/** 视频认证 */	
	private MemberVideoVerify videoVerify;
	
	/** 开始日期   */
	private Date beginTime;
	
	/** 结束日期  */
	private Date endTime;
	
	public MemberResetPayPwd() {
		super();
	}

	public MemberResetPayPwd(Long id){
		super(id);
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	@Length(min=0, max=255, message="支付密码长度必须介于 0 和 255 之间")
	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}
	
	@Length(min=0, max=255, message="失败原因长度必须介于 0 和 255 之间")
	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public MemberVideoVerify getVideoVerify() {
		return videoVerify;
	}

	public void setVideoVerify(MemberVideoVerify videoVerify) {
		this.videoVerify = videoVerify;
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