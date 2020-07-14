package com.jxf.nfs.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import com.jxf.mem.entity.Member;
import com.jxf.svc.annotation.ExcelField;
import com.jxf.svc.sys.crud.entity.CrudEntity;
import com.jxf.svc.utils.DateUtils;

/**
 * 充值流水Entity
 * @author wo
 * @version 2019-03-24
 */
public class NfsRchgActTrx extends CrudEntity<NfsRchgActTrx> {
	
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
	
	/** 交易状态 */
	public enum Status {
		
		/** 待支付 */
		wait,
		
		/** 支付成功 */
		success,
		
		/** 支付失败 */
		failure
	}
	
	/** 充值会员 */
	private Member member;			
	/** 充值金额 */
	private BigDecimal trxAmt;		
	/** 账户余额 */
	private BigDecimal curBal;		
	/** 充值方式 */
	private Type type;		
	/** 充值编号 */
	private Long rchgId;		
	/** 第三方支付订单号 */
	private String thirdPaymentNo;		
	/** 银行卡号 */
	private String cardNo;		
	/** 银行名称 */
	private String bankName;		
	/** 交易状态 */
	private Status status;
	
	private Date beginTime;		// 开始 充值时间
	
	private Date endTime;		// 结束 充值时间
	
	public NfsRchgActTrx() {
		super();
	}

	public NfsRchgActTrx(Long id){
		super(id);
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	
	public void setType(Type type) {
		this.type = type;
	}
	public void setTrxAmt(BigDecimal trxAmt) {
		this.trxAmt = trxAmt;
	}
	public void setCurBal(BigDecimal curBal) {
		this.curBal = curBal;
	}
	
	public void setThirdPaymentNo(String thirdPaymentNo) {
		this.thirdPaymentNo = thirdPaymentNo;
	}
	
	@NotNull(message="充值编号不能为空")
	public Long getRchgId() {
		return rchgId;
	}

	public void setRchgId(Long rchgId) {
		this.rchgId = rchgId;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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

	@ExcelField(title="商户订单号",align=2, sort=10)
	public String getRchgNo() {
		return String.valueOf(rchgId);
	}
		
	@ExcelField(title="用户姓名", align=2, sort=20)
	public String getMemberName() {
		return getMember().getName();
	}	
	
	@ExcelField(title="充值金额", align=2, sort=30)
	public BigDecimal getTrxAmt() {
		return trxAmt;
	}
	
	@ExcelField(title="账户余额", align=2, sort=40)
	public BigDecimal getCurBal() {
		return curBal;
	}
	
	@ExcelField(title="充值方式", dictType="rchgType", align=2, sort=50)
	public Type getType() {
		return type;
	}
	
	@Length(min=0, max=64, message="第三方支付订单号长度必须介于 0 和 64 之间")
	@ExcelField(title="第三方订单号", align=2, sort=60)
	public String getThirdPaymentNo() {
		return thirdPaymentNo;
	}
	
	@Length(min=0, max=255, message="银行名称长度必须介于 0 和 255 之间")
	@ExcelField(title="银行名称", align=2, sort=70)
	public String getBankName() {
		return bankName;
	}

	
	@Length(min=0, max=32, message="银行卡号长度必须介于 0 和 32 之间")
	@ExcelField(title="银行卡号", align=2, sort=80)
	public String getCardNo() {
		return cardNo;
	}

	@ExcelField(title="充值时间", align=2, sort=90)
	public String getRchgTime() {
		return DateUtils.getDateStr(getCreateTime(), "yyyy-MM-dd HH:mm:ss");
	}

		
}