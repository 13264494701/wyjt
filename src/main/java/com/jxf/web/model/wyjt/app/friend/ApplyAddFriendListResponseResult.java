package com.jxf.web.model.wyjt.app.friend;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者: xiaorongdian
 * @创建时间 :2018年10月30日 下午8:33:06
 * @功能说明:
 */
public class ApplyAddFriendListResponseResult {


	/** 申请列表 */
	private List<Apply>  applyList = new ArrayList<Apply>();	

	public List<Apply> getApplyList() {
		return applyList;
	}

	public void setApplyList(List<Apply> applyList) {
		this.applyList = applyList;
	}

	public class Apply {
		
		/** 申请ID */
		private String applyId;
		/** 电话 */
		private String phoneNo;
		/** 姓名 */
		private String name;
		/** 头像 */
		private String headImage;
		/** 验证消息*/
		private String note;
		/** 状态 0待同意 1已同意 */
		private Integer status;
		
		public String getApplyId() {
			return applyId;
		}
		public void setApplyId(String applyId) {
			this.applyId = applyId;
		}
		public String getPhoneNo() {
			return phoneNo;
		}
		public void setPhoneNo(String phoneNo) {
			this.phoneNo = phoneNo;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getHeadImage() {
			return headImage;
		}
		public void setHeadImage(String headImage) {
			this.headImage = headImage;
		}


		public Integer getStatus() {
			return status;
		}
		public void setStatus(Integer status) {
			this.status = status;
		}
		public String getNote() {
			return note;
		}
		public void setNote(String note) {
			this.note = note;
		}



		
	}
}
