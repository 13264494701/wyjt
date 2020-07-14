package com.jxf.report.entity;

import com.alibaba.fastjson.annotation.JSONField;
import javax.validation.constraints.NotNull;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 流量统计Entity
 * @author wo
 * @version 2019-02-28
 */
public class ReportLoaneeDataDaily extends CrudEntity<ReportLoaneeDataDaily> {
	
	private static final long serialVersionUID = 1L;
	/** 日期 */
	private String date;	
	/** 无忧借条APP */
	private Long wyjtApp;		
	/** 无忧借条微信 */
	private Long wyjtWeixin;	
	
	/** 报表类型 */
	private Integer type;
	
	public ReportLoaneeDataDaily() {
		super();
	}

	public ReportLoaneeDataDaily(Long id){
		super(id);
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="日期不能为空")
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	@NotNull(message="无忧借条APP不能为空")
	public Long getWyjtApp() {
		return wyjtApp;
	}

	public void setWyjtApp(Long wyjtApp) {
		this.wyjtApp = wyjtApp;
	}
	
	public Long getWyjtWeixin() {
		return wyjtWeixin;
	}

	public void setWyjtWeixin(Long wyjtWeixin) {
		this.wyjtWeixin = wyjtWeixin;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
}