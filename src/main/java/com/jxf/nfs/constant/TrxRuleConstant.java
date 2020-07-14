package com.jxf.nfs.constant;

import java.math.BigDecimal;

/**
 * 	交易分录常量
 * @author gaobo
 *
 */
public class TrxRuleConstant {

	/** 变更账户 交易序号 begin */
	
	/** 个人会员充值 */
	public static final String MEMBER_RECHARGE = "MB010";
	
	/** 个人会员提现(借款账户) */
	public static final String MEMBER_WITHDRAWALS = "MB020";
	/** 个人会员提现(可用余额) */
	public static final String MEMBER_WITHDRAWALS_AVL = "MB021";	
	/** 个人会员提现(手续费) */
	public static final String MEMBER_WITHDRAWALS_FEE = "MB022";
	/** 个人会员提现失败退款(借款账户) */
	public static final String MEMBER_WITHDRAWALS_REFUND_LOAN = "MB023";
	/** 个人会员提现失败退款(可用余额) */
	public static final String MEMBER_WITHDRAWALS_REFUND_AVL = "MB024";
	/** 个人会员提现失败退款(手续费) */
	public static final String MEMBER_WITHDRAWALS_REFUND_FEE = "MB025";
	
	/** 个人会员转账(付款人借款账户) */
	public static final String MEMBER_TRANSFER_LOANACT = "MB030";
	/** 个人会员转账 (付款人可用余额账户)*/
	public static final String MEMBER_TRANSFER_AVLACT = "MB031";
	
	/** 后台给借条用户加款 */
	public static final String MEMBER_BAL_ADD = "MB040";
	/** 后台给借条用户减款 */
	public static final String MEMBER_BAL_REDUCE = "MB050";
	/** 债转翻牌奖品 10元现金 */
	public static final String CR_BAL_ADD = "MB060";
	
	
	/** 借条达成-放款人可用资金转借款人-借款人申请借款放款人同意支付*/
	public static final String LOAN_DONE_AVLAMT = "LN010";
	/** 借条达成-放款人增加待收-借款人增加待还*/
	public static final String LOAN_DONE_REPAY = "LN020";
	/** 借条达成-手续费 */
	public static final String LOAN_DONE_FEE = "LN030";
		
	/** 放款人资金冻结-放款人主动放款|借款人申请借款放款人要求录视频*/
	public static final String FROZEN_LOANER_FUNDS = "LN040";
	/** 借条达成-放款人冻结资金转借款人-借款人同意放款人的主动放款|放款人审核视频通过 */
	public static final String LOAN_DONE_FROZENAMT = "LN050";
		
	/** 放款人资金解冻-借款人拒绝放款人的主动放款|放款人审核视频未通过借款申请过期 */
	public static final String UNFROZEN_LOANER_FUNDS = "LN060";	
	
	
	/**
	 *  全部还款更新账户余额操作规则区分可用余额和待收待还账户的原因：借款人在还款的时候可能会有逾期利息，
	 *  而逾期利息是不加在待收待还账户余额中的，又因为updateAct方法是按账户变更rule来调用的，
	 *  同一个rule下的item都是传的同一个amount。这样如果不在rule上区分可用余额账户和待收待还账户就会
	 *  造成在更新待收待还的时候多减去逾期利息的金额。部分还款不涉及逾期利息。
	 */
	
	/** 借款人全部还款(可用余额)*/
	public static final String REPAY_FULL_AVLBAL = "RP010";	
	/** 借款人全部还款(待还待收) */
	public static final String REPAY_FULL_PENDING_REPAY = "RP011";	
	/** 借款人申请部分还款 */
	public static final String LOANEE_APPLY_PARTIAL = "RP020";
	/** 借款人取消部分还款 */
	public static final String CANCEL_PARTIAL_REPAY= "RP030";
	/** 放款人确认同意部分还款*/
	public static final String LOANER_APPROVE_PARTIAL_REPAYMENT = "RP040";	
	/** 放款人确认同意延期申请(待收待还) */
	public static final String LOANER_APPROVE_DELAY_PENDINGREPAY = "RP041";	
	/** 放款人拒绝部分还款 */
	public static final String REFUSE_PARTIAL_PAYMENT = "RP050";
		
	/** 放款人确认线下还款 */
	public static final String LINE_DOWN_PAYMENT = "RP060";
	
	/** 借款人同意放款人的部分还款申请*/
	public static final String LOANEE_APPROVE_PARTIAL_REPAYMENT = "RP070";
	/** 借款人同意放款人的延期申请(待收待还)*/
	public static final String LOANEE_APPROVE_DELAY_PENDING_REPAY = "RP071";
	
	
	/** 公信堂借条达成服务费(借款账户)*/
	public static final String GXT_LOAN_DONE_LOANACT = "GL010";
	/** 公信堂借条达成服务费(可用余额)*/
	public static final String GXT_LOAN_DONE_AVLACT = "GL011";
	/** 公信堂借条达成服务费(微信支付)*/
	public static final String GXT_LOAN_DONE_WXPAY = "GL012";
	/** 公信堂借条达成(待收待还)*/
	public static final String GXT_LOAN_DONE_REPAY_RECEIVE = "GL013";
	
	/** 公信堂借条延期服务费(借款账户)*/
	public static final String GXT_LOAN_DELAY_LOANACT = "GD020";
	/** 公信堂借条延期服务费(可用余额)*/
	public static final String GXT_LOAN_DELAY_AVLACT = "GD021";
	/** 公信堂借条延期服务费(微信支付)*/
	public static final String GXT_LOAN_DELAY_WXPAY = "GD022";
	/** 公信堂借条延期(待收待还)*/
	public static final String GXT_LOAN_DELAY_REPAY_RECEIVE = "GD023";
	
	/** 公信堂退还服务费(借款账户)：借款人取消借条申请*/
	public static final String GXT_CANCEL_LOAN_REFUND_LOANACT = "GR010";
	/** 公信堂退还服务费(可用余额)：借款人取消借条申请*/
	public static final String GXT_CANCEL_LOAN_REFUND_AVLACT = "GR011";
	/** 公信堂退还服务费(微信支付)：借款人取消借条申请*/
	public static final String GXT_CANCEL_LOAN_REFUND_WXPAY = "GR012";
	
	
	/** 公信堂退还服务费(借款账户)：放款人拒绝借条申请*/
	public static final String GXT_REFUSE_LOAN_REFUND_LOANACT = "GR020";
	/** 公信堂退还服务费(可用余额)：放款人拒绝借条申请*/
	public static final String GXT_REFUSE_LOAN_REFUND_AVLACT = "GR021";
	/** 公信堂退还服务费(微信支付)： 放款人拒绝借条申请*/
	public static final String GXT_REFUSE_LOAN_REFUND_WXPAY = "GR022";
	
	/** 公信堂退还服务费(借款账户)：打借条申请超时*/
	public static final String GXT_TIMEOUT_LOAN_REFUND_LOANACT = "GR050";
	/** 公信堂退还服务费(可用余额)：打借条申请超时*/
	public static final String GXT_TIMEOUT_LOAN_REFUND_AVLACT = "GR051";
	/** 公信堂退还服务费(微信支付)：打借条申请超时*/
	public static final String GXT_TIMEOUT_LOAN_REFUND_WXPAY = "GR052";
	
	/** 公信堂退还服务费(借款账户)：借款人取消延期申请*/
	public static final String GXT_CANCEL_DELAY_REFUND_LOANACT = "GR030";
	/** 公信堂退还服务费(可用余额)：借款人取消延期申请*/
	public static final String GXT_CANCEL_DELAY_REFUND_AVLACT = "GR031";
	/** 公信堂借退还服务费(微信支付)：借款人取消延期申请*/
	public static final String GXT_CANCEL_DELAY_REFUND_WXPAY = "GR032";
	
	
	/** 公信堂退还服务费(借款账户)：放款人拒绝延期申请*/
	public static final String GXT_REFUSE_DELAY_REFUND_LOANACT = "GR040";
	/** 公信堂退还服务费(可用余额)：放款人拒绝延期申请*/
	public static final String GXT_REFUSE_DELAY_REFUND_AVLACT = "GR041";
	/** 公信堂退还服务费(微信支付)：放款人拒绝延期申请*/
	public static final String GXT_REFUSE_DELAY_REFUND_WXPAY = "GR042";
	
	/** 公信堂仲裁申请预缴款(微信账户) */
	public static final String GXT_ARBITRATION_PREPAY = "GA010";
	/** 公信堂仲裁退费(微信账户) */
	public static final String GXT_ARBITRATION_REFUND = "GA020";
	
	/** 公信堂申请强执预缴款(微信账户) */
	public static final String GXT_EXECUTION_PREPAY = "GE010";
	
	/** 公信堂强执失败退款 (微信账户)*/
	public static final String GXT_EXECUTION_REFUND = "GE020";
	
	
	/** 个人会员查看信用档案 */
	public static final String INDIVIDUAL_MEMBER_CHECK_CREDIT_FILES = "CA010";
	
	/** 1元档案 */
	public static final String ONE_YUAN_RECORD= "CA020";
		
	
	/** 申请催收预缴款 */
	public static final String COLLECTION_PREPAY = "CL010";
	
	/** 催收失败退款 */
	public static final String COLLECTION_FAILURE_REFUND = "CL020";
	
	
	
	
	/** 仲裁申请预缴款 */
	public static final String ARBITRATION_PREPAY = "AR010";
	
	/** 仲裁未受理退款 */
	public static final String ARBITRATION_REFUND = "AR020";
	
	/** 申请强执预缴款 */
	public static final String ARBITRATION_EXECUTION_PREPAY = "AR030";
	
	/** 强执失败退款 */
	public static final String ARBITRATION_EXECUTION_REFUND = "AR040";
	
	/** 法院诉讼费 */
	public static final String LEGAL_FARE = "LF010";
	
	
		
	/** 优放机构充值 */
	public static final String UFANG_RECHARGE = "UF010";
	
	/** 优放会员消费 */
	public static final String UFANG_CONSUME = "UF020";

	/** 优放机构派发交易金 */
	public static final String UFANG_FUND_DIST = "UF030";
	
	/** 优放机构回收交易金 */
	public static final String UFANG_FUND_COLL = "UF040";
	
	/** 后台给优放公司加款 */
	public static final String UFANG_BAL_ADD = "UF050";
	
	/** 后台给优放公司减款 */
	public static final String UFANG_BAL_REDUCE = "UF060";
		
	
	
	/** 个人会员实名认证 */
	public static final String INDIVIDUAL_MEMBER_CERTIFICATION = "CA020";
	
	/** 债权转让(借款账户) */
	public static final String CR_PAY_LOANBAL = "CR010";
	/** 债权转让(可用余额) */
	public static final String CR_PAY_AVLBAL = "CR011";
	/** 债权转让(待收账户) */
	public static final String CR_PAY_PENDINGRECEIVE= "CR012";
	/** 债权转让(手续费) */
	public static final String CR_PAY_FEE= "CR013";
	
	/** 转账单笔限额 */
	public static final Integer SINGLE_LIMIT = 5000;
	/** 转账一年限额 */
	public static final BigDecimal YEAR_LIMIT = new BigDecimal("500000");
	/** 转账一天限额 */
	public static final BigDecimal DAY_LIMIT = new BigDecimal("50000");
	/** 转账一天限制人脸认证次数 */
	public static final Integer TIMES_TODAY = 3;
	/** 申请页面显示已添加的好友的申请只显示最近15天 */
	public static final Integer SHOW_APPLY_DAYS = 15;
	
	/** 达成借条操作类型：放款人同意借款申请-0 */
	public static final Integer LOANER_AGREE_LOAN = 0;
	/** 达成借条操作类型：多人借款部分出借 -1*/
	public static final Integer LOANER_MULTI_LOAN = 1;
	/** 达成借条操作类型：审核视频通过 -2*/
	public static final Integer LOANER_PASS_VIDEO = 2;
	/** 达成借条操作类型:借款人同意主动放款-3 */
	public static final Integer LOANEE_AGREE_ACTIVE_LOAN = 3;
	
	
	/** 优放流量数据价格(信息不完善) */
	public static final Integer UFANG_DATA_LESS_PRICE = 10;
	
	/** 优放流量数据价格 (信息完善)*/
	public static final Integer UFANG_DATA_MORE_PRICE = 10;
	
}
