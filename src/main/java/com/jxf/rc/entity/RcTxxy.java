package com.jxf.rc.entity;


import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 风控 天下信用Entity
 *
 * @author wo
 * @version 2019-6-16
 */
public class RcTxxy extends CrudEntity<RcTxxy> {

	  
	
	public enum ReportStatus {

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
     * 报告编号
     */
    private String reportNo;
    /**
     * 手机号码
     */
    private String phoneNo;
    /**
     * 身份证号
     */
    private String idNo;
    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 渠道编码
     */
    private String channelCode;
 
    
    /**
     * 报告数据
     */
    private String reportData;
    
    
    private String reportPath;
    
    /**
     * 报告状态
     */
    private ReportStatus reportStatus;




    public RcTxxy() {
        super();
    }

    public RcTxxy(Long id) {
        super(id);
    }
    

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
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



    @Length(min = 0, max = 50, message = "真实姓名长度必须介于 0 和 50 之间")
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }


    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
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


	public ReportStatus getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(ReportStatus reportStatus) {
		this.reportStatus = reportStatus;
	}



}