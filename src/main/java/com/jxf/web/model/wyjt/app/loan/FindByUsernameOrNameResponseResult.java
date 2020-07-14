package com.jxf.web.model.wyjt.app.loan;

import java.util.ArrayList;
import java.util.List;

import com.jxf.web.model.wyjt.app.loan.LoanListForAppResponseResult.LoanForApp;

/**
 * @作者: xiaorongdian
 * @创建时间 :2018年12月5日 下午3:46:35
 * @功能说明:根据姓名或电话查找借条
 */
public class FindByUsernameOrNameResponseResult {

	/**
	 * 谁欠我钱 条数
	 */
	private Integer whoOweMeNum = 0;
	/**
	 * 我欠谁钱 条数
	 */
	private Integer meOweWhoNum = 0;
	/**
	 * 谁欠我钱数据
	 */
	private List<LoanForApp> whoOweMeList = new ArrayList<LoanForApp>();
	/**
	 * 我欠谁钱数据
	 */
	private List<LoanForApp> meOweWhoList = new ArrayList<LoanForApp>();
	
	public Integer getWhoOweMeNum() {
		return whoOweMeNum;
	}
	public void setWhoOweMeNum(Integer whoOweMeNum) {
		this.whoOweMeNum = whoOweMeNum;
	}
	public Integer getMeOweWhoNum() {
		return meOweWhoNum;
	}
	public void setMeOweWhoNum(Integer meOweWhoNum) {
		this.meOweWhoNum = meOweWhoNum;
	}
	public List<LoanForApp> getWhoOweMeList() {
		return whoOweMeList;
	}
	public void setWhoOweMeList(List<LoanForApp> whoOweMeList) {
		this.whoOweMeList = whoOweMeList;
	}
	public List<LoanForApp> getMeOweWhoList() {
		return meOweWhoList;
	}
	public void setMeOweWhoList(List<LoanForApp> meOweWhoList) {
		this.meOweWhoList = meOweWhoList;
	}
}
