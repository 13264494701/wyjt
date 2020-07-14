package com.jxf.rc.entity;


import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;
import com.jxf.svc.utils.enumUtils.BaseCodeEnum;

/**
 * 闪谍报告（中智诚）Entity
 * @author XIAORONGDIAN
 * @version 2019-03-21
 */
public class RcShandie extends CrudEntity<RcShandie> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 状态
	 */
	public enum Type implements BaseCodeEnum{
		
		paydayLoan("现金贷",1),
		
		microfinance("小额贷款",2),
		
		p2p("P2P",3),
		
		carLoan("车辆抵押贷款",4),
		
		houseLoan("房贷",5),
		
		ecommercePlatform("电商平台",6),
		
		undergraduateStaging("大学生分期",7),
		
		bluecollarWorkersInInstallment("蓝领分期",8),
		
		educationStage("教育分期",9),
		
		medicalBeautyInInstallment("医美分期",10),
		
		rentInstallment("租房分期",11),
		
		digitalStage("数码分期",12),
		
		carHire("汽车分期",13),
		
		decorateInInstallment("装修分期",14),
		
		tourismInInstallment("旅游分期",15),
		
		agricultureInInstallment("农业分期",16),
		
		other("其他",17),
		
		creditCard("信用卡",18),
		
		creditCardPayment("信用卡代偿",19),
		
		supplyChainFinance("供应链金融",20),
		
		financeLease("融资租赁",21),
		
		receiptForLoan("借条",22);
		
		private int code;
		private String name;
		
		Type(String name,int code) {
			this.code = code; 
			this.name = name; 
		}
		 
		@Override
		public int getCode() { return this.code; }
		@Override
		public String getName() { return this.name; }
	}
	
	/** 购买者 */
	private Long ufangUserId;		
			/** 姓名 */
	private String name;		
			/** 身份证 */
	private String idNo;		
			/** 手机号 */
	private String phoneNo;		
			/** 贷款类型 */
	private Type type;		
			/** 状态 */
	private String status;		
			/** 内容 */
	private String content;		
	
	public RcShandie() {
		super();
	}

	public RcShandie(Long id){
		super(id);
	}

	@Length(min=1, max=255, message="姓名长度必须介于 1 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=1, max=32, message="身份证长度必须介于 1 和 32 之间")
	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	
	@Length(min=1, max=32, message="手机号长度必须介于 1 和 32 之间")
	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	@Length(min=1, max=4, message="状态长度必须介于 1 和 4 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Long getUfangUserId() {
		return ufangUserId;
	}

	public void setUfangUserId(Long ufangUserId) {
		this.ufangUserId = ufangUserId;
	}

}