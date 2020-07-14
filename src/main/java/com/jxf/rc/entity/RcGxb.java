package com.jxf.rc.entity;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 风控 公信宝Entity
 *
 * @author Administrator
 * @version 2018-10-16
 */
public class RcGxb extends CrudEntity<RcGxb> {
	
	public enum ProdType {
			
			/** 无忧借条APP */
			app,
			/** 优放 */
			ufang,
			/** 优放贷 */
			ufdebt
		}

    public enum GxbAuthType {

        /**
         * 芝麻分
         */
        sesame_multiple,
        /**
         * 微信
         */
        wechat_phone,
        
        /** 借条*/
        debit,
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
    	 * 支付宝
    	 */
    	alipay,

    }
    public enum SubItem {
    	
    	/**
    	 * 借贷宝
    	 */
    	jdb,
    	/**
    	 * 米房源
    	 */
    	hhd,
    	
    	/** 
    	 * 今借到
    	 **/
    	jjd,
    	
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
     * 授权类型
     */
    private GxbAuthType authType;
    /**
     * 授权子类型
     */
    private SubItem subItem;
    /**
     * 授权唯一标识
     */
    private String token;
    /**
     * 姓名
     */
    private String name;
    /**
     * 身份证号码
     */
    private String idNo;
    /**
     * 手机号码
     */
    private String phoneNo;
    /**
     * 授权结果
     */
    private String authResult;
    
    private String userEmpNo;

    private String sesameScore;
    private String status;
    private String area;
    private String remark;
    private String avatar;
    private String nickname;
    private String queryTime;
    private String sex;
    private String signature;
    /** 任务数据*/
    private String taskData;
    /** 任务数据路径*/
    private String dataPath;
    /**报告数据*/
    private String reportData;
    /** 报告数据路径*/
    private String reportPath;
    /** 数据状态*/
    private DataStatus dataStatus;
    /** 数据状态*/
    private ReportStatus reportStatus;
    /** 渠道编码*/
    private String channelCode;
    /** 渠道数据源*/
    private String channelSrc;
    /** 渠道属性*/
    private String channelAttr;
    /** 认证手机号*/
    private String authPhoneNo;
    

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getChannelSrc() {
		return channelSrc;
	}

	public void setChannelSrc(String channelSrc) {
		this.channelSrc = channelSrc;
	}

	public String getChannelAttr() {
		return channelAttr;
	}

	public void setChannelAttr(String channelAttr) {
		this.channelAttr = channelAttr;
	}

	public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(String queryTime) {
        this.queryTime = queryTime;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSesameScore() {
        return sesameScore;
    }

    public void setSesameScore(String sesameScore) {
        this.sesameScore = sesameScore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RcGxb() {
        super();
    }

    public RcGxb(Long id) {
        super(id);
    }

    public GxbAuthType getAuthType() {
        return authType;
    }

    public void setAuthType(GxbAuthType authType) {
        this.authType = authType;
    }

    @Length(min = 0, max = 50, message = "授权唯一标识长度必须介于 0 和 50 之间")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Length(min = 0, max = 50, message = "姓名长度必须介于 0 和 50 之间")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 0, max = 18, message = "身份证号码长度必须介于 0 和 18 之间")
    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    @Length(min = 0, max = 11, message = "手机号码长度必须介于 0 和 11 之间")
    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAuthResult() {
        return authResult;
    }

    public void setAuthResult(String authResult) {
        this.authResult = authResult;
    }

	public String getUserEmpNo() {
		return userEmpNo;
	}

	public void setUserEmpNo(String userEmpNo) {
		this.userEmpNo = userEmpNo;
	}

	public void setSubItem(SubItem subItem) {
		this.subItem = subItem;
	}

	public SubItem getSubItem() {
		return subItem;
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

	public String getAuthPhoneNo() {
		return authPhoneNo;
	}

	public void setAuthPhoneNo(String authPhoneNo) {
		this.authPhoneNo = authPhoneNo;
	}


}