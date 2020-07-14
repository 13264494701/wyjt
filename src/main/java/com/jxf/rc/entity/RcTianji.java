package com.jxf.rc.entity;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;
import com.jxf.ufang.entity.UfangUser;

/**
 * 天机Entity
 * @author suhuimin
 * @version 2019-07-31
 */
public class RcTianji extends CrudEntity<RcTianji> {

	private static final long serialVersionUID = 1L;

	public enum ProdType {

		/** 无忧借条APP */
		app,
		/** 优放 */
		ufang,
		/** 优放贷 */
		ufdebt
	}

	public enum ChannelType {
		/**
		 * 运营商
		 */
		yunyingshang,
		/**
		 * 淘宝
		 */
		taobao,
		/**
		 * 银行卡账单
		 */
		wangyin,
		/**
		 * 社保
		 */
		shebao,
		/**
		 * 公积金
		 */
		gongjijin,
		/**
		 * 学信网
		 */
		xuexinwang,
		/**
		 * 京东
		 */
		jingdong,
		/**
		 *支付宝
		 */
		alipay,
		/**
		 * 微粒贷
		 */
		weilidai,
		/**
		 *芝麻分
		 */
		zmf,

	}

	public enum DataStatus {

		/**
		 * 任务创建
		 */
		task_created,
		/**
		 * 数据生成
		 */
		data_created,

		/**
		 * 数据整理
		 */
		data_arranged

	}

	public enum ReportStatus {

		/**
		 * 任务创建
		 */
		task_created,
		/**
		 * 报告生成
		 */
		report_created,
		/**
		 * 报告整理
		 */
		report_arranged

	}

	/** 优放用户 */
	private String ufangEmpNo;
	/** 产品类型 */
	private ProdType prodType;
	/** 回传ID */
	private Long orgId;
	/** 任务id */
	private String taskId;
	/** 手机号码 */
	private String phoneNo;
	/** 身份证号 */
	private String idNo;
	/** 用户名 */
	private String userName;
	/** 真实姓名 */
	private String realName;
	/** 渠道类型 */
	private ChannelType channelType;
	/** 渠道编码 */
	private String channelCode;
	/** 渠道数据源 */
	private String channelSrc;
	/** 渠道属性 */
	private String channelAttr;
	/** 数据路径 */
	private String dataPath;
	/** 报告数据 */
	private String reportData;
	/** 报告数据路径 */
	private String reportPath;
	/** 数据状态 */
	private DataStatus dataStatus;
	/** 报告数据状态 */
	private ReportStatus reportStatus;
	/** 任务数据 */
    private String taskData;
    /** 天机数据报告网页 */
    private String htmlPath;
    /** 天机数据报告网页源码 临时数据用，不持久化*/
    private String htmlStr;
    
    private UfangUser user;

	public RcTianji() {
		super();
	}

	public RcTianji(Long id) {
		super(id);
	}

	public String getUfangEmpNo() {
		return ufangEmpNo;
	}

	public void setUfangEmpNo(String ufangEmpNo) {
		this.ufangEmpNo = ufangEmpNo;
	}

	public ProdType getProdType() {
		return prodType;
	}

	public void setProdType(ProdType prodType) {
		this.prodType = prodType;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Length(min = 0, max = 11, message = "手机号码长度必须介于 0 和 11 之间")
	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	@Length(min = 0, max = 20, message = "身份证号长度必须介于 0 和 20 之间")
	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	@Length(min = 0, max = 50, message = "用户名长度必须介于 0 和 50 之间")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Length(min = 0, max = 50, message = "真实姓名长度必须介于 0 和 50 之间")
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public ChannelType getChannelType() {
		return channelType;
	}

	public void setChannelType(ChannelType channelType) {
		this.channelType = channelType;
	}

	@Length(min = 0, max = 10, message = "渠道编码长度必须介于 0 和 10 之间")
	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	@Length(min = 0, max = 50, message = "渠道数据源长度必须介于 0 和 50 之间")
	public String getChannelSrc() {
		return channelSrc;
	}

	public void setChannelSrc(String channelSrc) {
		this.channelSrc = channelSrc;
	}

	@Length(min = 0, max = 50, message = "渠道属性长度必须介于 0 和 50 之间")
	public String getChannelAttr() {
		return channelAttr;
	}

	public void setChannelAttr(String channelAttr) {
		this.channelAttr = channelAttr;
	}

	@Length(min = 0, max = 255, message = "数据路径长度必须介于 0 和 255 之间")
	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	@Length(min = 0, max = 255, message = "报告数据路径长度必须介于 0 和 255 之间")
	public String getReportPath() {
		return reportPath;
	}

	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}

	public DataStatus getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(DataStatus dataStatus) {
		this.dataStatus = dataStatus;
	}

	public ReportStatus getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(ReportStatus reportStatus) {
		this.reportStatus = reportStatus;
	}

	public String getTaskData() {
		return taskData;
	}

	public void setTaskData(String taskData) {
		this.taskData = taskData;
	}

	public UfangUser getUser() {
		return user;
	}

	public void setUser(UfangUser user) {
		this.user = user;
	}

	public String getReportData() {
		return reportData;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
	}

	public String getHtmlPath() {
		return htmlPath;
	}

	public void setHtmlPath(String htmlPath) {
		this.htmlPath = htmlPath;
	}

	public String getHtmlStr() {
		return htmlStr;
	}

	public void setHtmlStr(String htmlStr) {
		this.htmlStr = htmlStr;
	}


}