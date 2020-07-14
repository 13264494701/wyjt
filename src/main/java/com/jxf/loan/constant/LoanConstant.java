package com.jxf.loan.constant;

import com.jxf.svc.config.Global;


/**
 * 	借条常量
 * @author xrd
 *
 */
public class LoanConstant {

	
	/** 要求发送视频时读的文字 */
    public static final String NEED_VIDEO_CHECK = "我是${loanee}\n今日向${loaner}借款${amount}元\n自愿支付利息${interset}元\n并于${year}年${mon}月${day}日前本息还完\n立此为据,特此感谢";
    /** 争议解决方式文案1*/
    public static final String DISPUTE_RESOLUTION_0 = "争议解决方式：\n\t甲乙双方履行过程中发生争议，双方选择第一种方式解决：\n\t1、提交南宁仲裁委员会仲裁；\n\t2、提请原告住所地人民法院进行审理";
    //public static final String DISPUTE_RESOLUTION_0 = "争议解决方式：\n\t甲乙双方履行过程中发生争议，双方选择第一种方式解决：\n\t1、提交珠海仲裁委员会仲裁；\n\t2、向杭州互联网法院申请诉讼";
    /** 争议解决方式文案2*/
    public static final String DISPUTE_RESOLUTION_1 = "争议解决方式：\n\t甲乙双方履行过程中发生争议，双方选择第二种方式解决：\n\t1、提交南宁仲裁委员会仲裁；\n\t2、提请原告住所地人民法院进行审理";
    //public static final String DISPUTE_RESOLUTION_1 = "争议解决方式：\n\t甲乙双方履行过程中发生争议，双方选择第二种方式解决：\n\t1、提交珠海仲裁委员会仲裁；\n\t2、向杭州互联网法院申请诉讼";
    
    /** 系统发送消息的姓名 */
    public static final String SYS_NAME = "无忧借条";	
    
    /** 系统发送消息的头像 */
    public static final String SYS_HEAD_IMAGE =  Global.getConfig("domain") + "/mb/assets1/images/sys_msg_icon.png";	
    
    /** 延期利率 */
    public static final String DELAY_RATE = "delayRate";	
    /** 延期利息 */
    public static final String DELAY_INTEREST = "delayInterest";	
    /** 延期完最后还款日期 */
    public static final String LAST_REPAY_DATE = "lastRepayDate";	
    /** 当前还款日期 */
    public static final String CURRENT_REPAY_DATE = "currentRepayDate";	
    /** 原本金 */
    public static final String OLD_AMOUNT = "oldAmount";	
    /** 原利息 */
    public static final String OLD_INTEREST = "oldInterest";	
    /** 新利息 */
    public static final String NEW_INTEREST = "newInterest";	
    /** 部分还款金额 */
    public static final String PARTIAL_PAYMENT = "partialPayment";	
    /** 部分还款后应还金额 */
    public static final String REMAIN_PAYMENT = "remainPayment";	
    
    /** 转让后债权人 */
    public static final String CREDITOR = "creditor";	
    /** 转让后债权人电话 */
    public static final String PHONE_NO = "phoneNo";	
    /** 转让后债权人身份证 */
    public static final String ID_NO = "idNo";	
    /** 转让时间 */
    public static final String TIME = "time";	

    /** 借条本金 */
    public static final String LOAN_AMOUNT = "借条本金";	
    /** 借条利息 */
    public static final String LOAN_INTEREST = "借条利息";
    /** 借条状态 */
    public static final String LOAN_STATUS = "借条状态";

    /** 还款时间 */
	public static final String LOAN_REPAY_DATE = "还款时间";	
	/** 应还本金 */
	public static final String SHOULD_GIVE_AMOUNT = "应还本金";	
	/** 应还利息 */
	public static final String SHOULD_GIVE_INTEREST = "应还利息";	
	/** 逾期利息 */
	public static final String OVER_DUE_INTEREST = "逾期利息";	
	/** 全额还款金额 */
	public static final String TOTAL_AMOUNT = "全额还款金额";	
	/** 部分还款金额*/
	public static final String PARTIAL_PAY_AMOUNT = "部分还款金额";	
	/** 延期利息*/
	public static final String TXT_DELAY_INTEREST = "延期利息";	
	/** 延期时长*/
	public static final String DELAY_DAYS = "延期时长";	
	/** 借条类型 apply*/
	public static final Integer TYPE_APPLY = 0;	
	/** 借条类型 detail*/
	public static final Integer TYPE_DETAIL = 1;	
	/** 借条类型 record*/
	public static final Integer TYPE_RECORD = 2;	
	/** Ios版本号*/
	public static final String IOS_VERSION = "4.09";
	/** Android版本号*/
	public static final String ANDROID_VERSION = "3.61";
    
}
