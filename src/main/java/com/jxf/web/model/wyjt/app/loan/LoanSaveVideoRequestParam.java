package com.jxf.web.model.wyjt.app.loan;

/**
 * 保存借款上传的视频地址
 * @author Administrator
 *
 */
public class LoanSaveVideoRequestParam {
	/**
	 * 视频地址
	 */
	private String videoUrl;
	
	/**
	 * 借款单id
	 */
	private String loanId;

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}
	
	

}
