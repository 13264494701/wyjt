package com.jxf.nfs.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;


import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberCard;
import com.jxf.payment.entity.Payment;
import com.jxf.svc.annotation.ExcelField;
import com.jxf.svc.sys.crud.entity.CrudEntity;
import com.jxf.svc.utils.DateUtils;

/**
 * 充值记录Entity
 * @author suHuimin
 * @version 2018-10-10
 */
public class NfsRchgRecord extends CrudEntity<NfsRchgRecord> {
	
	private static final long serialVersionUID = 1L;
	
	/** 充值方式 */
	public enum Type {
		
		/** 其他 -0*/
		other,
		
		/** 富友 -1*/
		fuiou,
		
		/** 连连  -2*/
		lianlian,
		
		/** 支付宝  -3*/
		aliPay,
		
		/** 微信 -4*/
		weiXin
	}	
	/** 充值渠道 */
	public enum Channel {
		
		/** PC端 */
		pc,
		
		/** APP客户端 */
		app,
		
		/** 微信小程序 */
		minipro,
		
		/** 公信堂 */
		gxt
	}	
	/** 交易状态 */
	public enum Status {
		
		/** 待支付 */
		wait,
		
		/** 支付成功 */
		success,
		
		/** 支付失败 */
		failure
	}			
	/** 充值客户 */
	private Member member;		
	/** 充值银行卡 */
	private MemberCard card;
	/** 银行名称 */
	private String bankName;
	/** 充值金额 */
	private BigDecimal amount;
	/** 充值方式 */
	private Type type;
	/** 充值渠道 */
	private Channel channel;
	/** 充值状态 */
	private Status status;
	/** 支付单 */
	private Payment payment;
	/** 手续费 */
	private BigDecimal fee;
	
	/** 充值起始时间 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date beginTime;
	/** 充值结束时间 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date endTime;
	
	/**  最大充值金额*/
	private BigDecimal maxAmount;
	/** 最小充值金额*/
	private BigDecimal minAmount;



	public NfsRchgRecord() {
		super();
	}

	public NfsRchgRecord(Long id){
		super(id);
	}


	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	public MemberCard getCard() {
		return card;
	}

	public void setCard(MemberCard card) {
		this.card = card;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	

	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	@ExcelField(title="交易状态", dictType="rchgStatus", align=2, sort=100)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public BigDecimal getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}

	public BigDecimal getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}
	
	
	@ExcelField(title="商户订单号",align=2, sort=10)
	public String getPaymentNo() {
		return getPayment().getPaymentNo();
	}
	
	@ExcelField(title="用户姓名", align=2, sort=20)
	public String getMemberName() {
		return getMember().getName();
	}	
	
	@Length(min=1, max=64, message="银行名称长度必须介于 1 和 64 之间")
	@ExcelField(title="银行名称", align=2, sort=50)
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	@Length(min=1, max=32, message="银行卡号长度必须介于 1 和 32 之间")
	@ExcelField(title="银行卡号", align=2, sort=60)
	public String getCardNo() {
		return getCard().getCardNo();
	}

	@ExcelField(title="充值金额", align=2, sort=70)
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	@ExcelField(title="第三方订单号", align=2, sort=80)
	public String getThirdOrderNo() {
		return getPayment().getThirdPaymentNo();
	}
	
	@ExcelField(title="充值时间", align=2, sort=90)
	public String getRchgTime() {
		return DateUtils.getDateStr(getCreateTime(), "yyyy-MM-dd HH:mm:ss");
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

	public void setEndTimes(Date endTime) {
		this.endTime = endTime;
	}
}