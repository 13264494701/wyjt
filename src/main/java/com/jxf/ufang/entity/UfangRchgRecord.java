package com.jxf.ufang.entity;

import com.jxf.payment.entity.Payment;
import com.jxf.svc.annotation.ExcelField;
import com.jxf.svc.sys.crud.entity.CrudEntity;
import com.jxf.svc.utils.DateUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 优放充值记录Entity
 *
 * @author Administrator
 * @version 2018-12-07
 */
public class UfangRchgRecord extends CrudEntity<UfangRchgRecord> {

    /**
     * 充值方式
     */
    public enum Type {

        /**
         * 线上
         */
        online,

        /**
         * 线下
         */
        offline
    }

    /**
     * 充值渠道
     */
    public enum Channel {

        /**
         * PC端
         */
        pc,

        /**
         * APP应用程序
         */
        app,

        /**
         * 微信小程序
         */
        minipro
    }

    /**
     * 交易状态
     */
    public enum Status {

        /**
         * 等待支付
         */
        wait,

        /**
         * 支付成功
         */
        success,

        /**
         * 支付失败
         */
        failure
    }

    private static final long serialVersionUID = 1L;
    /**
     * 充值客户号
     */
    private UfangUser user;

    /**
     * 充值金额
     */
    private BigDecimal amount;
    /**
     * 充值方式
     */
    private Type type;
    /**
     * 充值渠道
     */
    private Channel channel;
    /**
     * 充值状态
     */
    private Status status;
    /**
     * 支付单
     */
    private Payment payment;

    /** 开始时间 */
    private Date beginTime;
    /** 结束时间*/
    private Date endTime;

    public UfangRchgRecord() {
        super();
    }

    public UfangRchgRecord(Long id) {
        super(id);
    }

    public UfangUser getUser() {
        return user;
    }

    public void setUser(UfangUser user) {
        this.user = user;
    }
    public Type getType() {
        return type;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
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
    
	@ExcelField(title="用户姓名", align=2, sort=10)
	public String getEmpName() {
		return getUser().getEmpNam();
	}	
	
    @ExcelField(title="充值金额", align=2, sort=20)
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
	@ExcelField(title="商户订单号",align=2, sort=30)
	public String getPaymentNo() {
		return getPayment().getPaymentNo();
	}
	

	@ExcelField(title="第三方订单号", align=2, sort=40)
	public String getThirdOrderNo() {
		return getPayment().getThirdPaymentNo();
	}
	

	@ExcelField(title="充值状态", dictType="rchgStatus", align=2, sort=50)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}