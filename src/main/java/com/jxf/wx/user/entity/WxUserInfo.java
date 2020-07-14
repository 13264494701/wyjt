package com.jxf.wx.user.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import com.jxf.mem.entity.Member;
import com.jxf.svc.sys.crud.entity.CrudEntity;
import com.jxf.web.wx.interceptor.WxUserInterceptor;
import com.jxf.wx.account.entity.WxAccount;

/**
 * 微信用户信息Entity
 * @author gaobo
 * @version 2018-10-16
 */
public class WxUserInfo extends CrudEntity<WxUserInfo> {
	
	private static final long serialVersionUID = 1L;
	
	/** "身份信息"属性名称 */
	public static final String PRINCIPAL_ATTRIBUTE_NAME = WxUserInterceptor.class.getName() + ".PRINCIPAL";
	
	/** 微信小程序账号 */
	private WxAccount account;		
	/** 用户标识 */
	private String openid;		
	/** UnionID */
	private String unionid;		
	/** 用户昵称 */
	private String nickname;		
	/** 用户头像 */
	private String headImage;		
	/** 是否会员 */
	private boolean isMember;
	/** 推荐人 */
	private WxUserInfo referrer;
	/** 关联会员 */
	private Member member;		
	/** 性别 */
	private String sex;		
	/** 城市 */
	private String city;		
	/** 国家 */
	private String country;		
	/** 省份 */
	private String province;		
	/** 语言 */
	private String language;		
	/** 是否关注 */
	private String isSubscribe;		
	/** 关注时间 */
	private Date subscribeTime;		
	/** 取消关注时间 */
	private Date unsubscribeTime;		
	
	public WxUserInfo() {
		super();
	}

	public WxUserInfo(Long id){
		super(id);
	}

	public WxAccount getAccount() {
		return account;
	}

	public void setAccount(WxAccount account) {
		this.account = account;
	}
	
	
	@Length(min=1, max=64, message="用户标识长度必须介于 1 和 64 之间")
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	@Length(min=1, max=64, message="UnionID长度必须介于 1 和 64 之间")
	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	
	@Length(min=1, max=64, message="用户昵称长度必须介于 1 和 64 之间")
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	@Length(min=1, max=255, message="用户头像长度必须介于 1 和 255 之间")
	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}
	
	@Length(min=1, max=1, message="是否会员长度必须介于 1 和 1 之间")
	public boolean getIsMember() {
		return isMember;
	}

	public void setIsMember(boolean isMember) {
		this.isMember = isMember;
	}
	
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	@Length(min=0, max=1, message="性别长度必须介于 0 和 1 之间")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@Length(min=0, max=50, message="城市长度必须介于 0 和 50 之间")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@Length(min=0, max=50, message="国家长度必须介于 0 和 50 之间")
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	@Length(min=0, max=50, message="省份长度必须介于 0 和 50 之间")
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	
	@Length(min=0, max=10, message="语言长度必须介于 0 和 10 之间")
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	@Length(min=0, max=1, message="是否关注长度必须介于 0 和 1 之间")
	public String getIsSubscribe() {
		return isSubscribe;
	}

	public void setIsSubscribe(String isSubscribe) {
		this.isSubscribe = isSubscribe;
	}
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getSubscribeTime() {
		return subscribeTime;
	}

	public void setSubscribeTime(Date subscribeTime) {
		this.subscribeTime = subscribeTime;
	}
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getUnsubscribeTime() {
		return unsubscribeTime;
	}

	public void setUnsubscribeTime(Date unsubscribeTime) {
		this.unsubscribeTime = unsubscribeTime;
	}

	public WxUserInfo getReferrer() {
		return referrer;
	}

	public void setReferrer(WxUserInfo referrer) {
		this.referrer = referrer;
	}




	
}