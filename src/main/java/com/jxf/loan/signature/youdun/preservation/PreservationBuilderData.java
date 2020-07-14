package com.jxf.loan.signature.youdun.preservation;

import com.jxf.loan.entity.NfsLoanArbitration;
import com.jxf.loan.entity.NfsLoanContract;
import com.jxf.loan.entity.NfsLoanOperatingRecord;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberCard;
import com.jxf.mem.entity.MemberVideoVerify;
import com.jxf.nfs.entity.NfsWdrlRecord;

public class PreservationBuilderData {
	/** 借条*/
	private NfsLoanRecord loanRecord;
	/** 合同*/
	private NfsLoanContract loanContract;
	/** 会员*/
	private Member member;
	/** 会员银行卡*/
	private MemberCard memberCard;
	/** 相应的操作记录*/
	private NfsLoanOperatingRecord operatingRecord;
	/** 对应的仲裁记录*/
	private NfsLoanArbitration loanArbitration;
	/** 借款人提现记录*/
	private NfsWdrlRecord wdrlRecord;
	/** 会员认证信息*/
	private MemberVideoVerify memberVideoVerify;
	/** 上一个合同编号*/
	private String lastContractCode;
	
	
	public NfsLoanRecord getLoanRecord() {
		return loanRecord;
	}
	public void setLoanRecord(NfsLoanRecord loanRecord) {
		this.loanRecord = loanRecord;
	}
	public NfsLoanContract getLoanContract() {
		return loanContract;
	}
	public void setLoanContract(NfsLoanContract loanContract) {
		this.loanContract = loanContract;
	}
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	public MemberCard getMemberCard() {
		return memberCard;
	}
	public void setMemberCard(MemberCard memberCard) {
		this.memberCard = memberCard;
	}
	public NfsLoanOperatingRecord getOperatingRecord() {
		return operatingRecord;
	}
	public void setOperatingRecord(NfsLoanOperatingRecord operatingRecord) {
		this.operatingRecord = operatingRecord;
	}
	public NfsLoanArbitration getLoanArbitration() {
		return loanArbitration;
	}
	public void setLoanArbitration(NfsLoanArbitration loanArbitration) {
		this.loanArbitration = loanArbitration;
	}
	public NfsWdrlRecord getWdrlRecord() {
		return wdrlRecord;
	}
	public void setWdrlRecord(NfsWdrlRecord wdrlRecord) {
		this.wdrlRecord = wdrlRecord;
	}
	public MemberVideoVerify getMemberVideoVerify() {
		return memberVideoVerify;
	}
	public void setMemberVideoVerify(MemberVideoVerify memberVideoVerify) {
		this.memberVideoVerify = memberVideoVerify;
	}
	public String getLastContractCode() {
		return lastContractCode;
	}
	public void setLastContractCode(String lastContractCode) {
		this.lastContractCode = lastContractCode;
	}
	
}
