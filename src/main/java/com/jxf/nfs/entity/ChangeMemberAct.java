package com.jxf.nfs.entity;

import java.math.BigDecimal;

import com.jxf.mem.entity.Member;

/**
 * @作者: xiaorongdian
 * @创建时间 :2018年11月10日 下午3:47:45
 * @功能说明:账户变更 传的参数
 */
public class ChangeMemberAct {

	/**
	 * trxCode : 
	 *  0001	个人会员充值
		0002	个人会员提现
		0003	个人会员转账
		0004         借款人取消部分还款
		0005	放款人放款交易达成(这不是主动放款)
		0006	借款人还款
		0007	借款申请部分还款
		0008	放款人确认同意部分还款
		0009	放款人主动放款
		0010	借款人确认同意放款人的主动放款
		0011          放款人要求录视频
		0012          放款人确认视频
		0013 	借款人拒绝主动放款 
		0014         放款人拒绝部分还款
		0015         放款人点对方已还款/收到线下还款 
		0016    1元查看信用档案
		1001	放款人申请催收预缴款
		1002	平台催收失败退款
		1003	放款人申请仲裁预缴款
		1004	仲裁未受理退款
		1005	仲裁胜诉放款人申请强执预缴款
		2001	个人会员查看信用档案
		2002	个人会员实名认证
		4001	客服后台加款
		4002	客服后台减款
		注:1.分不清借放款人时 loaner为用户 loanee为null
		   2.优放公司放款时 相当于loaner
		   3.优放公司可以为null  没有利息传new BigDecimal(0)
		   4.转账时转出者是loaner 转入者是loanee
	 */
	
	/**
	 * trxCode 交易序号 见 ActSubConstant
	 */
	private String trxCode; 
	/**
	 * loaner 放款人
	 */
	private Member loaner; 
	/**
	 * loanee 借款人
	 */
	private Member loanee;
	/**
	 * amount 金额(本金)
	 */
	private BigDecimal amount; 
	/**
	 * interest 利息
	 */
	private BigDecimal interest = BigDecimal.ZERO; 
	/**
	 * businessId 相关实体ID
	 */
	private Long businessId;
	/**
	 * me 当前用户
	 */
	private Member me;
	
	public String getTrxCode() {
		return trxCode;
	}
	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}
	public Member getLoaner() {
		return loaner;
	}
	public void setLoaner(Member loaner) {
		this.loaner = loaner;
	}
	public Member getLoanee() {
		return loanee;
	}
	public void setLoanee(Member loanee) {
		this.loanee = loanee;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public Long getBusinessId() {
		return businessId;
	}
	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
	public Member getMe() {
		return me;
	}
	public void setMe(Member me) {
		this.me = me;
	}
}
