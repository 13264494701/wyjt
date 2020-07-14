package com.jxf.loan.constant;

import java.util.ArrayList;
import java.util.List;

import com.jxf.web.model.wyjt.app.loan.LoanDetailForAppResponseResult;
import com.jxf.web.model.wyjt.app.loan.LoanDetailForAppResponseResult.TempList;

/**
 * 借据 对话 状态 旧借条的实体直接粘过来了 NewLoanMessage
 */
public class RecordMessage {
	
	/**底部按钮code
	 * * 放款人	2001：放款人拒绝借款		2005：放款人拒绝延期		2008：放款人去审核视频	2010：放款人未收到线下还款
	 * 			2012：放款人已收到线下还款	2015：放款人提醒收款		2016：放款人提醒录制视频	 
	 * 			2022：放款人同意延期		2027：放款人催款		             		 
	 * 			2035：放款人同意部分还款	2036：放款人拒绝部分还款	
	 * 			2042：放款人取消部分还款	2043：放款人取消延期
	 * 			
	 * 借款人		2003：发起新借款		2004：借款人拒绝收款		2006：借款人取消借款		2007：借款人去录制视频
	 * 			2009：借款人确认收款		2017：提醒借款/提醒同意延期/提醒同意部分还款/提醒确认线下还款
	 * 			2018：提醒审核视频		2025：借款人还款		2028：借款人取消延期		2031：借款人拒绝修改利息
	 * 			2032：借款人同意修改利息	2037：借款人取消部分还款	2040：借款人取消线下还款	2044：借款人拒绝部分还款
	 * 			2045：借款人拒绝延期		2047：借款人同意延期
	 */
	/** 操作异常 */
	public static final int RESULT_ERROR_WEBDATE = -100;
	/** 程序异常 */
	public static final int RESULT_ERROR_EXCEPTION = -400;
	
	
	/** 操作失败 */
	public static final String REAULT_ERROR_STRING = "操作失败";
	/** 用户失效，请重新登录 */
	public static final String REAULT_ERROR_USER ="用户失效，请重新登录";
	/** 您无此操作权限 */
	public static final String REAULT_ERROR_LOAN ="您无此操作权限";
	
	public static final String REAULT_ERROR_JK_SELF ="您有逾期15天的借条尚未还清，请还款后再进行借款！";
	
	public static final String REAULT_ERROR_FK_SELF ="您有逾期15天的借条尚未还清，请还款后再借款给好友！";
	
	public static final String REAULT_ERROR_JL_OTHER ="您的好友有逾期15天的借条尚未还清，需对方还款后您才可申请借款！";
	
	public static final String REAULT_ERROR_FK_OTHER ="您的好友有逾期15天的借条尚未还清，需对方还款后才可借款给对方！";
	
	/** 最大借款金额 */
    public static String maxLoanMoney = "1000000";
    /** 借条中心 每页显示数据条数 */
	public static int PAGE_SIZE = 20;
	/**
	 * 借款用途
	 * @param type
	 * @return
	 */
	public static String getPayPurpose(int type){
		switch (type) {
		case 1:
			return "临时周转";
		case 2:
			return "交房租";
		case 3:
			return "消费";
		case 4:
			return "还信用卡";
		case 5:
			return "报培训班";
		case 6:
			return "考驾照";
		case 7:
			return "其他";
		default:
			return "其他";
		}
	}
	/**
	 * 获取借款的单类型
	 * @param loanType
	 * @return  (0:普通借款;1:快速放款-常规借款;2:快速放款-闪电借出)
	 */
	public static String getLoanTypeStr(int loanType){
		switch (loanType) {
		case 0:
			return "普通借款";
		case 1:
			return "快速放款-常规借款";
		case 2:
			return "快速放款-闪电借出";
		default:
			return "未知类型";
		}
	}
	// 对话内容
	//文字对话内容
    /**手头稍紧，借点金子花花？*/
    public static final int TEXT1 =3001;
    /**地主家也没余粮啦！*/
    public static final int TEXT2 =3002;
    /**同是月光族，相借何太急！*/
    public static final int TEXT3 =3003;
    /**哈哈，朕不差钱儿，这就打给你！*/
    public static final int TEXT4 =3004;
    /**亲，钞票已上路，签收好评哦！*/
    public static final int TEXT5 =3005;
    /**放心，臣妾会尽快还上的！*/
    public static final int TEXT6 =3006;
    /**跟你交易实在是太愉快了！*/
    public static final int TEXT7 =3007;
	/** 朋友，借我点儿钱吧？*/
    public static final int TEXT8 =3008;
    /**  不好意思，我最近手头紧，没钱借你啊！ */
    public static final int TEXT9 =3009;
    /** 好的，我马上给你打款！ */
    public static final int TEXT10 =3010;
    /** 我已打款，注意查收！ */
    public static final int TEXT11 =3011;
    /** 放心，我会按时还上的！ */
    public static final int TEXT12 =3012;
    /** 快点儿还款吧，我最近手头也紧啦！ */
    public static final int TEXT13 =3013;
	
	public static String getMsg(int chatNum) {
		switch (chatNum) {
		case TEXT1:
            return "手头稍紧，借点金子花花？";
		case TEXT2:
            return "地主家也没余粮啦！";
		case TEXT3:
            return "同是月光族，相借何太急！";
		case TEXT4:
            return "哈哈，朕不差钱儿，这就打给你！";
		case TEXT5:
            return "亲，钞票已上路，签收好评哦！";
		case TEXT6:
            return "放心，臣妾会尽快还上的！";
		case TEXT7:
            return "跟你交易实在是太愉快了！";
		case TEXT8:
            return "朋友，借我点儿钱吧？";
		case TEXT9:
            return "不好意思，我最近手头紧，没钱借你啊！";
		case TEXT10:
            return "好的，我马上给你打款！";
		case TEXT11:
            return "我已打款，注意查收！";
		case TEXT12:
            return "放心，我会按时还上的！";
		case TEXT13:
            return "快点儿还款吧，我最近手头也紧啦！";
        default:
        	return "其他";
		}
    }
	
	public static List<TempList> tempList = new ArrayList<TempList>();
	
	static{
		tempList.add(new LoanDetailForAppResponseResult().new TempList("朋友，借我点儿钱吧？",TEXT8));
		tempList.add(new LoanDetailForAppResponseResult().new TempList("不好意思，我最近手头紧，没钱借你啊！",TEXT9));
		tempList.add(new LoanDetailForAppResponseResult().new TempList("好的，我马上给你打款！",TEXT10));
		tempList.add(new LoanDetailForAppResponseResult().new TempList("我已打款，注意查收！",TEXT11));
		tempList.add(new LoanDetailForAppResponseResult().new TempList("放心，我会按时还上的！",TEXT12));
		tempList.add(new LoanDetailForAppResponseResult().new TempList("快点儿还款吧，我最近手头也紧啦！",TEXT13));
	}
	
	
	
	/**********************    messageId start      *******************************/
	/** 文字语言对话   type：-100  手动输入:1;固定对话： */
	public static final int CHAT_1 = 1;
	
	/** 借款人申请延期   type:580 note:{"lastRepayDate":"2019-12-23","delayInterest":"20","currentRepayDate":"2019-01-09"}*/
	public static final int CHAT_3 = 3;
	
	/** 放款人修改利息   type:300 note:{"newInterest":"11.00","oldInterest":"0.00"}*/
	public static final int CHAT_4 = 4;
	
	/** 借款人申请部分还款   type:560 note: {"currentRepayDate":"2019-01-07","oldAmount":"99.93","lastRepayDate":"2019-01-07","delayInterest":"0.00","oldInterest":"0.00","partialPayment":"20.00"}*/
	public static final int CHAT_6 = 6;
	
	/** 系统收手续费   type:520 note:5.00*/
	public static final int CHAT_7 = 7;
	
	/** 放款人申请部分还款  type:570 note:{"lastRepayDate":"2018-12-22","delayInterest":"0","partialPayment":"1","currentRepayDate":"2019-01-09"}*/
	public static final int CHAT_8 = 8;
	
	/** 放款人申请延期还款   type：590 note:{"lastRepayDate":"2019-01-10","delayInterest":"0","currentRepayDate":"2019-01-09","oldInterest":"4.00",""}*/
	public static final int CHAT_9 = 9;
	
	/** 转让借条通知 type:500 note:{"creditor":"张三","phoneNo":"13999999999","idNo":"2100****2222","time":"2019-01-01"}*/
	public static final int CHAT_10 = 10;
	
	/** 借款人申请借款   type:100 note:"其他"*/
	public static final int CHAT_1101 = 1101;
	
	/** 借款人提醒借款   type:-200 note:"其他"*/
	public static final int CHAT_1102 = 1102;
	
	/** 借款人取消借款   type:840 note:"其他"*/
	public static final int CHAT_1103 = 1103;
	
	/** 借款人确认收款   type:500 note:"其他"*/
	public static final int CHAT_1104 = 1104;
	
	/** 借款人上传视频   type:440 note:"url地址"*/
	public static final int CHAT_1105 = 1105;
	
	/** 借款人拒绝收款   type:800 note:"其他"*/
	public static final int CHAT_1106 = 1106;
	
	/** 借款人提醒视频审核   type:-200 note:"其他"*/
	public static final int CHAT_1107 = 1107;
	
	/** 借款人拒绝修改利息   type:160 note:"其他"*/
	public static final int CHAT_1108 = 1108;
	
	/** 借款人同意修改利息   type:150 note:"其他"*/
	public static final int CHAT_1109 = 1109;
	
	/** 放款人同意借款   type:500 note:"其他"*/
	public static final int CHAT_1201 = 1201;
	
	/** 放款人发起借款   type:200 note:"其他"*/
	public static final int CHAT_1202 = 1202;
	
	/** 放款人提醒收款   type:-200 note:"其他"*/
	public static final int CHAT_1203 = 1203;
	
	/** 放款人拒绝借款   type:820 note:"其他"*/
	public static final int CHAT_1204 = 1204;
	
	/** 放款人提醒录制视频   type:-200 note:"其他"*/
	public static final int CHAT_1205 = 1205;
	
	/** 放款人视频审核通过   type:500 note:"其他"*/
	public static final int CHAT_1206 = 1206;
	
	/** 放款人视频审核未通过   type:420 note:"其他"*/
	public static final int CHAT_1208 = 1208;
	
	/** 借款人线下还款   type:540 note:"其他"*/
	public static final int CHAT_2101 = 2101;
	
	/** 借款人提醒确认申请延期   type:-200 note:"其他"*/
	public static final int CHAT_2102 = 2102;
	
	/** 借款人提醒确认线下还款   type:-200 note:"其他"*/
	public static final int CHAT_2104 = 2104;
	
	/** 借款人还款（分期：当期） type:500 note:"其他"*/
	public static final int CHAT_2105 = 2105;
	
	/** 借款人还款（分期：多期）   type:500 note:"其他"*/
	public static final int CHAT_2106 = 2106;
	
	/** 借款人还款（全额）   type:980 note:"其他"*/
	public static final int CHAT_2109 = 2109;
	
	/** 借款人提醒确认部分还款   type:-200 note:"其他"*/
	public static final int CHAT_2112 = 2112;
	
	/** 借款人取消线下还款   type:-200 note:"其他"*/
	public static final int CHAT_2115 = 2115;
	
	/** 放款人催款（最多3次）   type:-200 note:"其他"*/
	public static final int CHAT_2201 = 2201;
	
	/** 放款人拒绝延期  type:500 note:"其他"*/
	public static final int CHAT_2202 = 2202;
	
	/** 放款人同意延期   type:940 note:"其他"*/
	public static final int CHAT_2203 = 2203;
	
	/** 放款人未收到线下还款   type:500 note:"其他"*/
	public static final int CHAT_2205 = 2205;
	
	/** 放款人确认线下已还   type:900 note:"其他"*/
	public static final int CHAT_2206 = 2206;
	
	/** 借款人取消延期   type:500 note:"其他"*/
	public static final int CHAT_2208 = 2208;
	
	/** 放款人拒绝部分还款   type:500 note:"其他"*/
	public static final int CHAT_2214 = 2214;
	
	/** 放款人同意部分还款   type:920 note:"其他"*/
	public static final int CHAT_2215 = 2215;
	
	/** 取消部分还款   type:500 note:"其他"*/
	public static final int CHAT_2217 = 2217;
	
	/** 放款人发起仲裁   type:-200 note:仲裁费用金额*/
	public static final int CHAT_5005 = 5005;
	
	/** 放款人主动关闭借条 type:910 note:"其他"*/
	public static final int CHAT_2220 = 2220;
	
	/** 放款人提醒确认部分还款 type:-200 note:"其他"*/
	public static final int CHAT_2221 = 2221;
	
	/** 放款人提醒确认延期 type:-200 note:"其他"*/
	public static final int CHAT_2222 = 2222;
	
	/** 放款人取消部分还款 type:500 note:"其他"*/
	public static final int CHAT_2225 = 2225;
	
	/** 放款人取消延期 type:500 note:"其他"*/
	public static final int CHAT_2226 = 2226;
	
	/** 借款人拒绝部分还款 type:500 note:"其他"*/
	public static final int CHAT_2227 = 2227;
	
	/** 借款人拒绝延期 type:500 note:"其他"*/
	public static final int CHAT_2228 = 2228;
	
	/**********************    对话编码end     *******************************/
	
	//type start 原代码 NewLoanStatus
	/**
	 * -200:发送提醒消息
	 */
	public static final int SEND_REMIND = -200;
	/**
	 * -100:聊天
	 */
	public static final int SEND_MSG = -100;

	/**
	 * 100:借款人已发起借款申请，等待确认付款
	 */
	public static final int BORROWER_INITIATE_LOAN = 100;
	/**
	 * 150:同意修改利息，等待放款人付款
	 */
	public static final int BORROWER_CONFIRM_INTEREST = 150;
	/**
	 * 160:拒绝修改利息，等待放款人付款
	 */
	public static final int BORROWER_REFUSE_INTEREST = 160;
	/**
	 * 200:放款人已发起借款申请并支付，等待借款人确认
	 */
	public static final int LENDER_INITIATE_LOAN = 200;
	/**
	 * 300:放款人修改利息，等待借款人确认
	 */
	public static final int LENDER_MODIFY_INTEREST = 300;
	/**
	 * 400:放款人已付款，等待上传视频
	 */
	public static final int LENDER_PAID_WHAT_UPLOADVIDEO = 400;
	/**
	 * 420:视频审核未通过，等待重新上传
	 */
	public static final int LENDER_REVIEWVIDEO_NOTPASS = 420;
	/**
	 * 440:视频已上传，等待审核
	 */
	public static final int BORROWER_UPLOADED_VIDEO = 440;
	/**
	 * 500:还款中
	 */
	public static final int LENDER_PAID_REPAYMENT = 500;
	/**
	 * 510小程序取消部分还款
	 */
	public static final int WXLENDER_INITIATE_LOAN = 510;
	/**
	 * 515小程序取消延期还款
	 */
	public static final int WXLENDER_INITIATE = 515;
	/**
	 * 520:手续费
	 */
	public static final int FORMALITIES_FEE = 520;
	/**
	 * 530小程序拒绝部分还款
	 */
	public static final int WXREFUSING_PART_REPAYMENT = 530;
	                        
	/**
	 * 535小程序拒绝延期还款
	 */
	public static final int WXREFUSING_DELY_REPAYMENT= 535;

	/**
	 * 540:全额线下还款，待确认
	 */
	public static final int BORROWER_LINEDOWN_REPAYMENT = 540;
	/**
	 * 560:借款人申请部分还款，待确认
	 */
	public static final int BORROWER_PART_REPAYMENT = 560;
	/**
	 * 570:放款人申请部分还款，待确认
	 */
	public static final int LENDER_PART_REPAYMENT = 570;
	/**
	 * 580:借款人申请延期，待确认
	 */
	public static final int BORROWER_EXTENSION_REPAYMENT = 580;
	/**
	 * 590:放款人申请延期，待确认
	 */
	public static final int LENDER_EXTENSION_REPAYMENT = 590;
	/**
	 * 700:超时
	 */
	public static final int OPERATOR_CONFIRM_TIMEOUT = 700;
	/**
	 * 800:借款人拒绝借款
	 */
	public static final int BORROWER_REFUSE_LOAN = 800;
	/**
	 * 820:放款人拒绝借款
	 */
	public static final int LENDER_REFUSE_LOAN = 820;
	/**
	 * 840:放款人/借款人，取消借款/放款
	 */
	public static final int CANCEL_LOAN = 840;
	/**
	 * 900:确认线下还款
	 */
	public static final int LENDER_CONFIRM_LINEDOWN = 900;
	/**
	 * 910:放款人主动关闭借款单
	 */
	public static final int LENDER_CLOSE_LOAN = 910;
	/**
	 * 920:放款人/借款人 同意部分还款
	 */
	public static final int LENDER_AGREE_PART = 920;
	/**
	 * 940:放款人/借款人 同意延期
	 */
	public static final int LENDER_AGREE_EXTENSION = 940;
	/**
	 * 960:分期还款(记录日志使用)
	 */
	public static final int BORROWER_STAGING_REPAYMENT = 960;
	/**
	 * 980:已全额还款（线上）
	 */
	public static final int BORROWER_FULL_REPAYMENT = 980;

	//对话类型end
	
	public static String getLoanLogType(int type){
		switch (type) {
		case CHAT_1101:
			return "借款人申请借款";
		case CHAT_1102:
			return "借款人提醒借款";
		case CHAT_1103:
			return "借款人取消借款";
		case CHAT_1104:
			return "借款人确认收款";
		case CHAT_1105:
			return "借款人上传视频";
		case CHAT_1106:
			return "借款人拒绝收款 ";
		case CHAT_1107:
			return "借款人提醒视频审核 ";
		case CHAT_1108:
			return "借款人拒绝修改利息";
		case CHAT_1109:
			return "借款人同意修改利息";
		case CHAT_1201:
			return "放款人同意借款";
		case CHAT_1202:
			return "放款人发起借款 ";
		case CHAT_1203:
			return "放款人提醒收款";
		case CHAT_1204:
			return "放款人拒绝借款 ";
		case CHAT_1205:
			return "放款人提醒录制视频";
		case CHAT_1206:
			return "放款人视频审核通过";
		case CHAT_1208:
			return "放款人视频审核未通过";
		case CHAT_2101:
			return "借款人线下还款 ";
		case CHAT_2102:
			return "借款人提醒确认申请延期";
		case CHAT_2104:
			return "借款人提醒确认线下还款";
		case CHAT_2105:
			return "借款人还款（分期：当期）";
		case CHAT_2106:
			return "借款人还款（分期：多期）";
		case CHAT_2109:
			return "借款人还款（全额）";
		case CHAT_2112:
			return "借款人提醒确认部分还款";
		case CHAT_2115:
			return "取消线下还款";
		case CHAT_2201:
			return "放款人催款（最多3次）";
		case CHAT_2202:
			return "拒绝延期";
		case CHAT_2203:
			return "放款人同意延期";
		case CHAT_2205:
			return "放款人确认线下未还 ";
		case CHAT_2206:
			return "放款人确认线下已还";
		case CHAT_2208:
			return "取消延期";
		case CHAT_2214:
			return "拒绝部分还款";
		case CHAT_2215:
			return "放款人同意部分还款";
		case CHAT_2217:
			return "取消部分还款";
		case CHAT_5005:
			return "放款人发起仲裁";
		case CHAT_2220:
			return "放款人主动关闭借条";
		case CHAT_2221:
			return "放款人提醒确认部分还款";
		case CHAT_2222:
			return "放款人提醒确认延期";
		case CHAT_2225:
			return "放款人取消部分还款";
		case CHAT_2226:
			return "放款人取消延期";
		case CHAT_2227:
			return "借款人拒绝部分还款";
		case CHAT_2228:
			return "借款人拒绝延期";
		case CHAT_1:
			return "文字对话";
		case CHAT_3:
			return "借款人申请延期";
		case CHAT_4:
			return "放款人修改利息";
		case CHAT_6:
			return "借款人申请部分还款";
		case CHAT_7:
			return "系统收手续费";
		case CHAT_8:
			return "放款人申请部分还款";
		case CHAT_9:
			return "放款人申请延期还款";
		default:
			return "未知状态";
		}
		
	}
	/**
	 * 获取借款单状态
	 * 
	 * @param status
	 * @return
	 */
	public static String getTypeStr(int status) {
		switch (status) {
		case SEND_REMIND:
			return "消息提醒";
		case SEND_MSG:
			return "文字聊天";
		case BORROWER_INITIATE_LOAN:
			return "借款人已发起借款申请，等待确认付款";
		case BORROWER_CONFIRM_INTEREST:
			return "同意修改利息，等待放款人付款";
		case BORROWER_REFUSE_INTEREST:
			return "拒绝修改利息，等待放款人付款";
		case LENDER_INITIATE_LOAN:
			return "放款人已发起借款申请并支付，等待借款人确认";
		case LENDER_MODIFY_INTEREST:
			return "放款人修改利息，等待借款人确认";
		case LENDER_PAID_WHAT_UPLOADVIDEO:
			return "放款人已付款，等待上传视频";
		case LENDER_REVIEWVIDEO_NOTPASS:
			return "视频审核未通过，等待重新上传";
		case BORROWER_UPLOADED_VIDEO:
			return "视频已上传，等待审核";
		case LENDER_PAID_REPAYMENT:
			return "还款中";
		case FORMALITIES_FEE:
			return "手续费";
		case BORROWER_LINEDOWN_REPAYMENT:
			return "全额线下还款，待确认";
		case BORROWER_PART_REPAYMENT:
			return "借款人申请部分还款，待确认";
		case LENDER_PART_REPAYMENT:
			return "放款人申请部分还款，待确认";
		case BORROWER_EXTENSION_REPAYMENT:
			return "借款人申请延期，待确认";
		case LENDER_EXTENSION_REPAYMENT:
			return "放款人申请延期，待确认";
		case OPERATOR_CONFIRM_TIMEOUT:
			return "超时";
		case BORROWER_REFUSE_LOAN:
			return "借款人拒绝借款";
		case LENDER_REFUSE_LOAN:
			return "放款人拒绝借款";
		case CANCEL_LOAN:
			return "取消借款";
		case LENDER_CONFIRM_LINEDOWN:
			return "确认线下还款";
		case LENDER_CLOSE_LOAN:
			return "放款人主动关闭借款单";
		case LENDER_AGREE_PART:
			return "同意部分还款，生成新的借条";
		case LENDER_AGREE_EXTENSION:
			return "同意延期，生成新的借条";
		case BORROWER_STAGING_REPAYMENT:
			return "分期还款";
		case BORROWER_FULL_REPAYMENT:
			return "已全额线上还款";
		default:
			return "未知状态";
		}
	}
	
}
