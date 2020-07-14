package com.jxf.web.model.wyjt.app.member;
/**
 * @作者: xiaorongdian
 * @创建时间 :2018年11月6日 下午5:32:19
 * @功能说明: 账户流水
 */
public class ActTrxListRequestParam {

	/** 页码*/
	private Integer pageNo;
	/** 分组 -1 全部  0充值提现记录  1转账  2借款记录  3还款记录 4法律仲裁  5催收 */
	private Integer group;
	
	/** 月份  示例 2018-09 */
	private String month;
	
	/** 开始日期  示例   2018-09-01 */
	private String startTime;
	
	/** 结束日期  */
	private String endTime;
	
	/** 开始金额  */
	private String startMoney;
	
	/** 结束金额  */
	private String endMoney;
	
	/** 快捷筛选类型   1 近三个月的充值   2近三个月的转账   3近三个月的放款  4 近三个月的收款   */
	private String type;
	
	
	public Integer getGroup() {
		return group;
	}

	public void setGroup(Integer group) {
		this.group = group;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getEndMoney() {
		return endMoney;
	}

	public void setEndMoney(String endMoney) {
		this.endMoney = endMoney;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStartMoney() {
		return startMoney;
	}

	public void setStartMoney(String startMoney) {
		this.startMoney = startMoney;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	} 
	
}
