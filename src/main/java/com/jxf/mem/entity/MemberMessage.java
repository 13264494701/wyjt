package com.jxf.mem.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 会员消息Entity
 * @author gaobo
 * @version 2018-10-19
 */
public class MemberMessage extends CrudEntity<MemberMessage> {
	
	private static final long serialVersionUID = 1L;
	
	public enum Type{
		
		/**1 借款申请-单人 0*/
		loanApplication,
		
		/**1 取消借款申请 1*/
		withdrawloanApplication,
		
		/**1 提醒借款 2*/
		remindBorrowers,
		
		/**1 提醒录制视频 3*/
		recordVideo,
		
		/**1 视频上传成功 4*/
		successfulUploadVideo,
		
		/**1 提醒审核视频 5*/
		confirmVideo,
		
		/**1 视频未通过审核 6*/
		failVideo,
		
		/**1 提醒对方重新录视频 7*/
		rerecordVideo,
		
		/**2通过审核 8*/
		successfulAudit,
		
		/**2 打款成功-单人(借款人、不需要视频) 9*/
		successfulPaymentOne,
		
		/**1 打款成功-单人(放款人) 10*/
		successfulPaymentSecond,
		
		/**1 打款成功-单人(借款人、需要视频) 11*/
		successfulPaymentThree,
		
		/**1 拒绝借款申请 12*/
		refusePayment,
		
		/**1 放款申请 13*/
		lendApplication,
		
		/**1 拒绝放款申请 14*/
		refuseLendApplication,
		
		/**2 确认放款申请 15*/
		confirmLendApplication,
		
		/**2 全部还款 16*/
		fullRepayment,
		
		/**2 线下还款 17*/
		lineDownRepayment,
		
		/**2 拒绝线下还款 18*/
		refuseLineDownRepayment,
		
		/**2 部分还款申请 19*/
		partialRepaymentApplication,
		
		/**2 取消部分还款申请 20*/
		withdrawPartialRepaymentApplication,
		
		/**2 放款人提醒同意部分还款 21*/
		loanerReminderPartialRepayment,
		
		/**2 借款人提醒同意部分还款 22*/
		loaneeReminderPartialRepayment,
		
		/**2 确认部分还款申请 23*/
		confirmPartialRepaymentApplication,
		
		/**2 拒绝部分还款申请 24*/
		refusePartialRepaymentApplication,
		
		/**2 提醒收款 25*/
		reminderCollection,
		
		/**2 确认收到还款 26*/
		confirmReceiveRepayment,
		
		/**2 确认收到部分还款 27!*/
		confirmReceivePartialRepayment,
		
		/**2 延期申请 28*/
		extensionApplication,
		
		/**2 取消延期申请 29*/
		withdrawExtensionApplication,
		
		/**2 放款人提醒同意延期 30*/
		loanerReminderDelayApplication,
		
		/**2 借款人提醒同意延期 31*/
		loaneeReminderDelayApplication,
		
		/**2 确认延期申请 32*/
		confirmExtensionApplication,
		
		/**2 拒绝延期申请 33*/
		refuseExtensionApplication,
		
		/**1 多人借款 34!*/
		multiplayerLoan,
		
		/** 打款成功-多人35 !*/
		multiplayersuccessfulPayment,
		
		/** 多人借款-关闭36!*/
		multiplayerClosed,
		
		/** 转账 好友id37*/
		transferAccounts,
		
		/** 申请查看信用档案 38*/
		viewCreditFiles,
		
		/** 同意查看信用档案 39*/
		agreeViewCreditFiles,
		
		/** 拒绝查看信用档案 40*/
		refuseViewCreditFiles,
		
		/** 发送档案 41*/
		sendCreditFiles,
		
		/** 支付一元报告 42*/
		payReport,
		
		/** 查看一元报告 43*/
		checkReport,
		
		/** 申请仲裁-借款人44*/
		applicationArbitrationImsLoanee,
		
		/** 仲裁案件受理后-放款人 45*/
		acceptArbitrationImsLoaner,
		
		/** 仲裁案件受理后-借款人 46*/
		acceptArbitrationImsLoanee,
		
		/** 仲裁案件裁决后 47*/
		awardArbitrationImsLoaner,
		
		/** 缴纳仲裁费用后 48*/
		paymentArbitrationImsLoaner,
		
		/** 强执受理中49*/
		EnforcementAcceptanceImsLoaner,
		
		/** 强执缴费中50*/
		EnforcementPayImsLoaner,
		
		/** 强执进行中51*/
		EnforcementIngImsLoaner,
		
		/** 强执已结束52*/
		EnforcementEndImsLoaner,
		
		/** 强执已失效53*/
		EnforcementInvalidImsLoaner,
		
		/**2 到期前3天54 */
		repayDayBeforeThreeDay,
		
		/**2 到期当日 55*/
		repayDay,
		
		/**2逾期1天56 */
		overdueOneDay,
		
		/**2 逾期3天 57*/
		overdueThreeDay,
		
		/**2 逾期7天58 */
		overdueSevenDay,
		
		/**2 逾期15天以上 59*/
		overdueFifteenDay,
		
		/**2 申请转让 发借款人 60*/
		applyAuctionImsLoanee,
		
		/**2 转让成功 发借款人61*/
		successedAuctionImsLoanee,
		
		/**2 转让成功 发放款人 62*/
		successedAuctionImsLoaner,
		
		/**2 转让失败 超时失效 63*/
		outDateAuctionImsLoaner,
		
		/**2 转让失败 借款人已还款 64*/
		repayedAcutionImsLoaner,
		
		/**2 转让失败 审核失败 65*/
		auditFailAuctionImsLoaner,
		
		/**2 购入债转后 借款人还款成功 66*/
		paySuccessedAuctionImsLoaner,
		
		/** 单独为后台管理发站内信 不做跳转 没有模板 内容由客服填写 type = 67*/
		messageForAdmin,
		
		/** 借款人支付补借条服务费 68*/
		payFeeForLoan,
		
		/** 登录用户充值到账成功 69*/
		successfulRecharge,
		
		/** 登录用户提现到账成功 70*/
		cashWithdrawalAccount,
		
		/** 借条未达成，退还借款人服务费 71*/
		returnFee,
		
		/** 放款人支付仲裁费成功 72*/
		payArbitrationFee,
		
		/** 放款人仲裁失败 73*/
		arbitrationFailure,
		
		/** 放款人申请补借条 74!*/
		applicationLoaner,
		
		/** 借款人申请补借条 75!*/
		applicationLoanee,
		
		/**1 借款人补借条申请超时关闭 76*/
		applicationTimeoutLoanee,
		
		/**1 放款人补借条申请超时关闭 77*/
		applicationTimeoutLoaner,
		
		/**1 借款人拒绝了借条申请 78*/
		refuseApplicationLoanee,
		
		/**1 放款人拒绝了借条申请 79*/
		refuseApplicationLoaner,
		
		/**2 借款人同意了借条申请 80*/
		agreeApplicationLoanee,
		
		/**2 放款人同意了借条申请 81*/
		agreeApplicationLoaner,
		
		/**2 申请借条延期 82*/
		delayApplicationLoanee,
		
		/**2 收到借条对方的延期申请 83*/
		delayApplicationLoaner,
		
		/**2 申请人取消借条延期 84*/
		cancelDelayApplicationLoaner,
		
		/**2 被申请人拒绝了借条延期 85*/
		refuseDelayApplication,
		
		/**2 被申请人同意了借条延期 86*/
		agreeDelayApplication,
		
		/**2 借款人申请还款 87*/
		repaymentApplication,
		
		/**2 借款人申请还款-线下88 */
		repaymentApplicationLineDown,
		
		/**2 借款人取消还款申请 89*/
		cancelRepaymentApplication,
		
		/**2 放款人拒绝了借款人的还款申请 90*/
		refuseRepaymentApplication,
		
		/**2 放款人同意了借款人的还款申请 91*/
		agreeRepaymentApplication,
		
		/** 2放款人主动消条 92*/
		activeElimination,
		
		/**2 借款到期前2天还未还款，系统提醒93 */
		repayDayBeforeTwoDayLoanee,
		
		/**2 借款到期前3天还未还款，系统提醒 94*/
		repayDayBeforeThreeDayLoaner,
		
		/**2 借条今天到期，系统提醒 95*/
		repayTodayLoanee,
		
		/**2 借条今天到期，系统提醒 96*/
		repayTodayLoaner,
		
		/**2 借条今天逾期，系统提醒97 */
		overdueTodayLoanee,
		
		/**2 借条今天逾期，系统提醒 98*/
		overdueTodayLoaner,
		
		/** 放款人申请了仲裁 99!*/
		arbitrationApplication,
		
		/** 客服受理强执案件 100!*/
		toughCaseLoanee,
		
		/** 客服受理仲裁案件 101*/
		acceptanceArbitrationLoanee,
		
		/** 客服修改案件为出裁决 102*/
		acceptanceVerdictLoanee,
		
		/** 客服受理仲裁案件 103*/
		acceptanceArbitrationLoaner,
		
		/** 客服修改案件为等待组庭状态 104*/
		waitingCourt,
		
		/** 客服修改案件为出裁决 105*/
		acceptanceVerdictLoaner,
		
		/** 客服受理强执案件 106!*/
		toughCaseLoaner,
		
		/** 客服修改强执状态为已提交法院 107!*/
		SubmittedCourt,
		
		/** 客服修改强执状态为已结束 108!*/
		toughFinish,
		
		/** 强执失败/超时未交费 109!*/
		toughFail,
		
		/** 退款-强制执行服务费 110 */
		strongFailure,
		
		/** 分期还款 111 */
		amortizationLoan
		
		
	}
	public enum Group {
		
		/** app借条消息 */
		appLoanMessage,
		
		/** 公信堂交易消息 */
		gxtTransactionMessage,
		
		/** 公信堂借条消息 */
		gxtLoanMessage,
		
		/** 公信堂仲裁强执消息 */
		gxtArbitrationMessage,
		
		/** app服务消息 */
		appServiceMessage,
		
		/** app交易消息 */
		appTransactionMessage
		
		
	}
	/** 会员编号 */
	private Member member;		
	/** 显示分组 */
	private MemberMessage.Group groups;		
	/** 消息标题 */
	private String title;	
	/** 消息标题 */
	private String titleValue;	
	/** 消息内容 */
	private String content;		
	/** 是否已读 */
	private Boolean isRead;		
	/** 业务类型 */
	private MemberMessage.Type type;		
	/** 原业务ID */
	private Long orgId;	
	/** 原业务ID 类型 */
	private String orgType;	
	
	public MemberMessage() {
		super();
	}

	public MemberMessage(Long id){
		super(id);
	}
	
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	
	public MemberMessage.Group getGroups() {
		return groups;
	}

	public void setGroups(MemberMessage.Group groups) {
		this.groups = groups;
	}

	@Length(min=1, max=127, message="消息标题长度必须介于 1 和 127 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public Boolean isRead() {
		return isRead;
	}

	public void setRead(Boolean isRead) {
		this.isRead = isRead;
	}
	
	public MemberMessage.Type getType() {
		return type;
	}

	public void setType(MemberMessage.Type type) {
		this.type = type;
	}

	@NotNull(message="原业务ID不能为空")
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getTitleValue() {
		return titleValue;
	}

	public void setTitleValue(String titleValue) {
		this.titleValue = titleValue;
	}

	
	
}