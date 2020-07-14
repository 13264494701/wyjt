package com.jxf.web.model.wyjt.app.loan;

/***
 *   根据借条ID查询借款/放款的详情
 * @author wo
 *
 */
public class LoanDetailRequestParam {

	
	/** 申请ID */
	private String loanId;

	/** 借贷角色 0借款人 1放款人 */
	private Integer loanRole;  
	
	/** 借条类型 0单人 1多人*/
	private Integer loanType;
	
	/** 借条申请状态 0待确认/待放款 1待还/待收 2逾期 3已还  4已失效(查多人时 随便赋个值) */
	private Integer status;

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public Integer getLoanRole() {
		return loanRole;
	}

	public void setLoanRole(Integer loanRole) {
		this.loanRole = loanRole;
	}

	public Integer getLoanType() {
		return loanType;
	}

	public void setLoanType(Integer loanType) {
		this.loanType = loanType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
