package com.jxf.web.model.wyjt.app.loan;




import java.util.ArrayList;
import java.util.List;

public class RepayRecordResponseResult   {

	/** 总利息 */
	private String interest;
	/** 还款计划列表 */
	List<RepayRecord> repayRecordList = new ArrayList<RepayRecord>();//全额list长度为1，分期list长度为期数
	
	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}
	
	public List<RepayRecord> getRepayRecordList() {
		return repayRecordList;
	}

	public void setRepayRecordList(List<RepayRecord> repayRecordList) {
		this.repayRecordList = repayRecordList;
	}



	public static class RepayRecord {
		
		/** 期数序号 */
		private Integer periodsSeq;		
		
		/** 每期应还金额*/
		private String expectRepayAmt;	
		
		/** 每期应还本金 */
		private String expectRepayPrn;	
		
		/** 每期应还利息 */
		private String expectRepayInt;	
		/** 每期应还日期 */
		private String expectRepayDate;		
				
		/** 还款状态*/
		private Integer status;//0->未还;1->已还;2->逾期;

		public Integer getPeriodsSeq() {
			return periodsSeq;
		}

		public void setPeriodsSeq(Integer periodsSeq) {
			this.periodsSeq = periodsSeq;
		}

		public String getExpectRepayAmt() {
			return expectRepayAmt;
		}

		public void setExpectRepayAmt(String expectRepayAmt) {
			this.expectRepayAmt = expectRepayAmt;
		}

		public String getExpectRepayPrn() {
			return expectRepayPrn;
		}

		public void setExpectRepayPrn(String expectRepayPrn) {
			this.expectRepayPrn = expectRepayPrn;
		}

		public String getExpectRepayInt() {
			return expectRepayInt;
		}

		public void setExpectRepayInt(String expectRepayInt) {
			this.expectRepayInt = expectRepayInt;
		}

		public String getExpectRepayDate() {
			return expectRepayDate;
		}

		public void setExpectRepayDate(String expectRepayDate) {
			this.expectRepayDate = expectRepayDate;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}
		
				
	

	}
}
