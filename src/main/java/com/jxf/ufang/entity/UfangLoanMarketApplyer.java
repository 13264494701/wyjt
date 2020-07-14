package com.jxf.ufang.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;


import com.jxf.mem.entity.Member;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 优放贷申请人Entity
 * @author suHuimin
 * @version 2019-03-28
 */
public class UfangLoanMarketApplyer extends CrudEntity<UfangLoanMarketApplyer> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 实名认证状态
	 */
	public enum RealNameStatus {

		/** 未认证 */
		unauth,

		/** 已认证 */
		authed
	}
	
	/**
	 * 芝麻分认证状态
	 */
	public enum SesameStatus {

		/** 未认证 */
		unauth,

		/** 已认证 */
		authed
	}
	
	/**
	 * 运营商认证状态
	 */
	public enum OperatorStatus {

		/** 未认证 */
		unauth,

		/** 已认证 */
		authed
	}
	/**
	 *   申请渠道
	 */
	public enum Channel {

		/** APP */
		app,

		/** 微信公众号 */
		weixinPublic
	}
	
	/**
	 * 推送状态
	 */
	public enum PushStatus {
		
        /**
         * 待推送
         */
        pendingPush,

        /**
         * 已推送
         */
        hasPush
	}
	
	private UfangLoanMarket ufangLoanMarket;
	/** 手机号码 */
	private String phoneNo;		
	/** 申请人姓名 */
	private String name;		
	/** 身份证号 */
	private String idNo;		
	/** 银行卡号 */
	private String cardNo;	
	/** QQ号码 */
	private String qqNo;
	/** 微信号 */
	private String weixinNo;		
	/** 实名认证状态 0已认证,1未认证 */
	private RealNameStatus realNameStatus;		
	/** 芝麻分认证状态 0未认证1已认证 */
	private SesameStatus sesameStatus;		
	/** 芝麻分 */
	private int sesameScore;		
	/** 运营商认证状态 0：未认证；1：已认证 */
	private OperatorStatus operatorStatus;
	/** 申请渠道*/
	private Channel channel;
	/** 运营商报告查看地址 */
	private String reportTaskId;		
	/** 是否是app注册用户 0 不是 ,1 是 */
	private String appRegister;		
	/** app注册用户 */
	private Member member;
	/** 申请IP*/
	private String applyIp;
	/** 申请地区*/
	private String applyArea;
	/** 手机地区*/
	private String phoneArea;
	/** 申请次数*/
	private Integer applyTimes;
	
	/** 查询申请起始时间 */
	private Date beginTime;		
	/** 查询申请结束时间 */
	private Date endTime;
	
	
	/** 推送状态 */
	private PushStatus pushStatus;
	

	public UfangLoanMarketApplyer() {
		super();
	}

	public UfangLoanMarketApplyer(Long id){
		super(id);
	}

	public UfangLoanMarket getUfangLoanMarket() {
		return ufangLoanMarket;
	}

	public void setUfangLoanMarket(UfangLoanMarket ufangLoanMarket) {
		this.ufangLoanMarket = ufangLoanMarket;
	}

	@Length(min=1, max=11, message="手机号码长度必须介于 1 和 11 之间")
	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	@Length(min=0, max=64, message="申请人姓名长度必须介于 0 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=32, message="身份证号长度必须介于 0 和 32 之间")
	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	
	@Length(min=0, max=64, message="银行卡号长度必须介于 0 和 64 之间")
	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	@Length(min=0, max=32, message="微信号长度必须介于 0 和 32 之间")
	public String getWeixinNo() {
		return weixinNo;
	}

	public void setWeixinNo(String weixinNo) {
		this.weixinNo = weixinNo;
	}
	
	
	public RealNameStatus getRealNameStatus() {
		return realNameStatus;
	}

	public void setRealNameStatus(RealNameStatus realNameStatus) {
		this.realNameStatus = realNameStatus;
	}

	public SesameStatus getSesameStatus() {
		return sesameStatus;
	}

	public void setSesameStatus(SesameStatus sesameStatus) {
		this.sesameStatus = sesameStatus;
	}

	public int getSesameScore() {
		return sesameScore;
	}

	public void setSesameScore(int sesameScore) {
		this.sesameScore = sesameScore;
	}

	public OperatorStatus getOperatorStatus() {
		return operatorStatus;
	}

	public void setOperatorStatus(OperatorStatus operatorStatus) {
		this.operatorStatus = operatorStatus;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public String getReportTaskId() {
		return reportTaskId;
	}

	public void setReportTaskId(String reportTaskId) {
		this.reportTaskId = reportTaskId;
	}

	@Length(min=0, max=1, message="是否是app注册用户 0 不是 ,1 是长度必须介于 0 和 1 之间")
	public String getAppRegister() {
		return appRegister;
	}

	public void setAppRegister(String appRegister) {
		this.appRegister = appRegister;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Integer getApplyTimes() {
		return applyTimes;
	}

	public void setApplyTimes(Integer applyTimes) {
		this.applyTimes = applyTimes;
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

	public PushStatus getPushStatus() {
		return pushStatus;
	}

	public void setPushStatus(PushStatus pushStatus) {
		this.pushStatus = pushStatus;
	}

	public String getApplyIp() {
		return applyIp;
	}

	public void setApplyIp(String applyIp) {
		this.applyIp = applyIp;
	}

	public String getApplyArea() {
		return applyArea;
	}

	public void setApplyArea(String applyArea) {
		this.applyArea = applyArea;
	}

	public String getQqNo() {
		return qqNo;
	}

	public void setQqNo(String qqNo) {
		this.qqNo = qqNo;
	}

	public String getPhoneArea() {
		return phoneArea;
	}

	public void setPhoneArea(String phoneArea) {
		this.phoneArea = phoneArea;
	}

	
}