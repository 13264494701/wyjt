package com.jxf.web.model.gxt;


public class MemberInfoGxtResponseResult   {

	/** id */
	private String id;
	/** 会员姓名 */
	private String name;
	
	/** 会员头像 */
	private String headImage;
	
	/** 账户余额(可用+借款) */
	private String curBal;
	/** 待收金额 */
	private String pendingReceive;
	/** 待还金额 */
	private String pendingRepayment;
	
	/**实名认证=身份证+视频*/
	private Integer realIdentityStatus;//0->未认证,1->认证
	/**银行卡认证*/
	private Integer bankCardStatus;//0->未认证,1->认证
	/**支付密码设置*/
	private Integer payPwStatus;//0->未设置,1->设置
    /**邮箱是否设置*/
    private Integer emailStatus;//0->未设置,1->设置
    /** 身份证号 */
    private String idNo;
    /** 邮箱 */
    private String email;
	/** 手机号(脱敏) */
	private String username;
	/** 手机号(非脱敏) */
	private String realUsername;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHeadImage() {
		return headImage;
	}
	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}
	public String getCurBal() {
		return curBal;
	}
	public void setCurBal(String curBal) {
		this.curBal = curBal;
	}
	public String getPendingReceive() {
		return pendingReceive;
	}
	public void setPendingReceive(String pendingReceive) {
		this.pendingReceive = pendingReceive;
	}
	public String getPendingRepayment() {
		return pendingRepayment;
	}
	public void setPendingRepayment(String pendingRepayment) {
		this.pendingRepayment = pendingRepayment;
	}
	public Integer getRealIdentityStatus() {
		return realIdentityStatus;
	}
	public void setRealIdentityStatus(Integer realIdentityStatus) {
		this.realIdentityStatus = realIdentityStatus;
	}
	public Integer getBankCardStatus() {
		return bankCardStatus;
	}
	public void setBankCardStatus(Integer bankCardStatus) {
		this.bankCardStatus = bankCardStatus;
	}
	public Integer getPayPwStatus() {
		return payPwStatus;
	}
	public void setPayPwStatus(Integer payPwStatus) {
		this.payPwStatus = payPwStatus;
	}
	public Integer getEmailStatus() {
		return emailStatus;
	}
	public void setEmailStatus(Integer emailStatus) {
		this.emailStatus = emailStatus;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRealUsername() {
		return realUsername;
	}
	public void setRealUsername(String realUsername) {
		this.realUsername = realUsername;
	}

}
