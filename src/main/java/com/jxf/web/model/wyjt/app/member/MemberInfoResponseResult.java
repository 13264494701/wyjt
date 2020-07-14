package com.jxf.web.model.wyjt.app.member;


public class MemberInfoResponseResult   {

	/** 会员鉴权唯一标识 */
	private String memberToken;
	
	/** 会员ID */
	private String memberId;	
	/** 会员姓名 */
	private String name;
	/** 昵称 */
	private String nickname;
	
	/** 会员头像 */
	private String headImage;
	
	/**手机号码*/
	private String phoneNo;
	
	/**身份证号*/
	private String idNo;
	
	/** 邮箱 */
	private String email;
	/** 常用地址 */
	private String addr;

	
	/** 可用余额(用户充值的钱) */
	private String avlBal;

	/** 账户余额(可用+借款) */
	private String curBal;
	/** 借款账户 */
	private String loanBal;
	/** 待收金额 */
	private String pendingReceive;
	/** 待还金额 */
	private String pendingRepayment;
    /** 冻结金额*/
    private String frozenAmount;
    /** 红包收益*/
    private String redBag;//预留

	
	/**实名认证=身份证+视频*/
	private Integer realIdentityStatus;//0->未认证,1->认证
	/**银行卡认证*/
	private Integer bankCardStatus;//0->未认证,1->认证
	/**银行流水*/
	private Integer bankTrxStatus;
	/**运营商授权*/
	private Integer yunyingshangStatus;
	/**淘宝授权*/
	private Integer taobaoStatus;
	/**芝麻分*/
	private Integer zhimafenStatus;	
	/**学信网*/
	private Integer xuexingwangStatus;
	/**社保*/
	private Integer shebaoStatus;	
	/**公积金*/
	private Integer gongjijingStatus;
	/**支付密码设置*/
	private Integer payPwStatus;//0->未设置,1->设置
    /**邮箱是否设置*/
    private Integer emailStatus;//0->未设置,1->设置
    /**是否绑定了紧急联系人*/
    private Integer emergencyStatus;//0->未设置,1->设置
    
	/**好友人数超过标准值*/
	private Integer tooManyFriends;//0->未超限,1->超过
	
    /**显示余额标志*/
    private Integer showCurBal;//0->不显示,1->显示
    
	
	/**业务处理结果代码*/
	private Integer resultCode = 0;//1->弹出提示框 (您的账号在其他设备登录，如非本人操作，请及时修改密码或与客服联系。)
	/**业务处理结果描述*/
	private String resultMessage;
	
	/** 上次登录设备 */
	private String lastLoginDevice;
	
	/** 上次登录IP */
	private String lastLoginIp;
	
	/** 上次登录时间 */
	private String lastLoginTime;
	
	/** 新消息条数 */
	private Integer newMsgCount = 0;

	/** 信用评级 */
	private String rankName;

	/** 手机号(非脱敏) */
	private String realUsername;
	
	/** 是否平台用户 0不是1是*/
	private String isMember;
	
	/** 是否授权注册协议 0不是1是*/
	private String isAuth;
	/** 额度评估 */
	private String quotaAssessment;
	
	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	public String getMemberToken() {
		return memberToken;
	}
	public void setMemberToken(String memberToken) {
		this.memberToken = memberToken;
	}
	
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getHeadImage() {
		return headImage;
	}
	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
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
	public String getFrozenAmount() {
		return frozenAmount;
	}
	public void setFrozenAmount(String frozenAmount) {
		this.frozenAmount = frozenAmount;
	}
	public String getRedBag() {
		return redBag;
	}
	public void setRedBag(String redBag) {
		this.redBag = redBag;
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
	public Integer getBankTrxStatus() {
		return bankTrxStatus;
	}
	public void setBankTrxStatus(Integer bankTrxStatus) {
		this.bankTrxStatus = bankTrxStatus;
	}
	public Integer getYunyingshangStatus() {
		return yunyingshangStatus;
	}
	public void setYunyingshangStatus(Integer yunyingshangStatus) {
		this.yunyingshangStatus = yunyingshangStatus;
	}
	public Integer getTaobaoStatus() {
		return taobaoStatus;
	}
	public void setTaobaoStatus(Integer taobaoStatus) {
		this.taobaoStatus = taobaoStatus;
	}
	public Integer getZhimafenStatus() {
		return zhimafenStatus;
	}
	public void setZhimafenStatus(Integer zhimafenStatus) {
		this.zhimafenStatus = zhimafenStatus;
	}
	public Integer getXuexingwangStatus() {
		return xuexingwangStatus;
	}
	public void setXuexingwangStatus(Integer xuexingwangStatus) {
		this.xuexingwangStatus = xuexingwangStatus;
	}
	public Integer getShebaoStatus() {
		return shebaoStatus;
	}
	public void setShebaoStatus(Integer shebaoStatus) {
		this.shebaoStatus = shebaoStatus;
	}
	public Integer getGongjijingStatus() {
		return gongjijingStatus;
	}
	public void setGongjijingStatus(Integer gongjijingStatus) {
		this.gongjijingStatus = gongjijingStatus;
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
	public Integer getEmergencyStatus() {
		return emergencyStatus;
	}
	public void setEmergencyStatus(Integer emergencyStatus) {
		this.emergencyStatus = emergencyStatus;
	}
	public Integer getTooManyFriends() {
		return tooManyFriends;
	}
	public void setTooManyFriends(Integer tooManyFriends) {
		this.tooManyFriends = tooManyFriends;
	}
	public Integer getShowCurBal() {
		return showCurBal;
	}
	public void setShowCurBal(Integer showCurBal) {
		this.showCurBal = showCurBal;
	}
	public Integer getResultCode() {
		return resultCode;
	}
	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	
	public String getLastLoginDevice() {
		return lastLoginDevice;
	}
	public void setLastLoginDevice(String lastLoginDevice) {
		this.lastLoginDevice = lastLoginDevice;
	}
	public String getLastLoginIp() {
		return lastLoginIp;
	}
	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}
	public String getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
		

	public String getAvlBal() {
		return avlBal;
	}
	public void setAvlBal(String avlBal) {
		this.avlBal = avlBal;
	}
	public String getLoanBal() {
		return loanBal;
	}
	public void setLoanBal(String loanBal) {
		this.loanBal = loanBal;
	}

	public Integer getNewMsgCount() {
		return newMsgCount;
	}

	public void setNewMsgCount(Integer newMsgCount) {
		this.newMsgCount = newMsgCount;
	}

	public String getRealUsername() {
		return realUsername;
	}

	public void setRealUsername(String realUsername) {
		this.realUsername = realUsername;
	}

	public String getIsMember() {
		return isMember;
	}

	public void setIsMember(String isMember) {
		this.isMember = isMember;
	}

	public String getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(String isAuth) {
		this.isAuth = isAuth;
	}

	public String getQuotaAssessment() {
		return quotaAssessment;
	}

	public void setQuotaAssessment(String quotaAssessment) {
		this.quotaAssessment = quotaAssessment;
	}


}
