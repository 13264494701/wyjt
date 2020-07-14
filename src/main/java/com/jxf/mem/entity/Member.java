package com.jxf.mem.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.jxf.svc.sys.area.entity.Area;
import com.jxf.svc.sys.crud.entity.CrudEntity;
import com.jxf.web.app.interceptor.WyjtAppInterceptor;


/**
 * 会员管理Entity
 * @author HUO
 * @version 2016-04-25
 */
public class Member extends CrudEntity<Member> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 注册渠道
	 */
	public enum RegChannel {

		/** app*/
		app,

		/** 公信堂 */
		gxt
	}
	/**
	 * 性别
	 */
	public enum Gender {

		/** 男 */
		male,

		/** 女 */
		female
	}
	
	
	private String name;		// 会员姓名
	private String idNo;		// 身份证号
	private Integer verifiedList; //认证项{@link VerifiedUtils}

	private String nickname;		// 会员昵称
	private String headImage;       // 会员头像
	private Gender gender;		// 会员性别

	private String addr;		// 常用地址
	private Area area;		// 会员所属地区
	private String email;		// 邮箱地址
	private Boolean isEnabled;		// 是否可用
	private Boolean isLocked;		// 是否锁定
	private Boolean isAuth;		// 是否授权注册协议
	private String lockKey;		// 锁定KEY
	private Date lockedDate;		// 锁定日期
	private RegChannel regChannel; //注册渠道
	private String registerIp;		// 注册IP
	private Date loginDate;		// 最后登录日期
	private Integer loginFailureCount;		// 连续登录失败次数
	private String loginIp;		// 最后登录IP
	private String username;		// 用户名
	private String password;		// 登录密码
	private String payPassword;		// 支付密码
	
	private String safeKeyValue;		// 安全密钥
	private Date safeKeyExpire;		// 过期时间

	private Integer count;
	
	/** "身份信息"属性名称 */
	public static final String PRINCIPAL_ATTRIBUTE_NAME = WyjtAppInterceptor.class.getName() + ".PRINCIPAL";
	
	/** "用户名"Cookie名称 */
	public static final String USERNAME_COOKIE_NAME = "username";

	/** "昵称"Cookie名称 */
	public static final String NICKNAME_COOKIE_NAME = "nickname";
	
	/** "头像"Cookie名称 */
	public static final String HEADIMAGE_COOKIE_NAME = "headimage";
	
	/** 最大收藏商品数 */
	public static final Integer MAX_FAVORITE_COUNT = 10;
	
	/**会员等级*/
	private MemberRank memberRank;
	
	/**会员积分*/
	private MemberPoint  memberPoint;	
	
	/**会员账户*/
	private MemberAct  memberAct;	
	
	//Redis中业务常量 
	/** 账户余额 */
	public static final String CUR_BAL = "curBal";
	/** 可用余额 */
	public static final String AVL_BAL = "avlBal";
	/** 借款账户 */
	public static final String LOAN_BAL = "loanBal";
	/** 冻结账户 */
	public static final String FREEZEN_BAL = "freezenBal";
	/** 红包账户 */
	public static final String REDBAG_BAL = "redBagBal";
	/** 待收总额 */
	public static final String PENDING_RECEIVE = "pendingReceive";
	/** 待还总额 */
	public static final String PENDING_REPAYMENT = "pendingRepayment";
	/** 错误次数 */
	public static final String WRONG_TIMES = "wrongTimes";
	/** 是否锁定账户 */
	public static final String ISLOCKED = "isLocked";
	/** 锁定账户时间 */
	public static final String LOCKEDDATE = "lockedDate";
	//MEMBER_CACHE中业务常量end
	
	

	public Member() {
		super();
	}

	public Member(Long id){
		super(id);
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	
	@Length(min=0, max=64, message="会员昵称长度必须介于 0 和 64 之间")
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

	public MemberRank getMemberRank() {
		return memberRank;
	}

	public void setMemberRank(MemberRank memberRank) {
		this.memberRank = memberRank;
	}
	
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}	
	
	@Length(min=0, max=128, message="邮箱地址长度必须介于 0 和 128 之间")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}
	
	@Length(min=0, max=255, message="锁定KEY长度必须介于 0 和 255 之间")
	public String getLockKey() {
		return lockKey;
	}

	public void setLockKey(String lockKey) {
		this.lockKey = lockKey;
	}
	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	public Date getLockedDate() {
		return lockedDate;
	}

	public void setLockedDate(Date lockedDate) {
		this.lockedDate = lockedDate;
	}
	
	@Length(min=1, max=15, message="注册IP长度必须介于 1 和 15 之间")
	public String getRegisterIp() {
		return registerIp;
	}

	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}
	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	
	public Integer getLoginFailureCount() {
		return loginFailureCount;
	}

	public void setLoginFailureCount(Integer loginFailureCount) {
		this.loginFailureCount = loginFailureCount;
	}
	
	@Length(min=0, max=15, message="最后登录IP长度必须介于 0 和 15 之间")
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	
	@Length(min=1, max=64, message="用户名长度必须介于 1 和 64 之间")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Length(min=1, max=128, message="登录密码长度必须介于 1 和 128 之间")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public Date getSafeKeyExpire() {
		return safeKeyExpire;
	}

	public void setSafeKeyExpire(Date safeKeyExpire) {
		this.safeKeyExpire = safeKeyExpire;
	}
	
	public String getSafeKeyValue() {
		return safeKeyValue;
	}

	public void setSafeKeyValue(String safeKeyValue) {
		this.safeKeyValue = safeKeyValue;
	}

	public MemberPoint getMemberPoint() {
		return memberPoint;
	}

	public void setMemberPoint(MemberPoint memberPoint) {
		this.memberPoint = memberPoint;
	}
	public MemberAct getMemberAct() {
		return memberAct;
	}

	public void setMemberAct(MemberAct memberAct) {
		this.memberAct = memberAct;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getVerifiedList() {
		return verifiedList;
	}

	public void setVerifiedList(Integer verifiedList) {
		this.verifiedList = verifiedList;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public RegChannel getRegChannel() {
		return regChannel;
	}

	public void setRegChannel(RegChannel regChannel) {
		this.regChannel = regChannel;
	}

	public Boolean getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(Boolean isAuth) {
		this.isAuth = isAuth;
	}
}