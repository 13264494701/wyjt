package com.jxf.web.model.wyjt.app.loan;

import java.util.ArrayList;
import java.util.List;

public class LoanMoreInforResponseResult {

	
	/**
	 * 借据id
	 */
	private String loanId;

	/**
	 * 交易号
	 */
	private String tradeId;

	/**
	 * 借款人的姓名
	 */
	private String name;

	/**
	 * 金额
	 */
	private String amount;

	/**
	 * 利息
	 */
	private String interest;

	/**
	 * 申请时间
	 */
	private String askTime;

	/**
	 * 还款时间
	 */
	private String completeDate;

	/**
	 * 还款方式
	 */
	private Integer repayType;

	/**
	 * 借款用途
	 */
	private String loanPurp;

	/**
	 * 借条资金详情列表
	 */
	private List<Object> loanFundDetails = new ArrayList<Object>();
	
	/**
	 * 借条历史记录
	 */
	private List<HistoryRecord> historyRecords = new ArrayList<HistoryRecord>();
	/**
	 * 是否显示点击查看更多 0否 1是
	 */
	private Integer showMore = 0;
	
	/**
	 * 是否显示债权人 0否 1是
	 */
	private Integer showCreditor = 0;
	/**
	 * 债权人名字
	 */
	private String nowCreditor = "";
	
public class HistoryRecord{	
	
	/** 历史Id*/
	private String historyId;
	/** 名称 */
	private String name;
	/** 时间 */
	private String time;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getHistoryId() {
		return historyId;
	}
	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}
}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getAskTime() {
		return askTime;
	}

	public void setAskTime(String askTime) {
		this.askTime = askTime;
	}

	public String getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}

	public Integer getRepayType() {
		return repayType;
	}

	public void setRepayType(Integer repayType) {
		this.repayType = repayType;
	}

	public List<Object> getLoanFundDetails() {
		return loanFundDetails;
	}

	public void setLoanFundDetails(List<Object> loanFundDetails) {
		this.loanFundDetails = loanFundDetails;
	}

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public List<HistoryRecord> getHistoryRecords() {
		return historyRecords;
	}

	public void setHistoryRecords(List<HistoryRecord> historyRecords) {
		this.historyRecords = historyRecords;
	}

	public Integer getShowMore() {
		return showMore;
	}

	public void setShowMore(Integer showMore) {
		this.showMore = showMore;
	}

	public String getLoanPurp() {
		return loanPurp;
	}

	public void setLoanPurp(String loanPurp) {
		this.loanPurp = loanPurp;
	}

	public Integer getShowCreditor() {
		return showCreditor;
	}

	public void setShowCreditor(Integer showCreditor) {
		this.showCreditor = showCreditor;
	}

	public String getNowCreditor() {
		return nowCreditor;
	}

	public void setNowCreditor(String nowCreditor) {
		this.nowCreditor = nowCreditor;
	}

	
	
}
