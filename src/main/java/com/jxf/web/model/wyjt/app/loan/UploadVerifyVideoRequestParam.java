package com.jxf.web.model.wyjt.app.loan;
/**
 * @作者: xiaorongdian
 * @创建时间 :2018年12月24日 上午10:30:00
 * @功能说明:保存审核视频路径
 */
public class UploadVerifyVideoRequestParam {

	/** 视频路径 */
	private String verifyVideoUrl;
	
	/** 借条Id (后台的detailId) */
	private String loanId;
	
	public String getVerifyVideoUrl() {
		return verifyVideoUrl;
	}
	public void setVerifyVideoUrl(String verifyVideoUrl) {
		this.verifyVideoUrl = verifyVideoUrl;
	}
	public String getLoanId() {
		return loanId;
	}
	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}
}
