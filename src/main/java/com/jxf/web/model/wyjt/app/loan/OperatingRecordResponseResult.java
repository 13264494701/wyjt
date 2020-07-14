package com.jxf.web.model.wyjt.app.loan;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者: xiaorongdian
 * @创建时间 :2018年12月17日 下午1:37:10
 * @功能说明: 借条历史记录
 */
public class OperatingRecordResponseResult {

	/** 借条历史记录*/
	private List<OperatingRecord> operatingRecordRecords = new ArrayList<OperatingRecord>();
	
	public class OperatingRecord {	
		/** 记录Id*/
		private String operatingRecordId;
		/** 名称 */
		private String name;
		/** 时间 */
		private String time;
		/** 变化 */
		private String change;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getChange() {
			return change;
		}
		public void setChange(String change) {
			this.change = change;
		}
		public String getOperatingRecordId() {
			return operatingRecordId;
		}
		public void setOperatingRecordId(String operatingRecordId) {
			this.operatingRecordId = operatingRecordId;
		}
	}

	public List<OperatingRecord> getOperatingRecordRecords() {
		return operatingRecordRecords;
	}

	public void setOperatingRecordRecords(
			List<OperatingRecord> operatingRecordRecords) {
		this.operatingRecordRecords = operatingRecordRecords;
	}

}
