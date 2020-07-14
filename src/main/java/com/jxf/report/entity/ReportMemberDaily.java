package com.jxf.report.entity;


import com.alibaba.fastjson.annotation.JSONField;
import javax.validation.constraints.NotNull;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 用户统计Entity
 * @author Administrator
 * @version 2018-09-06
 */
public class ReportMemberDaily extends CrudEntity<ReportMemberDaily> {
	
	private static final long serialVersionUID = 1L;
	/** 日期 */
	private String date;
	/** 新增会员 */
	private Long newMembers;	
	/** 新增会员 */
	private Long newIdentityMembers;	
	/** 总会员数 */
	private Long totalMembers;		
	/** 总会员数 */
	private Long totalIdentityMembers;	
	/** 活跃会员 */
	private Long activeMembers;		
	/** 报表类型 */
	private Integer type;
	
	public ReportMemberDaily() {
		super();
	}

	public ReportMemberDaily(Long id){
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
	
	@NotNull(message="新增会员不能为空")
	public Long getNewMembers() {
		return newMembers;
	}

	public void setNewMembers(Long newMembers) {
		this.newMembers = newMembers;
	}
	
	@NotNull(message="总会员数不能为空")
	public Long getTotalMembers() {
		return totalMembers;
	}

	public void setTotalMembers(Long totalMembers) {
		this.totalMembers = totalMembers;
	}
	
	@NotNull(message="活跃会员不能为空")
	public Long getActiveMembers() {
		return activeMembers;
	}

	public void setActiveMembers(Long activeMembers) {
		this.activeMembers = activeMembers;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getNewIdentityMembers() {
		return newIdentityMembers;
	}

	public void setNewIdentityMembers(Long newIdentityMembers) {
		this.newIdentityMembers = newIdentityMembers;
	}

	public Long getTotalIdentityMembers() {
		return totalIdentityMembers;
	}

	public void setTotalIdentityMembers(Long totalIdentityMembers) {
		this.totalIdentityMembers = totalIdentityMembers;
	}
	
}