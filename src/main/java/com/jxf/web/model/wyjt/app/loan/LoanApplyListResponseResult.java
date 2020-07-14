package com.jxf.web.model.wyjt.app.loan;

import java.util.ArrayList;
import java.util.List;


/**
 * @作者: xiaorongdian
 * @创建时间 :2018年11月26日 上午11:44:48
 * @功能说明:申请列表返回
 */
public class LoanApplyListResponseResult {
	/** 借条申请List */
	List<Apply> applyList = new ArrayList<Apply>();
	
	public List<Apply> getApplyList() {
		return applyList;
	}
	public void setApplyList(List<Apply> applyList) {
		this.applyList = applyList;
	}
	
	public static class Apply {
	
		/** 借条申请ID */
		private String applyId;	
		
		/** 姓名 */
		private String partnerName;		
		/** 头像 */
		private String partnerHeadImage;	
		
		/** 还款方式 */
		private Integer repayType;	
		
		/** 借款金额 */
		private String amount;	
		
		/** 利息 */
		private String interest;
		
		/** 还款时间 */
		private String repayDate;		
		
		/** 借条进度*/
		private String progress;
		
		public String getApplyId() {
			return applyId;
		}

		public void setApplyId(String applyId) {
			this.applyId = applyId;
		}

		public String getPartnerName() {
			return partnerName;
		}

		public void setPartnerName(String partnerName) {
			this.partnerName = partnerName;
		}

		public String getPartnerHeadImage() {
			return partnerHeadImage;
		}

		public void setPartnerHeadImage(String partnerHeadImage) {
			this.partnerHeadImage = partnerHeadImage;
		}

		public Integer getRepayType() {
			return repayType;
		}

		public void setRepayType(Integer repayType) {
			this.repayType = repayType;
		}

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

		public String getInterest() {
			return interest;
		}

		public void setInterest(String interest) {
			this.interest = interest;
		}

		public String getRepayDate() {
			return repayDate;
		}

		public void setRepayDate(String repayDate) {
			this.repayDate = repayDate;
		}

		public String getProgress() {
			return progress;
		}

		public void setProgress(String progress) {
			this.progress = progress;
		}

	}
}
