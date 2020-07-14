package com.jxf.web.model.gxt;

import com.jxf.svc.config.Global;

/**
 * @作者: xiaorongdian
 * @创建时间 :2019年4月30日 下午3:06:49
 * @功能说明:公信堂借条详情
 */
public class LoanDetailForGxtResponseResult {
	
	
	/** 借条刷新用*/
	private String loan_type;
	
	/**
	 * 借条编号
	 */
	private String loanId;

	/**
	 * 借款人姓名
	 */
	private String loaneeName;
	/**
	 * 借款人Id
	 */
	private String loaneeId = "";
	/**
	 * 借款人头像
	 */
	private String loaneeHeadimage = "";
	/** 放款人姓名 */
	private String loanerName;
	/**
	 * 放款人Id
	 */
	private String loanerId = "";
	/** 放款人头像 */
	private String loanerHeadimage = "";	
	/** 延期费用 */
	private String delayFee = Global.getConfig("gxt.loanDelayFee");	
	/** 补借条费用 */
	private String applyLoanFee = Global.getConfig("gxt.loanDoneFee");;	
	/** 进度*/
	private String progress;	
	/** 状态 
	 *  0:待确认
		1:已取消
		2:已拒绝
		3:已超时
		4:今日还款/今日收款
		5:距离还款/收款日30天之内
		6:距离还款/收款日30天以上
		7:延期待确认
		8:还款/收款待确认
		9:今日逾期
		10:已逾期未超过15天
		11:已逾期超过15天
		12:已完成
	 * */
	private Integer status;	
	/**
	 * 距离收款/还款天数 逾期天数 status=5,10时有用 默认0
	 */
	private Integer days = 0;
	
	/**
	 * 延期还款状态，0 未超过5次,1 提示 已申请延期5次，不能申请延期
	 */
	private int delayStatus = 0;
	/**
	 * 仲裁状态 0 未仲裁 1已仲裁
	 */
	private Integer arbitrationStatus = 0;
	/**
	 * 仲裁流程界面(点击仲裁进度)
	 */
	private String arbitrationRecordUrl;
	/**
	 * 剩余的 天/小时 关闭 0不显示 1天 2小时
	 */
	private Integer remainStatus = 0;
	/**
	 * 剩余的天数/小时 默认0
	 */
	private Integer remainTime = 0;
	/**
	 * 当前登录人信息
	 */
	public UserInfo userInfo = new UserInfo();
	
	/**
	 * 借条信息
	 */
	public LoanInfo loanInfo = new LoanInfo();
	/**
	 * 交易信息
	 */
	public TradeInfo tradeInfo = new TradeInfo();
	/**
	 * 逾期信息
	 */
	public OverDueInfo overDueInfo = new OverDueInfo();
	/**
	 * 延期后借条信息
	 */
	public AfterDelayInfo afterDelayInfo = new AfterDelayInfo();
	
	//当前角色信息
	public class UserInfo {
		/**
		 * 当前角色身份 0借款人 1放款人 2无关人士
		 */
		private Integer loanRole = 2;
		/**
		 * 当前角色是否是
		 * 补借条申请/延期 的
		 * 发起者 0否 1是 
		 */
		private Integer initiator = 0;
		/**
		 * 姓名
		 */
		private String name = "未实名";
		/**
		 * 用户账户(就是电话)
		 */
		private String username = "";
		/**
		 * 实名认证 状态 0否 1已实名
		 */
		private Integer realIdentityStatus = 0;
		/**
		 * 账户余额(借款账户+可用余额)
		 */
		private String curBal;
		
		public Integer getLoanRole() {
			return loanRole;
		}
		public void setLoanRole(Integer loanRole) {
			this.loanRole = loanRole;
		}
		public Integer getInitiator() {
			return initiator;
		}
		public void setInitiator(Integer initiator) {
			this.initiator = initiator;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCurBal() {
			return curBal;
		}
		public void setCurBal(String curBal) {
			this.curBal = curBal;
		}
		public Integer getRealIdentityStatus() {
			return realIdentityStatus;
		}
		public void setRealIdentityStatus(Integer realIdentityStatus) {
			this.realIdentityStatus = realIdentityStatus;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		
	}
	
	
	public class LoanInfo{//借贷信息
		
		/** 应还金额 */
		private String dueRepayAmount;	
		
		/** 借款金额 */
		private String amount;	
	
		/**
		 * 还款方式 0到期还本付息 1按月等额本息
		 */
		private Integer repayType;
	
		/**
		 * 开始日期
		 */
		private String beginDate;
	
		/**
		 * 还款日期
		 */
		private String dueRepayDate;
		/**
		 * 利息
		 */
		private String interest;
	
		/**
		 * 借款用途
		 */
		private String loanPurp;
	
		/**
		 * 争议解决方式 0仲裁 1诉讼
		 */
		private Integer disputeResolution = 0;
		/**
		 * 借款协议地址
		 */
		private String contractUrl;
		/**
		 * 备注
		 */
		private String rwk = "";
		
		public String getAmount() {
			return amount;
		}
		public void setAmount(String amount) {
			this.amount = amount;
		}
		public Integer getRepayType() {
			return repayType;
		}
		public void setRepayType(Integer repayType) {
			this.repayType = repayType;
		}
		public String getBeginDate() {
			return beginDate;
		}
		public void setBeginDate(String beginDate) {
			this.beginDate = beginDate;
		}
		public String getDueRepayDate() {
			return dueRepayDate;
		}
		public void setDueRepayDate(String dueRepayDate) {
			this.dueRepayDate = dueRepayDate;
		}
		public String getInterest() {
			return interest;
		}
		public void setInterest(String interest) {
			this.interest = interest;
		}
		public String getLoanPurp() {
			return loanPurp;
		}
		public void setLoanPurp(String loanPurp) {
			this.loanPurp = loanPurp;
		}
		public Integer getDisputeResolution() {
			return disputeResolution;
		}
		public void setDisputeResolution(Integer disputeResolution) {
			this.disputeResolution = disputeResolution;
		}
		public String getContractUrl() {
			return contractUrl;
		}
		public void setContractUrl(String contractUrl) {
			this.contractUrl = contractUrl;
		}
		public String getRwk() {
			return rwk;
		}
		public void setRwk(String rwk) {
			this.rwk = rwk;
		}
		public String getDueRepayAmount() {
			return dueRepayAmount;
		}
		public void setDueRepayAmount(String dueRepayAmount) {
			this.dueRepayAmount = dueRepayAmount;
		}
	}
	
	//交易信息
	public class TradeInfo {
		/** 申请时间 */
		private String applyDate;
		/** 达成时间 */
		private String reachDate = "";
		/** 结束时间 */
		private String completeDate = "";
		
		public String getApplyDate() {
			return applyDate;
		}
		public void setApplyDate(String applyDate) {
			this.applyDate = applyDate;
		}
		public String getReachDate() {
			return reachDate;
		}
		public void setReachDate(String reachDate) {
			this.reachDate = reachDate;
		}
		public String getCompleteDate() {
			return completeDate;
		}
		public void setCompleteDate(String completeDate) {
			this.completeDate = completeDate;
		}
	}
	
	//逾期信息
	public class OverDueInfo {
		/** 逾期天数 */
		private String overdueDays = "";
		/** 逾期利息 */
		private String overdueInterest = "";
		public String getOverdueDays() {
			return overdueDays;
		}
		public void setOverdueDays(String overdueDays) {
			this.overdueDays = overdueDays;
		}
		public String getOverdueInterest() {
			return overdueInterest;
		}
		public void setOverdueInterest(String overdueInterest) {
			this.overdueInterest = overdueInterest;
		}
		
	}
	//延期后借条信息
	public class AfterDelayInfo {
		/** 借款金额 */
		private String amount = "";
		/** 逾期利息 */
		private String loanStart = "";
		/** 还款日期 */
		private String dueRepayDate = "";
		/** 利息 */
		private String interest = "";
		/** 延期申请的ID */
		private String delayId = "";
		
		public String getAmount() {
			return amount;
		}
		public void setAmount(String amount) {
			this.amount = amount;
		}
		public String getLoanStart() {
			return loanStart;
		}
		public void setLoanStart(String loanStart) {
			this.loanStart = loanStart;
		}
		public String getDueRepayDate() {
			return dueRepayDate;
		}
		public void setDueRepayDate(String dueRepayDate) {
			this.dueRepayDate = dueRepayDate;
		}
		public String getInterest() {
			return interest;
		}
		public void setInterest(String interest) {
			this.interest = interest;
		}
		public String getDelayId() {
			return delayId;
		}
		public void setDelayId(String delayId) {
			this.delayId = delayId;
		}
	}
	public String getLoanId() {
		return loanId;
	}
	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}
	public String getLoaneeName() {
		return loaneeName;
	}
	public void setLoaneeName(String loaneeName) {
		this.loaneeName = loaneeName;
	}
	public String getLoaneeHeadimage() {
		return loaneeHeadimage;
	}
	public void setLoaneeHeadimage(String loaneeHeadimage) {
		this.loaneeHeadimage = loaneeHeadimage;
	}
	public String getLoanerName() {
		return loanerName;
	}
	public void setLoanerName(String loanerName) {
		this.loanerName = loanerName;
	}
	public String getLoanerHeadimage() {
		return loanerHeadimage;
	}
	public void setLoanerHeadimage(String loanerHeadimage) {
		this.loanerHeadimage = loanerHeadimage;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public LoanInfo getLoanInfo() {
		return loanInfo;
	}
	public void setLoanInfo(LoanInfo loanInfo) {
		this.loanInfo = loanInfo;
	}
	public TradeInfo getTradeInfo() {
		return tradeInfo;
	}
	public void setTradeInfo(TradeInfo tradeInfo) {
		this.tradeInfo = tradeInfo;
	}
	public OverDueInfo getOverDueInfo() {
		return overDueInfo;
	}
	public void setOverDueInfo(OverDueInfo overDueInfo) {
		this.overDueInfo = overDueInfo;
	}
	public AfterDelayInfo getAfterDelayInfo() {
		return afterDelayInfo;
	}
	public void setAfterDelayInfo(AfterDelayInfo afterDelayInfo) {
		this.afterDelayInfo = afterDelayInfo;
	}
	public int getDelayStatus() {
		return delayStatus;
	}
	public void setDelayStatus(int delayStatus) {
		this.delayStatus = delayStatus;
	}
	public String getArbitrationRecordUrl() {
		return arbitrationRecordUrl;
	}
	public void setArbitrationRecordUrl(String arbitrationRecordUrl) {
		this.arbitrationRecordUrl = arbitrationRecordUrl;
	}
	public Integer getRemainStatus() {
		return remainStatus;
	}
	public void setRemainStatus(Integer remainStatus) {
		this.remainStatus = remainStatus;
	}
	public Integer getRemainTime() {
		return remainTime;
	}
	public void setRemainTime(Integer remainTime) {
		this.remainTime = remainTime;
	}
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public String getLoan_type() {
		return loan_type;
	}
	public void setLoan_type(String loan_type) {
		this.loan_type = loan_type;
	}
	public String getProgress() {
		return progress;
	}
	public void setProgress(String progress) {
		this.progress = progress;
	}
	public Integer getArbitrationStatus() {
		return arbitrationStatus;
	}
	public void setArbitrationStatus(Integer arbitrationStatus) {
		this.arbitrationStatus = arbitrationStatus;
	}
	public String getDelayFee() {
		return delayFee;
	}
	public void setDelayFee(String delayFee) {
		this.delayFee = delayFee;
	}
	public String getApplyLoanFee() {
		return applyLoanFee;
	}
	public void setApplyLoanFee(String applyLoanFee) {
		this.applyLoanFee = applyLoanFee;
	}
	public String getLoaneeId() {
		return loaneeId;
	}
	public void setLoaneeId(String loaneeId) {
		this.loaneeId = loaneeId;
	}
	public String getLoanerId() {
		return loanerId;
	}
	public void setLoanerId(String loanerId) {
		this.loanerId = loanerId;
	}
}
