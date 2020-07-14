package com.jxf.web.model.wyjt.app.loan;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者: xiaorongdian
 * @创建时间 :2018年12月17日 下午3:58:58
 * @功能说明:查看借条历史记录详情
 */
public class OperatingRecordDetailResponseResult {

	/** 题目 */
	private String title;
	/** 变化 */
	private String change;
	/** 发起方 */
	private String applyMemberName;
	/** 交易时间 */
	private String time;
	
	/** 操作详情 可能为[]*/
	private List<OperatingDetail> detailList = new ArrayList<OperatingDetail>();
	
	/** 更新后借条  */
	private List<OperatingDetail> nowRecord = new ArrayList<OperatingDetail>();
	
	
	public class OperatingDetail{
		
		/** 左边名称 */
		private String name;
		
		/** 右边值 */
		private String value;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getChange() {
		return change;
	}


	public void setChange(String change) {
		this.change = change;
	}

	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}

	public String getApplyMemberName() {
		return applyMemberName;
	}


	public void setApplyMemberName(String applyMemberName) {
		this.applyMemberName = applyMemberName;
	}


	public List<OperatingDetail> getDetailList() {
		return detailList;
	}


	public void setDetailList(List<OperatingDetail> detailList) {
		this.detailList = detailList;
	}


	public List<OperatingDetail> getNowRecord() {
		return nowRecord;
	}


	public void setNowRecord(List<OperatingDetail> nowRecord) {
		this.nowRecord = nowRecord;
	}
}
