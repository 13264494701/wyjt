package com.jxf.web.model.wyjt.app;

public class LoanButtonRequestParam {

	/**借条ID*/
	private String loanId;
	
	/**底部按钮code
	 * * 放款人	2001：放款人拒绝借款		2005：放款人拒绝延期		2008：放款人去审核视频	2010：放款人未收到线下还款
	 * 			2012：放款人已收到线下还款	2015：放款人提醒收款		2016：放款人提醒录制视频	2021：放款人同意借款
	 * 			2027：放款人催款		2033：放款人申请催收		2034：放款人申请仲裁
	 * 			2036：放款人拒绝部分还款	2038：放款人查看催收进度	2039：放款人查看仲裁进度	
	 * 			2041:延期/部分销账		2042：放款人取消部分还款	2043：放款人取消延期
	 * 			
	 * 借款人		2003：发起新借款		2004：借款人拒绝收款		2006：借款人取消借款		2007：借款人去录制视频
	 * 			2009：借款人确认收款		2014：借款人申请延期		2017：提醒借款/提醒同意延期/提醒同意部分还款/提醒确认线下还款
	 * 			2018：提醒审核视频		2025：借款人还款		2028：借款人取消延期		2031：借款人拒绝修改利息
	 * 			2032：借款人同意修改利息	2037：借款人取消部分还款	2040：借款人取消线下还款	2044：借款人拒绝部分还款
	 * 			2045：借款人拒绝延期		2046：借款人同意部分还款（支付，跳转到部分还款页面）	2047：借款人同意延期
	 */
	/**代码编号*/
	private int code;

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	
	
}
