package com.jxf.web.model.wyjt.app.friend;


/**
 * @作者: wo
 * @创建时间 :2018年11月18日 下午3:50:28
 * @功能说明:查询好友失信报告
 */
public class FriendOverdueReportResponseResult{

	/** 好友ID */
	private String friendId;
	/** 姓名 */
	private String name;
	/** 年龄 */
	private Integer age;
	/** 电话号码 */
	private String phoneNo;
	/** 身份证号码 */
	private String idNo;
	
	/** 逾期笔数 */
	private Integer quantity;

	/** 逾期金额 */
	private String amount;
	
	public String getFriendId() {
		return friendId;
	}
	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
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
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}

	


}
