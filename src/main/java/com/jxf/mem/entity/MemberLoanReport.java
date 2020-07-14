package com.jxf.mem.entity;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.jxf.ufang.entity.UfangExtendMember;



/**
 * 会员借贷分析/信用档案
 * @author zhj
 * @version 2019-03-26
 */
public class MemberLoanReport{

	/**好友id*/
	private Long id;
	/**类型:一周/一月/半年/总计*/
	private Integer type;
	/**真实姓名*/
	private String name;
	/**手机号*/
	private String username;
	/**信用记录*/
	private String crStatus;
	/**淘宝认证*/
	private String taobaoStatus;
	/**运营商认证*/
	private String yunyingshangStatus;
	/**芝麻分*/
	private String zhimafenStatus;
	/**学信网*/
	private String xuexingwangStatus;
	/**社保*/
	private String shebaoStatus;
	/**公积金*/
	private String gongjijingStatus;
	/**待还金额*/
	private BigDecimal pendingReceiveAmt;	 
	/**待还借条数量*/
	private Integer pendingReceiveQuantity;	
	/**待收金额*/
	private BigDecimal pendingRepaymentAmt;	
	/**待收借条数量*/
	private Integer pendingRepaymentQuantity;	
	/**逾期未还金额 */
	private BigDecimal overduePendingRepayAmt;	
	/**逾期未还借条数量 */
	private Integer overduePendingRepayQuantity;	
	/**逾期已还金额*/
	private BigDecimal overdueRepayedAmt;	
	/**逾期已还借条数量*/
	private Integer overdueRepayedQuantity;	
	/**借入金额*/
	private BigDecimal loanInAmt;	
	/**借入借条数量*/
	private Integer loanInQuantity;	
	/**借出金额 */
	private BigDecimal loanOutAmt;	
	/**借出借条数量 */
	private Integer loanOutQuantity;	
	/**按时还款金额 */
	private BigDecimal onTimeRepayedAmt;
	/**按时还款借条数量 */
	private Integer onTimeRepayedQuantity;	 
	/**延期还款金额*/
	private BigDecimal delayRepayedAmt;	
	/**延期还款借条数量*/
	private Integer delayRepayedQuantity;	

	/**花呗信用额度*/
	private String huabeiAmount;	
	/**余额宝额度*/
	private String yueAmount;	
	/**默认收货地址*/
	private String taoboAddr;	
	/**半年内订单数量*/
	private Integer orderCount;	
	/**半年内订单总额*/
	private String orderAmount;	
	/**半年内月均消费*/
	private String oneMonthMoney;	
	/**半年内价格最高产品*/
	private String maxMoney;	
	/**半年内价格最低产品*/
	private String minMoney;	
	
	/**运营商手机号*/
	private String yunyingshangUsername;	
	/**运营商*/
	private String yunyingshang;	
	/**使用时长*/
	private Long userTime;	
	/**日均通话时长*/
	private Integer oneDayCallTime;
	/**夜间通话次数*/
	private String nightCallCount;	
	/**通话次数*/
	private String callCount;
	/**连续无通话静默大于3天（次）*/
	private String silenceCount;
	/**通话记录*/
	private String callRecord;
	/**基础套餐金额*/
	private String taocanMoney;
	/**月均消费金额*/
	private String oneMonthUseMoney;
	/**话费记录*/
	private String phoneBillRecord;
	/**紧急联系人信息*/
	private List<UfangExtendMember> extendMemberList = new ArrayList<>();
	
	/**学信网 毕业院校*/
	private String colleges;
	/**学信网 专业*/
	private String major;
	/**学信网 入学时间*/
	private String entranceTime;
	/**学信网 学历*/
	private String education;
	/**学信网 毕业时间*/
	private String graduationTime;
	
	/**社保 单位名称*/
	private String shebaoCompanyName;
	/**社保 参加工作时间*/
	private String shebaoWorkTime;
	/**社保 家庭地址*/
	private String shebaoFamilyAddr;
	/**社保 起缴日*/
	private String shebaoPaymentTime;
	/**社保详情*/
	private String shebao;
	
	/**公积金 单位名称*/
	private String gongjijinCompanyName;
	/**公积金 开户日期*/
	private String gongjijinOpenAccountTime;
	/**公积金 缴费状态*/
	private String gongjijinPaymentStatus;
	/**公积金 最后缴费日期*/
	private String gongjijinLastPaymentTime;
	/**公积金 家庭地址*/
	private String gongjijinAddr;
	/**公积金详情*/
	private String gongjijin;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCrStatus() {
		return crStatus;
	}
	public void setCrStatus(String crStatus) {
		this.crStatus = crStatus;
	}
	public String getTaobaoStatus() {
		return taobaoStatus;
	}
	public void setTaobaoStatus(String taobaoStatus) {
		this.taobaoStatus = taobaoStatus;
	}
	public String getYunyingshangStatus() {
		return yunyingshangStatus;
	}
	public void setYunyingshangStatus(String yunyingshangStatus) {
		this.yunyingshangStatus = yunyingshangStatus;
	}
	public String getZhimafenStatus() {
		return zhimafenStatus;
	}
	public void setZhimafenStatus(String zhimafenStatus) {
		this.zhimafenStatus = zhimafenStatus;
	}
	public String getXuexingwangStatus() {
		return xuexingwangStatus;
	}
	public void setXuexingwangStatus(String xuexingwangStatus) {
		this.xuexingwangStatus = xuexingwangStatus;
	}
	public String getShebaoStatus() {
		return shebaoStatus;
	}
	public void setShebaoStatus(String shebaoStatus) {
		this.shebaoStatus = shebaoStatus;
	}
	public String getGongjijingStatus() {
		return gongjijingStatus;
	}
	public void setGongjijingStatus(String gongjijingStatus) {
		this.gongjijingStatus = gongjijingStatus;
	}
	public BigDecimal getPendingReceiveAmt() {
		return pendingReceiveAmt;
	}
	public void setPendingReceiveAmt(BigDecimal pendingReceiveAmt) {
		this.pendingReceiveAmt = pendingReceiveAmt;
	}
	public Integer getPendingReceiveQuantity() {
		return pendingReceiveQuantity;
	}
	public void setPendingReceiveQuantity(Integer pendingReceiveQuantity) {
		this.pendingReceiveQuantity = pendingReceiveQuantity;
	}
	public BigDecimal getPendingRepaymentAmt() {
		return pendingRepaymentAmt;
	}
	public void setPendingRepaymentAmt(BigDecimal pendingRepaymentAmt) {
		this.pendingRepaymentAmt = pendingRepaymentAmt;
	}
	public Integer getPendingRepaymentQuantity() {
		return pendingRepaymentQuantity;
	}
	public void setPendingRepaymentQuantity(Integer pendingRepaymentQuantity) {
		this.pendingRepaymentQuantity = pendingRepaymentQuantity;
	}
	public BigDecimal getOverduePendingRepayAmt() {
		return overduePendingRepayAmt;
	}
	public void setOverduePendingRepayAmt(BigDecimal overduePendingRepayAmt) {
		this.overduePendingRepayAmt = overduePendingRepayAmt;
	}
	public Integer getOverduePendingRepayQuantity() {
		return overduePendingRepayQuantity;
	}
	public void setOverduePendingRepayQuantity(Integer overduePendingRepayQuantity) {
		this.overduePendingRepayQuantity = overduePendingRepayQuantity;
	}
	public BigDecimal getOverdueRepayedAmt() {
		return overdueRepayedAmt;
	}
	public void setOverdueRepayedAmt(BigDecimal overdueRepayedAmt) {
		this.overdueRepayedAmt = overdueRepayedAmt;
	}
	public Integer getOverdueRepayedQuantity() {
		return overdueRepayedQuantity;
	}
	public void setOverdueRepayedQuantity(Integer overdueRepayedQuantity) {
		this.overdueRepayedQuantity = overdueRepayedQuantity;
	}
	public BigDecimal getLoanInAmt() {
		return loanInAmt;
	}
	public void setLoanInAmt(BigDecimal loanInAmt) {
		this.loanInAmt = loanInAmt;
	}
	public Integer getLoanInQuantity() {
		return loanInQuantity;
	}
	public void setLoanInQuantity(Integer loanInQuantity) {
		this.loanInQuantity = loanInQuantity;
	}
	public BigDecimal getLoanOutAmt() {
		return loanOutAmt;
	}
	public void setLoanOutAmt(BigDecimal loanOutAmt) {
		this.loanOutAmt = loanOutAmt;
	}
	public Integer getLoanOutQuantity() {
		return loanOutQuantity;
	}
	public void setLoanOutQuantity(Integer loanOutQuantity) {
		this.loanOutQuantity = loanOutQuantity;
	}
	public BigDecimal getOnTimeRepayedAmt() {
		return onTimeRepayedAmt;
	}
	public void setOnTimeRepayedAmt(BigDecimal onTimeRepayedAmt) {
		this.onTimeRepayedAmt = onTimeRepayedAmt;
	}
	public Integer getOnTimeRepayedQuantity() {
		return onTimeRepayedQuantity;
	}
	public void setOnTimeRepayedQuantity(Integer onTimeRepayedQuantity) {
		this.onTimeRepayedQuantity = onTimeRepayedQuantity;
	}
	public BigDecimal getDelayRepayedAmt() {
		return delayRepayedAmt;
	}
	public void setDelayRepayedAmt(BigDecimal delayRepayedAmt) {
		this.delayRepayedAmt = delayRepayedAmt;
	}
	public Integer getDelayRepayedQuantity() {
		return delayRepayedQuantity;
	}
	public void setDelayRepayedQuantity(Integer delayRepayedQuantity) {
		this.delayRepayedQuantity = delayRepayedQuantity;
	}
	public String getHuabeiAmount() {
		return huabeiAmount;
	}
	public void setHuabeiAmount(String huabeiAmount) {
		this.huabeiAmount = huabeiAmount;
	}
	public String getYueAmount() {
		return yueAmount;
	}
	public void setYueAmount(String yueAmount) {
		this.yueAmount = yueAmount;
	}
	public String getTaoboAddr() {
		return taoboAddr;
	}
	public void setTaoboAddr(String taoboAddr) {
		this.taoboAddr = taoboAddr;
	}
	public Integer getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getOneMonthMoney() {
		return oneMonthMoney;
	}
	public void setOneMonthMoney(String oneMonthMoney) {
		this.oneMonthMoney = oneMonthMoney;
	}
	public String getMaxMoney() {
		return maxMoney;
	}
	public void setMaxMoney(String maxMoney) {
		this.maxMoney = maxMoney;
	}
	public String getMinMoney() {
		return minMoney;
	}
	public void setMinMoney(String minMoney) {
		this.minMoney = minMoney;
	}
	public String getYunyingshangUsername() {
		return yunyingshangUsername;
	}
	public void setYunyingshangUsername(String yunyingshangUsername) {
		this.yunyingshangUsername = yunyingshangUsername;
	}
	public String getYunyingshang() {
		return yunyingshang;
	}
	public void setYunyingshang(String yunyingshang) {
		this.yunyingshang = yunyingshang;
	}
	public String getNightCallCount() {
		return nightCallCount;
	}
	public void setNightCallCount(String nightCallCount) {
		this.nightCallCount = nightCallCount;
	}
	public String getCallRecord() {
		return callRecord;
	}
	public void setCallRecord(String callRecord) {
		this.callRecord = callRecord;
	}
	public String getTaocanMoney() {
		return taocanMoney;
	}
	public void setTaocanMoney(String taocanMoney) {
		this.taocanMoney = taocanMoney;
	}
	public String getOneMonthUseMoney() {
		return oneMonthUseMoney;
	}
	public void setOneMonthUseMoney(String oneMonthUseMoney) {
		this.oneMonthUseMoney = oneMonthUseMoney;
	}
	public String getPhoneBillRecord() {
		return phoneBillRecord;
	}
	public void setPhoneBillRecord(String phoneBillRecord) {
		this.phoneBillRecord = phoneBillRecord;
	}
	public List<UfangExtendMember> getExtendMemberList() {
		return extendMemberList;
	}
	public void setExtendMemberList(List<UfangExtendMember> extendMemberList) {
		this.extendMemberList = extendMemberList;
	}
	public String getColleges() {
		return colleges;
	}
	public void setColleges(String colleges) {
		this.colleges = colleges;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getEntranceTime() {
		return entranceTime;
	}
	public void setEntranceTime(String entranceTime) {
		this.entranceTime = entranceTime;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getGraduationTime() {
		return graduationTime;
	}
	public void setGraduationTime(String graduationTime) {
		this.graduationTime = graduationTime;
	}
	public String getShebaoCompanyName() {
		return shebaoCompanyName;
	}
	public void setShebaoCompanyName(String shebaoCompanyName) {
		this.shebaoCompanyName = shebaoCompanyName;
	}
	public String getShebaoWorkTime() {
		return shebaoWorkTime;
	}
	public void setShebaoWorkTime(String shebaoWorkTime) {
		this.shebaoWorkTime = shebaoWorkTime;
	}
	public String getShebaoFamilyAddr() {
		return shebaoFamilyAddr;
	}
	public void setShebaoFamilyAddr(String shebaoFamilyAddr) {
		this.shebaoFamilyAddr = shebaoFamilyAddr;
	}
	public String getShebaoPaymentTime() {
		return shebaoPaymentTime;
	}
	public void setShebaoPaymentTime(String shebaoPaymentTime) {
		this.shebaoPaymentTime = shebaoPaymentTime;
	}
	public String getShebao() {
		return shebao;
	}
	public void setShebao(String shebao) {
		this.shebao = shebao;
	}
	public String getGongjijinCompanyName() {
		return gongjijinCompanyName;
	}
	public void setGongjijinCompanyName(String gongjijinCompanyName) {
		this.gongjijinCompanyName = gongjijinCompanyName;
	}
	public String getGongjijinOpenAccountTime() {
		return gongjijinOpenAccountTime;
	}
	public void setGongjijinOpenAccountTime(String gongjijinOpenAccountTime) {
		this.gongjijinOpenAccountTime = gongjijinOpenAccountTime;
	}
	public String getGongjijinPaymentStatus() {
		return gongjijinPaymentStatus;
	}
	public void setGongjijinPaymentStatus(String gongjijinPaymentStatus) {
		this.gongjijinPaymentStatus = gongjijinPaymentStatus;
	}
	public String getGongjijinLastPaymentTime() {
		return gongjijinLastPaymentTime;
	}
	public void setGongjijinLastPaymentTime(String gongjijinLastPaymentTime) {
		this.gongjijinLastPaymentTime = gongjijinLastPaymentTime;
	}
	public String getGongjijinAddr() {
		return gongjijinAddr;
	}
	public void setGongjijinAddr(String gongjijinAddr) {
		this.gongjijinAddr = gongjijinAddr;
	}
	public String getGongjijin() {
		return gongjijin;
	}
	public void setGongjijin(String gongjijin) {
		this.gongjijin = gongjijin;
	}
	public Long getUserTime() {
		return userTime;
	}
	public void setUserTime(Long userTime) {
		this.userTime = userTime;
	}
	public String getCallCount() {
		return callCount;
	}
	public void setCallCount(String callCount) {
		this.callCount = callCount;
	}
	public String getSilenceCount() {
		return silenceCount;
	}
	public void setSilenceCount(String silenceCount) {
		this.silenceCount = silenceCount;
	}
	public Integer getOneDayCallTime() {
		return oneDayCallTime;
	}
	public void setOneDayCallTime(Integer oneDayCallTime) {
		this.oneDayCallTime = oneDayCallTime;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	



	
}