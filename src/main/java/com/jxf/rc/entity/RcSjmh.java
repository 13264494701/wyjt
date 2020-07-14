package com.jxf.rc.entity;

import com.jxf.ufang.entity.UfangUser;
import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 风控 数据魔盒Entity
 *
 * @author Administrator
 * @version 2018-10-16
 */
public class RcSjmh extends CrudEntity<RcSjmh> {

	
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
    	jingdong
    	   
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

    private static final long serialVersionUID = 1L;
    
    /**
     * 产品类型
     */
    private ProdType prodType;
    /**
     * 回传ID
     */
    private Long orgId;
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 手机号码
     */
    private String phoneNo;
    /**
     * 身份证号
     */
    private String idNo;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 渠道类型
     */
    private ChannelType channelType;
    /**
     * 渠道编码
     */
    private String channelCode;
    /**
     * 渠道数据源
     */
    private String channelSrc;
    /**
     * 渠道属性
     */
    private String channelAttr;

    /**
     * 任务消息
     */
    private String taskMsg;
    /**
     * 任务数据
     */
    private String taskData;
    
    
    private String dataPath;
    
    /**
     * 报告数据
     */
    private String reportData;
    
    
    private String reportPath;
    
    /**
     * 数据状态
     */
    private DataStatus dataStatus;
    
    /**
     * 数据状态
     */
    private ReportStatus reportStatus;


    private UfangUser user;


    public RcSjmh() {
        super();
    }

    public RcSjmh(Long id) {
        super(id);
    }
    
    public UfangUser getUser() {
        return user;
    }

    public void setUser(UfangUser user) {
        this.user = user;
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


    @Length(min = 0, max = 50, message = "任务id长度必须介于 0 和 50 之间")
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

    @Length(min = 0, max = 18, message = "身份证号长度必须介于 0 和 18 之间")
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


    @Length(min = 0, max = 255, message = "任务消息长度必须介于 0 和 255 之间")
    public String getTaskMsg() {
        return taskMsg;
    }

    public void setTaskMsg(String taskMsg) {
        this.taskMsg = taskMsg;
    }

    public String getTaskData() {
        return taskData;
    }

    public void setTaskData(String taskData) {
        this.taskData = taskData;
    }

	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	public String getReportData() {
		return reportData;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
	}

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

}