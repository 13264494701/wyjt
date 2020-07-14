package com.jxf.mem.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 视频认证Entity
 * @author XIAORONGDIAN
 * @version 2018-10-10
 */
public class MemberVideoVerify extends CrudEntity<MemberVideoVerify> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 认证状态
	 */
	public enum Status {

		/** 待认证 */
		pendingReview,

		/** 认证通过 */
		verified,
		
		/** 认证失败 */
		failure,
		
		/** 客服已重置 */
		reset
	}
	/**
	 * 关联业务类型
	 */
	public enum Type {

		/** 实名认证APP-0 */
		realIdentity,

		/** 好友转账-1*/
		transfer,
		
		/** 修改支付密码-2*/
		setPayPwd,
		
		/** 更换手机-3 */
		changePhone
	}
	
	/**
	 * 申请渠道
	 */
	public enum Channel {

		/** APP */
		 app,
          
        /** 公信堂 */
          gxt
	}
	
	/** 会员 */
	private Member member;	
	/** 相关业务ID 修改支付密码记录ID/转账记录ID */
	private Long trxId;
	/**业务类型*/
	private Type type;
	/** 状态 */
	private Status status;		
	/** 失败原因 */
	private String failReason;		
	/** 真实姓名 */
	private String realName;		
	/** 身份证号 */
	private String idNo;		
	/** 身份证正面 */
	private String idcardFrontPhoto;		
	/** 身份证头像 */
	private String idcardPortraitPhoto;		
	/** 身份证背面 */
	private String idcardBackPhoto;		
	/** 活体照片 */
	private String livingPhoto;		
	/** 活体视频下载URL */
	private String videoUrl;		
	/** 民族 */
	private String nation;		
	/** 家庭地址 */
	private String address;	
	
	/** 申请渠道 */
	private Channel channel;
	
	/** 开始时间 */
	private Date beginTime;	
	/** 结束时间*/
	private Date endTime;	
	
	public MemberVideoVerify() {
		super();
	}

	public MemberVideoVerify(Long id){
		super(id);
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	public Long getTrxId() {
		return trxId;
	}

	public void setTrxId(Long trxId) {
		this.trxId = trxId;
	}
	
	@Length(min=0, max=255, message="失败原因长度必须介于 0 和 255 之间")
	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
	
	@Length(min=0, max=255, message="真实姓名长度必须介于 0 和 255 之间")
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	@Length(min=0, max=255, message="身份证正面长度必须介于 0 和 255 之间")
	public String getIdcardFrontPhoto() {
		return idcardFrontPhoto;
	}

	public void setIdcardFrontPhoto(String idcardFrontPhoto) {
		this.idcardFrontPhoto = idcardFrontPhoto;
	}
	
	@Length(min=0, max=255, message="身份证头像长度必须介于 0 和 255 之间")
	public String getIdcardPortraitPhoto() {
		return idcardPortraitPhoto;
	}

	public void setIdcardPortraitPhoto(String idcardPortraitPhoto) {
		this.idcardPortraitPhoto = idcardPortraitPhoto;
	}
	
	@Length(min=0, max=255, message="身份证背面长度必须介于 0 和 255 之间")
	public String getIdcardBackPhoto() {
		return idcardBackPhoto;
	}

	public void setIdcardBackPhoto(String idcardBackPhoto) {
		this.idcardBackPhoto = idcardBackPhoto;
	}
	
	@Length(min=0, max=255, message="活体照片长度必须介于 0 和 255 之间")
	public String getLivingPhoto() {
		return livingPhoto;
	}

	public void setLivingPhoto(String livingPhoto) {
		this.livingPhoto = livingPhoto;
	}
	
	@Length(min=0, max=255, message="活体视频下载URL长度必须介于 0 和 255 之间")
	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	
	@Length(min=0, max=200, message="民族长度必须介于 0 和 200 之间")
	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}
	
	@Length(min=0, max=200, message="家庭地址长度必须介于 0 和 200 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}



	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
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