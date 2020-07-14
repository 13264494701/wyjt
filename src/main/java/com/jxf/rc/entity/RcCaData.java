package com.jxf.rc.entity;



import java.util.Date;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 信用报告Entity
 * 
 * @author lmy
 * @version 2018-12-17
 */
public class RcCaData extends CrudEntity<RcCaData> {

	public enum Type {

		/**
		 * 紧急联系人 0
		 */
		emergency_people,
		/**
		 * 淘宝 1
		 */
		taobao,
		/**
		 * 运营商 2
		 */
		yunyingshang,

		/**
		 * 芝麻分 3
		 */
		zhimafen,
		/**
		 * 学信网 4
		 */

		xuexinwang,
		/**
		 * 社保 5
		 */
		shebao,
		/**
		 * 公积金 6
		 */
		gongjijin,
		/**
		 * 多头借贷 7
		 */
		duotoujiedai,
		/**
		 * 网银 8
		 */
		wangyin,
		/**
		 * 运营商报告（2.0） 9
		 */
		yunyingshang_report

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
	/** 报告编号 */
	private String reportNo;
	/** 内容 */
	private String content;
	
	/** 开始时间 */
	private Date beginTime;	
	/** 结束时间*/
	private Date endTime;
	

	public RcCaData() {
		super();
	}

	public RcCaData(Long id) {
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