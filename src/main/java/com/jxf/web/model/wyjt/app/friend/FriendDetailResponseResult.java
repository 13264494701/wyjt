package com.jxf.web.model.wyjt.app.friend;

import com.jxf.web.model.wyjt.app.member.CaAuthResponseResult;

/**
 * @作者: gaobo
 * @创建时间 :2018年11月5日 上午10:56:07
 * @功能说明:查询好友详细信息
 */
public class FriendDetailResponseResult {

	/** ID */
	private String friendId;
	
	/** 头像 */
	private String headImage;
	
	/** 真实姓名 */
	private String name;
	
	/** 实名认证=身份证+视频*/
	private Integer realIdentityStatus;//0->未认证,1->认证
	
	/** 信用评级 */
	private String rankName;
	
	/** 是否为好友  0不是 1是*/
	private Integer isFriend;
	
	/**
	 * 免费信用报告查看权限   0 未同意 1 同意
	 */
	private Integer isFreeCa;
	
	/** 信用报告认证*/
	private CaAuthResponseResult caAuthResponseResult;
	
	/** 信用评级 */
	private String quotaAssessment;


	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getRealIdentityStatus() {
		return realIdentityStatus;
	}

	public void setRealIdentityStatus(Integer realIdentityStatus) {
		this.realIdentityStatus = realIdentityStatus;
	}

	public Integer getIsFriend() {
		return isFriend;
	}

	public void setIsFriend(Integer isFriend) {
		this.isFriend = isFriend;
	}

	public CaAuthResponseResult getCaAuthResponseResult() {
		return caAuthResponseResult;
	}

	public void setCaAuthResponseResult(CaAuthResponseResult caAuthResponseResult) {
		this.caAuthResponseResult = caAuthResponseResult;
	}

	public Integer getIsFreeCa() {
		return isFreeCa;
	}

	public void setIsFreeCa(Integer isFreeCa) {
		this.isFreeCa = isFreeCa;
	}

	public String getQuotaAssessment() {
		return quotaAssessment;
	}

	public void setQuotaAssessment(String quotaAssessment) {
		this.quotaAssessment = quotaAssessment;
	}

}