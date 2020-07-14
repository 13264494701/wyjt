package com.jxf.mem.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 会员扩展信息Entity
 * @author XIAORONGDIAN
 * @version 2018-10-13
 */
public class MemberExtend extends CrudEntity<MemberExtend> {
	
	private static final long serialVersionUID = 1L;
	/** 会员ID */
	private Member member;		
	/** 邮箱 */
	private String email;		
	/** 二维码 */
	private String qrcode;		
	/** 性别 */
	private String sex;		
	/** 血型 */
	private String bloodType;		
	/** 民族 */
	private String nation;		
	/** 出生日期 */
	private Date birthdate;		
	/** 居住地区 */
	private Long houseArea;		
	/** 居住地址 */
	private String resiAddress;		
	/** 户口地址 */
	private String accAddress;		
	/** 毕业院校 */
	private String graduSchool;		
	/** 最高学历 */
	private String highestDegree;		
	/** 就业状况 */
	private String employSituation;		
	/** 公司名称 */
	private String companyName;		
	/** 公司电话 */
	private String companyPhone;		
	/** 公司地址 */
	private String companyLocation;		
	/** 婚姻状况 */
	private String maritalStatus;		
	/** 居住状况 */
	private String liveConditions;		
	/** 购车情况 */
	private String cardBuy;		
	/** 宗教信仰 */
	private String religiousBelief;		
	/** 新浪微博uid */
	private String wbUid;		
	/** 新浪微博AccessToken */
	private String wbAccessToken;		
	/** 微信号 */
	private String wxUid;		
	/** 腾讯uid */
	private String qqUid;		
	/** 腾讯AccessToken */
	private Date qqAccessToken;		
	/** 紧急联系人 */
	private String ecp1;		
	/** 与紧急联系人关系 */
	private String ecpR1;		
	/** 紧急联系人电话 */
	private String ecpPhoneNo1;		
	/** 紧急联系人2 */
	private String ecp2;		
	/** 与紧急联系人2关系 */
	private String ecpR2;		
	/** 紧急联系人2电话 */
	private String ecpPhoneNo2;		
	/** 紧急联系人3 */
	private String ecp3;		
	/** 与紧急联系人3关系 */
	private String ecpR3;		
	/** 紧急联系人3电话 */
	private String ecpPhoneNo3;		
	
	public MemberExtend() {
		super();
	}

	public MemberExtend(Long id){
		super(id);
	}

	@Length(min=0, max=64, message="邮箱长度必须介于 0 和 64 之间")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Length(min=0, max=255, message="二维码长度必须介于 0 和 255 之间")
	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
	
	@Length(min=0, max=1, message="性别长度必须介于 0 和 1 之间")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@Length(min=0, max=1, message="血型长度必须介于 0 和 1 之间")
	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}
	
	@Length(min=0, max=3, message="民族长度必须介于 0 和 3 之间")
	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	
	public Long getHouseArea() {
		return houseArea;
	}

	public void setHouseArea(Long houseArea) {
		this.houseArea = houseArea;
	}
	
	@Length(min=0, max=32, message="居住地址长度必须介于 0 和 32 之间")
	public String getResiAddress() {
		return resiAddress;
	}

	public void setResiAddress(String resiAddress) {
		this.resiAddress = resiAddress;
	}
	
	@Length(min=0, max=32, message="户口地址长度必须介于 0 和 32 之间")
	public String getAccAddress() {
		return accAddress;
	}

	public void setAccAddress(String accAddress) {
		this.accAddress = accAddress;
	}
	
	@Length(min=0, max=32, message="毕业院校长度必须介于 0 和 32 之间")
	public String getGraduSchool() {
		return graduSchool;
	}

	public void setGraduSchool(String graduSchool) {
		this.graduSchool = graduSchool;
	}
	
	@Length(min=0, max=32, message="最高学历长度必须介于 0 和 32 之间")
	public String getHighestDegree() {
		return highestDegree;
	}

	public void setHighestDegree(String highestDegree) {
		this.highestDegree = highestDegree;
	}
	
	@Length(min=0, max=1, message="就业状况长度必须介于 0 和 1 之间")
	public String getEmploySituation() {
		return employSituation;
	}

	public void setEmploySituation(String employSituation) {
		this.employSituation = employSituation;
	}
	
	@Length(min=0, max=32, message="公司名称长度必须介于 0 和 32 之间")
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	@Length(min=0, max=11, message="公司电话长度必须介于 0 和 11 之间")
	public String getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}
	
	@Length(min=0, max=32, message="公司地址长度必须介于 0 和 32 之间")
	public String getCompanyLocation() {
		return companyLocation;
	}

	public void setCompanyLocation(String companyLocation) {
		this.companyLocation = companyLocation;
	}
	
	@Length(min=0, max=1, message="婚姻状况长度必须介于 0 和 1 之间")
	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	
	@Length(min=0, max=1, message="居住状况长度必须介于 0 和 1 之间")
	public String getLiveConditions() {
		return liveConditions;
	}

	public void setLiveConditions(String liveConditions) {
		this.liveConditions = liveConditions;
	}
	
	@Length(min=0, max=1, message="购车情况长度必须介于 0 和 1 之间")
	public String getCardBuy() {
		return cardBuy;
	}

	public void setCardBuy(String cardBuy) {
		this.cardBuy = cardBuy;
	}
	
	@Length(min=0, max=32, message="宗教信仰长度必须介于 0 和 32 之间")
	public String getReligiousBelief() {
		return religiousBelief;
	}

	public void setReligiousBelief(String religiousBelief) {
		this.religiousBelief = religiousBelief;
	}
	
	@Length(min=0, max=16, message="新浪微博uid长度必须介于 0 和 16 之间")
	public String getWbUid() {
		return wbUid;
	}

	public void setWbUid(String wbUid) {
		this.wbUid = wbUid;
	}
	
	@Length(min=0, max=16, message="新浪微博AccessToken长度必须介于 0 和 16 之间")
	public String getWbAccessToken() {
		return wbAccessToken;
	}

	public void setWbAccessToken(String wbAccessToken) {
		this.wbAccessToken = wbAccessToken;
	}
	
	@Length(min=0, max=64, message="微信号长度必须介于 0 和 64 之间")
	public String getWxUid() {
		return wxUid;
	}

	public void setWxUid(String wxUid) {
		this.wxUid = wxUid;
	}
	
	@Length(min=0, max=16, message="腾讯uid长度必须介于 0 和 16 之间")
	public String getQqUid() {
		return qqUid;
	}

	public void setQqUid(String qqUid) {
		this.qqUid = qqUid;
	}
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getQqAccessToken() {
		return qqAccessToken;
	}

	public void setQqAccessToken(Date qqAccessToken) {
		this.qqAccessToken = qqAccessToken;
	}
	
	@Length(min=0, max=16, message="紧急联系人长度必须介于 0 和 16 之间")
	public String getEcp1() {
		return ecp1;
	}

	public void setEcp1(String ecp1) {
		this.ecp1 = ecp1;
	}
	
	@Length(min=0, max=4, message="与紧急联系人关系长度必须介于 0 和 4 之间")
	public String getEcpR1() {
		return ecpR1;
	}

	public void setEcpR1(String ecpR1) {
		this.ecpR1 = ecpR1;
	}
	
	@Length(min=0, max=11, message="紧急联系人电话长度必须介于 0 和 11 之间")
	public String getEcpPhoneNo1() {
		return ecpPhoneNo1;
	}

	public void setEcpPhoneNo1(String ecpPhoneNo1) {
		this.ecpPhoneNo1 = ecpPhoneNo1;
	}
	
	@Length(min=0, max=16, message="紧急联系人2长度必须介于 0 和 16 之间")
	public String getEcp2() {
		return ecp2;
	}

	public void setEcp2(String ecp2) {
		this.ecp2 = ecp2;
	}
	
	@Length(min=0, max=4, message="与紧急联系人2关系长度必须介于 0 和 4 之间")
	public String getEcpR2() {
		return ecpR2;
	}

	public void setEcpR2(String ecpR2) {
		this.ecpR2 = ecpR2;
	}
	
	@Length(min=0, max=11, message="紧急联系人2电话长度必须介于 0 和 11 之间")
	public String getEcpPhoneNo2() {
		return ecpPhoneNo2;
	}

	public void setEcpPhoneNo2(String ecpPhoneNo2) {
		this.ecpPhoneNo2 = ecpPhoneNo2;
	}
	
	@Length(min=0, max=16, message="紧急联系人3长度必须介于 0 和 16 之间")
	public String getEcp3() {
		return ecp3;
	}

	public void setEcp3(String ecp3) {
		this.ecp3 = ecp3;
	}
	
	@Length(min=0, max=4, message="与紧急联系人3关系长度必须介于 0 和 4 之间")
	public String getEcpR3() {
		return ecpR3;
	}

	public void setEcpR3(String ecpR3) {
		this.ecpR3 = ecpR3;
	}
	
	@Length(min=0, max=11, message="紧急联系人3电话长度必须介于 0 和 11 之间")
	public String getEcpPhoneNo3() {
		return ecpPhoneNo3;
	}

	public void setEcpPhoneNo3(String ecpPhoneNo3) {
		this.ecpPhoneNo3 = ecpPhoneNo3;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
}