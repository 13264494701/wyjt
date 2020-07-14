package com.jxf.nfs.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.alibaba.fastjson.annotation.JSONField;
import com.jxf.mem.entity.Member;
import com.jxf.svc.annotation.ExcelField;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 提现记录Entity
 * @author SuHuiin
 * @version 2018-10-23
 */
public class NfsWdrlRecord extends CrudEntity<NfsWdrlRecord> {
	
	private static final long serialVersionUID = 1L;
	
	public enum Type{
		
		/** 系统默认值 */
		_default,
		
		/** 上海富友金融网络技术有限公司 */
		fuiou,
		
		/** 连连银通电子支付有限公司 */
		lianlian
		
	}
	public enum Status{
		
		/** 待审核  0*/
		auditing,

		/** 待复审 1*/
		retrial,
		
		/** 待打款 2*/
		pendingPay,
		
		/** 已提交 3*/
		submited,
		
		/** 疑似重复订单  4*/
		mayRepeatOrder,
		
		/** 重复订单发送(人工查询) 5*/
		sendRepeatOrder,
		
		/** 已打款 6*/
		madeMoney,
		
		/** 打款失败 7*/
		failure,
		
		/** 已拒绝 8*/
		refuse,
		
		/** 已取消  9*/
		cancel,
		
		/** 可疑订单  10*/
		questionOrder
		 
	}	
	
	public enum FailedOrderStatus{
		/** 提现失败待审核 0*/
		auditing,
		
		/** 退款成功 1*/
		refundSuccessed,
		
		/** 退款失败 2*/
		refundFailed
		
	}
	
	/**
	 * 	提现数据来源
	 * @author Administrator
	 *
	 */
	public enum Source{
		/** APP 0*/
		APP,
		
		/** 公信堂 1*/
		GXT
		
	}
	
	/** 会员 */
	private Member member;		
	/** 会员姓名 */
	private String memberName;		
	/** 付款方式 */
	private Type type;		
	/** 第三方订单号 */
	private String thirdOrderNo;		
	/** 会员提现申请金额 */
	private BigDecimal amount;		
	/** 实付款金额 */
	private BigDecimal payAmount;		
	/** 提现手续费 */
	private String fee;		
	/** 银行号 */
	private Long bankId;		
	/** 银行名称 */
	private String bankName;		
	/** 银行卡号 */
	private String cardNo;	
	/** 审核时间 */
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date checkTime;		
	/** 提交连连时间 */
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date submitTime;	
	/** 打款时间 */
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date payTime;		
	/** 提现状态 */
	private Status status;	
	/** 提现失败状态 */
	private FailedOrderStatus failedOrderStatus;
	/** 提现数据来源 */
	private Source source;
	
	/** 查询申请起始时间 */
	private Date beginTime;		
	/** 查询申请结束时间 */
	private Date endTime;
	
	/** 查询提现最大金额 */
	private BigDecimal maxAmount;		
	/** 查询提现最小金额  */
	private BigDecimal minAmount;
	
	public NfsWdrlRecord() {
		super();
	}

	public NfsWdrlRecord(Long id){
		super(id);
	}
	
	@ExcelField(title="商户订单号",align=2, sort=10)
	public String getWdrlId() {
		return getId().toString();
	}
	
	@ExcelField(title="用户姓名", align=2, sort=20)
	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	
	@NotNull(message="银行号不能为空")
	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}
	
	@Length(min=1, max=64, message="银行名称长度必须介于 1 和 64 之间")
	@ExcelField(title="银行名称", align=2, sort=30)
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	@Length(min=1, max=32, message="银行卡号长度必须介于 1 和 32 之间")
	@ExcelField(title="银行卡号", align=2, sort=40)
	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	@ExcelField(title="提现金额", align=2, sort=50)
	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}	
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="审核时间", align=2, sort=60)
	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="提交连连时间", align=2, sort=70)
	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="付款时间", align=2, sort=70)
	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	@Length(min=0, max=20, message="第三方订单号长度必须介于 0 和 20 之间")
	@ExcelField(title="第三方订单号", align=2, sort=80)
	public String getThirdOrderNo() {
		return thirdOrderNo;
	}

	public void setThirdOrderNo(String thirdOrderNo) {
		this.thirdOrderNo = thirdOrderNo;
	}
	
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@ExcelField(title="提现渠道", dictType="wdrlRecordType", align=2, sort=90)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@ExcelField(title="交易状态", dictType="wdrlRecordStatus", align=2, sort=100)
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

	public FailedOrderStatus getFailedOrderStatus() {
		return failedOrderStatus;
	}

	public void setFailedOrderStatus(FailedOrderStatus failedOrderStatus) {
		this.failedOrderStatus = failedOrderStatus;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}
}