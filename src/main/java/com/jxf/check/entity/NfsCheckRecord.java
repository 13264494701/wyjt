package com.jxf.check.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 审核记录Entity
 * @author suHuimin
 * @version 2019-01-26
 */
public class NfsCheckRecord extends CrudEntity<NfsCheckRecord> {
	
	private static final long serialVersionUID = 1L;
	
	/** 原业务类型 */
	public enum OrgType {

		/** 借条会员提现 */
		withdraw,

		/** 借条会员加款 */
		memberAddBal,
		
		/** 借条会员减款 */
		memberSubBal,
		
		/** 优放机构加款 */
		ufangBrnAddBal,
		
		/** 优放机构减款 */
		ufangBrnSubBal,
		
		/** 注销申请 */
		memberCancellation

	}
	/** 初审状态 */
	public enum CheckStatus {
		
		/** 自动通过 */
		pass_auto,
		
		/** 手动通过 */
		pass_manual,

		/** 已拒绝 */
		reject,
		
		/** 已取消 */
		cancel

	}
	
			/** 原业务类型 */
	private OrgType orgType;		
			/** 原业务编号 */
	private Long orgId;		
			/** 初审人编号 */
	private String checkerNo;		
			/** 初审人姓名 */
	private String checkerName;		
			/** 初审时间 */
	private Date checkTime;		
			/** 初审状态 */
	private CheckStatus checkStatus;		
			/** 初审意见 */
	private String checkDesc;		
			/** 复审人编号 */
	private String recheckerNo;		
			/** 复审人姓名 */
	private String recheckerName;		
			/** 复审时间 */
	private Date recheckTime;		
			/** 复审状态 */
	private CheckStatus recheckStatus;		
			/** 复审意见 */
	private String recheckDesc;		
			/** 查询审核起始时间 */
	private Date beginTime;		
			/** 查询审核结束时间 */
	private Date endTime;
	
	public NfsCheckRecord() {
		super();
	}

	public NfsCheckRecord(Long id){
		super(id);
	}

	public OrgType getOrgType() {
		return orgType;
	}

	public void setOrgType(OrgType orgType) {
		this.orgType = orgType;
	}
	
	@NotNull(message="原业务编号不能为空")
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
	@Length(min=0, max=6, message="初审人编号长度必须介于 0 和 6 之间")
	public String getCheckerNo() {
		return checkerNo;
	}

	public void setCheckerNo(String checkerNo) {
		this.checkerNo = checkerNo;
	}
	
	@Length(min=0, max=64, message="初审人姓名长度必须介于 0 和 64 之间")
	public String getCheckerName() {
		return checkerName;
	}

	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	
	public CheckStatus getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(CheckStatus checkStatus) {
		this.checkStatus = checkStatus;
	}
	
	@Length(min=0, max=255, message="初审意见长度必须介于 0 和 255 之间")
	public String getCheckDesc() {
		return checkDesc;
	}

	public void setCheckDesc(String checkDesc) {
		this.checkDesc = checkDesc;
	}
	
	public String getRecheckerNo() {
		return recheckerNo;
	}

	public void setRecheckerNo(String recheckerNo) {
		this.recheckerNo = recheckerNo;
	}
	
	public String getRecheckerName() {
		return recheckerName;
	}

	public void setRecheckerName(String recheckerName) {
		this.recheckerName = recheckerName;
	}
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getRecheckTime() {
		return recheckTime;
	}

	public void setRecheckTime(Date recheckTime) {
		this.recheckTime = recheckTime;
	}
	
	public CheckStatus getRecheckStatus() {
		return recheckStatus;
	}

	public void setRecheckStatus(CheckStatus recheckStatus) {
		this.recheckStatus = recheckStatus;
	}
	
	@Length(min=0, max=255, message="复审意见长度必须介于 0 和 255 之间")
	public String getRecheckDesc() {
		return recheckDesc;
	}

	public void setRecheckDesc(String recheckDesc) {
		this.recheckDesc = recheckDesc;
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