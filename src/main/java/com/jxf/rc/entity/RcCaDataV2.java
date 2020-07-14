package com.jxf.rc.entity;



import java.util.Date;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 信用报告Entity
 * 
 * @author wo
 * @version 2019-08-12
 */
public class RcCaDataV2 extends CrudEntity<RcCaDataV2> {

	public enum Type {

		/**
		 * 芝麻分 0
		 */
		zmf,
		/**
		 * 运营商 1
		 */
		yys,
		/**
		 * 淘宝 2
		 */
		taobao,
		/**
		 * 学信网 3
		 */
		xxw,
		/**
		 * 社保 4
		 */
		shebao,
		/**
		 * 公积金 5
		 */
		gjj,
		/**
		 * 网银 6
		 */
		wangyin,


	}
	public enum Provider{
		/**
		 * 数据魔盒
		 */
		sjmh,
		/**
		 * 公信宝
		 */
		gxb,
		/**
		 * 天机
		 */
		tj
	}
	public enum Status{
		/**
		 * 认证中
		 */
		processing,
		/**
		 * 已认证
		 */
		success,
		/**
		 * 认证失败
		 */
		failure
	}
	
	
	private static final long serialVersionUID = 1L;
	/** 手机号码 */
	private String phoneNo;
	/** 身份证号码 */
	private String idNo;
	/** 姓名 */
	private String name;
	/** 数据类型 */
	private Type type;
	/** 数据来源 */
	private Provider provider;
	/** 数据来源 */
	private Status status;
	/** 报告编号 */
	private String reportNo;
	/** 内容 */
	private String content;

	/** 开始时间 */
	private Date beginTime;	
	/** 结束时间*/
	private Date endTime;
	
	public RcCaDataV2() {
		super();
	}

	public RcCaDataV2(Long id) {
		super(id);
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
}