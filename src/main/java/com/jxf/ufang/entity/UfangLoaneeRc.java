package com.jxf.ufang.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.alibaba.fastjson.annotation.JSONField;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 风控数据Entity
 * 
 * @author wo
 * @version 2019-07-14
 */
public class UfangLoaneeRc extends CrudEntity<UfangLoaneeRc> {

	private static final long serialVersionUID = 1L;

	/**
	 * 类型
	 */
	public enum Type {

		/** 无忧借条 */
		wyjt,

		/** 今借到 */
		jjd,

	}

	/**
	 * 渠道
	 */
	public enum Channel {

		/** 无忧借条 */
		wyjt,

		/** 今借到 */
		jjd,

	}

	/** 数据类型 */
	private Type type;
	/** 姓名 */
	private String name;
	/** 手机号码 */
	private String phoneNo;
	/** 身份证号 */
	private String idNo;
	/** 当前逾期金额 */
	private BigDecimal currentOverdueAmt;
	/** 最大逾期金额 */
	private BigDecimal maxOverdueAmt;
	/** 最近还款时间 */
	private Date lastRepayTime;
	
	/** 7天逾期金额 */
	private BigDecimal overdue7daysAmt;
	/** 7天逾期笔数 */
	private Integer overdue7daysCnt;
	/** 30天逾期金额 */
	private BigDecimal overdue30daysAmt;
	/** 30天逾期笔数 */
	private Integer overdue30daysCnt;
	/** 总待还笔数 */
	private Integer totalTorepayCnt;
	/** 总待还金额 */
	private BigDecimal totalTorepayAmt;

	/** 总还款笔数 */
	private Integer totalRepayedCnt;
	/** 总还款笔数 */
	private BigDecimal totalRepayedAmt;
	/** 总借款笔数 */
	private Integer totalLoanCnt;
	/** 总借款金额 */
	private BigDecimal totalLoanAmt;
	/** 数据渠道 */
	private Channel channel;

	/** 销售量 */
	private Integer sales;

	private Date beginTime; // 开始时间

	private Date endTime; // 结束时间

	public UfangLoaneeRc() {
		super();
	}

	public UfangLoaneeRc(Long id) {
		super(id);
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Length(min = 0, max = 64, message = "姓名长度必须介于 0 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min = 0, max = 11, message = "手机号码长度必须介于 0 和 11 之间")
	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	@Length(min = 0, max = 32, message = "身份证号长度必须介于 0 和 32 之间")
	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public BigDecimal getCurrentOverdueAmt() {
		return currentOverdueAmt;
	}

	public void setCurrentOverdueAmt(BigDecimal currentOverdueAmt) {
		this.currentOverdueAmt = currentOverdueAmt;
	}
	
	public BigDecimal getMaxOverdueAmt() {
		return maxOverdueAmt;
	}

	public void setMaxOverdueAmt(BigDecimal maxOverdueAmt) {
		this.maxOverdueAmt = maxOverdueAmt;
	}
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getLastRepayTime() {
		return lastRepayTime;
	}

	public void setLastRepayTime(Date lastRepayTime) {
		this.lastRepayTime = lastRepayTime;
	}
	
	public BigDecimal getOverdue7daysAmt() {
		return overdue7daysAmt;
	}

	public void setOverdue7daysAmt(BigDecimal overdue7daysAmt) {
		this.overdue7daysAmt = overdue7daysAmt;
	}
	

	public Integer getOverdue7daysCnt() {
		return overdue7daysCnt;
	}

	public void setOverdue7daysCnt(Integer overdue7daysCnt) {
		this.overdue7daysCnt = overdue7daysCnt;
	}
	
	public BigDecimal getOverdue30daysAmt() {
		return overdue30daysAmt;
	}

	public void setOverdue30daysAmt(BigDecimal overdue30daysAmt) {
		this.overdue30daysAmt = overdue30daysAmt;
	}
	

	public Integer getOverdue30daysCnt() {
		return overdue30daysCnt;
	}

	public void setOverdue30daysCnt(Integer overdue30daysCnt) {
		this.overdue30daysCnt = overdue30daysCnt;
	}
	

	public Integer getTotalTorepayCnt() {
		return totalTorepayCnt;
	}

	public void setTotalTorepayCnt(Integer totalTorepayCnt) {
		this.totalTorepayCnt = totalTorepayCnt;
	}
	
	public BigDecimal getTotalTorepayAmt() {
		return totalTorepayAmt;
	}

	public void setTotalTorepayAmt(BigDecimal totalTorepayAmt) {
		this.totalTorepayAmt = totalTorepayAmt;
	}
	

	
	public BigDecimal getTotalRepayedAmt() {
		return totalRepayedAmt;
	}

	public void setTotalRepayedAmt(BigDecimal totalRepayedAmt) {
		this.totalRepayedAmt = totalRepayedAmt;
	}
	public Integer getTotalRepayedCnt() {
		return totalRepayedCnt;
	}

	public void setTotalRepayedCnt(Integer totalRepayedCnt) {
		this.totalRepayedCnt = totalRepayedCnt;
	}
	

	public Integer getTotalLoanCnt() {
		return totalLoanCnt;
	}

	public void setTotalLoanCnt(Integer totalLoanCnt) {
		this.totalLoanCnt = totalLoanCnt;
	}
	
	public BigDecimal getTotalLoanAmt() {
		return totalLoanAmt;
	}

	public void setTotalLoanAmt(BigDecimal totalLoanAmt) {
		this.totalLoanAmt = totalLoanAmt;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Integer getSales() {
		return sales;
	}

	public void setSales(Integer sales) {
		this.sales = sales;
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