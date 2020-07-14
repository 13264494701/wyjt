package com.jxf.web.model.wyjt.app.loan;


public class LoanApplyPayRequestParam   {
	
	/** 申请ID */
	private String applyDetailId;		
	
	/** 支付密码 */
	private String payPassword;		
	
	/** 是否要求录制视频 */
	private Integer requireAliveVideo;//0->否，1->是		
	
	/** 争议解决方式 */
	private Integer disputeResolution;//0->仲裁,1->诉讼
	
	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public Integer getRequireAliveVideo() {
		return requireAliveVideo;
	}

	public void setRequireAliveVideo(Integer requireAliveVideo) {
		this.requireAliveVideo = requireAliveVideo;
	}

	public Integer getDisputeResolution() {
		return disputeResolution;
	}

	public void setDisputeResolution(Integer disputeResolution) {
		this.disputeResolution = disputeResolution;
	}

	public String getApplyDetailId() {
		return applyDetailId;
	}

	public void setApplyDetailId(String applyDetailId) {
		this.applyDetailId = applyDetailId;
	}
	
	
}
