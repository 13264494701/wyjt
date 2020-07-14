package com.jxf.mem.entity;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 实名认证Entity
 * @author wo
 * @version 2018-09-28
 */
public class MemberVerified extends CrudEntity<MemberVerified> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 状态
	 */
	public enum Status {

		/** 未认证 */
		unverified,

		/** 认证通过 */
		verified,
		
		/** 认证不通过 */
		failed
	}
	/** 会员编号 */
	private Member member;		
	/** 真实姓名 */
	private String realName;		
	/** 身份证号 */
	private String idNo;		
	/** 银行卡号 */
	private String cardNo;
	/** 手机号码 */
	private String phoneNo;
	/** 邮箱地址 */
	private String email;
	/** 图形验证码 */
	private String imageCaptcha;
	/** 图形验证码标识 */
	private String captchaId;
	/** 短信验证码 */
	private String smsCode;

	/** 状态 */
	private Status status;		
	
	public MemberVerified() {
		super();
	}

	public MemberVerified(Long id){
		super(id);
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	@Length(min=1, max=64, message="真实姓名长度必须介于 1 和 64 之间")
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	@Length(min=1, max=32, message="身份证号长度必须介于 1 和 32 之间")
	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	
	@Length(min=1, max=32, message="银行卡号长度必须介于 1 和 32 之间")
	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	@Length(min=0, max=64, message="邮箱地址长度必须介于 0 和 64 之间")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getImageCaptcha() {
		return imageCaptcha;
	}

	public void setImageCaptcha(String imageCaptcha) {
		this.imageCaptcha = imageCaptcha;
	}

	public String getCaptchaId() {
		return captchaId;
	}

	public void setCaptchaId(String captchaId) {
		this.captchaId = captchaId;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}
	

	
}